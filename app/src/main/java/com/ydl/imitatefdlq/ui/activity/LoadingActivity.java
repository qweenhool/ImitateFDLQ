package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.util.StatusBarCompat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingActivity extends AppCompatActivity{

    private static final int UPDATE_PROGRESS = 1;
    private static final int UPDATE_COMPLETE = 2;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.text_view)
    TextView textView;

    private Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_PROGRESS:
                    progressBar.incrementProgressBy(1);
                    if (progressBar.getProgress() == 100) {
                        handler.sendEmptyMessage(UPDATE_COMPLETE);
                    } else {
                        handler.sendEmptyMessage(UPDATE_PROGRESS);
                    }
                    break;
                case UPDATE_COMPLETE:
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusBar));

        new Thread(new Runnable() {
            @Override
            public void run() {
                //子线程模拟同步操作
                SystemClock.sleep(1000);
                handler.sendEmptyMessage(UPDATE_PROGRESS);
            }
        }).start();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        //防止点击返回按钮退出程序
    }
}

package com.ydl.imitatefdlq.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.util.EditTextUtils;
import com.ydl.imitatefdlq.util.StatusBarCompat;

public class SearchActivity extends AppCompatActivity {

    private EditText search;
    private TextView cancel;
    private ImageView clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusBar));
        iniView();

    }

    private void iniView() {
        clear = (ImageView) findViewById(R.id.iv_clear);
        search = (EditText) findViewById(R.id.et_search);
        //设置搜索框自动获得焦点并弹出软键盘
        search.setFocusable(true);
        search.setFocusableInTouchMode(true);
        search.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        cancel = (TextView) findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        EditTextUtils.clearButtonListener(search,clear);
    }
}

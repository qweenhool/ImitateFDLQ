package com.ydl.imitatefdlq.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.util.ActivityCollector;
import com.ydl.imitatefdlq.util.StatusBarCompat;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.ydl.imitatefdlq.R.id.action_base;

/**
 * Created by qweenhool on 2017/8/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;
    protected Toolbar toolbar;
    private FrameLayout container;
    private TextView textView;
    // icon图标id
    private int menuIconId;
    private String menuStr;

    OnClickListener onClickListenerTopLeft;
    OnClickListener onClickListenerTopRight;

    public interface OnClickListener {
        void onClick();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //设置status bar的颜色为黑色
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusBar));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        container = (FrameLayout) findViewById(R.id.fl_container);
        textView = (TextView) findViewById(R.id.tv_toolbar_name);

        //初始化设置 Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //将继承 getContentView() 得到的布局解析到 FrameLayout 里面
        LayoutInflater.from(BaseActivity.this).inflate(getContentView(), container);
        ActivityCollector.addActivity(this);
        //这句话要放到这里才不会出错
        unbinder = ButterKnife.bind(this);
        init(savedInstanceState);
    }

    protected abstract int getContentView();

    protected abstract void init(Bundle savedInstanceState);

    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            textView.setText(title);
        }
    }

    protected void setTopLeftButton(int iconResId, OnClickListener onClickListener) {
        toolbar.setNavigationIcon(iconResId);
        this.onClickListenerTopLeft = onClickListener;
    }

    protected void setTopRightButton(String menuStr, int menuIconId, OnClickListener onClickListener) {
        this.menuStr = menuStr;
        this.menuIconId = menuIconId;
        this.onClickListenerTopRight = onClickListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuIconId != 0 || !TextUtils.isEmpty(menuStr)) {
            getMenuInflater().inflate(R.menu.menu_base, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuIconId != 0) {
            menu.findItem(action_base).setIcon(menuIconId);
        }
        if (!TextUtils.isEmpty(menuStr)) {
            menu.findItem(action_base).setTitle(menuStr);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onClickListenerTopLeft.onClick();
        }
        if(item.getItemId() == R.id.action_base){
            onClickListenerTopRight.onClick();
        }
        return true; // true 告诉系统我们自己处理了点击事件
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        ActivityCollector.removeActivity(this);
    }


}

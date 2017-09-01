package com.ydl.imitatefdlq.ui.activity;

import android.os.Bundle;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.ui.base.BaseActivity;

import butterknife.ButterKnife;

public class RenterActivity extends BaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_renter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        setTitle("租客");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}

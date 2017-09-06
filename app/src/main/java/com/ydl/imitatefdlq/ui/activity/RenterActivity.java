package com.ydl.imitatefdlq.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenterActivity extends BaseActivity {

    @BindView(R.id.tv_room_name)
    TextView tvRoomName;
    @BindView(R.id.tv_house_name)
    TextView tvHouseName;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.ll_add_room_number)
    LinearLayout llAddRoomNumber;
    @BindView(R.id.rv_renter)
    RecyclerView rvRenter;
    @BindView(R.id.refresh_layout_renter)
    SmartRefreshLayout refreshLayoutRenter;
    @BindView(R.id.ll_hint)
    LinearLayout llHint;

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

    @OnClick({R.id.iv_edit, R.id.ll_add_room_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit:
                break;
            case R.id.ll_add_room_number:
                break;
        }
    }
}

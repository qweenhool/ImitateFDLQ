package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoActivity extends BaseActivity {

    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.riv_head_portrait)
    RoundImageView rivHeadPortrait;
    @BindView(R.id.rl_head_portrait)
    RelativeLayout rlHeadPortrait;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ll_online_collect_rents)
    LinearLayout llOnlineCollectRents;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.ll_collect_account)
    LinearLayout llCollectAccount;
    @BindView(R.id.tv_photo_number)
    TextView tvPhotoNumber;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.ll_exit)
    LinearLayout llExit;

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {


        setTitle("个人信息");

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

    @OnClick({R.id.rl_head_portrait, R.id.ll_online_collect_rents, R.id.ll_collect_account, R.id.ll_location, R.id.ll_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_head_portrait:
                break;
            case R.id.ll_online_collect_rents:
                break;
            case R.id.ll_collect_account:
                break;
            case R.id.ll_location:
                break;
            case R.id.ll_exit:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}

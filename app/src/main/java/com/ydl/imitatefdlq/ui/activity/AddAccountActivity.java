package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAccountActivity extends BaseActivity {

    @BindView(R.id.ll_bank_account)
    LinearLayout llBankAccount;
    @BindView(R.id.rl_wechat)
    RelativeLayout rlWechat;
    @BindView(R.id.rl_alipay)
    RelativeLayout rlAlipay;

    @BindView(R.id.et_cardholder_name)
    EditText etCardholderName;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.et_deposit_bank)
    EditText etDepositBank;
    @BindView(R.id.et_deposit_branch)
    EditText etDepositBranch;

    @BindView(R.id.et_wechat)
    EditText etWechat;
    @BindView(R.id.tv_wechat_hint)
    TextView tvWechatHint;

    @BindView(R.id.et_alipay)
    EditText etAlipay;
    @BindView(R.id.tv_Alipay_hint)
    TextView tvAlipayHint;

    @BindView(R.id.et_remark)
    EditText etRemark;

    @BindView(R.id.iv_scan)
    ImageView ivScan;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_account;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initView();

        setTitle("添加收款账户");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton("保存", 0, new OnClickListener() {
            @Override
            public void onClick() {

            }
        });
    }

    private void initView() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        switch (type) {//从JDK7开始加入支持String类型
            case "bank_card":
                llBankAccount.setVisibility(View.VISIBLE);
                rlWechat.setVisibility(View.GONE);
                rlAlipay.setVisibility(View.GONE);
                break;
            case "wechat":
                llBankAccount.setVisibility(View.GONE);
                rlWechat.setVisibility(View.VISIBLE);
                rlAlipay.setVisibility(View.GONE);
                break;
            case "alipay":
                llBankAccount.setVisibility(View.GONE);
                rlWechat.setVisibility(View.GONE);
                rlAlipay.setVisibility(View.VISIBLE);
                break;
            case "other":
                llBankAccount.setVisibility(View.GONE);
                rlWechat.setVisibility(View.GONE);
                rlAlipay.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_scan)
    public void onViewClicked() {
        //TODO 打开摄像头扫描银行卡
    }
}

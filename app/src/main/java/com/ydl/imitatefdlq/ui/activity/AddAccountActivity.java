package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.PayeeAccountBean;
import com.ydl.imitatefdlq.entity.PayeeAccountBeanDao;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.widget.XEditText;

import java.util.Date;
import java.util.UUID;

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

    @BindView(R.id.et_cardholder)
    EditText etCardholder;
    @BindView(R.id.et_card_number)
    XEditText etCardNumber;
    @BindView(R.id.et_deposit_bank)
    EditText etDepositBank;
    @BindView(R.id.et_deposit_branch)
    EditText etDepositBranch;

    @BindView(R.id.et_wechat)
    EditText etWechat;
    @BindView(R.id.et_alipay)
    EditText etAlipay;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.iv_scan)
    ImageView ivScan;

    private String type;
    private PayeeAccountBeanDao payeeAccountBeanDao;
    private PayeeAccountBean payeeAccountBean;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_account;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initView();

        initData();

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
                String uuid = UUID.randomUUID().toString();
                //返回最新的一条数据给上一个activity
                Intent intent = new Intent();
                intent.putExtra("uuid", uuid);

                switch (type) {//从JDK7开始加入支持String类型
                    case "bank_card":
                        payeeAccountBean.setId(uuid);
                        payeeAccountBean.setAccountName("银行卡");
                        payeeAccountBean.setAccountType(1);
                        payeeAccountBean.setOrderNumber(new Date());
                        if (!TextUtils.isEmpty(etCardholder.getText().toString().trim())) {
                            payeeAccountBean.setCardHolder(etCardholder.getText().toString().trim());
                        } else {
                            Toast.makeText(AddAccountActivity.this, "持卡人不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!TextUtils.isEmpty(etCardNumber.getText().toString().trim())) {
                            payeeAccountBean.setCardNumber(etCardNumber.getTrimmedString());
                        } else {
                            Toast.makeText(AddAccountActivity.this, "卡号不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!TextUtils.isEmpty(etDepositBank.getText().toString().trim())) {
                            payeeAccountBean.setDepositBank(etDepositBank.getText().toString());
                        } else {
                            Toast.makeText(AddAccountActivity.this, "开户行不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!TextUtils.isEmpty(etDepositBranch.getText().toString().trim())) {
                            payeeAccountBean.setDepositBranch(etDepositBranch.getText().toString());
                        }
                        if (!TextUtils.isEmpty(etRemark.getText().toString())) {
                            payeeAccountBean.setRemark(etRemark.getText().toString());
                        }
                        payeeAccountBeanDao.insert(payeeAccountBean);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case "wechat":
                        payeeAccountBean.setId(uuid);
                        payeeAccountBean.setAccountName("微信");
                        payeeAccountBean.setAccountType(2);
                        payeeAccountBean.setOrderNumber(new Date());
                        if (!TextUtils.isEmpty(etWechat.getText().toString().trim())) {
                            payeeAccountBean.setWechatAccount(etWechat.getText().toString());
                        } else {
                            Toast.makeText(AddAccountActivity.this, "微信号不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!TextUtils.isEmpty(etRemark.getText().toString())) {
                            payeeAccountBean.setRemark(etRemark.getText().toString());
                        }
                        payeeAccountBeanDao.insert(payeeAccountBean);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case "alipay":
                        payeeAccountBean.setId(uuid);
                        payeeAccountBean.setAccountName("支付宝");
                        payeeAccountBean.setAccountType(3);
                        payeeAccountBean.setOrderNumber(new Date());
                        if (!TextUtils.isEmpty(etAlipay.getText().toString().trim())) {
                            payeeAccountBean.setAlipayAccount(etAlipay.getText().toString());
                        } else {
                            Toast.makeText(AddAccountActivity.this, "支付宝号不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!TextUtils.isEmpty(etRemark.getText().toString())) {
                            payeeAccountBean.setRemark(etRemark.getText().toString());
                        }
                        payeeAccountBeanDao.insert(payeeAccountBean);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case "other":
                        payeeAccountBean.setId(uuid);
                        payeeAccountBean.setAccountName("其他账户");
                        payeeAccountBean.setAccountType(4);
                        payeeAccountBean.setOrderNumber(new Date());
                        if (!TextUtils.isEmpty(etRemark.getText().toString().trim())) {
                            payeeAccountBean.setRemark(etRemark.getText().toString());
                        } else {
                            Toast.makeText(AddAccountActivity.this, "备注不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        payeeAccountBeanDao.insert(payeeAccountBean);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initData() {
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        payeeAccountBeanDao = daoSession.getPayeeAccountBeanDao();
        payeeAccountBean = new PayeeAccountBean();
    }

    private void initView() {
        etCardNumber.setPattern(new int[]{4, 4, 4, 4, 4, 4, 3}, " ");

        type = getIntent().getStringExtra("type");
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

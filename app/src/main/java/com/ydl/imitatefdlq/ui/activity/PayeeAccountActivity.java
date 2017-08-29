package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.util.StatusBarCompat;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayeeAccountActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.tb_add_account)
    Toolbar tbAddAccount;
    @BindView(R.id.ll_add_account)
    LinearLayout llAddAccount;
    @BindView(R.id.tv_no_account)
    TextView tvNoAccount;
    @BindView(R.id.rv_bank_account)
    SwipeMenuRecyclerView rvBankAccount;
    @BindView(R.id.refresh_layout_bank_account)
    SmartRefreshLayout refreshLayoutBankAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payee_account);
        ButterKnife.bind(this);

        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusbar));

        initToolbar();

        initData();
    }

    private void initData() {

    }

    private void initToolbar() {
        setSupportActionBar(tbAddAccount);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        tvToolbarName.setText("收款账户");
        tbAddAccount.setNavigationIcon(R.drawable.back);
        tbAddAccount.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.ll_add_account)
    public void onViewClicked() {
        String[] accountTypeArr = new String[]{"银行卡", "微信", "支付宝", "其他"};
        StyledDialog.buildIosSingleChoose(Arrays.asList(accountTypeArr), new MyItemDialogListener() {
            @Override
            public void onItemClick(CharSequence charSequence, int i) {
                Intent bankCardIntent = new Intent(PayeeAccountActivity.this, AddAccountActivity.class);
                if ("银行卡".equals(charSequence)) {
                    bankCardIntent.putExtra("type", "bank_card");
                    startActivity(bankCardIntent);
                } else if ("微信".equals(charSequence)) {
                    bankCardIntent.putExtra("type", "wechat");
                    startActivity(bankCardIntent);
                } else if ("支付宝".equals(charSequence)) {
                    bankCardIntent.putExtra("type", "alipay");
                    startActivity(bankCardIntent);
                } else if ("其他".equals(charSequence)) {
                    bankCardIntent.putExtra("type", "other");
                    startActivity(bankCardIntent);
                }
            }
        })
                .setCancelable(true, true)
                .show();
    }

}

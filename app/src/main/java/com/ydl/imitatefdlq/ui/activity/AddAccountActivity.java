package com.ydl.imitatefdlq.ui.activity;

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

import com.githang.statusbar.StatusBarCompat;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.ydl.imitatefdlq.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAccountActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.tb_add_account)
    Toolbar tbAddAccount;
    @BindView(R.id.ll_add_account)
    LinearLayout llAddAccount;
    @BindView(R.id.tv_no_account)
    TextView tvNoAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        ButterKnife.bind(this);

        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorStatusbar), false);
        initToolbar();
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
                if ("银行卡".equals(charSequence)) {

                } else if ("微信".equals(charSequence)) {

                } else if ("支付宝".equals(charSequence)) {

                } else if ("其他".equals(charSequence)) {

                }
            }
        })
                .setCancelable(true, true)
                .show();
    }
}

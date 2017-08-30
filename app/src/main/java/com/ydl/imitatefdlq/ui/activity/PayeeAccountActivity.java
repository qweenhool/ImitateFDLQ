package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.PayeeAccountAdapter;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.PayeeAccountBean;
import com.ydl.imitatefdlq.entity.PayeeAccountBeanDao;
import com.ydl.imitatefdlq.util.StatusBarCompat;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

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

    private PayeeAccountBeanDao payeeAccountBeanDao;

    private List<PayeeAccountBean> payeeAccountBeanList;
    private PayeeAccountAdapter adapter;

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
        rvBankAccount.setLayoutManager(new LinearLayoutManager(this));
        //要在这里添加分割线，不然在onResume中会不停添加
        rvBankAccount.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#efeff4"),MATCH_PARENT,30));

        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        payeeAccountBeanDao = daoSession.getPayeeAccountBeanDao();

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
    protected void onResume() {
        super.onResume();

        payeeAccountBeanList = payeeAccountBeanDao.queryBuilder()
                .orderDesc(PayeeAccountBeanDao.Properties.OrderNumber)
                .list();

        if (payeeAccountBeanList.size() != 0) {
            adapter = new PayeeAccountAdapter(this, payeeAccountBeanList);
            rvBankAccount.setAdapter(adapter);


            refreshLayoutBankAccount.setVisibility(View.VISIBLE);
            tvNoAccount.setVisibility(View.GONE);
        } else {
            refreshLayoutBankAccount.setVisibility(View.GONE);
            tvNoAccount.setVisibility(View.VISIBLE);
        }

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

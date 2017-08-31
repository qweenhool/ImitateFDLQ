package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.PayeeAccountAdapter;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.PayeeAccountBean;
import com.ydl.imitatefdlq.entity.PayeeAccountBeanDao;
import com.ydl.imitatefdlq.util.StatusBarCompat;

import java.util.ArrayList;
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

        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        payeeAccountBeanDao = daoSession.getPayeeAccountBeanDao();

        View footer = LayoutInflater.from(this).inflate(R.layout.item_payee_account_footer, null);
        rvBankAccount.addFooterView(footer);
        rvBankAccount.setLayoutManager(new LinearLayoutManager(this));
        //要在这里添加分割线，不然在onResume中会不停添加
        rvBankAccount.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#efeff4"), MATCH_PARENT, 30));

        payeeAccountBeanList = new ArrayList<>();
        List<PayeeAccountBean> list = payeeAccountBeanDao.queryBuilder()
                .orderDesc(PayeeAccountBeanDao.Properties.OrderNumber)
                .list();
        payeeAccountBeanList.addAll(list);

        adapter = new PayeeAccountAdapter(this, payeeAccountBeanList);
        rvBankAccount.setAdapter(adapter);


        //设置item的点击事件
        rvBankAccount.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                adapter.setSelectItem(position);
            }
        });

        // 设置修改和删除菜单。
        rvBankAccount.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(PayeeAccountActivity.this);
                SwipeMenuItem modifyItem = new SwipeMenuItem(PayeeAccountActivity.this);
                modifyItem.setText("修改")
                        .setHeight(MATCH_PARENT)
                        .setWidth(250)
                        .setTextSize(16)
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.parseColor("#5687e7"));
                deleteItem.setText("删除")
                        .setHeight(MATCH_PARENT)
                        .setWidth(250)
                        .setTextSize(16)
                        .setTextColor(Color.WHITE)
                        .setBackgroundColor(Color.parseColor("#FE3B30"));
                rightMenu.addMenuItem(modifyItem); // 在Item右侧添加一个菜单。
                rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。

            }
        });
        //设置修改、删除菜单监听器
        rvBankAccount.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(final SwipeMenuBridge menuBridge) {
                switch (menuBridge.getPosition()) {
                    case 0:
                        menuBridge.closeMenu();
                        Intent intent = new Intent(PayeeAccountActivity.this, ModifyAccountActivity.class);
                        startActivity(intent);

                        break;
                    case 1:
                        StyledDialog.buildIosAlert("删除确认",
                                "您确定要删除这个收款账户吗？",
                                new MyDialogListener() {
                                    @Override
                                    public void onFirst() {
                                        //Todo 联网删除服务器数据

                                        //删除payeeAccount表中这一行数据
                                        int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                                        payeeAccountBeanDao.deleteByKey(payeeAccountBeanList.get(adapterPosition).getId());
                                        //TODO 删除room表中跟payeeAccount相关的收款账户

                                        //更新传入adapter的数据集
                                        payeeAccountBeanList.remove(adapterPosition);
                                        //houseBeanList里面没有数据就显示初始界面
                                        if (payeeAccountBeanList.size() == 0) {
                                            refreshLayoutBankAccount.setVisibility(View.GONE);
                                            tvNoAccount.setVisibility(View.VISIBLE);
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onSecond() {

                                    }
                                }).show();
                        menuBridge.closeMenu();
                        break;
                }
            }
        });


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

        if (payeeAccountBeanList.size() != 0) {
            refreshLayoutBankAccount.setVisibility(View.VISIBLE);
            tvNoAccount.setVisibility(View.GONE);
        } else {
            refreshLayoutBankAccount.setVisibility(View.GONE);
            tvNoAccount.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
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
                    startActivityForResult(bankCardIntent, 1);
                } else if ("微信".equals(charSequence)) {
                    bankCardIntent.putExtra("type", "wechat");
                    startActivityForResult(bankCardIntent, 1);
                } else if ("支付宝".equals(charSequence)) {
                    bankCardIntent.putExtra("type", "alipay");
                    startActivityForResult(bankCardIntent, 1);
                } else if ("其他".equals(charSequence)) {
                    bankCardIntent.putExtra("type", "other");
                    startActivityForResult(bankCardIntent, 1);
                }
            }
        })
                .setCancelable(true, true)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String uuid = data.getStringExtra("uuid");
                    List<PayeeAccountBean> list = payeeAccountBeanDao.queryBuilder()
                            .where(PayeeAccountBeanDao.Properties.Id.eq(uuid))
                            .list();
                    payeeAccountBeanList.add(0, list.get(0));
                }
                break;
            default:
        }
    }
}

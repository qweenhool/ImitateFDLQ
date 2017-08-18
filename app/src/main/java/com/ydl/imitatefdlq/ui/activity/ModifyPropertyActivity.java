package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.db.HouseDBHelper;
import com.ydl.imitatefdlq.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPropertyActivity extends AppCompatActivity {

    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_house_name)
    EditText etHouseName;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.tv_house_type)
    TextView tvHouseType;
    @BindView(R.id.ll_house_type)
    LinearLayout llHouseType;
    @BindView(R.id.iv_house_photo)
    RoundImageView ivHousePhoto;
    @BindView(R.id.ll_house_photo)
    LinearLayout llHousePhoto;
    @BindView(R.id.tv_account_info)
    TextView tvAccountInfo;
    @BindView(R.id.ll_receive_account)
    LinearLayout llReceiveAccount;
    @BindView(R.id.ll_block)
    LinearLayout llBlock;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_property);
        ButterKnife.bind(this);
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorStatusbar), false);

        initToolbar();

        initData();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        tvToolbarName.setText("修改房产");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData() {
        helper = new HouseDBHelper(this, "House.db", null, 1);
        db = helper.getWritableDatabase();

        Intent intent = getIntent();
        etHouseName.setText(intent.getStringExtra("NAME"));
        tvHouseType.setText(intent.getStringExtra("TYPE"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (TextUtils.isEmpty(etHouseName.getText().toString().trim())) {
                StyledDialog.buildIosAlert("提醒", "房产名不能为空", new MyDialogListener() {
                    @Override
                    public void onFirst() {

                    }

                    @Override
                    public void onSecond() {

                    }
                }).show();
            }else {
                //do something


            }
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.ll_house_type, R.id.ll_house_photo, R.id.ll_receive_account, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_house_type:
                break;
            case R.id.ll_house_photo:
                break;
            case R.id.ll_receive_account:
                break;
            case R.id.tv_delete:
                StyledDialog.buildIosAlert("删除确认", "删除房产将一并删除其所有的房号，租客及账单，您确定要删除吗？", new MyDialogListener() {
                    @Override
                    public void onFirst() {
                        Toast.makeText(ModifyPropertyActivity.this, "确认", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSecond() {
                        Toast.makeText(ModifyPropertyActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                break;
        }
    }

}

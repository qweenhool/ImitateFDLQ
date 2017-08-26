package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.graphics.Color;
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

import com.bigkoo.pickerview.OptionsPickerView;
import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.HouseBean;
import com.ydl.imitatefdlq.entity.HouseBeanDao;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.util.EditTextUtils;
import com.ydl.imitatefdlq.util.StatusBarCompat;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.util.Arrays;
import java.util.List;

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

    private String[] houseTypeArr;
    private OptionsPickerView opvHouseType;

    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private PictureBeanDao pictureBeanDao;

    private List<HouseBean> houseBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_property);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorStatusbar));
        EditTextUtils.clearButtonListener(etHouseName, ivClear);

        initToolbar();

        initData();

        initOptionPicker();
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
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        houseBeanDao = daoSession.getHouseBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();

        Intent intent = getIntent();
        String houseId = intent.getStringExtra("house_id");
        houseBeanList = houseBeanDao.queryBuilder()
                .where(HouseBeanDao.Properties.Id.eq(houseId))
                .list();
        //设置房产名称
        etHouseName.setText(houseBeanList.get(0).getHouseName());
        //设置房产类型
        tvHouseType.setText(houseBeanList.get(0).getHouseType());
        //设置EditText光标在最后
        etHouseName.requestFocus();
        etHouseName.setSelection(houseBeanList.get(0).getHouseName().length());
        //Todo 获取房产照片

        //Todo 获取收款账户
    }

    private void initOptionPicker() {
        houseTypeArr = new String[]{"住宅/小区/公寓", "商铺/门市房", "厂房/车间", "仓库/车库/停车位", "写字楼/办公室"};
        //开源库地址：https://github.com/Bigkoo/Android-PickerView
        opvHouseType = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tvHouseType.setText(houseTypeArr[options1]);
            }
        })
                .setTextColorCenter(Color.parseColor("#5287E7"))//滚轮文字颜色设置
                .setLineSpacingMultiplier(1.8f)//滚轮间距设置（1.2-2.0倍，此为文字高度的间距倍数）
                .build();
        opvHouseType.setPicker(Arrays.asList(houseTypeArr));
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
            } else {
                //Todo 点击保存，同步服务器

                //点击保存，同步数据库
                HouseBean houseBean = houseBeanList.get(0);
                houseBean.setHouseName(etHouseName.getText().toString());
                houseBean.setHouseType(tvHouseType.getText().toString());
                houseBeanDao.update(houseBean);
                finish();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ll_house_type, R.id.ll_house_photo, R.id.ll_receive_account, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_house_type:
                //点击后关闭软键盘
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                opvHouseType.show();
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

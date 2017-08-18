package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.db.HouseDBHelper;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.widget.RoundImageView;

/**
 * Created by qweenhool on 2017/8/17.
 */

public class RoomNumberActivity extends BaseActivity implements View.OnClickListener {

    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private String name;
    private String type;

    private RoundImageView rivHousePhoto;
    private TextView tvHouseName;
    private TextView tvHouseType;
    private ImageView ivEdit;
    private LinearLayout llAddRoomNumber;
    private LinearLayout llRoomNumberContainer;
    private LinearLayout llHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
        rivHousePhoto = (RoundImageView) findViewById(R.id.riv_house_photo);
        tvHouseName = (TextView) findViewById(R.id.tv_house_name);
        tvHouseType = (TextView) findViewById(R.id.tv_house_type);
        ivEdit = (ImageView) findViewById(R.id.iv_edit);
        llAddRoomNumber = (LinearLayout) findViewById(R.id.ll_add_room_number);
        llRoomNumberContainer = (LinearLayout) findViewById(R.id.ll_room_number_container);
        llHint = (LinearLayout) findViewById(R.id.ll_hint);

        ivEdit.setOnClickListener(this);
        llAddRoomNumber.setOnClickListener(this);
    }

    private void initData() {
        helper = new HouseDBHelper(this, "House.db", null, 1);
        db = helper.getWritableDatabase();

        Cursor cursor;
        Intent intent = getIntent();
        //从item点击事件获得的
        int id = intent.getIntExtra("ROOM_INDEX", 0);
        //添加房产界面点击保存后获得的用于查询刚刚保存的房产信息
        String last_index = intent.getStringExtra("LAST_INDEX");
        if (!TextUtils.isEmpty(last_index) && last_index.equals("last_index")) {
            //查询最后一行的数据
            cursor = db.rawQuery("select * from house order by id desc limit 0,1", null);
        } else {
            //根据item点击得到的position进而得到数据库的id来查找
            cursor = db.rawQuery("select * from house where id = ?", new String[]{String.valueOf(id)});
        }
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("name"));
                type = cursor.getString(cursor.getColumnIndex("type"));

                tvHouseName.setText(name);
                tvHouseType.setText(type);


            } while (cursor.moveToNext());
        }
        cursor.close();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_room_number;
    }

    @Override
    protected void initToolbar() {
        setTitle("房号");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("onBackPressed", "onBackPressed");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit:
                Intent modifyPropertyIntent = new Intent(this, ModifyPropertyActivity.class);
                modifyPropertyIntent.putExtra("NAME",name);
                modifyPropertyIntent.putExtra("TYPE",type);
                startActivity(modifyPropertyIntent);
                break;
            case R.id.ll_add_room_number:
                Intent addRoomNumberIntent = new Intent(this, AddRoomNumberActivity.class);
                startActivity(addRoomNumberIntent);
                break;
        }
    }


}

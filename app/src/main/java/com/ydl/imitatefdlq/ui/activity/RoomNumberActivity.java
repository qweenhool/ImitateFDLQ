package com.ydl.imitatefdlq.ui.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.feature.HouseDBHelper;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.widget.RoundImageView;

/**
 * Created by qweenhool on 2017/8/17.
 */

public class RoomNumberActivity extends BaseActivity {

    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

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
    }

    private void initData() {
        helper = new HouseDBHelper(this, "House.db", null, 1);
        db = helper.getWritableDatabase();

        //查询最后一行的数据
        Cursor cursor = db.rawQuery("select * from house order by id desc limit 0,1", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                Log.e("cursor",tvHouseName+"");
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
        Log.e("onBackPressed","onBackPressed");
    }
}

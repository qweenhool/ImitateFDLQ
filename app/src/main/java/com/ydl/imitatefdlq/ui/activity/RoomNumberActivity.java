package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.RoomNumberAdapter;
import com.ydl.imitatefdlq.db.HouseDBHelper;
import com.ydl.imitatefdlq.interfaze.OnItemClickListener;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.widget.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qweenhool on 2017/8/17.
 */

public class RoomNumberActivity extends BaseActivity implements OnItemClickListener {

    @BindView(R.id.riv_house_photo)
    RoundImageView rivHousePhoto;
    @BindView(R.id.tv_house_name)
    TextView tvHouseName;
    @BindView(R.id.tv_house_type)
    TextView tvHouseType;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.ll_add_room_number)
    LinearLayout llAddRoomNumber;
    @BindView(R.id.ll_hint)
    LinearLayout llHint;
    @BindView(R.id.rv_room_number)
    RecyclerView rvRoomNumber;
    @BindView(R.id.refresh_layout_room_number)
    SmartRefreshLayout refreshLayoutRoomNumber;

    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private String name;
    private String type;
    private String room_number;
    private RoomNumberAdapter adapter;
    private String[] roomNumberArr;

    @Override
    protected int getContentView() {
        return R.layout.activity_room_number;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initData();

        setTitle("房号");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        setTopRightButton(!TextUtils.isEmpty(room_number) ? "管理" : "", 0, new OnClickListener() {
            @Override
            public void onClick() {
                //do something

            }
        });
    }

    private void initData() {
        helper = new HouseDBHelper(this, "House.db", null, 1);
        db = helper.getWritableDatabase();

        Cursor cursor;
        Intent intent = getIntent();
        //从item点击事件获得的
        int id = intent.getIntExtra("room_index", 0);
        //添加房产界面点击保存后获得的用于查询刚刚保存的房产信息
        String last_index = intent.getStringExtra("last_index");
        if (!TextUtils.isEmpty(last_index) && last_index.equals("last_index")) {
            //查询最后一行的数据
            cursor = db.rawQuery("select * from house order by _id desc limit 0,1", null);
        } else {
            //根据item点击得到的position进而得到数据库的id来查找
            cursor = db.rawQuery("select * from house where id = ?", new String[]{String.valueOf(id)});
        }
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex("name"));
                type = cursor.getString(cursor.getColumnIndex("type"));
                room_number = cursor.getString(cursor.getColumnIndex("room_number"));

                //判断从数据库得到的房间号是否为空串，是的话就不显示RecyclerView，不为空的话就显示RecyclerView
                if (!TextUtils.isEmpty(room_number)) {
                    roomNumberArr = room_number.split(",");
                    adapter = new RoomNumberAdapter(this, roomNumberArr);
                    adapter.setOnItemClickListener(this);
                    rvRoomNumber.setLayoutManager(new LinearLayoutManager(this));
                    rvRoomNumber.setAdapter(adapter);

                    refreshLayoutRoomNumber.setVisibility(View.VISIBLE);
                    llHint.setVisibility(View.GONE);
                } else {
                    refreshLayoutRoomNumber.setVisibility(View.GONE);
                    llHint.setVisibility(View.VISIBLE);
                }

                tvHouseName.setText(name);
                tvHouseType.setText(type);

            } while (cursor.moveToNext());
        }
        cursor.close();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("onBackPressed", "onBackPressed");
    }

    @OnClick({R.id.iv_edit, R.id.ll_add_room_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit:
                Intent modifyPropertyIntent = new Intent(this, ModifyPropertyActivity.class);
                modifyPropertyIntent.putExtra("name", name);
                modifyPropertyIntent.putExtra("type", type);
                startActivity(modifyPropertyIntent);
                break;
            case R.id.ll_add_room_number:
                Intent addRoomNumberIntent = new Intent(this, AddRoomNumberActivity.class);
                startActivity(addRoomNumberIntent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onItemClick(int position) {
        Intent renterIntent = new Intent(this, RenterActivity.class);
        renterIntent.putExtra("room_number", roomNumberArr[position]);
        startActivity(renterIntent);
    }
}
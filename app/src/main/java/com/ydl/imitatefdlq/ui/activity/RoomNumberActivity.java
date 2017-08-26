package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.RoomNumberAdapter;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.HouseBean;
import com.ydl.imitatefdlq.entity.HouseBeanDao;
import com.ydl.imitatefdlq.entity.PictureBean;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.interfaze.OnItemClickListener;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.util.List;

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

    private RoomNumberAdapter adapter;

    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private RoomBeanDao roomBeanDao;
    private PictureBeanDao pictureBeanDao;

    private HouseBean houseBean;
    private RoomBean roomBean;
    private PictureBean pictureBean;

    private List<RoomBean> roomBeanList;
    private String houseId;

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

    }

    private void initData() {
        DaoSession daoSession = ((AppApplication) getApplication()).getDaoSession();
        houseBeanDao = daoSession.getHouseBeanDao();
        roomBeanDao = daoSession.getRoomBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();

        houseBean = new HouseBean();
        roomBean = new RoomBean();
        pictureBean = new PictureBean();

        Intent intent = getIntent();
        houseId = intent.getStringExtra("house_id");

        if (houseId != null) {
            List<HouseBean> houseBeanList = houseBeanDao.queryBuilder()
                    .where(HouseBeanDao.Properties.Id.eq(houseId))
                    .list();
            //设置房产名字
            tvHouseName.setText(houseBeanList.get(0).getHouseName());
            //设置房产类型
            switch (houseBeanList.get(0).getHouseType()) {
                case 1:
                    tvHouseType.setText("住宅/小区/公寓");
                    break;
                case 2:
                    tvHouseType.setText("商铺/门市房");
                    break;
                case 3:
                    tvHouseType.setText("厂房/车间");
                    break;
                case 4:
                    tvHouseType.setText("仓库/车库/停车位");
                    break;
                case 5:
                    tvHouseType.setText("写字楼/办公室");
                    break;
            }
            //Todo,设置房产照片

            //显示房间列表
            roomBeanList = roomBeanDao.queryBuilder()
                    .where(RoomBeanDao.Properties.HouseId.eq(houseId))
                    .orderDesc(RoomBeanDao.Properties.OrderNumber)
                    .list();
            if (roomBeanList != null) {
                adapter = new RoomNumberAdapter(this, roomBeanList);
                adapter.setOnItemClickListener(this);
                rvRoomNumber.setLayoutManager(new LinearLayoutManager(this));
                rvRoomNumber.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                rvRoomNumber.setAdapter(adapter);

                refreshLayoutRoomNumber.setVisibility(View.VISIBLE);
                llHint.setVisibility(View.GONE);

                setTopRightButton("管理", 0, new OnClickListener() {
                    @Override
                    public void onClick() {
                        //Todo 对房间号进行排序

                    }
                });
            } else {
                refreshLayoutRoomNumber.setVisibility(View.GONE);
                llHint.setVisibility(View.VISIBLE);
            }
        }
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
                modifyPropertyIntent.putExtra("house_id", houseId);
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
        renterIntent.putExtra("room_number", roomBeanList.get(position).getId());
        startActivity(renterIntent);
    }
}
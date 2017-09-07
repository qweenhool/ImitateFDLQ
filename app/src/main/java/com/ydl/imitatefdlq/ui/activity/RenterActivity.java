package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.HouseBean;
import com.ydl.imitatefdlq.entity.HouseBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenterActivity extends BaseActivity {

    private static final int DELETE_FINISH = 1;
    @BindView(R.id.tv_room_name)
    TextView tvRoomName;
    @BindView(R.id.tv_house_name)
    TextView tvHouseName;
    @BindView(R.id.iv_edit)
    ImageView ivEdit;
    @BindView(R.id.ll_add_room_number)
    LinearLayout llAddRoomNumber;
    @BindView(R.id.rv_renter)
    RecyclerView rvRenter;
    @BindView(R.id.refresh_layout_renter)
    SmartRefreshLayout refreshLayoutRenter;
    @BindView(R.id.ll_hint)
    LinearLayout llHint;

    private String roomId;

    private RoomBeanDao roomBeanDao;
    private HouseBeanDao houseBeanDao;

    private List<RoomBean> roomBeanList;
    private List<HouseBean> houseBeanList;

    @Override
    protected int getContentView() {
        return R.layout.activity_renter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initData();

        setTitle("租客");

        setTopLeftButton(R.drawable.back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    private void initData() {
        roomId = getIntent().getStringExtra("room_id");
        DaoSession daoSession = AppApplication.getInstance().getDaoSession();
        roomBeanDao = daoSession.getRoomBeanDao();
        houseBeanDao = daoSession.getHouseBeanDao();
        //设置房间名称
        roomBeanList = roomBeanDao.queryBuilder()
                .where(RoomBeanDao.Properties.Id.eq(roomId))
                .list();

        tvRoomName.setText(roomBeanList.get(0).getRoomName());
        //设置房产名称
        houseBeanList = houseBeanDao.queryBuilder()
                .where(HouseBeanDao.Properties.Id.eq(roomBeanList.get(0).getHouseId()))
                .list();
        tvHouseName.setText(houseBeanList.get(0).getHouseName());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_edit, R.id.ll_add_room_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_edit: //修改房号
                Intent modifyRoomNumberIntent = new Intent(this,ModifyRoomNumberActivity.class);
                modifyRoomNumberIntent.putExtra("room_id",roomId);
                modifyRoomNumberIntent.putExtra("house_name",houseBeanList.get(0).getHouseName());
                startActivityForResult(modifyRoomNumberIntent,DELETE_FINISH);
                break;
            case R.id.ll_add_room_number:
                //TODO 添加租客

                break;
        }
    }
}

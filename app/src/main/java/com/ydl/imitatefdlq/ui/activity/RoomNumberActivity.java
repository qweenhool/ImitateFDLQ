package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
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
import com.ydl.imitatefdlq.adapter.RoomNumberAdapter;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.HouseBean;
import com.ydl.imitatefdlq.entity.HouseBeanDao;
import com.ydl.imitatefdlq.entity.PictureBean;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by qweenhool on 2017/8/17.
 */

public class RoomNumberActivity extends BaseActivity {

    private static final int DELETE_FINISH = 1;
    private static final int ADD_FINISH = 2;

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
    SwipeMenuRecyclerView rvRoomNumber;
    @BindView(R.id.refresh_layout_room_number)
    SmartRefreshLayout refreshLayoutRoomNumber;

    private RoomNumberAdapter adapter;

    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private RoomBeanDao roomBeanDao;
    private PictureBeanDao pictureBeanDao;

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

        Intent intent = getIntent();
        houseId = intent.getStringExtra("house_id");

        if (houseId != null) {
            List<HouseBean> houseBeanList = houseBeanDao.queryBuilder()
                    .where(HouseBeanDao.Properties.Id.eq(houseId))
                    .list();
            //设置房产名字
            tvHouseName.setText(houseBeanList.get(0).getHouseName());
            //设置房产类型
            tvHouseType.setText(houseBeanList.get(0).getHouseType());
            //设置房产照片:根据picture表中的foreignId查到
            List<PictureBean> pictureBeanList = pictureBeanDao.queryBuilder()
                    .where(PictureBeanDao.Properties.ForeignId.eq(houseBeanList.get(0).getId()))
                    .list();
            if (pictureBeanList.size() != 0) {
                rivHousePhoto.setImageURI(Uri.parse(pictureBeanList.get(0).getPath()));
            }
            //显示房间列表
            roomBeanList = new ArrayList<>();
            List<RoomBean> list = roomBeanDao.queryBuilder()
                    .where(RoomBeanDao.Properties.HouseId.eq(houseId))
                    .orderDesc(RoomBeanDao.Properties.OrderNumber)
                    .list();
            roomBeanList.addAll(list);
            adapter = new RoomNumberAdapter(this, roomBeanList);
            rvRoomNumber.setLayoutManager(new LinearLayoutManager(this));
            rvRoomNumber.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#d7d7db")));
            rvRoomNumber.setAdapter(adapter);

            //设置item的点击事件
            rvRoomNumber.setSwipeItemClickListener(new SwipeItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(RoomNumberActivity.this, RenterActivity.class);
                    intent.putExtra("room_number", roomBeanList.get(position).getId());
                    startActivity(intent);
                }
            });

            // 设置删除菜单。
            rvRoomNumber.setSwipeMenuCreator(new SwipeMenuCreator() {
                @Override
                public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(RoomNumberActivity.this);
                    // 各种文字和图标属性设置。
                    deleteItem.setText("删除")
                            .setHeight(MATCH_PARENT)
                            .setWidth(250)
                            .setTextSize(15)
                            .setTextColor(Color.WHITE)
                            .setBackgroundColor(Color.parseColor("#FE3B30"));
                    rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。

                }
            });
            //设置删除菜单监听器
            rvRoomNumber.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
                @Override
                public void onItemClick(final SwipeMenuBridge menuBridge) {
                    final int adapterPosition = menuBridge.getAdapterPosition();
                    StyledDialog.buildIosAlert("删除确认",
                            "您确定要删除" + roomBeanList.get(adapterPosition).getRoomName() + ",同时将删除其全部租客及账单",
                            new MyDialogListener() {
                                @Override
                                public void onFirst() {
                                    //Todo 联网删除服务器数据

                                    //删除room表的这一行数据
                                    // RecyclerView的Item的position。
                                    roomBeanDao.deleteByKey(roomBeanList.get(adapterPosition).getId());
                                    roomBeanList.remove(adapterPosition);
                                    adapter.notifyDataSetChanged();
                                    //Todo 同时删除房号配置、照片，以及删除租客及账单
                                }

                                @Override
                                public void onSecond() {

                                }
                            }).show();
                    menuBridge.closeMenu();
                }
            });

            if (roomBeanList.size() != 0) {
                refreshLayoutRoomNumber.setVisibility(View.VISIBLE);
                llHint.setVisibility(View.GONE);

            } else {
                refreshLayoutRoomNumber.setVisibility(View.GONE);
                llHint.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //从修改房产页面返回，更新数据
        List<HouseBean> houseBeanList = houseBeanDao.queryBuilder()
                .where(HouseBeanDao.Properties.Id.eq(houseId))
                .list();
        //设置房产名字
        tvHouseName.setText(houseBeanList.get(0).getHouseName());
        //设置房产类型
        tvHouseType.setText(houseBeanList.get(0).getHouseType());
        //设置房产照片
        List<PictureBean> pictureBeanList = pictureBeanDao.queryBuilder()
                .where(PictureBeanDao.Properties.ForeignId.eq(houseBeanList.get(0).getId()))
                .list();
        if (pictureBeanList.size() != 0) {
            rivHousePhoto.setImageURI(Uri.parse(pictureBeanList.get(0).getPath()));
        } else {
            rivHousePhoto.setImageResource(R.drawable.room_info);
        }

        //从添加房号页面返回
        if (roomBeanList.size() != 0) {
            refreshLayoutRoomNumber.setVisibility(View.VISIBLE);
            llHint.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else {
            refreshLayoutRoomNumber.setVisibility(View.GONE);
            llHint.setVisibility(View.VISIBLE);
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
                startActivityForResult(modifyPropertyIntent, DELETE_FINISH);
                break;
            case R.id.ll_add_room_number:
                Intent addRoomNumberIntent = new Intent(this, AddRoomNumberActivity.class);
                addRoomNumberIntent.putExtra("house_id", houseId);
                startActivityForResult(addRoomNumberIntent,ADD_FINISH);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DELETE_FINISH:
                if (resultCode == RESULT_OK) {
                    finish();
                }
                break;
            case ADD_FINISH:
                if(resultCode == RESULT_OK){
                    String roomId = data.getStringExtra("room_id");
                    List<RoomBean> list = roomBeanDao.queryBuilder()
                            .where(RoomBeanDao.Properties.Id.eq(roomId))
                            .list();
                    roomBeanList.add(0,list.get(0));
                }
            default:
                break;
        }
    }
}
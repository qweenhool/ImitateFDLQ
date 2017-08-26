package com.ydl.imitatefdlq.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hss01248.dialog.StyledDialog;
import com.hss01248.dialog.interfaces.MyDialogListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.ydl.imitatefdlq.AppApplication;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.HousePropertyAdapter;
import com.ydl.imitatefdlq.entity.DaoSession;
import com.ydl.imitatefdlq.entity.HouseBean;
import com.ydl.imitatefdlq.entity.HouseBeanDao;
import com.ydl.imitatefdlq.entity.PictureBean;
import com.ydl.imitatefdlq.entity.PictureBeanDao;
import com.ydl.imitatefdlq.entity.RoomBean;
import com.ydl.imitatefdlq.entity.RoomBeanDao;
import com.ydl.imitatefdlq.ui.activity.AddHouseActivity;
import com.ydl.imitatefdlq.ui.activity.RoomNumberActivity;
import com.ydl.imitatefdlq.ui.activity.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by qweenhool on 2017/8/4.
 */

public class HousePropertyFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tb_house_property)
    Toolbar tbHouseProperty;
    @BindView(R.id.ll_house_list)
    LinearLayout llHouseList;
    @BindView(R.id.ll_add_house_button_above)
    LinearLayout llAddHouseButtonAbove;
    @BindView(R.id.ll_add_house_button_below)
    LinearLayout llAddHouseButtonBelow;
    @BindView(R.id.refresh_layout_house_property)
    SmartRefreshLayout refreshLayoutHouseProperty;
    @BindView(R.id.rv_house_property)
    SwipeMenuRecyclerView rvHouseProperty;

    private AppCompatActivity activity;
    private HousePropertyAdapter adapter;

    //每一个对象代表一张表
    private HouseBeanDao houseBeanDao;
    private RoomBeanDao roomBeanDao;
    private PictureBeanDao pictureBeanDao;

    private HouseBean houseBean;
    private RoomBean roomBean;
    private PictureBean pictureBean;

    private List<HouseBean> houseBeanList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_property, container, false);
        unbinder = ButterKnife.bind(this, view);
        Log.e("HousePropertyFragment", "onCreateView");

        initData();
        setToolbar();
        setRefreshLayout();

        return view;
    }

    private void initData() {
        activity = (AppCompatActivity) getActivity();

        DaoSession daoSession = ((AppApplication) activity.getApplication()).getDaoSession();
        houseBeanDao = daoSession.getHouseBeanDao();
        roomBeanDao = daoSession.getRoomBeanDao();
        pictureBeanDao = daoSession.getPictureBeanDao();

        houseBean = new HouseBean();
        roomBean = new RoomBean();
        pictureBean = new PictureBean();

    }

    private void setToolbar() {

        setHasOptionsMenu(true);

        tbHouseProperty.setNavigationIcon(R.drawable.ic_search);
        activity.setSupportActionBar(tbHouseProperty);
        tvToolbarName.setText("房产");

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //给actionbar添加空标题，否则会一直显示应用标题
            actionBar.setTitle("");

        }

        tbHouseProperty.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });
    }

    private void setRefreshLayout() {
        refreshLayoutHouseProperty.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //Todo 模拟联网操作
                refreshLayoutHouseProperty.finishRefresh(2000, true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("HousePropertyFragment", "onResume-----");

        houseBeanList = houseBeanDao.queryBuilder()
                .orderDesc(HouseBeanDao.Properties.OrderNumber)
                .list();
        if (houseBeanList.size() != 0) {
            adapter = new HousePropertyAdapter(activity, houseBeanList);
            rvHouseProperty.setLayoutManager(new LinearLayoutManager(activity));
            rvHouseProperty.setAdapter(adapter);

            //设置item的点击事件
            rvHouseProperty.setSwipeItemClickListener(new SwipeItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent intent = new Intent(activity, RoomNumberActivity.class);
                    intent.putExtra("house_id", houseBeanList.get(position).getId());
                    startActivity(intent);
                }
            });

            // 设置删除菜单。
            rvHouseProperty.setSwipeMenuCreator(new SwipeMenuCreator() {
                @Override
                public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(activity);
                    // 各种文字和图标属性设置。
                    deleteItem.setText("删除")
                            .setHeight(MATCH_PARENT)
                            .setWidth(250)
                            .setTextSize(16)
                            .setTextColor(Color.WHITE)
                            .setBackgroundColor(Color.parseColor("#FE3B30"));
                    rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。

                }
            });
            //设置删除菜单监听器
            rvHouseProperty.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
                @Override
                public void onItemClick(final SwipeMenuBridge menuBridge) {
                    //否则的话就关闭menu，通知adapter更新
                    StyledDialog.buildIosAlert("删除确认", "删除房产将一并删除其所有的房号,租客及账单，您确定要删除吗?", new MyDialogListener() {
                        @Override
                        public void onFirst() {
                            //Todo 联网服务器删除数据
                            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
                            houseBeanDao.deleteByKey(houseBeanList.get(adapterPosition).getId());
                            houseBeanList.remove(adapterPosition);
                            if (houseBeanList.size() == 0) {//houseBeanList里面没有数据就显示初始界面
                                llHouseList.setVisibility(View.GONE);
                                ivBg.setVisibility(View.VISIBLE);
                                llAddHouseButtonBelow.setVisibility(View.VISIBLE);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onSecond() {

                        }
                    }).show();
                    menuBridge.closeMenu();
                }
            });

            llHouseList.setVisibility(View.VISIBLE);
            ivBg.setVisibility(View.GONE);
            llAddHouseButtonBelow.setVisibility(View.GONE);
        } else {
            llHouseList.setVisibility(View.GONE);
            ivBg.setVisibility(View.VISIBLE);
            llAddHouseButtonBelow.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.e(this.getClass().getSimpleName(), "onCreateOptionsMenu-----");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.e(this.getClass().getSimpleName(), "onPrepareOptionsMenu-----");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("HousePropertyFragment", "onPause-----");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("HousePropertyFragment", "onStop-----");
    }

    @OnClick({R.id.ll_add_house_button_above, R.id.ll_add_house_button_below})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add_house_button_above:
                startAddHouseActivity();
                break;
            case R.id.ll_add_house_button_below:
                startAddHouseActivity();
                break;
        }
    }

    private void startAddHouseActivity() {
        Intent addHouseIntent = new Intent(getContext(), AddHouseActivity.class);
        startActivity(addHouseIntent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

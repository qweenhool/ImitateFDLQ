package com.ydl.imitatefdlq.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.db.HouseDBHelper;
import com.ydl.imitatefdlq.entity.House;
import com.ydl.imitatefdlq.ui.activity.AddHouseActivity;
import com.ydl.imitatefdlq.ui.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    @BindView(R.id.ll_add_house_gone)
    LinearLayout llAddHouseGone;
    @BindView(R.id.lv_house_property)
    ListView lvHouseProperty;
    @BindView(R.id.ll_add_house_button_above)
    LinearLayout llAddHouseButtonAbove;
    @BindView(R.id.ll_add_house_button_below)
    LinearLayout llAddHouseButtonBelow;
    @BindView(R.id.refresh_layout_house_property)
    SmartRefreshLayout refreshLayoutHouseProperty;

    private AppCompatActivity activity;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private List<House> houseList;
    private SimpleCursorAdapter adapter;

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
        helper = new HouseDBHelper(getContext(), "House.db", null, 1);
        db = helper.getWritableDatabase();
    }

    private void setToolbar() {

        setHasOptionsMenu(true);

        activity = (AppCompatActivity) getActivity();
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
                refreshLayoutHouseProperty.finishRefresh(2000, true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("HousePropertyFragment", "onResume-----");
        houseList = new ArrayList<>();
        //从id最大的那一行开始取数据，也就是最新的先取出来放recyclerview的最前面
        Cursor cursor = db.rawQuery("select * from house order by _id desc", null);
        if (cursor.moveToFirst()) {
//            do {
//                String name = cursor.getString(cursor.getColumnIndex("name"));
//                String type = cursor.getString(cursor.getColumnIndex("type"));
//                String photo = cursor.getString(cursor.getColumnIndex("photo"));
//                String account = cursor.getString(cursor.getColumnIndex("account"));
//                String roomNumber = cursor.getString(cursor.getColumnIndex("room_number"));
//
//                House house = new House();
//                house.setName(name);
//                house.setType(type);
//                house.setPhoto(photo);
//                house.setAccount(account);
//                house.setRoomNumber(roomNumber);
//                houseList.add(house);
//            } while (cursor.moveToNext());

            llAddHouseGone.setVisibility(View.VISIBLE);
            ivBg.setVisibility(View.GONE);
            llAddHouseButtonBelow.setVisibility(View.GONE);

            adapter = new SimpleCursorAdapter(activity, R.layout.item_house_property, null,
                    new String[]{"photo", "name", "room_number"}, new int[]{R.id.riv_house_photo, R.id.tv_house_name, R.id.tv_house_info}, 0);
            lvHouseProperty.setAdapter(adapter);
            adapter.changeCursor(cursor);

        } else {
            llAddHouseGone.setVisibility(View.GONE);
            ivBg.setVisibility(View.VISIBLE);
            llAddHouseButtonBelow.setVisibility(View.VISIBLE);
            cursor.close();
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

package com.ydl.imitatefdlq.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.HousePropertyAdapter;
import com.ydl.imitatefdlq.db.HouseDBHelper;
import com.ydl.imitatefdlq.entity.House;
import com.ydl.imitatefdlq.ui.activity.AddHouseActivity;
import com.ydl.imitatefdlq.ui.activity.RoomNumberActivity;
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

public class HousePropertyFragment extends Fragment implements HousePropertyAdapter.OnHouseItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tb_house_property)
    Toolbar tbHouseProperty;
    @BindView(R.id.ll_add_house_gone)
    LinearLayout llAddHouseGone;
    @BindView(R.id.rv_house_property)
    RecyclerView rvHouseProperty;
    @BindView(R.id.ll_add_house_button_above)
    LinearLayout llAddHouseButtonAbove;
    @BindView(R.id.ll_add_house_button_below)
    LinearLayout llAddHouseButtonBelow;
    @BindView(R.id.refresh_layout_house_property)
    SmartRefreshLayout refreshLayoutHouseProperty;

    private AppCompatActivity activity;
    private Context context;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private HousePropertyAdapter adapter;
    private List<House> houseList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_property, container, false);
        unbinder = ButterKnife.bind(this, view);
        Log.e("onCreateView","onCreateView");
        initData();
        setToolbar();
        setRefreshLayout();

        return view;
    }

    private void initData() {
        helper = new HouseDBHelper(getContext(), "House.db", null, 1);
        db = helper.getWritableDatabase();
        context = getContext();
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
        Cursor cursor = db.rawQuery("select * from house order by id desc", null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String account = cursor.getString(cursor.getColumnIndex("account"));
                String roomNumber = cursor.getString(cursor.getColumnIndex("room_number"));

                House house = new House();
                house.setName(name);
                house.setType(type);
                house.setPhoto(photo);
                house.setAccount(account);
                house.setRoomNumber(roomNumber);
                houseList.add(house);
                Log.e("HousePropertyFragment", "onResume-----" + houseList.size());
            } while (cursor.moveToNext());

            llAddHouseGone.setVisibility(View.VISIBLE);
            ivBg.setVisibility(View.GONE);
            llAddHouseButtonBelow.setVisibility(View.GONE);

        } else {
            llAddHouseGone.setVisibility(View.GONE);
            ivBg.setVisibility(View.VISIBLE);
            llAddHouseButtonBelow.setVisibility(View.VISIBLE);
        }
        cursor.close();

        adapter = new HousePropertyAdapter(context, houseList);
        adapter.setOnItemClickListener(this);
        rvHouseProperty.setLayoutManager(new LinearLayoutManager(context));
        rvHouseProperty.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        Log.e("onCreateOptionsMenu","onCreateOptionsMenu");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        Log.e("onPrepareOptionsMenu","onPrepareOptionsMenu");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
    public void onItemClick(int position) {
        Intent roomNumberIntent = new Intent(getContext(), RoomNumberActivity.class);
        roomNumberIntent.putExtra("ROOM_INDEX", adapter.getItemCount() - position);
        startActivity(roomNumberIntent);
    }
}

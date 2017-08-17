package com.ydl.imitatefdlq.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.feature.HouseDBHelper;
import com.ydl.imitatefdlq.ui.activity.AddHouseActivity;
import com.ydl.imitatefdlq.ui.activity.SearchActivity;

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
    @BindView(R.id.ll_add_house)
    LinearLayout tvAddHouse;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tb_house_property)
    Toolbar tbHouseProperty;
    @BindView(R.id.ll_add_house_gone)
    LinearLayout llAddHouseGone;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.sv_container)
    ScrollView svContainer;

    private AppCompatActivity activity;
    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_property, container, false);
        unbinder = ButterKnife.bind(this, view);


        initData();
        setToolbar();

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ll_add_house)
    public void onViewClicked() {
        Intent addRoomIntent = new Intent(getContext(), AddHouseActivity.class);
        startActivity(addRoomIntent);
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

    @Override
    public void onResume() {
        super.onResume();

        Cursor cursor = db.query("house", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String account = cursor.getString(cursor.getColumnIndex("account"));
                String roomNumber = cursor.getString(cursor.getColumnIndex("room_number"));

                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_remove_header, llContainer, false);
                llContainer.addView(view, 0);

            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}

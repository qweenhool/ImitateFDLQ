package com.ydl.imitatefdlq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.activity.AddHouseActivity;
import com.ydl.imitatefdlq.activity.SearchActivity;

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
    @BindView(R.id.tv_add_room)
    LinearLayout tvAddRoom;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.tb_house_property)
    Toolbar tbHouseProperty;

    private AppCompatActivity activity;

    public HousePropertyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house_property, container, false);
        unbinder = ButterKnife.bind(this, view);

        setToolbar();

        return view;
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

    @OnClick(R.id.tv_add_room)
    public void onViewClicked() {
        Intent addRoomIntent = new Intent(getContext(), AddHouseActivity.class);
        startActivity(addRoomIntent);
    }
}

package com.ydl.imitatefdlq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.activity.OtherActivity;
import com.ydl.imitatefdlq.activity.SearchActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qweenhool on 2017/8/4.
 */

public class SecondFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_add_room)
    LinearLayout tvAddRoom;
    @BindView(R.id.iv_bg)
    ImageView ivBg;

    private AppCompatActivity activity;

    public SecondFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        unbinder = ButterKnife.bind(this, view);

        setHasOptionsMenu(true);

        activity = (AppCompatActivity) getActivity();
        toolbar.setNavigationIcon(R.drawable.ic_search);
        activity.setSupportActionBar(toolbar);
        tvToolbarName.setText("房产");

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //给actionbar添加空标题，否则会一直显示应用标题
            actionBar.setTitle("");
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent searchIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_add_room)
    public void onViewClicked() {
        Intent addRoomIntent = new Intent(getContext(), OtherActivity.class);
        startActivity(addRoomIntent);
    }
}

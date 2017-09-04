package com.ydl.imitatefdlq.ui.fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.ui.activity.OtherActivity;
import com.ydl.imitatefdlq.adapter.TabAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qweenhool on 2017/8/4.
 */

public class ThirdFragment extends Fragment {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;
    @BindView(R.id.vp_fragment3)
    ViewPager vpFragment3;
    @BindView(R.id.stl_fragment3)
    SlidingTabLayout stlFragment3;
    private static final String TAG = "ThirdFragment";
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private String[] mTitles;
    private ArrayList<Fragment> mFragments;
    private AppCompatActivity activity;
    private TabAdapter mAdapter;


    public ThirdFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        unbinder = ButterKnife.bind(this, view);


        initToolbar();
        initFab();
        initData();

        return view;
    }

    private void initToolbar() {
        setHasOptionsMenu(true);

        toolbar.setNavigationIcon(R.drawable.ic_me);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //给actionbar添加空标题，否则会一直显示应用标题
            actionBar.setTitle("");
        }
    }


    private void initFab() {
        fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(),R.color.colorFAB)));
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_write));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,OtherActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mTitles = new String[]{"关注", "热门", "最新"};
        mFragments = new ArrayList<>();
        mFragments.add(new Tab1Fragment());
        mFragments.add(new Tab2Fragment());
        mFragments.add(new Tab3Fragment());

        mAdapter = new TabAdapter(activity.getSupportFragmentManager(), mTitles, mFragments);
        vpFragment3.setAdapter(mAdapter);
        vpFragment3.setOffscreenPageLimit(3);
        stlFragment3.setViewPager(vpFragment3, mTitles, getActivity(), mFragments);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent searchIntent = new Intent(getContext(), OtherActivity.class);
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(searchIntent);
                return true;
            case R.id.action_notice:
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
}

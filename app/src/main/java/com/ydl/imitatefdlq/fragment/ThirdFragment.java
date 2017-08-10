package com.ydl.imitatefdlq.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.activity.OtherActivity;
import com.ydl.imitatefdlq.entity.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by qweenhool on 2017/8/4.
 */

public class ThirdFragment extends Fragment {


    @BindView(R.id.tl_news)
    CommonTabLayout tlNews;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;

    private static final String TAG = "ThirdFragment";
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    private String[] mTitles = {"关注", "热门", "最新"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private AppCompatActivity activity;
    private Tab1Fragment fragment1;
    private Tab2Fragment fragment2;
    private Tab3Fragment fragment3;


    public ThirdFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate被执行了");
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        unbinder = ButterKnife.bind(this, view);
        Log.e(TAG, "onCreateView被执行了");
        setHasOptionsMenu(true);

        tlNews.setTabData(mTabEntities);
        toolbar.setNavigationIcon(R.drawable.ic_me);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

        fragment1 = new Tab1Fragment();
        fragment2 = new Tab2Fragment();
        fragment3 = new Tab3Fragment();


        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //给actionbar添加空标题，否则会一直显示应用标题
            actionBar.setTitle("");
        }

        tlNews.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        return view;
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

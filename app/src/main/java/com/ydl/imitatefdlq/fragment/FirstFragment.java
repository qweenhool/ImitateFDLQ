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
import android.widget.ListView;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.activity.SearchActivity;
import com.ydl.imitatefdlq.adapter.ListViewAdapter;
import com.ydl.imitatefdlq.customView.RefreshableView;


/**
 * Created by qweenhool on 2017/8/4.
 */

public class FirstFragment extends Fragment {

    private Toolbar mToolbar;
    private ListView mListView;
    private ListViewAdapter mAdapter;
    private RefreshableView mRefreshableView;


    public FirstFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);


        AppCompatActivity activity = (AppCompatActivity) getActivity();
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(mToolbar);

        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //给actionbar添加空标题，否则会一直显示应用标题
            actionBar.setTitle("");
        }

        mToolbar.setNavigationIcon(R.drawable.ic_search);
        //这句不添加home按钮不会显示
        setHasOptionsMenu(true);



        mRefreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mAdapter = new ListViewAdapter(getContext());
        mListView.setAdapter(mAdapter);

        mRefreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mRefreshableView.finishRefreshing();
            }
        }, 0);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
}

package com.ydl.imitatefdlq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.ydl.imitatefdlq.R;
import com.ydl.imitatefdlq.adapter.TabAdapter;
import com.ydl.imitatefdlq.ui.base.BaseActivity;
import com.ydl.imitatefdlq.ui.fragment.HomePageFragment;
import com.ydl.imitatefdlq.ui.fragment.HousePropertyFragment;
import com.ydl.imitatefdlq.ui.fragment.MyselfFragment;
import com.ydl.imitatefdlq.ui.fragment.ThirdFragment;
import com.ydl.imitatefdlq.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabAdapter mTabAdapter;
    private String[] mTabName;
    private List<Fragment> mFragments;

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        initView();
        initData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String homeProperty = intent.getStringExtra("home_property");
        if (homeProperty != null && homeProperty.equals("home_property")) {
            mViewPager.setCurrentItem(1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        toolbar.setVisibility(View.GONE);
    }

    private void initData() {
        //底部栏名称
        mTabName = new String[]{"首页", "房产", "房东汇", "我"};
        //新建fragment集合对象
        mFragments = new ArrayList<>();
        mFragments.add(new HomePageFragment());
        mFragments.add(new HousePropertyFragment());
        mFragments.add(new ThirdFragment());
        mFragments.add(new MyselfFragment());

        mTabAdapter = new TabAdapter(getSupportFragmentManager(), mTabName, mFragments);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mTabAdapter);
        //防止频繁的销毁视图
        mViewPager.setOffscreenPageLimit(4);
        //将ViewPager和TabLayout关联起来
        //This layout will be automatically populated from the PagerAdapter's page titles.
        mTabLayout.setupWithViewPager(mViewPager);


        TabLayout.Tab tab0 = mTabLayout.getTabAt(0);
        TabLayout.Tab tab1 = mTabLayout.getTabAt(1);
        TabLayout.Tab tab2 = mTabLayout.getTabAt(2);
        TabLayout.Tab tab3 = mTabLayout.getTabAt(3);

        tab0.setIcon(R.drawable.selector_tab1);
        tab1.setIcon(R.drawable.selector_tab2);
        tab2.setIcon(R.drawable.selector_tab3);
        tab3.setIcon(R.drawable.selector_tab4);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 不退出程序，进入后台
            moveTaskToBack(true);
        }
        return true;
    }
}

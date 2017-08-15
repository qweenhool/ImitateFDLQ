package com.ydl.imitatefdlq;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.githang.statusbar.StatusBarCompat;
import com.ydl.imitatefdlq.adapter.TabAdapter;
import com.ydl.imitatefdlq.customView.NoScrollViewPager;
import com.ydl.imitatefdlq.fragment.HomePageFragment;
import com.ydl.imitatefdlq.fragment.FourthFragment;
import com.ydl.imitatefdlq.fragment.HousePropertyFragment;
import com.ydl.imitatefdlq.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoScrollViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabAdapter mTabAdapter;
    private String[] mTabName;
    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏颜色为黑色，这里图省事引用了一个库
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorStatusbar), false);
        initView();
        initData();

    }


    private void initView() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

    }

    private void initData() {
        //底部栏名称
        mTabName = new String[]{"首页", "房产", "房东汇", "我"};
        //新建fragment集合对象
        mFragments = new ArrayList<>();
        mFragments.add(new HomePageFragment());
        mFragments.add(new HousePropertyFragment());
        mFragments.add(new ThirdFragment());
        mFragments.add(new FourthFragment());

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

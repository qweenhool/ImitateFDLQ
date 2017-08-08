package com.ydl.imitatefdlq;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.githang.statusbar.StatusBarCompat;
import com.ydl.imitatefdlq.adapter.TabAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabAdapter mTabAdapter;
    private String[] mTabName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置状态栏颜色为黑色，这里图省事引用了一个库
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this,R.color.colorStatusbar), false);
        initView();


    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        //底部栏名称
        mTabName = new String[]{"东", "南", "西", "北"};
        mTabAdapter = new TabAdapter(getSupportFragmentManager(),mTabName);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mTabAdapter);
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
}

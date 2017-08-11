package com.ydl.imitatefdlq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by qweenhool on 2017/8/5.
 */

public class TabAdapter extends FragmentPagerAdapter {


    private String[] mTabName;
    private List<Fragment> mFragments;

    public TabAdapter(FragmentManager fm, String[] tabName, List<Fragment> fragments) {
        super(fm);
        mTabName = tabName;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTabName.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabName[position];
    }


}

package com.ydl.imitatefdlq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ydl.imitatefdlq.fragment.FirstFragment;
import com.ydl.imitatefdlq.fragment.FourthFragment;
import com.ydl.imitatefdlq.fragment.SecondFragment;
import com.ydl.imitatefdlq.fragment.ThirdFragment;

/**
 * Created by qweenhool on 2017/8/5.
 */

public class TabAdapter extends FragmentPagerAdapter {


    private final String[] mTabName;

    public TabAdapter(FragmentManager fm, String[] tabName) {
        super(fm);
        this.mTabName=tabName;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FirstFragment();
            case 1:
                return new SecondFragment();
            case 2:
                return new ThirdFragment();
            case 3:
                return new FourthFragment();
        }
        return new FirstFragment();
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

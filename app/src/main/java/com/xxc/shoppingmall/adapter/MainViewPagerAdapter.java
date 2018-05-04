package com.xxc.shoppingmall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xuxingchen on 2017/11/17.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    public static final String[] TITLE=new String[]{"首页","商城","我的"};

    private List<Fragment> mFragments;


    public MainViewPagerAdapter(List<Fragment> fragments, FragmentManager fm) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        if (null == mFragments) {
            return 0;
        }
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }
}

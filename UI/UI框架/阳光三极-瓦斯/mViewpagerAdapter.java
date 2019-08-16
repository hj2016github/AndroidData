package com.gasplatform.ygsj.mashgasmonitoring.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class mViewpagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    public mViewpagerAdapter(FragmentManager fm) {
        super(fm);
    }
    public void setList(List<Fragment> list) {
        this.list = list;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}

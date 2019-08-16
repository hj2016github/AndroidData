package com.tyyh.android06_fragmentviewpager_day31;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 1,v4包的fragment; 2,重写几个几个方法,类似于Listview的adapter;
 */
public class MyAdapter extends FragmentPagerAdapter{
    private List<Fragment> list;
    public MyAdapter(FragmentManager fm) {
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

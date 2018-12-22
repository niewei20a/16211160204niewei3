package com.example.a18199.a16211160204niewei.Tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {

    private List<TabFragment> mFragmentList;//各导航的Fragment
    private List<String> mTitle; //导航的标题

    public FragmentAdapter(FragmentManager fm, List<TabFragment> mFragmentList, List<String> mTitle) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.mTitle = mTitle;
    }

    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }
}

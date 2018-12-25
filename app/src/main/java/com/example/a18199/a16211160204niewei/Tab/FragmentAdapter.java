package com.example.a18199.a16211160204niewei.Tab;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a18199.a16211160204niewei.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {

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

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       // mFragmentList.get(position).onDestroy();
        super.destroyItem(container, position, object);
    }
}

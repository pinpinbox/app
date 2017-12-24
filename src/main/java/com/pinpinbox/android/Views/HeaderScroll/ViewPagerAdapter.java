package com.pinpinbox.android.Views.HeaderScroll;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;



/**
 * Created by kevin9594 on 2016/7/31.
 */
public class ViewPagerAdapter extends FragmentPagerAdapterExt {

    private final Resources mResources;
    private final List<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm, Resources r, List<Fragment> fragments) {
        super(fm);
        this.mResources = r;
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public String makeFragmentTag(int position) {
        return "";
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    public boolean canScrollVertically(int position, int direction) {
        return true;
    }
}

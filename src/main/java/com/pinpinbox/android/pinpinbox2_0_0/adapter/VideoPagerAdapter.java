package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by vmage on 2018/4/27.
 */

public class VideoPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public VideoPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);

        this.fragmentList = fragmentList;

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

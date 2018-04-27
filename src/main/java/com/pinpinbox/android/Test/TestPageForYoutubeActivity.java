package com.pinpinbox.android.Test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumExplore;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemCategoryBanner;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCGAbannerImage;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCGAbannerVideo;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol102_GetCategoryArea;

import java.util.List;

/**
 * Created by vmage on 2018/4/17.
 */

public class TestPageForYoutubeActivity extends FragmentActivity{

    private Activity mActivity;

    private FragmentPagerItemAdapter adapter;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_pageforyoutube);
//        YouTubePlayerView youTubeView = (YouTubePlayerView)
//                findViewById(R.id.youtube_view);
//        youTubeView.initialize(DEVELOPER_KEY, this);



        init();


        set();


    }

    private void init(){

        mActivity = this;

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        int bannerWidth = ScreenUtils.getScreenWidth();

        int bannerHeight = (bannerWidth * 380) / 960;

        viewPager.setLayoutParams(new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(), bannerHeight));

    }

    private void set(){


        Protocol102_GetCategoryArea protocol102 = new Protocol102_GetCategoryArea(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                 "6",
                new Protocol102_GetCategoryArea.TaskCallBack() {
                    @Override
                    public void Prepare() {



                    }

                    @Override
                    public void Post() {



                    }

                    @Override
                    public void Success(List<ItemUser> cgaUserList, List<ItemAlbumExplore> itemAlbumExploreList, List<ItemCategoryBanner> itemCategoryBannerList, String categoryareaName) {


                        FragmentPagerItems fragmentPagerItems = new FragmentPagerItems(mActivity);


                        for (int i = 0; i < itemCategoryBannerList.size(); i++) {

                            MyLog.Set("e", mActivity.getClass(), "getBannerType => " + itemCategoryBannerList.get(i).getBannerType());
                            MyLog.Set("e", mActivity.getClass(), "getImageUrl => " + itemCategoryBannerList.get(i).getImageUrl());
                            MyLog.Set("e", mActivity.getClass(), "getImageLink => " + itemCategoryBannerList.get(i).getImageLink());
                            MyLog.Set("e", mActivity.getClass(), "getVideoIdByUrl => " + itemCategoryBannerList.get(i).getVideoIdByUrl());
                            MyLog.Set("e", mActivity.getClass(), "getVideoLink => " + itemCategoryBannerList.get(i).getVideoLink());
                            MyLog.Set("e", mActivity.getClass(), "isVideoAuto => " + itemCategoryBannerList.get(i).isVideoAuto());
                            MyLog.Set("e", mActivity.getClass(), "isVideoMute => " + itemCategoryBannerList.get(i).isVideoMute());
                            MyLog.Set("e", mActivity.getClass(), "isVideoRepeat => " + itemCategoryBannerList.get(i).isVideoRepeat());

                            String bannerType = itemCategoryBannerList.get(i).getBannerType();

                            Bundle bundle = new Bundle();

                            if(bannerType.equals(ItemCategoryBanner.TYPE_VIDEO)){

                                bundle.putString(Key.youtubeVideoId, itemCategoryBannerList.get(i).getVideoIdByUrl());

                                fragmentPagerItems.add(FragmentPagerItem.of("", FragmentCGAbannerVideo.class, bundle));

                            }

                            if(bannerType.equals(ItemCategoryBanner.TYPE_IMAGE)){

                                bundle.putString(Key.image, itemCategoryBannerList.get(i).getImageUrl());
                                bundle.putString(Key.imageLink, itemCategoryBannerList.get(i).getImageLink());

                                fragmentPagerItems.add(FragmentPagerItem.of("", FragmentCGAbannerImage.class, bundle));

                            }

                        }

                        adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), fragmentPagerItems);

                        viewPager.setAdapter(adapter);


//                        TestPageAdapter testPageAdapter = new TestPageAdapter(mActivity, itemCategoryBannerList);
//
//                        viewPager.setAdapter(testPageAdapter);

                    }

                    @Override
                    public void TimeOut() {



                    }


                }

        );


    }




}

package com.pinpinbox.android.Test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumExplore;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemCategoryBanner;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol102;

import java.util.List;

/**
 * Created by vmage on 2018/4/17.
 */

public class TestPageForYoutubeActivity extends FragmentActivity implements YouTubePlayer.OnInitializedListener {



    public static final String DEVELOPER_KEY = "AIzaSyATCeohA43aiTn-DkMI0ATpLJMiMWMDhdU";

    private Activity mActivity;

    private YouTubePlayer mYouTubePlayer;

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


        Protocol102 protocol102 = new Protocol102(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                 "6",
                new Protocol102.TaskCallBack() {
                    @Override
                    public void Prepare() {



                    }

                    @Override
                    public void Post() {



                    }

                    @Override
                    public void Success(List<ItemUser> cgaUserList, List<ItemAlbumExplore> itemAlbumExploreList, List<ItemCategoryBanner> itemCategoryBannerList, String categoryareaName) {

                        for (int i = 0; i < itemCategoryBannerList.size(); i++) {

                            MyLog.Set("e", mActivity.getClass(), "getBannerType => " + itemCategoryBannerList.get(i).getBannerType());
                            MyLog.Set("e", mActivity.getClass(), "getImageUrl => " + itemCategoryBannerList.get(i).getImageUrl());
                            MyLog.Set("e", mActivity.getClass(), "getImageLink => " + itemCategoryBannerList.get(i).getImageLink());
                            MyLog.Set("e", mActivity.getClass(), "getVideoIdByUrl => " + itemCategoryBannerList.get(i).getVideoIdByUrl());
                            MyLog.Set("e", mActivity.getClass(), "getVideoLink => " + itemCategoryBannerList.get(i).getVideoLink());
                            MyLog.Set("e", mActivity.getClass(), "isVideoAuto => " + itemCategoryBannerList.get(i).isVideoAuto());
                            MyLog.Set("e", mActivity.getClass(), "isVideoMute => " + itemCategoryBannerList.get(i).isVideoMute());
                            MyLog.Set("e", mActivity.getClass(), "isVideoRepeat => " + itemCategoryBannerList.get(i).isVideoRepeat());

                        }

                        TestPageAdapter testPageAdapter = new TestPageAdapter(mActivity, itemCategoryBannerList);

                        viewPager.setAdapter(testPageAdapter);



                    }

                    @Override
                    public void TimeOut() {



                    }


                }

        );


    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mYouTubePlayer = youTubePlayer;

        mYouTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        mYouTubePlayer.setPlaybackEventListener(playbackEventListener);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
            MyLog.Set("d", getClass(), "onAdStarted");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
            MyLog.Set("d", getClass(), "onError");
        }

        @Override
        public void onLoaded(String arg0) {
            MyLog.Set("d", getClass(), "onLoaded");
        }

        @Override
        public void onLoading() {
            MyLog.Set("d", getClass(), "onLoading");
        }

        @Override
        public void onVideoEnded() {
            MyLog.Set("d", getClass(), "onVideoEnded");
        }

        @Override
        public void onVideoStarted() {
            MyLog.Set("d", getClass(), "onVideoStarted");
        }
    };




    @Override
    protected void onDestroy() {
        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
            mYouTubePlayer = null;
        }
        super.onDestroy();
    }

}

package com.pinpinbox.android.Test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pinpinbox.android.R;

/**
 * Created by vmage on 2018/4/17.
 */

public class TestPageForYoutubeActivity extends Activity implements YouTubePlayer.OnInitializedListener {


    static private final String DEVELOPER_KEY = "AIzaSyATCeohA43aiTn-DkMI0ATpLJMiMWMDhdU";

    private YouTubePlayer mYouTubePlayer;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_pageforyoutube);
        YouTubePlayerView youTubeView = (YouTubePlayerView)
                findViewById(R.id.youtube_view);
        youTubeView.initialize(DEVELOPER_KEY, this);



        init();




    }

    private void init(){

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        int bannerWidth = ScreenUtils.getScreenWidth();

        int bannerHeight = (bannerWidth * 380) / 960;

        viewPager.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), bannerHeight));






    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

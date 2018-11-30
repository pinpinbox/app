package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

public class BannerSize {

    public static void set(View v, int marginDP){

        int bannerWidth = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(marginDP);

        int bannerHeight = (bannerWidth * 540) / 960;

        v.getLayoutParams().width = bannerWidth;

        v.getLayoutParams().height = bannerHeight;

        v.setLayoutParams(v.getLayoutParams());

    }

}

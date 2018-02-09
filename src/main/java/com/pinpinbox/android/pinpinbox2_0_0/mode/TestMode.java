package com.pinpinbox.android.pinpinbox2_0_0.mode;

import com.pinpinbox.android.BuildConfig;

/**
 * Created by vmage on 2016/2/4
 */
public class TestMode {

    public static String Domain = BuildConfig.initAPI;//正式機domain

    public static String domain(){

        String domain = "";

        domain = Domain;//正式機

        return domain;
    }

}

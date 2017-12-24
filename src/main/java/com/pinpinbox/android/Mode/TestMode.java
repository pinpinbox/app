package com.pinpinbox.android.Mode;

import com.pinpinbox.android.BuildConfig;

/**
 * Created by vmage on 2016/2/4
 */
public class TestMode {

    public static String Domain = BuildConfig.initAPI;//正式機domain

    public static String testW3domain = BuildConfig.initAPI;//測試機domain

//    public static String testW3domain = "http://platformvmage5.cloudapp.net/pinpinbox";

    //true 為測試模式
    public static boolean TESTMODE = true;

    public static String domain(){

        String domain = "";

//        if(TESTMODE){
//            domain = testW3domain;//測試模式
//        }else {
//            domain = Domain;//正式機
//        }

        domain = Domain;//正式機

        return domain;
    }

}

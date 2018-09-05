package com.pinpinbox.android.pinpinbox2_0_0.custom;

import com.google.android.gms.analytics.HitBuilders;

public class GAControl {

    public static void sendUserId(String user_id){

        PPBApplication.getInstance().getDefaultTracker().setClientId(user_id);

//        PPBApplication.getInstance().getDefaultTracker().set("&uid", user_id);

    }


    public static void sendViewName(String name){

        PPBApplication.getInstance().getDefaultTracker().setScreenName(name);
        PPBApplication.getInstance().getDefaultTracker().send(new HitBuilders.ScreenViewBuilder().build());

    }



}

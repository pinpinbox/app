package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import com.pinpinbox.android.SelfMadeClass.PPBApplication;

/**
 * Created by kevin9594 on 2017/3/11.
 */
public class StaggeredHeight {

    public static int setImageHeight(int getWidth, int getHeight){
        return getHeight* PPBApplication.getInstance().getStaggeredWidth()/getWidth;
    }



}

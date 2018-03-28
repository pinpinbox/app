package com.pinpinbox.android.pinpinbox2_0_0.custom;

/**
 * Created by vmage on 2015/3/11
 */
public class ClickUtils {



    private static long lastClickTime;


    public synchronized static boolean ButtonContinuousClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if(timeD<450){
            return true;
        }else {
            lastClickTime = time;
            return false;
        }

    }

    public synchronized static boolean ButtonContinuousClick_1s() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if(timeD<1000){
            return true;
        }else {
            lastClickTime = time;
            return false;
        }

    }


    public synchronized static boolean ButtonContinuousClick_4s() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if(timeD<4000){
            return true;
        }else {
            lastClickTime = time;
            return false;
        }
    }

    public synchronized static boolean ButtonContinuousClick_2s() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if(timeD<2000){
            return true;
        }else {
            lastClickTime = time;
            return false;
        }
    }

}

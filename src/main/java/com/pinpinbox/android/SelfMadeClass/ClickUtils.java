package com.pinpinbox.android.SelfMadeClass;

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
}

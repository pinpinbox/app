package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.pinpinbox2_0_0.mode.LOG;

/**
 * Created by kevin9594 on 2016/8/12.
 */
public class MyLog {

    public static void Set(String tagType, Class c, String message){

        if(LOG.isLogMode){

            switch (tagType){

                case "d":
                    Log.d(c.getSimpleName(), message);
                    break;

                case "e":
                    Log.e(c.getSimpleName(), message);
                    break;

                case "json":
                    Logger.json(message);
                    break;


            }
        }
    }

}

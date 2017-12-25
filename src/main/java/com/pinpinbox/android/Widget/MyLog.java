package com.pinpinbox.android.Widget;

import android.util.Log;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.Mode.LOG;

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

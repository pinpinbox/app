package com.pinpinbox.android.Utility;

import android.content.Context;
import android.os.SystemClock;

import com.flurry.android.FlurryAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2016/9/13.
 */
public class FlurryUtil {

    public static void onStartSession(Context context) {

            try {
                FlurryAgent.setLogEvents(true);
                FlurryAgent.onStartSession(context);
            } catch (Throwable t) {
            }

    }

    public static void onEndSession(Context context) {

            try {
                FlurryAgent.onEndSession(context);
            } catch (Throwable t) {
            }

    }

    public static void onEvent(String eventId) {

//        long currentTime = SystemClock.elapsedRealtime();

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date curDate   =   new Date(System.currentTimeMillis());//获取当前时间
        String   currentTime   =   df.format(curDate);


            try {
                Map<String, String> params = new HashMap<String, String>();
                params.put("time", currentTime);

                FlurryAgent.onEvent(eventId, params);
            } catch (Throwable t) {
            }

    }


    public static void onEventUseMap(String eventId, Map<String, String> map) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date curDate   =   new Date(System.currentTimeMillis());//获取当前时间
        String   currentTime   =   df.format(curDate);

            try {

                map.put("time", currentTime);

                FlurryAgent.onEvent(eventId, map);
            } catch (Throwable t) {
            }

    }


    public static void onEvent(String eventId, String paramValue) {

            try {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(eventId, paramValue);
                FlurryAgent.onEvent(eventId, params);
            } catch (Throwable t) {
            }

    }

    public static void onEvent(String eventId, String paramKey, String paramValue) {

            long currentTime = SystemClock.elapsedRealtime();
            try {
                Map<String, String> params = new HashMap<String, String>();
                params.put(paramKey, paramValue);
                FlurryAgent.onEvent(eventId, params);
            } catch (Throwable t) {
            }

    }



}

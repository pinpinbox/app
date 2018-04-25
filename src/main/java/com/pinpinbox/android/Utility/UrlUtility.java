package com.pinpinbox.android.Utility;

import android.content.Intent;

import java.util.HashMap;

/**
 * Created by vmage on 2018/4/25.
 */

public class UrlUtility {

    public static HashMap<String, String> UrlToMapGetValue(String url){
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String arg = url.substring(url.indexOf("?") + 1, url.length());
            String[] strs = arg.split("&");
            for (int x = 0; x < strs.length; x++) {
                String[] strs2 = strs[x].split("=");
                if (strs2.length == 2) {
                    System.out.println(strs2[0] + " , " + strs2[1]);
                    map.put(strs2[0], strs2[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static HashMap<String, String> UrlToMapGetValue(Intent intent){
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String url = intent.getDataString();
            String arg = url.substring(url.indexOf("?") + 1, url.length());
            String[] strs = arg.split("&");
            for (int x = 0; x < strs.length; x++) {
                String[] strs2 = strs[x].split("=");
                if (strs2.length == 2) {
                    System.out.println(strs2[0] + " , " + strs2[1]);
                    map.put(strs2[0], strs2[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}

package com.pinpinbox.android.Widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vmage on 2016/2/15.
 */
public class PPBWidget {

    public static String GetStringByJsonObject(JSONObject obj, String key){
        String strParam = "";
        try {
            if(obj!=null){
                    strParam = obj.getString(key);
                    if(strParam.equals("") || strParam.equals("null")){
                        strParam = "";
                    }
            }
        } catch (JSONException e) {
            strParam = "";
            e.printStackTrace();
        }
        return strParam;
    }


}

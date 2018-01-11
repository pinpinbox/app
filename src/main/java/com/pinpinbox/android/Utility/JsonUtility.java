package com.pinpinbox.android.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kevin9594 on 2017/1/15.
 */
public class JsonUtility {


    public static String GetString(JSONObject obj, String key) {
        String strParam = "";
        try {
            if (obj != null) {
                strParam = obj.getString(key);
                if (strParam == null || strParam.equals("") || strParam.equals("null")) {
                    strParam = "";
                }
            }
        } catch (JSONException e) {
            strParam = "";
            e.printStackTrace();
        }
        return strParam;
    }

    public static int GetInt(JSONObject obj, String key) {

        int intParam = -1;
        try {
            if (obj != null && !obj.getString(key).equals("null") && !obj.getString(key).equals("")) {
                intParam = obj.getInt(key);
            }
        } catch (Exception e) {
            intParam = -1;
            e.printStackTrace();
        }
        return intParam;

    }

    public static boolean GetBoolean(JSONObject obj, String key) {

        boolean b = false;

        try {
            if (obj != null) {
                b = obj.getBoolean(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;

    }

}

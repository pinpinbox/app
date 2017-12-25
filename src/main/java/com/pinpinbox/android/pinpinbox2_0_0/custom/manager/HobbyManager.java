package com.pinpinbox.android.pinpinbox2_0_0.custom.manager;

import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.Widget.Key;

/**
 * Created by vmage on 2017/11/3.
 */

public class HobbyManager {

    public static void SaveHobbyList(String jsonHobbyList){

        PPBApplication.getInstance().getData().edit().putString(Key.hobby, jsonHobbyList).commit();

    }


    public static String GetHobbyList(){

        return PPBApplication.getInstance().getData().getString(Key.hobby, "");

    }


}

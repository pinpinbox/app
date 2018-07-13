package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.app.Activity;

import com.pinpinbox.android.Utility.FileUtility;

/**
 * Created by vmage on 2017/6/7.
 */
public class CreateDir {

    public static void create(Activity mActivity, String id){

        FileUtility.createMyDir(mActivity, id);
        FileUtility.createCopyDir(mActivity, id);


    }


}

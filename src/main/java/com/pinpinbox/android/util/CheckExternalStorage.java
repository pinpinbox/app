package com.pinpinbox.android.util;

import android.os.Environment;

/**
 * Created by vmage on 2015/10/27.
 */
public class CheckExternalStorage {

    public static final int EXTERNAL_STORAGE_UNAVAILABLE = 1;


    /**
     * Check the external storage status
     *
     * @return
     */
    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}

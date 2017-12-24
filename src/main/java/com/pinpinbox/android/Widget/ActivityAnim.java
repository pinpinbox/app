package com.pinpinbox.android.Widget;

import android.app.Activity;

import com.pinpinbox.android.R;

/**
 * Created by vmage on 2016/6/29.
 */
public class ActivityAnim {

    public static void FinishAnim(Activity activity){


        activity.overridePendingTransition(R.anim.in_from_left_30, R.anim.out_to_right);

    }

    public static void StartAnim(Activity activity){

        activity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left_30);

    }


}

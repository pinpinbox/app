package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

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

    public static void StartAnimFromBottom(Activity activity){

        activity.overridePendingTransition(R.anim.bottom_enter, R.anim.view_stay);

    }

}

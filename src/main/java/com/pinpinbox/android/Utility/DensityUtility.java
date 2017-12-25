package com.pinpinbox.android.Utility;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.pinpinbox.android.SelfMadeClass.PPBApplication;

/**
 * Created by kevin9594 on 2015/11/22.
 */
public class DensityUtility {


    public static void setScreen(Activity activity) {

        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        PPBApplication.getInstance().setScreenWidth(outMetrics.widthPixels);
        PPBApplication.getInstance().setScreenHeight(outMetrics.heightPixels);


        if(SystemUtility.isTablet(activity.getApplicationContext())){

            //平版
            PPBApplication.getInstance().setStaggeredWidth(
                    (outMetrics.widthPixels - DensityUtility.dip2px(activity.getApplicationContext(), 64)) / 3
            );

        }else {

            //手機
            PPBApplication.getInstance().setStaggeredWidth(
                    (outMetrics.widthPixels - DensityUtility.dip2px(activity.getApplicationContext(), 48)) / 2
            );

        }


//        PPBApplication.getInstance().setStaggeredWidth(
//                (outMetrics.widthPixels - DensityUtility.dip2px(activity.getApplicationContext(), 48)) / 2
//        );

    }


    public static String screenW;
    public static String screenH;


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context,float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context,float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

}

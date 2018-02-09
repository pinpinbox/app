package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by vmage on 2017/12/5.
 */

public class StatusControl {

    private Activity mActivity;

    private SystemBarTintManager sysBarManager;

    private int currentActivityMode;

    public final static int LIGHT = 0;
    public final static int DARK = 1;

    public StatusControl(Activity mActivity, SystemBarTintManager sysBarManager) {
        this.mActivity = mActivity;
        this.sysBarManager = sysBarManager;

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
        setStatus();
//            }
//        });


    }

    private void setStatus() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            mActivity.getWindow().setStatusBarColor(Color.parseColor(ColorClass.ACTION_BAR));

        } else {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true, mActivity);
            }

            sysBarManager = new SystemBarTintManager(mActivity);
            sysBarManager.setStatusBarTintEnabled(true);
            sysBarManager.setStatusBarTintColor(Color.parseColor(ColorClass.STATUSBAR));

        }

        currentActivityMode = LIGHT;

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on, Activity mActivity) {
        Window win = mActivity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public void setStatusColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.getWindow().setStatusBarColor(Color.parseColor(ColorClass.ACTION_BAR));
        } else {
            sysBarManager.setStatusBarTintColor(Color.parseColor(ColorClass.STATUSBAR));
        }


    }

    public void setStatusColor(final int color) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            mActivity.getWindow().setStatusBarColor(color);

        } else {
            sysBarManager.setStatusBarTintColor(color);
        }


    }

    public void setStatusMode(final int mode) {


        switch (mode) {

            case LIGHT:


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    mActivity.getWindow().setStatusBarColor(Color.parseColor(ColorClass.ACTION_BAR));

                } else {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        setTranslucentStatus(true, mActivity);
                    }

                    sysBarManager = new SystemBarTintManager(mActivity);
                    sysBarManager.setStatusBarTintEnabled(true);
                    sysBarManager.setStatusBarTintColor(Color.parseColor(ColorClass.STATUSBAR));


                }


                break;

            case DARK:


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    mActivity.getWindow().setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));

                } else {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        setTranslucentStatus(true, mActivity);
                    }

                    sysBarManager = new SystemBarTintManager(mActivity);
                    sysBarManager.setStatusBarTintEnabled(true);
                    sysBarManager.setStatusBarTintColor(Color.parseColor(ColorClass.TRANSPARENT));


                }


                break;


        }


    }

    public void setCurrentActivityMode() {


        setStatusMode(currentActivityMode);

    }

    public void changeMode(int mode) {

        currentActivityMode = mode;

    }


}

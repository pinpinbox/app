package com.pinpinbox.android.SelfMadeClass;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;

import com.pinpinbox.android.R;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;


/**
 * Created by vmage on 2015/4/21
 */
public class LoadingAnimation {

    private Dialog mDialog;

    private static LoadingAnimation instance;


//    public static LoadingAnimation getInstance(Activity mActivity) {
//
//        if(instance==null){
//            return instance = new LoadingAnimation(mActivity);
//        }else {
//            return instance;
//        }
//    }


    public LoadingAnimation(Activity activity) {

        instance = this;

        mDialog = new Dialog(activity, R.style.myDialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            mDialog.getWindow().setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }

        mDialog.getWindow().setWindowAnimations(R.style.loading_show_dismiss);

        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_2_0_0_loading);

        View vCatchClose = mDialog.findViewById(R.id.vCatchClose);
        vCatchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.Set("d" , this.getClass(), "no close");
            }
        });


    }

    public void show() {

        try {

            if (!mDialog.isShowing()) {
                mDialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Dialog dialog() {
        return mDialog;
    }


}

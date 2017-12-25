package com.pinpinbox.android.SelfMadeClass;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.AVLoading.AVLoadingIndicatorView;

/**
 * Created by vmage on 2017/12/7.
 */

public class UploadingWithKeyBack {

        private Dialog dialog;


        private Animation loading_show_anim, loading_dismiss_anim;
    private RelativeLayout r;
    private AVLoadingIndicatorView avLoading;

    private PopupWindow popupWindow;
    private Activity mActivity;


    public UploadingWithKeyBack(Activity mActivity) {
        this.mActivity = mActivity;


        dialog = new Dialog(mActivity, R.style.myDialog);
        Window crosswin = dialog.getWindow();
        crosswin.setContentView(R.layout.dialog_2_0_0_loading);

        loading_show_anim= AnimationUtils.loadAnimation(mActivity.getApplicationContext(), R.anim.view_to_alpha1);//不做延遲顯示
        loading_dismiss_anim= AnimationUtils.loadAnimation(mActivity.getApplicationContext(), R.anim.view_to_alpha0);

        r = (RelativeLayout)dialog.findViewById(R.id.r);


    }

    public void show() {


        if(!dialog.isShowing()) {

            try {
                dialog.show();
                r.startAnimation(loading_show_anim);
            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    public void dismiss() {
        try {

            if (dialog != null && dialog.isShowing()) {
//                vRefresh.stopAnimation();
                r.startAnimation(loading_dismiss_anim);
                r.clearAnimation();
                dialog.dismiss();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }


    public Dialog getDialog() {
        return dialog;
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.popup;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

/**
 * Created by kevin9594 on 2017/1/7.
 */
public class PopupSelect {

    private Activity mActivity;

    private PopupWindow popupWindow;

    private RelativeLayout rBackground;

    private View vDarkBg;

    private View vPopup;

    private final static int intAnimDuration = 300;

    public PopupSelect(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setPopup(int viewId, int animStyle) {//pinpinbox_popupAnimation_bottom

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vPopup = inflater.inflate(viewId, null);
        popupWindow = new PopupWindow(vPopup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(animStyle);

        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        /*設置關閉時執行*/
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                ((DraggerActivity)mActivity).setCurrentActivityStatusMode();

                /*恢復背景*/
                ViewPropertyAnimator alphaTo0 = vDarkBg.animate();
                alphaTo0.setDuration(intAnimDuration)
                        .alpha(0)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                MyLog.Set("d", PopupSelect.class, "onAnimationEnd");

                                 /*移除模糊背景*/
                                rBackground.removeView(vDarkBg);
                                vDarkBg = null;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start();

                 /*移除模糊背景*/
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, intAnimDuration);


//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                    mActivity.getWindow().setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
//                }

            }
        });

    }

    public PopupWindow getPopupWindow() {
        return this.popupWindow;
    }

    public View getPopupView() {
        return this.vPopup;
    }

    public void show(RelativeLayout rBackground) {

        ((DraggerActivity)mActivity).setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));

        /*彈出時 設定background*/
        this.rBackground = rBackground;

        /*設定背景模糊*/
        setBlur(rBackground);

        /*彈出並顯示並模糊背景*/
        ViewPropertyAnimator alphaTo1 = vDarkBg.animate();
        alphaTo1.setDuration(intAnimDuration)
                .alpha(1)
                .start();
        popupWindow.showAtLocation(mActivity.getCurrentFocus(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    public void dismiss() {

        popupWindow.dismiss();
    }

    private void setBlur(RelativeLayout rBackground) {

        /*建立模糊視窗*/
        vDarkBg = new View(mActivity);
        vDarkBg.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        vDarkBg.setBackgroundColor(Color.parseColor(ColorClass.BLACK_ALPHA));

        /*先設置為透明*/
        vDarkBg.setAlpha(0);

        /*添加置background*/
        rBackground.addView(vDarkBg);


    }


}

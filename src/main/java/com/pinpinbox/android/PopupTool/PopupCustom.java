package com.pinpinbox.android.PopupTool;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Widget.MyLog;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

/**
 * Created by kevin9594 on 2017/1/7.
 */
public class PopupCustom {

    public interface DissmissWorks {
        void excute();
    }

    public DissmissWorks dissmissWorks() {
        return dissmissWorks;
    }

    public void setDissmissWorks(DissmissWorks dissmissWorks) {
        this.dissmissWorks = dissmissWorks;
    }

    private DissmissWorks dissmissWorks;


    private Activity mActivity;

    private PopupWindow popupWindow;

    private RelativeLayout rBackground;

    private BlurView blurView;

    private View vPopup;

    private final static int intAnimDuration = 150;

    public PopupCustom(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void resetBackground() {

        if (blurView != null) {

            ViewPropertyAnimator alphaTo0 = blurView.animate();
            alphaTo0.setDuration(intAnimDuration)
                    .alpha(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            MyLog.Set("d", PopupCustom.class, "onAnimationEnd");



                                 /*移除模糊背景*/
                            rBackground.removeView(blurView);
                            blurView = null;

                            if (dissmissWorks != null) {
                                dissmissWorks.excute();
                            }

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();

        }
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

                ((DraggerActivity) mActivity).setCurrentActivityStatusMode();

                //20171214
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mActivity.getWindow().setStatusBarColor(acStatusColor);
                }

                /*恢復背景*/
                resetBackground();

            }
        });


    }


    public PopupWindow getPopupWindow() {
        return this.popupWindow;
    }

    public View getPopupView() {
        return this.vPopup;
    }

    private int acStatusColor = 0;

    public void show(RelativeLayout rBackground) {


        //20171214

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            acStatusColor = mActivity.getWindow().getStatusBarColor();

        }

        ((DraggerActivity) mActivity).setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));


        /*彈出時 設定background*/
        this.rBackground = rBackground;

        /*設定背景模糊*/
        setBlur(rBackground);

        /*彈出並顯示並模糊背景*/
        ViewPropertyAnimator alphaTo1 = blurView.animate();
        alphaTo1.setDuration(intAnimDuration)
                .alpha(1)
                .start();
        popupWindow.showAtLocation(rBackground, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    public void dismiss() {

        popupWindow.dismiss();
    }

    private void setBlur(RelativeLayout rBackground) {

        /*建立模糊視窗*/
        blurView = new BlurView(mActivity);
        blurView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        blurView.setOverlayColor(Color.parseColor("#82000000"));

        final float radius = 4f;
        final View decorView = mActivity.getWindow().getDecorView();
        final View rootView = decorView.findViewById(android.R.id.content);
        final Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(mActivity, true)) //Preferable algorithm, needs RenderScript support mode enabled
                .blurRadius(radius);

        /*先設置為透明*/
        blurView.setAlpha(0);

        /*添加置background*/
        rBackground.addView(blurView);


    }


}

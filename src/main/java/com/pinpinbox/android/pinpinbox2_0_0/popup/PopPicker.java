package com.pinpinbox.android.pinpinbox2_0_0.popup;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ColorClass;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.PickerView;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

/**
 * Created by vmage on 2017/1/10.
 */
public class PopPicker {

    private Activity mActivity;

    private PopupWindow popupWindow;

    private RelativeLayout rBackground;

    private BlurView blurView;

    private View vPopup;

    private TextView tvTitle, tvConfirm;

    private PickerView pickerView;

    private String strSelect = "";

    private final static int intAnimDuration = 300;

    /*外部使用*/
    public PopupWindow getPopupWindow() {
        return this.popupWindow;
    }

    /*外部使用*/
    public View getPopupView() {
        return this.vPopup;
    }

    /*外部使用*/
    public TextView getTvConfirm() {
        return this.tvConfirm;
    }

    /*外部使用*/
    public String getStrSelect() {
        return this.strSelect;
    }

    /*外部使用*/
    public PickerView getPickerView() {
        return this.pickerView;
    }

    public PopPicker(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setPopup() {

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vPopup = inflater.inflate(R.layout.pop_2_0_0_picker, null);

        popupWindow = new PopupWindow(vPopup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.pinpinbox_popupAnimation_bottom);

        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        /*設置關閉時執行*/
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


                ((DraggerActivity)mActivity).setCurrentActivityStatusMode();

                /*恢復背景*/
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
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start();
            }
        });


        tvTitle = (TextView) vPopup.findViewById(R.id.tvTitle);
        tvConfirm = (TextView) vPopup.findViewById(R.id.tvConfirm);
        pickerView = (PickerView) vPopup.findViewById(R.id.picker);

        TextUtility.setBold(tvTitle, true);


        pickerView.setTextSize(DensityUtility.sp2px(mActivity.getApplicationContext(), 20));
        pickerView.setMarginAlpha(2.8f);
        pickerView.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int p) {
                strSelect = text;
            }
        });


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            mActivity.getWindow().setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
//        }

    }

    public void show(RelativeLayout rBackground) {

        ((DraggerActivity)mActivity).setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));

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

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitle(int title) {
        tvTitle.setText(title);
    }

    public void setPickerData(List<String> listData) {

        pickerView.setData(listData);

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

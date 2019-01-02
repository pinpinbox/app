package com.pinpinbox.android.pinpinbox2_0_0.popup;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.PickerView;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.List;

/**
 * Created by vmage on 2017/1/10.
 */
public class PopPicker {

    private Activity mActivity;

    private PopupWindow popupWindow;

    private RelativeLayout rBackground;

    private View vDarkBg;

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


                ((DraggerActivity) mActivity).setCurrentActivityStatusMode();

                //20171214
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mActivity.getWindow().setStatusBarColor(acStatusColor);
                }

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
                                MyLog.Set("d", PopupCustom.class, "onAnimationEnd");

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
            }
        });


        tvTitle = vPopup.findViewById(R.id.tvTitle);
        tvConfirm = vPopup.findViewById(R.id.tvConfirm);
        pickerView = vPopup.findViewById(R.id.picker);

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


    }


    private int acStatusColor = 0;

    public void show(RelativeLayout rBackground) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            acStatusColor = mActivity.getWindow().getStatusBarColor();
        }
        ((DraggerActivity) mActivity).setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));

        /*彈出時 設定background*/
        this.rBackground = rBackground;

        /*設定背景模糊*/
        setBlur(rBackground);

        /*彈出並顯示並模糊背景*/
        ViewPropertyAnimator alphaTo1 = vDarkBg.animate();
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

    public void clearReference(){
        mActivity = null;
        rBackground = null;
    }

}

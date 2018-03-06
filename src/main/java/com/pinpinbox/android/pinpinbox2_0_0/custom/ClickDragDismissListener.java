package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;

/**
 * Created by vmage on 2018/3/6.
 */

public class ClickDragDismissListener implements View.OnTouchListener {

    public interface ActionUpListener {
        void OnClick(View v);

        void OnDismiss();
    }

    private ActionUpListener actionUpListener;

    private float downY;
    private float moveY;

    private View vDrag;

    private static final int safeClickArea = 24;

    private boolean doDissmiss = false;

    public ClickDragDismissListener(View vDrag, ActionUpListener actionUpListener) {
        this.vDrag = vDrag;
        this.actionUpListener = actionUpListener;


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downY = event.getRawY();

                MyLog.Set("e", PopupCustom.class, "downY => " + downY);

                v.setPressed(true);

                break;
            case MotionEvent.ACTION_MOVE:

                moveY = event.getRawY();//手指所在位置

                MyLog.Set("e", PopupCustom.class, "moveY => " + moveY);


                //向下超過50 or 向上超過50 取消焦點
                if (moveY - downY >= safeClickArea || moveY - downY <= -safeClickArea) {
                    v.setPressed(false);
                }


                //判斷向下
                if (moveY >= downY) {


                    //判斷大於點擊安全距離
                    if (moveY - downY >= safeClickArea) {
                        vDrag.setTranslationY(moveY - downY - safeClickArea);
                    }


                    if (moveY - downY - safeClickArea > vDrag.getHeight() / 4) {
                        MyLog.Set("d", this.getClass(), "大於vDrag.getHeight()/4");
                        doDissmiss = true;
                    }else {
                        MyLog.Set("d", this.getClass(), "小於vDrag.getHeight()/4");
                        doDissmiss = false;
                    }


                }


                break;
            case MotionEvent.ACTION_UP:


                if (v.isPressed()) {

                    if (actionUpListener != null) {
                        actionUpListener.OnClick(v);
                    }

                    v.setPressed(false);

                } else {

                    vDrag.animate()
                            .translationY(0f)
                            .setDuration(200)
                            .start();

                    if (doDissmiss) {
                        if (actionUpListener != null) {
                            actionUpListener.OnDismiss();
                        }

                    }

                }


                break;
        }
        return true;
    }
}

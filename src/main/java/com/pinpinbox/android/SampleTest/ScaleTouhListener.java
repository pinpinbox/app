package com.pinpinbox.android.SampleTest;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.AnimationSuspensionTouch;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

/**
 * Created by kevin9594 on 2018/3/17.
 */

public class ScaleTouhListener implements View.OnTouchListener {


    public interface TouchCallBack {

        void Touch(View view);

        void Up(View view);
    }

    private TouchCallBack touchCallBack;

    private GestureDetector mGestureDetector;

    public ScaleTouhListener(TouchCallBack touchCallBack) {
        this.touchCallBack = touchCallBack;
    }

    private boolean click = false;

    private float downX = 0, downY = 0;
    private float moveX = 0, moveY = 0;

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (touchCallBack != null) {
            touchCallBack.Touch(v);
        }

//        mGestureDetector.onTouchEvent(event);

        float safeClickArea = 16f;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

//                downX = event.getRawX();
//                downY = event.getRawY();
//
//
//                MyLog.Set("e", this.getClass(), "downX => " + downX);
//                MyLog.Set("e", this.getClass(), "downY => " + downY);

                AnimationSuspensionTouch.pressed(v);

//                click = true;

                break;

            case MotionEvent.ACTION_MOVE:

                MyLog.Set("e", this.getClass(), "ACTION_MOVE");

//                moveX = event.getRawX();//手指所在位置
//                moveY = event.getRawY();//手指所在位置
//
//
////                MyLog.Set("e", this.getClass(), "moveX => " + moveX);
////                MyLog.Set("e", this.getClass(), "moveY => " + moveY);
//
//
//                float XX = moveX - downX;
//                float YY = moveY - downY;
//
////                MyLog.Set("e", this.getClass(), "XX => " + XX);
////                MyLog.Set("e", this.getClass(), "YY => " + YY);
//
//                if (YY >= safeClickArea || YY <= -safeClickArea || XX >= safeClickArea || XX <= -safeClickArea) {
//                    click = false;
//                    AnimationSuspensionTouch.reset(v);
//                } else {
//                    click = true;
//                }


                break;

            case MotionEvent.ACTION_UP:

                MyLog.Set("e", this.getClass(), "ACTION_UP");

                AnimationSuspensionTouch.reset(v, listener);


                break;

            case MotionEvent.ACTION_CANCEL:

                MyLog.Set("e", this.getClass(), "ACTION_CANCEL");

                AnimationSuspensionTouch.reset(v);

                break;

        }

        return true;
    }


    private Animator.AnimatorListener listener = new Animator.AnimatorListener() {


        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {

            if (touchCallBack != null) {
                touchCallBack.Up(null);
//                downX = 0;
//                downY = 0;
//                moveX = 0;
//                moveY = 0;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };


}

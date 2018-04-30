package com.pinpinbox.android.SampleTest;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.View;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

/**
 * Created by kevin9594 on 2018/3/17.
 */

public class ScaleTouhListener implements View.OnTouchListener {


    public interface TouchCallBack {

        void Touch();

        void Up();
    }

    private TouchCallBack touchCallBack;

    public ScaleTouhListener(TouchCallBack touchCallBack) {
        this.touchCallBack = touchCallBack;
    }


    private int duration = 70;

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
    @Override
    public boolean onTouch(View v, MotionEvent event) {


        if (touchCallBack != null){
            touchCallBack.Touch();
        }


        int safeClickArea = 24;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                downX = event.getRawX();
                downY = event.getRawY();


                MyLog.Set("e", this.getClass(), "downX => " + downX);
                MyLog.Set("e", this.getClass(), "downY => " + downY);

                v.animate().translationZ(0f)
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setListener(null)
                        .setDuration(duration)
                        .start();

                click = true;

                break;

            case MotionEvent.ACTION_MOVE:

                moveX = event.getRawX();//手指所在位置
                moveY = event.getRawY();//手指所在位置


                MyLog.Set("e", this.getClass(), "moveX => " + moveX);
                MyLog.Set("e", this.getClass(), "moveY => " + moveY);


                if (moveY - downY >= safeClickArea || moveY - downY <= -safeClickArea || moveX - downX >= safeClickArea || moveX - downX <= -safeClickArea) {
                    if (v.getScaleX() < 1f) {
                        v.animate().translationZ(v.getContext().getResources().getDimension(R.dimen.ppb200_translationZ_user))
                                .scaleX(1f)
                                .scaleY(1f)
                                .setListener(null)
                                .setDuration(duration)
                                .start();
                    }

                    click = false;

                } else {
                    click = true;

                }


                break;

            case MotionEvent.ACTION_UP:

                MyLog.Set("e", this.getClass(), "ACTION_UP");

                if (v.getScaleX() < 1f) {
                    v.animate().translationZ(v.getContext().getResources().getDimension(R.dimen.ppb200_translationZ_user))
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(duration)
                            .setListener(listener)
                            .start();
                }


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
            if (touchCallBack != null && click) {
                touchCallBack.Up();
                click = false;
                downX = 0;
                downY = 0;
                moveX = 0;
                moveY = 0;
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

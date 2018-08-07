package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.animation.Animator;
import android.view.View;

import com.pinpinbox.android.R;

public class AnimationSuspensionTouch {

    public static int duration = 70;


    public static void pressed(View view){

        if(view!=null){
            view.animate().translationZ(0f)
                    .scaleX(0.9f)
                    .scaleY(0.9f)
                    .setListener(null)
                    .setDuration(duration)
                    .start();
        }

    }

    public static void reset(View isOnTouchView){
        if (isOnTouchView!=null) {
            isOnTouchView.animate().translationZ(isOnTouchView.getContext().getResources().getDimension(R.dimen.ppb200_translationZ_default))
                    .scaleX(1f)
                    .scaleY(1f)
                    .setListener(null)
                    .setDuration(duration)
                    .start();

        }
    }

    public static void reset(View isOnTouchView, Animator.AnimatorListener listener){
        if (isOnTouchView!=null) {
            isOnTouchView.animate().translationZ(isOnTouchView.getContext().getResources().getDimension(R.dimen.ppb200_translationZ_default))
                    .scaleX(1f)
                    .scaleY(1f)
                    .setListener(listener)
                    .setDuration(duration)
                    .start();

        }
    }




}

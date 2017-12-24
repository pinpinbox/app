package com.pinpinbox.android.Widget;

import android.os.Handler;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;

import javax.xml.datatype.Duration;

/**
 * Created by kevin9594 on 2017/3/18.
 */
public class ViewVisibility {

    public static void setGone(final View v){

        ViewPropertyAnimator alphaTo0 = v.animate();
        alphaTo0.setDuration(200)
                .alpha(0)
                .start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               v.setVisibility(View.GONE);
            }
        },200);

    }

    public static void setVisible(View v){
        v.setVisibility(View.VISIBLE);
        ViewPropertyAnimator alphaTo1 = v.animate();
        alphaTo1.setDuration(200)
                .setStartDelay(200)
                .alpha(1)
                .start();
    }


}

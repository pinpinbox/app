package com.pinpinbox.android.pinpinbox2_0_0.custom.manager;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollectActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;

import java.util.List;

/**
 * Created by vmage on 2017/11/13.
 */

public class SnackManager {


    public static int bottombarHeight = 64;

    public static int snackbarHeight = 64;

    public static void showCollecttionSnack() {

        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();


        if (activityList.size() < 2) {

            return;

        }


        final Activity acLast = activityList.get(activityList.size() - 2); //-1為當前 -2為上一個


        /*view set*/
        final View v = LayoutInflater.from(acLast.getApplicationContext()).inflate(R.layout.snack_2_0_0_to_collection, null);
        final RelativeLayout rBackground = (RelativeLayout) v.findViewById(R.id.rBackground);

        /*view coordinate*/
        v.setY(ScreenUtils.getScreenHeight() - SizeUtils.dp2px(bottombarHeight) - SizeUtils.dp2px(snackbarHeight) - SizeUtils.dp2px(16));

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(), SizeUtils.dp2px(snackbarHeight));
        acLast.addContentView(v, p);
        v.setVisibility(View.VISIBLE);

        /*click*/
        rBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acLast.startActivity(new Intent(acLast, MyCollectActivity.class));
                ActivityAnim.StartAnim(acLast);
            }
        });


        /*animation*/
        snackAnim(v);


    }

    public static void showSettingsSnack(final Activity currentActivity, boolean hasBottomBar) {


        /*view set*/
        final View v = LayoutInflater.from(currentActivity.getApplicationContext()).inflate(R.layout.snack_2_0_0_to_set_video_autoplay, null);
        final RelativeLayout rBackground = (RelativeLayout) v.findViewById(R.id.rBackground);

         /*view coordinate*/
        if(hasBottomBar){
            v.setY(ScreenUtils.getScreenHeight() - SizeUtils.dp2px(bottombarHeight) - SizeUtils.dp2px(snackbarHeight) - SizeUtils.dp2px(16));
        }else {
            v.setY(ScreenUtils.getScreenHeight() - SizeUtils.dp2px(snackbarHeight) - SizeUtils.dp2px(16));
        }



        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(), SizeUtils.dp2px(snackbarHeight));
        currentActivity.addContentView(v, p);
        v.setVisibility(View.VISIBLE);

           /*click*/
        rBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityIntent.toSettings(currentActivity);
            }
        });

        snackAnim(v);


    }

    public static void snackAnim(final View v){

           /*animation*/
        ViewPropertyAnimator alphaTo1 = v.animate();
        alphaTo1.setStartDelay(500)
                .setDuration(200)
                .alpha(1f)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {


                        ViewPropertyAnimator alphaTo0 = v.animate();
                        alphaTo0.setStartDelay(3000)
                                .setDuration(200)
                                .alpha(0f)
                                .setListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        v.setVisibility(View.GONE);
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

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();

    }


}

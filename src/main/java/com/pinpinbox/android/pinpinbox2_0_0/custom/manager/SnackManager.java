package com.pinpinbox.android.pinpinbox2_0_0.custom.manager;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ColorClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollect2Activity;

import java.util.List;

/**
 * Created by vmage on 2017/11/13.
 */

public class SnackManager {


    public static int bottombarHeight = 64;

    public static int snackbarHeight = 64;



    public static void showCustomSnack(){

        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();


        if(activityList.size()<2){

            return;

        }


        final Activity acLast = activityList.get(activityList.size()-2); //-1為當前 -2為上一個


        /*view set*/
        final View v = LayoutInflater.from(acLast.getApplicationContext()).inflate(R.layout.snack_2_0_0_to_collection, null);
        final RelativeLayout rBackground = (RelativeLayout)v.findViewById(R.id.rBackground);

        /*view coordinate*/
        v.setY(ScreenUtils.getScreenHeight() - SizeUtils.dp2px(bottombarHeight) - SizeUtils.dp2px(snackbarHeight) - SizeUtils.dp2px(16));

        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ScreenUtils.getScreenWidth(), SizeUtils.dp2px(snackbarHeight));
        acLast.addContentView(v,p);
        v.setVisibility(View.VISIBLE);

        /*click*/
        rBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acLast.startActivity(new Intent(acLast, MyCollect2Activity.class));
                ActivityAnim.StartAnim(acLast);
            }
        });


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






    public static void showCollectionSnack(){


        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();


        final Activity acLast = activityList.get(activityList.size()-2); //-1為當前 -2為上一個

        final View decorView = acLast.getWindow().getDecorView();
        final View rootView = decorView.findViewById(android.R.id.content);


        Snackbar snackbar = Snackbar.make(rootView, R.string.pinpinbox_2_0_0_toast_message_save_album_done, 4000).setAction(R.string.pinpinbox_2_0_0_itemtype_work_manager, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acLast.startActivity(new Intent(acLast, MyCollect2Activity.class));
                ActivityAnim.StartAnim(acLast);
            }
        });



        View view = snackbar.getView();

        view.setBackgroundColor(Color.parseColor("#00acc1"));

        TextView tvMessage = (TextView) view.findViewById(R.id.snackbar_text);
        Button btAction = (Button) view.findViewById(R.id.snackbar_action);


        TextUtility.setBold(btAction, true);

        tvMessage.setTextColor(Color.parseColor(ColorClass.WHITE));
        btAction.setTextColor(Color.parseColor(ColorClass.WHITE));


        snackbar.show();



//        View v = LayoutInflater.from(acLast.getApplicationContext()).inflate(R.layout.list_item_2_0_0_collect, null);
//        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        SnackbarUtils
//                .with(rootView)
//                .setMessage(acLast.getResources().getString(R.string.pinpinbox_2_0_0_toast_message_save_album_done))
//                .setBgColor(Color.parseColor("#00acc1"))
//                .setDuration(4000);



    }


}

package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Login2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol95;

import java.util.List;

/**
 * Created by kevin9594 on 2017/7/8.
 */
public class IntentControl {

    public static void toLogin(final Activity currentActivity, final String user_id) {

        //call refresh token protocol

        new Protocol95(currentActivity, user_id, new Protocol95.TaskCallBack() {


            private LoadingAnimation loading;

            @Override
            public void Prepare() {

                PPBApplication.getInstance().getData().edit().remove(Key.token).commit();
                PPBApplication.getInstance().getData().edit().remove(Key.id).commit();

                loading = new LoadingAnimation(currentActivity);
                loading.show();

            }

            @Override
            public void Post() {
                loading.dismiss();
            }

            @Override
            public void Success() {

                Intent intent = new Intent(currentActivity, Login2Activity.class);
                currentActivity.startActivity(intent);
                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                for (int i = 0; i < activityList.size(); i++) {
                    activityList.get(i).finish();
                }
                currentActivity.overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

            }

            @Override
            public void TimeOut() {
                toLogin(currentActivity, user_id);
            }
        });

    }


    public static void toLoginAndEnableScan(final Activity currentActivity, final String user_id, final boolean enableScan) {

        //call refresh token protocol

        new Protocol95(currentActivity, user_id, new Protocol95.TaskCallBack() {


            private LoadingAnimation loading;

            @Override
            public void Prepare() {

                PPBApplication.getInstance().getData().edit().remove(Key.token).commit();
                PPBApplication.getInstance().getData().edit().remove(Key.id).commit();

                loading = new LoadingAnimation(currentActivity);
                loading.show();

            }

            @Override
            public void Post() {
                loading.dismiss();
            }

            @Override
            public void Success() {

                Intent intent = new Intent(currentActivity, Login2Activity.class);

                Bundle bundle = new Bundle();
                bundle.putBoolean(Key.scanIntent, enableScan);
                intent.putExtras(bundle);

                currentActivity.startActivity(intent);
                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                for (int i = 0; i < activityList.size(); i++) {
                    activityList.get(i).finish();
                }
                currentActivity.overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

            }

            @Override
            public void TimeOut() {
                toLoginAndEnableScan(currentActivity, user_id, enableScan);
            }
        });

    }


}

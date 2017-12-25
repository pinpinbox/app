package com.pinpinbox.android.pinpinbox2_0_0.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.pinpinbox.android.pinpinbox2_0_0.activity.FirstInstallActivity;

/**
 * Created by vmage on 2015/10/8.
 */
public class ExternalReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null){
            Bundle extras = intent.getExtras();

            if(!FirstInstallActivity.inBackground){
                MessageReceivingService.sendToApp(extras, context);
                Log.i("onReceive", "foreground");
            }
            else{


                try {
                    MessageReceivingService.saveToLog(extras, context);
                }catch (Exception e){
                    e.printStackTrace();
                }
//                Log.i("onReceive", "background");
            }
        }
    }
}

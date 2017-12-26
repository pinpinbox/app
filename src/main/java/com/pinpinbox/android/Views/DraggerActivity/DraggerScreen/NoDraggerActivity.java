package com.pinpinbox.android.Views.DraggerActivity.DraggerScreen;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StatusControl;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by kevin9594 on 2017/4/4.
 */
public class NoDraggerActivity extends FragmentActivity {

    private LoadingAnimation loading;
    private NoConnect noConnect;
    private StatusControl statusControl;

    public void setNoConnect(){
        if(noConnect==null) {
            noConnect = new NoConnect(this);
        }
    }

    public NoConnect getNoConnect(){
        return this.noConnect;
    }

    public void startLoading(){
        if(loading!=null) {
            loading.show();
        }
    }

    public void dissmissLoading(){
        if(loading!=null) {
            loading.dismiss();
        }
    }

    public LoadingAnimation getLoading(){
        return this.loading;
    }

    public void cancelTask(AsyncTask task){
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        statusControl = new StatusControl(this, new SystemBarTintManager(this));

        loading = new LoadingAnimation(this);
    }






}

package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Created by vmage on 2018/2/8.
 */

public class Protocol107 extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success();

        public abstract void TimeOut();
    }


    private Activity mActivity;

    private TaskCallBack callBack;

    private String user_id;
    private String token;

    private String result = "";
    private String message = "";
    private String response = "";



    @Override
    protected Object doInBackground(Void... voids) {
        return null;
    }




}

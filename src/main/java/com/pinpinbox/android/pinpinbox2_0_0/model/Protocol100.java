package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.DialogTool.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ConnectInstability;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Widget.IntentControl;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.PinPinToast;
import com.pinpinbox.android.Widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2017/10/13.
 */

public class Protocol100 extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success(String vote_left);

        public abstract void TimeOut();
    }

    private Activity mActivity;

    private TaskCallBack callBack;

    private String user_id;
    private String token;
    private String event_id;
    private String album_id;

    private String result = "";
    private String message = "";
    private String reponse = "";
    private String vote_left = "";


    public Protocol100(Activity mActivity, String user_id, String token, String event_id, String album_id, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;
        this.event_id = event_id;
        this.album_id = album_id;
        execute();
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        callBack.Prepare();
    }

    @Override
    protected Object doInBackground(Void... voids) {

        try {

            reponse = HttpUtility.uploadSubmit(true, Url.P100_Vote, putMap(), null);
            MyLog.Set("d", getClass(), "p100reponse => " + reponse);

        } catch (SocketTimeoutException t) {
            result = ResultType.TIMEOUT;
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (reponse != null && !reponse.equals("")) {

            try {
                JSONObject jsonObject = new JSONObject(reponse);
                result = JsonUtility.GetString(jsonObject, ProtocolKey.result);

                if(result.equals(ResultType.SYSTEM_OK)){

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    JSONObject jsonData = new JSONObject(data);

                    String event = JsonUtility.GetString(jsonData, ProtocolKey.event);

                    JSONObject jsonEvent = new JSONObject(event);

                    vote_left = JsonUtility.GetString(jsonEvent, ProtocolKey.vote_left);

                }else {
                    message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return null;
    }

    @Override
    public void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        callBack.Post();


        switch (result) {

            case ResultType.SYSTEM_OK:

                callBack.Success(vote_left);

                break;

            case ResultType.TOKEN_ERROR:

                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_token_error_to_login);

                IntentControl.toLogin(mActivity, user_id);

                break;

            case ResultType.USER_ERROR:

                DialogV2Custom.BuildError(mActivity, message);

                break;

            case ResultType.SYSTEM_ERROR:

                DialogV2Custom.BuildUnKnow(mActivity, mActivity.getClass().getSimpleName() + " => " + this.getClass() + " => " + ResultType.SYSTEM_ERROR);

                break;

            case ResultType.TIMEOUT:


                ConnectInstability connectInstability = new ConnectInstability() {
                    @Override
                    public void DoingAgain() {

                        callBack.TimeOut();
                    }
                };
                DialogV2Custom.BuildTimeOut(mActivity, connectInstability);


                break;

            case "":
                DialogV2Custom.BuildUnKnow(mActivity, mActivity.getClass().getSimpleName() + " => " + this.getClass().getSimpleName());
                break;

        }



    }


    private Map<String, String> putMap() {

        Map<String, String> map = new HashMap<>();
        map.put(Key.token, token);
        map.put(Key.album_id, album_id);
        map.put(Key.user_id, user_id);
        map.put(Key.event_id, event_id);

        return map;
    }


    public AsyncTask getTask() {
        return this;
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.IntentControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2018/2/8.
 */

public class Protocol109_InsertBookmark extends AsyncTask<Void, Void, Object> {


    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success();

        public abstract void TimeOut();
    }

    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;

    private TaskCallBack callBack;

    private String user_id;
    private String token;
    private String photo_id;


    private String result = "";
    private String message = "";
    private String response = "";


    public Protocol109_InsertBookmark(Activity mActivity, String user_id, String token, String photo_id, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;
        this.photo_id = photo_id;
        execute();
    }


    @Override
    public void onPreExecute() {
        super.onPreExecute();
        callBack.Prepare();
    }

    @Override
    public Object doInBackground(Void... voids) {

        try {

            response = HttpUtility.uploadSubmit(true, Url.P109_InsertBookMark, putMap(), null);
            MyLog.Set("d", getClass(), "p109reponse => " + response);

        } catch (SocketTimeoutException t) {
            result = ResultType.TIMEOUT;
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null && !response.equals("")) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                result = JsonUtility.GetString(jsonObject, ProtocolKey.result);
                if (!result.equals(ResultType.SYSTEM_OK)) {
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

                callBack.Success();

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

    @Override
    protected void onCancelled() {

        mActivity = null;

        super.onCancelled();
    }


    private Map<String, String> putMap() {

        Map<String, String> map = new HashMap<>();
        map.put(Key.photo_id, photo_id);
        map.put(Key.token, token);
        map.put(Key.user_id, user_id);

        return map;
    }


    public AsyncTask getTask() {
        return this;
    }



}

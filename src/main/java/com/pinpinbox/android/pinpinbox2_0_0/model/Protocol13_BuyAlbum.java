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
 * Created by vmage on 2017/7/12.
 */
public class Protocol13_BuyAlbum extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success();

        public abstract void IsOwnAlbum();

        public abstract void TimeOut();
    }


    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;

    private TaskCallBack callBack;

    private String user_id;
    private String token;
    private String album_id;
    private String platform;
    private String point;
    private String param;

    private String result = "";
    private String message = "";
    private String reponse = "";

    public Protocol13_BuyAlbum(Activity mActivity, String user_id, String token, String album_id, String platform, String point, String param, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;
        this.album_id = album_id;
        this.platform = platform;
        this.point = point;
        this.param = param;
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

            reponse = HttpUtility.uploadSubmit(true, Url.P13_BuyAlbum, putMap(), null);


            MyLog.Set("d", getClass(), "p13reponse => " + reponse);

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


    private Map<String, String> putMap() {

        Map<String, String> map = new HashMap<>();
        map.put(Key.album_id, album_id);
        map.put(Key.platform, platform);
        map.put(Key.token, token);
        map.put(Key.user_id, user_id);
        map.put(Key.point, point);

        if (!param.equals("")) {
            map.put(Key.param, param);
        }

        return map;
    }


    public AsyncTask getTask() {
        return this;
    }

    @Override
    protected void onCancelled() {

        mActivity = null;

        super.onCancelled();
    }
}

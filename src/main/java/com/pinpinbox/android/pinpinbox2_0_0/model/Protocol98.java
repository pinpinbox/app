package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.pinpinbox.android.DialogTool.DialogV2Custom;
import com.pinpinbox.android.SelfMadeClass.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2017/9/25.
 */
public class Protocol98 extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success();

        public abstract void UserExists();

        public abstract void TimeOut();

        public abstract void DoInBackground();


    }

    private Activity mActivity;

    private SharedPreferences getData;


    private TaskCallBack callBack;

    private String businessuser_id;
    private String facebook_id;
    private String timestamp;
    private String param;

    private String result = "";
    private String message = "";
    private String reponse = "";

    public String getResult() {
        return this.result;
    }

    public Protocol98(Activity mActivity, String businessuser_id, String facebook_id, String timestamp, String param, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.businessuser_id = businessuser_id;
        this.facebook_id = facebook_id;
        this.timestamp = timestamp;
        this.param = param;

        getData = PPBApplication.getInstance().getData();

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

            reponse = HttpUtility.uploadSubmit(true, Url.P98_BusinessRegister, putMap(), null);
            MyLog.Set("d", getClass(), "p98reponse => " + reponse);

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

                if (result.equals(ResultType.SYSTEM_OK) || result.equals(ResultType.USER_EXISTS)) {

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    JSONObject dataObject = new JSONObject(data);

                    String token = JsonUtility.GetString(dataObject, ProtocolKey.token);

                    JSONObject tokenObject = new JSONObject(token);

                    String realToken = JsonUtility.GetString(tokenObject, ProtocolKey.token);

                    String user_id = JsonUtility.GetString(tokenObject, ProtocolKey.user_id);

                    MyLog.Set("e", this.getClass(), "user_id => " + user_id);
                    MyLog.Set("e", this.getClass(), "realToken => " + realToken);


                    SharedPreferences.Editor editor = getData.edit();
                    editor.putString(Key.token, realToken);
                    editor.putString(Key.id, user_id);
                    editor.commit();

                    callBack.DoInBackground();


                } else {
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

            case ResultType.USER_EXISTS:
//                callBack.Success();
                callBack.UserExists();
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
        map.put(Key.businessuser_id, businessuser_id);
        map.put(Key.facebook_id, facebook_id);
        map.put(Key.timestamp, timestamp);
        String sign = IndexSheet.encodePPB(map);
        map.put(Key.sign, sign);
        map.put(Key.param, param);


        return map;
    }

    public AsyncTask getTask() {
        return this;
    }
}

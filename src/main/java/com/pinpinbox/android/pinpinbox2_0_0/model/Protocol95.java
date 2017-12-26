package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2017/7/6.
 */
public class Protocol95 extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success();

        public abstract void TimeOut();
    }


    private Activity mActivity;

    private TaskCallBack callBack;

    private String user_id;

    private String result = "";
    private String message = "";
    private String reponse = "";
    private String token = "";

    public Protocol95(Activity mActivity, String user_id, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
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

            reponse = HttpUtility.uploadSubmit(true, Url.P95_RefreshToken, putMap(), null);
            MyLog.Set("d", getClass(), "p95reponse => " + reponse);

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

                    String jsonToken = JsonUtility.GetString(jsonData, ProtocolKey.token);

                    JSONObject jsonT = new JSONObject(jsonToken);

                    token = JsonUtility.GetString(jsonT, ProtocolKey.token);


                }

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

                PPBApplication.getInstance().getData().edit().putString(Key.token, token).commit();
                PPBApplication.getInstance().getData().edit().putBoolean(Key.tokenUpDated, true).commit();

                callBack.Success();

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
        map.put(Key.user_id, user_id);

        String sign = IndexSheet.encodePPB(map);
        map.put(Key.sign, sign);



        return map;
    }


    public AsyncTask getTask() {
        return this;
    }


}

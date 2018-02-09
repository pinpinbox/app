package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.IntentControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2018/2/8.
 */

public class Protocol108 extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success(ItemExchange itemExchange);

        public abstract void IsEnd();

        public abstract void TimeOut();
    }

    private Activity mActivity;
    private TaskCallBack callBack;
    private String user_id, token, photo_id;

    private String result = "";
    private String message = "";
    private String response = "";

    private ItemExchange itemExchange;


    public Protocol108(Activity mActivity, String user_id, String token, String photo_id, TaskCallBack callBack) {
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
    protected Object doInBackground(Void... voids) {

        try {

            response = HttpUtility.uploadSubmit(true, Url.P108_GetPhotoUseFor, putMap(), null);

            MyLog.Set("d", getClass(), "p108reponse => " + response);


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

                if (result.equals(ResultType.SYSTEM_OK)) {

                    itemExchange = new ItemExchange();

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    JSONObject jsonData = new JSONObject(data);

                    String photousefor = JsonUtility.GetString(jsonData, ProtocolKey.photousefor);

                    JSONObject jsonPhotoUseFor = new JSONObject(photousefor);

                    itemExchange.setDescription(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.description));
                    itemExchange.setImage(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.image));
                    itemExchange.setName(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.name));
                    itemExchange.setPhotousefor_id(JsonUtility.GetInt(jsonPhotoUseFor, ProtocolKey.photousefor_id));

                    String bookmark = JsonUtility.GetString(jsonData, ProtocolKey.bookmark);

                    JSONObject jsonBookMark = new JSONObject(bookmark);
                    itemExchange.setIs_existing(JsonUtility.GetBoolean(jsonBookMark, ProtocolKey.is_existing));

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

                callBack.Success(itemExchange);

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

            case "":
                DialogV2Custom.BuildUnKnow(mActivity, mActivity.getClass().getSimpleName() + " => " + this.getClass().getSimpleName());
                break;

            case ResultType.PHOTOUSEFOR_USER_HAS_EXCHANGED:

               callBack.IsEnd();

                break;

//            case 2:
//
//                callBack.isAlreadyGet(itemExchange);
//
//                break;
//
//            case 3:
//
//                callBack.isEnd();
//
//                break;



            case ResultType.TIMEOUT:

                callBack.TimeOut();

                break;


        }


    }


    private Map<String, String> putMap() {

        Map<String, String> map = new HashMap<>();
        map.put(Key.token, token);
        map.put(Key.user_id, user_id);
        map.put(Key.photo_id, photo_id);

        return map;
    }


    public AsyncTask getTask() {
        return this;
    }



}

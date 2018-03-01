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
 * Created by vmage on 2018/3/1.
 */

public class Protocol111 extends AsyncTask<Void, Void, Object> {



    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success(ItemExchange itemExchange);

        public abstract void isBelongUser(ItemExchange itemExchange);

        public abstract void IsGained(ItemExchange itemExchange);

        public abstract void IsSlot(ItemExchange itemExchange);

        public abstract void TimeOut();
    }

    private Activity mActivity;
    private TaskCallBack callBack;
    private String response;
    private String user_id, token, photo_id, identifier;
    private String message;
    private String result = "";

    private static final String TIMEOUT = "timeout";

    private ItemExchange itemExchange;

    public Protocol111(Activity mActivity, String user_id, String token, String photo_id, String identifier, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;
        this.photo_id = photo_id;
        this.identifier = identifier;

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

            response = HttpUtility.uploadSubmit(true, Url.P111_SlotPhotoUseFor, putMap(), null);

            MyLog.Set("d", getClass(), "p111response => " + response);


        } catch (SocketTimeoutException t) {
            result = TIMEOUT;
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null && !response.equals("")) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                result = JsonUtility.GetString(jsonObject, ProtocolKey.result);

                if (result.equals(ResultType.SYSTEM_OK) ||
                        result.equals(ResultType.PHOTOUSEFOR_USER_HAS_GAINED) ||
                        result.equals(ResultType.PHOTOUSEFOR_USER_HAS_EXCHANGED) ||
                        result.equals(ResultType.PHOTOUSEFOR_USER_HAS_SLOTTED)) {

                    itemExchange = new ItemExchange();

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONObject jsonData = new JSONObject(data);

                    /*獎項是否在清單*/
                    String bookmark = JsonUtility.GetString(jsonData, ProtocolKey.bookmark);
                    JSONObject jsonBookMark = new JSONObject(bookmark);

                    itemExchange.setIs_existing(JsonUtility.GetBoolean(jsonBookMark, ProtocolKey.is_existing));



                    /*獎項資訊*/
                    String photousefor = JsonUtility.GetString(jsonData, ProtocolKey.photousefor);
                    JSONObject jsonPhotoUseFor = new JSONObject(photousefor);

                    itemExchange.setDescription(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.description));
                    itemExchange.setImage(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.image));
                    itemExchange.setName(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.name));
                    itemExchange.setPhotousefor_id(JsonUtility.GetInt(jsonPhotoUseFor, ProtocolKey.photousefor_id));



                    /*for user id*/
                    String photousefor_user = JsonUtility.GetString(jsonData, ProtocolKey.photousefor_user);
                    JSONObject jsonPhotoUseForUser = new JSONObject(photousefor_user);

                    itemExchange.setPhotousefor_user_id(JsonUtility.GetInt(jsonPhotoUseForUser, ProtocolKey.photousefor_user_id));


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

                callBack.isBelongUser(itemExchange);

                break;

            case ResultType.PHOTOUSEFOR_USER_HAS_GAINED:

                callBack.IsGained(itemExchange);

                break;

            case ResultType.PHOTOUSEFOR_USER_HAS_SLOTTED:

                callBack.IsSlot(itemExchange);

                break;


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
        map.put(Key.identifier, identifier);

        return map;
    }


    public AsyncTask getTask() {
        return this;
    }




}

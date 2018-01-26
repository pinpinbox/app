package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.app.Activity;
import android.os.AsyncTask;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2018/1/26.
 */

public class Protocol42 extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success(ItemExchange itemExchange);

        public abstract void isAlreadyGet(ItemExchange itemExchange);

        public abstract void isFinish();

        public abstract void TimeOut();
    }


    private Activity mActivity;
    private TaskCallBack callBack;
    private String response;
    private String user_id, token, photo_id, identifier;
    private String message;

    private static final int TIMEOUT = -2;
    private int result = -1;

    private ItemExchange itemExchange;


    public Protocol42(Activity mActivity, String user_id, String token, String photo_id, String identifier, TaskCallBack callBack) {
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

            response = HttpUtility.uploadSubmit(true, Url.P42_GetPhotoUseFor_User, putMap(), null);

            Logger.json(response);


        } catch (SocketTimeoutException t) {
            result = TIMEOUT;
            t.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (response != null && !response.equals("")) {

            try {
                JSONObject jsonObject = new JSONObject(response);
                result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                if (result == 1 || result == 2) {

                    itemExchange = new ItemExchange();

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    JSONObject jsonData = new JSONObject(data);

                    String photousefor = JsonUtility.GetString(jsonData, ProtocolKey.photousefor);
                    String photousefor_user = JsonUtility.GetString(jsonData, ProtocolKey.photousefor_user);

                    JSONObject jsonPhotoUseFor = new JSONObject(photousefor);
//                    String description = JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.description);
//                    String image  = JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.image);
//                    String name  = JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.name);
//                    String photousefor_id = JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.photousefor_id);

                    JSONObject jsonPhotoUseForUser = new JSONObject(photousefor_user);
//                    String photousefor_user_id = JsonUtility.GetString(jsonPhotoUseForUser, ProtocolKey.photousefor_user_id );

                    itemExchange.setDescription(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.description));
                    itemExchange.setImage(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.image));
                    itemExchange.setName(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.name));
                    itemExchange.setPhotousefor_id(JsonUtility.GetInt(jsonPhotoUseFor, ProtocolKey.photousefor_id));
                    itemExchange.setPhotousefor_user_id(JsonUtility.GetInt(jsonPhotoUseForUser, ProtocolKey.photousefor_user_id));

                } else if (result == 3) {


                } else if (result == 0) {
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

            case 1:

                callBack.Success(itemExchange);

                break;

            case 2:

                callBack.isAlreadyGet(itemExchange);

                break;

            case 3:

                callBack.isFinish();

                break;

            case 0:

                DialogV2Custom.BuildError(mActivity, message);

                break;

            case TIMEOUT:

                ConnectInstability connectInstability = new ConnectInstability() {
                    @Override
                    public void DoingAgain() {
                        callBack.TimeOut();
                    }
                };
                DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

                break;

            case -1:
                DialogV2Custom.BuildUnKnow(mActivity, mActivity.getClass().getSimpleName() + " => " + this.getClass().getSimpleName());
                break;


        }


    }

    private Map<String, String> putMap() {

        Map<String, String> map = new HashMap<>();
        map.put(Key.token, token);
        map.put(Key.id, user_id);
        map.put(Key.photo_id, photo_id);
        map.put(Key.identifier, identifier);
        String sign = IndexSheet.encodePPB(map);

        Map<String, String> sendMap = new HashMap<>();
        sendMap.put(Key.token, token);
        sendMap.put(Key.id, user_id);
        sendMap.put(Key.photo_id, photo_id);
        sendMap.put(Key.identifier, identifier);
        sendMap.put(Key.sign, sign);


        return sendMap;
    }


    public AsyncTask getTask() {
        return this;
    }


}





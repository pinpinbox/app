package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.IntentControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Protocol34_GetAlbumSettings extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success(ItemAlbum itemAlbum);

        public abstract void TimeOut();
    }

    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;

    private TaskCallBack callBack;

    private String user_id;
    private String token;
    private String album_id;


    private String result = "";
    private String message = "";
    private String reponse = "";

    private ItemAlbum itemAlbum;


    public Protocol34_GetAlbumSettings(Activity mActivity, String user_id, String token, String album_id, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;
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

            reponse = HttpUtility.uploadSubmit(true, Url.P34_GetAlbumSettings, putMap(), null);
            MyLog.Set("d", getClass(), "p34reponse => " + reponse);

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

                if (result.equals(ResultType.SYSTEM_OK)) {


                    String jsonData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONObject object = new JSONObject(jsonData);

                    itemAlbum = new ItemAlbum();

                    itemAlbum.setAudio_mode(JsonUtility.GetString(object, ProtocolKey.album_audio_mode));
                    itemAlbum.setAudio_refer(JsonUtility.GetString(object, ProtocolKey.album_audio_refer));
                    itemAlbum.setAudio_target(JsonUtility.GetString(object, ProtocolKey.album_audio_target));

                    itemAlbum.setName(JsonUtility.GetString(object, ProtocolKey.name));
                    itemAlbum.setDescription(JsonUtility.GetString(object, ProtocolKey.description));
                    itemAlbum.setLocation(JsonUtility.GetString(object, ProtocolKey.location));
                    itemAlbum.setWeather(JsonUtility.GetString(object, ProtocolKey.weather));
                    itemAlbum.setMood(JsonUtility.GetString(object, ProtocolKey.mood));
                    itemAlbum.setAct(JsonUtility.GetString(object, ProtocolKey.act));

                    itemAlbum.setCategoryarea_id(JsonUtility.GetInt(object, ProtocolKey.categoryarea_id));
                    itemAlbum.setCategory_id(JsonUtility.GetInt(object, ProtocolKey.category_id));

                    String albumindex = JsonUtility.GetString(object, ProtocolKey.albumindex);

                    JSONArray array = new JSONArray(albumindex);

                    List<String> albumindexList = new ArrayList<>();

                    if (array.length() > 0) {
                        for (int i = 0; i < array.length(); i++) {
                            albumindexList.add(array.getString(i));
                        }
                    }
                    itemAlbum.setAlbumindexList(albumindexList);

                    itemAlbum.setPoint(JsonUtility.GetInt(object, ProtocolKey.point));

                    itemAlbum.setReward_description(JsonUtility.GetString(object, ProtocolKey.reward_description));
                    itemAlbum.setReward_after_collect(JsonUtility.GetBoolean(object, ProtocolKey.reward_after_collect));
                    itemAlbum.setDisplay_num_of_collect(JsonUtility.GetBoolean(object, ProtocolKey.display_num_of_collect));


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

                callBack.Success(itemAlbum);

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
        map.put(Key.album_id, album_id);
        map.put(Key.token, token);
        map.put(Key.user_id, user_id);

        return map;
    }

    public AsyncTask getTask() {
        return this;
    }


}

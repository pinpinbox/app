package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
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

/**
 * Created by vmage on 2018/2/8.
 */

public class Protocol107_GetBookmarkList extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success(List<ItemExchange> itemExchangeList);

        public abstract void TimeOut();
    }


    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;

    private TaskCallBack callBack;

    private String user_id;
    private String token;

    private String result = "";
    private String message = "";
    private String response = "";

    private List<ItemExchange> itemExchangeList;


    public Protocol107_GetBookmarkList(Activity mActivity, String user_id, String token, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;

        itemExchangeList = new ArrayList<>();

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

            response = HttpUtility.uploadSubmit(true, Url.P107_GetBookmarkList, putMap(), null);

            MyLog.Set("d", getClass(), "p107reponse => " + response);

            Logger.json(response);


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


                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);


                    JSONArray dataArray = new JSONArray(data);

                    /*get time*/
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//                    String strCurrentTime = df.format(curDate);


                    for (int i = 0; i < dataArray.length(); i++) {

                        ItemExchange itemExchange = new ItemExchange();

                        JSONObject object = (JSONObject) dataArray.get(i);

                        String photo = JsonUtility.GetString(object, ProtocolKey.photo);
                        String photousefor = JsonUtility.GetString(object, ProtocolKey.photousefor);
                        String photousefor_user = JsonUtility.GetString(object, ProtocolKey.photousefor_user);

                        JSONObject jsonPhoto = new JSONObject(photo);
                        JSONObject jsonPhotoUseFor = new JSONObject(photousefor);


                        itemExchange.setHas_gained(JsonUtility.GetBoolean(jsonPhoto, ProtocolKey.has_gained));
                        itemExchange.setPhoto_id(JsonUtility.GetInt(jsonPhoto, ProtocolKey.photo_id));

                        itemExchange.setDescription(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.description));
                        itemExchange.setImage(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.image));
                        itemExchange.setName(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.name));
                        itemExchange.setPhotousefor_id(JsonUtility.GetInt(jsonPhotoUseFor, ProtocolKey.photousefor_id));
                        itemExchange.setStarttime(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.starttime));
                        itemExchange.setEndtime(JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.endtime));

                        if(photousefor_user!=null && !photousefor_user.equals("") && !photousefor_user.equals("null")){
                            JSONObject jsonPhotoUseForUser = new JSONObject(photousefor_user);
                            itemExchange.setPhotousefor_user_id(JsonUtility.GetInt(jsonPhotoUseForUser, ProtocolKey.photousefor_user_id));
                        }else {
                            itemExchange.setPhotousefor_user_id(-1);
                        }



//                        String time = "";

                        /*get end time*/
//                        String strEndTime = JsonUtility.GetString(jsonPhotoUseFor, ProtocolKey.endtime);
//
//                        if (strEndTime != null && !strEndTime.equals("") && !strEndTime.equals("null")) {
//                            Date currentTime = df.parse(strCurrentTime);
//                            Date endTime = df.parse(strEndTime);
//                            long l = endTime.getTime() - currentTime.getTime();
//                            long day = l / (24 * 60 * 60 * 1000);
//                            long hour = (l / (60 * 60 * 1000) - day * 24);
//                            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
//
//                            if (l > 0) {
//                                time = "" + day + "天" + hour + "小時" + min + "分";
//                            }
//                        }

//                        itemExchange.setTime(time);


                        itemExchange.setImageWidth(PPBApplication.getInstance().getStaggeredWidth());
                        itemExchange.setImageHeight(PPBApplication.getInstance().getStaggeredWidth());
                        itemExchange.setIs_existing(true); //該接口為列表 必為true


                        itemExchangeList.add(itemExchange);

                    }


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

                callBack.Success(itemExchangeList);

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


            case ResultType.TIMEOUT:


                ConnectInstability connectInstability = new ConnectInstability() {
                    @Override
                    public void DoingAgain() {

                        callBack.TimeOut();
                    }
                };
                DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

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
        map.put(Key.token, token);
        map.put(Key.user_id, user_id);

        return map;
    }


    public AsyncTask getTask() {
        return this;
    }


}

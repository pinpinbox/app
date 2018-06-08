package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vmage on 2018/1/3.
 */

public class Protocol104_GetSponsorList {


    public static abstract class TaskCallBack {

        public abstract void Prepare(int doingType);

        public abstract void Post(int doingType);

        public abstract void Success(int doingType);

        public abstract void TimeOut(int doingType);
    }


    private Activity mActivity;
    private Call callTask;
    private TaskCallBack callBack;


    private List<ItemUser> itemUserList;
    private String user_id;
    private String token;
    private String result = "";
    private String message = "";
    private String reponse = "";

    private int doingType;
    private int round, rangeCount;
    private int userCount = 0;

    private boolean sizeMax = false;
    private boolean noDataToastAppeared = false;


    public Protocol104_GetSponsorList(Activity mActivity, String user_id, String token, List<ItemUser> itemUserList, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;

        this.itemUserList = itemUserList;

        round = 0;

        rangeCount = 16;

    }


    public void GetList() {
        doingType = DoingTypeClass.DoDefault;
        callTask = new Call();
        callTask.execute();
    }

    public void Refresh() {

        doingType = DoingTypeClass.DoRefresh;


        callTask = new Call();
        callTask.execute();
    }

    public void LoadMore() {
        doingType = DoingTypeClass.DoMoreData;
        callTask = new Call();
        callTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class Call extends AsyncTask<Void, Void, Object> {


        @Override
        public void onPreExecute() {
            super.onPreExecute();

            if (doingType == DoingTypeClass.DoRefresh) {
                round = 0;
                sizeMax = false;
                noDataToastAppeared = false;
            }


            callBack.Prepare(doingType);
        }

        @Override
        public Object doInBackground(Void... voids) {

            try {

                reponse = HttpUtility.uploadSubmit(true, Url.P104_GetSponsorList, putMap(), null);
                MyLog.Set("d", getClass(), "p104reponse => " + reponse);

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


                        String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONArray jsonArrayData = new JSONArray(data);

                        userCount = jsonArrayData.length();

                        for (int i = 0; i < userCount; i++) {

                            JSONObject object = (JSONObject) jsonArrayData.get(i);

                            String user = JsonUtility.GetString(object, ProtocolKey.user);

                            JSONObject jsonUser = new JSONObject(user);

                            ItemUser itemUser = new ItemUser();

                            itemUser.setFollow(JsonUtility.GetBoolean(jsonUser, ProtocolKey.is_follow));
                            itemUser.setName(JsonUtility.GetString(jsonUser, ProtocolKey.user_name));
                            itemUser.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                            itemUser.setUser_id(JsonUtility.GetString(jsonUser, ProtocolKey.user_id));
                            itemUser.setPoint(JsonUtility.GetInt(jsonUser, ProtocolKey.point));
                            itemUser.setDisscuss(JsonUtility.GetBoolean(jsonUser, ProtocolKey.discuss));

                            itemUserList.add(itemUser);

                        }


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

            callBack.Post(doingType);

            switch (result) {

                case ResultType.SYSTEM_OK:

                    if (userCount == 0) {
                        sizeMax = true;
                        return;
                    } else {

                        callBack.Success(doingType);

                        if(userCount<rangeCount){
                            MyLog.Set("d", Protocol104_GetSponsorList.class, "項目少於" + rangeCount);
                            sizeMax = true;
                            return;
                        }

                        round = round + rangeCount;
                    }


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

                            callBack.TimeOut(doingType);
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

    }

    private Map<String, String> putMap() {

        Map<String, String> map = new HashMap<>();
        map.put(Key.limit, round + "," + rangeCount);
        map.put(Key.token, token);
        map.put(Key.user_id, user_id);

        return map;
    }

    public AsyncTask getTask() {
        return callTask;
    }


    public List<ItemUser> getItemUserList() {
        return this.itemUserList;
    }


    public int getDoingType() {
        return this.doingType;
    }

    public boolean isSizeMax() {
        return this.sizeMax;
    }

    public boolean isNoDataToastAppeared() {
        return this.noDataToastAppeared;
    }

    public void setNoDataToastAppeared(boolean noDataToastAppeared) {
        this.noDataToastAppeared = noDataToastAppeared;
    }

    public int getRangeCount() {
        return this.rangeCount;
    }


}

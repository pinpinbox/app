package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.R;
import com.pinpinbox.android.StringClass.DoingTypeClass;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.IntentControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
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
 * Created by vmage on 2017/10/12.
 */

public class Protocol99 {


    public static abstract class TaskCallBack {

        public abstract void Prepare(int doingType);

        public abstract void Post(int doingType);

        public abstract void Success(int doingType);

        public abstract void TimeOut(int doingType);
    }


    private Activity mActivity;
    private Call callTask;
    private TaskCallBack callBack;

    private List<ItemAlbum> itemAlbumList;

    private String user_id;
    private String token;
    private String event_id;
    private String vote_left;

    private String result = "";
    private String message = "";
    private String reponse = "";
    private String searchKey = "";

    private int minHeight;
    private int round, rangeCount;
    private int eventJoinCount;
    private int doingType;

    private boolean sizeMax = false;
    private boolean noDataToastAppeared = false;


    public Protocol99(Activity mActivity, String user_id, String token, String event_id, String searchKey, List<ItemAlbum> itemAlbumList, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;
        this.event_id = event_id;
        this.searchKey = searchKey;
        this.itemAlbumList = itemAlbumList;

        minHeight = DensityUtility.dip2px(mActivity.getApplicationContext(), 72);

        round = 0;

        rangeCount = 16;

    }


    public void GetList() {
        doingType = DoingTypeClass.DoDefault;
        MyLog.Set("e", getClass(), "GetList()");
        callTask = new Call();
        callTask.execute();
    }

    public void Refresh() {

        doingType = DoingTypeClass.DoRefresh;

        MyLog.Set("e", getClass(), "Refresh()");

        callTask = new Call();
        callTask.execute();
    }

    public void LoadMore() {
        doingType = DoingTypeClass.DoMoreData;
        MyLog.Set("e", getClass(), "LoadMore()");
        callTask = new Call();
        callTask.execute();
    }

    public void setSearchKey(String searchKey){
        this.searchKey = searchKey;
    }

//    public void Search(String searchKey){
//        doingType = DoingTypeClass.DoSearch;
//        MyLog.Set("e", getClass(), "Search()");
//        this.searchKey = searchKey;
//        callTask = new Call();
//        callTask.execute();
//
//    }

    private class Call extends AsyncTask<Void, Void, Object> {


        public Call(){}


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

                reponse = HttpUtility.uploadSubmit(true, Url.P99_GetEventVoteList, putMap(), null);
                MyLog.Set("d", getClass(), "p99reponse => " + reponse);

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

                        JSONObject jsonData = new JSONObject(data);

                        String event = JsonUtility.GetString(jsonData, ProtocolKey.event);

                        JSONObject jsonEvent = new JSONObject(event);


                        vote_left = JsonUtility.GetString(jsonEvent, ProtocolKey.vote_left);


                        String eventjoinArray = JsonUtility.GetString(jsonData, ProtocolKey.eventjoin);


                        JSONArray array = new JSONArray(eventjoinArray);

                        eventJoinCount = array.length();
                        for (int i = 0; i < eventJoinCount; i++) {

                            JSONObject object = (JSONObject) array.get(i);

                            String album = JsonUtility.GetString(object, ProtocolKey.album);
                            String user = JsonUtility.GetString(object, ProtocolKey.user);
                            String eventjoin = JsonUtility.GetString(object, ProtocolKey.eventjoin);

                            ItemAlbum itemAlbum = new ItemAlbum();

                            JSONObject jsonAlbum = new JSONObject(album);
                            JSONObject jsonUser = new JSONObject(user);
                            JSONObject jsonEventJoin = new JSONObject(eventjoin);


                        /*jsonAlbum*/
                            itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                            itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));
                            itemAlbum.setCover(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover));
                            itemAlbum.setHas_voted(JsonUtility.GetBoolean(jsonAlbum, ProtocolKey.has_voted));


                            try {
                                int width = jsonAlbum.getInt(ProtocolKey.cover_width);
                                int height = jsonAlbum.getInt(ProtocolKey.cover_height);
                                int image_height = StaggeredHeight.setImageHeight(width, height);

                                if (image_height < minHeight) {
                                    image_height = minHeight;
                                }

                                itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                                itemAlbum.setCover_height(image_height);
                                itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));


                            } catch (Exception e) {
                                itemAlbum.setCover_hex("");
                                itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                                itemAlbum.setCover_height(PPBApplication.getInstance().getStaggeredWidth());
                                MyLog.Set("e", this.getClass(), "圖片長寬無法讀取");
                            }

                            String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                            JSONObject jsonUsefor = new JSONObject(usefor);
                            itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                            itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                            itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));
                            itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));


                        /*jsonUser*/
                            itemAlbum.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));
                            itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                            itemAlbum.setUser_picture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));


                            itemAlbum.setEvent_join(JsonUtility.GetInt(jsonEventJoin, ProtocolKey.count));

                            itemAlbumList.add(itemAlbum);

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

                    if (eventJoinCount == 0) {
                        sizeMax = true;
                        return;
                    } else {

                        callBack.Success(doingType);

                        if(eventJoinCount<rangeCount){
                            MyLog.Set("d", Protocol99.class, "項目少於" + rangeCount);
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

    }

    private Map<String, String> putMap() {

        Map<String, String> map = new HashMap<>();
        map.put(Key.event_id, event_id);
        map.put(Key.limit, round + "," + rangeCount);
        map.put(Key.token, token);
        map.put(Key.user_id, user_id);
        map.put(Key.searchkey, searchKey);

        return map;
    }

    public AsyncTask getTask() {
        return callTask;
    }


    public List<ItemAlbum> getItemAlbumList() {
        return this.itemAlbumList;
    }

    public String getVoteLeft() {
        return this.vote_left;
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

package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.IntentControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumExplore;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Protocol102 extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void Post();

        public abstract void Success(List<ItemUser> cgaUserList, List<ItemAlbumExplore> itemAlbumExploreList);

        public abstract void TimeOut();
    }

    private Activity mActivity;

    private Protocol102.TaskCallBack callBack;


    private String user_id;
    private String token;
    private String categoryarea_id;


    private String result = "";
    private String message = "";
    private String reponse = "";

    private List<ItemUser> cgaUserList;
    private List<ItemAlbumExplore> itemAlbumExploreList;



    public Protocol102(Activity mActivity, String user_id, String token, String categoryarea_id, Protocol102.TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = user_id;
        this.token = token;
        this.categoryarea_id = categoryarea_id;

        cgaUserList = new ArrayList<>();

        itemAlbumExploreList = new ArrayList<>();


        execute();
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        callBack.Prepare();
    }

    private void getCGAList(JSONObject jsonData){

        try{

            String albumexplore = JsonUtility.GetString(jsonData, ProtocolKey.albumexplore);

           JSONArray jsonArrayAE = new JSONArray(albumexplore);

            for (int i = 0; i < jsonArrayAE.length(); i++) {

                ItemAlbumExplore itemAlbumExplore = new ItemAlbumExplore();

                JSONObject jsonAlbumexplore = (JSONObject)jsonArrayAE.get(i);

                String albumList = JsonUtility.GetString(jsonAlbumexplore, ProtocolKey.album);

                JSONArray jsonArrayAlbum = new JSONArray(albumList);


                itemAlbumExplore.setItemAlbumList(getAlbumList(jsonArrayAlbum));


                String albumexploreX = JsonUtility.GetString(jsonAlbumexplore, ProtocolKey.albumexplore);

                JSONObject jsonAlbumexploreX = new JSONObject(albumexploreX);

                String name = JsonUtility.GetString(jsonAlbumexploreX, ProtocolKey.name);

                itemAlbumExplore.setName(name);

                itemAlbumExploreList.add(itemAlbumExplore);

            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<ItemAlbum> getAlbumList(JSONArray jsonArrayAlbum){

        List<ItemAlbum> itemAlbumList = new ArrayList<>();




        try {


            int minHeight = DensityUtility.dip2px(mActivity.getApplicationContext(), 72);

            for (int j = 0; j < jsonArrayAlbum.length(); j++) {

                JSONObject jsonItem = (JSONObject) jsonArrayAlbum.get(j);

                String album = JsonUtility.GetString(jsonItem, ProtocolKey.album);
                String user = JsonUtility.GetString(jsonItem, ProtocolKey.user);


                JSONObject jsonAlbum = new JSONObject(album);
                JSONObject jsonUser = new JSONObject(user);

                ItemAlbum itemAlbum = new ItemAlbum();

                itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                itemAlbum.setCover(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover));
                itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));
                itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));

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

                    if(width>height){
                        itemAlbum.setImage_orientation(ItemAlbum.LANDSCAPE);
                    }else if(height>width){
                        itemAlbum.setImage_orientation(ItemAlbum.PORTRAIT);
                    }else {
                        itemAlbum.setImage_orientation(0);
                    }

                } catch (Exception e) {
                    itemAlbum.setCover_hex("");
                    itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                    itemAlbum.setCover_height(PPBApplication.getInstance().getStaggeredWidth());
                    MyLog.Set("e", this.getClass(), "圖片長寬無法讀取");
                }






                String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                JSONObject jsonUsefor = new JSONObject(usefor);
                itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));
                itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                itemAlbum.setImage(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.image));
                itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));


                itemAlbum.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));
                itemAlbum.setUser_picture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));

                itemAlbumList.add(itemAlbum);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return itemAlbumList;
    }


    private void getCGAuserList(JSONObject jsonData){

        try {

            String categoryarea = JsonUtility.GetString(jsonData, ProtocolKey.categoryarea);

            JSONObject jsonCGA = new JSONObject(categoryarea);

            String user = JsonUtility.GetString(jsonCGA, ProtocolKey.user);

            JSONArray userArray = new JSONArray(user);

            for (int i = 0; i < userArray.length(); i++) {

                JSONObject jsonUser = (JSONObject) userArray.get(i);

                ItemUser itemUser = new ItemUser();

                itemUser.setName(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                itemUser.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                itemUser.setUser_id(JsonUtility.GetString(jsonUser, ProtocolKey.user_id));

                cgaUserList.add(itemUser);
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    protected Object doInBackground(Void... voids) {

        try {

            reponse = HttpUtility.uploadSubmit(true, Url.P102_GetCategoryArea, putMap(), null);

            MyLog.Set("json", getClass(), reponse);

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

                    getCGAList(jsonData);

                    getCGAuserList(jsonData);


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

                callBack.Success(cgaUserList, itemAlbumExploreList);

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
        map.put(Key.token, token);
        map.put(Key.user_id, user_id);
        map.put(Key.categoryarea_id, categoryarea_id);


        return map;
    }


    public AsyncTask getTask() {
        return this;
    }


}

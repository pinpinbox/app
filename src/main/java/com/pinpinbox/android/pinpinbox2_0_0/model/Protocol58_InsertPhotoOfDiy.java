package com.pinpinbox.android.pinpinbox2_0_0.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Protocol58_InsertPhotoOfDiy extends AsyncTask<Void, Void, Object> {

    public static abstract class TaskCallBack {

        public abstract void Prepare();

        public abstract void UploadSuccessOnUiThread(int uploadedCount);

        public abstract void Post();

        public abstract void Success();

        public abstract void TimeOut();
    }


    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;
    private TaskCallBack callBack;

    private String user_id;
    private String token;
    private String album_id;

    private List<String> pathList;

    private String result = "";
    private String message = "";

    private boolean isMax = false;

    public Protocol58_InsertPhotoOfDiy(Activity mActivity, String album_id, List<String> pathList, TaskCallBack callBack) {
        this.mActivity = mActivity;
        this.callBack = callBack;
        this.user_id = PPBApplication.getInstance().getId();
        this.token = PPBApplication.getInstance().getToken();
        this.album_id = album_id;
        this.pathList = pathList;
        execute();
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        callBack.Prepare();
    }

    @Override
    public Object doInBackground(Void... voids) {

        Map<String, String> map = putMap();

        int count = pathList.size();


        for (int i = 0; i < count; i++) {

            MyLog.Set("e", getClass(), "pathppppp => " + pathList.get(i));

        }


        for (int i = 0; i < count; i++) {

            File file = new File(pathList.get(i));


            try {

                //不做timeout處理 第一項設為false
                String response = HttpUtility.uploadSubmit(false, ProtocolsClass.P58_InsertPhotoOfDiy, map, file);
                MyLog.Set("d", getClass(), "p58reponse => " + response);


                JSONObject jsonObject = new JSONObject(response);

                result = JsonUtility.GetString(jsonObject, ProtocolKey.result);

                if (result.equals("1")) {

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    JSONObject jsonData = new JSONObject(data);

                    //獲取相片最大上限張數
                    String userGrade = JsonUtility.GetString(jsonData, ProtocolKey.usergrade);
                    JSONObject jsonUserGrade = new JSONObject(userGrade);
                    int maxCount = JsonUtility.GetInt(jsonUserGrade, ProtocolKey.photo_limit_of_album);


                    //獲取作品當前張數
                    String photo = JsonUtility.GetString(jsonData, ProtocolKey.photo);
                    JSONArray photoArray = new JSONArray(photo);
                    int albumPhotoCount = photoArray.length();


                    //已上傳張數
                    final int uploadedCount = i + 1;


                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            callBack.UploadSuccessOnUiThread(uploadedCount);

                        }
                    });


                    //判斷張數是否超過最大值
                    if (albumPhotoCount >= maxCount) {

                        isMax = true;

                        break;
                    }


                } else if (result.equals("0")) {

                    message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

                    break;

                }


            } catch (Exception e) {
                e.printStackTrace();
                break;
            }


        }

        return null;
    }


    @Override
    public void onPostExecute(Object obj) {
        super.onPostExecute(obj);
        callBack.Post();


        switch (result) {

            case "1":

                if(isMax){
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_is_max);
                }

                callBack.Success();

                break;

            case "0":

                DialogV2Custom.BuildError(mActivity, message);

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

        Map<String, String> data = new HashMap<>();
        data.put(Key.id, user_id);
        data.put(Key.token, token);
        data.put(Key.album_id, album_id);
        String sign = IndexSheet.encodePPB(data);
        Map<String, String> sendData = new HashMap<>();
        sendData.put(Key.id, user_id);
        sendData.put(Key.token, token);
        sendData.put(Key.album_id, album_id);
        sendData.put(Key.sign, sign);

        return sendData;
    }


    public AsyncTask getTask() {
        return this;
    }


}

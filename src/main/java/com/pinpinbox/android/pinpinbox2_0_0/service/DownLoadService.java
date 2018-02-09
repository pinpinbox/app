package com.pinpinbox.android.pinpinbox2_0_0.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.OkHttpClientManager;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.ZipUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.DownloadBean;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCooperation2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMyUpLoad2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentOther2;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollect2Activity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vmage on 2017/5/2.
 */
public class DownLoadService extends Service {

    private DownLoadService downLoadService;
    private DownloadBean bean;
    private SharedPreferences getdata;

    private List<HashMap<String, Object>> listAblumId;


    private String id, token;
    private String myDir;


    public void onCreate() {
        super.onCreate();


    }

    private void init() {

        downLoadService = this;

        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");
        myDir = "/PinPinBox" + id + "/";

        bean = new DownloadBean();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SystemUtility.SysApplication.getInstance().addService(this);
        init();


        if(intent!=null) {

            Bundle bundle = intent.getExtras();
            String album_id = bundle.getString(Key.album_id, "");
            String className = bundle.getString(Key.className, "");
            int position = bundle.getInt(Key.position, -1);

            listAblumId = new ArrayList<>();

            HashMap<String, Object> map = new HashMap<>();
            map.put(Key.album_id, album_id);
            map.put(Key.className, className);
            map.put(Key.position, position);


            addAlbum(map);

            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);

        }


        return super.onStartCommand(intent, flags, startId);
    }

    private Fragment getFragment(String className){

        Fragment fragment = null;

        List<Activity>activityList = SystemUtility.SysApplication.getInstance().getmList();

        for (int i = 0; i < activityList.size(); i++) {

            if(activityList.get(i).getClass().getSimpleName().equals(MyCollect2Activity.class.getSimpleName())){

                Activity ac = activityList.get(i);

                fragment = ((MyCollect2Activity)ac).getFragment(className);

                break;
            }

        }

        return fragment;
    }

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 0:

                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute();

                    break;

                case 1:

//                    PinPinToast.showSuccessToast(getApplicationContext(), listAblumId.get(0).get(Key.album_id) + "下載成功");

                    String className = (String)listAblumId.get(0).get(Key.className);

                    Fragment fragment = getFragment(className);


                    if(fragment!=null){

                        Bundle bundle = msg.getData();

                        boolean isSuccess = bundle.getBoolean("download_success", false);


                              /*0 => 一般狀態
                                                           1 => 下載中
                                                           2 =>  完成*/

                        if(fragment.getClass().getSimpleName().equals(FragmentMyUpLoad2.class.getSimpleName())) {

                            if(isSuccess) {
                                ((FragmentMyUpLoad2) fragment).changeDownloadType((int) listAblumId.get(0).get(Key.position), 2);
                            }else {
                                ((FragmentMyUpLoad2) fragment).changeDownloadType((int) listAblumId.get(0).get(Key.position), 0);
                            }
                        }

                        if(fragment.getClass().getSimpleName().equals(FragmentOther2.class.getSimpleName())) {
                            if(isSuccess) {
                                ((FragmentOther2) fragment).changeDownloadType((int) listAblumId.get(0).get(Key.position), 2);
                            }else {
                                ((FragmentOther2) fragment).changeDownloadType((int) listAblumId.get(0).get(Key.position), 0);
                            }
                        }

                        if(fragment.getClass().getSimpleName().equals(FragmentCooperation2.class.getSimpleName())) {
                            if(isSuccess) {
                                ((FragmentCooperation2) fragment).changeDownloadType((int) listAblumId.get(0).get(Key.position), 2);
                            }else {
                                ((FragmentCooperation2) fragment).changeDownloadType((int) listAblumId.get(0).get(Key.position), 0);
                            }
                        }


                    }



                    listAblumId.remove(0);

                    if(listAblumId!=null && listAblumId.size()>0){
                        sendMessageByHandler(0);
                        MyLog.Set("d", DownLoadService.class, "keep download");
                    }else {
                        MyLog.Set("d", DownLoadService.class, "no file & close service");
                        stopSelf();
                    }

                    break;



            }
        }
    };


    private class DownloadTask extends AsyncTask<Void, Void, Object>{

        @Override
        protected Object doInBackground(Void... voids) {


            bean.size = bean.loadedSize = 0l;

            final String mAlbum_id = (String)listAblumId.get(0).get(Key.album_id);

            MyLog.Set("d", DownLoadService.class, "mAlbum_id => " + mAlbum_id);

            Map<String, String> data = new HashMap<>();
            data.put("id", id);
            data.put("token", token);
            data.put("albumid", mAlbum_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("albumid", mAlbum_id);
            sendData.put("sign", sign);


            final String dir = DirClass.sdPath + myDir + DirClass.dirZip;
            final String name = mAlbum_id + ".tmp";

            OkHttpClientManager.Param[] param = OkHttpClientManager.map2Params(sendData);
            try {
                Response response = OkHttpClientManager.post(ProtocolsClass.P14_DownloadAlbumZipFile, param);


                OkHttpClient okHttpClient = OkHttpClientManager.getInstance().getOKHttp();
                Call call = okHttpClient.newCall(response.request());
                call.enqueue(new com.squareup.okhttp.Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                        File zipFile = new File(DirClass.sdPath + myDir + DirClass.dirZip + mAlbum_id);
                        if(zipFile!=null && zipFile.exists()){
                            zipFile.delete();
                        }


                        listAblumId.remove(0);

                        if(listAblumId!=null && listAblumId.size()>0){
                            sendMessageByHandler(0);
                            MyLog.Set("d", DownLoadService.class, "keep download => catch");
                        }else {
                            MyLog.Set("d", DownLoadService.class, "no file & close service => catch");
                            stopSelf();
                        }

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        InputStream is = null;
                        byte[] buf = new byte[4096];
                        int len = 0;
                        FileOutputStream fos = null;
                        try {
                            is = response.body().byteStream();
                            File file = new File(dir, name);
                            fos = new FileOutputStream(file);

                            bean.size = response.body().contentLength();

                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                bean.loadedSize += len;
                            }
                            fos.flush();

                            if (bean.size > 0 && bean.loadedSize == bean.size) {
                                String localPath = DirClass.sdPath + myDir + DirClass.dirZip + mAlbum_id;
                                File tmpFile = new File(localPath + ".tmp");
                                tmpFile.renameTo(new File(localPath));

                            } else {}


                            ZipUtility.Unzip(DirClass.sdPath + myDir + DirClass.dirZip + mAlbum_id, DirClass.sdPath + myDir + DirClass.dirAlbumList + mAlbum_id + "/");

                            File zipFile = new File(DirClass.sdPath + myDir + DirClass.dirZip + mAlbum_id);
                            if(zipFile!=null && zipFile.exists()){
                                zipFile.delete();
                            }

                            sendMessageByHandler(1, true);


                        } catch (IOException e) {

                            sendMessageByHandler(1, false);

                            e.printStackTrace();

                        } finally {
                            try {
                                if (is != null) is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (fos != null) fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }





                    }
                });


            } catch (Exception e) {



                File zipFile = new File(DirClass.sdPath + myDir + DirClass.dirZip + mAlbum_id);
                if(zipFile!=null && zipFile.exists()){
                    zipFile.delete();
                }

                sendMessageByHandler(1, false);

//                listAblumId.remove(0);
//
//                if(listAblumId!=null && listAblumId.size()>0){
//                    sendMessageByHandler(0);
//                    MyLog.Set("d", DownLoadService.class, "keep download => catch");
//                }else {
//                    MyLog.Set("d", DownLoadService.class, "no file & close service => catch");
//                    stopSelf();
//                }
                e.printStackTrace();
            }

            return null;
        }
    }

    private void sendMessageByHandler(int what){

        Message msg = new Message();
        msg.what = what;
        mHandler.sendMessage(msg);

    }

    private void sendMessageByHandler(int what, boolean isSuccess){

        Bundle bundle = new Bundle();
        bundle.putBoolean("download_success", isSuccess);

        Message msg = new Message();
        msg.setData(bundle);
        msg.what = what;
        mHandler.sendMessage(msg);

    }



    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeService(this);
        MyLog.Set("d", DownLoadService.class, "onDestroy");

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void addAlbum(HashMap<String, Object>map) {
        listAblumId.add(map);
        for (int i = 0; i < listAblumId.size(); i++) {
            MyLog.Set("d", DownLoadService.class, "----------------album_id => " + listAblumId.get(i).get(Key.album_id));
        }
    }

}

package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.view.View;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.Mode.LOG;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.DirClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.HobbyManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

/**
 * Created by kevin9594 on 2015/1/17
 */
public class BeginActivity extends DraggerActivity {//02

    private Activity mActivity;

    private SharedPreferences getdata;
    private NoConnect noConnect;
    private LoadTask loadTask;

    private String id, token;
    private String p02Result, p02Message;
    private String p23Result, p23Message;
    private String p28Result, p28Message;
    private String myDir;
    private String strPoints;
    private String businessuser_id;

    private int hobbys = 0;
    private int doingType;
    private static final int DoLoad = 0;


    private boolean scanIntent = false;
    private boolean fromAwsMessage = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_begin);

        setSwipeBackEnable(false);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();

        init();

        doLoad();

    }



    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromAwsMessage = bundle.getBoolean("fromAwsMessage");
            scanIntent = bundle.getBoolean(Key.scanIntent, false);
            businessuser_id = bundle.getString(Key.businessuser_id, "");
        }
    }

    private void init() {
        mActivity = this;

        getdata = PPBApplication.getInstance().getData();
        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
        myDir = PPBApplication.getInstance().getMyDir();

        try {
            FileUtility.delAllFile(DirClass.sdPath + myDir + DirClass.dirZip);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyLog.Set("d", getClass(), "id的值是 ======== " + id + " ========");
        MyLog.Set("d", getClass(), "token的值是 ======== " + token + " ========");


    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoLoad:
                        doLoad();
                        break;
                }
            }
        };

        final DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.TIMEOUT);
        d.setConnectInstability(connectInstability);
        d.getBlurView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();

                Bundle bundle = new Bundle();
                bundle.putBoolean(Key.scanIntent, scanIntent);
                bundle.putString(Key.businessuser_id, businessuser_id);


                Intent intent = new Intent(BeginActivity.this, Login2Activity.class);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

            }
        });
        d.show();

    }

    public void doLoad() {

        if (!HttpUtility.isConnect(mActivity)) {
            noConnect = new NoConnect(mActivity);
            return;
        }

        loadTask = new LoadTask();
        loadTask.execute();


        MyLog.Set("d", getClass(), "id => " + id);
        MyLog.Set("d", getClass(), "token => " + token);
    }

    private class LoadTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoLoad;

        }

        @Override
        protected Void doInBackground(Void... params) {
            p02Result = protocol02();
            if (p02Result!=null && p02Result.equals("1")) {
                protocol28();
                protocol23();
            }else {
                p02Result = "";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (p02Result.equals("1")) {
                MyLog.Set("d", mActivity.getClass(), "token success");
                if (p28Result.equals("1")) {
                    createPinPinBoxDirs();
                    if (hobbys > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("fromAwsMessage", fromAwsMessage);
                        bundle.putBoolean("commonBackground", true);
                        bundle.putBoolean(Key.scanIntent, scanIntent);
                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        intent.setClass(BeginActivity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

                    } else {

                        Bundle bundle = new Bundle();
                        bundle.putBoolean("hideActionBar", true);

                        Intent intent = new Intent();
                        intent.putExtras(bundle);
                        intent.setClass(BeginActivity.this, Hobby2Activity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

                    }
                } else if (p28Result.equals("0")) {
                    DialogV2Custom.BuildError(mActivity, p28Message);
                } else {
                   DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
                }//p28Result.equals("1")

            } else if (p02Result.equals(Key.timeout)) {
                connectInstability();
            } else {

                Bundle bundle = new Bundle();
                bundle.putBoolean(Key.scanIntent, scanIntent);
                bundle.putString(Key.businessuser_id, businessuser_id);

                Intent intent = new Intent(BeginActivity.this, Login2Activity.class);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);
            }


        }
    }

    /*檢查token是否過期*/
    private String protocol02() {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P02_CheckToken, SetMapByProtocol.setParam02_checktoken(id, token), null);
            MyLog.Set("d", getClass(), "p02strJson => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p02Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);

                p02Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);

//                p02Result = jsonObject.getString(Key.result);

                if (p02Result.equals("0")) {

                    p02Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

//                    p02Message = jsonObject.getString(Key.message);
                } else if (p02Result.equals("1")) {

                } else {
                    p02Result = "";
                }

            } catch (Exception e) {
                p02Result = "";
                e.printStackTrace();
            }
        }

        return p02Result;
    }

    /*獲取會員資料*/
    private void protocol28() {

        try {
            String strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P28_Getprofile, SetMapByProtocol.setParam28_getprofile(id, token), null);
            JSONObject jsonObject = new JSONObject(strJson);

            if (LOG.isLogMode) {
                Logger.json(strJson);
            }


            p28Result = jsonObject.getString(Key.result);
            if (p28Result.equals("0")) {

                p28Message = jsonObject.getString(Key.message);

            } else if (p28Result.equals("1")) {

                String json_data = jsonObject.getString(Key.data);
                JSONObject object = new JSONObject(json_data);

                String birthday = object.getString(Key.birthday);
                String gender = object.getString(Key.gender);
                String nickname = object.getString(Key.nickname);
                String profilepic = object.getString(Key.profilepic);
                String usergrade = object.getString(Key.usergrade);
                String email = object.getString(Key.email);
                String cellphone = object.getString(Key.cellphone);
                String selfdescription = object.getString(Key.selfdescription);


                String phoneCountry = "";
                String phoneNumber = "";

                try {
                    phoneCountry = cellphone.substring(0, cellphone.indexOf(","));
                    MyLog.Set("d", mActivity.getClass(), "phoneCountry = " + phoneCountry);

                } catch (Exception e) {

                }


                try {
                    phoneNumber = cellphone.substring(cellphone.lastIndexOf(",") + 1);
                    MyLog.Set("d", mActivity.getClass(), "phoneNumber = " + phoneNumber);
                } catch (Exception e) {

                }

                String hobby = object.getString("hobby");
                MyLog.Set("d", mActivity.getClass(), "hobby = " + hobby);

                JSONArray jsonArray = new JSONArray(hobby);
                hobbys = jsonArray.length();



                /*2016.12.08 new add*/
                boolean bCreative = false;
                try {
                    bCreative = object.getBoolean(Key.creative);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                SharedPreferences.Editor editor = getdata.edit();
                editor.putString(Key.birthday, birthday);
                editor.putString(Key.gender, gender);
                editor.putString(Key.nickname, nickname);
                editor.putString(Key.profilepic, profilepic);
                editor.putString(Key.selfdescription, selfdescription);

                editor.putString(Key.cellphone, cellphone);
                editor.putString("countrynumber", phoneCountry);
                editor.putString("nocountry_phonenumber", phoneNumber);

                editor.putString(Key.email, email);
                editor.putString(Key.usergrade, usergrade);

                try {
                    /*2016.09.14新增*/
                    int follow = object.getInt(Key.follow);
                    int viewed = object.getInt(Key.viewed);
                    editor.putInt(Key.follow, follow);
                    editor.putInt(Key.viewed, viewed);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                editor.putBoolean(Key.creative, bCreative);

                editor.commit();

                //20171103
                HobbyManager.SaveHobbyList(hobby);



            }


        } catch (Exception e) {
            p28Result = "";
            e.printStackTrace();
        }

    }

    /*獲取會員點數*/
    private void protocol23() {
        try {
            String strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(id, token, "google"), null);
            MyLog.Set("d", getClass(), "p23strJson => " + strJson);
            JSONObject jsonObject = new JSONObject(strJson);
            p23Result = jsonObject.getString(ProtocolKey.result);
            if (p23Result.equals("1")) {
                strPoints = jsonObject.getString(ProtocolKey.data);
                getdata.edit().putString(ProtocolKey.point, strPoints).commit();
            } else if (p23Result.equals("0")) {
                p23Message = jsonObject.getString(ProtocolKey.message);
            } else {
                p23Result = "";
            }
        } catch (Exception e) {
            p23Result = "";
            e.printStackTrace();
        }
    }

    private void createPinPinBoxDirs() {
        FileUtility fileUtility = new FileUtility();
        fileUtility.createSDDir(myDir);
        fileUtility.createDirIn(myDir, DirClass.dirAlbumList);
        fileUtility.createDirIn(myDir, DirClass.dirZip);
        fileUtility.createDirIn(myDir, DirClass.dirCopy);
    }

    @Override
    public void onBackPressed() {
        Process.killProcess(Process.myPid());
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {

            if (noConnect == null) {
               noConnect = new NoConnect(this);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (loadTask != null && !loadTask.isCancelled()) {
            loadTask.cancel(true);
        }


        Recycle.IMG((ImageView) findViewById(R.id.begin_anim));


        System.gc();
        super.onDestroy();
    }


}
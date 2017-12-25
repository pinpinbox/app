package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.pinpinbox.android.DialogTool.CheckExecute;
import com.pinpinbox.android.DialogTool.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.DoingTypeClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.StringClass.TaskKeyClass;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.NoConnect;
import com.pinpinbox.android.Widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.service.MessageReceivingService;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vmage on 2015/4/14
 */
public class FirstInstallActivity extends DraggerActivity {

    private MessageReceivingService messageReceivingService = new MessageReceivingService();
    private Activity mActivity;
    private NoConnect noConnect;
    private SharedPreferences savedValues;
    private CheckVersionTask checkVersionTask;

    private String numOfMissedMessages;

    private int doingType;

    public static Boolean inBackground = true;
    private boolean fromAwsMessage = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_firstinstall);


        setSwipeBackEnable(false);

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);


        getBundle();


        init();

        clearTaskCache();

        doCheckVersion();


    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {

            fromAwsMessage = bundle.getBoolean("fromAwsMessage");

            MyLog.Set("d", getClass(), "---" + fromAwsMessage);

        }

    }

    private void init() {

        mActivity = this;

        DensityUtility.setScreen(this);


    }

    private void clearTaskCache() {

        SharedPreferences.Editor editor = PPBApplication.getInstance().getData().edit();

        editor.putBoolean(TaskKeyClass.firsttime_login, false).commit();

        editor.putBoolean(TaskKeyClass.firsttime_edit_profile, false).commit();

        editor.putBoolean(TaskKeyClass.firsttime_download_template, false).commit();

        editor.putBoolean(TaskKeyClass.create_free_album, false).commit();

        editor.putBoolean(TaskKeyClass.collect_free_album, false).commit();

        editor.putBoolean(TaskKeyClass.collect_pay_album, false).commit();

        editor.putBoolean(TaskKeyClass.share_to_fb, false).commit();

        editor.putBoolean(TaskKeyClass.firsttime_buy_point, false).commit();

    }



    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doCheckVersion();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doCheckVersion() {
        if (!HttpUtility.isConnect(mActivity)) {
            noConnect = new NoConnect(mActivity);
            return;
        }
        checkVersionTask = new CheckVersionTask();
        checkVersionTask.execute();
    }


    private class CheckVersionTask extends AsyncTask<Void, Void, Object> {

        private int p88Result = -1;
        private String p88Message = "";


        private Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder().url("https://play.google.com/store/apps/details?id=com.pinpinbox.android").build();
                        Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new com.squareup.okhttp.Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                            }

                            @Override
                            public void onResponse(final Response response) throws IOException {
                                String htmlStr = response.body().string();
                                Pattern pattern = Pattern.compile("\"softwareVersion\"\\W*([\\d\\.]+)");
                                Matcher matcher = pattern.matcher(htmlStr);
                                if (matcher.find()) {

                                    if (!SystemUtility.getVersionName(mActivity).equals(matcher.group(1))) {
                                        MyLog.Set("e", FirstInstallActivity.class, "------------------------- 版本不一樣 可更新 -------------------------");

                                        SharedPreferences getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
                                        getdata.edit().putBoolean(Key.update, true).commit();

                                        Message msg = new Message();
                                        msg.what = 1;
                                        mHandler.sendMessage(msg);


                                    } else {
                                        MyLog.Set("e", FirstInstallActivity.class, "------------------------- 版本相符 繼續執行 -------------------------");


                                        SharedPreferences getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
                                        getdata.edit().putBoolean(Key.update, false).commit();

                                        Message msg = new Message();
                                        msg.what = 2;
                                        mHandler.sendMessage(msg);

                                    }

                                } else { //有可能Google Play的網頁內容變動，但仍需讓使用者可以進到下一頁面
                                }
                            }
                        });
                        break;


                    case 1:

                        final DialogV2Custom d = new DialogV2Custom(mActivity);
                        d.setOrientation(LinearLayout.VERTICAL);
                        d.setStyle(DialogStyleClass.CHECK);
                        d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_be_later);
                        d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_pop_go_to_google_play);
                        d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_new_version);

                        d.getBlurView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d.dismiss();
                                finish();
                            }
                        });

                        d.getTvLeftOrTop().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d.dismiss();
                                dowork();
                            }
                        });

                        d.setCheckExecute(new CheckExecute() {
                            @Override
                            public void DoCheck() {
                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    MyLog.Set("d", mActivity.getClass(), "open google play app");
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    MyLog.Set("d", mActivity.getClass(), "open google play web");
                                }
                            }
                        });
                        d.show();

                        break;

                    case 2:
                        dowork();
                        break;


                }

            }

        };


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;

            MyLog.Set("e", FirstInstallActivity.class, "FirstInstallActivity  => loading");
//            showLoading();
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.p88_CheckUpDateVersion, SetMapByProtocol.setParam88_checkupdateversion("google", SystemUtility.getVersionName(mActivity)), null);
                MyLog.Set("d", getClass(), "p88strJson => " + strJson);
            } catch (SocketTimeoutException t) {
                p88Result = Key.TIMEOUT;
                t.printStackTrace();


            } catch (Exception e) {

                p88Result = -1;
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);
                    p88Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p88Result == 0) {
                        p88Message = JsonUtility.GetString(jsonObject, Key.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
//            stopLoading();
            MyLog.Set("e", FirstInstallActivity.class, "FirstInstallActivity  => dissmissLoading");
            if (p88Result == 1) {
                //要更新
                final DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setOrientation(LinearLayout.VERTICAL);
                d.setStyle(DialogStyleClass.CHECK);
                d.getTvLeftOrTop().setVisibility(View.GONE);
                d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_pop_go_to_google_play);
                d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_new_version);

                d.getBlurView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        finish();
                    }
                });

                d.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            MyLog.Set("d", mActivity.getClass(), "open google play app");
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            MyLog.Set("d", mActivity.getClass(), "open google play web");
                        }
                    }
                });
                d.show();


            } else if (p88Result == 2) {

                //先判斷有沒有新版本

                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);


            } else if (p88Result == 0) {
                DialogV2Custom.BuildError(mActivity, p88Message);

            } else if (p88Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                dowork();
            }
        }

    }


    private void dowork() {

        numOfMissedMessages = getString(R.string.num_of_missed_messages);
        Intent intentService = new Intent(mActivity, messageReceivingService.getClass());

        if (HttpUtility.isConnect(mActivity)) {
            startService(intentService);
        }


        boolean scanIntent = false;
        String businessuser_id = "";

        try {

            Uri uri = getIntent().getData();

            if (uri != null) {

                businessuser_id = uri.getQueryParameter(Key.businessuser_id);
                MyLog.Set("d", this.getClass(), "uri.getQueryParameter() => " + uri.getQueryParameter(Key.businessuser_id));
                if (businessuser_id != null && !businessuser_id.equals("") && !businessuser_id.equals("null")) {

                    scanIntent = true;
                } else {
                    scanIntent = false;
                }


//                List<String> pathSegments = uri.getPathSegments();
//
//                if(pathSegments!=null && pathSegments.size()>0){
//
//                    for (int i = 0; i < pathSegments.size(); i++) {
//
//                        String businessuser_id = pathSegments.get(i);
//
//                        MyLog.Set("d", this.getClass(), "businessuser_id => " + businessuser_id);
//
//                        if(!businessuser_id.equals("") && businessuser_id.equals("null") && businessuser_id==null){
//                            scanIntent = true;
//                        }
//
//
//
//                    }
//
//                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        Bundle bundle = new Bundle();
        bundle.putBoolean("fromAwsMessage", fromAwsMessage);
        bundle.putBoolean(Key.scanIntent, scanIntent);
        bundle.putString(Key.businessuser_id, businessuser_id);

        Intent intent = new Intent(FirstInstallActivity.this, BeginActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);
        finish();


    }

    public void onStop() {
        super.onStop();
        inBackground = true;
    }

    public void onResume() {
        super.onResume();
        inBackground = false;
        savedValues = MessageReceivingService.savedValues;
        int numOfMissedMessages = 0;
        if (savedValues != null) {
            numOfMissedMessages = savedValues.getInt(this.numOfMissedMessages, 0);
        }
        String newMessage = getMessage(numOfMissedMessages);
        if (!newMessage.equals("")) {
            Log.i("displaying message", newMessage);

        }
    }

    @Override
    public void onBackPressed() {
        Process.killProcess(Process.myPid());
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    // If messages have been missed, check the backlog. Otherwise check the current intent for a new message.
    private String getMessage(int numOfMissedMessages) {
        String message = "";
        String linesOfMessageCount = getString(R.string.lines_of_message_count);
        if (numOfMissedMessages > 0) {
            String plural = numOfMissedMessages > 1 ? "s" : "";
            Log.i("onResume", "missed " + numOfMissedMessages + " message" + plural);

            for (int i = 0; i < savedValues.getInt(linesOfMessageCount, 0); i++) {
                String line = savedValues.getString("MessageLine" + i, "");
                message += (line + "\n");
            }
            NotificationManager mNotification = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotification.cancel(R.string.notification_number);
            SharedPreferences.Editor editor = savedValues.edit();
            editor.putInt(this.numOfMissedMessages, 0);
            editor.putInt(linesOfMessageCount, 0);
            editor.commit();
        } else {
            Log.i("onResume", "no missed messages");
            Intent intent = getIntent();
            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    for (String key : extras.keySet()) {

                        message += key + "=" + extras.getString(key) + "\n";


                    }
                }
            }
        }
        message += "\n";
        return message;
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
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(this);

        System.gc();

        MyLog.Set("d", getClass(), "onDestroy()");


        super.onDestroy();
    }

}

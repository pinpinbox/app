package com.pinpinbox.android.pinpinbox2_0_0.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pinpinbox.android.Mode.LOG;
import com.pinpinbox.android.R;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.activity.FirstInstallActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.FromService2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Main2Activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MessageReceivingService extends Service {

    private GoogleCloudMessaging gcm;
    public static SharedPreferences savedValues, getdata;
    public static Bitmap bb = null;

    public String token;
    public String deviceid;


    public void onCreate() {
        super.onCreate();

        final String preferences = getString(R.string.preferences);
        savedValues = getSharedPreferences(preferences, Activity.MODE_PRIVATE);
        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        gcm = GoogleCloudMessaging.getInstance(getBaseContext());
        SharedPreferences savedValues = PreferenceManager.getDefaultSharedPreferences(this);
        if (savedValues.getBoolean(getString(R.string.first_launch), true)) {
            MyLog.Set("d", getClass(), "first_launch");
            register();
        }

    }


    public static void sendToApp(Bundle extras, Context context) {

//        Intent newIntent = new Intent();
//        newIntent.setClass(context, BeginActivity.class);
//        newIntent.putExtras(extras);
//        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(newIntent);
    }

    protected static void saveToLog(Bundle extras, Context context) {


/*以下方式可行*/
//        String message1 = extras.getString("message");
//        String icon1 = extras.getString("icon");
//        MyLog.Set("d" , MessageReceivingService.class, "message => " + message1);
//        MyLog.Set("d" , MessageReceivingService.class, "icon => " + icon1);


        String subject = "";
        SharedPreferences.Editor editor = savedValues.edit();
        String numOfMissedMessages = context.getString(R.string.num_of_missed_messages);
        int linesOfMessageCount = 0;
        String line = "";


        HashMap<String, Object> map = new HashMap<>();

        for (String key : extras.keySet()) {
            line = String.format("%s=%s", key, extras.getString(key));

            /**2016.08.09新增*/
            String strKey = line.substring(0, line.lastIndexOf("="));
            String strValue = line.substring(line.indexOf("=") + 1);

            MyLog.Set("d", MessageReceivingService.class, "--------------------------------------------------------------------");
            MyLog.Set("d", MessageReceivingService.class, "strKey => " + strKey);
            MyLog.Set("d", MessageReceivingService.class, "strValue => " + strValue);
            MyLog.Set("d", MessageReceivingService.class, "--------------------------------------------------------------------");


            map.put(strKey, strValue);

            String a = line.substring(0, 8);
            if (a.equals("message=")) {
                line = line.substring(line.indexOf("=") + 1);
                subject = line;
            }

            editor.putString("MessageLine" + linesOfMessageCount, line);
            linesOfMessageCount++;

        }

        editor.putInt(context.getString(R.string.lines_of_message_count), linesOfMessageCount);
        editor.putInt(numOfMissedMessages, savedValues.getInt(numOfMissedMessages, 0) + 1);
        editor.commit();


        String type = (String) map.get("type");
        String type_id = (String) map.get("type_id");
        String detail = (String) map.get("detail");
        String title = (String) map.get("title");
        String icon = (String) map.get("icon");
        String pinpinboard = (String) map.get("pinpinboard");

        if (LOG.isLogMode) {
            Log.e("----", "type =>" + type);
            Log.e("----", "type_id =>" + type_id);
            Log.e("----", "detail =>" + detail);
            Log.e("----", "title =>" + title);
            Log.e("----", "icon =>" + icon);
            Log.e("----", "pinpinboard =>" + pinpinboard);
        }


        /**
         * 1. apply没有返回值而commit返回boolean表明修改是否提交成功
         * 2. apply是将修改数据原子提交到内存, 而后异步真正提交到硬件磁盘,
         *    而commit是同步的提交到硬件磁盘，因此，在多个并发的提交commit的时候，
         *    他们会等待正在处理的commit保存到磁盘后在操作，从而降低了效率。
         *    而apply只是原子的提交到内容，后面有调用apply的函数的将会直接覆盖前面的内存数据，这样从一定程度上提高了很多效率。
         * 3. apply方法不会提示任何失败的提示。
         * 由于在一个进程中，sharedPreference是单实例，一般不会出现并发冲突，如果对提交的结果不关心的话，
         * 建议使用apply，当然需要确保提交成功且有后续操作的话，还是需要用commit的。
         * */
        SharedPreferences getawsdata = context.getSharedPreferences(SharedPreferencesDataClass.awsDetail, Activity.MODE_PRIVATE);

        getawsdata.edit().putString("type", type).apply();

        if (type_id != null && !type_id.equals("")) {
            getawsdata.edit().putString("type_id", type_id).apply();
        }

        if (detail != null && !detail.equals("")) {
            getawsdata.edit().putString("detail", detail).apply();
        }


        /*2016.10.29 new add*/
        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();

        if (activityList.size() > 0) {

            postNotification(new Intent(context, FromService2Activity.class), context, subject, title, icon);

        } else {

            Bundle bundle = new Bundle();
            bundle.putBoolean("fromAwsMessage", true);

            Intent intent = new Intent(context, FirstInstallActivity.class);
            intent.putExtras(bundle);

            postNotification(intent, context, subject, title, icon);


        }

    }

    protected static void postNotification(Intent intentAction, Context context, String subject, String mySubject, String icon) {

        GetPhotoTask getPhotoTask = new GetPhotoTask(intentAction, context, subject, mySubject, icon);
        getPhotoTask.execute();


    }

    private void register() {

        MyLog.Set("e", getClass(), "register");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    token = gcm.register(getString(R.string.project_number));
                    MyLog.Set("d", MessageReceivingService.class, "registrationId => " + token);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        deviceid = Build.SERIAL;
                    }

                    SharedPreferences getdata = getSharedPreferences(SharedPreferencesDataClass.deviceDetail, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = getdata.edit();
                    editor.putString("aws_registrationid", token);
                    editor.putString("deviceid", deviceid);
//                    editor.putBoolean("aws_first_register",true);
                    editor.commit();

                } catch (IOException e) {
                    Log.i("Registration Error", e.getMessage());
                }
                SharedPreferences.Editor editor = savedValues.edit();
                editor.putBoolean(getString(R.string.first_launch), false);
                editor.commit();
            }
        }).start();

//        postNotification(new Intent(this, FirstInstallActivity.class), this, "立即使用!", "pinpinbox", "");

    }

    public IBinder onBind(Intent arg0) {
        return null;
    }


    private static class GetPhotoTask extends AsyncTask<Void, Void, Object> {

        Intent intentAction;
        Context context;
        String subject;
        String mySubject;
        String icon;

        public GetPhotoTask(Intent intentAction, final Context context, String subject, String mySubject, String icon) {

            this.intentAction = intentAction;
            this.context = context;
            this.subject = subject;
            this.mySubject = mySubject;
            this.icon = icon;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 3;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;

//            bb = HttpUtility.getNetBitmap("https://ppb.sharemomo.com/static_file/pinpinbox/zh_TW/images/123.png");


            if (icon == null || icon.equals("")) {
                bb = BitmapFactory.decodeResource(context.getResources(), R.drawable.pinpin_192, opts);

                MyLog.Set("d", MessageReceivingService.class, "0");

            } else {
                try {
                    bb = HttpUtility.getNetBitmap(icon);

                    MyLog.Set("d", getClass(), "icon => " + icon);


                    MyLog.Set("d", MessageReceivingService.class, "1");
                } catch (Exception e) {
                    bb = BitmapFactory.decodeResource(context.getResources(), R.drawable.pinpin_192, opts);
                    MyLog.Set("d", MessageReceivingService.class, "2");
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            final NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            final PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intentAction,
                    Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL | PendingIntent.FLAG_UPDATE_CURRENT);


            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.pinpin_192)
                    .setLargeIcon(bb)
                    .setContentTitle(mySubject)
                    .setContentText(subject)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setTicker(subject)
                    .getNotification();

            mNotificationManager.notify(R.string.notification_number, notification);

            Activity mActivity = null;

            List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();

            int count = activityList.size();

            if (count > 0) {

                for (int i = 0; i < count; i++) {

                    String actName = activityList.get(i).getClass().getSimpleName();

                    if (actName.equals("Main2Activity")) {
                        mActivity = activityList.get(i);
                        break;
                    }
                }
            }

            if (mActivity != null) {
                ((Main2Activity) mActivity).showNewIcon();
            }


                /*20171019*/
            int badgeCount = getdata.getInt(Key.badgeCount, 0);
            MyLog.Set("d", MessageReceivingService.class, "badgeCount => " + badgeCount);
            try {

                badgeCount++;

                ShortcutBadger.applyCountOrThrow(context, badgeCount);

                getdata.edit().putInt(Key.badgeCount, badgeCount).commit();

            }catch (Exception e){
                e.printStackTrace();
            }






        }
    }

}
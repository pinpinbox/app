package com.pinpinbox.android.Utility;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;

import com.pinpinbox.android.pinpinbox2_0_0.bean.GridItem;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vmage on 2015/5/13
 */
public class SystemUtility {
    public static final int V2_2 = 8;
    public static final int V2_3 = 9;
    public static final int V2_3_3 = 10;
    public static final int V3_0 = 11;
    public static final int V3_1 = 12;
    public static final int V3_2 = 13;
    public static final int V4_0 = 14;
    public static final int V4_0_3 = 15;
    public static final int V4_1 = 16;
    public static final int V4_2 = 17;
    public static final int V4_3 = 18;
    public static final int V4_4 = 19;

    public static final int V4_42 = 20;
    public static final int V5 = Build.VERSION_CODES.LOLLIPOP; //21

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public static boolean Above_Equal_V5() {

        if (getSystemVersion() >= V5) {
            return true;
        } else {
            return false;
        }

    }

    public static void getAppDetailSettingIntent(Activity mActivity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= V2_3) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", mActivity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= V2_2) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", mActivity.getPackageName());
        }
        mActivity.startActivity(localIntent);
    }


    /**
     * @param
     * @return int
     * @throws
     * @Description: 检测当前的版本信息
     */
    public static int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }


    public static int getVersionCode(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    public static String getVersionName(Context context)//获取版本号(内部识别号)
    {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static ArrayList<String> getPhoneContacts(Context mContext) {
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> newlist = new ArrayList<>();


        Cursor cur = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cur.moveToFirst()) {
            int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
            do {

                String contactId = cur.getString(idColumn);
                int phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (phoneCount > 0) {
                    Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);

//                    Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
//                                    + " = " + contactId
//
//                                    +" and "+ ContactsContract.CommonDataKinds.Phone.TYPE+"="+ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, null, null);

                    if (phones.moveToFirst()) {
                        do {
                            //遍历所有的电话号码
                            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            list.add(phoneNumber);


                        } while (phones.moveToNext());
                    }
                }

            } while (cur.moveToNext());
        }
        cur.close();

        System.out.println(list.size());


        HashSet hashSet = new HashSet(list);
        ArrayList<String> relist = new ArrayList(hashSet);
        Collections.sort(relist);
        for (Object item : relist) {

            String s = item.toString();
            String ss = s.replace("-", "");//去掉-
            String sss = ss.replace(" ", "");//去掉空格
            System.out.println(sss);
            newlist.add(sss);
        }


        System.out.println(newlist.size());

        return newlist;

    }

    public static List<GridItem> getFromShareListPicPath(Activity activity) {
        List<GridItem> gridItemList = new ArrayList<>();

        Intent intent = activity.getIntent();//如果从外部进入APP，则实现以下方法
        if (Intent.ACTION_SEND.equals(intent.getAction())) {

            Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (imageUri != null) {
//处理单张图片

                MyLog.Set("d", SystemUtility.class, "getFromShareListPicPath => singular number" + imageUri.getPath());


                if (imageUri.getPath().contains("external/images/media")) {

                    GridItem gridItem = new GridItem();

                    String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
                    Cursor actualimagecursor = activity.managedQuery(imageUri, proj, null, null, null);
                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    actualimagecursor.moveToFirst();

                    /*原路徑*/
                    String img_path = actualimagecursor.getString(actual_image_column_index);
                    gridItem.setPath(img_path);

                    /*旋轉角度*/
                    int orientation = actualimagecursor.getInt(actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION));
                    gridItem.setDegree(orientation);

                    gridItemList.add(gridItem);
                }
            }

        } else if (Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction())) {

            List<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            if (imageUris != null) {
//处理多张图片
                for (int i = 0; i < imageUris.size(); i++) {


                    MyLog.Set("d", SystemUtility.class, "getFromShareListPicPath => plural number" + imageUris.get(i).getPath());


//                    if (imageUris.get(i).getPath().contains("external/video/media")) {
//
//                        String[] proj = {MediaStore.Video.Media.DATA};
//                        Cursor curVideo = activity.managedQuery(imageUris.get(i), proj, null, null, null);
//                        int actual_video_column_index = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//                        curVideo.moveToFirst();
//                        String videoPath = curVideo.getString(actual_video_column_index);
//                        picPath.add(videoPath);
//                    }


                    if (imageUris.get(i).getPath().contains("external/images/media")) {

                        GridItem gridItem = new GridItem();


                        String[] proj = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION};
                        Cursor actualimagecursor = activity.managedQuery(imageUris.get(i), proj, null, null, null);
                        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        actualimagecursor.moveToFirst();

                         /*原路徑*/
                        String img_path = actualimagecursor.getString(actual_image_column_index);
                        gridItem.setPath(img_path);

                    /*旋轉角度*/
                        int orientation = actualimagecursor.getInt(actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION));
                        gridItem.setDegree(orientation);

                        gridItemList.add(gridItem);


//                        String img_path = actualimagecursor.getString(actual_image_column_index);
//                        picPath.add(img_path);
                    }

                }
            }

        }

        return gridItemList;
    }

    public static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    public static void setAirplaneMode(Context context, boolean status) {
// 先判斷目前是已否開啟飛航模式
        boolean isAirplaneModeOn = isAirplaneModeOn(context);
        if ((status && isAirplaneModeOn) || (!status && !isAirplaneModeOn)) {
            return;
        }
        int mode = status ? 1 : 0;
// 設定飛航模式的狀態並廣播出去
        Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, mode);
        Intent i = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        i.putExtra("state", mode);
        context.sendBroadcast(i);
    }

    public static class SysApplication extends Application {
        private List<Activity> mList = new LinkedList<>();

        private List<Service> sList = new LinkedList<>();

        //为了实现每次使用该类时不创建新的对象而创建的静态对象
        private static SysApplication instance;

        //构造方法
        private SysApplication() {
        }

        //实例化一次
        public synchronized static SysApplication getInstance() {
            if (null == instance) {
                instance = new SysApplication();
            }
            return instance;
        }

        public List<Activity> getmList() {
            return this.mList;
        }

        public List<Service> getsList() {
            return sList;
        }


        //add Service
        public void addService(Service service) {
            sList.add(service);
        }


        // add Activity
        public void addActivity(Activity activity) {
            mList.add(activity);
        }


        public void removeActivity(Activity mActivity) {

            for (int i = 0; i < mList.size(); i++) {

                MyLog.Set("d", this.getClass(), mList.get(i).getLocalClassName());

                if (mList.get(i) != null) {


                    try {
                        if (mList.get(i).getLocalClassName().equals(mActivity.getLocalClassName())) {
                            Activity activity = mList.get(i);
                            activity.finish();
                            mList.remove(i);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

        }

        public void removeService(Service mService) {

            for (int i = 0; i < sList.size(); i++) {

                MyLog.Set("d", this.getClass(), sList.get(i).getClass().getSimpleName());

                if (sList.get(i) != null) {

                    if (sList.get(i).getClass().getSimpleName().equals(mService.getClass().getSimpleName())) {

                        sList.get(i).stopSelf();

                        sList.remove(i);

                    }
                }
            }
        }


        //关闭每一个list内的activity
        public void exit() {
            try {
                for (Activity activity : mList) {
                    if (activity != null) {

                        MyLog.Set("d", getClass(), "exit => remove => " + activity.getClass().getName());

                        activity.finish();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                MyLog.Set("d", getClass(), "-------------最後一個activity");

                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
            }
        }

        //杀进程
        public void onLowMemory() {
            super.onLowMemory();
            System.gc();
        }
    }

    public static boolean checkActivityExist(String strActivitySimpleName) {

        boolean isActivityExist = false;

        List<Activity> activityList = SysApplication.getInstance().getmList();
        for (int i = 0; i < activityList.size(); i++) {

            String name = activityList.get(i).getClass().getSimpleName();

            if (name.equals(strActivitySimpleName)) {
                isActivityExist = true;
                break;
            }

        }

        return isActivityExist;

    }


    public static Activity getActivity(String acName) {

        Activity mActivity = null;

        List<Activity> activityList = SysApplication.getInstance().getmList();
        for (int i = 0; i < activityList.size(); i++) {

            String name = activityList.get(i).getClass().getSimpleName();

            if (name.equals(acName)) {
                mActivity = activityList.get(i);
                break;
            }

        }


        return mActivity;
    }


}

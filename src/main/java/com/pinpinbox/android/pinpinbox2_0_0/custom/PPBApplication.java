package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.aviary.android.feather.sdk.IAviaryClientCredentials;
import com.blankj.utilcode.util.Utils;
import com.flurry.android.FlurryAgent;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.pinpinbox2_0_0.mode.LOG;
import com.pinpinbox.android.R;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by vmage on 2015/10/27
 */
public class PPBApplication extends MultiDexApplication implements IAviaryClientCredentials {
    private static final String CREATIVE_SDK_SAMPLE_CLIENT_SECRET = "1dc7e6b7-ad87-4cd2-b81c-9c29c86435c0";
    private static final String CREATIVE_SDK_SAMPLE_CLIENT_ID = "ec6b76ad3d2246d38cb06dc39fdaa22e";

    private int staggeredWidth;

    private int statusBarHeight = 0;

    private static PPBApplication instance;

    public static PPBApplication getInstance() {
        return instance;
    }


    private SharedPreferences getData, fbData;

    public void setSharedPreferences(SharedPreferences data) {
        this.getData = data;
    }

    public SharedPreferences getData() {

        //20171011
        if(getData==null) {
            getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        }

        return this.getData;
    }


    public void setSharedPreferencesByFacebook(SharedPreferences fbData) {
        this.fbData = fbData;
    }

    public SharedPreferences getFbData() {

        fbData = getSharedPreferences(SharedPreferencesDataClass.fb_registrationData, Activity.MODE_PRIVATE);


        return this.fbData;
    }

    public String getId() {

        //20171011
        if(getData==null) {
            getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        }

        return getData.getString(Key.id, "");
    }

    public String getToken() {

        //20171011
        if(getData==null) {
            getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        }

        return getData.getString(Key.token, "");
    }

    public String getMyDir() {

        return "PinPinBox" + getId() + "/";
    }


    @Override
    public void onCreate() {
        super.onCreate();


        instance = this;

        getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);

//        RichText.initCacheDir(this);

        setFont();

        setFlurryAgent();

        setUtils();


    }

    private void setFont() {
        /**2016.09.25*/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/NotoSans-Regular.ttf")
//                        .setDefaultFontPath("fonts/OpenSans-Bold.ttf")

                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    private void setFlurryAgent() {

        /**2016.09.13新增*/
        // 是否打印本地的Flurry Log

        if (LOG.isLogMode) {
            FlurryAgent.setLogEnabled(true);
        } else {
            FlurryAgent.setLogEnabled(false);
        }



        // 打印Log的级别
        FlurryAgent.setLogLevel(Log.DEBUG);

        // test B3N3F93Q9CFTRYMN9VZS
        //test2 MG8X6J2Z5VX8QCZX4XTX


//        if (!TestMode.TESTMODE && !LOG.isLogMode) {
//            FlurryAgent.init(this, "6R6SZQVX235W4YXJR8MM");//www 2.1.9
//        } else {
//            FlurryAgent.init(this, "5PYSZ533JSG4GTYJ5SJZ");//w3 2.0.0
//        }

        if(BuildConfig.FLAVOR.equals("w3_private")){
            FlurryAgent.init(this, "5PYSZ533JSG4GTYJ5SJZ");//w3 2.0.0
        }else if(BuildConfig.FLAVOR.equals("www_private")){
            FlurryAgent.init(this, "6R6SZQVX235W4YXJR8MM");//www 2.1.9
            return;
        }else if(BuildConfig.FLAVOR.equals("www_public")){
            FlurryAgent.init(this, "6R6SZQVX235W4YXJR8MM");//www 2.1.9
            return;
        }




//        if (TestMode.TESTMODE) {
//            FlurryAgent.init(this, "5PYSZ533JSG4GTYJ5SJZ");//w3 2.0.0
////            FlurryAgent.init(this, "657JPGZ82VWQS8ZB3JJR");//w3 1.1.0
////            FlurryAgent.init(this, "MG8X6J2Z5VX8QCZX4XTX");//w3 1.0.0
//        } else {
//
//              FlurryAgent.init(this, "6R6SZQVX235W4YXJR8MM");//www 2.1.9
//              FlurryAgent.init(this, "KTXRNZGVCJ3TG4YVGGDR");//www 2.0.0
////            FlurryAgent.init(this, "4QW2DPZ28BQWPPG5BBVY");//www 1.2.0
////            FlurryAgent.init(this, "F4J75J6D54FT9DG7F4FF");//www 1.1.0
////            FlurryAgent.init(this, "2P79F9VGZ6TSXYD36WD8");//www
//        }


        MyLog.Set("d", getClass(), "啟動Flurry");
    }

    private void setUtils(){

        Utils.init(this);

    }


    public int getStaggeredWidth() {
        return staggeredWidth;
    }

    public void setStaggeredWidth(int staggeredWidth) {
        this.staggeredWidth = staggeredWidth;
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    @Override
    public String getBillingKey() {
        return "";
    }

    @Override
    public String getClientID() {
        return CREATIVE_SDK_SAMPLE_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return CREATIVE_SDK_SAMPLE_CLIENT_SECRET;
    }




}

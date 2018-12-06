package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by vmage on 2015/10/27
 */
public class PPBApplication extends MultiDexApplication {



    public static final int PHONE = 10001;
    public static final int TABLE = 10002;


    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;


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
        if (getData == null) {
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
        if (getData == null) {
            getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        }

        return getData.getString(Key.id, "");
    }

    public String getToken() {

        //20171011
        if (getData == null) {
            getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        }

        return getData.getString(Key.token, "");
    }

    public String getMyDir() {

        return "PinPinBox" + getId() + "/";
    }

    public boolean isPhone(){

        if(!SystemUtility.isTablet(this)){
            return true;
        }else {
            return false;
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();


        instance = this;

        getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);

        setFont();

        setFlurryAgent();

        setUtils();

        setGA();

    }

    private void setFont() {
        /*2016.09.25*/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/NotoSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private void setFlurryAgent() {

        /*2016.09.13新增*/
        // 是否打印本地的Flurry Log

        if (BuildConfig.DEBUG) {
            FlurryAgent.setLogEnabled(true);
        } else {
            FlurryAgent.setLogEnabled(false);
        }


        // 打印Log的级别
        FlurryAgent.setLogLevel(Log.DEBUG);


        if (BuildConfig.FLAVOR.equals("w3_private")) {
            FlurryAgent.init(this, KeysForSKD.FLURRY_W3);//w3 2.0.0
        } else if (BuildConfig.FLAVOR.equals("www_private")) {
            FlurryAgent.init(this, KeysForSKD.FLURRY_WWW);//www 2.1.9
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            FlurryAgent.init(this, KeysForSKD.FLURRY_WWW);//www 2.1.9
        }
    }

    private void setUtils() {
        Utils.init(this);
    }

    private void setGA() {
        sAnalytics = GoogleAnalytics.getInstance(this);
        if (BuildConfig.FLAVOR.equals("w3_private") || BuildConfig.FLAVOR.equals("www_private") || BuildConfig.FLAVOR.equals("platformvmage5_private")) {
            sAnalytics.setDryRun(true);
        }

    }

    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
//            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
            sTracker = sAnalytics.newTracker(KeysForSKD.GOOGLE_ANALYTICS);

        }
        return sTracker;
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


}

package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.aviary.android.feather.sdk.IAviaryClientCredentials;
import com.blankj.utilcode.util.Utils;
import com.flurry.android.FlurryAgent;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.mode.LOG;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by vmage on 2015/10/27
 */
public class PPBApplication extends MultiDexApplication implements IAviaryClientCredentials {


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
        /*2016.09.25*/
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/NotoSans-Regular.ttf")
//                        .setDefaultFontPath("fonts/OpenSans-Bold.ttf")

                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    private void setFlurryAgent() {

        /*2016.09.13新增*/
        // 是否打印本地的Flurry Log

        if (LOG.isLogMode) {
            FlurryAgent.setLogEnabled(true);
        } else {
            FlurryAgent.setLogEnabled(false);
        }


        // 打印Log的级别
        FlurryAgent.setLogLevel(Log.DEBUG);



        if(BuildConfig.FLAVOR.equals("w3_private")){
            FlurryAgent.init(this, Keys_Flurry.w3);//w3 2.0.0
        }else if(BuildConfig.FLAVOR.equals("www_private")){
            FlurryAgent.init(this, Keys_Flurry.www);//www 2.1.9
            return;
        }else if(BuildConfig.FLAVOR.equals("www_public")){
            FlurryAgent.init(this, Keys_Flurry.www);//www 2.1.9
            return;
        }





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
        return Keys_Adobe.CREATIVE_SDK_CLIENT_ID;
    }

    @Override
    public String getClientSecret() {
        return Keys_Adobe.CREATIVE_SDK_CLIENT_SECRET;
    }




}

package com.pinpinbox.android.pinpinbox2_0_0.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.pinpinbox.android.pinpinbox2_0_0.mode.LOG;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kevin9594 on 2016/8/12.
 */
public class RegistraAWSService extends IntentService {

    private IntentService service;

    private SharedPreferences saveDevice, getdata;

    private String TAG = getClass().getSimpleName();
    private String id, token;
    private String deviceid, deviceToken;
    private String p50Result, p50Message;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RegistraAWSService(String name) {
        super(name);
    }

    public RegistraAWSService() {
        super("RegistraAWSService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    public void onCreate() {
        super.onCreate();

        init();


        if (LOG.isLogMode) {

            Log.d(TAG, "deviceid =>" + deviceid);
            Log.d(TAG, "deviceToken =>" + deviceToken);

        }

    }

    private void init() {

        service = this;

        saveDevice = getSharedPreferences(SharedPreferencesDataClass.deviceDetail, Activity.MODE_PRIVATE);
        deviceToken = saveDevice.getString("aws_registrationid", "");
        deviceid = saveDevice.getString("deviceid", "");


        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString("id", "");
        token = getdata.getString("token", "");


    }


    @Override
    public void onDestroy() {

        if (LOG.isLogMode) {
            Log.d(TAG, "onDestroy");
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        MyLog.Set("d", getClass(), "onHandleIntent");


        for (int i = 0; i < 3; i++) {
            doSetAWS();
            if (p50Result.equals("1")) {
                getdata.edit().putString("reg_aws", "1").commit();
                MyLog.Set("d", getClass(), "reg_aws success");
                break;
            } else {
                getdata.edit().putString("reg_aws", "0").commit();
                MyLog.Set("d", getClass(), "再執行doSetAWS => " + (i + 1));
            }
        }

        stopSelf();

    }

    private void doSetAWS() {

        Map<String, String> data = new HashMap<String, String>();
        data.put("id", id);
        data.put("token", token);
        data.put("devicetoken", deviceToken);
        data.put("identifier", deviceid);
        data.put("os", "android");
        String sign = IndexSheet.encodePPB(data);
        Map<String, String> sendData = new HashMap<String, String>();
        sendData.put("id", id);
        sendData.put("token", token);
        sendData.put("devicetoken", deviceToken);
        sendData.put("identifier", deviceid);
        sendData.put("os", "android");
        sendData.put("sign", sign);
        try {

            MyLog.Set("d", service.getClass(), "do protocol 50 SetAwsSns");

            String strJson = HttpUtility.uploadSubmit(600000, ProtocolsClass.P50_SetAwsSns, sendData, null);
            if (LOG.isLogMode) {
                Log.e(TAG, "p50strJson => " + strJson);
            }
            JSONObject object = new JSONObject(strJson);
            p50Result = object.getString(Key.result);

            if (p50Result.equals("1")) {

            } else if (p50Result.equals("0")) {
                p50Message = object.getString(Key.message);
            } else {
                p50Result = "";
            }

        } catch (Exception e) {
            p50Result = "";
        }


    }


}

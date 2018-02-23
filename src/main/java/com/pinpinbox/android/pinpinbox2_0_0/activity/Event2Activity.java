package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.Gradient.ScrimUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TagClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2016/5/25.
 */
public class Event2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private PopupCustom popContributeWay;

    private JSONArray eventTemplateArray;

    private GetEventTask getEventTask;
    private GetMyCollectTask getMyCollectTask;
//    private SendContributeTask sendContributeTask;
//    private FastCreateTask fastCreateTask;

    private String TAG = TagClass.TagEventActivity;
    private String id, token;
    private String url, event_id, image, strName, strTitle, strPrefixText, strSpecialUrl;
    private String p17Result, p17Message;
    private String sendAlbum_id;


    private int contribution = 0;
    private int intPopularity = 0;
    private int p76Result = -1;
    private int doingType;
    private static final int DoGetEvent = 0;
    private static final int DoGetMyCollect = 1;
    private static final int DoSendContribute = 2;
    private static final int DoFastCreate = 3;

    private boolean canCreate = true; //預設可以建立新作品

    private ArrayList<String> eventTemplateList;
    private ArrayList<HashMap<String, Object>> canContributeList;


    private ImageView eventImg;
    private ImageView backImg;
    private TextView tvName, tvTitle, tvEvent, tvContribute, tvExit, tvPopularity, tvExchange;
    private LinearLayout linVote;
    private RelativeLayout rDetail;

//    public void setCanCreate(boolean canCreate) {
//        this.canCreate = canCreate;
//    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_event);

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);


        getBundle();

        init();

        doGetEvent();

    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            event_id = bundle.getString(Key.event_id, "");
        }

    }

    private void init() {

        mActivity = this;

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        eventTemplateList = new ArrayList<>();
        canContributeList = new ArrayList<>();

        linVote = (LinearLayout) findViewById(R.id.linVote);
        rDetail = (RelativeLayout) findViewById(R.id.rDetail);

        tvName = (TextView) findViewById(R.id.tvName);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvEvent = (TextView) findViewById(R.id.tvEvent);
        tvContribute = (TextView) findViewById(R.id.tvContribute);
        tvExit = (TextView) findViewById(R.id.tvExit);
        tvPopularity = (TextView) findViewById(R.id.tvPopularity);
        tvExchange = (TextView) findViewById(R.id.tvExchange);

        eventImg = (ImageView) findViewById(R.id.eventImg);

        TextUtility.setBold(tvName, true);
        TextUtility.setBold(tvEvent, true);
        TextUtility.setBold(tvContribute, true);
        TextUtility.setBold(tvExchange, true);
        TextUtility.setBold((TextView) findViewById(R.id.tvVote), true);


        tvEvent.setOnClickListener(this);
        tvContribute.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        linVote.setOnClickListener(this);
        tvExchange.setOnClickListener(this);


        try {
            if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {
                if (rDetail != null) {
                    rDetail.setBackground(
                            ScrimUtil.makeCubicGradientScrimDrawable(
                                    Color.parseColor(ColorClass.BLACK_ALPHA),
                                    8, //渐变层数
                                    Gravity.BOTTOM)); //起始方向
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void toEvent() {


        Bundle bundle = new Bundle();
        bundle.putString("url", url);

        Intent intent = new Intent(mActivity, WebView2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);

    }

    private void toVote() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.event_id, event_id);

        Intent intent = new Intent(mActivity, Vote2Activity.class);
        intent.putExtras(bundle);

        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);


    }

    private void toSpecialUrl() {

        Uri content_url = Uri.parse(strSpecialUrl);
        Intent intent = new Intent();
        intent.setData(content_url);

        startActivity(intent);

    }

    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);


    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoGetEvent:
                        doGetEvent();
                        break;

                    case DoGetMyCollect:
                        doGetMyCollect();
                        break;

//                    case DoSendContribute:
//                        doSendContribute();
//                        break;


                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    private void doGetEvent() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        getEventTask = new GetEventTask();
        getEventTask.execute();
    }

    private void doGetMyCollect() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        getMyCollectTask = new GetMyCollectTask();
        getMyCollectTask.execute();
    }

    private void setContributeButton() {

        int count = canContributeList.size();

        if (count > 0) {

            for (int i = 0; i < count; i++) {

                boolean contributionstatus = (boolean) canContributeList.get(i).get("contributionstatus");

                if (contributionstatus) {

                    tvContribute.setText(R.string.pinpinbox_2_0_0_button_contribute_or_cancel);

                    if (strSpecialUrl != null && !strSpecialUrl.equals("") && !strSpecialUrl.equals("null")) {
                        tvExchange.setVisibility(View.VISIBLE);
                    }

                    break;
                } else {
                    tvContribute.setText(R.string.pinpinbox_2_0_0_event_contributors);
                    tvExchange.setVisibility(View.GONE);
                }

            }

        } else {

            tvContribute.setText(R.string.pinpinbox_2_0_0_event_contributors);
            tvExchange.setVisibility(View.GONE);

        }

    }


    @SuppressLint("StaticFieldLeak")
    public void resetCanContributeList() {

        canContributeList.clear();

        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... params) {
                getCanContributeList();
                return null;
            }


            @Override
            protected void onPostExecute(Object result) {
                super.onPostExecute(result);

                dissmissLoading();

                if (p17Result.equals("1")) {

                    setContributeButton();


                } else if (p17Result.equals("0")) {

                    DialogV2Custom.BuildError(mActivity, p17Message);

                } else if (p17Result.equals(Key.timeout)) {

                    connectInstability();

                } else {
                    DialogV2Custom.BuildUnKnow(mActivity, mActivity.getClass().getSimpleName());
                }


            }

        }.execute();


    }

    private void getCanContributeList() {
        String strJson = "";
        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P17_GetCloudAlbumList,
                    SetMapByProtocol.setParam17_getcloudalbumlist(id, token, "mine", "0,1000"), null);
            MyLog.Set("d", getClass(), "p17strJson => " + strJson);

        } catch (SocketTimeoutException timeout) {
            p17Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p17Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);
                if (p17Result.equals("1")) {
                    String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONArray jsonArray = new JSONArray(jdata);
                    int array = jsonArray.length();
                    int templateSize = eventTemplateList.size();
                    for (int i = 0; i < array; i++) {

                        JSONObject object = (JSONObject) jsonArray.get(i);

                        HashMap<String, Object> map = new HashMap<String, Object>();

                        String album = JsonUtility.GetString(object, ProtocolKey.album); //get album_id, name, cover, zipped, act
                        String template = JsonUtility.GetString(object, ProtocolKey.template); //get template_id
                        String user = JsonUtility.GetString(object, ProtocolKey.user); //user
                        String event = JsonUtility.GetString(object, ProtocolKey.event); //get can join event

                        JSONObject obj = new JSONObject(template);
                        String template_id = obj.getString(ProtocolKey.template_id);

                        JSONObject uj = new JSONObject(user);
                        String user_id = uj.getString(ProtocolKey.user_id);

                        JSONObject aj = new JSONObject(album);
                        String zipped = aj.getString(ProtocolKey.zipped);
                        String act = aj.getString(ProtocolKey.act);

                        for (int k = 0; k < templateSize; k++) {

                            if (template_id.equals(eventTemplateList.get(k))) { //如果protocol17獲取到的 template_id 等於適用活動的 template_id列其一

                                if (zipped.equals("1") && act.equals("open")) { //如果此作品zip已壓製並且隱私已打開

                                    if (user_id.equals(id)) { //此用戶ID為自己

                                        String album_id = aj.getString("album_id");
                                        String name = aj.getString("name");
                                        String cover = aj.getString("cover");
                                        String description = aj.getString("description");

                                        map.put("album_id", album_id);
                                        map.put("name", name);
                                        map.put("cover", cover);
                                        map.put("description", description);

                                        JSONArray eventArray = new JSONArray(event); //可參加投稿的活動列

                                        int eventCount = eventArray.length();
                                        for (int e = 0; e < eventCount; e++) {
                                            JSONObject o = (JSONObject) eventArray.get(e);
                                            String mEvent_id = o.getString("event_id");

                                            Log.e(TAG, "此活動ID => " + event_id);
                                            Log.e(TAG, "第" + e + "個活動ID => " + mEvent_id);

                                            if (mEvent_id.equals(event_id)) { //可參加投稿的活動列其一的活動ID 等於此次活動ID
                                                boolean contributionstatus = o.getBoolean("contributionstatus");
                                                map.put("contributionstatus", contributionstatus);

                                                MyLog.Set("d", mActivity.getClass(), "適用於此活動的作品 contributionstatus => " + contributionstatus);

                                                break;
                                            }
                                        }

                                        canContributeList.add(map);
                                    }
                                }
                            }
                        }
                    }
                } else if (p17Result.equals("0")) {
                    p17Message = jsonObject.getString(Key.message);
                } else {
                    p17Result = "";
                }


            } catch (Exception e) {

                p17Result = "";
                e.printStackTrace();
            }
        }
    }


    /**
     * 判斷活動時間 獲取活動用版型
     */
    private class GetEventTask extends AsyncTask<Void, Void, Object> {

        private String p76Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetEvent;
            startLoading();

        }

        @Override
        protected Void doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P76_GetEvent
                        , SetMapByProtocol.setParam76_getevent(id, token, event_id)
                        , null);

                MyLog.Set("d", getClass(), "p76strJson => " + strJson);


            } catch (SocketTimeoutException timeout) {
                p76Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p76Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p76Result == 1 || p76Result == 2) {

                        String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        Logger.json(data);


                        JSONObject jsonData = new JSONObject(data);

                        String event = JsonUtility.GetString(jsonData, ProtocolKey.event);
                        String event_templatejoin = JsonUtility.GetString(jsonData, ProtocolKey.event_templatejoin);
                        String special = JsonUtility.GetString(jsonData, ProtocolKey.special);

                        JSONObject jsonEvent = new JSONObject(event);
                        image = JsonUtility.GetString(jsonEvent, ProtocolKey.image);
                        url = JsonUtility.GetString(jsonEvent, ProtocolKey.url);
                        strName = JsonUtility.GetString(jsonEvent, ProtocolKey.name);
                        strTitle = JsonUtility.GetString(jsonEvent, ProtocolKey.title);
                        intPopularity = JsonUtility.GetInt(jsonEvent, ProtocolKey.popularity);
                        contribution = JsonUtility.GetInt(jsonEvent, ProtocolKey.contribution);
                        strPrefixText = JsonUtility.GetString(jsonEvent, ProtocolKey.prefix_text);

                        JSONObject jsonSpecial = new JSONObject(special);
                        strSpecialUrl = JsonUtility.GetString(jsonSpecial, ProtocolKey.url);

                        eventTemplateArray = new JSONArray(event_templatejoin);

                        for (int i = 0; i < eventTemplateArray.length(); i++) {

                            MyLog.Set("d", getClass(), eventTemplateArray.get(i) + "");

                            eventTemplateList.add(eventTemplateArray.get(i) + "");

                        }


                    } else if (p76Result == 0) {
                        p76Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            if (p76Result == 1 || p76Result == 2) {

                tvName.setText(strName);
                tvTitle.setText(strTitle);
                tvPopularity.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_popularity) + intPopularity);


                if (image != null && !image.equals("") && !image.isEmpty()) {

                    Picasso.with(mActivity.getApplicationContext())
                            .load(image)
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.bg_2_0_0_no_image)
                            .tag(mActivity.getApplicationContext())
                            .into(eventImg, new Callback() {
                                @Override
                                public void onSuccess() {
                                    ViewControl.AlphaTo1(findViewById(R.id.scrollView));
                                }

                                @Override
                                public void onError() {

                                    ViewControl.AlphaTo1(findViewById(R.id.scrollView));
                                }
                            });

                } else {

                    eventImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    eventImg.setImageResource(R.drawable.bg_2_0_0_no_image);
                    ViewControl.AlphaTo1(findViewById(R.id.scrollView));

                }


                if (p76Result == 1) {
                    doGetMyCollect();
                } else if (p76Result == 2) {
                    tvContribute.setBackgroundResource(R.drawable.click_2_0_0_second_grey_button_frame_white_radius);
                    tvContribute.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                    tvContribute.setText(R.string.pinpinbox_2_0_0_toast_message_event_is_finish);
                    tvContribute.setClickable(false);
                }


            } else if (p76Result == 0) {

                DialogV2Custom.BuildError(mActivity, p76Message);

            } else if (p76Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }

//        private void viewAnimStart() {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    ViewPropertyAnimator alphaTo1 = findViewById(R.id.scrollView).animate();
//                    alphaTo1.setDuration(400)
//                            .alpha(1)
//                            .start();
//
//                }
//            }, 200);
//        }


    }

    /**
     * 獲取我的收藏內能投稿的作品
     */
    public class GetMyCollectTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetMyCollect;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            getCanContributeList();

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p17Result.equals("1")) {

                setContributeButton();

            } else if (p17Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p17Message);
            } else if (p17Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }


//    public class SendContributeTask extends AsyncTask<Void, Void, Object> {
//
//        private String p73Message = "";
//
//        private int p73Result = -1;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            doingType = DoSendContribute;
//            startLoading();
//        }
//
//        @Override
//        protected Object doInBackground(Void... params) {
//            String strJson = "";
//            try {
//                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P73_SwitchStatusOfContribution
//                        , SetMapByProtocol.setParam73_switchstatusofcontribution(id, token, event_id, sendAlbum_id)
//                        , null);
//                MyLog.Set("json", getClass(), "p73strJson => " + strJson);
//
//            } catch (SocketTimeoutException timeout) {
//                p73Result = Key.TIMEOUT;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (strJson != null && !strJson.equals("")) {
//                try {
//                    JSONObject jsonObject = new JSONObject(strJson);
//                    p73Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
//                    if (p73Result == 0) {
//                        p73Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//            dissmissLoading();
//
//            if (p73Result == 1) {
//
//                canContributeList.clear();
//
//                PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_cancel);
//
//                canCreate = true;
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        boolean fastCreate = false;
//
//                        for (int i = 0; i < eventTemplateList.size(); i++) {
//
//                            String mTemid = eventTemplateList.get(i);
//                            if (mTemid.equals("0")) {
//                                fastCreate = true;
//                                break;
//                            }
//                        }
//
//                        if (fastCreate) {
//
//                            doFastCreate();
//
//                        } else {
//
//                            Bundle bundle = new Bundle();
//                            bundle.putBoolean(Key.isContribute, true);
//                            bundle.putString("event_id", event_id);
//                            bundle.putString("rank", "hot");//default
//
//                            Intent intent = new Intent(Event2Activity.this, TemList2Activity.class);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                            ActivityAnim.StartAnim(mActivity);
//
//                        }
//
//
//                    }
//                }, 300);
//
//            } else if (p73Result == 0) {
//                DialogV2Custom.BuildError(mActivity, p73Message);
//
//            } else if (p73Result == Key.TIMEOUT) {
//
//                connectInstability();
//
//            } else {
//                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
//            }
//
//        }
//    }

//    public class FastCreateTask extends AsyncTask<Void, Void, Object> {
//
//        private String p54Result, p54Message;
//        private String new_album_id;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            doingType = DoFastCreate;
//            startLoading();
//        }
//
//        @Override
//        protected Object doInBackground(Void... params) {
//            String strJson = "";
//            try {
//                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P54_InsertAlbumOfDiy,
//                        SetMapByProtocol.setParam54_insertalbumofdiy(id, token, "0"), null);
//                MyLog.Set("d", getClass(), "p54strJson => " + strJson);
//            } catch (SocketTimeoutException timeout) {
//                p54Result = Key.timeout;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (strJson != null && !strJson.equals("")) {
//                try {
//                    JSONObject jsonObject = new JSONObject(strJson);
//                    p54Result = jsonObject.getString(Key.result);
//                    if (p54Result.equals("1")) {
//                        new_album_id = jsonObject.getString(Key.data);
//                    } else if (p54Result.equals("0")) {
//                        p54Message = jsonObject.getString(Key.message);
//                    } else {
//                        p54Result = "";
//                    }
//
//                } catch (Exception e) {
//                    p54Result = "";
//                    e.printStackTrace();
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//            dissmissLoading();
//            if (p54Result.equals("1")) {
//
//                if (new_album_id != null) {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString("album_id", new_album_id);
//                    bundle.putString("identity", "admin");
//                    bundle.putInt("create_mode", 0);
//                    bundle.putBoolean(Key.isContribute, true);
//                    bundle.putBoolean(Key.isNewCreate, true);
//                    bundle.putString("event_id", event_id);
//                    Intent intent = new Intent(mActivity, Creation2Activity.class);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                    ActivityAnim.StartAnim(mActivity);
//                }
//            } else if (p54Result.equals("0")) {
//
//                DialogV2Custom.BuildError(mActivity, p54Message);
//
//            } else if (p54Result.equals(Key.timeout)) {
//                connectInstability();
//            } else {
//                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
//            }
//
//        }
//    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        switch (view.getId()) {
            case R.id.tvContribute:

                if (p76Result == 2) {

                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_event_is_finish);

                } else if (p76Result == 1) {


                    FlurryUtil.onEvent(FlurryKey.event_select_choose_own_work);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("templates", eventTemplateList);
                    bundle.putString("event_id", event_id);
                    bundle.putInt("contribution", contribution);
                    bundle.putString(Key.prefix_text, strPrefixText);
                    bundle.putString(Key.special, strSpecialUrl);
                    Intent intent = new Intent(mActivity, SelectMyWorks2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    ActivityAnim.StartAnim(mActivity);


                }

                break;

            case R.id.tvEvent:
                FlurryUtil.onEvent(FlurryKey.event_click_to_web_contants);
                toEvent();
                break;

            case R.id.tvExit:

                onBackPressed();

                break;

            case R.id.linVote:

                toVote();

                break;

            case R.id.tvExchange:

                toSpecialUrl();


                break;


        }


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onDestroy() {

        if (getEventTask != null && !getEventTask.isCancelled()) {
            getEventTask.cancel(true);
            getEventTask = null;
        }

        if (getMyCollectTask != null && !getMyCollectTask.isCancelled()) {
            getMyCollectTask.cancel(true);
            getMyCollectTask = null;
        }

//        if (sendContributeTask != null && !sendContributeTask.isCancelled()) {
//            sendContributeTask.cancel(true);
//            sendContributeTask = null;
//        }

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (image != null && !image.equals("null") && !image.equals("")) {

            Picasso.with(mActivity.getApplicationContext()).invalidate(image);

        }


        System.gc();

        super.onDestroy();
    }


}

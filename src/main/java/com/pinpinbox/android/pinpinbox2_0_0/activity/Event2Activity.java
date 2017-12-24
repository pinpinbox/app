package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.DialogTool.DialogCheckContribute;
import com.pinpinbox.android.DialogTool.DialogV2Custom;
import com.pinpinbox.android.Mode.LOG;
import com.pinpinbox.android.PopupTool.PopupCustom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.SelfMadeClass.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.TagClass;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Widget.ActivityAnim;
import com.pinpinbox.android.Widget.FlurryKey;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.PPBWidget;
import com.pinpinbox.android.Widget.PinPinToast;
import com.pinpinbox.android.Widget.ProtocolKey;
import com.pinpinbox.android.Widget.SetMapByProtocol;
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
    private SendContributeTask sendContributeTask;
    private FastCreateTask fastCreateTask;

    private String TAG = TagClass.TagEventActivity;
    private String id, token;
    private String url, event_id, image, strName, strTitle;
    private String p17Result, p17Message;
    private String sendAlbum_id;

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
    private TextView tvName, tvTitle, tvEvent, tvContribute, tvExit;
    private LinearLayout linVote;

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

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

        tvName = (TextView) findViewById(R.id.tvName);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvEvent = (TextView) findViewById(R.id.tvEvent);
        tvContribute = (TextView) findViewById(R.id.tvContribute);
        tvExit = (TextView)findViewById(R.id.tvExit);

        eventImg = (ImageView) findViewById(R.id.eventImg);
//        backImg = (ImageView) findViewById(R.id.backImg);

        TextUtility.setBold(tvName, true);
        TextUtility.setBold(tvEvent, true);
        TextUtility.setBold(tvContribute, true);
        TextUtility.setBold((TextView) findViewById(R.id.tvVote), true);


        tvEvent.setOnClickListener(this);
        tvContribute.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        linVote.setOnClickListener(this);

    }

    private void select() {


        if (popContributeWay == null) {

            popContributeWay = new PopupCustom(mActivity);
            popContributeWay.setPopup(R.layout.pop_2_0_0_select_send_contribute_way, R.style.pinpinbox_popupAnimation_bottom);
            View v = popContributeWay.getPopupView();
            TextView tvCreate = (TextView) v.findViewById(R.id.tvCreate);
            TextView tvSelect = (TextView) v.findViewById(R.id.tvSelect);
            TextUtility.setBold((TextView) v.findViewById(R.id.tvTitle), true);
            TextUtility.setBold(tvCreate, true);
            TextUtility.setBold(tvSelect, true);

            tvCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popContributeWay.dismiss();
                    FlurryUtil.onEvent(FlurryKey.event_select_create_new_work);
                    createNewWork();
                }
            });

            tvSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popContributeWay.dismiss();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FlurryUtil.onEvent(FlurryKey.event_select_choose_own_work);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList("templates", eventTemplateList);
                            bundle.putString("event_id", event_id);
                            Intent intent = new Intent(Event2Activity.this, SelectMyWorks2Activity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);
                        }
                    }).start();

                }
            });
            popContributeWay.show((RelativeLayout) findViewById(R.id.rBackground));
        } else {
            popContributeWay.show((RelativeLayout) findViewById(R.id.rBackground));
        }

    }

    private void createNewWork() {

        int count = canContributeList.size();

        /**先判斷有無投稿的作品*/
        for (int i = 0; i < count; i++) {
            boolean b = (boolean) canContributeList.get(i).get("contributionstatus");
            MyLog.Set("d", getClass(), "contributionstatus => " + b);
            if (b) {
                /**已有投稿作品*/
                canCreate = false;
                sendAlbum_id = (String) canContributeList.get(i).get("album_id");
                String name = (String) canContributeList.get(i).get("name");
                String cover = (String) canContributeList.get(i).get("cover");

                final DialogCheckContribute d = new DialogCheckContribute(mActivity);
                d.getTvTitle().setText(name);
                d.getTvDirections().setTextColor(Color.parseColor(ColorClass.PINK_FRIST));
                d.getTvDirections().setText(R.string.pinpinbox_2_0_0_dialog_message_check_is_contribute);
                Picasso.with(mActivity.getApplicationContext())
                        .load(cover)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.bg_2_0_0_no_image)
                        .centerInside()
                        .resize(112, 168)
                        .tag(mActivity.getApplicationContext())
                        .into(d.getCoverImg());

                d.getTvY().setText(R.string.pinpinbox_2_0_0_button_replace);
                d.getTvY().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.getDialog().dismiss();
                        doSendContribute();
                    }
                });
                break;
            }
        }

        /**沒有投稿作品 */
        if (canCreate) {

            boolean fastCreate = false;

            for (int i = 0; i < eventTemplateList.size(); i++) {

                String mTemid = eventTemplateList.get(i);
                if (mTemid.equals("0")) {
                    fastCreate = true;
                    break;
                }

            }

            if (fastCreate) {

                doFastCreate();

            } else {

                /*2017.08.02 目前沒有使用套版 如活動需求 要新增SubResultActivity*/

                Bundle bundle = new Bundle();
                bundle.putBoolean(Key.isContribute, true);
                bundle.putString("event_id", event_id);
                bundle.putString("rank", "hot");//default

                Intent intent = new Intent(Event2Activity.this, TemList2Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityAnim.StartAnim(mActivity);

            }

        }

    }

    private void toEvent() {

        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        startActivity(intent);



        //20171206
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//
//        Intent intent = new Intent(Event2Activity.this, WebView2Activity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        ActivityAnim.StartAnim(mActivity);

    }

    private void toVote() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.event_id, event_id);

        Intent intent = new Intent(mActivity, Vote2Activity.class);
        intent.putExtras(bundle);

        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);


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

                    case DoSendContribute:
                        doSendContribute();
                        break;


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

    private void doSendContribute() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        sendContributeTask = new SendContributeTask();
        sendContributeTask.execute();
    }

    private void doFastCreate() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        fastCreateTask = new FastCreateTask();
        fastCreateTask.execute();

    }

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


                } else if (p17Result.equals("0")) {

                    DialogV2Custom.BuildError(mActivity, p17Message);

                } else if (p17Result.equals(Key.timeout)) {

                    connectInstability();

                } else {
                    DialogV2Custom.BuildUnKnow(mActivity, mActivity.getClass().getSimpleName());
                }


            }

        };


    }

    private void getCanContributeList() {
        String strJson = "";
        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P17_GetCloudAlbumList,
                    SetMapByProtocol.setParam17_getcloudalbumlist(id, token, "mine", "0,500"), null);
            MyLog.Set("d", getClass(), "p17strJson => " + strJson);

        } catch (SocketTimeoutException timeout) {
            p17Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p17Result = jsonObject.getString("result");
                if (p17Result.equals("1")) {
                    String jdata = jsonObject.getString("data");
                    JSONArray jsonArray = new JSONArray(jdata);
                    int array = jsonArray.length();
                    int templateSize = eventTemplateList.size();
                    for (int i = 0; i < array; i++) {

                        JSONObject object = (JSONObject) jsonArray.get(i);

                        HashMap<String, Object> map = new HashMap<String, Object>();

                        String album = PPBWidget.GetStringByJsonObject(object, "album"); //get album_id, name, cover, zipped, act
                        String template = PPBWidget.GetStringByJsonObject(object, "template"); //get template_id
                        String user = PPBWidget.GetStringByJsonObject(object, "user"); //user
                        String event = PPBWidget.GetStringByJsonObject(object, "event"); //get can join event

                        JSONObject obj = new JSONObject(template);
                        String template_id = obj.getString("template_id");

                        JSONObject uj = new JSONObject(user);
                        String user_id = uj.getString("user_id");

                        JSONObject aj = new JSONObject(album);
                        String zipped = aj.getString("zipped");
                        String act = aj.getString("act");

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
                                        if (LOG.isLogMode) {
                                            Log.d(TAG, "適用於此活動的作品 user_id => " + user_id);
                                            Log.d(TAG, "適用於此活動的作品 template_id => " + template_id);
                                            Log.d(TAG, "適用於此活動的作品 album_id => " + album_id);
                                            Log.d(TAG, "適用於此活動的作品 name => " + name);
                                            Log.d(TAG, "適用於此活動的作品 cover => " + cover);
                                            Log.d(TAG, "適用於此活動的作品 description => " + description);
                                            System.out.println("------------------------------------------------------------------------------------------------------");
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

                        JSONObject jsonEvent = new JSONObject(event);
                        image = JsonUtility.GetString(jsonEvent, ProtocolKey.image);
                        url = JsonUtility.GetString(jsonEvent, ProtocolKey.url);
                        strName = JsonUtility.GetString(jsonEvent, ProtocolKey.name);
                        strTitle = JsonUtility.GetString(jsonEvent, ProtocolKey.title);

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


                if(image!=null && !image.equals("") && !image.isEmpty()) {

                    Picasso.with(mActivity.getApplicationContext())
                            .load(image)
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.bg_2_0_0_no_image)
                            .tag(mActivity.getApplicationContext())
                            .into(eventImg, new Callback() {
                                @Override
                                public void onSuccess() {
                                    viewAnimStart();
                                }

                                @Override
                                public void onError() {

                                    viewAnimStart();
                                }
                            });

                }else {

                    eventImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    eventImg.setImageResource(R.drawable.bg_2_0_0_no_image);

                    viewAnimStart();

                }






            } else if (p76Result == 0) {

                DialogV2Custom.BuildError(mActivity, p76Message);

            } else if (p76Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }

        private void viewAnimStart(){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ViewPropertyAnimator alphaTo1 = findViewById(R.id.scrollView).animate();
                    alphaTo1.setDuration(400)
                            .alpha(1)
                            .start();

                }
            }, 200);
        }


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

                int count = canContributeList.size();

                if (count > 0) {
                    select();
                } else {
                    createNewWork();
                }

            } else if (p17Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p17Message);
            } else if (p17Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }

    public class SendContributeTask extends AsyncTask<Void, Void, Object> {

        private String p73Message = "";

        private int p73Result = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoSendContribute;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P73_SwitchStatusOfContribution
                        , SetMapByProtocol.setParam73_switchstatusofcontribution(id, token, event_id, sendAlbum_id)
                        , null);
                MyLog.Set("json", getClass(), "p73strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p73Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p73Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p73Result == 0) {
                        p73Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            if (p73Result == 1) {

                canContributeList.clear();

                PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_cancel);

                canCreate = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        boolean fastCreate = false;

                        for (int i = 0; i < eventTemplateList.size(); i++) {

                            String mTemid = eventTemplateList.get(i);
                            if (mTemid.equals("0")) {
                                fastCreate = true;
                                break;
                            }
                        }

                        if (fastCreate) {

                            doFastCreate();

                        } else {

                            Bundle bundle = new Bundle();
                            bundle.putBoolean(Key.isContribute, true);
                            bundle.putString("event_id", event_id);
                            bundle.putString("rank", "hot");//default

                            Intent intent = new Intent(Event2Activity.this, TemList2Activity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);

                        }


                    }
                }, 300);

            } else if (p73Result == 0) {
                DialogV2Custom.BuildError(mActivity, p73Message);

            } else if (p73Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }

        }
    }

    public class FastCreateTask extends AsyncTask<Void, Void, Object> {

        private String p54Result, p54Message;
        private String new_album_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoFastCreate;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P54_InsertAlbumOfDiy,
                        SetMapByProtocol.setParam54_insertalbumofdiy(id, token, "0"), null);
                MyLog.Set("d", getClass(), "p54strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p54Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p54Result = jsonObject.getString(Key.result);
                    if (p54Result.equals("1")) {
                        new_album_id = jsonObject.getString(Key.data);
                    } else if (p54Result.equals("0")) {
                        p54Message = jsonObject.getString(Key.message);
                    } else {
                        p54Result = "";
                    }

                } catch (Exception e) {
                    p54Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p54Result.equals("1")) {

                if (new_album_id != null) {

                    Bundle bundle = new Bundle();
                    bundle.putString("album_id", new_album_id);
                    bundle.putString("identity", "admin");
                    bundle.putInt("create_mode", 0);
                    bundle.putBoolean(Key.isContribute, true);
                    bundle.putBoolean(Key.isNewCreate, true);
                    bundle.putString("event_id", event_id);
                    Intent intent = new Intent(mActivity, Creation2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    ActivityAnim.StartAnim(mActivity);
                }
            } else if (p54Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p54Message);

            } else if (p54Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }

        }
    }


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
                    doGetMyCollect();
                }

                break;

            case R.id.tvEvent:
                FlurryUtil.onEvent(FlurryKey.event_click_to_web_contants);
                toEvent();
                break;

            case R.id.tvExit:

                back();

                break;

            case R.id.linVote:

                toVote();

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

        if (sendContributeTask != null && !sendContributeTask.isCancelled()) {
            sendContributeTask.cancel(true);
            sendContributeTask = null;
        }

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (image != null && !image.equals("null") && !image.equals("")) {

            Picasso.with(mActivity.getApplicationContext()).invalidate(image);

        }


        System.gc();

        super.onDestroy();
    }


}

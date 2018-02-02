package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.SelectMyWorksAdpater;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogCheckContribute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2016/5/26.
 */
public class SelectMyWorks2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private SelectMyWorksAdpater adapter;

    private GetMyCollectTask getMyCollectTask;
    private SendContributeTask sendContributeTask;
    private FastCreateTask fastCreateTask;

    private ArrayList<String> templateidList;
    private ArrayList<ItemAlbum> canContributeAlbumList;

    private String id, token;
    private String event_id;
    private String p17Result, p17Message;
    private String p73Result, p73Message;
    private String sendAlbum_id;
    private String strPrefixText;

    private int doingType;
    private static final int DoGetMyCollect = 0;
    private static final int DoSendContribute = 1;
    private static final int DoFastCreate = 2;
    private int sendPosition;
    private int sendMaxCount = 0;
    private int isSendCount = 0;

    private ImageView backImg;
    private TextView tvCreate;
    private GridView gridView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_select_mywroks);
        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();
        init();

        doGetMyCollect();

    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            templateidList = bundle.getStringArrayList("templates");
            event_id = bundle.getString("event_id");
            sendMaxCount = bundle.getInt("contribution");
            strPrefixText = bundle.getString(Key.prefix_text, "");
        }
    }

    private void init() {
        mActivity = this;
        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        canContributeAlbumList = new ArrayList<>();

        backImg = (ImageView) findViewById(R.id.backImg);
        gridView = (GridView) findViewById(R.id.gridView);
        tvCreate = (TextView) findViewById(R.id.tvCreate);

        TextUtility.setBold((TextView) findViewById(R.id.tvTitle), true);
        
        backImg.setOnClickListener(this);
        tvCreate.setOnClickListener(this);

    }

    private void select() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String album_id = canContributeAlbumList.get(position).getAlbum_id();
                String name = canContributeAlbumList.get(position).getName();
                String cover = canContributeAlbumList.get(position).getCover();


                final DialogCheckContribute d = new DialogCheckContribute(mActivity);
                d.getTvTitle().setText(name);

                if (cover != null && !cover.equals("") && !cover.equals("null")) {
                    Picasso.with(mActivity.getApplicationContext())
                            .load(cover)
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.bg_2_0_0_no_image)
                            .fit()
                            .centerCrop()
                            .tag(mActivity.getApplicationContext())
                            .into(d.getCoverImg());
                } else {
                    d.getCoverImg().setImageResource(R.drawable.bg_2_0_0_no_image);
                }


                if (canContributeAlbumList.get(position).isContributionstatus()) {
                    d.getTvDirections().setTextColor(Color.parseColor(ColorClass.PINK_FRIST));
                    d.getTvDirections().setText(R.string.pinpinbox_2_0_0_dialog_message_reset_contribute);
                    d.getTvDirections().setGravity(View.FOCUS_LEFT);
                    d.getTvY().setText(R.string.pinpinbox_2_0_0_button_re_select);
                } else {
                    d.getTvDirections().setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                    d.getTvDirections().setText(R.string.pinpinbox_2_0_0_dialog_message_directions_works_contribute);
                    d.getTvDirections().setGravity(View.FOCUS_LEFT);
                    d.getTvY().setText(R.string.pinpinbox_2_0_0_button_submit);
                }


                d.getTvY().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        d.getDialog().dismiss();

                        sendAlbum_id = album_id;
                        sendPosition = position;

                        doSendContribute();

                    }
                });

            }
        });


    }

    private void createNewWork() {

        boolean fastCreate = false;

        for (int i = 0; i < templateidList.size(); i++) {

            String mTemid = templateidList.get(i);
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

            Intent intent = new Intent(mActivity, TemList2Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            ActivityAnim.StartAnim(mActivity);

        }

    }

    private void cleanCache() {

        int count = canContributeAlbumList.size();


        for (int i = 0; i < count; i++) {
            Picasso.with(mActivity.getApplicationContext()).invalidate(canContributeAlbumList.get(i).getCover());
        }

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

                    case DoGetMyCollect:
                        doGetMyCollect();
                        break;

                    case DoSendContribute:
                        doSendContribute();
                        break;

                    case DoFastCreate:
                        doFastCreate();
                        break;


                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

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

    public class GetMyCollectTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetMyCollect;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P17_GetCloudAlbumList, SetMapByProtocol.setParam17_getcloudalbumlist(id, token, "mine", "0,1000"), null);
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
                        int templateSize = templateidList.size();

                        for (int i = 0; i < array; i++) {

                            JSONObject object = (JSONObject) jsonArray.get(i);

                            ItemAlbum itemAlbum = new ItemAlbum();

                            String album = JsonUtility.GetString(object, ProtocolKey.album); //get album_id, name, cover, zipped, act
                            String template = JsonUtility.GetString(object, ProtocolKey.template); //get template_id
                            String user = JsonUtility.GetString(object, ProtocolKey.user); //user
                            String event = JsonUtility.GetString(object, ProtocolKey.event); //get can join event

                            JSONObject jsonTemplate = new JSONObject(template);
                            String template_id = JsonUtility.GetString(jsonTemplate, ProtocolKey.template_id);

                            JSONObject jsonUser = new JSONObject(user);
                            String user_id = JsonUtility.GetString(jsonUser, ProtocolKey.user_id);

                            JSONObject jsonAlbum = new JSONObject(album);

                            String zipped = JsonUtility.GetString(jsonAlbum, ProtocolKey.zipped);
                            String act = JsonUtility.GetString(jsonAlbum, ProtocolKey.act);


                            for (int k = 0; k < templateSize; k++) {

                                if (template_id.equals(templateidList.get(k))) { //如果protocol17獲取到的 template_id 等於適用活動的 template_id列其一

                                    if (zipped.equals("1") && act.equals("open")) { //如果此作品zip已壓製並且隱私已打開

                                        if (user_id.equals(id)) { //此用戶ID為自己


                                            itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                                            itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_name));
                                            itemAlbum.setCover(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover));
                                            itemAlbum.setDescription(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_description));


                                            JSONArray eventArray = new JSONArray(event); //可參加投稿的活動列

                                            MyLog.Set("d", mActivity.getClass(), "eventArray.toString()" + eventArray.toString());


                                            int eventCount = eventArray.length();

                                            for (int e = 0; e < eventCount; e++) {

                                                JSONObject jsonEvent = (JSONObject) eventArray.get(e);

                                                String mEvent_id = JsonUtility.GetString(jsonEvent, ProtocolKey.event_id);

                                                MyLog.Set("d", mActivity.getClass(), "此活動ID => " + event_id);
                                                MyLog.Set("d", mActivity.getClass(), "第" + e + "個活動ID => " + mEvent_id);

                                                if (mEvent_id.equals(event_id)) {

                                                    boolean contributionstatus = JsonUtility.GetBoolean(jsonEvent, ProtocolKey.contributionstatus);
                                                    itemAlbum.setContributionstatus(contributionstatus);

                                                    if(contributionstatus){
                                                        isSendCount++;
                                                    }

                                                    break;
                                                }
                                            }

                                            canContributeAlbumList.add(itemAlbum);


                                        }
                                    }
                                }
                            }
                        }


                    } else if (p17Result.equals("0")) {
                        p17Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    } else {
                        p17Result = "";
                    }

                } catch (Exception e) {

                    p17Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p17Result.equals("1")) {

                adapter = new SelectMyWorksAdpater(mActivity, canContributeAlbumList);
                gridView.setAdapter(adapter);

                select();

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

        private boolean contributionstatus;

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
                MyLog.Set("d", mActivity.getClass(), "p73strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p73Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p73Result = jsonObject.getString(Key.result);
                    if (p73Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);

                        JSONObject object = new JSONObject(jdata);
                        String event = object.getString(Key.event);

                        JSONObject obj = new JSONObject(event);
                        contributionstatus = obj.getBoolean("contributionstatus");

                    } else if (p73Result.equals("0")) {
                        p73Message = jsonObject.getString(Key.message);
                    } else {
                        p73Result = "";
                    }

                } catch (Exception e) {
                    p73Result = "";
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p73Result.equals("1")) {

                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();

                if (contributionstatus) {

                    PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_success);

                    for (int i = 0; i < activityList.size(); i++) {

                        String strClassName = activityList.get(i).getClass().getSimpleName();

                        MyLog.Set("e", this.getClass(), "strClassName => " + strClassName);

                        if (strClassName.equals(Event2Activity.class.getSimpleName())) {

                            MyLog.Set("e", this.getClass(), "---------------------------------------------");

                            ((Event2Activity) activityList.get(i)).resetCanContributeList();

                            break;
                        }

                    }


                    canContributeAlbumList.get(sendPosition).setContributionstatus(true);
                    isSendCount++;


                } else {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_cancel);

                    for (int i = 0; i < activityList.size(); i++) {

                        String strClassName = activityList.get(i).getClass().getSimpleName();

                        MyLog.Set("e", this.getClass(), "strClassName => " + strClassName);

                        if (strClassName.equals(Event2Activity.class.getSimpleName())) {

                            MyLog.Set("e", this.getClass(), "---------------------------------------------");

                            ((Event2Activity) activityList.get(i)).resetCanContributeList();

                            break;
                        }

                    }

                    canContributeAlbumList.get(sendPosition).setContributionstatus(false);
                    isSendCount--;

                }

                adapter.notifyDataSetChanged();

            } else if (p73Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p73Message);
            } else if (p73Result.equals(Key.timeout)) {

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

                    p54Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);

                    if (p54Result.equals("1")) {
                        new_album_id = jsonObject.getString(ProtocolKey.data);
                    } else if (p54Result.equals("0")) {
                        p54Message = jsonObject.getString(ProtocolKey.message);
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
                    bundle.putString(Key.prefix_text, strPrefixText);
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onClick(View v) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }
        
        switch (v.getId()){
            
            case R.id.tvCreate:


                if(isSendCount>=sendMaxCount){
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_is_max);
                }else {

                    MyLog.Set("e", this.getClass(), "開始建立作品");
                    createNewWork();
                }

                
                break;
                
            case R.id.backImg:
                
                back();
                
                break;
            
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {

            if (getNoConnect() == null) {
                setNoConnect();
            }
        }
    }

    @Override
    protected void onPause() {

        cleanCache();



        super.onPause();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroy() {

        cancelTask(getMyCollectTask);
        getMyCollectTask = null;

        cancelTask(sendContributeTask);
        sendContributeTask = null;

        cancelTask(fastCreateTask);
        fastCreateTask = null;

        Recycle.IMG(backImg);

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        System.gc();

        super.onDestroy();
    }


}

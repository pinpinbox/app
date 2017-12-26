package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.Mode.LOG;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.TagClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.SelectMyWorksAdpater;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogCheckContribute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2016/5/26.
 */
public class SelectMyWorks2Activity extends DraggerActivity {

    private Activity mActivity;

    private SelectMyWorksAdpater adapter;

    private GetMyCollectTask getMyCollectTask;
    private SendContributeTask sendContributeTask;

    private ArrayList<String> templateidList;
    private ArrayList<HashMap<String, Object>> canContributeList;

    private String TAG = TagClass.TagSelectMyWorksActivity;
    private String id, token;
    private String event_id;
    private String p17Result, p17Message;
    private String p73Result, p73Message;
    private String sendAlbum_id;

    private int doingType;
    private static final int DoGetMyCollect = 0;
    private static final int DoSendContribute = 1;
    private int sendPosition;

    private ImageView backImg;
    private GridView gridView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_select_mywroks);
        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();
        init();
        back();

        doGetMyCollect();

    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            templateidList = bundle.getStringArrayList("templates");
            event_id = bundle.getString("event_id");
        }
    }

    private void init() {
        mActivity = this;
        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        canContributeList = new ArrayList<>();

        backImg = (ImageView) findViewById(R.id.backImg);
        gridView = (GridView) findViewById(R.id.gridView);

        TextUtility.setBold((TextView)findViewById(R.id.tvTitle), true);

    }

    private void select() {

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final String album_id = (String) canContributeList.get(position).get("album_id");
                String name = (String) canContributeList.get(position).get("name");
                String cover = (String) canContributeList.get(position).get("cover");

                boolean contributionstatus = (boolean) canContributeList.get(position).get("contributionstatus");

                final DialogCheckContribute d = new DialogCheckContribute(mActivity);
                d.getTvTitle().setText(name);

                Picasso.with(mActivity.getApplicationContext())
                        .load(cover)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.bg_2_0_0_no_image)
                        .centerInside()
                        .resize(112, 168)
                        .tag(mActivity.getApplicationContext())
                        .into(d.getCoverImg());

                if (contributionstatus) {
                    d.getTvDirections().setTextColor(Color.parseColor(ColorClass.PINK_FRIST));
                    d.getTvDirections().setText(R.string.pinpinbox_2_0_0_dialog_message_reset_contribute);
                    d.getTvDirections().setGravity(View.FOCUS_LEFT);
                    d.getTvY().setText(R.string.pinpinbox_2_0_0_button_re_select);
                }else {
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

    private void cleanPicasso() {

        int count = canContributeList.size();

        for (int i = 0; i < count; i++) {
            String cover = (String) canContributeList.get(i).get("cover");
            Picasso.with(mActivity.getApplicationContext()).invalidate(cover);
        }

    }

    private void back() {

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivityAnim.FinishAnim(mActivity);
            }
        });

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
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P17_GetCloudAlbumList, SetMapByProtocol.setParam17_getcloudalbumlist(id, token, "mine", "0,300"), null);
                MyLog.Set("d", getClass(), "p17strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p17Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p17Result = jsonObject.getString(Key.result);
                    if (p17Result.equals("1")) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        int array = jsonArray.length();
                        int templateSize = templateidList.size();

                        for (int i = 0; i < array; i++) {

                            JSONObject object = (JSONObject) jsonArray.get(i);

                            HashMap<String, Object> map = new HashMap<String, Object>();

                            String album = JsonUtility.GetString(object, "album"); //get album_id, name, cover, zipped, act
                            String template = JsonUtility.GetString(object, "template"); //get template_id
                            String user = JsonUtility.GetString(object, "user"); //user
                            String event = JsonUtility.GetString(object, "event"); //get can join event

                            JSONObject obj = new JSONObject(template);
                            String template_id = obj.getString("template_id");

                            JSONObject uj = new JSONObject(user);
                            String user_id = uj.getString("user_id");

                            JSONObject aj = new JSONObject(album);
                            String zipped = aj.getString("zipped");
                            String act = aj.getString("act");


                            for (int k = 0; k < templateSize; k++) {

                                if (template_id.equals(templateidList.get(k))) { //如果protocol17獲取到的 template_id 等於適用活動的 template_id列其一

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

                                            MyLog.Set("d", mActivity.getClass(), "eventArray.toString()" + eventArray.toString());


                                            int eventCount = eventArray.length();

                                            for (int e = 0; e < eventCount; e++) {

                                                JSONObject o = (JSONObject) eventArray.get(e);

                                                String mEvent_id = o.getString("event_id");

                                                MyLog.Set("d", mActivity.getClass(), "此活動ID => " + event_id);
                                                MyLog.Set("d", mActivity.getClass(), "第" + e + "個活動ID => " + mEvent_id);

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

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p17Result.equals("1")) {

                adapter = new SelectMyWorksAdpater(mActivity, canContributeList);
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
                        if (strClassName.equals("Event2Activity")) {

                            ((Event2Activity) activityList.get(i)).resetCanContributeList();

                            break;
                        }

                    }


                    HashMap<String, Object> map = new HashMap<>();
                    map.put("album_id", (String) canContributeList.get(sendPosition).get("album_id"));
                    map.put("name", (String) canContributeList.get(sendPosition).get("name"));
                    map.put("cover", (String) canContributeList.get(sendPosition).get("cover"));
                    map.put("description", (String) canContributeList.get(sendPosition).get("description"));
                    map.put("contributionstatus", true);
                    canContributeList.set(sendPosition, map);

                } else {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_cancel);

                    for (int i = 0; i < activityList.size(); i++) {

                        String strClassName = activityList.get(i).getClass().getSimpleName();
                        if (strClassName.equals("Event2Activity")) {

                            ((Event2Activity) activityList.get(i)).resetCanContributeList();

                            break;
                        }

                    }


                    HashMap<String, Object> map = new HashMap<>();
                    map.put("album_id", (String) canContributeList.get(sendPosition).get("album_id"));
                    map.put("name", (String) canContributeList.get(sendPosition).get("name"));
                    map.put("cover", (String) canContributeList.get(sendPosition).get("cover"));
                    map.put("description", (String) canContributeList.get(sendPosition).get("description"));
                    map.put("contributionstatus", false);
                    canContributeList.set(sendPosition, map);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        finish();
        ActivityAnim.FinishAnim(mActivity);
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
        super.onPause();
    }

    @Override
    public void onDestroy() {


        if (getMyCollectTask != null && !getMyCollectTask.isCancelled()) {
            getMyCollectTask.cancel(true);
            getMyCollectTask = null;
        }

        if (sendContributeTask != null && !sendContributeTask.isCancelled()) {
            sendContributeTask.cancel(true);
            sendContributeTask = null;
        }

        cleanPicasso();

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);


        System.gc();

        super.onDestroy();
    }


}

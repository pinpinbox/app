package com.pinpinbox.android.Test.Templateinfo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.Test.PointActivity;
import com.pinpinbox.android.Test.CreateAlbum.CreateAlbumActivity;
import com.pinpinbox.android.Test.CreateAlbum.TemplateOwn;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogSet;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.mode.LOG;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TagClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.LinkText;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Creation2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2016/4/20
 */
public class TemplateInfoActivity extends DraggerActivity {

    private Activity mActivity;

    private GetAlbumTask getAlbumTask;
    private GetPointTask getPointTask;
    private BuyTemplateTask buyTemplateTask;
    private GetTemplateTask getTemplateTask;
    private GetReportTask getReportTask;
    private SendReportTask sendReportTask;
    private FirstDownTemTask firstDownTemTask;


    private Intent intentToCreation;

    private TemplateInformationHorAdpater otherAdpater, sampleAdapter;

    private ArrayList<HashMap<String, Object>> otherList, sampleList;
    private ArrayList<HashMap<String, Object>> reportList;
    private List<String> picList;
    private List<String> strReportList;

    private SharedPreferences getdata;

    private DialogSet dConfirm, dCheckBuyPoint;

    private String TAG = TagClass.TagTemplateInfoActivity;
    private String id, token;
    private String SDpath;
    private String myDir;


    //    tvPrize
    private TextView tvName, tvDescription, tvAuthor;
    //    private GridView gvContents;
//    private LinearLayout linContents;
    private ImageView backImg;
    private RelativeLayout rReport;
    private RecyclerView rcContents;


    private String template_id;
    private String temDescription, temName, temUsername;
    private String sTemplate_id, sAlbum_id;
    private String userPoint;
    private String p23Result, p23Message;
    private String p37Result, p37Message;
    private String p38Result, p38Message;
    private String p54Result, p54Message;
    private String p69Result, p69Message;
    private String p70Result, p70Message;
    private String p83Result, p83Message;
    private String editing_album_id;
    private String new_album_id;
    private String report_id;
    private String strReportSelect;
    private String event_id;

    private boolean temOwn;


    private int intPoint;
    private static int typePIC = 0;
    private static int typeOther = 1;
    private static int typeSample = 2;

    private int doingType;
    private static final int DoGetAlbum = 0;
    private static final int DoGetPoint = 1;
    private static final int DoBuyTemplate = 2;
    private static final int DoGetTemplate = 3;
    private static final int DoGetReport = 4;
    private static final int DoSendReport = 5;
    private static final int DoFirstDownTem = 6;

    private int MODE = 0;
    private int CONTRIBUTEMODE = 1;

    private Handler checkAlbumHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    checkOwn();
                    break;
                case 1:

                    //購買成功後
                    doGetAlbum();
                    break;
            }
        }
    };

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_templateinfo);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                SystemBarTintManager tintManager = new SystemBarTintManager(TemplateInfoActivity.this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.status_color_default);
//            }
//        });


        getBundleTemplate_id();

        init();

        doGetTemplate();


    }


    private void getBundleTemplate_id() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            template_id = bundle.getString("template_id");
            event_id = bundle.getString("event_id", "");
            MODE = bundle.getInt("contributeMode", 0);

        }

    }

    private void init() {

        mActivity = this;


        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
        userPoint = PPBApplication.getInstance().getData().getString("", "");

        myDir = "PinPinBox" + id + "/";
        SDpath = Environment.getExternalStorageDirectory() + "/";


//        tvName = (TextView) findViewById(R.id.tem_info_name);
        tvDescription = (TextView) findViewById(R.id.tem_info_description);

        tvAuthor = (TextView) findViewById(R.id.tem_info_author);


        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcContents = (RecyclerView) findViewById(R.id.rcContents);
        rcContents.setLayoutManager(layoutManager);


        backImg = (ImageView) findViewById(R.id.backImg);

//        rConfirm = (RelativeLayout) findViewById(R.id.rConfirm);
        rReport = (RelativeLayout) findViewById(R.id.rReport);

        picList = new ArrayList<>();
        otherList = new ArrayList<>();
        sampleList = new ArrayList<>();
        reportList = new ArrayList<>();
        strReportList = new ArrayList<>();

    }

    private void isCanUse() {

//        btConfirm.setBackgroundColor(Color.parseColor("#009688")); //teal500
//        tvConfirm.setText("套用並開始建立作品");

    }

    private void isCanNotUse() {


//        btConfirm.setBackgroundColor(Color.parseColor("#e91e63")); //pink500
//        tvConfirm.setText("套用需要 " + intPoint + " P");

    }


    private void back() {

        if (getTemplateTask != null && !getTemplateTask.isCancelled()) {
            getTemplateTask.cancel(true);
            getTemplateTask = null;
        }

        if (picList != null && picList.size() > 0) {
            for (int i = 0; i < picList.size(); i++) {
                String url = picList.get(i);
                Picasso.with(mActivity.getApplicationContext()).invalidate(url);
            }
        }

        if (otherList != null && otherList.size() > 0) {
            for (int i = 0; i < otherList.size(); i++) {
                String image = (String) otherList.get(i).get("image");
                Picasso.with(mActivity.getApplicationContext()).invalidate(image);
            }
        }

        if (sampleList != null && sampleList.size() > 0) {
            for (int i = 0; i < sampleList.size(); i++) {
                String cover = (String) sampleList.get(i).get("cover");
                Picasso.with(mActivity.getApplicationContext()).invalidate(cover);
            }
        }
        System.gc();
        finish();
        ActivityAnim.FinishAnim(mActivity);

    }


    private void confirm() {
        //2016.05.16修改
        Message message = new Message();
        message.what = 0;
        checkAlbumHandler.sendMessage(message);

    }


    private void deleteAllEdit() {
        FileUtility.delAllFile(SDpath + myDir + "copy/");
        FileUtility.delAllFile(SDpath + myDir + "readySend/");

    }


    private void checkOwn() {

        if (!temOwn) {//如果沒擁有 詢問是否購買

            if (intPoint > 0) {

                dConfirm = new DialogSet(mActivity);
                dConfirm.setAlbumContents_StringType(getResources().getString(R.string.pinpinbox_2_0_0_other_message_confirm_buy) + " " + intPoint + " P");
                dConfirm.getTextView_Y().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get user points
                        doGetPoint();
                    }
                });

            } else {
                doGetPoint();
            }

        } else {
            //有則 呼叫接口54 獲取album_id
            doGetAlbum();
        }
    }

    private void checkBuyPoint() {
        dCheckBuyPoint = new DialogSet(mActivity);
//        dCheckBuyPoint.setAlbumContents_Integer(R.string.point_insufficient);

        dCheckBuyPoint.getTextView_Y().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dCheckBuyPoint.getDialog().dismiss();
                Intent intentBuyPoint = new Intent(TemplateInfoActivity.this, PointActivity.class);
                startActivity(intentBuyPoint);
                ActivityAnim.StartAnim(mActivity);

            }
        });
    }


    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoGetAlbum:
                        doGetAlbum();
                        break;

                    case DoGetPoint:
                        doGetPoint();
                        break;

                    case DoBuyTemplate:
                        doBuyTemplate();
                        break;

                    case DoGetTemplate:
                        doGetTemplate();
                        break;

                    case DoGetReport:
                        doGetReport();
                        break;

                    case DoSendReport:
                        doSendReport();
                        break;

                    case DoFirstDownTem:
                        doFirstDownTem();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);


    }


    private void doGetAlbum() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getAlbumTask = new GetAlbumTask();
        getAlbumTask.execute();

    }

    private void doGetPoint() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getPointTask = new GetPointTask();
        getPointTask.execute();

    }

    private void doBuyTemplate() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        buyTemplateTask = new BuyTemplateTask();
        buyTemplateTask.execute();
    }

    private void doGetTemplate() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getTemplateTask = new GetTemplateTask();
        getTemplateTask.execute();
    }

    private void doGetReport() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getReportTask = new GetReportTask();
        getReportTask.execute();
    }

    private void doSendReport() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        sendReportTask = new SendReportTask();
        sendReportTask.execute();

    }

    private void doFirstDownTem() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        firstDownTemTask = new FirstDownTemTask();
        firstDownTemTask.execute();
    }

    private class GetAlbumTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetAlbum;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P54_InsertAlbumOfDiy, SetMapByProtocol.setParam54_insertalbumofdiy(id, token, template_id), null);
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

            if (p54Result.equals("1") && new_album_id != null) {
                if (LOG.isLogMode) {
                    Log.d(TAG, "條件成立");
                    Log.d(TAG, "p57Result => " + p54Result);
                    Log.d(TAG, "new_album_id => " + new_album_id);
                }
                if (dConfirm != null) {
                    dConfirm.getDialog().dismiss();
                }

                /**2016.08.16 新增刷新已擁有*/

                Activity activity = null;

                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();

                for (int i = 0; i < activityList.size(); i++) {

                    String actName = activityList.get(i).getClass().getSimpleName();

                    if (actName.equals("CreateAlbumActivity")) {

                        activity = activityList.get(i);

                        MyLog.Set("d", mActivity.getClass(), "get activity => CreateAlbumActivity");

                        break;

                    }
                }

                if (activity != null) {
                    List<Fragment> fragmentList = ((CreateAlbumActivity) activity).fragmentList();

                    for (int i = 0; i < fragmentList.size(); i++) {

                        Fragment fragment = fragmentList.get(i);

                        String strFragmentName = fragment.getClass().getSimpleName();

                        if (strFragmentName.equals("TemplateOwn")) {

                            MyLog.Set("d", mActivity.getClass(), "do    ((TemplateOwn)fragment).ReFresh()");

                            ((TemplateOwn) fragment).doRefresh();


                            break;
                        }

                    }
                }

                /**2016.09.28*/
                isCanUse();

                Bundle bundle = new Bundle();
                bundle.putString("album_id", new_album_id);
                bundle.putString("identity", "admin");
                bundle.putInt("create_mode", 1);

                if (MODE == CONTRIBUTEMODE) {//2016.05.31添加
                    bundle.putInt("contributeMode", CONTRIBUTEMODE);
                    bundle.putString("event_id", event_id);
                }

                intentToCreation = new Intent(TemplateInfoActivity.this, Creation2Activity.class);
                intentToCreation.putExtras(bundle);


                boolean bDownTem = getdata.getBoolean(TaskKeyClass.firsttime_download_template, false);
                if (bDownTem) {
                    startActivity(intentToCreation);
                    if (MODE == CONTRIBUTEMODE) {//2016.05.31添加
                        finish();
                    }
                    ActivityAnim.StartAnim(mActivity);

                } else {
                    doFirstDownTem();
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

    private class GetPointTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetPoint;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(id, token, "google"), null);
                MyLog.Set("d", getClass(), "p23strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p23Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p23Result = jsonObject.getString(Key.result);
                    if (p23Result.equals("1")) {
                        userPoint = jsonObject.getString(Key.data);
                    } else if (p23Result.equals("0")) {
                        p23Message = jsonObject.getString(Key.message);
                    } else {
                        p23Result = "";
                    }
                } catch (Exception e) {
                    p23Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p23Result.equals("1")) {
                if (dConfirm != null && dConfirm.getDialog().isShowing()) {
                    dConfirm.getDialog().dismiss();
                }
                int up = StringIntMethod.StringToInt(userPoint);

                if (up >= intPoint) {
                    MyLog.Set("d", getClass(), "can buy");
                    doBuyTemplate();
                } else {
                    MyLog.Set("d", getClass(), "to buy point");
                    checkBuyPoint();
                }
            } else if (p23Result.equals("0")) {

            } else if (p23Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }
        }
    }

    private class BuyTemplateTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoBuyTemplate;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P38BuyTemplate, SetMapByProtocol.setParam38_buytemplate(id, token, template_id, "google"), null);
                MyLog.Set("d", getClass(), "p38strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p38Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p38Result = jsonObject.getString(Key.result);

                    if (p38Result.equals("1")) {

                    } else if (p38Result.equals("0")) {
                        p38Message = jsonObject.getString(Key.message);
                    } else if (p38Result.equals("2")) {

                    } else {
                        p38Result = "";
                    }

                } catch (Exception e) {
                    p38Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p38Result.equals("1")) {
                Message message = new Message();
                message.what = 1;
                checkAlbumHandler.sendMessage(message);
            } else if (p38Result.equals("2")) {
                Message message = new Message();
                message.what = 1;
                checkAlbumHandler.sendMessage(message);
            } else if (p38Result.equals("0")) {

            } else if (p38Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }

        }
    }

    private class GetTemplateTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetTemplate;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P37GetTemplate, SetMapByProtocol.setParam37_gettemplate(id, token, template_id), null);
                MyLog.Set("d", getClass(), "p37strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p37Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);
                    p37Result = jsonObject.getString(Key.result);
                    if (p37Result.equals("1")) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONObject j = new JSONObject(jdata);


                        //版型照片內容
                        String frame = j.getString("frame");
                        JSONArray arrayFrame = new JSONArray(frame);
                        for (int i = 0; i < arrayFrame.length(); i++) {
                            JSONObject obj = (JSONObject) arrayFrame.get(i);
                            String url = obj.getString("url");
//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("url", url);
                            picList.add(url);
                        }

                        //版型資訊
                        String template = j.getString("template");
                        JSONObject objTem = new JSONObject(template);
                        temName = objTem.getString("name");
                        intPoint = objTem.getInt("point");
                        temOwn = objTem.getBoolean("own");

                        temDescription = objTem.getString("description");


                        //版型作者
                        String user = j.getString("user");
                        JSONObject objUser = new JSONObject(user);
                        temUsername = objUser.getString("name");

                        //sample
                        String album = j.getString("album");
                        JSONArray arrayAlbum = new JSONArray(album);
                        for (int i = 0; i < arrayAlbum.length(); i++) {
                            JSONObject obj = (JSONObject) arrayAlbum.get(i);
                            String album_id = obj.getString("album_id");
                            String cover = obj.getString("cover");
                            String name = obj.getString("name");
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("album_id", album_id);
                            map.put("cover", cover);
                            map.put("name", name);
                            sampleList.add(map);
                        }

                        //作者其他版型
                        String other = j.getString("other");
                        JSONArray arrayOther = new JSONArray(other);
                        for (int i = 0; i < arrayOther.length(); i++) {
                            JSONObject obj = (JSONObject) arrayOther.get(i);
                            String image = obj.getString("image");
                            String name = obj.getString("name");
                            String template_id = obj.getString("template_id");
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("image", image);
                            map.put("name", name);
                            map.put("template_id", template_id);
                            otherList.add(map);
                        }

                        System.out.println("otherList.size() => " + otherList.size());

                    } else if (p37Result.equals("0")) {

                        p37Message = jsonObject.getString(Key.message);

                    } else {

                        p37Result = "";

                    }
                } catch (Exception e) {
                    p37Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p37Result.equals("1")) {

                ContentsAdapter adapter = new ContentsAdapter(mActivity, picList);
                rcContents.setAdapter(adapter);



                otherAdpater = new TemplateInformationHorAdpater(mActivity, otherList, typeOther);


                sampleAdapter = new TemplateInformationHorAdpater(mActivity, sampleList, typeSample);


//                tvName.setText(temName);

                LinkText.set(mActivity, tvDescription, LinkText.defaultColor, LinkText.defaultColorOfHighlightedLink, temDescription);


                tvAuthor.setText(temUsername);


                /**2016.09.28新增*/
                if (temOwn) {

                    isCanUse();

                } else {


                    if (intPoint == 0) {

                        isCanUse();

                    } else {

                        isCanNotUse();

                    }


                }

//                if(intPoint>0){
//                    tvPrize.setText(intPoint + " P");
//                }else {
//                    tvPrize.setVisibility(View.GONE);
//                }


                ActivityClick ac = new ActivityClick();
                backImg.setOnClickListener(ac);
                rReport.setOnClickListener(ac);



            } else if (p37Result.equals("0")) {

            } else if (p37Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }

        }
    }

    private class GetReportTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetReport;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P69_GetReportintentlist,
                        SetMapByProtocol.setParam69_getreportintentlist(id, token), null);
                MyLog.Set("d", getClass(), "p69strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p69Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p69Result = jsonObject.getString(Key.result);
                    if (p69Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        int array = jsonArray.length();
                        for (int i = 0; i < array; i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            String reportintent_id = obj.getString("reportintent_id");
                            String name = obj.getString("name");
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("reportintent_id", reportintent_id);
                            map.put("name", name);
                            reportList.add(map);
                            strReportList.add(name);
                        }

                    } else if (p69Result.equals("0")) {
                        p69Message = jsonObject.getString(Key.message);
                    } else {
                        p69Result = "";
                    }

                } catch (Exception e) {
                    p69Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p69Result.equals("1")) {
//                showPop();
            } else if (p69Result.equals("0")) {

            } else if (p69Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }


        }
    }

    private class SendReportTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoSendReport;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P70_InsertReport,
                        SetMapByProtocol.setParam70_insertreport(id, token, report_id, "template", template_id), null);
                MyLog.Set("d", getClass(), "p70strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p70Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p70Result = jsonObject.getString(Key.result);
                    if (p70Result.equals("1")) {


                    } else if (p70Result.equals("0")) {
                        p70Message = jsonObject.getString(Key.message);
                    } else {
                        p70Result = "";
                    }

                } catch (Exception e) {
                    p70Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p70Result.equals("1")) {
//                if (popupWindow.isShowing()) {
//                    popupWindow.dismiss();
//                    PinPinToast.showSuccessToast(mActivity, R.string.send_report_success);

//                    View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast, null);
//                    TextView textView = (TextView) view.findViewById(R.id.tvToast);
//                    textView.setText("檢舉完成");
//                    Toast toast = new Toast(getApplicationContext());
//                    toast.setDuration(Toast.LENGTH_SHORT);
//                    toast.setView(view);
//                    toast.show();
//                }
            } else if (p70Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p70Message);

            } else if (p70Result.equals(Key.timeout)) {

                connectInstability();

            } else {
             DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }

    private class FirstDownTemTask extends AsyncTask<Void, Void, Object> {

        private String name;
        private String reward;
        private String reward_value;
        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoFirstDownTem;
            startLoading();
        }


        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, SetMapByProtocol.setParam83_dotask(id, token, TaskKeyClass.firsttime_download_template, "google"), null);
                if (LOG.isLogMode) {
                    Log.d(TAG, "p83strJson => " + strJson);
                }
            } catch (SocketTimeoutException timeout) {
                p83Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = jsonObject.getString(Key.result);

                    if (p83Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);

                        JSONObject object = new JSONObject(jdata);

                        String task = object.getString(Key.task);
                        String event = object.getString(Key.event);

                        JSONObject taskObj = new JSONObject(task);
                        name = taskObj.getString(Key.name);
                        reward = taskObj.getString(Key.reward);
                        reward_value = taskObj.getString(Key.reward_value);

                        JSONObject eventObj = new JSONObject(event);
                        url = eventObj.getString(Key.url);

                    } else if (p83Result.equals("2")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("0")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else {
                        p83Result = "";
                    }
                } catch (Exception e) {
                    p83Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p83Result.equals("1")) {

                final DialogHandselPoint d = new DialogHandselPoint(mActivity);
                d.getTvTitle().setText(name);

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + " " + reward_value + " P !");
                    /**獲取當前使用者P點*/
                    String point = getdata.getString("point", "");
                    int p = StringIntMethod.StringToInt(point);

                    /**任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /**加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /**儲存data*/
                    getdata.edit().putString("point", newP).commit();

                    getdata.edit().putBoolean(TaskKeyClass.firsttime_download_template, true).commit();

                    getdata.edit().putBoolean("datachange", true).commit();

                } else {
//                    d.getTvDescription().setText();
                }


                d.getTvLink().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        Intent intent = new Intent(mActivity, WebView2Activity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        ActivityAnim.StartAnim(mActivity);
                    }
                });

                d.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        startActivity(intentToCreation);
                        if (MODE == CONTRIBUTEMODE) {//2016.05.31添加
                            finish();
                        }
                        ActivityAnim.StartAnim(mActivity);
                    }
                });


            } else if (p83Result.equals("2")) {

                getdata.edit().putBoolean(TaskKeyClass.firsttime_download_template, true).commit();
                startActivity(intentToCreation);
                if (MODE == CONTRIBUTEMODE) {//2016.05.31添加
                    finish();
                }
                ActivityAnim.StartAnim(mActivity);

            } else if (p83Result.equals("0")) {

                if (LOG.isLogMode) {
                    Log.d(TAG, "p83Message" + p83Message);
                }

                getdata.edit().putBoolean(TaskKeyClass.firsttime_download_template, true).commit();
                startActivity(intentToCreation);
                if (MODE == CONTRIBUTEMODE) {//2016.05.31添加
                    finish();
                }
                ActivityAnim.StartAnim(mActivity);


            } else if (p83Result.equals(Key.timeout)) {

                connectInstability();

            } else {
             DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }


    private class ActivityClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                return;
            }

            if (!HttpUtility.isConnect(mActivity)) {
                setNoConnect();
                return;
            }

            switch (v.getId()) {
                case R.id.backImg:
                    back();
                    break;

//                case R.id.tem_info_share:
//                    share();
//                    break;


                case R.id.rReport:
                    doGetReport();
                    break;

//                case R.id.btConfirm:
//                    confirm();
//                    break;

//                case R.id.tem_info_author:
//                    toUser();
//                    break;

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
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (getAlbumTask != null && !getAlbumTask.isCancelled()) {
            getAlbumTask.cancel(true);
            getAlbumTask = null;
        }

        if (getPointTask != null && !getPointTask.isCancelled()) {
            getPointTask.cancel(true);
            getPointTask = null;
        }

        if (buyTemplateTask != null && !buyTemplateTask.isCancelled()) {
            buyTemplateTask.cancel(true);
            buyTemplateTask = null;
        }

        if (getTemplateTask != null && !getTemplateTask.isCancelled()) {
            getTemplateTask.cancel(true);
            getTemplateTask = null;
        }

        if (getReportTask != null && !getReportTask.isCancelled()) {
            getReportTask.cancel(true);
            getReportTask = null;
        }

        if (sendReportTask != null && !sendReportTask.isCancelled()) {
            sendReportTask.cancel(true);
            sendReportTask = null;
        }

        if (firstDownTemTask != null && !firstDownTemTask.isCancelled()) {
            firstDownTemTask.cancel(true);
            firstDownTemTask = null;
        }

        if (picList != null && picList.size() > 0) {
            for (int i = 0; i < picList.size(); i++) {
                String url = picList.get(i);
                Picasso.with(mActivity.getApplicationContext()).invalidate(url);
            }
        }

        if (otherList != null && otherList.size() > 0) {
            for (int i = 0; i < otherList.size(); i++) {
                String image = (String) otherList.get(i).get("image");
                Picasso.with(mActivity.getApplicationContext()).invalidate(image);
            }
        }

        if (sampleList != null && sampleList.size() > 0) {
            for (int i = 0; i < sampleList.size(); i++) {
                String cover = (String) sampleList.get(i).get("cover");
                Picasso.with(mActivity.getApplicationContext()).invalidate(cover);
            }
        }
        System.gc();

        super.onDestroy();

    }

}

package com.pinpinbox.android.Activity.RecommendAuthor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.DialogTool.DialogSet;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.SelfMadeClass.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.FlexiListView;
import com.pinpinbox.android.Widget.ActivityAnim;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.NoConnect;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2016/1/3.
 */
public class RecommendByOtherActivity extends DraggerActivity {


    private Activity mActivity;
    private NoConnect noConnect;

    private GetRecommendTask getRecommendTask;

    private String TAG = "RecommendByOtherActivity";
    private String id, token;
    private String mRecommendother;
    private String p47_count_from, p47_authorid, p47_authorname, p47_picfileurl, p47_inserttime;
    private String p47Result, p47Message;

    private int doingType;
    private static final int DoGetRecommend = 0;

    private FlexiListView flexiListView;
    private TextView tvAttentionAll, tvWhereFriend;

    private LoadingAnimation loading;
    private RecommendAdapter recommendAdapter;
    private ArrayList<HashMap<String, Object>> p47arraylist;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_recommend_by_other);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                SystemBarTintManager tintManager = new SystemBarTintManager(RecommendByOtherActivity.this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.black);
//            }
//        });

        getBundle();
        init();
        setWhereFriendText();
        toAuthor();
        back();

        doGetRecommend();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRecommendother = bundle.getString("attentionType","");
        }
    }

    private void init() {
        mActivity = this;
        flexiListView = (FlexiListView) findViewById(R.id.recommend_other_listview);
        tvAttentionAll = (TextView) findViewById(R.id.attention_all);
        tvWhereFriend = (TextView)findViewById(R.id.tvWhereFriend);
        SharedPreferences getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.id, "");
        p47arraylist = new ArrayList<>();
        loading = new LoadingAnimation(mActivity);

    }

    private void setWhereFriendText(){

        if(mRecommendother.length()>1){
            String a = mRecommendother.substring(0,1);
            MyLog.Set("d", getClass(), "first word => " + a);

            if(a.equals("f")){
                tvWhereFriend.setText("來自facebook");
            }else if(a.equals("c")){
                tvWhereFriend.setText("來自通訊錄");
            }


        }


    }

    public void changeFollowStatus(int position) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("count_from", (String) p47arraylist.get(position).get("count_from"));
        map.put("inserttime", (String) p47arraylist.get(position).get("inserttime"));
        map.put("authorid", (String) p47arraylist.get(position).get("authorid"));
        map.put("picfileurl", (String) p47arraylist.get(position).get("picfileurl"));
        map.put("authorname", (String) p47arraylist.get(position).get("authorname"));

        String attention = (String) p47arraylist.get(position).get("attention");

        switch (attention) {
            case "N":
                map.put("attention", "Y");
                p47arraylist.set(position, map);

                break;

            case "Y":
                map.put("attention", "N");
                p47arraylist.set(position, map);


                break;
        }

        recommendAdapter.notifyDataSetChanged();

    }

    private void toAuthor() {

        flexiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    noConnect = new NoConnect(mActivity);
                    return;
                }

//                Bundle bundle = new Bundle();
//                bundle.putString("author_id", (String) p47arraylist.get(position).get("authorid"));
//
//                Intent intent = new Intent(mActivity, .class);
//                intent.putExtras(bundle);
//                mActivity.startActivity(intent);
//                ActivityAnim.StartAnim(mActivity);


            }
        });
    }


    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoGetRecommend:
                        doGetRecommend();
                        break;
                }
            }
        };

        DialogSet d = new DialogSet(this);
        d.ConnectInstability();
        d.setConnectInstability(connectInstability);
    }

    private void doGetRecommend(){
        if(!HttpUtility.isConnect(this)){
            noConnect = new NoConnect(this);
            return;
        }
        getRecommendTask = new GetRecommendTask();
        getRecommendTask.execute();
    }

    private class GetRecommendTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetRecommend;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("rank", mRecommendother);
            data.put("limit", "0,100");
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("rank", mRecommendother);
            sendData.put("limit", "0,100");
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P47_GetRecommendedAuthor, sendData, null);
                MyLog.Set("d", mActivity.getClass(), "p47strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p47Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject j = new JSONObject(strJson);
                    p47Result = j.getString(Key.result);
                    if (p47Result.equals("1")) {
                        String jdata = j.getString(Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, Object> map = new HashMap<String, Object>();

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            String follow = obj.getString("follow");
                            JSONObject jf = new JSONObject(follow);
                            try {
                                String count_from = jf.getString("count_from");

                                map.put("count_from", count_from);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String user = obj.getString("user");
                            JSONObject ju = new JSONObject(user);
                            try {
                                String authorid = ju.getString("user_id");
                                map.put("authorid", authorid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                String authorname = ju.getString("name");
                                map.put("authorname", authorname);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                String picfileurl = ju.getString("picture");
                                map.put("picfileurl", picfileurl);

                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                            try {
                                String inserttime = ju.getString("inserttime");
                                map.put("inserttime", inserttime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            map.put("attention", "N");

                            p47arraylist.add(map);
                        }
                    }else if(p47Result.equals("0")){
                        p47Message = j.getString(Key.message);
                    }else {
                        p47Result = "";
                    }

                } catch (Exception e) {
                    p47Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();
            if (p47Result.equals("1")) {

                recommendAdapter = new RecommendAdapter(mActivity, p47arraylist);
                flexiListView.setAdapter(recommendAdapter);
                attentionAll();

            } else if (p47Result.equals("0")) {



            } else if (p47Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }


        }

    }









    private void attentionAll() {
        tvAttentionAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    noConnect = new NoConnect(mActivity);
                    return;
                }

                AttentionAll att = new AttentionAll();
                att.execute();
            }
        });

    }

    class AttentionAll extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();
            loading.show();
        }


        @Override
        protected Object doInBackground(Void... params) {

            for (int i = 0; i < p47arraylist.size(); i++) {

                if (p47arraylist.get(i).get("attention").equals("N")) {

                    Map<String, String> data = new HashMap<>();
                    data.put("id", id);
                    data.put("token", token);
                    data.put("authorid", (String) p47arraylist.get(i).get("authorid"));
                    String sign = IndexSheet.encodePPB(data);

                    Map<String, String> sendData = new HashMap<>();
                    sendData.put("id", id);
                    sendData.put("token", token);
                    sendData.put("authorid", (String) p47arraylist.get(i).get("authorid"));
                    sendData.put("sign", sign);
                    try {
                        String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P12_ChangeFollowStatus, sendData, null);//12
                        MyLog.Set("d", getClass(), "p12strJson => " + strJson);
                        JSONObject jsonObject = new JSONObject(strJson);
                        String result = jsonObject.getString("result");
                        if (result.equals("1")) {
                            String jdata = jsonObject.getString("data");
                            JSONObject object = new JSONObject(jdata);
                            String followstatus = object.getString("followstatus");
                            HashMap<String, Object> map = new HashMap<>();
                            switch (followstatus) {
                                case "0":
                                    map.put("count_from", (String) p47arraylist.get(i).get("count_from"));
                                    map.put("inserttime", (String) p47arraylist.get(i).get("inserttime"));
                                    map.put("authorid", (String) p47arraylist.get(i).get("authorid"));
                                    map.put("picfileurl", (String) p47arraylist.get(i).get("picfileurl"));
                                    map.put("authorname", (String) p47arraylist.get(i).get("authorname"));
                                    map.put("attention", "N");
                                    p47arraylist.set(i, map);
                                    break;

                                case "1":
                                    map.put("count_from", (String) p47arraylist.get(i).get("count_from"));
                                    map.put("inserttime", (String) p47arraylist.get(i).get("inserttime"));
                                    map.put("authorid", (String) p47arraylist.get(i).get("authorid"));
                                    map.put("picfileurl", (String) p47arraylist.get(i).get("picfileurl"));
                                    map.put("authorname", (String) p47arraylist.get(i).get("authorname"));
                                    map.put("attention", "Y");
                                    p47arraylist.set(i, map);
                                    break;
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();
            recommendAdapter.notifyDataSetChanged();


        }
    }

    private void back() {
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivityAnim.FinishAnim(mActivity);
            }
        });

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

            if (noConnect == null) {
                DialogSet d = new DialogSet(this);
                d.setNoConnect();
            }
        }
    }

    @Override
    protected void onStop() {
        if (p47arraylist != null && p47arraylist.size() > 0) {
            for (int i = 0; i < p47arraylist.size(); i++) {
                Picasso.with(getApplicationContext()).invalidate((String) p47arraylist.get(i).get("picfileurl"));
            }
            System.gc();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {

        if(getRecommendTask!=null && !getRecommendTask.isCancelled()){
            getRecommendTask.cancel(true);
            getRecommendTask = null;
        }

        if (p47arraylist != null && p47arraylist.size() > 0) {
            for (int i = 0; i < p47arraylist.size(); i++) {
                Picasso.with(getApplicationContext()).invalidate((String) p47arraylist.get(i).get("picfileurl"));
            }
            System.gc();
        }
        super.onDestroy();
    }


}

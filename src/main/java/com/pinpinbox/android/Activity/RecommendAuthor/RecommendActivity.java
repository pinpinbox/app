package com.pinpinbox.android.Activity.RecommendAuthor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.pinpinbox.android.DialogTool.DialogSet;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.SelfMadeClass.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Widget.ActivityAnim;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.NoConnect;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2015/9/24.
 */
public class RecommendActivity extends DraggerActivity {


    private Context mContext;
    private Activity mActivity;
    private SharedPreferences getdata;
    private LoadingAnimation loading;
    private RecommendAdapter recommendAdapter;
    private CallbackManager callbackManager;
    private NoConnect noConnect;

    private GetRecommendTask getRecommendTask;


    private String TAG = "RecommendActivity";
    private String id, token;
    private String arrayid;
    private String p47Result, p47Message;
//    private String p47_count_from, p47_authorid, p47_authorname, p47_picfileurl, p47_inserttime;
    private String p64Result, p64Message;
    private String arrayNumber;

    private int attentionCount;
    private int doingType;
    private static final int DoGetRecommend = 0;


    private ArrayList<HashMap<String, Object>> p47arraylist;
    private ArrayList<String> mID;
    private ArrayList<String> mContactsNumber;

    private RelativeLayout by_fb_button, by_contacts;
    private ListView recommendlistview;
    private ImageView fb, ct;

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
        FacebookSdk.sdkInitialize(getApplicationContext());

        setFBcontent();

//        setContentView(R.layout.activity_recommend);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                SystemBarTintManager tintManager = new SystemBarTintManager(RecommendActivity.this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.black);
//            }
//        });


        init();
        search_fb_friend();
        search_contacts();
        toAuthor();
        back();
        doGetRecommend();
    }

    private void setFBcontent() {


        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        MyLog.Set("d", getClass(), "fb friend => onSuccess");
                        MyLog.Set("d", getClass(), loginResult.getAccessToken().toString());

                        GraphRequest request = GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray array, GraphResponse response) {
                                String s = response.toString();

                                MyLog.Set("d", getClass(), s);
                                MyLog.Set("d", getClass(), "array.toString() => " +  array.toString());

                                try {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject obj = (JSONObject) array.get(i);
                                        String fb_id = obj.getString("id");
                                        mID.add(fb_id);
                                    }

                                    for (int i = 0; i < mID.size(); i++) {
                                        if (i == 0) {
                                            arrayid = mID.get(i);
                                        } else {
                                            arrayid = arrayid + "," + mID.get(i);
                                        }
                                    }

                                    MyLog.Set("d", getClass(), "arrayid => "+ arrayid);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(RecommendActivity.this, RecommendByOtherActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("attentionType", "facebook=" + arrayid);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                ActivityAnim.StartAnim(mActivity);

                            }
                        });
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        MyLog.Set("d", getClass(), "fb friend => onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        MyLog.Set("d", getClass(), "fb friend => onError");
                        exception.printStackTrace();

                    }

                });


    }

    private void init() {
        mContext = this;
        mActivity = this;

        loading = new LoadingAnimation(mActivity);

        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);

        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");

        p47arraylist = new ArrayList<>();
        mContactsNumber = new ArrayList<>();
        mID = new ArrayList<>();

//        recommendlistview = (ListView) findViewById(R.id.recommend_listview);
//        by_fb_button = (RelativeLayout) findViewById(R.id.by_fb_button);
//        by_contacts = (RelativeLayout) findViewById(R.id.by_contacts);

        View v = LayoutInflater.from(mActivity).inflate(R.layout.header_2_0_0_cooperation_search, null);
        recommendlistview.addHeaderView(v);

//        fb = (ImageView) findViewById(R.id.fb);
//        ct = (ImageView) findViewById(R.id.ct);

        recommendlistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void setdata() {
        recommendAdapter = new RecommendAdapter(this, p47arraylist);
        recommendlistview.setAdapter(recommendAdapter);
    }

    private void search_fb_friend() {

        by_fb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (HttpUtility.isConnect(mActivity)) {
                    LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("user_friends"));
                } else {
                    noConnect = new NoConnect(mActivity);
                }


            }
        });


//        by_fb_button.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        fb.setImageResource(R.drawable.icon_recommendactivity_fb);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//
//                        fb.setImageResource(R.drawable.icon_recommendactivity_fb_click);
//                        if(HttpUtility.isConnect(mActivity)) {
//                            LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("user_friends"));
//                        }else {
//                            noConnect = new NoConnect(mActivity);
//                        }
//                        break;
//                }
//                return true;
//            }
//        });


    }

    private void search_contacts() {

        by_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (HttpUtility.isConnect(mActivity)) {
                    mContactsNumber = SystemUtility.getPhoneContacts(mContext);

                    for (int i = 0; i < mContactsNumber.size(); i++) {
                        if (i == 0) {
                            arrayNumber = mContactsNumber.get(i);
                        } else {
                            arrayNumber = arrayNumber + "," + mContactsNumber.get(i);
                        }
                    }


                    Intent intent = new Intent(RecommendActivity.this, RecommendByOtherActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("attentionType", "cellphone=" + arrayNumber);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    ActivityAnim.StartAnim(mActivity);
                } else {
                    noConnect = new NoConnect(mActivity);
                }


            }
        });

    }

    private void toAuthor() {

        recommendlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    noConnect = new NoConnect(mActivity);
                    return;
                }

                position = position - 1;


            }
        });
    }

    private void back() {
        ImageView backImg = (ImageView) findViewById(R.id.back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivityAnim.FinishAnim(mActivity);
            }
        });
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
            data.put("rank", "official= ");
            data.put("limit", "0,36");
            String sign = IndexSheet.encodePPB(data);
            final Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("rank", "official= ");
            sendData.put("limit", "0,36");
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

                setdata();

            } else if (p47Result.equals("0")) {



            } else if (p47Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
        for (int i = 0; i < p47arraylist.size(); i++) {
            Picasso.with(getApplicationContext()).invalidate((String) p47arraylist.get(i).get("picfileurl"));
        }
        System.gc();
        super.onStop();
    }


    @Override
    public void onDestroy() {

        if(getRecommendTask!=null && !getRecommendTask.isCancelled()){
            getRecommendTask.cancel(true);
            getRecommendTask = null;
        }

        for (int i = 0; i < p47arraylist.size(); i++) {
            Picasso.with(getApplicationContext()).invalidate((String) p47arraylist.get(i).get("picfileurl"));
        }

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        System.gc();
        super.onDestroy();
    }


}

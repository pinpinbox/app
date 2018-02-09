package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.Gradient.ScrimUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewVisibility;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ChangeTypeListener;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerUserAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2017/3/18.
 */
public class TypeFacebookFriend2Activity extends Activity implements View.OnClickListener, ChangeTypeListener {

    private Activity mActivity;
    private CallbackManager callbackManager;
    private NoConnect noConnect;
    private LoadingAnimation loading;

    private GetRecommendTask getRecommendTask;
    private ChangeFollowTask changeFollowTask;

    private RecyclerUserAdapter adapter;

    private ArrayList<String> facebookIdList;
    private List<ItemUser> itemUserList;

    private LinearLayout linGuide;
    private RecyclerView rvFacebookFriends;
    private TextView tvBottom, tvGuide;



    private String facebookIds;
    private String id, token;

    private int doingType;
    private int clickPosition;


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_follow_facebook_friend);
        SystemUtility.SysApplication.getInstance().addActivity(this);

        setStatus();

        setGradient();

        init();

        setRecycler();


    }

    private void setStatus() {
        setTranslucentStatus(true);
    }

    private void setGradient() {

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {

            findViewById(R.id.vGradient).setBackground(
                    ScrimUtil.makeCubicGradientScrimDrawable(
                            getResources().getColor(R.color.com_facebook_blue), //顏色
                            8, //漸層數
                            Gravity.TOP)); //起始方向
        }

    }

    private void init() {

        mActivity = this;

        loading = new LoadingAnimation(this);

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        facebookIdList = new ArrayList<>();
        itemUserList = new ArrayList<>();

        linGuide = (LinearLayout) findViewById(R.id.linGuide);
        rvFacebookFriends = (RecyclerView)findViewById(R.id.rvFacebookFriends);
        tvGuide = (TextView)findViewById(R.id.tvGuide);
        TextView tvContents = (TextView) findViewById(R.id.tvContents);
        TextView tvConfirm = (TextView) findViewById(R.id.tvConfirm);

        tvBottom = (TextView) findViewById(R.id.tvBottom);


        TextUtility.setBold(tvContents, true);
        TextUtility.setBold(tvConfirm, true);

        tvConfirm.setOnClickListener(this);
        tvBottom.setOnClickListener(this);

        rvFacebookFriends.addItemDecoration(new SpacesItemDecoration(16));

    }


    private void setRecycler(){

        adapter = new RecyclerUserAdapter(mActivity, itemUserList);
        rvFacebookFriends.setAdapter(adapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvFacebookFriends.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvFacebookFriends.getAdapter(), manager.getSpanCount()));
        rvFacebookFriends.setLayoutManager(manager);

        View vHeader = LayoutInflater.from(this).inflate(R.layout.header_2_0_0_title_no_actionbar, null);

        TextUtility.setBold((TextView) vHeader.findViewById(R.id.tvTitle), true);


        RecyclerViewUtils.setHeaderView(rvFacebookFriends, vHeader);


        adapter.setOnRecyclerViewListener(new RecyclerUserAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {
            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


        adapter.setChangeTypeListener(this);



    }


    private void setConnectToFacebook(){
        FacebookSdk.sdkInitialize(getApplicationContext());
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
                                        facebookIdList.add(fb_id);
                                    }

                                    for (int i = 0; i < facebookIdList.size(); i++) {
                                        if (i == 0) {
                                            facebookIds = facebookIdList.get(i);
                                        } else {
                                            facebookIds = facebookIds + "," + facebookIdList.get(i);
                                        }
                                    }

                                    MyLog.Set("d", getClass(), "arrayid => "+ facebookIds);

                                    doGetRecommend();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        MyLog.Set("d", this.getClass(), "fb friend => onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        MyLog.Set("d", this.getClass(), "fb friend => onError");
                        exception.printStackTrace();

                    }

                });

    }

    private void cleanPicasso(){
        if(itemUserList!=null && itemUserList.size()>0){
            int count = itemUserList.size();
            for (int i = 0; i < count; i++) {
                Picasso.with(mActivity.getApplicationContext()).invalidate(itemUserList.get(i).getPicture());
            }
        }
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doGetRecommend();
                        break;

                    case DoingTypeClass.DoChangeFollow:
                        doChangeFollow();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doGetRecommend(){
        if(!HttpUtility.isConnect(this)){
            noConnect = new NoConnect(this);
            return;
        }
        getRecommendTask = new GetRecommendTask();
        getRecommendTask.execute();
    }

    private void doChangeFollow(){
        if(!HttpUtility.isConnect(this)){
            noConnect = new NoConnect(this);
            return;
        }
        changeFollowTask = new ChangeFollowTask();
        changeFollowTask.execute();
    }


    private class GetRecommendTask extends AsyncTask<Void, Void, Object> {

        private int p47Result = -1;
        private String p47Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("rank", "facebook=" + facebookIds);
            data.put("limit", "0,100");
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("rank", "facebook=" + facebookIds);
            sendData.put("limit", "0,100");
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P47_GetRecommendedAuthor, sendData, null);
                MyLog.Set("d", mActivity.getClass(), "p47strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p47Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p47Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p47Result==1) {

                        String jdata = JsonUtility.GetString(jsonObject,ProtocolKey.data);
                        JSONArray jsonArray = new JSONArray(jdata);

                        for (int i = 0; i < jsonArray.length(); i++) {


                            ItemUser itemUser = new ItemUser();

                            JSONObject object = (JSONObject) jsonArray.get(i);

                            String user = JsonUtility.GetString(object, ProtocolKey.user);
                            JSONObject jsonUser = new JSONObject(user);


                            itemUser.setName(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                            itemUser.setUser_id(JsonUtility.GetString(jsonUser, ProtocolKey.user_id));
                            itemUser.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                            itemUser.setFollow(false);

                            itemUserList.add(itemUser);
                        }

                    }else if(p47Result==0){
                        p47Message = JsonUtility.GetString(jsonObject,ProtocolKey.message);
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
            loading.dismiss();
            if (p47Result==1) {

                ViewPropertyAnimator alphaTo1 = findViewById(R.id.vGradient).animate();
                alphaTo1.setDuration(200)
                        .alpha(0.6f)
                        .start();

                ViewVisibility.setGone(linGuide);
                tvBottom.setText(R.string.pinpinbox_2_0_0_button_next);
                tvBottom.setTextColor(Color.parseColor(ColorClass.WHITE));
                tvBottom.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);
                (findViewById(R.id.rBottom)).setBackgroundColor(getResources().getColor(R.color.pinpinbox_2_0_0_action_bar_color));


                if(itemUserList.size()<1){

                    ViewVisibility.setGone(rvFacebookFriends);
                    ViewVisibility.setVisible(tvGuide);
                }else {

                    ViewVisibility.setVisible(rvFacebookFriends);
                    ViewVisibility.setGone(tvGuide);

                }



                adapter.notifyDataSetChanged();

            } else if (p47Result==0) {

                DialogV2Custom.BuildError(mActivity, p47Message);



            } else if (p47Result==Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }

    }

    private class ChangeFollowTask extends AsyncTask<Void, Void, Object> {

        private String p12Message = "";
        private int p12Result = -1;

        private int followstatus;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoChangeFollow;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("authorid", itemUserList.get(clickPosition).getUser_id());
            String sign = IndexSheet.encodePPB(data);

            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("authorid",  itemUserList.get(clickPosition).getUser_id());
            sendData.put("sign", sign);

            try {
                String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P12_ChangeFollowStatus, sendData, null);//12
                JSONObject jsonObject = new JSONObject(strJson);
                p12Result = JsonUtility.GetInt(jsonObject,ProtocolKey.result);

                if(p12Result==1){

                    String jsonData = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    JSONObject object = new JSONObject(jsonData);

                    followstatus = JsonUtility.GetInt(object, ProtocolKey.followstatus);


                }else if (p12Result==0) {
                    p12Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p12Result == 1) {

//                rvFollow.getItemAnimator().setSupportsChangeAnimations(true);
//                adapter.removeData(clickPosition);
//                rvFollow.getItemAnimator().setSupportsChangeAnimations(false);


                if(followstatus==0){

                    itemUserList.get(clickPosition).setFollow(false);

                }else {
                    //==1
                    itemUserList.get(clickPosition).setFollow(true);
                }
                adapter.notifyItemChanged(clickPosition);



            } else if (p12Result == 0) {

                DialogV2Custom.BuildError(mActivity, p12Message);

            } else if (p12Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }

    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public SpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
            if (parent.getChildAdapterPosition(view) != 0) {
                if (spanIndex == 0) {
                    outRect.left = 16;
                    outRect.right = 0;
                } else {//if you just have 2 span . Or you can use (staggeredGridLayoutManager.getSpanCount()-1) as last span
                    outRect.left = 0;
                    outRect.right = 16;
                }
                outRect.bottom = 32;
            }
            // Add top margin only for the first item to avoid double space between items
//            if (parent.getChildAdapterPosition(view) == 0) {
//                outRect.top = 32;
//                outRect.right = 32;
//            }
        }
    }

    @Override
    public void changeType(int position) {

        clickPosition = position;
        doChangeFollow();
    }

    @Override
    public void onClick(View view) {
        if(ClickUtils.ButtonContinuousClick()){
            return;
        }

        if(!HttpUtility.isConnect(this)){
            noConnect = new NoConnect(this);
            return;
        }

        switch (view.getId()) {
            case R.id.tvConfirm:

                setConnectToFacebook();

                LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList("user_friends"));

                break;

            case R.id.tvBottom:

                Bundle bundle = new Bundle();
                bundle.putBoolean("hideActionBar", true);

                Intent intent = new Intent(TypeFacebookFriend2Activity.this, Hobby2Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);
                finish();

                break;

        }


    }

    @Override
    public void onBackPressed() {

        List<Activity>activityList = SystemUtility.SysApplication.getInstance().getmList();

        boolean mainIsExist = false;

        if(activityList!=null && activityList.size()>0){
            for (int i = 0; i < activityList.size(); i++) {
                String actName = activityList.get(i).getClass().getSimpleName();
                if(actName.equals(Main2Activity.class.getSimpleName())){
                    mainIsExist = true;
                    break;
                }
            }
        }

        if(mainIsExist){
            finish();
            ActivityAnim.FinishAnim(mActivity);
        }else {

            final DialogV2Custom d = new DialogV2Custom(this);
            d.setStyle(DialogStyleClass.CHECK);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_exit_pinpinbox);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_exit);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    d.dismiss();
                    finish();
                    ActivityAnim.FinishAnim(mActivity);
                }
            });
            d.show();

        }




    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
                noConnect = new NoConnect(this);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
       cleanPicasso();
    }

    @Override
    public void onDestroy() {
        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);
        if (changeFollowTask != null && !changeFollowTask.isCancelled()) {
            changeFollowTask.cancel(true);
        }
        changeFollowTask = null;

        if (getRecommendTask != null && !getRecommendTask.isCancelled()) {
            getRecommendTask.cancel(true);
        }
        getRecommendTask = null;


       cleanPicasso();

        Recycle.IMG((ImageView)findViewById(R.id.backgroundImg));

        System.gc();
        MyLog.Set("d", this.getClass(), "onDestroy");

        super.onDestroy();
    }

}

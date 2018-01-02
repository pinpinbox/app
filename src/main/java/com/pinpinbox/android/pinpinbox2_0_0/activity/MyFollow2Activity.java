package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.DoingTypeClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ChangeTypeListener;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerMyFollowAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2017/3/5.
 */
public class MyFollow2Activity extends DraggerActivity implements View.OnClickListener, ChangeTypeListener {

    private Activity mActivity;

    private GetMyFollowTask getMyFollowTask;
    private RefreshTask refreshTask;
    private MoreDataTask moreDataTask;
    private ChangeFollowTask changeFollowTask;

    private RecyclerMyFollowAdapter adapter;

    private List<ItemUser> itemUserList;

    private String id, token;
    private String p85Message = "";

    private int round, count;
    private int doingType;
    private int p85Result = -1;
    private int loadCount = 0; //讀取到的數量
    private int clickPosition;

    private float scale = 0;

    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過

    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private SmoothProgressBar pbLoadMore;
    private RecyclerView rvFollow;
    private ImageView backImg;
    private TextView tvGuide;
    private View viewHeader;

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                MyLog.Set("e", mActivity.getClass(), "onLoad");
                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

                MyLog.Set("e", mActivity.getClass(), "sizeMax");
            }
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_follow);

//        supportPostponeEnterTransition();

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);

        init();
        setRecyclerRefreshLayouControl();
        setRecycler();

        doGetMyFollow();

    }



    private void init() {

        mActivity = this;


        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        round = 0;
        count = 50;

        itemUserList = new ArrayList<>();

        rvFollow = (RecyclerView) findViewById(R.id.rvFollow);
        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.pinPinBoxRefreshLayout);

        backImg = (ImageView) findViewById(R.id.backImg);

        tvGuide = (TextView) findViewById(R.id.tvGuide);

        pbLoadMore = (SmoothProgressBar) findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();

        viewHeader = LayoutInflater.from(this).inflate(R.layout.header_2_0_0_refresh_with_title, null);

        TextUtility.setBold((TextView) viewHeader.findViewById(R.id.tvTitle), true);

        backImg.setOnClickListener(this);

        rvFollow.setItemAnimator(new DefaultItemAnimator());
        rvFollow.addOnScrollListener(mOnScrollListener);

        //20171002
        SmoothProgressBar pbRefresh = (SmoothProgressBar)findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(findViewById(R.id.vRefreshAnim), pbRefresh);

    }

    private void setRecyclerRefreshLayouControl() {

        pinPinBoxRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }

            @Override
            public void onPullDistance(int distance) {


            }

            @Override
            public void onPullEnable(boolean enable) {
            }
        });

    }

    private void setRecycler() {

        adapter = new RecyclerMyFollowAdapter(mActivity, itemUserList);
        rvFollow.setAdapter(adapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvFollow.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = null;

        if (SystemUtility.isTablet(getApplicationContext())) {

            //平版
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        } else {

            //手機
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }

        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvFollow.getAdapter(), manager.getSpanCount()));
        rvFollow.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvFollow, viewHeader);

        adapter.setOnRecyclerViewListener(new RecyclerMyFollowAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                RoundCornerImageView userImg = (RoundCornerImageView) v.findViewById(R.id.userImg);


                Bundle bundle = new Bundle();
                bundle.putString(Key.author_id, itemUserList.get(position).getUser_id());
                bundle.putString(Key.picture, itemUserList.get(position).getPicture());
                bundle.putString(Key.name, itemUserList.get(position).getName());
                bundle.putInt(Key.changeFollowStatusItem, position);

                if (SystemUtility.Above_Equal_V5()) {

                    Intent intent = new Intent(MyFollow2Activity.this, Author2Activity.class);
                    intent.putExtras(bundle);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(mActivity,
                                    userImg,
                                    ViewCompat.getTransitionName(userImg));
                    startActivity(intent, options.toBundle());


                } else {

                    Intent intent = new Intent(mActivity, Author2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    ActivityAnim.StartAnim(mActivity);

                }

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        adapter.setChangeTypeListener(this);

    }

    private void cleanPicasso() {

        int count = itemUserList.size();

        for (int i = 0; i < count; i++) {
            Picasso.with(mActivity.getApplicationContext()).invalidate(itemUserList.get(i).getPicture());
        }

        System.gc();

    }

    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);

    }

    public void setClickPosition(int position) {
        this.clickPosition = position;
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doGetMyFollow();
                        break;

                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;

                    case DoingTypeClass.DoRefresh:
                        doRefresh();
                        break;

                    case DoingTypeClass.DoChangeFollow:
                        doChangeFollow();
                        break;


                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    public void doRefresh() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        if (getMyFollowTask != null && !getMyFollowTask.isCancelled()) {
            getMyFollowTask.cancel(true);
            getMyFollowTask = null;
        }

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
            refreshTask = null;
        }

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
            moreDataTask = null;
        }


        if (itemUserList.size() > 0) {
            cleanPicasso();
        }
        itemUserList.clear();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        round = 0;

        refreshTask = new RefreshTask();
        refreshTask.execute();


    }

    private void doMoreData() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }


        moreDataTask = new MoreDataTask();
        moreDataTask.execute();

    }

    private void doGetMyFollow() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        getMyFollowTask = new GetMyFollowTask();
        getMyFollowTask.execute();
    }

    public void doChangeFollow() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        changeFollowTask = new ChangeFollowTask();
        changeFollowTask.execute();

    }


    private void callProtocol85(String range) {

        Map<String, String> map = new HashMap<>();
        map.put(Key.id, id);
        map.put(Key.token, token);
        map.put(Key.limit, range);
        String sign = IndexSheet.encodePPB(map);
        Map<String, String> sendData = new HashMap<String, String>();
        sendData.put(Key.id, id);
        sendData.put(Key.token, token);
        sendData.put(Key.limit, range);
        sendData.put(Key.sign, sign);

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.p85_GetFollowToList, sendData, null);
            MyLog.Set("d", mActivity.getClass(), "p85strJson => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p85Result = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p85Result = JsonUtility.GetInt(jsonObject, Key.result);

                if (p85Result == 1) {
                    String jsonData = JsonUtility.GetString(jsonObject, Key.data);


                    if (jsonData != null && !jsonData.equals("")) {

                        JSONArray jsonArray = new JSONArray(jsonData);

                        loadCount = jsonArray.length();

                        for (int i = 0; i < loadCount; i++) {

                            JSONObject object = (JSONObject) jsonArray.get(i);

                            /*獲取用戶id, nickname, description, picture*/
                            String user = JsonUtility.GetString(object, Key.user);
                            JSONObject objUser = new JSONObject(user);

                            ItemUser itemUser = new ItemUser();

                            itemUser.setName(JsonUtility.GetString(objUser, Key.name));
                            itemUser.setPicture(JsonUtility.GetString(objUser, Key.picture));
                            itemUser.setUser_id(JsonUtility.GetString(objUser, Key.user_id));

                            itemUserList.add(itemUser);

                        }

                    } else {
                        loadCount = 0;
                    }

                } else if (p85Result == 0) {
                    p85Message = JsonUtility.GetString(jsonObject, Key.message);
                } else {
                    p85Result = -1;
                }

            } catch (Exception e) {
                p85Result = -1;
                e.printStackTrace();
            }
        }

    }


    private class GetMyFollowTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            round = 0;
            sizeMax = false;
        }

        @Override
        protected Object doInBackground(Void... params) {

            callProtocol85(round + "," + count);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);


            if (p85Result == 1) {

                if (loadCount == 0) {
                    sizeMax = true; // 已達最大值

                    tvGuide.setVisibility(View.VISIBLE);
                    pinPinBoxRefreshLayout.setVisibility(View.GONE);
                    return;

                } else {
                    tvGuide.setVisibility(View.GONE);
                    pinPinBoxRefreshLayout.setVisibility(View.VISIBLE);
                }

                round = round + count;

                adapter.notifyDataSetChanged();

                ViewPropertyAnimator alphaTo1 = pinPinBoxRefreshLayout.animate();
                alphaTo1.setDuration(750)
                        .alpha(1)
                        .start();


            } else if (p85Result == 0) {

                DialogV2Custom.BuildError(mActivity, p85Message);


            } else if (p85Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }

    }

    private class MoreDataTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            doingType = DoingTypeClass.DoMoreData;

            pbLoadMore.setVisibility(View.VISIBLE);
            pbLoadMore.progressiveStart();

        }

        @Override
        protected Object doInBackground(Void... params) {
            callProtocol85(round + "," + count);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();

            if (p85Result == 1) {

                if (loadCount == 0) {
                    sizeMax = true;
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }
                    return;
                }
                adapter.notifyItemRangeInserted(itemUserList.size(), count);

                round = round + count;


            } else if (p85Result == 0) {

                DialogV2Custom.BuildError(mActivity, p85Message);

            } else if (p85Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }

    }

    private class RefreshTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoRefresh;
        }

        @Override
        protected Object doInBackground(Void... params) {

            callProtocol85(round + "," + count);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            sizeMax = false;
            isNoDataToastAppeared = false;

            pinPinBoxRefreshLayout.setRefreshing(false);


            if (p85Result == 1) {

                if (loadCount == 0) {
                    sizeMax = true; // 已達最大值

                    tvGuide.setVisibility(View.VISIBLE);
                    pinPinBoxRefreshLayout.setVisibility(View.GONE);
                    return;

                } else {
                    tvGuide.setVisibility(View.GONE);
                    pinPinBoxRefreshLayout.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
                round = round + count;


            } else if (p85Result == 0) {

                DialogV2Custom.BuildError(mActivity, p85Message);

            } else if (p85Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }

    }

    private class ChangeFollowTask extends AsyncTask<Void, Void, Object> {

        private String p12Message = "";
        private int p12Result = -1;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoChangeFollow;
            startLoading();
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
            sendData.put("authorid", itemUserList.get(clickPosition).getUser_id());
            sendData.put("sign", sign);

            try {
                String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P12_ChangeFollowStatus, sendData, null);//12
                JSONObject jsonObject = new JSONObject(strJson);
                p12Result = JsonUtility.GetInt(jsonObject, Key.result);

                if (p12Result == 0) {
                    p12Message = JsonUtility.GetString(jsonObject, Key.message);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p12Result == 1) {

                removeItem(clickPosition);

            } else if (p12Result == 0) {

                DialogV2Custom.BuildError(mActivity, p12Message);

            } else if (p12Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }

    }

    public void removeItem(int clickPosition) {

        RecyclerView.ItemAnimator animator = rvFollow.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        adapter.removeData(clickPosition);

        RecyclerView.ItemAnimator animator2 = rvFollow.getItemAnimator();
        if (animator2 instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator2).setSupportsChangeAnimations(false);
        }

        if (itemUserList.size() < 1) {
            tvGuide.setVisibility(View.VISIBLE);
            pinPinBoxRefreshLayout.setVisibility(View.GONE);
        }
    }


    public void removeItemById(String user_id) {


        for (int i = 0; i < itemUserList.size(); i++) {

            if (user_id.equals(itemUserList.get(i).getUser_id())) {

                MyLog.Set("d", getClass(), "removeItemById => " + user_id);

                removeItem(i);
                break;
            }

        }
    }


    @Override
    public void changeType(int position) {

        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }

        clickPosition = position;

        final DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setMessage(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_dialog_message_check_cancel_follow)  + " " + itemUserList.get(position).getName() + "?");
        d.setStyle(DialogStyleClass.CHECK);
        d.setCheckExecute(new CheckExecute() {
            @Override
            public void DoCheck() {
                doChangeFollow();
            }
        });
        d.show();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backImg:
                back();
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
        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);
        if (getMyFollowTask != null && !getMyFollowTask.isCancelled()) {
            getMyFollowTask.cancel(true);
        }
        getMyFollowTask = null;

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
        }
        moreDataTask = null;

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
        }
        refreshTask = null;


        Recycle.IMG(backImg);

        cleanPicasso();
        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


}

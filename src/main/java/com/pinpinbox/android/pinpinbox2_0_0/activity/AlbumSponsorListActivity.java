package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerInteractiveAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol114_GetAlbumSponsorList;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopBoard;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * Created by vmage on 2018/6/8.
 */

public class AlbumSponsorListActivity extends DraggerActivity implements View.OnClickListener, RecyclerInteractiveAdapter.OnUserInterativeListener {


    private Activity mActivity;
    private List<ItemUser> itemUserList;
    private RecyclerInteractiveAdapter adapter;

    private FollowTask followTask;
    private AttentionTask attentionTask;
    private Protocol114_GetAlbumSponsorList protocol114;
    private PopBoard board;

    private RecyclerView rvUser;
    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private SmoothProgressBar pbLoadMore;
    private ImageView backImg;
    private TextView tvGuide;

    private String album_id;

    private boolean isDoingMore = false;

    private int doingType;
    private int clickPosition = -1;

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            if ((Object) protocol114.isSizeMax() != null && !protocol114.isSizeMax()) {
                MyLog.Set("e", mActivity.getClass(), "onLoad");

                if (isDoingMore) {
                    MyLog.Set("e", mActivity.getClass(), "正在讀取更多項目");
                    return;
                }

                doMoreData();

            } else {

                if (!protocol114.isNoDataToastAppeared()) {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);

                    protocol114.setNoDataToastAppeared(true);

                }

                MyLog.Set("e", mActivity.getClass(), "sizeMax");

            }

        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_userlist);

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();

        init();

        setRecycler();

        setProtocol();

        doGetSponsor();

    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){


            album_id = bundle.getString(Key.album_id, "");

        }


    }

    private void init() {

        mActivity = this;

        itemUserList = new ArrayList<>();

        rvUser = (RecyclerView) findViewById(R.id.rvUser);
        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.pinPinBoxRefreshLayout);
        backImg = (ImageView) findViewById(R.id.backImg);
        tvGuide = (TextView) findViewById(R.id.tvGuide);
        tvGuide.setText(R.string.pinpinbox_2_0_0_guide_sponsor_empty);


        pbLoadMore = (SmoothProgressBar) findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();

        SmoothProgressBar pbRefresh = (SmoothProgressBar) findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(findViewById(R.id.vRefreshAnim), pbRefresh);

        rvUser.setItemAnimator(new DefaultItemAnimator());
        rvUser.addOnScrollListener(mOnScrollListener);

        backImg.setOnClickListener(this);

        TextView tvAcTitile = (TextView) findViewById(R.id.tvActionBarTitle);
        tvAcTitile.setText(R.string.pinpinbox_2_0_0_title_who_sponsor_this_work);


    }

    private void setRecycler() {

        adapter = new RecyclerInteractiveAdapter(mActivity, itemUserList);
        rvUser.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvUser.setLayoutManager(manager);

        adapter.setOnRecyclerViewListener(new RecyclerInteractiveAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                if(itemUserList.get(position).getUser_id().equals(PPBApplication.getInstance().getId())){
                    return;
                }

                clickPosition = position;

                ActivityIntent.toUser(
                        mActivity,
                        true,
                        false,
                        itemUserList.get(position).getUser_id(),
                        itemUserList.get(position).getPicture(),
                        itemUserList.get(position).getName(),
                        v.findViewById(R.id.userImg)
                );

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        adapter.setOnUserInterativeListener(this);

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

    private void doGetSponsor() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        protocol114.GetList();

    }

    private void doRefresh() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        if (protocol114 != null && !protocol114.getTask().isCancelled()) {

            protocol114.getTask().cancel(true);

        }

        protocol114.Refresh();
    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        protocol114.LoadMore();
    }

    private void doAttention() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        attentionTask = new AttentionTask();
        attentionTask.execute();

    }

    private void doFollowTask() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        followTask = new FollowTask();
        followTask.execute();
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {

                    case DoingTypeClass.DoChangeFollow:
                        doAttention();
                        break;

                    case DoingTypeClass.DoFollowTask:
                        doFollowTask();
                        break;

                }
            }
        };
        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void setProtocol() {

        protocol114 = new Protocol114_GetAlbumSponsorList(
                mActivity,
                album_id,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                itemUserList,
                new Protocol114_GetAlbumSponsorList.TaskCallBack() {
                    @Override
                    public void Prepare(int doingType) {
                        switch (doingType) {

                            case DoingTypeClass.DoDefault:

                                startLoading();

                                break;

                            case DoingTypeClass.DoRefresh:

                                itemUserList.clear();
                                adapter.notifyDataSetChanged();


                                break;

                            case DoingTypeClass.DoMoreData:
                                isDoingMore = true;
                                pbLoadMore.setVisibility(View.VISIBLE);
                                pbLoadMore.progressiveStart();

                                break;

                        }
                    }

                    @Override
                    public void Post(int doingType) {

                        if (itemUserList.size() > 0) {
                            tvGuide.setVisibility(View.GONE);
                        } else {
                            tvGuide.setVisibility(View.VISIBLE);
                        }

                        switch (doingType) {

                            case DoingTypeClass.DoDefault:

                                dissmissLoading();

                                ViewControl.AlphaTo1(pinPinBoxRefreshLayout);


                                MyLog.Set("e", mActivity.getClass(), "public void Post");


                                break;

                            case DoingTypeClass.DoRefresh:

                                pinPinBoxRefreshLayout.setRefreshing(false);

                                break;

                            case DoingTypeClass.DoMoreData:
                                isDoingMore = false;
                                pbLoadMore.setVisibility(View.GONE);
                                pbLoadMore.progressiveStop();

                                break;

                        }

                    }

                    @Override
                    public void Success(int doingType) {
                        switch (doingType) {

                            case DoingTypeClass.DoDefault:

                                adapter.notifyDataSetChanged();

                                break;

                            case DoingTypeClass.DoRefresh:

                                adapter.notifyDataSetChanged();

                                break;

                            case DoingTypeClass.DoMoreData:

                                adapter.notifyItemRangeInserted(protocol114.getItemUserList().size(), protocol114.getRangeCount());

                                break;


                        }
                    }

                    @Override
                    public void TimeOut(int doingType) {
                        switch (doingType) {

                            case DoingTypeClass.DoDefault:

                                doGetSponsor();

                                break;

                            case DoingTypeClass.DoRefresh:

                                doRefresh();

                                break;

                            case DoingTypeClass.DoMoreData:

                                doMoreData();

                                break;

                        }
                    }
                }
        );

    }

    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);

    }

    private void cleanCache() {


        if (itemUserList != null) {

            int count = itemUserList.size();

            for (int i = 0; i < count; i++) {
                Picasso.get().invalidate(itemUserList.get(i).getPicture());
            }

            System.gc();

        }

    }


    @SuppressLint("StaticFieldLeak")
    private class AttentionTask extends AsyncTask<Void, Void, Object> {

        private int p12Result = -1;
        private String p12Message = "";

        private int followstatus;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoChangeFollow;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> data = new HashMap<>();
            data.put(Key.id, PPBApplication.getInstance().getId());
            data.put(Key.token, PPBApplication.getInstance().getToken());
            data.put("authorid", itemUserList.get(clickPosition).getUser_id());
            String sign = IndexSheet.encodePPB(data);

            Map<String, String> sendData = new HashMap<>();
            sendData.put(Key.id, PPBApplication.getInstance().getId());
            sendData.put(Key.token, PPBApplication.getInstance().getToken());
            sendData.put("authorid", itemUserList.get(clickPosition).getUser_id());
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P12_ChangeFollowStatus, sendData, null);//12
                MyLog.Set("d", getClass(), "p12strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p12Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p12Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p12Result == 1) {
                        String strData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONObject jsonData = new JSONObject(strData);

                        followstatus = JsonUtility.GetInt(jsonData, ProtocolKey.followstatus);

                        if (followstatus == 0) {
                            itemUserList.get(clickPosition).setFollow(false);
                        } else if (followstatus == 1) {
                            itemUserList.get(clickPosition).setFollow(true);
                        }


                    } else if (p12Result == 0) {
                        p12Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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
            if (p12Result == 1) {


                adapter.notifyItemChanged(clickPosition);

                if (followstatus == 1) {
                    doFollowTask();
                }


            } else if (p12Result == 0) {
                DialogV2Custom.BuildError(mActivity, p12Message);

            } else if (p12Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FollowTask extends AsyncTask<Void, Void, Object> {

        private String restriction;
        private String restriction_value;
        private String name;
        private String reward;
        private String reward_value;
        private String url;
        private String p83Result = "", p83Message = "";

        private int numberofcompleted;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFollowTask;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> map = new HashMap<>();
            map.put(MapKey.id, PPBApplication.getInstance().getId());
            map.put(MapKey.token, PPBApplication.getInstance().getToken());
            map.put(MapKey.task_for, TaskKeyClass.follow_user);
            map.put(MapKey.platform, "google");
            String sign = IndexSheet.encodePPB(map);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(MapKey.id, PPBApplication.getInstance().getId());
            sendData.put(MapKey.token, PPBApplication.getInstance().getToken());
            sendData.put(MapKey.task_for, TaskKeyClass.follow_user);
            sendData.put(MapKey.platform, "google");
            sendData.put(MapKey.type, Value.user);
            sendData.put(MapKey.type_id, itemUserList.get(clickPosition).getUser_id());
            sendData.put("sign", sign);

            String strJson = "";


            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                MyLog.Set("d", mActivity.getClass(), "p83strJson => " + strJson);
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
                        restriction = taskObj.getString(Key.restriction);
                        restriction_value = taskObj.getString(Key.restriction_value);

                        numberofcompleted = taskObj.getInt(Key.numberofcompleted);

                        JSONObject eventObj = new JSONObject(event);
                        url = eventObj.getString(Key.url);

                    } else if (p83Result.equals("2")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("3")) {
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

                if (restriction.equals("personal")) {
                    d.getTvTitle().setText(name);
                    d.getTvRestriction().setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_count) + numberofcompleted + "/" + restriction_value);
                    d.getTvRestriction().setVisibility(View.VISIBLE);
                } else {
                    d.getTvTitle().setText(name);
                }

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");
                    /*獲取當前使用者P點*/
                    String point = PPBApplication.getInstance().getData().getString(Key.point, "");
                    int p = StringIntMethod.StringToInt(point);

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    PPBApplication.getInstance().getData().edit().putString(Key.point, newP).commit();


                } else {
                    d.getTvDescription().setText(reward_value);
                }


                if(url==null || url.equals("")|| url.equals("null")){
                    d.getTvLink().setVisibility(View.GONE);
                }else {
                    d.getTvLink().setVisibility(View.VISIBLE);
                    d.getTvLink().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            Intent intent = new Intent(mActivity, WebViewActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);
                        }
                    });
                }

            } else if (p83Result.equals("2")) {


            } else if (p83Result.equals("3")) {


            } else if (p83Result.equals("0")) {


            } else if (p83Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }


    @Override
    public void onClick(View v) {


        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.backImg:
                back();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void doFollow(int position) {

        MyLog.Set("e", getClass(), "doFollow position =>" + position);

        clickPosition = position;

        doAttention();

    }

    @Override
    public void doPostMessage(int position) {
        MyLog.Set("e", getClass(), "doPostMessage position =>" + position);

        if(!itemUserList.get(position).isDisscuss()){
            PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_board_is_close);
            return;
        }

        if (board != null) {
            board.dismiss();
            board = null;
        }

        board = new PopBoard(mActivity, PopBoard.TypeUser, itemUserList.get(position).getUser_id(), (RelativeLayout) findViewById(R.id.rBackground), true);
        board.setSecondTitle(itemUserList.get(position).getName());


    }



    @Override
    protected void onPause() {

        cleanCache();

        super.onPause();

    }

    @Override
    public void onDestroy() {
        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (protocol114 != null) {
            cancelTask(protocol114.getTask());
        }

        if (attentionTask != null) {
            cancelTask(attentionTask);
        }

        if (followTask != null) {
            cancelTask(followTask);
        }

        cleanCache();

        Recycle.IMG(backImg);

        if (board != null) {
            board.dismiss();
            board = null;
        }


        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


}

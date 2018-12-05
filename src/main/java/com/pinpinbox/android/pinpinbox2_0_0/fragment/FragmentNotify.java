package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumInfoActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CreationActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MainActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollectActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerNotifyAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.GAControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by kevin9594 on 2016/12/17.
 */
public class FragmentNotify extends Fragment {

    private SharedPreferences getdata;
    private NoConnect noConnect;
    private SimpleDateFormat format;
    private Calendar calendar;
    private LoadingAnimation loading;

    private GetPushQueueTask getPushQueueTask;
    private MoreDataTask moreDataTask;
    private RefreshTask refreshTask;

    private ArrayList<HashMap<String, Object>> p87arrayList;

    private RecyclerNotifyAdapter adapter;

    private SmoothProgressBar pbLoadMore;
    private View viewHeader;
    private RecyclerView rvNotify;
    private TextView tvGuide;
    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;


    private String id, token;
    private String p87Result, p87Message;
    private String strCurrentTime; //當前時間
    private String strDateTitle; // 用於 item date;

    private int loadCount = 0; //讀取到的數量
    private int round = 0, count = 24;
    private int defaultCount = 8;
    private int dateCount = 0;
    private int doingType;


    private float scale = 0;

    //    private boolean doRefreshAnim = true;
    private boolean sizeMax = false;
    private boolean isCheckTodayData = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean isGetPushQueue = false;
    private boolean isDoingMore = false;


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                MyLog.Set("e", FragmentNotify.class, "onLoad");
                if (isDoingMore) {
                    MyLog.Set("e", FragmentNotify.class, "正在讀取更多項目");
                    return;
                }
                doMoreData();
            } else {
                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }
                MyLog.Set("e", FragmentNotify.class, "sizeMax");
            }
        }


    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_notify, container, false);

        viewHeader = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.header_2_0_0_notify, null);
//        refreshImg = (ImageView) viewHeader.findViewById(R.id.refreshImg);
        TextView tvTitle = (TextView) viewHeader.findViewById(R.id.tvTitle);
        TextUtility.setBold(tvTitle, true);


        pbLoadMore = (SmoothProgressBar) v.findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();


        tvGuide = (TextView) v.findViewById(R.id.tvGuide);
        rvNotify = (RecyclerView) v.findViewById(R.id.rvNotify);
        rvNotify.setItemAnimator(new DefaultItemAnimator());
        rvNotify.addOnScrollListener(mOnScrollListener);


        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) v.findViewById(R.id.pinPinBoxRefreshLayout);

        setRefreshListener();

        //20171002
        SmoothProgressBar pbRefresh = (SmoothProgressBar) v.findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(v.findViewById(R.id.vRefreshAnim), pbRefresh);


        return v;
    }

    private void setRefreshListener() {

        pinPinBoxRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh(true);
            }

            @Override
            public void onPullDistance(int distance) {


            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        init();
//
//        setRecycler();
//
//        doGetPushQueue();

    }

    private void init() {

        loading = ((MainActivity) getActivity()).getLoading();

        getdata = PPBApplication.getInstance().getData();
        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        p87arrayList = new ArrayList<>();

        format = new SimpleDateFormat("yyyy-MM-dd");
        calendar = Calendar.getInstance();

    }

    private void setRecycler() {

        adapter = new RecyclerNotifyAdapter(getActivity(), p87arrayList);
        rvNotify.setAdapter(adapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvNotify.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvNotify.setLayoutManager(manager);
        RecyclerViewUtils.setHeaderView(rvNotify, viewHeader);
        setUpdateHeader();//check

        adapter.setOnRecyclerViewListener(new RecyclerNotifyAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {
                itemClick(v, position);
            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void setUpdateHeader() {
        boolean update = getdata.getBoolean(Key.update, false);
        if (update) {
//                View vHeader = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.header_update, null);


            RelativeLayout rGoUpdateApp = (RelativeLayout) viewHeader.findViewById(R.id.rGoUpdateApp);

            rGoUpdateApp.setVisibility(View.VISIBLE);


            rGoUpdateApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        MyLog.Set("d", FragmentNotify.class, "open google play app");
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        MyLog.Set("d", FragmentNotify.class, "open google play web");
                    }
                }
            });


        }
    }

    public void scrollToTop() {

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvNotify.getLayoutManager();

        try {

            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

            if (firstVisibleItemPosition > 10) {
                rvNotify.scrollToPosition(10);
                MyLog.Set("d", getClass(), "先移動至第10項目");
            }

            rvNotify.smoothScrollToPosition(0);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void itemClick(View v, int position) {

        String type = (String) p87arrayList.get(position).get("type");

        if (type == null || type.equals("") || type.equals("null")) {

            String url = (String) p87arrayList.get(position).get(Key.url);

            if (url != null && !url.equals("null") && !url.equals("")) {

                ActivityIntent.toWeb(getActivity(), url, "");

            }

            return;
        }

        String type_id = (String) p87arrayList.get(position).get("type_id");


        if (type.equals(Key.categoryarea)) {

            if (!type_id.equals("") && type_id != null && !type.equals("null")) {

                ActivityIntent.toFeature(getActivity(), StringIntMethod.StringToInt(type_id));

            }

        }


        if (type.equals(Key.event)) {

            if (!type_id.equals("") && type_id != null && !type.equals("null")) {

                ActivityIntent.toEvent(getActivity(), type_id);

            }

        }


        if (type.equals(Key.user)) {


            if (!type_id.equals("") && type_id != null) {
                if (type_id.equals(id)) {
                    //me
                    ((MainActivity) getActivity()).toMePage(false);
                } else {
                    //other user
                    ActivityIntent.toUser(
                            getActivity(),
                            true,
                            false,
                            type_id,
                            (String) p87arrayList.get(position).get(Key.image),
                            null,
                            v.findViewById(R.id.userImg)
                    );
                }
            }

        }


        if (type.equals(Key.user_messageboard)) {
            if (!type_id.equals("") && type_id != null) {
                if (type_id.equals(id)) {
                    //me
                    ((MainActivity) getActivity()).toMePage(true);
                } else {
                    //other user
                    ActivityIntent.toUser(
                            getActivity(),
                            true,
                            true,
                            type_id,
                            (String) p87arrayList.get(position).get(Key.image),
                            null,
                            v.findViewById(R.id.userImg)
                    );
                }
            }
        }


        if (type.equals(Key.albumqueue)) {

            ActivityIntent.toAlbumInfo(getActivity(), false, type_id, null, 0, 0, 0, null);

        }


        if (type.equals(Key.albumqueue_messageboard)) {

            Bundle bundle = new Bundle();
            bundle.putString(Key.album_id, type_id);
            bundle.putBoolean(Key.pinpinboard, true);
            bundle.putBoolean(Key.shareElement, false);
            Intent intent = new Intent(getActivity(), AlbumInfoActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.bottom_enter, R.anim.view_stay);


        }

        if (type.equals(Key.albumcooperation)) {

            String identity = (String) p87arrayList.get(position).get(Key.identity);
            if (identity != null && identity.equals(Key.viewer)) {
                DialogV2Custom d = new DialogV2Custom(getActivity());
                d.setStyle(DialogStyleClass.CHECK);
                d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_grade_is_viewer_can_not_edit_content_and_go_to_cooperation_manage);
                d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_to_cooperation_manage);
                d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_be_later);

                d.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {
                        Bundle bundle = new Bundle();
                        bundle.putInt("toPage", 2);

                        Intent intent = new Intent(getActivity(), MyCollectActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        ActivityAnim.StartAnim(getActivity());
                    }
                });
                d.show();
                return;

            } else if (identity == null || identity.equals("")) {

                DialogV2Custom.BuildError(getActivity(), getResources().getString(R.string.pinpinbox_2_0_0_dialog_message_cooperation_is_cancel));

                return;
            }

            int template_id = (Integer) p87arrayList.get(position).get(Key.template_id);
            Intent intent = new Intent(getActivity(), CreationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Key.album_id, type_id);
            bundle.putString(Key.identity, identity);
            if (template_id == 0) {
                //快速
                bundle.putInt("create_mode", 0);

            } else {
                //版型
                bundle.putInt("create_mode", 1);
            }

            intent.putExtras(bundle);
            startActivity(intent);
            ActivityAnim.StartAnim(getActivity());
        }

    }

    private void protocol87(String limit) {

        MyLog.Set("d", getClass(), "limit => " + limit);

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.p87_GetPushQueue, SetMapByProtocol.setParam87_getpushqueue(id, token, limit), null);
            MyLog.Set("d", FragmentNotify.class, "p87strJson => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p87Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p87Result = jsonObject.getString(Key.result);
                if (p87Result.equals("1")) {
                    String jsonData = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    Logger.json(jsonData);

                    if (jsonData != null && !jsonData.equals("")) {

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        JSONArray jsonArray = new JSONArray(jsonData);
                        loadCount = jsonArray.length();

                        for (int i = 0; i < loadCount; i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);

                            /*get template_id*/
                            String template = JsonUtility.GetString(object, ProtocolKey.template);
                            int template_id = -1;
                            if (template != null && !template.equals("")) {
                                JSONObject templateObj = new JSONObject(template);
                                template_id = JsonUtility.GetInt(templateObj, ProtocolKey.template_id);
                            }

                            /*get identity*/
                            String cooperation = JsonUtility.GetString(object, ProtocolKey.cooperation);
                            String identity = "";
                            if (cooperation != null && !cooperation.equals("")) {
                                JSONObject cooperationObj = new JSONObject(cooperation);
                                identity = JsonUtility.GetString(cooperationObj, ProtocolKey.identity);
                            }


                            /*get notify detail*/
                            String pushqueue = JsonUtility.GetString(object, ProtocolKey.pushqueue);
                            JSONObject pushqueueObj = new JSONObject(pushqueue);
                            String message = JsonUtility.GetString(pushqueueObj, ProtocolKey.message);
                            String target2type = JsonUtility.GetString(pushqueueObj, ProtocolKey.target2type);
                            String target2type_id = JsonUtility.GetString(pushqueueObj, ProtocolKey.target2type_id);
                            String image_url = JsonUtility.GetString(pushqueueObj, ProtocolKey.image_url);
                            String inserttime = JsonUtility.GetString(pushqueueObj, ProtocolKey.inserttime);
                            String url = JsonUtility.GetString(pushqueueObj, ProtocolKey.url);


                            Date currentTime = df.parse(strCurrentTime);
                            Date messageTime = df.parse(inserttime);
                            long l = currentTime.getTime() - messageTime.getTime();
                            long day = l / (24 * 60 * 60 * 1000);
                            long hour = (l / (60 * 60 * 1000) - day * 24);
                            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
//                            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

                            String strMessageTime = "";
                            if (day != 0) {

                                if (day < 2) {
                                    strMessageTime = day + getResources().getString(R.string.pinpinbox_2_0_0_time_before_day_single);
                                } else {
                                    strMessageTime = day + getResources().getString(R.string.pinpinbox_2_0_0_time_before_day);
                                }


                            } else if (hour != 0) {

                                if (hour < 2) {
                                    strMessageTime = hour + getResources().getString(R.string.pinpinbox_2_0_0_time_before_hour_single);
                                } else {
                                    strMessageTime = hour + getResources().getString(R.string.pinpinbox_2_0_0_time_before_hour);
                                }


                            } else if (min != 0) {


                                if (min < 2) {
                                    strMessageTime = min + getResources().getString(R.string.pinpinbox_2_0_0_time_before_minute_single);
                                } else {
                                    strMessageTime = min + getResources().getString(R.string.pinpinbox_2_0_0_time_before_minute);
                                }

                            } else {
                                strMessageTime = getResources().getString(R.string.pinpinbox_2_0_0_time_before_just_now);
                            }


                            String strNewDate = inserttime.substring(0, 10);

                            /*檢查今日有無訊息*/
                            if (!isCheckTodayData) {
                                /*第一項日期不等於今日表示今日尚無訊息並添加無訊息item*/
                                if (!strNewDate.equals(strDateTitle)) {
                                    HashMap<String, Object> mapNoMessage = new HashMap<>();
                                    mapNoMessage.put("type", "date");
                                    mapNoMessage.put("date", Value.noMessage);
                                    p87arrayList.add(mapNoMessage);
                                    dateCount++;
                                }
                                isCheckTodayData = true;
                            }




                            /*取得第一項 date 跟 strDateTitle 比對
                            不等於則添加新的 dateTitle 並覆蓋 strDateTitle*/
                            if (!strNewDate.equals(strDateTitle)) {
                                HashMap<String, Object> mapNewTitle = new HashMap<>();
                                mapNewTitle.put("type", "date");
                                mapNewTitle.put("date", strNewDate + " " + getWeek(strNewDate));
                                p87arrayList.add(mapNewTitle);
                                strDateTitle = strNewDate;
                                dateCount++;
                            }


//                            ItemNotify itemNotify = new ItemNotify();
//                            itemNotify.setTemplate_id(template_id);
//                            itemNotify.setIdentity(identity);
//                            itemNotify.setImage_url(image_url);
//                            itemNotify.setMessage(message);
//                            itemNotify.setTarget2type(target2type);
//                            itemNotify.setTarget2type_id(target2type_id);
//                            itemNotify.setInserttime(strMessageTime);
//                            itemNotify.setUrl(url);

                            HashMap<String, Object> mp = new HashMap<>();
                            mp.put(Key.template_id, template_id);
                            mp.put(Key.identity, identity);
                            mp.put("image", image_url);
                            mp.put("message", message);
                            mp.put("type", target2type);
                            mp.put("type_id", target2type_id);
                            mp.put("messageTime", strMessageTime);
                            mp.put(Key.url, url);
                            p87arrayList.add(mp);

                        }

                    } else {
                        loadCount = 0;
                    }

                } else if (p87Result.equals("0")) {
                    p87Message = jsonObject.getString(Key.message);
                } else {
                    p87Result = "";
                }

            } catch (Exception e) {
                p87Result = "";
                e.printStackTrace();
            }
        }
    }

    private void cleanPicasso() {

        if (p87arrayList != null) {

            int count = p87arrayList.size();

            for (int i = 0; i < count; i++) {
                if (!((String) p87arrayList.get(i).get(MapKey.type)).equals("date")) {
                    String strImage = (String) p87arrayList.get(i).get("image");
                    Picasso.with(getActivity().getApplicationContext()).invalidate(strImage);
                }

            }
        }

    }

    private void cleanBadge() {

        getdata.edit().putInt(Key.badgeCount, 0).commit();
        ShortcutBadger.removeCount(getActivity().getApplicationContext());

        MyLog.Set("d", getClass(), "cleanBadge");

    }

    private void connectInstability() {
        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoingTypeClass.DoDefault:
                        doGetPushQueue();
                        break;
                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;
                    case DoingTypeClass.DoRefresh:
                        doRefresh(false);
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);
    }

    private void doGetPushQueue() {
        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }
        getPushQueueTask = new GetPushQueueTask();
        getPushQueueTask.execute();
    }

    public void doRefresh(boolean anim) {

//        doRefreshAnim = anim;

        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }

        if (getPushQueueTask != null && !getPushQueueTask.isCancelled()) {
            getPushQueueTask.cancel(true);
            getPushQueueTask = null;
        }

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
            refreshTask = null;
        }

        if (p87arrayList != null) {

            int count = p87arrayList.size();

            if (adapter != null) {
                adapter.notifyItemRangeRemoved(1, count);
            }

            /*回收資源*/
            if (count > 0) {
                cleanPicasso();
            }

            /*清空數聚*/
            p87arrayList.clear();


        } else {

            p87arrayList = new ArrayList<>();

        }


        //重置load次數
        round = 0;

        //重置日期項目
        dateCount = 0;

        //重置檢察當日訊息
        isCheckTodayData = false;


        refreshTask = new RefreshTask();
        refreshTask.execute();

    }

    private void doMoreData() {

        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
            moreDataTask = null;
        }

        moreDataTask = new MoreDataTask();
        moreDataTask.execute();

    }

    private void setCurrentTime() {
        /*獲取當前時間*/
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        strCurrentTime = df.format(curDate);


        /*添加第一項 date title*/
        strDateTitle = strCurrentTime.substring(0, 10);
        HashMap<String, Object> mapTitle = new HashMap<>();
        mapTitle.put("type", "date");
        mapTitle.put("date", strDateTitle + " " + getWeek(strDateTitle));
        p87arrayList.add(mapTitle);
        dateCount++;

    }

    private String getWeek(String pTime) {

        String Week = "";

        try {

            calendar.setTime(format.parse(pTime));

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += getResources().getString(R.string.pinpinbox_2_0_0_itemtype_sunday);
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += getResources().getString(R.string.pinpinbox_2_0_0_itemtype_monday);
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += getResources().getString(R.string.pinpinbox_2_0_0_itemtype_tuesday);
            ;
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += getResources().getString(R.string.pinpinbox_2_0_0_itemtype_wednesday);
            ;
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += getResources().getString(R.string.pinpinbox_2_0_0_itemtype_thursday);
            ;
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += getResources().getString(R.string.pinpinbox_2_0_0_itemtype_friday);
            ;
        }
        if (calendar.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += getResources().getString(R.string.pinpinbox_2_0_0_itemtype_saturday);
            ;
        }


        return Week;
    }

    private void setRound() {
        round = round + count;
        dateCount = 0;
    }

    private class GetPushQueueTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MyLog.Set("d", GetPushQueueTask.class, "GetPushQueueTask");

            doingType = DoingTypeClass.DoDefault;

            //20171205
            p87arrayList.clear();


            setCurrentTime();


            loading.show();


        }

        @Override
        protected Object doInBackground(Void... params) {

            protocol87(round + "," + defaultCount);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            isGetPushQueue = true;

            ViewControl.AlphaTo1(pinPinBoxRefreshLayout);


            if (p87Result.equals("1")) {
                if (loadCount > 0) {
                    tvGuide.setVisibility(View.GONE);
                    pinPinBoxRefreshLayout.setVisibility(View.VISIBLE);

                    adapter.notifyDataSetChanged();

                    round = round + defaultCount;
                    dateCount = 0;
                } else {
                    pinPinBoxRefreshLayout.setVisibility(View.GONE);
                    tvGuide.setVisibility(View.VISIBLE);
                }


                //20171019
                cleanBadge();


            } else if (p87Result.equals("0")) {

                DialogV2Custom.BuildError(getActivity(), p87Message);

            } else if (p87Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), this.getClass().getSimpleName());
            }


        }

    }

    private class MoreDataTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            isDoingMore = true;

            doingType = DoingTypeClass.DoMoreData;

            pbLoadMore.setVisibility(View.VISIBLE);
            pbLoadMore.progressiveStart();

        }

        @Override
        protected Object doInBackground(Void... params) {
            protocol87(round + "," + count);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            isDoingMore = false;

            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();

            if (p87Result.equals("1")) {

                MyLog.Set("e", FragmentNotify.class, "loadCount => " + loadCount);

                if (loadCount == 0) {
                    MyLog.Set("d", FragmentNotify.class, "已達最大值");
                    sizeMax = true; // 已達最大值
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }
                    return;
                }


                adapter.notifyItemRangeInserted(p87arrayList.size(), count + dateCount);

                setRound();

            } else if (p87Result.equals("0")) {

                DialogV2Custom.BuildError(getActivity(), p87Message);

            } else if (p87Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }

    }

    private class RefreshTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MyLog.Set("d", RefreshTask.class, "RefreshTask");

            sizeMax = false;
            doingType = DoingTypeClass.DoRefresh;

            setCurrentTime();

//            if (doRefreshAnim) {
//               pinPinBoxRefreshLayout.startPinpinboxRefresh();
//            }

        }

        @Override
        protected Object doInBackground(Void... params) {

            protocol87(round + "," + defaultCount);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            sizeMax = false;
            isNoDataToastAppeared = false;

            if (pinPinBoxRefreshLayout.getAlpha() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ViewPropertyAnimator alphaTo1 = pinPinBoxRefreshLayout.animate();
                        alphaTo1.setDuration(400)
                                .alpha(1)
                                .start();
                    }
                }, 200);
            }

            pinPinBoxRefreshLayout.setRefreshing(false);


            if (p87Result.equals("1")) {

                ((MainActivity) getActivity()).hideRP_notify();

                if (p87arrayList.size() > 0) {
                    tvGuide.setVisibility(View.GONE);
                    pinPinBoxRefreshLayout.setVisibility(View.VISIBLE);
                    adapter.notifyItemRangeInserted(1, p87arrayList.size());
                    round = round + defaultCount;
                    dateCount = 0;
                } else {
                    pinPinBoxRefreshLayout.setVisibility(View.GONE);
                    tvGuide.setVisibility(View.VISIBLE);

                }

                //20171019
                cleanBadge();


            } else if (p87Result.equals("0")) {
                DialogV2Custom.BuildError(getActivity(), p87Message);
            } else if (p87Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }

    }


    public boolean isGetPushQueue() {
        return isGetPushQueue;
    }


    @Override
    public void onPause() {
        cleanPicasso();
        super.onPause();
    }


    @Override
    public void onDestroy() {

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
            refreshTask = null;
        }

        if (getPushQueueTask != null && !getPushQueueTask.isCancelled()) {
            getPushQueueTask.cancel(true);
            getPushQueueTask = null;
        }

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
            moreDataTask = null;
        }

        cleanPicasso();

        System.gc();

        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


    private boolean isVisible;
    private boolean dowork = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        // Make sure that fragment is currently visible
        if (!isVisible && isResumed()) {
            // 调用Fragment隐藏的代码段

            MyLog.Set("e", getClass(), "目前看不到 FragmentNotify");


        } else if (isVisible && isResumed()) {
            // 调用Fragment显示的代码段

            MyLog.Set("e", getClass(), "可看見 FragmentNotify");
            GAControl.sendViewName("通知");

            if (!dowork) {
                init();
                setRecycler();
                doGetPushQueue();
                dowork = true;
            }
        }
    }


}


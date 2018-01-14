package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.pinpinbox.android.Activity.ReadAlbum.ReadAlbumActivity;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.DirClass;
import com.pinpinbox.android.StringClass.DoingTypeClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.TaskKeyClass;
import com.pinpinbox.android.StringClass.UrlClass;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumGroup2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Creation2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollect2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Reader2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCollectAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.listener.OnDetailClickListener;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;
import com.pinpinbox.android.pinpinbox2_0_0.service.DownLoadService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by vmage on 2017/1/6.
 */
public class FragmentCooperation2 extends Fragment implements OnDetailClickListener {

    private SharedPreferences getdata;
    private ShareDialog shareDialog;
    private LoadingAnimation loading;

    private GetAlbumTask getAlbumTask;
    private MoreDataTask moreDataTask;
    private RefreshTask refreshTask;
    private DeleteAlbumTask deleteAlbumTask;
    private CheckShareTask checkShareTask;
    private ShareTask shareTask;
    private ToAlbumTask toAlbumTask;

    public ArrayList<HashMap<String, Object>> p17arraylist;

    private RecyclerCollectAdapter adapter;

    private RecyclerView rvCollect;
    private SmoothProgressBar pbLoadMore;
    private RelativeLayout rGuideCooperation;
    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;

    private String id, token;
    private String p45Result, p45Message; //delete
    private String p17Result, p17Message; //getalbum
    private static final String rank = "cooperation";


    private int clickPosition;
    private int round = 0, rangeCount = 0;
    private int loadCount = 0;
    private int doingType;


    private static final int intCollectTypeCooperation = 102;

    private float scale = 0;

    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean isDoingMore = false;


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                MyLog.Set("e", FragmentCooperation2.class, "onLoad");
                if(isDoingMore){
                    MyLog.Set("e", FragmentCooperation2.class, "正在讀取更多項目");
                    return;
                }
                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

                MyLog.Set("e", FragmentMyUpLoad2.class, "sizeMax");
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_collect_all, container, false);
        rGuideCooperation = (RelativeLayout)v.findViewById(R.id.rGuideCooperation);
        rvCollect = (RecyclerView) v.findViewById(R.id.rvCollect);
        pbLoadMore = (SmoothProgressBar) v.findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();



        rvCollect.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemAnimator animator = rvCollect.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        rvCollect.addOnScrollListener(mOnScrollListener);

        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) v.findViewById(R.id.pinPinBoxRefreshLayout);

        //20171002
        SmoothProgressBar pbRefresh = (SmoothProgressBar) v.findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(v.findViewById(R.id.vRefreshAnim), pbRefresh);


        setRefreshListener();

        return v;
    }


    private void setRefreshListener() {

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        setRecycler();

        doGetAlbum();

    }

    private void init() {
        getdata = PPBApplication.getInstance().getData();
        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        loading = ((MyCollect2Activity)getActivity()).getLoading();

        p17arraylist = new ArrayList<>();

        round = 0;
        rangeCount = 16;
    }

    private void protocol17(String limit) {

        String strJson = "";
        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P17_GetCloudAlbumList, SetMapByProtocol.setParam17_getcloudalbumlist(id, token, rank, limit), null);

            MyLog.Set("d", getClass(), "p17strJson => " + rank + " => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p17Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p17Result = JsonUtility.GetString(jsonObject, Key.result);
                if (p17Result.equals("1")) {

                    String jdata = jsonObject.getString(Key.data);

                    JSONArray p17JsonArray = new JSONArray(jdata);

                    loadCount = p17JsonArray.length();

                    for (int i = 0; i < loadCount; i++) {

                        try {
                         /* 2016.04.14 添加template_id */
                            JSONObject obj = (JSONObject) p17JsonArray.get(i);
                            String album = JsonUtility.GetString(obj, Key.album);
                            String user = JsonUtility.GetString(obj, Key.user);
                            String template = JsonUtility.GetString(obj, Key.template);
                            String cooperation = JsonUtility.GetString(obj, Key.cooperation);
                            String cooperationstatistics = JsonUtility.GetString(obj, Key.cooperationstatistics);
                            String event = JsonUtility.GetString(obj, ProtocolKey.event);

                            JSONObject aj = new JSONObject(album);
                            String description = JsonUtility.GetString(aj, Key.description);//2016.07.04修正換行處裡
                            String p17_json_album_id = JsonUtility.GetString(aj, Key.album_id);
                            String p17_json_albumname = JsonUtility.GetString(aj, Key.name);
                            String p17_json_albumcover = JsonUtility.GetString(aj, Key.cover);


                            String p17_json_albumact =JsonUtility.GetString(aj, Key.act);
                            String p17_json_albuminsertdate = JsonUtility.GetString(aj, Key.insertdate);
                            String p17_json_albumzipped = JsonUtility.GetString(aj, Key.zipped);


                            JSONObject uj = new JSONObject(user);
                            String p17_json_user_id = JsonUtility.GetString(uj, Key.user_id);
                            String p17_json_username =JsonUtility.GetString(uj, Key.name);
                            String p17_json_picture = JsonUtility.GetString(uj, Key.picture);

                            JSONObject cj = new JSONObject(cooperation);
                            String p17_json_identity = JsonUtility.GetString(cj, Key.identity);

                            JSONObject csj = new JSONObject(cooperationstatistics);
                            String p17_json_cooperationstatistics = JsonUtility.GetString(csj, Key.count);

                            JSONObject temj = new JSONObject(template);
                            String p17_json_template_id = JsonUtility.GetString(temj, Key.template_id);





                            HashMap<String, Object> map = new HashMap<String, Object>();

                                /*0 => 一般狀態
                                                           1 => 下載中
                                                           2 =>  完成*/

                            map.put(Key.downloadType, 0);
                            File file = new File(DirClass.sdPath + PPBApplication.getInstance().getMyDir(), DirClass.dirAlbumList);
                            if(file!=null && file.exists()) {

                                String[] albumids = file.list();

                                if(albumids!=null){
                                    if (albumids.length > 0) {
                                        for (int k = 0; k < albumids.length; k++) {
                                            if (albumids[k].equals(p17_json_album_id)) {
                                                map.put(Key.downloadType, 2);
                                                break;
                                            }
                                        }
                                    }
                                }

                            }


                            map.put("album_id", p17_json_album_id);
                            map.put("albumname", p17_json_albumname);
                            map.put("albumdescription", description);
                            map.put("albumcover", p17_json_albumcover);
                            map.put(Key.act, p17_json_albumact);
                            map.put("albuminsertdate", p17_json_albuminsertdate);
                            map.put("user_id", p17_json_user_id);
                            map.put("username", p17_json_username);
                            map.put("picture", p17_json_picture);
                            map.put("zipped", p17_json_albumzipped);
                            map.put("count", p17_json_cooperationstatistics);
                            map.put("identity", p17_json_identity);
                            map.put("template_id", p17_json_template_id);

                            map.put("detail_is_open", false);

                            if(event==null || event.equals("") || event.equals("null")){
                                map.put("in_event", false);
                            }else {
                                map.put("in_event", true);
                            }


                            p17arraylist.add(map);

                        } catch (Exception e) {
                            loadCount = 0;
                            e.printStackTrace();
                        }
                    }//for

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

    private void setRecycler() {

        adapter = new RecyclerCollectAdapter(getActivity(), p17arraylist, intCollectTypeCooperation);
        rvCollect.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvCollect.setLayoutManager(manager);

        adapter.setOnRecyclerViewListener(new RecyclerCollectAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                clickPosition = position;

                if (((String) p17arraylist.get(position).get(Key.zipped)).equals("1")) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Key.album_id, (String) p17arraylist.get(position).get(Key.album_id));
                    Intent intent = new Intent(getActivity(), Reader2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    ActivityAnim.StartAnim(getActivity());
                }else {
                    PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_albumwork_undone);
                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        adapter.setOnDetailClickListener(this);

    }

    public void scrollToTop() {

        ExStaggeredGridLayoutManager linearLayoutManager = (ExStaggeredGridLayoutManager) rvCollect.getLayoutManager();

        int[] firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPositions(null);

        if (firstVisibleItemPosition[0] > 10) {
            rvCollect.scrollToPosition(10);
            MyLog.Set("d", getClass(), "先移動至第10項目");
        }

        rvCollect.smoothScrollToPosition(0);
    }


    public void cleanPicasso() {

        if (p17arraylist != null && p17arraylist.size() > 0) {
            for (int i = 0; i < p17arraylist.size(); i++) {
                Picasso.with(getActivity().getApplicationContext()).invalidate((String) p17arraylist.get(i).get("albumcover"));
                Picasso.with(getActivity().getApplicationContext()).invalidate((String) p17arraylist.get(i).get(Key.picture));
            }
            System.gc();
        }

    }

    private void systemShare() {

        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/jpg");
//        Uri u = Uri.parse((String) p17arraylist.get(clickPosition).get("albumcover"));
//        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        if((boolean)p17arraylist.get(clickPosition).get("in_event")){
            intent.putExtra(Intent.EXTRA_TEXT, (String) p17arraylist.get(clickPosition).get("albumname") + " , " + UrlClass.shareAlbumUrl + (String) p17arraylist.get(clickPosition).get("album_id"));
        }else {
            intent.putExtra(Intent.EXTRA_TEXT, (String) p17arraylist.get(clickPosition).get("albumname") + " , " + UrlClass.shareAlbumUrl + (String) p17arraylist.get(clickPosition).get("album_id") + "&autoplay=1");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(Intent.createChooser(intent, getActivity().getTitle()));
    }

    private void selectShareMode() {


        final PopupCustom p = new PopupCustom(getActivity());
        p.setPopup(R.layout.pop_2_0_0_select_share, R.style.pinpinbox_popupAnimation_bottom);

        TextView tvShareFB = (TextView) p.getPopupView().findViewById(R.id.tvShareFB);
        TextView tvShare = (TextView) p.getPopupView().findViewById(R.id.tvShare);

        TextUtility.setBold(tvShareFB, true);
        TextUtility.setBold(tvShare, true);
        TextUtility.setBold((TextView)p.getPopupView().findViewById(R.id.tvTitle), true);

        tvShareFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                p.getPopupWindow().dismiss();
                taskShare();
            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                p.getPopupWindow().dismiss();
                systemShare();
            }
        });


        p.show(((MyCollect2Activity)getActivity()).getBackground());


    }

    private void taskShare() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            shareDialog = new ShareDialog(getActivity());
            shareDialog.registerCallback(
                    ((MyCollect2Activity) getActivity()).getCallbackManager(),
                    new FacebookCallback<Sharer.Result>() {

                        @Override
                        public void onSuccess(Sharer.Result result) {

                            doShare();

                            MyLog.Set("d", getClass(), "shareDialog => onSuccess");

                        }

                        @Override
                        public void onCancel() {
                            MyLog.Set("d", getClass(), "shareDialog => onCancel");
                            shareDialog = null;
                        }

                        @Override
                        public void onError(FacebookException error) {
                            MyLog.Set("d", getClass(), "shareDialog => onError");
                            shareDialog = null;

                        }
                    });

            String strCover = (String) p17arraylist.get(clickPosition).get("albumcover");
            String strAlbum_id = (String) p17arraylist.get(clickPosition).get("album_id");

            String shareUrl = "";
            if((boolean)p17arraylist.get(clickPosition).get("in_event")){
               shareUrl = UrlClass.shareAlbumUrl + strAlbum_id;
            }else {
                shareUrl = UrlClass.shareAlbumUrl + strAlbum_id + "&autoplay=1";
            }



            if (strCover != null && !strCover.equals("")) {

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(shareUrl))
                        .setImageUrl(Uri.parse(strCover))
                        .build();
                shareDialog.show(content);

            } else {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(shareUrl))
                        .build();
                shareDialog.show(content);
            }

        } else {

        }

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doGetAlbum();
                        break;
                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;
                    case DoingTypeClass.DoRefresh:
                        doRefresh();
                        break;
                    case DoingTypeClass.DoDelete:
                        doDeleteAlbum();
                        break;
                    case DoingTypeClass.DoCheckShare:
                        doCheckShare();
                        break;
                    case DoingTypeClass.DoShareTask:
                        doShare();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);

    }

    private void doGetAlbum() {
        if (!HttpUtility.isConnect(getActivity())) {

            ((MyCollect2Activity) getActivity()).setNoConnect();
        }
        getAlbumTask = new GetAlbumTask();
        getAlbumTask.execute();
    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(getActivity())) {

            ((MyCollect2Activity) getActivity()).setNoConnect();
        }
        moreDataTask = new MoreDataTask();
        moreDataTask.execute();
    }

    public void doRefresh() {
        if (!HttpUtility.isConnect(getActivity())) {

            ((MyCollect2Activity) getActivity()).setNoConnect();
        }


        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
            refreshTask = null;
        }

        if (p17arraylist.size() > 0) {
            try {
                cleanPicasso();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        p17arraylist.clear();

        round = 0;

        if (adapter != null) {
            adapter.notifyDataSetChanged();

        }

        refreshTask = new RefreshTask();
        refreshTask.execute();

    }

    private void doDeleteAlbum() {
        if (!HttpUtility.isConnect(getActivity())) {

            ((MyCollect2Activity) getActivity()).setNoConnect();
        }
        deleteAlbumTask = new DeleteAlbumTask(clickPosition);
        deleteAlbumTask.execute();
    }

    private void doCheckShare() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MyCollect2Activity) getActivity()).setNoConnect();
            return;
        }
        checkShareTask = new CheckShareTask();
        checkShareTask.execute();
    }

    private void doShare() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MyCollect2Activity) getActivity()).setNoConnect();
            return;
        }
        shareTask = new ShareTask(clickPosition);
        shareTask.execute();
    }



    private class GetAlbumTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            round = 0;
            sizeMax = false;
        }

        @Override
        protected Void doInBackground(Void... params) {

            protocol17(round + "," + rangeCount);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);


            if (p17Result.equals("1")) {

                if(p17arraylist.size()>0) {

                    if(adapter!=null) {
                        adapter.notifyDataSetChanged();
                        if(loadCount<rangeCount){
                            MyLog.Set("d", this.getClass(), "項目少於" + rangeCount);
                            sizeMax = true;
                            return;
                        }
                        round = round + rangeCount;
                    }
                    rGuideCooperation.setVisibility(View.GONE);
                }else {
                    sizeMax = true;
                    rGuideCooperation.setVisibility(View.VISIBLE);
                }


            } else if (p17Result.equals("0")) {
                DialogV2Custom.BuildError(getActivity(), p17Message);
            } else if (p17Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
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
        protected Void doInBackground(Void... params) {

            protocol17(round + "," + rangeCount);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            isDoingMore = false;
            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();
            if (p17Result.equals("1")) {
                if (loadCount == 0) {
                    sizeMax = true; // 已達最大值
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }

                }else {


                    adapter.notifyItemRangeInserted(p17arraylist.size(), rangeCount);
                    if(loadCount<rangeCount){
                        MyLog.Set("d", this.getClass(), "項目少於" + rangeCount);
                        sizeMax = true;
                        return;
                    }
                    round = round + rangeCount;

                }


            } else if (p17Result.equals("0")) {
                DialogV2Custom.BuildError(getActivity(), p17Message);
            } else if (p17Result.equals(Key.timeout)) {
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
            doingType = DoingTypeClass.DoRefresh;

        }

        @Override
        protected Void doInBackground(Void... params) {

            protocol17(round + "," + rangeCount);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            sizeMax = false;
            isNoDataToastAppeared = false;

            pinPinBoxRefreshLayout.setRefreshing(false);


            if (p17Result.equals("1")) {


                if(p17arraylist.size()>0) {

                    if(adapter!=null) {
                        adapter.notifyDataSetChanged();
                        if(loadCount<rangeCount){
                            MyLog.Set("d", this.getClass(), "項目少於" + rangeCount);
                            sizeMax = true;
                            return;
                        }
                        round = round + rangeCount;
                    }
                    rGuideCooperation.setVisibility(View.GONE);
                }else {
                    sizeMax = true;
                    rGuideCooperation.setVisibility(View.VISIBLE);
                }

            } else if (p17Result.equals("0")) {
                DialogV2Custom.BuildError(getActivity(), p17Message);
            } else if (p17Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }
    }

    private class DeleteAlbumTask extends AsyncTask<Void, Void, Object> {
        private int mPosition;

        private String sdpath = Environment.getExternalStorageDirectory() + "/";
        private String myDir = "PinPinBox" + id + "/";
        private String dirAlbumList = "albumlist/";

        public DeleteAlbumTask(int position) {
            this.mPosition = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDelete;
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("type", "album");
            data.put("type_id", (String) p17arraylist.get(mPosition).get("album_id"));
            data.put("user_id", id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("type", "album");
            sendData.put("type_id", (String) p17arraylist.get(mPosition).get("album_id"));
            sendData.put("user_id", id);
            sendData.put("sign", sign);

            try {
                String strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P45_DeleteCooperation, sendData, null);
                MyLog.Set("d", getClass(), "p45strJson => " + strJson);

                JSONObject jsonObject = new JSONObject(strJson);
                p45Result = jsonObject.getString(Key.result);

                if (p45Result.equals("1")) {

                } else if (p45Result.equals("0")) {
                    p45Message = jsonObject.getString(Key.message);
                } else {
                    p45Result = "";
                }

            } catch (Exception e) {
                p45Result = "";
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p45Result.equals("1")) {
                deleteFile(mPosition);

                /** 2016.08.16 新增刪除作品兌換狀態*/
                SharedPreferences albumGiftChangeData = getActivity().getSharedPreferences("pinpinbox_" + (String) p17arraylist.get(mPosition).get("album_id"), Activity.MODE_PRIVATE);
                albumGiftChangeData.edit().clear().commit();
                MyLog.Set("d", getClass(), "pinpinbox_" + (String) p17arraylist.get(mPosition).get("album_id") + "already clear");


//                rvCollect.getItemAnimator().setSupportsChangeAnimations(true);
                RecyclerView.ItemAnimator animator = rvCollect.getItemAnimator();
                if (animator instanceof SimpleItemAnimator) {
                    ((SimpleItemAnimator) animator).setSupportsChangeAnimations(true);
                }

                adapter.remove(mPosition);

                RecyclerView.ItemAnimator animator2 = rvCollect.getItemAnimator();
                if (animator2 instanceof SimpleItemAnimator) {
                    ((SimpleItemAnimator) animator2).setSupportsChangeAnimations(false);
                }

//                rvCollect.getItemAnimator().setSupportsChangeAnimations(false);

                if(p17arraylist.size()<1) {
                    rGuideCooperation.setVisibility(View.VISIBLE);
                }


            } else if (p45Result.equals("0")) {

                DialogV2Custom.BuildError(getActivity(), p45Message);
            } else if (p45Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }
        }

        private void deleteFile(int position) {

            try {

                File file = new File(sdpath + myDir + dirAlbumList + (String) p17arraylist.get(position).get("album_id"));
                if (file.exists()) {

                    FileUtility.deleteFileFolder(new File(sdpath + myDir + dirAlbumList + (String) p17arraylist.get(position).get("album_id")));

                }

            }catch (Exception e){
                e.printStackTrace();
            }


        }

    }

    private class CheckShareTask extends AsyncTask<Void, Void, Object> {

        private String p84Result, p84Message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoCheckShare;
            loading.show();

        }


        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P84_CheckTaskCompleted, SetMapByProtocol.setParam84_checktaskcompleted(id, token, TaskKeyClass.share_to_fb, "google"), null);
                MyLog.Set("d", getClass(), "p84strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p84Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p84Result = jsonObject.getString(Key.result);
                    if (p84Result.equals("1")) {
                    } else if (p84Result.equals("2")) {
                        p84Message = jsonObject.getString(Key.message);
                    } else if (p84Result.equals("0")) {
                        p84Message = jsonObject.getString(Key.message);
                    } else {
                        p84Result = "";
                    }
                } catch (Exception e) {
                    p84Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();
            if (p84Result.equals("1")) {
                /*任務已完成*/
                getdata.edit().putBoolean(TaskKeyClass.share_to_fb, true).commit();
                systemShare();


            } else if (p84Result.equals("2")) {

                /*尚有次數未完成*/
                getdata.edit().putBoolean(TaskKeyClass.share_to_fb, false).commit();
                selectShareMode();

            } else if (p84Result.equals("0")) {

                systemShare();

            } else if (p84Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }
    }

    private class ShareTask extends AsyncTask<Void, Void, Object> {

        private String p83Result, p83Message;
        private String restriction;
        private String restriction_value;
        private String name;
        private String reward;
        private String reward_value;
        private String url;

        private int position;

        private int numberofcompleted;

        public ShareTask(int position) {
            this.position = position;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoShareTask;
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> map = new HashMap<>();
            map.put(MapKey.id, id);
            map.put(MapKey.token, token);
            map.put(MapKey.task_for, TaskKeyClass.share_to_fb);
            map.put(MapKey.platform, "google");
            String sign = IndexSheet.encodePPB(map);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(MapKey.id, id);
            sendData.put(MapKey.token, token);
            sendData.put(MapKey.task_for, TaskKeyClass.share_to_fb);
            sendData.put(MapKey.platform, "google");
            sendData.put(MapKey.type, "album");
            sendData.put(MapKey.type_id, (String) p17arraylist.get(position).get("album_id"));
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                MyLog.Set("d", getClass(), "p83strJson => " + strJson);
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

            if (p83Result.equals("1")) {

                final DialogHandselPoint d = new DialogHandselPoint(getActivity());

                if (restriction.equals("personal")) {
                    d.getTvTitle().setText(name + " " + numberofcompleted + " / " + restriction_value);
                } else {
                    d.getTvTitle().setText(name);
                }

                if (reward.equals("point")) {
                    d.getTvDescription().setText(getActivity().getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");
                    /**獲取當前使用者P點*/
                    String point = getdata.getString("point", "");
                    int p = StringIntMethod.StringToInt(point);

                    /**任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /**加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /**儲存data*/
                    getdata.edit().putString("point", newP).commit();

                    getdata.edit().putBoolean(TaskKeyClass.share_to_fb, false).commit();

                    getdata.edit().putBoolean("datachange", true).commit();

                } else {
//                    d.getTvDescription().setText();
                }


                d.getTvLink().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString(Key.url, url);
                        Intent intent = new Intent(getActivity(), WebView2Activity.class);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                        ActivityAnim.StartAnim(getActivity());
                    }
                });

            } else if (p83Result.equals("2")) {

                getdata.edit().putBoolean(TaskKeyClass.share_to_fb, true).commit();


            } else if (p83Result.equals("3")) {


            } else if (p83Result.equals("0")) {

                getdata.edit().putBoolean(TaskKeyClass.share_to_fb, true).commit();

            } else if (p83Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }

            shareDialog = null;


        }
    }

    private class ToAlbumTask extends AsyncTask<Void, Void, Object> {

        private String p68Result, p68Message;
        private String sdpath = Environment.getExternalStorageDirectory() + "/";
        private String myDir = "PinPinBox" + id + "/";
        private String dirAlbumList = "albumlist/";

        private int mPosition;

        private long lastmodifytime;//最後修改時間

        private long downloadtime;//下載時間

        public ToAlbumTask(int position) {
            this.mPosition = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String album_id = (String) p17arraylist.get(mPosition).get("album_id");

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P68_CheckAlbumZip, SetMapByProtocol.setParam68_checkalbumofdiy(id, token, album_id), null);
                MyLog.Set("d", getClass(), "p68strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p68Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p68Result = JsonUtility.GetString(jsonObject, Key.result);
                    if (p68Result.equals("1")) {
                        String data = JsonUtility.GetString(jsonObject, "data");
                        JSONObject object = new JSONObject(data);
                        lastmodifytime = object.getLong("modifytime");
                        downloadtime = getdata.getLong(album_id + "_modified_time", 0);

                        MyLog.Set("d", getClass(), "lastmodifytime => " + lastmodifytime);
                        MyLog.Set("d", getClass(), "downloadtime => " + downloadtime);
                    }else if(p68Result.equals("0")){
                        p68Message = JsonUtility.GetString(jsonObject, Key.message);
                    }else {
                        p68Result = "";
                    }
                } catch (Exception e) {
                    p68Result = "";
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();
            if (p68Result.equals("1")) {
                if (lastmodifytime != downloadtime) {
                    File file = new File(sdpath + myDir + dirAlbumList + (String) p17arraylist.get(mPosition).get("album_id"));
                    if (file.exists()) {
                        FileUtility.delAllFile(sdpath + myDir + dirAlbumList + (String) p17arraylist.get(mPosition).get("album_id"));
                        FileUtility.delFolder(sdpath + myDir + dirAlbumList + (String) p17arraylist.get(mPosition).get("album_id"));
                        MyLog.Set("d", FragmentOther2.class, "in client album delete");
                    }
                }

                /**儲存封面*/
                String album_id = (String) p17arraylist.get(mPosition).get("album_id");
                String cover = (String) p17arraylist.get(mPosition).get("albumcover");
                getdata.edit().putString("album_" + album_id + "_" + "cover", cover).commit();

                Intent intent = new Intent(getActivity(), ReadAlbumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("album_id", album_id);
                bundle.putBoolean("buy", false);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                ActivityAnim.StartAnim(getActivity());

            }else if(p68Result.equals("0")){
                DialogV2Custom.BuildError(getActivity(), p68Message);
            }else if(p68Result.equals(Key.timeout)){
                connectInstability();
            }else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }

    }

    public void changeDownloadType(int position, int doingType){

        HashMap<String, Object> mp = p17arraylist.get(position);

        if (mp.containsKey(Key.downloadType)) {
            mp.remove(Key.downloadType);

            mp.put(Key.downloadType, doingType);

            p17arraylist.remove(position);

            p17arraylist.add(position, mp);

        }

        adapter.notifyItemChanged(position);

    }

    @Override
    public void onPrivacyClick(int position) {

        MyLog.Set("d", getClass(), "onPrivacyClick => " + position);

    }

    @Override
    public void onEditClick(int position) {
        MyLog.Set("d", getClass(), "onEditClick => " + position);

        clickPosition = position;

        String identity = (String)p17arraylist.get(position).get(Key.identity);

        if(identity.equals(Key.viewer)){
            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_deficiency_identity);
        }else {

            Bundle bundle = new Bundle();
            bundle.putString(Key.album_id, (String) p17arraylist.get(position).get(Key.album_id));
            bundle.putString(Key.identity, identity);

            String strTemplate_id = (String) p17arraylist.get(position).get(Key.template_id);
            if (strTemplate_id.equals("0")) {
                bundle.putInt(Key.create_mode, 0);
            } else {
                bundle.putInt(Key.create_mode, 1);
            }

            Intent intent = new Intent(getActivity(), Creation2Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            ActivityAnim.StartAnim(getActivity());

        }





    }

    @Override
    public void onCooperationClick(int position) {
        MyLog.Set("d", getClass(), "onCooperationClick => " + position);

        String identity = (String) p17arraylist.get(position).get("identity");
        String album_id = (String) p17arraylist.get(position).get("album_id");

        if(identity.equals("admin") || identity.equals("approver")){

            Bundle bundle = new Bundle();
            bundle.putString(Key.album_id, album_id);
            bundle.putString(Key.identity, identity);
            Intent intent = new Intent(getActivity(), AlbumGroup2Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            ActivityAnim.StartAnim(getActivity());



        }else {
            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_deficiency_identity);
        }


    }

    @Override
    public void onShareClick(int position) {
        MyLog.Set("d", getClass(), "onShareClick => " + position);
        clickPosition = position;

        String strZipped = (String)p17arraylist.get(position).get(Key.zipped);
        if(!strZipped.equals("1")){
            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_albumwork_undone);
            return;
        }

        final String strAct = (String) p17arraylist.get(position).get(Key.act); // 隱私權 (close: 關閉 / open: 開啟)

        if (strAct != null && strAct.equals("open")) {
            boolean bShareToFB = getdata.getBoolean(TaskKeyClass.share_to_fb, false);
            if (bShareToFB) {
                systemShare();
            } else {
                doCheckShare();
            }
        } else {
            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_open_act_to_share);
        }

    }

    @Override
    public void onDeleteClick(final int position) {
        MyLog.Set("d", getClass(), "onDeleteClick => " + position);

        clickPosition = position;



        final DialogV2Custom d = new DialogV2Custom(getActivity());
        d.setStyle(DialogStyleClass.CHECK);
        d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_release_cooperation);
        d.setCheckExecute(new CheckExecute() {
            @Override
            public void DoCheck() {
                doDeleteAlbum();
            }
        });
        d.show();

    }

    @Override
    public void onDownloadClick(final int position) {
        MyLog.Set("d", getClass(), "onDownloadClick => " + position);

        FlurryUtil.onEvent(FlurryKey.work_manager_download);


        String zipped = (String)p17arraylist.get(position).get(Key.zipped);

        if(zipped.equals("0")){

            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_album_save_not_yet);

            return;
        }





        int downloadType = (int) p17arraylist.get(position).get(Key.downloadType);

        if (downloadType == 1) {
            MyLog.Set("d", getClass(), "點擊的作品正在下載");
            return;
        }

        changeDownloadType(position, 1);


        List<Service> serviceList = SystemUtility.SysApplication.getInstance().getsList();
        DownLoadService downLoadService = null;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).getClass().getSimpleName().equals(DownLoadService.class.getSimpleName())) {
                downLoadService = (DownLoadService) serviceList.get(i);
                break;
            }
        }


        String album_id = (String) p17arraylist.get(position).get("album_id");
        String className = this.getClass().getSimpleName();


        if (downLoadService == null) {
            downLoadService = new DownLoadService();
            Bundle bundle = new Bundle();
            bundle.putString(Key.album_id, album_id);
            bundle.putString(Key.className, className);
            bundle.putInt(Key.position, position);
            Intent intentService = new Intent(getActivity(), downLoadService.getClass());
            intentService.putExtras(bundle);
            getActivity().startService(intentService);
            MyLog.Set("d", getActivity().getClass(), "startService");

        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put(Key.album_id, album_id);
            map.put(Key.className, className);
            map.put(Key.position, position);

            MyLog.Set("d", getActivity().getClass(), "DownLoadService 已存在");
            downLoadService.addAlbum(map);
        }


    }

    @Override
    public void onPause() {
        cleanPicasso();
        super.onPause();
    }

    @Override
    public void onDestroy() {

        if (getAlbumTask != null && !getAlbumTask.isCancelled()) {
            getAlbumTask.cancel(true);
        }
        getAlbumTask = null;

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
        }
        moreDataTask = null;

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
        }
        refreshTask = null;

        if (deleteAlbumTask != null && !deleteAlbumTask.isCancelled()) {
            deleteAlbumTask.cancel(true);
        }
        deleteAlbumTask = null;

        if (checkShareTask != null && !checkShareTask.isCancelled()) {
            checkShareTask.cancel(true);
        }
        checkShareTask = null;

        if (shareTask != null && !shareTask.isCancelled()) {
            shareTask.cancel(true);
        }
        shareTask = null;

        cleanPicasso();

        super.onDestroy();
    }


}

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.UrlClass;
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
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumSettings2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Creation2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Main2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollect2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Reader2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCollectAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.HashMapKeyControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
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
public class FragmentMyUpLoad2 extends Fragment implements OnDetailClickListener {

    private SharedPreferences getdata;
    private ShareDialog shareDialog;
    private LoadingAnimation loading;

    private GetAlbumTask getAlbumTask;
    private MoreDataTask moreDataTask;
    private RefreshTask refreshTask;
    private DeleteAlbumTask deleteAlbumTask;
    private CheckShareTask checkShareTask;
    private ShareTask shareTask;
    //    private ToAlbumTask toAlbumTask;
    private GetOriginalSettingsTask getOriginalSettingsTask;
    private SendSettingsTask sendSettingsTask;
    private FastCreateTask fastCreateTask;

    public ArrayList<HashMap<String, Object>> p17arraylist;

    private RecyclerCollectAdapter adapter;

    private RecyclerView rvCollect;
    private SmoothProgressBar pbLoadMore;
    private LinearLayout linGuideMy;
    private TextView tvCreate;
    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;


    private String id, token;
    private String p16Result, p16Message; //delete
    private String p17Result, p17Message; //getalbum
    private String strOriginalAct = ""; //當下點擊作品的權限
    private static final String rank = "mine";


    private int clickPosition;
    private int round = 0, rangeCount = 0;
    private int loadCount = 0;
    private int doingType;

    private static final int DoGetOriginalSettings = 1009;
    private static final int intCollectTypeMy = 100;


    private float scale = 0;

    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean FragmentMeRefresh = false;
    private boolean isDoingMore = false;


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                MyLog.Set("e", FragmentMyUpLoad2.class, "onLoad");
                if (isDoingMore) {
                    MyLog.Set("e", FragmentMyUpLoad2.class, "正在讀取更多項目");
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
        linGuideMy = (LinearLayout) v.findViewById(R.id.linGuideMy);
        tvCreate = (TextView) v.findViewById(R.id.tvCreate);
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

        loading = ((MyCollect2Activity) getActivity()).getLoading();

        p17arraylist = new ArrayList<>();

        round = 0;
        rangeCount = 16;

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                doFastCreate();


            }
        });


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
                p17Result = jsonObject.getString(Key.result);
                if (p17Result.equals("1")) {

                    String jdata = jsonObject.getString(Key.data);

                    JSONArray p17JsonArray = new JSONArray(jdata);

                    loadCount = p17JsonArray.length();

                    for (int i = 0; i < loadCount; i++) {

                        try {
                         /* 2016.04.14 添加template_id */
                            final HashMap<String, Object> map = new HashMap<String, Object>();
                            JSONObject obj = (JSONObject) p17JsonArray.get(i);
                            String album = JsonUtility.GetString(obj, ProtocolKey.album);
                            String user = JsonUtility.GetString(obj, ProtocolKey.user);
                            String template = JsonUtility.GetString(obj, ProtocolKey.template);
                            String cooperation = JsonUtility.GetString(obj, ProtocolKey.cooperation);
                            String cooperationstatistics = JsonUtility.GetString(obj, ProtocolKey.cooperationstatistics);
                            String event = JsonUtility.GetString(obj, ProtocolKey.event);

                            JSONObject aj = new JSONObject(album);
                            String description = JsonUtility.GetString(aj, ProtocolKey.description);//2016.07.04修正換行處裡
                            String p17_json_album_id = JsonUtility.GetString(aj, ProtocolKey.album_id);
                            String p17_json_albumname = JsonUtility.GetString(aj, ProtocolKey.album_name);
                            String p17_json_albumcover = JsonUtility.GetString(aj, ProtocolKey.cover);


                            String p17_json_albumact = JsonUtility.GetString(aj, ProtocolKey.act);
                            String p17_json_albuminsertdate = JsonUtility.GetString(aj, ProtocolKey.insertdate);
                            String p17_json_albumzipped = JsonUtility.GetString(aj, ProtocolKey.zipped);


                            boolean isAudio = false;
                            try {
                                String usefor = JsonUtility.GetString(aj, ProtocolKey.usefor);
                                JSONObject jsonUsefor = new JSONObject(usefor);
                                isAudio = JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            JSONObject uj = new JSONObject(user);
                            String p17_json_user_id = JsonUtility.GetString(uj, ProtocolKey.user_id);
                            String p17_json_username = JsonUtility.GetString(uj, ProtocolKey.user_name);
                            String p17_json_picture = JsonUtility.GetString(uj, ProtocolKey.picture);

                            JSONObject cj = new JSONObject(cooperation);
                            String p17_json_identity = JsonUtility.GetString(cj, ProtocolKey.identity);

                            JSONObject csj = new JSONObject(cooperationstatistics);
                            String p17_json_cooperationstatistics = JsonUtility.GetString(csj, ProtocolKey.count);

                            JSONObject temj = new JSONObject(template);
                            String p17_json_template_id = JsonUtility.GetString(temj, ProtocolKey.template_id);


                                /*0 => 一般狀態
                                                           1 => 下載中
                                                           2 =>  完成*/
                            map.put(Key.downloadType, 0);
                            File file = new File(DirClass.sdPath + PPBApplication.getInstance().getMyDir(), DirClass.dirAlbumList);


                            if (file != null && file.exists()) {

                                String[] albumids = file.list();

                                if (albumids != null) {
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
                            map.put("identity", "admin"); //我上傳的相本 暫時強制admin
                            map.put("template_id", p17_json_template_id);
                            map.put("isAudio", isAudio);

                            map.put("detail_is_open", false);

                            if (event == null || event.equals("") || event.equals("null")) {
                                map.put("in_event", false);
                            } else {
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

        adapter = new RecyclerCollectAdapter(getActivity(), p17arraylist, intCollectTypeMy);
        rvCollect.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvCollect.setLayoutManager(manager);

        adapter.setOnRecyclerViewListener(new RecyclerCollectAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                //adapter已設定禁止連點

                clickPosition = position;

                if (((String) p17arraylist.get(position).get("zipped")).equals("1")) {

                    Bundle bundle = new Bundle();
                    bundle.putString(Key.album_id, (String) p17arraylist.get(position).get("album_id"));
                    Intent intent = new Intent(getActivity(), Reader2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    ActivityAnim.StartAnim(getActivity());

                } else {
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
                Picasso.with(getActivity().getApplicationContext()).invalidate((String) p17arraylist.get(i).get("picture"));
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

        if ((boolean) p17arraylist.get(clickPosition).get("in_event")) {
            intent.putExtra(Intent.EXTRA_TEXT, (String) p17arraylist.get(clickPosition).get("albumname") + " , " + UrlClass.shareAlbumUrl + (String) p17arraylist.get(clickPosition).get("album_id"));
        } else {
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
        TextUtility.setBold((TextView) p.getPopupView().findViewById(R.id.tvTitle), true);


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


        p.show(((MyCollect2Activity) getActivity()).getBackground());

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
            if ((boolean) p17arraylist.get(clickPosition).get("in_event")) {
                shareUrl = UrlClass.shareAlbumUrl + strAlbum_id;
            } else {
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

    public void updateClickPositionItem(HashMap<String, Object> map) {

        p17arraylist.set(clickPosition, map);

        adapter.notifyItemChanged(clickPosition);


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
                    case DoGetOriginalSettings:
                        doGetOriginalSettings();
                        break;
                    case DoingTypeClass.DoSend:
                        doSendSettings();
                        break;

                    case DoingTypeClass.DoFastCreate:
                        doFastCreate();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);
    }

    private void doGetAlbum() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MyCollect2Activity) getActivity()).setNoConnect();
            return;
        }
        getAlbumTask = new GetAlbumTask();
        getAlbumTask.execute();
    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MyCollect2Activity) getActivity()).setNoConnect();
            return;
        }
        moreDataTask = new MoreDataTask();
        moreDataTask.execute();
    }

    public void doRefresh() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MyCollect2Activity) getActivity()).setNoConnect();
            return;
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
            return;
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

    private void doGetOriginalSettings() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MyCollect2Activity) getActivity()).setNoConnect();
            return;
        }

        getOriginalSettingsTask = new GetOriginalSettingsTask((String) p17arraylist.get(clickPosition).get(Key.album_id));
        getOriginalSettingsTask.execute();

    }

    private void doSendSettings() {

        if (!HttpUtility.isConnect(getActivity())) {
            ((MyCollect2Activity) getActivity()).setNoConnect();
            return;
        }

        JSONObject obj = new JSONObject();

        try {

            strOriginalAct = (String) p17arraylist.get(clickPosition).get(Key.act);


            if (strOriginalAct.equals("open")) {
                obj.put("act", "close");
            } else {
                obj.put("act", "open");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        sendSettingsTask = new SendSettingsTask((String) p17arraylist.get(clickPosition).get(Key.album_id), obj.toString());
        sendSettingsTask.execute();

    }

    private void doFastCreate() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MyCollect2Activity) getActivity()).setNoConnect();
            return;
        }
        fastCreateTask = new FastCreateTask();
        fastCreateTask.execute();
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

                if (p17arraylist.size() > 0) {

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        if (loadCount < rangeCount) {
                            MyLog.Set("d", this.getClass(), "項目少於" + rangeCount);
                            sizeMax = true;
                            return;
                        }
                        round = round + rangeCount;
                    }

                    linGuideMy.setVisibility(View.GONE);
                } else {
                    sizeMax = true;
                    linGuideMy.setVisibility(View.VISIBLE);
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
                } else {


                    adapter.notifyItemRangeInserted(p17arraylist.size(), rangeCount);
                    if (loadCount < rangeCount) {
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


                if (p17arraylist.size() > 0) {

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        if (loadCount < rangeCount) {
                            MyLog.Set("d", this.getClass(), "項目少於" + rangeCount);
                            sizeMax = true;
                            return;
                        }
                        round = round + rangeCount;
                    }
                    linGuideMy.setVisibility(View.GONE);
                } else {
                    sizeMax = true;
                    linGuideMy.setVisibility(View.VISIBLE);
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
            data.put("albumid", (String) p17arraylist.get(mPosition).get("album_id"));
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("albumid", (String) p17arraylist.get(mPosition).get("album_id"));
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P16_DelAlbum, sendData, null);
                MyLog.Set("d", getClass(), "p16strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p16Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p16Result = jsonObject.getString(Key.result);

                    if (p16Result.equals("0")) {
                        p16Message = jsonObject.getString(Key.message);
                    } else if (p16Result.equals("1")) {

                    } else {
                        p16Result = "";
                    }
                } catch (Exception e) {
                    p16Result = "";
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p16Result.equals("1")) {
                deleteFile(mPosition);

                FragmentMeRefresh = true;


                /** 2016.08.16 新增刪除作品兌換狀態*/
                SharedPreferences albumGiftChangeData = getActivity().getSharedPreferences("pinpinbox_" + (String) p17arraylist.get(mPosition).get("album_id"), Activity.MODE_PRIVATE);
                albumGiftChangeData.edit().clear().commit();
                MyLog.Set("d", getClass(), "pinpinbox_" + (String) p17arraylist.get(mPosition).get("album_id") + "already clear");


                RecyclerView.ItemAnimator animator = rvCollect.getItemAnimator();
                if (animator instanceof SimpleItemAnimator) {
                    ((SimpleItemAnimator) animator).setSupportsChangeAnimations(true);
                }

//                rvCollect.getItemAnimator().setSupportsChangeAnimations(true);
                adapter.remove(mPosition);
//                rvCollect.getItemAnimator().setSupportsChangeAnimations(false);

                RecyclerView.ItemAnimator animator2 = rvCollect.getItemAnimator();
                if (animator2 instanceof SimpleItemAnimator) {
                    ((SimpleItemAnimator) animator2).setSupportsChangeAnimations(false);
                }

                if (p17arraylist.size() < 1) {
                    linGuideMy.setVisibility(View.VISIBLE);
                }


            } else if (p16Result.equals("0")) {
                DialogV2Custom.BuildError(getActivity(), p16Message);
            } else if (p16Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }
        }

        private void deleteFile(int position) {

            File file = new File(sdpath + myDir + dirAlbumList + (String) p17arraylist.get(position).get("album_id"));
            if (file.exists()) {

                FileUtility.deleteFileFolder(new File(sdpath + myDir + dirAlbumList + (String) p17arraylist.get(position).get("album_id")));

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



                HashMap<String, String>map = new HashMap<>();
                map.put(Key.type, Value.album);
                map.put(Key.type_id, (String)p17arraylist.get(clickPosition).get(Key.album_id));

                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P84_CheckTaskCompleted, SetMapByProtocol.setParam84_checktaskcompleted(id, token, TaskKeyClass.share_to_fb, "google", map), null);

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
                systemShare();


            } else if (p84Result.equals("2")) {

                /*尚有次數未完成*/
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

                    getdata.edit().putBoolean("datachange", true).commit();

                } else {
//                    d.getTvDescription().setText();
                }


                d.getTvLink().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        Intent intent = new Intent(getActivity(), WebView2Activity.class);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                        ActivityAnim.StartAnim(getActivity());
                    }
                });

            } else if (p83Result.equals("2")) {


            } else if (p83Result.equals("3")) {


            } else if (p83Result.equals("0")) {


            } else if (p83Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }

            shareDialog = null;


        }
    }

    private class GetOriginalSettingsTask extends AsyncTask<Void, Void, Object> {

        private String albumid;

        private String p34Result, p34Message;

        private String strAlbumName, strAlbumDescription, strFirstPaging, strSecondPaging;


        private int p57Result = -1;

        private String p57Message = "";

        private int photoCount = 0;


        public GetOriginalSettingsTask(String albumid) {
            this.albumid = albumid;
        }


        private void getPhotoList() {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P57_GetAlbumOfDiy, SetMapByProtocol.setParam57_getalbumofdiy(id, token, albumid), null);
                MyLog.Set("d", getActivity().getClass(), "p57strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p57Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p57Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p57Result == 1) {

                        String data = jsonObject.getString(ProtocolKey.data);

                        JSONObject jsonData = new JSONObject(data);

                        String photo = JsonUtility.GetString(jsonData, ProtocolKey.photo);

                        JSONArray jsonArray = new JSONArray(photo);

                        photoCount = jsonArray.length();


                    } else if (p57Result == 0) {//if(p57Result.equals("1"))

                        p57Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

                    } else {

                        p57Result = -1;

                    }
                } catch (Exception e) {
                    p57Result = -1;
                    e.printStackTrace();
                }
            }

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetOriginalSettings;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P34_GetAlbumSettings, SetMapByProtocol.setParam34_getalbumsettings(id, token, albumid), null);

                MyLog.Set("d", getClass(), "p34strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p34Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p34Result = jsonObject.getString(Key.result);

                    if (p34Result.equals("1")) {

                        String jsonData = jsonObject.getString(Key.data);
                        JSONObject object = new JSONObject(jsonData);
                        strAlbumName = JsonUtility.GetString(object, "title");
                        strAlbumDescription = JsonUtility.GetString(object, "description");
                        strFirstPaging = JsonUtility.GetString(object, "firstpaging");
                        strSecondPaging = JsonUtility.GetString(object, "secondpaging");


                    } else if (p34Result.equals("0")) {
                        p34Message = jsonObject.getString(Key.message);
                    } else {
                        p34Result = "";
                    }
                } catch (Exception e) {
                    p34Result = "";
                    e.printStackTrace();
                }

            }


            getPhotoList();


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p34Result.equals("1")) {

                if (strAlbumName.equals("") || strFirstPaging.equals("") || strSecondPaging.equals("")) {
                    PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_album_info_is_incomplete_can_not_make_public);
                    return;
                } else {


                    if (p57Result == 1) {

                        if (photoCount > 0) {

                            doSendSettings();

                        } else {

                            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_album_content_is_null);

                        }

                    } else if (p57Result == 0) {

                        DialogV2Custom.BuildError(getActivity(), p57Message);

                    } else if (p57Result == Key.TIMEOUT) {

                        //連同 34 一起重新讀取
                        connectInstability();

                    } else {
                        DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                    }

                }


            } else if (p34Result.equals("0")) {
                DialogV2Custom.BuildError(getActivity(), p34Message);
            } else if (p34Result.equals(Key.timeout)) {

                connectInstability();

            } else {

                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());

            }


        }
    }

    private class SendSettingsTask extends AsyncTask<Void, Void, Object> {

        private String albumid, settings;
        private String p33Result, p33Message;

        public SendSettingsTask(String albumid, String settings) {
            this.albumid = albumid;
            this.settings = settings;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSend;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String p33strJson = "";

            try {
                p33strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P33_AlbumSettings, SetMapByProtocol.setParam33_albumsettings(id, token, albumid, settings), null);
                MyLog.Set("d", getClass(), "p33strJson => " + p33strJson);
            } catch (SocketTimeoutException timeout) {
                p33Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (p33strJson != null && !p33strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(p33strJson);
                    p33Result = jsonObject.getString(Key.result);
                    if (p33Result.equals("1")) {

                    } else if (p33Result.equals("0")) {
                        p33Message = jsonObject.getString(Key.message);
                    } else {
                        p33Result = "";
                    }

                } catch (Exception e) {
                    p33Result = "";
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p33Result.equals("1")) {
                MyLog.Set("d", getClass(), "更新資訊 success");


                HashMap<String, Object> map = p17arraylist.get(clickPosition);

                if (strOriginalAct != null && strOriginalAct.equals("open")) {
                    HashMapKeyControl.changeMapKey(map, Key.act, "close");
                } else if (strOriginalAct != null && strOriginalAct.equals("close")) {
                    HashMapKeyControl.changeMapKey(map, Key.act, "open");
                }


//                HashMap<String, Object> map = new HashMap<>();
//                map.put("album_id", (String) p17arraylist.get(clickPosition).get("album_id"));
//                map.put("albumname", (String) p17arraylist.get(clickPosition).get("albumname"));
//                map.put("albumdescription", (String) p17arraylist.get(clickPosition).get("albumdescription"));
//                map.put("albumcover", (String) p17arraylist.get(clickPosition).get("albumcover"));
//                map.put("albuminsertdate", (String) p17arraylist.get(clickPosition).get("albuminsertdate"));
//                map.put("user_id", (String) p17arraylist.get(clickPosition).get("user_id"));
//                map.put("username", (String) p17arraylist.get(clickPosition).get("username"));
//                map.put("picture", (String) p17arraylist.get(clickPosition).get("picture"));
//                map.put("zipped", (String) p17arraylist.get(clickPosition).get("zipped"));
//                map.put("count", (String) p17arraylist.get(clickPosition).get("count"));
//                map.put("identity", "admin"); //我上傳的相本 暫時強制admin
//                map.put("template_id", (String) p17arraylist.get(clickPosition).get("template_id"));
//                map.put("detail_is_open", (boolean) p17arraylist.get(clickPosition).get("detail_is_open"));
//                if (strOriginalAct != null && strOriginalAct.equals("open")) {
//                    map.put(Key.act, "close");
//                } else if (strOriginalAct != null && strOriginalAct.equals("close")) {
//                    map.put(Key.act, "open");
//                }


                p17arraylist.set(clickPosition, map);
                adapter.notifyItemChanged(clickPosition);

                //20171010
                FragmentMeRefresh = true;


            } else if (p33Result.equals("0")) {

                DialogV2Custom.BuildError(getActivity(), p33Message);

            } else if (p33Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());

            }
        }
    }

    private class FastCreateTask extends AsyncTask<Void, Void, Object> {

        private int p54Result = -1;
        private String p54Message = "";

        private String newAlbum_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFastCreate;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {

                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P54_InsertAlbumOfDiy,
                        SetMapByProtocol.setParam54_insertalbumofdiy(id, token, "0"), null);
                MyLog.Set("d", getClass(), "p54strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p54Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p54Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p54Result == 1) {
                        newAlbum_id = JsonUtility.GetString(jsonObject, Key.data);
                    } else if (p54Result == 0) {
                        p54Message = JsonUtility.GetString(jsonObject, Key.message);
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
            if (p54Result == 1) {

                if (newAlbum_id != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Key.album_id, newAlbum_id);
                    bundle.putString(Key.identity, "admin");
                    bundle.putInt(Key.create_mode, 0);
                    bundle.putBoolean(Key.isNewCreate, true);
                    Intent intent = new Intent(getActivity(), Creation2Activity.class);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                    getActivity().finish();
                    ActivityAnim.StartAnim(getActivity());
                }

            } else if (p54Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p54Message);
            } else if (p54Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), this.getClass().getSimpleName());
            }

        }
    }

    public HashMap<String, Object> getClickMap() {
        return this.p17arraylist.get(clickPosition);
    }

    public void changeDownloadType(int position, int doingType) {

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

        clickPosition = position;

        doGetOriginalSettings();

    }

    @Override
    public void onEditClick(final int position) {
        MyLog.Set("d", getClass(), "onEditClick => " + position);

        clickPosition = position;

        final PopupCustom p = new PopupCustom(getActivity());
        p.setPopup(R.layout.pop_2_0_0_select_edit_album, R.style.pinpinbox_popupAnimation_bottom);
        p.show(((MyCollect2Activity) getActivity()).getBackground());

        TextView tvEditAlbum = (TextView) p.getPopupView().findViewById(R.id.tvEditAlbum);
        TextView tvEditAlbumInfo = (TextView) p.getPopupView().findViewById(R.id.tvEditAlbumInfo);

        TextUtility.setBold(tvEditAlbum, true);
        TextUtility.setBold(tvEditAlbumInfo, true);

        TextUtility.setBold((TextView) p.getPopupView().findViewById(R.id.tvTitle), true);


        tvEditAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                p.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, (String) p17arraylist.get(position).get(Key.album_id));
                bundle.putString(Key.identity, (String) p17arraylist.get(position).get(Key.identity));
                String strTemplate_id = (String) p17arraylist.get(position).get(Key.template_id);
                if (strTemplate_id.equals("0")) {
                    bundle.putInt(Key.create_mode, 0);
                } else {
                    bundle.putInt(Key.create_mode, 1);
                }

                Intent intent = new Intent(getActivity(), Creation2Activity.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                ActivityAnim.StartAnim(getActivity());
            }
        });

        tvEditAlbumInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                p.dismiss();

                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, (String) p17arraylist.get(position).get(Key.album_id));
                bundle.putString(Key.identity, (String) p17arraylist.get(position).get(Key.identity));

//                String cover = (String) p17arraylist.get(position).get("albumcover");
//
//                MyLog.Set("d", FragmentMyUpLoad2.class, "cover => " + cover);
//
//                if (cover != null && !cover.equals("") && !cover.equals("null")) {
//
//                    MyLog.Set("d", FragmentMyUpLoad2.class, "cover is exist");
//
//                    bundle.putBoolean(Key.cover, true);
//                } else {
//
//                    MyLog.Set("d", FragmentMyUpLoad2.class, "cover is no exist");
//
//                    bundle.putBoolean(Key.cover, false);
//                }


                if (p17arraylist.get(position).get("isAudio") == null) {
                    bundle.putBoolean(Key.audio, false);
                } else {
                    bundle.putBoolean(Key.audio, (boolean) p17arraylist.get(position).get("isAudio"));
                }


                Intent intent = new Intent(getActivity(), AlbumSettings2Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityAnim.StartAnim(getActivity());

            }
        });

    }

    @Override
    public void onCooperationClick(int position) {
        MyLog.Set("d", getClass(), "onCooperationClick => " + position);

        clickPosition = position;

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, (String) p17arraylist.get(position).get(Key.album_id));
        bundle.putString(Key.identity, (String) p17arraylist.get(position).get(Key.identity));
        Intent intent = new Intent(getActivity(), AlbumGroup2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(getActivity());


    }

    @Override
    public void onShareClick(int position) {
        MyLog.Set("d", getClass(), "onShareClick => " + position);
        clickPosition = position;

        String strZipped = (String) p17arraylist.get(position).get(Key.zipped);
        if (!strZipped.equals("1")) {
            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_albumwork_undone);
            return;
        }


        final String strAct = (String) p17arraylist.get(position).get(Key.act); // 隱私權 (close: 關閉 / open: 開啟)
        if (strAct != null && strAct.equals("open")) {
            doCheckShare();
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
        d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_delete);


        int type = (int) p17arraylist.get(position).get(Key.downloadType);


        if (type == 0) {
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_delete_album);
        } else if (type == 1) {
            PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_downloading);
            return;
        } else if (type == 2) {
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_delete_album_with_cache);
        }


        d.setCheckExecute(new CheckExecute() {
            @Override
            public void DoCheck() {
                d.dismiss();
                doDeleteAlbum();
            }
        });
        d.show();


    }

    @Override
    public void onDownloadClick(final int position) {
        MyLog.Set("d", getClass(), "onDownloadClick => " + position);

        FlurryUtil.onEvent(FlurryKey.work_manager_download);

        String zipped = (String) p17arraylist.get(position).get(Key.zipped);

        if (zipped.equals("0")) {

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

        if (FragmentMeRefresh) {
            List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
            Activity acMain = null;
            for (Activity ac : activityList) {
                if (ac.getClass().getSimpleName().equals(Main2Activity.class.getSimpleName())) {
                    acMain = ac;
                    break;
                }
            }
            if (acMain != null) {

                FragmentMe2 fragmentMe2 = (FragmentMe2) ((Main2Activity) acMain).getFragment(FragmentMe2.class.getSimpleName());

                if (fragmentMe2 != null && fragmentMe2.getAlbumList() != null) {
                    fragmentMe2.doRefresh(false);
                }

            }
        }

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

//        if (toAlbumTask != null && !toAlbumTask.isCancelled()) {
//            toAlbumTask.cancel(true);
//        }
//        toAlbumTask = null;

        if (getOriginalSettingsTask != null && !getOriginalSettingsTask.isCancelled()) {
            getOriginalSettingsTask.cancel(true);
        }
        getOriginalSettingsTask = null;

        if (sendSettingsTask != null && !sendSettingsTask.isCancelled()) {
            sendSettingsTask.cancel(true);
        }
        sendSettingsTask = null;

        if (fastCreateTask != null && !fastCreateTask.isCancelled()) {
            fastCreateTask.cancel(true);
        }
        fastCreateTask = null;

        cleanPicasso();

        super.onDestroy();
    }


}

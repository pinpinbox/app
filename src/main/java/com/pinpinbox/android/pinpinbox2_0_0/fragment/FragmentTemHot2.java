package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.HtmlUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CreateAlbumActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerTemListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import tyrantgit.explosionfield.ExplosionField;

/**
 * Created by vmage on 2017/1/18.
 */
public class FragmentTemHot2 extends Fragment {

    private ExplosionField mExplosionField;
    private NoConnect noConnect;

    private TemplateListTask templateListTask;
    private MoreDataTask moreDataTask;
    private RefreshTask refreshTask;

    private RecyclerTemListAdapter adapter;

    private ArrayList<HashMap<String, Object>> p36arraylist;

    private JSONArray p36JsonArray;

    private RecyclerView rvTemList;
    private SmoothProgressBar pbLoadMore;
    private PtrClassicFrameLayout swipeRefreshLayout;
    private View vHeader;
    private ImageView refreshImg;
    private TextView tvGuide;


    private static final String Rank = "hot";
    private String id, token;
    private String p36Message = "";


    private int p36Result = -1;
    private int round, count;
    private int doingType;

    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2_0_0_temlist, container, false);
        tvGuide = (TextView)v.findViewById(R.id.tvGuide);
        tvGuide.setText(R.string.pinpinbox_2_0_0_no_template);
        rvTemList = (RecyclerView) v.findViewById(R.id.rvTemList);
        pbLoadMore = (SmoothProgressBar) v.findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();

        rvTemList.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemAnimator animator = rvTemList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
//        rvTemList.getItemAnimator().setSupportsChangeAnimations(false);
        rvTemList.addOnScrollListener(mOnScrollListener);
        rvTemList.setHasFixedSize(true);

        swipeRefreshLayout = (PtrClassicFrameLayout) v.findViewById(R.id.swipeRefreshLayout);

        setRefreshListener();

        vHeader = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.header_2_0_0_124dp, null);
        refreshImg = (ImageView) vHeader.findViewById(R.id.refreshImg);
        return v;
    }


    private void setRefreshListener() {

        swipeRefreshLayout.setResistance(1.7f); //默认: 1.7f，越大，感觉下拉时越吃力。
        swipeRefreshLayout.setRatioOfHeaderHeightToRefresh(1.2f); //触发刷新时移动的位置比例  默认，1.2f，移动达到头部高度1.2倍时可触发刷新操作。
        swipeRefreshLayout.setDurationToClose(200); //回弹延时  默认 200ms，回弹到刷新高度所用时间
        swipeRefreshLayout.setDurationToCloseHeader(500); //头部回弹时间   默认1000ms
        swipeRefreshLayout.setPullToRefresh(false);
        swipeRefreshLayout.setKeepHeaderWhenRefresh(true);// 刷新是保持头部  默认值 true.

        StoreHouseHeader header = new StoreHouseHeader(getActivity().getApplicationContext());
        header.initWithString("123", 100);
        header.setTextColor(Color.parseColor("#ffffff"));

        swipeRefreshLayout.setHeaderView(header);
        swipeRefreshLayout.addPtrUIHandler(header);
        swipeRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                MyLog.Set("d", FragmentTemHot2.class, "in refresh");
                doRefresh(true);

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRecycler();
        doDefault();

    }

    private void init() {

        mExplosionField = ExplosionField.attach2Window(getActivity());


        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
        p36arraylist = new ArrayList<>();

        round = 0; //default
        count = 24;


    }

    private void protocol36(String range) {

        String strJson = "";

        Map<String, String> data = new HashMap<String, String>();
        data.put("id", id);
        data.put("token", token);
        data.put("rank", Rank);
        data.put("limit", range);
        String sign = IndexSheet.encodePPB(data);
        Map<String, String> sendData = new HashMap<String, String>();
        sendData.put("id", id);
        sendData.put("token", token);
        sendData.put("rank", Rank);
        sendData.put("limit", range);
        sendData.put("style_id", ((CreateAlbumActivity)getActivity()).getStyle_id());//不加入sign計算
        sendData.put("sign", sign);

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P36_GetTemplateList, sendData, null);
            MyLog.Set("d", getClass(), "p36strJson(" + Rank + ") => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p36Result = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p36Result = JsonUtility.GetInt(jsonObject, Key.result);
                if (p36Result == 1) {
                    String jsonData = JsonUtility.GetString(jsonObject, Key.data);

                    if (jsonData != null && !jsonData.equals("")) {

                        p36JsonArray = new JSONArray(jsonData);
                        for (int i = 0; i < p36JsonArray.length(); i++) {
                            JSONObject obj = (JSONObject) p36JsonArray.get(i);

                            String template = JsonUtility.GetString(obj, "template");
                            String templatestatistics = JsonUtility.GetString(obj, "templatestatistics");
                            String user = JsonUtility.GetString(obj, "user");

                            JSONObject obj_template = new JSONObject(template);
                            String s = JsonUtility.GetString(obj_template, "description");
                            String p36Description = HtmlUtility.delHTMLTag(s);
                            String p36Image = JsonUtility.GetString(obj_template, "image");
                            String p36name = JsonUtility.GetString(obj_template, "name");
                            String p36Point = JsonUtility.GetString(obj_template, "point");
                            String p36Template_id = JsonUtility.GetString(obj_template, "template_id");
                            boolean p36Own = JsonUtility.GetBoolean(obj_template, "own");

                            JSONObject obj_templatestatistics = new JSONObject(templatestatistics);
                            String p36Count = JsonUtility.GetString(obj_templatestatistics, "count");

                            JSONObject obj_user = new JSONObject(user);
                            String p36User = JsonUtility.GetString(obj_user, "name");

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put(Key.description, p36Description);
                            map.put(Key.image, p36Image);
                            map.put(Key.name, p36name);
                            map.put(Key.point, p36Point);
                            map.put(Key.template_id, p36Template_id);
                            map.put(Key.count, p36Count);
                            map.put(Key.own, p36Own);

                            if (p36User.equals("null")) {
                                map.put(MapKey.user, "");
                            } else {
                                map.put(MapKey.user, p36User);
                            }
                            p36arraylist.add(map);
                        }
                    } else {
                        p36JsonArray = null;
                    }


                } else if (p36Result == 0) {
                    p36Message = JsonUtility.GetString(jsonObject, Key.message);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }

    private void setRecycler() {

        adapter = new RecyclerTemListAdapter(getActivity(), p36arraylist, false);
        rvTemList.setAdapter(adapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvTemList.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvTemList.getAdapter(), manager.getSpanCount()));
        rvTemList.setLayoutManager(manager);


        RecyclerViewUtils.setHeaderView(rvTemList, vHeader);

        adapter.setOnRecyclerViewListener(new RecyclerTemListAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    public void scrollToTop(){

        ExStaggeredGridLayoutManager linearLayoutManager = (ExStaggeredGridLayoutManager) rvTemList.getLayoutManager();

        int[] firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPositions(null);

        if (firstVisibleItemPosition[0] > 10) {
            rvTemList.scrollToPosition(10);
            MyLog.Set("d", getClass(), "先移動至第10項目");
        }

        rvTemList.smoothScrollToPosition(0);
    }

    private void noScroll() {
        //禁止滾動
        rvTemList.setEnabled(false);
        rvTemList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void canScroll() {
        //恢復滾動
        rvTemList.setEnabled(true);
        rvTemList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }


    public void cleanPicasso() {
        if (p36arraylist != null && p36arraylist.size() > 0) {

            for (int i = 0; i < p36arraylist.size(); i++) {
                String url = (String) p36arraylist.get(i).get(MapKey.image);
                if (url != null && !url.equals("null") && !url.equals("") && getActivity() != null) {
                    Picasso.with(getActivity().getApplicationContext()).invalidate(url);
                }
            }
            System.gc();

        }
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                if (!HttpUtility.isConnect(getActivity())) {
                    noConnect = new NoConnect(getActivity());
                    ((CreateAlbumActivity) getActivity()).setNoConnect(noConnect);
                    return;
                }

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doDefault();
                        break;

                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;

                    case DoingTypeClass.DoRefresh:
                        doRefresh(true);
                        break;


                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);

    }


    public void doDefault() {

        templateListTask = new TemplateListTask();
        templateListTask.execute();


    }

    public void doMoreData() {

        moreDataTask = new MoreDataTask();
        moreDataTask.execute();

    }

    public void doRefresh(boolean doAnim) {

        if (templateListTask != null && !templateListTask.isCancelled()) {
            templateListTask.cancel(true);
            templateListTask = null;
        }

        if (p36arraylist.size() > 0) {
            cleanPicasso();
        }

        p36arraylist.clear();
        round = 0;

        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }

        refreshTask = new RefreshTask(doAnim);
        refreshTask.execute();


    }


    private class TemplateListTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            round = 0;
            sizeMax = false;
        }

        @Override
        protected Object doInBackground(Void... params) {

            protocol36(round + "," + count);


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);


            if (getActivity() != null) {

                if (p36Result == 1) {

                    if (p36arraylist.size() > 0) {

                        adapter.notifyDataSetChanged();
                        round = round + count;
                        tvGuide.setVisibility(View.GONE);
                    }else {
                        tvGuide.setVisibility(View.VISIBLE);
                    }


                } else if (p36Result == 0) {
                    DialogV2Custom.BuildError(getActivity(), p36Message);
                } else if (p36Result == Key.TIMEOUT) {
                    connectInstability();
                } else {
                    DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                }
            }


        }
    }

    private class MoreDataTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            doingType = DoingTypeClass.DoMoreData;

            noScroll();
            pbLoadMore.setVisibility(View.VISIBLE);
            pbLoadMore.progressiveStart();

        }

        @Override
        protected Object doInBackground(Void... params) {
            protocol36(round + "," + count);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            canScroll();
            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();

            if (p36Result == 1) {

                if (p36JsonArray.length() == 0) {
                    sizeMax = true; // 已達最大值
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }
                    return;
                }

                adapter.notifyItemRangeInserted(p36arraylist.size(), count);

                //添加下一次讀取範圍
                round = round + count;

            } else if (p36Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p36Message);
            } else if (p36Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }
        }


    }

    private class RefreshTask extends AsyncTask<Void, Void, Object> {

        private boolean doAnim;

        public RefreshTask(boolean doAnim) {
            this.doAnim = doAnim;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoRefresh;
            noScroll();
        }

        @Override
        protected Object doInBackground(Void... params) {
            protocol36(round + "," + count);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            sizeMax = false;
            isNoDataToastAppeared = false;
            canScroll();

            if (doAnim) {

                mExplosionField.explode(refreshImg);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.refreshComplete();
                    }
                }, 500);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshImg = (ImageView) vHeader.findViewById(R.id.refreshImg);
                        ViewControl.reset(refreshImg);
                        mExplosionField.clear();
                    }
                }, 1000);
            }


            if (p36Result == 1) {
                if(p36arraylist.size()>0) {

                    if(adapter!=null) {
                        adapter.notifyDataSetChanged();
                        round = round + count;
                    }
                    tvGuide.setVisibility(View.GONE);
                }else {
                    tvGuide.setVisibility(View.VISIBLE);
                }




            } else if (p36Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p36Message);
            } else if (p36Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }
        }
    }


    @Override
    public void onPause() {
        cleanPicasso();
        super.onPause();
    }

    @Override
    public void onDestroy(){

        if (templateListTask != null && !templateListTask.isCancelled()) {
            templateListTask.cancel(true);
        }
        templateListTask = null;

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
        }
        moreDataTask = null;

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
        }
        refreshTask = null;



        super.onDestroy();
    }

}

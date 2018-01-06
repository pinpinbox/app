package com.pinpinbox.android.Activity.CreateAlbum;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.HtmlUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.TemList2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.TemTitleListAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogSet;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2015/10/30.
 */


public class TemplateOwn extends Fragment {

    private TemplateListAdapter templateListAdapter;
    private TemplateListTask templateListTask;
    private RefreshTask refreshTask;
    private MoreDataTask moreDataTask;
    private NoConnect noConnect;

    private RelativeLayout r;
    private ListView temListView;
    private ListView subListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View viewFooter;
    private TextView tvNoTemUse;

    public ArrayList<HashMap<String, Object>> p36arraylist;
    private ArrayList<HashMap<String, Object>> sublist;


    private String id, token;
    private String p36Result, p36Message;
    private String p36Description, p36Image, p36name, p36Point, p36Template_id, p36Count, p36User;
    private static final String Rank = "own";

    private int loadCount = 0;
    private int round, count;
    private int doingType;
    private static final int DoRefresh = 0;
    private static final int DoMoreData = 1;


    private boolean countMax = false;
    private boolean toBottom = false;
    private boolean openSub;
    private boolean p36Own;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lay_createalbum_template, container, false);
        r = (RelativeLayout) v.findViewById(R.id.rCreate_album_sublistview);
        temListView = (ListView) v.findViewById(R.id.template_listview);
        subListView = (ListView) v.findViewById(R.id.create_album_sublistview);
        tvNoTemUse = (TextView) v.findViewById(R.id.tvNoTemUse);


        /**2016.10.27 new add*/
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.material_teal_800,
                R.color.material_blue_grey_800);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });


        openSub = ((CreateAlbumActivity) getActivity()).getOpenSub();

        if (openSub) {
            r.setVisibility(View.VISIBLE);
        } else {
            r.setVisibility(View.INVISIBLE);
        }


//        viewFooter = inflater.inflate(R.layout.listview_footer, null);
        viewFooter.setVisibility(View.INVISIBLE);
        temListView.addFooterView(viewFooter, null, false);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        templateListTask = new TemplateListTask();
        templateListTask.execute();

    }

    private void init() {
        SharedPreferences getdata = getActivity().getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");
        p36arraylist = new ArrayList<>();

        sublist = ((CreateAlbumActivity) getActivity()).getSublist();

        round = 24; //default
        count = 24;
    }

    private void setTemItem() {
//        templateListAdapter = new TemplateListAdapter(getActivity(), p36arraylist);
        temListView.setAdapter(templateListAdapter);
        temListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(getActivity())) {
                    noConnect = new NoConnect(getActivity());
                    return;
                }

//                Bundle bundle = new Bundle();
//                bundle.putString("template_id", (String) p36arraylist.get(position).get("template_id"));
//
//                Intent intent = new Intent(getActivity(), TemplateInfoActivity.class);
//                intent.putExtras(bundle);
//                getActivity().startActivity(intent);
//                ActivityAnim.StartAnim(getActivity());
            }
        });

        temListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        if (!toBottom) {
                            MyLog.Set("d", getClass(), "到底了");
                            toBottom = true;
                            if (!countMax) {
                                if (moreDataTask != null && !moreDataTask.isCancelled()) {
                                    moreDataTask.cancel(true);
                                    moreDataTask = null;
                                }
                                if (!HttpUtility.isConnect(getActivity())) {
                                    noConnect = new NoConnect(getActivity());
                                    return;
                                }
                                moreDataTask = new MoreDataTask();
                                moreDataTask.execute();
                            } else {

                                MyLog.Set("d", getClass(), "sizeMax");
                                toBottom = true;
                            }
                        }
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });


    }

    private void setSubItem() {
        TemTitleListAdapter temTitleListAdapter = new TemTitleListAdapter(getActivity(), sublist);
        subListView.setAdapter(temTitleListAdapter);

        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(getActivity())) {
                    noConnect = new NoConnect(getActivity());
                    return;
                }

                String style_id = (String) sublist.get(position).get("style_id");
                String style_name = (String) sublist.get(position).get("name");

                Bundle bundle = new Bundle();
                bundle.putString("rank", Rank);
                bundle.putString("style_id", style_id);
                bundle.putString("style_name", style_name);

                Intent intent = new Intent(getActivity(), TemList2Activity.class);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                ActivityAnim.StartAnim(getActivity());

            }
        });

    }

    public RelativeLayout getR() {
        return r;
    }

    public void setSubListVisibility() {

        getR().setVisibility(View.VISIBLE);

    }

    public void setSubListInVisibility() {

        getR().setVisibility(View.INVISIBLE);

    }

    public ArrayList<HashMap<String, Object>> getP36arraylist() {
        return this.p36arraylist;
    }

    public void cleanPicasso() {
        if (p36arraylist != null && p36arraylist.size() > 0) {

            for (int i = 0; i < p36arraylist.size(); i++) {
                String url = (String) p36arraylist.get(i).get("image");
                if (url != null && !url.equals("null") && !url.equals("") && getActivity() != null) {
                    Picasso.with(getActivity().getApplicationContext()).invalidate(url);
                }
            }
            System.gc();

        }
    }

    public void cleanAdpater() {
        if (p36arraylist != null) {
            p36arraylist.clear();
        }
        if (templateListAdapter != null) {
            templateListAdapter.notifyDataSetChanged();
            templateListAdapter = null;
        }
    }

    private void noScroll() {

        //禁止滾動
        temListView.setEnabled(false);
        temListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    private void canScroll() {

        //恢復滾動
        temListView.setEnabled(true);
        temListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }

    private void callProtocol36(String range){

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
        sendData.put("style_id", "");//不加入sign計算
        sendData.put("sign", sign);

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P36_GetTemplateList, sendData, null);
            MyLog.Set("d", getClass(), "p36strJson(" + Rank + ") => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p36Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p36Result = jsonObject.getString(Key.result);
                if (p36Result.equals("1")) {
                    String jsonData = JsonUtility.GetString(jsonObject, Key.data);

                    if (jsonData != null && !jsonData.equals("")) {

                        JSONArray jsonArray = new JSONArray(jsonData);
                        loadCount = jsonArray.length();
                        for (int i = 0; i < loadCount; i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);

                            String template = JsonUtility.GetString(obj, "template");
                            String templatestatistics = JsonUtility.GetString(obj, "templatestatistics");
                            String user = JsonUtility.GetString(obj, "user");

                            JSONObject obj_template = new JSONObject(template);
                            String s = JsonUtility.GetString(obj_template, "description");
                            p36Description = HtmlUtility.delHTMLTag(s);
                            p36Image = JsonUtility.GetString(obj_template, "image");
                            p36name = JsonUtility.GetString(obj_template, "name");
                            p36Point = JsonUtility.GetString(obj_template, "point");
                            p36Template_id = JsonUtility.GetString(obj_template, "template_id");
                            p36Own = obj_template.getBoolean("own");

                            JSONObject obj_templatestatistics = new JSONObject(templatestatistics);
                            p36Count = JsonUtility.GetString(obj_templatestatistics, "count");

                            JSONObject obj_user = new JSONObject(user);
                            p36User = JsonUtility.GetString(obj_user, "name");

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("description", p36Description);
                            map.put("image", p36Image);
                            map.put("name", p36name);
                            map.put("point", p36Point);
                            map.put("template_id", p36Template_id);
                            map.put("count", p36Count);
                            map.put("own", p36Own);

                            if (p36User.equals("null")) {
                                map.put("user", "");
                            } else {
                                map.put("user", p36User);
                            }
                            p36arraylist.add(map);
                        }
                    }else {
                        loadCount = 0;
                    }


                } else if (p36Result.equals("0")) {
                    p36Message = jsonObject.getString(Key.message);
                } else {
                    p36Result = "";
                }


            } catch (Exception e) {
                p36Result = "";
                e.printStackTrace();
            }

        }





    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoRefresh:
                        doRefresh();
                        break;

                    case DoMoreData:
                        doMoreData();
                        break;


                }
            }
        };

        DialogSet d = new DialogSet(getActivity());
        d.ConnectInstability();
        d.setConnectInstability(connectInstability);

    }

    public void doRefresh() {

        if (!HttpUtility.isConnect(getActivity())) {
            noConnect = new NoConnect(getActivity());
            return;
        }
        if (p36arraylist.size() > 0) {
            cleanPicasso();
        }
        p36arraylist.clear();
        if (templateListAdapter != null) {
            templateListAdapter.notifyDataSetChanged();
            templateListAdapter = null;
        }

        round = 0;

        refreshTask = new RefreshTask();
        refreshTask.execute();

    }

    private void doMoreData(){

        if (!HttpUtility.isConnect(getActivity())) {
            noConnect = new NoConnect(getActivity());
            return;
        }

        moreDataTask = new MoreDataTask();
        moreDataTask.execute();

    }

    private class TemplateListTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = ((CreateAlbumActivity) getActivity()).getStrOwnJson();

            if (strJson != null && !strJson.equals("")) {
                try {
                    MyLog.Set("d", getClass(), "p36strJson(" + Rank + ") => " + strJson);

                    JSONObject jsonObject = new JSONObject(strJson);
                    p36Result = jsonObject.getString(Key.result);
                    if (p36Result.equals("1")) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);

                            String template = JsonUtility.GetString(obj, "template");
                            String templatestatistics = JsonUtility.GetString(obj, "templatestatistics");
                            String user = JsonUtility.GetString(obj, "user");

                            JSONObject obj_template = new JSONObject(template);
                            String s = JsonUtility.GetString(obj_template, "description");
                            p36Description = HtmlUtility.delHTMLTag(s);
                            p36Image = JsonUtility.GetString(obj_template, "image");
                            p36name = JsonUtility.GetString(obj_template, "name");
                            p36Point = JsonUtility.GetString(obj_template, "point");
                            p36Template_id = JsonUtility.GetString(obj_template, "template_id");
                            p36Own = obj_template.getBoolean("own");


                            JSONObject obj_templatestatistics = new JSONObject(templatestatistics);
                            p36Count = JsonUtility.GetString(obj_templatestatistics, "count");

                            JSONObject obj_user = new JSONObject(user);
                            p36User = JsonUtility.GetString(obj_user, "name");

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("description", p36Description);
                            map.put("image", p36Image);
                            map.put("name", p36name);
                            map.put("point", p36Point);
                            map.put("template_id", p36Template_id);
                            map.put("count", p36Count);
                            map.put("own", p36Own);

                            if (p36User.equals("null")) {
                                map.put("user", "");
                            } else {
                                map.put("user", p36User);
                            }
                            p36arraylist.add(map);

                        }
                    } else if (p36Result.equals("0")) {
                        p36Message = jsonObject.getString(Key.message);
                    } else {
                        p36Result = "";
                    }


                } catch (Exception e) {
                    p36Result = "";
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);


            if (getActivity() != null && p36Result!=null) {

                if (p36Result.equals("1")) {

                    if(p36arraylist.size()==0){
                        tvNoTemUse.setVisibility(View.VISIBLE);
                    }else {
                        tvNoTemUse.setVisibility(View.GONE);
                    }

                    try {
                        setTemItem();
                        setSubItem();
                    } catch (Exception e) {
                        //2015/12/23 error
                        e.printStackTrace();
                    }
                } else if (p36Result.equals("0")) {

                } else {
                    DialogSet d = new DialogSet(getActivity());
                    d.DialogUnKnow();
                }


            }


        }
    }

    private class RefreshTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoRefresh;
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }

        }

        @Override
        protected Object doInBackground(Void... params) {

            callProtocol36(round + "," + count);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            swipeRefreshLayout.setRefreshing(false);

            toBottom = false;
            countMax = false;

            if(p36Result.equals("1")){

                if(p36arraylist.size()==0){
                    tvNoTemUse.setVisibility(View.VISIBLE);
                }else {
                    tvNoTemUse.setVisibility(View.GONE);
                }

//                templateListAdapter = new TemplateListAdapter(getActivity(), p36arraylist);
                temListView.setAdapter(templateListAdapter);

                round = round + count;

            }else if(p36Result.equals("0")){

            }else if(p36Result.equals(Key.timeout)){
                connectInstability();
            }else {
                DialogSet d = new DialogSet(getActivity());
                d.DialogUnKnow();
            }

        }
    }

    private class MoreDataTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoMoreData;
            viewFooter.setVisibility(View.VISIBLE);
            noScroll();
        }

        @Override
        protected Object doInBackground(Void... params) {
            callProtocol36(round + "," + count);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            viewFooter.setVisibility(View.INVISIBLE);
            canScroll();
            toBottom = false;

            if (p36Result.equals("1")) {

                round = round + count;

                if (loadCount == 0) {
                    countMax = true; // 已達最大值
                }

                templateListAdapter.notifyDataSetChanged();

            } else if (p36Result.equals("0")) {

            } else if (p36Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogSet d = new DialogSet(getActivity());
                d.DialogUnKnow();
            }


        }

    }

    @Override
    public void onDestroy() {

        if (templateListTask != null && !templateListTask.isCancelled()) {
            templateListTask.cancel(true);
            templateListTask = null;
        }

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
            refreshTask = null;
        }

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
            moreDataTask = null;
        }

        cleanPicasso();
        cleanAdpater();


        MyLog.Set("d", getClass(), "onDestroy");
        super.onDestroy();
    }



}

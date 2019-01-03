package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogQRCode;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.BitmapUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewVisibility;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ChangeTypeListener;
import com.pinpinbox.android.pinpinbox2_0_0.listener.GroupSetListener;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerGroupAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerSearchUserByCooperationAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSearch;
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
 * Created by vmage on 2017/3/31.
 */
public class AlbumGroupActivity extends DraggerActivity implements View.OnClickListener, ChangeTypeListener {

    private Activity mActivity;
    private CountDownTimer countDownTimer;
    private InputMethodManager inputMethodManager;

    private PopupCustom popChangeCooperation;

    private GetCooperationTask getCooperationTask;
    private SearchTask searchTask;
    private InsertCooperationTask insertCooperationTask;
    private DeleteCooperationTask deleteCooperationTask;
    private GetQrCodeTask getQrCodeTask;
    private ChangeCooperationTask changeCooperationTask;

    private RecyclerGroupAdapter groupAdapter;
    private RecyclerSearchUserByCooperationAdapter searchAdapter;

    private List<ItemUser> groupUserList, searchUserList;

    private Bitmap bmpQRCode;

    private RecyclerView rvSearch, rvGroup;
    private ImageView backImg, scanImg, clearImg;
    private EditText edSearch;
    private TextView tvGuide;
    private LinearLayout linPopGuide;
    private SmoothProgressBar pbRefresh;

    private String id, token;
    private String album_id;
    private String p44Message = "";
    private String strMyIdentity = "";
    private String strEditIdentity = "";

    private int p44Result = -1;
    private int doingType;
    private int clickPosition;

    private boolean isLoading = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_albumgroup);
        SystemUtility.SysApplication.getInstance().addActivity(this);



        getBundle();

        init();

        setGroupRecycler();

        setSearchRecycler();

        doGetCooperation();

        setSearch();

    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            album_id = bundle.getString(Key.album_id, "");
            strMyIdentity = bundle.getString(Key.identity,"");
        }

    }

    private void init() {

        mActivity = this;

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        groupUserList = new ArrayList<>();
        searchUserList = new ArrayList<>();

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        rvSearch = (RecyclerView) findViewById(R.id.rvSearch);
        rvGroup = (RecyclerView) findViewById(R.id.rvGroup);
        backImg = (ImageView) findViewById(R.id.backImg);
        scanImg = (ImageView) findViewById(R.id.scanImg);
        clearImg = (ImageView) findViewById(R.id.clearImg);
        edSearch = (EditText) findViewById(R.id.edSearch);
        tvGuide = (TextView) findViewById(R.id.tvGuide);

        pbRefresh = (SmoothProgressBar) findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();


        backImg.setOnClickListener(this);
        clearImg.setOnClickListener(this);
        scanImg.setOnClickListener(this);
    }

    private void setGroupRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvGroup.setLayoutManager(layoutManager);
        groupAdapter = new RecyclerGroupAdapter(this, groupUserList);
        rvGroup.setAdapter(groupAdapter);




        groupAdapter.setGroupSetListener(new GroupSetListener() {
            @Override
            public void delete(int position) {

                if(strMyIdentity.equals(groupUserList.get(position).getIdentity())){
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_can_not_delete_approver);
                    return;
                }


                clickPosition = position;

                MyLog.Set("d", mActivity.getClass(), "刪除的是 => " + groupUserList.get(position).getName());

                doDeleteCooperation();
            }

            @Override
            public void changeType(int position) {


                if(strMyIdentity.equals(groupUserList.get(position).getIdentity())){
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_can_not_change_act_each_other_by_approver);
                    return;
                }


                clickPosition = position;

                if(popChangeCooperation!=null){
                    popChangeCooperation.show((RelativeLayout)findViewById(R.id.rBackground));
                }else {
                    setChangeCooperationPop();
                }

                MyLog.Set("d", mActivity.getClass(), "點擊的位置是 => " + position);


            }
        });

    }

    private void setChangeCooperationPop(){
        popChangeCooperation = new PopupCustom(mActivity);
        popChangeCooperation.setPopup(R.layout.pop_2_0_0_change_cooperation, R.style.pinpinbox_popupAnimation_bottom);
        View vPop = popChangeCooperation.getPopupView();

        linPopGuide = vPop.findViewById(R.id.linPopGuide);
        ImageView guideImg = vPop.findViewById(R.id.guideImg);
        TextView tvApprover = vPop.findViewById(R.id.tvApprover);
        TextView tvEditor = vPop.findViewById(R.id.tvEditor);
        TextView tvViewer = vPop.findViewById(R.id.tvViewer);

        if(!strMyIdentity.equals("admin")){
            tvApprover.setVisibility(View.GONE);
        }else {
            tvApprover.setVisibility(View.VISIBLE);
        }


        linPopGuide.setOnClickListener(this);
        guideImg.setOnClickListener(this);
        tvApprover.setOnClickListener(this);
        tvEditor.setOnClickListener(this);
        tvViewer.setOnClickListener(this);

        popChangeCooperation.show((RelativeLayout)findViewById(R.id.rBackground));



    }

    private void setSearchRecycler() {

        searchAdapter = new RecyclerSearchUserByCooperationAdapter(mActivity, searchUserList);
        rvSearch.setAdapter(searchAdapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(searchAdapter);
        rvSearch.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvSearch.getAdapter(), manager.getSpanCount()));
        rvSearch.setLayoutManager(manager);

        View vHeader = LayoutInflater.from(this).inflate(R.layout.header_2_0_0_cooperation_search, null);
        TextUtility.setBold((TextView) vHeader.findViewById(R.id.tvTitle), true);

        RecyclerViewUtils.setHeaderView(rvSearch, vHeader);


        rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if(newState==1){
                    //在用手指滾動
                    inputMethodManager.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                }


            }
        });


        searchAdapter.setOnRecyclerViewListener(new RecyclerSearchUserByCooperationAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


        searchAdapter.setChangeTypeListener(this);


    }

    private void loadDataBegin() {
        pbRefresh.setVisibility(View.VISIBLE);
        pbRefresh.progressiveStart();
        isLoading = true;
    }

    private void loadDataEnd() {
        pbRefresh.progressiveStop();
        pbRefresh.setVisibility(View.GONE);
        isLoading = false;
    }

    private void setSearch() {

        countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MyLog.Set("d", FragmentSearch.class, "timer =>" + (millisUntilFinished / 1000) + "");
            }

            @Override
            public void onFinish() {
                MyLog.Set("d", FragmentSearch.class, "timer => finish()");
                countDownTimer.cancel();

                cleanData();




                doSearch();


            }
        };

        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                MyLog.Set("d", FragmentSearch.class, "afterTextChanged");

                if (s.toString().equals("")) {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }

                    clearImg.setVisibility(View.GONE);

                } else {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer.start();

                        MyLog.Set("d", FragmentSearch.class, "重新倒數");
                    }

                    clearImg.setVisibility(View.VISIBLE);

                }

            }
        });

    }

    private void cleanData(){
        int count = searchUserList.size();

        if(count>0) {

            searchAdapter.notifyItemRangeRemoved(0, count);

            for (int i = 0; i < count; i++) {
                Picasso.with(mActivity.getApplicationContext()).invalidate(searchUserList.get(i).getPicture());
            }
            System.gc();
        }
        searchUserList.clear();

    }

    private void cleanPicasso(){
        int count = searchUserList.size();
        if(count>0) {
            for (int i = 0; i < count; i++) {
                Picasso.with(mActivity.getApplicationContext()).invalidate(searchUserList.get(i).getPicture());
            }
        }

        int count2 = groupUserList.size();
        if(count2>0) {
            for (int i = 0; i < count2; i++) {
                Picasso.with(mActivity.getApplicationContext()).invalidate(groupUserList.get(i).getPicture());
            }
        }

        System.gc();

    }

    private void back() {
        finish();
        ActivityAnim.FinishAnim(this);
    }

    private void setProtocol44() {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P44_GetCooperationList, SetMapByProtocol.setParam44_getcooperationlist(id, token, Key.album, album_id), null);
            MyLog.Set("d", mActivity.getClass(), "p44strJson => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p44Result = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p44Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                if (p44Result == 1) {
                    String jsonData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);

                        ItemUser itemUser = new ItemUser();

                        String user = JsonUtility.GetString(object, ProtocolKey.user);
                        String cooperation = JsonUtility.GetString(object, ProtocolKey.cooperation);

                        JSONObject jsonUser = new JSONObject(user);
                        itemUser.setName(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                        itemUser.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                        itemUser.setUser_id(JsonUtility.GetString(jsonUser, ProtocolKey.user_id));


                        JSONObject jsonCooperation = new JSONObject(cooperation);
                        String identity = JsonUtility.GetString(jsonCooperation, ProtocolKey.identity);
                        itemUser.setIdentity(identity);

                        if (identity.equals("admin")) {

                            groupUserList.add(0, itemUser);

                        } else {

                            groupUserList.add(itemUser);
                        }

                    }

                } else if (p44Result == 0) {
                    p44Message = JsonUtility.GetString(jsonObject, Key.message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoGetCooperation:
                        doGetCooperation();
                        break;

                    case DoingTypeClass.DoSearchUserList:
                        doSearch();
                        break;

                    case DoingTypeClass.DoJoinCooperation:
                        doInsertCooperation();
                        break;

                    case DoingTypeClass.DoDeleteCooperation:
                        doDeleteCooperation();
                        break;

                    case DoingTypeClass.DoGetQrCode:
                        doGetQrCode();
                        break;

                    case DoingTypeClass.DoChangeCooperation:
                        doChangeCooperation();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doGetCooperation() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getCooperationTask = new GetCooperationTask();
        getCooperationTask.execute();
    }

    private void doSearch() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        searchTask = new SearchTask();
        searchTask.execute();

    }

    private void doInsertCooperation(){
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        insertCooperationTask = new InsertCooperationTask();
        insertCooperationTask.execute();

    }

    private void doDeleteCooperation() {
        if (!HttpUtility.isConnect(this)) {
           setNoConnect();
            return;
        }
        deleteCooperationTask = new DeleteCooperationTask();
        deleteCooperationTask.execute();

    }

    private void doGetQrCode(){
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getQrCodeTask = new GetQrCodeTask();
        getQrCodeTask.execute();

    }

    private void doChangeCooperation(){
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        changeCooperationTask = new ChangeCooperationTask();
        changeCooperationTask.execute();

    }


    private class GetCooperationTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();
            doingType = DoingTypeClass.DoGetCooperation;
        }

        @Override
        protected Object doInBackground(Void... params) {


            setProtocol44();

            if (p44Result == 1) {

                /**get QRcode image*/
//                setProtocol89();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p44Result == 1) {

                groupAdapter.notifyDataSetChanged();

            } else if (p44Result == 0) {

                DialogV2Custom.BuildError(mActivity, p44Message);

            } else if (p44Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }

    }

    private class SearchTask extends AsyncTask<Void, Void, Object> {

        private int p41Result = -1;
        private String p41Message = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSearchUserList;
            loadDataBegin();
        }

        @Override
        protected Object doInBackground(Void... params) {


            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P41_Search, SetMapByProtocol.setParam41_search(id, token, Key.user, edSearch.getText().toString(), "0,40"), null);
                MyLog.Set("d", getClass(), "p41strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p41Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p41Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p41Result == 1) {
                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);

                            ItemUser itemUser = new ItemUser();

                            String user = JsonUtility.GetString(object, ProtocolKey.user);

                            JSONObject jsonUser = new JSONObject(user);
                            itemUser.setName(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                            itemUser.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));

                            String mUserId = JsonUtility.GetString(jsonUser, ProtocolKey.user_id);
                            itemUser.setUser_id(mUserId);

                            boolean canAdd = true;

                            for (int k = 0; k < groupUserList.size(); k++) {
                                if (mUserId.equals(id) || mUserId.equals(groupUserList.get(k).getUser_id())) {
                                    canAdd = false;
                                    MyLog.Set("e", mActivity.getClass(), "此用戶為自己或者已加入共用");
                                    break;
                                }
                            }

                            itemUser.setInvite(false);

                            if (canAdd) {
                                searchUserList.add(itemUser);
                            }

                        }

                    } else if (p41Result == 0) {
                        p41Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            loadDataEnd();

            if (p41Result == 1) {


                if(searchUserList.size()>0){
                    ViewVisibility.setVisible(rvSearch);
                    ViewVisibility.setGone(tvGuide);
                }else {

                    tvGuide.setText(R.string.pinpinbox_2_0_0_guide_no_match_keyword_creator);
                    ViewVisibility.setVisible(tvGuide);
                    ViewVisibility.setGone(rvSearch);

                }

                searchAdapter.notifyDataSetChanged();

            } else if (p41Result == 0) {
                DialogV2Custom.BuildError(mActivity, p41Message);
            } else if (p41Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }

        }
    }

    private class InsertCooperationTask extends AsyncTask<Void, Void, Object> {

        private int p46Result = -1;
        private String p46Message = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoJoinCooperation;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P46_InsertCooperation, SetMapByProtocol.setParam46_insertcooperation(id, token, Key.album, album_id, searchUserList.get(clickPosition).getUser_id()), null);
                MyLog.Set("d", getClass(), "p41strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p46Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p46Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                   if (p46Result == 0) {
                        p46Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            if (p46Result == 1) {

//                searchUserList.get(clickPosition).setInvite(true);

                MyLog.Set("d", mActivity.getClass(), "加入共用 => " + searchUserList.get(clickPosition).getName());

                ItemUser itemUser = new ItemUser();

                itemUser.setName(searchUserList.get(clickPosition).getName());
                itemUser.setUser_id(searchUserList.get(clickPosition).getUser_id());
                itemUser.setPicture(searchUserList.get(clickPosition).getPicture());
                itemUser.setIdentity("viewer");


//                groupUserList.add(1, itemUser);
//                groupAdapter.notifyItemInserted(1);

                groupAdapter.addData(1, itemUser);

//                MyLog.Set("e", getClass(), "------" + groupUserList.size());
//                MyLog.Set("e", getClass(), "++++++" + groupAdapter.getItemUserList().size());


                searchUserList.get(clickPosition).setInvite(true);
                searchAdapter.notifyItemChanged(clickPosition);


            } else if (p46Result == 0) {
                DialogV2Custom.BuildError(mActivity, p46Message);
            } else if (p46Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }

        }
    }

    private class DeleteCooperationTask extends AsyncTask<Void, Void, Object> {

        private int p45Result = -1;
        private String p45Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDeleteCooperation;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P45_DeleteCooperation,
                        SetMapByProtocol.setParam45_deletecooperation(id, token, Key.album, album_id, groupUserList.get(clickPosition).getUser_id()),
                        null);
                MyLog.Set("d", getClass(), "p45strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p45Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p45Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                   if (p45Result==0) {
                        p45Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            if (p45Result==1) {

                if(searchUserList.size()>0){
                    String gId = groupUserList.get(clickPosition).getUser_id();
                    int count = searchUserList.size();
                    for (int i = 0; i < count; i++) {
                        String sId = searchUserList.get(i).getUser_id();
                        if(gId.equals(sId)){
                            searchUserList.get(i).setInvite(false);
                            searchAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }


                groupAdapter.removeData(clickPosition);



            } else if (p45Result==0) {
                DialogV2Custom.BuildError(mActivity, p45Message);

            } else if (p45Result==Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }

    }

    private class GetQrCodeTask extends AsyncTask<Void, Void, Object> {

        private int p89Result = -1;
        private String p89Message = "";
        private String strBase64ByQRCode = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetQrCode;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            JSONObject obj = new JSONObject();
            try {
                obj.put("is_cooperation", true);
            } catch (Exception e) {
                e.printStackTrace();
            }

            HashMap<String, String> data = new HashMap<>();
            data.put(Key.id, id);
            data.put(Key.token, token);
            data.put(Key.type, Value.album);
            data.put(Key.type_id, album_id);
            data.put(Key.effect, Value.execute);
            String sign = IndexSheet.encodePPB(data);

            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, id);
            sendData.put(Key.token, token);
            sendData.put(Key.type, Value.album);
            sendData.put(Key.type_id, album_id);
            sendData.put(Key.effect, Value.execute);

            sendData.put(Key.is, obj.toString());

            sendData.put(Key.sign, sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P89_GetQRCode, sendData,null);
                MyLog.Set("d", getClass(), "p45strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p89Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p89Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if(p89Result==1) {

                        strBase64ByQRCode = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    }else if (p89Result==0) {
                        p89Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            if (p89Result==1) {

                bmpQRCode = BitmapUtility.base64ToBitmap(strBase64ByQRCode);

                DialogQRCode d = new DialogQRCode(mActivity, bmpQRCode);
                d.show();


            } else if (p89Result==0) {
                DialogV2Custom.BuildError(mActivity, p89Message);

            } else if (p89Result==Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }

    }

    private class ChangeCooperationTask extends AsyncTask<Void, Void, Object> {

        private int p63Result = -1;
        private String p63Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoChangeCooperation;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String user_id = groupUserList.get(clickPosition).getUser_id();

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P63_UpDateCooperation,
                        SetMapByProtocol.setParam63_updatecooperation(id, token, Key.album, album_id, user_id, strEditIdentity),
                        null);
                MyLog.Set("d", getClass(), "p63strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p63Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p63Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p63Result==0) {
                        p63Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            if (p63Result==1) {

                groupUserList.get(clickPosition).setIdentity(strEditIdentity);
                groupAdapter.notifyItemChanged(clickPosition);


            } else if (p63Result==0) {
                DialogV2Custom.BuildError(mActivity, p63Message);

            } else if (p63Result==Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }

    }


    @Override
    public void changeType(int position) {

        /*有header*/
        clickPosition = position;

        boolean isInvite = searchUserList.get(clickPosition).isInvite();

        if(!isInvite){

            doInsertCooperation();

        }else {

            PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_already_in_cooperation_list);

        }



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.backImg:

                back();

                break;

            case R.id.clearImg:

                edSearch.setText("");

                break;

            case R.id.scanImg:

                if(bmpQRCode!=null && !bmpQRCode.isRecycled()){
                    DialogQRCode d = new DialogQRCode(mActivity, bmpQRCode);
                    d.show();
                }else {
                    doGetQrCode();
                }
                break;

            case R.id.linPopGuide:
                popChangeCooperation.dismiss();
                break;

            case R.id.guideImg:

                ViewVisibility.setVisible(linPopGuide);

                break;

            case R.id.tvApprover:
                popChangeCooperation.dismiss();
                strEditIdentity = "approver";
                doChangeCooperation();
                break;

            case R.id.tvEditor:
                popChangeCooperation.dismiss();
                strEditIdentity = "editor";
                doChangeCooperation();
                break;

            case R.id.tvViewer:
                popChangeCooperation.dismiss();
                strEditIdentity = "viewer";
                doChangeCooperation();
                break;




        }


    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if(bmpQRCode!=null && !bmpQRCode.isRecycled()){
            bmpQRCode.recycle();
            bmpQRCode = null;
        }

        cleanPicasso();

        cancelTask(getQrCodeTask);
        cancelTask(getCooperationTask);
        cancelTask(insertCooperationTask);
        cancelTask(searchTask);
        cancelTask(deleteCooperationTask);
        cancelTask(changeCooperationTask);

        Recycle.IMG(backImg);
        Recycle.IMG(scanImg);
        Recycle.IMG(clearImg);
        Recycle.IMG((ImageView)findViewById(R.id.searchImg));

        if(groupUserList!=null) {
            groupUserList.clear();
        }
        groupUserList = null;
        groupAdapter = null;

        if(searchUserList!=null){
            searchUserList.clear();
        }

        searchUserList = null;
        searchAdapter = null;

        System.gc();

    }
}

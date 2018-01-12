package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.DoingTypeClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumInfo2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Author2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Main2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerSearchAlbumAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerSearchUserAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ExLinearLayoutManager;
import com.squareup.picasso.Picasso;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * Created by kevin9594 on 2017/3/4.
 */
public class FragmentSearch2 extends Fragment implements View.OnClickListener {


    private NoConnect noConnect;
    private CountDownTimer countDownTimer;
    private LoadingAnimation loading;
    private InputMethodManager inputMethodManager;

    private GetDefaultDataTask getDefaultDataTask;
    private UserListTask userListTask;
    private AlbumListTask albumListTask;

    private RecyclerSearchUserAdapter userAdapter;
    private RecyclerSearchAlbumAdapter albumAdapter;

    private ArrayList<HashMap<String, Object>> currentUserList;
//    private ArrayList<HashMap<String, Object>> currentAlbumList;

    private ArrayList<HashMap<String, Object>> defaultUserList;
    //    private ArrayList<HashMap<String, Object>> defaultAlbumList;
    private ArrayList<HashMap<String, Object>> searchUserList;
//    private ArrayList<HashMap<String, Object>> searchAlbumList;


    private ArrayList<ItemAlbum> currentAlbumList, defaultAlbumList, searchAlbumList;


    private String id, token;
    private String strSearch = "";

    private static final int REQUEST_CODE_CAMERA = 104;
    private int doingType;

    private boolean isLoading = false;

    private View vHeader;
    private RecyclerView rvSearch;
    private EditText edSearch;
    private ImageView clearImg, scanImg;
    private TextView tvGuideNoAlbum;
//            , tvScanSearch;
    private SmoothProgressBar pbRefresh;

    /*header*/
    private RecyclerView rvUser;
    private TextView tvSearchUserTitle, tvSearchAlbumTitle;
    private TextView tvGuideNoUser;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_search, container, false);

        rvSearch = (RecyclerView) v.findViewById(R.id.rvSearch);
        edSearch = (EditText) v.findViewById(R.id.edSearch);
        clearImg = (ImageView) v.findViewById(R.id.clearImg);
        tvGuideNoAlbum = (TextView) v.findViewById(R.id.tvGuideNoAlbum);
//        tvScanSearch = (TextView) v.findViewById(R.id.tvScanSearch);
        scanImg = (ImageView)v.findViewById(R.id.scanImg);

        pbRefresh = (SmoothProgressBar) v.findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();


        vHeader = LayoutInflater.from(getActivity()).inflate(R.layout.header_2_0_0_search, null);
        rvUser = (RecyclerView) vHeader.findViewById(R.id.rvUser);
        tvSearchUserTitle = (TextView) vHeader.findViewById(R.id.tvSearchUserTitle);
        tvSearchAlbumTitle = (TextView) vHeader.findViewById(R.id.tvSearchAlbumTitle);
        tvGuideNoUser = (TextView) vHeader.findViewById(R.id.tvGuideNoUser);

//        TextUtility.setBold(tvScanSearch, true);
        TextUtility.setBold(tvSearchUserTitle, true);
        TextUtility.setBold(tvSearchAlbumTitle, true);

        clearImg.setOnClickListener(this);
        scanImg.setOnClickListener(this);
//        tvScanSearch.setOnClickListener(this);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        init();
//        setUserRecycler();
//        setAlbumRecycler();
//        doGetDefaultData();
//        setSearch();


    }

    private void init() {

        loading = ((Main2Activity) getActivity()).getLoading();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        currentUserList = new ArrayList<>();
        currentAlbumList = new ArrayList<>();

        defaultUserList = new ArrayList<>();
        defaultAlbumList = new ArrayList<>();
        searchUserList = new ArrayList<>();
        searchAlbumList = new ArrayList<>();

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
    }


    private void setUserRecycler() {
        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvUser.setLayoutManager(layoutManager);
        userAdapter = new RecyclerSearchUserAdapter(getActivity(), currentUserList);
        rvUser.setAdapter(userAdapter);

        userAdapter.setOnRecyclerViewListener(new RecyclerSearchUserAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }


                if(currentUserList==null || currentUserList.size()<1){
                    return;
                }


                String strSearch = edSearch.getText().toString();

                if (strSearch.equals("")) {
                    FlurryUtil.onEvent(FlurryKey.search_select_default_user);
                } else {
                    FlurryUtil.onEvent(FlurryKey.search_success_user_to_select);
                }

                Bundle bundle = new Bundle();
                bundle.putString(Key.author_id, (String) currentUserList.get(position).get(Key.user_id));
                bundle.putString(Key.picture, (String) currentUserList.get(position).get(Key.picture));
                bundle.putString(Key.name, (String) currentUserList.get(position).get(Key.name));


                if (SystemUtility.Above_Equal_V5()) {

                    Intent intent = new Intent(getActivity(), Author2Activity.class).putExtras(bundle);
                    RoundCornerImageView userImg = (RoundCornerImageView) v.findViewById(R.id.userImg);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(),
                                    userImg,
                                    ViewCompat.getTransitionName(userImg));
                    startActivity(intent, options.toBundle());


                } else {


                    Intent intent = new Intent(getActivity(), Author2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    ActivityAnim.StartAnim(getActivity());


                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void setAlbumRecycler() {

        rvSearch.addItemDecoration(new SpacesItemDecoration(16));

        albumAdapter = new RecyclerSearchAlbumAdapter(getActivity(), currentAlbumList);
        rvSearch.setAdapter(albumAdapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(albumAdapter);
        rvSearch.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = null;

        if (SystemUtility.isTablet(getActivity().getApplicationContext())) {

            //平版
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        } else {

            //手機
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }


//        ExStaggeredGridLayoutManager manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvSearch.getAdapter(), manager.getSpanCount()));
        rvSearch.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvSearch, vHeader);

        albumAdapter.setOnRecyclerViewListener(new RecyclerSearchAlbumAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if(currentAlbumList==null || currentAlbumList.size()<1){
                    return;
                }

                String strSearch = edSearch.getText().toString();

                if (strSearch.equals("")) {
                    FlurryUtil.onEvent(FlurryKey.search_select_default_album);
                } else {
                    FlurryUtil.onEvent(FlurryKey.search_success_album_to_select);
                }

                ImageView img = (ImageView) v.findViewById(R.id.coverImg);

                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, currentAlbumList.get(position).getAlbum_id());
                bundle.putString(Key.cover, currentAlbumList.get(position).getCover());
                bundle.putInt(Key.image_orientation, currentAlbumList.get(position).getImage_orientation());
                Intent intent = new Intent(getActivity(), AlbumInfo2Activity.class);
                intent.putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(getActivity(),
                                img,
                                ViewCompat.getTransitionName(img));
                startActivity(intent, options.toBundle());


//                startActivity(intent);
//                ActivityAnim.StartAnim(getActivity());

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1) {
                    //在用手指滾動
                    inputMethodManager.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                }
            }
        });

    }

    private void setSearch() {

        countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MyLog.Set("d", FragmentSearch2.class, "timer =>" + (millisUntilFinished / 1000) + "");
            }

            @Override
            public void onFinish() {
                MyLog.Set("d", FragmentSearch2.class, "timer => finish()");
                countDownTimer.cancel();

                if (edSearch.getText().toString().equals(strSearch)) {

                    MyLog.Set("d", FragmentSearch2.class, "字串沒變更");

                } else {

                    strSearch = edSearch.getText().toString();

                    loadDataBegin();
                    doSearchUser();
                    doSearchAlbum();

                }


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

                MyLog.Set("d", FragmentSearch2.class, "afterTextChanged");


                if (s.toString().equals("")) {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }

                    setDefaultData();

                    clearImg.setVisibility(View.GONE);

                } else {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer.start();

                        MyLog.Set("d", FragmentSearch2.class, "重新倒數");
                    }

                    clearImg.setVisibility(View.VISIBLE);


                }


            }
        });


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

    private void setDefaultData() {

        /*回復標題*/
        tvSearchUserTitle.setText(R.string.pinpinbox_2_0_0_title_recommend_creator);
        tvSearchAlbumTitle.setText(R.string.pinpinbox_2_0_0_title_popular_album);


        /*移除搜尋提示*/
        tvGuideNoAlbum.setVisibility(View.GONE);
        tvGuideNoUser.setVisibility(View.GONE);

        /*清除當前所有資料*/
        currentUserList.clear();
        currentAlbumList.clear();

        /*加入預設資料*/
        for (int i = 0; i < defaultUserList.size(); i++) {
            currentUserList.add(defaultUserList.get(i));
        }
        for (int i = 0; i < defaultAlbumList.size(); i++) {
            currentAlbumList.add(defaultAlbumList.get(i));
        }
        userAdapter.notifyItemRangeChanged(0, currentUserList.size());
        albumAdapter.notifyItemRangeChanged(0, currentAlbumList.size());

    }

    private void setSearchUserData() {

        for (int i = 0; i < searchUserList.size(); i++) {
            currentUserList.add(searchUserList.get(i));
        }

        if (currentUserList.size() < 1) {
            tvGuideNoUser.setVisibility(View.VISIBLE);
        } else {
            tvGuideNoUser.setVisibility(View.GONE);
        }

        userAdapter.notifyItemRangeInserted(0, currentUserList.size());

    }

    private void setSearchAlbumData() {

        for (int i = 0; i < searchAlbumList.size(); i++) {
            currentAlbumList.add(searchAlbumList.get(i));
        }


        if (currentAlbumList.size() < 1) {
            tvGuideNoAlbum.setVisibility(View.VISIBLE);
        } else {
            tvGuideNoAlbum.setVisibility(View.GONE);
        }

        albumAdapter.notifyItemRangeInserted(0, currentAlbumList.size());

    }

    public void scrollToTop() {

        try {

            ExStaggeredGridLayoutManager linearLayoutManager = (ExStaggeredGridLayoutManager) rvSearch.getLayoutManager();

            int[] firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPositions(null);

            if (firstVisibleItemPosition[0] > 10) {
                rvSearch.scrollToPosition(10);
                MyLog.Set("d", getClass(), "先移動至第10項目");
            }

            rvSearch.smoothScrollToPosition(0);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void cleanPicasso() {

        if (currentUserList != null && currentUserList.size() > 0) {
            for (int i = 0; i < currentUserList.size(); i++) {
                String strPicture = (String) currentUserList.get(i).get(Key.picture);
                Picasso.with(getActivity().getApplicationContext()).invalidate(strPicture);
            }
            currentUserList.clear();
        }

        if (currentAlbumList != null && currentAlbumList.size() > 0) {
            for (int i = 0; i < currentAlbumList.size(); i++) {
                String cover = currentAlbumList.get(i).getCover();
                Picasso.with(getActivity().getApplicationContext()).invalidate(cover);
            }
            currentAlbumList.clear();
        }


        if (defaultUserList != null && defaultUserList.size() > 0) {
            for (int i = 0; i < defaultUserList.size(); i++) {
                String strPicture = (String) defaultUserList.get(i).get(Key.picture);
                Picasso.with(getActivity().getApplicationContext()).invalidate(strPicture);
            }
            defaultUserList.clear();
        }

        if (defaultAlbumList != null && defaultAlbumList.size() > 0) {
            for (int i = 0; i < defaultAlbumList.size(); i++) {
                String cover = defaultAlbumList.get(i).getCover();
                Picasso.with(getActivity().getApplicationContext()).invalidate(cover);
            }
            defaultAlbumList.clear();
        }

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoingTypeClass.DoGetSearchDefaultData:
                        doGetDefaultData();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);
    }

    private void doGetDefaultData() {

        if (checkConnect()) {
            getDefaultDataTask = new GetDefaultDataTask();
            getDefaultDataTask.execute();
        }


    }

    private void doSearchUser() {
        if (checkConnect()) {
            userListTask = new UserListTask();
            userListTask.execute();
        }
    }

    private void doSearchAlbum() {

        if (checkConnect()) {
            albumListTask = new AlbumListTask();
            albumListTask.execute();
        }

    }

    private boolean checkConnect() {
        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((Main2Activity) getActivity()).setNoConnect();
            return false;
        } else {
            return true;
        }
    }

    private class GetDefaultDataTask extends AsyncTask<Void, Void, Object> {

        private int p86ResultByUser = -1, p86ResultByAlbum = -1;
        private String p86Message = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetSearchDefaultData;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            protocol86ToGetUser();

            if (p86ResultByUser == 1) {
                protocol86ToGetAlbum();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ViewPropertyAnimator alphaTo1 = rvSearch.animate();
                    alphaTo1.setDuration(400)
                            .alpha(1)
                            .start();
                }
            }, 200);

            if (p86ResultByUser == 1 && p86ResultByAlbum == 1) {

                setDefaultData();

                return;
            }


            if (p86ResultByUser == Key.TIMEOUT || p86ResultByAlbum == Key.TIMEOUT) {

                defaultUserList.clear();
                defaultAlbumList.clear();
                connectInstability();
                return;
            }


            if (p86ResultByUser == 0 || p86ResultByAlbum == 0) {
                DialogV2Custom.BuildError(getActivity(), p86Message);
                return;
            }

            DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
        }

        private void protocol86ToGetUser() {
            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.p86_GetRecommendedList,
                        SetMapByProtocol.setParam86_getrecommendedlist(id, token, Key.user, "0,16"), null);
                MyLog.Set("d", getClass(), "p86strJson(user) => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p86ResultByUser = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p86ResultByUser = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p86ResultByUser == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);


                            String user = JsonUtility.GetString(j, ProtocolKey.user);
                            JSONObject object = new JSONObject(user);

                            String name = JsonUtility.GetString(object, ProtocolKey.name);
                            String picture = JsonUtility.GetString(object, ProtocolKey.picture);
                            String user_id = JsonUtility.GetString(object, ProtocolKey.user_id);


                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put(Key.name, name);
                            map.put(Key.user_id, user_id);
                            map.put(Key.picture, picture);

                            if (!user_id.equals(id)) {
                                defaultUserList.add(map);
                            }
                        }

                    } else if (p86ResultByUser == 0) {
                        p86Message = jsonObject.getString(ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private void protocol86ToGetAlbum() {
            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.p86_GetRecommendedList,
                        SetMapByProtocol.setParam86_getrecommendedlist(id, token, Key.album, "0,16"), null);
                MyLog.Set("d", getClass(), "p86strJson(album) => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p86ResultByAlbum = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p86ResultByAlbum = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p86ResultByAlbum == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        int minHeight = DensityUtility.dip2px(getActivity().getApplicationContext(), 72);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);

                            ItemAlbum itemAlbum = new ItemAlbum();


                            String album = JsonUtility.GetString(j, ProtocolKey.album);
                            JSONObject jsonAlbum = new JSONObject(album);

                            itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                            itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));

                            String cover = JsonUtility.GetString(jsonAlbum, ProtocolKey.cover);
                            itemAlbum.setCover(cover);

                            try {
                                int width = jsonAlbum.getInt(ProtocolKey.cover_width);
                                int height = jsonAlbum.getInt(ProtocolKey.cover_height);
                                int image_height = StaggeredHeight.setImageHeight(width, height);

                                if (image_height < minHeight) {
                                    image_height = minHeight;
                                }

                                itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                                itemAlbum.setCover_height(image_height);
                                itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));

                                if(width>height){
                                    itemAlbum.setImage_orientation(ItemAlbum.LANDSCAPE);
                                }else if(height>width){
                                    itemAlbum.setImage_orientation(ItemAlbum.PORTRAIT);
                                }else {
                                    itemAlbum.setImage_orientation(0);
                                }

                            } catch (Exception e) {
                                itemAlbum.setCover_hex("");
                                itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                                itemAlbum.setCover_height(PPBApplication.getInstance().getStaggeredWidth());
                                MyLog.Set("e", this.getClass(), "圖片長寬無法讀取");
                            }

                            String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                            JSONObject jsonUsefor = new JSONObject(usefor);
                            itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                            itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                            itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));
                            itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));


                            String user = JsonUtility.GetString(j, ProtocolKey.user);
                            JSONObject jsonUser = new JSONObject(user);
                            itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));

                            defaultAlbumList.add(itemAlbum);
                        }

                    } else if (p86ResultByAlbum == 0) {
                        p86Message = JsonUtility.GetString(jsonObject, Key.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private class UserListTask extends AsyncTask<Void, Void, Object> {

        private int p41Result = -1;
        private String p41Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSearchUserList;

            /*執行搜尋前全部移除*/
            userAdapter.notifyItemRangeRemoved(0, currentUserList.size());

            if (searchUserList.size() > 0) {
                searchUserList.clear();
            }

            if (currentUserList.size() > 0) {
                for (int i = 0; i < currentUserList.size(); i++) {
                    String strPicture = (String) currentUserList.get(i).get(Key.picture);
                    Picasso.with(getActivity().getApplicationContext()).invalidate(strPicture);
                }
                currentUserList.clear();
            }

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P41_Search,
                        SetMapByProtocol.setParam41_search(id, token, "user", strSearch, "0,32"), null);
                MyLog.Set("d", getClass(), "p41strJson(user) =>" + strJson);
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
                            JSONObject j = (JSONObject) jsonArray.get(i);


                            String user = JsonUtility.GetString(j, ProtocolKey.user);
                            JSONObject object = new JSONObject(user);

                            String name = JsonUtility.GetString(object, ProtocolKey.name);
                            String picture = JsonUtility.GetString(object, ProtocolKey.picture);
                            String user_id = JsonUtility.GetString(object, ProtocolKey.user_id);

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put(Key.name, name);
                            map.put(Key.user_id, user_id);
                            map.put(Key.picture, picture);

                            if (!user_id.equals(id)) {
                                searchUserList.add(map);
                            }
                        }


                    } else if (p41Result == 0) {
                        p41Message = jsonObject.getString(Key.message);
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

                tvSearchUserTitle.setText(R.string.pinpinbox_2_0_0_title_find_creator);
                setSearchUserData();

                //20171113
                if(rvSearch.getAlpha()==0f){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ViewPropertyAnimator alphaTo1 = rvSearch.animate();
                            alphaTo1.setDuration(400)
                                    .alpha(1)
                                    .start();
                        }
                    }, 200);

                }



            } else if (p41Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p41Message);
            } else if (p41Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }

        }


    }

    private class AlbumListTask extends AsyncTask<Void, Void, Object> {

        private int p41Result = -1;
        private String p41Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSearchAlbumList;

                   /*執行搜尋前全部移除*/
            albumAdapter.notifyItemRangeRemoved(0, currentAlbumList.size());

            if (searchAlbumList.size() > 0) {
                searchAlbumList.clear();
            }

            if (currentAlbumList.size() > 0) {
                for (int i = 0; i < currentAlbumList.size(); i++) {
                    String cover = currentAlbumList.get(i).getCover();
                    Picasso.with(getActivity().getApplicationContext()).invalidate(cover);
                }
                currentAlbumList.clear();
            }


        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P41_Search,
                        SetMapByProtocol.setParam41_search(id, token, "album", strSearch, "0,32"), null);
                MyLog.Set("d", getClass(), "p41strJson(album) =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p41Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p41Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p41Result == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);

                        int minHeight = DensityUtility.dip2px(getActivity().getApplicationContext(), 72);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);

                            ItemAlbum itemAlbum = new ItemAlbum();


                            String album = JsonUtility.GetString(j, ProtocolKey.album);
                            JSONObject jsonAlbum = new JSONObject(album);

                            itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                            itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));

                            String cover = JsonUtility.GetString(jsonAlbum, ProtocolKey.cover);
                            itemAlbum.setCover(cover);

                            try {
                                int width = jsonAlbum.getInt(ProtocolKey.cover_width);
                                int height = jsonAlbum.getInt(ProtocolKey.cover_height);
                                int image_height = StaggeredHeight.setImageHeight(width, height);

                                if (image_height < minHeight) {
                                    image_height = minHeight;
                                }

                                itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                                itemAlbum.setCover_height(image_height);
                                itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));
                                if(width>height){
                                    itemAlbum.setImage_orientation(ItemAlbum.LANDSCAPE);
                                }else if(height>width){
                                    itemAlbum.setImage_orientation(ItemAlbum.PORTRAIT);
                                }else {
                                    itemAlbum.setImage_orientation(0);
                                }

                            } catch (Exception e) {
                                itemAlbum.setCover_hex("");
                                itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                                itemAlbum.setCover_height(PPBApplication.getInstance().getStaggeredWidth());
                                MyLog.Set("e", this.getClass(), "圖片長寬無法讀取");
                            }

                            String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                            JSONObject jsonUsefor = new JSONObject(usefor);
                            itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                            itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                            itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));
                            itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));


                            String user = JsonUtility.GetString(j, ProtocolKey.user);
                            JSONObject jsonUser = new JSONObject(user);
                            itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));

                            searchAlbumList.add(itemAlbum);
                        }


                    } else if (p41Result == 0) {
                        p41Message = jsonObject.getString(ProtocolKey.message);
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

                tvSearchAlbumTitle.setText(R.string.pinpinbox_2_0_0_title_find_album);
                setSearchAlbumData();


            } else if (p41Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p41Message);
            } else if (p41Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }

    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
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
    public void onClick(View v) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (v.getId()) {

            case R.id.clearImg:
                edSearch.setText("");
                break;

            case R.id.scanImg:

                switch (checkPermission(getActivity(), Manifest.permission.CAMERA)) {

                    case SUCCESS:
                        ((Main2Activity) getActivity()).toScan();
                        break;
                    case REFUSE:
                        MPermissions.requestPermissions(FragmentSearch2.this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
                        break;

//                    case REFUSE_NO_ASK:
//                        MPermissions.requestPermissions(FragmentSearch2.this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
//                        PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_open_permission_camera);
//                        SystemUtility.getAppDetailSettingIntent(getActivity());
//                        break;
                }

//                ((Main2Activity)getActivity()).toScan();

                break;


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

//        for (int i = 0; i < grantResults.length; i++) {
//            MyLog.Set("d", getClass(), "grantResults => " + grantResults[i]);
////            PackageManager.PERMISSION_GRANTED = 0; 接受
////            PackageManager.PERMISSION_DENIED = -1; 拒絕 & 拒絕(勾選不再問)
//        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final int SUCCESS = 0;
    private final int REFUSE = -1;

    private int checkPermission(Activity ac, String permission) {

        int doingType = 0;

        if (ActivityCompat.checkSelfPermission(ac, permission) == PackageManager.PERMISSION_GRANTED) {
            //已授權
            doingType = SUCCESS;

            MyLog.Set("d", getClass(), "已授權");

        } else {
            //未授權 判斷是否彈出詢問框 true => 彈出
//            if(ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)){
//
//                MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale = true");

            doingType = REFUSE;

//            }else {
//
//                MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale = false");
//
//                doingType = REFUSE_NO_ASK;
//            }
        }
        return doingType;

    }

    @PermissionGrant(REQUEST_CODE_CAMERA)
    public void requestCameraSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((Main2Activity) getActivity()).toScan();
            }
        }, 500);


    }

    @PermissionDenied(REQUEST_CODE_CAMERA)
    public void requestCameraFailed() {


        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");

            DialogV2Custom d = new DialogV2Custom(getActivity());
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_camera);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    SystemUtility.getAppDetailSettingIntent(getActivity());
                }
            });
            d.show();

        } else {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");

        }


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

//            MyLog.Set("e", getClass(), "目前看不到 FragmentSearch2");


        } else if (isVisible && isResumed()) {
            // 调用Fragment显示的代码段

//            MyLog.Set("e", getClass(), "可看見 FragmentSearch2");

            if (!dowork) {
                init();
                setUserRecycler();
                setAlbumRecycler();
                doGetDefaultData();
                setSearch();
                dowork = true;
            }
        }
    }

    @Override
    public void onPause() {

        if (currentUserList != null && currentAlbumList != null) {

            if (currentUserList.size() > 0) {
                for (int i = 0; i < currentUserList.size(); i++) {
                    String strPicture = (String) currentUserList.get(i).get(Key.picture);
                    Picasso.with(getActivity().getApplicationContext()).invalidate(strPicture);
                }
            }

            if (currentAlbumList.size() > 0) {
                for (int i = 0; i < currentAlbumList.size(); i++) {
                    String cover = currentAlbumList.get(i).getCover();
                    Picasso.with(getActivity().getApplicationContext()).invalidate(cover);
                }
            }

            System.gc();
        }


        super.onPause();
    }

    @Override
    public void onDestroy() {

        if (getDefaultDataTask != null && !getDefaultDataTask.isCancelled()) {
            getDefaultDataTask.cancel(true);
        }
        getDefaultDataTask = null;

        if (userListTask != null && !userListTask.isCancelled()) {
            userListTask.cancel(true);
        }
        userListTask = null;

        if (albumListTask != null && !albumListTask.isCancelled()) {
            albumListTask.cancel(true);
        }
        albumListTask = null;

        cleanPicasso();
        System.gc();


        super.onDestroy();
    }

}

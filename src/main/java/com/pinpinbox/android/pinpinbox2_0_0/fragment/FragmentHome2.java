package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Feature2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Main2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.BannerPageAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCategoryNameAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerHomeAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerHomeUserAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerPopularAlbumAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumCategory;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemHomeBanner;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.AutoPageScrollManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ExLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.JsonParamTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.RequestCodeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;
import com.squareup.picasso.Picasso;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by kevin9594 on 2016/12/17.
 */
public class FragmentHome2 extends Fragment implements View.OnClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener {

    private NoConnect noConnect;
    private LoadingAnimation loading;
    private AutoPageScrollManager autoPageScrollManager;
    private InputMethodManager inputMethodManager;
    private CountDownTimer countDownTimer;

    private FragmentSearch2 fragmentSearch2;

    private AttentionTask attentionTask;
    private MoreDataTask moreDataTask;
    private RefreshTask refreshTask;
    private GetADTask getADTask;

    private JSONArray p20JsonArray;

    private RecyclerHomeAdapter recyclerHomeAdapter;
    private BannerPageAdapter bannerPageAdapter;
    private RecyclerCategoryNameAdapter categoryNameAdapter;
    private RecyclerHomeUserAdapter userAdapter;
    private RecyclerPopularAlbumAdapter popularAlbumAdapter;

    private List<ItemAlbumCategory> itemAlbumCategoryList;
    private List<ItemAlbum> itemAlbumList;
    private List<ItemHomeBanner> itemHomeBannerList;
    private List<ItemUser> itemUserList;
    private List<ItemAlbum> itemAlbumPopularList;
    private List<View> bannerViewList;
    private List<String> albumIdList;

    private String id, token;
    private String p09Message = "";
    private String p20Result, p20Message;
    private String p86MessageUser = "";
    private String p86MessageAlbum = "";
    private String p75Message = "";
    private String p103Result = "";
    private String p103Message = "";
    private String strRank;
    private String strJsonData = "";

    private int p09Result = -1;
    private int p75Result = -1;
    private int p86ResultUser = -1;
    private int p86ResultAlbum = -1;


    private int round; //listview添加前的初始值
    private int count; //listview每次添加的數量
    private int p20total = 0;
    private int reCreateCount = 0;

    private int doingType;
    private int deviceType;
    private static final int DoFirstLogin = 3;
    private static final int DoFastCreate = 4;
    private static final int DoGetAD = 5;
    private static final int DoJoinCooperation = 6;
    private static final int PHONE = 10001;
    private static final int TABLE = 10002;


    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean isDoingMore = false;
    private boolean isDoingRefreshBanner = true;
    private boolean isSearch = false;

    private RecyclerView rvHome, rvCategory, rvRecommendUser, rvPopularAlbum;
    private SmoothProgressBar pbLoadMore, pbRefresh;
    private View viewHeader;
    private ViewPager vpBanner;
    private CircleIndicator indicator;
    private LinearLayout linBanner;
    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private EditText edSearch;
    private ImageView scanImg;


    private LinearLayout linHobby, linFollow;
    private TextView tvNew, tvFollow;
    private View vHobby, vFollow;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (SystemUtility.isTablet(getActivity().getApplicationContext())) {

            //平版
            deviceType = TABLE;

        } else {

            //手機
            deviceType = PHONE;

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_home, container, false);

        initView(v);


        initHeaderView();


        //20171123
        setScrollListener();


        //20171128
        tvShowTime = (TextView) v.findViewById(R.id.tvShowTime);
        if (BuildConfig.FLAVOR.equals("w3_private") || BuildConfig.FLAVOR.equals("www_private")) {
            testSet();
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            tvShowTime.setVisibility(View.GONE);
        }


        return v;
    }


    private void initView(View v) {

        pbRefresh = (SmoothProgressBar) v.findViewById(R.id.pbRefresh);
        rvHome = (RecyclerView) v.findViewById(R.id.rvHome);
        pbLoadMore = (SmoothProgressBar) v.findViewById(R.id.pbLoadMore);
        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) v.findViewById(R.id.pinPinBoxRefreshLayout);
        edSearch = (EditText) v.findViewById(R.id.edSearch);
        scanImg = (ImageView) v.findViewById(R.id.scanImg);

        pbLoadMore.progressiveStop();
        pbRefresh.progressiveStop();

        rvHome.addItemDecoration(new SpacesItemDecoration(16));
        rvHome.setItemAnimator(new DefaultItemAnimator());
        rvHome.addOnScrollListener(mOnScrollListener);

        mOnScrollListener.setvActionBar((LinearLayout) v.findViewById(R.id.linActionBar));

        pinPinBoxRefreshLayout.setOnPullRefreshListener(this);
        pinPinBoxRefreshLayout.setRefreshView(v.findViewById(R.id.vRefreshAnim), pbRefresh);

        scanImg.setOnClickListener(this);

    }


    private void initHeaderView() {

        viewHeader = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.header_2_0_0_home, null);
        vpBanner = (ViewPager) viewHeader.findViewById(R.id.vpBanner);
        indicator = (CircleIndicator) viewHeader.findViewById(R.id.indicator);
        linBanner = (LinearLayout) viewHeader.findViewById(R.id.linBanner);
        linHobby = (LinearLayout) viewHeader.findViewById(R.id.linHobby);
        linFollow = (LinearLayout) viewHeader.findViewById(R.id.linFollow);
        rvCategory = (RecyclerView) viewHeader.findViewById(R.id.rvCategory);
        rvRecommendUser = (RecyclerView) viewHeader.findViewById(R.id.rvRecommendUser);
        rvPopularAlbum = (RecyclerView) viewHeader.findViewById(R.id.rvPopularAlbum);
        tvNew = (TextView) viewHeader.findViewById(R.id.tvNew);
        tvFollow = (TextView) viewHeader.findViewById(R.id.tvFollow);
        vHobby = viewHeader.findViewById(R.id.vHobby);
        vFollow = viewHeader.findViewById(R.id.vFollow);

        linHobby.setOnClickListener(this);
        linFollow.setOnClickListener(this);

        TextUtility.setBold((TextView) viewHeader.findViewById(R.id.tvNewsFeed), true);
        TextUtility.setBold((TextView) viewHeader.findViewById(R.id.tvExplore), true);
        TextUtility.setBold((TextView) viewHeader.findViewById(R.id.tvRecommend), true);
        TextUtility.setBold((TextView) viewHeader.findViewById(R.id.tvPopularity), true);

    }


    /*test version*/
    private TextView tvShowTime;

    public void hideShowTime(boolean b) {

        if (b) {
            tvShowTime.setVisibility(View.VISIBLE);
        } else {
            tvShowTime.setVisibility(View.GONE);
        }
    }

    private void testSet() {
        tvShowTime.setVisibility(View.VISIBLE);
        tvShowTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recyclerHomeAdapter != null) {

                    if (!recyclerHomeAdapter.isShowTime()) {
                        recyclerHomeAdapter.setShowTime(true);
                    } else {
                        recyclerHomeAdapter.setShowTime(false);
                    }

                    recyclerHomeAdapter.notifyDataSetChanged();

                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        setSearch();
        setRecycler();
        setCategoryRecycler();
        setUserRecycler();
        setPopularRecycler();
        doAttention();

    }

    private void init() {

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        loading = ((Main2Activity) getActivity()).getLoading();

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
        strRank = "latest"; //default latest

        round = 0;
        count = 16;

        itemAlbumList = new ArrayList<>();
        itemHomeBannerList = new ArrayList<>();
        albumIdList = new ArrayList<>();
        itemAlbumCategoryList = new ArrayList<>();
        itemUserList = new ArrayList<>();
        itemAlbumPopularList = new ArrayList<>();


    }

    private void setSearch() {


        edSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    MyLog.Set("e", FragmentHome2.this.getClass(), "監聽輸入框");

                    showSearch();

                } else {
                    // 此处为失去焦点时的处理内容
                    MyLog.Set("e", FragmentHome2.this.getClass(), "取消監聽");


                }


            }
        });


        countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                countDownTimer.cancel();

                if (fragmentSearch2 != null && fragmentSearch2.isAdded()) {

                    fragmentSearch2.setOnFinish();

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

                    if (fragmentSearch2 != null && fragmentSearch2.isAdded()) {

                        fragmentSearch2.setOnFinish();

                    }


                } else {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer.start();

                        MyLog.Set("d", FragmentSearch2.class, "重新倒數");
                    }


                }


            }
        });


    }

    private void showSearch() {
        if (fragmentSearch2 == null) {
            fragmentSearch2 = new FragmentSearch2();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.frameSearch, fragmentSearch2).commit();
        } else {

            if (fragmentSearch2.isHidden()) {
                getActivity().getSupportFragmentManager().beginTransaction().show(fragmentSearch2).commit();
            }

        }

        isSearch = true;
        scanImg.setImageResource(R.drawable.ic200_cancel_dark);

    }

    private void hideSearch() {
        if (fragmentSearch2 != null) {
            getActivity().getSupportFragmentManager().beginTransaction().hide(fragmentSearch2).commit();
        }

        isSearch = false;
        scanImg.setImageResource(R.drawable.ic200_scancamera_dark);
    }

    public void scrollToTop() {

        ExStaggeredGridLayoutManager linearLayoutManager = (ExStaggeredGridLayoutManager) rvHome.getLayoutManager();

        if (linearLayoutManager != null && rvHome != null) {

            try {

                int[] firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPositions(null);

                if (firstVisibleItemPosition[0] > 10) {
                    rvHome.scrollToPosition(10);
                    MyLog.Set("d", getClass(), "先移動至第10項目");
                }
                rvHome.smoothScrollToPosition(0);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void setRecycler() {

        recyclerHomeAdapter = new RecyclerHomeAdapter(getActivity(), itemAlbumList);
        rvHome.setAdapter(recyclerHomeAdapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(recyclerHomeAdapter);
        rvHome.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = null;

        if (deviceType == TABLE) {

            //平版
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        } else {

            //手機
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }

        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvHome.getAdapter(), manager.getSpanCount()));
        rvHome.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvHome, viewHeader);

        recyclerHomeAdapter.setOnRecyclerViewListener(new RecyclerHomeAdapter.OnRecyclerViewListener() {

            @Override
            public void onItemClick(int position, View v) {

                Map<String, String> params = new HashMap<String, String>();
                params.put("album name", itemAlbumList.get(position).getName());
                params.put("album id: ", itemAlbumList.get(position).getAlbum_id());

                FlurryUtil.onEventUseMap(FlurryKey.home_click_albuminfo, params);

                ActivityIntent.toAlbumInfo(
                        getActivity(),
                        true,
                        itemAlbumList.get(position).getAlbum_id(),
                        itemAlbumList.get(position).getCover(),
                        itemAlbumList.get(position).getImage_orientation(),
                        v.findViewById(R.id.coverImg)
                );


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


    }

    private void setCategoryRecycler() {

        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvCategory.setLayoutManager(layoutManager);
        categoryNameAdapter = new RecyclerCategoryNameAdapter(getActivity(), itemAlbumCategoryList);
        rvCategory.setAdapter(categoryNameAdapter);

        categoryNameAdapter.setOnRecyclerViewListener(new RecyclerCategoryNameAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                Bundle bundle = new Bundle();

                if (itemAlbumCategoryList.get(position).getCategoryarea_id() == JsonParamTypeClass.NULLCATEGORYID) {

                    bundle.putString(Key.jsonData, strJsonData);

                }

                bundle.putInt(Key.categoryarea_id, itemAlbumCategoryList.get(position).getCategoryarea_id());

                startActivity(new Intent(getActivity(), Feature2Activity.class).putExtras(bundle));

                ActivityAnim.StartAnim(getActivity());

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }

        });


    }

    private void setUserRecycler() {

        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRecommendUser.setLayoutManager(layoutManager);
        userAdapter = new RecyclerHomeUserAdapter(getActivity(), itemUserList);
        rvRecommendUser.setAdapter(userAdapter);

        userAdapter.setOnRecyclerViewListener(new RecyclerHomeUserAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                if (itemUserList.get(position).getUser_id().equals(id)) {

                    ((Main2Activity) getActivity()).toMePage(false);

                    return;
                }


                ActivityIntent.toUser(
                        getActivity(),
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

    }

    private void setPopularRecycler() {

        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPopularAlbum.setLayoutManager(layoutManager);
        popularAlbumAdapter = new RecyclerPopularAlbumAdapter(getActivity(), itemAlbumPopularList);
        rvPopularAlbum.setAdapter(popularAlbumAdapter);

        popularAlbumAdapter.setOnRecyclerViewListener(new RecyclerPopularAlbumAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                ActivityIntent.toAlbumInfo(
                        getActivity(),
                        true,
                        itemAlbumPopularList.get(position).getAlbum_id(),
                        itemAlbumPopularList.get(position).getCover(),
                        itemAlbumPopularList.get(position).getImage_orientation(),
                        v.findViewById(R.id.coverImg)
                );

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


    }


    private void getUpdateList(String limit) {

        Map<String, String> data = new HashMap<String, String>();
        data.put("id", id);
        data.put("token", token);
        data.put("limit", limit);
        String sign = IndexSheet.encodePPB(data);

        final Map<String, String> sendData = new HashMap<String, String>();
        sendData.put("id", id);
        sendData.put("token", token);
        sendData.put("rank", strRank);
        sendData.put("limit", limit);
        sendData.put("sign", sign);

        MyLog.Set("d", getClass(), "傳遞的rank => " + strRank);

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P20_GetUpdateList, sendData, null);//20
            MyLog.Set("d", getClass(), "p20strJson => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p20Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p20Result = jsonObject.getString(Key.result);
                if (p20Result.equals("1")) {
                    String jsonData = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    Logger.json(jsonData);

                    p20JsonArray = new JSONArray(jsonData);

                    int jsonArrayCount = p20JsonArray.length();

                    int minHeight = DensityUtility.dip2px(getActivity().getApplicationContext(), 72);

                    for (int i = 0; i < jsonArrayCount; i++) {

                        JSONObject object = (JSONObject) p20JsonArray.get(i);

                        String album = JsonUtility.GetString(object, ProtocolKey.album);
                        String albumstatistics = JsonUtility.GetString(object, ProtocolKey.albumstatistics);
                        String notice = JsonUtility.GetString(object, ProtocolKey.notice);
                        String user = JsonUtility.GetString(object, ProtocolKey.user);

                        JSONObject jsonAlbum = new JSONObject(album);
                        JSONObject jsonAlbumstatistics = new JSONObject(albumstatistics);
                        JSONObject jsonNotice = new JSONObject(notice);
                        JSONObject jsonUser = new JSONObject(user);


                        /*20171031************************************************/
                        boolean isAlbumExist = false;

                        String album_id = JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id);

                        if (albumIdList.size() > 0) {

                            for (int j = 0; j < albumIdList.size(); j++) {

                                if (album_id.equals(albumIdList.get(j))) {

                                    MyLog.Set("e", FragmentHome2.class, "重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了");

                                    isAlbumExist = true;
                                    break;
                                }

                            }

                        }

                        if (isAlbumExist) continue;

                        albumIdList.add(album_id);
                        /* ********************************************************/

                        /*album*/
                        ItemAlbum itemAlbum = new ItemAlbum();
                        itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                        itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));
                        itemAlbum.setLocation(JsonUtility.GetString(jsonAlbum, ProtocolKey.location));
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


                            if (width > height) {
                                itemAlbum.setImage_orientation(ItemAlbum.LANDSCAPE);
                            } else if (height > width) {
                                itemAlbum.setImage_orientation(ItemAlbum.PORTRAIT);
                            } else {
                                itemAlbum.setImage_orientation(0);
                            }


                        } catch (Exception e) {

                            itemAlbum.setCover_hex("");
//                            itemAlbum.setColor(0);
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


                        /*albumstatistics*/
                        itemAlbum.setLikes(JsonUtility.GetInt(jsonAlbumstatistics, ProtocolKey.likes));
                        itemAlbum.setViewed(JsonUtility.GetInt(jsonAlbumstatistics, ProtocolKey.viewed));

                        /*notice*/
                        String strTime = "";
                        strTime = JsonUtility.GetString(jsonNotice, ProtocolKey.difftime);
                        String y = strTime.substring(0, strTime.indexOf(","));
                        int year = Integer.valueOf(y);
                        String noYear = strTime.substring(strTime.indexOf(",") + 1);

                        String m = noYear.substring(0, noYear.indexOf(","));
                        int month = Integer.valueOf(m);
                        String noMonth = noYear.substring(noYear.indexOf(",") + 1);

                        String d = noMonth.substring(0, noMonth.indexOf(","));
                        int day = Integer.valueOf(d);
                        String noDay = noMonth.substring(noMonth.indexOf(",") + 1);

                        String h = noDay.substring(0, noDay.indexOf(","));
                        int hour = Integer.valueOf(h);

                        String minute = noDay.substring(noDay.indexOf(",") + 1);

                        if (year > 0) {

                            if (year < 2) {
                                strTime = year + getResources().getString(R.string.pinpinbox_2_0_0_time_before_year_single);
                            } else {
                                strTime = year + getResources().getString(R.string.pinpinbox_2_0_0_time_before_year);
                            }


                        } else {
                            if (month > 0) {

                                if (month < 2) {
                                    strTime = month + getResources().getString(R.string.pinpinbox_2_0_0_time_before_month_single);
                                } else {
                                    strTime = month + getResources().getString(R.string.pinpinbox_2_0_0_time_before_month);
                                }

                            } else {
                                if (day > 0) {

                                    if (day < 2) {
                                        strTime = day + getResources().getString(R.string.pinpinbox_2_0_0_time_before_day_single);
                                    } else {
                                        strTime = day + getResources().getString(R.string.pinpinbox_2_0_0_time_before_day);
                                    }


                                } else {
                                    if (hour > 0) {

                                        if (hour < 2) {
                                            strTime = hour + getResources().getString(R.string.pinpinbox_2_0_0_time_before_hour_single);
                                        } else {
                                            strTime = hour + getResources().getString(R.string.pinpinbox_2_0_0_time_before_hour);
                                        }

                                    } else {
                                        if (minute.equals("0")) {

                                            strTime = getResources().getString(R.string.pinpinbox_2_0_0_time_before_just_now);

                                        } else {

                                            if (minute.equals("1")) {
                                                strTime = minute + getResources().getString(R.string.pinpinbox_2_0_0_time_before_minute_single);
                                            } else {
                                                strTime = minute + getResources().getString(R.string.pinpinbox_2_0_0_time_before_minute);
                                            }


                                        }
                                    }
                                }
                            }
                        }

                        itemAlbum.setDifftime(strTime);


                        /*user*/
                        itemAlbum.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));
                        itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                        itemAlbum.setUser_picture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));


//                        int userid = JsonUtility.GetInt(jsonUser, ProtocolKey.user_id);

//                                if((userid + "").equals("189")){

//                                }

                        itemAlbumList.add(itemAlbum);


                    }

                    p20total = itemAlbumList.size();

                    MyLog.Set("d", getClass(), "共" + p20total + "本");

                } else if (p20Result.equals("0")) {
                    p20Message = jsonObject.getString(Key.message);
                } else {
                    p20Result = "";
                }

            } catch (Exception e) {
                p20Result = "";
                e.printStackTrace();
            }

        }

    }

    private void getBannerList() {

        if (itemHomeBannerList != null && itemHomeBannerList.size() > 0) {


            for (int i = 0; i < itemHomeBannerList.size(); i++) {
                String image = itemHomeBannerList.get(i).getImage();
                Picasso.with(getActivity().getApplicationContext()).invalidate(image);
            }

            itemHomeBannerList.clear();
        }


        String strJson = "";
        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P75_GetAdList, SetMapByProtocol.setParam75_getadlist(id, token, "1"), null);
            Logger.json(strJson);
        } catch (SocketTimeoutException timeout) {
            p75Result = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p75Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                if (p75Result == 1) {
                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONArray jsonArray = new JSONArray(data);

                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = (JSONObject) jsonArray.get(i);
                            ItemHomeBanner itemHomeBanner = new ItemHomeBanner();

                            try {
                                String event = JsonUtility.GetString(object, ProtocolKey.event);
                                if (event != null) {
                                    JSONObject jsonEvent = new JSONObject(event);
                                    itemHomeBanner.setEvent_id(JsonUtility.GetString(jsonEvent, ProtocolKey.event_id));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                String ad = JsonUtility.GetString(object, ProtocolKey.ad);
                                if (ad != null) {
                                    JSONObject jsonAd = new JSONObject(ad);
                                    itemHomeBanner.setImage(JsonUtility.GetString(jsonAd, ProtocolKey.image));
                                    itemHomeBanner.setUrl(JsonUtility.GetString(jsonAd, ProtocolKey.url));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                                /*2016.08.19新增album template user Object*/
                            try {
                                String album = JsonUtility.GetString(object, ProtocolKey.album);
                                if (album != null) {
                                    JSONObject jsonAlbum = new JSONObject(album);
                                    itemHomeBanner.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                String template = JsonUtility.GetString(object, ProtocolKey.template);
                                if (template != null) {
                                    JSONObject jsonTemplate = new JSONObject(template);
                                    itemHomeBanner.setTemplate_id(JsonUtility.GetString(jsonTemplate, ProtocolKey.template_id));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                String user = JsonUtility.GetString(object, ProtocolKey.user);
                                if (user != null) {
                                    JSONObject jsonUser = new JSONObject(user);
                                    itemHomeBanner.setUser_id(JsonUtility.GetString(jsonUser, ProtocolKey.user_id));

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            itemHomeBannerList.add(itemHomeBanner);

                        }
                    }
                } else if (p75Result == 0) {
                    p75Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void getThemeList() {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, Url.P103_GetThemeArea,
                    SetMapByProtocol.setParam103_getthemearea(id, token),
                    null);
            MyLog.Set("json", getClass(), strJson);
        } catch (SocketTimeoutException timeout) {
            p103Result = ResultType.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {

                JSONObject jsonObject = new JSONObject(strJson);
                p103Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);

                if (p103Result.equals(ResultType.SYSTEM_OK)) {

                    /*Feature需要獲取name*/
                    strJsonData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONObject jsonData = new JSONObject(strJsonData);

                    /*添加theme至第一項*/
                    String themearea = JsonUtility.GetString(jsonData, ProtocolKey.themearea);

                    JSONObject jsonThemeArea = new JSONObject(themearea);

                    ItemAlbumCategory itemAlbumCategory = new ItemAlbumCategory();
                    itemAlbumCategory.setCategoryarea_id(JsonParamTypeClass.NULLCATEGORYID);
                    itemAlbumCategory.setName(JsonUtility.GetString(jsonThemeArea, ProtocolKey.name));
                    itemAlbumCategory.setColorhex(JsonUtility.GetString(jsonThemeArea, ProtocolKey.colorhex));
                    itemAlbumCategory.setImage_360x360(JsonUtility.GetString(jsonThemeArea, ProtocolKey.image_360x360));

                    itemAlbumCategoryList.add(itemAlbumCategory);


                }

                if (!p103Result.equals(ResultType.SYSTEM_OK)) {
                    p103Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void getCategoryList() {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P09_RetrieveCategoryList,
                    SetMapByProtocol.setParam09_retrievecatgeorylist(id, token),
                    null);
            MyLog.Set("json", getClass(), strJson);
        } catch (SocketTimeoutException timeout) {
            p09Result = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p09Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                if (p09Result == 1) {

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    JSONArray jsonArray = new JSONArray(data);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = (JSONObject) jsonArray.get(i);

                        String categoryarea = JsonUtility.GetString(object, ProtocolKey.categoryarea);
                        JSONObject jsonCategoryarea = new JSONObject(categoryarea);

                        ItemAlbumCategory itemAlbumCategory = new ItemAlbumCategory();
                        itemAlbumCategory.setCategoryarea_id(JsonUtility.GetInt(jsonCategoryarea, ProtocolKey.categoryarea_id));
                        itemAlbumCategory.setName(JsonUtility.GetString(jsonCategoryarea, ProtocolKey.name));
                        itemAlbumCategory.setColorhex(JsonUtility.GetString(jsonCategoryarea, ProtocolKey.colorhex));
                        itemAlbumCategory.setImage_360x360(JsonUtility.GetString(jsonCategoryarea, ProtocolKey.image_360x360));

                        itemAlbumCategoryList.add(itemAlbumCategory);
                    }


                } else if (p09Result == 0) {
                    p09Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void getUserList() {


        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P86_GetRecommendedList,
                    SetMapByProtocol.setParam86_getrecommendedlist(id, token, Key.user, "0,16"), null);
            MyLog.Set("d", getClass(), "p86strJson(user) =>" + strJson);
        } catch (SocketTimeoutException timeout) {
            p86ResultUser = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p86ResultUser = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                if (p86ResultUser == 1) {

                    String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONArray jsonArray = new JSONArray(jdata);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = (JSONObject) jsonArray.get(i);


                        String user = JsonUtility.GetString(j, ProtocolKey.user);
                        JSONObject object = new JSONObject(user);


                        ItemUser itemUser = new ItemUser();
                        itemUser.setName(JsonUtility.GetString(object, ProtocolKey.name));
                        itemUser.setPicture(JsonUtility.GetString(object, ProtocolKey.picture));
                        itemUser.setUser_id(JsonUtility.GetString(object, ProtocolKey.user_id));
                        itemUserList.add(itemUser);


                    }


                } else if (p86ResultUser == 0) {
                    p86MessageUser = jsonObject.getString(Key.message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void getPopularList() {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P86_GetRecommendedList,
                    SetMapByProtocol.setParam86_getrecommendedlist(id, token, Key.album, "0,8"), null);
            MyLog.Set("d", getClass(), "p86strJson(album) => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p86ResultAlbum = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p86ResultAlbum = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                if (p86ResultAlbum == 1) {

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

                            itemAlbum.setCover_width(width);
                            itemAlbum.setCover_height(height);
                            itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));

                            if (width > height) {
                                itemAlbum.setImage_orientation(ItemAlbum.LANDSCAPE);
                            } else if (height > width) {
                                itemAlbum.setImage_orientation(ItemAlbum.PORTRAIT);
                            } else {
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

                        itemAlbumPopularList.add(itemAlbum);
                    }

                } else if (p86ResultAlbum == 0) {
                    p86MessageAlbum = JsonUtility.GetString(jsonObject, Key.message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setBanner() {

        bannerViewList = new ArrayList<>();


        int bannerWidth = ScreenUtils.getScreenWidth();

        int bannerHeight = (bannerWidth * 540) / 960;

//        /*banner width = 960 , height = 540*/
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(bannerWidth, bannerHeight);

        for (int i = 0; i < itemHomeBannerList.size(); i++) {
            View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.page_2_0_0_banner_image, null);

            bannerViewList.add(view);

        }

        vpBanner.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), bannerHeight));

        if (bannerPageAdapter != null) {
            bannerPageAdapter = null;
        }

        bannerPageAdapter = new BannerPageAdapter(getActivity(), bannerViewList, itemHomeBannerList, this);
        vpBanner.setAdapter(bannerPageAdapter);
        indicator.setViewPager(vpBanner);


        if (itemHomeBannerList.size() > 1) {
            autoPageScrollManager = new AutoPageScrollManager(vpBanner, 5, itemHomeBannerList.size());
        }

        Glide.get(getActivity().getApplicationContext()).clearMemory();


        //判斷是否是活動
        String event_id = itemHomeBannerList.get(0).getEvent_id();


        if (event_id != null && !event_id.equals("") && !event_id.equals("null")) {

            /*當前為活動*/

            //判斷image是否曾經顯示過
            boolean isUrlExist = false;
            String bannerList = PPBApplication.getInstance().getData().getString(Key.oldbannerUrlList, "[]");
            try {
                JSONArray bannerArray = new JSONArray(bannerList);
                for (int i = 0; i < bannerArray.length(); i++) {
                    String strOldImageUrl = (String) bannerArray.get(i);
                    if (strOldImageUrl.equals(itemHomeBannerList.get(0).getImage())) {
                        isUrlExist = true;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isUrlExist) {
                if (autoPageScrollManager != null) {

                    autoPageScrollManager.post();
                }
            }

        } else {


            /*不為活動 直接自動播放*/
            if (autoPageScrollManager != null) {

                autoPageScrollManager.post();
            }


        }


    }

    private void setScrollListener() {

        rvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                MyLog.Set("e", FragmentHome2.class, "newState => " + newState);


                switch (newState) {

                    case 1:
                        pinPinBoxRefreshLayout.setPullRefresh(false);
                        break;

                    case 2:
                        pinPinBoxRefreshLayout.setPullRefresh(true);
                        break;


                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        vpBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1://正在滑動

                        if (autoPageScrollManager != null) {
                            autoPageScrollManager.removeRunnable();
                            pinPinBoxRefreshLayout.setPullRefresh(false);
                        }

                        break;

                    case 2://滑動完畢

                        if (autoPageScrollManager != null) {
                            pinPinBoxRefreshLayout.setPullRefresh(true);
                            autoPageScrollManager.post();
                        }

                        break;

                }

            }
        });

    }

    private void changeRank(String rank) {

        try {
            if (rank.equals("latest")) {

                tvNew.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                vHobby.setVisibility(View.VISIBLE);
                TextUtility.setBold(tvNew, true);

                tvFollow.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                vFollow.setVisibility(View.INVISIBLE);
                TextUtility.setBold(tvFollow, false);


            } else if (rank.equals("follow")) {

                tvNew.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                vHobby.setVisibility(View.INVISIBLE);
                TextUtility.setBold(tvNew, false);

                tvFollow.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                vFollow.setVisibility(View.VISIBLE);
                TextUtility.setBold(tvFollow, true);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cleanPicasso() {

        if (itemAlbumList != null && itemAlbumList.size() > 0) {

            for (int i = 0; i < itemAlbumList.size(); i++) {

                String picture = itemAlbumList.get(i).getUser_picture();
                String path_albumcover = itemAlbumList.get(i).getCover();

                if (picture != null && !picture.equals("")) {
                    Picasso.with(getActivity().getApplicationContext()).invalidate(picture);
                }


                if (path_albumcover != null && !path_albumcover.equals("")) {
                    Picasso.with(getActivity().getApplicationContext()).invalidate(path_albumcover);
                }

            }

        }


        if (itemHomeBannerList != null && itemHomeBannerList.size() > 0) {

            for (int i = 0; i < itemHomeBannerList.size(); i++) {

                String image = itemHomeBannerList.get(i).getImage();
                if (image != null && !image.equals("")) {
                    Picasso.with(getActivity().getApplicationContext()).invalidate(image);
                }
            }

        }


        if (itemAlbumPopularList != null && itemAlbumPopularList.size() > 0) {

            for (int i = 0; i < itemAlbumPopularList.size(); i++) {

                String cover = itemAlbumPopularList.get(i).getCover();
                if (cover != null && !cover.equals("")) {
                    Picasso.with(getActivity().getApplicationContext()).invalidate(cover);
                }
            }

        }


        if (itemUserList != null && itemUserList.size() > 0) {

            for (int i = 0; i < itemUserList.size(); i++) {

                String picture = itemUserList.get(i).getPicture();
                if (picture != null && !picture.equals("")) {
                    Picasso.with(getActivity().getApplicationContext()).invalidate(picture);
                }
            }

        }


        MyLog.Set("d", getClass(), "cleanPicasso");

        System.gc();

    }

    public SmoothProgressBar getPbRefresh() {
        return this.pbRefresh;
    }

    public EditText getEdSearch() {
        return this.edSearch;
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doAttention();
                        break;

                    case DoGetAD:
                        doGetAD();
                        break;

                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;

                    case DoingTypeClass.DoRefresh:
                        doRefresh();
                        break;

//                    case DoFastCreate:
//                        doFastCreate();
//                        break;
//
//                    case DoFirstLogin:
//                        doFirstLogin();
//                        break;
//
//                    case DoJoinCooperation:
//                        doJoinCooperation();
//                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);

    }

    private void doAttention() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((Main2Activity) getActivity()).setNoConnect();
            return;
        }
        attentionTask = new AttentionTask();
        attentionTask.execute();
    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((Main2Activity) getActivity()).setNoConnect();
            return;
        }
        moreDataTask = new MoreDataTask();
        moreDataTask.execute();
    }

    private void doRefresh() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((Main2Activity) getActivity()).setNoConnect();
            return;
        }

        if (attentionTask != null && !attentionTask.isCancelled()) {
            attentionTask.cancel(true);
            attentionTask = null;
        }


        if (p20total > 0) {
            cleanPicasso();
        }

        albumIdList.clear();
        itemAlbumList.clear();
        itemAlbumCategoryList.clear();
        itemUserList.clear();
        itemAlbumPopularList.clear();

        p20total = 0;
        round = 0;

        recyclerHomeAdapter.notifyDataSetChanged();
        categoryNameAdapter.notifyDataSetChanged();
        userAdapter.notifyDataSetChanged();
        popularAlbumAdapter.notifyDataSetChanged();

        if (autoPageScrollManager != null) {
            autoPageScrollManager.recycler();
            autoPageScrollManager = null;
        }


        refreshTask = new RefreshTask();
        refreshTask.execute();

    }

    private void doGetAD() {
        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((Main2Activity) getActivity()).setNoConnect();
            return;
        }
        getADTask = new GetADTask();
        getADTask.execute();


    }

    public class AttentionTask extends AsyncTask<Void, Void, Object> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            round = 0;
            sizeMax = false;
            loading.show();

        }

        @Override
        protected Object doInBackground(Void... params) {


            getThemeList();

            getCategoryList();

            getUserList();

            getPopularList();

            getUpdateList(round + "," + count);


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);


            try {

                if (p09Result == 1) {

                    categoryNameAdapter.notifyDataSetChanged();

                } else if (p09Result == 0) {

                    loading.dismiss();

                    DialogV2Custom.BuildError(getActivity(), p09Message);
                    return;
                } else if (p09Result == Key.TIMEOUT) {
                    connectInstability();
                    loading.dismiss();
                    return;
                } else {
                    DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (p20Result.equals("1")) {


                    doGetAD();

                } else if (p20Result.equals("0")) {
                    DialogV2Custom.BuildError(getActivity(), p20Message);

                    loading.dismiss();

                } else if (p20Result.equals(Key.timeout)) {
                    connectInstability();
                    loading.dismiss();
                } else {
                    DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                    loading.dismiss();
                }

            } catch (Exception e) {
                loading.dismiss();
                /*2016.11.24 new add*/
                if (reCreateCount == 0) {
                    doAttention();
                }
                reCreateCount++;
            }

        }
    }

    public class MoreDataTask extends AsyncTask<Void, Void, Object> {

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
            getUpdateList(round + "," + count);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            isDoingMore = false;

//            canScroll();
            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();

            if (p20Result.equals("1")) {

                if (p20JsonArray.length() == 0) {
                    MyLog.Set("d", FragmentHome2.class, "已達最大值");
                    sizeMax = true; // 已達最大值
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }
                    return;
                }

                recyclerHomeAdapter.notifyItemRangeInserted(p20total, count);

                //添加下一次讀取範圍
                round = round + count;

            } else if (p20Result.equals("0")) {
                DialogV2Custom.BuildError(getActivity(), p20Message);
            } else if (p20Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }
        }


    }

    public class RefreshTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoRefresh;

        }

        @Override
        protected Object doInBackground(Void... params) {

            getThemeList();

            getCategoryList();

            getUserList();

            getPopularList();

            getUpdateList(round + "," + count);

            if (isDoingRefreshBanner) {
                getBannerList();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            sizeMax = false;

            pinPinBoxRefreshLayout.setRefreshing(false);


            try {

                if (p09Result == 1) {
                    categoryNameAdapter.notifyDataSetChanged();
                } else if (p09Result == 0) {
                    DialogV2Custom.BuildError(getActivity(), p09Message);
                    return;
                } else if (p09Result == Key.TIMEOUT) {
                    connectInstability();
                    return;
                } else {
                    DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (p86ResultUser == 1) {
                    userAdapter.notifyDataSetChanged();
                } else if (p86ResultUser == 0) {
                    DialogV2Custom.BuildError(getActivity(), p86MessageUser);
                    return;
                } else if (p86ResultUser == Key.TIMEOUT) {
                    connectInstability();
                    return;
                } else {
                    DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                if (p86ResultAlbum == 1) {
                    popularAlbumAdapter.notifyDataSetChanged();
                } else if (p86ResultAlbum == 0) {
                    DialogV2Custom.BuildError(getActivity(), p86MessageAlbum);
                    return;
                } else if (p86ResultAlbum == Key.TIMEOUT) {
                    connectInstability();
                    return;
                } else {
                    DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            if (p20Result.equals("1")) {
                recyclerHomeAdapter.notifyDataSetChanged();
                round = round + count;

                if (isDoingRefreshBanner) {

                    if (p75Result == 1) {
                        if (itemHomeBannerList.size() == 0) {
                            linBanner.setVisibility(View.GONE);
                        } else {
                            setBanner();
                        }
                    } else if (p75Result == 0) {
                        DialogV2Custom.BuildError(getActivity(), p75Message);
                    }

                } else {
                    isDoingRefreshBanner = true;
                }


            } else if (p20Result.equals("0")) {

                DialogV2Custom.BuildError(getActivity(), p20Message);

            } else if (p20Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }
        }
    }

    public class GetADTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetAD;

        }

        @Override
        protected Object doInBackground(Void... params) {

            getBannerList();

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            ViewControl.AlphaTo1(pinPinBoxRefreshLayout);


            if (p75Result == 1) {
                if (itemHomeBannerList.size() == 0) {
                    linBanner.setVisibility(View.GONE);
                } else {

                    setBanner();

                }


                recyclerHomeAdapter.notifyDataSetChanged();

                userAdapter.notifyDataSetChanged();

                popularAlbumAdapter.notifyDataSetChanged();

                round = round + count;


            } else if (p75Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p75Message);
            } else if (p75Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), this.getClass().getSimpleName());
            }
        }
    }


    @Override
    public void onClick(View view) {


        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }


        switch (view.getId()) {


            case R.id.linHobby:

                strRank = "latest";

                changeRank(strRank);

                isDoingRefreshBanner = false;

                doRefresh();

                break;

            case R.id.linFollow:

                strRank = "follow";

                changeRank(strRank);

                isDoingRefreshBanner = false;

                FlurryUtil.onEvent(FlurryKey.home_click_follow);

                doRefresh();

                break;


            case R.id.scanImg:


                if (isSearch) {
                    edSearch.setText("");
                    edSearch.clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                    hideSearch();
                } else {

                    switch (checkPermission(getActivity(), Manifest.permission.CAMERA)) {

                        case SUCCESS:
                            ((Main2Activity) getActivity()).toScan();
                            break;
                        case REFUSE:
                            MPermissions.requestPermissions(FragmentHome2.this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
                            break;

                    }


                }


                break;


        }

    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        if (!isVisibleToUser && isResumed()) {
//            MyLog.Set("e", getClass(), "isVisibleToUser => " + false);
//            resetBannerAutoScroll();
//
//        } else if (isVisibleToUser && isResumed() && ((Main2Activity) getActivity()).getPage() == 0) {
//            MyLog.Set("e", getClass(), "isVisibleToUser => " + true);
//            beginBannerAutoScroll();
//
//        }
//
//    }
//
//
//    private void resetBannerAutoScroll() {
//
//        MyLog.Set("e", getClass(), "resetBannerAutoScroll");
//
//        if (autoPageScrollManager != null) {
//            autoPageScrollManager.removeRunnable();
//            vpBanner.setCurrentItem(0);
//            vpBanner.setAdapter(null);
//
//        }
//        Glide.get(getActivity().getApplicationContext()).clearMemory();
//
//
//    }
//
//
//    private void beginBannerAutoScroll() {
//
//        MyLog.Set("e", getClass(), "beginBannerAutoScroll");
//
//        if (autoPageScrollManager != null) {
//            vpBanner.setAdapter(bannerPageAdapter);
//            autoPageScrollManager.post();
//        }
//    }


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {

                if (isDoingMore) {
                    MyLog.Set("e", FragmentHome2.class, "正在讀取更多項目");
                    return;
                }


                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

                MyLog.Set("e", FragmentHome2.class, "sizeMax");
            }
        }


    };

    @Override
    public void onRefresh() {
        doRefresh();
    }

    @Override
    public void onPullDistance(int distance) {

//        MyLog.Set("d", getClass(), "onPullDistance => " + distance);
//
//
//        floatScaleX = (float)distance / 100;
//
//        vRefreshAnim.setScaleX(floatScaleX);
//        if (distance < 40) {
//            avLoading.setScaleX(0);
//            avLoading.setScaleX(0);
//        } else {
//
//            if (distance > 40) {
//                scale = (distance - 40) / 80f;
//                if (scale <= 1) {
//                    avLoading.setScaleX(scale);
//                    avLoading.setScaleY(scale);
//
//                }
//
//            }
//        }
    }

    @Override
    public void onPullEnable(boolean enable) {

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

                if (deviceType == TABLE) {

                    if (spanIndex == 0) {
                        outRect.left = mSpace;
                        outRect.right = 0;
                    } else if (spanIndex == 1) {//if you just have 2 span . Or you can use (staggeredGridLayoutManager.getSpanCount()-1) as last span
                        outRect.left = mSpace;
                        outRect.right = mSpace;
                    } else {
                        outRect.left = 0;
                        outRect.right = mSpace;
                    }

                } else if (deviceType == PHONE) {

                    if (spanIndex == 0) {
                        outRect.left = mSpace;
                        outRect.right = 0;
                    } else {//if you just have 2 span . Or you can use (staggeredGridLayoutManager.getSpanCount()-1) as last span
                        outRect.left = 0;
                        outRect.right = mSpace;
                    }

                }

                outRect.bottom = 32;


            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final int SUCCESS = 0;
    private final int REFUSE = -1;
    private static final int REQUEST_CODE_CAMERA = 104;

    private int checkPermission(Activity ac, String permission) {

        int doingType = 0;

        if (ActivityCompat.checkSelfPermission(ac, permission) == PackageManager.PERMISSION_GRANTED) {
            //已授權
            doingType = SUCCESS;

            MyLog.Set("d", getClass(), "已授權");

        } else {
            //未授權 判斷是否彈出詢問框 true => 彈出

            doingType = REFUSE;

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


    /*進畫面 比Resume先執行*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MyLog.Set("d", getClass(), "resultCode => " + resultCode);
        MyLog.Set("d", getClass(), "requestCode => " + requestCode);


        if (requestCode == RequestCodeClass.CloseHighLight) {


//            if (autoPageScrollManager != null) {
//                MyLog.Set("e", getClass(), "-------- 4 --------");
//
//                autoPageScrollManager.post();
//
//            }

        }


    }


    @Override
    public void onPause() {

        cleanPicasso();

        if (autoPageScrollManager != null) {
            autoPageScrollManager.removeRunnable();
        }

        if (bannerPageAdapter != null && bannerPageAdapter.getGifList() != null && bannerPageAdapter.getGifList().size() > 0) {


            MyLog.Set("e", getClass(), "bannerPageAdapter.getGifListImg().size() => " + bannerPageAdapter.getGifList().size());


            for (int i = 0; i < bannerPageAdapter.getGifList().size(); i++) {

                ImageView img = (ImageView) bannerPageAdapter.getGifList().get(i).get(Key.imageView);

                if (img != null) {

                    Glide.with(getActivity().getApplicationContext()).clear(img);

                }

            }


        }


        super.onPause();
    }


    @Override
    public void onResume() {


        if (autoPageScrollManager != null) {

            autoPageScrollManager.post();
        }


        if (bannerPageAdapter != null) {


            if (bannerPageAdapter.getGifList() != null && bannerPageAdapter.getGifList().size() > 0) {


                for (int i = 0; i < bannerPageAdapter.getGifList().size(); i++) {

                    String url = (String) bannerPageAdapter.getGifList().get(i).get(Key.url);
                    ImageView imageView = (ImageView) bannerPageAdapter.getGifList().get(i).get(Key.imageView);

                    if (imageView != null) {
                        Glide.with(getActivity().getApplicationContext())
                                .asGif()
                                .load(url)
                                .apply(bannerPageAdapter.getOpts())
                                .into(imageView);
                    }


                }


            }


        }


        super.onResume();
    }

    @Override
    public void onDestroy() {

        if (attentionTask != null && !attentionTask.isCancelled()) {
            attentionTask.cancel(true);
        }
        attentionTask = null;

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
        }
        moreDataTask = null;

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
        }
        refreshTask = null;

        if (getADTask != null && !getADTask.isCancelled()) {
            getADTask.cancel(true);
        }
        getADTask = null;

        /*nullj移除所有runnable*/
        if (autoPageScrollManager != null) {
            autoPageScrollManager.recycler();
        }


        cleanPicasso();
        super.onDestroy();
    }


}

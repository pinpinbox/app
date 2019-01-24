package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MainActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebViewActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerBannerAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerFeatureAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerHomeAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerNewJoinAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemHomeBanner;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.GAControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ExLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.RequestCodeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.IntentControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DismissExcute;
import com.pinpinbox.android.pinpinbox2_0_0.libs.GalleryRecyclerView.CardScaleHelper;
import com.pinpinbox.android.pinpinbox2_0_0.libs.GalleryRecyclerView.SpeedRecyclerView;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol21_UpdateUser;
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

/**
 * Created by kevin9594 on 2016/12/17.
 */
public class FragmentHome extends Fragment implements View.OnClickListener, SuperSwipeRefreshLayout.OnPullRefreshListener {

    private LoadingAnimation loading;
    private InputMethodManager inputMethodManager;
    private CountDownTimer countDownTimer;

    private FragmentSearch fragmentSearch;
    private FragmentCategory fragmentCategory;

    private GetAllDataTask getAllDataTask;
    private MoreDataTask moreDataTask;
    private RefreshTask refreshTask;
    private Protocol21_UpdateUser protocol21;

    private JSONArray p20JsonArray;

    private RecyclerHomeAdapter recyclerHomeAdapter;
    private RecyclerNewJoinAdapter newJoinAdapter;
    private RecyclerBannerAdapter bannerAdapter;
    private RecyclerFeatureAdapter featureRecommendAdapter, featureHotAdapter;

    private List<ItemAlbum> itemAlbumList;
    private List<ItemHomeBanner> itemHomeBannerList;
    private List<ItemUser> itemNewJoinList, itemRecommendList, itemHotList;
    private List<String> reAlbumIdList;

    private String id, token;
    private String p20Result, p20Message, p115Result, p115Message, p116Result, p116Message;
    private String p75Message = "", p86Message = "";
    private String strRank;


    private int p75Result = -1, p86Result = -1;


    private int round; //listview添加前的初始值
    private int count; //listview每次添加的數量
    private int p20total = 0;
    private int reCreateCount = 0;

    private int doingType;


    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean isDoingMore = false;
    private boolean isSearch = false;
    private boolean isGetData = false;


    private SpeedRecyclerView rvBanner;
    private RecyclerView rvHome, rvRecommendUser, rvRecommend, rvHot;
    private SmoothProgressBar pbLoadMore, pbRefresh;
    private View viewHeader;

    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private EditText edSearch;
    private ImageView scanImg, categoryImg;
    private TextView tvCategory;
    private LinearLayout linCategory;


    private LinearLayout linHobby, linFollow;
    private TextView tvNew, tvFollow;
    private View vHobby, vFollow;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_home, container, false);

        initView(v);

        initHeaderView();

        tvShowTime = v.findViewById(R.id.tvShowTime);
        if (BuildConfig.FLAVOR.equals("w3_private") || BuildConfig.FLAVOR.equals("www_private") || BuildConfig.FLAVOR.equals("platformvmage5_private")) {
            testSet();
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            tvShowTime.setVisibility(View.GONE);
        }


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        setSearch();

        setBannerRecycler();
        setRecommendRecycler();
        setHotRecycler();
        setNewJoinRecycler();
        setRecycler();

        doGetAllData();


    }

    @Override
    public void onResume() {


        if (bannerAdapter != null) {


            if (bannerAdapter.getGifList() != null && bannerAdapter.getGifList().size() > 0) {


                for (int i = 0; i < bannerAdapter.getGifList().size(); i++) {

                    String url = (String) bannerAdapter.getGifList().get(i).get(Key.url);
                    ImageView imageView = (ImageView) bannerAdapter.getGifList().get(i).get(Key.imageView);

                    if (imageView != null) {
                        Glide.with(getActivity().getApplicationContext())
                                .asGif()
                                .load(url)
                                .apply(bannerAdapter.getOpts())
                                .into(imageView);
                    }


                }


            }


        }

        super.onResume();
    }

    @Override
    public void onPause() {

        cleanCache();


        if (bannerAdapter != null && bannerAdapter.getGifList() != null && bannerAdapter.getGifList().size() > 0) {


            MyLog.Set("e", getClass(), "bannerPageAdapter.getGifListImg().size() => " + bannerAdapter.getGifList().size());


            for (int i = 0; i < bannerAdapter.getGifList().size(); i++) {

                ImageView img = (ImageView) bannerAdapter.getGifList().get(i).get(Key.imageView);

                if (img != null) {

                    Glide.with(getActivity().getApplicationContext()).clear(img);

                }

            }


        }


        super.onPause();
    }

    @Override
    public void onDestroy() {

        if (getAllDataTask != null && !getAllDataTask.isCancelled()) {
            getAllDataTask.cancel(true);
        }
        getAllDataTask = null;

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
        }
        moreDataTask = null;

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
        }
        refreshTask = null;


        if (protocol21 != null && !protocol21.isCancelled()) {
            protocol21.cancel(true);
        }
        protocol21 = null;


        cleanCache();
        super.onDestroy();
    }

    private void initView(View v) {

        pbRefresh = v.findViewById(R.id.pbRefresh);
        rvHome = v.findViewById(R.id.rvHome);
        pbLoadMore = v.findViewById(R.id.pbLoadMore);
        pinPinBoxRefreshLayout = v.findViewById(R.id.pinPinBoxRefreshLayout);
        edSearch = v.findViewById(R.id.edSearch);
        scanImg = v.findViewById(R.id.scanImg);
        linCategory = v.findViewById(R.id.linCategory);
        categoryImg = v.findViewById(R.id.categoryImg);
        tvCategory = v.findViewById(R.id.tvCategory);

        pbLoadMore.progressiveStop();
        pbRefresh.progressiveStop();


        rvHome.addItemDecoration(new SpacesItemDecoration(16, true));
        rvHome.setItemAnimator(new DefaultItemAnimator());
        rvHome.addOnScrollListener(mOnScrollListener);

        mOnScrollListener.setvActionBar(v.findViewById(R.id.linActionBar));

        pinPinBoxRefreshLayout.setOnPullRefreshListener(this);
        pinPinBoxRefreshLayout.setRefreshView(v.findViewById(R.id.vRefreshAnim), pbRefresh);

        scanImg.setOnClickListener(this);
        linCategory.setOnClickListener(this);

    }

    private void initHeaderView() {

        viewHeader = getActivity().getLayoutInflater().inflate(R.layout.header_2_0_0_home, null);

        linHobby = viewHeader.findViewById(R.id.linHobby);
        linFollow = viewHeader.findViewById(R.id.linFollow);
        rvBanner = viewHeader.findViewById(R.id.rvBanner);
        rvRecommend = viewHeader.findViewById(R.id.rvRecommend);
        rvHot = viewHeader.findViewById(R.id.rvHot);
        rvRecommendUser = viewHeader.findViewById(R.id.rvRecommendUser);
        tvNew = viewHeader.findViewById(R.id.tvNew);
        tvFollow = viewHeader.findViewById(R.id.tvFollow);
        vHobby = viewHeader.findViewById(R.id.vHobby);
        vFollow = viewHeader.findViewById(R.id.vFollow);

        linHobby.setOnClickListener(this);
        linFollow.setOnClickListener(this);


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

    private void init() {

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        loading = ((MainActivity) getActivity()).getLoading();

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
        strRank = "latest"; //default latest

        round = 0;
        count = 16;

        itemAlbumList = new ArrayList<>();
        reAlbumIdList = new ArrayList<>();


        //banner
        itemHomeBannerList = new ArrayList<>();

        //專區精選 - 推薦
        itemRecommendList = new ArrayList<>();

        //專區精選 - 熱門
        itemHotList = new ArrayList<>();

        //新加入
        itemNewJoinList = new ArrayList<>();

    }

    private void setSearch() {

        edSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    MyLog.Set("e", FragmentHome.this.getClass(), "監聽輸入框");

                    if (mOnScrollListener.isUpScrolling()) {
                        return;
                    }

                    if (fragmentCategory != null && !fragmentCategory.isHidden()) {
                        hideCategory();
                    }

                    showSearch();

                } else {
                    // 此处为失去焦点时的处理内容
                    MyLog.Set("e", FragmentHome.this.getClass(), "取消監聽");

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

                if (fragmentSearch != null && fragmentSearch.isAdded()) {

                    fragmentSearch.setOnFinish();

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

                MyLog.Set("d", FragmentSearch.class, "afterTextChanged");


                if (s.toString().equals("")) {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }

                    if (fragmentSearch != null && fragmentSearch.isAdded()) {

                        fragmentSearch.setOnFinish();

                    }


                } else {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer.start();

                        MyLog.Set("d", FragmentSearch.class, "重新倒數");
                    }


                }


            }
        });


    }

    private void showSearch() {
        if (fragmentSearch == null) {
            fragmentSearch = new FragmentSearch();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.frameSearch, fragmentSearch).commit();
        } else {

            if (fragmentSearch.isHidden()) {
                getActivity().getSupportFragmentManager().beginTransaction().show(fragmentSearch).commit();
            }

        }

        isSearch = true;
        scanImg.setImageResource(R.drawable.ic200_cancel_dark);

        mOnScrollListener.setCanControlActionBar(false);

    }

    public void hideSearch() {
        if (fragmentSearch != null) {
            getActivity().getSupportFragmentManager().beginTransaction().hide(fragmentSearch).commit();
        }

        isSearch = false;
        scanImg.setImageResource(R.drawable.ic200_scancamera_dark);

        mOnScrollListener.setCanControlActionBar(true);

    }

    private void showCategory() {

        if (fragmentCategory == null) {
            fragmentCategory = new FragmentCategory();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.frameSearch, fragmentCategory).commit();
        } else {

            if (fragmentCategory.isHidden()) {
                getActivity().getSupportFragmentManager().beginTransaction().show(fragmentCategory).commit();
            }

        }

        categoryImg.setImageResource(R.drawable.ic200_category_dark);
        tvCategory.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

        mOnScrollListener.setCanControlActionBar(false);

    }

    public void hideCategory() {

        if (fragmentCategory != null) {
            getActivity().getSupportFragmentManager().beginTransaction().hide(fragmentCategory).commit();
        }

        categoryImg.setImageResource(R.drawable.ic200_category_light);
        tvCategory.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

        mOnScrollListener.setCanControlActionBar(true);

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

    private void setBannerRecycler() {


        bannerAdapter = new RecyclerBannerAdapter(getActivity(), itemHomeBannerList, this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        rvBanner.setLayoutManager(layoutManager);

        rvBanner.setAdapter(bannerAdapter);

        if (PPBApplication.getInstance().isPhone()) {

            CardScaleHelper mCardScaleHelper = new CardScaleHelper();

            mCardScaleHelper.attachToRecyclerView(rvBanner);

        }

        bannerAdapter.setOnRecyclerViewListener(new RecyclerBannerAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (itemHomeBannerList != null && itemHomeBannerList.size() > 0) {

                    FlurryUtil.onEvent(FlurryKey.home_click_event);

                    Bundle bundle = new Bundle();

                    Intent intent = new Intent();

                    String url = itemHomeBannerList.get(position).getUrl();
                    String event_id = itemHomeBannerList.get(position).getEvent_id();
                    String album_id = itemHomeBannerList.get(position).getAlbum_id();
                    String user_id = itemHomeBannerList.get(position).getUser_id();


                    MyLog.Set("e", FragmentHome.class, "url => " + url);
                    MyLog.Set("e", FragmentHome.class, "event_id => " + event_id);
                    MyLog.Set("e", FragmentHome.class, "album_id => " + album_id);
                    MyLog.Set("e", FragmentHome.class, "user_id => " + user_id);

                    MyLog.Set("e", FragmentHome.class, "positionpositionposition => " + position);


                    if (album_id != null && !album_id.equals("")) {

                        ActivityIntent.toAlbumInfo(getActivity(), false, album_id, null, 0, 0, 0, null);

                        return;
                    }


                    if (user_id != null && !user_id.equals("")) {

                        ActivityIntent.toUser(getActivity(), false, false, user_id, null, null, null);

                        return;
                    }

                    if (event_id != null && !event_id.equals("")) {

                        ActivityIntent.toEvent(getActivity(), event_id);

                        return;

                    }


                    if (url == null || url.equals("")) {

                        PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_null_intent);

                    } else {

                        Uri uri = Uri.parse(url);

                        String lastPath = uri.getLastPathSegment();

                        if (lastPath.equals("point")) {

                            ActivityIntent.toBuyPoint(getActivity());

                        } else {


                            album_id = uri.getQueryParameter(Key.album_id);
                            if (album_id != null && !album_id.equals("")) {
                                ActivityIntent.toAlbumInfo(getActivity(), false, album_id, null, 0, 0, 0, null);
                                return;
                            }


                            user_id = uri.getQueryParameter(Key.user_id);
                            if (user_id != null && !user_id.equals("")) {
                                ActivityIntent.toUser(getActivity(), false, false, user_id, null, null, null);
                                return;
                            }

                            event_id = uri.getQueryParameter(Key.event_id);
                            if (event_id != null && !event_id.equals("")) {
                                ActivityIntent.toEvent(getActivity(), event_id);
                                return;
                            }


                            String categoryareaId = uri.getQueryParameter(Key.categoryarea_id);
                            if (categoryareaId != null && !categoryareaId.equals("")) {
                                ActivityIntent.toFeature(getActivity(), StringIntMethod.StringToInt(categoryareaId));
                                return;
                            }

                            bundle.putString("url", url);
                            intent.putExtras(bundle);
                            intent.setClass(getActivity(), WebViewActivity.class);
                            getActivity().startActivity(intent);
                            ActivityAnim.StartAnim(getActivity());


                        }

                    }


                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }

        });

    }

    private void setRecommendRecycler() {

        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRecommend.setLayoutManager(layoutManager);
        featureRecommendAdapter = new RecyclerFeatureAdapter(getActivity(), itemRecommendList);
        rvRecommend.setAdapter(featureRecommendAdapter);

        featureRecommendAdapter.setOnRecyclerViewListener(new RecyclerFeatureAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (itemRecommendList.get(position).getUser_id().equals(id)) {
                    ((MainActivity) getActivity()).toMePage(false);
                } else {
                    ActivityIntent.toUser(
                            getActivity(),
                            false,
                            false,
                            itemRecommendList.get(position).getUser_id(),
                            itemRecommendList.get(position).getPicture(),
                            itemRecommendList.get(position).getName(),
                            v.findViewById(R.id.userImg)
                    );
                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void setHotRecycler() {

        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvHot.setLayoutManager(layoutManager);
        featureHotAdapter = new RecyclerFeatureAdapter(getActivity(), itemHotList);
        rvHot.setAdapter(featureHotAdapter);

        featureHotAdapter.setOnRecyclerViewListener(new RecyclerFeatureAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (itemHotList.get(position).getUser_id().equals(id)) {
                    ((MainActivity) getActivity()).toMePage(false);
                } else {
                    ActivityIntent.toUser(
                            getActivity(),
                            false,
                            false,
                            itemHotList.get(position).getUser_id(),
                            itemHotList.get(position).getPicture(),
                            itemHotList.get(position).getName(),
                            v.findViewById(R.id.userImg)
                    );
                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


    }

    private void setNewJoinRecycler() {

        ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRecommendUser.setLayoutManager(layoutManager);
        newJoinAdapter = new RecyclerNewJoinAdapter(getActivity(), itemNewJoinList);
        rvRecommendUser.setAdapter(newJoinAdapter);

        newJoinAdapter.setOnRecyclerViewListener(new RecyclerNewJoinAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                if (itemNewJoinList.get(position).getUser_id().equals(id)) {

                    ((MainActivity) getActivity()).toMePage(false);

                    return;
                }


                ActivityIntent.toUser(
                        getActivity(),
                        true,
                        false,
                        itemNewJoinList.get(position).getUser_id(),
                        itemNewJoinList.get(position).getPicture(),
                        itemNewJoinList.get(position).getName(),
                        v.findViewById(R.id.userImg)
                );

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void setRecycler() {

        recyclerHomeAdapter = new RecyclerHomeAdapter(getActivity(), itemAlbumList);
        rvHome.setAdapter(recyclerHomeAdapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(recyclerHomeAdapter);
        rvHome.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = null;

        if (PPBApplication.getInstance().isPhone()) {

            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        } else {

            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

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
                        itemAlbumList.get(position).getCover_width(),
                        itemAlbumList.get(position).getCover_height(),
                        v.findViewById(R.id.coverImg)
                );


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void getBannerList() {

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

                        itemHomeBannerList.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = (JSONObject) jsonArray.get(i);

                            ItemHomeBanner itemHomeBanner = new ItemHomeBanner();

                            try {
                                String event = JsonUtility.GetString(object, ProtocolKey.event);
                                if (event != null && !event.equals("")) {
                                    JSONObject jsonEvent = new JSONObject(event);
                                    itemHomeBanner.setEvent_id(JsonUtility.GetString(jsonEvent, ProtocolKey.event_id));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                String ad = JsonUtility.GetString(object, ProtocolKey.ad);
                                if (ad != null && !ad.equals("")) {
                                    JSONObject jsonAd = new JSONObject(ad);
                                    itemHomeBanner.setImage(JsonUtility.GetString(jsonAd, ProtocolKey.image));
                                    itemHomeBanner.setUrl(JsonUtility.GetString(jsonAd, ProtocolKey.url));
                                    itemHomeBanner.setName(JsonUtility.GetString(jsonAd, ProtocolKey.name));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            /*2016.08.19新增album template user Object*/
                            try {
                                String album = JsonUtility.GetString(object, ProtocolKey.album);
                                if (album != null && !album.equals("")) {
                                    JSONObject jsonAlbum = new JSONObject(album);
                                    itemHomeBanner.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            try {
                                String user = JsonUtility.GetString(object, ProtocolKey.user);
                                if (user != null && !user.equals("")) {
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

    private void getRecommendList() {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P86_GetRecommendedList,
                    SetMapByProtocol.setParam86_getrecommendedlist(id, token, "user", "0,6"), null);
            Logger.json(strJson);
        } catch (SocketTimeoutException timeout) {
            p86Result = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p86Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                if (p86Result == 1) {

                    String strData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONArray jsonArray = new JSONArray(strData);
                    itemRecommendList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = (JSONObject) jsonArray.get(i);

                        String user = JsonUtility.GetString(j, ProtocolKey.user);
                        JSONObject object = new JSONObject(user);

                        ItemUser itemUser = new ItemUser();
                        itemUser.setCover(JsonUtility.GetString(object, ProtocolKey.cover));
                        itemUser.setPicture(JsonUtility.GetString(object, ProtocolKey.picture));
                        itemUser.setDescription(JsonUtility.GetString(object, ProtocolKey.description));
                        itemUser.setUser_id(JsonUtility.GetString(object, ProtocolKey.user_id));
                        itemRecommendList.add(itemUser);

                    }


                } else if (p86Result == 0) {

                    p86Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }


    }

    private void getHotList() {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, Url.P115_GetHotList,
                    SetMapByProtocol.setParam115_gethotlist(id, token, "0,6"), null);
            Logger.json(strJson);
        } catch (SocketTimeoutException timeout) {
            p115Result = ResultType.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p115Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);
                if (p115Result.equals(ResultType.SYSTEM_OK)) {

                    String strData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONArray jsonArray = new JSONArray(strData);
                    itemHotList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = (JSONObject) jsonArray.get(i);

                        String user = JsonUtility.GetString(j, ProtocolKey.user);
                        JSONObject object = new JSONObject(user);

                        ItemUser itemUser = new ItemUser();
                        itemUser.setCover(JsonUtility.GetString(object, ProtocolKey.cover));
                        itemUser.setPicture(JsonUtility.GetString(object, ProtocolKey.picture));
                        itemUser.setDescription(JsonUtility.GetString(object, ProtocolKey.description));
                        itemUser.setUser_id(JsonUtility.GetString(object, ProtocolKey.user_id));
                        itemHotList.add(itemUser);

                    }


                } else {

                    p115Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }

    }

    private void getNewJoinUserList() {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, Url.P116_GetNewJoinList,
                    SetMapByProtocol.setParam116_getnewjoinlist(id, token, "0,6"), null);
            Logger.json(strJson);
        } catch (SocketTimeoutException timeout) {
            p116Result = ResultType.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p116Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);
                if (p116Result.equals(ResultType.SYSTEM_OK)) {

                    String strData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONArray jsonArray = new JSONArray(strData);
                    itemNewJoinList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject j = (JSONObject) jsonArray.get(i);

                        String user = JsonUtility.GetString(j, ProtocolKey.user);
                        JSONObject object = new JSONObject(user);

                        ItemUser itemUser = new ItemUser();
                        itemUser.setName(JsonUtility.GetString(object, ProtocolKey.name));
                        itemUser.setPicture(JsonUtility.GetString(object, ProtocolKey.picture));
                        itemUser.setUser_id(JsonUtility.GetString(object, ProtocolKey.user_id));
                        itemNewJoinList.add(itemUser);

                    }


                } else {

                    p116Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }


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
//                        String user = JsonUtility.GetString(object, ProtocolKey.user);

                        JSONObject jsonAlbum = new JSONObject(album);
                        JSONObject jsonAlbumstatistics = new JSONObject(albumstatistics);
                        JSONObject jsonNotice = new JSONObject(notice);
//                        JSONObject jsonUser = new JSONObject(user);


                        /*20171031************************************************/
                        boolean isAlbumExist = false;

                        String album_id = JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id);

                        if (reAlbumIdList.size() > 0) {

                            for (int j = 0; j < reAlbumIdList.size(); j++) {

                                if (album_id.equals(reAlbumIdList.get(j))) {

                                    MyLog.Set("e", FragmentHome.class, "重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了重複了");

                                    isAlbumExist = true;
                                    break;
                                }

                            }

                        }

                        if (isAlbumExist) continue;

                        reAlbumIdList.add(album_id);
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
                                itemAlbum.setImage_orientation(ItemAlbum.SQUARE);
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
//                        itemAlbum.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));
//                        itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));
//                        itemAlbum.setUser_picture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));

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

    private void changeRank(String rank) {

        try {
            if (rank.equals("latest")) {

                tvNew.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                vHobby.setVisibility(View.VISIBLE);

                tvFollow.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                vFollow.setVisibility(View.INVISIBLE);


            } else if (rank.equals("follow")) {

                tvNew.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                vHobby.setVisibility(View.INVISIBLE);

                tvFollow.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                vFollow.setVisibility(View.VISIBLE);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cleanCache() {

        if (itemAlbumList != null && itemAlbumList.size() > 0) {

            for (int i = 0; i < itemAlbumList.size(); i++) {

//                String picture = itemAlbumList.get(i).getUser_picture();
//                if (picture != null && !picture.equals("")) {
//                    Picasso.with(getActivity().getApplicationContext()).invalidate(picture);
//                }

                String path_albumcover = itemAlbumList.get(i).getCover();
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


        if (itemNewJoinList != null && itemNewJoinList.size() > 0) {

            for (int i = 0; i < itemNewJoinList.size(); i++) {

                String picture = itemNewJoinList.get(i).getPicture();
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

    public boolean isSearch() {
        return isSearch;
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doGetAllData();
                        break;

                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;

                    case DoingTypeClass.DoRefresh:
                        doRefresh();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);

    }

    private void doGetAllData() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }

        getAllDataTask = new GetAllDataTask();
        getAllDataTask.execute();

    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(getActivity())) {
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }
        moreDataTask = new MoreDataTask();
        moreDataTask.execute();
    }

    private void doRefresh() {

        if (!HttpUtility.isConnect(getActivity())) {
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }

        if (getAllDataTask != null && !getAllDataTask.isCancelled()) {
            getAllDataTask.cancel(true);
            getAllDataTask = null;
        }


        if (p20total > 0) {
            cleanCache();
        }

        itemHomeBannerList.clear();
        itemRecommendList.clear();
        itemHotList.clear();
        itemNewJoinList.clear();
        itemAlbumList.clear();
        reAlbumIdList.clear();

        p20total = 0;
        round = 0;

        bannerAdapter.notifyDataSetChanged();
        featureRecommendAdapter.notifyDataSetChanged();
        featureHotAdapter.notifyDataSetChanged();
        newJoinAdapter.notifyDataSetChanged();
        recyclerHomeAdapter.notifyDataSetChanged();

        refreshTask = new RefreshTask();
        refreshTask.execute();

    }

    @SuppressLint("StaticFieldLeak")
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

            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();

            if (p20Result.equals("1")) {

                if (p20JsonArray.length() == 0) {
                    MyLog.Set("d", FragmentHome.class, "已達最大值");
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

    @SuppressLint("StaticFieldLeak")
    public class RefreshTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoRefresh;

        }

        @Override
        protected Object doInBackground(Void... params) {

            getBannerList();

            getRecommendList();

            getHotList();

            getNewJoinUserList();

            getUpdateList(round + "," + count);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            sizeMax = false;

            pinPinBoxRefreshLayout.setRefreshing(false);

            postData();


        }

    }

    @SuppressLint("StaticFieldLeak")
    public class GetAllDataTask extends AsyncTask<Void, Void, Object> {

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

            getBannerList();

            getRecommendList();

            getHotList();

            getNewJoinUserList();

            itemAlbumList.clear();
            reAlbumIdList.clear();
            p20total = 0;
            round = 0;

            getUpdateList(round + "," + count);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            ViewControl.AlphaTo1(pinPinBoxRefreshLayout);

            postData();

            checkNewletter();


        }

        private void checkNewletter() {

            if (!PPBApplication.getInstance().getData().getBoolean(Key.checkNewsletter, false)) {

                DialogV2Custom dlgCheckNL = new DialogV2Custom(getActivity());
                dlgCheckNL.setStyle(DialogStyleClass.CHECK);
                dlgCheckNL.setMessage(R.string.pinpinbox_2_0_0_other_text_select_newsletter_default_receiving);
                dlgCheckNL.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_subscribe);
                dlgCheckNL.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_no_subscribe);
                dlgCheckNL.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {
                        doUploadNewsletter(true);
                    }
                });

                dlgCheckNL.setDismissExcute(new DismissExcute() {
                    @Override
                    public void AfterDismissDo() {
                        doUploadNewsletter(false);
                    }
                });
                dlgCheckNL.getDarkBg().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                dlgCheckNL.show();

            }

        }

    }

    private void postData() {


        /*banner*/
        try {

            if (p75Result == 1) {

                bannerAdapter.notifyDataSetChanged();

            } else if (p75Result == 0) {

                DialogV2Custom.BuildError(getActivity(), p75Message);

                return;

            } else if (p75Result == Key.TIMEOUT) {

                connectInstability();

                return;

            } else {

                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());

                return;

            }

        } catch (Exception e) {

            e.printStackTrace();

        }



        /*專區精選- 推薦*/
        try {

            if (p86Result == 1) {
                featureRecommendAdapter.notifyDataSetChanged();
            } else if (p86Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p86Message);
                return;

            } else if (p86Result == Key.TIMEOUT) {
                connectInstability();
                return;
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                return;
            }

        } catch (Exception e) {

            e.printStackTrace();

        }



        /*新加入*/
        try {

            switch (p115Result) {

                case ResultType.SYSTEM_OK:
                    featureHotAdapter.notifyDataSetChanged();
                    break;

                case ResultType.TOKEN_ERROR:
                    PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_token_error_to_login);
                    IntentControl.toLogin(getActivity(), id);
                    return;

                case ResultType.USER_ERROR:
                    DialogV2Custom.BuildError(getActivity(), p115Message);
                    return;

                case ResultType.SYSTEM_ERROR:
                    DialogV2Custom.BuildUnKnow(getActivity(), getActivity().getClass().getSimpleName() + " => " + this.getClass() + " => " + ResultType.SYSTEM_ERROR);
                    return;

                case ResultType.TIMEOUT:
                    connectInstability();
                    return;

                case "":
                    DialogV2Custom.BuildUnKnow(getActivity(), getActivity().getClass().getSimpleName() + " => " + this.getClass().getSimpleName());
                    return;

            }
        } catch (Exception e) {

            e.printStackTrace();

        }



        /*新加入*/
        try {

            switch (p116Result) {

                case ResultType.SYSTEM_OK:
                    newJoinAdapter.notifyDataSetChanged();
                    break;

                case ResultType.TOKEN_ERROR:
                    PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_token_error_to_login);
                    IntentControl.toLogin(getActivity(), id);
                    return;

                case ResultType.USER_ERROR:
                    DialogV2Custom.BuildError(getActivity(), p116Message);
                    return;

                case ResultType.SYSTEM_ERROR:
                    DialogV2Custom.BuildUnKnow(getActivity(), getActivity().getClass().getSimpleName() + " => " + this.getClass() + " => " + ResultType.SYSTEM_ERROR);
                    return;

                case ResultType.TIMEOUT:
                    connectInstability();
                    return;

                case "":
                    DialogV2Custom.BuildUnKnow(getActivity(), getActivity().getClass().getSimpleName() + " => " + this.getClass().getSimpleName());
                    return;

            }
        } catch (Exception e) {

            e.printStackTrace();

        }



        /*瀑布流作品 不用return ( 主要 recyclerview 要最後執行)*/
        if (p20Result.equals("1")) {

            recyclerHomeAdapter.notifyDataSetChanged();

            round = round + count;

        } else if (p20Result.equals("0")) {

            DialogV2Custom.BuildError(getActivity(), p20Message);

        } else if (p20Result.equals(Key.timeout)) {

            connectInstability();

        } else {

            DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());

        }

    }

    private void doUploadNewsletter(final boolean newsletter) {

        String param = "";

        try {
            JSONObject object = new JSONObject();
            object.put(ProtocolKey.newsletter, newsletter);
            param = object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        protocol21 = new Protocol21_UpdateUser(
                getActivity(),
                id,
                token,
                param,
                new Protocol21_UpdateUser.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        loading.show();
                    }

                    @Override
                    public void Post() {
                        loading.dismiss();
                    }

                    @Override
                    public void Success() {

                        if (newsletter) {
                            PinPinToast.showSuccessToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_success_subscribe);
                        } else {
                            PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_cancel_subscribe);
                        }

                        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkNewsletter, true).commit();


                    }

                    @Override
                    public void TimeOut() {
                        doUploadNewsletter(newsletter);
                    }
                }

        );

    }

    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }

        switch (view.getId()) {


            case R.id.linHobby:

                strRank = "latest";

                changeRank(strRank);

                doRefresh();

                break;

            case R.id.linFollow:

                strRank = "follow";

                changeRank(strRank);

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
                            ((MainActivity) getActivity()).toScan();
                            break;
                        case REFUSE:
                            MPermissions.requestPermissions(FragmentHome.this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
                            break;

                    }

                }

                break;

            case R.id.linCategory:

                if (mOnScrollListener.isUpScrolling()) {
                    return;
                }

                if (isSearch) {
                    edSearch.clearFocus();
                    inputMethodManager.hideSoftInputFromWindow(edSearch.getWindowToken(), 0);
                    hideSearch();
                }


                if (fragmentCategory == null || fragmentCategory.isHidden()) {

                    showCategory();

                } else {

                    hideCategory();

                }

                break;

        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed() && ((MainActivity) getActivity()).getPage() == 0) {
            MyLog.Set("e", getClass(), "isVisibleToUser => " + true);
            GAControl.sendViewName("首頁");
        }

    }


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {

                if (isDoingMore) {
                    MyLog.Set("e", FragmentHome.class, "正在讀取更多項目");
                    return;
                }


                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

                MyLog.Set("e", FragmentHome.class, "sizeMax");
            }
        }


    };

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
                ((MainActivity) getActivity()).toScan();
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


}

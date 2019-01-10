package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.orhanobut.logger.Logger;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.BitmapUtility;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.Gradient.ScrimUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.StringUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.activity.IncomeActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MainActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollectActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebViewActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerAuthorAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.GAControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.UrlClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.libs.crop.Crop;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol101_SetUserCover;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopBoard;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by vmage on 2017/3/24.
 */
public class FragmentMe extends Fragment implements View.OnClickListener {

    private LoadingAnimation loading;
    private ExStaggeredGridLayoutManager manager;

    private Protocol101_SetUserCover protocol101;

    private File fileCover;

    //    private PopupCustom popMenu;
    private PopBoard board;

    private GetCreativeTask getCreativeTask;
    private MoreDataTask moreDataTask;
    private RefreshTask refreshTask;

    private RecyclerAuthorAdapter authorAdapter;

    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;


    private List<ItemAlbum> albumList;

    private JSONArray p40JsonArray;
    private ItemUser itemUser;

    private RecyclerView rvAuthor;
    private SmoothProgressBar pbLoadMore;
    private RoundCornerImageView userImg;
    private ImageView bannerImg, webImg, facebookImg, googleImg, instagramImg, linkedinImg, pinterestImg, twitterImg, youtubeImg, messageImg, aboutImg, shareImg, incomeImg;
    private RelativeLayout rBackground, rFragmentBackground, rBackgroundParallax;
    private LinearLayout linLink, linSponsorList, linFollowMe;
    private TextView tvName, tvFollow, tvViewed, tvCreativeName, tvSponsor, tvUploadBanner, tvGuide;
    private View viewHeader;
    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private RelativeLayout rCreativeName;

    private String id, token;
    private String p40Result = "", p40Message = "";

    private static final String IMAGE_UNSPECIFIED = "image/*";

    private int round; //listview添加前的初始值
    private int rangeCount; //listview每次添加的數量
    private int defaultCount;
    private int doingType;
    private int deviceType;
    private int afterCheckPermissionType;
    private static final int PHONE = 10001;
    private static final int TABLE = 10002;

    private static final int NONE = 0;
    //    private static final int PHOTO_GRAPH = 1;
    private static final int PHOTO_FILES = 2;

    private static final int toSelectPhoto = 3001;
    private static final int toWorkManager = 3002;


    private float scale = 0;

    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean isPullAnimRefresh = true;
    private boolean sendScrollMore = false;
    private boolean isDoingMore = false;
    private boolean isVisible;
    private boolean dowork = false;
    private boolean isGetData = false;

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "onLoad");
                if (isDoingMore) {
                    MyLog.Set("e", FragmentMe.class, "正在讀取更多項目");
                    return;
                }
                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

                MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "sizeMax");
            }
        }

    };

    public static void doRefreshInOtherClass() {
        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
        for (int i = 0; i < activityList.size(); i++) {
            String actName = activityList.get(i).getClass().getSimpleName();
            if (actName.equals(MainActivity.class.getSimpleName())) {

                FragmentMe fragmentMe = (FragmentMe) ((MainActivity) activityList.get(i)).getFragment(FragmentMe.class.getSimpleName());

                if (fragmentMe.isGetData()) {
                    (
                            (FragmentMe) ((MainActivity) activityList.get(i)).getFragment(FragmentMe.class.getSimpleName())
                    ).doRefresh(false);
                }


                break;
            }
        }
    }


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

        View v = inflater.inflate(R.layout.fragment_2_0_0_me, container, false);

        rBackground = ((MainActivity) getActivity()).getBackground();
        rFragmentBackground = v.findViewById(R.id.rFragmentBackground);
        rBackgroundParallax = v.findViewById(R.id.rBackgroundParallax);
        rCreativeName = v.findViewById(R.id.rCreativeName);

        messageImg = v.findViewById(R.id.messageImg);
        aboutImg = v.findViewById(R.id.aboutImg);
        shareImg = v.findViewById(R.id.shareImg);
        incomeImg = v.findViewById(R.id.incomeImg);

        tvCreativeName = v.findViewById(R.id.tvCreativeName);
        tvUploadBanner = v.findViewById(R.id.tvUploadBanner);

        bannerImg = v.findViewById(R.id.bannerImg);
        rvAuthor = v.findViewById(R.id.rvAuthor);
        pbLoadMore = v.findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();


        rvAuthor.addItemDecoration(new SpacesItemDecoration(16, true));

        rvAuthor.setItemAnimator(new DefaultItemAnimator());
        rvAuthor.addOnScrollListener(mOnScrollListener);

        pinPinBoxRefreshLayout = v.findViewById(R.id.pinPinBoxRefreshLayout);
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

        //20171002
        SmoothProgressBar pbRefresh = v.findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(v.findViewById(R.id.vRefreshAnim), pbRefresh);
        pinPinBoxRefreshLayout.setUserBgViewScale(rBackgroundParallax);


        viewHeader = LayoutInflater.from(getActivity()).inflate(R.layout.header_2_0_0_user, null);
        userImg = viewHeader.findViewById(R.id.userImg);

        tvGuide = viewHeader.findViewById(R.id.tvGuide);
        tvName = viewHeader.findViewById(R.id.tvName);

        tvFollow = viewHeader.findViewById(R.id.tvFollow);
        tvViewed = viewHeader.findViewById(R.id.tvViewed);
        tvSponsor = viewHeader.findViewById(R.id.tvSponsor);

        linLink = viewHeader.findViewById(R.id.linLink);

        webImg = viewHeader.findViewById(R.id.webImg);
        facebookImg = viewHeader.findViewById(R.id.facebookImg);
        googleImg = viewHeader.findViewById(R.id.googleImg);
        instagramImg = viewHeader.findViewById(R.id.instagramImg);
        linkedinImg = viewHeader.findViewById(R.id.linkedinImg);
        pinterestImg = viewHeader.findViewById(R.id.pinterestImg);
        twitterImg = viewHeader.findViewById(R.id.twitterImg);
        youtubeImg = viewHeader.findViewById(R.id.youtubeImg);


        linSponsorList = viewHeader.findViewById(R.id.linSponsorList);
        linFollowMe = viewHeader.findViewById(R.id.linFollowMe);


        webImg.setOnClickListener(this);
        facebookImg.setOnClickListener(this);
        googleImg.setOnClickListener(this);
        instagramImg.setOnClickListener(this);
        linkedinImg.setOnClickListener(this);
        pinterestImg.setOnClickListener(this);
        twitterImg.setOnClickListener(this);
        youtubeImg.setOnClickListener(this);

        messageImg.setOnClickListener(this);
        aboutImg.setOnClickListener(this);
        shareImg.setOnClickListener(this);
        incomeImg.setOnClickListener(this);
        tvUploadBanner.setOnClickListener(this);

        linSponsorList.setOnClickListener(this);
        linFollowMe.setOnClickListener(this);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void init() {

        loading = ((MainActivity) getActivity()).getLoading();


        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        albumList = new ArrayList<>();

        round = 0;
        rangeCount = 16;

        if (deviceType == TABLE) {

            //平版
            defaultCount = 12;

        } else {

            //手機
            defaultCount = 4;

        }

//        defaultCount = 4;

    }

    private void setRecycler() {


        authorAdapter = new RecyclerAuthorAdapter(getActivity(), albumList);
        rvAuthor.setAdapter(authorAdapter);

        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(authorAdapter);
        rvAuthor.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        manager = null;

        if (deviceType == TABLE) {

            //平版
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        } else {

            //手機
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }


        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvAuthor.getAdapter(), manager.getSpanCount()));
        rvAuthor.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvAuthor, viewHeader);

        authorAdapter.setOnRecyclerViewListener(new RecyclerAuthorAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {


                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                ActivityIntent.toAlbumInfo(
                        getActivity(),
                        true,
                        albumList.get(position).getAlbum_id(),
                        albumList.get(position).getCover(),
                        albumList.get(position).getImage_orientation(),
                        albumList.get(position).getCover_width(),
                        albumList.get(position).getCover_height(),
                        v.findViewById(R.id.coverImg)
                );


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


        //20171109
        manager.setScrollEnabled(false);


    }

    private void setdata() {

        if (!itemUser.getPicture().equals("")) {
            Picasso.with(getActivity().getApplicationContext())
                    .load(itemUser.getPicture())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(getActivity().getApplicationContext())
                    .into(userImg);
        } else {
            userImg.setImageResource(R.drawable.member_back_head);
        }


        String strCover = itemUser.getCover();
        if (strCover == null || strCover.equals("") || strCover.equals("null")) {
            bannerImg.setImageResource(R.drawable.bg200_user_default);
        } else {
            Picasso.with(getActivity().getApplicationContext())
                    .load(strCover)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg200_user_default)
                    .tag(getActivity().getApplicationContext())
                    .into(bannerImg, new Callback() {
                        @Override
                        public void onSuccess() {

                            rBackgroundParallax.setBackgroundColor(Color.parseColor(ColorClass.GREY_SECOND));

                            bannerImg.setAlpha(0.8f);


                        }

                        @Override
                        public void onError() {

                        }
                    });
        }


        tvName.setText(itemUser.getName());

        if (itemUser.getCreative_name().equals("")) {
            rCreativeName.setVisibility(View.GONE);
        } else {
            rCreativeName.setVisibility(View.VISIBLE);
            tvCreativeName.setText(itemUser.getCreative_name());
            tvCreativeName.setVisibility(View.VISIBLE);
            try {
                if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {

                    if (rCreativeName != null) {
                        rCreativeName.setBackground(
                                ScrimUtil.makeCubicGradientScrimDrawable(
                                        Color.parseColor(ColorClass.BLACK_ALPHA),
                                        8, //渐变层数
                                        Gravity.BOTTOM)); //起始方向

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int intFollow = itemUser.getCount_from();
        int intViewed = itemUser.getViewed();
        int intSponsor = itemUser.getBesponsored();

        StringUtil.ThousandToK(tvFollow, intFollow);
        StringUtil.ThousandToK(tvViewed, intViewed);
        StringUtil.ThousandToK(tvSponsor, intSponsor);


//        String text = "";
//        Resources resources = this.getResources();
//        InputStream is = null;
//        try {
//            is = resources.openRawResource(R.raw.htmltest);
//            byte buffer[] = new byte[is.available()];
//            is.read(buffer);
//            text = new String(buffer);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


//      設置在application  RichText.initCacheDir(getActivity().getApplicationContext());

//        RichText.fromHtml(text)
//                .imageClick(new OnImageClickListener() {
//                    @Override
//                    public void imageClicked(List<String> imageUrls, int position) {
//
//                        MyLog.Set("d", this.getClass(), imageUrls.get(position));
//
//                    }
//                })
//
//                .urlClick(new OnUrlClickListener() {
//                    @Override
//                    public boolean urlClicked(String url) {
//                        return false;
//                    }
//                })
//                .error(R.drawable.bg_2_0_0_no_image)
//                .autoPlay(true)
//                .into(tvDescription);


//        RichText
//                .from(text) // 数据源
//                .type(RichText.TYPE_MARKDOWN) // 数据格式,不设置默认是Html,使用fromMarkdown的默认是Markdown格式
//                .autoFix(true) // 是否自动修复，默认true
//                .autoPlay(true) // gif图片是否自动播放
//                .showBorder(true) // 是否显示图片边框
//                .borderColor(Color.RED) // 图片边框颜色
//                .borderSize(10) // 边框尺寸
//                .borderRadius(50) // 图片边框圆角弧度
//                .scaleType(ImageHolder.ScaleType.FIT_CENTER) // 图片缩放方式
//                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT) // 图片占位区域的宽高
//                .fix(imageFixCallback) // 设置自定义修复图片宽高
//                .fixLink(linkFixCallback) // 设置链接自定义回调
//                .noImage(true) // 不显示并且不加载图片
//                .resetSize(false) // 默认false，是否忽略img标签中的宽高尺寸（只在img标签中存在宽高时才有效），true：忽略标签中的尺寸并触发SIZE_READY回调，false：使用img标签中的宽高尺寸，不触发SIZE_READY回调
//                .clickable(true) // 是否可点击，默认只有设置了点击监听才可点击
//                .imageClick(onImageClickListener) // 设置图片点击回调
//                .imageLongClick(onImageLongClickListener) // 设置图片长按回调
//                .urlClick(onURLClickListener) // 设置链接点击回调
//                .urlLongClick(onUrlLongClickListener) // 设置链接长按回调
//                .placeHolder(placeHolder) // 设置加载中显示的占位图
//                .error(errorImage) // 设置加载失败的错误图
//                .cache(Cache.ALL) // 缓存类型，默认为Cache.ALL（缓存图片和图片大小信息和文本样式信息）
//                .imageGetter(yourImageGetter) // 设置图片加载器，默认为DefaultImageGetter，使用okhttp实现
//                .bind(tag) // 绑定richText对象到某个object上，方便后面的清理
//                .done(callback) // 解析完成回调
//                .into(textView); // 设置目标TextView


        setUrl();

        authorAdapter.notifyDataSetChanged();


        if (albumList.size() > 0) {
            tvGuide.setVisibility(View.GONE);
        } else {
            tvGuide.setVisibility(View.VISIBLE);
        }

        SharedPreferences.Editor editor = PPBApplication.getInstance().getData().edit();
        editor.putString(Key.nickname, itemUser.getName());
        editor.putString(Key.selfdescription, itemUser.getDescription());
        editor.putString(Key.profilepic, itemUser.getPicture());
        editor.commit();


    }

    private boolean checkUrl(String url, View v) {

        boolean isUrlExist = false;

        if (url == null || url.equals("") || url.equals("null")) {

            v.setVisibility(View.GONE);
            isUrlExist = false;
        } else {

            v.setVisibility(View.VISIBLE);
            isUrlExist = true;
        }
        return isUrlExist;
    }

    private void setUrl() {

        boolean bWeb = checkUrl(itemUser.getWeb(), webImg);

        boolean bFacebook = checkUrl(itemUser.getFacebook(), facebookImg);

        boolean bGoogle = checkUrl(itemUser.getGoogle(), googleImg);

        boolean bInstagram = checkUrl(itemUser.getInstagram(), instagramImg);

        boolean bLinkedin = checkUrl(itemUser.getLinkedin(), linkedinImg);

        boolean bPinterest = checkUrl(itemUser.getPinterest(), pinterestImg);

        boolean bTwitter = checkUrl(itemUser.getTwitter(), twitterImg);

        boolean bYoutube = checkUrl(itemUser.getYoutube(), youtubeImg);

        if (!bWeb && !bFacebook && !bGoogle && !bInstagram && !bLinkedin && !bPinterest && !bTwitter && !bYoutube) {
            linLink.setVisibility(View.GONE);
        } else {
            linLink.setVisibility(View.VISIBLE);
        }


    }

    private void callProtocol40(String range) {

        String strJson = "";
        try {

            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P40_GetCreative,
                    SetMapByProtocol.setParam40_getcreative(id, token, id, range), null);

            if (BuildConfig.DEBUG) {
                Logger.json(strJson);
            }

        } catch (SocketTimeoutException timeout) {
            p40Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p40Result = jsonObject.getString(Key.result);
                if (p40Result.equals("1")) {

                    if (itemUser == null) {
                        itemUser = new ItemUser();
                        itemUser.setUser_id(id);
                    }

                    String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONObject jsonData = new JSONObject(jdata);

                    /*獲取用戶內容*/
                    String user = JsonUtility.GetString(jsonData, ProtocolKey.user);
                    JSONObject jsonUser = new JSONObject(user);
                    itemUser.setDescription(JsonUtility.GetString(jsonUser, ProtocolKey.description));
                    itemUser.setName(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                    itemUser.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                    itemUser.setViewed(JsonUtility.GetInt(jsonUser, ProtocolKey.viewed));
                    itemUser.setCover(JsonUtility.GetString(jsonUser, ProtocolKey.cover));

                    String strCreativeName = JsonUtility.GetString(jsonUser, ProtocolKey.creative_name);

                    if (StringUtils.isTrimEmpty(strCreativeName)) {
                        strCreativeName = "";
                    }
                    itemUser.setCreative_name(strCreativeName);

                    String userstatistics = JsonUtility.GetString(jsonData, ProtocolKey.userstatistics);
                    JSONObject jsonUserStatistics = new JSONObject(userstatistics);
                    itemUser.setBesponsored(JsonUtility.GetInt(jsonUserStatistics, ProtocolKey.besponsored));


                    String sociallink = JsonUtility.GetString(jsonUser, ProtocolKey.sociallink);

                    if (sociallink != null && !sociallink.equals("")) {
                        JSONObject jsonLink = new JSONObject(sociallink);
                        itemUser.setFacebook(JsonUtility.GetString(jsonLink, ProtocolKey.facebook));
                        itemUser.setGoogle(JsonUtility.GetString(jsonLink, ProtocolKey.google));
                        itemUser.setInstagram(JsonUtility.GetString(jsonLink, ProtocolKey.instagram));
                        itemUser.setLinkedin(JsonUtility.GetString(jsonLink, ProtocolKey.linkedin));
                        itemUser.setPinterest(JsonUtility.GetString(jsonLink, ProtocolKey.pinterest));
                        itemUser.setTwitter(JsonUtility.GetString(jsonLink, ProtocolKey.twitter));
                        itemUser.setWeb(JsonUtility.GetString(jsonLink, ProtocolKey.web));
                        itemUser.setYoutube(JsonUtility.GetString(jsonLink, ProtocolKey.youtube));
                    }

                    /*獲取關注數量*/
                    String follow = JsonUtility.GetString(jsonData, ProtocolKey.follow);
                    JSONObject jsonFollow = new JSONObject(follow);
                    itemUser.setCount_from(JsonUtility.GetInt(jsonFollow, ProtocolKey.count_from));
                    itemUser.setFollow(JsonUtility.GetBoolean(jsonFollow, ProtocolKey.follow));

                    /*獲取作品內容*/
                    String album = JsonUtility.GetString(jsonData, ProtocolKey.album);
                    p40JsonArray = new JSONArray(album);
                    int minHeight = DensityUtility.dip2px(getActivity().getApplicationContext(), 72);
                    for (int i = 0; i < p40JsonArray.length(); i++) {

                        JSONObject jsonAlbum = (JSONObject) p40JsonArray.get(i);

                        ItemAlbum itemAlbum = new ItemAlbum();
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
                            MyLog.Set("e", FragmentMe.class, "圖片出錯 預設圖片長寬");
                        }

                        String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                        JSONObject jsonUsefor = new JSONObject(usefor);
                        itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                        itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                        itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));
                        itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));

                        albumList.add(itemAlbum);

                    }


                    //20171025
                    /*獲取收益統計*/
                    String split = JsonUtility.GetString(jsonData, ProtocolKey.split);
                    JSONObject jsonSplit = new JSONObject(split);
                    itemUser.setRatio(JsonUtility.GetString(jsonSplit, ProtocolKey.ratio));
                    itemUser.setSum(JsonUtility.GetInt(jsonSplit, ProtocolKey.sum));
                    itemUser.setSumofsettlement(JsonUtility.GetInt(jsonSplit, ProtocolKey.sumofsettlement));
                    itemUser.setSumofunsettlement(JsonUtility.GetInt(jsonSplit, ProtocolKey.sumofunsettlement));
                    itemUser.setCompany_identity(JsonUtility.GetString(jsonSplit, ProtocolKey.identity));

                    try {
                        String creative = JsonUtility.GetString(jsonData, ProtocolKey.creative);
                        JSONObject jsonCreative = new JSONObject(creative);
                        itemUser.setInfo_url(JsonUtility.GetString(jsonCreative, ProtocolKey.info_url));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (p40Result.equals("0")) {
                    p40Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

                } else {
                    p40Result = "";
                }
            } catch (Exception e) {
                p40Result = "";
                e.printStackTrace();
            }
        }

    }

    private void toCollect() {
        startActivity(new Intent(getActivity(), MyCollectActivity.class));
        ActivityAnim.StartAnim(getActivity());
    }

    public void scrollToTop() {

//        ExStaggeredGridLayoutManager linearLayoutManager = (ExStaggeredGridLayoutManager) rvAuthor.getLayoutManager();
//
//        try {
//            int[] firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPositions(null);
//
//            if (firstVisibleItemPosition[0] > 10) {
//                rvAuthor.scrollToPosition(10);
//                MyLog.Set("d", getClass(), "先移動至第10項目");
//            }
//
//            rvAuthor.smoothScrollToPosition(0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        //20171007 因banner改為即時至頂
        rvAuthor.scrollBy(0, -mOnScrollListener.getScrolledDistance());


    }

    private void cleanPicasso() {

        if (albumList != null) {

            int count = albumList.size();

            for (int i = 0; i < count; i++) {
                Picasso.with(getActivity().getApplicationContext()).invalidate(albumList.get(i).getCover());
            }

        }

        if (itemUser != null) {

            if (itemUser.getPicture() != null && !itemUser.getPicture().equals("")) {
                try {
                    Picasso.with(getActivity().getApplicationContext()).invalidate(itemUser.getPicture());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.gc();
    }

    private void toCheckPermission(int type) {
        switch (checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            case SUCCESS:

                switch (type) {

                    case toSelectPhoto:
                        toPhotos();
                        break;

                    case toWorkManager:
                        toCollect();
                        break;
                }

                break;
            case REFUSE:
                MPermissions.requestPermissions(FragmentMe.this, REQUEST_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;
        }
    }

    private void toSponsorList() {
        ActivityIntent.toSponsorList(getActivity());
    }

    private void toFollowMe() {
        ActivityIntent.toFollowMe(getActivity());
    }

    private void showBoard() {

        if (board == null) {
            board = new PopBoard(getActivity(), PopBoard.TypeUser, itemUser.getUser_id(), rBackground, true);
        } else {
            board.clearList();
            board.doGetBoard();
        }

        ((MainActivity) getActivity()).setShowBoard(false);

    }

    private void systemShare() {

        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/jpg");
//        if (itemUser.getPicture() != null && !itemUser.getPicture().equals("")) {
//            Uri u = Uri.parse(itemUser.getPicture());
//            intent.putExtra(Intent.EXTRA_STREAM, u);
//        }
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, itemUser.getName() + " , " + UrlClass.shareUserUrl + itemUser.getUser_id());//分享內容
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(Intent.createChooser(intent, getActivity().getTitle()));

    }

    private void toWebView(String title, String url) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.title, title);
        bundle.putString(Key.url, url);

        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(getActivity());

    }

    private void toPhotos() {


        try {
            fileCover = FileUtility.createUserBannerFile(getActivity(), id);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTO_FILES);

    }

    private void toIncome() {

        Bundle bundle = new Bundle();
        bundle.putInt(Key.sum, itemUser.getSum());
        bundle.putInt(Key.sumofsettlement, itemUser.getSumofsettlement());
        bundle.putInt(Key.sumofunsettlement, itemUser.getSumofunsettlement());
        bundle.putString(Key.ratio, itemUser.getRatio());
        bundle.putString(Key.company_identity, itemUser.getCompany_identity());

        Intent intent = new Intent(getActivity(), IncomeActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(getActivity());

    }

    private void doGetCreative() {
        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }

        getCreativeTask = new GetCreativeTask();
        getCreativeTask.execute();

    }

    public void doRefresh(boolean anim) {

        isPullAnimRefresh = anim;

        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }

        if (getCreativeTask != null && !getCreativeTask.isCancelled()) {
            getCreativeTask.cancel(true);
        }
        getCreativeTask = null;


        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
        }
        refreshTask = null;

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
        }
        moreDataTask = null;


        scrollToTop();


        if (albumList != null) {
            if (albumList.size() > 0) {
                cleanPicasso();
            }
            albumList.clear();
        }

        if (authorAdapter != null) {
            authorAdapter.notifyDataSetChanged();
        }


        round = 0;

        refreshTask = new RefreshTask();
        refreshTask.execute();

    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(getActivity())) {
//            noConnect = new NoConnect(getActivity());
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }
        moreDataTask = new MoreDataTask();
        moreDataTask.execute();
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoingTypeClass.DoDefault:
                        doGetCreative();
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


    private class GetCreativeTask extends AsyncTask<Void, Void, Object> {
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
            callProtocol40(round + "," + defaultCount);
            return null;
        }


        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            ViewControl.AlphaTo1(rFragmentBackground);

            if (p40Result.equals("1")) {

                setdata();

                ViewControl.AlphaTo1((LinearLayout) viewHeader.findViewById(R.id.linContents));

                ViewControl.AlphaTo1((LinearLayout) viewHeader.findViewById(R.id.linData));

                ViewControl.AlphaTo1(tvName);

                mOnScrollListener.setBackgroundParallaxViews(rBackgroundParallax, tvUploadBanner);

                manager.setScrollEnabled(true);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (((MainActivity) getActivity()).getShowBoard()) {
                            showBoard();
                        }
                    }
                }, 200);


                if (p40JsonArray.length() == 0) {
                    sizeMax = true; // 已達最大值
                } else {

                    if (p40JsonArray.length() < defaultCount) {
                        MyLog.Set("d", FragmentMe.class, "項目少於(defaultCount)" + defaultCount);
                        sizeMax = true;
                        return;
                    }

                    round = round + defaultCount;

                }


                isGetData = true;


            } else if (p40Result.equals("0")) {

                DialogV2Custom.BuildError(getActivity(), p40Message);

            } else if (p40Result.equals(Key.timeout)) {
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
        protected Object doInBackground(Void... params) {
            callProtocol40(round + "," + rangeCount);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            isDoingMore = false;
            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();
            if (p40Result.equals("1")) {

                if (p40JsonArray.length() == 0) {
                    sizeMax = true;
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }
                } else {

                    authorAdapter.notifyItemRangeInserted(albumList.size(), rangeCount);

                    if (p40JsonArray.length() < rangeCount) {
                        MyLog.Set("d", this.getClass(), "項目少於" + rangeCount);
                        sizeMax = true;
                        return;
                    }

                    round = round + rangeCount;

                }


                if (!sendScrollMore) {
                    FlurryUtil.onEvent(FlurryKey.myprefecture_scroll_more);
                    sendScrollMore = true;
                }


            } else if (p40Result.equals("0")) {

                DialogV2Custom.BuildError(getActivity(), p40Message);

            } else if (p40Result.equals(Key.timeout)) {
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

            //不執行拉伸動畫
            if (!isPullAnimRefresh) {

                pinPinBoxRefreshLayout.startPinpinboxRefresh();

            }

        }

        @Override
        protected Object doInBackground(Void... params) {

            callProtocol40(round + "," + defaultCount);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            sizeMax = false;
            isNoDataToastAppeared = false;


            if (pinPinBoxRefreshLayout.getAlpha() == 0) {
                ViewControl.AlphaTo1(pinPinBoxRefreshLayout);
            }

            pinPinBoxRefreshLayout.setRefreshing(false);
            if (!isPullAnimRefresh) {
                pinPinBoxRefreshLayout.stopPinpinboxRefresh();
            }

            //刷新完設回預設值
            isPullAnimRefresh = true;

            if (p40Result.equals("1")) {

                setdata();

                if (p40JsonArray.length() == 0) {
                    sizeMax = true; // 已達最大值
                } else {

                    if (p40JsonArray.length() < defaultCount) {
                        MyLog.Set("d", FragmentMe.class, "項目少於(defaultCount)" + defaultCount);
                        sizeMax = true;
                        return;
                    }

                    round = round + defaultCount;

                }


            } else if (p40Result.equals("0")) {

                DialogV2Custom.BuildError(getActivity(), p40Message);

            } else if (p40Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }
        }

    }


    private static final int REQUEST_CODE_SDCARD = 105;
    private final int SUCCESS = 0;
    private final int REFUSE = -1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private int checkPermission(Activity ac, String permission) {

        int doingType = 0;

        if (ActivityCompat.checkSelfPermission(ac, permission) == PackageManager.PERMISSION_GRANTED) {
            //已授權
            doingType = SUCCESS;
        } else {
            //未授權 判斷是否彈出詢問框 true => 彈出
//            if(ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)){
            doingType = REFUSE;
//            }else {
//                doingType = REFUSE_NO_ASK;
//            }
        }
        return doingType;

    }

    @PermissionGrant(REQUEST_CODE_SDCARD)
    public void requestSdcardSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                toCollect();

                switch (afterCheckPermissionType) {

                    case toSelectPhoto:
                        toPhotos();
                        break;

                    case toWorkManager:
                        toCollect();
                        break;
                }

            }
        }, 500);

    }

    @PermissionDenied(REQUEST_CODE_SDCARD)
    public void requestSdcardFailed() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");

            DialogV2Custom d = new DialogV2Custom(getActivity());
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_sdcard);
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


    public List<ItemAlbum> getAlbumList() {
        return this.albumList;
    }

    public boolean isGetData() {
        return this.isGetData;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        // Make sure that fragment is currently visible
        if (!isVisible && isResumed()) {
            // 调用Fragment隐藏的代码段

            MyLog.Set("e", getClass(), "目前看不到 FragmentMe");


        } else if (isVisible && isResumed()) {
            // 调用Fragment显示的代码段

            MyLog.Set("e", getClass(), "可看見 FragmentMe");

            GAControl.sendViewName("個人專區");

            if (!dowork) {
                init();
                setRecycler();
                doGetCreative();
                dowork = true;
            } else {

                if (((MainActivity) getActivity()).getShowBoard()) {
                    showBoard();
                }


            }

        }
    }

    private Intent fromCrop;

    public void doUploadCover(final Intent data) {

        fromCrop = data;


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(fileCover.getPath(), options);


        options.inJustDecodeBounds = false;

        Bitmap bmp = BitmapFactory.decodeFile(fileCover.getPath(), options);


        final Bitmap bitmap = BitmapUtility.zoom(bmp, 960, 450);

        try {
            FileOutputStream out = new FileOutputStream(fileCover);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        }





        /* **********************************************************************************/


        Bundle extras = data.getExtras();
        if (extras != null) {

            protocol101 = new Protocol101_SetUserCover(
                    getActivity(),
                    itemUser.getUser_id(),
                    token,
                    fileCover,
                    new Protocol101_SetUserCover.TaskCallBack() {
                        @Override
                        public void Prepare() {

                        }

                        @Override
                        public void Post() {

                        }

                        @Override
                        public void Success(String cover) {


                            itemUser.setCover(cover);


                            if (cover == null || cover.equals("") || cover.equals("null")) {
                                bannerImg.setImageResource(R.drawable.bg200_user_default);
                            } else {
                                Picasso.with(getActivity().getApplicationContext())
                                        .load(cover)
                                        .config(Bitmap.Config.RGB_565)
                                        .error(R.drawable.bg200_user_default)
                                        .tag(getActivity().getApplicationContext())
                                        .into(bannerImg);
                            }

                            if (bitmap != null && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }


                            if (fileCover != null && fileCover.exists()) {
                                fileCover.delete();
                                fileCover = null;
                            }

                        }

                        @Override
                        public void TimeOut() {

                            doUploadCover(fromCrop);

                        }
                    });


        }


    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (view.getId()) {


            case R.id.messageImg:
                showBoard();
                break;

            case R.id.aboutImg:
                Bundle bundle = new Bundle();
//                bundle.putString(Key.url, UrlClass.shareUserUrl + itemUser.getUser_id() + "&appview=true");

                bundle.putString(Key.url, itemUser.getInfo_url());
                bundle.putString(Key.title, itemUser.getName());

                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityAnim.StartAnim(getActivity());
                break;

            case R.id.shareImg:
                systemShare();
                break;

            case R.id.incomeImg:
                toIncome();
                break;

            case R.id.tvUploadBanner:
                afterCheckPermissionType = toSelectPhoto;
                toCheckPermission(toSelectPhoto);
                break;


            case R.id.webImg:
                toWebView("home", itemUser.getWeb());
                break;

            case R.id.facebookImg:
                toWebView("facebook", itemUser.getFacebook());
                break;

            case R.id.googleImg:
                toWebView("google plus", itemUser.getGoogle());
                break;

            case R.id.instagramImg:
                toWebView("instagram", itemUser.getInstagram());
                break;

            case R.id.linkedinImg:
                toWebView("linkedin", itemUser.getLinkedin());
                break;

            case R.id.pinterestImg:
                toWebView("pinterest", itemUser.getPinterest());
                break;

            case R.id.twitterImg:
                toWebView("twitter", itemUser.getTwitter());
                break;

            case R.id.youtubeImg:
                toWebView("youtube", itemUser.getYoutube());
                break;


            case R.id.linSponsorList:

                toSponsorList();

                break;

            case R.id.linFollowMe:

                //to followme
                toFollowMe();

                break;


        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MyLog.Set("d", getClass(), "onActivityResult");
        MyLog.Set("d", getClass(), "requestCode => " + requestCode);
        MyLog.Set("d", getClass(), "resultCode => " + resultCode);

        try {
            if (resultCode == NONE) {
                return;
            }

            if (data == null) {
                return;
            }

            switch (requestCode) {

                case PHOTO_FILES:

                    Crop.of(data.getData(), Uri.fromFile(fileCover))
                            .withAspect(960, 450)
                            .start(getActivity(), FragmentMe.this);


                    break;

                case Crop.REQUEST_CROP:

                    /*從 MainActivity ActivityResult執行*/

                    doUploadCover(data);


                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onPause() {
        super.onPause();
        cleanPicasso();
    }

    @Override
    public void onDestroy() {

        if (getCreativeTask != null && !getCreativeTask.isCancelled()) {
            getCreativeTask.cancel(true);
            getCreativeTask = null;
        }

        if (moreDataTask != null && !moreDataTask.isCancelled()) {
            moreDataTask.cancel(true);
            moreDataTask = null;
        }

        if (refreshTask != null && !refreshTask.isCancelled()) {
            refreshTask.cancel(true);
            refreshTask = null;
        }

        if (protocol101 != null && !protocol101.isCancelled()) {
            protocol101.cancel(true);
        }


        Recycle.IMG(webImg);
        Recycle.IMG(facebookImg);
        Recycle.IMG(googleImg);
        Recycle.IMG(instagramImg);
        Recycle.IMG(linkedinImg);
        Recycle.IMG(pinterestImg);
        Recycle.IMG(twitterImg);
        Recycle.IMG(youtubeImg);

        cleanPicasso();

        super.onDestroy();
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.Test.OldMainActivity;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.IntentControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentHome2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMe2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentNotify2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentScanSearch2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSearch2;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.service.RegistraAWSService;
import com.richpath.RichPath;
import com.richpath.RichPathView;
import com.richpathanimator.AnimationListener;
import com.richpathanimator.RichPathAnimator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2016/12/17.
 */
public class Main2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private NoConnect noConnect;

    private ViewGroup.LayoutParams tabParams;

    private SharedPreferences getdata, getAWSDetail;


    private FastCreateTask fastCreateTask;
    private FirstLoginTask firstLoginTask;
    private JoinCooperationTask joinCooperationTask;

    private FragmentHome2 fragmentHome2;
    private FragmentSearch2 fragmentSearch2;
    private FragmentMe2 fragmentMe2;
    private FragmentNotify2 fragmentNotify2;
    private FragmentScanSearch2 fragmentScanSearch2;

    private RichPath userTop, userBottom;
    private RichPath notifyTop, notifyBottom;
    private RichPath searchInSide, searchOutSide, searchReflective;
    private RichPath insideLeft, insideCenter, insideRight, fillLarge, fillSmall;

    private List<ImageView> icons;

    private FragmentPagerItemAdapter adapter;

    private ViewPager viewPager;
    private RelativeLayout rGudieCreate, rNotify, rUser, rSearch, rHome;
    private ImageView homeImg, searchImg, createImg, newImg;
    private RichPathView svgNotify, svgUser, svgSearch, svgHome;


    private String strCooperationAlbumId = "";
    private String id, token;


    private int doingType;

    private int showDistance;
    private int hideDistance;


    private boolean mainIsExist = false;
    private boolean fromAwsMessage = false;
    private boolean OtherAreaClikcToHome = false;
    private boolean scanIntent = false;

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_main);

        setSwipeBackEnable(false);

        getdata = PPBApplication.getInstance().getData();

        checkClickOffLine();

        checkMainExist();

        getBundle();

        init();

        //掃描判斷
        if (id.equals("") || token.equals("")) {

            try {
                Uri uri = getIntent().getData();

                String businessuser_id = "";

                if (uri != null) {
                    scanIntent = false;
                    businessuser_id = uri.getQueryParameter(Key.businessuser_id);

                    if (businessuser_id != null && !businessuser_id.equals("") && !businessuser_id.equals("null")) {
                        MyLog.Set("d", this.getClass(), "uri.getQueryParameter() businessuser_id => " + uri.getQueryParameter("businessuser_id"));
                        scanIntent = true;
                    } else {
                        scanIntent = false;
                    }
                }

                if (scanIntent) {

                    Bundle bundle = new Bundle();
                    bundle.putBoolean(Key.scanIntent, scanIntent);
                    bundle.putString(Key.businessuser_id, businessuser_id);

                    Intent intent = new Intent(mActivity, Login2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

                    return;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //20171018在網頁上操作開啟APP 無登入狀態下 往上整合
        if (id.equals("") || token.equals("")) {

            Intent intent = new Intent(mActivity, Login2Activity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

            return;
        }


        setFragment();

        setPageListener();

        checkFirstLogin();

        doScheme(getIntent());

        getAwsDetail();

        checkAwsReg();

        setFlurryUserId();

        checkNewDay();


        ImageView testbuttonImg = (ImageView) findViewById(R.id.testbutton);//src
        testbuttonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mActivity, OldMainActivity.class);
                startActivity(intent);
                finish();
                ActivityAnim.StartAnim(mActivity);

            }
        });


        if(BuildConfig.FLAVOR.equals("w3_private")){
            testbuttonImg.setVisibility(View.VISIBLE);
        }else if(BuildConfig.FLAVOR.equals("www_private")){
            testbuttonImg.setVisibility(View.GONE);
        }else if(BuildConfig.FLAVOR.equals("www_public")){

            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            mFirebaseAnalytics.setUserId(id + " , " + getdata.getString(Key.nickname, "---"));
            testbuttonImg.setVisibility(View.GONE);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewControl.AlphaTo1(findViewById(R.id.rAlpha));
            }
        }, 600);

    }


    private void checkClickOffLine() {

        boolean isClickOffLine = getdata.getBoolean(Key.clickOffLine, false);
        if (isClickOffLine) {
            FlurryUtil.onEvent(FlurryKey.click_off_line);
            getdata.edit().putBoolean(Key.clickOffLine, false).commit();
        }

    }

    private void checkMainExist() {

        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
        for (int i = 0; i < activityList.size(); i++) {
            String actName = activityList.get(i).getClass().getSimpleName();
            if (actName.equals("Main2Activity")) {
                mainIsExist = true;
                break;
            }
        }
        if (!mainIsExist) {
            SystemUtility.SysApplication.getInstance().addActivity(this);
        }

    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromAwsMessage = bundle.getBoolean("fromAwsMessage");

            scanIntent = bundle.getBoolean(Key.scanIntent, false);


            MyLog.Set("d", getClass(), "(Main2Activity)scanIntent => " + scanIntent);

        }
    }

    private void init() {

        mActivity = this;

        getAWSDetail = getSharedPreferences(SharedPreferencesDataClass.awsDetail, Activity.MODE_PRIVATE);

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        /*防止crash 遺失銀幕長寬*/
        DensityUtility.setScreen(this);

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        showDistance = outMetrics.heightPixels / 9;
        hideDistance = outMetrics.heightPixels / 53;


//        loading = new LoadingAnimation(this);

        icons = new ArrayList<>();

        homeImg = (ImageView) findViewById(R.id.homeImg);
        searchImg = (ImageView) findViewById(R.id.searchImg);
        createImg = (ImageView) findViewById(R.id.createImg);
        newImg = (ImageView) findViewById(R.id.newImg);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        rGudieCreate = (RelativeLayout) findViewById(R.id.rGudieCreate);
        rNotify = (RelativeLayout) findViewById(R.id.rNotify);
        rUser = (RelativeLayout) findViewById(R.id.rUser);
        rSearch = (RelativeLayout) findViewById(R.id.rSearch);
        rHome = (RelativeLayout) findViewById(R.id.rHome);
        svgNotify = (RichPathView) findViewById(R.id.svgNotify);
        svgUser = (RichPathView) findViewById(R.id.svgUser);
        svgSearch = (RichPathView) findViewById(R.id.svgSearch);
        svgHome = (RichPathView) findViewById(R.id.svgHome);


        viewPager.setOffscreenPageLimit(3);

        homeImg.setOnClickListener(this);
        searchImg.setOnClickListener(this);
        rUser.setOnClickListener(this);
        rNotify.setOnClickListener(this);
        rSearch.setOnClickListener(this);
        rHome.setOnClickListener(this);
        createImg.setOnClickListener(this);


        notifyTop = svgNotify.findRichPathByName("top");
        notifyBottom = svgNotify.findRichPathByName("bottom");

        userTop = svgUser.findRichPathByName("top");
        userBottom = svgUser.findRichPathByName("bottom");

        searchInSide = svgSearch.findRichPathByName("inside");
        searchOutSide = svgSearch.findRichPathByName("outside");
        searchReflective = svgSearch.findRichPathByName("reflective");


        insideLeft = svgHome.findRichPathByName("insideLeft");
        insideCenter = svgHome.findRichPathByName("insideCenter");
        insideRight = svgHome.findRichPathByName("insideRight");
        fillLarge = svgHome.findRichPathByName("fillLarge");
        fillSmall = svgHome.findRichPathByName("fillSmall");


//        if (SystemUtility.isTablet(getApplicationContext())) {
//            MyLog.Set("d", getClass(), "isTablet");
//
//            svgUser.setScaleX(0.75f);
//            svgUser.setScaleY(0.75f);
//
//            svgNotify.setScaleX(0.75f);
//            svgNotify.setScaleY(0.75f);
//
//
//        }else {
//            svgUser.setScaleX(1f);
//            svgUser.setScaleY(1f);
//
//            svgNotify.setScaleX(1f);
//            svgNotify.setScaleY(1f);
//        }


    }

    private void setFragment() {

        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(mActivity)
                .add("", FragmentHome2.class)
                .add("", FragmentSearch2.class)
                .add("", FragmentMe2.class)
                .add("", FragmentNotify2.class)
                .create());

        viewPager.setAdapter(adapter);

    }

    public Fragment getFragment(String fragmentName) {

        @SuppressLint("RestrictedApi") List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        Fragment getFragment = null;

        for (int i = 0; i < fragmentList.size(); i++) {

            Fragment fragment = fragmentList.get(i);

            if (fragment.getClass().getSimpleName().equals(fragmentName)) {
                getFragment = fragment;
                MyLog.Set("d", getClass(), "fragmentName => " + fragmentName);
                break;
            }

        }

        return getFragment;
    }

    public RelativeLayout getBackground() {
        return (RelativeLayout) findViewById(R.id.rBackground);
    }

    private void setPageListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (position == 2) {

//                    Fragment fragment = getFragment(FragmentMe2.class.getSimpleName());
//
//                    if (((FragmentMe2) fragment).getAlbumList()!=null&&((FragmentMe2) fragment).getAlbumList().size() == 0) {
//                        showGuideCreate(true);
//                    }

                } else {

                    if (isShowGuideCreate) {
                        showGuideCreate(false);
                    }

                }


                switch (position) {
                    case 0://home
//                        homeImg.setImageResource(R.drawable.pinpin_192);
//                        searchImg.setImageResource(R.drawable.ic200_search_light);
//                        userImg.setImageResource(R.drawable.ic200_user_light);
//                        notifyImg.setImageResource(R.drawable.ic200_notify_light);
                        homePressed();
                        searchNormal();
                        userNormal();
                        notifyNormal();
                        break;


                    case 1://search
//                        homeImg.setImageResource(R.drawable.pinpin_192_alpha);
//                        searchImg.setImageResource(R.drawable.ic200_search_dark);
//                        userImg.setImageResource(R.drawable.ic200_user_light);
//                        notifyImg.setImageResource(R.drawable.ic200_notify_light);
                        homeNormal();
                        searchPressed();
                        userNormal();
                        notifyNormal();
                        break;

                    case 2://user
//                        homeImg.setImageResource(R.drawable.pinpin_192_alpha);
//                        searchImg.setImageResource(R.drawable.ic200_search_light);
//                        userImg.setImageResource(R.drawable.ic200_user_dark);
//                        notifyImg.setImageResource(R.drawable.ic200_notify_light);
                        homeNormal();
                        searchNormal();
                        userPressed();
                        notifyNormal();

                        break;

                    case 3://notify
//                        homeImg.setImageResource(R.drawable.pinpin_192_alpha);
//                        searchImg.setImageResource(R.drawable.ic200_search_light);
////                        userImg.setImageResource(R.drawable.ic200_user_light);
////                        notifyImg.setImageResource(R.drawable.ic200_notify_dark);
                        homeNormal();
                        searchNormal();
                        userNormal();
                        notifyPressed();

                        if (newImg.getVisibility() == View.VISIBLE) {
                            Fragment fragment = getFragment(FragmentNotify2.class.getSimpleName());

                            if (((FragmentNotify2) fragment).isGetPushQueue()) {
                                ((FragmentNotify2) fragment).doRefresh(false);
                                newImg.setVisibility(View.GONE);
                            } else {
                                newImg.setVisibility(View.GONE);
                            }

                        }


                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void checkFirstLogin() {
        if (!getdata.getBoolean(TaskKeyClass.firsttime_login, false)) {
            doTask();
        }
    }

    private void doTask() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doFirstLogin();
            }
        }, 1000);

    }

    private void doScheme(Intent intent) {
        setScheme(intent);
    }

    //未開啟時 這裡獲取intent (已開啟時從newIntent獲取)
    private void setScheme(Intent intent) {

        DensityUtility.setScreen(this);

        PPBApplication.getInstance().setSharedPreferences(getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE));

        try {
            Uri uri = intent.getData();
            try {
                if (uri != null) {

                    uriControl(intent, uri);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getValue(Intent intent) {
        String value = "";
        try {
            String dataString = intent.getDataString();
            String arg = dataString.substring(dataString.indexOf("?") + 1, dataString.length());

            String[] strs = arg.split("&");
            HashMap<String, String> map = new HashMap<String, String>();
            for (int x = 0; x < strs.length; x++) {
                String[] strs2 = strs[x].split("=");
                if (strs2.length == 2) {
                    System.out.println(strs2[0] + " , " + strs2[1]);
                    map.put(strs2[0], strs2[1]);
                    value = strs2[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    private HashMap<String, String> getValueMap(Intent intent) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            String dataString = intent.getDataString();
            String arg = dataString.substring(dataString.indexOf("?") + 1, dataString.length());
            String[] strs = arg.split("&");
            for (int x = 0; x < strs.length; x++) {
                String[] strs2 = strs[x].split("=");
                if (strs2.length == 2) {
                    System.out.println(strs2[0] + " , " + strs2[1]);
                    map.put(strs2[0], strs2[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

//    private void showLogout() {
//
//        DialogV2Custom d = new DialogV2Custom(mActivity);
//        d.setStyle(DialogStyleClass.CHECK);
//
//        d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_warning_to_logout);
//
//        d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_logout);
//        d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_button_skip);
//        d.setCheckExecute(new CheckExecute() {
//            @Override
//            public void DoCheck() {
//                toLogin();
//            }
//        });
//        d.show();
//    }

    private void toLogin() {

        IntentControl.toLoginAndEnableScan(mActivity, id, true);

    }


    private void toAlbumInfo(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, value);
        Intent intent = new Intent(mActivity, AlbumInfo2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);
    }

//    private void toTemInfo(String value) {
//        Bundle bundle = new Bundle();
//        bundle.putString("template_id", value);
//        Intent intent = new Intent(mActivity, TemplateInfoActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        ActivityAnim.StartAnim(mActivity);
//    }

    private void toAuthor(String value) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.author_id, value);
        Intent intent = new Intent(mActivity, Author2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);
    }

    public void toMePage() {
        viewPager.setCurrentItem(2, false);
    }

    private void toEvent(String value) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.event_id, value);

        Intent intent = new Intent(mActivity, Event2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);


    }

    private void toCreation(Uri uri) {

        String mAlbum_id = uri.getQueryParameter(Key.album_id);

        String mTemId = uri.getQueryParameter(Key.template_id);

        String mIdentity = uri.getQueryParameter(Key.identity);

        MyLog.Set("d", mActivity.getClass(), "mAlbum_id => " + mAlbum_id);
        MyLog.Set("d", mActivity.getClass(), "mTemId => " + mTemId);
        MyLog.Set("d", mActivity.getClass(), "mIdentity => " + mIdentity);

        if (mAlbum_id != null && !mAlbum_id.equals("") && mTemId != null && !mTemId.equals("") && mIdentity != null && !mIdentity.equals("")) {

            Bundle bundle = new Bundle();
            bundle.putString(Key.album_id, mAlbum_id);
            bundle.putString(Key.identity, mIdentity);

            int intTem = StringIntMethod.StringToInt(mTemId);

            if (intTem > 0) {
                bundle.putInt(Key.create_mode, 1);
            } else {
                bundle.putInt(Key.create_mode, 0);
            }

            Intent intent = new Intent(mActivity, Creation2Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            ActivityAnim.StartAnim(mActivity);
        }
    }

    public void toScan() {
        fragmentScanSearch2 = new FragmentScanSearch2();
        if (!fragmentScanSearch2.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.frame, fragmentScanSearch2).commit();
        }

    }

    public void getAwsDetail() {
        if (fromAwsMessage) {
            String type = getAWSDetail.getString("type", "");
            if (!type.equals("")) {
                String type_id = getAWSDetail.getString("type_id", "");

                switch (type) {
                    case "albumqueue":

                        toAlbumInfo(type_id);
                        break;

                    case "albumqueue@messageboard":

                        Bundle bundle = new Bundle();
                        bundle.putString("album_id", type_id);
                        bundle.putBoolean(Key.pinpinboard, true);
                        Intent intent = new Intent(mActivity, AlbumInfo2Activity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        ActivityAnim.StartAnim(mActivity);


                        break;

                    case "user":
                        toAuthor(type_id);
                        break;

                    case "profile":
                        toMePage();
                        break;
                }
            }
            getAWSDetail.edit().clear().commit();
        }

    }

    private void checkAwsReg() {
        String strRegAws = getdata.getString("reg_aws", "");
        if (strRegAws.equals("1")) {
            MyLog.Set("d", this.getClass(), "strRegAws => " + strRegAws);
        } else {
            Intent intentService = new Intent(this, RegistraAWSService.class);
            if (HttpUtility.isConnect(this)) {
                startService(intentService);
                MyLog.Set("d", getClass(), "startService");
            }
        }
    }

    private void setFlurryUserId() {
        FlurryAgent.setUserId(id + " , " + getdata.getString(Key.nickname, "---"));
    }

    private void checkNewDay() {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());
        String strCurrentTime = formatter.format(curDate);

        String addviewed_time = getdata.getString("addviewed_time", "");

        if (addviewed_time.equals(strCurrentTime)) {

            MyLog.Set("d", this.getClass(), "同一天");

        } else {
            getdata.edit().remove("addviewed").commit();
            MyLog.Set("d", this.getClass(), "清除所有增加過瀏覽次數的id");
        }

        getdata.edit().putString("addviewed_time", strCurrentTime).commit();


    }


    public void showNewIcon() {


        newImg.setVisibility(View.VISIBLE);

        RichPath top = svgNotify.findRichPathByName("top");
        RichPath bottom = svgNotify.findRichPathByName("bottom");
        RichPathAnimator
                .animate(top)
                .interpolator(new DecelerateInterpolator())
                .rotation(0, 20, -20, 10, -10, 5, -5, 2, -2, 0)
                .duration(3000)

                .andAnimate(bottom)
                .interpolator(new DecelerateInterpolator())
                .rotation(0, 10, -10, 5, -5, 2, -2, 0)
                .duration(3000)
                .startDelay(50)
                .start();


    }

    public void hideNewIcon() {
        newImg.setVisibility(View.GONE);
    }


    /*notify icon*/
    private void notifyNormal() {

        try {

            notifyTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
            notifyBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyPressed() {

        try {

            notifyTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));
            notifyBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void notifyAnim() {
        RichPathAnimator
                .animate(notifyTop)
                .interpolator(new DecelerateInterpolator())
                .rotation(0, 20, -20, 10, -10, 5, -5, 2, -2, 0)
                .duration(3000)

                .andAnimate(notifyBottom)
                .interpolator(new DecelerateInterpolator())
                .rotation(0, 10, -10, 5, -5, 2, -2, 0)
                .duration(3000)
                .startDelay(50)

                .start();
    }

    /*user icon*/
    private void userNormal() {

        try {


            userTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
            userBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void userPressed() {

        try {

            userTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));
            userBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));

        } catch (Exception e) {
            e.printStackTrace();
        }

        RichPathAnimator
                .animate(userTop)
                .interpolator(new DecelerateInterpolator())
                .fillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey))
                .duration(200)

                .andAnimate(userBottom)
                .interpolator(new DecelerateInterpolator())
                .fillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey))
                .duration(200)

                .start();


//        RichPathAnimator
//                .animate(userTop)
//                .interpolator(new DecelerateInterpolator())
//                .scale(1.3f)
//                .duration(200)
//
//                .andAnimate(userBottom)
//                .interpolator(new DecelerateInterpolator())
//                .scale(0.7f)
//                .translationY(14f)
//                .duration(200)
//
//                .start();

    }

    /*search icon*/
    private void searchNormal() {

        searchReflective.setFillColor(ContextCompat.getColor(mActivity, R.color.transparent));
        searchInSide.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        searchOutSide.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));


    }

    private void searchPressed() {
        searchReflective.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));
        searchInSide.setFillColor(ContextCompat.getColor(mActivity, R.color.transparent));
        searchOutSide.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));

    }

    private void searchAnim() {
        RichPathAnimator
                .animate(searchReflective)
                .interpolator(new DecelerateInterpolator())
                .fillColor(ContextCompat.getColor(mActivity, R.color.transparent), ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey))
                .trimPathEnd(0, 1)
                .duration(600)
                .animationListener(new AnimationListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                        if (viewPager.getCurrentItem() != 1) {
                            searchNormal();
                        }

                    }
                })

                .start();
    }

    /*home icon*/
    private void homeNormal() {


        insideLeft.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        insideCenter.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        insideRight.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        fillLarge.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        fillSmall.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));

    }

    private void homePressed() {

//        insideLeft.setFillColor((OtherAreaClikcToHome ? ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo) : ContextCompat.getColor(mActivity, R.color.white)));


        if (OtherAreaClikcToHome) {
            insideLeft.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
            insideCenter.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
            insideRight.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
            OtherAreaClikcToHome = false;
        } else {
            insideLeft.setFillColor(ContextCompat.getColor(mActivity, R.color.white));
            insideCenter.setFillColor(ContextCompat.getColor(mActivity, R.color.white));
            insideRight.setFillColor(ContextCompat.getColor(mActivity, R.color.white));
        }


        fillLarge.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
        fillSmall.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
    }

    private void homeAnim() {

        insideLeft.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
        insideCenter.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
        insideRight.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
        fillLarge.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));
        fillSmall.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo));

        RichPathAnimator
                .animate(insideLeft)
                .interpolator(new DecelerateInterpolator())
                .fillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo), ContextCompat.getColor(mActivity, R.color.white))

//                .trimPathEnd(0, 1)
                .duration(200)

                .andAnimate(insideCenter)
                .interpolator(new DecelerateInterpolator())
                .fillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo), ContextCompat.getColor(mActivity, R.color.white))
                .startDelay(100)
//                .trimPathEnd(0, 1)
                .duration(200)

                .andAnimate(insideRight)
                .interpolator(new DecelerateInterpolator())
                .fillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_logo), ContextCompat.getColor(mActivity, R.color.white))
                .startDelay(200)
//                .trimPathEnd(0, 1)
                .duration(200)

                .animationListener(new AnimationListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                        if (viewPager.getCurrentItem() != 0) {
                            homeNormal();
                        }

                    }
                })

                .start();


    }


//    public void setNoConnect(NoConnect noConnect) {
//
//
//
//
//        this.noConnect = noConnect;
//    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoFastCreate:
                        doFastCreate();
                        break;

                    case DoingTypeClass.DoFirstLogin:
                        doFirstLogin();
                        break;

                    case DoingTypeClass.DoJoinCooperation:
                        doJoinCooperation();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doFastCreate() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        fastCreateTask = new FastCreateTask();
        fastCreateTask.execute();
    }

    private void doFirstLogin() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        firstLoginTask = new FirstLoginTask();
        firstLoginTask.execute();

    }

    private void doJoinCooperation() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        joinCooperationTask = new JoinCooperationTask();
        joinCooperationTask.execute();
    }

    public class FastCreateTask extends AsyncTask<Void, Void, Object> {

        private int p54Result = -1;
        private String p54Message = "";

        private String newAlbum_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFastCreate;
            startLoading();
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
            dissmissLoading();
            if (p54Result == 1) {

                if (newAlbum_id != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Key.album_id, newAlbum_id);
                    bundle.putString(Key.identity, "admin");
                    bundle.putInt(Key.create_mode, 0);
                    bundle.putBoolean(Key.isNewCreate, true);
                    Intent intent = new Intent(mActivity, Creation2Activity.class);
                    intent.putExtras(bundle);


                    startActivity(intent);
                    ActivityAnim.StartAnim(mActivity);
                }
            } else if (p54Result == 0) {

                DialogV2Custom.BuildError(mActivity, p54Message);

            } else if (p54Result == Key.TIMEOUT) {
                connectInstability();
            } else {

                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());

            }

        }
    }

    private class FirstLoginTask extends AsyncTask<Void, Void, Object> {

        private String name;
        private String reward;
        private String reward_value;
        private String url;
        private String p83Message = "";

        private int p83Result = -1;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFirstLogin;
            startLoading();
        }


        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";
            try {

                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, SetMapByProtocol.setParam83_dotask(id, token, TaskKeyClass.firsttime_login, "google"), null);
                MyLog.Set("d", getClass(), "p83strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p83Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p83Result == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONObject object = new JSONObject(jdata);

                        String task = JsonUtility.GetString(object, ProtocolKey.task);
                        String event = JsonUtility.GetString(object, ProtocolKey.event);

                        JSONObject jsonTask = new JSONObject(task);
                        name = JsonUtility.GetString(jsonTask, ProtocolKey.name);
                        reward = JsonUtility.GetString(jsonTask, ProtocolKey.reward);
                        reward_value = JsonUtility.GetString(jsonTask, ProtocolKey.reward_value);

                        JSONObject jsonEvent = new JSONObject(event);
                        url = JsonUtility.GetString(jsonEvent, ProtocolKey.url);

                    } else if (p83Result == 2) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result == 3) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result == 0) {
                        p83Message = jsonObject.getString(Key.message);
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
            if (p83Result == 1) {

                DialogHandselPoint d = new DialogHandselPoint(mActivity);
                d.getTvTitle().setText(name);

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");

                    /*獲取當前使用者P點*/
                    String point = getdata.getString("point", "");
                    int p;
                    if (point.equals("")) {
                        p = 0;
                    } else {
                        p = StringIntMethod.StringToInt(point);
                    }

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    getdata.edit().putString(Key.point, newP).commit();
                    getdata.edit().putBoolean("datachange", true).commit();
                    getdata.edit().putBoolean(TaskKeyClass.firsttime_login, true).commit();


                } else {
//                    d.getTvDescription().setText();
                }


                if(url==null || url.equals("")|| url.equals("null")){
                    d.getTvLink().setVisibility(View.GONE);
                }else {
                    d.getTvLink().setVisibility(View.VISIBLE);
                    d.getTvLink().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            Intent intent = new Intent(mActivity, WebView2Activity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);
                        }
                    });
                }


            } else if (p83Result == 2) {

                getdata.edit().putBoolean(TaskKeyClass.firsttime_login, true).commit();

            } else if (p83Result == 3) {

                MyLog.Set("d", getClass(), "result = 3 , repeat share");

            } else if (p83Result == 0) {

                MyLog.Set("d", getClass(), "p83Message" + p83Message);
                getdata.edit().putBoolean(TaskKeyClass.firsttime_login, true).commit();

            } else if (p83Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    private class JoinCooperationTask extends AsyncTask<Void, Void, Object> {

        //        private String sdpath = Environment.getExternalStorageDirectory() + "/";
//        private String myDir = "PinPinBox" + id + "/";
        private String dirAlbumList = "albumlist/";


        private String p46Result, p46Message;
        private String p63Result, p63Message;
        private String p17Result, p17Message;
        private String p68Result, p68Message;
        private String strTemplate_id = "";
        private String strIdentity = "";


        /**
         * protocol17 params
         */
        private String strZipped = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();
            doingType = DoingTypeClass.DoJoinCooperation;

        }

        @Override
        protected Object doInBackground(Void... params) {

            /**加入共用(權限為預覽者)*/
            protocol46();

            /**獲取zipped*/
            if (p46Result.equals("1")) {
                protocol17();
            }

//            /**此作品可下載並判斷作品更新時間*/
//            if (p46Result.equals("1") && p17Result.equals("1") && strZipped.equals("1")) {
//                protocol68();
//            }


            /**權限改成共用者並進入編輯器*/
//            if (p46Result.equals("1")) {
//                protocol63();
//            }
//            if (p63Result.equals("1")) {
//                protocol17();
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();
            if (p46Result.equals("1") && p17Result.equals("1")) {

                if (!strZipped.equals("1")) {

                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_save_not_yet);

                    return;
                }


                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                for (int i = 0; i < activityList.size(); i++) {
                    String strActivityName = activityList.get(i).getClass().getSimpleName();
                    if (!strActivityName.equals(mActivity.getClass().getSimpleName())) {
                        SystemUtility.SysApplication.getInstance().removeActivity(activityList.get(i));
                    }
                }


                /**儲存封面*/

                Intent intent = new Intent(mActivity, Reader2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, strCooperationAlbumId);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityAnim.StartAnim(mActivity);


            } else if (p46Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p46Message);

            } else if (p46Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }

        /**
         * 加入共用清單
         */
        private void protocol46() {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P46_InsertCooperation, SetMapByProtocol.setParam46_insertcooperation(id, token, Value.album, strCooperationAlbumId, id), null);
                MyLog.Set("d", mActivity.getClass(), "p46strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p46Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p46Result = JsonUtility.GetString(jsonObject, Key.result);

                if (p46Result.equals("1")) {

                } else if (p46Result.equals("0")) {
                    p46Message = JsonUtility.GetString(jsonObject, Key.message);
                } else {
                    p46Result = "";
                }

            } catch (Exception e) {
                p46Result = "";
                e.printStackTrace();
            }
        }

        /**
         * 修改權限為共用者
         */
        private void protocol63() {

            //修改權限  //admin<管理者> / approver<副管理者> / editor<共用者> / viewer<瀏覽者>
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true,
                        ProtocolsClass.P63_UpDateCooperation,
                        SetMapByProtocol.setParam63_updatecooperation(id, token, Value.album, strCooperationAlbumId, id, "editor"), null);

                MyLog.Set("d", mActivity.getClass(), "p63strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p63Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p63Result = JsonUtility.GetString(jsonObject, Key.result);

                if (p63Result.equals("1")) {

                } else if (p63Result.equals("0")) {
                    p63Message = JsonUtility.GetString(jsonObject, Key.message);
                } else {
                    p63Result = "";
                }

            } catch (Exception e) {
                p63Result = "";
                e.printStackTrace();
            }


        }

        /**
         * 獲取template
         */
        private void protocol17() {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P17_GetCloudAlbumList, SetMapByProtocol.setParam17_getcloudalbumlist(id, token, "cooperation", "0,200"), null);
                MyLog.Set("d", getClass(), "p17strJson => cooperation => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p17Result = Key.timeout;
            } catch (Exception e) {
                p17Result = "";
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p17Result = jsonObject.getString(Key.result);
                    if (p17Result.equals("1")) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONArray p17JsonArray = new JSONArray(jdata);
                        int arrayCount = p17JsonArray.length();
                        for (int i = 0; i < arrayCount; i++) {
                            try {
                                JSONObject obj = (JSONObject) p17JsonArray.get(i);

                                String album = JsonUtility.GetString(obj, Key.album);
                                String template = JsonUtility.GetString(obj, Key.template);
                                String cooperation = JsonUtility.GetString(obj, Key.cooperation);

                                JSONObject aj = new JSONObject(album);

                                String p17_json_album_id = JsonUtility.GetString(aj, Key.album_id);

                                if (p17_json_album_id.equals(strCooperationAlbumId)) {

                                    strZipped = JsonUtility.GetString(aj, Key.zipped);

                                    JSONObject temj = new JSONObject(template);
                                    strTemplate_id = JsonUtility.GetString(temj, Key.template_id);

                                    JSONObject cj = new JSONObject(cooperation);
                                    strIdentity = JsonUtility.GetString(cj, Key.identity);
                                    break;
                                }

                            } catch (Exception e) {
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

    }

//    public static List<String> getListPicPath(Activity activity) {
//        List<String> picPath = new ArrayList<>();
//
//        Intent intent = activity.getIntent();//如果从外部进入APP，则实现以下方法
//        if (Intent.ACTION_SEND.equals(intent.getAction())) {
////            if (intent.getType().startsWith("image/")) {
//            Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
//            if (imageUri != null) {
////处理单张图片
//                Log.v("从其他APP分享的", imageUri.getPath());
//
//                if (!imageUri.getPath().contains("external/images/media"))
//                    picPath.add(imageUri.getPath());
//                else {
//                    String[] proj = {MediaStore.Images.Media.DATA};
//                    Cursor actualimagecursor = activity.managedQuery(imageUri, proj, null, null, null);
//                    int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                    actualimagecursor.moveToFirst();
//                    String img_path = actualimagecursor.getString(actual_image_column_index);
//                    picPath.add(img_path);
//                }
//            }
////            }
//        } else if (Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction())) {
////            if (intent.getType().startsWith("image/")) {
//
//            List<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//            if (imageUris != null) {
////处理多张图片
//                for (int i = 0; i < imageUris.size(); i++) {
//
//                    Log.v("从其他APP分享的", imageUris.get(i).getPath());
//
//
//                    if (imageUris.get(i).getPath().contains("external/video/media")) {
//
//                        String[] proj = {MediaStore.Video.Media.DATA};
//                        Cursor curVideo = activity.managedQuery(imageUris.get(i), proj, null, null, null);
//                        int actual_video_column_index = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
//                        curVideo.moveToFirst();
//                        String videoPath = curVideo.getString(actual_video_column_index);
//                        picPath.add(videoPath);
//                    }
//
//
//                    if (imageUris.get(i).getPath().contains("external/images/media")) {
//                        String[] proj = {MediaStore.Images.Media.DATA};
//                        Cursor actualimagecursor = activity.managedQuery(imageUris.get(i), proj, null, null, null);
//                        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                        actualimagecursor.moveToFirst();
//                        String img_path = actualimagecursor.getString(actual_image_column_index);
//                        picPath.add(img_path);
//                    }
//
//
////                    if (!imageUris.get(i).getPath().contains("external/images/media")) {
////                        picPath.add(imageUris.get(i).getPath());
////
////                    } else {
////                        String[] proj = {MediaStore.Images.Media.DATA};
////                        Cursor actualimagecursor = activity.managedQuery(imageUris.get(i), proj, null, null, null);
////                        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
////                        actualimagecursor.moveToFirst();
////                        String img_path = actualimagecursor.getString(actual_image_column_index);
////
////
////                        picPath.add(img_path);
////                    }
//                }
//            }
////            }
//
//        }
//
//        return picPath;
//    }


    private boolean isShowGuideCreate = false;

//    public LoadingAnimation getLoading() {
//        return this.loading;
//    }

    public int getPage() {
        return this.viewPager.getCurrentItem();
    }

    public void showGuideCreate(boolean show) {

//        float showPosition = createImg.getY()-144;
//        float hidePosition = createImg.getY()-24;

        float showPosition = createImg.getY() - showDistance;
        float hidePosition = createImg.getY() - hideDistance;

        if (show) {

            ViewPropertyAnimator alphaTo1 = rGudieCreate.animate();
            alphaTo1.setDuration(200).y(showPosition)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(1)
                    .start();

            isShowGuideCreate = true;

        } else {


            ViewPropertyAnimator alphaTo0 = rGudieCreate.animate();
            alphaTo0.setDuration(200).y(hidePosition)
                    .scaleX(0.0f)
                    .scaleY(0.0f)
                    .alpha(0)
                    .start();

            isShowGuideCreate = false;
        }

    }

//    public void showCollectionSnack(){
//        Snackbar snackbar = Snackbar.make(getBackground(), R.string.pinpinbox_2_0_0_toast_message_save_album_done, 4000).setAction(R.string.pinpinbox_2_0_0_itemtype_work_manager, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mActivity, MyCollect2Activity.class));
//                ActivityAnim.StartAnim(mActivity);
//            }
//        });
//
//        View view = snackbar.getView();
//
//        view.setBackgroundColor(Color.parseColor(ColorClass.MAIN_FIRST));
//
//
//        TextView tvMessage = (TextView) view.findViewById(R.id.snackbar_text);
//        Button btAction = (Button) view.findViewById(R.id.snackbar_action);
//
//
//        TextUtility.setBold(btAction, true);
//
//        tvMessage.setTextColor(Color.parseColor(ColorClass.WHITE));
//        btAction.setTextColor(Color.parseColor(ColorClass.WHITE));
//
//
//        snackbar.show();
//    }

    private void uriControl(Intent intent, Uri uri) {

        List<String> strPathPrefix1 = uri.getPathSegments();
        String param0 = strPathPrefix1.get(0);

        String value = getValue(intent);
        MyLog.Set("d", getClass(), "value => " + value);

        String a = uri.getPath();
        MyLog.Set("d", getClass(), "uri.getPath() => " + a);

        switch (param0) {
            case "index":
                MyLog.Set("d", getClass(), "param0 => index");
                String param1 = strPathPrefix1.get(1);
                if (param1.equals("highway")) {
                    HashMap<String, String> map = getValueMap(intent);
                    String type = map.get(Key.type);
                    String is_cooperation = "";

                    if (type.equals(Value.album)) {
                        strCooperationAlbumId = map.get(Key.type_id);
                        is_cooperation = map.get(Key.is_cooperation);
                        if (is_cooperation.equals("1")) {
                            doJoinCooperation();
                            return;
                        }
                    }
                }


                if (param1.equals(Key.album)) {

                    HashMap<String, String> map = getValueMap(intent);
//                                String type = map.get(Key.type);
                    String album_id = "";

                    album_id = map.get(Key.album_id);
                    toAlbumInfo(album_id);

                    return;

                }

                break;

            case "album":

                FlurryUtil.onEvent(FlurryKey.from_web_to_albuminfo);
                toAlbumInfo(value);
                break;

            case "template":


                break;

            case "creative":

                FlurryUtil.onEvent(FlurryKey.from_web_to_author);
                toAuthor(value);
                break;

            case "profile":

                FlurryUtil.onEvent(FlurryKey.from_web_to_myprefecture);
                toMePage();
                break;

            case "create":

                FlurryUtil.onEvent(FlurryKey.from_web_to_fastcreate);
                doFastCreate();
                break;

            case "event":

                FlurryUtil.onEvent(FlurryKey.from_web_to_event);
                toEvent(value);

                break;

            case "diy":
                toCreation(uri);

                break;


        }

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        try {
            Uri uri = intent.getData();
            try {
                if (uri != null) {


                    //20171018
                    List<Activity> acList = SystemUtility.SysApplication.getInstance().getmList();
                    for (int i = 0; i < acList.size(); i++) {

                        Activity ac = acList.get(i);
                        String strAcName = ac.getClass().getSimpleName();

                        if (!strAcName.equals(Main2Activity.class.getSimpleName())) {
                            ac.finish();
                        }
                    }

                    uriControl(intent, uri);


                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (view.getId()) {

//            case R.id.homeImg:
//
//
//                if (viewPager.getCurrentItem() == 0) {
//                    ((FragmentHome2) getFragment(FragmentHome2.class.getSimpleName())).scrollToTop();
//                    return;
//                }
//                viewPager.setCurrentItem(0, false);
//
//                break;

//            case R.id.searchImg:
//
//
//                if (viewPager.getCurrentItem() == 1) {
//                    ((FragmentSearch2) getFragment(FragmentSearch2.class.getSimpleName())).scrollToTop();
//                    return;
//                }
//                viewPager.setCurrentItem(1, false);
//
//                break;

//            case R.id.userImg:
//
//                Fragment fragment = getFragment(FragmentMe2.class.getSimpleName());
//
//                if (viewPager.getCurrentItem() == 2) {
//                    ((FragmentMe2) fragment).scrollToTop();
//                    return;
//                }
//
//
//                viewPager.setCurrentItem(2, false);
//
//
//                break;

            case R.id.rHome:

                if (viewPager.getCurrentItem() != 0) {
                    OtherAreaClikcToHome = true;
                }


                homeAnim();

                if (viewPager.getCurrentItem() == 0) {
                    ((FragmentHome2) getFragment(FragmentHome2.class.getSimpleName())).scrollToTop();
                    return;
                }
                viewPager.setCurrentItem(0, false);
                break;

            case R.id.rSearch:

                searchAnim();

                if (viewPager.getCurrentItem() == 1) {
                    ((FragmentSearch2) getFragment(FragmentSearch2.class.getSimpleName())).scrollToTop();
                    return;
                }


                viewPager.setCurrentItem(1, false);

                break;

            case R.id.rUser:

                Fragment fragment = getFragment(FragmentMe2.class.getSimpleName());

                if (viewPager.getCurrentItem() == 2) {
                    ((FragmentMe2) fragment).scrollToTop();
                    return;
                }


                viewPager.setCurrentItem(2, false);

                break;

            case R.id.rNotify:


                notifyAnim();

                if (viewPager.getCurrentItem() == 3) {
                    ((FragmentNotify2) getFragment(FragmentNotify2.class.getSimpleName())).scrollToTop();
                    return;
                }

                viewPager.setCurrentItem(3, false);


                break;

            case R.id.createImg:

                FlurryUtil.onEvent(FlurryKey.home_click_create);

                doFastCreate();

                break;


        }


    }


//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//
//            MyLog.Set("d", getClass(), "-------------landscape(横)");
//
//            ((FragmentHome2) getFragment(FragmentHome2.class.getSimpleName())).landscapeRecyclerView();
//
//
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            MyLog.Set("d", getClass(), "-------------portrait(直)");
//
//            ((FragmentHome2) getFragment(FragmentHome2.class.getSimpleName())).portraitRecyclerView();
//
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MyLog.Set("d", getClass(), "requestCode => " + requestCode);
        MyLog.Set("d", getClass(), "resultCode => " + resultCode);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {


        if (fragmentScanSearch2 != null && fragmentScanSearch2.isAdded()) {

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(fragmentScanSearch2).commit();
            return;
        }

        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
        int count = activityList.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String actName = activityList.get(i).getClass().getSimpleName();
                MyLog.Set("d", this.getClass(), "activity simplename => " + actName);
                if (!actName.equals("Main2Activity")) {
                    activityList.get(i).finish();
                }
            }
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity != null) {
                    Process.killProcess(Process.myPid());
                }
            }
        }, 200);

    }

    @Override
    protected void onPause() {

        if (createImg != null){
            createImg.setClickable(false);
        }


        super.onPause();
    }

    @Override
    public void onResume() {


        if (createImg != null){
            createImg.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createImg.setClickable(true);
                    MyLog.Set("e", mActivity.getClass(), "可以點擊建立作品了");
                }
            },1000);

        }

        super.onResume();
    }


    @Override
    public void onDestroy() {

        if (fastCreateTask != null && !fastCreateTask.isCancelled()) {
            fastCreateTask.cancel(true);
        }
        fastCreateTask = null;

//        RichText.recycle();

        Glide.get(getApplicationContext()).clearMemory();

        SystemUtility.SysApplication.getInstance().removeActivity(this);

        super.onDestroy();
    }


}

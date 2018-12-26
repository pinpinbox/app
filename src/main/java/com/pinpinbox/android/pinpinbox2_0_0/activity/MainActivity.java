package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flurry.android.FlurryAgent;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SampleTest.OldMainActivity;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.UrlUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.GAControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
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
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCategory;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentHome;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMe;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMenu;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentNotify;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentScanSearch;
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
public class MainActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private NoConnect noConnect;

    private SharedPreferences getdata, getAWSDetail;

    private FastCreateTask fastCreateTask;
    private FirstLoginTask firstLoginTask;
    private JoinCooperationTask joinCooperationTask;

    private FragmentScanSearch fragmentScanSearch;

    private RichPath userTop, userBottom;
    private RichPath notifyTop, notifyBottom;
    private RichPath insideLeft, insideCenter, insideRight, fillLarge, fillSmall;
    private RichPath menuTop, menuCenter, menuBottom;


    private List<ImageView> icons;

    private FragmentPagerItemAdapter adapter;

    private ViewPager viewPager;
    private RelativeLayout rNotify, rUser, rHome, rMenu;
    private ImageView createImg;
    private RichPathView svgNotify, svgUser, svgHome, svgMenu;
    private View vRPnotify, vRPmenu;
    private TextView tvTabHome, tvTabMe, tvTabNotify, tvTabMenu;

    private String strCooperationAlbumId = "";
    private String id, token;

    private int doingType;

    private boolean mainIsExist = false;
    private boolean fromAwsMessage = false;
    private boolean OtherAreaClikcToHome = false;
    private boolean scanIntent = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_main);

        setSwipeBackEnable(false);

        getdata = PPBApplication.getInstance().getData();

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

                    Intent intent = new Intent(mActivity, LoginActivity.class);
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

            Intent intent = new Intent(mActivity, LoginActivity.class);
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

        testbuttonImg = (ImageView) findViewById(R.id.testbutton);


        if(BuildConfig.FLAVOR.equals("platformvmage5_private")){
            testSet();
        } else if (BuildConfig.FLAVOR.equals("w3_private")) {
            testSet();
        } else if (BuildConfig.FLAVOR.equals("www_private")) {
            testSet();
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            testbuttonImg.setVisibility(View.GONE);
        }

        GAControl.sendUserId(id);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewControl.AlphaTo1(findViewById(R.id.rAlpha));
            }
        }, 600);

    }

    private ImageView testbuttonImg;

    private void testSet() {


        testbuttonImg.setVisibility(View.VISIBLE);

        testbuttonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mActivity, OldMainActivity.class);
                startActivity(intent);
                finish();
                ActivityAnim.StartAnim(mActivity);


            }
        });

        createImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FragmentHome fragmentHome = (FragmentHome) getFragment(FragmentHome.class.getSimpleName());
                if (testbuttonImg.getVisibility() == View.VISIBLE) {
                    testbuttonImg.setVisibility(View.GONE);
                    fragmentHome.hideShowTime(false);
                } else {
                    testbuttonImg.setVisibility(View.VISIBLE);
                    fragmentHome.hideShowTime(true);
                }
                return true;
            }
        });

    }


    private void checkMainExist() {

        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
        for (int i = 0; i < activityList.size(); i++) {
            String actName = activityList.get(i).getClass().getSimpleName();
            if (actName.equals("MainActivity")) {
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


            MyLog.Set("d", getClass(), "(MainActivity)scanIntent => " + scanIntent);

        }
    }

    private void init() {

        mActivity = this;

        getAWSDetail = getSharedPreferences(SharedPreferencesDataClass.awsDetail, Activity.MODE_PRIVATE);

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        /*防止crash 遺失銀幕長寬*/
        DensityUtility.setScreen(this);

        icons = new ArrayList<>();

        createImg = (ImageView) findViewById(R.id.createImg);
        viewPager = (ViewPager) findViewById(R.id.viewPager);


        rNotify = (RelativeLayout) findViewById(R.id.rNotify);
        rUser = (RelativeLayout) findViewById(R.id.rUser);
        rHome = (RelativeLayout) findViewById(R.id.rHome);
        rMenu = (RelativeLayout) findViewById(R.id.rMenu);

        svgNotify = (RichPathView) findViewById(R.id.svgNotify);
        svgUser = (RichPathView) findViewById(R.id.svgUser);
        svgHome = (RichPathView) findViewById(R.id.svgHome);
        svgMenu = (RichPathView) findViewById(R.id.svgMenu);

        tvTabHome = (TextView) findViewById(R.id.tvTabHome);
        tvTabMe = (TextView) findViewById(R.id.tvTabMe);
        tvTabNotify = (TextView) findViewById(R.id.tvTabNotify);
        tvTabMenu = (TextView) findViewById(R.id.tvTabMenu);

        vRPnotify = findViewById(R.id.vRPnotify);
        vRPmenu = findViewById(R.id.vRPmenu);

        viewPager.setOffscreenPageLimit(3);


        rUser.setOnClickListener(this);
        rNotify.setOnClickListener(this);
//        rSearch.setOnClickListener(this);
        rHome.setOnClickListener(this);
        rMenu.setOnClickListener(this);

        createImg.setOnClickListener(this);


        notifyTop = svgNotify.findRichPathByName("top");
        notifyBottom = svgNotify.findRichPathByName("bottom");

        userTop = svgUser.findRichPathByName("top");
        userBottom = svgUser.findRichPathByName("bottom");

//        searchInSide = svgSearch.findRichPathByName("inside");
//        searchOutSide = svgSearch.findRichPathByName("outside");
//        searchReflective = svgSearch.findRichPathByName("reflective");


        insideLeft = svgHome.findRichPathByName("insideLeft");
        insideCenter = svgHome.findRichPathByName("insideCenter");
        insideRight = svgHome.findRichPathByName("insideRight");
        fillLarge = svgHome.findRichPathByName("fillLarge");
        fillSmall = svgHome.findRichPathByName("fillSmall");

        menuTop = svgMenu.findRichPathByName("top");
        menuCenter = svgMenu.findRichPathByName("center");
        menuBottom = svgMenu.findRichPathByName("bottom");

        menuTop.setPivotToCenter(true);
        menuCenter.setPivotToCenter(true);
        menuBottom.setPivotToCenter(true);


    }

    private void setFragment() {

        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(mActivity)
                .add("", FragmentHome.class)
                .add("", FragmentMe.class)
                .add("", FragmentNotify.class)
                .add("", FragmentMenu.class)
                .create());

        viewPager.setAdapter(adapter);

    }

    public Fragment getFragment(String fragmentName) {

        @SuppressLint("RestrictedApi")
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

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


                switch (position) {
                    case 0://home

                        homePressed();
                        userNormal();
                        notifyNormal();
                        menuNormal();
                        break;


                    case 1://search



                        homeNormal();
                        userPressed();
                        notifyNormal();
                        menuNormal();

                        break;

                    case 2://user

                        homeNormal();
                        userNormal();
                        notifyPressed();
                        menuNormal();

                        if (vRPnotify.getVisibility() == View.VISIBLE) {
                            Fragment fragment = getFragment(FragmentNotify.class.getSimpleName());

                            if (((FragmentNotify) fragment).isGetPushQueue()) {
                                ((FragmentNotify) fragment).doRefresh(false);
                                vRPnotify.setVisibility(View.GONE);
                            } else {
                                vRPnotify.setVisibility(View.GONE);
                            }

                        }

                        break;

                    case 3://notify

                        homeNormal();
                        userNormal();
                        notifyNormal();
                        menuPressed();

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

    private void toAlbumInfo(String value) {
        ActivityIntent.toAlbumInfo(mActivity, false, value, null, 0, 0, 0, null);
    }

    private void toAuthor(String value, boolean openBoard) {

        ActivityIntent.toUser(mActivity, false, openBoard, value, null, null, null);

    }

    private boolean showBoard = false;

    public boolean getShowBoard() {
        return this.showBoard;
    }

    public void setShowBoard(boolean showBoard) {
        this.showBoard = showBoard;
    }

    public void toMePage(boolean showBoard) {

        this.showBoard = showBoard;

        viewPager.setCurrentItem(1, false);
    }

    private void toEvent(String value) {

        ActivityIntent.toEvent(mActivity, value);


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

            Intent intent = new Intent(mActivity, CreationActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            ActivityAnim.StartAnim(mActivity);
        }
    }

    public void toScan() {
        fragmentScanSearch = new FragmentScanSearch();
        if (!fragmentScanSearch.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.frame, fragmentScanSearch).commit();
        }

    }

    public void getAwsDetail() {
        if (fromAwsMessage) {
            String type = getAWSDetail.getString("type", "");

            if (type != null && !type.equals("null") && !type.equals("")) {

                String type_id = getAWSDetail.getString("type_id", "");

                switch (type) {
                    case "albumqueue":
                        toAlbumInfo(type_id);
                        break;

                    case "albumqueue@messageboard":
                        Bundle bundle = new Bundle();
                        bundle.putString(Key.album_id, type_id);
                        bundle.putBoolean(Key.pinpinboard, true);
                        bundle.putBoolean(Key.shareElement, false);
                        Intent intent = new Intent(mActivity, AlbumInfoActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        ActivityAnim.StartAnimFromBottom(mActivity);
                        break;

                    case "user@messageboard":
                        if (!type_id.equals("") && type_id != null) {
                            if (type_id.equals(id)) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toMePage(true);
                                    }
                                }, 500);
                            } else {
                                toAuthor(type_id, true);
                            }
                        }
                        break;

                    case "user":
                        toAuthor(type_id, false);
                        break;

                    case "profile":
                        toMePage(false);
                        break;
                }

            } else {


                String url = getAWSDetail.getString("url", "");

                if (url != null && !url.equals("null") && !url.equals("")) {

                    ActivityIntent.toWeb(mActivity, url, "");

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

    public void showRP_notify() {

        vRPnotify.setVisibility(View.VISIBLE);

        notifyAnim();


    }

    public void hideRP_notify() {
        vRPnotify.setVisibility(View.GONE);
    }


    public void showRP_menu() {
        vRPmenu.setVisibility(View.VISIBLE);

    }

    public void hide_menu() {
        vRPmenu.setVisibility(View.GONE);
    }

    /*menu icon*/
    private void menuNormal() {

        menuTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        menuCenter.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        menuBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));

        tvTabMenu.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

    }

    private void menuPressed() {

        menuTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));
        menuCenter.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));
        menuBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));

        tvTabMenu.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));


    }

    private void menuAnim() {

        RichPathAnimator
                .animate(menuTop)
                .interpolator(new DecelerateInterpolator())
                .translationY(0, 8, 0)
                .fillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey))
                .duration(300)

                .andAnimate(menuCenter)
                .interpolator(new DecelerateInterpolator())
                .fillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey))
                .duration(300)


                .andAnimate(menuBottom)
                .interpolator(new DecelerateInterpolator())
                .translationY(0, -8, 0)
                .fillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey))
                .duration(300)


                .start();

    }


    /*notify icon*/
    private void notifyNormal() {

        try {

            notifyTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
            notifyBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        } catch (Exception e) {
            e.printStackTrace();
        }


        tvTabNotify.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

    }

    private void notifyPressed() {

        try {

            notifyTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));
            notifyBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));

        } catch (Exception e) {
            e.printStackTrace();
        }

        tvTabNotify.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

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

        tvTabMe.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));


    }

    private void userPressed() {

        try {

            userTop.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));
            userBottom.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_first_grey));

        } catch (Exception e) {
            e.printStackTrace();
        }

        tvTabMe.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

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


    }

    /*home icon*/
    private void homeNormal() {


        insideLeft.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        insideCenter.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        insideRight.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        fillLarge.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));
        fillSmall.setFillColor(ContextCompat.getColor(mActivity, R.color.pinpinbox_2_0_0_second_grey));

        tvTabHome.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));


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


        tvTabHome.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));


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
                    Intent intent = new Intent(mActivity, CreationActivity.class);
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


                if (url == null || url.equals("") || url.equals("null")) {
                    d.getTvLink().setVisibility(View.GONE);
                } else {
                    d.getTvLink().setVisibility(View.VISIBLE);
                    d.getTvLink().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ActivityIntent.toWeb(mActivity, url, null);

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

            /*加入共用(權限為預覽者)*/
            protocol46();

            /*獲取zipped*/
            if (p46Result.equals("1")) {
                protocol17();
            }

//            /**此作品可下載並判斷作品更新時間*/
//            if (p46Result.equals("1") && p17Result.equals("1") && strZipped.equals("1")) {
//                protocol68();
//            }


            /*權限改成共用者並進入編輯器*/
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


                /*儲存封面*/

                Intent intent = new Intent(mActivity, ReaderActivity.class);
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


    public int getPage() {
        return this.viewPager.getCurrentItem();
    }

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
                    HashMap<String, String> map = UrlUtility.UrlToMapGetValue(intent);
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

                    HashMap<String, String> map = UrlUtility.UrlToMapGetValue(intent);
//                                String type = map.get(Key.type);
                    String album_id = "";

                    album_id = map.get(Key.album_id);
                    toAlbumInfo(album_id);

                    return;

                }


                if (param1 != null && param1.equals("index")) {


                    String param2 = strPathPrefix1.get(2);

                    if (param2 != null && param2.equals("adjustapp")) {

                        HashMap<String, String> map = UrlUtility.UrlToMapGetValue(intent);
                        String album_id = "";
                        album_id = map.get(Key.album_id);
                        toAlbumInfo(album_id);

                        return;
                    }


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
                toAuthor(value, false);
                break;

            case "profile":

                FlurryUtil.onEvent(FlurryKey.from_web_to_myprefecture);
                toMePage(false);
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

                        if (!strAcName.equals(MainActivity.class.getSimpleName())) {
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

        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }

        switch (view.getId()) {

            case R.id.rHome:

                if (viewPager.getCurrentItem() != 0) {
                    OtherAreaClikcToHome = true;
                }


                homeAnim();

                if (viewPager.getCurrentItem() == 0) {




                    ((FragmentHome) getFragment(FragmentHome.class.getSimpleName())).scrollToTop();
                    return;
                }
                viewPager.setCurrentItem(0, false);
                break;

            case R.id.rUser:

                Fragment fragment = getFragment(FragmentMe.class.getSimpleName());

                if (viewPager.getCurrentItem() == 1) {
                    ((FragmentMe) fragment).scrollToTop();
                    return;
                }


                viewPager.setCurrentItem(1, false);

                break;

            case R.id.rNotify:


                notifyAnim();

                if (viewPager.getCurrentItem() == 2) {
                    ((FragmentNotify) getFragment(FragmentNotify.class.getSimpleName())).scrollToTop();
                    return;
                }

                viewPager.setCurrentItem(2, false);


                break;

            case R.id.rMenu:

                menuAnim();

                viewPager.setCurrentItem(3, false);

                break;

            case R.id.createImg:

                FlurryUtil.onEvent(FlurryKey.home_click_create);

                doFastCreate();

                break;


        }


    }


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


        if (fragmentScanSearch != null && fragmentScanSearch.isAdded()) {

            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(fragmentScanSearch).commit();
            return;
        }

        FragmentHome fragmentHome = (FragmentHome) getFragment(FragmentHome.class.getSimpleName());

        FragmentCategory fragmentCategory = (FragmentCategory) getFragment(FragmentCategory.class.getSimpleName());

        if(fragmentHome!=null && fragmentHome.isSearch()){
            fragmentHome.hideSearch();
            return;
        }


        if(fragmentHome!=null && fragmentCategory!=null && !fragmentCategory.isHidden()){
            fragmentHome.hideCategory();
            return;
        }




        if(viewPager.getCurrentItem()!=0){
            viewPager.setCurrentItem(0,false);
            return;
        }



        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
        int count = activityList.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String actName = activityList.get(i).getClass().getSimpleName();
                MyLog.Set("d", this.getClass(), "activity simplename => " + actName);
                if (!actName.equals("MainActivity")) {
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
    public void onResume() {


        if (createImg != null) {
            createImg.setClickable(false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createImg.setClickable(true);
                    MyLog.Set("e", mActivity.getClass(), "可以點擊建立作品了");
                }
            }, 300);

        }

        super.onResume();
    }

    @Override
    protected void onPause() {

        if (createImg != null) {
            createImg.setClickable(false);
        }


        super.onPause();
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

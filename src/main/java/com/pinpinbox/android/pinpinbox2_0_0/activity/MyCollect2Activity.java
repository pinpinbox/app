package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCooperation2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMyUpLoad2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentOther2;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/1/6.
 */
public class MyCollect2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private LoadingAnimation loading;
    private CallbackManager callbackManager;

    private List<TextView> tabs;

    private FragmentPagerItemAdapter adapter;

    private RelativeLayout rActionBar;
    //    private SmartTabLayout viewPagerTab;
    private ViewPager viewPager;
    private ImageView backImg;
    private TextView tvTabMy, tvTabOther, tvTabShare;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_mycollect);


        SystemUtility.SysApplication.getInstance().addActivity(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        init();

        setFragment();

        setPageListener();


        getBundle();

    }


    private void init() {
        mActivity = this;

        loading = this.getLoading();

        tabs = new ArrayList<>();

        rActionBar = (RelativeLayout) findViewById(R.id.rActionBar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        backImg = (ImageView) findViewById(R.id.backImg);

        tvTabMy = (TextView) findViewById(R.id.tvTabMy);
        tvTabOther = (TextView) findViewById(R.id.tvTabOther);
        tvTabShare = (TextView) findViewById(R.id.tvTabShare);


        tabs.add(tvTabMy);
        tabs.add(tvTabOther);
        tabs.add(tvTabShare);

        /*default*/
        tabs.get(0).setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        TextUtility.setBold(tabs.get(0), true);


        viewPager.setOffscreenPageLimit(4);

        backImg.setOnClickListener(this);
        rActionBar.setOnClickListener(this);
        tvTabMy.setOnClickListener(this);
        tvTabOther.setOnClickListener(this);
        tvTabShare.setOnClickListener(this);




    }

    private void setFragment() {

        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(mActivity)
                .add("", FragmentMyUpLoad2.class)
                .add("", FragmentOther2.class)
                .add("", FragmentCooperation2.class)
                .create());

        viewPager.setAdapter(adapter);
//        viewPagerTab.setViewPager(viewPager);

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

    private void setPageListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                switch (position) {
                    case 0:
                        tabs.get(0).setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                        TextUtility.setBold(tabs.get(0), true);
                        tabs.get(1).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        TextUtility.setBold(tabs.get(1), false);
                        tabs.get(2).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        TextUtility.setBold(tabs.get(2), false);
                        break;
                    case 1:
                        tabs.get(0).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        TextUtility.setBold(tabs.get(0), false);
                        tabs.get(1).setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                        TextUtility.setBold(tabs.get(1), true);
                        tabs.get(2).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        TextUtility.setBold(tabs.get(2), false);
                        break;
                    case 2:
                        tabs.get(0).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        TextUtility.setBold(tabs.get(0), false);
                        tabs.get(1).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        TextUtility.setBold(tabs.get(1), false);
                        tabs.get(2).setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                        TextUtility.setBold(tabs.get(2), true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            int page = bundle.getInt("toPage", 0);

            viewPager.setCurrentItem(page);

        }


    }

    private void back() {

        boolean exitApp = true;

        final List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
        final int count = activityList.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String strAcName = activityList.get(i).getClass().getSimpleName();
                if (strAcName.equals(Main2Activity.class.getSimpleName())) {
                    exitApp = false;
                    break;
                }
            }
        }

        if (exitApp) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mActivity != null) {
                        Process.killProcess(Process.myPid());
                    }
                }
            }, 200);

        } else {
            finish();
            ActivityAnim.FinishAnim(mActivity);
        }

    }


    public CallbackManager getCallbackManager() {
        return this.callbackManager;
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.backImg:

                back();

                break;


            case R.id.tvTabMy:

                if(viewPager!=null){
                    viewPager.setCurrentItem(0, true);
                }

                break;

            case R.id.tvTabOther:

                if(viewPager!=null){
                    viewPager.setCurrentItem(1, true);
                }


                break;

            case R.id.tvTabShare:

                if(viewPager!=null){
                    viewPager.setCurrentItem(2, true);
                }


                break;











//            case R.id.rActionBar:
//
////                    getFragment();
//
//                int intPage = viewPager.getCurrentItem();
//
//                switch (intPage) {
//                    case 0:
//
////                            fragmentHome.scrollToTop();
//
//                        break;
//
//                    case 1:
//
//                        break;
//
//                    case 2:
//
//                        break;
//                }
//
//
//                break;

        }


    }


    public RelativeLayout getBackground() {
        return (RelativeLayout) findViewById(R.id.rBackground);
    }


//    @Override
//    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
//
//        LayoutInflater inflater = LayoutInflater.from(container.getContext());
//        View vTab = inflater.inflate(R.layout.tab_text, container, false);
//
//        TextView tvTab = (TextView) vTab.findViewById(R.id.tvTab);
//
//
//
//        switch (position) {
//            case 0:
//                tvTab.setText(R.string.pinpinbox_2_0_0_itemtype_collect_my);
//                tvTab.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
//
//                TextUtility.setBold(tvTab, true);
//
//                tabs.add(position, tvTab);
//                break;
//            case 1:
//                tvTab.setText(R.string.pinpinbox_2_0_0_itemtype_collect_other);
//                tabs.add(position, tvTab);
//                break;
//            case 2:
//                tvTab.setText(R.string.pinpinbox_2_0_0_itemtype_collect_cooperation);
//                tabs.add(position, tvTab);
//                break;
//
//
//            default:
//                throw new IllegalStateException("Invalid position: " + position);
//        }
//
//
//        return vTab;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {

        back();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {
            if (getNoConnect() == null) {
                setNoConnect();
            }
        }
    }

    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        callbackManager = null;

        if (loading != null && !loading.dialog().isShowing()) {
            loading.dismiss();
            loading = null;
        }

        Recycle.IMG(backImg);

        super.onDestroy();
    }


}

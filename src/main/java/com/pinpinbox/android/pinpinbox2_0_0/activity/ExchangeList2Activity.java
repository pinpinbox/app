package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentExchangeDone2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentExchangeUnfinished2;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2018/2/1.
 */

public class ExchangeList2Activity extends DraggerActivity implements View.OnClickListener {


    private Activity mActivity;
    private FragmentPagerItemAdapter adapter;

    private ViewPager vpExchange;
    private ImageView backImg;
    private TextView tvActionBarTitle, tvTabUnfinished, tvTabDone;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_exchangelist);

        SystemUtility.SysApplication.getInstance().addActivity(this);


        init();

        setFragment();

        setPageListener();

    }

    private void init() {

        mActivity = this;

        vpExchange = (ViewPager) findViewById(R.id.vpExchange);
        backImg = (ImageView) findViewById(R.id.backImg);
        tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        tvTabUnfinished = (TextView) findViewById(R.id.tvTabUnfinished);
        tvTabDone = (TextView) findViewById(R.id.tvTabDone);

        backImg.setOnClickListener(this);
        tvTabUnfinished.setOnClickListener(this);
        tvTabDone.setOnClickListener(this);

        TextUtility.setBold(tvActionBarTitle, true);
        ViewControl.AlphaTo1(tvActionBarTitle);

    }

    private void setFragment() {

        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(mActivity)
                .add("", FragmentExchangeUnfinished2.class)
                .add("", FragmentExchangeDone2.class)
                .create());

        vpExchange.setAdapter(adapter);

    }

    private void setPageListener() {

        vpExchange.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        focuseUnfinished();
                        break;
                    case 1:
                        focuseDone();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        /*set default*/

        focuseUnfinished();


    }

    private void focuseUnfinished() {
        tvTabUnfinished.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        TextUtility.setBold(tvTabUnfinished, true);
        tvTabDone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        TextUtility.setBold(tvTabDone, false);

    }

    private void focuseDone() {
        tvTabUnfinished.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        TextUtility.setBold(tvTabUnfinished, false);
        tvTabDone.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        TextUtility.setBold(tvTabDone, true);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.backImg:
                onBackPressed();
                break;

            case R.id.tvTabUnfinished:
                vpExchange.setCurrentItem(0, true);
                break;

            case R.id.tvTabDone:
                vpExchange.setCurrentItem(1, true);
                break;

        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {

        finish();
        ActivityAnim.FinishAnim(mActivity);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        Recycle.IMG(backImg);

        super.onDestroy();
    }

}

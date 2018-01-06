package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/10/25.
 */

public class Income2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private ImageView backImg;
    private TextView tvCountAccumulation, tvCountUnsettlement, tvCountWithdraw;
    private TextView tvTitle, tvBelowTitleText;
    private RelativeLayout rWithdraw;

    private String strRatio;
    private String strCompanyIdentity;

    private int intTotal, intUnsettlement, intWithdraw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_income);
        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();

        init();

        setData();


    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            intTotal = bundle.getInt(Key.sum, 0);
            intUnsettlement = bundle.getInt(Key.sumofunsettlement, 0);
            intWithdraw = bundle.getInt(Key.sumofsettlement, 0);

            strRatio = bundle.getString(Key.ratio, "");
            strCompanyIdentity = bundle.getString(Key.company_identity, "");

            MyLog.Set("e", getClass(), "intTotal => " + intTotal);
            MyLog.Set("e", getClass(), "intUnsettlement => " + intUnsettlement);
            MyLog.Set("e", getClass(), "intWithdraw => " + intWithdraw);

        }
    }

    private void init() {

        mActivity = this;


        rWithdraw = (RelativeLayout)findViewById(R.id.rWithdraw);
        backImg = (ImageView) findViewById(R.id.backImg);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvBelowTitleText = (TextView)findViewById(R.id.tvBelowTitleText);
        tvCountAccumulation = (TextView) findViewById(R.id.tvCountAccumulation);
        tvCountUnsettlement = (TextView) findViewById(R.id.tvCountUnsettlement);
        tvCountWithdraw = (TextView) findViewById(R.id.tvCountWithdraw);

        TextUtility.setBold(tvTitle, true);
        TextUtility.setBold(tvCountAccumulation, true);
        TextUtility.setBold(tvCountUnsettlement, true);
        TextUtility.setBold(tvCountWithdraw, true);

        backImg.setOnClickListener(this);


//        company<公司> / company_downline<公司下線> / general<一般> / personal<個人> / personal_downline<個人下線>
        if(strCompanyIdentity.equals("company_downline")){
            rWithdraw.setVisibility(View.GONE);
        }else {
            rWithdraw.setVisibility(View.VISIBLE);
        }



    }

    private void setData(){

        tvBelowTitleText.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_profit_percentage) + strRatio);

        tvCountAccumulation.setText(intTotal + "");

        tvCountUnsettlement.setText(intUnsettlement + "");

        tvCountWithdraw.setText(intWithdraw + "");
    }


    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);

    }

    @Override
    public void onClick(View v) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (v.getId()) {

            case R.id.backImg:

                back();

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
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        Recycle.IMG(backImg);
        Recycle.IMG((ImageView) findViewById(R.id.bgImg));

        System.gc();
        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


}

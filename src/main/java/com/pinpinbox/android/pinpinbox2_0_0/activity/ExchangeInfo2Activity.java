package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2018/2/1.
 */

public class ExchangeInfo2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private ImageView backImg;
    private TextView tvExchangeTime, tvName, tvDescription, tvContactTitle, tvRegisterPhone;
    private RoundCornerImageView exchangeImg;
    private ItemExchange itemExchange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_exchangeinfo);

        supportPostponeEnterTransition();

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();

        init();

        setData();


    }

    private void getBundle(){

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){

            itemExchange = (ItemExchange) bundle.getSerializable("exchangeItem");

        }
    }


    private void init() {

        mActivity = this;

        backImg = (ImageView) findViewById(R.id.backImg);
        exchangeImg = (RoundCornerImageView) findViewById(R.id.exchangeImg);

        tvExchangeTime = (TextView) findViewById(R.id.tvExchangeTime);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvContactTitle = (TextView) findViewById(R.id.tvContactTitle);
        tvRegisterPhone = (TextView) findViewById(R.id.tvRegisterPhone);

        TextUtility.setBold(tvExchangeTime, true);
        TextUtility.setBold(tvName, true);
        TextUtility.setBold(tvContactTitle, true);
        TextUtility.setBold(tvRegisterPhone, true);

        tvRegisterPhone.setOnClickListener(this);
        backImg.setOnClickListener(this);


        if (PPBApplication.getInstance().getData().getBoolean(Key.is_FB_Login, false)) {
            tvRegisterPhone.setVisibility(View.GONE);
        } else {
            tvRegisterPhone.setVisibility(View.VISIBLE);
        }


    }

    private void setData(){

        tvExchangeTime.setText(itemExchange.getTime());
        tvName.setText(itemExchange.getName());
        tvDescription.setText(itemExchange.getDescription());

        if (SystemUtility.Above_Equal_V5()) {
            exchangeImg.setTransitionName(itemExchange.getImage());
        }

        if (!itemExchange.getImage().equals("")) {

            Picasso.with(getApplicationContext())
                    .load(itemExchange.getImage())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .noFade()
                    .tag(getApplicationContext())
                    .into(exchangeImg, new Callback() {
                        @Override
                        public void onSuccess() {

                            supportStartPostponedEnterTransition();

                        }

                        @Override
                        public void onError() {

                            supportStartPostponedEnterTransition();

                        }
                    });


        }else {

            supportStartPostponedEnterTransition();

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.backImg:
                onBackPressed();
                break;

            case R.id.tvRegisterPhone:

                break;


        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {

        if (SystemUtility.getSystemVersion() >= SystemUtility.V5) {
            supportFinishAfterTransition();
        }else {
            finish();
            ActivityAnim.FinishAnim(mActivity);
        }

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

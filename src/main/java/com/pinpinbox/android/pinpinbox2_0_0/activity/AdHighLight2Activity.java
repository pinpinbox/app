package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import eightbitlab.com.blurview.BlurView;

/**
 * Created by vmage on 2017/11/22.
 */

public class AdHighLight2Activity extends FragmentActivity implements View.OnClickListener {

    private ImageView bannerImg;

    private TextView tvClose;
    private BlurView blurview;

    private String strBannerUrl;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_adfocus);

        setLastActivityBlur();

        init();

        supportPostponeEnterTransition();

        setShareElementAnim();

        saveUrl();

    }


    private void setLastActivityBlur() {

        blurview = (BlurView) findViewById(R.id.blurview);

        Activity activity = SystemUtility.getActivity(Main2Activity.class.getSimpleName());


        if (activity != null) {

            ViewControl.setBlur(activity, blurview, 4f);

        }

    }


    private void init() {


        bannerImg = (ImageView) findViewById(R.id.bannerImg);

        tvClose = (TextView) findViewById(R.id.tvClose);

        TextUtility.setBold((TextView) findViewById(R.id.tvTitle), true);


        tvClose.setOnClickListener(this);


    }


    private void setShareElementAnim() {
        strBannerUrl = getIntent().getExtras().getString(Key.image, "");


        if (SystemUtility.Above_Equal_V5()) {
            bannerImg.setTransitionName(strBannerUrl);
        }


        if (!strBannerUrl.equals("")) {
            Picasso.with(getApplicationContext())
                    .load(strBannerUrl)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .noFade()
                    .resize(1080, 428)//adjustViewBounds
                    .centerInside()
                    .tag(getApplicationContext())
                    .into(bannerImg, new Callback() {
                        @Override
                        public void onSuccess() {

                            MyLog.Set("d", AdHighLight2Activity.class, "onSuccess !strBannerUrl => " + strBannerUrl);

                            supportStartPostponedEnterTransition();

                            ViewControl.AlphaTo1(blurview);


                        }

                        @Override
                        public void onError() {

                            MyLog.Set("d", AdHighLight2Activity.class, "onError strBannerUrl => " + strBannerUrl);

                            supportStartPostponedEnterTransition();

                        }
                    });
        } else {

            supportStartPostponedEnterTransition();

        }

    }

    private void saveUrl() {

        String bannerList = PPBApplication.getInstance().getData().getString(Key.oldbannerUrlList, "[]");


        JSONArray bannerArray = null;

        try {

            bannerArray = new JSONArray(bannerList);

            bannerArray.put(strBannerUrl);

            PPBApplication.getInstance().getData().edit().putString(Key.oldbannerUrlList, bannerArray.toString()).commit();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void back() {

        if (SystemUtility.getSystemVersion() >= SystemUtility.V5) {


            ViewControl.AlphaTo0(blurview);


            supportFinishAfterTransition();

        } else {
            MyLog.Set("d", getClass(), "手機版本小於5.0");

            finish();
            overridePendingTransition(0, R.anim.bottom_exit);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tvClose:

                back();

                break;


        }


    }


    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}

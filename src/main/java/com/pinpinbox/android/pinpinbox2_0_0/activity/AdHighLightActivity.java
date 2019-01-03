package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

/**
 * Created by vmage on 2017/11/22.
 */

public class AdHighLightActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView bannerImg;

    private TextView tvClose;

    private View vDarkBg;

    private String strBannerUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_adfocus);


        init();

        supportPostponeEnterTransition();

        setShareElementAnim();

        saveUrl();

    }


    private void init() {

        vDarkBg = findViewById(R.id.vDarkBg);

        bannerImg = findViewById(R.id.bannerImg);

        tvClose = findViewById(R.id.tvClose);

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

                            MyLog.Set("d", AdHighLightActivity.class, "onSuccess !strBannerUrl => " + strBannerUrl);

                            supportStartPostponedEnterTransition();

                            ViewControl.AlphaTo1(vDarkBg);


                        }

                        @Override
                        public void onError() {

                            MyLog.Set("d", AdHighLightActivity.class, "onError strBannerUrl => " + strBannerUrl);

                            supportStartPostponedEnterTransition();

                        }
                    });
        } else {

            supportStartPostponedEnterTransition();

        }

    }

    private void saveUrl() {

        String bannerList = PPBApplication.getInstance().getData().getString(Key.oldbannerUrlList, "[]");

        //test/wip


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

            ViewControl.AlphaTo0(vDarkBg);
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

        if(strBannerUrl!=null && !strBannerUrl.equals("") && !strBannerUrl.equals("")) {
            Picasso.with(getApplicationContext()).invalidate(strBannerUrl);
            bannerImg.setImageBitmap(null);
            bannerImg.setImageDrawable(null);
        }

        super.onDestroy();
    }


}

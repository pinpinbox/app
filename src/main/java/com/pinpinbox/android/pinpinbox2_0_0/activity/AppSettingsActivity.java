package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.UrlClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.IntentControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;

import java.io.File;

/**
 * Created by vmage on 2017/5/22.
 */
public class AppSettingsActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private TextView tvCleanCache, tvAudioSound, tvCategoryVideoAutoplay, tvNorm, tvAboutUs, tvLogout;
    private ImageView audioSoundImg, categoryVideoAutoplayImg, backImg;

    private boolean soundEnable = true;
    private boolean videoAutoplayEnable = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_appsettings);
        SystemUtility.SysApplication.getInstance().addActivity(this);

        init();

        checkAutoPlayAudioType();

        checkAutoPlayVideoType();

    }

    private void init() {

        mActivity = this;

        soundEnable = PPBApplication.getInstance().getData().getBoolean(Key.soundEnable, true);
        videoAutoplayEnable = PPBApplication.getInstance().getData().getBoolean(Key.videoAutoplayEnable, true);

        tvCleanCache = (TextView) findViewById(R.id.tvCleanCache);
        tvAudioSound = (TextView) findViewById(R.id.tvAudioSound);
        tvCategoryVideoAutoplay = (TextView) findViewById(R.id.tvCategoryVideoAutoplay);
        tvNorm = (TextView) findViewById(R.id.tvNorm);
        tvAboutUs = (TextView) findViewById(R.id.tvAboutUs);
        tvLogout = (TextView) findViewById(R.id.tvLogout);

        audioSoundImg = (ImageView) findViewById(R.id.audioSoundImg);
        categoryVideoAutoplayImg = (ImageView) findViewById(R.id.categoryVideoAutoplayImg);
        backImg = (ImageView) findViewById(R.id.backImg);


        tvCleanCache.setOnClickListener(this);
        tvAudioSound.setOnClickListener(this);
        tvCategoryVideoAutoplay.setOnClickListener(this);
        tvNorm.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        backImg.setOnClickListener(this);


    }

    private void clean() {

        File file = new File(DirClass.sdPath);

        String[] allfilespath = file.list();

        if (allfilespath != null && allfilespath.length > 0) {

            String id = PPBApplication.getInstance().getId();

            for (int i = 0; i < allfilespath.length; i++) {

                String dirName = allfilespath[i];

                MyLog.Set("d", getClass(), "dirName => " + dirName);


                if ((dirName).equals("PinPinBox" + id)) {

                    FileUtility.delAllFile(DirClass.sdPath + dirName + "/" + DirClass.dirAlbumList);

                    FileUtility.delAllFile(DirClass.sdPath + dirName + "/" + DirClass.dirZip);

                    FileUtility.delAllFile(DirClass.sdPath + dirName + "/" + DirClass.dirCopy);

//                    FileUtility.deleteFileFolder(new File(DirClass.sdPath + allfilespath[i]));
                    PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_clean_done);

                }
            }


        } else {

            PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_never_donwload);

        }

    }

    private void checkAutoPlayAudioType() {
        if (soundEnable) {
            audioSoundImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        } else {
            audioSoundImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        }
    }

    private void changeAutoPlayAudioType() {


        if (soundEnable) {

            soundEnable = false;

            audioSoundImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        } else {

            soundEnable = true;

            audioSoundImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        }

        PPBApplication.getInstance().getData().edit().putBoolean(Key.soundEnable, soundEnable).apply();


    }


    private void checkAutoPlayVideoType(){

        if (videoAutoplayEnable) {
            categoryVideoAutoplayImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        } else {
            categoryVideoAutoplayImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        }

    }

    private void changeAutoPlayVideoType(){

        if (videoAutoplayEnable) {

            videoAutoplayEnable = false;

            categoryVideoAutoplayImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        } else {

            videoAutoplayEnable = true;

            categoryVideoAutoplayImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        }

        PPBApplication.getInstance().getData().edit().putBoolean(Key.videoAutoplayEnable, videoAutoplayEnable).apply();

    }


    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);


    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }


        switch (view.getId()) {

            case R.id.tvCleanCache:

                clean();

                break;

            case R.id.tvAudioSound:

                changeAutoPlayAudioType();


                break;

            case R.id.tvCategoryVideoAutoplay:

                changeAutoPlayVideoType();

                break;

            case R.id.tvNorm:
                Bundle bundle = new Bundle();
                bundle.putString(Key.url, UrlClass.platform);
                Intent itWeb = new Intent(mActivity, WebViewActivity.class);
                itWeb.putExtras(bundle);
                startActivity(itWeb);
                ActivityAnim.StartAnim(mActivity);
                break;

            case R.id.tvAboutUs:

                Bundle b = new Bundle();
                b.putString(Key.className, AppSettingsActivity.class.getSimpleName());

                Intent itAbout = new Intent(mActivity, GuidePageActivity.class);
                itAbout.putExtras(b);
                startActivity(itAbout);
                ActivityAnim.StartAnim(mActivity);
                break;

            case R.id.tvLogout:

                IntentControl.toLogin(mActivity, PPBApplication.getInstance().getId());

                break;


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
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);


        Recycle.IMG(backImg);
        Recycle.IMG((ImageView) findViewById(R.id.bgImg));

        System.gc();
        super.onDestroy();
    }


}

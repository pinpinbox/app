package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.StringClass.DirClass;
import com.pinpinbox.android.StringClass.UrlClass;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Widget.ActivityAnim;
import com.pinpinbox.android.Widget.IntentControl;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.PinPinToast;
import com.pinpinbox.android.Widget.Recycle;

import java.io.File;

/**
 * Created by vmage on 2017/5/22.
 */
public class AppSettings2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private TextView tvCleanCache, tvAudioSound, tvNorm, tvAboutUs, tvLogout;
    private ImageView audioSoundImg, backImg;

    private boolean soundEnable = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_appsettings);
        SystemUtility.SysApplication.getInstance().addActivity(this);

        init();

        checkAutoPlayAudioType();

    }

    private void init() {

        mActivity = this;

        soundEnable = PPBApplication.getInstance().getData().getBoolean(Key.soundEnable, true);

        tvCleanCache = (TextView) findViewById(R.id.tvCleanCache);
        tvAudioSound = (TextView) findViewById(R.id.tvAudioSound);
        tvNorm = (TextView) findViewById(R.id.tvNorm);
        tvAboutUs = (TextView) findViewById(R.id.tvAboutUs);
        tvLogout = (TextView) findViewById(R.id.tvLogout);

        audioSoundImg = (ImageView) findViewById(R.id.audioSoundImg);
        backImg = (ImageView) findViewById(R.id.backImg);


        tvCleanCache.setOnClickListener(this);
        tvAudioSound.setOnClickListener(this);
        tvNorm.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        backImg.setOnClickListener(this);


        TextUtility.setBold((TextView) findViewById(R.id.tvTitle), true);

        TextUtility.setBold(tvLogout, true);
        TextUtility.setBold(tvCleanCache, true);
        TextUtility.setBold(tvAudioSound, true);
        TextUtility.setBold(tvNorm, true);
        TextUtility.setBold(tvAboutUs, true);


    }

    private void clean() {

        File file = new File(DirClass.sdPath);

        String allfilespath[] = file.list();

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

        PPBApplication.getInstance().getData().edit().putBoolean(Key.soundEnable, soundEnable).commit();


    }


    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);


    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }


        switch (view.getId()) {

            case R.id.tvCleanCache:

                clean();

                break;

            case R.id.tvAudioSound:

                changeAutoPlayAudioType();


                break;

            case R.id.tvNorm:
                Bundle bundle = new Bundle();
                bundle.putString(Key.url, UrlClass.platform);
                Intent itWeb = new Intent(mActivity, WebView2Activity.class);
                itWeb.putExtras(bundle);
                startActivity(itWeb);
                ActivityAnim.StartAnim(mActivity);
                break;

            case R.id.tvAboutUs:

                Bundle b = new Bundle();
                b.putString(Key.className, AppSettings2Activity.class.getSimpleName());

                Intent itAbout = new Intent(mActivity, GuidePage2Activity.class);
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
        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MainActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.RedPointManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.RequestCodeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.UrlClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2018/5/30.
 */

public class FragmentMenu extends Fragment implements View.OnClickListener {

    private List<View> vRedPointList;

    private View vRPeditProfile, vRPworkManage, vRPmyFollow, vRPrecent, vRPbutPoint, vRPexchangeList, vRPsettings;
    private TextView tvLearnNow;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_menu, container, false);

        initView(v);

        setRPlist(v);

        setClick(v);

        return v;
    }

    private void initView(View v){

        vRPeditProfile = v.findViewById(R.id.vRPeditProfile);
        vRPworkManage = v.findViewById(R.id.vRPworkManage);
        vRPmyFollow = v.findViewById(R.id.vRPmyFollow);
        vRPrecent = v.findViewById(R.id.vRPrecent);
        vRPbutPoint = v.findViewById(R.id.vRPbutPoint);
        vRPexchangeList = v.findViewById(R.id.vRPexchangeList);
        vRPsettings = v.findViewById(R.id.vRPsettings);


        tvLearnNow = v.findViewById(R.id.tvLearnNow);

    }

    private void setRPlist(View v) {

        vRedPointList = new ArrayList<>();
        vRedPointList.add(vRPeditProfile);
        vRedPointList.add(vRPworkManage);
        vRedPointList.add(vRPmyFollow);
        vRedPointList.add(vRPrecent);
        vRedPointList.add(vRPbutPoint);
        vRedPointList.add(vRPexchangeList);
        vRedPointList.add(vRPsettings);

    }


    private void setClick(View v) {

        ( v.findViewById(R.id.linToEditProfile)).setOnClickListener(this);
        ( v.findViewById(R.id.linToWorkManage)).setOnClickListener(this);
        ( v.findViewById(R.id.linToMyFollow)).setOnClickListener(this);
        ( v.findViewById(R.id.linToRecent)).setOnClickListener(this);
        ( v.findViewById(R.id.linToBuyPoint)).setOnClickListener(this);
        ( v.findViewById(R.id.linToExchangeList)).setOnClickListener(this);
        ( v.findViewById(R.id.linSettings)).setOnClickListener(this);

        tvLearnNow.setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

    }

    private void init() {

    }

    private void removeRedPoint(View vRedPoint) {

        vRedPoint.setVisibility(View.GONE);

    }

    private void toCheckPermission() {

        ((MainActivity)getActivity()).commonCheckPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                RequestCodeClass.REQUEST_CODE_SDCARD,
                R.string.pinpinbox_2_0_0_dialog_message_open_permission_sdcard,
                new DraggerActivity.CheckPermissionCallBack() {
                    @Override
                    public void success() {
                        ActivityIntent.toWorkManage(getActivity());
                    }
                });

    }

    private void checkHideMenuRP() {

        boolean isRPexist = false;
        if (vRedPointList != null && vRedPointList.size() > 0) {
            for (int i = 0; i < vRedPointList.size(); i++) {
                if (vRedPointList.get(i).getVisibility() == View.VISIBLE) {
                    isRPexist = true;
                    break;
                }
            }
        } else {
            isRPexist = false;
        }


        Activity mActivity = SystemUtility.getActivity(MainActivity.class.getSimpleName());
        if (mActivity != null) {
            if (!isRPexist) {
                ((MainActivity) mActivity).hide_menu();
            }
        }


    }

    private void checkShowRedPoints() {

        if (PPBApplication.getInstance().getData().getBoolean(Key.checkRP_editProfile, false)) {
            vRPeditProfile.setVisibility(View.VISIBLE);
        } else {
            vRPeditProfile.setVisibility(View.GONE);
        }


        if (PPBApplication.getInstance().getData().getBoolean(Key.checkRP_workManage, false)) {
            vRPworkManage.setVisibility(View.VISIBLE);
        } else {
            vRPworkManage.setVisibility(View.GONE);
        }


        if (PPBApplication.getInstance().getData().getBoolean(Key.checkRP_myFollow, false)) {
            vRPmyFollow.setVisibility(View.VISIBLE);
        } else {
            vRPmyFollow.setVisibility(View.GONE);
        }


        if (PPBApplication.getInstance().getData().getBoolean(Key.checkRP_recent, false)) {
            vRPrecent.setVisibility(View.VISIBLE);
        } else {
            vRPrecent.setVisibility(View.GONE);
        }


        if (PPBApplication.getInstance().getData().getBoolean(Key.checkRP_butPoint, false)) {
            vRPbutPoint.setVisibility(View.VISIBLE);
        } else {
            vRPbutPoint.setVisibility(View.GONE);
        }


        if (PPBApplication.getInstance().getData().getBoolean(Key.checkRP_exchangeList, false)) {
            vRPexchangeList.setVisibility(View.VISIBLE);
        } else {
            vRPexchangeList.setVisibility(View.GONE);
        }


        if (PPBApplication.getInstance().getData().getBoolean(Key.checkRP_settings, false)) {
            vRPsettings.setVisibility(View.VISIBLE);
        } else {
            vRPsettings.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {

        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }


        switch (v.getId()) {

            case R.id.linToEditProfile:

                removeRedPoint(vRPeditProfile);
                RedPointManager.showOrHideOnEditProfile(false);
                checkHideMenuRP();
                ActivityIntent.toEditProfile(getActivity());

                break;

            case R.id.linToWorkManage:

                removeRedPoint(vRPworkManage);
                RedPointManager.showOrHideOnWorkManage(false);
                checkHideMenuRP();
                toCheckPermission();

                break;

            case R.id.linToMyFollow:

                removeRedPoint(vRPmyFollow);
                RedPointManager.showOrHideOnMyFollow(false);
                checkHideMenuRP();
                ActivityIntent.toMyFollow(getActivity());


                break;

            case R.id.linToRecent:

                removeRedPoint(vRPrecent);
                RedPointManager.showOrHideOnRecent(false);
                checkHideMenuRP();
                ActivityIntent.toRecent(getActivity());

                break;

            case R.id.linToBuyPoint:

                removeRedPoint(vRPbutPoint);
                RedPointManager.showOrHideOnBuyPoint(false);
                checkHideMenuRP();
                ActivityIntent.toBuyPoint(getActivity());

                break;

            case R.id.linToExchangeList:

                removeRedPoint(vRPexchangeList);
                RedPointManager.showOrHideOnExchangeList(false);
                checkHideMenuRP();
                ActivityIntent.toExchangeList(getActivity());

                break;

            case R.id.linSettings:

                removeRedPoint(vRPsettings);
                RedPointManager.showOrHideOnSettings(false);
                checkHideMenuRP();
                ActivityIntent.toSettings(getActivity());

                break;


            case R.id.tvLearnNow:

                ActivityIntent.toWeb(getActivity(), UrlClass.about, null);

                break;


        }


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that fragment is currently visible
        if (!isVisibleToUser && isResumed()) {
            // 调用Fragment隐藏的代码段

            MyLog.Set("e", getClass(), "目前看不到 FragmentMenu");


        } else if (isVisibleToUser && isResumed()) {
            // 调用Fragment显示的代码段

            MyLog.Set("e", getClass(), "可看見 FragmentMenu");
            checkShowRedPoints();


        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkShowRedPoints();



    }
}

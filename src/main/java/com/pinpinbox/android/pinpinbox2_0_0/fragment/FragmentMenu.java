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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Main2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.RedPointManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.richpath.RichPathView;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2018/5/30.
 */

public class FragmentMenu extends Fragment implements View.OnClickListener {

    private List<View> vRedPointList;

    private View vRPeditProfile, vRPworkManage, vRPmyFollow, vRPrecent, vRPbutPoint, vRPexchangeList, vRPsettings;


    private RichPathView svgMenu;


    private TextView reset;
    private RelativeLayout r1, r2, r3, r4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_menu, container, false);

        setRPlist(v);

        setBold(v);

        setClick(v);

        return v;
    }

    private void setRPlist(View v) {

        vRPeditProfile = v.findViewById(R.id.vRPeditProfile);
        vRPworkManage = v.findViewById(R.id.vRPworkManage);
        vRPmyFollow = v.findViewById(R.id.vRPmyFollow);
        vRPrecent = v.findViewById(R.id.vRPrecent);
        vRPbutPoint = v.findViewById(R.id.vRPbutPoint);
        vRPexchangeList = v.findViewById(R.id.vRPexchangeList);
        vRPsettings = v.findViewById(R.id.vRPsettings);

        vRedPointList = new ArrayList<>();
        vRedPointList.add(vRPeditProfile);
        vRedPointList.add(vRPworkManage);
        vRedPointList.add(vRPmyFollow);
        vRedPointList.add(vRPrecent);
        vRedPointList.add(vRPbutPoint);
        vRedPointList.add(vRPexchangeList);
        vRedPointList.add(vRPsettings);

    }

    private void setBold(View v) {
        TextUtility.setBold((TextView) v.findViewById(R.id.tvTitle), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvToEditProfile), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvToWorkManage), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvToMyFollow), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvToRecent), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvToBuyPoint), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvToExchangeList), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvSettings), true);
    }

    private void setClick(View v) {

        ((LinearLayout) v.findViewById(R.id.linToEditProfile)).setOnClickListener(this);
        ((LinearLayout) v.findViewById(R.id.linToWorkManage)).setOnClickListener(this);
        ((LinearLayout) v.findViewById(R.id.linToMyFollow)).setOnClickListener(this);
        ((LinearLayout) v.findViewById(R.id.linToRecent)).setOnClickListener(this);
        ((LinearLayout) v.findViewById(R.id.linToBuyPoint)).setOnClickListener(this);
        ((LinearLayout) v.findViewById(R.id.linToExchangeList)).setOnClickListener(this);
        ((LinearLayout) v.findViewById(R.id.linSettings)).setOnClickListener(this);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();


//        final RichPath menuTop = svgMenu.findRichPathByName("top");
//        final RichPath menuCenter = svgMenu.findRichPathByName("center");
//        final RichPath menuBottom = svgMenu.findRichPathByName("bottom");
//
//
//        menuTop.setPivotToCenter(true);
//        menuCenter.setPivotToCenter(true);
//        menuBottom.setPivotToCenter(true);
//
//
//        r1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                RichPathAnimator
//                        .animate(menuTop)
//                        .interpolator(new DecelerateInterpolator())
//                        .translationX(0, 5, -5, 5, -5, 0)
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//
//                        .andAnimate(menuCenter)
//                        .interpolator(new DecelerateInterpolator())
//                        .translationX(0, 5, -5, 5, -5, 0)
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//                        .startDelay(200)
//
//                        .andAnimate(menuBottom)
//                        .interpolator(new DecelerateInterpolator())
//                        .translationX(0, 5, -5, 5, -5, 0)
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//                        .startDelay(400)
//
//
//                        .start();
//
//
//            }
//        });
//
//
//        r2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                RichPathAnimator
//                        .animate(menuTop)
//                        .interpolator(new DecelerateInterpolator())
//                        .translationY(0, 8, 0)
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//
//                        .andAnimate(menuCenter)
//                        .interpolator(new DecelerateInterpolator())
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//
//
//                        .andAnimate(menuBottom)
//                        .interpolator(new DecelerateInterpolator())
//                        .translationY(0, -8, 0)
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//
//
//                        .start();
//
//
//            }
//        });
//
//        r3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//                RichPathAnimator
//                        .animate(menuTop)
//                        .interpolator(new DecelerateInterpolator())
//                        .scaleX(0.2f, 1f)
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//
//                        .andAnimate(menuCenter)
//                        .interpolator(new DecelerateInterpolator())
//                        .scaleX(0.2f, 1f)
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//                        .startDelay(200)
//
//
//                        .andAnimate(menuBottom)
//                        .interpolator(new DecelerateInterpolator())
//                        .scaleX(0.2f, 1f)
//                        .fillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey), ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_first_grey))
//                        .duration(300)
//                        .startDelay(400)
//
//                        .start();
//
//
//            }
//        });
//
//        r4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//
//            }
//        });
//
//
//        reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                menuTop.setFillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey));
//                menuCenter.setFillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey));
//                menuBottom.setFillColor(ContextCompat.getColor(getActivity(), R.color.pinpinbox_2_0_0_second_grey));
//
//                menuTop.setScaleX(1f);
//                menuCenter.setScaleX(1f);
//                menuBottom.setScaleX(1f);
//
//
//
//
//
//
//
//
//
//            }
//        });


    }

    private void init() {


    }

    private void removeRedPoint(View vRedPoint) {

        vRedPoint.setVisibility(View.GONE);

    }


    private static final int REQUEST_CODE_SDCARD = 105;
    private final int SUCCESS = 0;
    private final int REFUSE = -1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private int checkPermission(Activity ac, String permission) {

        int doingType = -9999;

        if (ActivityCompat.checkSelfPermission(ac, permission) == PackageManager.PERMISSION_GRANTED) {
            //已授權
            doingType = SUCCESS;
        } else {
            //未授權 判斷是否彈出詢問框 true => 彈出
            doingType = REFUSE;

        }
        return doingType;

    }

    @PermissionGrant(REQUEST_CODE_SDCARD)
    public void requestSdcardSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ActivityIntent.toWorkManage(getActivity());

            }
        }, 500);

    }

    @PermissionDenied(REQUEST_CODE_SDCARD)
    public void requestSdcardFailed() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");

            DialogV2Custom d = new DialogV2Custom(getActivity());
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_sdcard);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    SystemUtility.getAppDetailSettingIntent(getActivity());
                }
            });
            d.show();

        } else {
            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");

        }
    }

    private void toCheckPermission() {
        switch (checkPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            case SUCCESS:

                ActivityIntent.toWorkManage(getActivity());

                break;
            case REFUSE:
                MPermissions.requestPermissions(FragmentMenu.this, REQUEST_CODE_SDCARD, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
        }
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


        Activity mActivity = SystemUtility.getActivity(Main2Activity.class.getSimpleName());
        if (mActivity != null) {
            if (!isRPexist) {
                ((Main2Activity) mActivity).hide_menu();
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

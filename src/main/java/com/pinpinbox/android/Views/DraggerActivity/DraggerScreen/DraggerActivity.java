package com.pinpinbox.android.Views.DraggerActivity.DraggerScreen;

/**
 * Created by kevin9594 on 2016/8/28.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerMargin.SwipeBackActivityHelper;
import com.pinpinbox.android.Views.DraggerActivity.NoDragger.NoDraggerLayout;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackActivityInterface;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackLayout;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StatusControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;


public abstract class DraggerActivity extends FragmentActivity implements SwipeBackActivityInterface {

    private Activity mActivity = this;
    private DraggerBackHelper backActivityHelper;
    private LoadingAnimation loading;
    private NoConnect noConnect;
    private StatusControl statusControl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(statusControl==null) {

            statusControl = new StatusControl(mActivity, new SystemBarTintManager(mActivity));

        }

        initSwipeBack();

        loading = new LoadingAnimation(this);

    }

    private void initSwipeBack() {
        backActivityHelper = new DraggerBackHelper(this);
        backActivityHelper.onActivityCreate();
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
    }

    public StatusControl getStatusControl() {


        if (statusControl == null) {

            statusControl = new StatusControl(mActivity, new SystemBarTintManager(mActivity));

        }

        return this.statusControl;
    }

    public void setNoConnect() {
        noConnect = new NoConnect(this);
    }

    public NoConnect getNoConnect() {
        return this.noConnect;
    }

    public void startLoading() {
        if (loading != null) {
            loading.show();
        } else {
            loading = new LoadingAnimation(this);
            loading.show();
        }
    }

    public void dissmissLoading() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    public LoadingAnimation getLoading() {
        return this.loading;
    }

    public void setStatusDark() {

        if (statusControl != null) {

            statusControl.setStatusMode(StatusControl.DARK);

        }

    }

    public void setStatusLight() {

        if (statusControl != null) {

            statusControl.setStatusMode(StatusControl.LIGHT);

        }

    }

    public void setStatusColor(int color){

        if(statusControl!=null){

            statusControl.setStatusColor(color);

        }

    }

    public void setCurrentActivityStatusMode(){

        if (statusControl != null) {

           statusControl.setCurrentActivityMode();

        }


    }

    public void changeActivityStatusMode(int mode){


        if(statusControl!=null){

            statusControl.changeMode(mode);

        }

    }

    public void cancelTask(AsyncTask task) {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
    }




    private int REQUEST_CODE = 1;
    private CheckPermissionCallBack callBack;
    private int message;
    private String permission;


    public interface CheckPermissionCallBack{

        void success();

    }

    public void commonCheckPermission(String permission, int request_code, int message, CheckPermissionCallBack callBack){
        this.permission = permission;
        this.REQUEST_CODE = request_code;
        this.callBack = callBack;
        this.message = message;

        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            //尚未授權
            ActivityCompat.requestPermissions(this, new String[]{permission}, request_code);

        } else {

            //已授權
            callBack.success();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callBack.success();
                    }
                }, 500);

            }else {

                if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {

                    DialogV2Custom d = new DialogV2Custom(mActivity);
                    d.setStyle(DialogStyleClass.CHECK);
                    d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
                    d.setMessage(message);
                    d.setCheckExecute(new CheckExecute() {
                        @Override
                        public void DoCheck() {
                            SystemUtility.getAppDetailSettingIntent(mActivity);
                        }
                    });
                    d.show();

                }

            }

        }

    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        backActivityHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && backActivityHelper != null) {
            return backActivityHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return backActivityHelper.getSwipeBackLayout();
    }

    @Override
    public NoDraggerLayout getNoDraggerLayout() {
        return null;
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        SwipeBackActivityHelper.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void onResume() {

        super.onResume();
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

        if (loading != null && loading.dialog().isShowing()) {
            loading.dismiss();
        }


        super.onDestroy();
    }

}

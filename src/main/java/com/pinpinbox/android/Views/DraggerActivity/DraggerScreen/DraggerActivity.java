package com.pinpinbox.android.Views.DraggerActivity.DraggerScreen;

/**
 * Created by kevin9594 on 2016/8/28.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerMargin.SwipeBackActivityHelper;
import com.pinpinbox.android.Views.DraggerActivity.NoDragger.NoDraggerLayout;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackActivityInterface;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackLayout;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StatusControl;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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


    //    public View getRootView() {
////        return ((ViewGroup)this.findViewById(android.R.id.content)).getChildAt(0);
//
////        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////        getWindow().getDecorView().setBackgroundDrawable(null);
//
//
//        return getWindow().getDecorView().findViewById(android.R.id.content);
//    }

}

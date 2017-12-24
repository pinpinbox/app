package com.pinpinbox.android.Test;

/**
 * Created by kevin9594 on 2016/8/28.
 */

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerMargin.SwipeBackActivityHelper;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerBackHelper;
import com.pinpinbox.android.Views.DraggerActivity.NoDragger.NoDraggerLayout;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackActivityInterface;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackLayout;
import com.pinpinbox.android.Widget.NoConnect;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public abstract class TestDraggerActivity extends FragmentActivity implements SwipeBackActivityInterface {

    private DraggerBackHelper backActivityHelper;
    private LoadingAnimation loading;
    private NoConnect noConnect;
    private SystemBarTintManager sysBarManager;


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSwipeBack();

        setStatus();


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));



    }

    private void initSwipeBack() {
        backActivityHelper = new DraggerBackHelper(this);
        backActivityHelper.onActivityCreate();
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
    }



    private void setStatus() {








        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                sysBarManager = new SystemBarTintManager(TestDraggerActivity.this);
                sysBarManager.setStatusBarTintEnabled(true);
                sysBarManager.setStatusBarTintColor(Color.parseColor(ColorClass.STATUSBAR));
//                sysBarManager.setStatusBarTintColor(Color.parseColor(ColorClass.TRANSPARENT));
            }
        });

    }

    public void setStatusColor(int color){

        sysBarManager.setStatusBarTintColor(color);

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

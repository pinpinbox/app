package com.pinpinbox.android.Views.DraggerActivity.NoDragger;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.pinpinbox.android.Views.DraggerActivity.DraggerMargin.SwipeBackActivityHelper;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackActivityInterface;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackLayout;

/**
 * Created by vmage on 2016/9/5.
 */
public class NoDraggerMarginTopActivity extends FragmentActivity implements SwipeBackActivityInterface {

    private NoDraggerHelper backActivityHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSwipeBack();
    }

    private void initSwipeBack() {
        backActivityHelper = new NoDraggerHelper(this);
        backActivityHelper.onActivityCreate();
        NoDraggerLayout swipeBackLayout = getNoDraggerLayout();
//        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_BOTTOM);
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
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
        return null;
    }

    @Override
    public NoDraggerLayout getNoDraggerLayout() {
        return backActivityHelper.getNoDraggerLayout();
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

}


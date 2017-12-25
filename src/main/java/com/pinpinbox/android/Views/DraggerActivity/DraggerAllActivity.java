package com.pinpinbox.android.Views.DraggerActivity;

/**
 * Created by kevin9594 on 2016/7/8.
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public abstract class DraggerAllActivity extends FragmentActivity implements SwipeBackActivityInterface {

    private SwipeBackActivityHelper backActivityHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSwipeBack();
    }

    private void initSwipeBack() {
        backActivityHelper = new SwipeBackActivityHelper(this);
        backActivityHelper.onActivityCreate();
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
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
        return backActivityHelper.getSwipeBackLayout();
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

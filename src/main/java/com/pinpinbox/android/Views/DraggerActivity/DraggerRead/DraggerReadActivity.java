package com.pinpinbox.android.Views.DraggerActivity.DraggerRead;

/**
 * Created by kevin9594 on 2016/8/28.
 */
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.pinpinbox.android.Views.DraggerActivity.DraggerMargin.SwipeBackActivityHelper;
import com.pinpinbox.android.Views.DraggerActivity.NoDragger.NoDraggerLayout;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackActivityInterface;
import com.pinpinbox.android.Views.DraggerActivity.SwipeBackLayout;

public abstract class DraggerReadActivity extends FragmentActivity implements SwipeBackActivityInterface {

    private SwipeBackReadBackActivityHelper backActivityHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSwipeBack();
    }

    private void initSwipeBack() {
        backActivityHelper = new SwipeBackReadBackActivityHelper(this);
        backActivityHelper.onActivityCreate();
        SwipeBackLayout swipeBackLayout = getSwipeBackLayout();
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
    public NoDraggerLayout getNoDraggerLayout(){
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

}

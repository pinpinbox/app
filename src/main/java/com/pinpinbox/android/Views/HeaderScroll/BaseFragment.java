package com.pinpinbox.android.Views.HeaderScroll;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.pinpinbox.android.Views.HeaderScroll.scrollable.CanScrollVerticallyDelegate;
import com.pinpinbox.android.Views.HeaderScroll.scrollable.OnFlingOverListener;


/**
 * Created by kevin9594 on 2016/7/31.
 */
public abstract class BaseFragment extends Fragment implements CanScrollVerticallyDelegate, OnFlingOverListener {

    public static final String ARG_COLOR = "arg.Color";

    protected <V> V findView(View view, int id) {
        //noinspection unchecked
        return (V) view.findViewById(id);
    }


    protected int getColor() {
        return 0;
    }

    @Override
    public void onViewCreated(View view, Bundle sis) {
        super.onViewCreated(view, sis);

        view.setBackgroundColor(getColor());
    }


    public abstract CharSequence getTitle(Resources r);

    public abstract String getSelfTag();
}

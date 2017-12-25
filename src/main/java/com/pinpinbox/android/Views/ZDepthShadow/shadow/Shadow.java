package com.pinpinbox.android.Views.ZDepthShadow.shadow;

import android.graphics.Canvas;

import com.pinpinbox.android.Views.ZDepthShadow.ZDepthParam;


public interface Shadow {
    public void setParameter(ZDepthParam parameter, int left, int top, int right, int bottom);
    public void onDraw(Canvas canvas);
}

package com.pinpinbox.android.Views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.pinpinbox.android.R;

/**
 * Created by vmage on 2017/10/30.
 */

public class ScaleInRelativeLayout extends RelativeLayout{

    private Animation scaleSmallAnimation;
    private Animation scaleBigAnimation;

    public ScaleInRelativeLayout(Context context) {
        super(context);
    }

    public ScaleInRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleInRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private View view, vFocuse;

    public void setScaleView(View view, View vFocuse){
        this.view = view;
        this.vFocuse = vFocuse;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);



        /*如果無設定 scaleView 則選用rootView*/
        View vScale = null;

        if(view!=null){
            vScale = view;
        }else {
            vScale = getRootView();
        }

        vScale.invalidate();

        if (gainFocus) {


            scaleLarge(vScale);
        } else {
            scaleDefault(vScale);
        }
    }

    private void scaleDefault(View view) {
        if (scaleSmallAnimation == null) {
            scaleSmallAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_default);
        }
        startAnimation(scaleSmallAnimation);
    }

    private void scaleLarge(View view) {
        if (scaleBigAnimation == null) {
            scaleBigAnimation = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_large);
        }
        startAnimation(scaleBigAnimation);
    }


}

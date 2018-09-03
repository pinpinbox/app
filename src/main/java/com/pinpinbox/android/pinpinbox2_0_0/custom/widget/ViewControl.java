package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.RelativeLayout;

/**
 * Created by vmage on 2016/12/19.
 */
public class ViewControl {

    public static void AlphaTo1(View view){
        ViewPropertyAnimator alphaTo1 = view.animate();
        alphaTo1.setDuration(450)
                .alpha(1f)
                .start();
    }

    public static void AlphaTo1(View view, int duration){
        ViewPropertyAnimator alphaTo1 = view.animate();
        alphaTo1.setDuration(duration)
                .alpha(1f)
                .start();
    }

    public static void AlphaTo0(View view){

        ViewPropertyAnimator alphaTo0 = view.animate();
        alphaTo0.setDuration(450)
                .alpha(0f)
                .start();

    }

    public static void AlphaTo0(View view, int duration){

        ViewPropertyAnimator alphaTo0 = view.animate();
        alphaTo0.setDuration(duration)
                .alpha(0f)
                .start();

    }



    public static void reset(View root) {
        root.setScaleX(1);
        root.setScaleY(1);
        root.setAlpha(1);
    }


    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    /*
* 获取控件宽
*/
    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredWidth());
    }

    /*
    * 获取控件高
    */
    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return (view.getMeasuredHeight());
    }

    /*
    * 设置控件所在的位置X，并且不改变宽高，
    * X为绝对位置，此时Y可能归0
    */
    public static void setLayoutX(View view, int x) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, margin.topMargin, x + margin.width, margin.bottomMargin);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    /*
    * 设置控件所在的位置Y，并且不改变宽高，
    * Y为绝对位置，此时X可能归0
    */
    public static void setLayoutY(View view, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(margin.leftMargin, y, margin.rightMargin, y + margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    /*
    * 设置控件所在的位置YY，并且不改变宽高，
    * XY为绝对位置
    */
    public static void setLayout(View view, int x, int y) {
        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, x + margin.width, y + margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }


}

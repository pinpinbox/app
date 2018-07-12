package com.pinpinbox.android.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

public class DismissScrollView extends ScrollView implements View.OnTouchListener {


    private static final int START_DRAG_DISTANCE = 36;

    private static final int START_ALPHA_ANIM_DISTANCE = 50;


    private GestureDetector mGestureDetector;

    private int topDistance = 0;


    public DismissScrollView(Context context) {
        super(context);
        setGD(context);
    }

    public DismissScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGD(context);
    }

    public DismissScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGD(context);
    }

    public DismissScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setGD(context);
    }


    private View vTouchRange = null;


    public void setvTouchRange(View vTouchRange) {
        this.vTouchRange = vTouchRange;
    }


    private void setGD(Context context) {

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {


            private float START_DRAG_X = 0;
            private boolean getDragPosition = false;


            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {


                float scrollY = e2.getY() - e1.getY();
                float scrollX = e2.getX() - e1.getX();


                if (scrollY > START_DRAG_DISTANCE) {


                    vTouchRange.setTranslationX(getTranslationX() + scrollX);
                    vTouchRange.setTranslationY(getTranslationY() + scrollY - START_DRAG_DISTANCE);


                    MyLog.Set("e", DismissScrollView.class, "scrollY => " + scrollY);
                    MyLog.Set("e", DismissScrollView.class, "scrollX => " + scrollX);


                }


                return true;
            }
        });

    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        topDistance = t;


        if (t > START_ALPHA_ANIM_DISTANCE) {


        }


    }

    float lastX = 0;
    float lastY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mGestureDetector.onTouchEvent(event);

        float x = event.getRawX();
        float y = event.getRawY();

        if (event.getAction() == MotionEvent.ACTION_UP) {

//            vTouchRange.setTranslationX(0);
//            vTouchRange.setTranslationY(0);

            vTouchRange.animate().translationX(0).setDuration(200);
            vTouchRange.animate().translationY(0).setDuration(200);


        }
//        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//
//
//            float deltaX = x - lastX;
//            float deltaY = y - lastY;
//            float translationX = vTouchRange.getTranslationX() + deltaX;
//            float translationY = vTouchRange.getTranslationY() + deltaY;
//
//
//            MyLog.Set("e", DismissScrollView.class, "tY => " + translationY);
//            MyLog.Set("e", DismissScrollView.class, "topDistance => " + topDistance);
//            MyLog.Set("e", DismissScrollView.class, "deltaY => " + deltaY);
//
//
//
//            if (translationY > START_DRAG_DISTANCE){
//                vTouchRange.setTranslationX(translationX);
//                vTouchRange.setTranslationY(translationY - START_DRAG_DISTANCE);
//
//            }
//
//
//
//
//
//        }
//
//        lastX = x;
//        lastY = y;


        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

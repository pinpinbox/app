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

    /* set drag x*/
    private float START_DRAG_X = 0;
    private float START_DRAG_Y = 0;
    private boolean getDragPosition = false;
    private boolean isDragging = false;
    /* *********************/


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

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                if(e1==null || e2 == null){
                    return true;
                }


                float scrollY = e2.getY() - e1.getY();
                float scrollX = e2.getX() - e1.getX();

                if (scrollY > START_DRAG_DISTANCE && topDistance == 0) {
                    isDragging = true;
                }


                if(isDragging){

                    if (!getDragPosition) {
                        getDragPosition = true;
                        START_DRAG_X = scrollX;
                        START_DRAG_Y = scrollY - START_DRAG_DISTANCE;
                    }


                    vTouchRange.setTranslationX(getTranslationX() + scrollX - START_DRAG_X);
                    vTouchRange.setTranslationY(getTranslationY() + scrollY - START_DRAG_DISTANCE - START_DRAG_Y);


                    MyLog.Set("d", DismissScrollView.class, "scrollY => " + scrollY);
                    MyLog.Set("d", DismissScrollView.class, "scrollX => " + scrollX);
                    MyLog.Set("e", DismissScrollView.class, "START_DRAG_X => " + START_DRAG_X);
                    MyLog.Set("e", DismissScrollView.class, "START_DRAG_Y => " + START_DRAG_Y);

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        //先执行滑屏事件
        mGestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {

            vTouchRange.animate().translationX(0).setDuration(200);
            vTouchRange.animate().translationY(0).setDuration(200);

            START_DRAG_X = 0;
            getDragPosition = false;
            isDragging = false;

        }


        super.dispatchTouchEvent(event);


        //return true => do onTouchEvent
        return true;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {



        return false;
    }
}

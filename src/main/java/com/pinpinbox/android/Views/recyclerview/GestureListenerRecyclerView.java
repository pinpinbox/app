package com.pinpinbox.android.Views.recyclerview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;


public class GestureListenerRecyclerView extends RecyclerView {
    public GestureListenerRecyclerView(Context context) {
        super(context);
    }

    public GestureListenerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureListenerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    private GestureDetector mGestureDetector;




    public void enableGesture(Activity mActivity){

         mGestureDetector = new GestureDetector(mActivity, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {


                MyLog.Set("e", GestureListenerRecyclerView.class, "-------------------onFling---------------------");

                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onDown(MotionEvent e) {

                MyLog.Set("e", GestureListenerRecyclerView.class, "onDown");
                return super.onDown(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {



                MyLog.Set("e", GestureListenerRecyclerView.class, "onScroll");


                return true;
            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {


        if(mGestureDetector!=null){
            mGestureDetector.onTouchEvent(e);
        }


        return super.onTouchEvent(e);
    }
}

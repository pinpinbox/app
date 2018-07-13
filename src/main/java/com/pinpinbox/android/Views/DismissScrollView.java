package com.pinpinbox.android.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ScrollView;

import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

public class DismissScrollView extends ScrollView implements View.OnTouchListener {


    private static final int START_DRAG_DISTANCE = 36;

    private static final int DISMISS_DISTANCE = 130;

    private GestureDetector mGestureDetector;

    private View vTouchRange = null; //觸發拖拽範圍

    private View[] viewsAlpha; //拖拽時漸隱

    private Activity mActivity = null;

    /* drag control*/
    private float START_DRAG_X = 0;
    private float START_DRAG_Y = 0;
    private boolean getDragPosition = false;
    private boolean isDragging = false;
    private boolean dismissAC = false;
    private boolean isAlpha = false;
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









    private void setGD(final Context context) {

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {

                MyLog.Set("d", DismissScrollView.class, "onDown");
                return super.onDown(e);
            }

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

                    MyLog.Set("d", DismissScrollView.class, "isDragging");

                    if (!getDragPosition) {
                        getDragPosition = true;
                        START_DRAG_X = scrollX;
                        START_DRAG_Y = scrollY - START_DRAG_DISTANCE;
                    }


                    vTouchRange.setTranslationX(getTranslationX() + scrollX - START_DRAG_X);
                    vTouchRange.setTranslationY(getTranslationY() + scrollY - START_DRAG_DISTANCE - START_DRAG_Y);


                    /*判斷是否已處於透明狀態*/
                    if(!isAlpha) {
                        isAlpha = true;
                        if (viewsAlpha!=null && viewsAlpha.length > 0) {
                            for (View viewAlpha : viewsAlpha) {
                                if (viewAlpha != null) {
                                    ViewPropertyAnimator alphaTo0 = viewAlpha.animate();
                                    alphaTo0.setDuration(200)
                                            .alpha(0)
                                            .start();
                                }
                            }
                        }
                    }


                    /*判斷超過關閉視窗距離*/
                    if (scrollY > DISMISS_DISTANCE || scrollX > DISMISS_DISTANCE) {
                        dismissAC = true;
                    } else {
                        dismissAC = false;
                    }









                }



                return true;
            }
        });

    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        topDistance = t;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            float statusAlpha = (t <= 0) ? 1 : (100 / ((float) t * 1f));

            MyLog.Set("d", DismissScrollView.class, "alpha => " + statusAlpha);

            if(mActivity!=null) {
                if (statusAlpha < 0.3) {
                    mActivity.getWindow().setStatusBarColor(Color.argb(245, 255, 255, 255));
                } else if (statusAlpha >= 1) {
                    mActivity.getWindow().setStatusBarColor(Color.argb(0, 255, 255, 255));
                } else {
                    float a = statusAlpha * 255;
                    mActivity.getWindow().setStatusBarColor(Color.argb(255 - (int) a, 255, 255, 255));
                }
            }


        }



    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        //先执行滑屏事件
        mGestureDetector.onTouchEvent(event);


        if(!isDragging){
            super.dispatchTouchEvent(event);
        }


        if (event.getAction() == MotionEvent.ACTION_UP) {



            if(dismissAC){
                dismissAC = false;
                if (closeActivityListener != null) {
                    closeActivityListener.close();
                }
            }else {

                if (viewsAlpha!=null && viewsAlpha.length > 0) {
                    for (View viewAlpha : viewsAlpha) {
                        if (viewAlpha != null) {
                            ViewPropertyAnimator alphaTo1 = viewAlpha.animate();
                            alphaTo1.setDuration(200)
                                    .alpha(1)
                                    .start();
                        }
                    }
                }


                vTouchRange.animate().translationX(0).translationY(0).setDuration(200);
                START_DRAG_X = 0;
                START_DRAG_Y = 0;
                getDragPosition = false;
                isDragging = false;
                isAlpha = false;



            }












        }



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











    public void setvTouchRange(View vTouchRange) {
        this.vTouchRange = vTouchRange;
    }

    public void setViewsAlpha(View...views){
        this.viewsAlpha = views;
    }


    public void setActivity(Activity mActivity){
        this.mActivity = mActivity;
    }


    public interface CloseActivityListener {
        void close();
    }

    public void setCloseActivityListener(CloseActivityListener closeActivityListener) {
        this.closeActivityListener = closeActivityListener;
    }

    private CloseActivityListener closeActivityListener;





}

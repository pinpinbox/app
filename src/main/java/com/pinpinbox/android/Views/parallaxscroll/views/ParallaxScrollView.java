package com.pinpinbox.android.Views.parallaxscroll.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ScrollView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.ArrayList;

public class ParallaxScrollView extends ScrollView implements View.OnTouchListener {

    private Activity mActivity;
    private Context context;
    private static final int DEFAULT_PARALLAX_VIEWS = 1;
    private static final float DEFAULT_INNER_PARALLAX_FACTOR = 1.9F;
    private static final float DEFAULT_PARALLAX_FACTOR = 1.9F;
    private static final float DEFAULT_ALPHA_FACTOR = -1F;
    private int numOfParallaxViews = DEFAULT_PARALLAX_VIEWS;
    private float innerParallaxFactor = DEFAULT_PARALLAX_FACTOR;
    private float parallaxFactor = DEFAULT_PARALLAX_FACTOR;
    private float alphaFactor = DEFAULT_ALPHA_FACTOR;
    private ArrayList<ParallaxedView> parallaxedViews = new ArrayList<ParallaxedView>();


    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ParallaxScrollView(Context context) {
        super(context);
    }

    protected void init(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ParallaxScroll);
        this.parallaxFactor = typeArray.getFloat(R.styleable.ParallaxScroll_parallax_factor, DEFAULT_PARALLAX_FACTOR);
        this.alphaFactor = typeArray.getFloat(R.styleable.ParallaxScroll_alpha_factor, DEFAULT_ALPHA_FACTOR);
        this.innerParallaxFactor = typeArray.getFloat(R.styleable.ParallaxScroll_inner_parallax_factor, DEFAULT_INNER_PARALLAX_FACTOR);
        this.numOfParallaxViews = typeArray.getInt(R.styleable.ParallaxScroll_parallax_views_num, DEFAULT_PARALLAX_VIEWS);
        typeArray.recycle();

        this.context = context;


        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            //监听双击手势
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                Log.d("data", "点击了两次按钮！ ");
//                return true;
//            }

            //监听滑动手势
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//                return super.onFling(e1, e2, velocityX, velocityY);
//            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                // 参数解释：
                // e1：第1个ACTION_DOWN MotionEvent
                // e2：最后一个ACTION_MOVE MotionEvent
                // velocityX：X轴上的移动速度，像素/秒
                // velocityY：Y轴上的移动速度，像素/秒

                // 触发条件 ：
                // X轴的坐标位移大于FLING_MIN_DISTANCE，且移动速度大于FLING_MIN_VELOCITY个像素/秒

//                if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
//                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//                    // Fling left
//                    Toast.makeText(getContext(), "Fling Left", Toast.LENGTH_SHORT).show();
//                } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
//                        && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//                    // Fling right
//                    Toast.makeText(getContext(), "Fling Right", Toast.LENGTH_SHORT).show();
//                }

                return false;
            }


            //监听拖动的手势
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //设置控件跟随手势移动


                if (top == 0 && e1 != null && e2 != null) {


                    MyLog.Set("e", ParallaxScrollView.class, (e2.getY() - e1.getY()) + "");

                    float scrollX = e2.getX() - e1.getX();
                    float scrollY = e2.getY() - e1.getY();


                    if (scrollY > 36) {
                        freeScroll = true;
                    }

                    if (freeScroll) {

                        vDismiss.setTranslationY((getTranslationY() + scrollY) - 36);
                        vDismiss.setTranslationX(getTranslationX() + scrollX);


//                        if (!isTranslucent) {
//                            isTranslucent = true;
//                            SwipeBackActivityHelper.convertActivityToTranslucent(mActivity);//translucent = true
//                        }
//                        float windowalpha = 1 - scrollY / 3000;
//                        if (windowalpha >= 0f && windowalpha <= 1f) {
//                            wParams.alpha = (windowalpha);
//                            mActivity.getWindow().setAttributes(wParams);
//                        }


                        if (!isAlpha) {
                            isAlpha = true;
                            if (viewsAlpha.length > 0) {
                                for (int i = 0; i < viewsAlpha.length; i++) {

                                    if (viewsAlpha[i] != null) {

                                        ViewPropertyAnimator alphaTo0 = viewsAlpha[i].animate();
                                        alphaTo0.setDuration(150)
                                                .alpha(0)
                                                .start();
                                        final int finalI = i;
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                viewsAlpha[finalI].setVisibility(GONE);
                                            }
                                        }, 200);

                                    }

                                }

                            }

                        }

                        if (scrollY > 130 || scrollX > 130) {
                            closeActivity = true;
                        } else {
                            closeActivity = false;
                        }

                    }

                    onScroll = true;

                }

                return true;
            }
        });
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        makeViewsParallax();
    }


    //背景透明相關
//    private WindowManager.LayoutParams wParams;
//    private boolean isTranslucent = false;

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
//        wParams = mActivity.getWindow().getAttributes();
    }


    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        top = t;

        float parallax = parallaxFactor;
        float alpha = alphaFactor;


        for (ParallaxedView parallaxedView : parallaxedViews) {
            parallaxedView.setOffset((float) t / parallax);

            if (t > 50) {

                t = t - 50;

                parallax *= innerParallaxFactor;
                if (alpha != DEFAULT_ALPHA_FACTOR) {
                    float fixedAlpha = (t <= 0) ? 1 : (100 / ((float) t * alpha));


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (fixedAlpha < 0.3) {
                            mActivity.getWindow().setStatusBarColor(Color.argb(245, 255, 255, 255));
                        } else if (fixedAlpha >= 1) {
                            mActivity.getWindow().setStatusBarColor(Color.argb(0, 255, 255, 255));
                        } else {
                            float a = fixedAlpha * 255;
                            mActivity.getWindow().setStatusBarColor(Color.argb(255 - (int) a, 255, 255, 255));

                        }


                    }

                    parallaxedView.setAlpha(fixedAlpha);

                    alpha /= alphaFactor;
                }
                parallaxedView.animateNow();

            } else {


                parallax *= innerParallaxFactor;
                if (alpha != DEFAULT_ALPHA_FACTOR) {

                    parallaxedView.setAlpha(1f);


                    alpha /= alphaFactor;
                }
                parallaxedView.animateNow();


            }


        }


    }

    private void makeViewsParallax() {
        if (getChildCount() > 0 && getChildAt(0) instanceof ViewGroup) {
            ViewGroup viewsHolder = (ViewGroup) getChildAt(0);
            int numOfParallaxViews = Math.min(this.numOfParallaxViews, viewsHolder.getChildCount());
            for (int i = 0; i < numOfParallaxViews; i++) {
                ParallaxedView parallaxedView = new ScrollViewParallaxedItem(viewsHolder.getChildAt(i));
                parallaxedViews.add(parallaxedView);
            }
        }
    }

    private GestureDetector mGestureDetector;

    private int top = 0;

    private boolean freeScroll = false;
    private boolean onScroll = false;
    private boolean closeActivity = false;
    private boolean isAlpha = false;

    private View vDismiss;
    private View[] viewsAlpha;

    public void setScrollDismissView(View vDismiss) {
        this.vDismiss = vDismiss;
    }

    public void setAlphaView(View... viewsAlpha) {
        this.viewsAlpha = viewsAlpha;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取view相对于手机屏幕的xy值
//        if(top==0) {
//            int x = (int) event.getRawX();
//            int y = (int) event.getRawY();
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    int deltaX = x - lastX;
//                    int deltaY = y - lastY;
//                    int translationX = (int) (ViewHelper.getTranslationX(this) + deltaX);
//                    int translationY = (int) (ViewHelper.getTranslationY(this) + deltaY);
//                    ViewHelper.setTranslationX(this, translationX);
//                    ViewHelper.setTranslationY(this, translationY);
//
//                    break;
//                case MotionEvent.ACTION_UP:
//
//                    ViewHelper.setTranslationX(this, 0);
//                    ViewHelper.setTranslationY(this, 0);
//
//                    break;
//                default:
//                    break;
//            }
//            lastX = x;
//            lastY = y;
//        }
//        return true;

        mGestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {

            if (onScroll) {

                if (closeActivity) {
                    MyLog.Set("e", ParallaxScrollView.class, "關閉Activity");
                    closeActivity = false;

                    if (closeActivityListener != null) {

//                        SwipeBackActivityHelper.convertActivityFromTranslucent(mActivity);//translucent = false

                        closeActivityListener.close();
                    }


                } else {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (viewsAlpha.length > 0) {

                                for (int i = 0; i < viewsAlpha.length; i++) {

                                    if (viewsAlpha[i] != null) {
                                        viewsAlpha[i].setVisibility(VISIBLE);
                                        ViewPropertyAnimator alphaTo1 = viewsAlpha[i].animate();
                                        alphaTo1.setDuration(150)
                                                .alpha(1)
                                                .start();
                                    }

                                }

                            }

                        }
                    }, 200);

                    vDismiss.setTranslationX(0);
                    vDismiss.setTranslationY(0);


                    isAlpha = false;
                    onScroll = false;
                    freeScroll = false;


//                    wParams.alpha = 1f;
//                    mActivity.getWindow().setAttributes(wParams);
//                    isTranslucent = false;

                }


            }
        }

        return super.onTouchEvent(event);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }


    protected class ScrollViewParallaxedItem extends ParallaxedView {

        public ScrollViewParallaxedItem(View view) {
            super(view);
        }

        @Override
        protected void translatePreICS(View view, float offset) {
            view.offsetTopAndBottom((int) offset - lastOffset);
            lastOffset = (int) offset;
        }
    }


    public interface CloseActivityListener {
        void close();
    }

    public void setCloseActivityListener(CloseActivityListener closeActivityListener) {
        this.closeActivityListener = closeActivityListener;
    }

    private CloseActivityListener closeActivityListener;


}

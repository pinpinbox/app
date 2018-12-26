package com.pinpinbox.android.Views.recyclerview;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.AnimationSuspensionTouch;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

/**
 * Created by cundong on 2015/10/9.
 * <p/>
 * 继承自RecyclerView.OnScrollListener，可以监听到是否滑动到页面最低部
 */
public class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener implements OnListLoadNextPageListener {

    /**
     * 当前RecyclerView类型
     */
    protected LayoutManagerType layoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;

    /**
     * 当前滑动的状态
     */
    private int currentScrollState = 0;

    /**
     * 判斷滑動至頂部
     */
    private boolean isTop = false;
    public static boolean TOP = false;

    private boolean isUpScrolling = false;

    private View vTop = null;

    public void setTitleTop(View vTop) {
        this.vTop = vTop;
    }


    private boolean task = false;
    private TitleShowLintener titleShowListener;

    public void setTitleShowListener(TitleShowLintener titleShowListener) {
        this.titleShowListener = titleShowListener;
    }


    private View vActionBar;
    private boolean acbIsHide = false;
    private boolean canControlActionBar = true;

    public void setvActionBar(View vActionBar) {
        this.vActionBar = vActionBar;
    }

    public void setCanControlActionBar(boolean canControlActionBar){
        this.canControlActionBar = canControlActionBar;
    }


    private int scrolledDistance = 0;

    //    vRefreshAnim
    private float floatAlpha = 0;
    private float floatTranslationY = 0;


    private View[] views;


    public void setBackgroundParallaxViews(View... views) {

        this.views = views;
    }


    public int getScrolledDistance() {
        return this.scrolledDistance;
    }


    private View vFloatToolBar;
    private View vScaleView;
    private GradientDrawable gDrawable;
    private float ScaleSize = 1f;
    private int defaultColor;
    private int actionColor;
    private ValueAnimator colorAnimationToAction, colorAnimationToDefault;

    public void setFloatToolBar(View vTool, View vScale) {
        this.vFloatToolBar = vTool;
        this.vScaleView = vScale;

        MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "vFloatToolBar => width(dp) => " + SizeUtils.px2dp(SizeUtils.getMeasuredWidth(vFloatToolBar)));

        MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "w(dp) => " + SizeUtils.px2dp(ScreenUtils.getScreenWidth()));

        ScaleSize = ((float) ScreenUtils.getScreenWidth() / (float) SizeUtils.getMeasuredWidth(vFloatToolBar)) + 0.2f;

        MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "ScaleSize => " + ScaleSize);

        vFloatToolBar.setTag(true);


//        defaultColor = PPBApplication.getInstance().getResources().getColor(R.color.pinpinbox_2_0_0_first_main, null);
        defaultColor = PPBApplication.getInstance().getResources().getColor(R.color.pinpinbox_2_0_0_action_bar_color, null);
        actionColor = PPBApplication.getInstance().getResources().getColor(R.color.pinpinbox_2_0_0_action_bar_color, null);

        gDrawable = (GradientDrawable) vScaleView.getBackground();


        colorAnimationToAction = ValueAnimator.ofObject(new ArgbEvaluator(), defaultColor, actionColor);
        colorAnimationToAction.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                gDrawable.setColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimationToAction.setDuration(200);

        colorAnimationToDefault = ValueAnimator.ofObject(new ArgbEvaluator(), actionColor, defaultColor);
        colorAnimationToDefault.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                gDrawable.setColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimationToDefault.setDuration(200);


    }


    private View vScaleTouchView;
    private boolean isTouch = false;

    public void setvScaleTouchView(View vScaleTouchView) {
        this.vScaleTouchView = vScaleTouchView;
        isTouch = false;
    }

    public boolean isUpScrolling(){
        return isUpScrolling;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        scaleViewControl();

        scrolledDistance += dy;
        MyLog.Set("d", EndlessRecyclerOnScrollListener.class, "scrolledDistance => " + scrolledDistance);



        /*懸浮工具*/
        floatToolBarControl();

        /*parallax*/
        parallaxControl();

       /*顯示標題*/
        titleControl();

        /*set actionbarcontrol  判斷上下滑*/
        if(canControlActionBar) {
            actionBarVisibilityControl(dy);
        }


        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LayoutManagerType.LinearLayout;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LayoutManagerType.GridLayout;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LayoutManagerType.StaggeredGridLayout;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LinearLayout:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GridLayout:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case StaggeredGridLayout:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {

                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);


                break;
        }

    }

    private void scaleViewControl(){

        if (vScaleTouchView != null && vScaleTouchView.getScaleX() < 1f && !isTouch) {
            isTouch = true;
            vScaleTouchView.animate().translationZ(vScaleTouchView.getContext().getResources().getDimension(R.dimen.ppb200_translationZ_default))
                    .scaleX(1f)
                    .scaleY(1f)
                    .setListener(null)
                    .setDuration(150)
                    .start();
        }

    }

    private void floatToolBarControl() {


        if (vFloatToolBar != null) {


            MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "tool => getY" + vFloatToolBar.getY());

            MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "tool => getY(dp)" + SizeUtils.px2dp(vFloatToolBar.getY()));

            MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "tool => getTop" + vFloatToolBar.getTop());//固定

            MyLog.Set("e", EndlessRecyclerOnScrollListener.class, "status height => " + PPBApplication.getInstance().getStatusBarHeight());


            if (scrolledDistance > vFloatToolBar.getTop() - PPBApplication.getInstance().getStatusBarHeight()) {

                MyLog.Set("e", getClass(), "-------------------------------");

                vFloatToolBar.setY(PPBApplication.getInstance().getStatusBarHeight() - 1);

                if (!(boolean) vFloatToolBar.getTag()) {

                    ViewPropertyAnimator scaleToMatch = vScaleView.animate();
                    scaleToMatch.setDuration(200)
                            .scaleX(ScaleSize)
                            .translationZ(0f)
                            .start();
                    vFloatToolBar.setTag(true);

//                colorAnimationToAction.start();
                }

            } else {
                vFloatToolBar.setTranslationY(-scrolledDistance);

                if ((boolean) vFloatToolBar.getTag()) {

                    ViewPropertyAnimator scaleTo1 = vScaleView.animate();
                    scaleTo1.setDuration(200)
                            .scaleX(1.0f)
                            .translationZ(32f)
                            .start();

                    vFloatToolBar.setTag(false);

//                colorAnimationToDefault.start();

                }

            }


        }


    }

    private void titleControl() {
        if (vTop != null && titleShowListener != null) {

            /*actionbar + statusbar*/
            if (scrolledDistance > vTop.getTop() + 152) {

                if (!task) {
                    titleShowListener.show();
                    task = true;
                    MyLog.Set("d", EndlessRecyclerOnScrollListener.class, "出現title");
                }

            } else {
                if (task) {
                    titleShowListener.hide();
                    task = false;
                    MyLog.Set("d", EndlessRecyclerOnScrollListener.class, "隱藏title");
                }
            }
        }

    }

    private void parallaxControl() {

        if (scrolledDistance <= 1000) {
            floatAlpha = (float) scrolledDistance / 200;
            floatTranslationY = (float) scrolledDistance / 3;
        }

        MyLog.Set("d", EndlessRecyclerOnScrollListener.class, " floatAlpha " + floatAlpha);
        MyLog.Set("d", EndlessRecyclerOnScrollListener.class, " floatTranslationY " + floatTranslationY);

        if (views != null && views.length > 0) {
            for (View view : views) {
                view.setAlpha(1 - floatAlpha);
                view.setTranslationY(0 - floatTranslationY);
            }
        }

    }

    private void actionBarVisibilityControl(int dy) {


        if (vActionBar != null) {


            if (dy > 0) {
                //往上滾
                if (!acbIsHide) {
                    acbIsHide = true;
                    vActionBar.animate()
                            .translationY(-PPBApplication.getInstance().getResources().getDimension(R.dimen.ppb200_action_bar_height_with_status))
                            .alpha(0f)
                            .setDuration(400)
                            .start();
                }

                isUpScrolling = true;

            } else if (dy < 0) {
                //往下滾
                if (acbIsHide) {
                    acbIsHide = false;
                    vActionBar.animate()
                            .translationY(0)
                            .alpha(1f)
                            .setDuration(400)
                            .start();
                }

                isUpScrolling = false;

            }

        }


    }


    private View isOnTouchView;

    public void setIsTouchingView(View isOnTouchView){
        this.isOnTouchView = isOnTouchView;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        currentScrollState = newState;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItemPosition) >= totalItemCount - 1)) {
            onLoadNextPage(recyclerView);
        }

        if (inputMethodManager != null && editText != null) {
            if (newState == 1) {
                //在用手指滾動
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }

        }

//        if(currentScrollState ==0){
//            isUpScrolling = false;
//        }


        AnimationSuspensionTouch.reset(isOnTouchView);

    }

    private InputMethodManager inputMethodManager;
    private EditText editText;

    public void setInputStatus(InputMethodManager inputMethodManager, EditText editText) {
        this.inputMethodManager = inputMethodManager;
        this.editText = editText;
    }


    /**
     * 取数组中最大值
     *
     * @param lastPositions
     * @return
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    @Override
    public void onLoadNextPage(final View view) {
    }


    public static enum LayoutManagerType {
        LinearLayout,
        StaggeredGridLayout,
        GridLayout
    }
}

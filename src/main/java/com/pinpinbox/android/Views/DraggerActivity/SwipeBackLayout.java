package com.pinpinbox.android.Views.DraggerActivity;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pinpinbox.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: walid ( copy to githup)
 * Date ： 2016/03/21 
 */
public class SwipeBackLayout extends FrameLayout {




    /**
     *關閉activity距離
     */
    private static final int FINISH_DISTANCE = 360;

    /**
     * Minimum velocity that will be detected as a fling 瞬間滑動速度
     */
    private static final int MIN_FLING_VELOCITY = 1800;

    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;

    private static final int FULL_ALPHA = 255;

    /**
     * Edge flag indicating that the left edge should be affected.
     */
    public static final int EDGE_LEFT = ViewDragHelper.EDGE_LEFT;

    /**
     * Edge flag indicating that the right edge should be affected.
     */
    public static final int EDGE_RIGHT = ViewDragHelper.EDGE_RIGHT;

    /**
     * Edge flag indicating that the bottom edge should be affected.
     */
    public static final int EDGE_BOTTOM = ViewDragHelper.EDGE_BOTTOM;

    /**
     * Edge flag set indicating all edges should be affected.
     */
    public static final int EDGE_ALL = EDGE_LEFT | EDGE_RIGHT | EDGE_BOTTOM;

    /**
     * A view is not currently being dragged or animating as a result of a
     * fling/snap.
     */
    public static final int STATE_IDLE = ViewDragHelper.STATE_IDLE;

    /**
     * A view is currently being dragged. The position is currently changing as
     * a result of user input or simulated user input.
     */
    public static final int STATE_DRAGGING = ViewDragHelper.STATE_DRAGGING;

    /**
     * A view is currently settling into place as a result of a fling or
     * predefined non-interactive motion.
     */
    public static final int STATE_SETTLING = ViewDragHelper.STATE_SETTLING;

    /**
     * Default threshold of scroll
     */
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3f;

    private static final int OVERSCROLL_DISTANCE = 10;

    private static final int[] EDGE_FLAGS = {
            EDGE_LEFT, EDGE_RIGHT, EDGE_BOTTOM, EDGE_ALL
    };

    private int mEdgeFlag;

    /**
     * Threshold of scroll, we will close the activity, when scrollPercent over
     * this value;
     */
    private float mScrollThreshold = DEFAULT_SCROLL_THRESHOLD;

    private Activity activity;
    private boolean enable = true;
    private View contentView;
    private ViewDragHelper dragHelper;
    private float scrollPercent;
    private int contentLeft;
    private int contentTop;

    /**
     * The set of listeners to be sent events through.
     */
    private List<SwipeListener> listeners;

//    private Drawable shadowLeft;
//    private Drawable shadowRight;
//    private Drawable shadowBottom;

    private float scrimOpacity;

    private int scrimColor = DEFAULT_SCRIM_COLOR;

    private boolean inLayout;
    private Rect tmpRect = new Rect();
    /**
     * Edge being dragged
     */
    private int trackingEdge;

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SwipeBackLayoutStyle);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, new ViewDragCallback());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout);
        int edgeSize = a.getDimensionPixelSize(R.styleable.SwipeBackLayout_edge_size, -1);
        if (edgeSize > 0) {
            setEdgeSize(edgeSize);
        }
        int mode = EDGE_FLAGS[a.getInt(R.styleable.SwipeBackLayout_edge_flag, 0)];
        setEdgeTrackingEnabled(mode);
//        int shadowLeft = a.getResourceId(R.styleable.SwipeBackLayout_shadow_left, R.drawable.ic_launcher);
//        int shadowRight = a.getResourceId(R.styleable.SwipeBackLayout_shadow_right, R.drawable.ic_launcher);
//        int shadowBottom = a.getResourceId(R.styleable.SwipeBackLayout_shadow_bottom, R.drawable.ic_launcher);
//        setShadow(shadowLeft, EDGE_LEFT);
//        setShadow(shadowRight, EDGE_RIGHT);
//        setShadow(shadowBottom, EDGE_BOTTOM);
        a.recycle();
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        dragHelper.setMinVelocity(minVel);
        dragHelper.setMaxVelocity(minVel * 2f);
    }

    /**
     * Sets the sensitivity of the NavigationLayout.
     *
     * @param context     The application context.
     * @param sensitivity value between 0 and 1, the final value for touchSlop =
     *                    ViewConfiguration.getScaledTouchSlop * (1 / s);
     */
    public void setSensitivity(Context context, float sensitivity) {
        dragHelper.setSensitivity(context, sensitivity);
    }

    /**
     * Set up contentView which will be moved by user gesture
     */
    private void setContentView(View view) {
        contentView = view;
    }

    public void setEnableGesture(boolean enable) {
        this.enable = enable;
    }

    /**
     * Enable edge tracking for the selected edges of the parent view. The
     * callback's
     * {@link me.imid.swipebacklayout.lib.ViewDragHelper.Callback#onEdgeTouched(int, int)}
     * and
     * {@link me.imid.swipebacklayout.lib.ViewDragHelper.Callback#onEdgeDragStarted(int, int)}
     * methods will only be invoked for edges for which edge tracking has been
     * enabled.
     *
     * @param edgeFlags Combination of edge flags describing the edges to watch
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setEdgeTrackingEnabled(int edgeFlags) {
        mEdgeFlag = edgeFlags;
        dragHelper.setEdgeTrackingEnabled(mEdgeFlag);
    }

    /**
     * Set a color to use for the scrim that obscures primary content while a
     * drawer is open.
     *
     * @param color Color to use in 0xAARRGGBB format.
     */
    public void setScrimColor(int color) {
        scrimColor = color;
        invalidate();
    }

    /**
     * Set the size of an edge. This is the range in pixels along the edges of
     * this view that will actively detect edge touches or drags if edge
     * tracking is enabled.
     *
     * @param size The size of an edge in pixels
     */
    public void setEdgeSize(int size) {
        dragHelper.setEdgeSize(size);
    }

    /**
     * Add a callback to be invoked when a swipe event is sent to this view.
     *
     * @param listener the swipe listener to attach to this view
     */
    public void addSwipeListener(SwipeListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    /**
     * Removes a listener from the set of listeners
     *
     * @param listener
     */
    public void removeSwipeListener(SwipeListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    public interface SwipeListener {
        /**
         * Invoke when state change
         *
         * @param state         flag to describe scroll state
         * @param scrollPercent scroll percent of this view
         * @see #STATE_IDLE
         * @see #STATE_DRAGGING
         * @see #STATE_SETTLING
         */
        void onScrollStateChange(int state, float scrollPercent);

        /**
         * Invoke when edge touched
         *
         * @param edgeFlag edge flag describing the edge being touched
         * @see #EDGE_LEFT
         * @see #EDGE_RIGHT
         * @see #EDGE_BOTTOM
         */
        void onEdgeTouch(int edgeFlag);

        /**
         * Invoke when scroll percent over the threshold for the first time
         */
        void onScrollOverThreshold();
    }

    /**
     * Set scroll threshold, we will close the activity, when scrollPercent over
     * this value
     *
     * @param threshold
     */
    public void setScrollThresHold(float threshold) {
        if (threshold >= 1.0f || threshold <= 0) {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
        mScrollThreshold = threshold;
    }

    /**
     * Set a drawable used for edge shadow.
     *
     * @param shadow    Drawable to use
     * @param edgeFlags Combination of edge flags describing the edge to set
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setShadow(Drawable shadow, int edgeFlag) {
//        if ((edgeFlag & EDGE_LEFT) != 0) {
//            shadowLeft = shadow;
//        } else if ((edgeFlag & EDGE_RIGHT) != 0) {
//            shadowRight = shadow;
//        } else if ((edgeFlag & EDGE_BOTTOM) != 0) {
//            shadowBottom = shadow;
//        }
        invalidate();
    }

    /**
     * Set a drawable used for edge shadow.
     *
     * @param resId     Resource of drawable to use
     * @param edgeFlags Combination of edge flags describing the edge to set
     * @see #EDGE_LEFT
     * @see #EDGE_RIGHT
     * @see #EDGE_BOTTOM
     */
    public void setShadow(int resId, int edgeFlag) {
        setShadow(getResources().getDrawable(resId), edgeFlag);
    }

    /**
     * Scroll out contentView and finish the activity
     */
    public void scrollToFinishActivity() {
        final int childWidth = contentView.getWidth();
        final int childHeight = contentView.getHeight();

        int left = 0, top = 0;
        if ((mEdgeFlag & EDGE_LEFT) != 0) {
            left = childWidth + FINISH_DISTANCE + OVERSCROLL_DISTANCE;
            trackingEdge = EDGE_LEFT;
        } else if ((mEdgeFlag & EDGE_RIGHT) != 0) {
            left = -childWidth - FINISH_DISTANCE - OVERSCROLL_DISTANCE;
            trackingEdge = EDGE_RIGHT;
        } else if ((mEdgeFlag & EDGE_BOTTOM) != 0) {
            top = -childHeight - FINISH_DISTANCE - OVERSCROLL_DISTANCE;
//            top = 0;
            trackingEdge = EDGE_BOTTOM;
        }

        dragHelper.smoothSlideViewTo(contentView, left, top);
        invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!enable) {
            return false;
        }
        try {
            return dragHelper.shouldInterceptTouchEvent(event);
        } catch (ArrayIndexOutOfBoundsException e) {
            // FIXME: handle exception
            // issues #9
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!enable) {
            return false;
        }
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        inLayout = true;
        if (contentView != null) {
            contentView.layout(contentLeft, contentTop,
                    contentLeft + contentView.getMeasuredWidth(),
                    contentTop + contentView.getMeasuredHeight());
        }
        inLayout = false;
    }

    @Override
    public void requestLayout() {
        if (!inLayout) {
            super.requestLayout();
        }
    }


    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == contentView;

        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (scrimOpacity > 0 && drawContent
                && dragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return ret;
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (scrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * scrimOpacity);
        final int color = alpha << 24 | (scrimColor & 0xffffff);

        if ((trackingEdge & EDGE_LEFT) != 0) {
            canvas.clipRect(0, 0, child.getLeft(), getHeight());
        } else if ((trackingEdge & EDGE_RIGHT) != 0) {
            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
        } else if ((trackingEdge & EDGE_BOTTOM) != 0) {
            canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
        }
        canvas.drawColor(color);
    }

    private void drawShadow(Canvas canvas, View child) {
        final Rect childRect = tmpRect;
        child.getHitRect(childRect);

//        if ((mEdgeFlag & EDGE_LEFT) != 0) {
//            shadowLeft.setBounds(childRect.left - shadowLeft.getIntrinsicWidth(), childRect.top,
//                    childRect.left, childRect.bottom);
//            shadowLeft.setAlpha((int) (scrimOpacity * FULL_ALPHA));
//            shadowLeft.draw(canvas);
//        }
//
//        if ((mEdgeFlag & EDGE_RIGHT) != 0) {
//            shadowRight.setBounds(childRect.right, childRect.top,
//                    childRect.right + shadowRight.getIntrinsicWidth(), childRect.bottom);
//            shadowRight.setAlpha((int) (scrimOpacity * FULL_ALPHA));
//            shadowRight.draw(canvas);
//        }
//
//        if ((mEdgeFlag & EDGE_BOTTOM) != 0) {
//            shadowBottom.setBounds(childRect.left, childRect.bottom, childRect.right,
//                    childRect.bottom + shadowBottom.getIntrinsicHeight());
//            shadowBottom.setAlpha((int) (scrimOpacity * FULL_ALPHA));
//            shadowBottom.draw(canvas);
//        }
    }

    public void attachToActivity(Activity activity) {
        this.activity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowBackground
        });
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();


        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    @Override
    public void computeScroll() {
        scrimOpacity = 1 - scrollPercent;
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        private boolean mIsScrollOverValid;

        @Override
        public boolean tryCaptureView(View view, int i) {
            boolean ret = dragHelper.isEdgeTouched(mEdgeFlag, i);
            if (ret) {
                if (dragHelper.isEdgeTouched(EDGE_LEFT, i)) {
                    trackingEdge = EDGE_LEFT;
                } else if (dragHelper.isEdgeTouched(EDGE_RIGHT, i)) {
                    trackingEdge = EDGE_RIGHT;
                } else if (dragHelper.isEdgeTouched(EDGE_BOTTOM, i)) {
                    trackingEdge = EDGE_BOTTOM;
                }
                if (listeners != null && !listeners.isEmpty()) {
                    for (SwipeListener listener : listeners) {
                        listener.onEdgeTouch(trackingEdge);
                    }
                }
                mIsScrollOverValid = true;
            }
            boolean directionCheck = false;
            if (mEdgeFlag == EDGE_LEFT || mEdgeFlag == EDGE_RIGHT) {
                directionCheck = !dragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL, i);
            } else if (mEdgeFlag == EDGE_BOTTOM) {
                directionCheck = !dragHelper
                        .checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL, i);
            } else if (mEdgeFlag == EDGE_ALL) {
                directionCheck = true;
            }
            return ret & directionCheck;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mEdgeFlag & (EDGE_LEFT | EDGE_RIGHT);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mEdgeFlag & EDGE_BOTTOM;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if ((trackingEdge & EDGE_LEFT) != 0) {
                scrollPercent = Math.abs((float) left
                        / (contentView.getWidth() + FINISH_DISTANCE));
            } else if ((trackingEdge & EDGE_RIGHT) != 0) {
                scrollPercent = Math.abs((float) left
                        / (contentView.getWidth() + FINISH_DISTANCE));
            } else if ((trackingEdge & EDGE_BOTTOM) != 0) {
                scrollPercent = Math.abs((float) top
                        / (contentView.getHeight() +FINISH_DISTANCE));
//                scrollPercent = 1;
            }
            contentLeft = left;
            contentTop = top;
            invalidate();
            if (scrollPercent < mScrollThreshold && !mIsScrollOverValid) {
                mIsScrollOverValid = true;
            }
            if (listeners != null && !listeners.isEmpty()
                    && dragHelper.getViewDragState() == STATE_DRAGGING
                    && scrollPercent >= mScrollThreshold && mIsScrollOverValid) {
                mIsScrollOverValid = false;
                for (SwipeListener listener : listeners) {
                    listener.onScrollOverThreshold();
                }
            }

            if (scrollPercent >= 1) {
                if (!activity.isFinishing()) {
                    activity.finish();
                    activity.overridePendingTransition(0, 0);
                }
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            final int childHeight = releasedChild.getHeight();
            int left = 0, top = 0;
            if ((trackingEdge & EDGE_LEFT) != 0) {
                left = xvel > 0 || xvel == 0 && scrollPercent > mScrollThreshold ?
                        childWidth + FINISH_DISTANCE + OVERSCROLL_DISTANCE : 0;
            } else if ((trackingEdge & EDGE_RIGHT) != 0) {
                left = xvel < 0 || xvel == 0 && scrollPercent > mScrollThreshold ?
                        -(childWidth + FINISH_DISTANCE + OVERSCROLL_DISTANCE) : 0;
            } else if ((trackingEdge & EDGE_BOTTOM) != 0) {
                top = yvel < 0 || yvel == 0 && scrollPercent > mScrollThreshold ?
                        -(childHeight + FINISH_DISTANCE + OVERSCROLL_DISTANCE) : 0;
//                top = 0;
            }
            dragHelper.settleCapturedViewAt(left, top);
            invalidate();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int ret = 0;
            if ((trackingEdge & EDGE_LEFT) != 0) {
                ret = Math.min(child.getWidth(), Math.max(left, 0));
            } else if ((trackingEdge & EDGE_RIGHT) != 0) {
                ret = Math.min(0, Math.max(left, -child.getWidth()));
            }
            return ret;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int ret = 0;
            if ((trackingEdge & EDGE_BOTTOM) != 0) {
                ret = Math.min(0, Math.max(top, -child.getHeight()));
            }
            return ret;
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (listeners != null && !listeners.isEmpty()) {
                for (SwipeListener listener : listeners) {
                    listener.onScrollStateChange(state, scrollPercent);
                }
            }
        }
    }
}

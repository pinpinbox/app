package com.pinpinbox.android.Views.DraggerActivity.NoDragger;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.DraggerActivity.ViewDragHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2016/9/5.
 */
public class NoDraggerLayout extends FrameLayout {


//    drag distance 60


    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400;

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

    public NoDraggerLayout(Context context) {
        this(context, null);
    }

    public NoDraggerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SwipeBackLayoutStyle);
    }

    public NoDraggerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }



    /**
     * Set up contentView which will be moved by user gesture
     */
    private void setContentView(View view) {
        contentView = view;
    }


    public void setEdgeTrackingEnabled(int edgeFlags) {
//        mEdgeFlag = edgeFlags;
//        dragHelper.setEdgeTrackingEnabled(mEdgeFlag);
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
//        if (scrimOpacity > 0 && drawContent
//                && dragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
//            drawShadow(canvas, child);
//            drawScrim(canvas, child);
//        }
        return ret;
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

    }

}

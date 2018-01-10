package com.pinpinbox.android.pinpinbox2_0_0.libs.TouchRange;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

public class TouchRange {

    private static final float DEFAULT_BOUNDS = 8.0f;

    private Builder mBuilder;
    private TouchDelegateGroup mTouchDelegateGroup;

    private TouchRange(Builder builder) {
        this.mBuilder = builder;
    }

    public void change() {
        View[] views = mBuilder.views;
        if (views == null) {
            return;
        }
        for (final View view : views) {
            final View parent = (View) view.getParent();
            if (mTouchDelegateGroup == null) {
                mTouchDelegateGroup = new TouchDelegateGroup(parent);
            }
            parent.post(new Runnable() {
                @Override
                public void run() {
                    Rect rect = new Rect();
                    view.setEnabled(true);
                    view.getHitRect(rect);

                    rect.left -= TouchRange.this.mBuilder.bounds[0];
                    rect.top -= TouchRange.this.mBuilder.bounds[1];
                    rect.right += TouchRange.this.mBuilder.bounds[2];
                    rect.bottom += TouchRange.this.mBuilder.bounds[3];

                    TouchDelegate touchDelegate = new TouchDelegate(rect, view);
                    if (View.class.isInstance(view.getParent())) {
                        mTouchDelegateGroup.addTouchDelegate(touchDelegate);
                    }
                }
            });
            parent.setTouchDelegate(mTouchDelegateGroup);
        }

    }

    /**
     * Set the view
     *
     * @param v You need to set the touch-size view
     * @return {@link Builder}
     */
    public static Builder let(View... v) {
        return new Builder(v);
    }

    public static class Builder {

        private View[] views;
        private int[] bounds;

        Builder(View[] views) {
            this.views = views;
        }

        /**
         * set touch bounds
         *
         * @param bounds Touch bounds value(left,top,right,bottom),the value unit is dp.
         *               If you do not set the bounds value, the default value is 20dp.
         * @return {@link TouchRange}
         */
        public TouchRange bounds(float... bounds) {
            if (bounds.length == 0) {
                int bound = dp2px(DEFAULT_BOUNDS);
                this.bounds = new int[]{bound, bound, bound, bound};
            }
            if (bounds.length == 1) {
                int bound = dp2px(bounds[0]);
                this.bounds = new int[]{bound, bound, bound, bound};
            }
            if (bounds.length == 2) {
                int bound0 = dp2px(bounds[0]);
                int bound1 = dp2px(bounds[1]);
                this.bounds = new int[]{bound0, bound1, bound0, bound1};
            }
            if (bounds.length == 3) {
                this.bounds = new int[]{dp2px(bounds[0]), dp2px(bounds[1]),
                        dp2px(bounds[0]), 0};
            }
            if (bounds.length == 4) {
                this.bounds = new int[]{dp2px(bounds[0]), dp2px(bounds[1]),
                        dp2px(bounds[2]), dp2px(bounds[3])};
            }
            return new TouchRange(this);
        }

        /**
         * dp to px
         */
        private int dp2px(float dpValue) {
            return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
        }

    }


}
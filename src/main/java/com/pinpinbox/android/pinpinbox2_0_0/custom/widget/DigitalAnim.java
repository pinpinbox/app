package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.animation.ValueAnimator;
import android.widget.TextView;

/**
 * Created by kevin9594 on 2016/12/23.
 */
public class DigitalAnim {

    public static ValueAnimator ofInt(int... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(values);
        return anim;
    }

    public static ValueAnimator setIntChangeAnimation(final TextView textView, int startValue, final int endValue, int duration) {
        final ValueAnimator animator = ofInt(startValue, endValue);
        animator.setDuration(duration);
        animator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //设置瞬时的数据值到界面上
                        textView.setText(valueAnimator.getAnimatedValue().toString());

                        if (valueAnimator.getAnimatedValue().toString().equals(endValue)) {
                            animator.cancel();
                        }
                    }
                });
        return animator;
    }

}

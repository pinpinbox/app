package com.pinpinbox.android.Views.DraggerActivity;

/**
 * Created by kevin9594 on 2016/7/8.
 */

import com.pinpinbox.android.Views.DraggerActivity.NoDragger.NoDraggerLayout;

/**
 * Author: walid ( copy to githup)
 * Date ： 2016/03/21 18:00
 */
public interface SwipeBackActivityInterface {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    SwipeBackLayout getSwipeBackLayout();

    NoDraggerLayout getNoDraggerLayout();

    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();

}

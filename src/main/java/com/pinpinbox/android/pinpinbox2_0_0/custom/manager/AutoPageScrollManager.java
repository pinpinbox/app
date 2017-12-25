package com.pinpinbox.android.pinpinbox2_0_0.custom.manager;

import android.os.Handler;
import android.support.v4.view.ViewPager;

/**
 * Created by vmage on 2017/9/14.
 */
public class AutoPageScrollManager {

    private ViewPager vp;

    private boolean isAuto = false;

    private int duration;
    private int size;

    private Handler autoScrollHandler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            //無限輪播
            if (vp.getCurrentItem() != size - 1) {

                vp.setCurrentItem(vp.getCurrentItem() + 1, true);

            } else {
                vp.setCurrentItem(0, true);
            }


        }
    };


    public Handler getAutoScrollHandler() {
        return this.autoScrollHandler;
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public AutoPageScrollManager(ViewPager vp, int duration, int size) {
        this.vp = vp;
        this.duration = duration;
        this.size = size;
    }

    public void post() {

        autoScrollHandler.postDelayed(runnable, duration * 1000);
        isAuto = true;

    }

    public void removeRunnable() {
        autoScrollHandler.removeCallbacks(runnable);
        isAuto = false;

    }

    public void recycler() {
        autoScrollHandler.removeCallbacksAndMessages(null);
        autoScrollHandler = null;
    }

    public boolean isAuto() {
        return this.isAuto;
    }


}

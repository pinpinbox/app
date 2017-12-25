package com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight;

/**
 * On Target State Changed Listener
 *
 * @author takusemba
 * @since 13/07/2017
 **/
public interface OnTargetStateChangedListener<T extends Target> {
    /**
     * Called when Target is started
     */
     void onStarted(T target);

    /**
     * Called when Target is started
     */
    void onEnded(T target);
}

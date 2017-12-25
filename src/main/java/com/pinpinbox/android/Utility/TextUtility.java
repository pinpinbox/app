package com.pinpinbox.android.Utility;

import android.text.TextPaint;
import android.widget.TextView;

/**
 * Created by vmage on 2017/3/2.
 */
public class TextUtility {

    public static void setBold(TextView tv, boolean bold) {
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(bold);
    }


}

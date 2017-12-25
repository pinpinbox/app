package com.pinpinbox.android.Views.TextView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by vmage on 2016/3/11.
 */
public class ZoomTextView extends View {

    private Paint mPaint;

    private Rect mBounds;

    private String strValue;

    private int mColor;

    public ZoomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds = new Rect();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColor);

        mPaint.setTextSize(16);
        mPaint.getTextBounds(strValue, 0, strValue.length(), mBounds);
        float textWidth = mBounds.width();
        float textHeight = mBounds.height();
        canvas.drawText(strValue, getWidth() / 2 - textWidth / 2, getHeight() / 2
                + textHeight / 2, mPaint);
    }

    public void setStrValue(String str){
        this.strValue = str;
    }

    public void setTextColor(int color){
        this.mColor = color;
    }


}

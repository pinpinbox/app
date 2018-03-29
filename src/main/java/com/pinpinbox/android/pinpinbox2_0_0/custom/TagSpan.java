package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.style.ReplacementSpan;

/**
 * Created by vmage on 2018/3/29.
 */

public class TagSpan extends ReplacementSpan {

    // span width
    private int mSize;
    // text and background wireframe color
    private int mColor;
    // tag text size
    private int mTextSizePx;
    // background radius
    private int mRadiusPx;
    // background wireframe right margin
    private int mRightMarginPx;

    public TagSpan(int color, int textSizePx, int radiusPx, int rightMarginPx) {
        mColor = color;
        mTextSizePx = textSizePx;
        mRadiusPx = radiusPx;
        mRightMarginPx = rightMarginPx;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mSize = (int) paint.measureText(text, start, end) + mRightMarginPx;
        return mSize;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        drawTagRect(canvas, x, y, paint);
        drawTagText(canvas, text, x, start, end, y, paint);
    }

    private void drawTagRect(Canvas canvas, float x, int y, Paint paint) {
        paint.setColor(mColor);
        paint.setAntiAlias(true);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        final float strokeWidth = paint.getStrokeWidth();
        RectF oval = new RectF(x + strokeWidth + 0.5f, y + fontMetrics.ascent, x + mSize + strokeWidth + 0.5f - mRightMarginPx, y + fontMetrics.descent);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(oval, mRadiusPx, mRadiusPx, paint);
    }

    private void drawTagText(Canvas canvas, CharSequence text, float x, int start, int end, int y, Paint paint) {
//        paint.setTextSize(mTextSizePx);
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        final int textCenterX = (int)x + (mSize - mRightMarginPx) / 2;
        int textBaselineY = (y - fontMetrics.descent - fontMetrics.ascent) / 2 + fontMetrics.descent;
        final String tag = text.subSequence(start, end).toString();
        canvas.drawText(tag, textCenterX, textBaselineY, paint);
    }

}

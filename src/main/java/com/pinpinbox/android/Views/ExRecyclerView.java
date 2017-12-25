package com.pinpinbox.android.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vmage on 2017/11/6.
 */

public class ExRecyclerView extends RecyclerView {

    private Context mContext;

    private TextView tvGuide;

    private int mTextColor;

    private int mTextSize;

    private int mTextBg;

    private Rect mRect;

    private Paint mPaint;


    public ExRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public ExRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        init(context);

    }

    private void init(Context context) {

        this.mContext = context;

        mPaint = new Paint();

        mPaint.setTextSize(24);

        mRect = new Rect();

        mPaint.getTextBounds("99999", 0, 4, mRect);




//        tvGuide = new TextView(mContext);
//        tvGuide.setTextSize(24);
//        tvGuide.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
//        tvGuide.setGravity(Gravity.CENTER);
//        tvGuide.setText("JSDFIOFJSIOFJIODFJIOj");
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(160, 48);
//        tvGuide.setLayoutParams(params);






    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw bg
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        //draw text
        mPaint.setColor(Color.YELLOW);
        canvas.drawText("99999", getWidth()/2 - mRect.width()/2, getHeight()/2 + mRect.height()/2, mPaint);


    }


}

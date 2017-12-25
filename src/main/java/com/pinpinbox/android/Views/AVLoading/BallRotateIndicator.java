package com.pinpinbox.android.Views.AVLoading;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;


import java.util.ArrayList;

/**
 * Created by kevin9594 on 2017/4/23.
 */
public class BallRotateIndicator extends Indicator {

    private int alpha;

    float scaleFloat=0.5f;

    float degress;

    private Matrix mMatrix;

    public BallRotateIndicator(){
        mMatrix=new Matrix();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float radius=getWidth()/10;
        float x = getWidth()/ 2;
        float y=getHeight()/2;

        /*mMatrix.preTranslate(-centerX(), -centerY());
        mMatrix.preRotate(degress,centerX(),centerY());
        mMatrix.postTranslate(centerX(), centerY());
        canvas.concat(mMatrix);*/

        canvas.rotate(degress,centerX(),centerY());

        paint.setAlpha(alpha);

        canvas.save();
        canvas.translate(x - radius * 2 - radius, y);
        canvas.scale(scaleFloat, scaleFloat);
//        canvas.saveLayerAlpha(null,alpha);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scaleFloat, scaleFloat);
//        canvas.saveLayerAlpha(null,alpha);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(x + radius * 2 + radius, y);
        canvas.scale(scaleFloat, scaleFloat);
//        canvas.saveLayerAlpha(null,alpha);
        canvas.drawCircle(0,0,radius, paint);
        canvas.restore();


    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators=new ArrayList<>();
        ValueAnimator scaleAnim=ValueAnimator.ofFloat(0.5f,1,0.5f);
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(-1);

        addUpdateListener(scaleAnim,new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleFloat = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        ValueAnimator rotateAnim=ValueAnimator.ofFloat(0,180,360);
        addUpdateListener(rotateAnim,new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rotateAnim.setDuration(1000);
        rotateAnim.setRepeatCount(-1);

        ValueAnimator alphaAnim=ValueAnimator.ofInt(100,225,100);
        addUpdateListener(alphaAnim,new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (int)animation.getAnimatedValue();
                postInvalidate();
            }
        });
        alphaAnim.setDuration(1000);
        alphaAnim.setRepeatCount(-1);


        animators.add(scaleAnim);
        animators.add(rotateAnim);
        animators.add(alphaAnim);
        return animators;
    }

}

package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.pinpinbox.android.R;


/**
 * Created by kevin9594 on 2018/3/3.
 */

public class GiftAnim {




    public static abstract class Call{
        public abstract void onEnd();
    }

    private Activity mActivity;
    private Call call;

    private View vParent;
    private View vGift;
    private View topView, bottomView;
    private View star1Img, star2Img, star3Img, star4Img;

    public GiftAnim(Activity mActivity, View vParent, Call call){

        this.mActivity = mActivity;
        this.vParent = vParent;
        this.call = call;

        vGift =  vParent.findViewById(R.id.rGift);
        topView = vParent.findViewById(R.id.topView);
        bottomView = vParent.findViewById(R.id.bottomView);
        star1Img = vParent.findViewById(R.id.star1Img);
        star2Img = vParent.findViewById(R.id.star2Img);
        star3Img = vParent.findViewById(R.id.star3Img);
        star4Img = vParent.findViewById(R.id.star4Img);

        vGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                animBounce();
            }
        });

        vGift.setVisibility(View.VISIBLE);

    }

    public void setClickable(boolean click){
        vGift.setClickable(click);
    }

    private void animBounce() {

        YoYo.with(Techniques.Bounce)
                .duration(400)
                .repeat(1)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        animOpen();
                    }
                })
                .playOn(vGift);

    }

    private void animOpen(){

        topView.animate()
                .rotation(45f)//角度
                .translationY(-96f)
                .translationX(180f)
                .setDuration(400)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        star1();
                        star2();
                        star3();
                        star4();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();

    }

    private void star1(){

        //DecelerateInterpolator 持續減速

        star1Img.animate()
                .setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator())
                .translationY(-356f)
                .translationX(-48f)
                .setDuration(600)
                .start();

    }

    private void star2(){

        star2Img.animate()
                .setStartDelay(150)
                .setInterpolator(new DecelerateInterpolator())
                .translationY(-480f)
                .translationX(-186f)
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(600)
                .start();

    }

    private void star3(){

        star3Img.animate()
                .setStartDelay(200)
                .setInterpolator(new DecelerateInterpolator())
                .translationY(-240f)
                .translationX(-228f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(600)
                .start();

    }

    private void star4(){

        star4Img.animate()
                .setStartDelay(250)
                .setInterpolator(new DecelerateInterpolator())
                .translationY(-196f)
                .translationX(-64f)
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(600)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if(call!=null){

                            vGift.animate()
                                    .alpha(0f)
                                    .setDuration(600)
                                    .start();

                            call.onEnd();
                            setClickable(false);
                        }

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();

    }



}

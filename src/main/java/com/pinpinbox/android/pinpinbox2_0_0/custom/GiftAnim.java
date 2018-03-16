package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.blankj.utilcode.util.ScreenUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;


/**
 * Created by kevin9594 on 2018/3/3.
 */

public class GiftAnim {


    public static abstract class Call {
        public abstract void onEnd();
    }

    private Activity mActivity;
    private Call call;

    private View vParent;
    private View vGift;
    private View topView, bottomView;
    private View star1Img, star2Img, star3Img, star4Img;

    public GiftAnim(Activity mActivity, View vParent, Call call) {

        this.mActivity = mActivity;
        this.vParent = vParent;
        this.call = call;

        vGift = vParent.findViewById(R.id.rGift);
        topView = vParent.findViewById(R.id.topView);
        bottomView = vParent.findViewById(R.id.bottomView);
        star1Img = vParent.findViewById(R.id.star1Img);
        star2Img = vParent.findViewById(R.id.star2Img);
        star3Img = vParent.findViewById(R.id.star3Img);
        star4Img = vParent.findViewById(R.id.star4Img);

        vGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick_4s()) {
                    return;
                }
                animBounce();
            }
        });

        vGift.setVisibility(View.VISIBLE);

    }

    public void setClickable(boolean click) {
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

    private void animOpen() {

        MyLog.Set("e", getClass(), "dpi => " + mActivity.getResources().getDisplayMetrics().densityDpi);
        MyLog.Set("e", getClass(), "density => " + mActivity.getResources().getDisplayMetrics().density);

        MyLog.Set("e", getClass(), "xdpi => " + mActivity.getResources().getDisplayMetrics().xdpi);
        MyLog.Set("e", getClass(), "ydpi => " + mActivity.getResources().getDisplayMetrics().ydpi);

        MyLog.Set("e", getClass(), "getScreenWidth => " + ScreenUtils.getScreenWidth());
        MyLog.Set("e", getClass(), "getScreenHeight => " + ScreenUtils.getScreenHeight());


        topView.animate()
                .rotation(45f)//角度
                .translationY(-ScreenUtils.getScreenHeight()/18)//18.5  default-96
                .translationX(ScreenUtils.getScreenWidth()/6)//6 default180
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

    private void star1() {

        //DecelerateInterpolator 持續減速

        star1Img.animate()
                .setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator())
                .translationY(-ScreenUtils.getScreenHeight()/5)//5 default-356
                .translationX(-ScreenUtils.getScreenWidth()/22)//22.5 default-48
                .setDuration(600)
                .start();

    }

    private void star2() {

        star2Img.animate()
                .setStartDelay(150)
                .setInterpolator(new DecelerateInterpolator())
                .translationY(-ScreenUtils.getScreenHeight()/4)//3.7 default-480
                .translationX(-ScreenUtils.getScreenWidth()/6)//5.8 default-186
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(600)
                .start();

    }

    private void star3() {

        star3Img.animate()
                .setStartDelay(200)
                .setInterpolator(new DecelerateInterpolator())
                .translationY(-ScreenUtils.getScreenHeight()/7)//7.4 default-240
                .translationX(-ScreenUtils.getScreenWidth()/5)//4.7 default-228
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(600)
                .start();

    }

    private void star4() {


        star4Img.animate()
                .setStartDelay(250)
                .setInterpolator(new DecelerateInterpolator())
                .translationY(-ScreenUtils.getScreenHeight()/9)//9 default-196
                .translationX(-ScreenUtils.getScreenWidth()/17)//16.8 default-64
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(600)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                        if (call != null) {

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

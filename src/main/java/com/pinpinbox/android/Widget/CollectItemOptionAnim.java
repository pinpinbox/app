package com.pinpinbox.android.Widget;

import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Created by kevin9594 on 2016/3/12.
 */
public class CollectItemOptionAnim {



    public static void Open_4_option(View backgrounview, View vLeft, View vRight, View vLeftUp, View vRightUp){

        int x = backgrounview.getWidth();
        int y = backgrounview.getHeight();

        vLeft.setVisibility(View.VISIBLE);
        vRight.setVisibility(View.VISIBLE);
        vLeftUp.setVisibility(View.VISIBLE);
        vRightUp.setVisibility(View.VISIBLE);




        ViewPropertyAnimator openleft = vLeft.animate();
        openleft.setDuration(300)
                .translationXBy(-x / 3)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();


        ViewPropertyAnimator openright = vRight.animate();
        openright.setDuration(300)
                .translationXBy(x / 3)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();

        ViewPropertyAnimator openleftup = vLeftUp.animate();
        openleftup.setDuration(300)
                .translationXBy(-x / 6)
                .translationYBy(-y / 5)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();

        ViewPropertyAnimator openrightup = vRightUp.animate();
        openrightup.setDuration(300)
                .translationXBy(x / 6)
                .translationYBy(-y / 5)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();

        ViewPropertyAnimator alphaToTrans = backgrounview.animate();
        alphaToTrans.setDuration(300)
                .alpha(0.3f)
                .start();


    }

    public static void Close_4_option(View backgrounview, View vLeft, View vRight, View vLeftUp, View vRightUp){

        int x = backgrounview.getWidth();
        int y = backgrounview.getHeight();

        View[] views = { vLeft,  vRight,  vLeftUp,  vRightUp};




        ViewPropertyAnimator closeleft = vLeft.animate();
        closeleft.setDuration(300)
                .translationXBy(x / 3)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();


        ViewPropertyAnimator closeright = vRight.animate();
        closeright.setDuration(300)
                .translationXBy(-x / 3)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();

        ViewPropertyAnimator closeleftup = vLeftUp.animate();
        closeleftup.setDuration(300)
                .translationXBy(x / 6)
                .translationYBy(y / 5)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();

        ViewPropertyAnimator closerightup = vRightUp.animate();
        closerightup.setDuration(300)
                .translationXBy(-x / 6)
                .translationYBy(y / 5)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();

        ViewPropertyAnimator alphaRecover = backgrounview.animate();
        alphaRecover.setDuration(300)
                .alpha(1)
                .start();
    }

    public static void Open_3_option(View background, View vLeft, View vRight, View vUp){

        int x = background.getWidth();
        int y = background.getHeight();


        ViewPropertyAnimator openleft = vLeft.animate();
        openleft.setDuration(300)
                .translationXBy(-x / 3)
                .translationYBy(-y / 20)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();

        ViewPropertyAnimator openright = vRight.animate();
        openright.setDuration(300)
                .translationXBy(x / 3)
                .translationYBy(-y / 20)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();


        ViewPropertyAnimator opentup = vUp.animate();
        opentup.setDuration(300)
                .translationYBy(-x / 3)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();


        /**2016.09.03新增*/
        ViewPropertyAnimator alphaToTrans = background.animate();
        alphaToTrans.setDuration(300)
                .alpha(0.3f)
                .start();

    }

    public static void Close_3_option(View background, View vLeft, View vRight, View vUp){
        int x = background.getWidth();
        int y = background.getHeight();
        ViewPropertyAnimator closeleft = vLeft.animate();
        closeleft.setDuration(300)
                .translationXBy(x / 3)
                .translationYBy(y / 20)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();

        ViewPropertyAnimator closeright = vRight.animate();
        closeright.setDuration(300)
                .translationXBy(-x / 3)
                .translationYBy(y / 20)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();


        ViewPropertyAnimator closeup = vUp.animate();
        closeup.setDuration(300)
                .translationYBy(x / 3)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();

        /**2016.09.03新增*/
        ViewPropertyAnimator alphaRecover = background.animate();
        alphaRecover.setDuration(300)
                .alpha(1)
                .start();

    }

    public static void Open_2_option(View background, View vLeftUp, View vRightUp){
        int x = background.getWidth();
        int y = background.getHeight();

        vLeftUp.setVisibility(View.VISIBLE);
        vRightUp.setVisibility(View.VISIBLE);



        ViewPropertyAnimator openleftup = vLeftUp.animate();
        openleftup.setDuration(300)
                .translationXBy(-x / 6)
                .translationYBy(-y / 5)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();


        ViewPropertyAnimator openrightup = vRightUp.animate();
        openrightup.setDuration(300)
                .translationXBy(x / 6)
                .translationYBy(-y / 5)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1)
                .start();

        /**2016.09.03新增*/
        ViewPropertyAnimator alphaToTrans = background.animate();
        alphaToTrans.setDuration(300)
                .alpha(0.3f)
                .start();


    }

    public static void Close_2_option(View background, View vLeftUp, View vRightUp){

        int x = background.getWidth();
        int y = background.getHeight();


        ViewPropertyAnimator closeleftup = vLeftUp.animate();
        closeleftup.setDuration(300)
                .translationXBy(x / 6)
                .translationYBy(y / 5)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();


        ViewPropertyAnimator closerightup = vRightUp.animate();
        closerightup.setDuration(300)
                .translationXBy(-x / 6)
                .translationYBy(y / 5)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0)
                .start();

        /**2016.09.03新增*/
        ViewPropertyAnimator alphaRecover = background.animate();
        alphaRecover.setDuration(300)
                .alpha(1)
                .start();

    }

}

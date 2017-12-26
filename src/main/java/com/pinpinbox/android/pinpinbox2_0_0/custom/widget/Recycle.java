package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by vmage on 2016/5/19.
 */
public class Recycle {

    public static void IMG(ImageView img){
        if(img!=null && img.getId() != 0){

            img.setImageResource(0);
            img = null;
        }
    }

    public static void VIEW(View v){
        try {
            if (v != null) {
                v.setBackground(null);
                v = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void BMP(Bitmap bitmap){
        if(bitmap!=null && !bitmap.isRecycled()){
            bitmap.isRecycled();
            bitmap = null;
        }
    }


}

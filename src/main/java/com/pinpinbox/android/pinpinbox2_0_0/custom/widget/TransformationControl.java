package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.squareup.picasso.Transformation;

public class TransformationControl {


    /*灰皆 0f=>全灰*/
    public static class SaturationTransformation implements Transformation {

        private float saturation;

        public SaturationTransformation(float saturation) {
            this.saturation = saturation;
        }

        @Override
        public Bitmap transform(Bitmap source) {


            Bitmap output = Bitmap.createBitmap(source.getWidth(),
                    source.getHeight(), Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(saturation);
            paint.setColorFilter(new ColorMatrixColorFilter(matrix));
            canvas.drawBitmap(source, 0, 0, paint);
            source.recycle();
            return output;
        }

        @Override
        public String key() {
            return "Saturation()";
        }

    }

    /*亮度 小於0=>暗 大於0=>亮*/
    public static class BrightnessTransformation implements Transformation {

        private int brightness;

        public BrightnessTransformation(int brightness) {
            this.brightness = brightness;
        }

        public BrightnessTransformation() {
            this.brightness = -5;
        }

        @Override
        public Bitmap transform(Bitmap source) {

            if(source==null){
                MyLog.Set("e", TransformationControl.class, "source==null");
            }

            Bitmap output = Bitmap.createBitmap(source.getWidth(),
                    source.getHeight(), Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            ColorMatrix matrix = new ColorMatrix();
            float[] array = {
                    1, 0, 0, 0, brightness,
                    0, 1, 0, 0, brightness,
                    0, 0, 1, 0, brightness,
                    0, 0, 0, 1, 0
            };
            matrix.set(array);
            paint.setColorFilter(new ColorMatrixColorFilter(matrix));
            canvas.drawBitmap(source, 0, 0, paint);


            source.recycle();
            return output;
        }

        @Override
        public String key() {
            return "Brightness()";
        }

    }

}

package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

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

    /*亮度 小於1f=>暗  大於1f=>亮*/
    public static class BrightnessTransformation implements Transformation {

        private float brightness;

        public BrightnessTransformation(float brightness) {
            this.brightness = brightness;
        }

        public BrightnessTransformation() {
            this.brightness = 0.97f;
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
                    brightness, 0, 0, 0, 0,
                    0, brightness, 0, 0, 0,
                    0, 0, brightness, 0, 0,
                    0, 0, 0, 1f, 0
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

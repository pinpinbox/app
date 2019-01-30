package com.pinpinbox.android.Utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.TransformationControl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageUtility {

    public static boolean isImageExist(String imageUrl) {

        if (imageUrl != null && !imageUrl.equals("null") && !imageUrl.equals("") && !imageUrl.isEmpty()) {

            return true;

        } else {

            return false;

        }

    }


    public static boolean isFileExist(File file) {

        if (file != null) {
            return true;
        } else {
            return false;
        }

    }


    public static void setImage(Activity mActivity, final ImageView imageView, String imageUrl) {

        if (isImageExist(imageUrl)) {

            Picasso.get()
                    .load(imageUrl)
                    .config(Bitmap.Config.RGB_565)
                    .priority(Picasso.Priority.HIGH)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .transform(new TransformationControl.BrightnessTransformation())
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            imageView.setAlpha(0f);
                            imageView.animate().setDuration(200).alpha(0.9f).start();
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

            imageView.setTag(imageUrl);

        } else {

            imageView.setImageResource(R.drawable.bg_2_0_0_no_image);

        }


    }

    public static void setImage(Activity mActivity, ImageView imageView, String imageUrl, Callback callback) {

        if (isImageExist(imageUrl)) {

            Picasso.get()
                    .load(imageUrl)
                    .config(Bitmap.Config.RGB_565)
                    .priority(Picasso.Priority.HIGH)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .transform(new TransformationControl.BrightnessTransformation())
                    .into(imageView, callback);

            imageView.setTag(imageUrl);

        } else {

            imageView.setImageResource(R.drawable.bg_2_0_0_no_image);

        }


    }

    public static void setCommonImage(Activity mActivity, ImageView imageView, String imageUrl) {

        if (isImageExist(imageUrl)) {

            Picasso.get()
                    .load(imageUrl)
                    .config(Bitmap.Config.RGB_565)
                    .priority(Picasso.Priority.HIGH)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(imageView);

            imageView.setTag(imageUrl);

        } else {

            imageView.setImageResource(R.drawable.bg_2_0_0_no_image);

        }

    }

    public static void setFileImageToGrid(Activity mActivity, ImageView imageView, File file) {

        if (isFileExist(file)) {

            //fit 調整圖片大小至view邊寬
            Picasso.get()
                    .load(file)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .fit()
                    .centerCrop()
                    .transform(new TransformationControl.BrightnessTransformation())
                    .into(imageView);

            imageView.setTag(file.getPath());

        } else {

            imageView.setImageResource(R.drawable.bg_2_0_0_no_image);

        }

    }


}

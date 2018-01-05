package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by kevin9594 on 2016/9/25.
 */
public class PinPinToast {

    public static Toast toast;

    public static Toast getIntance() {
        return toast;
    }


    public static void ShowToast(Activity mActivity, String message) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast, null);

        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);

        tvToast.setText(message);

        if (toast == null) {
            toast = new Toast(mActivity.getApplicationContext());
            toast.setDuration(200);
        }

//        Toast toast = new Toast(mActivity.getApplicationContext());
//        toast.setDuration(200);
        toast.setView(view);
        toast.show();


    }


    public static void ShowToast(Activity mActivity, int message) {

        View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(message);

        if (toast == null) {
            toast = new Toast(mActivity.getApplicationContext());
            toast.setDuration(200);
        }

//        Toast toast = new Toast(mActivity.getApplicationContext());
//        toast.setDuration(200);
        toast.setView(view);
        toast.show();
    }

    public static void showErrorToast(Activity mActivity, String message) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast_error, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(message);
        if (toast == null) {
            toast = new Toast(mActivity.getApplicationContext());
            toast.setDuration(200);
        }

        toast.setView(view);
        toast.show();
    }

    public static void showErrorToast(Context context, String message) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.pinpinbox_toast_error, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(message);
        if (toast == null) {
            toast = new Toast(context.getApplicationContext());
            toast.setDuration(200);
        }

        toast.setView(view);
        toast.show();
    }





    public static void showErrorToast(Activity mActivity, int message) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast_error, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(message);
        if (toast == null) {
            toast = new Toast(mActivity.getApplicationContext());
            toast.setDuration(200);
        }

        toast.setView(view);
        toast.show();
    }


    public static void showErrorToast(Context context, int message) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.pinpinbox_toast_error, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(message);
        if (toast == null) {
            toast = new Toast(context.getApplicationContext());
            toast.setDuration(200);
        }

        toast.setView(view);
        toast.show();
    }





    public static void showSuccessToast(Activity mActivity, String message) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast_success, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(message);
        if (toast == null) {
            toast = new Toast(mActivity.getApplicationContext());
            toast.setDuration(200);
        }

//        Toast toast = new Toast(mActivity.getApplicationContext());
//        toast.setDuration(200);
        toast.setView(view);
        toast.show();
    }

    public static void showSuccessToast(Activity mActivity, int message) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast_success, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(message);
        if (toast == null) {
            toast = new Toast(mActivity.getApplicationContext());
            toast.setDuration(200);
        }

//        Toast toast = new Toast(mActivity.getApplicationContext());
//        toast.setDuration(200);
        toast.setView(view);
        toast.show();
    }


    public static void showSuccessToast(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.pinpinbox_toast_success, null);
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
        tvToast.setText(message);
        if (toast == null) {
            toast = new Toast(context);
            toast.setDuration(200);
        }

        toast.setView(view);
        toast.show();
    }

    public static void showSponsorToast(Context context, String message, String pictureUrl){

        View view = LayoutInflater.from(context).inflate(R.layout.pinpinbox_toast_sponsor, null);

        TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        RoundCornerImageView userImg = (RoundCornerImageView)view.findViewById(R.id.userImg);

        if (pictureUrl == null || pictureUrl.equals("")) {
            userImg.setImageResource(R.drawable.member_back_head);
        } else {
            Picasso.with(context)
                    .load(pictureUrl)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(context)
                    .into(userImg);
        }


        if (toast == null) {
            toast = new Toast(context);
            toast.setDuration(400);
        }

        toast.setView(view);
        toast.show();

        YoYo.with(Techniques.Swing)
                .duration(600)
                .repeat(1)
                .playOn(userImg);


    }



}

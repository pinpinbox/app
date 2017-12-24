package com.pinpinbox.android.Widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pinpinbox.android.R;

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

//        Toast toast = new Toast(mActivity.getApplicationContext());
//        toast.setDuration(200);
        toast.setView(view);
        toast.show();
    }


}

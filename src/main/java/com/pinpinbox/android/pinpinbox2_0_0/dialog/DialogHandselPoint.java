package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ColorClass;
import com.pinpinbox.android.Utility.TextUtility;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

/**
 * Created by vmage on 2016/7/12.
 */
public class DialogHandselPoint{

    public Activity mActivity;
    public Dialog mDialog;

    private RelativeLayout rClose;
    private TextView tvTitle, tvRestriction, tvDescription, tvLink;
    private ImageView img;
    private BlurView blurView;

    public DialogHandselPoint(Activity activity) {
        this.mActivity = activity;
        mDialog = new Dialog(activity, R.style.myDialog);

        Window window = mDialog.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }



        window.setWindowAnimations(R.style.dialog_enter_exit);
        window.setContentView(R.layout.dialog_2_0_0_handsel_point);


        rClose = (RelativeLayout) mDialog.findViewById(R.id.close);
        rClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
        tvDescription = (TextView) mDialog.findViewById(R.id.tvDescription);
        tvRestriction = (TextView)mDialog.findViewById(R.id.tvRestriction);
        tvLink = (TextView) mDialog.findViewById(R.id.tvLink);
        img = (ImageView)mDialog.findViewById(R.id.img);
        blurView = (BlurView)mDialog.findViewById(R.id.blurview);

        TextUtility.setBold(tvTitle, true);

        setBlur();

        mDialog.show();
    }


    public TextView getTvTitle(){
        return this.tvTitle;
    }

    public TextView getTvRestriction(){
        return this.tvRestriction;
    }

    public TextView getTvDescription(){
        return this.tvDescription;
    }

    public TextView getTvLink(){
        return this.tvLink;
    }

    public ImageView getImg(){
        return this.img;
    }

    public Dialog getDialog(){
        return this.mDialog;
    }

    private void setBlur(){
        final float radius = 4f;
        final View decorView = mActivity.getWindow().getDecorView();
        final View rootView = decorView.findViewById(android.R.id.content);
        final Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(mActivity, true)) //Preferable algorithm, needs RenderScript support mode enabled
                .blurRadius(radius);
    }



}

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
 * Created by kevin9594 on 2016/5/27.
 */
public class DialogCheckContribute {

    public Activity activity;
    public Dialog mDialog;

    private RelativeLayout rClose;
    private TextView tvY, tvN, tvTitle, tvDirections;
    private ImageView coverImg;
    private BlurView blurView;


    public DialogCheckContribute(final Activity activity) {
        this.activity = activity;
        mDialog = new Dialog(activity, R.style.myDialog);

        Window window = mDialog.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }
        window.setWindowAnimations(R.style.dialog_enter_exit);
        window.setContentView(R.layout.dialog_2_0_0_check_contribute);


        init();

        setDialog();

        mDialog.show();
    }

    private void init(){

        rClose = (RelativeLayout) mDialog.findViewById(R.id.close);
        blurView = (BlurView)mDialog.findViewById(R.id.blurview);

        tvY = (TextView)mDialog.findViewById(R.id.y);
        tvN = (TextView)mDialog.findViewById(R.id.n);
        tvTitle = (TextView)mDialog.findViewById(R.id.tvTitle);
        tvDirections = (TextView)mDialog.findViewById(R.id.tvDirections);
        coverImg = (ImageView)mDialog.findViewById(R.id.coverImg);

        TextUtility.setBold(tvTitle, true);
        TextUtility.setBold(tvY, true);



    }

    private void setDialog(){

        rClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        tvN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        setBlur();

    }

    public Dialog getDialog(){
        return this.mDialog;
    }

    public RelativeLayout getrClose() {
        return rClose;
    }

    public TextView getTvY(){
        return this.tvY;
    }

    public TextView getTvN(){
        return this.tvN;
    }

    public TextView getTvTitle(){
        return this.tvTitle;
    }

    public TextView getTvDirections(){
        return this.tvDirections;
    }

    public ImageView getCoverImg(){
        return this.coverImg;
    }

    private void setBlur(){
        final float radius = 4f;
        final View decorView = activity.getWindow().getDecorView();
        final View rootView = decorView.findViewById(android.R.id.content);
        final Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(activity, true)) //Preferable algorithm, needs RenderScript support mode enabled
                .blurRadius(radius);
    }

}

package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

/**
 * Created by kevin9594 on 2016/5/27.
 */
public class DialogCheckContribute {

    public Activity activity;
    public Dialog mDialog;

    private RelativeLayout rClose;
    private TextView tvY, tvN, tvTitle, tvDirections;
    private ImageView coverImg;

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

        rClose =  mDialog.findViewById(R.id.close);

        tvY = mDialog.findViewById(R.id.y);
        tvN = mDialog.findViewById(R.id.n);
        tvTitle = mDialog.findViewById(R.id.tvTitle);
        tvDirections = mDialog.findViewById(R.id.tvDirections);
        coverImg = mDialog.findViewById(R.id.coverImg);

        TextUtility.setBold(tvTitle, true);




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


}

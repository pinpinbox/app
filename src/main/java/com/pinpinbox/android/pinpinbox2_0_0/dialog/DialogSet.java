package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.activity.OffLine2Activity;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.Utility.SystemUtility;

import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;


/**
 * Created by vmage on 2015/8/17
 */
public class DialogSet{
    public Activity activity;
    public Dialog dialog;

    private RelativeLayout rClose;
    public TextView tv, y, n, center_check, tvOtherChose;
    private ImageView minImg;
    private BlurView blurView;

    public String string;
    public int strContents;

    public DialogSet(final Activity activity) {
        this.activity = activity;
        dialog = new Dialog(activity, R.style.myDialog);
        Window crosswin = dialog.getWindow();
        crosswin.setWindowAnimations(R.style.dialog_enter_exit);
        crosswin.setContentView(R.layout.dialog_all_oneline);

        rClose = (RelativeLayout) dialog.findViewById(R.id.close);
        blurView = (BlurView) dialog.findViewById(R.id.blurview);

        tv = (TextView) dialog.findViewById(R.id.ask_contents);
        y = (TextView) dialog.findViewById(R.id.y);
        n = (TextView) dialog.findViewById(R.id.n);
        minImg = (ImageView)dialog.findViewById(R.id.minImg);
        center_check = (TextView) dialog.findViewById(R.id.center_check);

        /**2016.12.14 new add*/
        tvOtherChose = (TextView)dialog.findViewById(R.id.tvOtherChose);

        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        rClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        setBlur();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if(bCloseActivity){
                    activity.finish();
                }

            }
        });

        try {

            dialog.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showMinImage(){
        this.minImg.setVisibility(View.VISIBLE);
    }

    public ImageView getMinImg(){
        return this.minImg;
    }

    public void setAlbumContents_Integer(int str) {
        this.strContents = str;
        tv.setText(str);
    }

    public void setAlbumContents_StringType(String str) {
        this.string = str;
        tv.setText(string);
    }

    public void setbCloseActivity(boolean b){

        this.bCloseActivity = b;

    }

    public TextView getCenter_check() {
        return center_check;
    }

    public TextView getTextView_Y() {
        return y;
    }

    public TextView getTextView_N() {
        return n;
    }

    public TextView getTvOtherChose(){
        return tvOtherChose;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public BlurView getBlurView(){
        return this.blurView;
    }

    public RelativeLayout getrClose(){
        return this.rClose;
    }

    private boolean bCloseActivity = false;

    public ConnectInstability connectInstability;

    public void setConnectInstability(ConnectInstability connectInstability){
        this.connectInstability = connectInstability;
    }


    public void ConnectInstability(){

        setAlbumContents_Integer(R.string.connection_instability);
//        y.setText(R.string.try_again);
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();

                if(connectInstability!=null){

                    connectInstability.DoingAgain();

                }
            }
        });
//        n.setText(R.string.offline_read);
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                for (int i = 0; i < activityList.size(); i++) {
                    if(i == activityList.size()-1){
                        Intent intent = new Intent(activity, OffLine2Activity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        break;
                    }else {
                        activityList.get(i).finish();
                    }
                }
            }
        });

    }

    public void DialogUnKnow() {
        bCloseActivity = true;
        getTextView_Y().setVisibility(View.INVISIBLE);
        getTextView_N().setVisibility(View.INVISIBLE);
//        setAlbumContents_Integer(R.string.unknown_error);
    }

    public void setMessageError(String message) {
        bCloseActivity = true;
        getTextView_Y().setVisibility(View.INVISIBLE);
        getTextView_N().setVisibility(View.INVISIBLE);
        setAlbumContents_StringType(message);
    }

    private boolean closeApp = true;

    public void setNoConnect() {

//        setAlbumContents_Integer(R.string.inspection_of_network);
        getTextView_Y().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeApp = false;
                dialog.dismiss();

            }
        });

        getTextView_N().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeApp = true;
                dialog.dismiss();
            }
        });

        rClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeApp = true;
                dialog.dismiss();
            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {



                if (closeApp) {
                    activity.finish();
                    SystemUtility.SysApplication.getInstance().exit();


//                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {



                    Intent intent = new Intent(activity, OffLine2Activity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });


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

    public void setBlur(View v){
        final float radius = 4f;
        final Drawable windowBackground = v.getBackground();
        blurView.setupWith(v)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(activity, true)) //Preferable algorithm, needs RenderScript support mode enabled
                .blurRadius(radius);
    }

}


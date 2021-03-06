package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by kevin9594 on 2017/2/11.
 */
public class DialogV2Custom {

    private Activity mActivity;
    private Dialog mDialog;
    private ConnectInstability connectInstability;
    private CheckExecute checkExecute;
    private DismissExcute dismissExcute;

    private TextView tvLeftOrTop, tvCenter, tvRightOrBottom, tvMessage;
    private ImageView bgImg, smallImg;
    private RelativeLayout rTopBackground;
    private LinearLayout linBottom;
    private View vDarkBg;

    private int orientation = LinearLayout.HORIZONTAL;


    public DialogV2Custom(final Activity mActivity) {

        this.mActivity = mActivity;

        mDialog = new Dialog(mActivity, R.style.myDialog);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            mDialog.getWindow().setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }


        mDialog.getWindow().setWindowAnimations(R.style.dialog_enter_exit);
        mDialog.getWindow().setContentView(R.layout.dialog_2_0_0_custom);


        tvLeftOrTop = mDialog.findViewById(R.id.tvLeftOrTop);
        tvCenter = mDialog.findViewById(R.id.tvCenter);
        tvRightOrBottom = mDialog.findViewById(R.id.tvRightOrBottom);
        tvMessage = mDialog.findViewById(R.id.tvMessage);

        vDarkBg = mDialog.findViewById(R.id.vDarkBg);
        bgImg = mDialog.findViewById(R.id.bgImg);
        smallImg = mDialog.findViewById(R.id.smallImg);

        rTopBackground = mDialog.findViewById(R.id.rTopBackground);
        linBottom = mDialog.findViewById(R.id.linBottom);


    }


    public TextView getTvLeftOrTop() {
        return this.tvLeftOrTop;
    }

    public TextView getTvCenter() {
        return this.tvCenter;
    }

    public TextView getTvRightOrBottom() {
        return this.tvRightOrBottom;
    }

    public TextView getTvMessage() {
        return this.tvMessage;
    }

    public ImageView getBgImg() {
        return this.bgImg;
    }

    public RelativeLayout getrTopBackground() {
        return this.rTopBackground;
    }

    public LinearLayout getLinBottom() {
        return this.linBottom;
    }

    public View getDarkBg() {
        return this.vDarkBg;
    }

    public Dialog getmDialog() {
        return this.mDialog;
    }

    public ImageView getSmallImg() {
        return smallImg;
    }

    public static void BuildUnKnow(Activity mActivity, String strToFlurryMessage) {
        DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.UNKNOW);
        d.sendErrorToFlurry(strToFlurryMessage);
        d.show();
    }

    public static void BuildError(Activity mActivity, String strMessage) {

        DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.ERROR);
        d.setMessage(strMessage);
        d.show();

    }


    public static void BuildTimeOut(Activity mActivity, ConnectInstability connectInstability) {

        DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.TIMEOUT);
        d.setConnectInstability(connectInstability);
        d.show();

    }

    public static void BuildNoConnect(Activity mActivity) {
        DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.NOCONNECT);
        d.show();
    }


    public void setSmallImgByFile(String path) {

        smallImg.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(new File(path))
                .config(Bitmap.Config.RGB_565)
                .resize(120, 120)
                .centerCrop()
                .error(R.drawable.bg_2_0_0_no_image)
                .tag(mActivity.getApplicationContext())
                .into(smallImg);
    }

    public void setSmallImgByUrl(String url) {

        smallImg.setVisibility(View.VISIBLE);

        Picasso.get()
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .resize(120, 120)
                .centerCrop()
                .error(R.drawable.bg_2_0_0_no_image)
                .tag(mActivity.getApplicationContext())
                .into(smallImg);
    }

    public void setSmallImg(int id) {

        smallImg.setVisibility(View.VISIBLE);
        smallImg.setImageResource(id);

    }


    /**
     * 設定 dialog 風格
     */
    public void setStyle(int style) {

        switch (style) {
            case DialogStyleClass.ERROR:
                setError();
                break;

            case DialogStyleClass.SUCCESS:

                break;

            case DialogStyleClass.CHECK:
                setCheck();
                break;

            case DialogStyleClass.TIMEOUT:
                setTimeOut();

                break;

            case DialogStyleClass.NOCONNECT:
                setNoConnect();
                break;

            case DialogStyleClass.UNKNOW:
                setUnKnow();
                break;


        }

    }

    public void show() {
        if (mDialog != null) {

            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }

            try {
                mDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


    public void dismiss() {
        if (mDialog != null)
            try {
                mDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void setError() {

        rTopBackground.setBackgroundResource(R.drawable.border_2_0_0_dialog_error);
        bgImg.setImageResource(R.drawable.icon_2_0_0_dialog_error);
        tvLeftOrTop.setVisibility(View.GONE);
        tvRightOrBottom.setVisibility(View.GONE);
        tvCenter.setVisibility(View.VISIBLE);

        tvCenter.setText(R.string.pinpinbox_2_0_0_dialog_close);
        tvCenter.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvCenter.setBackgroundResource(R.drawable.click_2_0_0_default);


        tvCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dismissExcute != null) {
                    dismissExcute.AfterDismissDo();
                }


                dismiss();
            }
        });

        vDarkBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dismissExcute != null) {
                    dismissExcute.AfterDismissDo();
                }


                dismiss();
            }
        });

    }

    private void setTimeOut() {

        rTopBackground.setBackgroundResource(R.drawable.border_2_0_0_dialog_timeout);
        bgImg.setImageResource(R.drawable.icon_2_0_0_dialog_timeout);
        linBottom.setOrientation(LinearLayout.HORIZONTAL);
        tvCenter.setVisibility(View.GONE);
        ViewControl.setMargins(tvLeftOrTop, 0, 0, DensityUtility.dip2px(mActivity.getApplicationContext(), 8), 0);
        ViewControl.setMargins(tvRightOrBottom, DensityUtility.dip2px(mActivity.getApplicationContext(), 8), 0, 0, 0);

        tvMessage.setText(R.string.pinpinbox_2_0_0_dialog_message_connection_instability);


        tvRightOrBottom.setText(R.string.pinpinbox_2_0_0_dialog_try_again);
        tvRightOrBottom.setBackgroundResource(R.drawable.click_2_0_0_default);

        tvRightOrBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (connectInstability != null) {
                    connectInstability.DoingAgain();
                }
            }
        });


        tvLeftOrTop.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvLeftOrTop.setBackgroundResource(R.drawable.click_2_0_0_default);


        tvLeftOrTop.setText(R.string.pinpinbox_2_0_0_dialog_close_pinpinbox);
        tvLeftOrTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mActivity.finish();
                SystemUtility.SysApplication.getInstance().exit();
            }
        });


    }

    private void setCheck() {

        rTopBackground.setBackgroundResource(R.drawable.border_2_0_0_dialog_check);
        bgImg.setImageResource(R.drawable.icon_2_0_0_dialog_pinpin);
        linBottom.setOrientation(orientation);
        tvCenter.setVisibility(View.GONE);

        if (orientation == LinearLayout.HORIZONTAL) {//default

            ViewControl.setMargins(tvLeftOrTop, 0, 0, DensityUtility.dip2px(mActivity.getApplicationContext(), 8), 0);
            ViewControl.setMargins(tvRightOrBottom, DensityUtility.dip2px(mActivity.getApplicationContext(), 8), 0, 0, 0);

        } else if (orientation == LinearLayout.VERTICAL) {

            ViewControl.setMargins(tvLeftOrTop, 0, 0, 0, 0);
            ViewControl.setMargins(tvRightOrBottom, 0, DensityUtility.dip2px(mActivity.getApplicationContext(), 8), 0, 0);

        }

        tvRightOrBottom.setText(R.string.pinpinbox_2_0_0_dialog_check);
        tvRightOrBottom.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvRightOrBottom.setBackgroundResource(R.drawable.click_2_0_0_default);

        tvLeftOrTop.setText(R.string.pinpinbox_2_0_0_dialog_cancel);
        tvLeftOrTop.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvLeftOrTop.setBackgroundResource(R.drawable.click_2_0_0_default);

        vDarkBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvRightOrBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

                if (checkExecute != null) {
                    checkExecute.DoCheck();
                }

            }
        });

        tvLeftOrTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();


                if (dismissExcute != null) {
                    dismissExcute.AfterDismissDo();
                }

            }
        });


    }

    private void setNoConnect() {

        rTopBackground.setBackgroundResource(R.drawable.border_2_0_0_dialog_noconnect);
        bgImg.setImageResource(R.drawable.icon_2_0_0_dialog_noconnect);
        linBottom.setOrientation(LinearLayout.HORIZONTAL);
        tvCenter.setVisibility(View.GONE);


        tvMessage.setText(R.string.pinpinbox_2_0_0_dialog_message_inspection_of_network);

        tvRightOrBottom.setVisibility(View.GONE);

        tvLeftOrTop.setText(R.string.pinpinbox_2_0_0_dialog_close_pinpinbox);//單行


        tvLeftOrTop.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvLeftOrTop.setBackgroundResource(R.drawable.click_2_0_0_default);

        tvLeftOrTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mActivity.finish();
                SystemUtility.SysApplication.getInstance().exit();

            }
        });

    }

    private void setUnKnow() {

        rTopBackground.setBackgroundResource(R.drawable.border_2_0_0_dialog_error);
        bgImg.setImageResource(R.drawable.icon_2_0_0_dialog_unknow);
        tvCenter.setVisibility(View.GONE);
        linBottom.setOrientation(LinearLayout.VERTICAL);

        tvMessage.setText(R.string.pinpinbox_2_0_0_dialog_message_unknow);

        tvLeftOrTop.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvLeftOrTop.setGravity(Gravity.LEFT);
        tvLeftOrTop.setText(R.string.pinpinbox_2_0_0_dialog_message_send_error_message);

        tvRightOrBottom.setText(R.string.pinpinbox_2_0_0_dialog_close);
        tvRightOrBottom.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvRightOrBottom.setBackgroundResource(R.drawable.click_2_0_0_default);

        tvRightOrBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        vDarkBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void sendErrorToFlurry(final String mPosition) {

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                FlurryUtil.onEvent(FlurryKey.UnKnow, mPosition);
                mActivity.finish();
            }
        });


    }

    public void setMessage(String strMessage) {
        tvMessage.setText(strMessage);
    }

    public void setMessage(int intMessage) {
        tvMessage.setText(intMessage);
    }

    public void setConnectInstability(ConnectInstability connectInstability) {
        this.connectInstability = connectInstability;
    }

    public void setCheckExecute(CheckExecute checkExecute) {
        this.checkExecute = checkExecute;
    }

    public void setDismissExcute(DismissExcute dismissExcute) {
        this.dismissExcute = dismissExcute;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }


}

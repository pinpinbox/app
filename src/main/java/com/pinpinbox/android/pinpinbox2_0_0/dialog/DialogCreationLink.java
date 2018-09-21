package com.pinpinbox.android.pinpinbox2_0_0.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

public class DialogCreationLink {

    public Activity activity;
    public Dialog mDialog;

    private ImageView confirmImg, cancelImg;
    private EditText edLinkName1, edLinkName2, edLinkUrl1, edLinkUrl2;

    public DialogCreationLink(final Activity activity) {
        this.activity = activity;
        mDialog = new Dialog(activity, R.style.myDialog);
        Window window = mDialog.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }
        window.setWindowAnimations(R.style.dialog_enter_exit);
        window.setContentView(R.layout.dialog_2_0_0_creation_link);

        edLinkName1 = mDialog.findViewById(R.id.edLinkName1);
        edLinkName2 = mDialog.findViewById(R.id.edLinkName2);
        edLinkUrl1 = mDialog.findViewById(R.id.edLinkUrl1);
        edLinkUrl2 = mDialog.findViewById(R.id.edLinkUrl2);


        confirmImg = mDialog.findViewById(R.id.confirmImg);
        cancelImg = mDialog.findViewById(R.id.cancelImg);

        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    public ImageView getConfirmImg(){
        return this.confirmImg;
    }

    public Dialog getDialog(){
        return this.mDialog;
    }

    public EditText getEdLinkName1() {
        return edLinkName1;
    }

    public EditText getEdLinkName2() {
        return edLinkName2;
    }

    public EditText getEdLinkUrl1() {
        return edLinkUrl1;
    }

    public EditText getEdLinkUrl2() {
        return edLinkUrl2;
    }
}

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

/**
 * Created by vmage on 2016/8/30.
 */
public class DialogCreationDescription {

    public Activity activity;
    public Dialog mDialog;

    private ImageView confirmImg, cancelImg;
    private EditText etDescription;

    public String string;

    public DialogCreationDescription(final Activity activity) {
        this.activity = activity;
        mDialog = new Dialog(activity, R.style.myDialog);
        Window window = mDialog.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
        }
        window.setWindowAnimations(R.style.dialog_enter_exit);
        window.setContentView(R.layout.dialog_2_0_0_creation_description);

        confirmImg = (ImageView)mDialog.findViewById(R.id.confirmImg);
        cancelImg = (ImageView)mDialog.findViewById(R.id.cancelImg);
        etDescription = (EditText)mDialog.findViewById(R.id.etDescription);

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

    public EditText getEtDescription(){
        return this.etDescription;
    }

    public Dialog getDialog(){
        return this.mDialog;
    }

}

package com.pinpinbox.android.Widget;

import android.app.Activity;
import android.app.Dialog;

import com.pinpinbox.android.DialogTool.DialogSet;
import com.pinpinbox.android.DialogTool.DialogV2Custom;
import com.pinpinbox.android.StringClass.DialogStyleClass;

/**
 * Created by vmage on 2016/5/20.
 */
public class NoConnect {

    public Dialog dlgNoConnect;

    public NoConnect(final Activity mActivity){


        DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.NOCONNECT);
        d.show();
        dlgNoConnect = d.getmDialog();


//                DialogSet d = new DialogSet(mActivity);
//                d.setNoConnect();
//                dlgNoConnect = d.getDialog();


    }

    public Dialog getDlgNoConnect(){
        return this.dlgNoConnect;
    }


}

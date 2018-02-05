package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.List;

/**
 * Created by kevin9594 on 2016/8/14.
 */
public class FromService2Activity extends Activity {

    private Activity mActivity;

    private String type_id = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        final SharedPreferences awsData = getSharedPreferences(SharedPreferencesDataClass.awsDetail, Activity.MODE_PRIVATE);
        final String type = awsData.getString("type", "");

        if (type == null || type.equals("")) {
            finish();
            return;
        }

        final DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.CHECK);
        d.getTvLeftOrTop().setVisibility(View.GONE);
        if (type != null && !type.equals("")) {
            type_id = awsData.getString("type_id", "");
            switch (type) {
                case "user":
                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_go_to_creator_area);
                    break;
                case "albumqueue":
                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_go_to_album_info);
                    break;

                case "albumqueue@messageboard":
                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_go_to_album_info);
                    break;

                case "albumcooperation":

                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_go_to_cooperation_manage);

                    break;

            }
        }
        d.getBlurView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                finish();
            }
        });
        d.getTvRightOrBottom().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();

                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                int count = activityList.size();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        String actName = activityList.get(i).getClass().getSimpleName();
                        MyLog.Set("d", this.getClass(), "activity simplename => " + actName);
                        if (!actName.equals("Main2Activity")) {
                            activityList.get(i).finish();
                        }
                    }
                }

                if (type != null && !type.equals("")) {
                    switch (type) {
                        case "user":
                            toUserArea();
                            break;
                        case "albumqueue":
                            toAlbumInfo();
                            break;

                        case "albumqueue@messageboard":


                            toAlbumInfoMessageBoard();

                            break;

                        case "albumcooperation":

                            toCooperation();

                            break;


                    }
                }
                awsData.edit().clear().commit();
            }
        });
        d.show();
    }

    //    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    private void toUserArea() {

        Bundle bundle = new Bundle();
        bundle.putString("author_id", type_id);

        Intent intent = new Intent(FromService2Activity.this, Author2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    private void toAlbumInfo() {

        Bundle bundle = new Bundle();
        bundle.putString("album_id", type_id);

        Intent intent = new Intent(FromService2Activity.this, AlbumInfo2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    private void toAlbumInfoMessageBoard() {
        Bundle bundle = new Bundle();
        bundle.putString("album_id", type_id);
        bundle.putBoolean(Key.pinpinboard, true);
        Intent intent = new Intent(mActivity, AlbumInfo2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void toCooperation() {

        Bundle bundle = new Bundle();
        bundle.putInt("toPage", 2);

        Intent intent = new Intent(mActivity, MyCollect2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();


    }


}

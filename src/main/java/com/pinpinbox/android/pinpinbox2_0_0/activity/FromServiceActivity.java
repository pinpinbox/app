package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.List;

/**
 * Created by kevin9594 on 2016/8/14.
 */
public class FromServiceActivity extends Activity {

    private Activity mActivity;

    private String type_id = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;

        final SharedPreferences awsData = getSharedPreferences(SharedPreferencesDataClass.awsDetail, Activity.MODE_PRIVATE);
        final String type = awsData.getString("type", "");

        if (type == null || type.equals("") || type.equals("null")) {

            String url = awsData.getString("url", "");

            if(url!=null && !url.equals("null") && !url.equals("")){

                ActivityIntent.toWeb(mActivity, url, "");

            }

            finish();
            return;
        }


        /*不跳提示 直接前往*/
        if(type!=null && !type.equals("null") && !type.equals("") && type.equals("categoryarea")){

            type_id = awsData.getString("type_id", "");

            Activity mainAc = removeOtherGetMainAc();

            ActivityIntent.toFeature(mainAc, StringIntMethod.StringToInt(type_id));
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

                case "user@messageboard":

                    d.setMessage("開啟留言版?");
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

                case "event":

                    d.setMessage("前往活動頁?");

                    break;

            }
        }
        d.getDarkBg().setOnClickListener(new View.OnClickListener() {
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

                Activity mainAc = removeOtherGetMainAc();

                if (type != null && !type.equals("")) {
                    switch (type) {

                        case "user":
                            toUserArea(false);
                            break;

                        case "user@messageboard":

                            if (!type_id.equals("") && type_id != null) {
                                if (type_id.equals(PPBApplication.getInstance().getId())) {
                                    //me
                                    finish();


                                    final Activity finalMainAc = mainAc;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((MainActivity) finalMainAc).toMePage(true);
                                            finish();//再添加一次以防殘留
                                        }
                                    },500);


                                } else {
                                    //other user
                                    MyLog.Set("e", mActivity.getClass(), "至其他用戶留言版");
                                    toUserArea(true);
                                }
                            }

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

                        case "event":

                            ActivityIntent.toEvent(mainAc, type_id);

                            break;


                    }
                }
                awsData.edit().clear().commit();
            }
        });
        d.show();
    }


    private Activity removeOtherGetMainAc(){

        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
        int count = activityList.size();

        Activity mActivity = null;

        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String actName = activityList.get(i).getClass().getSimpleName();
                MyLog.Set("d", this.getClass(), "activity simplename => " + actName);
                if (!actName.equals(MainActivity.class.getSimpleName())) {
                    activityList.get(i).finish();
                } else {
                    mActivity = activityList.get(i);
                }
            }
        }

        return mActivity;
    }

    //    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    private void toUserArea(boolean openBoard) {

        ActivityIntent.toUser(mActivity, false, openBoard, type_id, null, null, null);
        finish();

    }

    private void toAlbumInfo() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, type_id);
        bundle.putBoolean(Key.shareElement, false);
        Intent intent = new Intent(mActivity, AlbumInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        ActivityAnim.StartAnimFromBottom(mActivity);

    }

    private void toAlbumInfoMessageBoard() {
        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, type_id);
        bundle.putBoolean(Key.pinpinboard, true);
        bundle.putBoolean(Key.shareElement, false);
        Intent intent = new Intent(mActivity, AlbumInfoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        ActivityAnim.StartAnimFromBottom(mActivity);


    }

    private void toCooperation() {

        Bundle bundle = new Bundle();
        bundle.putInt("toPage", 2);

        Intent intent = new Intent(mActivity, MyCollectActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();


    }

}

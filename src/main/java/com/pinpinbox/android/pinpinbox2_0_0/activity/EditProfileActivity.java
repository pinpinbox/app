package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.PickerView;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.HobbyManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.RequestCodeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMe;
import com.pinpinbox.android.pinpinbox2_0_0.libs.crop.Crop;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol21_UpdateUser;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by vmage on 2017/3/1.
 */
public class EditProfileActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private SharedPreferences getData;
    private PopupCustom popBirthday;

    private Protocol21_UpdateUser protocol21;
    private UpdatePicTask updatePicTask;
    private FirstEditProfileTask firstEditProfileTask;
    private GetProfileTask profileTask;

    private File filePicture;

    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private EditText edName, edCreativearea, edEmail;
    private EditText edFacebook, edGoogle, edInstagram, edLinkedin, edPinterest, edTwitter, edYouTube, edWeb;
    private TextView tvPassword, tvPhone, tvBoy, tvGirl, tvPrivate, tvBirthday, tvHobby, tvConfirm, tvNewsletter;
    private ImageView backImg, selectNewsletterImg;
    private RoundedImageView profileImg;

    private String id, token;
    private String strGender = "1";
    private String strYear = "1980", strMonth = "06", strDay = "15";

    private int doingType;
    private static final int NONE = 0;
    private static final int PHOTO_GRAPH = 1;
    private static final int PHOTO_FILES = 2;
    private static final String IMAGE_UNSPECIFIED = "image/*";

    private boolean isChangePic = false;

    private boolean isSelectNewsletter = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_editprofile);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        init();

        setRefreshListener();

        doGetProfile();

    }

    private void init() {

        mActivity = this;

        getData = PPBApplication.getInstance().getData();
        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.pinPinBoxRefreshLayout);

        edName = (EditText) findViewById(R.id.edName);
        edCreativearea = (EditText) findViewById(R.id.edCreativearea);
        edEmail = (EditText) findViewById(R.id.edEmail);

        tvPassword = (TextView) findViewById(R.id.tvPassword);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvBoy = (TextView) findViewById(R.id.tvBoy);
        tvGirl = (TextView) findViewById(R.id.tvGirl);
        tvPrivate = (TextView) findViewById(R.id.tvPrivate);
        tvBirthday = (TextView) findViewById(R.id.tvBirthday);
        tvHobby = (TextView) findViewById(R.id.tvHobby);
        tvConfirm = (TextView) findViewById(R.id.tvConfirm);
        tvNewsletter = (TextView)findViewById(R.id.tvNewsletter);

        selectNewsletterImg = (ImageView)findViewById(R.id.selectNewsletterImg);
        backImg = (ImageView) findViewById(R.id.backImg);
        profileImg = (RoundedImageView) findViewById(R.id.profileImg);


        tvPassword.setOnClickListener(this);
        tvPhone.setOnClickListener(this);
        tvBoy.setOnClickListener(this);
        tvGirl.setOnClickListener(this);
        tvPrivate.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        tvHobby.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        backImg.setOnClickListener(this);
        profileImg.setOnClickListener(this);
        selectNewsletterImg.setOnClickListener(this);


//        TextUtility.setBold((TextView) findViewById(R.id.tvTitle), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv0), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv1), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv2), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv3), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv4), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv5), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv6), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv7), true);
//        TextUtility.setBold((TextView) findViewById(R.id.tv8), true);

        if (getData.getBoolean(Key.is_FB_Login, false)) {
            findViewById(R.id.linPassword).setVisibility(View.GONE);
        } else {
            findViewById(R.id.linPassword).setVisibility(View.VISIBLE);
        }

        /*20171002*/
        SmoothProgressBar pbRefresh = (SmoothProgressBar) findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(findViewById(R.id.vRefreshAnim), pbRefresh);

        edName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });


        /*20171108*/
        edFacebook = (EditText) findViewById(R.id.edFacebook);
        edGoogle = (EditText) findViewById(R.id.edGoogle);
        edInstagram = (EditText) findViewById(R.id.edInstagram);
        edLinkedin = (EditText) findViewById(R.id.edLinkedin);
        edPinterest = (EditText) findViewById(R.id.edPinterest);
        edTwitter = (EditText) findViewById(R.id.edTwitter);
        edYouTube = (EditText) findViewById(R.id.edYouTube);
        edWeb = (EditText) findViewById(R.id.edWeb);


        try {
            filePicture = FileUtility.createHeadFile(mActivity, id);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setProfile() {

        String profilepic = getData.getString(Key.profilepic, "");

        if (profilepic != null && !profilepic.equals("") && !profilepic.equals("null")) {
            Picasso.get().
                    load(profilepic).
                    config(Bitmap.Config.RGB_565).
                    tag(mActivity.getApplicationContext()).
                    error(R.drawable.member_back_head).
                    into(profileImg);
        } else {
            profileImg.setImageResource(R.drawable.member_back_head);
        }

        edName.setText(getData.getString(Key.nickname, ""));
        edCreativearea.setText(getData.getString(Key.creative_name, ""));
        edEmail.setText(getData.getString(Key.email, getData.getString(Key.account, "")));


        if (getData.getString(Key.gender, "2").equals("0")) {
            //is girl
            setIsGirl();
        } else if (getData.getString(Key.gender, "2").equals("1")) {
            //is boy
            setIsBoy();
        } else {
            //is private
            setIsPrivate();
        }


        tvBirthday.setText(getData.getString("year", "1980") +
                "-" + getData.getString("month", "06") +
                "-" + getData.getString("day", "15"));


        edFacebook.setText(getData.getString(Key.sociallink_facebook, ""));
        edGoogle.setText(getData.getString(Key.sociallink_google, ""));
        edInstagram.setText(getData.getString(Key.sociallink_instagram, ""));
        edLinkedin.setText(getData.getString(Key.sociallink_linkedin, ""));
        edPinterest.setText(getData.getString(Key.sociallink_pinterest, ""));
        edTwitter.setText(getData.getString(Key.sociallink_twitter, ""));
        edYouTube.setText(getData.getString(Key.sociallink_youtube, ""));
        edWeb.setText(getData.getString(Key.sociallink_web, ""));

        isSelectNewsletter = getData.getBoolean(Key.newsletter, true);
        selectNewsletter(isSelectNewsletter);




    }

    private void setIsGirl() {

        tvPrivate.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvPrivate.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

        tvBoy.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvBoy.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

        tvGirl.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvGirl.setBackgroundResource(R.drawable.border_2_0_0_click_default_radius);

        strGender = "0";

    }

    private void setIsBoy() {

        tvPrivate.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvPrivate.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

        tvBoy.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvBoy.setBackgroundResource(R.drawable.border_2_0_0_click_default_radius);

        tvGirl.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvGirl.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

        strGender = "1";

    }

    private void setIsPrivate() {

        tvPrivate.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvPrivate.setBackgroundResource(R.drawable.border_2_0_0_click_default_radius);

        tvBoy.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvBoy.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

        tvGirl.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvGirl.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

        strGender = "2";


    }

   private void selectNewsletter(boolean isSelect) {

        if (isSelect) {
            selectNewsletterImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
            isSelectNewsletter =  true;
        } else {
            selectNewsletterImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
            isSelectNewsletter =  false;
        }

    }

    private void setBirthdayPicker() {

        popBirthday = new PopupCustom(mActivity);
        popBirthday.setPopup(R.layout.pop_2_0_0_date, R.style.pinpinbox_popupAnimation_bottom);
        View v = popBirthday.getPopupView();

//        TextUtility.setBold((TextView) v.findViewById(R.id.tvTitle), true);

        TextView tvDateConfirm = v.findViewById(R.id.tvDateConfirm);

        PickerView pkYear = v.findViewById(R.id.pkYear);
        PickerView pkMonth = v.findViewById(R.id.pkMonth);
        PickerView pkDay = v.findViewById(R.id.pkDay);

        pkYear.setTextSize(DensityUtility.sp2px(mActivity.getApplicationContext(), 20));
        pkYear.setMarginAlpha(2.8f);
        pkYear.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

        pkMonth.setTextSize(DensityUtility.sp2px(mActivity.getApplicationContext(), 20));
        pkMonth.setMarginAlpha(2.8f);
        pkMonth.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

        pkDay.setTextSize(DensityUtility.sp2px(mActivity.getApplicationContext(), 20));
        pkDay.setMarginAlpha(2.8f);
        pkDay.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);

        List<String> listYear = new ArrayList<>();
        List<String> listMonth = new ArrayList<>();
        List<String> listDay = new ArrayList<>();

        for (int i = 1900; i < year + 1; i++) {
            listYear.add(i + "");
        }
        pkYear.setData(listYear);

        for (int i = 1; i < 13; i++) {
            listMonth.add(i + "");
        }
        pkMonth.setData(listMonth);

        for (int i = 1; i < 32; i++) {
            listDay.add(i + "");
        }
        pkDay.setData(listDay);

        strYear = getData.getString("year", "1980");
        strMonth = getData.getString("month", "06");
        strDay = getData.getString("day", "15");

        try {
            int iy = StringIntMethod.StringToInt(strYear);
            int im = StringIntMethod.StringToInt(strMonth);
            int id = StringIntMethod.StringToInt(strDay);
            int y = iy - 1900;
            pkYear.setSelected(y);
            pkMonth.setSelected(im - 1);
            pkDay.setSelected(id - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        pkYear.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int p) {
                strYear = text;
            }
        });
        pkMonth.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int p) {
                strMonth = text;
            }
        });
        pkDay.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text, int p) {
                strDay = text;
            }
        });


        tvDateConfirm.setOnClickListener(this);

    }

    private void setRefreshListener() {

        pinPinBoxRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                doGetProfile();
            }

            @Override
            public void onPullDistance(int distance) {


            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });

    }

    private void toCamera() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTO_FILES);
    }

    public void startPhotoZoom(Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");//调用Android系统自带的一个图片剪裁页面,
//        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
//        intent.putExtra("crop", "true");//进行修剪
//        intent.putExtra("aspectX", 1);// aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 300);// outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputY", 300);
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, PHOTO_RESOULT);


    }

    public Uri queryUri(File file) {

        final String where = MediaStore.Images.Media.DATA + "='" + file.getAbsolutePath() + "'";

        Cursor cursor = mActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, where, null, null);
        if (cursor == null) {
            return null;
        }
        int id = -1;
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                id = cursor.getInt(0);
            }
            cursor.close();
        }

        if (id == -1) {
            return null;
        }
        return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id));

    }

    private void checkFirstEdit() {
        boolean FirstEditTaskDone = getData.getBoolean(TaskKeyClass.firsttime_edit_profile, false);
        if (FirstEditTaskDone) {
            back();
        } else {
            doFristEdit();
        }
    }

    private void back() {
        finish();
        ActivityAnim.FinishAnim(mActivity);
    }

    private void doUpdateProfile() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put(Key.birthday, tvBirthday.getText().toString());
            jsonObject.put(Key.creative_name, edCreativearea.getText().toString());
            jsonObject.put(Key.email, edEmail.getText().toString());
            jsonObject.put(Key.name, edName.getText().toString());
            jsonObject.put(Key.gender, strGender);


            JSONObject jsonSociallink = new JSONObject();
            jsonSociallink.put(ProtocolKey.web, edWeb.getText().toString());
            jsonSociallink.put(ProtocolKey.facebook, edFacebook.getText().toString());
            jsonSociallink.put(ProtocolKey.google, edGoogle.getText().toString());
            jsonSociallink.put(ProtocolKey.twitter, edTwitter.getText().toString());
            jsonSociallink.put(ProtocolKey.youtube, edYouTube.getText().toString());
            jsonSociallink.put(ProtocolKey.instagram, edInstagram.getText().toString());
            jsonSociallink.put(ProtocolKey.pinterest, edPinterest.getText().toString());
            jsonSociallink.put(ProtocolKey.linkedin, edLinkedin.getText().toString());


//            String sociallink = jsonSociallink.toString();

            jsonObject.put(ProtocolKey.sociallink, jsonSociallink);

            jsonObject.put(ProtocolKey.newsletter, isSelectNewsletter);


        } catch (Exception e) {
            e.printStackTrace();
        }

        String param = jsonObject.toString();

        protocol21 = new Protocol21_UpdateUser(mActivity, id, token, param, new Protocol21_UpdateUser.TaskCallBack() {
            @Override
            public void Prepare() {
                doingType = DoingTypeClass.DoUpdateProfile;
                startLoading();
            }

            @Override
            public void Post() {
                dissmissLoading();
            }

            @Override
            public void Success() {

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_edit_finish);

                SharedPreferences.Editor editor = getData.edit();

                editor.putBoolean("datachange", true);//2016.03.05新增 判斷是否已變更
                editor.putString(Key.nickname, edName.getText().toString());
                editor.putString(Key.email, edEmail.getText().toString());
                editor.putString(Key.gender, strGender);
                editor.putString(Key.creative_name, edCreativearea.getText().toString());
                editor.putString(Key.birthday, tvBirthday.getText().toString());
                editor.putString("year", strYear);
                editor.putString("month", strMonth);
                editor.putString("day", strDay);
                editor.commit();


                FragmentMe.doRefreshInOtherClass();

                if (isChangePic) {
                    doUpdatePic();
                } else {
                    checkFirstEdit();
                }

            }

            @Override
            public void TimeOut() {
                doUpdateProfile();
            }
        });
    }

    private void doUpdatePic() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        updatePicTask = new UpdatePicTask();
        updatePicTask.execute();
    }

    private void doFristEdit() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        firstEditProfileTask = new FirstEditProfileTask();
        firstEditProfileTask.execute();
    }

    private void doGetProfile() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        profileTask = new GetProfileTask();
        profileTask.execute();

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoUpdateProfile:
                        doUpdateProfile();
                        break;

                    case DoingTypeClass.DoFristEdit:
                        doFristEdit();
                        break;

                    case DoingTypeClass.DoRefresh:
                        doGetProfile();
                        break;


                }
            }
        };


        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    private class UpdatePicTask extends AsyncTask<Void, Void, Object> {

        private int p05Result = -1;
        private String p05Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            if (filePicture.exists()) {

                String filename = filePicture.getName();

                Map<String, String> signdata = new HashMap<String, String>();
                signdata.put("id", id);
                signdata.put("token", token);
                String head_sign = IndexSheet.encodePPB(signdata);
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                map.put("token", token);
                map.put("picfile", filename);
                map.put("sign", head_sign);

                String strJson = "";
                try {
                    strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P05_UpdateProfilePic, map, filePicture);
                    MyLog.Set("d", getClass(), "p05strjson => " + strJson);
                } catch (SocketTimeoutException timeout) {
                    p05Result = Key.TIMEOUT;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (strJson != null && !strJson.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        p05Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                        if (p05Result == 1) {
                            String strProfilepic = JsonUtility.GetString(jsonObject, ProtocolKey.data);//pic url
                            getData.edit().putString("profilepic", strProfilepic).commit();
                        } else if (p05Result == 0) {
                            p05Message = jsonObject.getString(ProtocolKey.message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p05Result == 1) {


                FragmentMe.doRefreshInOtherClass();

                checkFirstEdit();

            } else if (p05Result == 0) {

                DialogV2Custom.BuildError(mActivity, p05Message);


            } else if (p05Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }

    }

    private class FirstEditProfileTask extends AsyncTask<Void, Void, Object> {

        private String name;
        private String reward;
        private String reward_value;
        private String url;

        private int p83Result = -1;
        private String p83Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFristEdit;
            startLoading();
        }


        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, SetMapByProtocol.setParam83_dotask(id, token, TaskKeyClass.firsttime_edit_profile, "google"), null);
                MyLog.Set("d", getClass(), "p83strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p83Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = JsonUtility.GetInt(jsonObject, Key.result);

                    if (p83Result == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, Key.data);

                        JSONObject object = new JSONObject(jdata);
                        String task = JsonUtility.GetString(object, Key.task);
                        String event = JsonUtility.GetString(object, Key.event);

                        JSONObject taskObj = new JSONObject(task);
                        name = JsonUtility.GetString(taskObj, Key.name);
                        reward = JsonUtility.GetString(taskObj, Key.reward);
                        reward_value = JsonUtility.GetString(taskObj, Key.reward_value);

                        JSONObject eventObj = new JSONObject(event);
                        url = JsonUtility.GetString(eventObj, Key.url);

                    } else if (p83Result == 2) {
                        p83Message = JsonUtility.GetString(jsonObject, Key.message);
                    } else if (p83Result == 0) {
                        p83Message = JsonUtility.GetString(jsonObject, Key.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p83Result == 1) {

                final DialogHandselPoint d = new DialogHandselPoint(mActivity);
                d.getTvTitle().setText(name);
                if (reward.equals("point")) {
                    d.getTvDescription().setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + " " + reward_value + " P !");
                    /**獲取當前使用者P點*/
                    String point = getData.getString("point", "");
                    int p = StringIntMethod.StringToInt(point);

                    /**任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /**加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /**儲存data*/
                    getData.edit().putString("point", newP).commit();

                    getData.edit().putBoolean(TaskKeyClass.firsttime_edit_profile, true).commit();

                }


                if (url == null || url.equals("") || url.equals("null")) {
                    d.getTvLink().setVisibility(View.GONE);
                } else {
                    d.getTvLink().setVisibility(View.VISIBLE);
                    d.getTvLink().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            Intent intent = new Intent(mActivity, WebViewActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);
                        }
                    });
                }


                d.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        back();
                    }
                });


            } else if (p83Result == 2) {

                getData.edit().putBoolean(TaskKeyClass.firsttime_edit_profile, true).commit();
                back();

            } else if (p83Result == 0) {

                MyLog.Set("d", getClass(), "p83Message" + p83Message);

                getData.edit().putBoolean(TaskKeyClass.firsttime_edit_profile, true).commit();
                back();

            } else if (p83Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    private class GetProfileTask extends AsyncTask<Void, Void, Object> {

        private int p28Result = -1;
        private String p28Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoRefresh;

            tvConfirm.setClickable(false);
        }

        @Override
        protected Object doInBackground(Void... params) {

            try {

                String strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P28_Getprofile, SetMapByProtocol.setParam28_getprofile(id, token), null);

                if (BuildConfig.DEBUG) {
                    Logger.json(strJson);
                }

                JSONObject jsonObject = new JSONObject(strJson);
                p28Result = JsonUtility.GetInt(jsonObject, Key.result);
                if (p28Result == 1) {
                    String strData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONObject object = new JSONObject(strData);

                    String jsonBirthday = JsonUtility.GetString(object, ProtocolKey.birthday);

                    String jsonGender = JsonUtility.GetString(object, ProtocolKey.gender);

                    String jsonNickname = JsonUtility.GetString(object, ProtocolKey.nickname);

                    String jsonProfilepic = JsonUtility.GetString(object, ProtocolKey.profilepic);

                    String jsonSelfdescription = JsonUtility.GetString(object, ProtocolKey.selfdescription);

                    String jsonCellphone = JsonUtility.GetString(object, ProtocolKey.cellphone);

                    String jsonEmail = JsonUtility.GetString(object, ProtocolKey.email);

                    String jsonUsergrade = JsonUtility.GetString(object, ProtocolKey.usergrade);

                    boolean bCreative = JsonUtility.GetBoolean(object, ProtocolKey.creative);

                    int follow = JsonUtility.GetInt(object, Key.follow);
                    int viewed = JsonUtility.GetInt(object, Key.viewed);

                    String creative_name = JsonUtility.GetString(object, ProtocolKey.creative_name);

                    String strCover = JsonUtility.GetString(object, ProtocolKey.cover);

                    boolean isNewsletter = JsonUtility.GetBoolean(object, ProtocolKey.newsletter);


                    SharedPreferences.Editor editor = getData.edit();
                    editor.putString(Key.id, id);
                    editor.putString(Key.token, token);
                    editor.putString(Key.gender, jsonGender);
                    editor.putString(Key.birthday, jsonBirthday);

                    String year = jsonBirthday.substring(0, jsonBirthday.indexOf("-"));
                    String month = jsonBirthday.substring(jsonBirthday.indexOf("-") + 1, jsonBirthday.lastIndexOf("-"));
                    String day = jsonBirthday.substring(jsonBirthday.lastIndexOf("-") + 1);
                    editor.putString("year", year);

                    editor.putString("month", month);
                    editor.putString("day", day);
                    editor.putString(Key.selfdescription, jsonSelfdescription);
                    editor.putString(Key.nickname, jsonNickname);
                    editor.putString(Key.cellphone, jsonCellphone);
                    editor.putString(Key.profilepic, jsonProfilepic);
                    editor.putString(Key.email, jsonEmail);
                    editor.putString(Key.usergrade, jsonUsergrade);
                    editor.putInt(Key.follow, follow);
                    editor.putInt(Key.viewed, viewed);
                    editor.putBoolean(Key.creative, bCreative);
                    editor.putString(Key.creative_name, creative_name);
                    editor.putString(Key.cover, strCover);
                    editor.putBoolean(Key.newsletter, isNewsletter);

                    /*20171108*/
                    String sociallink = JsonUtility.GetString(object, ProtocolKey.sociallink);
                    if (sociallink != null && !sociallink.equals("") && !sociallink.equals("null")) {
                        JSONObject jsonLink = new JSONObject(sociallink);
                        editor.putString(Key.sociallink_facebook, JsonUtility.GetString(jsonLink, ProtocolKey.facebook));
                        editor.putString(Key.sociallink_google, JsonUtility.GetString(jsonLink, ProtocolKey.google));
                        editor.putString(Key.sociallink_instagram, JsonUtility.GetString(jsonLink, ProtocolKey.instagram));
                        editor.putString(Key.sociallink_linkedin, JsonUtility.GetString(jsonLink, ProtocolKey.linkedin));
                        editor.putString(Key.sociallink_pinterest, JsonUtility.GetString(jsonLink, ProtocolKey.pinterest));
                        editor.putString(Key.sociallink_twitter, JsonUtility.GetString(jsonLink, ProtocolKey.twitter));
                        editor.putString(Key.sociallink_youtube, JsonUtility.GetString(jsonLink, ProtocolKey.youtube));
                        editor.putString(Key.sociallink_web, JsonUtility.GetString(jsonLink, ProtocolKey.web));
                    }
                    /* *********************************************************************/

                    editor.commit();


                    //20171103
                    String hobby = JsonUtility.GetString(object, ProtocolKey.hobby);
                    HobbyManager.SaveHobbyList(hobby);


                } else if (p28Result == 0) {
                    p28Message = JsonUtility.GetString(jsonObject, Key.message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            pinPinBoxRefreshLayout.setRefreshing(false);

            tvConfirm.setClickable(true);

            if (p28Result == 1) {

                setProfile();

                if (pinPinBoxRefreshLayout.getAlpha() == 0f) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ViewPropertyAnimator alphaTo1 = pinPinBoxRefreshLayout.animate();
                            alphaTo1.setDuration(400)
                                    .alpha(1)
                                    .start();

                        }
                    }, 200);

                }


            } else if (p28Result == 0) {

                DialogV2Custom.BuildError(mActivity, p28Message);

            } else if (p28Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvGirl:
                setIsGirl();
                break;

            case R.id.tvBoy:
                setIsBoy();
                break;

            case R.id.tvPrivate:
                setIsPrivate();
                break;

            case R.id.backImg:
                back();
                break;

            case R.id.tvBirthday:
                if (popBirthday == null) {
                    setBirthdayPicker();
                }
                popBirthday.show((RelativeLayout) findViewById(R.id.rBackground));
                break;

            case R.id.tvDateConfirm:
                tvBirthday.setText(strYear + "-" + strMonth + "-" + strDay);
                popBirthday.dismiss();
                break;

            case R.id.profileImg:

                commonCheckPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        RequestCodeClass.REQUEST_CODE_SDCARD,
                        R.string.pinpinbox_2_0_0_dialog_message_open_permission_sdcard,
                        new CheckPermissionCallBack() {
                            @Override
                            public void success() {
                                toCamera();
                            }
                        });


                break;

            case R.id.tvConfirm:
                doUpdateProfile();
                break;

            case R.id.tvPassword:
                Intent i1 = new Intent(mActivity, ChangePasswordActivity.class);
                startActivity(i1);
                ActivityAnim.StartAnim(mActivity);
                break;

            case R.id.tvPhone:

                Intent i2 = new Intent(mActivity, ChangePhoneActivity.class);
                startActivity(i2);
                ActivityAnim.StartAnim(mActivity);

                break;

            case R.id.tvHobby:

                Bundle bundle = new Bundle();
                bundle.putBoolean("hideActionBar", false);

                Intent i3 = new Intent(mActivity, HobbyActivity.class);
                i3.putExtras(bundle);
                startActivity(i3);
                ActivityAnim.StartAnim(mActivity);
                break;

            case R.id.selectNewsletterImg:

                if(isSelectNewsletter){
                    selectNewsletter(false);
                }else {
                    selectNewsletter(true);
                }

                break;


        }

    }


    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        MyLog.Set("d", getClass(), "onActivityResult");
        MyLog.Set("d", getClass(), "requestCode => " + requestCode);
        MyLog.Set("d", getClass(), "resultCode => " + resultCode);

        try {
            if (resultCode == NONE) {
                return;
            }

            if (data == null) {
                return;
            }

            switch (requestCode) {
                case PHOTO_GRAPH:

                    Crop.of(data.getData(), Uri.fromFile(filePicture))
                            .asSquare()
                            .start(mActivity);


                    break;

                case PHOTO_FILES:

//                    startPhotoZoom(data.getData());

                    Crop.of(data.getData(), Uri.fromFile(filePicture))
                            .asSquare()
                            .start(mActivity);

                    break;


                case Crop.REQUEST_CROP:
                    Bundle extras = data.getExtras();
                    if (extras != null) {

                        Picasso.get()
                                .load(filePicture)
                                .config(Bitmap.Config.RGB_565)
                                .resize(160, 160)
                                .error(R.drawable.member_back_head)
                                .tag(mActivity.getApplicationContext())
                                .centerCrop()
                                .into(profileImg);


                        isChangePic = true;
                    }
                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        SystemUtility.SysApplication.getInstance().removeActivity(this);

        cancelTask(protocol21);
        cancelTask(updatePicTask);
        cancelTask(firstEditProfileTask);
        cancelTask(profileTask);

        if (filePicture != null) {
            Picasso.get().invalidate(filePicture);
        }
        profileImg.setImageBitmap(null);
        profileImg.setImageDrawable(null);
        profileImg.setImageResource(0);

        if (filePicture != null && filePicture.exists()) {
            filePicture.delete();
            filePicture = null;
        }

        Recycle.IMG(backImg);

        System.gc();

        super.onDestroy();
    }


}

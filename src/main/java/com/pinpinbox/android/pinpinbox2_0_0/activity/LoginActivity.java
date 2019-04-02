package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.orhanobut.logger.Logger;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SampleTest.OldMainActivity;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.CreateDir;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.HobbyManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.UrlClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentScanSearch;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol95_RefreshToken;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol98_BusinessSubUserFastRegister;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopPicker;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vmage on 2017/1/13.
 */
public class LoginActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private SharedPreferences getdata, fbData;
    private NoConnect noConnect;
    private CallbackManager callbackManager;

    private FragmentScanSearch fragmentScanSearch;

    private PopPicker popPicker;
    private CountDownTimer cTimer;


    private LoginTask loginTask;
    private FBLoginTask fbLoginTask;
    private NoAccountToRegisterTask noAccountToRegisterTask;
    private CheckEmailTask checkEmailTask;
    private RequestSmsTask requestSmsTask;
    private GetPasswordTask getPasswordTask;
    private Protocol95_RefreshToken protocol95;
    private Protocol98_BusinessSubUserFastRegister protocol98;


    private RelativeLayout rBackground;
    private View vLogin, vRegister, vVerificationCode, vGetPassword;
    private FrameLayout frame;


    /*login*/
    private TextView tvLoginLogin, tvLoginToRegister, tvLoginFacebook, tvAboutUs, tvLoginTitle, tvLoginScanToRegister, tvLoginNorm;
    private EditText edLoginMail, edLoginPassword;
    private ImageView loginGetPasswordImg;

    /*register*/
    private TextView tvRegisterTitle, tvRegisterNext, tvRegCheckEmail, tvRegPassword, tvRegPasswordCheck;
    private EditText edRegNickname, edRegMail, edRegPassword, edRegPasswordCheck;
    private LinearLayout linRegisterBack;


    /*verification code*/
    private LinearLayout linVerBack;
    private TextView tvVerTitle, tvVerCountry, tvVerSendPhone, tvVerFinish, tvVerTime, tvVerNewsletter;
    private EditText edVerPhone, edVerCode;
    private RelativeLayout rVerCode;
    private ImageView verSelectNewsletterImg;

    /*get password*/
    private LinearLayout linPwdBack;
    private TextView tvPwdTitle, tvPwdCountry, tvPwdSendPhone;
    private EditText edPwdPhone, edPwdMail;
    private RelativeLayout rRegNickname, rRegMail, rRegPassword, rRegPasswordCheck;
    private RelativeLayout rPwdMail;

    private String id = "";
    private String token = "";
    private String businessuser_id = "";

    private int p28Result = -1;
    private String p28Message = "";

    private static final int REQUEST_CODE_CAMERA = 104;
    private static final int REQUEST_CODE_SMS = 105;

    private int doingType;
    private int hobbys = 0;

    private double latitude = 0.0;
    private double longitude = 0.0;

    private boolean isAlreadInitRegister = false; //已設置註冊畫面
    private boolean isAlreadInitVerificationCode = false; //已設置驗證碼畫面
    private boolean isClickNext = false; //是否點擊下一步
    private boolean isSelectCountryByVer = false; //是否是在驗證碼畫面選擇國碼
    private boolean isFacebookRegister = false;
    private boolean isBusinessRegister = false;
    private boolean isToHobby = false;


    private boolean isNicknameAlready = false;
    private boolean isEmail = false;
    private boolean isCheckedEmail = false;
    private boolean isPassword = false;
    private boolean isPasswordCheck = false;

    private boolean isPhone = false;
    private boolean isCanSendAgain = true;
    private boolean isVerCode = false;

    private boolean isPwdPhone = false;
    private boolean isPwdMail = false;

    private boolean scanIntent = false;

    private boolean isSelectNewsletter = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setFBcontent();
        setContentView(R.layout.activity_2_0_0_login);
        setSwipeBackEnable(false);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();

        init();

        setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));

    }

    public void testButton() {

        HorizontalScrollView svHorizontal = (HorizontalScrollView) vLogin.findViewById(R.id.svHorizontal);


        if (BuildConfig.FLAVOR.equals("w3_private") || BuildConfig.FLAVOR.equals("platformvmage5_private")) {


            svHorizontal.setVisibility(View.VISIBLE);


            Button btMenu = (Button) vLogin.findViewById(R.id.btMenu);
            Button btTest01 = (Button) vLogin.findViewById(R.id.btTest01);
            Button btC1 = (Button) vLogin.findViewById(R.id.btC1);
            Button btC2 = (Button) vLogin.findViewById(R.id.btC2);
            Button btC3 = (Button) vLogin.findViewById(R.id.btC3);
            Button btC4 = (Button) vLogin.findViewById(R.id.btC4);
            Button btC5 = (Button) vLogin.findViewById(R.id.btC5);
            Button btC6 = (Button) vLogin.findViewById(R.id.btC6);

            TestClick testClick = new TestClick();
            btMenu.setOnClickListener(testClick);
            btTest01.setOnClickListener(testClick);
            btC1.setOnClickListener(testClick);
            btC2.setOnClickListener(testClick);
            btC3.setOnClickListener(testClick);
            btC4.setOnClickListener(testClick);
            btC5.setOnClickListener(testClick);
            btC6.setOnClickListener(testClick);


        } else if (BuildConfig.FLAVOR.equals("www_private")) {
            svHorizontal.setVisibility(View.INVISIBLE);
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            svHorizontal.setVisibility(View.INVISIBLE);
        }


    }

    private class TestClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btMenu:
                    Intent intent = new Intent(mActivity, OldMainActivity.class);
                    startActivity(intent);
                    finish();
                    ActivityAnim.StartAnim(mActivity);
                    break;

                case R.id.btTest01:
                    edLoginMail.setText("test01@vmage.com");
                    edLoginPassword.setText("qqqqqqqq");

//                    edLoginMail.setText("k57764328@vmage.com.tw");
//                    edLoginPassword.setText("qqqqqqqq");

                    break;

                case R.id.btC1:
                    edLoginMail.setText("cailum_1@vmage.com.tw");
                    edLoginPassword.setText("jimlyt0713");
                    break;

                case R.id.btC2:
                    edLoginMail.setText("cailum_2@gmail.com.tw");
                    edLoginPassword.setText("jimlyt0713");
                    break;

                case R.id.btC3:
                    edLoginMail.setText("cailum_3@gmail.com.tw");
                    edLoginPassword.setText("jimlyt0713");
                    break;

                case R.id.btC4:
                    edLoginMail.setText("cailum_4@gmail.com.tw");
                    edLoginPassword.setText("jimlyt0713");
                    break;

                case R.id.btC5:
                    edLoginMail.setText("cailum_5@gmail.com.tw");
                    edLoginPassword.setText("jimlyt0713");
                    break;

                case R.id.btC6:
                    edLoginMail.setText("cailum_6@gmail.com.tw");
                    edLoginPassword.setText("jimlyt0713");
                    break;


            }
        }
    }

    private void setFBcontent() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

//                        if (loading.dialog().isShowing()) {
//                            loading.dismiss();
//                        }

                        dissmissLoading();

                        MyLog.Set("d", mActivity.getClass(), "fblogin => onSuccess");
                        MyLog.Set("d", mActivity.getClass(), "loginResult.getAccessToken().toString() => " + loginResult.getAccessToken().toString());

//                        {AccessToken token:ACCESS_TOKEN_REMOVED permissions:[user_friends, contact_email, email, public_profile, user_birthday, user_about_me]}

                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                if (jsonObject != null) {
                                    getFacebookJsonObjectToDoLogin(jsonObject);
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,birthday,gender,picture.type(large)");
                        request.setParameters(parameters);
                        request.executeAsync();


//                        Bundle bundle = new Bundle();
//                        bundle.putString("fields",
//                                "id,icon,permalink_url,source");
//
//                        new GraphRequest(
//                                AccessToken.getCurrentAccessToken(),
//                                "625054814355249",
//                                bundle,
//                                HttpMethod.GET,
//                                new GraphRequest.Callback() {
//                                    public void onCompleted(GraphResponse response) {
//                                         /* handle the result */
//                                        MyLog.Set("d", LoginActivity.class, "response.toString() => " + response.toString());
//                                        MyLog.Set("d", LoginActivity.class, "response.getRawResponse() => " + response.getRawResponse());
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(response.getRawResponse());
//                                            String source = JsonUtility.GetString(jsonObject, "source");
//
//                                            Bundle b = new Bundle();
//                                            b.putString("videopath", source);
//
//                                            Intent intent  = new Intent(mActivity, VideoPlayActivity.class);
//                                            startActivity(intent.putExtras(b));
//                                            ActivityAnim.StartAnim(mActivity);
//
//
//                                        }catch (Exception e){
//                                            e.printStackTrace();
//                                        }
//
//
//
//                                    }
//                                }
//                        ).executeAsync();


                    }

                    @Override
                    public void onCancel() {
//                        if (loading.dialog().isShowing()) {
//                            loading.dismiss();
//                        }
                        dissmissLoading();
                        MyLog.Set("d", mActivity.getClass(), "fblogin => onCancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        MyLog.Set("e", mActivity.getClass(), "fblogin => onError");
//                        if (loading.dialog().isShowing()) {
//                            loading.dismiss();
//                        }
                        dissmissLoading();
                        exception.printStackTrace();

                    }

                });

    }

    private void init() {

        mActivity = this;

        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        fbData = getSharedPreferences(SharedPreferencesDataClass.fb_registrationData, Activity.MODE_PRIVATE);
        PPBApplication.getInstance().setSharedPreferences(getdata);
        PPBApplication.getInstance().setSharedPreferencesByFacebook(fbData);

        rBackground = (RelativeLayout) findViewById(R.id.rBackground);

        vLogin = LayoutInflater.from(this).inflate(R.layout.lay_2_0_0_login, null);
        vRegister = LayoutInflater.from(this).inflate(R.layout.lay_2_0_0_register, null);
        vVerificationCode = LayoutInflater.from(this).inflate(R.layout.lay_2_0_0_get_verificationcode, null);
        vGetPassword = LayoutInflater.from(this).inflate(R.layout.lay_2_0_0_get_password, null);

        rBackground.addView(vLogin);

        initViewLogin();
        initViewRegister();
        initViewVerificationCode();
        initViewGetPassword();

        /*set bold*/
        setTextBold();

        /*set default listener*/
        setLoginEditTextListener();
        setGetPasswordListener();

        /*set click*/
        setClickControl();

        /*default*/
        toLogin();

        testButton();

    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            scanIntent = bundle.getBoolean(Key.scanIntent, false);
            businessuser_id = bundle.getString(Key.businessuser_id, "");

            MyLog.Set("d", getClass(), "(LoginActivity)scanIntent => " + scanIntent);

        }
    }

    private void initViewLogin() {

        frame = vLogin.findViewById(R.id.frame);

        tvLoginLogin = vLogin.findViewById(R.id.tvLoginLogin);
        tvLoginToRegister = vLogin.findViewById(R.id.tvLoginToRegister);
        tvLoginFacebook = vLogin.findViewById(R.id.tvLoginFacebook);
        tvAboutUs = vLogin.findViewById(R.id.tvAboutUs);
        tvLoginTitle = vLogin.findViewById(R.id.tvLoginTitle);
        tvLoginScanToRegister = vLogin.findViewById(R.id.tvLoginScanToRegister);
        tvLoginNorm = vLogin.findViewById(R.id.tvLoginNorm);


        edLoginMail = vLogin.findViewById(R.id.edLoginMail);
        edLoginPassword = vLogin.findViewById(R.id.edLoginPassword);
        edLoginPassword.setImeOptions(EditorInfo.IME_ACTION_SEND);
        edLoginPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    login();
                }
                return false;
            }
        });

        loginGetPasswordImg = vLogin.findViewById(R.id.loginGetPasswordImg);


        if (scanIntent) {

            if (businessuser_id != null && !businessuser_id.equals("") && !businessuser_id.equals("null")) {

                isBusinessRegister = true;

                getFacebookProfile();

            } else {

                //登出後返回登入
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toScan();
                    }
                }, 500);
            }

        }
    }

    private void initViewRegister() {
        tvRegisterTitle = vRegister.findViewById(R.id.tvRegisterTitle);
        tvRegisterNext = vRegister.findViewById(R.id.tvRegisterNext);
        tvRegCheckEmail = vRegister.findViewById(R.id.tvRegCheckEmail);
        tvRegPassword = vRegister.findViewById(R.id.tvRegPassword);
        tvRegPasswordCheck = vRegister.findViewById(R.id.tvRegPasswordCheck);

        edRegNickname = vRegister.findViewById(R.id.edRegNickname);
        edRegMail = vRegister.findViewById(R.id.edRegMail);
        edRegPassword = vRegister.findViewById(R.id.edRegPassword);
        edRegPasswordCheck = vRegister.findViewById(R.id.edRegPasswordCheck);

        linRegisterBack = vRegister.findViewById(R.id.linRegisterBack);

        rRegNickname = vRegister.findViewById(R.id.rRegNickname);
        rRegMail = vRegister.findViewById(R.id.rRegMail);
        rRegPassword = vRegister.findViewById(R.id.rRegPassword);
        rRegPasswordCheck = vRegister.findViewById(R.id.rRegPasswordCheck);

    }

    private void initViewVerificationCode() {
        linVerBack = vVerificationCode.findViewById(R.id.linVerBack);

        tvVerTitle = vVerificationCode.findViewById(R.id.tvVerTitle);
        tvVerCountry = vVerificationCode.findViewById(R.id.tvVerCountry);
        tvVerSendPhone = vVerificationCode.findViewById(R.id.tvVerSendPhone);
        tvVerFinish = vVerificationCode.findViewById(R.id.tvVerFinish);
        tvVerTime = vVerificationCode.findViewById(R.id.tvVerTime);
        tvVerNewsletter = vVerificationCode.findViewById(R.id.tvVerNewsletter);

        edVerPhone = vVerificationCode.findViewById(R.id.edVerPhone);
        edVerCode = vVerificationCode.findViewById(R.id.edVerCode);

        rVerCode = vVerificationCode.findViewById(R.id.rVerCode);

        verSelectNewsletterImg = vVerificationCode.findViewById(R.id.verSelectNewsletterImg);


        selectNewsletter(true);

    }

    private void initViewGetPassword() {
        linPwdBack = vGetPassword.findViewById(R.id.linPwdBack);

        tvPwdTitle = vGetPassword.findViewById(R.id.tvPwdTitle);
        tvPwdCountry = vGetPassword.findViewById(R.id.tvPwdCountry);
        tvPwdSendPhone = vGetPassword.findViewById(R.id.tvPwdSendPhone);

        edPwdPhone = vGetPassword.findViewById(R.id.edPwdPhone);
        edPwdMail = vGetPassword.findViewById(R.id.edPwdMail);

        rPwdMail = vGetPassword.findViewById(R.id.rPwdMail);

    }

    private void setTextBold() {

//        /*login*/
//        TextUtility.setBold(tvLoginTitle, tvLoginLogin, tvLoginToRegister, tvLoginFacebook, tvAboutUs);
//
//
//        /*register*/
//        TextUtility.setBold(tvRegisterTitle, tvRegisterNext, tvRegPassword, tvRegPasswordCheck);
//
//
//        /*verification code*/
//        TextUtility.setBold(tvVerTitle, tvVerSendPhone, tvVerFinish);
//
//        /*get password*/
//        TextUtility.setBold(tvPwdTitle, tvPwdSendPhone);

    }

    private void setLoginEditTextListener() {


        edLoginMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                MyLog.Set("d", LoginActivity.class, s.toString());

                edPwdMail.setText(s.toString());


            }
        });

        String defaultAccount = PPBApplication.getInstance().getData().getString(Key.account, "");

        edLoginMail.setText(defaultAccount);


    }

    private void setRegEditTextListener() {

        edRegNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() > 0) {

                    rRegNickname.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                    isNicknameAlready = true;

                } else {
                    rRegNickname.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    isNicknameAlready = false;
                }

            }
        });

        edRegMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                /*reset check email*/
                if (isCheckedEmail) {
                    tvRegCheckEmail.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);
                    tvRegCheckEmail.setClickable(true);
                    tvRegCheckEmail.setText(R.string.pinpinbox_2_0_0_button_examination);
                    tvRegCheckEmail.setTextColor(Color.parseColor(ColorClass.WHITE));
                    isCheckedEmail = false;

                }

                if (isEmail(s.toString())) {
                    isEmail = true;
                    rRegMail.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                } else {
                    isEmail = false;
                    rRegMail.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                }
            }
        });

        edRegPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                edRegPasswordCheck.setText("");
                isPasswordCheck = false;

                if (s.toString().length() < 8) {
                    rRegPassword.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    tvRegPassword.setVisibility(View.GONE);
                    isPassword = false;
                } else {
                    rRegPassword.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                    tvRegPassword.setVisibility(View.VISIBLE);
                    isPassword = true;
                }

            }
        });

        edRegPasswordCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.toString().equals(edRegPassword.getText().toString())) {

                    if (s.toString().length() > 7) {
                        rRegPasswordCheck.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                        tvRegPasswordCheck.setVisibility(View.VISIBLE);
                        isPasswordCheck = true;
                    }

                } else {
                    rRegPasswordCheck.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    tvRegPasswordCheck.setVisibility(View.GONE);
                    isPasswordCheck = false;
                }


            }
        });

    }

    private void setVerEditTextListener() {

        edVerPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() < 1) {
                    edVerPhone.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    isPhone = false;
                } else {
                    edVerPhone.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                    isPhone = true;
                }

            }
        });

        edVerCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() < 1) {
                    rVerCode.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    isVerCode = false;
                } else {
                    rVerCode.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                    isVerCode = true;
                }

            }
        });


    }

    private void setGetPasswordListener() {
        edPwdPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 1) {
                    edPwdPhone.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    isPwdPhone = false;
                } else {
                    edPwdPhone.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                    isPwdPhone = true;
                }

            }
        });

        edPwdMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isEmail(s.toString())) {
                    rPwdMail.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                    isPwdMail = true;
                } else {
                    rPwdMail.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    isPwdMail = false;
                }

            }
        });


    }

    private void setClickControl() {

        /*login*/
        tvLoginLogin.setOnClickListener(this);
        tvLoginToRegister.setOnClickListener(this);
        tvLoginFacebook.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        loginGetPasswordImg.setOnClickListener(this);
        tvLoginScanToRegister.setOnClickListener(this);
        tvLoginNorm.setOnClickListener(this);

        /*register*/
        tvRegisterNext.setOnClickListener(this);
        linRegisterBack.setOnClickListener(this);
        tvRegCheckEmail.setOnClickListener(this);

        /*verification code*/
        linVerBack.setOnClickListener(this);
        tvVerCountry.setOnClickListener(this);
        tvVerSendPhone.setOnClickListener(this);
        tvVerFinish.setOnClickListener(this);
        verSelectNewsletterImg.setOnClickListener(this);

        /*get password*/
        linPwdBack.setOnClickListener(this);
        tvPwdCountry.setOnClickListener(this);
        tvPwdSendPhone.setOnClickListener(this);
    }

    public void removeFragmentScanSearch2() {

        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .remove(fragmentScanSearch).commit();

    }

    private void getFacebookProfile() {
        isFacebookRegister = true;
        LoginManager.getInstance()
                .logInWithReadPermissions(mActivity,
                        Arrays.asList("user_birthday", "email", "user_friends"));
    }


    private void login() {

        isFacebookRegister = false;

        if (edLoginMail.getText().toString().equals("")) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_account);
            return;
        }

        if (edLoginPassword.getText().toString().equals("")) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_pwd);
            return;
        }

        doLogin();

    }

    private void selectNewsletter(boolean isSelect) {

        if (isSelect) {

            verSelectNewsletterImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
            isSelectNewsletter = true;

        } else {

            verSelectNewsletterImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
            isSelectNewsletter = false;

        }


    }


    /*--------------------------------------------------------------------------------*/

    private void toLogin() {

        checkViewToGone(vRegister);
        checkViewToGone(vGetPassword);

        checkViewToVisible(vLogin);

    }

    private void toRegister() {

//        getdata.edit().putString(Key.id, "4325").commit();
//        getdata.edit().putString(Key.token, "f7febe74bbeadd602d4799d6e46487a0").commit();
//
//        Intent intent =new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();

        if (!isAlreadInitRegister) {
            isAlreadInitRegister = true;
            setRegEditTextListener();
            MyLog.Set("d", LoginActivity.class, " isAlreadInitRegister = true;");
        }

        checkViewToGone(vLogin);
        checkViewToGone(vVerificationCode);

        checkViewToVisible(vRegister);
    }

    private void toVerificationCode() {

        /*check nickname*/
        if (!isNicknameAlready) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_nickname);
            rRegNickname.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }

        /*check mail*/
        if (edRegMail.getText().toString().equals("")) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_account);
            rRegMail.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }

        if (!isEmail) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_mail_type_is_error);
            rRegMail.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }

        /*check password*/
        if (edRegPassword.getText().toString().equals("")) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_pwd_is_not_fill_in);
            rRegPassword.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }

        if (!isPassword) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_pwd_is_morethan_eight_words);
            rRegPassword.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }

        /*check passwordcheck*/
        if (!isPasswordCheck) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_pwd_twice_enter_is_not_match);
            rRegPasswordCheck.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }

        if (isCheckedEmail) {
            if (!isAlreadInitVerificationCode) {
                isAlreadInitVerificationCode = true;
                setVerEditTextListener();
                MyLog.Set("d", LoginActivity.class, " isAlreadInitVerificationCode = true;");
            }
            checkViewToGone(vRegister);
            checkViewToVisible(vVerificationCode);
        } else {
            isClickNext = true;
            doCheckEmail();
        }


//        setVerEditTextListener();
//        checkViewToGone(vRegister);
//        checkViewToVisible(vVerificationCode);

    }

    private void toGetPassword() {

        checkViewToGone(vLogin);

        checkViewToVisible(vGetPassword);

    }

    private void toNorm() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.url, UrlClass.platform);

        Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);
    }

    public void toScan() {
        fragmentScanSearch = new FragmentScanSearch();

        Bundle bundle = new Bundle();
        bundle.putBoolean(Key.scanLogin, true);
        fragmentScanSearch.setArguments(bundle);

        if (!fragmentScanSearch.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(frame.getId(), fragmentScanSearch).commit();
        }

    }

    /*--------------------------------------------------------------------------------*/

    private void checkViewToGone(final View readyGoneView) {
        if (readyGoneView.getAlpha() > 0) {
            ViewPropertyAnimator alphaTo0 = readyGoneView.animate();
            alphaTo0.setDuration(300)
                    .alpha(0)
                    .start();
        }
    }

    private void checkViewToVisible(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rBackground.removeAllViews();
                rBackground.addView(view);
                ViewPropertyAnimator alphaTo1 = view.animate();
                alphaTo1.setDuration(300)
                        .alpha(1)
                        .start();
            }
        }, 300);
    }

    private boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /*--------------------------------------------------------------------------------*/

    private void getFacebookJsonObjectToDoLogin(JSONObject jsonObject) {
        try {
            String picture = JsonUtility.GetString(jsonObject, ProtocolKey.picture);
            JSONObject object = new JSONObject(picture);
            String data = JsonUtility.GetString(object, ProtocolKey.data);

            JSONObject o = new JSONObject(data);
            String furl = JsonUtility.GetString(o, ProtocolKey.url);


            String fid = JsonUtility.GetString(jsonObject, ProtocolKey.id);

//            String furl = "http://graph.facebook.com/" + fid + "/picture?type=large";


            String fgender = JsonUtility.GetString(jsonObject, ProtocolKey.gender);
            switch (fgender) {
                case "male":
                    fgender = "1";
                    break;
                case "female":
                    fgender = "0";
                    break;
                default:
                    fgender = "2";
                    break;
            }

            String femail = JsonUtility.GetString(jsonObject, ProtocolKey.email);

            String fname = JsonUtility.GetString(jsonObject, ProtocolKey.name);


            String fbirthday = "";
            try {
                String getbirthday = jsonObject.getString(ProtocolKey.birthday);
                String year = getbirthday.substring(6, 10);
                String month = getbirthday.substring(0, 2);
                String day = getbirthday.substring(3, 5);
                fbirthday = year + "-" + month + "-" + day;

            } catch (Exception e) {
                e.printStackTrace();
            }

            MyLog.Set("d", LoginActivity.class, "furl => " + furl);
            MyLog.Set("d", LoginActivity.class, "fid => " + fid);
            MyLog.Set("d", LoginActivity.class, "fgender => " + fgender);
            MyLog.Set("d", LoginActivity.class, "femail => " + femail);
            MyLog.Set("d", LoginActivity.class, "fname => " + fname);
            MyLog.Set("d", LoginActivity.class, "fbirthday => " + fbirthday);


            SharedPreferences.Editor editor = fbData.edit();
            editor.putString("fid", fid);
            editor.putString("fgender", fgender);
            editor.putString("femail", femail);
            editor.putString("fname", fname);
            editor.putString("fbirthday", fbirthday);
            editor.putString("furl", furl);
            editor.commit();


            if (isBusinessRegister) {
                doBusinessRegister();
            } else {
                doFBLogin();
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void protocol05() {
        try {
            /**先下載臉書大頭照*/
            HttpUtility hu = new HttpUtility();
            hu.downPic(fbData.getString("furl", ""),
                    DirClass.pathName_headpicture, //name
                    DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id), //path
                    mActivity.getApplicationContext());


            File file = FileUtility.createHeadFile(mActivity, id);
            String filename = file.getName();

            MyLog.Set("e", this.getClass(), "protocol05() => PPBApplication.getInstance().getId() => " + PPBApplication.getInstance().getId());
            MyLog.Set("e", this.getClass(), " protocol05() => PPBApplication.getInstance().getToken() => " + PPBApplication.getInstance().getToken());

            Map<String, String> signdata = new HashMap<>();
            signdata.put(Key.id, id);
            signdata.put(Key.token, token);
            String head_sign = IndexSheet.encodePPB(signdata);

            Map<String, String> map = new HashMap<>();
            map.put(Key.id, id);
            map.put(Key.token, token);
            map.put(Key.picfile, filename);
            map.put(Key.sign, head_sign);
            String picstrJson = HttpUtility.uploadSubmit(ProtocolsClass.P05_UpdateProfilePic, map, file);
            MyLog.Set("d", mActivity.getClass(), "p05strJson => " + picstrJson);

        } catch (Exception e) {
            MyLog.Set("d", this.getClass(), "---------download picture error----------");

        }
    }

    private void protocol23() {

        MyLog.Set("e", this.getClass(), "protocol23() => PPBApplication.getInstance().getId() => " + PPBApplication.getInstance().getId());
        MyLog.Set("e", this.getClass(), " protocol23() => PPBApplication.getInstance().getToken() => " + PPBApplication.getInstance().getToken());


        try {
            String strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(
                    id,
                    token,
                    "google"), null);
            MyLog.Set("d", mActivity.getClass(), "p23strJson => " + strJson);
            JSONObject jsonObject = new JSONObject(strJson);
            int p23Result = JsonUtility.GetInt(jsonObject, Key.result);
            if (p23Result == 1) {
                String strPoints = JsonUtility.GetString(jsonObject, Key.data);
                getdata.edit().putString(Key.point, strPoints).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void protocol28() {

        MyLog.Set("e", this.getClass(), "protocol28() => PPBApplication.getInstance().getId() =>" + PPBApplication.getInstance().getId());
        MyLog.Set("e", this.getClass(), "protocol28() => PPBApplication.getInstance().getToken() =>" + PPBApplication.getInstance().getToken());

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        try {
            Map<String, String> data = new HashMap<String, String>();
            data.put(Key.id, id);
            data.put(Key.token, token);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, id);
            sendData.put(Key.token, token);
            sendData.put(Key.sign, sign);
            String strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P28_Getprofile, sendData, null);

            if (BuildConfig.DEBUG) {
                Logger.json(strJson);
            }

            JSONObject jsonObject = new JSONObject(strJson);
            p28Result = JsonUtility.GetInt(jsonObject, Key.result);
            if (p28Result == 1) {
                String strData = JsonUtility.GetString(jsonObject, Key.data);
                JSONObject object = new JSONObject(strData);

                String json_birthday = JsonUtility.GetString(object, Key.birthday);

                String json_gender = JsonUtility.GetString(object, Key.gender);

                String json_nickname = JsonUtility.GetString(object, Key.nickname);

                String json_profilepic = JsonUtility.GetString(object, Key.profilepic);

                String json_selfdescription = JsonUtility.GetString(object, Key.selfdescription);

                String json_cellphone = JsonUtility.GetString(object, Key.cellphone);

                String json_email = JsonUtility.GetString(object, Key.email);

                String json_usergrade = JsonUtility.GetString(object, Key.usergrade);

                String hobby = JsonUtility.GetString(object, Key.hobby);

                JSONArray jsonArray = new JSONArray(hobby);
                hobbys = jsonArray.length();

                boolean bCreative = JsonUtility.GetBoolean(object, Key.creative);

                int follow = JsonUtility.GetInt(object, Key.follow);
                int viewed = JsonUtility.GetInt(object, Key.viewed);

                String creative_name = JsonUtility.GetString(object, ProtocolKey.creative_name);

                String strCover = JsonUtility.GetString(object, ProtocolKey.cover);

                boolean isNewsletter = JsonUtility.GetBoolean(object, ProtocolKey.newsletter);


                SharedPreferences.Editor editor = getdata.edit();
                editor.putString(Key.id, id);
                editor.putString(Key.token, token);
                editor.putString(Key.gender, json_gender);
                editor.putString(Key.birthday, json_birthday);
                String year = json_birthday.substring(0, json_birthday.indexOf("-"));

                String month = json_birthday.substring(json_birthday.indexOf("-") + 1
                        , json_birthday.lastIndexOf("-"));
                String day = json_birthday.substring(json_birthday.lastIndexOf("-") + 1);

                editor.putString("year", year);
                editor.putString("month", month);
                editor.putString("day", day);

                editor.putString(Key.selfdescription, json_selfdescription);

                editor.putString(Key.nickname, json_nickname);
                editor.putString(Key.cellphone, json_cellphone);
                editor.putString(Key.profilepic, json_profilepic);
                editor.putString(Key.email, json_email);
                editor.putString(Key.usergrade, json_usergrade);
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

                getdata.edit().remove("sms_exist");


                editor.commit();

                //20171103
                HobbyManager.SaveHobbyList(hobby);

            } else if (p28Result == 0) {
                p28Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void selectCountry() {


        if (popPicker == null) {
            String[] countries = getResources().getStringArray(R.array.phone_code_list);
            List<String> listCountry = new ArrayList<String>();
            for (int i = 0; i < countries.length; i++) {
                listCountry.add(countries[i]);
            }
            popPicker = new PopPicker(mActivity);
            popPicker.setPopup();
            popPicker.setTitle(R.string.pinpinbox_2_0_0_pop_title_choose_country_number);
            popPicker.setPickerData(listCountry);
            popPicker.getPickerView().setSelected(2);

            popPicker.getTvConfirm().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isSelectCountryByVer) {
                        tvVerCountry.setText(popPicker.getStrSelect());
                    } else {
                        tvPwdCountry.setText(popPicker.getStrSelect());
                    }
                    popPicker.dismiss();

                }
            });
        }
        popPicker.show(rBackground);
    }

    private void checkTokenToMain() {

        boolean tokenUpDated = PPBApplication.getInstance().getData().getBoolean(Key.tokenUpDated, false);

        if (tokenUpDated) {
            checkToHobbyOrMain();
        } else {


            protocol95 = new Protocol95_RefreshToken(mActivity, id, new Protocol95_RefreshToken.TaskCallBack() {
                @Override
                public void Prepare() {
                    startLoading();
                }

                @Override
                public void Post() {
                    dissmissLoading();
                }

                @Override
                public void Success() {
                    checkToHobbyOrMain();
                }

                @Override
                public void TimeOut() {

                    checkTokenToMain();

                }
            });


        }


    }

    private void checkToHobbyOrMain() {

        if (isToHobby) {

            Bundle bundle = new Bundle();
            bundle.putBoolean("hideActionBar", true);

            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, HobbyActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);
            finish();
        } else {


//            if(!SystemUtility.Above_Equal_V5() || isFacebookRegister){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);
            finish();
//            }else {
//
//
//                Intent intent = new Intent(mActivity, MainActivity.class);
//
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(mActivity,
//                                tvLoginLogin,
//                                ViewCompat.getTransitionName(tvLoginLogin));
//
//                startActivity(intent, options.toBundle());
//
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        finishAfterTransition();
//                    }
//                },2000);
//
//
//            }

        }


    }

    private void checkCameraPermission() {

        switch (checkPermission(mActivity, Manifest.permission.CAMERA)) {
            case SUCCESS:
                toScan();
                break;
            case REFUSE:
                MPermissions.requestPermissions(LoginActivity.this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
                break;
        }

    }

    private void checkSMSPermission(){

        switch (checkPermission(mActivity, Manifest.permission.RECEIVE_SMS)) {
            case SUCCESS:
                doRequestSms();
                break;
            case REFUSE:
                MPermissions.requestPermissions(LoginActivity.this, REQUEST_CODE_SMS, Manifest.permission.RECEIVE_SMS);
                break;
        }

    }


    /*--------------------------------------------------------------------------------*/

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {

                    case DoingTypeClass.DoLogin:
                        doLogin();
                        break;

                    case DoingTypeClass.DoFBLogin:
                        doFBLogin();
                        break;

                    case DoingTypeClass.DoNoAccountToRegister:
                        doNoAccountToRegister();
                        break;

                    case DoingTypeClass.DoCheckEmail:
                        doCheckEmail();
                        break;

                    case DoingTypeClass.DoRequestSms:
                        doRequestSms();
                        break;

                    case DoingTypeClass.DoGetPassword:
                        doGetPassword();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doLogin() {
        loginTask = new LoginTask();
        loginTask.execute();
    }

    private void doFBLogin() {

        fbLoginTask = new FBLoginTask();
        fbLoginTask.execute();

    }

    private void doNoAccountToRegister() {
        noAccountToRegisterTask = new NoAccountToRegisterTask();
        noAccountToRegisterTask.execute();
    }

    private void doCheckEmail() {
        checkEmailTask = new CheckEmailTask();
        checkEmailTask.execute();
    }

    private void doRequestSms() {

        requestSmsTask = new RequestSmsTask();
        requestSmsTask.execute();
    }

    private void doGetPassword() {
        getPasswordTask = new GetPasswordTask();
        getPasswordTask.execute();
    }

    public void doBusiness(String businessuser_id) {

        this.businessuser_id = businessuser_id;

        if (fragmentScanSearch != null && fragmentScanSearch.isAdded()) {
            removeFragmentScanSearch2();
        }

        isBusinessRegister = true;

        getFacebookProfile();

    }

    private void doBusinessRegister() {


        MyLog.Set("d", this.getClass(), "businessuser_id => " + businessuser_id + "\n" + "準備執行經濟帳號註冊");


        /*param*/
        String param = "";
        try {

            String gender = fbData.getString(Key.gender, "none");

            if (gender.equals("0")) {
                gender = "female";
            } else if (gender.equals("1")) {
                gender = "male";
            } else {
                gender = "none";
            }

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(Key.account, fbData.getString("femail", ""));
            jsonObject.put(Key.birthday, fbData.getString("fbirthday", ""));
            jsonObject.put(Key.name, fbData.getString("fname", ""));
            jsonObject.put(Key.gender, gender);

            param = jsonObject.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*timestamp*/
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(new Date(System.currentTimeMillis()));

        MyLog.Set("d", this.getClass(), "time => " + time);

        long timestamp = 0;

        try {
            Date date = df.parse(time);

            timestamp = date.getTime() / 1000L;

            MyLog.Set("d", this.getClass(), "timestamp => " + timestamp);

        } catch (Exception e) {
            e.printStackTrace();
        }


        protocol98 = new Protocol98_BusinessSubUserFastRegister(
                mActivity,
                businessuser_id,
                fbData.getString("fid", ""),
                timestamp + "",
                param,
                new Protocol98_BusinessSubUserFastRegister.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        startLoading();
                    }

                    @Override
                    public void DoInBackground() {

                        protocol28();

                        protocol23();

                        if (protocol98.getResult().equals(ResultType.SYSTEM_OK)) {
                            //註冊成功才會上傳頭像
                            protocol05();
                        }


                    }


                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success() {


                        PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_register_success);

                        CreateDir.create(mActivity, id);


                        getdata.edit().putBoolean(Key.checkNewsletter, true).commit();


                        Intent intent = new Intent();
                        intent.setClass(LoginActivity.this, TypeFacebookFriendActivity.class);
                        startActivity(intent);
                        finish();
                        ActivityAnim.StartAnim(mActivity);


                    }

                    @Override
                    public void UserExists() {

                        getdata.edit().putBoolean(Key.is_FB_Login, true).commit();

                        if (p28Result == 1) {

                            CreateDir.create(mActivity, id);

                            File file = new File(DirClass.pathHeaderPicture);
                            if (file.exists()) {
                                file.delete();
                            }

                            if (hobbys > 0) {
                                isToHobby = false;
                            } else {
                                isToHobby = true;
                                MyLog.Set("d", mActivity.getClass(), "興趣尚未選擇 即將跳轉至興趣畫面");
                            }
                            checkTokenToMain();

                        } else if (p28Result == 0) {
                            DialogV2Custom.BuildError(mActivity, p28Message);
                        } else {
                            DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
                        }


                    }

                    @Override
                    public void TimeOut() {
                        doBusinessRegister();
                    }
                });

    }

    private class LoginTask extends AsyncTask<Void, Void, Object> {

        private int p01Result = -1;
        private String p01Message = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoLogin;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(
                        true,
                        ProtocolsClass.P01_Login,
                        SetMapByProtocol.setParam01_login(edLoginMail.getText().toString(),
                                edLoginPassword.getText().toString()), null);
                if (BuildConfig.DEBUG) {
                    Logger.json(strJson);
                }

            } catch (SocketTimeoutException timeout) {
                p01Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);
//                    p01Result = jsonObject.getString(Key.result);
                    p01Result = JsonUtility.GetInt(jsonObject, Key.result);

                    if (p01Result == 1) {

                        String strData = JsonUtility.GetString(jsonObject, Key.data);

                        JSONObject object = new JSONObject(strData);
                        id = JsonUtility.GetString(object, Key.id);
                        token = JsonUtility.GetString(object, Key.token);
                        String strBirthday = JsonUtility.GetString(object, ProtocolKey.birthday);
                        String strGender = JsonUtility.GetString(object, ProtocolKey.gender);
                        String strNickname = JsonUtility.GetString(object, ProtocolKey.nickname);
                        String strProfilepic = JsonUtility.GetString(object, ProtocolKey.profilepic);
                        String strSelfdescription = JsonUtility.GetString(object, ProtocolKey.selfdescription);
                        String strUsergrade = JsonUtility.GetString(object, ProtocolKey.usergrade);
                        if (strUsergrade.equals("")) {
                            strUsergrade = "free";
                        }

                        String strCellphone = "";
                        String phoneCountry = "";
                        String phoneNumber = "";
                        try {
                            if (object.getString("cellphone") != null | !object.getString("cellphone").equals("")) {
                                strCellphone = object.getString(Key.cellphone);

                                phoneCountry = strCellphone.substring(0, strCellphone.indexOf(","));

                                phoneNumber = strCellphone.substring(strCellphone.lastIndexOf(",") + 1);

                            } else {
                                strCellphone = "";
                                phoneCountry = "";
                                phoneNumber = "";
                            }
                        } catch (Exception e) {
                            strCellphone = "";
                            phoneCountry = "";
                            phoneNumber = "";
                        }

                        int intFollow = 0;
                        int intViewed = 0;


                        intFollow = JsonUtility.GetInt(object, ProtocolKey.follow);
                        intViewed = JsonUtility.GetInt(object, ProtocolKey.viewed);

                        boolean bCreative = JsonUtility.GetBoolean(object, ProtocolKey.creative);

                        String creative_name = JsonUtility.GetString(object, ProtocolKey.creative_name);

                        String strCover = JsonUtility.GetString(object, ProtocolKey.cover);


                        SharedPreferences.Editor editor = getdata.edit();

                        editor.putString(Key.id, id);
                        editor.putString(Key.token, token);
                        editor.putString(Key.email, edLoginMail.getText().toString());
                        editor.putString(Key.account, edLoginMail.getText().toString());
                        editor.putString(Key.gender, strGender);
                        editor.putString(Key.birthday, strBirthday);
                        editor.putString(Key.selfdescription, strSelfdescription);
                        editor.putString(Key.nickname, strNickname);
                        editor.putString(Key.usergrade, strUsergrade);
                        editor.putString(Key.profilepic, strProfilepic);
                        editor.putString(Key.creative_name, creative_name);
                        editor.putString(Key.cover, strCover);

                        String year = strBirthday.substring(0, strBirthday.indexOf("-"));
                        String month = strBirthday.substring(strBirthday.indexOf("-") + 1, strBirthday.lastIndexOf("-"));
                        String day = strBirthday.substring(strBirthday.lastIndexOf("-") + 1);
                        editor.putString("year", year);
                        editor.putString("month", month);
                        editor.putString("day", day);

                        editor.putString(Key.cellphone, strCellphone);
                        editor.putString("countrynumber", phoneCountry);
                        editor.putString("nocountry_phonenumber", phoneNumber);

                        editor.putInt(Key.follow, intFollow);
                        editor.putInt(Key.viewed, intViewed);

                        editor.putBoolean(Key.is_FB_Login, false);
                        editor.putBoolean(Key.creative, bCreative);


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

                        /*獲取P點*/
                        protocol23();


                    } else if (p01Result == 0) {
                        p01Message = JsonUtility.GetString(jsonObject, Key.message);
                    } else {
                        p01Result = -1;
                    }

                } catch (Exception e) {
                    p01Result = -1;
                }

            }

            return null;
        }


        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p01Result == 1) {

                CreateDir.create(mActivity, id);

                isToHobby = false;

                checkTokenToMain();


            } else if (p01Result == 0) {
                DialogV2Custom.BuildError(mActivity, p01Message);

            } else if (p01Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }

    }

    private class FBLoginTask extends AsyncTask<Void, Void, Object> {

        private int p35Result = -1;
        private String p35Message = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFBLogin;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true,
                        ProtocolsClass.P35_FacebookLogin,
                        SetMapByProtocol.setParam35_facebooklogin(fbData.getString("fid", "")),
                        null);
                MyLog.Set("d", mActivity.getClass(), "p35strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p35Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p35Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p35Result == 1) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONObject object = new JSONObject(jdata);
                        id = JsonUtility.GetString(object, Key.id);
                        token = JsonUtility.GetString(object, Key.token);

                        SharedPreferences.Editor editor = getdata.edit();
                        editor.putString(Key.token, token);
                        editor.putString(Key.id, id);
                        editor.commit();

                        protocol28();
                        protocol23();

                    } else if (p35Result == 0) {
                        p35Message = JsonUtility.GetString(jsonObject, Key.message);
                    } else if (p35Result == 2) {

                    } else {
                        p35Result = -1;
                    }

                } catch (Exception e) {
                    p35Result = -1;
                    e.printStackTrace();//p35出錯
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p35Result == 1) {

                getdata.edit().putBoolean(Key.is_FB_Login, true).commit();

                if (p28Result == 1) {

                    CreateDir.create(mActivity, id);

                    if (hobbys > 0) {

                        isToHobby = false;

                    } else {

                        isToHobby = true;

                        MyLog.Set("d", mActivity.getClass(), "興趣尚未選擇 即將跳轉至興趣畫面");

                    }
                    checkTokenToMain();

                } else if (p28Result == 0) {
                    DialogV2Custom.BuildError(mActivity, p28Message);
                } else {
                    DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
                }

            } else if (p35Result == 2) {
                //停權或用戶不存在
                doNoAccountToRegister();

            } else if (p35Result == 0) {
                DialogV2Custom.BuildError(mActivity, p35Message);
            } else if (p35Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }

    }

    private class NoAccountToRegisterTask extends AsyncTask<Void, Void, Object> {

        private int p04Result = -1;
        private String p04Message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoNoAccountToRegister;
            startLoading();

            PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_registered);

//            getLngAndLat(mActivity);


        }

        @Override
        protected Void doInBackground(Void... params) {

            String strName = "";
            String strEmail = "";
            String strPassword = "";
            String strPhone = "";
            String strSms = "";
            String strWay = "";
            String strWayId = "";
            String strGender = "";
            String strBirthday = "";


            if (isFacebookRegister) {
                strName = fbData.getString("fname", "");
                strEmail = fbData.getString("femail", "");
                strPassword = "";
                strPhone = "";
                strSms = "";
                strWay = "facebook";
                strWayId = fbData.getString("fid", "");

                if (fbData.getString("fgender", "").equals("")) {
                    strGender = "none";
                } else {
                    strGender = fbData.getString("fgender", "");
                }

                if (fbData.getString("fbirthday", "").equals("")) {
                    strBirthday = "1900-01-01";
                } else {
                    strBirthday = fbData.getString("fbirthday", "");
                }
            } else {

                String strCountryNumber = tvVerCountry.getText().toString().substring(tvVerCountry.getText().toString().indexOf("+") + 1);
                String strCompletePhone = strCountryNumber + "," + edVerPhone.getText().toString();


                strName = edRegNickname.getText().toString();
                strEmail = edRegMail.getText().toString();
                strPassword = edRegPassword.getText().toString();
                strPhone = strCompletePhone;
                strSms = edVerCode.getText().toString();
                strWay = "none";
                strWayId = "null";
                strGender = "none";
                strBirthday = "1900-01-01";
            }


            Map<String, String> data = new HashMap<String, String>();
            data.put(MapKey.name, strName);
            data.put(MapKey.account, strEmail);
            data.put(MapKey.password, strPassword);
            data.put(MapKey.cellphone, strPhone);
            data.put(MapKey.smspassword, strSms);
            data.put(MapKey.way, strWay);
            data.put(MapKey.way_id, strWayId);
            String sign = IndexSheet.encodePPB(data);

            Map<String, String> senddata = new HashMap<String, String>();
            senddata.put(MapKey.name, strName);
            senddata.put(MapKey.account, strEmail);
            senddata.put(MapKey.password, strPassword);
            senddata.put(MapKey.cellphone, strPhone);
            senddata.put(MapKey.smspassword, strSms);
            senddata.put(MapKey.way, strWay);
            senddata.put(MapKey.way_id, strWayId);

            senddata.put(MapKey.gender, strGender);
            senddata.put(MapKey.birthday, strBirthday);

            if (isSelectNewsletter) {
                senddata.put(MapKey.newsletter, "1");
            } else {
                senddata.put(MapKey.newsletter, "0");
            }


//            senddata.put(MapKey.coordinate, latitude + "," + longitude);
            senddata.put(MapKey.sign, sign);

            String strJson = "";

            MyLog.Set("e", mActivity.getClass(), "isSelectNewsletter = > " + isSelectNewsletter);

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P04_Registration, senddata, null);
                MyLog.Set("d", mActivity.getClass(), "p04strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p04Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p04Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p04Result == 1) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONObject object = new JSONObject(jdata);
                        id = object.getString(Key.id);
                        token = object.getString(Key.token);


                        String year = strBirthday.substring(0, 4);
                        String month = strBirthday.substring(5, 7);
                        String day = strBirthday.substring(8, 10);
                        SharedPreferences.Editor editor = getdata.edit();
                        editor.putString(Key.id, id);
                        editor.putString(Key.token, token);
                        editor.putString(Key.nickname, strName);
                        editor.putString(Key.email, strEmail);
                        editor.putString(Key.account, strEmail);
                        editor.putString(Key.gender, strGender);
                        editor.putString(Key.birthday, strBirthday);
                        editor.putString(Key.selfdescription, "");
                        editor.putString("year", year);
                        editor.putString("month", month);
                        editor.putString("day", day);

                        editor.putInt(Key.follow, 0);
                        editor.putInt(Key.viewed, 0);

                        editor.putBoolean(Key.creative, false); //無帳號預設 false

                        if (isFacebookRegister) {
                            editor.putString("countrynumber", "");
                            editor.putString("nocountry_phonenumber", "");
                            editor.putString(Key.cellphone, "");
                            editor.putBoolean(Key.is_FB_Login, true);
                        } else {

                            String strCountryNumber = tvVerCountry.getText().toString().substring(tvVerCountry.getText().toString().indexOf("+") + 1);
                            String strCompletePhone = strCountryNumber + "," + edVerPhone.getText().toString();

                            editor.putString("countrynumber", tvVerCountry.getText().toString().substring(tvVerCountry.getText().toString().indexOf("+") + 1));
                            editor.putString("nocountry_phonenumber", edVerPhone.getText().toString());
                            editor.putString(Key.cellphone, strCompletePhone);
                            editor.putBoolean(Key.is_FB_Login, false);
                        }

                        editor.putString(Key.creative_name, "");

                        editor.putBoolean(Key.newsletter, isSelectNewsletter);


                        editor.commit();


                    } else if (p04Result == 0) {
                        p04Message = JsonUtility.GetString(jsonObject, Key.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (isFacebookRegister) {

                protocol05();

            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (isFacebookRegister) {
                File file = new File(DirClass.pathHeaderPicture);
                if (file.exists()) {
                    file.delete();
                }
            }

            if (p04Result == 1) {

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_register_success);

                CreateDir.create(mActivity, id);

                if (isFacebookRegister) {
                    getdata.edit().putBoolean(Key.checkNewsletter, true).commit();
                }

                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, TypeFacebookFriendActivity.class);
                startActivity(intent);
                finish();
                ActivityAnim.StartAnim(mActivity);


            } else if (p04Result == 0) {
                DialogV2Custom.BuildError(mActivity, p04Message);
            } else if (p04Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    private class CheckEmailTask extends AsyncTask<Void, Void, Object> {

        private int p52Result = -1;
        private String p52Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoCheckEmail;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true,
                        ProtocolsClass.P52_Check,
                        SetMapByProtocol.setParam52_check("account", edRegMail.getText().toString()), null);
                MyLog.Set("d", getClass(), "p52strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p52Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p52Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p52Result == 0) {
                        p52Message = JsonUtility.GetString(jsonObject, Key.message);
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

            if (p52Result == 1) {

                tvRegCheckEmail.setBackgroundResource(0);
                tvRegCheckEmail.setClickable(false);
                tvRegCheckEmail.setText("OK");
                tvRegCheckEmail.setTextColor(Color.parseColor(ColorClass.MAIN_FIRST));
                isCheckedEmail = true;

                if (isClickNext) {
                    if (!isAlreadInitVerificationCode) {
                        isAlreadInitVerificationCode = true;
                        setVerEditTextListener();
                        MyLog.Set("d", LoginActivity.class, " isAlreadInitVerificationCode = true;");
                    }
                    checkViewToGone(vRegister);
                    checkViewToVisible(vVerificationCode);
                }


            } else if (p52Result == 0) {

                DialogV2Custom.BuildError(mActivity, p52Message);

            } else if (p52Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }

    }

    private class RequestSmsTask extends AsyncTask<Void, Void, Object> {

        private int p03Result = -1;
        private String p03Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoRequestSms;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";

            String strCountryNumber = tvVerCountry.getText().toString().substring(tvVerCountry.getText().toString().indexOf("+") + 1);
            String strCompletePhone = strCountryNumber + "," + edVerPhone.getText().toString();


            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P03_RequestSmsPassword,
                        SetMapByProtocol.setParam03_requestsmspwd(edRegMail.getText().toString(),
                                strCompletePhone, "register"), null);
                MyLog.Set("d", getClass(), "p03strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p03Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p03Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p03Result == 0) {
                        p03Message = JsonUtility.GetString(jsonObject, Key.message);
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

            if (p03Result == 1) {

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_ver_code_is_already_send);


                cTimer = new CountDownTimer(60000, 1000) {
                    public void onTick(long millisUntilFinished) {


                        MyLog.Set("d", mActivity.getClass(), "剩餘 " + millisUntilFinished / 1000 + " 秒");

                        tvVerTime.setVisibility(View.VISIBLE);
                        tvVerSendPhone.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius_big);
                        tvVerSendPhone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

                        String strTime = (millisUntilFinished / 1000) + "";


                        tvVerTime.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_time_left0) + strTime + getResources().getString(R.string.pinpinbox_2_0_0_other_text_time_left1));

                        isCanSendAgain = false;
                    }

                    public void onFinish() {
                        tvVerTime.setVisibility(View.INVISIBLE);
                        tvVerSendPhone.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);
                        tvVerSendPhone.setTextColor(Color.parseColor(ColorClass.WHITE));
                        isCanSendAgain = true;
                    }
                }.start();

            } else if (p03Result == 0) {

                DialogV2Custom.BuildError(mActivity, p03Message);

            } else if (p03Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }

    }

    private class GetPasswordTask extends AsyncTask<Void, Void, Object> {

        private int p07Result = -1;
        private String p07Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetPassword;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strCountryNumber = tvPwdCountry.getText().toString().substring(tvPwdCountry.getText().toString().indexOf("+") + 1);
            String strCompletePhone = strCountryNumber + "," + edPwdPhone.getText().toString();

            Map<String, String> data = new HashMap<String, String>();
            data.put(MapKey.account, edPwdMail.getText().toString());
            data.put(MapKey.cellphone, strCompletePhone);
            String sign = IndexSheet.encodePPB(data);

            HashMap<String, String> sendData = new HashMap<String, String>();
            sendData.put(MapKey.account, edPwdMail.getText().toString());
            sendData.put(MapKey.cellphone, strCompletePhone);
            sendData.put(MapKey.sign, sign);

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P07_RetrievePassword, sendData, null);//07
                MyLog.Set("d", getClass(), "p07strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p07Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p07Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p07Result == 0) {
                        p07Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            if (p07Result == 1) {

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_pwd_is_already_send);

                edLoginMail.setText(edPwdMail.getText().toString());

            } else if (p07Result == 0) {

                DialogV2Custom.BuildError(mActivity, p07Message);

            } else if (p07Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }

    }

    /*--------------------------------------------------------------------------------*/

    @Override
    public void onClick(View v) {
        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }

        if (!HttpUtility.isConnect(mActivity)) {
            noConnect = new NoConnect(mActivity);
            return;
        }

        switch (v.getId()) {

            /*login*/
            case R.id.tvLoginLogin:

                login();

                break;

            case R.id.tvLoginToRegister:
                toRegister();
                break;

            case R.id.tvLoginFacebook:

                isBusinessRegister = false;

                getFacebookProfile();


                break;

            case R.id.tvAboutUs:

                Intent intent = new Intent(LoginActivity.this, GuidePageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);


                break;

            case R.id.loginGetPasswordImg:
                toGetPassword();
                break;

            case R.id.tvLoginScanToRegister:

                checkCameraPermission();


                break;

            case R.id.tvLoginNorm:
                toNorm();
                break;


            /*register*/
            case R.id.tvRegisterNext:
                toVerificationCode();
                break;


            case R.id.linRegisterBack:
                toLogin();
                break;

            case R.id.tvRegCheckEmail:
                if (edRegMail.getText().toString().equals("")) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_account);
                    return;
                }

                if (!isEmail(edRegMail.getText().toString())) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_mail_type_is_error);
                    return;
                }

                isClickNext = false;
                doCheckEmail();
                break;


            /*verification code*/
            case R.id.linVerBack:
                toRegister();
                break;

            case R.id.tvVerCountry:
                isSelectCountryByVer = true;
                selectCountry();

                break;
            case R.id.tvVerSendPhone:

                if (!isPhone) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_cellphone_number);
                    return;
                }

//                if (isCanSendAgain) {
//                    checkSMSPermission();
//                }

                if (isCanSendAgain) {
                    doRequestSms();
                }

                break;
            case R.id.tvVerFinish:
                if (!isVerCode) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_ver_code);
                    return;
                }
                isFacebookRegister = false;
                doNoAccountToRegister();
                break;

            case R.id.verSelectNewsletterImg:

                if (isSelectNewsletter) {
                    selectNewsletter(false);
                } else {
                    selectNewsletter(true);
                }

                break;

            /*get password*/
            case R.id.linPwdBack:
                toLogin();
                break;

            case R.id.tvPwdCountry:
                isSelectCountryByVer = false;
                selectCountry();

                break;

            case R.id.tvPwdSendPhone:

                if (!isPwdPhone) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_cellphone_number);
                    edPwdPhone.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    return;
                }
                if (edPwdMail.getText().toString().equals("")) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_account);
                    rPwdMail.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    return;
                }

                if (!isPwdMail) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_mail_type_is_error);
                    return;
                }


                doGetPassword();


                break;


        }
    }


    //    double latitude = 0.0;
//    double longitude = 0.0;
//    private void getLngAndLat(Activity context) {
//
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//            } else {//当GPS信号弱没获取到位置的时候又从网络获取
//                getLngAndLatWithNetwork();
////                return getLngAndLatWithNetwork();
//            }
//        } else {    //从网络获取经纬度
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//            }
//        }
////        return longitude + "," + latitude;
//    }
//
//    public void getLngAndLatWithNetwork() {
////        double latitude = 0.0;
////        double longitude = 0.0;
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if (location != null) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//        }
////        return longitude + "," + latitude;
//    }
//
//    LocationListener locationListener = new LocationListener() {
//
//        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        // Provider被enable时触发此函数，比如GPS被打开
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        // Provider被disable时触发此函数，比如GPS被关闭
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//        @Override
//        public void onLocationChanged(Location location) {
//        }
//    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final int SUCCESS = 0;
    private final int REFUSE = -1;

    private int checkPermission(Activity ac, String permission) {

        int doingType = 0;

        if (ActivityCompat.checkSelfPermission(ac, permission) == PackageManager.PERMISSION_GRANTED) {
            //已授權
            doingType = SUCCESS;
        } else {
            //未授權 判斷是否彈出詢問框 true => 彈出
//            if(ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)){
            doingType = REFUSE;

//            }else {
//                doingType = REFUSE_NO_ASK;
//            }
        }
        return doingType;

    }

    @PermissionGrant(REQUEST_CODE_CAMERA)
    public void requestCameraSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toScan();
            }
        }, 500);


    }

    @PermissionDenied(REQUEST_CODE_CAMERA)
    public void requestCameraFailed() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");

            DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_camera);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    SystemUtility.getAppDetailSettingIntent(mActivity);
                }
            });
            d.show();

        } else {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");

        }
    }


//    @PermissionGrant(REQUEST_CODE_SMS)
//    public void requestSMSSuccess() {
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doRequestSms();
//            }
//        }, 500);
//
//    }
//
//    @PermissionDenied(REQUEST_CODE_SMS)
//    public void requestSMSFailed() {
//
//        if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.RECEIVE_SMS)) {
//
//            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");
//
//            DialogV2Custom d = new DialogV2Custom(mActivity);
//            d.setStyle(DialogStyleClass.CHECK);
//            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
//            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_sms);
//            d.setCheckExecute(new CheckExecute() {
//                @Override
//                public void DoCheck() {
//                    SystemUtility.getAppDetailSettingIntent(mActivity);
//                }
//            });
//            d.show();
//
//        } else {
//            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");
//
//        }
//    }








    @Override
    public void onBackPressed() {

        if (fragmentScanSearch != null && fragmentScanSearch.isAdded()) {
            removeFragmentScanSearch2();
            return;
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mActivity != null) {
                    Process.killProcess(Process.myPid());
                }
            }
        }, 200);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {
            if (noConnect == null) {
                noConnect = new NoConnect(this);
            }
        }
    }

    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (cTimer != null) {
            cTimer.cancel();
        }


        if (loginTask != null && !loginTask.isCancelled()) {
            loginTask.cancel(true);
        }
        loginTask = null;

        if (fbLoginTask != null && !fbLoginTask.isCancelled()) {
            fbLoginTask.cancel(true);
        }
        fbLoginTask = null;

        if (noAccountToRegisterTask != null && !noAccountToRegisterTask.isCancelled()) {
            noAccountToRegisterTask.cancel(true);
        }
        noAccountToRegisterTask = null;

        if (checkEmailTask != null && !checkEmailTask.isCancelled()) {
            checkEmailTask.cancel(true);
        }
        checkEmailTask = null;

        if (requestSmsTask != null && !requestSmsTask.isCancelled()) {
            requestSmsTask.cancel(true);
        }
        requestSmsTask = null;

        if (getPasswordTask != null && !getPasswordTask.isCancelled()) {
            getPasswordTask.cancel(true);
        }
        getPasswordTask = null;

        if (protocol95 != null && !protocol95.isCancelled()) {
            protocol95.cancel(true);
        }
        protocol95 = null;

        if (protocol98 != null && !protocol98.isCancelled()) {
            protocol98.cancel(true);
        }
        protocol98 = null;


//        Recycle.VIEW(vLogin.findViewById(R.id.r1));
//        Recycle.VIEW(vLogin.findViewById(R.id.r2));
//        Recycle.VIEW(vLogin.findViewById(R.id.r3));

        Recycle.IMG((ImageView) vLogin.findViewById(R.id.bgImg));
        Recycle.IMG((ImageView) vLogin.findViewById(R.id.img2));
        Recycle.IMG(loginGetPasswordImg);


        Recycle.IMG((ImageView) vRegister.findViewById(R.id.img2));
        Recycle.IMG((ImageView) vRegister.findViewById(R.id.img3));
        Recycle.IMG((ImageView) vRegister.findViewById(R.id.img4));
        Recycle.IMG((ImageView) vRegister.findViewById(R.id.img5));
        Recycle.IMG((ImageView) vRegister.findViewById(R.id.backImg));


        Recycle.IMG((ImageView) vVerificationCode.findViewById(R.id.img2));
        Recycle.IMG((ImageView) vVerificationCode.findViewById(R.id.backImg));


        Recycle.IMG((ImageView) vGetPassword.findViewById(R.id.img2));
        Recycle.IMG((ImageView) vGetPassword.findViewById(R.id.backImg));

        System.gc();

        super.onDestroy();


    }

}

package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.DialogTool.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.SelfMadeClass.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.StringClass.DoingTypeClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Widget.ActivityAnim;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.PinPinToast;
import com.pinpinbox.android.Widget.Recycle;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/3/2
 */
public class ChangePassword2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private UpDatePasswordTask upDatePasswordTask;

    private EditText edPwdCurrent, edPwdChangeTo, edPwdAgain;
    private TextView tvConfirm;
    private ImageView backImg;

    private int doingType;

    private boolean isPasswordCheck = false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_changepassword);
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        SystemUtility.SysApplication.getInstance().addActivity(this);


        init();

        setEditTextListener();

    }

    private void init() {

        mActivity = this;

        edPwdCurrent = (EditText) findViewById(R.id.edPwdCurrent);
        edPwdChangeTo = (EditText) findViewById(R.id.edPwdChangeTo);
        edPwdAgain = (EditText) findViewById(R.id.edPwdAgain);
        tvConfirm = (TextView) findViewById(R.id.tvConfirm);
        backImg = (ImageView) findViewById(R.id.backImg);

        TextUtility.setBold((TextView) findViewById(R.id.tv1), true);
        TextUtility.setBold((TextView) findViewById(R.id.tv2), true);
        TextUtility.setBold((TextView) findViewById(R.id.tv3), true);
        TextUtility.setBold((TextView) findViewById(R.id.tvTitle), true);
        TextUtility.setBold(tvConfirm, true);


        tvConfirm.setOnClickListener(this);
        backImg.setOnClickListener(this);


    }

    private void setEditTextListener() {

        edPwdCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equals("")) {
                    edPwdCurrent.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                } else {
                    edPwdCurrent.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                }

            }
        });


        final TextView tvPwdChangeToCheck = (TextView) findViewById(R.id.tvPwdChangeToCheck);
        final TextView tvPwdAgainCheck = (TextView) findViewById(R.id.tvPwdAgainCheck);

        edPwdChangeTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                edPwdAgain.setText("");
                isPasswordCheck = false;

                if (s.toString().length() < 8) {
                    edPwdChangeTo.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    tvPwdChangeToCheck.setVisibility(View.GONE);

                } else {
                    edPwdChangeTo.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                    tvPwdChangeToCheck.setVisibility(View.VISIBLE);

                }

            }
        });

        edPwdAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.toString().equals(edPwdChangeTo.getText().toString())) {

                    if (s.toString().length() > 7) {
                        edPwdAgain.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                        tvPwdAgainCheck.setVisibility(View.VISIBLE);
                        isPasswordCheck = true;
                    }

                } else {
                    edPwdAgain.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                    tvPwdAgainCheck.setVisibility(View.GONE);
                    isPasswordCheck = false;
                }


            }
        });


    }

    private void confirm() {



        /*帳號當前密碼空的*/
        if (edPwdCurrent.getText().toString().equals("")) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_pwd);
            edPwdCurrent.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }

        /*要變更為的密碼為空的*/
        if (edPwdChangeTo.getText().toString().equals("")) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_pwd_is_not_fill_in);
            edPwdChangeTo.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }

        /*密碼至少8字元*/
        if (edPwdChangeTo.getText().toString().length() < 8) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_pwd_is_morethan_eight_words);
            return;
        }

        /*再輸入為空的*/
        if (edPwdAgain.getText().toString().equals("")) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_pwd_is_morethan_eight_words);
            return;
        }

         /*check passwordcheck*/
        if (!isPasswordCheck) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_inputbox_pwd_twice_enter_is_not_match);
            edPwdAgain.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
            return;
        }


        doUpDatePassword();


    }

    private void back() {
        finish();
        ActivityAnim.FinishAnim(mActivity);
    }


    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doUpDatePassword();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    private void doUpDatePassword() {
        if (!HttpUtility.isConnect(mActivity)) {
          setNoConnect();
            return;
        }

        upDatePasswordTask = new UpDatePasswordTask();
        upDatePasswordTask.execute();
    }


    public class UpDatePasswordTask extends AsyncTask<Void, Void, Object> {

        private int p22Result = -1;
        private String p22Message = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
           startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> data = new HashMap<String, String>();
            data.put(Key.id, PPBApplication.getInstance().getId());
            data.put(Key.token, PPBApplication.getInstance().getToken());
            data.put("oldpwd", edPwdCurrent.getText().toString());
            data.put("newpwd", edPwdAgain.getText().toString());
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, PPBApplication.getInstance().getId());
            sendData.put(Key.token, PPBApplication.getInstance().getToken());
            sendData.put("oldpwd", edPwdCurrent.getText().toString());
            sendData.put("newpwd", edPwdAgain.getText().toString());
            sendData.put(Key.sign, sign);


            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P22_UpdatePassword(), sendData, null);
                MyLog.Set("d", getClass(), "p22strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p22Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p22Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p22Result == 0) {
                        p22Message = jsonObject.getString(Key.message);
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

            if (p22Result == 1) {
                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_edit_finish);

                back();


            } else if (p22Result == 0) {
                DialogV2Custom.BuildError(mActivity, p22Message);
            } else if (p22Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }
    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        if (!HttpUtility.isConnect(mActivity)) {
          setNoConnect();
            return;
        }


        switch (view.getId()) {

            case R.id.tvConfirm:

                confirm();

                break;

            case R.id.backImg:
                back();
                break;

        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        back();
    }


    @Override
    public void onDestroy() {
        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        cancelTask(upDatePasswordTask);

        Recycle.IMG(backImg);

        System.gc();

        super.onDestroy();
    }


}

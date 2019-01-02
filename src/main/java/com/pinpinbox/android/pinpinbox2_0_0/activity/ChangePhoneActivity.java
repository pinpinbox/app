package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol112_RequestSmsPwdForUpdateCellphone;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol53_UpdateCellphone;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopPicker;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2017/3/6.
 */
public class ChangePhoneActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private PopPicker popPicker;
    private CountDownTimer cTimer;

    private Protocol53_UpdateCellphone protocol53;
    private Protocol112_RequestSmsPwdForUpdateCellphone protocol112;
    //    private RequestSmsTask requestSmsTask;
//    private UpdateCellphoneTask updateCellphoneTask;

    private RelativeLayout rCode;
    private ImageView backImg;
    private TextView tvCountry, tvSendPhone, tvDone, tvCurrentNumber, tvTime;
    private EditText edPhone, edCode;

    private int doingType;

    private boolean isCanSendAgain = true;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_changephone);
        SystemUtility.SysApplication.getInstance().addActivity(this);
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        init();
        setEditTextListener();


    }


    private void init() {

        mActivity = this;

        rCode = (RelativeLayout) findViewById(R.id.rCode);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvSendPhone = (TextView) findViewById(R.id.tvSendPhone);
        tvDone = (TextView) findViewById(R.id.tvDone);
        tvCurrentNumber = (TextView) findViewById(R.id.tvCurrentNumber);
        tvTime = (TextView) findViewById(R.id.tvTime);
        edPhone = (EditText) findViewById(R.id.edPhone);
        edCode = (EditText) findViewById(R.id.edCode);
        backImg = (ImageView) findViewById(R.id.backImg);

        TextUtility.setBold((TextView) findViewById(R.id.tvTitle), true);
        TextUtility.setBold((TextView) findViewById(R.id.tv1), true);



        tvCountry.setOnClickListener(this);
        tvSendPhone.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        backImg.setOnClickListener(this);

        tvCurrentNumber.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_current_phone_number) + PPBApplication.getInstance().getData().getString(Key.cellphone, ""));

    }

    private void setEditTextListener() {

        edPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() < 1) {
                    edPhone.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                } else {
                    edPhone.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                }

            }
        });

        edCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() < 1) {
                    rCode.setBackgroundResource(R.drawable.border_2_0_0_pink_third_radius);
                } else {
                    rCode.setBackgroundResource(R.drawable.border_2_0_0_grey_third_radius);
                }

            }
        });


    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {

                    case DoingTypeClass.DoRequestSms:
                        doRequestSms();
                        break;


                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }


    private void doRequestSms() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }


        String strCountryNumber = tvCountry.getText().toString().substring(tvCountry.getText().toString().indexOf("+") + 1);
        String strCompletePhone = "+" + strCountryNumber + edPhone.getText().toString();

        protocol112 = new Protocol112_RequestSmsPwdForUpdateCellphone(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                strCompletePhone,
                new Protocol112_RequestSmsPwdForUpdateCellphone.TaskCallBack() {
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
                        PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_ver_code_is_already_send);
                        cTimer = new CountDownTimer(60000, 1000) {
                            public void onTick(long millisUntilFinished) {

                                MyLog.Set("d", mActivity.getClass(), "剩餘 " + millisUntilFinished / 1000 + " 秒");

                                tvTime.setVisibility(View.VISIBLE);
                                tvSendPhone.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                                tvSendPhone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

                                String strTime = (millisUntilFinished / 1000) + "";

                                tvTime.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_time_left0) + strTime + getResources().getString(R.string.pinpinbox_2_0_0_other_text_time_left1));

                                isCanSendAgain = false;
                            }

                            public void onFinish() {
                                tvTime.setVisibility(View.INVISIBLE);
                                tvSendPhone.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);
                                tvSendPhone.setTextColor(Color.parseColor(ColorClass.WHITE));
                                isCanSendAgain = true;
                            }
                        }.start();
                    }

                    @Override
                    public void TimeOut() {
                        doRequestSms();
                    }
                }

        );


    }

    private void doUpdateCellPhone() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        String strCountryNumber = tvCountry.getText().toString().substring(tvCountry.getText().toString().indexOf("+") + 1);
        String strCompletePhone = "+" + strCountryNumber + edPhone.getText().toString();

        protocol53 = new Protocol53_UpdateCellphone(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                strCompletePhone,
                edCode.getText().toString(),
                new Protocol53_UpdateCellphone.TaskCallBack() {
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
                        PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_edit_finish);

                        SharedPreferences.Editor editor = PPBApplication.getInstance().getData().edit();
                        editor.putString("countrynumber", tvCountry.getText().toString().substring(tvCountry.getText().toString().indexOf("+") + 1));
                        editor.putString("nocountry_phonenumber", edPhone.getText().toString());
                        editor.putString("cellphone", tvCountry.getText().toString().substring(tvCountry.getText().toString().indexOf("+") + 1) + "," + edPhone.getText().toString());
                        editor.commit();

                        onBackPressed();

                    }

                    @Override
                    public void TimeOut() {
                        doUpdateCellPhone();
                    }
                }
        );


    }


//    private class RequestSmsTask extends AsyncTask<Void, Void, Object> {
//
//        private int p03Result = -1;
//        private String p03Message = "";
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            doingType = DoingTypeClass.DoRequestSms;
//            startLoading();
//        }
//
//
//        @Override
//        protected Object doInBackground(Void... params) {
//            String strJson = "";
//
//            String strCountryNumber = tvCountry.getText().toString().substring(tvCountry.getText().toString().indexOf("+") + 1);
//            String strCompletePhone = strCountryNumber + "," + edPhone.getText().toString();
//
//
//            try {
//                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P03_RequestSmsPassword,
//                        SetMapByProtocol.setParam03_requestsmspwd(PPBApplication.getInstance().getData().getString(Key.account, ""),
//                                strCompletePhone, "editcellphone"), null);
//                MyLog.Set("d", getClass(), "p03strJson => " + strJson);
//            } catch (SocketTimeoutException timeout) {
//                p03Result = Key.TIMEOUT;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (strJson != null && !strJson.equals("")) {
//                try {
//                    JSONObject jsonObject = new JSONObject(strJson);
//                    p03Result = JsonUtility.GetInt(jsonObject, Key.result);
//                    if (p03Result == 0) {
//                        p03Message = JsonUtility.GetString(jsonObject, Key.message);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//            dissmissLoading();
//
//            if (p03Result == 1) {
//
//                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_ver_code_is_already_send);
//
//
//                cTimer = new CountDownTimer(60000, 1000) {
//                    public void onTick(long millisUntilFinished) {
//
//
//                        MyLog.Set("d", mActivity.getClass(), "剩餘 " + millisUntilFinished / 1000 + " 秒");
//
//                        tvTime.setVisibility(View.VISIBLE);
//                        tvSendPhone.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius_big);
//                        tvSendPhone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
//
//                        String strTime = (millisUntilFinished / 1000) + "";
//
//                        tvTime.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_time_left0) + strTime + getResources().getString(R.string.pinpinbox_2_0_0_other_text_time_left1));
//
//                        isCanSendAgain = false;
//                    }
//
//                    public void onFinish() {
//                        tvTime.setVisibility(View.INVISIBLE);
//                        tvSendPhone.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);
//                        tvSendPhone.setTextColor(Color.parseColor(ColorClass.WHITE));
//                        isCanSendAgain = true;
//                    }
//                }.start();
//
//            } else if (p03Result == 0) {
//                DialogV2Custom.BuildError(mActivity, p03Message);
//            } else if (p03Result == Key.TIMEOUT) {
//                connectInstability();
//            } else {
//                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
//            }
//
//        }
//
//    }

//    private class UpdateCellphoneTask extends AsyncTask<Void, Void, Object> {
//
//        private int p53Result = -1;
//        private String p53Message;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            doingType = DoingTypeClass.DoUpDateCellPhone;
//            startLoading();
//        }
//
//        @Override
//        protected Object doInBackground(Void... params) {
//
//
//            String strOldPhone = PPBApplication.getInstance().getData().getString(Key.cellphone, "");
//
//
//            String strCountryNumber = tvCountry.getText().toString().substring(tvCountry.getText().toString().indexOf("+") + 1);
//            String strCompletePhone = strCountryNumber + "," + edPhone.getText().toString();
//
//
//            Map<String, String> data = new HashMap<String, String>();
//            data.put(Key.id, PPBApplication.getInstance().getId());
//            data.put(Key.token, PPBApplication.getInstance().getToken());
//            data.put("oldcellphone", strOldPhone);
//            data.put("newcellphone", strCompletePhone);
//            data.put("smspassword", edCode.getText().toString());
//            String sign = IndexSheet.encodePPB(data);
//
//            Map<String, String> sendData = new HashMap<String, String>();
//            sendData.put(Key.id, PPBApplication.getInstance().getId());
//            sendData.put(Key.token, PPBApplication.getInstance().getToken());
//            sendData.put("oldcellphone", strOldPhone);
//            sendData.put("newcellphone", strCompletePhone);
//            sendData.put("smspassword", edCode.getText().toString());
//            sendData.put(Key.sign, sign);
//
//            String strJson = "";
//
//            try {
//                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P53_UpdateCellphone, sendData, null);
//                MyLog.Set("d", getClass(), "p53strJson => " + strJson);
//            } catch (SocketTimeoutException timeout) {
//                p53Result = Key.TIMEOUT;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (strJson != null && !strJson.equals("")) {
//                try {
//                    JSONObject jsonObject = new JSONObject(strJson);
//                    p53Result = JsonUtility.GetInt(jsonObject, Key.result);
//                    if (p53Result == 0) {
//                        p53Message = jsonObject.getString(Key.message);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//            dissmissLoading();
//            if (p53Result == 1) {
//
//
//                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_edit_finish);
//
//                SharedPreferences.Editor editor = PPBApplication.getInstance().getData().edit();
//                editor.putString("countrynumber", tvCountry.getText().toString().substring(tvCountry.getText().toString().indexOf("+") + 1));
//                editor.putString("nocountry_phonenumber", edPhone.getText().toString());
//                editor.putString("cellphone", tvCountry.getText().toString().substring(tvCountry.getText().toString().indexOf("+") + 1) + "," + edPhone.getText().toString());
//                editor.commit();
//
//
//                back();
//
//
//            } else if (p53Result == 0) {
//                DialogV2Custom.BuildError(mActivity, p53Message);
//            } else if (p53Result == Key.TIMEOUT) {
//                connectInstability();
//            } else {
//                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
//            }
//        }
//    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tvCountry:

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


                            tvCountry.setText(popPicker.getStrSelect());

                            popPicker.dismiss();

                        }
                    });
                }
                popPicker.show((RelativeLayout) findViewById(R.id.rBackground));


                break;

            case R.id.tvSendPhone:

                if (edPhone.getText().toString().length() < 1) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_cellphone_number);
                    return;
                }

                if (isCanSendAgain) {
                    doRequestSms();
                }


                break;

            case R.id.tvDone:

                if (edCode.getText().toString().length() < 1) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_enter_ver_code);
                    return;
                }

                doUpdateCellPhone();

                break;

            case R.id.backImg:
                onBackPressed();
                break;

        }

    }

    @Override
    public void onBackPressed() {
        finish();
        ActivityAnim.FinishAnim(mActivity);
    }


    @Override
    public void onDestroy() {
        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);
        cancelTask(protocol112);

        if (cTimer != null) {
            cTimer.cancel();
            cTimer = null;
        }

        Recycle.IMG(backImg);

        System.gc();
        super.onDestroy();
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.RedPointManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ResultCodeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogExchange;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DismissExcute;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol106;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol109;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol110;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol43;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2018/2/1.
 */

public class ExchangeInfo2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private Protocol43 protocol43;
    private Protocol106 protocol106;
    private Protocol109 protocol109;
    private Protocol110 protocol110;

    private ImageView backImg;
    private TextView tvExchange, tvExchangeTime, tvName, tvDescription, tvContactTitle, tvRegisterPhone;
    private EditText edName, edPhone, edAddress;
    private RoundCornerImageView exchangeImg;
    private ItemExchange itemExchange;

    private boolean isExchanged = false;
    private boolean isSlotType = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_exchangeinfo);

        supportPostponeEnterTransition();

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();

        init();

        setData();

        setTime();


    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            itemExchange = (ItemExchange) bundle.getSerializable("exchangeItem");
            isExchanged = bundle.getBoolean("isExchanged", false);
            isSlotType = bundle.getBoolean("isSlotType", false);

        }
    }


    private void init() {

        mActivity = this;

        backImg = (ImageView) findViewById(R.id.backImg);
        exchangeImg = (RoundCornerImageView) findViewById(R.id.exchangeImg);


        edName = (EditText) findViewById(R.id.edName);
        edPhone = (EditText) findViewById(R.id.edPhone);
        edAddress = (EditText) findViewById(R.id.edAddress);

        tvExchange = (TextView) findViewById(R.id.tvExchange);
        tvExchangeTime = (TextView) findViewById(R.id.tvExchangeTime);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvContactTitle = (TextView) findViewById(R.id.tvContactTitle);
        tvRegisterPhone = (TextView) findViewById(R.id.tvRegisterPhone);

        TextUtility.setBold(tvExchange, true);
        TextUtility.setBold(tvExchangeTime, true);
        TextUtility.setBold(tvName, true);
        TextUtility.setBold(tvContactTitle, true);
        TextUtility.setBold(tvRegisterPhone, true);

        tvRegisterPhone.setOnClickListener(this);
        backImg.setOnClickListener(this);
        tvExchange.setOnClickListener(this);


        if (PPBApplication.getInstance().getData().getBoolean(Key.is_FB_Login, false)) {
            tvRegisterPhone.setVisibility(View.GONE);
        } else {
            tvRegisterPhone.setVisibility(View.VISIBLE);
        }

        if (isExchanged) {
            //已完成
            tvExchangeTime.setVisibility(View.GONE);
            tvExchange.setText(R.string.pinpinbox_2_0_0_button_send);
            tvContactTitle.setText(R.string.pinpinbox_2_0_0_title_update_contact);

        } else {
            //未領取
            tvExchangeTime.setVisibility(View.VISIBLE);
            tvExchange.setText(R.string.pinpinbox_2_0_0_button_exchange_now);
            tvContactTitle.setText(R.string.pinpinbox_2_0_0_title_contact);
        }


    }

    private void setData() {


        MyLog.Set("e", getClass(), "startTime => " + itemExchange.getStarttime());
        MyLog.Set("e", getClass(), "endTime => " + itemExchange.getEndtime());

        if (!isExchanged) {
            setTime();
        }


        tvName.setText(itemExchange.getName());
        tvDescription.setText(itemExchange.getDescription());

        if (SystemUtility.Above_Equal_V5()) {
            exchangeImg.setTransitionName(itemExchange.getImage());
        }


        edName.setText(PPBApplication.getInstance().getData().getString(Key.contact_name, ""));
        edPhone.setText(PPBApplication.getInstance().getData().getString(Key.contact_phone, ""));
        edAddress.setText(PPBApplication.getInstance().getData().getString(Key.contact_address, ""));

        if (!itemExchange.getImage().equals("")) {

            Picasso.with(getApplicationContext())
                    .load(itemExchange.getImage())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .noFade()
                    .tag(getApplicationContext())
                    .into(exchangeImg, new Callback() {
                        @Override
                        public void onSuccess() {

                            supportStartPostponedEnterTransition();

                        }

                        @Override
                        public void onError() {

                            supportStartPostponedEnterTransition();

                        }
                    });


        } else {

            supportStartPostponedEnterTransition();

        }

    }

    private void setTime() {


        if (itemExchange.getEndtime() == null || itemExchange.getEndtime().equals("") || itemExchange.getEndtime().equals("null")) {


        } else {
                    /*獲取當前時間*/
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间

            try {
                Date endDate = df.parse(itemExchange.getEndtime());


                long l = endDate.getTime() - curDate.getTime();
                long day = l / (24 * 60 * 60 * 1000);
                long hour = (l / (60 * 60 * 1000) - day * 24);
                long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

//                tvExchangeTime.setText("剩餘時間:" + (int)(day-1) + "天" + (int)(24-hour - 1) + "小時" + min + "分");
                tvExchangeTime.setText("剩餘時間:" + day + "天" + hour  + "小時" + min + "分");

            } catch (Exception e) {
                e.printStackTrace();
            }


//            try {
//
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//                Date endDate = df.parse(itemExchange.getEndtime());
//
//                long diff =  endDate.getTime() -curDate.getTime() ;
//                long days = diff / (1000 * 60 * 60 * 24);
//                long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
//                long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
//                tvExchangeTime.setText("剩餘時間:" + days + "天" + hours + "小時" + minutes + "分");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


        }


    }

    private void doGetPhotousefor_user_id() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        PPBApplication.getInstance().getData().edit().putString(Key.contact_name, edName.getText().toString()).commit();
        PPBApplication.getInstance().getData().edit().putString(Key.contact_address, edAddress.getText().toString()).commit();
        PPBApplication.getInstance().getData().edit().putString(Key.contact_phone, edPhone.getText().toString()).commit();

        if (isExchanged) {

            doUpdate();

        } else {

            DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.setMessage("注意獎項是否註明現場領取或寄送，如為現場領取則領取方本人須在現場執行兌換動作");
            d.getTvRightOrBottom().setText("兌換");
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {

                    if (isSlotType) {

                        doExchange();

                    } else {

                        protocol110 = new Protocol110(
                                mActivity,
                                PPBApplication.getInstance().getId(),
                                PPBApplication.getInstance().getToken(),
                                itemExchange.getPhoto_id() + "",
                                getSharedPreferences(SharedPreferencesDataClass.deviceDetail, Activity.MODE_PRIVATE).getString("deviceid", ""),

                                new Protocol110.TaskCallBack() {
                                    @Override
                                    public void Prepare() {
                                        startLoading();
                                    }

                                    @Override
                                    public void Post() {
                                        dissmissLoading();
                                    }

                                    @Override
                                    public void Success(int puuId) {
                                        itemExchange.setPhotousefor_user_id(puuId);
                                        doExchange();
                                    }

                                    @Override
                                    public void TimeOut() {
                                        doGetPhotousefor_user_id();
                                    }
                                }
                        );

                    }

                }
            });
            d.show();


        }


    }


    private void doExchange() {

        protocol106 = new Protocol106(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                itemExchange.getPhotousefor_user_id(),
                getParam(),
                new Protocol106.TaskCallBack() {
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

//                        PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_exchange_success);

                        Activity acReader = SystemUtility.getActivity(Reader2Activity.class.getSimpleName());
                        if (acReader != null) {
                            ((Reader2Activity) acReader).reFreshExchange();
                        }


                        Activity acExchangeList = SystemUtility.getActivity(ExchangeList2Activity.class.getSimpleName());
                        if (acExchangeList != null) {
                            setResult(ResultCodeClass.ScrollToDonePage);
                        }


                        if (!itemExchange.isIs_existing()) {
                            doAddToDoneList();
                        } else {

                            DialogExchange d = new DialogExchange(mActivity);
                            d.setDismissExcute(new DismissExcute() {
                                @Override
                                public void AfterDismissDo() {
                                    onBackPressed();
                                }
                            });
                            d.show();

                        }


                    }

                    @Override
                    public void TimeOut() {
                        doExchange();
                    }
                }
        );


    }

    private void doAddToDoneList() {

        protocol109 = new Protocol109(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                itemExchange.getPhoto_id() + "",
                new Protocol109.TaskCallBack() {
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

                        MyLog.Set("d", mActivity.getClass(), "---------成功加入清單---------");

                        DialogExchange d = new DialogExchange(mActivity);
                        d.setDismissExcute(new DismissExcute() {
                            @Override
                            public void AfterDismissDo() {

                                RedPointManager.showOrHideOnExchangeList(true);

                                onBackPressed();
                            }
                        });
                        d.show();
                    }

                    @Override
                    public void TimeOut() {
                        doAddToDoneList();
                    }
                }
        );

    }

    private void doUpdate() {


        protocol43 = new Protocol43(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                itemExchange.getPhotousefor_user_id(),
                getParam(),
                new Protocol43.TaskCallBack() {
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

                        PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_update_done);

                        onBackPressed();


                    }

                    @Override
                    public void TimeOut() {
                        doUpdate();
                    }
                }
        );


    }

    private String getParam() {

        String strParam = "";

        try {
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("address", edAddress.getText().toString());
            jsonParam.put("cellphone", edPhone.getText().toString());
            jsonParam.put("name", edName.getText().toString());

            strParam = jsonParam.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strParam;

    }

    @Override
    public void onClick(View v) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (v.getId()) {

            case R.id.backImg:
                onBackPressed();
                break;

            case R.id.tvRegisterPhone:

                edPhone.setText(PPBApplication.getInstance().getData().getString(Key.cellphone, ""));


                break;

            case R.id.tvExchange:

                doGetPhotousefor_user_id();

                break;


        }

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {


        if (SystemUtility.getSystemVersion() >= SystemUtility.V5) {
            supportFinishAfterTransition();
        } else {
            finish();
            ActivityAnim.FinishAnim(mActivity);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        cancelTask(protocol43);
        cancelTask(protocol106);
        cancelTask(protocol109);
        cancelTask(protocol110);

        Recycle.IMG(backImg);

        super.onDestroy();
    }


}

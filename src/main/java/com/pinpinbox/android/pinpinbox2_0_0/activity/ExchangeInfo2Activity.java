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
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentExchangeUnfinished2;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol106;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol109;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol110;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol43;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2018/2/1.
 */

public class ExchangeInfo2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private Protocol43 protocol43;
    private Protocol106 protocol106;
    private Protocol110 protocol110;

    private ImageView backImg;
    private TextView tvExchange, tvExchangeTime, tvName, tvDescription, tvContactTitle, tvRegisterPhone;
    private EditText edName, edPhone, edAddress;
    private RoundCornerImageView exchangeImg;
    private ItemExchange itemExchange;

    private int photo_id;
    private int photousefor_user_id;

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


    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            itemExchange = (ItemExchange) bundle.getSerializable("exchangeItem");
            isExchanged = bundle.getBoolean("isExchanged", false);
            isSlotType = bundle.getBoolean("isSlotType", false);
            photo_id = bundle.getInt(Key.photo_id, -1);

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

//        tvExchangeTime.setText(itemExchange.getTime());



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

    private void doGetPhotousefor_user_id() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        PPBApplication.getInstance().getData().edit().putString(Key.contact_name, edName.getText().toString()).commit();
        PPBApplication.getInstance().getData().edit().putString(Key.contact_address, edAddress.getText().toString()).commit();
        PPBApplication.getInstance().getData().edit().putString(Key.contact_phone, edPhone.getText().toString()).commit();


        if(isSlotType){

            //為抽獎得到的獎項 故不需要接口110歸屬用戶

            photousefor_user_id = itemExchange.getPhotousefor_user_id();

            if (isExchanged) {
                doUpdate();
            } else {
                doExchange();
            }


        }else {
            protocol110 = new Protocol110(
                    mActivity,
                    PPBApplication.getInstance().getId(),
                    PPBApplication.getInstance().getToken(),
                    photo_id + "",
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
                        public void Success(int puf_user_id) {
                            photousefor_user_id = puf_user_id;


                            if (isExchanged) {
                                doUpdate();
                            } else {
                                doExchange();
                            }

                        }

                        @Override
                        public void TimeOut() {
                            doGetPhotousefor_user_id();
                        }
                    }
            );
        }






    }

    private void doExchange() {

        protocol106 = new Protocol106(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                photousefor_user_id,
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

                        PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_exchange_success);

                        Activity acExchangeList = SystemUtility.getActivity(ExchangeList2Activity.class.getSimpleName());
                        if (acExchangeList != null) {

                            FragmentExchangeUnfinished2 fragmentExchangeUnfinished2 = (FragmentExchangeUnfinished2) ((ExchangeList2Activity) acExchangeList).getFragment(FragmentExchangeUnfinished2.class.getSimpleName());

                            fragmentExchangeUnfinished2.moveItem();

                            ((ExchangeList2Activity)acExchangeList).scrollToDonePage();




                        }else {
                            doAddToDoneList();
                        }

                        Activity acReader = SystemUtility.getActivity(Reader2Activity.class.getSimpleName());
                        if(acReader!=null){
                            ((Reader2Activity)acReader).reFreshExchange();
                        }


                        doAddToDoneList();

                    }

                    @Override
                    public void TimeOut() {
                        doExchange();
                    }
                }
        );


    }

    private void doAddToDoneList(){

        Protocol109 protocol109 = new Protocol109(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                photo_id + "",
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

//                        Activity acExchangeList = SystemUtility.getActivity(ExchangeList2Activity.class.getSimpleName());
//                        if (acExchangeList != null) {
//
//
//                            ItemExchange newItemExchange = new ItemExchange();
//                            newItemExchange.setHas_gained(true);
//                            newItemExchange.setPhoto_id(photo_id);
//                            newItemExchange.setDescription(itemExchange.getDescription());
//                            newItemExchange.setImage(itemExchange.getImage());
//                            newItemExchange.setName(itemExchange.getName());
//                            newItemExchange.setPhotousefor_id(itemExchange.getPhotousefor_id());
//                            newItemExchange.setImageWidth(PPBApplication.getInstance().getStaggeredWidth());
//                            newItemExchange.setImageHeight(PPBApplication.getInstance().getStaggeredWidth());
//
//                            newItemExchange.setPhotousefor_user_id(itemExchange.getPhotousefor_user_id());
//
//
//                            FragmentExchangeDone2 fragmentExchangeDone2 = (FragmentExchangeDone2) ((ExchangeList2Activity) acExchangeList).getFragment(FragmentExchangeDone2.class.getSimpleName());
//
//                            fragmentExchangeDone2.addItem(newItemExchange);

                            MyLog.Set("e", mActivity.getClass(), "---------成功加入清單---------");


//                        }


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
                photousefor_user_id,
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

                    }

                    @Override
                    public void TimeOut() {
                        doUpdate();
                    }
                }
        );


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
        cancelTask(protocol110);

        Recycle.IMG(backImg);

        super.onDestroy();
    }


}

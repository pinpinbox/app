package com.pinpinbox.android.Activity.BuyPoint;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogSet;
import com.pinpinbox.android.Mode.LOG;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.StringClass.TaskKeyClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.NiceSpinner.NiceSpinner;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;
import com.pinpinbox.android.util.IabHelper;
import com.pinpinbox.android.util.IabResult;
import com.pinpinbox.android.util.Inventory;
import com.pinpinbox.android.util.Purchase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2015/12/27.
 */
public class PointActivity extends DraggerActivity {

    private Activity mActivity;
    private Context mContext;
    private SharedPreferences getdata;
    private ValueAnimator animator;
    private Animation loadAnimation;
    private LoadingAnimation loading;
    private NoConnect noConnect;

    private GetPointTask getPointTask;
    private GetPointStoreTask getPointStoreTask;
    private GetPayloadTask getPayloadTask;
    private BuyCompleteTask buyCompleteTask;
    private FirstBuyPointTask firstBuyPointTask;

    private ArrayList<HashMap<String, Object>> pointDetailList;

    //    private PickerView pickerView;
//    private ImageView selectImg;
//    private ImageView lightImg;
//    private RelativeLayout selectR;
    private TextView tvNT, tvCurrentPoint;
    private RelativeLayout rBuy;
    private NiceSpinner spinner;

    public static boolean hasITEM = false;
    private boolean selectOpen;

    private int REFRESHPOINT = 55;
    private int doingType;
    private static final int DoGetPoint = 0;
    private static final int DoGetPointStore = 1;
    private static final int DoGetPayLoad = 2;
    private static final int DoComplete = 3;
    private static final int DoFirstBuy = 4;

    private String TAG = "PointActivity";
    private String id, token;
    private String strP, strNT;
    private String strPoints;
    private String product_id;
    private String p23Result, p23Message,
            p24Result, p24Message,
            p29Result, p29Message,
            p30Result, p30getPoints, p30Message,
            p83Result, p83Message;


    private String order_id;
    private String aa, bb, cc, dd, ee, ff, gg, hh, ii, jj = "NA";

    public String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAml6Hl7GI2+gQsoXuKfG+z2fpw2T8+v84WiQm16vuzEufKN4pZ3/ZQ0zAMSpZNf4BZbpWGUt8tj7wQaEayD7KFBoLZ3mhH9NLZD/6SbEKXbRjH0QxDcyBN/1E1d3iWR85Mi1w0I8y2+CnE6ZtIYbvvYTCEOTyjZv2ml9/h84sWoOQ/tNSaIEPOrtocLH0Q39IzZlsYrFj9d4DHZXBbMNaf3CpmkT9YF3+JSdnggAsKOI5gDx8Ebud+FGvtq+WPhGRIJGV0SZ4UtD4Z3K9r1kKxIfEvuOf4ERIK5WLP0Uj0QQgcA0P8P/qLYQgv1LLVIlwRn5rPFVTc88dOfDOqmactQIDAQAB";


    private Inventory mInventory;
    private IabHelper mHelper;

    public int requestCode = 177;

    private List<String> listdata;

    //    private GetPointContentTask getPointContentTask;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    callGoogleInAppBilling();
                    break;

                case 1:
                    ProductDetail();
                    break;

                case 2:

                    doComplete();

                    break;

            }
        }
    };

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_buypoint);

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }


        init();
        back();

        doGetPoint();

//        if (HttpUtility.isConnect(mActivity)) {
//            getPointContentTask = new GetPointContentTask();
//            getPointContentTask.execute();
//        } else {
//            noConnect = new NoConnect(mActivity);
//        }


//        base64EncodedPublicKey = new String(Base64.decode(key.getBytes(), Base64.DEFAULT));
//        System.out.print(base64EncodedPublicKey);

//        Button b = (Button)findViewById(R.id.ddd);
//
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                 loadAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_once);
//                 lightImg.startAnimation(loadAnimation);
//
//
//            }
//        });

    }

    private void init() {

        mActivity = this;
        mContext = getApplicationContext();

        loading = new LoadingAnimation(mActivity);

        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");

//        pickerView = (PickerView) findViewById(R.id.buyP_picker);
//        selectImg = (ImageView) findViewById(R.id.buyP_selectButton);
//        lightImg = (ImageView) findViewById(R.id.lightImg);
//        selectR = (RelativeLayout) findViewById(R.id.selectR);
        tvNT = (TextView) findViewById(R.id.tvNT);
        rBuy = (RelativeLayout) findViewById(R.id.rBuy);
        tvCurrentPoint = (TextView) findViewById(R.id.tvCurrentPoint);
        spinner = (NiceSpinner) findViewById(R.id.spinner);
        spinner.setBackground(R.drawable.bg_spinner);


        selectOpen = false;

        listdata = new ArrayList<>();
        pointDetailList = new ArrayList<>();
    }

//    /* 開起選項*/
//    private void openSelect() {
//        tvP.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!selectOpen) {
//                    selectImg.setVisibility(View.INVISIBLE);
//                    selectR.setVisibility(View.VISIBLE);
//                    selectOpen = true;
//                } else {
//                    selectImg.setVisibility(View.VISIBLE);
//                    selectR.setVisibility(View.INVISIBLE);
//                    selectOpen = false;
//                }
//            }
//        });
//    }

    /* 點選購買 (獲取payload)*/
   /*protocol29()*/
    private void buyClick() {
        rBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    noConnect = new NoConnect(mActivity);
                    return;
                }

                strP = spinner.getText().toString();
//                strP = tvP.getText().toString();
                strNT = tvNT.getText().toString();
                switch (strP) {
                    case "42 P ":
                        product_id = (String) pointDetailList.get(0).get("platform_flag");
                        break;
                    case "126 P ":
                        product_id = (String) pointDetailList.get(1).get("platform_flag");
                        break;
                    case "294 P ":
                        product_id = (String) pointDetailList.get(2).get("platform_flag");
                        break;
                    case "756 P ":
                        product_id = (String) pointDetailList.get(3).get("platform_flag");
                        break;
                    case "2016 P ":
                        product_id = (String) pointDetailList.get(4).get("platform_flag");
                        break;
                }

                MyLog.Set("d", getClass(), "product_id =>" + product_id);

                final DialogSet dialogSet = new DialogSet(mActivity);
//                dialogSet.setAlbumContents_StringType(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_message_confirm_buy) + " " + strP + " : " + strNT + " " + mActivity.getResources().getString(R.string.t));
                dialogSet.getTextView_Y().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogSet.getDialog().dismiss();

                        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                            return;
                        }

                        if (!HttpUtility.isConnect(mActivity)) {
                            noConnect = new NoConnect(mActivity);
                            return;
                        }

                        loading.show();

                        String lastOrderId = getdata.getString("LastErrorOrderId", "");

                        if (lastOrderId != null && !lastOrderId.equals("")) {

                            MyLog.Set("e", getClass(), "reCall order_id");

                            order_id = lastOrderId;
                            Message msg = new Message();
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                        } else {

                            MyLog.Set("e", getClass(), "New Call order_id");

                            doGetPayLoad();

                        }

                    }
                });
            }
        });
    }

    /*關閉activity*/
    private void back() {
        ImageView back = (ImageView) findViewById(R.id.buyP_backImg);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivityAnim.FinishAnim(mActivity);
            }
        });
    }

    /* **************************************************************************************************/
//    private void protocol23() {
//        try {
//            String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(id, token, "google"), null);
//            Log.e(TAG, "p23strJson = >" + strJson);
//            JSONObject jsonObject = new JSONObject(strJson);
//            p23Result = jsonObject.getString("result");
//
//            if (p23Result.equals("1")) {
//                strPoints = jsonObject.getString("data");
//            } else if (p23Result.equals("0")) {
//                p23Message = jsonObject.getString("message");
//            } else {
//                p23Result = "";
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void protocol24() {
//        try {
//            String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P24_GetPointStore, SetMapByProtocol.setParam24_getpointstore(id, token, "google", "TWD"), null);
//            Log.d(TAG, "p24strJson => " + strJson);
//
//            JSONObject jsonObject = new JSONObject(strJson);
//            p24Result = jsonObject.getString("result");
//
//            if (p24Result.equals("1")) {
//                String jdata = jsonObject.getString("data");
//                JSONArray jsonArray = new JSONArray(jdata);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject obj = (JSONObject) jsonArray.get(i);
//
//                    String platform_flag = obj.getString("platform_flag");
//                    String total = obj.getString("total");
//                    String obtain = obj.getString("obtain");
//
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("platform_flag", platform_flag);
//                    map.put("total", total);
//                    map.put("obtain", obtain);
//                    pointDetailList.add(map);
//                }
//
//            } else if (p24Result.equals("0")) {
//                p24Message = jsonObject.getString("message");
//            } else {
//                p24Result = "";
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void protocol29() {
//        try {
//            String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P29_GetPayload, SetMapByProtocol.setParam29_getpayload(id, token, "google", product_id), null);
//            System.out.println("strJson29 => " + strJson);
//            JSONObject jsonObject = new JSONObject(strJson);
//            p29result = jsonObject.getString("result");
//            if (p29result.equals("1")) {
//                order_id = jsonObject.getString("data");
//                Log.e(TAG, "order_id(payload) => " + order_id);
//            } else if (p29result.equals("0")) {
//                p29Message = jsonObject.getString("message");
//            } else {
//                p29result = "";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void protocol30() {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("itemType", ff);
//            jsonObject.put("jsonPurchaseInfo", gg);
//            jsonObject.put("signature", ii);
//
//            String dataSignature = jsonObject.toString();
//            System.out.print("dataSignature => " + dataSignature);
//
//            JSONObject jsonObject1 = new JSONObject(dataSignature);
//            String jsonPurchaseInfo = jsonObject1.getString("jsonPurchaseInfo");
//            System.out.println("jsonPurchaseInfo =>" + jsonPurchaseInfo);
//
//            String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P30_Finishpurchased, SetMapByProtocol.setParam30_Finishpurchased(id, token, "google", order_id, dataSignature), null);
//            if (LOG.isLogMode) {
//                Log.d(TAG, "p30strJson => " + strJson);
//            }
//            JSONObject object = new JSONObject(strJson);
//            p30Result = object.getString("result");
//            if (p30Result.equals("1")) {
//                p30getPoints = object.getString("data");
//            } else if (p30Result.equals("0")) {
//                p30Message = object.getString("message");
//            } else {
//                p30Result = "";
//            }
//
//        } catch (Exception e) {
//            p30Result = "";
//            e.printStackTrace();
//        }
//    }

//    private void getResult23() {
//        try {
//            if (p23Result.equals("1")) {
//                tvUserPoint.setText(strPoints);
//            } else if (p23Result.equals("0")) {
//                DialogSet d = new DialogSet(mActivity);
//                d.setMessageError(p23Message);
//            } else {
//                DialogSet d = new DialogSet(mActivity);
//                d.DialogUnKnow();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getResult24() {
//        if (p24Result.equals("1")) {
//            pickerView.setTextSize(DensityUtility.sp2px(mActivity, 18));
//
//            for (int i = 0; i < pointDetailList.size(); i++) {
//                listdata.add((String) pointDetailList.get(i).get("obtain"));
//            }
//            pickerView.setData(listdata);
//            pickerView.setOnSelectListener(new PickerView.onSelectListener() {
//                @Override
//                public void onSelect(String str, int p) {
//                    int point = StringIntMethod.StringToInt(str);
//                    tvP.setText(point + " P");
//                    tvNT.setText("NT " + (int) (point / 1.4));
//                }
//            });
//
//            openSelect(); //開起選項
//
//        } else if (p24Result.equals("0")) {
//            DialogSet d = new DialogSet(mActivity);
//            d.setMessageError(p24Message);
//        } else {
//            DialogSet d = new DialogSet(mActivity);
//            d.DialogUnKnow();
//        }
//    }
     /* **************************************************************************************************/

    /*此為 msg.what = 0*/
   /* ******************************************************/
   /* 呼叫 google in appbilling*/
   /*loadFruitInventory() , purchaseFruit()*/
    private void callGoogleInAppBilling() {
//        mHelper = new IabHelper(mContext, base64EncodedPublicKey);
        mHelper = new IabHelper(mContext, key);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // TODO Error handling of In-App Billing integration!
                    Log.e("PPP", "Load error" + result.getMessage());
                } else {
                    System.out.println("*********************************************************");
                    loadFruitInventory();
                }
            }
        });
//        boolean testPurchase = true; //測試
//        if (testPurchase) {
//            new Thread() {
//
//                public void run() {
//                    try {
//                        if (!hasITEM) {
//                            sleep(2000);
//                            System.out.println("--------------------------------------");
//                            purchaseFruit(product_id);
//                            Log.i("CHECK!!!!!!", "Check ITEM status " + hasITEM);
//                        }
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }.start();
//        }

    }


    /* 獲取商品清單*/
   /* verifyDeveloperPayload()*/
    public void loadFruitInventory() {
        //Log.e("hasITEM", "" + hasITEM);

//        MyLog.Set("d", getClass(), "loadFruitInventory");

        mHelper.queryInventoryAsync(true,
                new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result,
                                                         Inventory inventory) {
                        if (result.isSuccess()) {
                            mInventory = inventory;
                            //Do we have that virtual item already?
                            // Do we have the premium upgrade?
                            Purchase premiumPurchase = inventory.getPurchase(product_id);
                            hasITEM = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));

//                            Log.i("hasITEM", "hasITEM: " + (hasITEM ? "Yes" : "No"));

                            if (!hasITEM) {
//                                Log.i("CHECK!!!!!!", "Check ITEM status " + hasITEM);
                                purchaseFruit(product_id);
                            }


                        } else {
//                            Log.i("HelloBTaiwan", "query inventory fail");
                        }
                    }
                }
        );
    }

    boolean verifyDeveloperPayload(Purchase p) {
        order_id = p.getDeveloperPayload();
        return true;
    }

    /* 購買點選的商品*/
   /* send msg 1*/
    private void purchaseFruit(String sku) {
        mHelper.launchPurchaseFlow(this, sku, requestCode,
                new IabHelper.OnIabPurchaseFinishedListener() {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase info) {
                        if (result.isSuccess()) {
//                            Log.i("Buy item", "Success to buy it");
                            mHelper.queryInventoryAsync(true,
                                    new IabHelper.QueryInventoryFinishedListener() {
                                        @Override
                                        public void onQueryInventoryFinished(IabResult result,
                                                                             Inventory inventory) {
                                            if (result.isSuccess()) {
                                                mInventory = inventory;

                                                Purchase premiumPurchase = inventory.getPurchase(product_id);
                                                hasITEM = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));

//                                                Log.i("hasITEM", "hasITEM: " + (hasITEM ? "Yes" : "No"));
                                                if (hasITEM) {
                                                    Message msg = new Message();
                                                    msg.what = 1;
                                                    mHandler.sendMessage(msg);
                                                }


                                            } else {
//                                                Log.i("HelloBTaiwan", "query inventory fail");
                                            }
                                        }
                                    }
                            );
                        } else {
                            // TODO Error handling!
                            Log.i("Buy item", "Fail to buy it! " + result.getMessage());
                            //目前有出現之錯誤
                            //信用卡號遭拒
                            getdata.edit().putString("LastErrorOrderId", order_id).commit();


                        }
                    }

                }, order_id);

    }
   /* ******************************************************/


    /*此為 msg.what =  1  */
   /* **************************************************************************************/
   /*商品內容*/
   /*send msg 2*/
    private void ProductDetail() {
        List<Purchase> purchaseList = mInventory.getAllPurchases();
        mHelper.consumeAsync(purchaseList, new IabHelper.OnConsumeMultiFinishedListener() {
            @Override
            public void onConsumeMultiFinished(List<Purchase> purchases, List<IabResult> results) {

                aa = "";
                bb = "";
                cc = "";
                dd = "";
                ee = "";
                ff = "";
                gg = "";
                hh = "";
                ii = "";
                jj = "";
                for (Purchase pp : purchases) {
                    aa += pp.getSku();
                    bb += pp.getPackageName();
                    cc += pp.getDeveloperPayload();
                    dd += pp.getToken();
                    ee += pp.getOrderId();
                    ff += pp.getItemType();
                    gg += pp.getOriginalJson();
                    hh += pp.getPurchaseTime();
                    ii += pp.getSignature();
                    jj += pp.getPurchaseState();
                }

                if (LOG.isLogMode) {
                    System.out.println("pp.getSku() => " + aa);
                    System.out.println("pp.getPackageName() => " + bb);
                    System.out.println("pp.getDeveloperPayload() => " + cc);
                    System.out.println("pp.getToken() => " + dd);
                    System.out.println("pp.getOrderId() => " + ee);
                    System.out.println("pp.getItemType() => " + ff);
                    System.out.println("pp.getOriginalJson() => " + gg);
                    System.out.println("pp.getPurchaseTime() => " + hh);
                    System.out.println("pp.getSignature() => " + ii);
                    System.out.println("pp.getPurchaseState() => " + jj);
                }

                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessage(msg);


            }
        });
    }
   /* **************************************************************************************/


   /* ********************************************************************************************************/

    private ValueAnimator setIntChangeAnimation(int startValue, int endValue, int duration) {
        animator = ValueAnimator.ofInt(startValue, endValue);
        animator.setDuration(duration);
        animator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //设置瞬时的数据值到界面上
                        tvCurrentPoint.setText(valueAnimator.getAnimatedValue().toString());

                        if (valueAnimator.getAnimatedValue().toString().equals(p30getPoints)) {
                            animator.cancel();
                        }
                    }
                });
        return animator;
    }

//    private void setLightAnimation() {
//        loadAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.rotate_once);
//        lightImg.startAnimation(loadAnimation);
//    }

    /* *******************************************************************************************************/

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {
                    case DoGetPoint:
                        doGetPoint();
                        break;

                    case DoGetPointStore:
                        doGetPointStore();
                        break;

                    case DoGetPayLoad:
                        doGetPayLoad();
                        break;

                    case DoComplete:
                        doComplete();
                        break;

                    case DoFirstBuy:
                        doFirstBuy();
                        break;

                }
            }
        };

        DialogSet d = new DialogSet(mActivity);
        d.ConnectInstability();
        d.setConnectInstability(connectInstability);

    }

    private void doGetPoint() {
        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }
        getPointTask = new GetPointTask();
        getPointTask.execute();
    }

    private void doGetPointStore() {

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        getPointStoreTask = new GetPointStoreTask();
        getPointStoreTask.execute();

    }

    private void doGetPayLoad() {
        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }
        getPayloadTask = new GetPayloadTask();
        getPayloadTask.execute();
    }

    private void doComplete() {
        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }
        buyCompleteTask = new BuyCompleteTask();
        buyCompleteTask.execute();
    }

    private void doFirstBuy() {
        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }
        firstBuyPointTask = new FirstBuyPointTask();
        firstBuyPointTask.execute();
    }

    private class GetPointTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetPoint;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {
            Map<String, String> data = new HashMap<>();
            data.put("id", id);
            data.put("token", token);
            data.put("platform", "google");
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("platform", "google");
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(id, token, "google"), null);
                MyLog.Set("d", getClass(), "p23strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p23Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p23Result = jsonObject.getString(Key.result);
                    if (p23Result.equals("1")) {

                        strPoints = jsonObject.getString(Key.data);

//                        String userPoint = jsonObject.getString("data");
//                        userP = StringIntMethod.StringToInt(userPoint);
                    } else if (p23Result.equals("0")) {
                        p23Message = jsonObject.getString(Key.message);
                    } else {
                        p23Result = "";
                    }
                } catch (Exception e) {
                    p23Result = "";
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            if (p23Result.equals("1")) {
                tvCurrentPoint.setText(strPoints);

                doGetPointStore();

            } else if (p23Result.equals("0")) {

            } else if (p23Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                MyLog.Set("d", getClass(), "p23Result => XX");

                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }


        }
    }

    private class GetPointStoreTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetPointStore;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P24_GetPointStore, SetMapByProtocol.setParam24_getpointstore(id, token, "google", "TWD"), null);
                MyLog.Set("d", getClass(), "p24strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p24Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p24Result = jsonObject.getString(Key.result);

                    if (p24Result.equals("1")) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);

                            String platform_flag = obj.getString("platform_flag");
                            String total = obj.getString("total");
                            String obtain = obj.getString("obtain");

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("platform_flag", platform_flag);
                            map.put("total", total);
                            map.put("obtain", obtain);
                            pointDetailList.add(map);
                        }

                    } else if (p24Result.equals("0")) {
                        p24Message = jsonObject.getString(Key.message);
                    } else {
                        p24Result = "";
                    }

                } catch (Exception e) {
                    p24Result = "";
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            if (p24Result.equals("1")) {

                for (int i = 0; i < pointDetailList.size(); i++) {
                    listdata.add((String) pointDetailList.get(i).get("obtain") + " P ");
                }

                spinner.attachDataSource(listdata);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        int point = StringIntMethod.StringToInt((String) pointDetailList.get(position).get("obtain"));
//                        tvP.setText(point + " P");
                        tvNT.setText("NT " + (int) (point / 1.4));


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


//                pickerView.setTextSize(DensityUtility.sp2px(mActivity, 18));
////                pickerView.setTextColor(Color.parseColor("#ff424242"));
//
//                for (int i = 0; i < pointDetailList.size(); i++) {
//                    listdata.add((String) pointDetailList.get(i).get("obtain"));
//                }
//                pickerView.setData(listdata);
//                pickerView.setOnSelectListener(new PickerView.onSelectListener() {
//                    @Override
//                    public void onSelect(String str, int p) {
//                        int point = StringIntMethod.StringToInt(str);
//                        tvP.setText(point + " P");
//                        tvNT.setText("NT " + (int) (point / 1.4));
//                    }
//                });
//
//                openSelect(); //開起選項

                buyClick();  //點選購買
            } else if (p24Result.equals("0")) {

            } else if (p24Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }


        }
    }

    /*獲取payload */
    /*send msg 0*/
    /* protocol29*/
    class GetPayloadTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetPayLoad;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P29_GetPayload, SetMapByProtocol.setParam29_getpayload(id, token, "google", product_id), null);
                MyLog.Set("d", getClass(), "strJson29 => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p29Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p29Result = jsonObject.getString(Key.result);
                    if (p29Result.equals("1")) {
                        order_id = jsonObject.getString(Key.data);

                        MyLog.Set("e", getClass(), "order_id(payload) => " + order_id);

                    } else if (p29Result.equals("0")) {
                        p29Message = jsonObject.getString(Key.message);
                    } else {
                        p29Result = "";
                    }
                } catch (Exception e) {
                    p29Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            if (p29Result.equals("1")) {
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
            } else if (p29Result.equals("0")) {

            } else if (p29Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }


        }


    }

    /*此為 msg.what =  2 */
   /*完成購買*/
   /*protocol30*/
    class BuyCompleteTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoComplete;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String dataSignature = "";
            String strJson = "";

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("itemType", ff);
                jsonObject.put("jsonPurchaseInfo", gg);
                jsonObject.put("signature", ii);

                dataSignature = jsonObject.toString();
                MyLog.Set("d", getClass(), "dataSignature => " + dataSignature);

                JSONObject sobj = new JSONObject(dataSignature);
                String jsonPurchaseInfo = sobj.getString("jsonPurchaseInfo");

                MyLog.Set("d", getClass(), "jsonPurchaseInfo => " + jsonPurchaseInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (dataSignature != null && !dataSignature.equals("")) {
                try {
                    strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P30_Finishpurchased, SetMapByProtocol.setParam30_Finishpurchased(id, token, "google", order_id, dataSignature), null);
                    MyLog.Set("d", getClass(), "p30strJson => " + strJson);
                } catch (SocketTimeoutException timeout) {
                    p30Result = Key.timeout;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject object = new JSONObject(strJson);
                    p30Result = object.getString(Key.result);
                    if (p30Result.equals("1")) {
                        p30getPoints = object.getString(Key.data);
                    } else if (p30Result.equals("0")) {
                        p30Message = object.getString(Key.message);
                    } else {
                        p30Result = "";
                    }

                } catch (Exception e) {
                    p30Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p30Result.equals("1")) {

                order_id = "";
                getdata.edit().remove("LastErrorOrderId").commit();
                if (LOG.isLogMode) {
                    Log.d(TAG, "success buy remove LastErrorOrderId");
                }

                Boolean bFirstBuyPoint = getdata.getBoolean(TaskKeyClass.firsttime_buy_point, false);
                if (bFirstBuyPoint) {
                    doWork();
                } else {
                    doFirstBuy();
                }

//                int startP = StringIntMethod.StringToInt(strPoints);
//                int endP = StringIntMethod.StringToInt(p30getPoints);
//
//                setIntChangeAnimation(startP, endP, 1500).start();
//
//                setLightAnimation();
//
//                getdata.edit().putString("point", p30getPoints).commit();
//
//                getdata.edit().putBoolean("datachange", true).commit();//2016.03.22新增 判斷是否已變更
//
//                setResult(REFRESHPOINT);
//
//                if (mHelper != null) {
//                    mHelper.dispose();
//                }
//                mHelper = null;
//
//                strPoints = p30getPoints;

//                                tvUserPoint.setText(p30getPoints);
            } else if (p30Result.equals("0")) {

            } else if (p30Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }
        }
    }

    private class FirstBuyPointTask extends AsyncTask<Void, Void, Object> {

        private String name;
        private String reward;
        private String reward_value;
        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoFirstBuy;
            loading.show();
        }


        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, SetMapByProtocol.setParam83_dotask(id, token, TaskKeyClass.firsttime_buy_point, "google"), null);
                MyLog.Set("d", getClass(), "p83strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p83Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = jsonObject.getString(Key.result);

                    if (p83Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);

                        JSONObject object = new JSONObject(jdata);

                        String task = object.getString(Key.task);
                        String event = object.getString(Key.event);

                        JSONObject taskObj = new JSONObject(task);
                        name = taskObj.getString(Key.name);
                        reward = taskObj.getString(Key.reward);
                        reward_value = taskObj.getString(Key.reward_value);

                        JSONObject eventObj = new JSONObject(event);
                        url = eventObj.getString(Key.url);

                    } else if (p83Result.equals("2")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("0")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else {
                        p83Result = "";
                    }
                } catch (Exception e) {
                    p83Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p83Result.equals("1")) {
                final DialogHandselPoint d = new DialogHandselPoint(mActivity);
                d.getTvTitle().setText(name);

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + " " + reward_value + " P !");
                    /**獲取當前使用者P點*/
                    String point = getdata.getString("point", "");
                    int p = StringIntMethod.StringToInt(point);

                    /**任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /**加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /**儲存data*/
                    getdata.edit().putString("point", newP).commit();

                    getdata.edit().putBoolean(TaskKeyClass.firsttime_buy_point, true).commit();

                    getdata.edit().putBoolean("datachange", true).commit();

                } else {
//                    d.getTvDescription().setText();
                }


                d.getTvLink().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        Intent intent = new Intent(mActivity, WebView2Activity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        ActivityAnim.StartAnim(mActivity);
                    }
                });

                d.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        doWork();
                    }
                });


            } else if (p83Result.equals("2")) {

                getdata.edit().putBoolean(TaskKeyClass.firsttime_buy_point, true).commit();

                doWork();

            } else if (p83Result.equals("0")) {

                MyLog.Set("d", getClass(), "p83Message" + p83Message);

                getdata.edit().putBoolean(TaskKeyClass.firsttime_buy_point, true).commit();

                doWork();




            } else if (p83Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }


        }
    }

    private void doWork() {

        int startP = StringIntMethod.StringToInt(strPoints);
        int endP = StringIntMethod.StringToInt(p30getPoints);

        setIntChangeAnimation(startP, endP, 1500).start();

//        setLightAnimation();

        getdata.edit().putString("point", p30getPoints).commit();

        getdata.edit().putBoolean("datachange", true).commit();//2016.03.22新增 判斷是否已變更

        setResult(REFRESHPOINT);

        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;

        strPoints = p30getPoints;


        /**2016.08.17 新增*/
        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();

        for (int i = 0; i < activityList.size(); i++) {

            String actName = activityList.get(i).getClass().getSimpleName();

            if (actName.equals("")) {



                break;

            }
        }


    }

   /* **********************************************************************************************************************/


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        MyLog.Set("d", getClass(), "onActivityResult handled by IABUtil.1");

        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            MyLog.Set("d", getClass(), "onActivityResult handled by IABUtil.2");
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            MyLog.Set("d", getClass(), "onActivityResult handled by IABUtil.");
            loading.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        ActivityAnim.FinishAnim(mActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {

            if (noConnect == null) {
                DialogSet d = new DialogSet(this);
                d.setNoConnect();
            }
        }
    }

    @Override
    public void onDestroy() {

        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;

        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        animator = null;

        if (getPointTask != null && !getPointTask.isCancelled()) {
            getPointTask.cancel(true);
            getPointTask = null;
        }

        if (getPointStoreTask != null && !getPointStoreTask.isCancelled()) {
            getPointStoreTask.cancel(true);
            getPointStoreTask = null;
        }

        if (getPayloadTask != null && !getPayloadTask.isCancelled()) {
            getPayloadTask.cancel(true);
            getPayloadTask = null;
        }

        if (buyCompleteTask != null && !buyCompleteTask.isCancelled()) {
            buyCompleteTask.cancel(true);
            buyCompleteTask = null;
        }

        if (firstBuyPointTask != null && !firstBuyPointTask.isCancelled()) {
            firstBuyPointTask.cancel(true);
            firstBuyPointTask = null;
        }

        super.onDestroy();

    }


}

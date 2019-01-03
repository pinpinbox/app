package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.PointAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemPoint;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
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

import in.srain.cube.views.GridViewWithHeaderAndFooter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/3/7.
 */
public class BuyPointActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private Inventory mInventory;
    private IabHelper mHelper;

    private List<ItemPoint> pointList;

    private GetPointTask getPointTask;
    private GetPointStoreTask getPointStoreTask;
    private GetPayloadTask getPayloadTask;
    private BuyCompleteTask buyCompleteTask;
    private FirstBuyPointTask firstBuyPointTask;

    private GridViewWithHeaderAndFooter gvPoint;
    private ImageView backImg;
    private TextView tvCurrentPoint, tvSum, tvConfirm;

    private String id, token;
    private String product_id;
    private String order_id = "";
    private String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAml6Hl7GI2+gQsoXuKfG+z2fpw2T8+v84WiQm16vuzEufKN4pZ3/ZQ0zAMSpZNf4BZbpWGUt8tj7wQaEayD7KFBoLZ3mhH9NLZD/6SbEKXbRjH0QxDcyBN/1E1d3iWR85Mi1w0I8y2+CnE6ZtIYbvvYTCEOTyjZv2ml9/h84sWoOQ/tNSaIEPOrtocLH0Q39IzZlsYrFj9d4DHZXBbMNaf3CpmkT9YF3+JSdnggAsKOI5gDx8Ebud+FGvtq+WPhGRIJGV0SZ4UtD4Z3K9r1kKxIfEvuOf4ERIK5WLP0Uj0QQgcA0P8P/qLYQgv1LLVIlwRn5rPFVTc88dOfDOqmactQIDAQAB";
    private String aa, bb, cc, dd, ee, ff, gg, hh, ii, jj = "NA";

    private int doingType;
    private int clickPosition = 0;
    public int requestCode = 177;

    public static boolean hasITEM = false;


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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_buy_point);
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        SystemUtility.SysApplication.getInstance().addActivity(this);

        init();

        doGetPoint();


    }


    private void init() {

        mActivity = this;

        pointList = new ArrayList<>();


        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();


        gvPoint = (GridViewWithHeaderAndFooter) findViewById(R.id.gvPoint);
        backImg = (ImageView) findViewById(R.id.backImg);


        View vHeader = LayoutInflater.from(mActivity).inflate(R.layout.header_2_0_0_point, null);
        View vfooter = LayoutInflater.from(mActivity).inflate(R.layout.footer_2_0_0_point, null);

        tvCurrentPoint = vHeader.findViewById(R.id.tvCurrentPoint);
        tvSum = vfooter.findViewById(R.id.tvSum);
        tvConfirm = vfooter.findViewById(R.id.tvConfirm);


        gvPoint.addHeaderView(vHeader, null, false);
        gvPoint.addFooterView(vfooter, null, false);

        backImg.setOnClickListener(this);


    }

    private void setPoint(String point) {
        tvCurrentPoint.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_current_point0) + point + getResources().getString(R.string.pinpinbox_2_0_0_other_text_current_point1));
    }

    private void back() {
        finish();
        ActivityAnim.FinishAnim(mActivity);
    }

    @SuppressLint("StaticFieldLeak")
    private void doWork() {

        if (mHelper != null) {
            mHelper.dispose();
        }


        new AsyncTask<Void, Void, Object>() {

            private int p23Result = -1;
            private String p23Message = "";
            private String point;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                startLoading();
            }


            @Override
            protected Object doInBackground(Void... voids) {
                Map<String, String> data = new HashMap<>();
                data.put(Key.id, id);
                data.put(Key.token, token);
                data.put(Key.platform, "google");
                String sign = IndexSheet.encodePPB(data);
                Map<String, String> sendData = new HashMap<String, String>();
                sendData.put(Key.id, id);
                sendData.put(Key.token, token);
                sendData.put(Key.platform, "google");
                sendData.put(Key.sign, sign);

                String strJson = "";

                try {
                    strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(id, token, "google"), null);
                    MyLog.Set("d", getClass(), "p23strJson => " + strJson);
                } catch (SocketTimeoutException timeout) {
                    p23Result = Key.TIMEOUT;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (strJson != null && !strJson.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(strJson);
                        p23Result = JsonUtility.GetInt(jsonObject, Key.result);
                        if (p23Result == 1) {

                            point = JsonUtility.GetString(jsonObject, Key.data);

                            PPBApplication.getInstance().getData().edit().putString(Key.point, point).commit();

                        } else if (p23Result == 0) {
                            p23Message = jsonObject.getString(Key.message);
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

                if (p23Result == 1) {

                    setPoint(PPBApplication.getInstance().getData().getString(Key.point, ""));

                    PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_buy_point_success_to_refresh);

                } else if (p23Result == 0) {
                    DialogV2Custom.BuildError(mActivity, p23Message);

                } else if (p23Result == Key.TIMEOUT) {
                    connectInstability();
                } else {

                    DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
                }


            }
        }.execute();


    }


    /*此為 msg.what = 0*/
   /* ******************************************************/
   /* 呼叫 google in appbilling*/
   /*loadFruitInventory() , purchaseFruit()*/
    private void callGoogleInAppBilling() {
//        mHelper = new IabHelper(mContext, base64EncodedPublicKey);
        mHelper = new IabHelper(mActivity.getApplicationContext(), key);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e("PPP", "Load error" + result.getMessage());
                } else {
                    loadFruitInventory();
                }
            }
        });
    }

    /* 獲取商品清單*/
   /* verifyDeveloperPayload()*/
    public void loadFruitInventory() {

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

                            if (!hasITEM) {
                                purchaseFruit(product_id);
                            }
                        } else {

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
                                            }
                                        }
                                    }
                            );
                        } else {
                            Log.i("Buy item", "Fail to buy it! " + result.getMessage());
                            //目前有出現之錯誤
                            //信用卡號遭拒
                            PPBApplication.getInstance().getData().edit().putString("LastErrorOrderId", order_id).commit();
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

                if (BuildConfig.DEBUG) {
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


    private void doGetPoint() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getPointTask = new GetPointTask();
        getPointTask.execute();
    }

    private void doGetPointStore() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getPointStoreTask = new GetPointStoreTask();
        getPointStoreTask.execute();
    }

    private void doGetPayLoad() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getPayloadTask = new GetPayloadTask();
        getPayloadTask.execute();
    }

    private void doComplete() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        buyCompleteTask = new BuyCompleteTask();
        buyCompleteTask.execute();
    }

    private void doFirstBuy() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        firstBuyPointTask = new FirstBuyPointTask();
        firstBuyPointTask.execute();
    }


    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }

                switch (doingType) {
                    case DoingTypeClass.DoDefault:
                        doGetPoint();
                        break;
                    case DoingTypeClass.DoGetPointStore:
                        doGetPointStore();
                        break;
                    case DoingTypeClass.DoGetPayLoad:
                        doGetPayLoad();
                        break;
                    case DoingTypeClass.DoComplete:
                        doComplete();
                        break;
                    case DoingTypeClass.DoFirstBuy:
                        doFirstBuy();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    @SuppressLint("StaticFieldLeak")
    private class GetPointTask extends AsyncTask<Void, Void, Object> {

        private int p23Result = -1;
        private String p23Message = "";
        private String point;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            Map<String, String> data = new HashMap<>();
            data.put(Key.id, id);
            data.put(Key.token, token);
            data.put(Key.platform, "google");
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, id);
            sendData.put(Key.token, token);
            sendData.put(Key.platform, "google");
            sendData.put(Key.sign, sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(id, token, "google"), null);
                MyLog.Set("d", getClass(), "p23strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p23Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p23Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p23Result == 1) {

                        point = JsonUtility.GetString(jsonObject, Key.data);

                    } else if (p23Result == 0) {
                        p23Message = jsonObject.getString(Key.message);
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

            if (p23Result == 1) {

                setPoint(point);

                doGetPointStore();

            } else if (p23Result == 0) {
                DialogV2Custom.BuildError(mActivity, p23Message);

            } else if (p23Result == Key.TIMEOUT) {
                connectInstability();
            } else {

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetPointStoreTask extends AsyncTask<Void, Void, Object> {

        private int p24Result = -1;
        private String p24Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetPointStore;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P24_GetPointStore, SetMapByProtocol.setParam24_getpointstore(id, token, "google", "TWD"), null);
                MyLog.Set("d", getClass(), "p24strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p24Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p24Result = JsonUtility.GetInt(jsonObject, Key.result);

                    if (p24Result == 1) {
                        String jdata = JsonUtility.GetString(jsonObject, Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);

                            String platform_flag = obj.getString("platform_flag");
                            String total = obj.getString("total");
                            String obtain = obj.getString("obtain");

                            ItemPoint itemPoint = new ItemPoint();

                            itemPoint.setPlatform_flag(platform_flag);
                            itemPoint.setTotal(total);
                            itemPoint.setObtain(obtain);
                            itemPoint.setSelect(false);

                            pointList.add(itemPoint);
                        }

                    } else if (p24Result == 0) {
                        p24Message = jsonObject.getString(Key.message);
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

            if (p24Result == 1) {

                final PointAdapter adapter = new PointAdapter(mActivity, pointList);
                gvPoint.setAdapter(adapter);

                /*default*/
                pointList.get(0).setSelect(true);
                adapter.notifyDataSetChanged();
                notifydata(0);

                gvPoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        for (int i = 0; i < pointList.size(); i++) {
                            if (i == position) {
                                pointList.get(i).setSelect(true);
                            } else {
                                pointList.get(i).setSelect(false);
                            }
                        }

                        adapter.notifyDataSetChanged();
                        notifydata(position);

                    }
                });


                tvConfirm.setOnClickListener((View.OnClickListener) mActivity);


            } else if (p24Result == 0) {
                DialogV2Custom.BuildError(mActivity, p24Message);
            } else if (p24Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }

        private void notifydata(int position) {

            int point = StringIntMethod.StringToInt(pointList.get(position).getObtain());
            MyLog.Set("d", mActivity.getClass(), "point => " + point);
            tvSum.setText("NT " + (int) (point / 1.4));

            clickPosition = position;

        }


    }

    /*獲取payload */
    /*send msg 0*/
    /* protocol29*/
    @SuppressLint("StaticFieldLeak")
    private class GetPayloadTask extends AsyncTask<Void, Void, Object> {


        private int p29Result = -1;
        private String p29Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetPayLoad;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P29_GetPayload, SetMapByProtocol.setParam29_getpayload(id, token, "google", product_id), null);
                MyLog.Set("d", getClass(), "strJson29 => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p29Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p29Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p29Result == 1) {
                        order_id = JsonUtility.GetString(jsonObject, Key.data);
                        MyLog.Set("e", getClass(), "order_id(payload) => " + order_id);
                    } else if (p29Result == 0) {
                        p29Message = JsonUtility.GetString(jsonObject, Key.message);
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

            if (p29Result == 1) {
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
            } else if (p29Result == 0) {
                DialogV2Custom.BuildError(mActivity, p29Message);
            } else if (p29Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }


    }

    /*此為 msg.what =  2 */
   /*完成購買*/
   /*protocol30*/
    @SuppressLint("StaticFieldLeak")
    private class BuyCompleteTask extends AsyncTask<Void, Void, Object> {

        private int p30Result = -1;
        private String p30Message = "";
        private String p30getPoints;

        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoComplete;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String dataSignature = "";
            String strJson = "";

            try {
                jsonObject = new JSONObject();
                jsonObject.put("itemType", ff);
                jsonObject.put("jsonPurchaseInfo", gg);
                jsonObject.put("signature", ii);




//                aa += pp.getSku(); **
//                bb += pp.getPackageName();
//                cc += pp.getDeveloperPayload(); **
//                dd += pp.getToken(); **
//                ee += pp.getOrderId(); **
//                ff += pp.getItemType(); **
//                gg += pp.getOriginalJson();**
//                hh += pp.getPurchaseTime();
//                ii += pp.getSignature(); **
//                jj += pp.getPurchaseState(); **


                //20171108
                jsonObject.put("orderId", ee);
                jsonObject.put("token", dd);
                jsonObject.put("sku", aa);
                jsonObject.put("developerPayload", cc);
                jsonObject.put("purchaseState", jj);




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
                    p30Result = Key.TIMEOUT;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p30Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p30Result == 1) {
                        p30getPoints = jsonObject.getString(Key.data);

//                        PPBApplication.getInstance().getData().edit().putString("point", p30getPoints).commit();

                    } else if (p30Result == 0) {
                        p30Message = jsonObject.getString(Key.message);
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

            if (p30Result == 1) {

                order_id = "";
                PPBApplication.getInstance().getData().edit().remove("LastErrorOrderId").commit();

                Boolean bFirstBuyPoint = PPBApplication.getInstance().getData().getBoolean(TaskKeyClass.firsttime_buy_point, false);
                if (bFirstBuyPoint) {
                    doWork();
                } else {
                    doFirstBuy();
                }

            } else if (p30Result == 0) {

                DialogV2Custom.BuildError(mActivity, p30Message);

            } else if (p30Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class FirstBuyPointTask extends AsyncTask<Void, Void, Object> {

        private String name;
        private String reward;
        private String reward_value;
        private String url;

        private int p83Result = -1;
        private String p83Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFirstBuy;
            startLoading();
        }


        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, SetMapByProtocol.setParam83_dotask(id, token, TaskKeyClass.firsttime_buy_point, "google"), null);
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
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result == 0) {
                        p83Message = jsonObject.getString(Key.message);
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
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");
                    /**獲取當前使用者P點*/
                    String point = PPBApplication.getInstance().getData().getString(Key.point, "");
                    int p = StringIntMethod.StringToInt(point);

                    /**任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /**加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /**儲存data*/
                    PPBApplication.getInstance().getData().edit().putString(Key.point, newP).commit();

                    PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.firsttime_buy_point, true).commit();

                    PPBApplication.getInstance().getData().edit().putBoolean("datachange", true).commit();

                } else {
//                    d.getTvDescription().setText();
                }

                if(url==null || url.equals("")|| url.equals("null")){
                    d.getTvLink().setVisibility(View.GONE);
                }else {
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
                        doWork();
                    }
                });

            } else if (p83Result == 2) {

                PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.firsttime_buy_point, true).commit();

                doWork();

            } else if (p83Result == 0) {

                MyLog.Set("d", getClass(), "p83Message" + p83Message);

                PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.firsttime_buy_point, true).commit();

                doWork();

            } else if (p83Result == Key.TIMEOUT) {

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

                DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setStyle(DialogStyleClass.CHECK);
                d.setMessage(getResources().getString(R.string.pinpinbox_2_0_0_other_message_confirm_buy) + pointList.get(clickPosition).getObtain() + "P:" + tvSum.getText().toString() + "?");
                d.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {

                        startLoading();

                        product_id = pointList.get(clickPosition).getPlatform_flag();

                        String lastOrderId = PPBApplication.getInstance().getData().getString("LastErrorOrderId", "");

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
                d.show();


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        MyLog.Set("d", getClass(), "onActivityResult handled by IABUtil.1");

        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            MyLog.Set("d", getClass(), "onActivityResult handled by IABUtil.2");
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            MyLog.Set("d", getClass(), "onActivityResult handled by IABUtil.");
            dissmissLoading();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onDestroy() {

        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;

        if (getPointTask != null && !getPointTask.isCancelled()) {
            getPointTask.cancel(true);
        }
        getPointTask = null;

        if (getPointStoreTask != null && !getPointStoreTask.isCancelled()) {
            getPointStoreTask.cancel(true);
        }
        getPointStoreTask = null;

        if (getPayloadTask != null && !getPayloadTask.isCancelled()) {
            getPayloadTask.cancel(true);
        }
        getPayloadTask = null;

        if (buyCompleteTask != null && !buyCompleteTask.isCancelled()) {
            buyCompleteTask.cancel(true);
        }
        buyCompleteTask = null;

        if (firstBuyPointTask != null && !firstBuyPointTask.isCancelled()) {
            firstBuyPointTask.cancel(true);
        }
        firstBuyPointTask = null;

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        System.gc();

        super.onDestroy();
    }


}

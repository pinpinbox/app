package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.HobbyAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemHobby;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.HobbyManager;
import com.squareup.picasso.Picasso;

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
 * Created by vmage on 2017/3/8.
 */
public class Hobby2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private NoConnect noConnect;
    private LoadingAnimation loading;
    private HobbyAdapter adapter;

    private SendHobbyTask sendHobbyTask;
    private GetHobbyTask getHobbyTask;

    //    private ArrayList<HashMap<String, Object>> hobbyList;
    private List<ItemHobby> itemHobbyList;
    private List<Integer> myHobbyList;

    private RelativeLayout rBottom;
    private GridViewWithHeaderAndFooter gvHobby;


    private int selectCount = 0;
    private int doingType;

    private boolean hideActionBar = false;




    @Override
    public void onCreate(Bundle savedInstanceState) {//06
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_hobby);
        setSwipeBackEnable(false);

        getBundle();

        init();

        getMyHobbyList();

        doGetHobby();

        selectHobby();

    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            hideActionBar = bundle.getBoolean("hideActionBar", false);


            if (hideActionBar) {
                findViewById(R.id.rActionBar).setVisibility(View.GONE);
            } else {
                findViewById(R.id.rActionBar).setVisibility(View.VISIBLE);
                setSwipeBackEnable(true);
            }


        }

    }

    private void init() {

        mActivity = this;

        loading = new LoadingAnimation(this);

        itemHobbyList = new ArrayList<>();
        myHobbyList = new ArrayList<>();

        rBottom = (RelativeLayout) findViewById(R.id.rBottom);
        gvHobby = (GridViewWithHeaderAndFooter) findViewById(R.id.gvHobby);
        TextView tvConfirm = (TextView) findViewById(R.id.tvConfirm);
        ImageView backImg = (ImageView) findViewById(R.id.backImg);


        View vHeader = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.header_2_0_0_title, null);
        gvHobby.addHeaderView(vHeader, null, false);


        TextView tvTitle = (TextView) vHeader.findViewById(R.id.tvTitle);
        TextView tvBelowTitleText = (TextView) vHeader.findViewById(R.id.tvBelowTitleText);
        tvBelowTitleText.setVisibility(View.VISIBLE);


        tvTitle.setText(R.string.pinpinbox_2_0_0_title_select_hobby);
        tvBelowTitleText.setText(R.string.pinpinbox_2_0_0_other_text_hobby_select_count);


        TextUtility.setBold(tvConfirm, true);
        TextUtility.setBold(tvTitle, true);

        tvConfirm.setOnClickListener(this);
        backImg.setOnClickListener(this);


        if (hideActionBar) {
           tvConfirm.setText(R.string.pinpinbox_2_0_0_other_text_select_done_start_use_pinpinbox);
        } else {


            if (SystemUtility.isTablet(getApplicationContext())) {

                //平版
                tvConfirm.setMinWidth(DensityUtility.dip2px(getApplicationContext(), 168));

            } else {

                //手機
                tvConfirm.setMinWidth(DensityUtility.dip2px(getApplicationContext(), 112));

            }




            tvConfirm.setText(R.string.pinpinbox_2_0_0_button_send);
        }


    }

    private void getMyHobbyList() {

        try {
            JSONArray jsonHobbyArray = new JSONArray(HobbyManager.GetHobbyList());


            MyLog.Set("e", getClass(), "aaaaaaaaa => " + jsonHobbyArray.toString());

            for (int i = 0; i < jsonHobbyArray.length(); i++) {

                int hobby_id = jsonHobbyArray.getInt(i);

                MyLog.Set("e", getClass(), "getMyHobbyList => " + hobby_id);

                myHobbyList.add(hobby_id);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void selectHobby() {


        gvHobby.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                boolean select = itemHobbyList.get(position).isSelect();
                if (!select) {

                    if (selectCount == 3) {

                        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_hobby_count_max);

                    } else {
                        itemHobbyList.get(position).setSelect(true);
                        selectCount++;
                    }


                } else {
                    itemHobbyList.get(position).setSelect(false);
                    selectCount--;
                }

                adapter.notifyDataSetChanged();


                if (selectCount == 0) {
                    rBottom.setVisibility(View.INVISIBLE);
                } else {
                    rBottom.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);

    }

    private String getSelects() {
        String s = "";
        for (int i = 0; i < itemHobbyList.size(); i++) {
            boolean select = itemHobbyList.get(i).isSelect();
            if (select) {

                String id = StringIntMethod.IntToString(itemHobbyList.get(i).getHobby_id());


                s = s + "," + id;


            }
        }
        s = s.substring(1);

        return s;
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {
                    case DoingTypeClass.DoDefault:
                        doGetHobby();
                        break;

                    case DoingTypeClass.DoSend:
                        doSendHobby();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doGetHobby() {

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        getHobbyTask = new GetHobbyTask();
        getHobbyTask.execute();


    }

    private void doSendHobby() {
        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }
        sendHobbyTask = new SendHobbyTask();
        sendHobbyTask.execute();
    }


    private class GetHobbyTask extends AsyncTask<Void, Void, Object> {

        private int p94Result = -1;
        private String p94Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            loading.show();


        }

        @Override
        protected Object doInBackground(Void... params) {

            String id = PPBApplication.getInstance().getId();
            String token = PPBApplication.getInstance().getToken();

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P94_GetHobbyList, SetMapByProtocol.setParam94_gethobbylist(id, token), null);//06
                MyLog.Set("d", getClass(), "p94strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p94Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p94Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p94Result == 1) {

                        String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONArray jsonArray = new JSONArray(data);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = (JSONObject) jsonArray.get(i);

                            String hobby = JsonUtility.GetString(object, ProtocolKey.hobby);

                            JSONObject jsonHobby = new JSONObject(hobby);

                            ItemHobby itemHobby = new ItemHobby();

                            itemHobby.setHobby_id(JsonUtility.GetInt(jsonHobby, ProtocolKey.hobby_id));
                            itemHobby.setName(JsonUtility.GetString(jsonHobby, ProtocolKey.name));
                            itemHobby.setImage_url(JsonUtility.GetString(jsonHobby, ProtocolKey.image_url));


                            for (int j = 0; j < myHobbyList.size(); j++) {

                                int hobbyId = myHobbyList.get(j);

                                if (itemHobby.getHobby_id() == hobbyId) {
                                    itemHobby.setSelect(true);
                                    selectCount++;

                                    break;
                                } else {
                                    itemHobby.setSelect(false);
                                }

                            }


//                            itemHobby.setSelect(false);
                            itemHobbyList.add(itemHobby);

                        }


                    } else if (p94Result == 0) {
                        p94Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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
            loading.dismiss();
            if (p94Result == 1) {

                adapter = new HobbyAdapter(mActivity, itemHobbyList);
                gvHobby.setAdapter(adapter);

                if(myHobbyList.size()>0){
                    rBottom.setVisibility(View.VISIBLE);
                }else {
                    rBottom.setVisibility(View.GONE);
                }


            } else if (p94Result == 0) {
                DialogV2Custom.BuildError(mActivity, p94Message);

            } else if (p94Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }


    private class SendHobbyTask extends AsyncTask<Void, Void, Object> {

        private int p06Result = -1;
        private String p06Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
            doingType = DoingTypeClass.DoSend;

        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", PPBApplication.getInstance().getId());
            data.put("token", PPBApplication.getInstance().getToken());
            data.put("hobby", getSelects());
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", PPBApplication.getInstance().getId());
            sendData.put("token", PPBApplication.getInstance().getToken());
            sendData.put("hobby", getSelects());
            sendData.put("sign", sign);
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P06_UpdateProfileHobby, sendData, null);//06
                MyLog.Set("d", getClass(), "p06strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p06Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p06Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p06Result == 0) {
                        p06Message = JsonUtility.GetString(jsonObject, Key.message);
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
            loading.dismiss();
            if (p06Result == 1) {

                HobbyManager.SaveHobbyList( "[" + getSelects() + "]");

                if (hideActionBar) {

                    Bundle bundle = new Bundle();
                    bundle.putBoolean("commonBackground", true);

                    Intent intent = new Intent(mActivity, Main2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

                } else {


                    finish();
                    ActivityAnim.FinishAnim(mActivity);


                }

            } else if (p06Result == 0) {
                DialogV2Custom.BuildError(mActivity, p06Message);

            } else if (p06Result == Key.TIMEOUT) {

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

        switch (view.getId()) {
            case R.id.tvConfirm:
                doSendHobby();
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
        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        if (hideActionBar) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_hobby_is_null);
        } else {
            back();
        }


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

        if (sendHobbyTask != null && !sendHobbyTask.isCancelled()) {
            sendHobbyTask.cancel(true);
        }
        sendHobbyTask = null;


        int count = itemHobbyList.size();
        for (int i = 0; i < count; i++) {
            String url = itemHobbyList.get(i).getImage_url();
            Picasso.with(getApplicationContext()).invalidate(url);
        }
        adapter = null;

        System.gc();

        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


}

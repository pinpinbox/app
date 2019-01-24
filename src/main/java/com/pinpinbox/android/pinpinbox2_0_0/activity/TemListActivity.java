package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.SampleTest.CreateAlbum.TemplateListAdapter;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemTemplate;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vmage on 2016/5/18.
 */
public class TemListActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private TemSubTask temSubTask;
    private BuyTemplateTask buyTemplateTask;

    private ImageView backImg;
    private TextView tvTitle;
    private GridView gvTemplate;


    private List<ItemTemplate> templateList;

    private String id, token;

    private String rank, style_id, style_name, event_id;
    private String clickTemplate_id;

    private String p36Result, p36Message;

    private int doingType;
    private static final int DoTemSub = 0;

    private boolean p36Own;
    private boolean isContribute = false;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_template_list);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundleContents();

        init();
        setTitle();

        doTemSub();


    }

    private void getBundleContents() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            rank = bundle.getString("rank", "");
            style_id = bundle.getString("style_id", "");
            style_name = bundle.getString("style_name", "");
            event_id = bundle.getString("event_id", "");
            isContribute = bundle.getBoolean(Key.isContribute, false);

        }
    }

    private void init() {

        mActivity = this;

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();


        templateList = new ArrayList<>();

        backImg = (ImageView) findViewById(R.id.backImg);
        tvTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        gvTemplate = (GridView) findViewById(R.id.gvTemplate);
        backImg.setOnClickListener(this);


    }

    private void setTitle() {

        if (isContribute) {
            /*從投稿畫面進來 顯示選擇版型*/

            tvTitle.setText(R.string.pinpinbox_2_0_0_title_choose_template);

        } else {

            switch (rank) {
                case "hot":
                    tvTitle.setText((getResources().getString(R.string.create_hot)) + " / " + style_name);
                    break;

                case "free":
                    tvTitle.setText((getResources().getString(R.string.create_free)) + " / " + style_name);
                    break;

                case "sponsored":
                    tvTitle.setText((getResources().getString(R.string.create_pay)) + " / " + style_name);
                    break;

                case "own":
                    tvTitle.setText((getResources().getString(R.string.create_own)) + " / " + style_name);
                    break;

            }

        }

    }


    private void setTemItem() {
        TemplateListAdapter templateListAdapter = new TemplateListAdapter(mActivity, templateList);
        gvTemplate.setAdapter(templateListAdapter);
        gvTemplate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }


                clickTemplate_id = templateList.get(position).getTemplate_id();


                doGetTemplate();


//                if (!templateList.get(position).isOwn()) {//如果沒擁有 詢問是否購買
//
//
//                    getTemplate();
//
//
//                } else {
//                    //有則 呼叫接口54 獲取album_id
//                    doGetAlbum();
//                }


//                Bundle bundle = new Bundle();
//                bundle.putString("template_id", templateList.get(position).getTemplate_id());
//
//                if (MODE == CONTRIBUTEMODE) {
//                    bundle.putInt("contributeMode", CONTRIBUTEMODE);
//                    bundle.putString("event_id", event_id);
//                }
//
//
//                Intent intent = new Intent(TemListActivity.this, TemplateInfoActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                if (MODE == CONTRIBUTEMODE) {
//                    finish();
//                }
//                ActivityAnim.StartAnim(mActivity);


            }
        });
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
                    case DoTemSub:
                        doTemSub();
                        break;

                    case DoingTypeClass.DoBuyTemplate:
                        doGetTemplate();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(this, connectInstability);

    }


    private void doGetTemplate() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        buyTemplateTask = new BuyTemplateTask();
        buyTemplateTask.execute();

    }


    private void doTemSub() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        temSubTask = new TemSubTask();
        temSubTask.execute();

    }

    private class TemSubTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();
            MyLog.Set("e", mActivity.getClass(), "****************" + this.getClass().getSimpleName());
        }

        @Override
        protected Object doInBackground(Void... params) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("rank", rank);
            data.put("limit", "0,100");
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("rank", rank);
            sendData.put("limit", "0,100");

            if (isContribute) {

                sendData.put("style_id", "");
                sendData.put("event_id", event_id);

            } else {

                sendData.put("style_id", style_id);
            }
            sendData.put("sign", sign);


            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P36_GetTemplateList, sendData, null);
                MyLog.Set("d", getClass(), "p36strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p36Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p36Result = jsonObject.getString(Key.result);
                    if (p36Result.equals("1")) {
                        String jdata = jsonObject.getString("data");
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            String template = obj.getString("template");
                            JSONObject obj_template = new JSONObject(template);
                            String description = obj_template.getString("description");
                            String image = obj_template.getString("image");
                            String name = obj_template.getString("name");
                            String point = obj_template.getString("point");
                            String template_id = obj_template.getString("template_id");
                            boolean own = obj_template.getBoolean("own");

                            String templatestatistics = obj.getString("templatestatistics");
                            JSONObject obj_templatestatistics = new JSONObject(templatestatistics);
                            String count = obj_templatestatistics.getString("count");

                            String user = obj.getString("user");
                            JSONObject obj_user = new JSONObject(user);
                            String username = obj_user.getString("name");


                            ItemTemplate itemTemplate = new ItemTemplate();
                            itemTemplate.setDescription(description);
                            itemTemplate.setImage(image);
                            itemTemplate.setName(name);
                            itemTemplate.setPoint(StringIntMethod.StringToInt(point));
                            itemTemplate.setTemplate_id(template_id);
                            itemTemplate.setCount(count);
                            itemTemplate.setOwn(own);
                            if (username == null || username.equals("null") || username.equals("")) {
                                itemTemplate.setUserName("");
                            } else {
                                itemTemplate.setUserName(username);
                            }

                            templateList.add(itemTemplate);

//                            HashMap<String, Object> map = new HashMap<String, Object>();
//                            map.put("description", description);
//                            map.put("image", image);
//                            map.put("name", name);
//                            map.put("point", point);
//                            map.put("template_id", template_id);
//                            map.put("count", count);
//                            map.put("own", own);
//
//                            if (username.equals("null")) {
//                                map.put("user", "");
//                            } else {
//                                map.put("user", username);
//                            }
//                            p36arraylist.add(map);

                        }
                    } else if (p36Result.equals("0")) {
                        p36Message = jsonObject.getString(ProtocolKey.message);
                    } else {
                        p36Result = "";
                    }
                } catch (Exception e) {
                    p36Result = "";
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p36Result.equals("1")) {

                if (templateList.size() == 0) {

                } else {
                    setTemItem();
                }

            } else if (p36Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p36Message);

            } else if (p36Result.equals(Key.timeout)) {

                connectInstability();

            } else {

                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());

            }


        }
    }

    private class BuyTemplateTask extends AsyncTask<Void, Void, Object> {

        private int p38Result = -1;
        private String p38Message = "";

        private int p54Result = -1;
        private String p54Message = "";

        private String newAlbum_id;

        private int protocol38() {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P38BuyTemplate, SetMapByProtocol.setParam38_buytemplate(id, token, clickTemplate_id, "google"), null);
                MyLog.Set("d", getClass(), "p38strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p38Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);

                    p38Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p38Result == 0) {

                        p38Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);

                    } else if (p38Result == 1 || p38Result == 2) {


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return p38Result;
        }

        private void protocol54() {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P54_InsertAlbumOfDiy, SetMapByProtocol.setParam54_insertalbumofdiy(id, token, clickTemplate_id), null);
                MyLog.Set("d", getClass(), "p54strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p54Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);

                    p54Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p54Result == 1) {
                        newAlbum_id = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    } else if (p54Result == 0) {
                        p54Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoBuyTemplate;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            if (protocol38() == 1 || protocol38() == 2) {

                protocol54();

            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (p38Result == 0) {
                DialogV2Custom.BuildError(mActivity, p38Message);
                return;
            }


            if (p54Result == 1) {

                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, newAlbum_id);
                bundle.putString(Key.identity, "admin");
                bundle.putInt(Key.create_mode, 1);
                bundle.putBoolean(Key.isNewCreate, true);

                if (isContribute) {//2016.05.31添加
                    bundle.putBoolean(Key.isContribute, true);
                    bundle.putString(Key.event_id, event_id);
                }

                Intent intent = new Intent(mActivity, CreationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                ActivityAnim.StartAnim(mActivity);

//                boolean bDownTem = getdata.getBoolean(TaskKeyClass.firsttime_download_template, false);
//                if (bDownTem) {
//                    startActivity(intent);
//                    if (MODE == CONTRIBUTEMODE) {//2016.05.31添加
//                        finish();
//                    }
//                    ActivityAnim.StartAnim(mActivity);
//
//                } else {
//                    doFirstDownTem();
//                }


            } else if (p54Result == 0) {

                DialogV2Custom.BuildError(mActivity, p54Message);

            } else if (p54Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backImg:
                back();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        back();
    }


    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (templateList != null && templateList.size() > 0) {

            int array = templateList.size();

            for (int i = 0; i < array; i++) {

                Picasso.with(mActivity.getApplicationContext()).invalidate(templateList.get(i).getImage());

            }
        }

        cancelTask(temSubTask);
        cancelTask(buyTemplateTask);

        Recycle.IMG(backImg);

        System.gc();

        super.onDestroy();
    }


}

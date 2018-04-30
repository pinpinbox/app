package com.pinpinbox.android.SampleTest.CreateAlbum;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.ControllableViewPager;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.UserGradeChange;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Creation2Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2016/4/17
 */
public class CreateAlbumActivity extends DraggerActivity {

    private Activity mActivity;
    private SharedPreferences getdata;
    private ActivityClick ac;
    private NoConnect noConnect;
    private LoadingAnimation loading;

    private FastCreateTask fastCreateTask;
    private LoadDataTask loadDataTask;
    private GetSubListTask getSubListTask;

    private TemplateHot templateHot;
    private TemplateFree templateFree;
    private TemplateSponsored templateSponsored;
    private TemplateOwn templateOwn;

    private ArrayList<HashMap<String, Object>> positionList;
    private ArrayList<HashMap<String, Object>> subList;

    private String id, token;
    private String SDpath, myDir;

    private String p36Result, p36Message;
    private String p48Result, p48Message;
    private String p54Result, p54Message;
    private String new_album_id;


    private String strHotJson, strFreeJson, strSponsoredJson, strOwnJson;

    private boolean openSubmenu;
    private boolean isTimeOut = false;

    private int intUserGrade;
    private int doingType;
    private static final int DoFastCreate = 0;
    private static final int DoLoadData = 1;
    private static final int DoGetSubList = 2;

    private ControllableViewPager viewPager;
    private RelativeLayout rCreatealbumSublist;

    private ImageView sublistImg;
    private ImageView backImg;



    private FragmentPagerItemAdapter adapter;

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


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_album);

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                SystemBarTintManager tintManager = new SystemBarTintManager(CreateAlbumActivity.this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.status_color_default);
//            }
//        });


        init();

        doGetSubList();


//        LoadDataTask loadDataTask = new LoadDataTask();
//        loadDataTask.execute();

        backImg.setOnClickListener(ac);

    }

    private void init() {

        mActivity = this;
        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        ac = new ActivityClick();
        loading = new LoadingAnimation(mActivity);

        /**********************************************************************************************/

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        w = dm.widthPixels;
//        h = dm.heightPixels;

        /**********************************************************************************************/

        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");
        myDir = "PinPinBox" + id + "/";
        SDpath = Environment.getExternalStorageDirectory() + "/";
        String ug = getdata.getString(Key.usergrade, "");
        String strUserGrade = UserGradeChange.PicCountByUserGrade(ug);
        intUserGrade = StringIntMethod.StringToInt(strUserGrade);
        openSubmenu = false;//defaul close

        /**********************************************************************************************/

        positionList = new ArrayList<>();
        subList = new ArrayList<>();

        /**********************************************************************************************/


//        viewPager = (ControllableViewPager) findViewById(R.id.createalbum_viewpager);
//        viewPager.setOffscreenPageLimit(3);
//        rCreatealbumSublist = (RelativeLayout) findViewById(R.id.createalbum_sublist);
//
//        sublistImg = (ImageView) findViewById(R.id.createalbum_sublistImg);
        backImg = (ImageView) findViewById(R.id.backImg);




    }

    public List<Fragment> fragmentList() {
        return getSupportFragmentManager().getFragments();
    }

    private void clickSublist() {

        if (!openSubmenu) {
            //打開 子列表

            openSubmenu = true;


            for (int i = 0; i < fragmentList().size(); i++) {

                Fragment fragment = fragmentList().get(i);


                String strFragmentName = fragment.getClass().getSimpleName();

                switch (strFragmentName) {

                    case "TemplateHot":
                        ((TemplateHot) fragment).setSubListVisibility();
                        break;

                    case "TemplateFree":
                        ((TemplateFree) fragment).setSubListVisibility();
                        break;

                    case "TemplateSponsored":
                        ((TemplateSponsored) fragment).setSubListVisibility();
                        break;

                    case "TemplateOwn":
                        ((TemplateOwn) fragment).setSubListVisibility();
                        break;
                }

            }

        } else {

            //關閉 子列表

            openSubmenu = false;

            for (int i = 0; i < adapter.getCount(); i++) {

                Fragment fragment = fragmentList().get(i);

                String strFragmentName = fragment.getClass().getSimpleName();

                switch (strFragmentName) {

                    case "TemplateHot":
                        ((TemplateHot) fragment).setSubListInVisibility();
                        break;

                    case "TemplateFree":
                        ((TemplateFree) fragment).setSubListInVisibility();
                        break;

                    case "TemplateSponsored":
                        ((TemplateSponsored) fragment).setSubListInVisibility();
                        break;

                    case "TemplateOwn":
                        ((TemplateOwn) fragment).setSubListInVisibility();
                        break;
                }

            }


        }
    }

    private void click_fast() {

    }

    private String getJson(String rank) {

        String strJson = "";

        Map<String, String> data = new HashMap<String, String>();
        data.put("id", id);
        data.put("token", token);
        data.put("rank", rank);
        data.put("limit", "0,24");
        String sign = IndexSheet.encodePPB(data);
        Map<String, String> sendData = new HashMap<String, String>();
        sendData.put("id", id);
        sendData.put("token", token);
        sendData.put("rank", rank);
        sendData.put("limit", "0,24");
        sendData.put("style_id", "");//不加入sign計算
        sendData.put("sign", sign);

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P36_GetTemplateList, sendData, null);
        } catch (SocketTimeoutException timeout) {
            isTimeOut = true;
            p36Result = Key.timeout;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strJson;

    }

    private void cleanAllPicasso() {

        if (templateHot != null) {
            templateHot.cleanPicasso();
            templateHot.cleanAdpater();
        }

        if (templateFree != null) {
            templateFree.cleanPicasso();
            templateFree.cleanAdpater();
        }

        if (templateSponsored != null) {
            templateSponsored.cleanPicasso();
            templateSponsored.cleanAdpater();
        }

        if (templateOwn != null) {
            templateOwn.cleanPicasso();
            templateOwn.cleanAdpater();
        }
        System.gc();
    }

    public boolean getOpenSub() {
        return this.openSubmenu;
    }

    public String getStrHotJson() {
        return this.strHotJson;
    }

    public String getStrFreeJson() {
        return this.strFreeJson;
    }

    public String getStrSponsoredJson() {
        return this.strSponsoredJson;
    }

    public String getStrOwnJson() {
        return this.strOwnJson;
    }

    public ArrayList<HashMap<String, Object>> getSublist() {
        return subList;
    }

    private void deleteAllEdit() {
        FileUtility.delAllFile(SDpath + myDir + "copy/");
        FileUtility.delAllFile(SDpath + myDir + "readySend/");
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoFastCreate:
                        doFastCreate();
                        break;

                    case DoLoadData:
                        doLoadData();
                        break;

                    case DoGetSubList:
                        doGetSubList();
                        break;
                }
            }
        };



    }

    private void doFastCreate() {

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        fastCreateTask = new FastCreateTask();
        fastCreateTask.execute();
    }

    private void doLoadData() {

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        loadDataTask = new LoadDataTask();
        loadDataTask.execute();
    }

    private void doGetSubList() {

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        getSubListTask = new GetSubListTask();
        getSubListTask.execute();
    }

    private class ActivityClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                return;
            }
            if (HttpUtility.isConnect(mActivity)) {
                switch (v.getId()) {
//                    case R.id.createalbum_sublist:
//                        clickSublist();
//                        break;
                    case R.id.backImg:
                        cleanAllPicasso();
                        finish();
                        ActivityAnim.FinishAnim(mActivity);
                        break;
                }
            } else {
                noConnect = new NoConnect(mActivity);
            }
        }
    }

    private class FastCreateTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoFastCreate;
            loading.show();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P54_InsertAlbumOfDiy,
                        SetMapByProtocol.setParam54_insertalbumofdiy(id, token, "0"), null);
                MyLog.Set("d", getClass(), "p54strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p54Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p54Result = jsonObject.getString(Key.result);
                    if (p54Result.equals("1")) {
                        new_album_id = jsonObject.getString(Key.data);
                    } else if (p54Result.equals("0")) {
                        p54Message = jsonObject.getString(Key.message);
                    } else {
                        p54Result = "";
                    }

                } catch (Exception e) {
                    p54Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p54Result.equals("1")) {
                if (new_album_id != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("album_id", new_album_id);
                    bundle.putString("identity", "admin");
                    bundle.putInt("create_mode", 0);
                    Intent intent = new Intent(CreateAlbumActivity.this, Creation2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    ActivityAnim.StartAnim(mActivity);
                }
            } else if (p54Result.equals("0")) {


            } else if (p54Result.equals(Key.timeout)) {

                connectInstability();

            } else {

            }


        }
    }

    private class GetSubListTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetSubList;
            loading.show();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P48_GetTemplateStyleList, SetMapByProtocol.setParam48_gettemplatestylelist(id, token), null);
                MyLog.Set("d", mActivity.getClass(), "p48strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p48Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p48Result = jsonObject.getString(Key.result);
                    if (p48Result.equals("1")) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            String style_id = obj.getString("style_id");
                            String name = obj.getString("name");

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("style_id", style_id);
                            map.put("name", name);
                            subList.add(map);
                        }
                    } else if (p48Result.equals("0")) {
                        p48Message = jsonObject.getString(Key.message);
                    } else {
                        p48Result = "";
                    }
                } catch (Exception e) {
                    p48Result = "";
                    e.printStackTrace();
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p48Result.equals(Key.timeout)) {
                connectInstability();
                return;
            }


            if (p48Result.equals("1")) {

                rCreatealbumSublist.setOnClickListener(ac);

            } else if (p48Result.equals("0")) {



            } else {

            }

            doLoadData();


        }
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoLoadData;
            loading.show();
            isTimeOut = false;
        }

        @Override
        protected Object doInBackground(Void... params) {

            strHotJson = getJson("hot");
            strFreeJson = getJson("free");
            strSponsoredJson = getJson("sponsored");
            strOwnJson = getJson("own");


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (isTimeOut) {
                connectInstability();
                return;
            }

            templateHot = new TemplateHot();
            templateFree = new TemplateFree();
            templateSponsored = new TemplateSponsored();
            templateOwn = new TemplateOwn();

            adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), FragmentPagerItems.with(mActivity)
                    .add(R.string.create_own, TemplateOwn.class)
                    .add(R.string.create_hot, TemplateHot.class)
                    .add(R.string.create_free, TemplateFree.class)
                    .add(R.string.create_pay, TemplateSponsored.class)
                    .create());

            viewPager.setAdapter(adapter);

//            SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
//            viewPagerTab.setViewPager(viewPager);

            rCreatealbumSublist.setOnClickListener(ac);
            click_fast();

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {

        if (templateHot != null && templateHot.isAdded()) {
            templateHot.cleanPicasso();
            templateHot.cleanAdpater();
        }

        if (templateFree != null && templateFree.isAdded()) {
            templateFree.cleanPicasso();
            templateFree.cleanAdpater();
        }

        if (templateSponsored != null && templateSponsored.isAdded()) {
            templateSponsored.cleanPicasso();
            templateSponsored.cleanAdpater();
        }

        if (templateOwn != null && templateOwn.isAdded()) {
            templateOwn.cleanPicasso();
            templateOwn.cleanAdpater();
        }
        System.gc();
        finish();
        ActivityAnim.FinishAnim(mActivity);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(mActivity)) {
            if (noConnect == null) {

            }

        }
    }

    @Override
    protected void onPause() {

        if (templateHot != null && templateHot.isAdded()) {
            templateHot.cleanPicasso();
        }

        if (templateFree != null && templateFree.isAdded()) {
            templateFree.cleanPicasso();
        }

        if (templateSponsored != null && templateSponsored.isAdded()) {
            templateSponsored.cleanPicasso();
        }

        if (templateOwn != null && templateOwn.isAdded()) {
            templateOwn.cleanPicasso();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (fastCreateTask != null && !fastCreateTask.isCancelled()) {
            fastCreateTask.cancel(true);
            fastCreateTask = null;
        }


        Recycle.IMG(backImg);

        Recycle.IMG(sublistImg);

        templateHot = null;
        templateFree = null;
        templateSponsored = null;
        templateOwn = null;

        System.gc();

        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }

}

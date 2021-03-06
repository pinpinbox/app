package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.ControllableViewPager;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentTemFree2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentTemHot2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentTemOwn2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentTemSponsored2;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupList;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vmage on 2017/1/17.
 */
public class CreateAlbumActivity extends DraggerActivity {

    private Activity mActivity;
    private NoConnect noConnect;
    private PopupList popupList;
    private LoadingAnimation loading;

    private FragmentTemOwn2 fragmentTemOwn2;
    private FragmentTemHot2 fragmentTemHot2;
    private FragmentTemFree2 fragmentTemFree2;
    private FragmentTemSponsored2 fragmentTemSponsored2;

    private GetSubListTask getSubListTask;

    private List<TextView> tabs;
    private ArrayList<HashMap<String, Object>> subList;

    private SmartTabLayout viewPagerTab;
    private ControllableViewPager viewPager;
    private TextView tvSubList;
    private ImageView backImg;
    private RelativeLayout rBackground;
    private RelativeLayout rActionBar;

    private String id, token;
    private String style_id = "";

    private int doingType;

    public void setNoConnect(NoConnect noConnect) {
        this.noConnect = noConnect;
    }

    public String getStyle_id() {
        return this.style_id;
    }

    public LoadingAnimation getLoading() {
        return this.loading;
    }

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
        setContentView(R.layout.activity_2_0_0_createalbum);

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                SystemBarTintManager sysBarManager = new SystemBarTintManager(CreateAlbumActivity.this);
                sysBarManager.setStatusBarTintEnabled(true);
                sysBarManager.setStatusBarTintColor(Color.parseColor(ColorClass.STATUSBAR));
            }
        });

        init();
        doGetSubList();

    }

    private void init() {

        mActivity = this;

        loading = new LoadingAnimation(this);

        tabs = new ArrayList<>();
        subList = new ArrayList<>();

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        viewPager = (ControllableViewPager) findViewById(R.id.viewPager);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewPagerTab);
        tvSubList = (TextView) findViewById(R.id.tvSubList);
        backImg = (ImageView) findViewById(R.id.backImg);
        rBackground = (RelativeLayout) findViewById(R.id.rBackground);
        rActionBar = (RelativeLayout) findViewById(R.id.rActionBar);


        viewPager.setOffscreenPageLimit(4);
    }

    private void setFragment() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(mActivity)
                .add("", FragmentTemOwn2.class)
                .add("", FragmentTemHot2.class)
                .add("", FragmentTemFree2.class)
                .add("", FragmentTemSponsored2.class)
                .create());

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
    }



    private void setPageListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private void setBold(TextView tv, boolean isBold) {
                TextPaint tp = tv.getPaint();
                tp.setFakeBoldText(isBold);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        tabs.get(0).setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                        setBold(tabs.get(0), true);
                        tabs.get(1).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(1), false);
                        tabs.get(2).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(2), false);
                        tabs.get(3).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(3), false);
                        break;
                    case 1:
                        tabs.get(0).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(0), false);
                        tabs.get(1).setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                        setBold(tabs.get(1), true);
                        tabs.get(2).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(2), false);
                        tabs.get(3).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(3), false);
                        break;
                    case 2:
                        tabs.get(0).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(0), false);
                        tabs.get(1).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(1), false);
                        tabs.get(2).setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                        setBold(tabs.get(2), true);
                        tabs.get(3).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(3), false);
                        break;
                    case 3:
                        tabs.get(0).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(0), false);
                        tabs.get(1).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(1), false);
                        tabs.get(2).setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        setBold(tabs.get(2), false);
                        tabs.get(3).setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                        setBold(tabs.get(3), true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void selectSubList() {

        if (popupList == null) {

//            popupList = new PopupList(mActivity);
//            popupList.setPopup();
//            popupList.setTitle(R.string.pinpinbox_2_0_0_pop_title_choose_category);
//
//            popupList.getListView().setAdapter(new ReportListAdapter(mActivity, subList));
//
//            popupList.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    popupList.dismiss();
//
//                    MyLog.Set("d", mActivity.getClass(), (String) subList.get(position).get(Key.name));
//
//                    tvSubList.setText((String) subList.get(position).get(Key.name));
//
//                    style_id = (String) subList.get(position).get(Key.style_id);
//
//                    refreshFragment();
//                }
//            });

        }

        popupList.show(rBackground);
    }



    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
//                    case DoFastCreate:
//                        doFastCreate();
//                        break;
//
//                    case DoLoadData:
//                        doLoadData();
//                        break;

                    case DoingTypeClass.DoGetSubList:
                        doGetSubList();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    private void doGetSubList() {

        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }

        getSubListTask = new GetSubListTask();
        getSubListTask.execute();
    }

    private class ClickControl implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tvSubList:

                    selectSubList();

                    break;

                case R.id.backImg:
                    finish();
                    ActivityAnim.FinishAnim(mActivity);
                    break;

                case R.id.rActionBar:


                    int intPage = viewPager.getCurrentItem();

                    switch (intPage){
                        case 0: //own
                            fragmentTemOwn2.scrollToTop();
                            break;

                        case 1: //hot
                            fragmentTemHot2.scrollToTop();
                            break;

                        case 2: //free
                            fragmentTemFree2.scrollToTop();
                            break;

                        case 3: //Sponsored
                            fragmentTemSponsored2.scrollToTop();
                            break;
                    }

                    break;

            }


        }
    }

    private class GetSubListTask extends AsyncTask<Void, Void, Object> {

        private int p48Result = -1;
        private String p48Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetSubList;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P48_GetTemplateStyleList, SetMapByProtocol.setParam48_gettemplatestylelist(id, token), null);
                MyLog.Set("d", mActivity.getClass(), "p48strJson =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p48Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p48Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p48Result == 1) {

                        HashMap<String, Object> defaultMap = new HashMap<>();
                        defaultMap.put(Key.style_id, "");
                        defaultMap.put(Key.name, "全 部");
                        subList.add(defaultMap);


                        String jdata = JsonUtility.GetString(jsonObject, Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            String style_id = JsonUtility.GetString(obj, Key.style_id);
                            String name = JsonUtility.GetString(obj, Key.name);

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put(Key.style_id, style_id);
                            map.put(Key.name, name);
                            subList.add(map);
                        }
                    } else if (p48Result == 0) {
                        p48Message = jsonObject.getString(Key.message);
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

            if (p48Result == 1) {

                setFragment();

                setPageListener();

                ClickControl cc = new ClickControl();
                tvSubList.setOnClickListener(cc);
                backImg.setOnClickListener(cc);
                rActionBar.setOnClickListener(cc);


            } else if (p48Result == 0) {

                DialogV2Custom.BuildError(mActivity, p48Message);

            } else if (p48Result == Key.TIMEOUT) {
                connectInstability();
            } else {

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(mActivity)) {
            if (noConnect == null) {
                noConnect = new NoConnect(mActivity);
            }

        }
    }

    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (getSubListTask != null && !getSubListTask.isCancelled()) {
            getSubListTask.cancel(true);
        }
        getSubListTask = null;

        Recycle.IMG(backImg);


        System.gc();

        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }

}

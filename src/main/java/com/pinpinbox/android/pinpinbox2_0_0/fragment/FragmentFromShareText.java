package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.FromSharePhoto2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol80_InsertVideoOfDiy;

import org.json.JSONObject;

import java.net.SocketTimeoutException;

public class FragmentFromShareText extends Fragment implements View.OnClickListener {


    private Protocol80_InsertVideoOfDiy protocol80_insertVideoOfDiy;
    private FastCreateTask fastCreateTask;

    private FragmentPagerItemAdapter adapter;

    private ItemAlbum itemAlbum;

    private ImageView backImg;
    private TextView tvTabMy, tvTabShare, tvNewCreate, tvSend;
    private ViewPager viewPager;

    private String text = "";

    private boolean isNewCreate = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            text = bundle.getString(Key.text, "");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_fromsharetext, container, false);

        backImg = v.findViewById(R.id.backImg);
        tvTabMy = v.findViewById(R.id.tvTabMy);
        tvTabShare = v.findViewById(R.id.tvTabShare);
        tvNewCreate = v.findViewById(R.id.tvNewCreate);
        tvSend = v.findViewById(R.id.tvSend);
        viewPager = v.findViewById(R.id.viewPager);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        setFragment();

        setPageListener();

    }

    private void init() {

        //default
        pageMyType();

        backImg.setOnClickListener(this);
        tvTabMy.setOnClickListener(this);
        tvTabShare.setOnClickListener(this);
        tvNewCreate.setOnClickListener(this);
        tvSend.setOnClickListener(this);

    }

    private void pageMyType() {

        tvTabMy.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        TextUtility.setBold(tvTabMy, true);

        tvTabShare.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        TextUtility.setBold(tvTabShare, false);

    }

    private void pageShareType() {


        tvTabMy.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        TextUtility.setBold(tvTabMy, false);

        tvTabShare.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        TextUtility.setBold(tvTabShare, true);


    }

    private void setFragment() {

        Bundle bundleMine = new Bundle();
        bundleMine.putString("rank", FragmentSelectAlbum.MINE);

        Bundle bundleShare = new Bundle();
        bundleShare.putString("rank", FragmentSelectAlbum.COOPERATION);


        adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("", FragmentSelectAlbum.class, bundleMine)
                .add("", FragmentSelectAlbum.class, bundleShare)
                .create());

        viewPager.setAdapter(adapter);

    }

    private void setPageListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {

                    case 0:
                        pageMyType();
                        break;

                    case 1:
                        pageShareType();
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void doSendSharedToAlbum() {

        if (!HttpUtility.isConnect(getActivity())) {
            ((FromSharePhoto2Activity) getActivity()).setNoConnect();
            return;
        }

        protocol80_insertVideoOfDiy = new Protocol80_InsertVideoOfDiy(
                getActivity(),
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                itemAlbum.getAlbum_id(),
                "embed",
                text,
                new Protocol80_InsertVideoOfDiy.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        ((FromSharePhoto2Activity) getActivity()).startLoading();
                    }

                    @Override
                    public void Post() {
                        ((FromSharePhoto2Activity) getActivity()).dissmissLoading();
                    }

                    @Override
                    public void Success() {

                        ActivityIntent.toCreation(getActivity(), itemAlbum.getAlbum_id(), itemAlbum.getIdentity(), itemAlbum.getTemplate_id(), isNewCreate, true);
                        getActivity().finish();

                    }

                    @Override
                    public void TimeOut() {
                        doSendSharedToAlbum();
                    }
                }

        );


    }

    private void doFastCreate() {

        if (!HttpUtility.isConnect(getActivity())) {
            ((FromSharePhoto2Activity) getActivity()).setNoConnect();
            return;
        }

        fastCreateTask = new FastCreateTask();
        fastCreateTask.execute();

    }


    public void setItemAlbum(ItemAlbum itemAlbum) {
        this.itemAlbum = itemAlbum;
    }

    public void clearOtherPageItem(int otherPage) {

        FragmentSelectAlbum fragmentSelectAlbum = (FragmentSelectAlbum) adapter.getPage(otherPage);
        fragmentSelectAlbum.resetItem();

    }


    private void connectInstability() {
        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                doFastCreate();
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);
    }


    @SuppressLint("StaticFieldLeak")
    public class FastCreateTask extends AsyncTask<Void, Void, Object> {

        private int p54Result = -1;
        private String p54Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ((FromSharePhoto2Activity) getActivity()).startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {

                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P54_InsertAlbumOfDiy,
                        SetMapByProtocol.setParam54_insertalbumofdiy(PPBApplication.getInstance().getId(), PPBApplication.getInstance().getToken(), "0"), null);
                MyLog.Set("json", getClass(), strJson);
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

                        itemAlbum = new ItemAlbum();

                        itemAlbum.setAlbum_id(JsonUtility.GetString(jsonObject, ProtocolKey.data));
                        itemAlbum.setIdentity("admin");
                        itemAlbum.setTemplate_id(0);

                    } else if (p54Result == 0) {
                        p54Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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
            ((FromSharePhoto2Activity) getActivity()).dissmissLoading();

            if (p54Result == 1) {

                isNewCreate = true;

                doSendSharedToAlbum();

            } else if (p54Result == 0) {

                DialogV2Custom.BuildError(getActivity(), p54Message);

            } else if (p54Result == Key.TIMEOUT) {

                connectInstability();

            } else {

                DialogV2Custom.BuildUnKnow(getActivity(), this.getClass().getSimpleName());

            }

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.backImg:

                getActivity().finish();

                break;

            case R.id.tvTabMy:

                viewPager.setCurrentItem(0);

                break;

            case R.id.tvTabShare:

                viewPager.setCurrentItem(1);

                break;

            case R.id.tvNewCreate:

                doFastCreate();

                break;

            case R.id.tvSend:

                if (itemAlbum == null) {
                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_choose_a_work);
                    return;
                }

                doSendSharedToAlbum();

                break;

        }

    }

    @Override
    public void onDestroy() {

        if (protocol80_insertVideoOfDiy != null && !protocol80_insertVideoOfDiy.isCancelled()) {
            protocol80_insertVideoOfDiy.cancel(true);
        }
        protocol80_insertVideoOfDiy = null;

        if (fastCreateTask != null && !fastCreateTask.isCancelled()) {
            fastCreateTask.cancel(true);
        }
        fastCreateTask = null;


        super.onDestroy();
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CategoryBookCaseActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MainActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCategoryAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumCategory;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.JsonParamTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.BannerSize;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.ResultType;
import com.pinpinbox.android.pinpinbox2_0_0.protocol.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class FragmentCategory extends Fragment implements View.OnClickListener {

    private LoadingAnimation loading;

    private GetCategoryTask getCategoryTask;

    private RecyclerCategoryAdapter adapter;

    private List<ItemAlbumCategory> itemAlbumCategoryList;

    private ItemAlbumCategory italbumTheme;

    private RecyclerView rvCategory;


    /**
     * header
     */
    private View viewHeader;
    private RoundCornerImageView cgBannerImg;
    private String strThemeData = "";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_2_0_0_category, container, false);

        initView(v);

        initHeaderView();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();

        setRecycler();

        doGetCategory();

    }


    private void initView(View v) {

        rvCategory = v.findViewById(R.id.rvCategory);



            rvCategory.addItemDecoration(new SpacesItemDecoration(16, true, true));


        rvCategory.setItemAnimator(new DefaultItemAnimator());

    }

    private void initHeaderView() {

        viewHeader = getActivity().getLayoutInflater().inflate(R.layout.header_2_0_0_category, null);

        cgBannerImg = viewHeader.findViewById(R.id.cgBannerImg);

        cgBannerImg.setOnClickListener(this);

    }

    private void init() {

        loading = ((MainActivity) getActivity()).getLoading();

        itemAlbumCategoryList = new ArrayList<>();

    }

    private void setRecycler() {

        adapter = new RecyclerCategoryAdapter(getActivity(), itemAlbumCategoryList);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvCategory.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        ExStaggeredGridLayoutManager manager = null;

        if (PPBApplication.getInstance().isPhone()) {
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }

        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvCategory.getAdapter(), manager.getSpanCount()));
        rvCategory.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvCategory, viewHeader);


        adapter.setOnRecyclerViewListener(new RecyclerCategoryAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                ActivityIntent.toFeature(getActivity(), itemAlbumCategoryList.get(position).getCategoryarea_id());

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                doGetCategory();

            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);

    }

    private void doGetCategory() {

        if (!HttpUtility.isConnect(getActivity())) {
            ((MainActivity) getActivity()).setNoConnect();
            return;
        }

        getCategoryTask = new GetCategoryTask();
        getCategoryTask.execute();

    }


    @SuppressLint("StaticFieldLeak")
    public class GetCategoryTask extends AsyncTask<Void, Void, Object> {

        private String p103Result, p103Message;

        private String p09Message;

        private int p09Result;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            getCategoryList();

            getThemeList();

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            try {

                if (p09Result == 1) {

//                    int bannerWidth = ScreenUtils.getScreenWidth();
//
//                    int bannerHeight = (bannerWidth * 540) / 960;
//
//                    cgBannerImg.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), bannerHeight));

                    BannerSize.set(cgBannerImg, 32);


                    adapter.notifyDataSetChanged();

                } else if (p09Result == 0) {

                    loading.dismiss();

                    DialogV2Custom.BuildError(getActivity(), p09Message);

                    return;

                } else if (p09Result == Key.TIMEOUT) {

                    loading.dismiss();

                    connectInstability();

                    return;

                } else {

                    loading.dismiss();

                    DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());

                }

            } catch (Exception e) {

                e.printStackTrace();

            }



            try{

                if(p103Result.equals(ResultType.SYSTEM_OK)){

                    ImageUtility.setCommonImage(getActivity(), cgBannerImg, italbumTheme.getImage_360x360());

                }else {

                    cgBannerImg.setVisibility(View.GONE);

                }

            }catch (Exception e){
                e.printStackTrace();
            }




        }

        private void getCategoryList() {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P09_RetrieveCategoryList,
                        SetMapByProtocol.setParam09_retrievecatgeorylist(PPBApplication.getInstance().getId(), PPBApplication.getInstance().getToken()),
                        null);
                MyLog.Set("json", getClass(), strJson);
            } catch (SocketTimeoutException timeout) {
                p09Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p09Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p09Result == 1) {

                        String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONArray jsonArray = new JSONArray(data);

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = (JSONObject) jsonArray.get(i);

                            String categoryarea = JsonUtility.GetString(object, ProtocolKey.categoryarea);
                            JSONObject jsonCategoryarea = new JSONObject(categoryarea);

                            ItemAlbumCategory itemAlbumCategory = new ItemAlbumCategory();
                            itemAlbumCategory.setCategoryarea_id(JsonUtility.GetInt(jsonCategoryarea, ProtocolKey.categoryarea_id));
                            itemAlbumCategory.setName(JsonUtility.GetString(jsonCategoryarea, ProtocolKey.name));
                            itemAlbumCategory.setColorhex(JsonUtility.GetString(jsonCategoryarea, ProtocolKey.colorhex));
                            itemAlbumCategory.setImage_360x360(JsonUtility.GetString(jsonCategoryarea, ProtocolKey.image_360x360));

                            itemAlbumCategoryList.add(itemAlbumCategory);
                        }


                    } else if (p09Result == 0) {
                        p09Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        private void getThemeList() {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, Url.P103_GetThemeArea,
                        SetMapByProtocol.setParam103_getthemearea(PPBApplication.getInstance().getId(), PPBApplication.getInstance().getToken()),
                        null);
                MyLog.Set("json", getClass(), strJson);
            } catch (SocketTimeoutException timeout) {
                p103Result = ResultType.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);
                    p103Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);

                    if (p103Result.equals(ResultType.SYSTEM_OK)) {

                        /*Feature需要獲取name*/
                        strThemeData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONObject jsonData = new JSONObject(strThemeData);

                        String themearea = JsonUtility.GetString(jsonData, ProtocolKey.themearea);

                        JSONObject jsonThemeArea = new JSONObject(themearea);

                        italbumTheme = new ItemAlbumCategory();
                        italbumTheme.setCategoryarea_id(JsonParamTypeClass.NULLCATEGORYID);
                        italbumTheme.setName(JsonUtility.GetString(jsonThemeArea, ProtocolKey.name));
                        italbumTheme.setColorhex(JsonUtility.GetString(jsonThemeArea, ProtocolKey.colorhex));
                        italbumTheme.setImage_360x360(JsonUtility.GetString(jsonThemeArea, ProtocolKey.image_360x360));

                    }

                    if (!p103Result.equals(ResultType.SYSTEM_OK)) {
                        p103Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }


    @Override
    public void onClick(View v) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (v.getId()){

            case R.id.cgBannerImg:

                Bundle bundle = new Bundle();

                if (italbumTheme.getCategoryarea_id() == JsonParamTypeClass.NULLCATEGORYID) {

                    bundle.putString(Key.jsonData, strThemeData);

                }

                bundle.putInt(Key.categoryarea_id, italbumTheme.getCategoryarea_id());

                startActivity(new Intent(getActivity(), CategoryBookCaseActivity.class).putExtras(bundle));

                ActivityAnim.StartAnim(getActivity());

                break;

        }


    }


}

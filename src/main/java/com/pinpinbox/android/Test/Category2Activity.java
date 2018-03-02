package com.pinpinbox.android.Test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.ResideLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.CategoryAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCategoryAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumCategory;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StaggeredHeight;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2017/3/19.
 */
public class Category2Activity extends DraggerActivity implements View.OnClickListener {


    private Activity mActivity;

    private GetCategoryListTask getCategoryListTask;
    private SetCategoryTask setCategoryTask;
    private MoreDataTask moreDataTask;

    private RecyclerCategoryAdapter categoryAreaAdapter;

    private List<ItemAlbumCategory> itemAlbumCategoryList;
    private List<ItemAlbum> albumList;

    private ResideLayout reside;
    private ListView lvCategoryList;
    private RecyclerView rvCategory;
    private ImageView backImg;
    private TextView tvActionBarTitle;
    private SmoothProgressBar pbLoadMore;


    private String id, token;
    private String p10Message = "";
    private String categoryName;

    private int p10Result = -1;
    private int doingType;
    private int round, count;
    private int loadCount;
    private int category_id;
    private int deviceType;
    private static final int PHONE = 10001;
    private static final int TABLE = 10002;


    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean isShowSelectCategoryFirst = false;
    private boolean isDoingMore = false;


    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                MyLog.Set("e", mActivity.getClass(), "onLoad");
                if(isDoingMore){
                    MyLog.Set("e", mActivity.getClass(), "正在讀取更多項目");
                    return;
                }
                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

                MyLog.Set("e", mActivity.getClass(), "sizeMax");
            }
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_category);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        init();

        selectCategory();

        doGetCategoryList();


    }


    private void init() {

        if (SystemUtility.isTablet(getApplicationContext())) {
            //平版
            deviceType = TABLE;
        } else {
            //手機
            deviceType = PHONE;
        }

        mActivity = this;

        count = 16;

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        lvCategoryList = (ListView) findViewById(R.id.lvCategoryList);
        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        backImg = (ImageView) findViewById(R.id.backImg);
        tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        TextUtility.setBold(tvActionBarTitle, true);

        reside = (ResideLayout) findViewById(R.id.reside);
        reside.setAlphaView(lvCategoryList);
        pbLoadMore = (SmoothProgressBar) findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();


        View vHeader = LayoutInflater.from(mActivity).inflate(R.layout.header_2_0_0_title, null);

        TextView tvTitle = (TextView) vHeader.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.pinpinbox_2_0_0_title_explore_album);

        TextUtility.setBold(tvTitle, true);
        lvCategoryList.addHeaderView(vHeader);

        itemAlbumCategoryList = new ArrayList<>();
        albumList = new ArrayList<>();

        rvCategory.addItemDecoration(new SpacesItemDecoration(16));
        rvCategory.setItemAnimator(new DefaultItemAnimator());
        rvCategory.addOnScrollListener(mOnScrollListener);

        backImg.setOnClickListener(this);


        reside.setPanelSlideListener(new ResideLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

                if(albumList.size()==0&& slideOffset<0.9){
                    reside.openPane();

                    if(!isShowSelectCategoryFirst) {

                        PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_select_category_first);

                        isShowSelectCategoryFirst = true;
                    }
                }


            }

            @Override
            public void onPanelOpened(View panel) {

                MyLog.Set("d", this.getClass(), "開啟");

            }

            @Override
            public void onPanelClosed(View panel) {
                MyLog.Set("d", this.getClass(), "關閉");

//                if(albumList ==null ||  albumList.size()==0){
//
//                    reside.openPane();
//
//                    if(!isShowSelectCategoryFirst) {
//
//                        PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_select_category_first);
//
//                        isShowSelectCategoryFirst = true;
//                    }
//
//                }


            }
        });
        reside.openPane();

    }

    private void setRecycler() {

        categoryAreaAdapter = new RecyclerCategoryAdapter(mActivity, albumList);
        rvCategory.setAdapter(categoryAreaAdapter);

//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        StaggeredGridLayoutManager manager = null;

        if(deviceType == TABLE){

            //平版
            manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        }else if(deviceType == PHONE){

            //手機
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }



        rvCategory.setLayoutManager(manager);

        categoryAreaAdapter.setOnRecyclerViewListener(new RecyclerCategoryAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if(reside.isOpen()){
                    return;
                }

                ActivityIntent.toAlbumInfo(
                        mActivity,
                        true,
                        albumList.get(position).getAlbum_id(),
                        albumList.get(position).getCover(),
                        0,
                        v.findViewById(R.id.coverImg)
                );



            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }

    private void selectCategory() {

        lvCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i!=0) {

                    categoryName = itemAlbumCategoryList.get(i - 1).getName();

                    category_id = itemAlbumCategoryList.get(i - 1).getCategoryarea_id();

                    doSetCategory();
                }

            }
        });

    }

    private void protocol10(String limit) {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P10_RetrieveRankList,
                    SetMapByProtocol.setParam10_retrievehotrank(id, token, StringIntMethod.IntToString(category_id), limit),
                    null);
           MyLog.Set("json", getClass(), strJson);
        } catch (SocketTimeoutException timeout) {
            p10Result = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p10Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);


                if (p10Result == 1) {

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                    JSONArray jsonArray = new JSONArray(data);

                    loadCount = jsonArray.length();

                    int minHeight = DensityUtility.dip2px(mActivity.getApplicationContext(), 72);

                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject object = (JSONObject) jsonArray.get(i);

                        String album = JsonUtility.GetString(object, ProtocolKey.album);
                        String albumstatistics = JsonUtility.GetString(object, ProtocolKey.albumstatistics);
                        String user = JsonUtility.GetString(object, ProtocolKey.user);

                        JSONObject jsonAlbum = new JSONObject(album);
                        JSONObject jsonAlbumstatistics = new JSONObject(albumstatistics);
                        JSONObject jsonUser = new JSONObject(user);


                        ItemAlbum itemAlbum = new ItemAlbum();

                        /*set album*/
                        itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                        itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));

                        String cover = JsonUtility.GetString(jsonAlbum, ProtocolKey.cover);
                        itemAlbum.setCover(cover);

                        try {
                            int width = jsonAlbum.getInt(ProtocolKey.cover_width);
                            int height = jsonAlbum.getInt(ProtocolKey.cover_height);
                            int image_height = StaggeredHeight.setImageHeight(width, height);

                            if (image_height < minHeight) {
                                image_height = minHeight;
                            }

                            itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                            itemAlbum.setCover_height(image_height);

                            itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));

                        } catch (Exception e) {
                            itemAlbum.setCover_hex("");
                            itemAlbum.setCover_width(PPBApplication.getInstance().getStaggeredWidth());
                            itemAlbum.setCover_height(PPBApplication.getInstance().getStaggeredWidth());
                            MyLog.Set("e", this.getClass(), "圖片長寬無法讀取");
                        }

                        String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                        JSONObject jsonUsefor = new JSONObject(usefor);
                        itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                        itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                        itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));
                        itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));

                        /*set user*/
                        itemAlbum.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));
                        itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                        itemAlbum.setUser_picture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));

                        /*set albumstatistics*/
                        itemAlbum.setViewed(JsonUtility.GetInt(jsonAlbumstatistics, ProtocolKey.viewed));
                        itemAlbum.setLikes(JsonUtility.GetInt(jsonAlbumstatistics, ProtocolKey.likes));

                        albumList.add(itemAlbum);

                    }

                } else if (p10Result == 0) {
                    p10Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                }
            } catch (Exception e) {
                loadCount = 0;
                e.printStackTrace();
            }
        }


    }

    public void scrollToTop() {

        StaggeredGridLayoutManager linearLayoutManager = (StaggeredGridLayoutManager) rvCategory.getLayoutManager();

        int[] firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPositions(null);

        if (firstVisibleItemPosition[0] > 10) {
            rvCategory.scrollToPosition(10);
            MyLog.Set("d", getClass(), "先移動至第10項目");
        }

        rvCategory.smoothScrollToPosition(0);
    }

    private void back() {

        if (reside.isOpen()) {
            finish();
            ActivityAnim.FinishAnim(mActivity);
        } else {
            reside.openPane();
        }

    }

    private void cleanPicasso(){

        int count = albumList.size();

        if(count>0){

            for (int i = 0; i < count; i++) {

                String cover = albumList.get(i).getCover();

                String picture = albumList.get(i).getUser_picture();

                Picasso.with(getApplicationContext()).invalidate(cover);
                Picasso.with(getApplicationContext()).invalidate(picture);

            }


        }

        System.gc();

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {


                switch (doingType) {
                    case DoingTypeClass.DoDefault:
                        doGetCategoryList();
                        break;

                    case DoingTypeClass.DoSetCategory:
                        doSetCategory();
                        break;

                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;


                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    private void doGetCategoryList() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getCategoryListTask = new GetCategoryListTask();
        getCategoryListTask.execute();

    }

    private void doSetCategory() {


        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        setCategoryTask = new SetCategoryTask();
        setCategoryTask.execute();

    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        moreDataTask = new MoreDataTask();
        moreDataTask.execute();
    }

    private class GetCategoryListTask extends AsyncTask<Void, Void, Object> {

        private String p09Message = "";
        private int p09Result = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... voids) {


            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P09_RetrieveCategoryList,
                        SetMapByProtocol.setParam09_retrievecatgeorylist(id, token),
                        null);
                MyLog.Set("d", getClass(), "p09strJson => " + strJson);
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

//                            String mName = JsonUtility.GetString(jsonCategoryarea, ProtocolKey.name);
//                            String name = "";
//                            for (int k = 0; k < mName.length(); k++) {
//
//                                char m = mName.charAt(k);
//                                name = name + m + " ";
//                            }
//                            itemAlbumCategory.setName(name);

                            itemAlbumCategoryList.add(itemAlbumCategory);
                        }


                    } else if (p09Result == 0) {
                        p09Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dissmissLoading();

            if (p09Result == 1) {
                CategoryAdapter adapter = new CategoryAdapter(mActivity, itemAlbumCategoryList);
                lvCategoryList.setAdapter(adapter);

                final ViewPropertyAnimator a1 = lvCategoryList.animate();
                a1.setDuration(750)
                        .alpha(1);
                a1.start();

            } else if (p09Result == 0) {
                DialogV2Custom.BuildError(mActivity, p09Message);
            } else if (p09Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }


    }

    private class SetCategoryTask extends AsyncTask<Void, Void, Object> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSetCategory;
            startLoading();

            if(categoryAreaAdapter!=null) {

                categoryAreaAdapter.notifyItemRangeRemoved(0, albumList.size());
                categoryAreaAdapter=null;

            }

            cleanPicasso();

            albumList.clear();

            round = 0;
            loadCount = 0;
            sizeMax = false;
            isNoDataToastAppeared = false;

        }

        @Override
        protected Object doInBackground(Void... voids) {

            protocol10(round + "," + count);
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            dissmissLoading();

            if (p10Result == 1) {

                final ViewPropertyAnimator a1 = tvActionBarTitle.animate();
                a1.setDuration(200)
                        .alpha(0);
                a1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvActionBarTitle.setText(categoryName);
                    }
                }, 200);



                final ViewPropertyAnimator a = tvActionBarTitle.animate();
                a.setDuration(200)
                        .setStartDelay(200)
                        .alpha(1);

                a.start();

                setRecycler();

                reside.closePane();
                if (albumList.size() > 0) {
                    round = round + count;
                }



                switch (categoryName){


                    case "旅遊記錄":
                        FlurryUtil.onEvent(FlurryKey.category_travel);
                    break;

                    case "愛上攝影":
                        FlurryUtil.onEvent(FlurryKey.category_photography);
                        break;

                    case "藝術創作":
                        FlurryUtil.onEvent(FlurryKey.category_art);
                        break;

                    case "人物寫真":
                        FlurryUtil.onEvent(FlurryKey.category_portrait);
                        break;

                    case "休閒生活":
                        FlurryUtil.onEvent(FlurryKey.category_life);
                        break;

                    case "可愛寵物":
                        FlurryUtil.onEvent(FlurryKey.category_pet);
                        break;

                    case "動漫公仔":
                        FlurryUtil.onEvent(FlurryKey.category_anime);
                        break;







                    case "旅遊":
                        FlurryUtil.onEvent(FlurryKey.category_travel);
                        break;

                    case "攝影":
                        FlurryUtil.onEvent(FlurryKey.category_photography);
                        break;

                    case "藝術":
                        FlurryUtil.onEvent(FlurryKey.category_art);
                        break;

                    case "美食":
                        FlurryUtil.onEvent(FlurryKey.category_food);
                        break;

                    case "生活":
                        FlurryUtil.onEvent(FlurryKey.category_life);
                        break;

                    case "享知":
                        FlurryUtil.onEvent(FlurryKey.category_knowledge);
                        break;

                    case "寵物":
                        FlurryUtil.onEvent(FlurryKey.category_pet);
                        break;

                    case "時尚":
                        FlurryUtil.onEvent(FlurryKey.category_fashion);
                        break;

                    case "麻朵":
                        FlurryUtil.onEvent(FlurryKey.category_model);
                        break;

                    case "其他":
                        FlurryUtil.onEvent(FlurryKey.category_other);
                        break;

                    //已取消
//                    case "樂活資訊":
//                        FlurryUtil.onEvent(FlurryKey.category_information);
//                        break;



                    case "拼旅遊":
                        FlurryUtil.onEvent(FlurryKey.category_travel);
                        break;

                    case "拼攝影":
                        FlurryUtil.onEvent(FlurryKey.category_photography);
                        break;

                    case "拼藝術":
                        FlurryUtil.onEvent(FlurryKey.category_art);
                        break;

                    case "拼美食":
                        FlurryUtil.onEvent(FlurryKey.category_food);
                        break;

                    case "拼生活":
                        FlurryUtil.onEvent(FlurryKey.category_life);
                        break;

                    case "拼享知":
                        FlurryUtil.onEvent(FlurryKey.category_knowledge);
                        break;

                    case "拼寵物":
                        FlurryUtil.onEvent(FlurryKey.category_pet);
                        break;

                    case "拼時尚":
                        FlurryUtil.onEvent(FlurryKey.category_fashion);
                        break;

                    case "拼麻朵":
                        FlurryUtil.onEvent(FlurryKey.category_model);
                        break;

                    case "拼其他":
                        FlurryUtil.onEvent(FlurryKey.category_other);
                        break;






                }







            } else if (p10Result == 0) {
                DialogV2Custom.BuildError(mActivity, p10Message);
            } else if (p10Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }


    }

    public class MoreDataTask extends AsyncTask<Void, Void, Object> {

        private int moreFirstPosition;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            isDoingMore = true;

            doingType = DoingTypeClass.DoMoreData;

            pbLoadMore.setVisibility(View.VISIBLE);
            pbLoadMore.progressiveStart();

            moreFirstPosition = albumList.size();

        }

        @Override
        protected Object doInBackground(Void... params) {
            protocol10(round + "," + count);
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            isDoingMore = false;

            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();

            if (p10Result==1) {

                if (loadCount == 0) {
                    MyLog.Set("d", this.getClass(), "已達最大值");
                    sizeMax = true; // 已達最大值
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }
                    return;
                }

                categoryAreaAdapter.notifyItemRangeInserted(albumList.size(), count);

                //添加下一次讀取範圍
                round = round + count;



            } else if (p10Result==0) {
                DialogV2Custom.BuildError(mActivity, p10Message);
            } else if (p10Result==Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }


    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public SpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int position = parent.getChildAdapterPosition(view);
            int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
//            if (parent.getChildAdapterPosition(view) != 0) { //無header就取消

                if (deviceType == TABLE) {

                    if (spanIndex == 0) {
                        outRect.left = mSpace;
                        outRect.right = 0;
                    } else if (spanIndex == 1) {
                        outRect.left = mSpace;
                        outRect.right = mSpace;
                    } else {
                        outRect.left = 0;
                        outRect.right = mSpace;
                    }

                } else if (deviceType == PHONE) {

                    if (spanIndex == 0) {
                        outRect.left = mSpace;
                        outRect.right = 0;
                    } else {
                        outRect.left = 0;
                        outRect.right = mSpace;
                    }

                }

                outRect.bottom = 32;


//            }


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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        back();
    }


    @Override
    protected void onPause() {
        super.onPause();
        cleanPicasso();
    }


    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        cleanPicasso();
        cancelTask(getCategoryListTask);
        cancelTask(setCategoryTask);
        cancelTask(moreDataTask);

        Recycle.IMG(backImg);

        System.gc();

        super.onDestroy();
    }
}

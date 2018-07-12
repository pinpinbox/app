package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCategoryAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
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
public class CategoryContents2Activity extends DraggerActivity implements View.OnClickListener {


    private Activity mActivity;


    private SetCategoryTask setCategoryTask;
    private MoreDataTask moreDataTask;

    private RecyclerCategoryAdapter categoryAreaAdapter;

    private List<ItemAlbum> albumList;


    private RecyclerView rvCategory;
    private ImageView backImg;
    private TextView tvActionBarTitle;
    private SmoothProgressBar pbLoadMore;


    private String id, token;
    private String p10Message = "";
    private String strCategoryName = "";

    private int p10Result = -1;
    private int doingType;
    private int round, count;
    private int loadCount;
    private int categoryarea_id;
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
                if (isDoingMore) {
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
        setContentView(R.layout.activity_2_0_0_currentcategoryall);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();

        init();

        doSetCategory();



    }

    private boolean isFocus = false;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        MyLog.Set("e", getClass(), "hasFocus => " + hasFocus);

        if(hasFocus){

            if(!isFocus){
//                doSetCategory();
                isFocus = true;
            }

        }


        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
    }

    private void getBundle() {


        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){

            categoryarea_id = bundle.getInt(Key.categoryarea_id, -1);

            strCategoryName = bundle.getString(Key.categoryarea_name, "");

        }


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


        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        backImg = (ImageView) findViewById(R.id.backImg);
        tvActionBarTitle = (TextView) findViewById(R.id.tvActionBarTitle);
        TextUtility.setBold(tvActionBarTitle, true);


        pbLoadMore = (SmoothProgressBar) findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();


        albumList = new ArrayList<>();

        rvCategory.addItemDecoration(new SpacesItemDecoration(16));
        rvCategory.setItemAnimator(new DefaultItemAnimator());
        rvCategory.addOnScrollListener(mOnScrollListener);

        backImg.setOnClickListener(this);


        tvActionBarTitle.setText(strCategoryName);


    }

    private void setRecycler() {

        categoryAreaAdapter = new RecyclerCategoryAdapter(mActivity, albumList);
        rvCategory.setAdapter(categoryAreaAdapter);

        StaggeredGridLayoutManager manager = null;

        if (deviceType == TABLE) {

            //平版
            manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        } else if (deviceType == PHONE) {

            //手機
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        }



        rvCategory.setLayoutManager(manager);

        categoryAreaAdapter.setOnRecyclerViewListener(new RecyclerCategoryAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                ActivityIntent.toAlbumInfo(
                        mActivity,
                        true,
                        albumList.get(position).getAlbum_id(),
                        albumList.get(position).getCover(),
                        albumList.get(position).getImage_orientation(),
                        albumList.get(position).getCover_width(),
                        albumList.get(position).getCover_height(),
                        v.findViewById(R.id.coverImg)
                );

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }



    private void protocol10(String limit) {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P10_RetrieveRankList,
                    SetMapByProtocol.setParam10_retrievehotrank(id, token, StringIntMethod.IntToString(categoryarea_id), limit),
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


                            if(width>height){
                                itemAlbum.setImage_orientation(ItemAlbum.LANDSCAPE);
                            }else if(height>width){
                                itemAlbum.setImage_orientation(ItemAlbum.PORTRAIT);
                            }else {
                                itemAlbum.setImage_orientation(0);
                            }



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

            finish();
            ActivityAnim.FinishAnim(mActivity);

    }

    private void cleanPicasso() {

        int count = albumList.size();

        if (count > 0) {

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



    private class SetCategoryTask extends AsyncTask<Void, Void, Object> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSetCategory;
            startLoading();

            if (categoryAreaAdapter != null) {

                categoryAreaAdapter.notifyItemRangeRemoved(0, albumList.size());
                categoryAreaAdapter = null;

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




                final ViewPropertyAnimator a = tvActionBarTitle.animate();
                a.setDuration(200)
                        .setStartDelay(200)
                        .alpha(1);

                a.start();

                setRecycler();

                if (albumList.size() > 0) {
                    round = round + count;
                }

//                switch (strCategoryName) {
//
//
//                    case "拼旅遊":
//                        FlurryUtil.onEvent(FlurryKey.category_travel);
//                        break;
//
//                    case "拼攝影":
//                        FlurryUtil.onEvent(FlurryKey.category_photography);
//                        break;
//
//                    case "拼藝術":
//                        FlurryUtil.onEvent(FlurryKey.category_art);
//                        break;
//
//                    case "拼美食":
//                        FlurryUtil.onEvent(FlurryKey.category_food);
//                        break;
//
//                    case "拼生活":
//                        FlurryUtil.onEvent(FlurryKey.category_life);
//                        break;
//
//                    case "拼享知":
//                        FlurryUtil.onEvent(FlurryKey.category_knowledge);
//                        break;
//
//                    case "拼寵物":
//                        FlurryUtil.onEvent(FlurryKey.category_pet);
//                        break;
//
//                    case "拼時尚":
//                        FlurryUtil.onEvent(FlurryKey.category_fashion);
//                        break;
//
//                    case "拼寶貝":
//                        FlurryUtil.onEvent(FlurryKey.category_model);
//                        break;
//
//                    case "拼其他":
//                        FlurryUtil.onEvent(FlurryKey.category_other);
//                        break;
//
//
//                }


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



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            isDoingMore = true;

            doingType = DoingTypeClass.DoMoreData;

            pbLoadMore.setVisibility(View.VISIBLE);
            pbLoadMore.progressiveStart();

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

            if (p10Result == 1) {

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

            } else if (p10Result == 0) {
                DialogV2Custom.BuildError(mActivity, p10Message);
            } else if (p10Result == Key.TIMEOUT) {

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
        cancelTask(setCategoryTask);
        cancelTask(moreDataTask);

        Recycle.IMG(backImg);

        System.gc();

        super.onDestroy();
    }
}

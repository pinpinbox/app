package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerExploreAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumExplore;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemCategoryBanner;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ExLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.JsonParamTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCGAbannerImage;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCGAbannerVideo;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCategoryUser;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol102;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/11/17.
 */

public class Feature2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private FragmentCategoryUser fragmentCategoryUser;
    private Protocol102 protocol102;

    private FragmentPagerItemAdapter adapter;

    private List<ItemUser> userList;
    private List<ItemAlbumExplore> albumExploreList;

    private LinearLayout linContents, linUser;
    private ImageView backImg;
    private TextView tvTitle;
    private ScrollView svContents;
    private ViewPager vpBanner;
    private FrameLayout frameUser;

    private String strJsonData = "";

    private int categoryarea_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_feature);

//        SystemUtility.SysApplication.getInstance().addActivity(this);


        getBundle();

        init();

        doGetFeature();

    }

    private boolean isFocus = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        MyLog.Set("e", getClass(), "hasFocus => " + hasFocus);

        if (hasFocus) {

            if (!isFocus) {
//                doGetFeature();
                isFocus = true;
            }

        }


        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            categoryarea_id = bundle.getInt(Key.categoryarea_id, JsonParamTypeClass.NULLCATEGORYID);

            if (categoryarea_id == JsonParamTypeClass.NULLCATEGORYID) {
                strJsonData = bundle.getString(Key.jsonData, "");
            }


        }

    }

    private void init() {

        mActivity = this;

        userList = new ArrayList<>();

        linContents = (LinearLayout) findViewById(R.id.linContents);
        linUser = (LinearLayout) findViewById(R.id.linUser);
        svContents = (ScrollView) findViewById(R.id.svContents);
        backImg = (ImageView) findViewById(R.id.backImg);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        vpBanner = (ViewPager) findViewById(R.id.vpBanner);
        frameUser = (FrameLayout) findViewById(R.id.frameUser);

        TextUtility.setBold(tvTitle, true);

        linUser.setOnClickListener(this);
        backImg.setOnClickListener(this);

        int bannerWidth = ScreenUtils.getScreenWidth();
        int bannerHeight = (bannerWidth * 540) / 960;
        vpBanner.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), bannerHeight));


    }

    private void doGetFeature() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }


        if (categoryarea_id == JsonParamTypeClass.NULLCATEGORYID) {
            linUser.setVisibility(View.GONE);
//            tvAll.setVisibility(View.GONE);

            decodeJsonToSetTheme();

        } else {


            protocol102 = new Protocol102(
                    mActivity,
                    PPBApplication.getInstance().getId(),
                    PPBApplication.getInstance().getToken(),
                    categoryarea_id + "",
                    new Protocol102.TaskCallBack() {
                        @Override
                        public void Prepare() {

                            startLoading();

                        }

                        @Override
                        public void Post() {

                            dissmissLoading();

                        }

                        @Override
                        public void Success(List<ItemUser> cgaUserList, List<ItemAlbumExplore> itemAlbumExploreList, List<ItemCategoryBanner> itemCategoryBannerList, String categoryareaName) {

                            setUserList(cgaUserList);

                            setCGAList(itemAlbumExploreList);

                            setBannerList(itemCategoryBannerList);

                            showContents(categoryareaName);


                        }

                        @Override
                        public void TimeOut() {

                            doGetFeature();

                        }


                    }

            );

        }


    }

    private void decodeJsonToSetTheme() {

        List<ItemAlbumExplore> itemAlbumExploreList = new ArrayList<>();

        String strCategoryareaName = "";

        try {

            JSONObject jsonData = new JSONObject(strJsonData);

            String themearea = JsonUtility.GetString(jsonData, ProtocolKey.themearea);

            JSONObject jsonThemeArea = new JSONObject(themearea);

            strCategoryareaName = JsonUtility.GetString(jsonThemeArea, ProtocolKey.name);

            String albumexplore = JsonUtility.GetString(jsonData, ProtocolKey.albumexplore);

            JSONArray jsonArrayAE = new JSONArray(albumexplore);

            for (int i = 0; i < jsonArrayAE.length(); i++) {

                ItemAlbumExplore itemAlbumExplore = new ItemAlbumExplore();

                JSONObject jsonAlbumexplore = (JSONObject) jsonArrayAE.get(i);

                /*get album list*/
                String albumList = JsonUtility.GetString(jsonAlbumexplore, ProtocolKey.album);
                JSONArray jsonArrayAlbum = new JSONArray(albumList);
                itemAlbumExplore.setItemAlbumList(getAlbumList(jsonArrayAlbum));


                /*get name*/
                String albumexploreX = JsonUtility.GetString(jsonAlbumexplore, ProtocolKey.albumexplore);
                JSONObject jsonAlbumexploreX = new JSONObject(albumexploreX);
                String name = JsonUtility.GetString(jsonAlbumexploreX, ProtocolKey.name);
                String url = JsonUtility.GetString(jsonAlbumexploreX, ProtocolKey.url);
                itemAlbumExplore.setName(name);
                itemAlbumExplore.setUrl(url);

                itemAlbumExploreList.add(itemAlbumExplore);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setCGAList(itemAlbumExploreList);

        showContents(strCategoryareaName);

    }

    private List<ItemAlbum> getAlbumList(JSONArray jsonArrayAlbum) {

        List<ItemAlbum> itemAlbumList = new ArrayList<>();

        try {

            for (int i = 0; i < jsonArrayAlbum.length(); i++) {

                JSONObject jsonItem = (JSONObject) jsonArrayAlbum.get(i);

                String album = JsonUtility.GetString(jsonItem, ProtocolKey.album);
                String user = JsonUtility.GetString(jsonItem, ProtocolKey.user);


                JSONObject jsonAlbum = new JSONObject(album);
                JSONObject jsonUser = new JSONObject(user);

                ItemAlbum itemAlbum = new ItemAlbum();

                itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_id));
                itemAlbum.setCover(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover));
                itemAlbum.setCover_hex(JsonUtility.GetString(jsonAlbum, ProtocolKey.cover_hex));
                itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));

                String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                JSONObject jsonUsefor = new JSONObject(usefor);
                itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));
                itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                itemAlbum.setImage(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.image));
                itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));


                itemAlbum.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));
                itemAlbum.setUser_picture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));

                itemAlbumList.add(itemAlbum);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return itemAlbumList;
    }

    private void setUserList(List<ItemUser> cgaUserList) {


        if (userList == null) {
            userList = new ArrayList<>();
        }

        for (int i = 0; i < cgaUserList.size(); i++) {
            addUsers(cgaUserList, i);

            userList.add(cgaUserList.get(i));

        }

        for (int i = 0; i < cgaUserList.size(); i++) {
            View v = linUser.getChildAt(i);
            v.setLayoutParams(new LinearLayout.LayoutParams(SizeUtils.dp2px(36), SizeUtils.dp2px(36)));
            ViewControl.setMargins(v, -SizeUtils.dp2px(16), 0, 0, 0);
        }

    }

    private void setCGAList(final List<ItemAlbumExplore> itemAlbumExploreList) {


        if (albumExploreList == null) {
            albumExploreList = new ArrayList<>();
        }


        for (int i = 0; i < itemAlbumExploreList.size(); i++) {

            albumExploreList.add(itemAlbumExploreList.get(i));

            View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_feature, null);

            TextView tvName = (TextView) view.findViewById(R.id.tvName);

            tvName.setText(itemAlbumExploreList.get(i).getName());


            TextView tvMore = (TextView) view.findViewById(R.id.tvMore);

            if (itemAlbumExploreList.get(i).getUrl() != null && !itemAlbumExploreList.get(i).getUrl().equals("") && !itemAlbumExploreList.get(i).getUrl().equals("null")) {
                tvMore.setVisibility(View.VISIBLE);
                final int finalI = i;
                tvMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyLog.Set("e", mActivity.getClass(), "itemAlbumExploreList.get(i).getUrl() => " + itemAlbumExploreList.get(finalI).getUrl());

                        Uri uri = Uri.parse(itemAlbumExploreList.get(finalI).getUrl());
                        String user_id = uri.getQueryParameter(Key.user_id);
                        String album_id = uri.getQueryParameter(Key.album_id);
                        String categoryarea_id = uri.getQueryParameter(Key.categoryarea_id);
                        String event_id = uri.getQueryParameter(Key.event_id);
                        String category_id = uri.getQueryParameter("category_id");

                        if (user_id == null) {
                            MyLog.Set("e", mActivity.getClass(), "user_id==null");
                        } else {
                            MyLog.Set("e", mActivity.getClass(), "user_id => " + user_id);
                            toUser(user_id);
                            return;
                        }

                        if (album_id == null) {
                            MyLog.Set("e", mActivity.getClass(), "album_id==null");
                        } else {
                            MyLog.Set("e", mActivity.getClass(), "album_id => " + album_id);
                            toAlbum(album_id);
                            return;
                        }

                        if (categoryarea_id != null && category_id == null) {
                            MyLog.Set("e", mActivity.getClass(), "categoryarea_id => " + categoryarea_id);

                            List<String> list = uri.getPathSegments();

                            //有explore => 書櫃，無則前往全部瀑布流

                            boolean toContents = true;

                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).equals("explore")) {
                                    toContents = false;
                                    break;
                                }
                            }

                            if (toContents) {
                                toCurrentContents(StringIntMethod.StringToInt(categoryarea_id), tvTitle.getText().toString());
                            } else {
                                toFeature(StringIntMethod.StringToInt(categoryarea_id));
                            }


                            return;

                        } else {
                            MyLog.Set("e", mActivity.getClass(), "categoryarea_id==null");
                        }

                        if (event_id == null) {
                            MyLog.Set("e", mActivity.getClass(), "event_id==null");
                        } else {
                            MyLog.Set("e", mActivity.getClass(), "event_id => " + event_id);
                            toEvent(event_id);
                            return;
                        }

                        toWeb(itemAlbumExploreList.get(finalI).getUrl(), "");


                    }
                });
            } else {
                tvMore.setVisibility(View.GONE);
            }


            RecyclerView rvFeatureContent = (RecyclerView) view.findViewById(R.id.rvFeatureContent);


            ExLinearLayoutManager layoutManager = new ExLinearLayoutManager(mActivity);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvFeatureContent.setLayoutManager(layoutManager);
            RecyclerExploreAdapter exploreAdapter = new RecyclerExploreAdapter(mActivity, itemAlbumExploreList.get(i).getItemAlbumList());
            rvFeatureContent.setAdapter(exploreAdapter);

            linContents.addView(view);

            final int CGAItemPosition = i;

            exploreAdapter.setOnRecyclerViewListener(new RecyclerExploreAdapter.OnRecyclerViewListener() {
                @Override
                public void onItemClick(int position, View v) {

                    if (ClickUtils.ButtonContinuousClick()) {
                        return;
                    }


                    ActivityIntent.toAlbumInfo(
                            mActivity,
                            true,
                            (itemAlbumExploreList.get(CGAItemPosition).getItemAlbumList()).get(position).getAlbum_id(),
                            (itemAlbumExploreList.get(CGAItemPosition).getItemAlbumList()).get(position).getCover(),
                            (itemAlbumExploreList.get(CGAItemPosition).getItemAlbumList()).get(position).getImage_orientation(),
                            v.findViewById(R.id.coverImg)
                    );


                }

                @Override
                public boolean onItemLongClick(int position, View v) {
                    return false;
                }
            });

        }

    }


    private void setBannerList(List<ItemCategoryBanner> itemCategoryBannerList) {


        if (itemCategoryBannerList == null || itemCategoryBannerList.size() == 0) {
            MyLog.Set("e", getClass(), "no banner");
            vpBanner.setVisibility(View.GONE);
            return;
        }


        FragmentPagerItems fragmentPagerItems = new FragmentPagerItems(mActivity);

        for (int i = 0; i < itemCategoryBannerList.size(); i++) {

            MyLog.Set("e", mActivity.getClass(), "getBannerType => " + itemCategoryBannerList.get(i).getBannerType());
            MyLog.Set("e", mActivity.getClass(), "getImageUrl => " + itemCategoryBannerList.get(i).getImageUrl());
            MyLog.Set("e", mActivity.getClass(), "getImageLink => " + itemCategoryBannerList.get(i).getImageLink());
            MyLog.Set("e", mActivity.getClass(), "getVideoIdByUrl => " + itemCategoryBannerList.get(i).getVideoIdByUrl());
            MyLog.Set("e", mActivity.getClass(), "getVideoLink => " + itemCategoryBannerList.get(i).getVideoLink());
            MyLog.Set("e", mActivity.getClass(), "isVideoAuto => " + itemCategoryBannerList.get(i).isVideoAuto());
            MyLog.Set("e", mActivity.getClass(), "isVideoMute => " + itemCategoryBannerList.get(i).isVideoMute());
            MyLog.Set("e", mActivity.getClass(), "isVideoRepeat => " + itemCategoryBannerList.get(i).isVideoRepeat());

            MyLog.Set("e", mActivity.getClass(), "--------------------------------------------------------------------------------------");

            String bannerType = itemCategoryBannerList.get(i).getBannerType();

            Bundle bundle = new Bundle();

            if (bannerType.equals(ItemCategoryBanner.TYPE_VIDEO)) {

                bundle.putString(Key.youbuteVideoId, itemCategoryBannerList.get(i).getVideoIdByUrl());

                fragmentPagerItems.add(FragmentPagerItem.of("", FragmentCGAbannerVideo.class, bundle));


            }

            if (bannerType.equals(ItemCategoryBanner.TYPE_IMAGE)) {

                bundle.putString(Key.image, itemCategoryBannerList.get(i).getImageUrl());
                bundle.putString(Key.imageLink, itemCategoryBannerList.get(i).getImageLink());

                fragmentPagerItems.add(FragmentPagerItem.of("", FragmentCGAbannerImage.class, bundle));


            }

        }


        adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), fragmentPagerItems);

        vpBanner.setAdapter(adapter);


    }


    private void showContents(String categoryareaName) {
        tvTitle.setText(categoryareaName);

        ViewControl.AlphaTo1(svContents);

    }

    private void addUsers(List<ItemUser> cgaUserList, int position) {

        RoundCornerImageView userImg = (RoundCornerImageView) LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_category_user, null);

        String picture = cgaUserList.get(position).getPicture();

        if (picture != null && !picture.equals("null") && !picture.equals("")) {
            Picasso.with(getApplicationContext())
                    .load(cgaUserList.get(position).getPicture())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(getApplicationContext())
                    .into(userImg);
        } else {
            userImg.setImageResource(R.drawable.member_back_head);
        }

        linUser.addView(userImg);

    }

    private void openUserList() {


        frameUser.setVisibility(View.VISIBLE);


        Animation animation = AnimationUtils.loadAnimation(getApplication(), R.anim.right_exist);

        linUser.startAnimation(animation);

        Bundle bundle = new Bundle();

        bundle.putSerializable("userList", (Serializable) userList);


        if (fragmentCategoryUser == null) {
            fragmentCategoryUser = new FragmentCategoryUser();
            fragmentCategoryUser.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.top_enter, R.anim.top_exit)
                    .add(R.id.frameUser, fragmentCategoryUser, fragmentCategoryUser.getClass().getSimpleName())
                    .commit();


        } else {
            getSupportFragmentManager().beginTransaction().show(fragmentCategoryUser).commit();
        }

    }

    public void hideLargeUserList() {


        frameUser.setVisibility(View.GONE);

    }

    private void toCurrentContents(int categoryarea_id, String title) {

        ActivityIntent.toCategoryContents(mActivity, categoryarea_id, title);

    }

    private void toFeature(int categoryarea_id) {

        Bundle bundle = new Bundle();

        bundle.putInt(Key.categoryarea_id, categoryarea_id);

        startActivity(new Intent(mActivity, Feature2Activity.class).putExtras(bundle));

        ActivityAnim.StartAnim(mActivity);


    }

    private void toAlbum(String album_id) {
        ActivityIntent.toAlbumInfo(mActivity, false, album_id, null, 0, null);
    }

    private void toUser(String user_id) {
        ActivityIntent.toUser(mActivity, false, false, user_id, null, null, null);
    }

    private void toEvent(String event_id) {

        ActivityIntent.toEvent(mActivity, event_id);

    }

    private void toWeb(String url, String title) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.url, url);
        bundle.putString(Key.title, title);
        Intent intent = new Intent(mActivity, WebView2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);

    }


    private void back() {
        finish();
        ActivityAnim.FinishAnim(mActivity);
    }

    public void cleanCache() {

//        private List<ItemUser> userList;
//        private List<ItemAlbumExplore> albumExploreList;

        if (userList != null && userList.size() > 0) {

            for (int i = 0; i < userList.size(); i++) {

                Picasso.with(getApplicationContext()).invalidate(userList.get(i).getPicture());

            }

        }


        if (albumExploreList != null && albumExploreList.size() > 0) {


            for (int i = 0; i < albumExploreList.size(); i++) {


                List<ItemAlbum> albumList = albumExploreList.get(i).getItemAlbumList();

                for (int j = 0; j < albumList.size(); j++) {

                    Picasso.with(getApplicationContext()).invalidate(albumList.get(j).getCover());

                }
            }


        }

        System.gc();

    }


    public LinearLayout getLinUser() {
        return this.linUser;
    }

    public void removeFragment() {
        fragmentCategoryUser = null;
    }

    @Override
    public void onClick(View v) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (v.getId()) {

            case R.id.linUser:

                openUserList();

                break;

            case R.id.backImg:

                back();

                break;

//            case R.id.tvAll:
//
//                toCurrentAllWorks(categoryarea_id, tvTitle.getText().toString());
//
//                break;

        }

    }

    @Override
    public void onBackPressed() {

        back();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onPause() {
        super.onPause();
        cleanCache();
    }


    @Override
    public void onDestroy() {


//        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        cancelTask(protocol102);

        cleanCache();
        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


}

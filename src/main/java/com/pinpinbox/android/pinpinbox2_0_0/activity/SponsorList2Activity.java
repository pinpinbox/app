package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerSponsorAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol104;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopBoard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2018/1/2.
 */

public class SponsorList2Activity extends DraggerActivity implements View.OnClickListener, RecyclerSponsorAdapter.OnUserInterativeListener {

    private Activity mActivity;
    private List<ItemUser> itemUserList;
    private RecyclerSponsorAdapter adapter;

    private Protocol104 protocol104;
    private PopBoard board;

    private RecyclerView rvSponsor;
    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private SmoothProgressBar pbLoadMore;
    private ImageView backImg;

    private boolean isDoingMore = false;

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);

            if (protocol104 == null) {
                return;
            }


            if ((Object) protocol104.isSizeMax() != null && !protocol104.isSizeMax()) {
                MyLog.Set("e", mActivity.getClass(), "onLoad");

                if (isDoingMore) {
                    MyLog.Set("e", mActivity.getClass(), "正在讀取更多項目");
                    return;
                }

                doMoreData();

            } else {

                if (!protocol104.isNoDataToastAppeared()) {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);

                    protocol104.setNoDataToastAppeared(true);

                }

                MyLog.Set("e", mActivity.getClass(), "sizeMax");

            }
        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_sponsorlist);

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);

        init();

        setRecycler();

        setProtocol();

    }

    private void init() {

        mActivity = this;

        itemUserList = new ArrayList<>();

        rvSponsor = (RecyclerView) findViewById(R.id.rvSponsor);
        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.pinPinBoxRefreshLayout);
        backImg = (ImageView) findViewById(R.id.backImg);


        pbLoadMore = (SmoothProgressBar) findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();

        SmoothProgressBar pbRefresh = (SmoothProgressBar) findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(findViewById(R.id.vRefreshAnim), pbRefresh);

        rvSponsor.setItemAnimator(new DefaultItemAnimator());
        rvSponsor.addOnScrollListener(mOnScrollListener);

        backImg.setOnClickListener(this);

    }

    private void setRecycler() {

        adapter = new RecyclerSponsorAdapter(mActivity, itemUserList);
        rvSponsor.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvSponsor.setLayoutManager(manager);

        adapter.setOnRecyclerViewListener(new RecyclerSponsorAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                RoundCornerImageView userImg = (RoundCornerImageView) v.findViewById(R.id.userImg);

                Bundle bundle = new Bundle();
                bundle.putString(Key.author_id, itemUserList.get(position).getUser_id());
                bundle.putString(Key.picture, itemUserList.get(position).getPicture());
                bundle.putString(Key.name, itemUserList.get(position).getName());

                MyLog.Set("e", mActivity.getClass(), "itemUserList.get(position).getUser_id() => " + itemUserList.get(position).getUser_id());
                MyLog.Set("e", mActivity.getClass(), "itemUserList.get(position).getPicture() => " + itemUserList.get(position).getPicture());
                MyLog.Set("e", mActivity.getClass(), "itemUserList.get(position).getName() => " + itemUserList.get(position).getName());

                if (SystemUtility.Above_Equal_V5()) {

                    Intent intent = new Intent(SponsorList2Activity.this, Author2Activity.class);
                    intent.putExtras(bundle);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(mActivity,
                                    userImg,
                                    ViewCompat.getTransitionName(userImg));
                    startActivity(intent, options.toBundle());


                } else {

                    Intent intent = new Intent(mActivity, Author2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    ActivityAnim.StartAnim(mActivity);

                }


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        adapter.setOnUserInterativeListener(this);

        pinPinBoxRefreshLayout.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }

            @Override
            public void onPullDistance(int distance) {

            }

            @Override
            public void onPullEnable(boolean enable) {

            }
        });

    }

    private void doGetSponsor() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        protocol104.GetList();

    }

    private void doRefresh() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        if (protocol104 != null && !protocol104.getTask().isCancelled()) {

            protocol104.getTask().cancel(true);

        }

        protocol104.Refresh();
    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        protocol104.LoadMore();
    }

    private void setProtocol() {


        ItemUser itemUser = new ItemUser();
        itemUser.setUser_id("140");
        itemUser.setName("cailum_1");
        itemUser.setPicture("https://ppb.sharemomo.com/storage/zh_TW/user/140/picture$80bc.jpg");
        itemUser.setFollow(false);
        itemUserList.add(itemUser);


        ItemUser itemUser1 = new ItemUser();
        itemUser1.setUser_id("311");
        itemUser1.setName("妹紙1");
        itemUser1.setPicture("https://ppb.sharemomo.com/storage/zh_TW/user/311/picture$0e8d.jpg");
        itemUser1.setFollow(false);
        itemUserList.add(itemUser1);

        adapter.notifyDataSetChanged();


//        protocol104 = new Protocol104(
//                mActivity,
//                PPBApplication.getInstance().getId(),
//                PPBApplication.getInstance().getToken(),
//                itemUserList,
//                new Protocol104.TaskCallBack() {
//                    @Override
//                    public void Prepare(int doingType) {
//                        switch (doingType) {
//
//                            case DoingTypeClass.DoDefault:
//
//                                startLoading();
//
//                                break;
//
//                            case DoingTypeClass.DoRefresh:
//
//                                itemUserList.clear();
//                                adapter.notifyDataSetChanged();
//
//
//                                break;
//
//                            case DoingTypeClass.DoMoreData:
//                                isDoingMore = true;
//                                pbLoadMore.setVisibility(View.VISIBLE);
//                                pbLoadMore.progressiveStart();
//
//                                break;
//
//                        }
//                    }
//
//                    @Override
//                    public void Post(int doingType) {
//
//                        switch (doingType) {
//
//                            case DoingTypeClass.DoDefault:
//
//                                dissmissLoading();
//
//                                ViewControl.AlphaTo1(pinPinBoxRefreshLayout);
//
//                                break;
//
//                            case DoingTypeClass.DoRefresh:
//
//                                pinPinBoxRefreshLayout.setRefreshing(false);
//
//                                break;
//
//                            case DoingTypeClass.DoMoreData:
//                                isDoingMore = false;
//                                pbLoadMore.setVisibility(View.GONE);
//                                pbLoadMore.progressiveStop();
//
//                                break;
//
//                        }
//
//                    }
//
//                    @Override
//                    public void Success(int doingType) {
//                        switch (doingType) {
//
//                            case DoingTypeClass.DoDefault:
//
//                                adapter.notifyDataSetChanged();
//
//                                break;
//
//                            case DoingTypeClass.DoRefresh:
//
//                                adapter.notifyDataSetChanged();
//
//                                break;
//
//                            case DoingTypeClass.DoMoreData:
//
//                                adapter.notifyItemRangeInserted(protocol104.getItemUserList().size(), protocol104.getRangeCount());
//
//                                break;
//
//
//                        }
//                    }
//
//                    @Override
//                    public void TimeOut(int doingType) {
//                        switch (doingType) {
//
//                            case DoingTypeClass.DoDefault:
//
//                                doGetSponsor();
//
//                                break;
//
//                            case DoingTypeClass.DoRefresh:
//
//                                doRefresh();
//
//                                break;
//
//                            case DoingTypeClass.DoMoreData:
//
//                                doMoreData();
//
//                                break;
//
//                        }
//                    }
//                }
//        );

    }

    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);

    }

    private void cleanCache() {


        if (itemUserList != null) {

            int count = itemUserList.size();

            for (int i = 0; i < count; i++) {
                Picasso.with(mActivity.getApplicationContext()).invalidate(itemUserList.get(i).getPicture());
            }

            System.gc();

        }

    }

    @Override
    public void onClick(View v) {


        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }

        switch (v.getId()) {
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
    public void doFollow(int position) {

        MyLog.Set("e", getClass(), "doFollow position =>" + position);

    }

    @Override
    public void doPostMessage(int position) {
        MyLog.Set("e", getClass(), "doPostMessage position =>" + position);

//        if (board == null) {

        if (board != null) {
            board.dismiss();
            board = null;
        }

        board = new PopBoard(mActivity, PopBoard.TypeUser, itemUserList.get(position).getUser_id(), (RelativeLayout) findViewById(R.id.rBackground), true);
        board.setSecondTitle(itemUserList.get(position).getName());


//        } else {
//            board.clearList();
//            board.doGetBoard();
//        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onPause() {

        cleanCache();

        super.onPause();

    }

    @Override
    public void onDestroy() {
        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if(protocol104!=null) {
            cancelTask(protocol104.getTask());
        }

        cleanCache();

        Recycle.IMG(backImg);

        if (board != null) {
            board.dismiss();
            board = null;
        }


        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }

}

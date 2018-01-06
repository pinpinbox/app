package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.DoingTypeClass;
import com.pinpinbox.android.StringClass.SystemType;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.ExStaggeredGridLayoutManager;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.HeaderSpanSizeLookup;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerVoteAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SpacesItemDecoration;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol100;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol99;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class Vote2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private ExStaggeredGridLayoutManager manager = null;

    private Protocol99 protocol99;
    private Protocol100 protocol100;

    private RecyclerVoteAdapter voteAdapter;

    private List<ItemAlbum> itemAlbumList;

    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;
    private SmoothProgressBar pbLoadMore;
    private RecyclerView rvVote;
    private ImageView backImg;
    private TextView tvRemaining;
    private View vHeader;

    private String event_id;
    private String id, token;

    private int deviceType;

    private boolean isDoingMore = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_vote);

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getBundle();

        init();

        setRecycler();

        setProtocol();

        doGetVote();


    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            event_id = bundle.getString(Key.event_id, "");
        }

    }

    private void init() {

        if (SystemUtility.isTablet(getApplicationContext())) {
            //平版
            deviceType = SystemType.TABLE;
            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        } else {
            //手機
            deviceType = SystemType.PHONE;
            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }

        mActivity = this;

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        itemAlbumList = new ArrayList<>();


        tvRemaining = (TextView) findViewById(R.id.tvRemaining);
        backImg = (ImageView) findViewById(R.id.backImg);
        rvVote = (RecyclerView) findViewById(R.id.rvVote);
        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.pinPinBoxRefreshLayout);


        SmoothProgressBar pbRefresh = (SmoothProgressBar) findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(findViewById(R.id.vRefreshAnim), pbRefresh);


        pbLoadMore = (SmoothProgressBar) findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();


        rvVote.addItemDecoration(new SpacesItemDecoration(16, deviceType, true));
        rvVote.setItemAnimator(new DefaultItemAnimator());
        rvVote.addOnScrollListener(mOnScrollListener);

        /*********************************************************************************************************/


        vHeader = LayoutInflater.from(this).inflate(R.layout.header_2_0_0_title, null);

        TextView tvTitle = (TextView) vHeader.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.pinpinbox_2_0_0_title_entries);

        ViewControl.setMargins(tvTitle,
                DensityUtility.dip2px(getApplicationContext(), 12),
                DensityUtility.dip2px(getApplicationContext(), 92),
                0,
                DensityUtility.dip2px(getApplicationContext(), 32));

        TextUtility.setBold(tvTitle, true);


        backImg.setOnClickListener(this);


    }

    private void setRecycler() {

        voteAdapter = new RecyclerVoteAdapter(mActivity, itemAlbumList);
        rvVote.setAdapter(voteAdapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(voteAdapter);
        rvVote.setAdapter(mHeaderAndFooterRecyclerViewAdapter);


        manager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) rvVote.getAdapter(), manager.getSpanCount()));
        rvVote.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvVote, vHeader);

        voteAdapter.setOnRecyclerViewListener(new RecyclerVoteAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }


                final ImageView img = (ImageView) v.findViewById(R.id.coverImg);

                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, itemAlbumList.get(position).getAlbum_id());
                bundle.putString(Key.cover, itemAlbumList.get(position).getCover());
                bundle.putBoolean("return", true);

                Intent intent = new Intent(mActivity, AlbumInfo2Activity.class).putExtras(bundle);

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(mActivity,
                                img,
                                ViewCompat.getTransitionName(img));
                startActivity(intent, options.toBundle());


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

        voteAdapter.setOnVoteListener(new RecyclerVoteAdapter.onVoteListener() {
            @Override
            public void onVoteClick(final int position) {

                DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setStyle(DialogStyleClass.CHECK);
                d.setMessage(getResources().getString(R.string.pinpinbox_2_0_0_other_text_vote_for) + itemAlbumList.get(position).getName() + "]?");
                d.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {
                        doVote(position);
                    }
                });
                d.show();


            }
        });

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

    private void setProtocol() {
        protocol99 = new Protocol99(
                mActivity,
                id,
                token,
                event_id,
                itemAlbumList,
                new Protocol99.TaskCallBack() {
                    @Override
                    public void Prepare(int doingType) {

                        switch (doingType) {

                            case DoingTypeClass.DoDefault:

                                startLoading();

                                break;

                            case DoingTypeClass.DoRefresh:

                                itemAlbumList.clear();
                                voteAdapter.notifyDataSetChanged();


                                break;

                            case DoingTypeClass.DoMoreData:
                                isDoingMore = true;
                                pbLoadMore.setVisibility(View.VISIBLE);
                                pbLoadMore.progressiveStart();

                                break;

                        }

                    }

                    @Override
                    public void Post(int doingType) {

                        switch (doingType) {

                            case DoingTypeClass.DoDefault:

                                dissmissLoading();

                                ViewControl.AlphaTo1(pinPinBoxRefreshLayout);

                                break;

                            case DoingTypeClass.DoRefresh:

                                pinPinBoxRefreshLayout.setRefreshing(false);

                                break;

                            case DoingTypeClass.DoMoreData:
                                isDoingMore = false;
                                pbLoadMore.setVisibility(View.GONE);
                                pbLoadMore.progressiveStop();

                                break;

                        }
                    }

                    @Override
                    public void Success(int doingType) {
                        switch (doingType) {

                            case DoingTypeClass.DoDefault:

                                tvRemaining.setText(protocol99.getVoteLeft());
                                voteAdapter.notifyDataSetChanged();

                                break;

                            case DoingTypeClass.DoRefresh:
                                tvRemaining.setText(protocol99.getVoteLeft());
                                voteAdapter.notifyDataSetChanged();

                                break;

                            case DoingTypeClass.DoMoreData:

                                tvRemaining.setText(protocol99.getVoteLeft());


                                voteAdapter.notifyItemRangeInserted(protocol99.getItemAlbumList().size(), protocol99.getRangeCount());


                                break;


                        }
                    }

                    @Override
                    public void TimeOut(int doingType) {

                        switch (doingType) {

                            case DoingTypeClass.DoDefault:

                                doGetVote();

                                break;

                            case DoingTypeClass.DoRefresh:

                                doRefresh();

                                break;

                            case DoingTypeClass.DoMoreData:

                                doMoreData();

                                break;


                        }

                    }
                });
    }

    private void doGetVote() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        protocol99.GetList();

    }

    private void doRefresh() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        if (protocol99 != null && !protocol99.getTask().isCancelled()) {

            protocol99.getTask().cancel(true);

        }

        protocol99.Refresh();
    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        protocol99.LoadMore();
    }

    private void doVote(final int position) {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        protocol100 = new Protocol100(
                mActivity,
                id,
                token,
                event_id,
                itemAlbumList.get(position).getAlbum_id(),
                new Protocol100.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success(String vote_left) {

                        tvRemaining.setText(vote_left);

                        itemAlbumList.get(position).setHas_voted(true);

                        voteAdapter.notifyItemChanged(position);

                    }

                    @Override
                    public void TimeOut() {
                        doVote(position);
                    }
                }
        );

    }

    private void back() {
        finish();
        ActivityAnim.FinishAnim(mActivity);
    }

    private void cleanCache() {


        if (itemAlbumList != null) {

            int count = itemAlbumList.size();

            for (int i = 0; i < count; i++) {
                Picasso.with(mActivity.getApplicationContext()).invalidate(itemAlbumList.get(i).getCover());
                Picasso.with(mActivity.getApplicationContext()).invalidate(itemAlbumList.get(i).getUser_picture());
            }

            System.gc();

        }

    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if ((Object) protocol99.isSizeMax() != null && !protocol99.isSizeMax()) {
                MyLog.Set("e", mActivity.getClass(), "onLoad");

                if (isDoingMore) {
                    MyLog.Set("e", mActivity.getClass(), "正在讀取更多項目");
                    return;
                }

                doMoreData();

            } else {

                if (!protocol99.isNoDataToastAppeared()) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    protocol99.setNoDataToastAppeared(true);

                }

                MyLog.Set("e", mActivity.getClass(), "sizeMax");
            }
        }

    };


    @Override
    public void onClick(View v) {

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
    protected void onPause() {
        super.onPause();
        cleanCache();
    }


    @Override
    public void onDestroy() {

        if (protocol99 != null) {
            cancelTask(protocol99.getTask());
        }

        if (protocol100 != null) {
            cancelTask(protocol100.getTask());
        }


        Recycle.IMG(backImg);


        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        cleanCache();

        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


}

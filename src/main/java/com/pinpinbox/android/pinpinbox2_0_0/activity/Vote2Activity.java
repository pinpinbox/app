package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.SuperSwipeRefreshLayout;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerVoteAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ExLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSearch2;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol100_Vote;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol99_GetEventVoteList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class Vote2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;
    private ExLinearLayoutManager manager = null;
    private CountDownTimer countDownTimer;
    private InputMethodManager inputMethodManager;

    private Protocol99_GetEventVoteList protocol99;
    private Protocol100_Vote protocol100;

    private RecyclerVoteAdapter voteAdapter;

    private List<ItemAlbum> itemAlbumList;

    private SuperSwipeRefreshLayout pinPinBoxRefreshLayout;

    private SmoothProgressBar pbLoadMore, pbRefresh;
    private RecyclerView rvVote;
    private ImageView backImg, clearImg;
    private TextView tvRemaining;
    private View vHeader;
    private EditText edSearch;

    private String event_id;
    private String id, token;
    private String strSearch = "";

    private int deviceType;
    private int clickposition = -1;

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

        setSearch();

        doGetVote();


    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            event_id = bundle.getString(Key.event_id, "");
        }

    }

    private void init() {

//        if (SystemUtility.isTablet(getApplicationContext())) {
//            //平版
//            deviceType = SystemType.TABLE;
//            manager = new ExStaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        } else {
//            //手機
//            deviceType = SystemType.PHONE;
//            manager = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        }


        mActivity = this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();

        itemAlbumList = new ArrayList<>();

        edSearch = (EditText) findViewById(R.id.edSearch);
        tvRemaining = (TextView) findViewById(R.id.tvRemaining);
        backImg = (ImageView) findViewById(R.id.backImg);
        clearImg = (ImageView) findViewById(R.id.clearImg);
        rvVote = (RecyclerView) findViewById(R.id.rvVote);
        pinPinBoxRefreshLayout = (SuperSwipeRefreshLayout) findViewById(R.id.pinPinBoxRefreshLayout);


        pbRefresh = (SmoothProgressBar) findViewById(R.id.pbRefresh);
        pbRefresh.progressiveStop();
        pinPinBoxRefreshLayout.setRefreshView(findViewById(R.id.vRefreshAnim), pbRefresh);


        pbLoadMore = (SmoothProgressBar) findViewById(R.id.pbLoadMore);
        pbLoadMore.progressiveStop();


        mOnScrollListener.setInputStatus(inputMethodManager, edSearch);
        rvVote.addOnScrollListener(mOnScrollListener);


        /*********************************************************************************************************/


        vHeader = LayoutInflater.from(this).inflate(R.layout.header_2_0_0_title, null);

        TextView tvTitle = (TextView) vHeader.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.pinpinbox_2_0_0_title_entries);

        ViewControl.setMargins(tvTitle,
                DensityUtility.dip2px(getApplicationContext(), 12),
                DensityUtility.dip2px(getApplicationContext(), 134),
                0,
                DensityUtility.dip2px(getApplicationContext(), 32));

        TextUtility.setBold(tvTitle, true);


        backImg.setOnClickListener(this);
        clearImg.setOnClickListener(this);

    }

    private void setRecycler() {

        voteAdapter = new RecyclerVoteAdapter(mActivity, itemAlbumList, mOnScrollListener);
        rvVote.setAdapter(voteAdapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(voteAdapter);
        rvVote.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvVote.setLayoutManager(manager);

        RecyclerViewUtils.setHeaderView(rvVote, vHeader);

        voteAdapter.setOnRecyclerViewListener(new RecyclerVoteAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                clickposition = position;


                ActivityIntent.toAlbumInfo(
                        mActivity,
                        true, itemAlbumList.get(position).getAlbum_id(),
                        itemAlbumList.get(position).getCover(),
                        itemAlbumList.get(position).getImage_orientation(),
                        v.findViewById(R.id.coverImg)
                );


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
        protocol99 = new Protocol99_GetEventVoteList(
                mActivity,
                id,
                token,
                event_id,
                strSearch,
                itemAlbumList,
                new Protocol99_GetEventVoteList.TaskCallBack() {
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

                        voteAdapter.setSearchKeyCheck(strSearch);


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

    private void setSearch() {

        countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MyLog.Set("d", mActivity.getClass(), "timer =>" + (millisUntilFinished / 1000) + "");
            }

            @Override
            public void onFinish() {
                MyLog.Set("d", mActivity.getClass(), "timer => finish()");
                countDownTimer.cancel();

                if (edSearch.getText().toString().equals(strSearch)) {

                    MyLog.Set("d", mActivity.getClass(), "字串沒變更");

                } else {
                    strSearch = edSearch.getText().toString();
                    protocol99.setSearchKey(strSearch);
                    doRefresh();
                }
            }
        };


        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                MyLog.Set("d", FragmentSearch2.class, "afterTextChanged");


                if (s.toString().equals("")) {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }

//                    setDefaultData();

                    strSearch = s.toString();
                    protocol99.setSearchKey(strSearch);
                    doRefresh();

                    clearImg.setVisibility(View.GONE);

                } else {

                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        countDownTimer.start();

                        MyLog.Set("d", FragmentSearch2.class, "重新倒數");
                    }

                    clearImg.setVisibility(View.VISIBLE);


                }


            }
        });


    }

//    private void loadDataBegin() {
//        pbRefresh.setVisibility(View.VISIBLE);
//        pbRefresh.progressiveStart();
//        isLoading = true;
//    }
//
//    private void loadDataEnd() {
//        pbRefresh.progressiveStop();
//        pbRefresh.setVisibility(View.GONE);
//        isLoading = false;
//    }

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

        protocol100 = new Protocol100_Vote(
                mActivity,
                id,
                token,
                event_id,
                itemAlbumList.get(position).getAlbum_id(),
                new Protocol100_Vote.TaskCallBack() {
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

                        itemAlbumList.get(position).setEvent_join(itemAlbumList.get(position).getEvent_join() + 1);

                        voteAdapter.notifyItemChanged(position);

                    }

                    @Override
                    public void TimeOut() {
                        doVote(position);
                    }
                }
        );

    }

//    public void changeVoteStatus(boolean vote, String remain){
//
//        itemAlbumList.get(clickposition).setHas_voted(vote);
//        voteAdapter.notifyItemChanged(clickposition);
//
//        tvRemaining.setText(remain);
//
//    }

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

            case R.id.clearImg:

                edSearch.setText("");

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

package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.MapUtility;
import com.pinpinbox.android.Utility.StringUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.AVLoading.AVLoadingIndicatorView;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.ControlSizeScrollView;
import com.pinpinbox.android.Views.ControllableViewPager;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.PinchImageView;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.PhotoPageAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerReaderAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemPhoto;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickDragDismissListener;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.GiftAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.SnackManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.UrlClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.LinkText;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StatusControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.mode.LOG;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol108;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol109;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol111;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol13;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopBoard;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import uk.breedrapps.vimeoextractor.OnVimeoExtractionListener;
import uk.breedrapps.vimeoextractor.VimeoExtractor;
import uk.breedrapps.vimeoextractor.VimeoVideo;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/2/21.
 */
public class Reader2Activity extends DraggerActivity implements View.OnClickListener, LocationListener, ClickDragDismissListener.ActionUpListener {

    private Activity mActivity;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private MediaPlayer mediaPlayer;
    private Timer timer;
    private LocationManager mLocManager;
    private Location mLocation = null;
    private GoogleMap mapAlbum, mapPhoto;
    private PopupCustom popInfo, popPageMap, popMore, popSelectShare;
    private PopBoard board;

    private GetAlbumInfoTask getAlbumInfoTask;
    //    private CollectAlbumTask collectAlbumTask;
    private FirstCollectAlbumTask firstCollectAlbumTask;
    private GetPointTask getPointTask;
    private SendContributeTask sendContributeTask;
    private CheckShareTask checkShareTask;
    private ShareTask shareTask;
    private SendLikeTask sendLikeTask;
    private DeleteLikeTask deleteLikeTask;
    private Protocol13 protocol13;
    private Protocol108 protocol108;
    private Protocol109 protocol109;
    private Protocol111 protocol111;

    private ItemAlbum itemAlbum;
    private List<ItemPhoto> photoContentsList;

    private PhotoPageAdapter adapter;
    private RecyclerReaderAdapter recyclerReaderAdapter;

    private RelativeLayout rActionBar, rBottom, rPhoto;
    private ControllableViewPager vpReader;
    private RecyclerView rvReader;
    private LinearLayout linDescription;
    private TextView tvPageDescription, tvPage, tvCurrentPoint;
    private EditText edPoint;
    private ImageView backImg, autoplayImg, voiceImg, locationImg, messageImg, likeImg, moreImg;
    private SeekBar seekBar;


    private String album_id = "";
    private String id, token;
    private String event_id = "";
    private String strRecent = "[]";
    private String comfirmPoint = "0";
    private String event;
    private String strSpecialUrl;

    private int doingType;
    private int lastPosition = 0;
    private int photoTotalCount = 0;
    private int userPoint = 0;


    private double mLocLat; //緯度
    private double mLocLng; //經度

    private boolean isFBShareComplate = false;
    private boolean isPhotoMode = false;
    private boolean isAutoPlay = false;
    private boolean isAutoPlayIng = true;
    private boolean isPageAudioExist = false; //判斷單頁作品存在
    private boolean isAudioExist = false; //判斷整本作品音樂存在
    private boolean isSaveToRecent = false;
    private boolean isPlayAudio = true;
    private boolean soundEnable = true;


    private Handler autoScrollHandler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            vpReader.setCurrentItem(vpReader.getCurrentItem() + 1, true);
        }
    };
    private TimerTask timerTask;


    /*info*/
    private TextView tvAlbumName, tvAlbumDescription, tvAlbumUser;
    private ImageView closeImg;
    private LinearLayout linBackground;
    private RelativeLayout rMap;

    /*page location*/
    private TextView tvPageLocation;
    private ImageView closeMapImg;

    private boolean isNewCreate = false;
    private boolean isContribute = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_reader);

        setSwipeBackEnable(false);

//        getStatusControl().setStatusMode(StatusControl.DARK);

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setStatusDark();
        changeActivityStatusMode(StatusControl.DARK);

        setOnCreateContent();

        getBundle();

        init();

        initView();

        initInfoView();

        initPageMap();

        setRecycler();

        setListener();

        doGetAlbumInfo();


    }


    private void setOnCreateContent() {

        /*檢查網路*/
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        /*設置activity list*/
        SystemUtility.SysApplication.getInstance().addActivity(this);

//        /*設置facebook api*/
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//
//
//        /*設置facebook share api*/
//        shareDialog = new ShareDialog(this);
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//
//            @Override
//            public void onSuccess(Sharer.Result result) {
//                doShareTask();
//                MyLog.Set("d", mActivity.getClass(), "shareDialog => onSuccess");
//            }
//
//            @Override
//            public void onCancel() {
//                MyLog.Set("d", mActivity.getClass(), "shareDialog => onCancel");
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                MyLog.Set("d", mActivity.getClass(), "shareDialog => onError");
//            }
//        });

    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        album_id = bundle.getString(Key.album_id, "");
        event_id = bundle.getString(Key.event_id, "");
        strSpecialUrl = bundle.getString(Key.special, "");
        isNewCreate = bundle.getBoolean(Key.isNewCreate, false);
        isContribute = bundle.getBoolean(Key.isContribute, false);
    }

    private void init() {
        mActivity = this;

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
        strRecent = PPBApplication.getInstance().getData().getString(Key.recentAlbumdata, "[]");

        soundEnable = PPBApplication.getInstance().getData().getBoolean(Key.soundEnable, true);


        itemAlbum = new ItemAlbum();
        photoContentsList = new ArrayList<>();

        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    }

    private void initView() {
        rActionBar = (RelativeLayout) findViewById(R.id.rActionBar);
        rBottom = (RelativeLayout) findViewById(R.id.rBottom);
        rPhoto = (RelativeLayout) findViewById(R.id.rPhoto);
        vpReader = (ControllableViewPager) findViewById(R.id.vpReader);
        rvReader = (RecyclerView) findViewById(R.id.rvReader);
        linDescription = (LinearLayout) findViewById(R.id.linDescription);
        tvPageDescription = (TextView) findViewById(R.id.tvPageDescription);
        tvPage = (TextView) findViewById(R.id.tvPage);
        backImg = (ImageView) findViewById(R.id.backImg);
        autoplayImg = (ImageView) findViewById(R.id.autoplayImg);
        locationImg = (ImageView) findViewById(R.id.locationImg);
        voiceImg = (ImageView) findViewById(R.id.voiceImg);
        messageImg = (ImageView) findViewById(R.id.messageImg);
        likeImg = (ImageView) findViewById(R.id.likeImg);
        moreImg = (ImageView) findViewById(R.id.moreImg);
        seekBar = (SeekBar) findViewById(R.id.seekBar);


        ControlSizeScrollView svDescription = (ControlSizeScrollView) findViewById(R.id.svDescription);
        svDescription.setVerticalFadingEdgeEnabled(true);
        svDescription.setFadingEdgeLength(32);


//        try {
//            if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {
//
//                if (linDescription != null) {
//
//                    linDescription.setBackground(
//                            ScrimUtil.makeCubicGradientScrimDrawable(
//                                    Color.parseColor("#cc000000"), //颜色
//                                    16, //渐变层数
//                                    Gravity.BOTTOM)); //起始方向
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            private int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (mediaPlayer != null) {
                    progress = i;
                }

                MyLog.Set("d", mActivity.getClass(), "i => " + i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);

                    if (!mediaPlayer.isPlaying()) {
                        soundEnable = true; //設為true 將不受音笑開關的預設值
                        mediaPlayer.start();
                        voiceImg.setImageResource(R.drawable.ic200_audio_play_white);
                    }

                }


            }
        });


        autoplayImg.setOnClickListener(this);
        locationImg.setOnClickListener(this);
        messageImg.setOnClickListener(this);
        likeImg.setOnClickListener(this);
        moreImg.setOnClickListener(this);
        voiceImg.setOnClickListener(this);
        backImg.setOnClickListener(this);

        setClickable(false);


    }

    private void initInfoView() {

        popInfo = new PopupCustom(mActivity);
        popInfo.setPopup(R.layout.pop_2_0_0_readerinfo, R.style.pinpinbox_popupAnimation_bottom);

        View v = popInfo.getPopupView();
        linBackground = (LinearLayout) v.findViewById(R.id.linBackground);
        rMap = (RelativeLayout) v.findViewById(R.id.rMap);
        closeImg = (ImageView) v.findViewById(R.id.closeImg);
        tvAlbumName = (TextView) v.findViewById(R.id.tvAlbumName);
        tvAlbumDescription = (TextView) v.findViewById(R.id.tvAlbumDescription);
        tvAlbumUser = (TextView) v.findViewById(R.id.tvAlbumUser);

        try {

            SupportMapFragment infoMap = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            infoMap.getMapAsync(new AlbumMapCallBack());


        } catch (Exception e) {
            e.printStackTrace();
        }

        TextUtility.setBold(tvAlbumName, true);


        closeImg.setOnClickListener(this);
    }

    private void initPageMap() {

        popPageMap = new PopupCustom(mActivity);
        popPageMap.setPopup(R.layout.pop_2_0_0_map, R.style.pinpinbox_popupAnimation_bottom);
        View v = popPageMap.getPopupView();
        tvPageLocation = (TextView) v.findViewById(R.id.tvPageLocation);
        closeMapImg = (ImageView) v.findViewById(R.id.closeMapImg);

        try {

            SupportMapFragment pageMap = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapPageLocation);
            pageMap.getMapAsync(new PageMapCallBack());

        } catch (Exception e) {
            e.printStackTrace();
        }

        closeMapImg.setOnClickListener(this);


    }

    private class AlbumMapCallBack implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mapAlbum = googleMap;
            mapAlbum.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            UiSettings setting = mapAlbum.getUiSettings();
            setting.setTiltGesturesEnabled(true);
//            mapAlbum.setMyLocationEnabled(true);
        }
    }

    private class PageMapCallBack implements OnMapReadyCallback {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mapPhoto = googleMap;
            mapPhoto.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            UiSettings setting = mapPhoto.getUiSettings();
            setting.setTiltGesturesEnabled(true);
//            mapPhoto.setMyLocationEnabled(true);


        }
    }

    private void setClickable(boolean clickable) {
        autoplayImg.setClickable(clickable);
        locationImg.setClickable(clickable);
        voiceImg.setClickable(clickable);
        moreImg.setClickable(clickable);
    }

    private void setRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvReader.setLayoutManager(layoutManager);

        RecyclerView.ItemAnimator animator = rvReader.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

//        rvReader.getItemAnimator().setSupportsChangeAnimations(false);


        recyclerReaderAdapter = new RecyclerReaderAdapter(mActivity, photoContentsList);
        rvReader.setAdapter(recyclerReaderAdapter);

    }

    private void setPageAdapter() {

        adapter = null;

        adapter = new PhotoPageAdapter(mActivity, photoContentsList, itemAlbum);

        vpReader.setOffscreenPageLimit(3);
        vpReader.setAdapter(adapter);

    }

    private void setListener() {

        vpReader.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int page = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //****先執行 後執行onPageSelected

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }


                page = position;

                //更新縮圖選擇邊框
                photoContentsList.get(lastPosition).setSelect(false);
                photoContentsList.get(position).setSelect(true);
                recyclerReaderAdapter.notifyItemChanged(lastPosition);
                recyclerReaderAdapter.notifyItemChanged(position);
                rvReader.scrollToPosition(position);


                //銷毀前頁撥放器
                if (photoContentsList.get(lastPosition).getVideoView() != null) {
                    MyLog.Set("d", mActivity.getClass(), "重置videoView => " + lastPosition);
                    photoContentsList.get(lastPosition).getVideoView().reset();
//                    photoContentsList.get(lastPosition).setVideoView(null);
                }


                setPage(position);
                setPageDetail(position);
                setPageDescription(position);
                setPageLocation(position);
                setPhotoMode(position);
                resetPhotoSide(position);
                setAuto(position);

                if (isPageAudioExist) {

                    setPageAudio(position);

                }

                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1://正在滑動

                        if (isAutoPlay) {
                            autoScrollHandler.removeCallbacks(runnable);
                        }

                        break;

                    case 2://滑動完畢
                        //****先執行 後執行onPageSelected
                        MyLog.Set("d", mActivity.getClass(), "滑動完畢");

                        int p = vpReader.getCurrentItem();
                        if (p == page) {

                            if (isAutoPlay && isAutoPlayIng) {
                                int duration = photoContentsList.get(page).getDuration();
                                setAutoPlay(duration, page);
                            }

                        } else {

                            if (isPageAudioExist) {
                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.stop();
                                    }
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                }
                            }
                        }

                        break;

                }
            }


        });


        recyclerReaderAdapter.setOnRecyclerViewListener(new RecyclerReaderAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

//                if (!itemAlbum.isOwn() && position == photoContentsList.size() - 1) {
//                    return;
//                }
                vpReader.setCurrentItem(position, false);
            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });
    }

    private void back() {

        if (isNewCreate) {

            if (isContribute) {
                final DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setStyle(DialogStyleClass.CHECK);
                d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_cancel_submission_go_to_events_page);
                d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_button_exit);

                if (strSpecialUrl != null && !strSpecialUrl.equals("") && !strSpecialUrl.equals("null")) {
                    d.getTvRightOrBottom().setVisibility(View.VISIBLE);
                    d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_eventexchange);
                    d.getTvRightOrBottom().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toSpecialUrl();
                        }
                    });
                } else {
                    d.getTvRightOrBottom().setVisibility(View.GONE);
                }


                d.getTvLeftOrTop().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        toMyCollect();
                    }
                });


                d.show();
            } else {
                toMyCollect();
            }


        } else {

            finish();
            ActivityAnim.FinishAnim(mActivity);
        }

    }

    private void toMyCollect() {

        SnackManager.showCustomSnack();

        finish();
        ActivityAnim.FinishAnim(mActivity);
    }

    private void toSpecialUrl() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(strSpecialUrl);
        intent.setData(content_url);
        startActivity(intent);
    }

    private void setPage(int position) {
        if (!itemAlbum.isOwn() && position == photoTotalCount) {
            return;
        }
        tvPage.setText((position + 1) + " / " + photoTotalCount);

    }


    private void setVideo(final View vPage, final String videoTarget, final int position) {

        RelativeLayout rVideo = (RelativeLayout) vPage.findViewById(R.id.rVideo);
        rVideo.setVisibility(View.VISIBLE);

        VideoView videoView = null;
        if (photoContentsList.get(position).getVideoView() == null) {
            videoView = (VideoView) vPage.findViewById(R.id.videoview);
            photoContentsList.get(position).setVideoView(videoView);
        } else {
            videoView = photoContentsList.get(position).getVideoView();
        }


        vPage.findViewById(R.id.vModeChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setModeChange();
            }
        });


        final TextView tvRefreshVideo = (TextView)vPage.findViewById(R.id.tvRefreshVideo);

        try{
            Uri uri = null;
            uri = Uri.parse(videoTarget);
            videoView.setVideoURI(uri);

            final boolean[] isEnd = {false};

            final VideoView finalVideoView = videoView;
            videoView.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared() {
                    tvRefreshVideo.setVisibility(View.GONE);
                    finalVideoView.start();
                }
            });

            videoView.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion() {
                    isEnd[0] = true;
                }
            });

            videoView.getVideoControls().setButtonListener(new VideoControlsButtonListener() {
                @Override
                public boolean onPlayPauseClicked() {

                    MyLog.Set("e", mActivity.getClass(), "onPlayPauseClicked");

                    if (isEnd[0]) {
                        finalVideoView.restart();
                        isEnd[0] = false;
                    }

                    return false;
                }

                @Override
                public boolean onPreviousClicked() {
                    MyLog.Set("e", this.getClass(), "onPreviousClicked");
                    return false;
                }

                @Override
                public boolean onNextClicked() {
                    MyLog.Set("e", this.getClass(), "onNextClicked");
                    return false;
                }

                @Override
                public boolean onRewindClicked() {
                    MyLog.Set("e", this.getClass(), "onRewindClicked");
                    return false;
                }

                @Override
                public boolean onFastForwardClicked() {
                    MyLog.Set("e", this.getClass(), "onFastForwardClicked");
                    return false;
                }
            });

        }catch (Exception e){

            videoView.release();
            photoContentsList.get(position).setVideoView(null);

            tvRefreshVideo.setVisibility(View.VISIBLE);
            tvRefreshVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvRefreshVideo.setVisibility(View.GONE);
                    setVideo(vPage, videoTarget, position);
                }
            });


        }

    }

    private void setVideoClick(ImageView centerImg, final int position) {
        centerImg.setImageResource(R.drawable.click_2_0_0_video_white);
        centerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }

                PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_play_audio_ready);

                checkAudioType(position);
            }
        });

    }

    private void setPageDetail(final int position) {

        final View vPage = vpReader.findViewById(position);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                PinPinToast.ShowToast(mActivity, "重新啟動閱讀器");

//                Bundle bundle = new Bundle();
//
//                bundle.putString(Key.album_id, album_id);
//                bundle.putString(Key.event_id, event_id);
//                bundle.putString(Key.special, strSpecialUrl);
//                bundle.putBoolean(Key.isNewCreate, isNewCreate);
//                bundle.putBoolean(Key.isContribute, isContribute);
//
//
//                startActivity(new Intent(mActivity, Reader2Activity.class).putExtras(bundle));
//                finish();
//
//            }
//        },3000);


        if (vPage == null || vpReader == null) {
            DialogV2Custom.BuildUnKnow(mActivity, "setPageDetail error");
            return;
        }


        ImageView centerImg = (ImageView) vPage.findViewById(R.id.centerImg);

        /*移置adapter執行*/
//        final View vType = vPage.findViewById(R.id.vType);

        final LinearLayout linExchange = (LinearLayout) vPage.findViewById(R.id.linExchange);

        /*移置adapter執行*/
//        final ImageView refreshImg = (ImageView) vPage.findViewById(R.id.refreshImg);
//        refreshImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (ClickUtils.ButtonContinuousClick()) {
//                    return;
//                }
//
//
//                final PinchImageView picImg = (PinchImageView) vPage.findViewById(R.id.photoImg);
//
//                String url = photoContentsList.get(position).getImage_url();
//
//                if (url != null && !url.equals("") && !url.equals("null") && !url.isEmpty()) {
//
//                    Picasso.with(getApplicationContext())
//                            .load(url)
//                            .config(Bitmap.Config.RGB_565)
//                            .error(R.drawable.bg_2_0_0_no_image)
//                            .tag(mActivity.getApplicationContext())
//                            .into(picImg, new Callback() {
//                                @Override
//                                public void onSuccess() {
//                                    refreshImg.setVisibility(View.GONE);
//                                }
//
//                                @Override
//                                public void onError() {
//                                    MyLog.Set("d", Reader2Activity.class, "new Callback => onError");
//                                    refreshImg.setVisibility(View.VISIBLE);
//                                }
//                            });
//
//                } else {
//
//                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_photo_does_not_exist);
//
//                }
//
//
//            }
//        });

        try {

            String usefor = photoContentsList.get(position).getUsefor();
            MyLog.Set("d", getClass(), "usefor => " + usefor);
            switch (usefor) {

                case "image":


                    break;

                case "video":


//                    setVideoClick(centerImg, position);


                    String videoRefer = photoContentsList.get(position).getVideo_refer();
                    final String videoTarget = photoContentsList.get(position).getVideo_target();


                    if (videoRefer.equals("file")) {


                        setVideo(vPage, videoTarget, position);


                    } else if (videoRefer.equals("embed")) {

                        String youtubeUrl = StringUtil.checkYoutubeId(videoTarget);
                        if (youtubeUrl == null || youtubeUrl.equals("null")) {

                            if (!StringUtil.containsString(videoTarget, "vimeo") &&
                                    !StringUtil.containsString(videoTarget, "dailymotion") &&
                                    !StringUtil.containsString(videoTarget, "facebook")) {

                                setVideo(vPage, videoTarget, position);

                            } else {

                                setVideoClick(centerImg, position);

                            }

                        } else {

                            setVideoClick(centerImg, position);

                        }

                    }


                    break;

                case "exchange":

                    if (!itemAlbum.isOwn()) {
                        isNoOwnShowCollectType(vPage);
                    } else {

//                        vType.setVisibility(View.VISIBLE);

                        linExchange.setVisibility(View.VISIBLE);

                        //依linExchange判斷是否呼叫接口

                        doGetExchange(vPage, position, linExchange);

                    }
                    break;

                case "slot":


                    if (!itemAlbum.isOwn()) {
                        isNoOwnShowCollectType(vPage);
                    } else {

//                        vType.setVisibility(View.VISIBLE);

                        JSONArray jsonArray = new JSONArray(PPBApplication.getInstance().getData().getString(Key.slot_photo_id, "[]"));

                        boolean isSlotted = false;


                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = (JSONObject) jsonArray.get(i);

                                String sPhoto_id = object.getString(Key.photo_id);

                                if (sPhoto_id.equals(photoContentsList.get(position).getPhoto_id() + "")) {
                                    isSlotted = true;
                                    break;
                                }
                            }
                        }

                        if (isSlotted) {
                            doSlot(vPage, position, linExchange);
                        } else {


                            GiftAnim giftAnim = new GiftAnim(mActivity,
                                    vPage,
                                    new GiftAnim.Call() {
                                        @Override
                                        public void onEnd() {
                                            MyLog.Set("d", this.getClass(), "GiftAnim onEnd");
                                            doSlot(vPage, position, linExchange);
                                        }
                                    });





                        }


                    }


                    break;

                case "lastPage":


                    //最後一頁
                    LinearLayout linLastPage = (LinearLayout) vPage.findViewById(R.id.linLastPage);
                    linLastPage.setVisibility(View.VISIBLE);

                    LinearLayout linSponsor = (LinearLayout) vPage.findViewById(R.id.linSponsor);
                    TextView tvTitle = (TextView) vPage.findViewById(R.id.tvTitle);
                    tvCurrentPoint = (TextView) vPage.findViewById(R.id.tvCurrentPoint);
                    edPoint = (EditText) vPage.findViewById(R.id.edPoint);
                    TextView tvClick = (TextView) vPage.findViewById(R.id.tvClick);

                    TextUtility.setBold(tvTitle, true);

                    ImageView lastPageImg = (ImageView) vPage.findViewById(R.id.lastPageImg);


                    if (itemAlbum.getPoint() == 0) {
                        tvCurrentPoint.setVisibility(View.GONE);
                        if (photoContentsList.size() - 1 == itemAlbum.getCount_photo()) {

                            lastPageImg.setVisibility(View.GONE);
                            tvTitle.setText(R.string.pinpinbox_2_0_0_dialog_message_read_done);
                            tvClick.setBackgroundResource(R.drawable.click_2_0_0_grey_third_radius);
                            tvClick.setText(R.string.pinpinbox_2_0_0_button_exit);
                            tvClick.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

                            tvClick.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    back();
                                }
                            });

                            return;

                        } else {

                            lastPageImg.setVisibility(View.VISIBLE);
                            lastPageImg.setImageResource(R.drawable.bg200_preview_collect);

                            tvTitle.setText(R.string.pinpinbox_2_0_0_dialog_message_read_all_content);
                            tvClick.setText(R.string.pinpinbox_2_0_0_button_collect);

                            tvClick.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    checkToCollectAlbum(itemAlbum.getPoint() + "");
                                }
                            });


                        }
                    }


                    if (itemAlbum.getPoint() > 0) {

                        lastPageImg.setVisibility(View.VISIBLE);
                        lastPageImg.setImageResource(R.drawable.bg200_preview_sponsor);

                        linSponsor.setVisibility(View.VISIBLE);

                        setCurrentPoint();

                        edPoint.setHint(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_lowest_point) + itemAlbum.getPoint());

                        tvClick.setText(getResources().getString(R.string.pinpinbox_2_0_0_button_sponsor));
//                        + itemAlbum.getPoint() + "P"

                        if (photoContentsList.size() - 1 == itemAlbum.getCount_photo()) {
                            tvTitle.setText(R.string.pinpinbox_2_0_0_dialog_message_sponsor_by_like_album);

                        } else {
                            tvTitle.setText(R.string.pinpinbox_2_0_0_dialog_message_sponsor_point_to_read_all_content);
                        }


                        tvClick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //檢查第一位數
                                String p = edPoint.getText().toString();
                                if (p.equals("")) {
                                    p = "0";
                                }
                                if (p.length() > 1 && p.substring(0, 1).equals("0")) {
                                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_first_number_can_not_set_zero);
                                    return;
                                }


                                //檢查輸入空值
                                if (edPoint.getText().toString().equals("")) {
                                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_input_sponsor_count);
                                    return;
                                }


                                //檢查最低額度輸入
                                int inputPoint = StringIntMethod.StringToInt(edPoint.getText().toString());
                                if (inputPoint < itemAlbum.getPoint()) {
                                    PinPinToast.showErrorToast(mActivity, mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_lowest_point) + itemAlbum.getPoint());
                                    return;
                                }

                                //檢查現有P點是否足夠
                                int currentPoint = StringIntMethod.StringToInt(PPBApplication.getInstance().getData().getString(Key.point, "0"));
                                if (inputPoint > currentPoint) {

                                    checkBuyPoint();
                                    return;

                                }

                                checkToCollectAlbum(edPoint.getText().toString());

                            }
                        });

                    }


                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private HashMap<Integer, Protocol108> protocol108HashMap;

    @SuppressLint("UseSparseArrays")
    private void doGetExchange(final View vPage, final int position, final LinearLayout linExchange) {


        /*error*/
        final AVLoadingIndicatorView loading = (AVLoadingIndicatorView) vPage.findViewById(R.id.vRefreshExchange);
        final LinearLayout linTimeout = (LinearLayout) vPage.findViewById(R.id.linTimeout);
        final TextView tvAgain = (TextView) vPage.findViewById(R.id.tvAgain);


        /*success*/
        final TextView tvExchangeName = (TextView) vPage.findViewById(R.id.tvExchangeName);
        final TextView tvExchangeDescription = (TextView) vPage.findViewById(R.id.tvExchangeDescription);
        final RoundCornerImageView exchangeImg = (RoundCornerImageView) vPage.findViewById(R.id.exchangeImg);

        /*bottom*/
        final TextView tvAddToExchangeList = (TextView) vPage.findViewById(R.id.tvAddToExchangeList);
        final TextView tvChange = (TextView) vPage.findViewById(R.id.tvChange);
        final RelativeLayout rAddToExchangeList = (RelativeLayout) vPage.findViewById(R.id.rAddToExchangeList);


        /*end*/
        final TextView tvExchangeEnd = (TextView) vPage.findViewById(R.id.tvExchangeEnd);

        TextUtility.setBold(tvExchangeName, true);
        TextUtility.setBold(tvAddToExchangeList, true);
        TextUtility.setBold(tvChange, true);
        TextUtility.setBold(tvExchangeEnd, true);


        if (protocol108HashMap == null) {
            protocol108HashMap = new HashMap<>();
        }

        if (protocol108HashMap.size() > 0 && !protocol108HashMap.get(position).isCancelled()) {
            cancelTask(protocol108HashMap.get(position));
            protocol108HashMap.remove(position);

        }


        protocol108 = new Protocol108(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                photoContentsList.get(position).getPhoto_id() + "",
                new Protocol108.TaskCallBack() {

                    private void showContents(ItemExchange itemExchange) {

                        Picasso.with(mActivity.getApplicationContext())
                                .load(itemExchange.getImage())
                                .config(Bitmap.Config.RGB_565)
                                .error(R.drawable.bg_2_0_0_no_image)
                                .tag(mActivity.getApplicationContext())
                                .into(exchangeImg);

                        linExchange.setVisibility(View.VISIBLE);

                        if (linExchange.getAlpha() == 0f) {
                            ViewControl.AlphaTo1(linExchange);
                        } else {
                            linExchange.setAlpha(1f);
                        }

                        tvExchangeName.setText(itemExchange.getName());

                        tvExchangeDescription.setText(itemExchange.getDescription());

                    }

                    private void canExchange(final ItemExchange itemExchange) {

                        showContents(itemExchange);

                        tvChange.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vv) {
                                if (ClickUtils.ButtonContinuousClick_1s()) {
                                    return;
                                }

                                exchangeImg.setTransitionName(itemExchange.getImage());

                                itemExchange.setPhoto_id(photoContentsList.get(position).getPhoto_id());

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("exchangeItem", itemExchange);
                                bundle.putBoolean("isExchanged", false);


                                Intent intent = new Intent(mActivity, ExchangeInfo2Activity.class).putExtras(bundle);

                                linExchange.setTransitionName("bg_exchange");

                                ActivityOptionsCompat option1 = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(mActivity,
                                                linExchange,
                                                ViewCompat.getTransitionName(linExchange));

                                ActivityOptionsCompat option2 = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(mActivity,
                                                exchangeImg,
                                                ViewCompat.getTransitionName(exchangeImg));

                                option2.update(option1);

                                startActivity(intent, option2.toBundle());

                            }
                        });


                        if (itemExchange.isIs_existing()) {
                            isInExchangeList();
                        } else {

                            /*依狀態判定是否可以點擊*/
                            rAddToExchangeList.setOnClickListener(new View.OnClickListener() {

                                private void doAddToExchangeList() {

                                    if (!HttpUtility.isConnect(mActivity)) {
                                        setNoConnect();
                                        return;
                                    }

                                    protocol109 = new Protocol109(
                                            mActivity,
                                            PPBApplication.getInstance().getId(),
                                            PPBApplication.getInstance().getToken(),
                                            photoContentsList.get(position).getPhoto_id() + "",
                                            new Protocol109.TaskCallBack() {
                                                @Override
                                                public void Prepare() {
                                                    startLoading();
                                                }

                                                @Override
                                                public void Post() {
                                                    dissmissLoading();
                                                }

                                                @Override
                                                public void Success() {
                                                    isInExchangeList();
                                                }

                                                @Override
                                                public void TimeOut() {
                                                    doAddToExchangeList();
                                                }
                                            }
                                    );


                                }

                                @Override
                                public void onClick(View v) {
                                    if (ClickUtils.ButtonContinuousClick()) {
                                        return;
                                    }
                                    doAddToExchangeList();

                                }
                            });

                        }
                    }

                    private void isInExchangeList() {
                        tvAddToExchangeList.setText(R.string.pinpinbox_2_0_0_other_text_is_in_exchange_list);
                        tvAddToExchangeList.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        rAddToExchangeList.setClickable(false);
                    }

                    private void gained() {

                        rAddToExchangeList.setVisibility(View.GONE);

                        ScrollView svExchange = (ScrollView) vPage.findViewById(R.id.svExchange);
                        svExchange.setPadding(0, 0, 0, 0);


                        tvChange.setBackgroundResource(R.drawable.border_2_0_0_white_radius);
                        tvChange.setText(R.string.pinpinbox_2_0_0_itemtype_exchange_done);
                        tvChange.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        tvChange.setClickable(false);
                    }

                    private void expired(){
                        tvExchangeEnd.setVisibility(View.VISIBLE);

                        if (tvExchangeEnd.getAlpha() == 0f) {
                            ViewControl.AlphaTo1(tvExchangeEnd);
                        } else {
                            tvExchangeEnd.setAlpha(1f);
                        }

                    }

                    @Override
                    public void Prepare() {

                        protocol108HashMap.put(position, protocol108);

                        linTimeout.setVisibility(View.GONE);
                        linTimeout.setAlpha(0f);

                        loading.smoothToShow();

                    }

                    @Override
                    public void Post() {

                        protocol108HashMap.remove(position);
                        loading.smoothToHide();

                    }


                    @Override
                    public void Success(final ItemExchange itemExchange) {
                        canExchange(itemExchange);
                    }

                    @Override
                    public void isBelongUser(ItemExchange itemExchange) {
                        canExchange(itemExchange);
                    }

                    @Override
                    public void IsGained(ItemExchange itemExchange) {
                        showContents(itemExchange);

                        gained();
                    }

                    @Override
                    public void IsExpired(){
                        expired();
                    }


                    @Override
                    public void TimeOut() {


                        if (linExchange.getVisibility() == View.VISIBLE) {
                            linExchange.setVisibility(View.GONE);
                            linExchange.setAlpha(0f);
                        }


                        linTimeout.setVisibility(View.VISIBLE);
                        ViewControl.AlphaTo1(linTimeout);

                        tvAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vv) {
                                if (ClickUtils.ButtonContinuousClick_1s()) {
                                    return;
                                }
                                doGetExchange(vPage, position, linExchange);
                            }
                        });


                    }
                }

        );

    }

    private void doSlot(final View vPage, final int position, final LinearLayout linExchange) {


        /*error*/
        final AVLoadingIndicatorView loading = (AVLoadingIndicatorView) vPage.findViewById(R.id.vRefreshExchange);
        final LinearLayout linTimeout = (LinearLayout) vPage.findViewById(R.id.linTimeout);
        final TextView tvAgain = (TextView) vPage.findViewById(R.id.tvAgain);


        /*success*/
        final TextView tvExchangeName = (TextView) vPage.findViewById(R.id.tvExchangeName);
        final TextView tvExchangeDescription = (TextView) vPage.findViewById(R.id.tvExchangeDescription);
        final RoundCornerImageView exchangeImg = (RoundCornerImageView) vPage.findViewById(R.id.exchangeImg);

        /*bottom*/
        final TextView tvAddToExchangeList = (TextView) vPage.findViewById(R.id.tvAddToExchangeList);
        final TextView tvChange = (TextView) vPage.findViewById(R.id.tvChange);
        final RelativeLayout rAddToExchangeList = (RelativeLayout) vPage.findViewById(R.id.rAddToExchangeList);


        /*end*/
        final TextView tvExchangeEnd = (TextView) vPage.findViewById(R.id.tvExchangeEnd);

        TextUtility.setBold(tvExchangeName, true);
        TextUtility.setBold(tvAddToExchangeList, true);
        TextUtility.setBold(tvChange, true);
        TextUtility.setBold(tvExchangeEnd, true);


        protocol111 = new Protocol111(
                mActivity,
                PPBApplication.getInstance().getId(),
                PPBApplication.getInstance().getToken(),
                photoContentsList.get(position).getPhoto_id() + "",
                getSharedPreferences(SharedPreferencesDataClass.deviceDetail, Activity.MODE_PRIVATE).getString("deviceid", ""),
                new Protocol111.TaskCallBack() {

                    private void showContents(ItemExchange itemExchange) {


                        Picasso.with(mActivity.getApplicationContext())
                                .load(itemExchange.getImage())
                                .config(Bitmap.Config.RGB_565)
                                .error(R.drawable.bg_2_0_0_no_image)
                                .tag(mActivity.getApplicationContext())
                                .into(exchangeImg);

                        linExchange.setVisibility(View.VISIBLE);

                        if (linExchange.getAlpha() == 0f) {
                            ViewControl.AlphaTo1(linExchange);
                        } else {
                            linExchange.setAlpha(1f);
                        }

                        if(itemExchange.getName()!=null && !itemExchange.getName().equals("null") && !itemExchange.getName().equals("")){
                            tvExchangeName.setText(itemExchange.getName());
                        }else {
                            tvExchangeName.setVisibility(View.VISIBLE);
                        }


                        tvExchangeDescription.setText(itemExchange.getDescription());


                        if(itemExchange.isUseless_award()){
                            tvChange.setVisibility(View.GONE);
                            rAddToExchangeList.setVisibility(View.GONE);
                            ScrollView svExchange = (ScrollView) vPage.findViewById(R.id.svExchange);
                            svExchange.setPadding(0, 0, 0, 0);

                        }else {
                            tvChange.setVisibility(View.VISIBLE);
                        }


                        /*本地保存*/
                        save();


                    }

                    private void canExchange(final ItemExchange itemExchange) {

                        tvChange.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ClickUtils.ButtonContinuousClick_1s()) {
                                    return;
                                }

                                exchangeImg.setTransitionName(itemExchange.getImage());
                                linExchange.setTransitionName("bg_exchange");

                                itemExchange.setPhoto_id(photoContentsList.get(position).getPhoto_id());

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("exchangeItem", itemExchange);
                                bundle.putBoolean("isExchanged", false);
                                bundle.putBoolean("isSlotType", true);

                                Intent intent = new Intent(mActivity, ExchangeInfo2Activity.class).putExtras(bundle);


                                ActivityOptionsCompat option1 = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(mActivity,
                                                linExchange,
                                                ViewCompat.getTransitionName(linExchange));

                                ActivityOptionsCompat option2 = ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(mActivity,
                                                exchangeImg,
                                                ViewCompat.getTransitionName(exchangeImg));

                                option2.update(option1);

                                startActivity(intent, option2.toBundle());

                            }
                        });


                        if (itemExchange.isIs_existing()) {
                            isInExchangeList();
                        } else {

                            /*依狀態判定是否可以點擊*/
                            rAddToExchangeList.setOnClickListener(new View.OnClickListener() {

                                private void doAddToExchangeList() {

                                    if (!HttpUtility.isConnect(mActivity)) {
                                        setNoConnect();
                                        return;
                                    }

                                    protocol109 = new Protocol109(
                                            mActivity,
                                            PPBApplication.getInstance().getId(),
                                            PPBApplication.getInstance().getToken(),
                                            photoContentsList.get(position).getPhoto_id() + "",
                                            new Protocol109.TaskCallBack() {
                                                @Override
                                                public void Prepare() {
                                                    startLoading();
                                                }

                                                @Override
                                                public void Post() {
                                                    dissmissLoading();
                                                }

                                                @Override
                                                public void Success() {
                                                    isInExchangeList();
                                                }

                                                @Override
                                                public void TimeOut() {
                                                    doAddToExchangeList();
                                                }
                                            }
                                    );


                                }

                                @Override
                                public void onClick(View v) {
                                    if (ClickUtils.ButtonContinuousClick()) {
                                        return;
                                    }
                                    doAddToExchangeList();

                                }
                            });

                        }
                    }

                    private void isInExchangeList() {
                        tvAddToExchangeList.setText(R.string.pinpinbox_2_0_0_other_text_is_in_exchange_list);
                        tvAddToExchangeList.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        rAddToExchangeList.setClickable(false);
                    }

                    private void gained() {

                        rAddToExchangeList.setVisibility(View.GONE);

                        ScrollView svExchange = (ScrollView) vPage.findViewById(R.id.svExchange);
                        svExchange.setPadding(0, 0, 0, 0);

                        tvChange.setBackgroundResource(R.drawable.border_2_0_0_white_radius);
                        tvChange.setText(R.string.pinpinbox_2_0_0_itemtype_exchange_done);
                        tvChange.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                        tvChange.setClickable(false);
                    }

                    private void expired(){

                        save();

                        tvExchangeEnd.setVisibility(View.VISIBLE);

                        if (tvExchangeEnd.getAlpha() == 0f) {
                            ViewControl.AlphaTo1(tvExchangeEnd);
                        } else {
                            tvExchangeEnd.setAlpha(1f);
                        }

                    }

                    private void save(){
                        try {
                            JSONObject object = new JSONObject();
                            object.put(Key.slot_photo_id, photoContentsList.get(position).getPhoto_id() + "");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            /*獲取原排序array*/
                            JSONArray jsonCookieArray = new JSONArray(PPBApplication.getInstance().getData().getString(Key.slot_photo_id, "[]"));

                            /*判斷是否在原排序內*/
                            boolean isPhotoIdExist = false;
                            for (int i = 0; i < jsonCookieArray.length(); i++) {
                                JSONObject obj = (JSONObject) jsonCookieArray.get(i);

                                String photo_id = obj.getString(Key.photo_id);
                                if (photo_id != null && photo_id.equals(photoContentsList.get(position).getPhoto_id() + "")) {
                                    isPhotoIdExist = true;
                                    break;
                                }
                            }

                            if (isPhotoIdExist) {
                                return;
                            } else {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put(Key.photo_id, photoContentsList.get(position).getPhoto_id() + "");
                                jsonCookieArray.put(jsonObject);
                            }

                            PPBApplication.getInstance().getData().edit().putString(Key.slot_photo_id, jsonCookieArray.toString()).commit();
                            Logger.json(PPBApplication.getInstance().getData().getString(Key.slot_photo_id, "[]"));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void Prepare() {

                        linTimeout.setVisibility(View.GONE);
                        linTimeout.setAlpha(0f);

                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success(ItemExchange itemExchange) {

                        showContents(itemExchange);

                        canExchange(itemExchange);
                    }

                    @Override
                    public void isBelongUser(ItemExchange itemExchange) {

                        showContents(itemExchange);
                        canExchange(itemExchange);
                    }

                    @Override
                    public void IsGained(ItemExchange itemExchange) {
                        showContents(itemExchange);
                        gained();
                    }

                    @Override
                    public void IsSlot(ItemExchange itemExchange) {

                        showContents(itemExchange);
                        canExchange(itemExchange);
                    }

                    @Override
                    public void IsExpired() {
                        expired();
                    }

                    @Override
                    public void Fail() {

                        vPage.findViewById(R.id.rGift).animate()
                                .alpha(1f)
                                .setDuration(600)
                                .start();

                    }

                    @Override
                    public void TimeOut() {

                        MyLog.Set("e", mActivity.getClass(), "-----TimeOut");


                        if (linExchange.getVisibility() == View.VISIBLE) {
                            linExchange.setVisibility(View.GONE);
                            linExchange.setAlpha(0f);
                        }


                        linTimeout.setVisibility(View.VISIBLE);
                        ViewControl.AlphaTo1(linTimeout);

                        tvAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ClickUtils.ButtonContinuousClick_1s()) {
                                    return;
                                }
                                doSlot(vPage, position, linExchange);
                            }
                        });

                    }
                });


    }

    private void setCurrentPoint() {

        if (tvCurrentPoint != null) {

            tvCurrentPoint.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_current_point) + PPBApplication.getInstance().getData().getString(Key.point, ""));

        }

    }

    private void checkAudioType(int position) {

        String videoRefer = photoContentsList.get(position).getVideo_refer();
        final String videoTarget = photoContentsList.get(position).getVideo_target();

        MyLog.Set("d", getClass(), "videoRefer => " + videoRefer);

        switch (videoRefer) {

            case "file":
                toPlayVideo(videoTarget);
                break;

            case "embed":
                String youtubeUrl = StringUtil.checkYoutubeId(videoTarget);
                try {
                    if (youtubeUrl == null || youtubeUrl.equals("null")) {
                        if (StringUtil.containsString(videoTarget, "vimeo")) {
                            MyLog.Set("d", mActivity.getClass(), "vimeo播放 => " + videoTarget);
                            VimeoExtractor.getInstance().fetchVideoWithURL(videoTarget, null, new OnVimeoExtractionListener() {
                                @Override
                                public void onSuccess(VimeoVideo video) {
                                    MyLog.Set("d", mActivity.getClass(), "video.getStreams().toString() =>" + video.getStreams().toString());
                                    String stream = "";
                                    stream = video.getStreams().get("1080p");
                                    if (stream == null || stream.equals("null")) {

                                        stream = video.getStreams().get("720p");

                                        if (stream == null || stream.equals("null")) {

                                            stream = video.getStreams().get("540p");

                                            if (stream == null || stream.equals("null")) {
                                                stream = video.getStreams().get("360p");
                                            }

                                        }


                                    }

                                    MyLog.Set("d", mActivity.getClass(), "stream => " + stream);

                                    if (stream == null) {

                                        Bundle bundle = new Bundle();
                                        bundle.putString(Key.url, videoTarget);
                                        Intent intent = new Intent(mActivity, WebView2Activity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        ActivityAnim.StartAnim(mActivity);

                                    } else {
                                        toPlayVideo(stream);
                                    }


                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                }
                            });


                        } else if (StringUtil.containsString(videoTarget, "dailymotion")) {

//                            PinPinToast.ShowToast(mActivity, "dailymotion");

                            toPlayVideo(videoTarget);

                        } else {
                            toPlayVideo(videoTarget);
                        }


                    } else {
                        MyLog.Set("d", mActivity.getClass(), "youtube播放 => " + youtubeUrl);
                        Intent intent = new Intent(Reader2Activity.this, YouTubeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("path", videoTarget);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        ActivityAnim.StartAnim(mActivity);
                    }
                } catch (Exception e) {
                    toPlayVideo(videoTarget);
                    e.printStackTrace();
                }

                break;
            case "none":
                MyLog.Set("d", getClass(), "play mode => none");
                break;
            case "system":
                MyLog.Set("d", getClass(), "play mode => system");
                break;
        }
    }

    private void toPlayVideo(String url) {

        ActivityIntent.toVideoPlay(mActivity, url);
    }

    private void isNoOwnShowCollectType(View parent) {

//        vType.setVisibility(View.VISIBLE);
        LinearLayout linCollect = (LinearLayout) parent.findViewById(R.id.linCollect);
        linCollect.setVisibility(View.VISIBLE);
        TextView tvCollect = (TextView) parent.findViewById(R.id.tvCollect);
        TextUtility.setBold(tvCollect, true);

        if (itemAlbum.getPoint() > 0) {


//          tvCollect.setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_button_sponsor) + itemAlbum.getPoint() + "P");
            tvCollect.setText(R.string.pinpinbox_2_0_0_button_sponsor);

        } else {

            tvCollect.setText(R.string.pinpinbox_2_0_0_button_collect);
        }

        tvCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (itemAlbum.getPoint() > 0) {

                    vpReader.setCurrentItem(photoContentsList.size());

                } else {
                    doCollectAlbum();
                }

            }
        });

    }

    public void setModeChange() {
        if (!isPhotoMode) {
            //一般模式 => 相片模式
            rBottom.setVisibility(View.GONE);
            rActionBar.setVisibility(View.GONE);

            isPhotoMode = true;
        } else {
            //相片模式 => 一般模式

            rBottom.setVisibility(View.VISIBLE);
            rActionBar.setVisibility(View.VISIBLE);

            isPhotoMode = false;
        }
    }

    private void setPhotoMode(int position) {

        if (vpReader == null) {
            DialogV2Custom.BuildUnKnow(mActivity, "setPhotoMode error");
            return;
        }

        if (vpReader.findViewById(position) != null) {

            View v = vpReader.findViewById(position);

            PinchImageView photoImg = (PinchImageView) v.findViewById(R.id.photoImg);

            photoImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setModeChange();
                }
            });
        }

    }

    private void setPageDescription(int position) {

        String strDescription = photoContentsList.get(position).getDescription();

        LinkText.set(mActivity, tvPageDescription, LinkText.inReaderDefaultColor, LinkText.inReaderHighLight, strDescription);

//        tvPageDescription.setText(strDescription);
        if (strDescription.equals("")) {
            linDescription.setVisibility(View.GONE);
        } else {
            linDescription.setVisibility(View.VISIBLE);
        }


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                int line = tvPageDescription.getLineCount();

                if (line <= 1) {
                    tvPageDescription.setGravity(Gravity.CENTER);
                } else {
                    tvPageDescription.setGravity(Gravity.LEFT);
                }

            }
        });

    }

    private void setPageLocation(int position) {

        String strLocation = photoContentsList.get(position).getLocation();

        if (strLocation.equals("")) {
            locationImg.setVisibility(View.GONE);
        } else {
            locationImg.setVisibility(View.VISIBLE);
            tvPageLocation.setText(strLocation);
            setLocationOnMap(photoContentsList.get(position).getLocation(), mapPhoto);
        }

    }

    private void resetPhotoSide(final int position) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int mPosition = position + 1;
                int nPosition = position - 1;

                if (mPosition < photoContentsList.size()) {

                    try {
                        PinchImageView mPhotoImg = (PinchImageView) (vpReader.findViewById(mPosition)).findViewById(R.id.photoImg);
                        mPhotoImg.reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (nPosition >= 0) {
                    try {
                        PinchImageView nPhotoImg = (PinchImageView) (vpReader.findViewById(nPosition)).findViewById(R.id.photoImg);
                        nPhotoImg.reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }, 400);

    }

    private void setPageAudio(int position) {

        String audioUrl = photoContentsList.get(position).getAudio_target();
        String audioRefer = photoContentsList.get(position).getAudio_refer();
        boolean audioLoop = photoContentsList.get(position).isAudio_loop();

        switch (audioRefer) {

            case "file":
                voiceImg.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                setFileSystemAudio(audioUrl, audioLoop);
                break;

            case "system":
                voiceImg.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                setFileSystemAudio(audioUrl, audioLoop);

            case "embed":
                voiceImg.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
                break;

            case "none":
                voiceImg.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                break;

        }

    }

    private void pageAudioPrepared() {


        if (isPlayAudio) {
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(0);


            if (soundEnable) {
                mediaPlayer.start();
                voiceImg.setImageResource(R.drawable.ic200_audio_play_white);

                startTimer();


            } else {
                voiceImg.setImageResource(R.drawable.ic200_audio_stop_white);
            }


        } else {
            voiceImg.setImageResource(R.drawable.ic200_audio_stop_white);
        }


    }

    private void setFileSystemAudio(final String audio_target, final boolean isAudio_loop) {

        if (audio_target != null && !audio_target.equals("") && !audio_target.equals("null")) {


            if (mediaPlayer != null) {
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }


            mediaPlayer = new MediaPlayer();
            mediaPlayer.setLooping(isAudio_loop);

            try {

                mediaPlayer.setDataSource(audio_target);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer m) {
                        pageAudioPrepared();
                    }
                });

                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer m, int i, int i1) {

                        mediaPlayer.reset();
                        mediaPlayer.setLooping(isAudio_loop);
                        try {
                            mediaPlayer.setDataSource(audio_target);
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer m) {
                                    pageAudioPrepared();
                                }
                            });
                            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer m, int i, int i1) {
                                    audioErrorState();
                                    return false;
                                }
                            });

                        } catch (Exception e) {
                            audioErrorState();
                            e.printStackTrace();
                        }


                        return false;
                    }
                });


            } catch (Exception e) {
                audioErrorState();
                e.printStackTrace();
            }


        }
    }

    private void setAuto(int position) {

        if (isAutoPlay) {
            autoScrollHandler.removeCallbacks(runnable);

            if (isAutoPlayIng) {
                int duration = photoContentsList.get(position).getDuration();
                setAutoPlay(duration, position);
            }
        }

    }

    private void setAutoPlay(int duration, int position) {
                /*判斷秒數大於0*/
        if (duration != 0) {

                    /*判斷不是最後頁*/
            if (position != photoContentsList.size() - 1) {

                        /*判斷auto play 開關*/
                if (isAutoPlay) {
                    String strUsefor = photoContentsList.get(position).getUsefor();

                        /*此頁為圖片或影片才執行自動撥放*/
                    if (strUsefor.equals("image") || strUsefor.equals("video")) {
                        autoScrollHandler.postDelayed(runnable, duration * 1500); //default 1000 手機上執行效果太快
                    }
                }
            }
        }
    }

    private void setLocationOnMap(final String location, final GoogleMap map) {

        final String strNewLocation = location.replaceAll(" ", "");
        new Thread(new Runnable() {

            private Double locLat = 0.0, locLng = 0.0;

            @Override
            public void run() {
                JSONObject obj = MapUtility.getLocationInfo(strNewLocation);
                try {
                    if (obj.get("results") != null && !obj.get("results").equals("")) {
                        if (((JSONArray) obj.get("results")).length() != 0) {
                            locLat = ((JSONArray) obj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            locLng = ((JSONArray) obj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            MyLog.Set("d", mActivity.getClass(), "locLat(緯度) => " + locLat);
                            MyLog.Set("d", mActivity.getClass(), "locLng(經度) => " + locLng);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mLocation == null) {
                            return;
                        }

                        final LatLng latlng = new LatLng(locLat, locLng);

                        try {
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 8), 2000, new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {
                                    map.addMarker(new MarkerOptions()
                                            .title(location)
                                            .position(latlng))
                                            .showInfoWindow();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        }).start();
    }

    private void showMore() {

        popMore = new PopupCustom(mActivity);
        popMore.setPopup(R.layout.pop_2_0_0_albuminfo_more_in_reader, R.style.pinpinbox_popupAnimation_bottom);


        LinearLayout linCollect = (LinearLayout) popMore.getPopupView().findViewById(R.id.linCollect);
        LinearLayout linShare = (LinearLayout) popMore.getPopupView().findViewById(R.id.linShare);
        LinearLayout linInfo = (LinearLayout) popMore.getPopupView().findViewById(R.id.linInfo);

        TextView tvCollect = (TextView) popMore.getPopupView().findViewById(R.id.tvCollect);

        TextUtility.setBold(tvCollect, true);
        TextUtility.setBold((TextView) popMore.getPopupView().findViewById(R.id.tvTitle), true);
        TextUtility.setBold((TextView) popMore.getPopupView().findViewById(R.id.tvShare), true);
        TextUtility.setBold((TextView) popMore.getPopupView().findViewById(R.id.tvInfo), true);


        View vContent = popMore.getPopupView().findViewById(R.id.linBackground);

        linCollect.setOnTouchListener(new ClickDragDismissListener(vContent, this));
        linShare.setOnTouchListener(new ClickDragDismissListener(vContent, this));
        linInfo.setOnTouchListener(new ClickDragDismissListener(vContent, this));

//        linCollect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popMore.getPopupWindow().dismiss();
//                checkToCollectAlbum(itemAlbum.getPoint() + "");
//            }
//        });
//
//        linShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popMore.getPopupWindow().dismiss();
//                share();
//            }
//        });
//
//        linInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                popMore.getPopupWindow().dismiss();
//                popInfo.show((RelativeLayout) findViewById(R.id.rBackground));
//            }
//        });

        if (itemAlbum.getUser_id() == StringIntMethod.StringToInt(id)) {

            linCollect.setVisibility(View.GONE);

        } else {
            linCollect.setVisibility(View.VISIBLE);
            if (itemAlbum.isOwn()) {

                tvCollect.setText(R.string.pinpinbox_2_0_0_itemtype_collected);
                tvCollect.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                linCollect.setClickable(false);

            } else {

                tvCollect.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                if (itemAlbum.getPoint() > 0) {


                    tvCollect.setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_itemtype_collect_need_sponsor) + itemAlbum.getPoint() + "P)");

                    TextView tvSponsorMore = (TextView) popMore.getPopupView().findViewById(R.id.tvSponsorMore);
                    tvSponsorMore.setVisibility(View.VISIBLE);
                    tvSponsorMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popMore.getPopupWindow().dismiss();
                            vpReader.setCurrentItem(photoContentsList.size() - 1);
                        }
                    });


                } else {


                    tvCollect.setText(R.string.pinpinbox_2_0_0_itemtype_collect);


                }
            }
        }
        popMore.show((RelativeLayout) findViewById(R.id.rBackground));

    }

    private void share() {

//        boolean bShareToFB = PPBApplication.getInstance().getData().getBoolean(TaskKeyClass.share_to_fb, false);
//        if (bShareToFB) {
//            systemShare();
//        } else {
//            doCheckShare();
//        }

        doCheckShare();

    }

    private void systemShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/jpg");
//        if (photoContentsList.get(0).getImage_url() != null && !photoContentsList.get(0).getImage_url().equals("")) {
//            Uri u = Uri.parse(photoContentsList.get(0).getImage_url());
//            intent.putExtra(Intent.EXTRA_STREAM, u);
//        }
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");


        MyLog.Set("e", getClass(), "event => " + event);

        if (event.equals("") || event == null || event.equals("null")) {
            intent.putExtra(Intent.EXTRA_TEXT, itemAlbum.getName() + " , " + UrlClass.shareAlbumUrl + album_id + "&autoplay=1");
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, itemAlbum.getName() + " , " + UrlClass.shareAlbumUrl + album_id);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(Intent.createChooser(intent, mActivity.getTitle()));
    }

    private void selectShareMode() {

        popSelectShare = new PopupCustom(mActivity);
        popSelectShare.setPopup(R.layout.pop_2_0_0_select_share, R.style.pinpinbox_popupAnimation_bottom);

        TextView tvShareFB = (TextView) popSelectShare.getPopupView().findViewById(R.id.tvShareFB);
        TextView tvShare = (TextView) popSelectShare.getPopupView().findViewById(R.id.tvShare);

        TextUtility.setBold((TextView) popSelectShare.getPopupView().findViewById(R.id.tvTitle), true);
        TextUtility.setBold(tvShareFB, true);
        TextUtility.setBold(tvShare, true);


        View vContents = popSelectShare.getPopupView().findViewById(R.id.linBackground);

        tvShareFB.setOnTouchListener(new ClickDragDismissListener(vContents, this));
        tvShare.setOnTouchListener(new ClickDragDismissListener(vContents, this));


        popSelectShare.show((RelativeLayout) findViewById(R.id.rBackground));


    }

    private void taskShare() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {


            String shareUrl = "";

            if (event.equals("") || event == null || event.equals("null")) {
                shareUrl = UrlClass.shareAlbumUrl + album_id + "&autoplay=1";
            } else {
                shareUrl = UrlClass.shareAlbumUrl + album_id;
            }


            if (photoContentsList.size() > 0) {

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(shareUrl))
                        .setImageUrl(Uri.parse(photoContentsList.get(0).getImage_url()))
                        .build();
                shareDialog.show(content);

            } else {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(shareUrl))
                        .build();
                shareDialog.show(content);
            }


        } else {

            MyLog.Set("e", getClass(), "-----------------***********************-------------------");
        }

    }

    public void checkToCollectAlbum(String point) {

        comfirmPoint = point;

        MyLog.Set("d", getClass(), "comfirmPoint => " + comfirmPoint);

        if (point.equals("0")) {
            doCollectAlbum();
        } else {

            final DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvMessage().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_message_confirm_sponsor) + "(" + point + "P) ?");
            CheckExecute checkExecute = new CheckExecute() {
                @Override
                public void DoCheck() {
                    d.dismiss();
                    doGetPoint();
                }
            };
            d.setCheckExecute(checkExecute);
            d.show();

        }
    }

    private void checkBuyPoint() {

        final DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.CHECK);
        d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_point_insufficient);
        d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_go_buy_point);
        d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_be_later);
        d.setCheckExecute(new CheckExecute() {
            @Override
            public void DoCheck() {
                Intent intent = new Intent(mActivity, BuyPoint2Activity.class);
                mActivity.startActivity(intent);
                ActivityAnim.StartAnim(mActivity);
            }
        });
        d.show();

    }

    private void createTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (mediaPlayer != null && !seekBar.isPressed()) {

                    try {
                        int position = mediaPlayer.getCurrentPosition();

                        MyLog.Set("d", mActivity.getClass(), " mediaPlayer.getCurrentPosition() => " + position);

                        seekBar.setProgress(position);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }


            }


        };
    }

    private void startTimer() {
        if (timer == null) {
            createTimerTask();
            timer = new Timer();
            timer.schedule(timerTask, 0, 500);
        }

    }

    private void cleanTimer() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        timerTask = null;

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = null;
    }

    private void audioErrorState() {
        mediaPlayer.release();
        mediaPlayer = null;
        cleanTimer();
        voiceImg.setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);
        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_audio_error);
    }

    private void mediaPlayerStatusChange() {

        soundEnable = true;//設為true 將不受音笑開關的預設值


        if (mediaPlayer != null) {

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                voiceImg.setImageResource(R.drawable.ic200_audio_stop_white);
                isPlayAudio = false;
            } else {
                mediaPlayer.start();
                voiceImg.setImageResource(R.drawable.ic200_audio_play_white);
                isPlayAudio = true;
            }

        }
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoGetAlbumInfo:
                        doGetAlbumInfo();
                        break;

                    case DoingTypeClass.DoCollectAlbum:
                        doCollectAlbum();
                        break;

                    case DoingTypeClass.DoFirstCollectTask:
                        doFirstCollectTask();
                        break;

                    case DoingTypeClass.DoGetPoint:
                        doGetPoint();
                        break;

                    case DoingTypeClass.DoSendContribute:
                        doSendContribute();
                        break;
                    case DoingTypeClass.DoSendLike:
                        doSendLike();
                        break;

                    case DoingTypeClass.DoDeleteLike:
                        doDeleteLike();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    private void doGetAlbumInfo() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getAlbumInfoTask = new GetAlbumInfoTask();
        getAlbumInfoTask.execute();

    }

    private void doGetPoint() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getPointTask = new GetPointTask();
        getPointTask.execute();

    }

    private void doCollectAlbum() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        protocol13 = new Protocol13(
                mActivity,
                id,
                token,
                album_id,
                "google",
                comfirmPoint,
                new Protocol13.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        doingType = DoingTypeClass.DoCollectAlbum;
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success() {
                        dissmissLoading();

                        itemAlbum.setOwn(true);

                        if (itemAlbum.getPoint() > 0) {
//                            PinPinToast.showSuccessToast(mActivity, itemAlbum.getUser_name() + getResources().getString(R.string.pinpinbox_2_0_0_toast_message_thank_by_sponsor));


                            PinPinToast.showSponsorToast(
                                    mActivity.getApplicationContext(),
                                    itemAlbum.getUser_name() + getResources().getString(R.string.pinpinbox_2_0_0_toast_message_thank_by_sponsor),
                                    itemAlbum.getUser_picture()
                            );


                            //需要在其他地方立即顯示
                            PPBApplication.getInstance().getData().edit().putString(Key.point, (userPoint - StringIntMethod.StringToInt(comfirmPoint)) + "").commit();


                        } else {
                            PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_added_to_collect);
                        }

                        FlurryUtil.onEvent(FlurryKey.reader_collect_success);

                        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                        for (Activity ac : activityList) {
                            if (ac.getClass().getSimpleName().equals(AlbumInfo2Activity.class.getSimpleName())) {
                                ((AlbumInfo2Activity) ac).setOwn(true);
                                break;
                            }
                        }

                        doGetAlbumInfo();

                        if (itemAlbum.getPoint() == 0) {

                            doFirstCollectTask();

                        } else {

                            doFirstCollectTask();

                        }
                    }

                    @Override
                    public void IsOwnAlbum() {

                    }

                    @Override
                    public void TimeOut() {
                        doCollectAlbum();
                    }
                });

    }

    private void doFirstCollectTask() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        firstCollectAlbumTask = new FirstCollectAlbumTask();
        firstCollectAlbumTask.execute();

    }

    private void doSendContribute() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        sendContributeTask = new SendContributeTask();
        sendContributeTask.execute();

    }

    private void doCheckShare() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        checkShareTask = new CheckShareTask();
        checkShareTask.execute();

    }

    private void doShareTask() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        shareTask = new ShareTask();
        shareTask.execute();

    }

    private void doSendLike() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        sendLikeTask = new SendLikeTask();
        sendLikeTask.execute();
    }

    private void doDeleteLike() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        deleteLikeTask = new DeleteLikeTask();
        deleteLikeTask.execute();
    }

    private class GetAlbumInfoTask extends AsyncTask<Void, Void, Object> {

        private int p08Result = -1;
        private String p08Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetAlbumInfo;
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P08_RetrieveAlbumProfile,
                        SetMapByProtocol.setParam08_retrievealbumprofile(id, token, album_id), null);//08

                if (LOG.isLogMode) {
                    Logger.json(strJson);
                }
            } catch (SocketTimeoutException timeout) {
                p08Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonResult = new JSONObject(strJson);
                    p08Result = JsonUtility.GetInt(jsonResult, ProtocolKey.result);
                    if (p08Result == 1) {

                        JSONObject jsonData = new JSONObject(JsonUtility.GetString(jsonResult, ProtocolKey.data));


                    /*album detail*/
                        JSONObject jsonAlbum = new JSONObject(JsonUtility.GetString(jsonData, ProtocolKey.album));
                        itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_name));
                        itemAlbum.setDescription(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_description));
                        itemAlbum.setLocation(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_location));
                        itemAlbum.setOwn(JsonUtility.GetBoolean(jsonAlbum, ProtocolKey.album_own));
                        itemAlbum.setPoint(JsonUtility.GetInt(jsonAlbum, ProtocolKey.album_point));
                        itemAlbum.setCount_photo(JsonUtility.GetInt(jsonAlbum, ProtocolKey.album_count_photo));
                        itemAlbum.setAudio_mode(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_audio_mode));
                        itemAlbum.setAudio_loop(JsonUtility.GetBoolean(jsonAlbum, ProtocolKey.album_audio_loop));
                        itemAlbum.setAudio_refer(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_audio_refer));
                        itemAlbum.setAudio_target(JsonUtility.GetString(jsonAlbum, ProtocolKey.album_audio_target));
                        itemAlbum.setIs_likes(JsonUtility.GetBoolean(jsonAlbum, ProtocolKey.is_likes));


                     /*album usefor*/
                        String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.album_usefor);
                        JSONObject jsonUsefor = new JSONObject(usefor);
                        itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.album_exchange));
                        itemAlbum.setImage(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.album_image));
                        itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.album_slot));
                        itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.album_video));
                        itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.album_audio));


                    /*user detail*/
                        JSONObject jsonUser = new JSONObject(JsonUtility.GetString(jsonData, ProtocolKey.user));
                        itemAlbum.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));
                        itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.user_name));
                        itemAlbum.setUser_picture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));

                    /*event*/
                        event = JsonUtility.GetString(jsonData, ProtocolKey.event);






                    /*photo detail*/
                        try {
                            JSONArray jsonArray = new JSONArray(JsonUtility.GetString(jsonData, ProtocolKey.photo));
                            int array = jsonArray.length();

                            photoContentsList.clear();

                            for (int i = 0; i < array; i++) {

                                ItemPhoto itemPhoto = new ItemPhoto();

                                JSONObject obj = (JSONObject) jsonArray.get(i);

                                itemPhoto.setAudio_loop(JsonUtility.GetBoolean(obj, ProtocolKey.photo_audio_loop));
                                itemPhoto.setAudio_refer(JsonUtility.GetString(obj, ProtocolKey.photo_audio_refer));
                                itemPhoto.setAudio_target(JsonUtility.GetString(obj, ProtocolKey.photo_audio_target));

                                itemPhoto.setDescription(JsonUtility.GetString(obj, ProtocolKey.photo_description));
                                itemPhoto.setDuration(JsonUtility.GetInt(obj, ProtocolKey.photo_duration));
                                itemPhoto.setLocation(JsonUtility.GetString(obj, ProtocolKey.photo_location));
                                itemPhoto.setName(JsonUtility.GetString(obj, ProtocolKey.photo_name));
                                itemPhoto.setPhoto_id(JsonUtility.GetInt(obj, ProtocolKey.photo_id));
                                itemPhoto.setUsefor(JsonUtility.GetString(obj, ProtocolKey.photo_usefor));

                                itemPhoto.setVideo_refer(JsonUtility.GetString(obj, ProtocolKey.photo_video_refer));
                                itemPhoto.setVideo_target(JsonUtility.GetString(obj, ProtocolKey.photo_video_target));

                                itemPhoto.setImage_url(JsonUtility.GetString(obj, ProtocolKey.photo_image_url));
                                itemPhoto.setImage_url_thumbnail(JsonUtility.GetString(obj, ProtocolKey.photo_image_url_thumbnail));

                                itemPhoto.setSelect(false);

                                photoContentsList.add(itemPhoto);
                            }

                            itemAlbum.setCover(photoContentsList.get(0).getImage_url_thumbnail());

                            photoTotalCount = photoContentsList.size();


                            if (!itemAlbum.isOwn()) {
                                ItemPhoto itemPhoto = new ItemPhoto();
                                itemPhoto.setAudio_loop(false);
                                itemPhoto.setAudio_refer("");
                                itemPhoto.setAudio_target("");
                                itemPhoto.setDescription("");
                                itemPhoto.setDuration(0);
                                itemPhoto.setLocation("");
                                itemPhoto.setName("");
                                itemPhoto.setPhoto_id(0);
                                itemPhoto.setUsefor("lastPage");
                                itemPhoto.setVideo_refer("");
                                itemPhoto.setVideo_target("");
                                itemPhoto.setImage_url("");
                                itemPhoto.setImage_url_thumbnail("lastPage");
                                itemPhoto.setSelect(false);
                                photoContentsList.add(itemPhoto);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (p08Result == 0) {
                        p08Message = JsonUtility.GetString(jsonResult, ProtocolKey.message);
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
            dissmissLoading();

            if (p08Result == 1) {


                if (photoContentsList.size() < 1) {

                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_there_are_no_contents_in_the_work);

                    return;
                }


                setLike();

                setPageAdapter();

                photoContentsList.get(0).setSelect(true);
                recyclerReaderAdapter.notifyDataSetChanged();
                rvReader.scrollToPosition(0);


                vpReader.setCurrentItem(0, false);

                setClickable(true);

                setInfoDate();

                checkAudio();

                checkAutoPlay();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        setPage(0);
                        setPageDetail(0);
                        setPageDescription(0);
                        setPageLocation(0);
                        setPhotoMode(0);
                        resetPhotoSide(0);
                        setAuto(0);

                        if (isAudioExist) {
                            setAlbumAudio();
                        } else {
                            setPageAudio(0);
                        }

                        lastPosition = 0;

                    }
                }, 500);


                if (!isSaveToRecent) {
                    saveToRecent();
                }

            } else if (p08Result == 0) {

                DialogV2Custom.BuildError(mActivity, p08Message);

            } else if (p08Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }

        private void setLike() {

            if (itemAlbum.is_likes()) {
                likeImg.setImageResource(R.drawable.ic200_like_main);
            } else {
                likeImg.setImageResource(R.drawable.ic200_like_white);
            }

        }

        private void setInfoDate() {

            LinkText.set(mActivity,
                    tvAlbumDescription,
                    LinkText.defaultColorWhite,
                    LinkText.defaultColorOfHighlightedLink,
                    itemAlbum.getDescription());

            tvAlbumName.setText(itemAlbum.getName());
            tvAlbumUser.setText(itemAlbum.getUser_name());
            //time

            if (itemAlbum.getLocation().equals("")) {
                rMap.setVisibility(View.GONE);
            } else {
                rMap.setVisibility(View.VISIBLE);
                setLocationOnMap(itemAlbum.getLocation(), mapAlbum);
            }


        }

        private void checkAudio() {


            if (!itemAlbum.getAudio_target().equals("")) {
                isAudioExist = true;
                voiceImg.setVisibility(View.VISIBLE);
                seekBar.setVisibility(View.VISIBLE);
            } else {
                isAudioExist = false;
                voiceImg.setVisibility(View.GONE);
                seekBar.setVisibility(View.GONE);
                int count = photoContentsList.size();
                for (int i = 0; i < count; i++) {
                    String audioUrl = photoContentsList.get(i).getAudio_target();
                    if (!audioUrl.equals("")) {
                        isPageAudioExist = true;
                        break;
                    }
                }

            }

        }

        private void checkAutoPlay() {

            int count = photoContentsList.size();
            for (int i = 0; i < count; i++) {

                int duration = photoContentsList.get(i).getDuration();
                if (duration > 0) {
                    isAutoPlay = true;
                    break;
                }
            }


            if (isAutoPlay) {
                autoplayImg.setVisibility(View.VISIBLE);
                autoplayImg.setImageResource(R.drawable.ic200_autoplay_pink);

            } else {
                autoplayImg.setVisibility(View.GONE);
            }

        }

        private void setAlbumAudio() {

            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }


            mediaPlayer = new MediaPlayer();
            mediaPlayer.setLooping(itemAlbum.isAudio());

            try {
                mediaPlayer.setDataSource(itemAlbum.getAudio_target());
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer m) {

                        albumAudioPrepared();

                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer m, int i, int i1) {

                        /*第一次音效讀取失敗 再嘗試一次*/

                        mediaPlayer.reset();

                        mediaPlayer.setLooping(itemAlbum.isAudio());

                        try {
                            mediaPlayer.setDataSource(itemAlbum.getAudio_target());
                            mediaPlayer.prepareAsync();
                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer m) {
                                    albumAudioPrepared();
                                }
                            });

                            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                @Override
                                public boolean onError(MediaPlayer m, int i, int i1) {
                                    audioErrorState();
                                    return false;
                                }
                            });


                        } catch (Exception e) {
                            audioErrorState();
                            e.printStackTrace();
                        }

                        return false;
                    }
                });


            } catch (Exception e) {
                audioErrorState();
                e.printStackTrace();
            }

        }

        private void albumAudioPrepared() {


            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(0);


            if (soundEnable) {
                mediaPlayer.start();

                startTimer();


            }


            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                voiceImg.setImageResource(R.drawable.ic200_audio_play_white);
            } else {
                voiceImg.setImageResource(R.drawable.ic200_audio_stop_white);
            }


        }

        private void saveToRecent() {

            try {
                /*獲取原排序array*/
                JSONArray jsonCookieArray = new JSONArray(strRecent);


                   /*判斷是否在原排序內*/
                for (int i = 0; i < jsonCookieArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonCookieArray.get(i);

                    String albumId = obj.getString(Key.album_id);
                    if (albumId.equals(album_id)) {
                        try {

                            if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {

                                jsonCookieArray.remove(i);

                            }
                            break;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                /*判斷是否超過20筆*/
                if (jsonCookieArray.length() > 19) {
                    try {
                        jsonCookieArray.remove(19);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                 /*建立新排序*/
                JSONArray newCookieArray = new JSONArray();

                /*建立新cookie新增至新排序第一順位*/
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(Key.album_id, album_id);
                jsonObject.put(Key.name, itemAlbum.getName());
                jsonObject.put(Key.user, itemAlbum.getUser_name());
                jsonObject.put(Key.cover, itemAlbum.getCover());
                newCookieArray.put(jsonObject);

                  /*原排序新增至新排序*/
                for (int i = 0; i < jsonCookieArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonCookieArray.get(i);
                    newCookieArray.put(obj);
                }


                PPBApplication.getInstance().getData().edit().putString(Key.recentAlbumdata, newCookieArray.toString()).commit();
                Logger.json(PPBApplication.getInstance().getData().getString(Key.recentAlbumdata, "[]"));
                isSaveToRecent = true;

            } catch (Exception e) {
                e.printStackTrace();
            }

            List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
            for (int i = 0; i < activityList.size(); i++) {
                String actName = activityList.get(i).getClass().getSimpleName();
                if (actName.equals(RecentAlbum2Activity.class.getSimpleName())) {
                    ((RecentAlbum2Activity) activityList.get(i)).setRecent();
                    MyLog.Set("d", mActivity.getClass(), "refresh recent");
                    break;
                }
            }

        }
    }

    private class GetPointTask extends AsyncTask<Void, Void, Object> {

        private String p23Message = "";

        private int p23Result = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetPoint;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(id, token, "google"), null);

                MyLog.Set("d", getClass(), "p23strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p23Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p23Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p23Result == 1) {

                        userPoint = JsonUtility.GetInt(jsonObject, ProtocolKey.data);

                    } else if (p23Result == 0) {
                        p23Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            dissmissLoading();

            if (p23Result == 1) {

                if (userPoint < itemAlbum.getPoint()) {
                    MyLog.Set("d", this.getClass(), "P點不足 前去買點");
                    checkBuyPoint();
                } else {
                    MyLog.Set("d", this.getClass(), "P點足夠 進行購買");
                    doCollectAlbum();
                }

            } else if (p23Result == 0) {

                DialogV2Custom.BuildError(mActivity, p23Message);

            } else if (p23Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }

    }

    private class FirstCollectAlbumTask extends AsyncTask<Void, Void, Object> {

        private String restriction;
        private String restriction_value;
        private String name;
        private String reward;
        private String reward_value;
        private String url;
        private String p83Message = "";

        private int p83Result = -1;
        private int numberofcompleted;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFirstCollectTask;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> map = new HashMap<>();
            map.put(Key.id, id);
            map.put(Key.token, token);

            if (itemAlbum.getPoint() == 0) {
                map.put(Key.task_for, TaskKeyClass.collect_free_album);
            } else {
                map.put(Key.task_for, TaskKeyClass.collect_pay_album);
            }

            map.put(Key.platform, "google");
            String sign = IndexSheet.encodePPB(map);

            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, id);
            sendData.put(Key.token, token);


            if (itemAlbum.getPoint() == 0) {
                sendData.put(Key.task_for, TaskKeyClass.collect_free_album);
            } else {
                sendData.put(Key.task_for, TaskKeyClass.collect_pay_album);
            }

            sendData.put(Key.platform, "google");
            sendData.put(Key.type, "album");
            sendData.put(Key.type_id, album_id);
            sendData.put(Key.sign, sign);


            String strJson = "";
            try {
                if (itemAlbum.getPoint() == 0) {
                    strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                } else {
                    strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                }
                MyLog.Set("d", getClass(), "p83strJson => " + strJson);
                MyLog.Set("d", getClass(), "p83strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p83Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p83Result == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONObject object = new JSONObject(jdata);

                        String task = JsonUtility.GetString(object, ProtocolKey.task);
                        String event = JsonUtility.GetString(object, ProtocolKey.event);

                        JSONObject jsonTask = new JSONObject(task);
                        name = JsonUtility.GetString(jsonTask, ProtocolKey.name);
                        reward = JsonUtility.GetString(jsonTask, ProtocolKey.reward);
                        reward_value = JsonUtility.GetString(jsonTask, ProtocolKey.reward_value);
                        restriction = JsonUtility.GetString(jsonTask, ProtocolKey.restriction);
                        restriction_value = JsonUtility.GetString(jsonTask, ProtocolKey.restriction_value);
                        numberofcompleted = JsonUtility.GetInt(jsonTask, ProtocolKey.numberofcompleted);

                        JSONObject jsonEvent = new JSONObject(event);
                        url = JsonUtility.GetString(jsonEvent, ProtocolKey.url);

                    } else if (p83Result == 2 || p83Result == 0) {
                        p83Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            dissmissLoading();

            if (p83Result == 1) {

                final DialogHandselPoint d = new DialogHandselPoint(mActivity);

                if (restriction.equals("personal")) {
                    d.getTvTitle().setText(name);
                    d.getTvRestriction().setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_count) + numberofcompleted + "/" + restriction_value);
                    d.getTvRestriction().setVisibility(View.VISIBLE);
                } else {
                    d.getTvTitle().setText(name);
                }


                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");
                    /*獲取當前使用者P點*/
                    String point = PPBApplication.getInstance().getData().getString(Key.point, "");
                    int p = StringIntMethod.StringToInt(point);

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    PPBApplication.getInstance().getData().edit().putString(Key.point, newP).commit();

//                    if (itemAlbum.getPoint() == 0) {
//                        PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.collect_free_album, false).commit();
//                    } else {
//                        PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.collect_pay_album, false).commit();
//                    }


                } else {
                    d.getTvDescription().setText(reward_value);
                }


                if (url == null || url.equals("")) {
                    d.getTvLink().setVisibility(View.GONE);
                } else {
                    d.getTvLink().setVisibility(View.VISIBLE);
                    d.getTvLink().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            Intent intent = new Intent(mActivity, WebView2Activity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);
                        }
                    });
                }


            } else if (p83Result == 2 || p83Result == 0) {

            } else if (p83Result == 3) {

            } else if (p83Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }

        }
    }

    private class SendContributeTask extends AsyncTask<Void, Void, Object> {

        private String p73Message = "";

        private int p73Result = -1;

        private boolean contributionstatus;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSendContribute;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P73_SwitchStatusOfContribution
                        , SetMapByProtocol.setParam73_switchstatusofcontribution(id, token, event_id, album_id)
                        , null);
                MyLog.Set("d", getClass(), "p73strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p73Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p73Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p73Result == 1) {

                        String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONObject jsonData = new JSONObject(data);
                        String event = JsonUtility.GetString(jsonData, ProtocolKey.event);

                        JSONObject jsonEvent = new JSONObject(event);
                        contributionstatus = JsonUtility.GetBoolean(jsonEvent, ProtocolKey.contributionstatus);

                    } else if (p73Result == 0) {
                        p73Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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
            dissmissLoading();
            if (p73Result == 1) {

                if (contributionstatus) {
                    PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_success);
                    FlurryUtil.onEvent(FlurryKey.event_create_from_contribute_finish);
                } else {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_contribute_cancel);
                }

                toMyCollect();


            } else if (p73Result == 0) {

                DialogV2Custom.BuildError(mActivity, p73Message);

            } else if (p73Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }

        }
    }

    private class CheckShareTask extends AsyncTask<Void, Void, Object> {

        private int p84Result = -1;
        private String p84Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoCheckShare;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";

            try {
                HashMap<String, String> map = new HashMap<>();
                map.put(Key.type, Value.album);
                map.put(Key.type_id, album_id);

                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P84_CheckTaskCompleted, SetMapByProtocol.setParam84_checktaskcompleted(id, token, TaskKeyClass.share_to_fb, "google", map), null);

                MyLog.Set("d", getClass(), "p84strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p84Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p84Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p84Result == 1) {
                    } else if (p84Result == 2) {
                        p84Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    } else if (p84Result == 0) {
                        p84Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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


            dissmissLoading();

            if (p84Result == 1) {
                /*任務已完成*/
                systemShare();


            } else if (p84Result == 2) {

                /*尚有次數未完成*/
                selectShareMode();

            } else if (p84Result == 0) {

                systemShare();


            } else if (p84Result == Key.TIMEOUT) {

                connectInstability();

            } else {

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());

            }


        }
    }

    private class ShareTask extends AsyncTask<Void, Void, Object> {

        private String p83Message = "";
        private String restriction;
        private String restriction_value;
        private String name;
        private String reward;
        private String reward_value;
        private String url;

        private int p83Result = -1;
        private int numberofcompleted;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoShareTask;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> map = new HashMap<>();
            map.put(MapKey.id, id);
            map.put(MapKey.token, token);
            map.put(MapKey.task_for, TaskKeyClass.share_to_fb);
            map.put(MapKey.platform, "google");
            String sign = IndexSheet.encodePPB(map);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(MapKey.id, id);
            sendData.put(MapKey.token, token);
            sendData.put(MapKey.task_for, TaskKeyClass.share_to_fb);
            sendData.put(MapKey.platform, "google");
            sendData.put(MapKey.type, "album");
            sendData.put(MapKey.type_id, album_id);
            sendData.put(Key.sign, sign);

            String strJson = "";


            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                MyLog.Set("d", mActivity.getClass(), "p83strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p83Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p83Result == 1) {

                        String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONObject jsonData = new JSONObject(data);

                        String task = jsonData.getString(ProtocolKey.task);
                        String event = jsonData.getString(ProtocolKey.event);

                        JSONObject taskObj = new JSONObject(task);
                        name = taskObj.getString(ProtocolKey.name);
                        reward = taskObj.getString(ProtocolKey.reward);
                        reward_value = taskObj.getString(ProtocolKey.reward_value);
                        restriction = taskObj.getString(ProtocolKey.restriction);
                        restriction_value = taskObj.getString(ProtocolKey.restriction_value);

                        numberofcompleted = taskObj.getInt(ProtocolKey.numberofcompleted);

                        JSONObject jsonEvent = new JSONObject(event);
                        url = JsonUtility.GetString(jsonEvent, ProtocolKey.url);

                    } else if (p83Result == 2 || p83Result == 3 || p83Result == 4) {
                        p83Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            dissmissLoading();

            if (p83Result == 1) {

                final DialogHandselPoint d = new DialogHandselPoint(mActivity);

                if (restriction.equals("personal")) {
                    d.getTvTitle().setText(name);
                    d.getTvRestriction().setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_count) + numberofcompleted + "/" + restriction_value);
                    d.getTvRestriction().setVisibility(View.VISIBLE);
                } else {
                    d.getTvTitle().setText(name);
                }

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");
                    /*獲取當前使用者P點*/
                    String point = PPBApplication.getInstance().getData().getString(Key.point, "");
                    int p = StringIntMethod.StringToInt(point);

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    PPBApplication.getInstance().getData().edit().putString(Key.point, newP).commit();


                } else {
                    d.getTvDescription().setText(reward_value);
                }


                if (url == null || url.equals("") || url.equals("null")) {
                    d.getTvLink().setVisibility(View.GONE);
                } else {
                    d.getTvLink().setVisibility(View.VISIBLE);
                    d.getTvLink().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            Intent intent = new Intent(mActivity, WebView2Activity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);
                        }
                    });
                }

            } else if (p83Result == 2) {


            } else if (p83Result == 3) {

            } else if (p83Result == 0) {

            } else if (p83Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    private class SendLikeTask extends AsyncTask<String, Integer, Integer> {

        private String p92Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSendLike;
            startLoading();

        }


        @Override
        protected Integer doInBackground(String... strings) {

            int result = -1;

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P92_InsertAlbum2Likes,
                        SetMapByProtocol.setParam92_insertalbum2likes(
                                id, token, album_id), null);

                MyLog.Set("d", getClass(), "p92strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (result == 0) {
                        p92Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (result == 1) {


                likeImg.setImageResource(R.drawable.ic200_like_main);
                itemAlbum.setIs_likes(true);


                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                for (Activity ac : activityList) {
                    if (ac.getClass().getSimpleName().equals(AlbumInfo2Activity.class.getSimpleName())) {
                        ((AlbumInfo2Activity) ac).setIsLike();
                        break;
                    }
                }


            } else if (result == 0) {
                DialogV2Custom.BuildError(mActivity, p92Message);
            } else if (result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }
    }

    private class DeleteLikeTask extends AsyncTask<String, Integer, Integer> {

        private String p93Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDeleteLike;
            startLoading();
        }


        @Override
        protected Integer doInBackground(String... strings) {

            int result = -1;

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P93_DeleteAlbum2Likes,
                        SetMapByProtocol.setParam93_deletealbum2likes(
                                id, token, album_id), null);

                MyLog.Set("d", getClass(), "p93strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (result == 0) {
                        p93Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (result == 1) {
//
//                int count = StringIntMethod.StringToInt(tvLikeCount.getText().toString());
//                tvLikeCount.setText((count - 1) + "");

                likeImg.setImageResource(R.drawable.ic200_like_white);
                itemAlbum.setIs_likes(false);

                List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                for (Activity ac : activityList) {
                    if (ac.getClass().getSimpleName().equals(AlbumInfo2Activity.class.getSimpleName())) {
                        ((AlbumInfo2Activity) ac).setDeleteLike();
                        break;
                    }
                }

            } else if (result == 0) {
                DialogV2Custom.BuildError(mActivity, p93Message);
            } else if (result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }

    }

    public void reFreshExchange() {
        setPageDetail(vpReader.getCurrentItem());
    }

    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }


        switch (view.getId()) {

            case R.id.backImg:

                back();

                break;

            case R.id.voiceImg:

                mediaPlayerStatusChange();

                break;

            case R.id.autoplayImg:
                if (isAutoPlayIng) {
                    autoplayImg.setImageResource(R.drawable.ic200_autoplay_white);
                    isAutoPlayIng = false;
                    autoScrollHandler.removeCallbacks(runnable);
                    MyLog.Set("d", mActivity.getClass(), "autoScrollHandler.removeCallbacks(runnable)");
                } else {
                    autoplayImg.setImageResource(R.drawable.ic200_autoplay_pink);
                    isAutoPlayIng = true;
                    int currentDuration = photoContentsList.get(vpReader.getCurrentItem()).getDuration();
                    setAutoPlay(currentDuration, vpReader.getCurrentItem());
                }
                break;

            case R.id.locationImg:

                popPageMap.show((RelativeLayout) findViewById(R.id.rBackground));
                break;

            case R.id.moreImg:

                showMore();

                break;

            case R.id.messageImg:


                FlurryUtil.onEvent(FlurryKey.reader_click_board);

                if (board == null) {
                    board = new PopBoard(mActivity, PopBoard.TypeAlbum, album_id, (RelativeLayout) findViewById(R.id.rBackground), true);
                } else {
                    board.clearList();
                    board.doGetBoard();
                }

                break;

            case R.id.likeImg:

                FlurryUtil.onEvent(FlurryKey.reader_click_like);

                if (itemAlbum.is_likes()) {
                    doDeleteLike();
                } else {
                    doSendLike();
                }

                break;

//            case R.id.collectImg:
//
//                break;
//
//            case R.id.infoImg:
//
//                popInfo.show((RelativeLayout) findViewById(R.id.rBackground));
//
//                break;
//
//            case R.id.shareImg:
//
//                break;

            case R.id.closeImg:
                popInfo.dismiss();
                break;

            case R.id.closeMapImg:
                popPageMap.dismiss();
                break;
        }

    }

    @Override
    public void OnClick(View v) {


        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }


        switch (v.getId()) {

            case R.id.linCollect:

                if (!itemAlbum.isOwn()) {
                    popMore.dismiss();
                    checkToCollectAlbum(itemAlbum.getPoint() + "");
                }

                break;

            case R.id.linShare:
                popMore.dismiss();
                share();
                break;

            case R.id.linInfo:
                popMore.dismiss();
                popInfo.show((RelativeLayout) findViewById(R.id.rBackground));
                break;


            case R.id.tvShareFB:
                popSelectShare.dismiss();
                if (!isFBShareComplate) {

                          /*設置facebook api*/
                    FacebookSdk.sdkInitialize(getApplicationContext());
                    callbackManager = CallbackManager.Factory.create();


                       /*設置facebook share api*/
                    shareDialog = new ShareDialog(mActivity);
                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

                        @Override
                        public void onSuccess(Sharer.Result result) {
                            doShareTask();
                            MyLog.Set("d", mActivity.getClass(), "shareDialog => onSuccess");
                        }

                        @Override
                        public void onCancel() {
                            MyLog.Set("d", mActivity.getClass(), "shareDialog => onCancel");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            MyLog.Set("d", mActivity.getClass(), "shareDialog => onError");
                        }
                    });
                    isFBShareComplate = true;
                }

                taskShare();
                break;

            case R.id.tvShare:
                popSelectShare.dismiss();
                systemShare();

                break;


        }


    }

    @Override
    public void OnDismiss() {

        if (popMore != null && popMore.getPopupWindow().isShowing()) {
            popMore.dismiss();
        }

        if (popSelectShare != null && popSelectShare.getPopupWindow().isShowing()) {
            popSelectShare.dismiss();
        }

    }


    @Override
    public void onLocationChanged(Location location) {
        if (mLocation != null) {
            mLocation.setLatitude(location.getLatitude());
            mLocation.setLongitude(location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            MyLog.Set("d", getClass(), "-------------landscape(横)");


            linBackground.setOrientation(LinearLayout.HORIZONTAL);
            rPhoto.setVisibility(View.GONE);

            //全屏顯示
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);


            //狀態欄透明隱藏
//            setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));

            getStatusControl().setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));

            (findViewById(R.id.vStatusHeight)).setVisibility(View.GONE);

            setSwipeBackEnable(false);


        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            MyLog.Set("d", getClass(), "-------------portrait(直)");
            linBackground.setOrientation(LinearLayout.VERTICAL);
            rPhoto.setVisibility(View.VISIBLE);

            //顯示狀態欄
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

            //顯示狀態欄顏色
//            setStatusColor(Color.parseColor(ColorClass.STATUSBAR));
            getStatusControl().setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));
            (findViewById(R.id.vStatusHeight)).setVisibility(View.VISIBLE);


            setSwipeBackEnable(true);

        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            MyLog.Set("d", getClass(), "media pause");

        }

        cleanTimer();


    }


    private Criteria criteria;


    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();


        try {

            if (criteria == null) {
                criteria = new Criteria();
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
            }

            if (mLocManager == null) {
                mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            }

            String bestProvider = mLocManager.getBestProvider(criteria, true);
            Location newLoction = null;
            if (bestProvider != null) {
                newLoction = mLocManager.getLastKnownLocation(bestProvider);
            }

            if (mLocation == null) {
                mLocation = new Location("");
            }

            if (newLoction != null) {
                mLocation.setLatitude(newLoction.getLatitude());
                mLocation.setLongitude(newLoction.getLongitude());


                MyLog.Set("e", getClass(), "newLoction.getLatitude() => " + newLoction.getLatitude());
                MyLog.Set("e", getClass(), "newLoction.getLongitude() => " + newLoction.getLongitude());
            } else {

                MyLog.Set("e", getClass(), "newLoction == null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


//        try {
//
//            Criteria criteria = new Criteria();
//            criteria.setPowerRequirement(Criteria.POWER_LOW);
//            criteria.setAccuracy(Criteria.ACCURACY_FINE);
//
//            String bestProvider = mLocManager.getBestProvider(criteria, true);
//            Location newLoction = null;
////            if (bestProvider != null)
////                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////                    // TODO: Consider calling
////                    //    ActivityCompat#requestPermissions
////                    // here to request the missing permissions, and then overriding
////                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                    //                                          int[] grantResults)
////                    // to handle the case where the user grants the permission. See the documentation
////                    // for ActivityCompat#requestPermissions for more details.
////                    return;
////                }
//            newLoction = mLocManager.getLastKnownLocation(bestProvider);
//
//            if (mLocation == null) {
//                mLocation = new Location("");
//            }
//
//            if (newLoction != null) {
//                mLocation.setLatitude(newLoction.getLatitude());
//                mLocation.setLongitude(newLoction.getLongitude());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        if (mediaPlayer != null && !mediaPlayer.isPlaying() && isPlayAudio) {


            mediaPlayer.start();
            MyLog.Set("d", getClass(), "音樂播放, 開始計時");


            startTimer();

        }

        if (adapter != null && photoContentsList.size() > 0) {

            setCurrentPoint();

        }

    }

    @Override
    public void onDestroy() {


        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (board != null && board.getPopupWindow().isShowing()) {
            board.dismiss();
        }

        cancelTask(sendLikeTask);
        cancelTask(deleteLikeTask);
        cancelTask(getAlbumInfoTask);
        cancelTask(getPointTask);
        cancelTask(firstCollectAlbumTask);
        cancelTask(sendContributeTask);
        cancelTask(checkShareTask);
        cancelTask(shareTask);
        cancelTask(protocol13);
        cancelTask(protocol109);
        cancelTask(protocol111);

        if (protocol108HashMap != null && protocol108HashMap.size() > 0) {

            for (int i = 0; i < protocol108HashMap.size(); i++) {

                if (protocol108HashMap.get(i) != null && !protocol108HashMap.get(i).isCancelled()) {
                    cancelTask(protocol108HashMap.get(i));
                }

            }

        }

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer = null;

        cleanTimer();


        autoScrollHandler.removeCallbacksAndMessages(null);

        autoScrollHandler = null;

        callbackManager = null;

        mLocation = null;

        mapPhoto = null;
        mapAlbum = null;


        //20171023
//        Picasso.Builder picassoBuilder = null;
//        if(adapter!=null){
//            picassoBuilder = adapter.getPicasso();
//        }


        int count = photoContentsList.size();
        for (int i = 0; i < count; i++) {

            String url = photoContentsList.get(i).getImage_url();

            Picasso.with(getApplicationContext()).invalidate(url);


            String urlthum = photoContentsList.get(i).getImage_url_thumbnail();
            Picasso.with(getApplicationContext()).invalidate(urlthum);


            if (photoContentsList.get(i).getVideoView() != null) {
                photoContentsList.get(i).getVideoView().release();
            }


        }


        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && edPoint != null) {

                imm.hideSoftInputFromWindow(edPoint.getWindowToken(), 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Glide.get(mActivity.getApplicationContext()).clearMemory();

        System.gc();

        super.onDestroy();
    }


}

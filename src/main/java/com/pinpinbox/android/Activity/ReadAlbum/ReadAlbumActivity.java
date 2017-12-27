package com.pinpinbox.android.Activity.ReadAlbum;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.logger.Logger;
import com.pinpinbox.android.Mode.LOG;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.AlbumDownLoad;
import com.pinpinbox.android.SelfMadeClass.ClickUtils;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.StringClass.TaskKeyClass;
import com.pinpinbox.android.StringClass.UrlClass;
import com.pinpinbox.android.Utility.BitmapUtility;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.MapUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.ControllableViewPager;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.MyGallery;
import com.pinpinbox.android.Views.PinchImageView;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Reader2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.VideoPlayActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.YouTubeActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.GalleryAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.LinkText;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogSet;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.breedrapps.vimeoextractor.OnVimeoExtractionListener;
import uk.breedrapps.vimeoextractor.VimeoExtractor;
import uk.breedrapps.vimeoextractor.VimeoVideo;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/*
 * Created by vmage on 2015/12/8.
 */
public class ReadAlbumActivity extends DraggerActivity implements LocationListener {


    private SharedPreferences getdata;
    private SharedPreferences getMusicData;
    private SharedPreferences getDeviceData;
    private SharedPreferences albumGiftChangeData;

    private Activity mActivity;
    private Bundle bundle;
    private Bundle mSavedInstanceState = null;
    private MediaPlayer mMediaPlayer;
    private MediaPlayer pageMediaPlayer;
    private LoadingAnimation loading;
    private LocationManager mLocManager;
    private Location mLocation = null;
    private CountDownTimer countDownTimer;//兌換時間倒數
    private AlbumDownLoad albumDownLoad;
    private NoConnect noConnect;
    private CloseReadTask closeReadTask;
    private Dialog dialogGift;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private Bitmap bmpGiftImg;
    private Bitmap bmpExchangeImg;

    private Handler audioHandler = new Handler();
    private Runnable audioRun = new Runnable() {
        @Override
        public void run() {
            MyLog.Set("d", mActivity.getClass(), "延遲執行音樂播放");
            setPageMusic(page);
        }
    };

    private boolean isDownload = false;
    private boolean isOptionsOpen = false;
    private boolean isItemClick = false;
    private boolean isPhotoMode = false;
    private boolean buy;
    private boolean albumeExist = false;
    private boolean isAlbumAudioExist = false;
    private boolean albumMusicPlay;
    private boolean isAutoPlay = false;
    private boolean isAutoPlaying = true;

    private String TAG = "ReadAlbumActivity";
    private String id, token;
    private String device_id;
    private String album_id;
    private String download_id;
    private String coverUrl;
    private String myDir;
    private String photousefor_user_id;
    private String photousefor_name, photousefor_image, strPhotoUseForDescription;
    private String mapMarkTitle;
    private String strDownloadCover;

    private String p42Result, p42Message;
    private String p43Result, p43Message;
    private String p73Result, p73Message;
    private String p83Result, p83Message;
    private String p84Result, p84Message;

    private String sdPath = Environment.getExternalStorageDirectory() + "/";
    private String dirAlbumList = "albumlist/";
    private String dirZip = "zip/";
    private String event_id;

    private int MODE = 0;
    private int CONTRIBUTEMODE = 1;
    private int total = 0;

    private boolean bMediaPlayerPause = false;
    private boolean bPageMediaPlayerPause = false;
    private boolean isFirstFocusChanged = false;

    private RelativeLayout rActionBar, rGallery, rVoice;
    private LinearLayout linTool;
    private LinearLayout linDescription;
    private ImageView backImg, optionImg, shareImg;
    private ImageView voiceImg, autoplayImg;
    private TextView tvTitle, tvPage, tvDescription;
    private ControllableViewPager viewPager;
    private MyGallery gallery;

    //dataview
    private RelativeLayout rAlbumInfo, rMap;
    private LinearLayout linAlbumInfo;
    private ImageView readOptions_close;
    private TextView data_tvAlbumDescription, data_tvAuthor, data_tvCreateDate;
    private ImageView dataLinkA, dataLinkB;


//   private String data_strAlbumDescription, data_strAuthor, data_strCreateDate, data_strAlbumLocation;
    //audio_mode(相本音頻模式) => none(無) / singular(單首播放) / plural(多首播放)
    //audio_loop(相本音頻循環播放) => true(是) / false(否)
    //audio_refer(相本音頻參考方式) => file(檔案) / embed(嵌入) / none(無) / system(系統)
    //audio_target(相本音頻參考目標) => bgm.mp3 / {url} / null / bgm.mp3


    private String strAlbumAudio_mode, strAlbumAudio_loop, strAlbumAudio_refer, strAlbumAudio_target; //相本音樂相關參數
    private String strAlbumAuthor, strAlbumDescription, strAlbumInserttime, strAlbumLocation, strAlbumTitle;

    private int w, h;

    private int page = 0; //頁數

    private List<String> picList;
    private List<View> viewList;
    private List<File> files;

    private List<String> photoAudioLoopList;
    private List<String> photoAudioReferList;
    private List<String> photoAudioTargetList;

    private List<String> photoDescriptionList;
    private List<Integer> photoDurationList;
    private List<String> photoLocationList;

    private List<String> photoNameList;
    private List<String> photoIdList;
    private List<String> photoUseForList;

    private List<String> photoVideoReferList;
    private List<String> photoVideoTargetList;

    private List<String> photoHyperlinkList;

//    private ArrayList<HashMap<String, Object>> photoHyperlinkMapList;

    private AnimationDrawable animationDrawable;

    private PageAdapter pageAdapter;
    private GalleryAdapter gAdapter;

    //location
    private GoogleMap mMap;
    private double locLat;
    private double locLng;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:

                    albumDownLoad.getCrdlg().dismiss();

                    albumMusicPlay = false; //defalue close music

                    albumWork();
                    break;
                case 1://結束打開gift的動畫
                    animationDrawable.stop();
                    viewPager.noScroll(false);
                    break;


            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(page + 1, true);
        }
    };

    private Handler autoScrollHandler = new Handler();


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

    @SuppressWarnings("ResourceType")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_read_album);

        SystemUtility.SysApplication.getInstance().addActivity(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {

                doShare();

                MyLog.Set("d", mActivity.getClass(), "shareDialog => onSuccess");


            }

            @Override
            public void onCancel() {
                Log.d(TAG, "shareDialog => onCancel");
            }

            @Override
            public void onError(FacebookException error) {

                MyLog.Set("d", mActivity.getClass(), "shareDialog => onError");

            }
        });

        mSavedInstanceState = savedInstanceState;

        getBundle_album_id();
        init();
        initAlbumdDataView();
        optionsClick();
        back();


//        checkAlbumExist();
//        if (albumeExist) {//直接開啟相本
//            albumWork();
//        } else {//下載
//
//            if (HttpUtility.isConnect(mActivity)) {
//                download();
//            } else {
//                noConnect = new NoConnect(mActivity);
//            }
//        }

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            MyLog.Set("d", mActivity.getClass(), "landscape 橫屏");
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            MyLog.Set("d", mActivity.getClass(), "portrait 豎屏");
        }

    }


    //Activity窗口获得或失去焦点时被调用,在onResume之后或onPause之后
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        MyLog.Set("d", getClass(), "onWindowFocusChanged");
        if (!isFirstFocusChanged) {
            MyLog.Set("d", getClass(), "!isFirstFocusChanged");

            checkAlbumExist();
            if (albumeExist) {//直接開啟相本
                albumWork();
            } else {//下載

                if (HttpUtility.isConnect(mActivity)) {
                    download();
                } else {
                    noConnect = new NoConnect(mActivity);
                }
            }
            isFirstFocusChanged = true;
        }
    }

    private void getBundle_album_id() {
        bundle = getIntent().getExtras();

        if (bundle != null) {

            album_id = bundle.getString("album_id");
            Log.d(TAG, "getBundle_album_id => " + album_id);

            event_id = bundle.getString("event_id", "");
            MODE = bundle.getInt("contributeMode", 0);


            Log.d(TAG, "event_id => " + event_id);
            Log.d(TAG, "MODE => " + MODE);

        }
    }

    private void init() {
        mActivity = this;

        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        getMusicData = getSharedPreferences(SharedPreferencesDataClass.albummusic, Activity.MODE_PRIVATE);
        getDeviceData = getSharedPreferences(SharedPreferencesDataClass.deviceDetail, Activity.MODE_PRIVATE);
        albumGiftChangeData = getSharedPreferences("pinpinbox_" + album_id, Activity.MODE_PRIVATE);
        id = getdata.getString("id", "");
        token = getdata.getString("token", "");
        coverUrl = getdata.getString("album_" + album_id + "_" + "cover", "");
        device_id = getDeviceData.getString("deviceid", "");
        myDir = "PinPinBox" + id + "/";
        albumMusicPlay = getMusicData.getBoolean(album_id, false);

        if (albumMusicPlay) {
            MyLog.Set("d", getClass(), "播放音樂");
        } else {
            MyLog.Set("d", getClass(), "不播放音樂");
        }


        picList = new ArrayList<>();
        viewList = new ArrayList<>();

        photoHyperlinkList = new ArrayList<>();

        photoAudioLoopList = new ArrayList<>();
        photoAudioReferList = new ArrayList<>();
        photoAudioTargetList = new ArrayList<>();

        photoDescriptionList = new ArrayList<>();
        photoDurationList = new ArrayList<>();
        photoLocationList = new ArrayList<>();

        photoNameList = new ArrayList<>();
        photoIdList = new ArrayList<>();
        photoUseForList = new ArrayList<>();

        photoVideoReferList = new ArrayList<>();
        photoVideoTargetList = new ArrayList<>();

        mMediaPlayer = new MediaPlayer();
        loading = new LoadingAnimation(mActivity);

        rActionBar = (RelativeLayout) findViewById(R.id.rActionBar);
//        rGallery = (RelativeLayout) findViewById(R.id.rGallery);
//        rAlbumInfo = (RelativeLayout) findViewById(R.id.rAlbumInfo);
//        rVoice = (RelativeLayout) findViewById(R.id.rVoice);
//        linTool = (LinearLayout) findViewById(R.id.linTool);
        gallery = (MyGallery) findViewById(R.id.gallery);

//        linAlbumInfo = (LinearLayout) findViewById(R.id.linAlbumInfo);
        linDescription = (LinearLayout) findViewById(R.id.linDescription);
//        backImg = (ImageView) findViewById(R.id.readBack);
//        optionImg = (ImageView) findViewById(R.id.optionImg);
        voiceImg = (ImageView) findViewById(R.id.voiceImg);
        autoplayImg = (ImageView) findViewById(R.id.autoplayImg);
        shareImg = (ImageView) findViewById(R.id.shareImg);
        tvTitle = (TextView) findViewById(R.id.readTitle);
        tvPage = (TextView) findViewById(R.id.tvPage);
        tvDescription = (TextView) findViewById(R.id.tvPageDescription);
        viewPager = (ControllableViewPager) findViewById(R.id.readViewPager);
        getDisplay();


//        setInterval(long)設置自動滾動的時間以毫秒為單位，默認為DEFAULT_INTERVAL。
//        setDirection(int)設置自動滾動的方向，默認為RIGHT。
//        setCycle(boolean) 設置是否自動循環時自動滾動到達最後一個或第一個項目，默認為true。
//        setScrollDurationFactor(double) 設置由滑動動畫的持續時間會改變的因素。
//        setSlideBorderMode(int)設置如何在最後一個或第一個項目時滑動處理，默認為SLIDE_BORDER_MODE_NONE。
//        setStopScrollWhenTouch(boolean) 設置是否停止自動滾動觸摸時，默認為true。
//        setBorderAnimation(boolean) 設置是否在最後或第一項動畫時自動滾動，默認值為true。
//        你不能用結合ViewPagerIndicator如果setCycle(true)。

    }

    /*獲取銀幕長寬*/
    private void getDisplay() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels;
        h = dm.heightPixels;
    }

    /*設定album info 控件*/
    private void initAlbumdDataView() {

        rAlbumInfo.setY(h);

        try {
//            mMap = (
//                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)
//            ).getMap();
//            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            UiSettings setting = mMap.getUiSettings();
//            setting.setTiltGesturesEnabled(true);
//            mMap.setMyLocationEnabled(true);



            mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        rMap = (RelativeLayout) findViewById(R.id.rMap);

//        data_scrollview = (ScrollView) findViewById(R.id.album_data_scrollview);
//        data_tvPictureDescription = (TextView) findViewById(R.id.album_data_page_decription);
//        data_tvAlbumDescription = (TextView) findViewById(R.id.album_data_albumdecription);
//        data_tvAuthor = (TextView) findViewById(R.id.album_data_author);
//        data_tvCreateDate = (TextView) findViewById(R.id.album_data_createdate);

//        dataVoiceImg = (ImageView) findViewById(R.id.album_data_voice);
//        dataShare = (ImageView) findViewById(R.id.album_data_share);
//        dataLinkA = (ImageView) findViewById(R.id.album_data_link_a);
//        dataLinkB = (ImageView) findViewById(R.id.album_data_link_b);
//
//        readOptions_close = (ImageView) findViewById(R.id.closeOptionImg);
    }

    /*album info 開關動畫*/
    private void optionsClick() {

        optionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!HttpUtility.isConnect(mActivity)) {
                    noConnect = new NoConnect(mActivity);
                    return;
                }

                isOptionsOpen = true;

                ViewPropertyAnimator up = rAlbumInfo.animate();
                up.setDuration(300);
                up.translationYBy(-h);
                up.start();
                viewPager.noScroll(true);

                try {
                    setMapLocation(page);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    setHyperlink(page);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        readOptions_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPropertyAnimator down = rAlbumInfo.animate();
                down.setDuration(300);
                down.translationYBy(h);
                down.start();
                viewPager.noScroll(false);
                mMap.clear();
                isOptionsOpen = false;
            }
        });

    }

    /*檢查此作品是否存在*/
    private void checkAlbumExist() {
        File f = new File(sdPath + myDir + dirAlbumList);
        String[] albumlist = f.list();
        for (int i = 0; i < albumlist.length; i++) {
            if (albumlist[i].equals(album_id)) {
                albumeExist = true;
                break;
            } else {
                albumeExist = false;
            }
        }

        if (albumeExist) {
            MyLog.Set("d", getClass(), "albumeExist = true");
        } else {
            MyLog.Set("d", getClass(), "albumeExist = false");
        }
    }

    private void albumWork() {
        LoadContentTask task = new LoadContentTask();
        task.execute();
    }

    private void download() {

        isDownload = true;

        buy = bundle.getBoolean("buy");

        if (buy) {
            download_id = bundle.getString("download_id");
        }

        albumDownLoad = new AlbumDownLoad();
        albumDownLoad.getAlbumDownLoad(mActivity, album_id, download_id, mHandler, buy);

        View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast, null);
        final Toast toast = new Toast(mActivity.getApplicationContext());
        TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
//        tvToast.setText(R.string.downloading);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();


        albumDownLoad.getCrdlg().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {


                Bitmap downBackGroundBmp = albumDownLoad.getDownBackGroundBmp();

                if (downBackGroundBmp != null && !downBackGroundBmp.isRecycled()) {
                    downBackGroundBmp.recycle();
                }

                setTitleByAlbumName();
                MyLog.Set("d", mActivity.getClass(), "刪除zip => " + album_id);
                File deleteZip = new File(sdPath + myDir + dirZip + album_id);

                if (deleteZip.exists()) {

                    deleteZip.delete();

                }

                isDownload = false;

                //2016.06.09 clean cover

                if (coverUrl != null && !coverUrl.equals("")) {
                    Picasso.with(mActivity.getApplicationContext()).invalidate(coverUrl);
                    System.gc();
                }

                toast.cancel();
                if (albumDownLoad.getCrdlg().isShowing()) {
                    albumDownLoad.getCrdlg().dismiss();
                }

            }
        });

    }

    /*獲取 info 內容*/
    private void getTxt() {
        //audio_mode(相本音頻模式) => none(無) / singular(單首播放) / plural(多首播放)
        //audio_loop(相本音頻循環播放) => true(是) (1) / false(否) (0)
        //audio_refer(相本音頻參考方式) => file(檔案) / embed(嵌入) / none(無) / system(系統)
        //audio_target(相本音頻參考目標) => bgm.mp3 / {url} / null / bgm.mp3
        try {
            String strInfoPath = FileUtility.getTXTdata(sdPath + myDir + dirAlbumList + album_id + "/" + "info.txt#asdf");
            if (strInfoPath != null) {
                JSONObject jsonObject = new JSONObject(FileUtility.getTXTdata(sdPath + myDir + dirAlbumList + album_id + "/" + "info.txt#asdf"));

                Logger.json(jsonObject.toString());

                strAlbumTitle = JsonUtility.GetString(jsonObject, "title");
                strAlbumAuthor = JsonUtility.GetString(jsonObject, "author");
                strAlbumDescription = JsonUtility.GetString(jsonObject, "description");
                strAlbumInserttime = JsonUtility.GetString(jsonObject, "inserttime");
                strAlbumLocation = JsonUtility.GetString(jsonObject, "location");
                strAlbumAudio_mode = JsonUtility.GetString(jsonObject, "audio_mode");
                strAlbumAudio_loop = JsonUtility.GetString(jsonObject, "audio_loop");
                strAlbumAudio_refer = JsonUtility.GetString(jsonObject, "audio_refer");
                strAlbumAudio_target = JsonUtility.GetString(jsonObject, "audio_target");

                if (LOG.isLogMode) {
                    Log.e(TAG, " strAlbumTitle => " + strAlbumTitle);
                    Log.e(TAG, " strAlbumAuthor => " + strAlbumAuthor);
                    Log.e(TAG, " strAlbumDescription => " + strAlbumDescription);
                    Log.e(TAG, " strAlbumInserttime => " + strAlbumInserttime);
                    Log.e(TAG, " strAlbumLocation => " + strAlbumLocation);
                    Log.e(TAG, " strAlbumAudio_mode => " + strAlbumAudio_mode);
                    Log.e(TAG, " strAlbumAudio_loop => " + strAlbumAudio_loop);
                    Log.e(TAG, " strAlbumAudio_refer => " + strAlbumAudio_refer);
                    Log.e(TAG, " strAlbumAudio_target => " + strAlbumAudio_target);
                }

                String photo = JsonUtility.GetString(jsonObject, "photo");
                JSONArray jsonArray = new JSONArray(photo);

                int array = jsonArray.length();
                for (int i = 0; i < array; i++) {
                    JSONObject j = (JSONObject) jsonArray.get(i);
                    String audio_loop = j.getString("audio_loop");//(相片音頻循環播放) => true(是) / false(否)
                    String audio_refer = j.getString("audio_refer");//(相片音頻參考方式) => file(檔案) / embed(嵌入) / none(無) / system(系統)
                    String audio_target = j.getString("audio_target");//(相片音頻參考目標) => [photo index].mp3 / {url} / null / [photo index].mp3


                    String description = j.getString("description");

                    int duration = j.getInt("duration");
                    String location = j.getString("location");
                    String name = j.getString("name");
                    String photo_id = j.getString("photo_id");
                    String usefor = j.getString("usefor");
                    String video_refer = j.getString("video_refer");
                    String video_target = j.getString("video_target");
                    String hyperlink = j.getString("hyperlink");
//                    if (!hyperlink.equals("null")) {
//                        JSONArray jarray = new JSONArray(hyperlink);
//                        System.out.println("有hyperlink");
//                        for (int k = 0; k < jarray.length(); k++) {
//
//                            JSONObject object = (JSONObject) jarray.get(k);
//                            String icon = object.getString("icon");
//                            String text = object.getString("text");
//                            String url = object.getString("url");
//
//                            if (LOG.isLogMode) {
//                                Log.d(TAG, "icon => " + icon);
//                                Log.d(TAG, "text => " + text);
//                                Log.d(TAG, "url => " + url);
//                            }
//
//                            HashMap<String, Object> map = new HashMap<>();
//                            map.put("icon", icon);
//                            map.put("text", text);
//                            map.put("url", url);
//                            photoHyperlinkMapList.add(map);
//                        }
//                    }

                    if (LOG.isLogMode) {
                        Log.e(TAG, "audio_loop => " + audio_loop + "(相片音頻循環播放) => true(是)(1) / false(否)(0)"); //1 = 是 , 0 = 否
                        Log.e(TAG, "audio_refer => " + audio_refer + "(相片音頻參考方式) => file(檔案) / embed(嵌入) / none(無) / system(系統)");
                        Log.e(TAG, "audio_target => " + audio_target + "(相片音頻參考目標) => [photo index].mp3 / {url} / null / [photo index].mp3");
                        Log.e(TAG, "description => " + description);
                        Log.e(TAG, "duration => " + duration);
                        Log.e(TAG, "hyperlink => " + hyperlink);
                        Log.e(TAG, "location => " + location);
                        Log.e(TAG, "name => " + name);
                        Log.e(TAG, "photo_id => " + photo_id);
                        Log.e(TAG, "usefor => " + usefor + "(相片用途) => image(圖像) / video(視頻) / exchange(兌換) / slot(拉霸)"); //(相片用途) => image(圖像) / video(視頻) / exchange(兌換) / slot(拉霸)
                        Log.e(TAG, "video_refer => " + video_refer + "(相片影視參考方式) => file(檔案) / embed(嵌入) / none(無) / system(系統)");
                        Log.e(TAG, "video_target => " + video_target + "[photo index].mp4 / {url} / null / [photo index].mp4"); //(相片影視參考目標) =>
                        System.out.println("-------------------------------------------------------------------------------");
                    }

                    photoHyperlinkList.add(hyperlink);

                    photoAudioLoopList.add(audio_loop);
                    photoAudioReferList.add(audio_refer);
                    photoAudioTargetList.add(audio_target);

                    photoDescriptionList.add(description);
                    photoDurationList.add(duration);
                    photoLocationList.add(location);

                    photoNameList.add(name);
                    photoIdList.add(photo_id);
                    photoUseForList.add(usefor);

                    photoVideoReferList.add(video_refer);
                    photoVideoTargetList.add(video_target);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*設置title*/
    private void setTitleByAlbumName() {
        if (strAlbumTitle != null) {
            tvTitle.setText(strAlbumTitle);
        }
    }

    /*獲取相片*/
    private void getPicture() {

        picList = FileUtility.getPictureListByAlbumSuffix_asdf(sdPath + myDir + dirAlbumList + "/" + album_id + "/");

        files = new ArrayList<>();

        for (int i = 0; i < picList.size(); i++) {

            File file = new File(picList.get(i));
            files.add(file);

            MyLog.Set("d", this.getClass(), "picList.get(i) => " + picList.get(i));

        }

        total = files.size();

    }

    /*設置相片*/
    private void setPicture() {

        pageAdapter = new PageAdapter(mActivity, files);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(3);
//        viewPager.setPageTransformer(true, new TabletTransformer());

    }

    /*設置底下縮略圖*/
    private void setGallery() {

        gAdapter = new GalleryAdapter(mActivity, files, photoUseForList, photoAudioReferList);
        gallery.setAdapter(gAdapter);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                if (isAutoPlay) {
                    autoScrollHandler.removeCallbacks(runnable);
                    MyLog.Set("d", mActivity.getClass(), "autoScrollHandler.removeCallbacks(runnable)");
                }
                int duration = photoDurationList.get(position);
                MyLog.Set("d", mActivity.getClass(), "duration => " + duration);

                setAutoPlay(duration, position);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(position);
                        isItemClick = false;
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        if (isAutoPlay) {
            gallery.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    autoScrollHandler.removeCallbacks(runnable);
                    MyLog.Set("d", mActivity.getClass(), "autoScrollHandler.removeCallbacks(runnable)");
                    return false;
                }
            });
        }

    }

    /*設置頁數*/
    private void setPage(int page) {

        tvPage.setText((page + 1) + " / " + total);

    }

    String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    public String checkYoutubeId(@NonNull String videoUrl) {
        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);
        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    /*設置每頁內容 (圖像 影像 兌換 拉霸)*/
    private void setPageDetail(final int page) {

        View v = viewPager.findViewById(page);

        final ImageView centerImg = (ImageView) v.findViewById(R.id.centerImg);
        final ImageView exchangeImg = (ImageView) v.findViewById(R.id.exchangeImg);
        final TextView tvExchange = (TextView) v.findViewById(R.id.tvExchange);
        final TextView tvTimer = (TextView) v.findViewById(R.id.tvTime);
        RelativeLayout rPageType = (RelativeLayout) v.findViewById(R.id.r_page_type);

        try {
            String usefor = photoUseForList.get(page);

            switch (usefor) {
                case "image":
                    if (LOG.isLogMode) {
                        Log.d(TAG, "此頁為 => image(圖像)");
                    }
                    rPageType.setVisibility(View.INVISIBLE);
                    break;
                case "video":
                    rPageType.setBackgroundColor(Color.parseColor("#000000"));
                    rPageType.setAlpha(0.5f);
                    if (LOG.isLogMode) {
                        Log.d(TAG, "此頁為 => video(視頻)");
                    }
                    centerImg.setImageResource(R.drawable.click_videoplay_310x310);
                    centerImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                                return;
                            }

                            if (!HttpUtility.isConnect(mActivity)) {
                                noConnect = new NoConnect(mActivity);
                                return;
                            }

                            View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast, null);
                            TextView textView = (TextView) view.findViewById(R.id.tvToast);
                            textView.setText("準備撥放影片");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(view);
                            toast.show();

                           /* (相片影視參考方式) => file(檔案) / embed(嵌入) / none(無) / system(系統)*/
                            switch (photoVideoReferList.get(page)) {
                                case "file":
                                    if (LOG.isLogMode) {
                                        Log.d(TAG, "播放方式為 => file");
                                    }

                                    String path = "";

                                    String str = photoVideoTargetList.get(page).substring(0, 4);

                                    if (str.equals("http")) {
                                        path = photoVideoTargetList.get(page);
                                    } else {
                                        path = sdPath + myDir + dirAlbumList + album_id + "/" + photoVideoTargetList.get(page) + "#asdf";
                                    }


                                    if (LOG.isLogMode) {
                                        Log.e(TAG, "file路徑 => " + path);
                                    }

                                    Intent intentV = new Intent(ReadAlbumActivity.this, VideoPlayActivity.class);
                                    Bundle bundleV = new Bundle();
                                    bundleV.putString("videopath", path);
                                    intentV.putExtras(bundleV);
                                    startActivity(intentV);
                                    ActivityAnim.StartAnim(mActivity);


                                    break;
                                case "embed":
                                    if (LOG.isLogMode) {
                                        Log.d(TAG, "播放方式為 => embed");
                                    }

                                    String strEmbedPath = photoVideoTargetList.get(page);


                                    /*2016.09.06新增判斷是否youtube*/
                                    try {
                                        String ytUrl = checkYoutubeId(strEmbedPath);


                                        if (ytUrl == null || ytUrl.equals("null")) {


                                            if (containsString(strEmbedPath, "vimeo")) {

                                                MyLog.Set("d", mActivity.getClass(), "確認vimeo播放 => " + strEmbedPath);


                                                VimeoExtractor.getInstance().fetchVideoWithURL(strEmbedPath, null, new OnVimeoExtractionListener() {
                                                    @Override
                                                    public void onSuccess(VimeoVideo video) {


                                                        MyLog.Set("d", mActivity.getClass(), video.getStreams().toString());

                                                        String stream = "";

                                                        stream = video.getStreams().get("1080p");

                                                        if (stream == null || stream.equals("null")) {
                                                            stream = video.getStreams().get("720p");
                                                            if (stream == null || stream.equals("null")) {
                                                                stream = video.getStreams().get("360p");
                                                            }
                                                        }


                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("videopath", stream);

                                                        Intent intent = new Intent(mActivity, VideoPlayActivity.class);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                        ActivityAnim.StartAnim(mActivity);

                                                    }

                                                    @Override
                                                    public void onFailure(Throwable throwable) {
                                                        //Error handling here
                                                    }
                                                });


                                            } else {

                                                MyLog.Set("d", mActivity.getClass(), "ytUrl == null && containsString(strEmbedPath, vimeo) == false => " + "使用一般撥放器");

                                                Intent intent = new Intent(ReadAlbumActivity.this, VideoPlayActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("videopath", strEmbedPath);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                ActivityAnim.StartAnim(mActivity);

//                                                Bundle bundle = new Bundle();
//                                                bundle.putString("url", strEmbedPath);
//                                                Intent intent = new Intent(ReadAlbumActivity.this, WebView2Activity.class);
//                                                intent.putExtras(bundle);
//                                                startActivity(intent);
//                                                ActivityAnim.StartAnim(mActivity);
                                            }


                                        } else {


                                            MyLog.Set("d", mActivity.getClass(), "確認可以用youtube播放 => " + ytUrl);

                                            Intent intentEmbed = new Intent(ReadAlbumActivity.this, YouTubeActivity.class);
                                            Bundle bundleEmbed = new Bundle();
                                            bundleEmbed.putString("path", strEmbedPath);
                                            intentEmbed.putExtras(bundleEmbed);
                                            startActivity(intentEmbed);
                                            ActivityAnim.StartAnim(mActivity);

                                        }


                                    } catch (Exception e) {

                                        MyLog.Set("e", mActivity.getClass(), "catch => 使用一般撥放器");

                                        Intent intent = new Intent(ReadAlbumActivity.this, VideoPlayActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("videopath", strEmbedPath);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        ActivityAnim.StartAnim(mActivity);

//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("url", strEmbedPath);
//                                        Intent intent = new Intent(ReadAlbumActivity.this, WebView2Activity.class);
//                                        intent.putExtras(bundle);
//                                        startActivity(intent);
//                                        ActivityAnim.StartAnim(mActivity);


                                    }



                                    break;
                                case "none":
                                    if (LOG.isLogMode) {
                                        Log.d(TAG, "播放方式為 => none");
                                    }
                                    break;
                                case "system":
                                    if (LOG.isLogMode) {
                                        Log.d(TAG, "播放方式為 => system");
                                    }
                                    break;
                            }
                        }
                    });

                    break;
                case "exchange"://button_read_album_exchange_click
                    if (LOG.isLogMode) {
                        Log.d(TAG, "此頁為 => exchange(兌換)");
                    }
                    rPageType.setBackgroundColor(Color.parseColor("#000000"));
                    rPageType.setAlpha(0.5f);

                    String photoChangeType = albumGiftChangeData.getString(photoIdList.get(page), "");
                    if (photoChangeType == null | photoChangeType.equals("")) {
//                        exchangeImg.setImageResource(R.drawable.click_read_album_exchange);
                        tvExchange.setText(R.string.exchange);
                        exchangeImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (HttpUtility.isConnect(mActivity)) {
                                    GetUserForByExchangeTask g = new GetUserForByExchangeTask(page, exchangeImg, tvExchange);
                                    g.execute();
                                } else {
                                    noConnect = new NoConnect(mActivity);
                                }
                            }
                        });
                    } else {
                        JSONObject jsonObject = new JSONObject(photoChangeType);
                        if ((Object) jsonObject.getBoolean("changeType") != null) {
                            final boolean change = jsonObject.getBoolean("changeType");
                            if (change) {

                                /*2016.08.11取消已兌換顯示*/
                                exchangeImg.setImageResource(R.drawable.icon_read_album_exchanged);
                                tvExchange.setTextColor(Color.parseColor("#393939"));

                                String result = jsonObject.getString("result");
                                if (result.equals("1")) {
                                    tvExchange.setText(R.string.exchanged);
                                } else if (result.equals("3")) {
                                    tvExchange.setText(R.string.exchanged_end);
                                } else if (result.equals("2")) {
                                    tvExchange.setText(R.string.exchanged);
                                }


                            } else {
//                                exchangeImg.setImageResource(R.drawable.click_read_album_exchange);
                                tvExchange.setText(R.string.exchange);

                                exchangeImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (LOG.isLogMode) {
                                            Log.d(TAG, "點擊兌換再重新進行一次判斷");
                                        }

                                        if (HttpUtility.isConnect(mActivity)) {
                                            GetUserForByExchangeTask g = new GetUserForByExchangeTask(page, exchangeImg, tvExchange);
                                            g.execute();
                                        } else {
                                            noConnect = new NoConnect(mActivity);
                                        }


                                    }
                                });
                            }
                        }
                    }


                    break;
                case "slot":
                    if (LOG.isLogMode) {
                        Log.d(TAG, "此頁為 => slot(拉霸)");
                    }
                    rPageType.setBackgroundColor(Color.parseColor("#000000"));
                    rPageType.setAlpha(0.5f);

                    String photoChangeType2 = albumGiftChangeData.getString(photoIdList.get(page), "");

                    if (photoChangeType2 == null | photoChangeType2.equals("")) {
                        slotStatusInit(page, centerImg, exchangeImg, tvExchange, tvTimer);
                    } else {
                        JSONObject obj = new JSONObject(photoChangeType2);
                        if ((Object) obj.getBoolean("changeType") != null) {
                            boolean change = obj.getBoolean("changeType");
                            if (change) {
                                centerImg.setVisibility(View.GONE);
                                exchangeImg.setImageResource(R.drawable.icon_read_album_exchanged);
                                tvExchange.setTextColor(Color.parseColor("#393939"));

                                String result = obj.getString("result");
                                if (result.equals("1")) {
                                    tvExchange.setText(R.string.exchanged);
                                } else if (result.equals("3")) {
                                    tvExchange.setText(R.string.exchanged_end);
                                } else if (result.equals("2")) {
                                    tvExchange.setText(R.string.exchanged);
                                }


                            } else {
//                                exchangeImg.setImageResource(R.drawable.click_read_album_exchange);
                                tvExchange.setText(R.string.exchange);
                                exchangeImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        System.out.println("點擊了兌換");
                                        if (HttpUtility.isConnect(mActivity)) {
                                            AfterGiftToChangeTask a = new AfterGiftToChangeTask(page, exchangeImg, tvExchange, null);
                                            a.execute();
                                        } else {
                                            noConnect = new NoConnect(mActivity);
                                        }

                                    }
                                });
                            }
                        } else {
                            slotStatusInit(page, centerImg, exchangeImg, tvExchange, tvTimer);
                        }
                    }


                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setPageListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {

                if (!HttpUtility.isConnect(mActivity)) {
                    noConnect = new NoConnect(mActivity);
                    return;
                }


                page = position;


                setPage(position);
                setPageDescription(position);
                setMode(position);
                setPageDetail(position);


                //不是經由點擊觸發 也就是滑動觸發
                if (!isItemClick) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gallery.setSelection(position, true);
                        }
                    });
                }


                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if ((position + 1) < files.size()) {

                                if (viewPager.findViewById(page + 1) != null) {
                                    PinchImageView lastPicImg = (PinchImageView) (viewPager.findViewById(page + 1)).findViewById(R.id.pic);
                                    lastPicImg.reset();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            if (position - 1 >= 0) {
                                if (viewPager.findViewById(page - 1) != null) {
                                    PinchImageView nextPicImg = (PinchImageView) viewPager.findViewById(page - 1).findViewById(R.id.pic);
                                    nextPicImg.reset();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, 400);


                if (!isAlbumAudioExist) {
                    //整本作品音樂不存在才執行單頁音樂


                    audioHandler.postDelayed(audioRun, 500);


//                    setPageMusic(page);
                }


                if (animationDrawable != null) {//暫時解決
                    if (animationDrawable.isRunning()) {
                        animationDrawable.stop();
                    }
                }


                if (position == 30 || position == 60 || position == 90 || position == 120 || position == 150
                        || position == 180 || position == 210 || position == 240) {
                    cleanPicasso();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0://沒動作

                        MyLog.Set("d", mActivity.getClass(), "沒動作");
                        break;


                    case 1://正在滑動

                        if (isAutoPlay) {
                            autoScrollHandler.removeCallbacks(runnable);
                            MyLog.Set("d", mActivity.getClass(), "autoScrollHandler.removeCallbacks(runnable)");
                        }

                        MyLog.Set("d", mActivity.getClass(), "正在滑動");

                        break;
                    case 2://滑動完畢
                        MyLog.Set("d", mActivity.getClass(), "滑動完畢");

                        int p = viewPager.getCurrentItem();

                        if (p == page) {
                            MyLog.Set("d", mActivity.getClass(), "同一頁 不執行任何動作");
                        } else {
                            if (pageMediaPlayer != null) {
                                if (pageMediaPlayer.isPlaying()) {
                                    pageMediaPlayer.stop();
                                }
                                try {
                                    pageMediaPlayer.release();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                pageMediaPlayer = null;


                            }
                        }


                        MyLog.Set("d", mActivity.getClass(), "viewPager.getCurrentItem() = > " + p);

                        break;
                }

            }
        });
    }

    private void setMode(int position) {

        if (viewPager != null) {

            if (viewPager.findViewById(position) != null) {

                View v = viewPager.findViewById(position);

                PinchImageView photoImg = (PinchImageView) v.findViewById(R.id.pic);

                photoImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!isPhotoMode) {
                            //一般模式 => 相片模式

                            linTool.setVisibility(View.GONE);
                            linDescription.setVisibility(View.GONE);
                            rActionBar.setVisibility(View.GONE);
                            rGallery.setVisibility(View.GONE);
                            optionImg.setVisibility(View.GONE);
                            tvPage.setVisibility(View.GONE);
                            isPhotoMode = true;
                        } else {
                            //相片模式 => 一般模式
                            linTool.setVisibility(View.VISIBLE);


                            /*2016.10.24新增*/
                            if (tvDescription.getText().toString().equals("")) {
                                linDescription.setVisibility(View.INVISIBLE);
                            } else {
                                linDescription.setVisibility(View.VISIBLE);
                            }


                            rActionBar.setVisibility(View.VISIBLE);
                            rGallery.setVisibility(View.VISIBLE);
                            optionImg.setVisibility(View.VISIBLE);
                            tvPage.setVisibility(View.VISIBLE);
                            isPhotoMode = false;
                        }


                    }
                });
            }

        }

    }

    private void checkAutoPlay() {

        for (int i = 0; i < photoDurationList.size(); i++) {

            int duration = photoDurationList.get(i);
            if (duration > 0) {
                isAutoPlay = true;
                break;
            }
        }

        if (isAutoPlay) {
            autoplayImg.setVisibility(View.VISIBLE);
            autoplayImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isAutoPlaying) {
                        autoplayImg.setImageResource(R.drawable.icon_photo_auto_play_white_120x120);
                        isAutoPlaying = false;
                        autoScrollHandler.removeCallbacks(runnable);
                        MyLog.Set("d", mActivity.getClass(), "autoScrollHandler.removeCallbacks(runnable)");
                    } else {
                        autoplayImg.setImageResource(R.drawable.icon_photo_auto_stop_white_120x120);
                        isAutoPlaying = true;
                        int currentDuration = photoDurationList.get(page);
                        setAutoPlay(currentDuration, page);
                    }
                }
            });
        } else {
            autoplayImg.setVisibility(View.GONE);
        }


    }

    private void setAutoPlay(int duration, int position) {
                /*判斷秒數大於0*/
        if (duration != 0) {

                    /*判斷不是最後頁*/
            if (position != picList.size() - 1) {

                        /*判斷auto play 開關*/
                if (isAutoPlaying) {
                    String strUsefor = photoUseForList.get(position);

                        /*此頁為圖片或影片才執行自動撥放*/
                    if (strUsefor.equals("image") || strUsefor.equals("video")) {
                        autoScrollHandler.postDelayed(runnable, duration * 1000);
                    }
                }


            }

        }
    }


    private void setFixedData() {
        LinkText.set(mActivity, data_tvAlbumDescription, LinkText.defaultColorWhite, LinkText.defaultColorOfHighlightedLink, strAlbumDescription);

//        data_tvAlbumDescription.setText(strAlbumDescription);
        data_tvAuthor.setText(strAlbumAuthor);
        data_tvCreateDate.setText(strAlbumInserttime);

    }

    private void setPageDescription(int p) {

        if (photoDescriptionList != null && photoDescriptionList.size() != 0) {
            String strDescription = photoDescriptionList.get(p);

            if (strDescription.equals("") || strDescription.equals("null")) {

                if (LOG.isLogMode) {
                    Log.d(TAG, "no page description");
                }
                tvDescription.setText("");


//                （1）0    --------   VISIBLE    可见
//                （1）4    --------   INVISIBLE    不可见但是占用布局空间
//                （1）8    --------   GONE    不可见也不占用布局空间

                linDescription.setVisibility(View.GONE);


            } else {

                if (LOG.isLogMode) {
                    Log.d(TAG, "strDescription => " + strDescription);
                }
                tvDescription.setText(strDescription);

                if (!isPhotoMode) {
                    linDescription.setVisibility(View.VISIBLE);
                }

            }
        }


    }

    private void setHyperlink(final int p) {

        String thisPath = sdPath + myDir + dirAlbumList + album_id + "/";

        if (photoHyperlinkList.size() != 0) {
            if (!photoHyperlinkList.get(p).equals("null")) {


                String strHyperLink = photoHyperlinkList.get(p);

                try {
                    JSONArray jsonArray = new JSONArray(strHyperLink);

                    JSONObject objA = (JSONObject) jsonArray.get(0);
                    String iconA = objA.getString("icon");
                    String textA = objA.getString("text");
                    final String urlA = objA.getString("url");

                    if (urlA != null && !urlA.equals("") && !urlA.equals("null")) {
                        dataLinkA.setImageResource(R.drawable.button_album_data_link_click);
                        dataLinkA.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", urlA);
                                Intent intent = new Intent(ReadAlbumActivity.this, WebView2Activity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                ActivityAnim.StartAnim(mActivity);
                            }
                        });
                    } else {
                        dataLinkA.setImageResource(R.drawable.button_album_data_link);
                        dataLinkA.setClickable(false);
                    }


                    JSONObject objB = (JSONObject) jsonArray.get(1);

                    String iconB = objB.getString("icon");
                    String textB = objB.getString("text");
                    final String urlB = objB.getString("url");

                    if (urlB != null && !urlB.equals("") && !urlB.equals("null")) {
                        dataLinkB.setImageResource(R.drawable.button_album_data_link_click);
                        dataLinkB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", urlB);
                                Intent intent = new Intent(ReadAlbumActivity.this, WebView2Activity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                ActivityAnim.StartAnim(mActivity);
                            }
                        });
                    } else {
                        dataLinkB.setImageResource(R.drawable.button_album_data_link);
                        dataLinkB.setClickable(false);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                if (photoHyperlinkMapList.get(0) != null) {
//                    String pathA = thisPath + p + "-hyperlink-0.png#asdf";
//                    dataLinkA.setImageResource(R.drawable.button_album_data_link_click);
//                    dataLinkA.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
//                                Log.d(TAG, "禁止連續點擊");
//                                return;
//                            }
//
//                            if (!HttpUtility.isConnect(mActivity)) {
//                                noConnect = new NoConnect(mActivity);
//                                return;
//                            }
//
//                            Uri uri = Uri.parse((String) photoHyperlinkMapList.get(0).get("url"));
//                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(it);
//                        }
//                    });
//
//
//                }
//
//                if (photoHyperlinkMapList.get(1) != null) {
//                    String pathB = thisPath + p + "-hyperlink-1.png#asdf";
//                    dataLinkB.setImageResource(R.drawable.button_album_data_link_click);
//                    dataLinkB.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
//                                Log.d(TAG, "禁止連續點擊");
//                                return;
//                            }
//
//                            if (!HttpUtility.isConnect(mActivity)) {
//                                noConnect = new NoConnect(mActivity);
//                                return;
//                            }
//
//                            Uri uri = Uri.parse((String) photoHyperlinkMapList.get(1).get("url"));
//                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(it);
//                        }
//                    });
//                }


            } else {
                dataLinkA.setImageResource(R.drawable.click_album_data_link);
                dataLinkB.setImageResource(R.drawable.click_album_data_link);
            }//if (!photoHyperlinkList.get(p).equals("null"))
        } else {
            dataLinkA.setImageResource(R.drawable.click_album_data_link);
            dataLinkB.setImageResource(R.drawable.click_album_data_link);
        }

    }

    /*設置每頁音樂*/
    private void setPageMusic(final int p) {
        if (photoAudioTargetList.size() != 0) {
            String audio_target = photoAudioTargetList.get(p);//尚未設定
            String audio_refer = photoAudioReferList.get(p);
            String audio_loop = photoAudioLoopList.get(p);
            switch (audio_refer) {
                case "file":
                    setFileSystemMusic(audio_target, audio_loop);
                    rVoice.setVisibility(View.VISIBLE);
                    break;
                case "embed":
                    rVoice.setVisibility(View.VISIBLE);
                    break;
                case "none":
                    rVoice.setVisibility(View.GONE);
                    break;
                case "system":
                    setFileSystemMusic(audio_target, audio_loop);
                    rVoice.setVisibility(View.VISIBLE);
                    break;
            }

        } else {

            rVoice.setVisibility(View.GONE);

//            voiceImg.setImageResource(R.drawable.button_voice);
            albumMusicPlay = false;
        }


    }

    private void setFileSystemMusic(String audio_target, String audio_loop) {


        String str = audio_target.substring(0, 4);

        String path = "";

        if (str.equals("http")) {
            path = audio_target;

            MyLog.Set("d", getClass(), "這頁音樂存在");

            if (pageMediaPlayer != null) {

                if (pageMediaPlayer.isPlaying()) {
                    pageMediaPlayer.stop();
                }
                pageMediaPlayer.release();
                pageMediaPlayer = null;
            }

            try {
                pageMediaPlayer = new MediaPlayer();
                pageMediaPlayer.reset();
                if (audio_loop.equals("1")) {
                    pageMediaPlayer.setLooping(true);//重複播放
                } else {
                    pageMediaPlayer.setLooping(false);//單次播放

                }
                pageMediaPlayer.setDataSource(path);
                pageMediaPlayer.prepare();
                if (albumMusicPlay) {
                    pageMediaPlayer.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (pageMediaPlayer.isPlaying()) {
//                voiceImg.setImageResource(R.drawable.button_voice_click);
                albumMusicPlay = true;
            } else {
//                voiceImg.setImageResource(R.drawable.button_voice);
                albumMusicPlay = false;
            }


        } else {
            path = sdPath + myDir + dirAlbumList + album_id + "/" + audio_target + "#asdf";
            File musicFile = new File(path);
            if (musicFile.exists()) {

                MyLog.Set("d", getClass(), "這頁音樂存在");

                if (pageMediaPlayer != null) {

                    if (pageMediaPlayer.isPlaying()) {
                        pageMediaPlayer.stop();
                    }
                    pageMediaPlayer.release();
                    pageMediaPlayer = null;
                }
                try {
                    pageMediaPlayer = new MediaPlayer();
                    pageMediaPlayer.reset();
                    if (audio_loop.equals("1")) {
                        pageMediaPlayer.setLooping(true);//重複播放
                    } else {
                        pageMediaPlayer.setLooping(false);//單次播放
                    }
                    FileInputStream fis = new FileInputStream(musicFile);
                    pageMediaPlayer.setDataSource(fis.getFD());
                    pageMediaPlayer.prepare();
                    if (albumMusicPlay) {
                        pageMediaPlayer.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (pageMediaPlayer.isPlaying()) {
//                    voiceImg.setImageResource(R.drawable.button_voice_click);
                    albumMusicPlay = true;
                } else {
//                    voiceImg.setImageResource(R.drawable.button_voice);
                    albumMusicPlay = false;
                }
            }


        }

    }

    private void setAlbumMusic(File bgmFile) {
        if (LOG.isLogMode) {
            Log.d(TAG, "bgm.mp3存在");
        }
        isAlbumAudioExist = true;
        try {
            FileInputStream fis = new FileInputStream(bgmFile);

            if (strAlbumAudio_loop.equals("true")) {
                mMediaPlayer.setLooping(true);
            } else {
                mMediaPlayer.setLooping(false);
            }

            mMediaPlayer.setDataSource(fis.getFD());
            mMediaPlayer.prepare();


            if (albumMusicPlay) {
                mMediaPlayer.start();
            }

            if (mMediaPlayer != null) {

                if (mMediaPlayer.isPlaying()) {
//                    voiceImg.setImageResource(R.drawable.button_voice_click);
                    albumMusicPlay = true;//下次進這相本要播放音樂
                } else {
//                    voiceImg.setImageResource(R.drawable.button_voice);
                    albumMusicPlay = false;//下次進這相本要播放音樂
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setShareClick() {

        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    noConnect = new NoConnect(mActivity);
                    return;
                }

                boolean bShareToFB = getdata.getBoolean(TaskKeyClass.share_to_fb, false);
                if (bShareToFB) {
                    systemShare();
                } else {
                    doCheckShare();
                }

            }
        });

    }

    private void musicClick() {
        voiceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicClickContents();
            }
        });
    }

    /*設置點擊音樂button*/
    private void musicClickContents() {
        if (isAlbumAudioExist) {//相本有音樂

            if (LOG.isLogMode) {
                Log.d(TAG, "整本作品的音樂");
            }


            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
//                voiceImg.setImageResource(R.drawable.button_voice);
                albumMusicPlay = false;////下次進這相本不播放音樂

                if (LOG.isLogMode) {
                    Log.d(TAG, "目前狀態 : 相本音樂暫停");
                }
            } else {
                mMediaPlayer.start();
//                voiceImg.setImageResource(R.drawable.button_voice_click);
                albumMusicPlay = true;//下次進這相本要播放音樂
                if (LOG.isLogMode) {
                    Log.d(TAG, "目前狀態 : 相本音樂播放");
                }
            }

        } else {

            if (LOG.isLogMode) {
                Log.d(TAG, "作品單頁的音樂");
            }

            if (pageMediaPlayer != null) {

                if (pageMediaPlayer.isPlaying()) {
                    pageMediaPlayer.pause();
//                    voiceImg.setImageResource(R.drawable.button_voice);
                    albumMusicPlay = false;////下次進這相本不播放音樂
                    MyLog.Set("d", getClass(), "目前狀態 : 單頁音樂暫停");
                } else {
                    pageMediaPlayer.start();
//                    voiceImg.setImageResource(R.drawable.button_voice_click);
                    albumMusicPlay = true;//下次進這相本要播放音樂
                    MyLog.Set("d", getClass(), "目前狀態 : 單頁音樂播放");
                }

            } else {
                MyLog.Set("d", getClass(), "pageMediaPlayer == null");
            }

        }

    }

    private void setMapLocation(final int position) {

        String strlocation = "";

        if (photoLocationList.get(position) != null && !photoLocationList.get(position).equals("") && !photoLocationList.get(position).equals("null")) {

            if (LOG.isLogMode) {
                Log.d(TAG, "show page location");
            }

            strlocation = photoLocationList.get(position);

        } else {

            if (LOG.isLogMode) {
                Log.d(TAG, "show album location");
            }


            if (strAlbumLocation != null && !strAlbumLocation.equals("") && !strAlbumLocation.equals("null")) {

                strlocation = strAlbumLocation;

            } else {
                strlocation = "";

            }

        }


        if (strlocation.equals("")) {

            Log.e(TAG, "尚未設定圖片");

            rMap.setVisibility(View.GONE);


        } else {

            rMap.setVisibility(View.VISIBLE);

            /*地圖有設定才顯示*/
            mapMarkTitle = strlocation;

            final String newLocation = strlocation.replaceAll(" ", "");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject obj = MapUtility.getLocationInfo(newLocation);
                    try {
                        if (obj.get("results") != null && !obj.get("results").equals("")) {


                            if (((JSONArray) obj.get("results")).length() != 0) {

                                locLat = ((JSONArray) obj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                locLng = ((JSONArray) obj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                System.out.println(locLat);
                                System.out.println(locLng);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                            animateLatLng();
                        }
                    });
                }


            }).start();

        }


    }

    /*設置點擊分享*/
    private void selectShareMode() {



    }

    private void systemShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        if (coverUrl != null) {
            Uri u = Uri.parse(coverUrl);
            intent.putExtra(Intent.EXTRA_STREAM, u);
        }
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, strAlbumTitle + " , " + UrlClass.shareAlbumUrl + album_id + "&autoplay=1");//分享內容
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(Intent.createChooser(intent, mActivity.getTitle()));
    }

    private void taskShare() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            if (coverUrl != null && !coverUrl.equals("")) {

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(UrlClass.shareAlbumUrl + album_id + "&autoplay=1"))
                        .setImageUrl(Uri.parse(coverUrl))
                        .build();
                shareDialog.show(content);

            } else {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(UrlClass.shareAlbumUrl + album_id + "&autoplay=1"))
                        .build();
                shareDialog.show(content);
            }


        } else {
            Log.e(TAG, "-----------------***********************-------------------");
        }

    }

    /*
     * 2016.10.11新增更換作品當頁底圖
     */
    private void changePhoto(int position, Bitmap bitmap) {

        PinchImageView picImg = (PinchImageView) (viewPager.findViewById(position)).findViewById(R.id.pic);
        picImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        picImg.setImageBitmap(bitmap);


        File file = new File(picList.get(position));
        if (file.exists()) {
            file.delete();
            MyLog.Set("d", mActivity.getClass(), "已刪除 => " + picList.get(position));
        }

        BitmapUtility.saveToLocal(bitmap, picList.get(position), 100);

        pageAdapter.notifyDataSetChanged();
        gAdapter.notifyDataSetChanged();


    }

    public void setNoConnect(NoConnect noConnect) {
        this.noConnect = noConnect;
    }

    private void changeFinish(ImageView mExchangeImg, TextView tvExchange, int position, String result) {
        mExchangeImg.setImageResource(R.drawable.icon_read_album_exchanged);
        tvExchange.setTextColor(Color.parseColor("#393939"));

        if (result.equals("1")) {
            tvExchange.setText(R.string.exchanged);
        } else if (result.equals("2")) {
            tvExchange.setText(R.string.exchanged);
        } else if (result.equals("3")) {
            tvExchange.setText(R.string.exchanged_end);
        }

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("changeType", true);
            jsonObject.put("photousefor_user_id", photousefor_user_id);
            jsonObject.put("date", date);
            jsonObject.put("result", result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String str = jsonObject.toString();


        albumGiftChangeData.edit().putString(photoIdList.get(position), str).commit();
    }

    private void ChangeGiftCheck(final Bitmap bitmap, final ImageView mExchangeImg, final TextView tvExchange, final int position) {

//        dialogGift = new Dialog(mActivity, R.style.Dialog_Fullscreen);
        dialogGift = new Dialog(mActivity, R.style.myDialog);
        Window crosswin = dialogGift.getWindow();
        crosswin.setContentView(R.layout.dialog_exchange_check);
        dialogGift.show();

        final TextView tvY = (TextView) dialogGift.findViewById(R.id.y);
        final TextView tvN = (TextView) dialogGift.findViewById(R.id.n);
        final TextView tvName = (TextView) dialogGift.findViewById(R.id.name);
        final TextView tvDescription = (TextView) dialogGift.findViewById(R.id.tvDescription);
        final TextView tvCheck = (TextView) dialogGift.findViewById(R.id.check);
        ImageView img = (ImageView) dialogGift.findViewById(R.id.img);

        tvName.setText(photousefor_name);

        /*2016.08.05新增description*/
        tvDescription.setText(strPhotoUseForDescription);

        LoadPictureTask task = new LoadPictureTask(img, photousefor_image);
        task.execute();

        tvN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogGift.dismiss();

            }
        });

        tvY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    Log.d(TAG, "禁止連續點擊");
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    noConnect = new NoConnect(mActivity);
                    return;
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        protocol43(photousefor_user_id);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (p43Result.equals("1")) {

                                    mExchangeImg.setClickable(false);//兌換成功取消當下點擊效果
                                    changeFinish(mExchangeImg, tvExchange, position, "1");

                                    tvName.setTextColor(Color.parseColor("#e91e63"));
                                    tvName.setText(60 + "");

                                    /*2016.08.05修改說明由後台設定*/
//                                    tvDescription.setText("(請於時效內依出示給服務人員)");
                                    countDownTimer = new CountDownTimer(60000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            tvName.setText((millisUntilFinished / 1000) + "");
                                        }

                                        @Override
                                        public void onFinish() {
                                            dialogGift.dismiss();
                                            countDownTimer.cancel();
                                        }
                                    };

                                    countDownTimer.start();

                                    tvY.setVisibility(View.INVISIBLE);
                                    tvN.setVisibility(View.INVISIBLE);
                                    tvCheck.setVisibility(View.VISIBLE);
                                    tvCheck.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogGift.dismiss();
                                            countDownTimer.cancel();
                                        }
                                    });


                                    /*2016.10.11新增*/
                                    changePhoto(position, bitmap);

                                    MyLog.Set("d", mActivity.getClass(), "picList.get(position) => " + picList.get(position));
                                    MyLog.Set("d", mActivity.getClass(), "以執行兌換");
                                    MyLog.Set("d", mActivity.getClass(), " pageAdapter.notifyDataSetChanged();");


                                } else if (p43Result.equals("0")) {

                                } else if (p43Result.equals("2")) {

                                    mExchangeImg.setClickable(false);
                                    changeFinish(mExchangeImg, tvExchange, position, "2");
                                } else {
                                    DialogSet d = new DialogSet(mActivity);
                                    d.DialogUnKnow();
                                }
                            }
                        });

                    }
                }).start();
                dialogGift.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
//                        if (bitmap != null && !bitmap.isRecycled()) {
//                            bitmap.recycle();
//                        }
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                        /*2016.10.07新增*/
                        if (photousefor_image != null && !photousefor_image.equals("")) {

                            Picasso.with(mActivity.getApplicationContext()).invalidate(photousefor_image);

                            System.gc();

                        }


                    }
                });
            }
        });

    }

    private void protocol43(String photousefor_user_id) {
        Map<String, String> data = new HashMap<String, String>();
        data.put("id", id);
        data.put("token", token);
        data.put("photousefor_user_id", photousefor_user_id);

        String sign = IndexSheet.encodePPB(data);
        Map<String, String> sendData = new HashMap<String, String>();
        sendData.put("id", id);
        sendData.put("token", token);
        sendData.put("photousefor_user_id", photousefor_user_id);
        sendData.put("sign", sign);
        try {
            String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P43_UpDatePhotoUseFor_User, sendData, null);//64
            if (LOG.isLogMode) {
                Log.d(TAG, "p43strJson =>" + strJson);
            }
            JSONObject jsonObject = new JSONObject(strJson);
            p43Result = jsonObject.getString("result");
            if (p43Result.equals("0")) {
                p43Message = jsonObject.getString("message");
            } else if (p43Result.equals("2")) {
                p43Message = jsonObject.getString("message");
            }
        } catch (Exception e) {
            p43Result = "";
            e.printStackTrace();
        }


    }

    private void checkMode() {

        if (MODE == CONTRIBUTEMODE) {

            final DialogSet d = new DialogSet(mActivity);
//            d.setAlbumContents_Integer(R.string.directions_works_contribute_by_read_album);
            d.getTextView_Y().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.getDialog().dismiss();

                    doSendContribute();

                }
            });

            d.getTextView_N().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MODE = 0;

                    closeReadTask = new CloseReadTask();
                    closeReadTask.execute();

                }
            });

        } else {
            closeReadTask = new CloseReadTask();
            closeReadTask.execute();
        }
    }

    /*設置拉霸頁面*/
    @SuppressWarnings("ResourceType")
    private void slotStatusInit(final int position, final ImageView centerImg, final ImageView exchangeImg, final TextView tvExchange, final TextView tvTimer) {
        centerImg.setImageResource(R.drawable.gift_00000);

        centerImg.setOnTouchListener(new View.OnTouchListener() {

            private boolean isTouch = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        MyLog.Set("d", mActivity.getClass(), "MotionEvent.ACTION_DOWN");

                        isTouch = true;

                        break;

                    case MotionEvent.ACTION_UP:


                        MyLog.Set("d", mActivity.getClass(), "MotionEvent.ACTION_UP");

                        if (isTouch) {
                            if (!HttpUtility.isConnect(mActivity)) {
                                noConnect = new NoConnect(mActivity);
                                break;
                            }

                            try {

                                centerImg.setImageResource(R.drawable.read_album_open_gift);
                                animationDrawable = (AnimationDrawable) centerImg.getDrawable();
                                animationDrawable.start();

                                if (animationDrawable.isRunning()) {
                                    viewPager.noScroll(true);
                                }
                                Message message = new Message();
                                message.what = 1;
                                mHandler.sendMessageDelayed(message, 1000);

                                GetUserForByOpenGiftTask g = new GetUserForByOpenGiftTask(position, centerImg, exchangeImg, tvExchange);
                                g.execute();

                                isTouch = false;


                            } catch (OutOfMemoryError error) {
                                error.printStackTrace();
                            }
                        }

                        break;

                    case MotionEvent.ACTION_CANCEL:


                        break;

                }


                return true;
            }
        });
    }


    private int doingType;
    private static final int DoGetUserForByExchange = 0;
    private static final int DoGetUserForByOpenGift = 1;
    private static final int DoAfterGiftToChange = 2;

    private static final int DoSendContribute = 3;
    private static final int DoCheckShare = 4;
    private static final int DoShare = 5;


    private class GetUserForByExchangeTask extends AsyncTask<Void, Void, Object> {

        private int position;
        private ImageView mExchangeImg;
        private TextView tvExchange;


        public GetUserForByExchangeTask(int p, ImageView exchangeImg, TextView tvExchange) {
            this.position = p;
            this.mExchangeImg = exchangeImg;
            this.tvExchange = tvExchange;
        }

        @Override
        protected void onPreExecute() {
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("photo_id", photoIdList.get(position));
            data.put("identifier", device_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("photo_id", photoIdList.get(position));
            sendData.put("identifier", device_id);
            sendData.put("sign", sign);
            try {
                String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P42_GetPhotoUseFor_User, sendData, null);

                MyLog.Set("d", mActivity.getClass(), "p42strJson =>" + strJson);

                JSONObject jsonObject = new JSONObject(strJson);
                p42Result = jsonObject.getString("result");
                if (p42Result.equals("1") || p42Result.equals("2")) {

                    String jdata = jsonObject.getString("data");
                    JSONObject obj = new JSONObject(jdata);
                    String photousefor_user = obj.getString("photousefor_user");

                    JSONObject object = new JSONObject(photousefor_user);
                    photousefor_user_id = object.getString("photousefor_user_id");

                    MyLog.Set("d", mActivity.getClass(), "photousefor_user_id =>" + photousefor_user_id);

                    String photousefor = obj.getString("photousefor");
                    JSONObject ob = new JSONObject(photousefor);
                    photousefor_name = ob.getString("name");
                    photousefor_image = ob.getString("image");

                    /*2016.08.05 新增description*/
                    strPhotoUseForDescription = ob.getString(Key.description);

                    try {
                        bmpExchangeImg = HttpUtility.getNetBitmap(photousefor_image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (p42Result.equals("0")) {
                    p42Message = jsonObject.getString("message");
                } else if (p42Result.equals("3")) {

                    p42Message = jsonObject.getString("message");
                }
            } catch (Exception e) {
                p42Result = "";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            loading.dismiss();
            if (p42Result.equals("1")) {
                ChangeGiftCheck(bmpExchangeImg, mExchangeImg, tvExchange, position);

            } else if (p42Result.equals("0")) {
                p42Result = "";
                p43Result = "";
                photousefor_user_id = "";
                DialogSet d = new DialogSet(mActivity);
                d.getTextView_N().setVisibility(View.INVISIBLE);
                d.getTextView_Y().setVisibility(View.INVISIBLE);
                d.setAlbumContents_StringType(p42Message);
            } else if (p42Result.equals("2")) {//已兌換完的狀態
                p42Result = "";
                p43Result = "";
                photousefor_user_id = "";

                mExchangeImg.setClickable(false);
                changeFinish(mExchangeImg, tvExchange, position, "2");

                PinchImageView picImg = (PinchImageView) (viewPager.findViewById(position)).findViewById(R.id.pic);
                picImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                picImg.setImageBitmap(bmpExchangeImg);
                pageAdapter.notifyDataSetChanged();


                /*2016.10.11新增*/
                changePhoto(position, bmpExchangeImg);



            } else if (p42Result.equals("3")) {

                p42Result = "";
                p43Result = "";
                photousefor_user_id = "";

                mExchangeImg.setClickable(false);
                changeFinish(mExchangeImg, tvExchange, position, "3");



            }
        }
    }

    private class GetUserForByOpenGiftTask extends AsyncTask<Void, Void, Object> {

        private int position;
        private ImageView mCenterImg;
        private ImageView mExchangeImg;
        private TextView tvExchange;


        public GetUserForByOpenGiftTask(int p, ImageView centerImg, ImageView exchangeImg, TextView tvexchange) {
            this.position = p;
            this.mCenterImg = centerImg;
            this.mExchangeImg = exchangeImg;
            this.tvExchange = tvexchange;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Object doInBackground(Void... params) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("photo_id", photoIdList.get(position));
            data.put("identifier", device_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("photo_id", photoIdList.get(position));
            sendData.put("identifier", device_id);
            sendData.put("sign", sign);
            try {
                String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P42_GetPhotoUseFor_User, sendData, null);
                if (LOG.isLogMode) {
                    Log.d(TAG, "p42strJson =>" + strJson);
                }
                JSONObject jsonObject = new JSONObject(strJson);
                p42Result = jsonObject.getString("result");
                if (p42Result.equals("1") || p42Result.equals("2")) {

                    String jdata = jsonObject.getString("data");
                    JSONObject obj = new JSONObject(jdata);
                    String photousefor_user = obj.getString("photousefor_user");

                    JSONObject object = new JSONObject(photousefor_user);
                    photousefor_user_id = object.getString("photousefor_user_id");
                    if (LOG.isLogMode) {
                        Log.d(TAG, "photousefor_user_id =>" + photousefor_user_id);
                    }

                    String photousefor = obj.getString("photousefor");
                    JSONObject ob = new JSONObject(photousefor);
                    photousefor_name = ob.getString("name");
                    photousefor_image = ob.getString("image");
                    /*2016.08.05 新增description*/
                    strPhotoUseForDescription = ob.getString(Key.description);

                    try {
                        bmpGiftImg = HttpUtility.getNetBitmap(photousefor_image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (p42Result.equals("0")) {
                    p42Message = jsonObject.getString("message");
                } else if (p42Result.equals("3")) {

                    p42Message = jsonObject.getString("message");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (p42Result.equals("1")) {

                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
                String date = sDateFormat.format(new java.util.Date());

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("changeType", false);
                    jsonObject.put("photousefor_user_id", photousefor_user_id);
                    jsonObject.put("name", photousefor_name);
                    jsonObject.put("image", photousefor_image);
                    jsonObject.put("date", date);
                    jsonObject.put("result", p42Result);

                    /*2016.08.05新增description*/
                    jsonObject.put(Key.description, strPhotoUseForDescription);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                String str = jsonObject.toString();


                /*保存獎項訊息*/
//                SharedPreferences albumGiftChangeData = getSharedPreferences("pinpinbox_" + album_id, Activity.MODE_PRIVATE);
                albumGiftChangeData.edit().putString(photoIdList.get(position), str).commit();


                mCenterImg.setVisibility(View.INVISIBLE);//2016.02.17 GONE => INVISIBLE
//                mExchangeImg.setImageResource(R.drawable.click_read_album_exchange);
                tvExchange.setTextColor(Color.parseColor("#ffffff"));
                tvExchange.setText(R.string.exchange);
                mExchangeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AfterGiftToChangeTask a = new AfterGiftToChangeTask(position, mExchangeImg, tvExchange, bmpGiftImg);
                        a.execute();
                    }
                });


                PinchImageView picImg = (PinchImageView) (viewPager.findViewById(position)).findViewById(R.id.pic);
                picImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                picImg.setImageBitmap(bmpGiftImg);
                pageAdapter.notifyDataSetChanged();


            } else if (p42Result.equals("2")) {

                /*已領過 且消耗掉*/
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
                String date = sDateFormat.format(new java.util.Date());

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("changeType", true);
                    jsonObject.put("photousefor_user_id", photousefor_user_id);
                    jsonObject.put("name", photousefor_name);
                    jsonObject.put("image", photousefor_image);
                    jsonObject.put("date", date);
                    jsonObject.put("result", p42Result);

                    /*2016.08.05新增description*/
                    jsonObject.put(Key.description, strPhotoUseForDescription);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                String str = jsonObject.toString();


                /*保存獎項訊息*/
                albumGiftChangeData.edit().putString(photoIdList.get(position), str).commit();
                mCenterImg.setVisibility(View.GONE);
                mExchangeImg.setImageResource(R.drawable.icon_read_album_exchanged);
                tvExchange.setTextColor(Color.parseColor("#393939"));
                tvExchange.setText(R.string.exchanged);


                /*2016.10.11新增*/
                changePhoto(position, bmpGiftImg);

//                /*獎項圖片取代當頁圖片*/
//                PinchImageView picImg = (PinchImageView) (viewPager.findViewById(position)).findViewById(R.id.pic);
//                picImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                picImg.setImageBitmap(bmpGiftImg);
//                pageAdapter.notifyDataSetChanged();
//
//                File file = new File(picList.get(position));
//                if (file.exists()) {
//                    file.delete();
//                    MyLog.Set("d", mActivity.getClass(), "已刪除 => " + picList.get(position));
//                }
//
//                BitmapUtility.saveToLocal(bmpGiftImg, picList.get(position), 100);

                MyLog.Set("d", mActivity.getClass(), "picList.get(position) => " + picList.get(position));
                MyLog.Set("d", mActivity.getClass(), "以執行兌換");
                MyLog.Set("d", mActivity.getClass(), " pageAdapter.notifyDataSetChanged();");


            } else if (p42Result.equals("3")) {

                mExchangeImg.setClickable(false);//抽獎已結束 取消點擊效果


                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
                String date = sDateFormat.format(new java.util.Date());

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("changeType", true);
                    jsonObject.put("photousefor_user_id", photousefor_user_id);
                    jsonObject.put("date", date);
                    jsonObject.put("result", p42Result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String str = jsonObject.toString();

                albumGiftChangeData.edit().putString(photoIdList.get(position), str).commit();

                mCenterImg.setVisibility(View.GONE);
                mExchangeImg.setImageResource(R.drawable.icon_read_album_exchanged);
                tvExchange.setTextColor(Color.parseColor("#393939"));
                tvExchange.setText(R.string.exchanged_end);
            } else if (p42Result.equals("0")) {

            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }
        }
    }

    private class AfterGiftToChangeTask extends AsyncTask<Void, Void, Object> {

        private int position;
        private ImageView mExchangeImg;
        private TextView tvExchange;
        private Bitmap bmpGiftImg;

        public AfterGiftToChangeTask(int p, ImageView exchangeImg, TextView tvexchange, Bitmap bmpGiftImg) {
            this.position = p;
            this.mExchangeImg = exchangeImg;
            this.tvExchange = tvexchange;
            this.bmpGiftImg = bmpGiftImg;

        }

        @Override
        protected void onPreExecute() {
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String str = albumGiftChangeData.getString(photoIdList.get(position), "");
            try {
                JSONObject jsonObject = new JSONObject(str);
                if (jsonObject.getString("photousefor_user_id") != null) {
                    photousefor_user_id = jsonObject.getString("photousefor_user_id");
                    photousefor_name = jsonObject.getString("name");
                    photousefor_image = jsonObject.getString("image");

                    /*2016.08.05 新增description*/
                    strPhotoUseForDescription = jsonObject.getString(Key.description);


                    /*2016.08.16 新增如點擊過拉霸 呈現以兌換狀態時 重新獲取圖片bmp*/
                    if (bmpGiftImg == null || bmpGiftImg.isRecycled()) {

                        try {
                            bmpGiftImg = HttpUtility.getNetBitmap(photousefor_image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            loading.dismiss();

            ChangeGiftCheck(bmpGiftImg, mExchangeImg, tvExchange, position);

        }
    }


    private SendContributeTask sendContributeTask;
    private CheckShareTask checkShareTask;
    private ShareTask shareTask;

    private void connectInstability() {
        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoSendContribute:
                        doSendContribute();
                        break;

                    case DoCheckShare:
                        doCheckShare();
                        break;

                    case DoShare:
                        doShare();
                        break;
                }
            }
        };

        DialogSet d = new DialogSet(mActivity);
        d.ConnectInstability();
        d.setConnectInstability(connectInstability);
    }

    private void doSendContribute() {
        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }
        sendContributeTask = new SendContributeTask();
        sendContributeTask.execute();

    }

    private void doCheckShare() {
        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }
        checkShareTask = new CheckShareTask();
        checkShareTask.execute();
    }

    private void doShare() {
        if (!HttpUtility.isConnect(this)) {
            noConnect = new NoConnect(this);
            return;
        }
        shareTask = new ShareTask();
        shareTask.execute();
    }

    private class SendContributeTask extends AsyncTask<Void, Void, Object> {

        boolean contributionstatus;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoSendContribute;
            loading.show();
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
                p73Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p73Result = jsonObject.getString(Key.result);
                    if (p73Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);

                        JSONObject object = new JSONObject(jdata);
                        String event = object.getString("event");

                        JSONObject obj = new JSONObject(event);
                        contributionstatus = obj.getBoolean("contributionstatus");

                    } else if (p73Result.equals("0")) {
                        p73Message = jsonObject.getString(Key.message);
                    } else {
                        p73Result = "";
                    }

                } catch (Exception e) {
                    p73Result = "";
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();

            if (p73Result.equals("1")) {

                if (contributionstatus) {

//                    PinPinToast.ShowToast(mActivity, R.string.contribute_success);

                    MODE = 0;

                } else {

//                    PinPinToast.ShowToast(mActivity, R.string.contribute_cancel);

                }

            } else if (p73Result.equals("0")) {

            } else if (p73Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }

        }
    }

    private class CheckShareTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoCheckShare;
            loading.show();
        }


        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P84_CheckTaskCompleted, SetMapByProtocol.setParam84_checktaskcompleted(id, token, TaskKeyClass.share_to_fb, "google"), null);
                MyLog.Set("d", getClass(), "p84strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p84Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);
                    p84Result = jsonObject.getString(Key.result);

                    if (p84Result.equals("1")) {
                    } else if (p84Result.equals("2")) {
                        p84Message = jsonObject.getString(Key.message);
                    } else if (p84Result.equals("0")) {
                        p84Message = jsonObject.getString(Key.message);
                    } else {
                        p84Result = "";
                    }
                } catch (Exception e) {
                    p84Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();
            if (p84Result.equals("1")) {
                /*任務已完成*/
                getdata.edit().putBoolean(TaskKeyClass.share_to_fb, true).commit();
                systemShare();


            } else if (p84Result.equals("2")) {

                /*尚有次數未完成*/
                getdata.edit().putBoolean(TaskKeyClass.share_to_fb, false).commit();
                selectShareMode();

            } else if (p84Result.equals("0")) {



            } else if (p84Result.equals(Key.timeout)) {

                connectInstability();

            } else {

            }


        }
    }

    private class ShareTask extends AsyncTask<Void, Void, Object> {

        private String restriction;
        private String restriction_value;
        private String name;
        private String reward;
        private String reward_value;
        private String url;

        private int numberofcompleted;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoShare;
            loading.show();
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
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                MyLog.Set("d", getClass(), "p83strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p83Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = jsonObject.getString(Key.result);

                    if (p83Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);

                        JSONObject object = new JSONObject(jdata);

                        String task = object.getString(Key.task);
                        String event = object.getString(Key.event);

                        JSONObject taskObj = new JSONObject(task);
                        name = taskObj.getString(Key.name);
                        reward = taskObj.getString(Key.reward);
                        reward_value = taskObj.getString(Key.reward_value);
                        restriction = taskObj.getString(Key.restriction);
                        restriction_value = taskObj.getString(Key.restriction_value);

                        numberofcompleted = taskObj.getInt(Key.numberofcompleted);

                        JSONObject eventObj = new JSONObject(event);
                        url = eventObj.getString(Key.url);

                    } else if (p83Result.equals("2")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("3")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("0")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else {
                        p83Result = "";
                    }
                } catch (Exception e) {
                    p83Result = "";
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();
            if (p83Result.equals("1")) {

                final DialogHandselPoint d = new DialogHandselPoint(mActivity);

                if (restriction.equals("personal")) {
                    d.getTvTitle().setText(name + " " + numberofcompleted + " / " + restriction_value);
                } else {
                    d.getTvTitle().setText(name);
                }

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + " " + reward_value + " P !");
                    /*獲取當前使用者P點*/
                    String point = getdata.getString("point", "");
                    int p = StringIntMethod.StringToInt(point);

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    getdata.edit().putString("point", newP).commit();

                    getdata.edit().putBoolean(TaskKeyClass.share_to_fb, false).commit();

                    getdata.edit().putBoolean("datachange", true).commit();

                } else {
//                    d.getTvDescription().setText();
                }


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

            } else if (p83Result.equals("2")) {

                getdata.edit().putBoolean(TaskKeyClass.share_to_fb, true).commit();


            } else if (p83Result.equals("3")) {

                if (LOG.isLogMode) {
                    Log.d(TAG, "result = 3 , repeat share");
                }

            } else if (p83Result.equals("0")) {

                if (LOG.isLogMode) {
                    Log.d(TAG, "p83Message" + p83Message);
                }



                getdata.edit().putBoolean(TaskKeyClass.share_to_fb, true).commit();

            } else if (p83Result.equals(Key.timeout)) {

                connectInstability();

            } else {

            }


        }
    }

    private class LoadContentTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                getTxt();
            } catch (Exception e) {
                Log.e("ReadAlbumActivity", "txt error");
                e.printStackTrace();
            }

            try {
                getPicture();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

//            PinPinToast.ShowToast(mActivity, R.string.download_finish);

//            View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast, null);
//            TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
//            tvToast.setText(R.string.download_finish);
//
//            Toast toastFinish = new Toast(mActivity.getApplicationContext());
//            toastFinish.setDuration(Toast.LENGTH_SHORT);
//            toastFinish.setView(view);
//            toastFinish.show();

            albumDownLoad = null;

            try {
                setTitleByAlbumName();
            } catch (Exception e) {
                Log.e("ReadAlbumActivity", "setTitleByAlbumName error");
                e.printStackTrace();
            }

            try {
                setPicture();
            } catch (Exception e) {
                Log.e("ReadAlbumActivity", "setPicture error");
                e.printStackTrace();
            }

            try {
                setGallery();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                setPageDetail(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                setPage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                setPageDescription(0); //設定每頁敘述
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                setMode(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                checkAutoPlay();
            } catch (Exception e) {
                e.printStackTrace();
            }

            setPageListener();

            setFixedData(); //設定固定參數

            setHyperlink(0); //設定連結
            setShareClick();


            if(strAlbumAudio_target!=null && !strAlbumAudio_target.equals("")) {
                String str = strAlbumAudio_target.substring(0, 4);

                if (str.equals("http")) {

                    rVoice.setVisibility(View.VISIBLE);
                    isAlbumAudioExist = true;
                    try {
                        if (strAlbumAudio_loop.equals("true")) {
                            mMediaPlayer.setLooping(true);
                        } else {
                            mMediaPlayer.setLooping(false);
                        }

                        mMediaPlayer.setDataSource(strAlbumAudio_target);
                        mMediaPlayer.prepare();
                        if (albumMusicPlay) {
                            mMediaPlayer.start();
                        }
                        if (mMediaPlayer != null) {

                            if (mMediaPlayer.isPlaying()) {
//                                voiceImg.setImageResource(R.drawable.button_voice_click);
                                albumMusicPlay = true;//下次進這相本要播放音樂
                            } else {
//                                voiceImg.setImageResource(R.drawable.button_voice);
                                albumMusicPlay = false;//下次進這相本要播放音樂
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    File bgmFile = new File(sdPath + myDir + dirAlbumList + album_id + "/bgm.mp3#asdf");

                    if (bgmFile.exists()) {
                        rVoice.setVisibility(View.VISIBLE);
                        setAlbumMusic(bgmFile);
                    } else {

                        MyLog.Set("d", getClass(), "相本音樂不存在 預設第一頁音樂");

                        if (albumMusicPlay) {
//                            voiceImg.setImageResource(R.drawable.button_voice_click);
                        } else {
                            MyLog.Set("d", getClass(), "上次保存為音樂關閉");
//                            voiceImg.setImageResource(R.drawable.button_voice);
                        }

                        if (!isAlbumAudioExist) {
                            setPageMusic(0);
                        }
                    }
                }

            }else {
                rVoice.setVisibility(View.GONE);
            }






            musicClick();


            try {

                /*獲取原排序array*/
                JSONArray jsonCookieArray = new JSONArray(getdata.getString("recentAlbumdata", "[]"));
                Log.e(TAG, "jsonArray => " + jsonCookieArray.toString());


                if (jsonCookieArray.length() > 19) {
                    for (int i = 19; i < jsonCookieArray.length(); i++) {
                        try {
                            jsonCookieArray.remove(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }


                /*判斷是否在原排序內*/
                for (int i = 0; i < jsonCookieArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonCookieArray.get(i);

                    String albumid = obj.getString("album_id");
                    if (albumid.equals(album_id)) {

                        try {
                            jsonCookieArray.remove(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                /*建立新排序*/
                JSONArray newCookieArray = new JSONArray();

                /*建立新cookie新增至新排序第一順位*/
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("album_id", album_id);
                jsonObject.put("name", strAlbumTitle);
                jsonObject.put("user", strAlbumAuthor);
                jsonObject.put("description", strAlbumDescription);
                jsonObject.put("path", picList.get(0));

                MyLog.Set("d", mActivity.getClass(), "jsonObject => " + jsonObject.toString());

                newCookieArray.put(jsonObject);

                /*原排序內新增至新排序*/
                for (int i = 0; i < jsonCookieArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonCookieArray.get(i);
                    newCookieArray.put(obj);
                }

//                jsonCookieArray.put(jsonObject);
                getdata.edit().putString("recentAlbumdata", newCookieArray.toString()).commit();


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private class CloseReadTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            File file = new File(sdPath + myDir + dirAlbumList);
            File[] filelist = file.listFiles();


            if (filelist.length != 0) {
                for (int i = 0; i < filelist.length; i++) {
                    File[] childFile = filelist[i].listFiles();
                    if (childFile.length == 0) {
                        filelist[i].delete();
                    }
                }
            }

//            if (albumMusicExist) {
            getMusicData.edit().putBoolean(album_id, albumMusicPlay).commit();
//            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            if (mMap != null) {
                mMap.clear();
                mMap = null;
            }

            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
            finish();
            ActivityAnim.FinishAnim(mActivity);
        }
    }

    private class LoadPictureTask extends AsyncTask<Void, Void, Object> {

        private int bmpw, bmph, wh;
        private ImageView picImg;
        private String imgUrl;

        public LoadPictureTask(ImageView picImg, String imgUrl) {
            this.picImg = picImg;
            this.imgUrl = imgUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Object doInBackground(Void... params) {
            BitmapFactory.Options opts = HttpUtility.getBitmapWidthHeightFromUrl(imgUrl);
            bmpw = opts.outWidth;
            bmph = opts.outHeight;
            wh = bmpw * bmph;

            MyLog.Set("d", getClass(), bmpw + "x" + bmph + "=" + wh);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);


            if (picImg != null) {
                if (imgUrl != null) {

                    if (wh > 1600000) {

                        Picasso.with(mActivity.getApplicationContext())
                                .load(imgUrl)
                                .config(Bitmap.Config.RGB_565)
                                .resize(bmpw / 3, bmph / 3)
                                .error(R.drawable.no_image)
                                .tag(mActivity.getApplicationContext())
                                .into(picImg);


                    } else if (wh < 1600000 && wh > 700000) {

                        Picasso.with(mActivity.getApplicationContext())
                                .load(imgUrl)
                                .config(Bitmap.Config.RGB_565)
                                .resize(bmpw / 2, bmph / 2)
                                .error(R.drawable.no_image)
                                .tag(mActivity.getApplicationContext())
                                .into(picImg);
                    } else {

                        Picasso.with(mActivity.getApplicationContext())
                                .load(imgUrl)
                                .config(Bitmap.Config.RGB_565)
                                .resize(bmpw, bmph)
                                .error(R.drawable.no_image)
                                .tag(mActivity.getApplicationContext())
                                .into(picImg);
                    }

                }//if(pathlist!=null)
            }


        }
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLocation != null) {
            mLocation.setLatitude(location.getLatitude());
            mLocation.setLongitude(location.getLongitude());
//            animateLatLng();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private String getBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        return mLocManager.getBestProvider(criteria, true);
    }

    private void updateCurrentLoction() {
        String bestProvider = getBestProvider();
        Location newLoction = null;
        if (bestProvider != null)
            newLoction = mLocManager.getLastKnownLocation(bestProvider);

        if (mLocation == null) {
            mLocation = new Location("");
        }

        if (newLoction != null) {
            mLocation.setLatitude(newLoction.getLatitude());
            mLocation.setLongitude(newLoction.getLongitude());
        }
    }

//    private void setLatLng() {
//        if (mLocation == null) {
//            return;
//        }
//
//        LatLng latlng = new LatLng(locLat, locLng);
//        if ((latlng.latitude == 0) && (latlng.longitude == 0)) {
//            //mMapView.moveCamera(CameraUpdateFactory.newLatLng(latlng));
//        } else {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
//        }
//    }

    private void animateLatLng() {
        if (mLocation == null) {
            return;
        }

        final LatLng latlng = new LatLng(locLat, locLng);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 5), 2000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {


                mMap.addMarker(new MarkerOptions()
                        .title(mapMarkTitle)
                        .position(latlng))
                        .showInfoWindow();


//                MarkerOptions mMarkOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_mark));
//                mMarkOption.position(latlng);
//                mMarkOption.title(mapMarkTitle);
//
//                mMap.addMarker(mMarkOption);


            }

            @Override
            public void onCancel() {

            }
        });

    }


    /*
     * 判斷是否包含字串
     */
    public static boolean containsString(String src, String dest) {
        boolean flag = false;
        if (src.contains(dest)) {
            flag = true;
        }
        return flag;
    }

    private void back() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMode();
            }
        });
    }

    private void cleanPicasso() {
        if (picList != null) {
            for (int i = 0; i < picList.size(); i++) {
                Picasso.with(mActivity).invalidate(files.get(i));
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {

        if (dialogGift != null && dialogGift.isShowing()) {
            return;
        }

        if (isOptionsOpen) {
            ViewPropertyAnimator down = rAlbumInfo.animate();
            down.setDuration(300);
            down.translationYBy(h);
            down.start();
            viewPager.noScroll(false);
            mMap.clear();
            isOptionsOpen = false;
            return;
        }

        checkMode();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (LOG.isLogMode) {
                Log.i(TAG, "-------------landscape"); // 横屏
            }

            linAlbumInfo.setOrientation(LinearLayout.HORIZONTAL);

        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (LOG.isLogMode) {
                Log.i(TAG, "-------------portrait"); // 豎屏
            }
            linAlbumInfo.setOrientation(LinearLayout.VERTICAL);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            bMediaPlayerPause = true;
            if (LOG.isLogMode) {
                Log.d(TAG, "作品音樂暫停");
            }
        }

        if (pageMediaPlayer != null && pageMediaPlayer.isPlaying()) {
            pageMediaPlayer.pause();
            bPageMediaPlayerPause = true;
            if (LOG.isLogMode) {
                Log.d(TAG, "單頁音樂暫停");
            }
        }

        if (isAutoPlay && isAutoPlaying) {
            autoplayImg.setImageResource(R.drawable.icon_photo_auto_play_white_120x120);
            isAutoPlaying = false;
            autoScrollHandler.removeCallbacks(runnable);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            updateCurrentLoction();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setLatLng();

        if (bMediaPlayerPause && mMediaPlayer != null) {
            mMediaPlayer.start();
            bMediaPlayerPause = false;
            if (LOG.isLogMode) {
                Log.d(TAG, "作品音樂播放");
            }
        }
        if (bPageMediaPlayerPause && pageMediaPlayer != null) {
            pageMediaPlayer.start();
            bPageMediaPlayerPause = false;
            if (LOG.isLogMode) {
                Log.d(TAG, "單頁音樂播放");
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {
            if (noConnect == null) {
                DialogSet d = new DialogSet(this);
                d.setNoConnect();
            }
        }
    }

    @Override
    public void onDestroy() {
        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        mHandler.removeCallbacksAndMessages(null);
        autoScrollHandler.removeCallbacksAndMessages(null);
        audioHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        autoScrollHandler = null;
        audioHandler = null;

        if (dialogGift != null && dialogGift.isShowing()) {
            dialogGift.dismiss();
            dialogGift = null;
        }


        if (closeReadTask != null && !closeReadTask.isCancelled()) {
            closeReadTask.cancel(true);
        }
        closeReadTask = null;

        if (sendContributeTask != null && !sendContributeTask.isCancelled()) {
            sendContributeTask.cancel(true);
        }
        sendContributeTask = null;

        if (checkShareTask != null && !checkShareTask.isCancelled()) {
            checkShareTask.cancel(true);
        }
        checkShareTask = null;

        if (shareTask != null && !shareTask.isCancelled()) {
            shareTask.cancel(true);
        }
        shareTask = null;

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }
        mMediaPlayer = null;

        if (pageMediaPlayer != null) {
            pageMediaPlayer.release();
        }
        pageMediaPlayer = null;

        if (bmpGiftImg != null && !bmpGiftImg.isRecycled()) {
            bmpGiftImg.recycle();
        }
        bmpGiftImg = null;

        if (bmpExchangeImg != null && !bmpExchangeImg.isRecycled()) {
            bmpExchangeImg.recycle();
        }
        bmpExchangeImg = null;

        if (picList != null) {
            for (int i = 0; i < picList.size(); i++) {
                viewList.clear();
                Picasso.with(mActivity).invalidate(files.get(i));
            }
        }

        mLocation = null;

        pageAdapter = null;

        gAdapter = null;

        callbackManager = null;

        shareDialog = null;

        mActivity = null;

        mMap = null;


        Recycle.IMG(backImg);
        Recycle.IMG(optionImg);
        Recycle.IMG(readOptions_close);
        Recycle.IMG(voiceImg);
        Recycle.IMG(shareImg);
        Recycle.IMG(dataLinkA);
        Recycle.IMG(dataLinkB);

        System.gc();

        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }


}

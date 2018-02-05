package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aviary.android.feather.sdk.AviaryIntent;
import com.aviary.android.feather.sdk.internal.Constants;
import com.aviary.android.feather.sdk.internal.headless.utils.MegaPixels;
import com.czt.mp3recorder.MP3Recorder;
import com.pinpinbox.android.Test.CreateAlbum.ChangeItemAdapter;
import com.pinpinbox.android.Test.CreateAlbum.SelectPreviewAdapter;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.DirClass;
import com.pinpinbox.android.StringClass.DoingTypeClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.StringClass.TaskKeyClass;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.EditDragGridView;
import com.pinpinbox.android.Views.PinchImageView;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerAlbumSettingsAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCreationAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumSettings;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ScrollLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.SnackManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogCreationDescription;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogCreationLocation;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCooperation2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMe2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMyUpLoad2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSelectPhoto2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSelectVideo2;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.OnSpotlightEndedListener;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.OnSpotlightStartedListener;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.SimpleTarget;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.Spotlight;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol33;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;
import com.pinpinbox.android.util.CheckExternalStorage;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/3/8.
 */
public class Creation2Activity extends DraggerActivity implements View.OnClickListener, LocationListener {


    private Activity mActivity;
    private SharedPreferences getdata;
    private ItemAlbumSettings.Builder builder;
    private Location mLocation = null;
    private LocationManager mLocManager;
    private Criteria criteria;

    private GetPhotoTask getPhotoTask;
    private ChangeTask changeTask;
    private DeleteTask deleteTask;
    private AfterAviaryTask afterAviaryTask;
    private ConfirmTask confirmTask;
    private DeleteBitmapToReFreshTask deleteBitmapToReFreshTask;
    private SaveToFinishTask saveToFinishTask;
    private UpLoadAudioTask upLoadAudioTask;
    private DeleteAudioTask deleteAudioTask;
    private FirstCreateFreeAlbumTask firstCreateFreeAlbumTask;
    private DescriptionTask descriptionTask;
    private LocationTask locationTask;
    private SendPreviewTask sendPreviewTask;
    private GetSettingsTask getSettingsTask;
    private SetSettingsTask setSettingsTask;
    private SendAudioTask sendAudioTask;
    private Protocol33 protocol33;

    private FragmentSelectPhoto2 fragmentSelectPhoto2;
    private FragmentSelectVideo2 fragmentSelectVideo2;

    private PopupCustom popCreationSet, popCreatePreview, popCreateSort, popCreateAdd, popCreateAudio;

    private DialogCreationLocation dlgCreationLocation;

    private ChangeItemAdapter pop_qeAdapter;
    private SelectPreviewAdapter selectPreviewAdapter;
    private RecyclerAlbumSettingsAdapter audioAdapter;

    private RecyclerCreationAdapter adapter;
    private ScrollLinearLayoutManager linearLayoutManager;


    private ArrayList<HashMap<String, Object>> selectingList;
    private ArrayList<HashMap<String, Object>> photoList;
    private List<ItemAlbumSettings> audioList;

    private JSONArray jsonArray;//已有相片張數
    private File uploadFile;//上傳的photo
    private Bitmap bmp;

    private MediaPlayer mPlayer = null, mpBackground;
    private MP3Recorder mp3Recorder;

    private String aviaryPath = DirClass.sdPath + PPBApplication.getInstance().getMyDir() + DirClass.pathName_FromAviary;
    private String mp3Path = DirClass.sdPath + PPBApplication.getInstance().getMyDir() + DirClass.pathName_RecordMp3;

    private String strPrefixText;
    private String picUrl;//顯示中的相片
    private String album_id;//相本id
    //    private String template_id;//套版id
    private String identity;//權限
    private String id, token;
    private String frame;//此相本所用之版型
    private String p33Result, p33Message;
    private String p55Result, p55Message;
    private String p57Result, p57Message; // get all photo detail
    private String p59Result, p59Message; // add photo
    private String p60Result, p60Message; // delete photo
    private String p62Result, p62Message; // sort all photo
    private String p78Result, p78Message; // add audio
    private String p79Result, p79Message; // delete audio
    private String p83Result, p83Message;


    /*
     * 有photo array => 57, 59, 60, 62, 78, 79
     */

    private String event_id;
    private String audioUrl, videoUrl, strDescription, strLocation;

    private static final int REQUEST_CODE_SDCARD = 105;
    private static final int REQUEST_CODE_RECORD = 106;
    private static final int UpLoad_OK = 111;
    private static final int ACTION_REQUEST_FEATHER = 100;
    private static final int CreationFast = 0;//快速
    private static final int CreationTemplate = 1;//套版
    private static final int CreationActivity = 2;//活動
    private static final int CreationEdit = 3;//編輯
    private static final int AddPicture = 66; //增加單張照片

    private int createMode = 0;//建立模式 預設快速

    private int lastPosition = 0;//選擇的位置(前一次)
    private int thisPosition = 0;//選擇的位置(當前)
    private int intUserGrade = 22;//預設最大張數
    private int intAudio = -1;


    private int doingType;
    private static final int DoConfirm = 0;
    private static final int DoSaveToFinish = 1;
    private static final int DoAfterAviary = 3;
    private static final int DoDeletePhoto = 4;
    private static final int DoChange = 5;
    private static final int DoUploadAudio = 88;
    private static final int DoDeleteAudio = 7;
    private static final int DoFirstCreateFreeAlbum = 8;
    private static final int DoDescription = 9;
    private static final int DoDeleteBitmapToRefresh = 10;
    private static final int DoSendPreview = 11;
    private static final int DoLocation = 14;


    private static final String NONE = "none";
    private static final String SINGULAR = "singular"; //單首
    private static final String PLURAL = "plural"; //多首


    private String mySelectAudioMode = NONE;
    private String currentAudioMode = NONE;


    private boolean intFirstCallP57 = true;
    private boolean photoChangeType = false;//預設判斷更換順序為否
    private boolean photoPreviewType = false;
    //    private boolean clickType = true; //預設按鈕可點擊
    private boolean bAddPage = false;
    private boolean bFirstInto = true;
    private boolean isModify = false;
    private boolean isContribute = false;
    private boolean isNewCreate = false;
    private boolean isSendFlurry = false;
    private boolean isShowAudioGuide = false;

    private RelativeLayout rPlay_Delete, rBackground, rLocationDelete, rPhotoSettingBar, rAudioRecording;


    private ImageView addPicImg, addUserImg, refreshImg, backImg, videoPlayImg, albumSetImg,
            selectAudioNoneImg, selectAudioPageImg, selectAudioBackgroundImg, photo_or_templateImg;
    private ImageView locationImg, audioRecordingImg, audioPlayImg, deleteImg, aviaryImg, locationDeleteImg, audioDeleteImg;

    private TextView tvSelectAudioNone, tvSelectAudioPage, tvSelectAudioBackground;


    private PinchImageView photoImg;
    private TextView tvPicCount, tvCheck, tvDescription, tvLocation, tvSelect_Photo_or_Template, tvAddDescription;
    private RecyclerView rvPhoto;

    private LinearLayout linDetail, linDetailDescription, linDetailLocation;

    private RippleBackground rippleBackgroundRecording, rippleBackgroundPlay;

    private GridView gvPreview;
    private EditDragGridView gvSort;
    private RecyclerView rvAudio;

//    public String getLngAndLatWithNetwork() {
//        double latitude = 0.0;
//        double longitude = 0.0;
//        LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if (location != null) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//        }
//        return longitude + "," + latitude;
//    }
//
//    private LocationListener locationListener = new LocationListener() {
//
//        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        // Provider被enable时触发此函数，比如GPS被打开
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        // Provider被disable时触发此函数，比如GPS被关闭
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//
//        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//        @Override
//        public void onLocationChanged(Location location) {
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_2_0_0_creation);
        setSwipeBackEnable(false);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        getCreationMode();
        init();
        setRecycler();
        doGetPhoto();

//        //需要開啟定位權限
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                getLngAndLatWithNetwork();
//            }
//        });


    }

    private void getCreationMode() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            album_id = bundle.getString(Key.album_id, "");
            identity = bundle.getString(Key.identity, "admin");
            createMode = bundle.getInt(Key.create_mode, 0);
            isNewCreate = bundle.getBoolean(Key.isNewCreate, false);

            MyLog.Set("d", getClass(), "bundle => album_id => " + album_id);
            MyLog.Set("d", getClass(), "bundle => identity => " + identity);
            MyLog.Set("d", getClass(), "bundle => createMode => " + createMode);


            event_id = bundle.getString(Key.event_id, "");
            isContribute = bundle.getBoolean(Key.isContribute, false);
            strPrefixText = bundle.getString(Key.prefix_text, "");

            MyLog.Set("d", getClass(), "bundle => event_id => " + event_id);
            MyLog.Set("d", getClass(), "bundle => isContibute => " + isContribute);
            MyLog.Set("d", getClass(), "bundle => strPrefixText => " + strPrefixText);

            //測試用
//            createMode = CreationTemplate;
//            album_id = "3047";


        }

    }

    private void init() {

        mActivity = this;
        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");


        /***********************************************************************************************************/


        File file = new File(aviaryPath);
        if (file.exists()) {
            file.delete();
        }

        File f1 = new File(DirClass.pathHeaderPicture);
        if (f1.exists()) {
            f1.delete();
        }

//        File f = new File(DirClass.pathRecordMp3);
//        if (!f.exists()) {
//            try {
//                f.createNewFile();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


        /***********************************************************************************************************/


        photoList = new ArrayList<>();
        audioList = new ArrayList<>();
        builder = new ItemAlbumSettings.Builder();


    }

    private void setRecycler() {

        rvPhoto = (RecyclerView) findViewById(R.id.rvPhoto);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPhoto.setLayoutManager(layoutManager);
        adapter = new RecyclerCreationAdapter(this, photoList, identity);
        rvPhoto.setAdapter(adapter);

        adapter.setOnRecyclerViewListener(new RecyclerCreationAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }

                /*20170908 錄音不可切換*/
                if (mp3Recorder != null && mp3Recorder.isRecording()) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_audio_recording);
                    return;
                }

                  /*20170908 播放不可切換*/
                if (mPlayer != null && mPlayer.isPlaying()) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_audio_playing);
                    return;
                }


                thisPosition = position;

                setChangeProject();

//                checkLocation(thisPosition);
//                checkDescription(thisPosition);
//                checkMp3_setAudioUrl(thisPosition);
//                setImageUrl(thisPosition);
//                selectBackground(thisPosition, lastPosition);
//                adapter.notifyDataSetChanged();
                lastPosition = thisPosition;


            }

            @Override
            public boolean onItemLongClick(int position, View v) {

                changeItemClick();

                return false;
            }
        });

    }

    private void initView() {

        tvPicCount = (TextView) findViewById(R.id.tvPicCount);
        tvCheck = (TextView) findViewById(R.id.tvCheck);
        tvAddDescription = (TextView) findViewById(R.id.tvAddDescription);

        deleteImg = (ImageView) findViewById(R.id.deleteImg);
        addPicImg = (ImageView) findViewById(R.id.addPicImg);
        aviaryImg = (ImageView) findViewById(R.id.aviaryImg);
        addUserImg = (ImageView) findViewById(R.id.addUserImg);
        photoImg = (PinchImageView) findViewById(R.id.photoImg);
        refreshImg = (ImageView) findViewById(R.id.refreshImg);
        backImg = (ImageView) findViewById(R.id.backImg);
        videoPlayImg = (ImageView) findViewById(R.id.videoPlayImg);

        rippleBackgroundRecording = (RippleBackground) findViewById(R.id.rippleRecording);
        rippleBackgroundPlay = (RippleBackground) findViewById(R.id.ripplePlay);

        audioRecordingImg = (ImageView) findViewById(R.id.audioRecording);
        audioPlayImg = (ImageView) findViewById(R.id.audioPlay);
        audioDeleteImg = (ImageView) findViewById(R.id.audioDeleteImg);

        rBackground = (RelativeLayout) findViewById(R.id.rBackground);
        rPlay_Delete = (RelativeLayout) findViewById(R.id.rPlay_Delete);

        //20171106
        rAudioRecording = (RelativeLayout) findViewById(R.id.rAudioRecording);



        /*2016.08.30新增*/
        linDetail = (LinearLayout) findViewById(R.id.linDetail);
        linDetailDescription = (LinearLayout) findViewById(R.id.linDetailDescription);
        linDetailLocation = (LinearLayout) findViewById(R.id.linDetailLocation);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvLocation = (TextView) findViewById(R.id.tvLocation);


        /*2016.11.28 new add*/
        albumSetImg = (ImageView) findViewById(R.id.albumSetImg);


        /*2016.11.29 new add*/
        setAllPop();

        /*2017.08.30 new add*/

        rPhotoSettingBar = (RelativeLayout) findViewById(R.id.rPhotoSettingBar);

        rLocationDelete = (RelativeLayout) findViewById(R.id.rLocationDelete);
        locationImg = (ImageView) findViewById(R.id.locationImg);
        locationDeleteImg = (ImageView) findViewById(R.id.locationDeleteImg);


        tvCheck.setOnClickListener(this);
        refreshImg.setOnClickListener(this);
        albumSetImg.setOnClickListener(this);

        TextUtility.setBold(tvCheck, true);


    }

    private void setAllPop() {

        /***/
        popCreatePreview = new PopupCustom(mActivity);
        popCreatePreview.setPopup(R.layout.pop_2_0_0_creation_preview, R.style.pinpinbox_popupAnimation_bottom);
        gvPreview = (GridView) popCreatePreview.getPopupView().findViewById(R.id.gvPreview);
        TextView tvPreviewConfirm = (TextView) popCreatePreview.getPopupView().findViewById(R.id.tvPreviewConfirm);
        tvPreviewConfirm.setOnClickListener(this);
        TextUtility.setBold((TextView) popCreatePreview.getPopupView().findViewById(R.id.tvTitle), true);
        TextUtility.setBold(tvPreviewConfirm, true);

        popCreatePreview.getPopupView().findViewById(R.id.linBackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popCreatePreview.dismiss();
            }
        });

        /***/
        popCreateSort = new PopupCustom(mActivity);
        popCreateSort.setPopup(R.layout.pop_2_0_0_creation_sort, R.style.pinpinbox_popupAnimation_bottom);
        gvSort = (EditDragGridView) popCreateSort.getPopupView().findViewById(R.id.gvSort);
        TextView tvSortConfirm = (TextView) popCreateSort.getPopupView().findViewById(R.id.tvSortConfirm);
        tvSortConfirm.setOnClickListener(this);
        TextUtility.setBold((TextView) popCreateSort.getPopupView().findViewById(R.id.tvTitle), true);
        TextUtility.setBold(tvSortConfirm, true);

        popCreateSort.getPopupView().findViewById(R.id.linBackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popCreateSort.dismiss();
            }
        });

        /***/
        popCreationSet = new PopupCustom(mActivity);
        popCreationSet.setPopup(R.layout.pop_2_0_0_creation_set, R.style.pinpinbox_popupAnimation_bottom);
        TextView tvSort = (TextView) popCreationSet.getPopupView().findViewById(R.id.tvSort);
        TextView tvSelectPreview = (TextView) popCreationSet.getPopupView().findViewById(R.id.tvSelectPreview);
        TextView tvSetAudio = (TextView) popCreationSet.getPopupView().findViewById(R.id.tvSetAudio);

        TextUtility.setBold((TextView) popCreationSet.getPopupView().findViewById(R.id.tvTitle), true);
        TextUtility.setBold(tvSort, true);
        TextUtility.setBold(tvSelectPreview, true);
        TextUtility.setBold(tvSetAudio, true);
        tvSort.setOnClickListener(this);
        tvSelectPreview.setOnClickListener(this);
        tvSetAudio.setOnClickListener(this);


        /***/
        popCreateAdd = new PopupCustom(mActivity);
        popCreateAdd.setPopup(R.layout.pop_2_0_0_creation_add, R.style.pinpinbox_popupAnimation_bottom);
        LinearLayout linAddPhoto = (LinearLayout) popCreateAdd.getPopupView().findViewById(R.id.linAddPhoto);
        LinearLayout linAddVideo = (LinearLayout) popCreateAdd.getPopupView().findViewById(R.id.linAddVideo);
        tvSelect_Photo_or_Template = (TextView) popCreateAdd.getPopupView().findViewById(R.id.tvSelect_Photo_or_Template);
        photo_or_templateImg = (ImageView) popCreateAdd.getPopupView().findViewById(R.id.photo_or_templateImg);

        TextUtility.setBold(tvSelect_Photo_or_Template, true);
        TextUtility.setBold((TextView) popCreateAdd.getPopupView().findViewById(R.id.tvVideo), true);
        TextUtility.setBold((TextView) popCreateAdd.getPopupView().findViewById(R.id.tvTitle), true);

        linAddPhoto.setOnClickListener(this);
        linAddVideo.setOnClickListener(this);


        /***/
        popCreateAudio = new PopupCustom(mActivity);
        popCreateAudio.setPopup(R.layout.pop_2_0_0_creation_audio, R.style.pinpinbox_popupAnimation_bottom);

        (popCreateAudio.getPopupView().findViewById(R.id.tvSave)).setOnClickListener(this);


        selectAudioNoneImg = (ImageView) popCreateAudio.getPopupView().findViewById(R.id.selectAudioNoneImg);
        selectAudioPageImg = (ImageView) popCreateAudio.getPopupView().findViewById(R.id.selectAudioPageImg);
        selectAudioBackgroundImg = (ImageView) popCreateAudio.getPopupView().findViewById(R.id.selectAudioBackgroundImg);

        tvSelectAudioNone = (TextView) popCreateAudio.getPopupView().findViewById(R.id.tvSelectAudioNone);
        tvSelectAudioPage = (TextView) popCreateAudio.getPopupView().findViewById(R.id.tvSelectAudioPage);
        tvSelectAudioBackground = (TextView) popCreateAudio.getPopupView().findViewById(R.id.tvSelectAudioBackground);


        TextUtility.setBold((TextView) popCreateAudio.getPopupView().findViewById(R.id.tvTitle), true);
        TextUtility.setBold(tvSelectAudioNone, true);
        TextUtility.setBold(tvSelectAudioPage, true);
        TextUtility.setBold(tvSelectAudioBackground, true);
        TextUtility.setBold((TextView) popCreateAudio.getPopupView().findViewById(R.id.tvSave), true);


//        LinearLayout linSelectAudioNouse = (LinearLayout) popCreateAudio.getPopupView().findViewById(R.id.linSelectAudioNouse);
//        LinearLayout linSelectAudioPage = (LinearLayout) popCreateAudio.getPopupView().findViewById(R.id.linSelectAudioPage);
//        LinearLayout linSelectAudioBackground = (LinearLayout) popCreateAudio.getPopupView().findViewById(R.id.linSelectAudioBackground);


        linearLayoutManager = new ScrollLinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvAudio = (RecyclerView) popCreateAudio.getPopupView().findViewById(R.id.rvAudio);
        rvAudio.setLayoutManager(linearLayoutManager);

        selectAudioNoneImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAudioMode(NONE);
            }
        });
        selectAudioPageImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAudioMode(PLURAL);
            }
        });
        selectAudioBackgroundImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAudioMode(SINGULAR);
            }
        });


        audioAdapter = new RecyclerAlbumSettingsAdapter(mActivity, audioList);
        rvAudio.setAdapter(audioAdapter);

        audioAdapter.setOnRecyclerViewListener(new RecyclerAlbumSettingsAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(final int position, View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                if (mySelectAudioMode != SINGULAR) {
                    return;
                }


                if (audioList.get(position).isSelect()) {
                    MyLog.Set("d", mActivity.getClass(), "已是選取狀態");

                    if (mpBackground != null && mpBackground.isPlaying()) {
                        mpBackground.stop();
                        return;
                    }

                    playAudio(position);

                } else {

                    MyLog.Set("d", mActivity.getClass(), "選取位置 => " + position);
                    for (int i = 0; i < audioList.size(); i++) {
                        if (audioList.get(i).isSelect()) {
                            MyLog.Set("d", mActivity.getClass(), "取消選取 => " + i);
                            audioList.get(i).setSelect(false);
                            audioAdapter.notifyItemChanged(i);
                            break;
                        }
                    }


                    MyLog.Set("d", mActivity.getClass(), "選取 => " + position);
                    audioList.get(position).setSelect(true);
                    audioAdapter.notifyItemChanged(position);

                    playAudio(position);
                }

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


        popCreateAudio.setDissmissWorks(new PopupCustom.DissmissWorks() {
            @Override
            public void excute() {
                if (mpBackground != null) {
                    mpBackground.release();
                }
                mpBackground = null;
            }
        });
//        popCreateAudio.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                popCreateAudio.resetBackground();
//
//            }
//        });


    }

    private void playAudio(final int position) {


        if (mpBackground == null) {
            mpBackground = new MediaPlayer();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mpBackground.reset();
                    mpBackground.setDataSource(audioList.get(position).getUrl());
                    mpBackground.prepare(); // prepare自动播放
                    mpBackground.start();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isShowAudioGuide) {
                                PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_select_same_item_can_play_stop);
                                isShowAudioGuide = true;
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void setAudioMode(String mode) {

        switch (mode) {

            case NONE://無音樂
                selectAudioNoneImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
                selectAudioPageImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioBackgroundImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

                tvSelectAudioNone.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                tvSelectAudioPage.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioBackground.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));


                linearLayoutManager.setScrollEnabled(false);

                rvAudio.setAlpha(0.2f);


                mySelectAudioMode = NONE;


                break;

            case PLURAL://多首
                selectAudioNoneImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioPageImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
                selectAudioBackgroundImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

                tvSelectAudioNone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioPage.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                tvSelectAudioBackground.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

                linearLayoutManager.setScrollEnabled(false);

                rvAudio.setAlpha(0.2f);


                mySelectAudioMode = PLURAL;
                break;

            case SINGULAR://單首
                selectAudioNoneImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioPageImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioBackgroundImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);

                tvSelectAudioNone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioPage.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioBackground.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));

                linearLayoutManager.setScrollEnabled(true);

                rvAudio.setAlpha(1f);

                mySelectAudioMode = SINGULAR;
                break;


        }

    }

    /*刪除當前照片*/
    private void deleteClick() {
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }


                if (jsonArray.length() != 0) {

                    //20171018
                    if (!checkMyPhoto(thisPosition)) {
                        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
                        return;
                    }


                    final DialogV2Custom d = new DialogV2Custom(mActivity);
                    d.setStyle(DialogStyleClass.CHECK);
                    d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_delete);
                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_delete_item);
                    d.setCheckExecute(new CheckExecute() {
                        @Override
                        public void DoCheck() {
                            doDeletePhoto();
                        }
                    });
                    d.show();

                } else {

                    MyLog.Set("d", mActivity.getClass(), "No Photo");

                }
            }
        });
    }

    /*下載當前照片 帶入adobe*/
    private void aviaryClick() {
        aviaryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }


                MyLog.Set("d", mActivity.getClass(), "被選擇的是 => " + lastPosition);

                if (jsonArray.length() != 0) {

                    //20171018
                    if (!checkMyPhoto(thisPosition)) {
                        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
                        return;
                    }


                    BeforeAviaryTask beforeAviaryTask = new BeforeAviaryTask();
                    beforeAviaryTask.execute();
                } else {

                    MyLog.Set("d", mActivity.getClass(), "jsonArray => " + jsonArray.length());

                }


            }
        });
    }

    /*刷新*/
    private void reFreshClick() {

        if (jsonArray.length() > 0) {

            if (getPhotoTask != null && !getPhotoTask.isCancelled()) {
                getPhotoTask.cancel(true);
                getPhotoTask = null;
            }

            isModify = true;

            doDeleteBitmapToRefresh();

        }

    }

    /*錄音*/
    private void recordingClick() {

        audioRecordingImg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                //20171018
                if (!checkMyPhoto(thisPosition)) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
                    return;
                }

                if (mPlayer != null && mPlayer.isPlaying()) {
                    View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast, null);
                    TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
                    tvToast.setText(R.string.pinpinbox_2_0_0_toast_message_audio_playing);
                    Toast toast = new Toast(mActivity.getApplicationContext());
                    toast.setDuration(500);
                    toast.setView(view);
                    toast.show();

                    return;
                }

                if (currentAudioMode.equals(SINGULAR)) {

                    final DialogV2Custom d = new DialogV2Custom(mActivity);
                    d.setStyle(DialogStyleClass.CHECK);
                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_change_album_mode_singular_to_plural);
                    d.setCheckExecute(new CheckExecute() {
                        @Override
                        public void DoCheck() {
                            d.dismiss();
                            setAudioMode(PLURAL);
                            doSendAudio();
                        }
                    });
                    d.show();

                    return;
                }

//                resetRipple(rippleBackgroundRecording);

                if (mp3Recorder == null) {

                    switch (checkPermission(mActivity, Manifest.permission.RECORD_AUDIO)) {
                        case SUCCESS:

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    record();
                                }
                            }, 200);


                            break;
                        case REFUSE:
                            MPermissions.requestPermissions(mActivity, REQUEST_CODE_RECORD, Manifest.permission.RECORD_AUDIO);
                            break;

                    }


                } else {

                    if (mp3Recorder.isRecording()) {
                        mp3Recorder.stop();
                        mp3Recorder = null;
                        rippleBackgroundRecording.stopRippleAnimation();
//                        resetRipple(rippleBackgroundRecording);

                        setClickable(true);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                doUploadAudio();

                            }
                        }, 300);//500

                    }


                }
            }
        });


    }

    private void record() {

            /*畫面禁止點擊    開始錄音*/
        setClickable(false);


        mp3Path = DirClass.sdPath + PPBApplication.getInstance().getMyDir() + DirClass.pathName_RecordMp3;
        MyLog.Set("e", getClass(), mp3Path);

        File fMp3 = new File(mp3Path);
        if (!fMp3.exists()) {
            try {

                FileUtility.createSDFile(mp3Path);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try {


            mp3Recorder = new MP3Recorder(fMp3);
            mp3Recorder.start();
//            setRipple(rippleBackgroundRecording, audioRecordingImg);
            audioRecordingImg.setImageResource(R.drawable.ic200_recording_white);
            rippleBackgroundRecording.startRippleAnimation();

            rPlay_Delete.setVisibility(View.GONE);

        } catch (IOException e) {
            if (mp3Recorder != null) {
                mp3Recorder.stop();
                mp3Recorder = null;
                rippleBackgroundRecording.stopRippleAnimation();
//                resetRipple(rippleBackgroundRecording);
            }
            setClickable(true);
            e.printStackTrace();
        }


    }

    /*播放錄音*/
    private void playAudioClick() {

        audioPlayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                if (mp3Recorder != null && mp3Recorder.isRecording()) {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_audio_recording);

                    return;

                }


                /*單獨添加*/
                refreshImg.setClickable(false);

                /*畫面禁止點擊*/
                setClickable(false);

                /*reset ripple*/
//                resetRipple(rippleBackgroundPlay);

                if (mPlayer != null && mPlayer.isPlaying()) {
                    cleanMp3();
                    rippleBackgroundPlay.stopRippleAnimation();
//                    resetRipple(rippleBackgroundPlay);
                    refreshImg.setClickable(true);
                    setClickable(true);
                    return;
                }

                if (mPlayer == null) {

//                    setRipple(rippleBackgroundPlay, audioPlayImg);
                    rippleBackgroundPlay.startRippleAnimation();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                mPlayer = new MediaPlayer();
                                mPlayer.setDataSource(audioUrl);

                                mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                    @Override
                                    public boolean onError(MediaPlayer mp, int what, int extra) {
                                        MyLog.Set("e", this.getClass(), "setOnErrorListener");
                                        cleanMp3();
                                        rippleBackgroundPlay.stopRippleAnimation();
//                                        resetRipple(rippleBackgroundPlay);
                                        refreshImg.setClickable(true);
                                        setClickable(true);
                                        return false;
                                    }
                                });

                                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        cleanMp3();
                                        rippleBackgroundPlay.stopRippleAnimation();
//                                        resetRipple(rippleBackgroundPlay);
                                        refreshImg.setClickable(true);
                                        setClickable(true);
                                    }
                                });


                                mPlayer.prepare();
                                mPlayer.start();


                            } catch (Exception e) {
                                refreshImg.setClickable(true);
                                setClickable(true);
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        });

    }

    /*刪除錄音*/
    private void deleteAudioClick() {
        audioDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                //20171018
                if (!checkMyPhoto(thisPosition)) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
                    return;
                }

                if (mp3Recorder != null && mp3Recorder.isRecording()) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_audio_recording);
                    return;

                }

                if (mPlayer != null && mPlayer.isPlaying()) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_audio_playing);
                    return;
                }

                DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setStyle(DialogStyleClass.CHECK);
                d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_delete);
                d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_delete_recording);
                d.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {
                        doDeleteAudio();
                    }
                });
                d.show();

            }
        });
    }

    /*播放影片*/
    private void playVideoClick() {

        videoPlayImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString("videopath", videoUrl);
                Intent intent = new Intent(mActivity, VideoPlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityAnim.StartAnim(mActivity);

            }
        });

    }

    /*單頁說明*/
    private void addDescription() {

//        addDescriptionImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final DialogCreationDescription d = new DialogCreationDescription(mActivity);
//                d.getEtDescription().setText(strDescription);
//                d.getConfirmImg().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        d.getDialog().dismiss();
//                        MyLog.Set("d", mActivity.getClass(), "準備傳遞說明");
//                        strDescription = d.getEtDescription().getText().toString();
//                        doDescription(strDescription);
//
//                    }
//                });
//
//
//            }
//        });

        tvAddDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                if (photoList == null || photoList.size() < 1) {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_add_item_first_to_add_description);

                    return;
                }

                //20171018
                if (!checkMyPhoto(thisPosition)) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
                    return;
                }


                final DialogCreationDescription d = new DialogCreationDescription(mActivity);
                d.getEtDescription().setText(strDescription);
                d.getConfirmImg().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        d.getDialog().dismiss();
                        strDescription = d.getEtDescription().getText().toString();
                        doDescription(strDescription);

                    }
                });


            }
        });


    }


    /*單頁地點*/
    private void addLocation() {

        locationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                //20171018
                if (!checkMyPhoto(thisPosition)) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
                    return;
                }


                if (dlgCreationLocation == null) {

                    MyLog.Set("d", mActivity.getClass(), "dlgCreationLocation==null");

                    dlgCreationLocation = new DialogCreationLocation((FragmentActivity) mActivity, mLocation);
                    dlgCreationLocation.getEdLocation().setText(strLocation);
                    dlgCreationLocation.setLocation(strLocation);
                    dlgCreationLocation.getConfirmImg().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dlgCreationLocation.getDialog().dismiss();
                            strLocation = dlgCreationLocation.getEdLocation().getText().toString();
                            doLocation(strLocation);
                        }
                    });
                } else {

                    MyLog.Set("d", mActivity.getClass(), "dlgCreationLocation!=null");

                    dlgCreationLocation.getEdLocation().setText(strLocation);
                    dlgCreationLocation.setLocation(strLocation);
                    dlgCreationLocation.getDialog().show();

                }
            }
        });

    }


    /*刪除地點*/
    private void deleteLocation() {

        locationDeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                //20171018
                if (!checkMyPhoto(thisPosition)) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
                    return;
                }

                DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setStyle(DialogStyleClass.CHECK);
                d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_delete);
                d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_delete_location);
                d.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {
                        strLocation = "";
                        doLocation(strLocation);
                    }
                });
                d.show();
            }
        });


    }


    /*送出作品*/
    public void sendAlbumCheck() {

        if (jsonArray != null && jsonArray.length() == 0) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_content_is_null);
            return;
        }

        if (identity != null && identity.equals("admin")) {

            boolean bCreateFreeAlbum = getdata.getBoolean(TaskKeyClass.create_free_album, false);
            if (bCreateFreeAlbum) {
                sendWork();
            } else {
                doFirstCreateFreeAlbum();
            }


        } else {

            DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_close_creation);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {

                    back();

                }
            });
            d.show();

        }

    }

    private void sendWork() {
        if (isModify) {
            MyLog.Set("d", getClass(), "isModify");
            doConfirm();
        } else {
            MyLog.Set("d", getClass(), "isModify = false");
            toAlbumSettings();
        }
    }

    /*添加新一頁*/
    private void addPicture() {
        addPicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }

                /*選取已達上限*/
                if (jsonArray.length() >= intUserGrade) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_count_max);
                    return;
                }


                SDpermissionEnable();


            }
        });
    }


    /*開啟SD權限*/
    private void SDpermissionEnable() {



                     /*20170915將權限判斷移至選項前*/
        switch (checkPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            case SUCCESS:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkCreateMode();
                    }
                }, 300);


                break;
            case REFUSE:

                MPermissions.requestPermissions(mActivity, REQUEST_CODE_SDCARD, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;

        }


    }

    /*判斷建立模式*/
    private void checkCreateMode() {

        switch (createMode) {

            case CreationFast: //快速

                tvSelect_Photo_or_Template.setText(R.string.pinpinbox_2_0_0_itemtype_photo);
                photo_or_templateImg.setImageResource(R.drawable.ic200_camera_dark);
                popCreateAdd.show(rBackground);

                break;

            case CreationTemplate: //套版

//                tvSelect_Photo_or_Template.setText(R.string.pinpinbox_2_0_0_itemtype_template);
//                photo_or_templateImg.setImageResource(R.drawable.icon_select_template);

                Intent intent = new Intent(mActivity, CreationTemplate2Activity.class);
                Bundle bundle = new Bundle();

                MyLog.Set("d", getClass(), "要傳送到TemplateAddPictureActivity的identity => " + identity);
                MyLog.Set("d", getClass(), "要傳送到TemplateAddPictureActivity的album_id => " + album_id);

                bundle.putString(Key.album_id, album_id);
                bundle.putString(Key.identity, identity);
                bundle.putString(Key.frame, frame);
                intent.putExtras(bundle);
                startActivityForResult(intent, AddPicture);
                ActivityAnim.StartAnim(mActivity);

                break;

        }


    }


    /*新增用戶*/
    private void addUser() {
        addUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }

                if (identity != null) {

                    if (identity.equals("editor")) {
                        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deficiency_identity);
                    } else {

                        FlurryUtil.onEvent(FlurryKey.creation_into_adduser_view);

                        Bundle bundle = new Bundle();
                        bundle.putString(Key.album_id, album_id);
                        bundle.putString(Key.identity, identity);
                        Intent intent = new Intent(mActivity, AlbumGroup2Activity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        ActivityAnim.StartAnim(mActivity);
                    }
                }
            }
        });
    }

    /*作品設定*/
    private void AlbumSetContent() {

        gvSort.setOnChangeListener(new EditDragGridView.OnChanageListener() {

            @Override
            public void onChange(int from, int to) {
                photoChangeType = true;
                HashMap<String, Object> temp = photoList.get(from);
                if (from < to) {
                    for (int i = from; i < to; i++) {
                        Collections.swap(photoList, i, i + 1);
                    }

                } else if (from > to) {
                    for (int i = from; i > to; i--) {
                        Collections.swap(photoList, i, i - 1);
                    }
                }
                photoList.set(to, temp);
                pop_qeAdapter.notifyDataSetChanged();
            }
        });

        popChangeListener();
        popConfirmPreviewListener();

    }

    private void changeItemClick() {

        MyLog.Set("d", getClass(), "changeItemClick photoList size => " + photoList.size());

        if (photoList != null && photoList.size() <= 1) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_item_bigger_than_1_to_sort);
            return;
        }

        if (pop_qeAdapter == null) {
            pop_qeAdapter = new ChangeItemAdapter(mActivity, photoList);
            gvSort.setAdapter(pop_qeAdapter);
        } else {
            pop_qeAdapter.notifyDataSetChanged();
        }
        popCreateSort.show(rBackground);
    }

    private void selectPreviewClick() {

        if (photoList != null && photoList.size() == 0) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_content_is_null);
            return;
        }

        /*將當下list帶入選擇畫面的list供選擇用*/
        selectingList = photoList;

        HashMap<String, Object> map = new HashMap<>();

        map.put("photo_id", selectingList.get(0).get("photo_id"));
        map.put("image_url", selectingList.get(0).get("image_url"));
        map.put("image_url_thumbnail", selectingList.get(0).get("image_url_thumbnail"));
        map.put("image_url_operate", selectingList.get(0).get("image_url_operate"));
        map.put("select", selectingList.get(0).get("select"));
        map.put("name", selectingList.get(0).get("name"));
        map.put("audio_url", selectingList.get(0).get("audio_url"));
        map.put("video_url", selectingList.get(0).get("video_url"));
        map.put(Key.description, selectingList.get(0).get(Key.description));
        map.put(Key.location, selectingList.get(0).get(Key.location));
        map.put(Key.is_preview, true);

        //20171018
        map.put(Key.user_id, selectingList.get(0).get(Key.user_id));

        photoList.set(0, map);

        if (selectPreviewAdapter != null) {
            selectPreviewAdapter.notifyDataSetChanged();
        } else {

            MyLog.Set("d", mActivity.getClass(), "selectPreviewAdapter => null");
            selectPreviewAdapter = new SelectPreviewAdapter(mActivity, selectingList);
            gvPreview.setAdapter(selectPreviewAdapter);

            gvPreview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0) {
                        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_cover_can_not_remove);
                        return;
                    }

                    photoPreviewType = true;


                    HashMap<String, Object> map = new HashMap<>();

                    map.put("photo_id", selectingList.get(position).get("photo_id"));
                    map.put("image_url", selectingList.get(position).get("image_url"));
                    map.put("image_url_thumbnail", selectingList.get(position).get("image_url_thumbnail"));
                    map.put("image_url_operate", selectingList.get(position).get("image_url_operate"));
                    map.put("select", selectingList.get(position).get("select"));
                    map.put("name", selectingList.get(position).get("name"));
                    map.put("audio_url", selectingList.get(position).get("audio_url"));
                    map.put("video_url", selectingList.get(position).get("video_url"));
                    map.put(Key.description, selectingList.get(position).get(Key.description));
                    map.put(Key.location, selectingList.get(position).get(Key.location));
                    boolean b = (boolean) selectingList.get(position).get("is_preview");

                    if (b) {
                        map.put(Key.is_preview, false);
                    } else {
                        map.put(Key.is_preview, true);
                    }

                    //20171018
                    map.put(Key.user_id, selectingList.get(position).get(Key.user_id));

                    photoList.set(position, map);
                    selectPreviewAdapter.notifyDataSetChanged();

                }
            });

        }

        popCreatePreview.show(rBackground);


    }

    private void backCheck() {

        DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.CHECK);
        d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_close_creation);
        d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_exit);
        d.setCheckExecute(new CheckExecute() {
            @Override
            public void DoCheck() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (identity != null && identity.equals("admin")) {


                            if (!isModify) {

                                MyLog.Set("d", mActivity.getClass(), "無修改 直接退出");

                                back();

                            } else {

                                MyLog.Set("d", mActivity.getClass(), "有修改 儲存再退出");
                                doSaveToFinish();

                            }


//                            if (jsonArray != null && jsonArray.length() > 0) {
//                                if (isModify) {
//                                    MyLog.Set("d", getClass(), "有修改 儲存再退出");
//                                    doSaveToFinish();
//                                } else {
//                                    MyLog.Set("d", getClass(), "無修改 直接退出");
//                                    back();
//                                }
//                            } else {
//                                back();
//                            }


                        } else {

                            MyLog.Set("d", mActivity.getClass(), "權限不足不儲存 直接退出");

                            //權限不足不儲存 直接退出
                            back();


                        }
                    }
                }, 200);
            }
        });
        d.show();

    }

    private void back() {

        if (isNewCreate) {
//            Intent intent = new Intent(mActivity, MyCollect2Activity.class);
//            startActivity(intent);
//            finish();
//            ActivityAnim.StartAnim(mActivity);


//            setResult(ResultCodeClass.creationback);


//            SnackManager.showCollectionSnack();
            SnackManager.showCustomSnack();

            finish();
            ActivityAnim.FinishAnim(mActivity);


        } else {

            boolean exitApp = true;
            final List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
            final int count = activityList.size();
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    String strAcName = activityList.get(i).getClass().getSimpleName();
                    if (strAcName.equals(Main2Activity.class.getSimpleName())) {
                        exitApp = false;
                        break;
                    }
                }
            }

            if (exitApp) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mActivity != null) {
                            Process.killProcess(Process.myPid());
                        }
                    }
                }, 200);

            } else {
                finish();
                ActivityAnim.FinishAnim(mActivity);
            }


        }


    }

    private boolean checkMyPhoto(int position) {


        if (identity.equals("admin")) {
            return true;
        } else {
            String user_id = (String) photoList.get(position).get(Key.user_id);
            if (user_id.equals(id)) {
                return true;
            } else {
                return false;
            }
        }


    }

    private int[] getViewXY(View view) {
        int[] location = new int[2];
//        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        view.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        //location [0]--->x坐标,location [1]--->y坐标
        return location;
    }

//    private void setRipple(RippleBackground ripple, View v) {
//
//        int location[] = getViewXY(v);
//        int x = location[0];
//        int y = location[1];
//
//
//        /*獲取icon點擊中心*/
//        int recording_center_x = x + (v.getWidth() / 2);
//        int recording_center_y = y + (v.getHeight() / 2);
//
//        MyLog.Set("d", getClass(), "recording_center_x_y => " + recording_center_x + " , " + recording_center_y);
//
//
//        /*獲取動畫中心*/
//
//        int ripLocation[] = getViewXY(ripple);
//
//        int rx = ripLocation[0];
//        int ry = ripLocation[1];
//        int rippleBackground_center_x = rx + (ripple.getWidth() / 2);
//        int rippleBackground_center_y = ry + (ripple.getHeight() / 2);
////        int rippleBackground_center_x = (int) ripple.getX() + (ripple.getWidth() / 2);
////        int rippleBackground_center_y = (int) ripple.getY() + (ripple.getHeight() / 2);
//
//
//        MyLog.Set("d", getClass(), "rippleBackground_center_x_y => " + rippleBackground_center_x + " , " + rippleBackground_center_y);
//
//
//        /*算出兩中心距離*/
//        int distanceX = recording_center_x - rippleBackground_center_x;
//        int distanceY = recording_center_y - rippleBackground_center_y;
//
//
//        /*設置動畫左上位置*/
//        ripple.setX(distanceX);
//        ripple.setY(distanceY);
//
//    }
//
//    private void resetRipple(RippleBackground rippleBackground) {
//        rippleBackground.setX(0);
//        rippleBackground.setY(0);
//    }

    private void popChangeListener() {

        popCreateSort.setDissmissWorks(new PopupCustom.DissmissWorks() {
            @Override
            public void excute() {
                if (photoChangeType) {

                    for (int i = 0; i < photoList.size(); i++) {
                        String a = (String) photoList.get(i).get("image_url_operate");
                        MyLog.Set("d", getClass(), "be change => " + a);
                    }

                    doChange();

                }
            }
        });

//        popCreateSort.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//
//                popCreateSort.resetBackground();
//
//
//            }
//        });
    }

    private void popConfirmPreviewListener() {

        popCreatePreview.setDissmissWorks(new PopupCustom.DissmissWorks() {
            @Override
            public void excute() {
                if (photoPreviewType) {
                    doSendPreview();
                }
            }
        });

//        popCreatePreview.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//
//
//                popCreatePreview.resetBackground();
//
//
//
//            }
//        });

    }

    private void addVideo() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);

        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (fragmentSelectVideo2 == null) {
            fragmentSelectVideo2 = new FragmentSelectVideo2();
            fragmentSelectVideo2.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragmentSelectVideo2, fragmentSelectVideo2.getClass().getSimpleName()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().show(fragmentSelectVideo2).commit();
            fragmentSelectVideo2.doGetAlbumContent();
        }

    }

    private void addPicByFast() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);
        bundle.putInt(Key.create_mode, createMode);

        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (fragmentSelectPhoto2 == null) {
            fragmentSelectPhoto2 = new FragmentSelectPhoto2();
            fragmentSelectPhoto2.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragmentSelectPhoto2, fragmentSelectPhoto2.getClass().getSimpleName()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().show(fragmentSelectPhoto2).commit();
            fragmentSelectPhoto2.doGetAlbumContent();
        }
    }

    public void reFreshBottomPhoto() {
        if (photoList != null && photoList.size() > 0) {
            try {
                photoList.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if (getPhotoTask != null && !getPhotoTask.isCancelled()) {
            getPhotoTask.cancel(true);
            getPhotoTask = null;
        }

        isModify = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doGetPhoto();
            }
        }, 300);


    }

//    private void addPicByTemplate() {
//
//        Intent intent = new Intent(mActivity, CreationTemplate2Activity.class);
//        Bundle bundle = new Bundle();
//
//        MyLog.Set("d", getClass(), "要傳送到TemplateAddPictureActivity的identity => " + identity);
//        MyLog.Set("d", getClass(), "要傳送到TemplateAddPictureActivity的album_id => " + album_id);
//
//        bundle.putString(Key.album_id, album_id);
//        bundle.putString(Key.identity, identity);
//        bundle.putString(Key.frame, frame);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, AddPicture);
//        ActivityAnim.StartAnim(mActivity);
//
//    }

    private void setClickable(boolean b) {
        tvCheck.setClickable(b);
        deleteImg.setClickable(b);
        addPicImg.setClickable(b);
        aviaryImg.setClickable(b);
        addUserImg.setClickable(b);
//        addDescriptionImg.setClickable(b);
        albumSetImg.setClickable(b);
        locationImg.setClickable(b);

    }

    private void setCount() {
        tvPicCount.setText(jsonArray.length() + "/" + intUserGrade);
    }

    private void checkDetail() {

        if (strDescription.equals("") && strLocation.equals("")) {
            linDetail.setVisibility(View.GONE);
        } else {
            linDetail.setVisibility(View.VISIBLE);
        }
    }

    private void checkLocation(int position) {

        strLocation = (String) photoList.get(position).get(Key.location);

        if (strLocation == null || strLocation.equals("null") || strLocation.equals("")) {

            rLocationDelete.setVisibility(View.GONE);
            linDetailLocation.setVisibility(View.GONE);
            strLocation = "";

        } else {

            /*有單頁地點*/
            rLocationDelete.setVisibility(View.VISIBLE);
            linDetailLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(strLocation);

        }

    }

    private void checkDescription(int position) {

        strDescription = (String) photoList.get(position).get(Key.description);

        if (strDescription == null || strDescription.equals("null") || strDescription.equals("")) {

            tvAddDescription.setText(R.string.pinpinbox_2_0_0_button_add_description);
            linDetailDescription.setVisibility(View.GONE);
            strDescription = "";

        } else {

            /*有單頁說明*/

            tvAddDescription.setText(R.string.pinpinbox_2_0_0_button_modify_description);
            linDetailDescription.setVisibility(View.VISIBLE);
            tvDescription.setText(strDescription);

        }

    }

    private void checkCount() {
        if (jsonArray.length() > 0) {
            rPhotoSettingBar.setVisibility(View.VISIBLE);

        } else {
            rPhotoSettingBar.setVisibility(View.INVISIBLE);

        }
    }

    private void checkMp3_setAudioUrl(int position) {


        /*先判斷是否為影片*/
        videoUrl = (String) photoList.get(position).get("video_url");
        if (videoUrl == null || videoUrl.equals("null") || videoUrl.equals("")) {

            /*不為影片*/

            aviaryImg.setVisibility(View.VISIBLE);

            videoPlayImg.setVisibility(View.GONE);

            audioUrl = (String) photoList.get(position).get("audio_url");
            if (audioUrl == null || audioUrl.equals("null") || audioUrl.equals("")) {
                audioRecordingImg.setImageResource(R.drawable.ic200_micro_white);

//                audioRecordingImg.setVisibility(View.VISIBLE);
                rAudioRecording.setVisibility(View.VISIBLE);


                rPlay_Delete.setVisibility(View.GONE);
            } else {
                //有錄音 可播放
//                audioRecordingImg.setVisibility(View.GONE);
                rAudioRecording.setVisibility(View.GONE);
                rPlay_Delete.setVisibility(View.VISIBLE);
            }

        } else {
            /*此頁為影片 隱藏錄音，特效選項*/
//            audioRecordingImg.setVisibility(View.GONE);
            rAudioRecording.setVisibility(View.GONE);
            rPlay_Delete.setVisibility(View.GONE);
            aviaryImg.setVisibility(View.GONE);

            videoPlayImg.setVisibility(View.VISIBLE);

        }

    }

    private void setImageUrl(int position) {

        picUrl = (String) photoList.get(position).get("image_url_operate");

        if (jsonArray.length() != 0) {

            Picasso.with(mActivity)
                    .load(picUrl)
                    .config(Bitmap.Config.RGB_565)
                    .into(target);
        }

    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            MyLog.Set("d", mActivity.getClass(), "onBitmapLoaded");
//          photoLoadingImg.setVisibility(View.GONE);
            photoImg.reset();
            photoImg.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            MyLog.Set("d", mActivity.getClass(), "onBitmapFailed");
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_can_not_load_photo_try_again);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            MyLog.Set("d", mActivity.getClass(), "onPrepareLoad");

        }
    };

    private void selectBackground(int thisposition, int lastposition) {

        if (lastposition != photoList.size()) {
            //原本點擊的恢復
//            HashMap<String, Object> hashMap = new HashMap<>();
//            hashMap.put("photo_id", (String) photoList.get(lastposition).get("photo_id"));
//            hashMap.put("image_url", (String) photoList.get(lastposition).get("image_url"));
//            hashMap.put("image_url_thumbnail", (String) photoList.get(lastposition).get("image_url_thumbnail"));
//            hashMap.put("image_url_operate", (String) photoList.get(lastposition).get("image_url_operate"));
//            hashMap.put("select", false);
//
//            /*2016.06.23新增*/
//            hashMap.put("audio_url", (String) photoList.get(lastposition).get("audio_url"));
//            hashMap.put("video_url", (String) photoList.get(lastposition).get("video_url"));
//
//            /*2016.08.30新增*/
//            hashMap.put("description", (String) photoList.get(lastposition).get("description"));
//
//            if (lastposition == 0) {
//                hashMap.put("name", getResources().getString(R.string.pinpinbox_2_0_0_other_text_creation_cover));
//            } else {
//                hashMap.put("name", lastposition);
//            }
//
//            /*2016.11.29 new add*/
//            hashMap.put(Key.is_preview, (boolean) photoList.get(lastposition).get(Key.is_preview));
//            photoList.set(lastposition, hashMap);


            HashMap<String, Object> mp = photoList.get(lastposition);

            if (mp.containsKey(Key.select)) {
                mp.remove(Key.select);
                mp.put(Key.select, false);
                photoList.remove(lastposition);
                photoList.add(lastposition, mp);
            }


        }


//        --------------------------------------------------------------


        //點擊的改背景
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("photo_id", (String) photoList.get(thisposition).get("photo_id"));
//        map.put("image_url", (String) photoList.get(thisposition).get("image_url"));
//        map.put("image_url_thumbnail", (String) photoList.get(thisposition).get("image_url_thumbnail"));
//        map.put("image_url_operate", (String) photoList.get(thisposition).get("image_url_operate"));
//        map.put("select", true);
//
//        /*2016.06.23新增*/
//        map.put("audio_url", (String) photoList.get(thisposition).get("audio_url"));
//        map.put("video_url", (String) photoList.get(thisposition).get("video_url"));
//
//        /*2016.08.30新增*/
//        map.put("description", (String) photoList.get(thisposition).get("description"));
//
//        if (thisposition == 0) {
//            map.put("name", getResources().getString(R.string.pinpinbox_2_0_0_other_text_creation_cover));
//        } else {
//            map.put("name", thisposition);
//        }
//
//        /*2016.11.29 new add*/
//        map.put(Key.is_preview, (boolean) photoList.get(thisposition).get(Key.is_preview));
//
//        photoList.set(thisposition, map);


        HashMap<String, Object> mp = photoList.get(thisposition);

        if (mp.containsKey(Key.select)) {
            mp.remove(Key.select);
            mp.put(Key.select, true);
            photoList.remove(thisposition);
            photoList.add(thisposition, mp);
        }


    }


    private void getUploadFile() {

        File f = new File(aviaryPath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileUtility.createSDFile(aviaryPath);//建立新檔案

            MyLog.Set("d", getClass(), "建立空白文件成功");

            uploadFile = new File(aviaryPath);

        } catch (Exception e) {

            MyLog.Set("e", getClass(), "建立文件失敗");

            if (f.exists()) {
                f.delete();
            }
            e.printStackTrace();
        }

    }

    private void deleteUploadFile() {
        if (uploadFile != null && uploadFile.exists()) {
            uploadFile.delete();
        }
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
            System.gc();
        }
    }

    private void startFeather() {
        if (!CheckExternalStorage.isExternalStorageAvailable()) {
            showDialog(CheckExternalStorage.EXTERNAL_STORAGE_UNAVAILABLE);
            return;
        }


        Uri fromUri = Uri.fromFile(uploadFile);

        Intent newIntent = new AviaryIntent.Builder(this).setData(fromUri)
                .withOutput(fromUri)
                .withOutputFormat(Bitmap.CompressFormat.JPEG)
                .withOutputSize(MegaPixels.Mp5).withNoExitConfirmation(true)
                .saveWithNoChanges(false).withPreviewSize(1024)
                .build();

//        isFromAviary = true;


        if (fragmentSelectPhoto2 != null) {
            fragmentSelectPhoto2.setFromPPBCamera(true);
        }

        startActivityForResult(newIntent, ACTION_REQUEST_FEATHER);

    }

    private void cleanMp3() {

        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;

    }

    private void getJsonArray_setBottomList(JSONObject jsonObject) {
        try {
            String jdata = jsonObject.getString(Key.data);

            JSONObject j = new JSONObject(jdata);

            String usergrade = j.getString(Key.usergrade);
            JSONObject uj = new JSONObject(usergrade);
            intUserGrade = uj.getInt("photo_limit_of_album"); //2016.05.04

            MyLog.Set("d", getClass(), "intUserGrade => " + intUserGrade);

            String photo = j.getString("photo");
            jsonArray = new JSONArray(photo);
            photoList.clear();
            int array = jsonArray.length();
            for (int i = 0; i < array; i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String photo_id = obj.getString("photo_id");
                String image_url = obj.getString("image_url");
                String image_url_thumbnail = obj.getString("image_url_thumbnail");
                String image_url_operate = obj.getString("image_url_operate");

                //2016.06.23新增
                String audio_url = obj.getString("audio_url");
                String video_url = obj.getString("video_url");


                //2016.08.30新增
                String description = obj.getString("description");

                //2016.11.24 new adde
                boolean is_preview = obj.getBoolean(Key.is_preview);


                //2017.08.31 add
                String location = obj.getString(ProtocolKey.location);

                //20171018
                String user_id = obj.getString(ProtocolKey.user_id);


                MyLog.Set("d", getClass(),
                        "photo_id => " + photo_id + "\n"
                                + "image_url => " + image_url + "\n"
                                + "image_url_thumbnail => " + image_url_thumbnail + "\n"
                                + "image_url_operate => " + image_url_operate + "\n"
                                + "audio_url => " + audio_url + "\n"
                                + "video_url => " + video_url + "\n"
                                + "description => " + description + "\n"
                                + "is_preview => " + is_preview + "\n"
                                + "location => " + location + "\n"
                                + "user_id => " + user_id
                );


                HashMap<String, Object> map = new HashMap<>();
                map.put("photo_id", photo_id);
                map.put("image_url", image_url);
                map.put("image_url_thumbnail", image_url_thumbnail);
                map.put("image_url_operate", image_url_operate);
                map.put("select", false);

                if (i == 0) {
                    map.put("name", getResources().getString(R.string.pinpinbox_2_0_0_other_text_creation_cover));
                } else {
                    map.put("name", i);
                }

                //2016.06.23新增
                map.put("audio_url", audio_url);
                map.put("video_url", video_url);


                //2016.08.30新增
                map.put("description", description);

                //2016.11.24 new add
                map.put(Key.is_preview, is_preview);

                //2017.08.31 add
                map.put(Key.location, location);

                //20171018
                map.put(Key.user_id, user_id);

                photoList.add(map);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setChangeProject() {

        checkCount();
        checkLocation(thisPosition);
        checkDescription(thisPosition);
        checkDetail();
        checkMp3_setAudioUrl(thisPosition);
        setImageUrl(thisPosition);
        selectBackground(thisPosition, lastPosition);
        adapter.notifyDataSetChanged();


    }

    public void TapCreation() {

        addPicImg.setClickable(false);
        addUserImg.setClickable(false);
        albumSetImg.setClickable(false);
//        final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(mActivity);
//        builder.setAnimationInterpolator(new FastOutSlowInInterpolator());
//        builder.setCaptureTouchEventOutsidePrompt(true);
//        builder.setPrimaryText("");
//        builder.setFocalRadius(20f);
//        builder.setBackgroundColourFromRes(R.color.pinpinbox_2_0_0_first_main);
//        builder.setFocalColourFromRes(R.color.pinpinbox_2_0_0_first_main);
//        builder.setSecondaryTextColourFromRes(R.color.white);
//        builder.setTarget(addPicImg)
//                .setSecondaryText(R.string.pinpinbox_2_0_0_other_text_first_create0)
//                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
//                    @Override
//                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
//
//                    }
//
//                    @Override
//                    public void onHidePromptComplete() {
//
//
//                        builder.setTarget(addUserImg)
//                                .setSecondaryText(R.string.pinpinbox_2_0_0_other_text_first_create1)
//                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
//                                    @Override
//                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
//
//                                    }
//
//                                    @Override
//                                    public void onHidePromptComplete() {
//
//                                        builder.setTarget(albumSetImg)
//                                                .setSecondaryText(R.string.pinpinbox_2_0_0_other_text_first_create2)
//                                                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
//                                                    @Override
//                                                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
//
//                                                    }
//
//                                                    @Override
//                                                    public void onHidePromptComplete() {
//
//                                                        getdata.edit().putBoolean(TaskKeyClass.first_to_creation, true).commit();
//                                                        MyLog.Set("d", mActivity.getClass(), "save first_to_creation");
//
//
//                                                        SDpermissionEnable();
//
//
//                                                        new Handler().postDelayed(new Runnable() {
//                                                            @Override
//                                                            public void run() {
//                                                                addPicImg.setClickable(true);
//                                                                addUserImg.setClickable(true);
//                                                                albumSetImg.setClickable(true);
//                                                            }
//                                                        }, 300);
//
//                                                    }
//                                                }).show();
//
//
//                                    }
//                                }).show();
//
//                    }
//                }).show();


        SimpleTarget firstTarget = new SimpleTarget.Builder(mActivity)
                .setPoint(addPicImg)
                .setDescription(getResources().getString(R.string.pinpinbox_2_0_0_other_text_first_create0))
                .build();


        SimpleTarget secondTarget = new SimpleTarget.Builder(mActivity)
                .setPoint(addUserImg)
                .setDescription(getResources().getString(R.string.pinpinbox_2_0_0_other_text_first_create1))
                .build();

        SimpleTarget thirdTarget = new SimpleTarget.Builder(mActivity)
                .setPoint(albumSetImg)
                .setDescription(getResources().getString(R.string.pinpinbox_2_0_0_other_text_first_create2))
                .build();


//        CustomTarget thirdTarget =
//                new CustomTarget.Builder(mActivity).setPoint(addPicImg)
//                        .setRadius(100f)
//                        .setView(R.layout.test_custom_spotlight)
//                        .build();


        Spotlight.with(this)
                .setDuration(400) // duration of Spotlight emerging and disappearing in ms
                .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
                .setTargets(firstTarget, secondTarget, thirdTarget) // set targets. see below for more info
                .setOnSpotlightStartedListener(new OnSpotlightStartedListener() { // callback when Spotlight starts
                    @Override
                    public void onStarted() {
//                        Toast.makeText(mActivity.getApplicationContext(), "spotlight is started", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnSpotlightEndedListener(new OnSpotlightEndedListener() { // callback when Spotlight ends
                    @Override
                    public void onEnded() {
//                        Toast.makeText(mActivity.getApplicationContext(), "spotlight is ended", Toast.LENGTH_SHORT).show();


                        getdata.edit().putBoolean(TaskKeyClass.first_to_creation, true).commit();
                        MyLog.Set("d", mActivity.getClass(), "save first_to_creation");


                        SDpermissionEnable();


//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                                addPicImg.setClickable(true);
                                addUserImg.setClickable(true);
                                albumSetImg.setClickable(true);
//                            }
//                        }, 300);


                    }
                })
                .start(); // start Spotlight

    }

    private void toAlbumSettings() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);

        /*判斷是否新建利的作品*/

        if (isNewCreate) {
            if (!isSendFlurry) {
                FlurryUtil.onEvent(FlurryKey.creation_save_by_newcreate);
                isSendFlurry = true;
            }
        }

        bundle.putBoolean(Key.isNewCreate, isNewCreate);

        /*判斷投稿*/
        if (isContribute) {
            bundle.putBoolean(Key.isContribute, true);
            bundle.putString(Key.event_id, event_id);
            bundle.putString(Key.prefix_text, strPrefixText);
        }

        /*判斷audio*/
        String audioUrl = "";
        for (int i = 0; i < photoList.size(); i++) {
            audioUrl = (String) photoList.get(i).get("audio_url");
            if (!audioUrl.equals("") && !audioUrl.equals("null")) {
                MyLog.Set("d", getClass(), "有audio");
                break;
            }
        }
        if (!audioUrl.equals("") && !audioUrl.equals("null")) {
            bundle.putBoolean(Key.audio, true);
        } else {
            bundle.putBoolean(Key.audio, false);
        }

        /*判斷關閉編輯器*/
        bundle.putBoolean(Key.isCreationExist, true);

        /*判斷有無cover*/
        bundle.putBoolean(Key.cover, true);

        Intent intent = new Intent(mActivity, AlbumSettings2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);

        /*修改動作取消*/
        isModify = false;


    }

    private void refreshCollectAndInfo() {

        List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();


        Activity acCollect = null;
        Activity acInfo = null;
        Activity acMain = null;

        for (int i = 0; i < activityList.size(); i++) {
            if (activityList.get(i).getClass().getSimpleName().equals(MyCollect2Activity.class.getSimpleName())) {
                acCollect = activityList.get(i);
            }

            if (activityList.get(i).getClass().getSimpleName().equals(AlbumInfo2Activity.class.getSimpleName())) {
                acInfo = activityList.get(i);
            }

            if (activityList.get(i).getClass().getSimpleName().equals(Main2Activity.class.getSimpleName())) {
                acMain = activityList.get(i);
            }

        }

        if (acCollect != null) {

            ((FragmentMyUpLoad2) ((MyCollect2Activity) acCollect).getFragment(FragmentMyUpLoad2.class.getSimpleName())).doRefresh();
            MyLog.Set("d", getClass(), "refresh FragmentMyUpLoad2");

            ((FragmentCooperation2) ((MyCollect2Activity) acCollect).getFragment(FragmentCooperation2.class.getSimpleName())).doRefresh();
            MyLog.Set("d", getClass(), "refresh FragmentCooperation2");

        }

        if (acInfo != null) {

            ((AlbumInfo2Activity) acInfo).doGetAlbumInfo();

        }

        if (acInfo != null) {

            FragmentMe2 fragmentMe2 = (FragmentMe2) ((Main2Activity) acMain).getFragment(FragmentMe2.class.getSimpleName());

            if (fragmentMe2 != null && fragmentMe2.getAlbumList() != null) {
                fragmentMe2.doRefresh(false);
            }

        }


    }

    private void cleanPicasso() {

        if (photoList != null && photoList.size() > 0) {

            for (int i = 0; i < photoList.size(); i++) {

                String url = (String) photoList.get(i).get("image_url_operate");
                String bigurl = (String) photoList.get(i).get("image_url_thumbnail");
                Picasso.with(getApplicationContext()).invalidate(url);
                Picasso.with(getApplicationContext()).invalidate(bigurl);

            }

        }

        System.gc();

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {
                    case DoConfirm:
                        doConfirm();
                        break;

                    case DoSaveToFinish:
                        doSaveToFinish();
                        break;

                    case DoingTypeClass.DoDefault:
                        doGetPhoto();
                        break;

                    case DoAfterAviary:
                        doAfterAviary();
                        break;

                    case DoDeletePhoto:
                        doDeletePhoto();
                        break;

                    case DoChange:
                        doChange();
                        break;

                    case DoUploadAudio:
                        doUploadAudio();
                        break;

                    case DoDeleteAudio:
                        doDeleteAudio();
                        break;

                    case DoFirstCreateFreeAlbum:
                        doFirstCreateFreeAlbum();
                        break;

                    case DoDescription:
                        doDescription(strDescription);
                        break;

                    case DoLocation:
                        doLocation(strLocation);
                        break;

                    case DoDeleteBitmapToRefresh:
                        doDeleteBitmapToRefresh();
                        break;

                    case DoSendPreview:
                        doSendPreview();
                        break;

                    case DoingTypeClass.DoGetSettings:
                        doGetSettings();
                        break;

                    case DoingTypeClass.DoSetSettings:
                        doSetSettings();
                        break;

                    case DoingTypeClass.DoSendAudio:
                        doSendAudio();
                        break;


                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doConfirm() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        confirmTask = new ConfirmTask();
        confirmTask.execute();
    }

    private void doSaveToFinish() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        saveToFinishTask = new SaveToFinishTask();
        saveToFinishTask.execute();
    }

    private void doGetPhoto() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        getPhotoTask = new GetPhotoTask();
        getPhotoTask.execute();
    }

    private void doAfterAviary() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        afterAviaryTask = new AfterAviaryTask();
        afterAviaryTask.execute();
    }

    private void doDeletePhoto() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        deleteTask = new DeleteTask();
        deleteTask.execute();
    }

    private void doChange() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        changeTask = new ChangeTask();
        changeTask.execute();
    }

    private void doUploadAudio() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        upLoadAudioTask = new UpLoadAudioTask();
        upLoadAudioTask.execute();
    }

    private void doDeleteAudio() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        deleteAudioTask = new DeleteAudioTask();
        deleteAudioTask.execute();
    }

    private void doFirstCreateFreeAlbum() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        firstCreateFreeAlbumTask = new FirstCreateFreeAlbumTask();
        firstCreateFreeAlbumTask.execute();
    }

    private void doDescription(String strDes) {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        descriptionTask = new DescriptionTask(strDes);
        descriptionTask.execute();
    }

    private void doLocation(String strLoc) {


        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        locationTask = new LocationTask(strLoc);
        locationTask.execute();


    }

    public void doDeleteBitmapToRefresh() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        deleteBitmapToReFreshTask = new DeleteBitmapToReFreshTask();
        deleteBitmapToReFreshTask.execute();

    }

    private void doSendPreview() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        sendPreviewTask = new SendPreviewTask();
        sendPreviewTask.execute();
    }

    private void doGetSettings() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getSettingsTask = new GetSettingsTask();
        getSettingsTask.execute();

    }

    private void doSetSettings() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        setSettingsTask = new SetSettingsTask();
        setSettingsTask.execute();

    }

    private void doSendAudio() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        sendAudioTask = new SendAudioTask();
        sendAudioTask.execute();

    }

    /*protocol32*/
    private class GetSettingsTask extends AsyncTask<Void, Void, Object> {

        private int p32Result = -1;
        private String p32Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetSettings;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P32_GetAlbumdataOptions, SetMapByProtocol.setParam32_getalbumdataoptions(id, token), null);

                MyLog.Set("d", this.getClass(), "p32strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p32Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p32Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p32Result == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONObject object = new JSONObject(jdata);

                        String audio = JsonUtility.GetString(object, ProtocolKey.audio);

                        /*添加空音樂*/
                        JSONArray audioArray = new JSONArray(audio);
                        for (int i = 0; i < audioArray.length(); i++) {
                            JSONObject obj = audioArray.getJSONObject(i);
                            audioList.add(
                                    builder
                                            .id(JsonUtility.GetInt(obj, ProtocolKey.id))
                                            .name(JsonUtility.GetString(obj, ProtocolKey.name))
                                            .url(JsonUtility.GetString(obj, ProtocolKey.url))
                                            .select(false)
                                            .build()
                            );
                        }

                    } else if (p32Result == 0) {
                        p32Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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
            if (p32Result == 1) {

                audioAdapter.notifyDataSetChanged();

                for (int i = 0; i < audioList.size(); i++) {

                    MyLog.Set("d", mActivity.getClass(), audioList.get(i).getName());
                    MyLog.Set("d", mActivity.getClass(), audioList.get(i).getId() + "");

                }


                doSetSettings();

            } else if (p32Result == 0) {

                DialogV2Custom.BuildError(mActivity, p32Message);


            } else if (p32Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }

    /*protocol33*/
    private class SendPreviewTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoSendPreview;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            ArrayList<String> photoIdList = new ArrayList<>();

            for (int i = 0; i < selectingList.size(); i++) {

                boolean b = (boolean) selectingList.get(i).get(Key.is_preview);
                if (b) {
                    photoIdList.add((String) selectingList.get(i).get("photo_id"));
                }
            }


            String str = "";

            for (int i = 0; i < photoIdList.size(); i++) {
                if (i == 0) {
                    str = photoIdList.get(i);
                } else {
                    str = str + "," + photoIdList.get(i);
                }
            }


            JSONObject obj = new JSONObject();
            try {
                obj.put("preview", str);
            } catch (Exception e) {
                e.printStackTrace();
            }


            MyLog.Set("d", mActivity.getClass(), " obj.toString() => " + obj.toString());


            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P33_AlbumSettings, SetMapByProtocol.setParam33_albumsettings(id, token, album_id, obj.toString()), null);
                MyLog.Set("d", getClass(), "p33strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p33Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p33Result = jsonObject.getString(Key.result);
                    if (p33Result.equals("1")) {


                    } else if (p33Result.equals("0")) {
                        p33Message = jsonObject.getString(Key.message);
                    } else {
                        p33Result = "";
                    }
                } catch (Exception e) {
                    p33Result = "";
                    e.printStackTrace();
                }
            } else {
                p33Result = "";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            popCreatePreview.dismiss();

            if (p33Result.equals("1")) {

                isModify = true;

                photoPreviewType = false;

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_edit_finish);

                /*將選擇的動作帶入作品list做更新*/
                photoList = selectingList;

            } else if (p33Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p33Message);

            } else if (p33Result.equals(Key.timeout)) {

                connectInstability();

            } else {

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    private class SendAudioTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSendAudio;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            JSONObject obj = new JSONObject();
            try {
                obj.put("audio_mode", mySelectAudioMode);

                if (mySelectAudioMode.equals(SINGULAR))

                    for (int i = 0; i < audioList.size(); i++) {
                        if (audioList.get(i).isSelect()) {
                            obj.put(ProtocolKey.audio, audioList.get(i).getId());
                        }
                    }


            } catch (Exception e) {
                e.printStackTrace();
            }


            MyLog.Set("d", mActivity.getClass(), " obj.toString() => " + obj.toString());


            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P33_AlbumSettings, SetMapByProtocol.setParam33_albumsettings(id, token, album_id, obj.toString()), null);
                MyLog.Set("d", mActivity.getClass(), "p33strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p33Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p33Result = jsonObject.getString(Key.result);
                    if (p33Result.equals("1")) {

                    } else if (p33Result.equals("0")) {
                        p33Message = jsonObject.getString(Key.message);
                    } else {
                        p33Result = "";
                    }
                } catch (Exception e) {
                    p33Result = "";
                    e.printStackTrace();
                }
            } else {
                p33Result = "";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            popCreatePreview.dismiss();

            if (p33Result.equals("1")) {

                isModify = true;

                if (!mySelectAudioMode.equals(currentAudioMode)) {
                    PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_mode_is_change);
                }


                currentAudioMode = mySelectAudioMode;

                popCreateAudio.dismiss();

                doDeleteBitmapToRefresh();


            } else if (p33Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p33Message);

            } else if (p33Result.equals(Key.timeout)) {

                connectInstability();

            } else {

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    /*protocol34*/
    private class SetSettingsTask extends AsyncTask<Void, Void, Object> {


        private int p34Result = -1;
        private String p34Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSetSettings;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P34_GetAlbumSettings, SetMapByProtocol.setParam34_getalbumsettings(id, token, album_id), null);

                MyLog.Set("d", getClass(), "p34strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p34Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p34Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);

                    if (p34Result == 1) {

                        String jsonData = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONObject object = new JSONObject(jsonData);

                        intAudio = JsonUtility.GetInt(object, ProtocolKey.audio);

                        MyLog.Set("d", mActivity.getClass(), "intAudio => " + intAudio);


                    } else if (p34Result == 0) {
                        p34Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            if (p34Result == 1) {

                for (int i = 0; i < audioList.size(); i++) {
                    if (intAudio == audioList.get(i).getId()) {
                        audioList.get(i).setSelect(true);
                        audioAdapter.notifyItemChanged(i);
                        rvAudio.scrollToPosition(i);
                        break;
                    }
                }

                setAudioMode(currentAudioMode);

            } else if (p34Result == 0) {

                DialogV2Custom.BuildError(mActivity, p34Message);


            } else if (p34Result == Key.TIMEOUT) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }

    }

    /*protocol55*/
    private class ConfirmTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoConfirm;
            startLoading();
            PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_is_saving);

        }

        @Override
        protected Object doInBackground(Void... params) {
            Map<String, String> data = new HashMap<String, String>();
            data.put(Key.id, id);
            data.put(Key.token, token);
            data.put(Key.album_id, album_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, id);
            sendData.put(Key.token, token);
            sendData.put(Key.album_id, album_id);
            sendData.put(Key.sign, sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P55_UpDateAlbumOfDiy, sendData, null);
                MyLog.Set("d", getClass(), "p55strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p55Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p55Result = jsonObject.getString(Key.result);
                    if (p55Result.equals("1")) {

                        MyLog.Set("d", mActivity.getClass(), "建立完成 " + album_id + " 相本");

                    } else if (p55Result.equals("0")) {
                        p55Message = jsonObject.getString(Key.message);
                    } else {
                        p55Result = "";
                    }
                } catch (Exception e) {
                    p55Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p55Result.equals("1")) {

                refreshCollectAndInfo();

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_save_album_done);

                toAlbumSettings();

            } else if (p55Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p55Message);
            } else if (p55Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }
    }

    private class SaveToFinishTask extends AsyncTask<Void, Void, Object> {

        private Toast toast;

        @SuppressLint("WrongConstant")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();

            doingType = DoSaveToFinish;

            View view = LayoutInflater.from(mActivity).inflate(R.layout.pinpinbox_toast, null);
            TextView tvToast = (TextView) view.findViewById(R.id.tvToast);
            tvToast.setText(R.string.pinpinbox_2_0_0_toast_message_saving_album);

            toast = new Toast(mActivity.getApplicationContext());
            toast.setDuration(200);
            toast.setView(view);
            toast.show();

        }

        @Override
        protected Object doInBackground(Void... params) {
            Map<String, String> data = new HashMap<String, String>();
            data.put(Key.id, id);
            data.put(Key.token, token);
            data.put(Key.album_id, album_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, id);
            sendData.put(Key.token, token);
            sendData.put(Key.album_id, album_id);
            sendData.put(Key.sign, sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P55_UpDateAlbumOfDiy, sendData, null);
                MyLog.Set("d", getClass(), "p55strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p55Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);
                    p55Result = jsonObject.getString(Key.result);
                    if (p55Result.equals("1")) {

                        MyLog.Set("d", getClass(), "成功建立 " + album_id + " 相本");

                    } else if (p55Result.equals("0")) {
                        p55Message = jsonObject.getString(Key.message);
                    } else {
                        p55Result = "";
                    }
                } catch (Exception e) {
                    p55Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (toast != null) {
                toast.cancel();
                toast = null;
            }

            if (p55Result.equals("1")) {


                if (photoList != null && photoList.size() == 0) {


                    MyLog.Set("d", this.getClass(), "沒有相片 將權限修改為關閉");

                    doActClose();

                } else {

                    refreshCollectAndInfo();
                    back();

                }


//                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_save_album_done);


            } else if (p55Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p55Message);

            } else if (p55Result.equals(Key.timeout)) {

                connectInstability();

            } else {

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());

            }


        }

        private void doActClose() {


            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put(ProtocolKey.act, "close");
            } catch (Exception e) {
                e.printStackTrace();
            }

            String settings = jsonObject.toString();

            protocol33 = new Protocol33(
                    mActivity,
                    id,
                    token,
                    album_id,
                    settings,
                    new Protocol33.TaskCallBack() {

                        @Override
                        public void Prepare() {
                            doingType = DoingTypeClass.DoSend;
                            startLoading();
                        }

                        @Override
                        public void Post() {
                            dissmissLoading();
                        }

                        @Override
                        public void Success() {

                            refreshCollectAndInfo();
                            back();

                        }

                        @Override
                        public void TimeOut() {
                            doActClose();
                        }


                    }
            );


        }
    }

    /*protocol57*/
    private class GetPhotoTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P57_GetAlbumOfDiy, SetMapByProtocol.setParam57_getalbumofdiy(id, token, album_id), null);
                MyLog.Set("d", mActivity.getClass(), "p57strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p57Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p57Result = JsonUtility.GetString(jsonObject, Key.result);
                    if (p57Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                        String jdata = JsonUtility.GetString(jsonObject, Key.data);
                        JSONObject jsonData = new JSONObject(jdata);

                        String album = JsonUtility.GetString(jsonData, ProtocolKey.album);
                        JSONObject jsonAlbum = new JSONObject(album);
                        currentAudioMode = JsonUtility.GetString(jsonAlbum, ProtocolKey.album_audio_mode);

                        if (intFirstCallP57) {

                            frame = jsonData.getString("frame");
                            intFirstCallP57 = false;
                        }

                    } else if (p57Result.equals("0")) {//if(p57Result.equals("1"))
                        p57Message = jsonObject.getString(Key.message);
                    } else {
                        p57Result = "";
                    }
                } catch (Exception e) {
                    p57Result = "";
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p57Result.equals("1")) {

                //2016.06.28註銷
                int array = jsonArray.length();

                if (bFirstInto) {

                    MyLog.Set("d", mActivity.getClass(), "第一次進入");

                    initView();
                    setCount();
                    deleteClick();
                    aviaryClick();


                    /*2016.06.21新增錄音相關*/
                    recordingClick();
                    playAudioClick();
                    deleteAudioClick();

                    /*2016.06.24新增播放影片*/
                    playVideoClick();

                    /*2016.08.30新增單頁說明*/
                    addDescription();
//                    deleteDescription();

                    /*2017.08.30新增地圖資訊*/
                    addLocation();
                    deleteLocation();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addPicture();
                        }
                    }, 400);


                    addUser();


                    backImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            backCheck();
                        }
                    });


                    /*2016.10.11新增*/
                    if (identity != null && identity.equals("admin")) {
                        tvCheck.setText(R.string.pinpinbox_2_0_0_button_next);
                    } else {
                        tvCheck.setText(R.string.pinpinbox_2_0_0_button_done);
                    }

                    /*2016.11.28 new add*/
                    AlbumSetContent();

                    bFirstInto = false;

                }


                if (array != 0) {

                    rPhotoSettingBar.setVisibility(View.VISIBLE);


                    setCount();


                    if (adapter == null) {
                        adapter = new RecyclerCreationAdapter(mActivity, photoList, identity);
                    }

                    rvPhoto.setAdapter(adapter);

                    if (jsonArray.length() != thisPosition) {

                        switch (createMode) {

                            case CreationFast: //快速

                                thisPosition = jsonArray.length() - 1;
                                lastPosition = jsonArray.length() - 1;

                                break;

                            case CreationTemplate: //套版
                                if (bAddPage) {
                                    MyLog.Set("d", mActivity.getClass(), "add new page");
                                    thisPosition = jsonArray.length() - 1;
                                    lastPosition = jsonArray.length() - 1;
                                }
                                break;
                        }

                        setChangeProject();


                    }
                } else {

                    /*作品無內容*/

                    rPhotoSettingBar.setVisibility(View.INVISIBLE);


                    boolean bFirstToCreation = getdata.getBoolean(TaskKeyClass.first_to_creation, false);

                    if (!bFirstToCreation) {

                        MyLog.Set("d", mActivity.getClass(), "open creation teach");

                        TapCreation();

                    } else {
                        SDpermissionEnable();
                    }

                }

                bAddPage = false; //照片添加完成 取消增加狀態

            } else if (p57Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p57Message);
            } else if (p57Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }

    }

    private class DeleteBitmapToReFreshTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoDeleteBitmapToRefresh;
            startLoading();

        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                deleteUploadFile();
            } catch (Exception e) {
                e.printStackTrace();
            }


            photoList.clear();

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P57_GetAlbumOfDiy, SetMapByProtocol.setParam57_getalbumofdiy(id, token, album_id), null);
                MyLog.Set("d", getClass(), "p57strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p57Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);
                    p57Result = JsonUtility.GetString(jsonObject, Key.result);
                    if (p57Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                        String jdata = JsonUtility.GetString(jsonObject, Key.data);
                        JSONObject jsonData = new JSONObject(jdata);

                        String album = JsonUtility.GetString(jsonData, ProtocolKey.album);
                        JSONObject jsonAlbum = new JSONObject(album);
                        currentAudioMode = JsonUtility.GetString(jsonAlbum, ProtocolKey.album_audio_mode);

                        if (intFirstCallP57) {

                            frame = jsonData.getString("frame");
                            intFirstCallP57 = false;
                        }

                    } else if (p57Result.equals("0")) {//if(p57Result.equals("1"))
                        p57Message = jsonObject.getString(Key.message);
                    } else {
                        p57Result = "";
                    }
                } catch (Exception e) {
                    p57Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();


            if (p57Result.equals("1")) {

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
//                    adapter = null;
                }

                int array = jsonArray.length();

                if (array != 0) {

                    setCount();

                    setChangeProject();

                    /*2017.08.31 remove*/
//                    if (array != thisPosition) {
//
//                        switch (createMode) {
//
//                            case CreationFast: //快速
//                                thisPosition = jsonArray.length() - 1;
//                                lastPosition = jsonArray.length() - 1;
//
//                                break;
//
//                            case CreationTemplate: //套版
//                                if (bAddPage) {
//                                    thisPosition = jsonArray.length() - 1;
//                                    lastPosition = jsonArray.length() - 1;
//                                }
//                                break;
//                        }
//
//                        setChangeProject();
//
//                    }
                }


            } else if (p57Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p57Message);
            } else if (p57Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }

    /*protocol59*/
    private class AfterAviaryTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoAfterAviary;
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String sPhoto_id = (String) photoList.get(lastPosition).get("photo_id");
            String path = (String) photoList.get(lastPosition).get("image_url_operate");

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("album_id", album_id);
            data.put("photo_id", sPhoto_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("album_id", album_id);
            sendData.put("photo_id", sPhoto_id);
            sendData.put("sign", sign);
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P59_UpDatePhotoOfDiy, sendData, uploadFile);
                MyLog.Set("d", getClass(), "p59strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p59Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);

                    p59Result = jsonObject.getString(Key.result);
                    if (p59Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                        if (bmp != null && !bmp.isRecycled()) {
                            bmp.recycle();
                            bmp = null;
                            System.gc();
                        }

                    } else if (p59Result.equals("0")) {
                        p59Message = jsonObject.getString(Key.message);
                    } else {
                        p59Result = "";
                    }


                } catch (Exception e) {
                    p59Result = "";
                    deleteUploadFile();
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            deleteUploadFile();

            if (p59Result != null) {
                if (p59Result.equals("1")) {

                    isModify = true; //已修改內容
                    setChangeProject();
                    lastPosition = thisPosition;


                } else if (p59Result.equals("0")) {
                    DialogV2Custom.BuildError(mActivity, p59Message);
                } else if (p59Result.equals(Key.timeout)) {
                    connectInstability();
                } else {
                    DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
                }


            }

        }
    }

    /*protocol60*/
    private class DeleteTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoDeletePhoto;
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String sPhoto_id = (String) photoList.get(thisPosition).get("photo_id");
            String path = (String) photoList.get(thisPosition).get("image_url_operate");
            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("album_id", album_id);
            data.put("photo_id", sPhoto_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("album_id", album_id);
            sendData.put("photo_id", sPhoto_id);
            sendData.put("sign", sign);

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P60_DeletePhotoOfDiy, sendData, null);
                MyLog.Set("d", getClass(), "p60strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p60Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p60Result = jsonObject.getString(Key.result);
                    if (p60Result.equals("1")) {
                        getJsonArray_setBottomList(jsonObject);
                    } else if (p60Result.equals("0")) {
                        p60Message = jsonObject.getString(Key.message);
                    } else {
                        p60Result = "";
                    }

                } catch (Exception e) {
                    p60Result = "";
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p60Result != null) {
                if (p60Result.equals("1")) {

                    isModify = true; //已修改內容

                    setCount();
                    adapter.notifyDataSetChanged();

                    if (jsonArray.length() != 0) {

                        rPhotoSettingBar.setVisibility(View.VISIBLE);


                        if (thisPosition == jsonArray.length()) {
                            thisPosition = thisPosition - 1;
                            lastPosition = lastPosition - 1;
                        }


                        setChangeProject();


                    } else {
                        photoImg.setImageDrawable(null);
                        rPhotoSettingBar.setVisibility(View.INVISIBLE);


                    }
                } else if (p60Result.equals("0")) {
                    DialogV2Custom.BuildError(mActivity, p60Message);
                } else if (p60Result.equals(Key.timeout)) {
                    connectInstability();
                } else {
                    DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
                }

            }
        }

    }

    /*protocol62*/
    private class ChangeTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoChange;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String sort = "";
            for (int i = 0; i < photoList.size(); i++) {
                if (i == 0) {
                    sort = (String) photoList.get(i).get("photo_id");
                } else {
                    sort = sort + "," + (String) photoList.get(i).get("photo_id");
                }

            }
            MyLog.Set("d", getClass(), "排序為 => " + sort);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P62_SortPhotoOfDiy, SetMapByProtocol.setParam62_sortphotoofdiy(id, token, album_id, sort), null);
                MyLog.Set("d", getClass(), "p62strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p62Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);

                    p62Result = jsonObject.getString(Key.result);

                    if (p62Result.equals("1")) {
                        getJsonArray_setBottomList(jsonObject);
                    } else if (p62Result.equals("0")) {
                        p62Message = jsonObject.getString(Key.message);
                    } else {
                        p62Result = "";
                    }
                } catch (Exception e) {
                    p62Result = "";
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p62Result.equals("1")) {
                isModify = true; //已修改內容

                thisPosition = 0;
                lastPosition = 0;
                setChangeProject();
                photoChangeType = false;

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_edit_finish);

            } else if (p62Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p62Message);

            } else if (p62Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    /*protocol78*/
    private class UpLoadAudioTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoUploadAudio;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String sPhoto_id = (String) photoList.get(thisPosition).get("photo_id");

            File file = new File(mp3Path);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P78_UpdateAudioOfDiy, SetMapByProtocol.setParam78_UpdateAudioOfDiy(id, token, album_id, sPhoto_id), file);
                MyLog.Set("d", mActivity.getClass(), "p78strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p78Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p78Result = jsonObject.getString(Key.result);
                    if (p78Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                    } else if (p78Result.equals("0")) {
                        p78Message = jsonObject.getString(Key.message);
                    } else {
                        p78Result = "";
                    }

                } catch (Exception e) {
                    p78Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p78Result.equals("1")) {

                isModify = true; //已修改內容

                setChangeProject();

//                audioRecordingImg.setVisibility(View.GONE);
                rAudioRecording.setVisibility(View.GONE);
                rPlay_Delete.setVisibility(View.VISIBLE);

                if (!currentAudioMode.equals(PLURAL)) {
                    setAudioMode(PLURAL);
                    doSendAudio();
                }


            } else if (p78Result.equals("0")) {
                audioRecordingImg.setImageResource(R.drawable.ic200_micro_white);
                DialogV2Custom.BuildError(mActivity, p78Message);

            } else if (p78Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                audioRecordingImg.setImageResource(R.drawable.ic200_micro_white);
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    /*protocol79*/
    private class DeleteAudioTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoDeleteAudio;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String photo_id = (String) photoList.get(thisPosition).get("photo_id");
            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P79_DeleteAudioOfDiy, SetMapByProtocol.setParam79_deleteaudioofdiy(id, token, album_id, photo_id), null);
                MyLog.Set("d", getClass(), "p79strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p79Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p79Result = jsonObject.getString(Key.result);
                    if (p79Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                    } else if (p79Result.equals("0")) {
                        p79Message = jsonObject.getString(Key.message);
                    } else {
                        p79Result = "";
                    }

                } catch (Exception e) {
                    p79Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p79Result.equals("1")) {
                isModify = true; //已修改內容

                File f = new File(mp3Path);
                if (f.exists()) {
                    f.delete();
                    MyLog.Set("d", mActivity.getClass(), "delete => " + mp3Path);
                }

                setChangeProject();

                audioRecordingImg.setImageResource(R.drawable.ic200_micro_white);
//                audioRecordingImg.setVisibility(View.VISIBLE);
                rAudioRecording.setVisibility(View.VISIBLE);
                rPlay_Delete.setVisibility(View.GONE);

                if (mPlayer != null) {
                    mPlayer.stop();
                    mPlayer.release();
                    mPlayer = null;
                }


            } else if (p79Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p79Message);

            } else if (p79Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    /*protocol83*/
    private class FirstCreateFreeAlbumTask extends AsyncTask<Void, Void, Object> {

        private String name;
        private String reward;
        private String reward_value;
        private String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoFirstCreateFreeAlbum;
            startLoading();
        }


        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, SetMapByProtocol.setParam83_dotask(id, token, TaskKeyClass.create_free_album, "google"), null);
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

                        JSONObject eventObj = new JSONObject(event);
                        url = eventObj.getString(Key.url);

                    } else if (p83Result.equals("2")) {
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
            dissmissLoading();
            if (p83Result.equals("1")) {
                final DialogHandselPoint d = new DialogHandselPoint(mActivity);
                d.getTvTitle().setText(name);

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");

                    /*獲取當前使用者P點*/
                    String point = getdata.getString("point", "");
                    int p = StringIntMethod.StringToInt(point);

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    getdata.edit().putString("point", newP).commit();

                    getdata.edit().putBoolean(TaskKeyClass.create_free_album, true).commit();

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

                d.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        sendWork();
                    }
                });


            } else if (p83Result.equals("2")) {

                getdata.edit().putBoolean(TaskKeyClass.create_free_album, true).commit();
                sendWork();

            } else if (p83Result.equals("0")) {

                MyLog.Set("d", this.getClass(), "p83Message" + p83Message);

                /*2016.11.14 new add*/
                sendWork();


            } else if (p83Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    /*新增刪除說明都使用protocol59*/
    private class DescriptionTask extends AsyncTask<Void, Void, Object> {

        private String des;

        public DescriptionTask(String des) {
            this.des = des;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoDescription;
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String photo_id = (String) photoList.get(thisPosition).get("photo_id");

            MyLog.Set("d", getClass(), "allready description => " + des);

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("album_id", album_id);
            data.put("photo_id", photo_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("album_id", album_id);
            sendData.put("photo_id", photo_id);
            sendData.put("settings[description]", des);
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P59_UpDatePhotoOfDiy, sendData, null);
                MyLog.Set("d", getClass(), "p59strJson by settings description => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p59Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);

                    p59Result = jsonObject.getString(Key.result);
                    if (p59Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                    } else if (p59Result.equals("0")) {
                        p59Message = jsonObject.getString(Key.message);
                    } else {
                        p59Result = "";
                    }

                } catch (Exception e) {
                    p59Result = "";
                    deleteUploadFile();
                    e.printStackTrace();

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p59Result != null) {
                if (p59Result.equals("1")) {

                    isModify = true; //已修改內容
                    setChangeProject();
                    lastPosition = thisPosition;


                } else if (p59Result.equals("0")) {
                    DialogV2Custom.BuildError(mActivity, p59Message);
                } else if (p59Result.equals(Key.timeout)) {
                    connectInstability();
                } else {
                    DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
                }


            }

        }
    }


    /*新增刪除地點都使用protocol59*/
    private class LocationTask extends AsyncTask<Void, Void, Object> {

        private String location;

        public LocationTask(String location) {
            this.location = location;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoLocation;
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String photo_id = (String) photoList.get(thisPosition).get("photo_id");

            MyLog.Set("d", getClass(), "allready location => " + location);

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("album_id", album_id);
            data.put("photo_id", photo_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("album_id", album_id);
            sendData.put("photo_id", photo_id);
            sendData.put("settings[location]", location);
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P59_UpDatePhotoOfDiy, sendData, null);
                MyLog.Set("d", getClass(), "p59strJson by settings location => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p59Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);

                    p59Result = jsonObject.getString(Key.result);
                    if (p59Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                    } else if (p59Result.equals("0")) {
                        p59Message = jsonObject.getString(Key.message);
                    } else {
                        p59Result = "";
                    }

                } catch (Exception e) {
                    p59Result = "";
                    deleteUploadFile();
                    e.printStackTrace();

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p59Result != null) {
                if (p59Result.equals("1")) {

                    isModify = true; //已修改內容
                    setChangeProject();
                    lastPosition = thisPosition;


                } else if (p59Result.equals("0")) {
                    DialogV2Custom.BuildError(mActivity, p59Message);
                } else if (p59Result.equals(Key.timeout)) {
                    connectInstability();
                } else {
                    DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
                }


            }

        }
    }


    private class BeforeAviaryTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getUploadFile();
            HttpUtility hu = new HttpUtility();
            hu.downPic((String) photoList.get(lastPosition).get("image_url"),
                    DirClass.pathName_FromAviary,
                    DirClass.sdPath + "PinPinBox" + id + "/", getApplication().getApplicationContext());

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();

            startFeather();

        }
    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        cleanPicasso();

        switch (view.getId()) {
            case R.id.tvCheck:
                sendAlbumCheck();
                break;

            case R.id.refreshImg:
                reFreshClick();
                break;

            case R.id.albumSetImg:

                //20171018
//                if(!checkMyPhoto(thisPosition)){
//                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
//                    return;
//                }

                //20171019
                if (!identity.equals("admin")) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deficiency_identity);
                    return;
                }


                popCreationSet.show(rBackground);
                break;

            /*pop creation set*/
            case R.id.tvSort:
                popCreationSet.dismiss();
                changeItemClick();
                break;

            case R.id.tvSelectPreview:
                popCreationSet.dismiss();
                selectPreviewClick();
                break;

            case R.id.tvSetAudio:
                popCreationSet.dismiss();

                setAudioMode(currentAudioMode);
                popCreateAudio.show(rBackground);

                if (audioList == null || audioList.size() == 0) {
                    doGetSettings();
                }

                break;

             /*pop creation audio*/
            case R.id.tvSave:

                if (mySelectAudioMode.equals(currentAudioMode)) {
                    popCreateAudio.dismiss();
                    doSendAudio();
                    return;
                }


                DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setStyle(DialogStyleClass.CHECK);
                d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_change_album_mode);
                d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_cancel);
                d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_confirm_to_change_audio_mode);
                d.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {
                        doSendAudio();
                    }
                });
                d.show();

                break;


            /*pop creation preview*/
            case R.id.tvPreviewConfirm:
                popCreatePreview.dismiss();
                break;

            /*pop creation sort*/
            case R.id.tvSortConfirm:
                popCreateSort.dismiss();
                break;

            /*pop creation add*/
            case R.id.linAddPhoto:
                popCreateAdd.dismiss();

                addPicByFast();

                break;

            case R.id.linAddVideo:
                popCreateAdd.dismiss();

                addVideo();

                break;


        }

    }


    public String getAlbum_id() {
        return album_id;
    }

    public String getIdentity() {
        return identity;
    }

    public String getEvent_id() {
        return this.event_id;
    }


    public boolean isModify() {
        return this.isModify;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {

        if (fragmentSelectPhoto2 != null && fragmentSelectPhoto2.isAdded() && fragmentSelectPhoto2.isVisible()) {

            getSupportFragmentManager().beginTransaction().hide(fragmentSelectPhoto2).commit();


        } else if (fragmentSelectVideo2 != null && fragmentSelectVideo2.isAdded() && fragmentSelectVideo2.isVisible()) {

            getSupportFragmentManager().beginTransaction().hide(fragmentSelectVideo2).commit();

        } else {
            backCheck();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        MyLog.Set("d", getClass(), "resultCode => " + resultCode);
        MyLog.Set("d", getClass(), "requestCode => " + requestCode);


        if (resultCode == UpLoad_OK) {

            if (requestCode == AddPicture) {
                bAddPage = true;//確定增加照片
                isModify = true; //2016.06.07新增判斷確定有增加照片

                if (getPhotoTask != null && !getPhotoTask.isCancelled()) {
                    getPhotoTask.cancel(true);
                    getPhotoTask = null;
                }


                doDeleteBitmapToRefresh();
            }
        } else if (resultCode == RESULT_OK) {
            if (requestCode == ACTION_REQUEST_FEATHER) {
                boolean changed = true;
                if (null != data) {
                    Bundle extra = data.getExtras();

                    if (null != extra) {
                        // image was changed by the user?
                        changed = extra.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);

                        MyLog.Set("d", getClass(), "ready do AfterAviaryTask()");

                        doAfterAviary();

                    }
                }
                if (!changed) {

                    MyLog.Set("d", getClass(), "User did not modify the image, but just clicked on 'Done' button");

                }
            }
        } else {
            deleteUploadFile();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final int SUCCESS = 0;
    private final int REFUSE = -1;

    private int checkPermission(Activity ac, String permission) {

        int doingType = 0;

        if (ActivityCompat.checkSelfPermission(ac, permission) == PackageManager.PERMISSION_GRANTED) {
            //已授權
            doingType = SUCCESS;
        } else {
            //未授權 判斷是否彈出詢問框 true => 彈出
//            if(ActivityCompat.shouldShowRequestPermissionRationale(ac, permission)){
            doingType = REFUSE;
//            }else {
//                doingType = REFUSE_NO_ASK;
//            }
        }
        return doingType;

    }


    @PermissionGrant(REQUEST_CODE_SDCARD)
    public void requestSdcardSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                checkCreateMode();

            }
        }, 500);

    }

    @PermissionDenied(REQUEST_CODE_SDCARD)
    public void requestSdcardFailed() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");

            DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_sdcard);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    SystemUtility.getAppDetailSettingIntent(mActivity);
                }
            });
            d.show();

        } else {
            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");

        }
    }


    @PermissionGrant(REQUEST_CODE_RECORD)
    public void requestRecordSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                record();
            }
        }, 300);//500
    }


    @PermissionDenied(REQUEST_CODE_RECORD)
    public void requestRecordFailed() {

        if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.RECORD_AUDIO)) {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");

            DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_record);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    SystemUtility.getAppDetailSettingIntent(mActivity);
                }
            });
            d.show();

        } else {
            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");

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
    public void onPause() {
        if (mpBackground != null) {
            if (mpBackground.isPlaying()) {

                MyLog.Set("d", getClass(), "正在播放音樂 音樂暫停");

                mpBackground.pause();
                isPause = true;
            } else {

                MyLog.Set("d", getClass(), "無音樂播放 回收mediaPlayer");

                mpBackground.stop();
                mpBackground.release();
                mpBackground = null;
            }

        }

        super.onPause();
    }

    private boolean isPause = false;

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        if (mpBackground != null) {

            if (isPause) {
                mpBackground.start();
            }
        }

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

                MyLog.Set("e", getClass(), "-9-9-9-9-9-9--9-9-9-9-9-9");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = null;

        if (mpBackground != null) {
            mpBackground.release();
        }
        mpBackground = null;

        mLocManager = null;
        mLocation = null;

        cancelTask(getPhotoTask);
        cancelTask(changeTask);
        cancelTask(deleteTask);
        cancelTask(afterAviaryTask);
        cancelTask(confirmTask);
        cancelTask(deleteBitmapToReFreshTask);
        cancelTask(saveToFinishTask);
        cancelTask(upLoadAudioTask);
        cancelTask(deleteAudioTask);
        cancelTask(firstCreateFreeAlbumTask);
        cancelTask(descriptionTask);
        cancelTask(sendPreviewTask);
        cancelTask(getSettingsTask);
        cancelTask(setSettingsTask);
        cancelTask(sendAudioTask);
        cancelTask(locationTask);

        Recycle.IMG(videoPlayImg);
        Recycle.IMG(photo_or_templateImg);
        Recycle.IMG(addUserImg);
//        Recycle.IMG(addDescriptionImg);
//        Recycle.IMG(descriptionDeleteImg);
        Recycle.IMG(albumSetImg);
        Recycle.IMG(refreshImg);
        Recycle.IMG(deleteImg);
        Recycle.IMG(aviaryImg);
        Recycle.IMG(addPicImg);
        Recycle.IMG(backImg);
        Recycle.IMG(audioRecordingImg);
        Recycle.IMG(audioDeleteImg);
        Recycle.IMG(audioPlayImg);

        cleanPicasso();

        adapter = null;

        Picasso.with(this).cancelRequest(target);


        if (fragmentSelectPhoto2 != null && fragmentSelectPhoto2.isAdded()) {

            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(fragmentSelectPhoto2)
                    .commitAllowingStateLoss();
            fragmentSelectPhoto2 = null;
            MyLog.Set("d", this.getClass(), "移除 => fragmentSelectPicture");
        }

        if (fragmentSelectVideo2 != null && fragmentSelectVideo2.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .remove(fragmentSelectVideo2)
                    .commitAllowingStateLoss();
            fragmentSelectVideo2 = null;
            MyLog.Set("d", this.getClass(), "移除 => fragmentSelectVideo2");
        }

        deleteUploadFile();

        File f = new File(mp3Path);
        if (f.exists()) {
            f.delete();
        }

        System.gc();

        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }

}


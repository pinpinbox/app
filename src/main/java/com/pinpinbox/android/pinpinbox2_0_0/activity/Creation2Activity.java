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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.czt.mp3recorder.MP3Recorder;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.github.ybq.android.spinkit.SpinKitView;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SampleTest.CreateAlbum.ChangeItemAdapter;
import com.pinpinbox.android.SampleTest.CreateAlbum.SelectPreviewAdapter;
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
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumSettings;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemHyperlink;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickDragDismissListener;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.GAControl;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.KeysForSKD;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.ScrollLinearLayoutManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.SnackManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
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
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogCreationLink;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogCreationLocation;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentCooperation2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMe2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMyUpLoad2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSelectAudio2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSelectPhoto2;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSelectVideo2;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.OnSpotlightEndedListener;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.OnSpotlightStartedListener;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.SimpleTarget;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.Spotlight;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol33_AlbumSettings;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol34_GetAlbumSettings;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/3/8.
 */
public class Creation2Activity extends DraggerActivity implements View.OnClickListener, LocationListener, ClickDragDismissListener.ActionUpListener {


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
    private UpLoadPageAudioTask upLoadPageAudioTask;
    private DeleteAudioTask deleteAudioTask;
    private FirstCreateFreeAlbumTask firstCreateFreeAlbumTask;
    private DescriptionTask descriptionTask;
    private LocationTask locationTask;
    private GetAlbumDataOptionsTask getAlbumDataOptionsTask;
    private Protocol33_AlbumSettings protocol33;
    private Protocol34_GetAlbumSettings protocol34;
    private SendHyperlinkTask sendHyperlinkTask;

    private FragmentSelectPhoto2 fragmentSelectPhoto2;
    private FragmentSelectVideo2 fragmentSelectVideo2;
    private FragmentSelectAudio2 fragmentSelectAudio2;

    private PopupCustom popCreationSet, popCreateSort, popCreateAdd, popCreateAudio, popSelectAudioFile;
//   popCreatePreview


    private DialogCreationLocation dlgCreationLocation;
    private DialogCreationLink dlgCreationLink;

    private ChangeItemAdapter pop_qeAdapter;
    private SelectPreviewAdapter selectPreviewAdapter;
    private RecyclerAlbumSettingsAdapter audioAdapter;

    private RecyclerCreationAdapter adapter;
    private ScrollLinearLayoutManager linearLayoutManager;


    //    private ArrayList<HashMap<String, Object>> selectingList;
    private ArrayList<HashMap<String, Object>> photoList;
    private List<ItemAlbumSettings> audioList;

    private JSONArray jsonArray;//已有相片張數
    private File downloadFile;
    private File uploadFile;//上傳的photo
    private Bitmap bmp;

    private MediaPlayer mPlayer = null, mpBackground;
    private MP3Recorder mp3Recorder;

    private String mp3Path = "";

    private String strPreviewPageNum = "";
    private String strPrefixText;
    private String strSpecialUrl;
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

    private int sendPreviewCount;


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
    private int previewMode = -1;


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
    private static final int DoSendHyperLink = 12;
    private static final int DoLocation = 14;


    private static final String NONE = "none";
    private static final String SINGULAR = "singular"; //單首
    private static final String PLURAL = "plural"; //多首
    private static final String CUSTOM = "custom"; //上傳 自定義 不是由server給予

    private static final int PREVIEW_ALL = 99;
    private static final int PREVIEW_PAGE = 98;

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
    private boolean isCustomAudio = false;


    private RelativeLayout rPlay_Delete, rBackground, rLocationDelete, rPhotoSettingBar, rAudioRecording, rLinkDelete;

    private ImageView addPicImg, addUserImg, refreshImg, backImg, videoPlayImg, albumSetImg, photo_or_templateImg;
    private ImageView selectPreviewPage, selectPreviewAll;

    private ImageView selectAudioNoneImg, selectAudioPageImg, selectAudioBackgroundImg, selectAudioCustomImg, controlImg;

    private ImageView locationImg, audioRecordingImg, audioPlayImg, linkImg, deleteImg, aviaryImg, locationDeleteImg, audioDeleteImg, linkDeleteImg;

    private TextView tvSelectAudioNone, tvSelectAudioPage, tvSelectAudioBackground, tvSelectAudioCustom, tvSelectFile, tvAudioTarget, tvSendPreview;

    private SpinKitView uploadSingularAudioLoading;

    private EditText edPreviewPageStart;

    private PinchImageView photoImg;
    private TextView tvPicCount, tvCheck, tvDescription, tvLocation, tvSelect_Photo_or_Template, tvAddDescription;
    private RecyclerView rvPhoto;

    private LinearLayout linDetail, linDetailDescription, linDetailLocation, linPreviewAll, linPreviewPage;

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
            strSpecialUrl = bundle.getString(Key.special, "");

            MyLog.Set("d", getClass(), "bundle => event_id => " + event_id);
            MyLog.Set("d", getClass(), "bundle => isContibute => " + isContribute);
            MyLog.Set("d", getClass(), "bundle => strPrefixText => " + strPrefixText);

            boolean isFromlocal = bundle.getBoolean(Key.fromlocal, false);

            if (isFromlocal) {
                isModify = true;
            }


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


        try {

            File fileAviary = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.pathName_FromAviary);
            if (fileAviary.exists()) {
                fileAviary.delete();
            }

            File fileRecord = new File(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.pathName_RecordMp3);
            if (fileRecord.exists()) {
                fileRecord.delete();
            }

        } catch (Exception e) {

            e.printStackTrace();

        }


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

        photoImg = (PinchImageView) findViewById(R.id.photoImg);

        tvPicCount = (TextView) findViewById(R.id.tvPicCount);
        tvCheck = (TextView) findViewById(R.id.tvCheck);
        tvAddDescription = (TextView) findViewById(R.id.tvAddDescription);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvLocation = (TextView) findViewById(R.id.tvLocation);


        deleteImg = (ImageView) findViewById(R.id.deleteImg);
        addPicImg = (ImageView) findViewById(R.id.addPicImg);
        aviaryImg = (ImageView) findViewById(R.id.aviaryImg);
        addUserImg = (ImageView) findViewById(R.id.addUserImg);
        refreshImg = (ImageView) findViewById(R.id.refreshImg);
        backImg = (ImageView) findViewById(R.id.backImg);
        videoPlayImg = (ImageView) findViewById(R.id.videoPlayImg);
        audioRecordingImg = (ImageView) findViewById(R.id.audioRecordingImg);
        audioPlayImg = (ImageView) findViewById(R.id.audioPlayImg);
        audioDeleteImg = (ImageView) findViewById(R.id.audioDeleteImg);
        albumSetImg = (ImageView) findViewById(R.id.albumSetImg);
        locationImg = (ImageView) findViewById(R.id.locationImg);
        locationDeleteImg = (ImageView) findViewById(R.id.locationDeleteImg);
        linkDeleteImg = (ImageView) findViewById(R.id.linkDeleteImg);
        linkImg = (ImageView) findViewById(R.id.linkImg);

        rippleBackgroundRecording = (RippleBackground) findViewById(R.id.rippleRecording);
        rippleBackgroundPlay = (RippleBackground) findViewById(R.id.ripplePlay);


        rBackground = (RelativeLayout) findViewById(R.id.rBackground);
        rPlay_Delete = (RelativeLayout) findViewById(R.id.rPlay_Delete);
        rAudioRecording = (RelativeLayout) findViewById(R.id.rAudioRecording);
        rPhotoSettingBar = (RelativeLayout) findViewById(R.id.rPhotoSettingBar);
        rLocationDelete = (RelativeLayout) findViewById(R.id.rLocationDelete);
        rLinkDelete = (RelativeLayout) findViewById(R.id.rLinkDelete);


        /*2016.08.30新增*/
        linDetail = (LinearLayout) findViewById(R.id.linDetail);
        linDetailDescription = (LinearLayout) findViewById(R.id.linDetailDescription);
        linDetailLocation = (LinearLayout) findViewById(R.id.linDetailLocation);

        setAllPop();

        tvCheck.setOnClickListener(this);
        refreshImg.setOnClickListener(this);
        albumSetImg.setOnClickListener(this);

        TextUtility.setBold(tvCheck, true);


//        if (SystemUtility.getSystemVersion() < Build.VERSION_CODES.O) {
//            aviaryImg.setVisibility(View.VISIBLE);
//        } else {
//            if (BuildConfig.FLAVOR.equals("w3_private")) {
//                aviaryImg.setVisibility(View.VISIBLE);
//            } else {
//                aviaryImg.setVisibility(View.GONE);
//            }
//        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private void setAllPop() {

        /**/
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


        /**/
        popCreationSet = new PopupCustom(mActivity);
        popCreationSet.setPopup(R.layout.pop_2_0_0_creation_set, R.style.pinpinbox_popupAnimation_bottom);
        TextView tvSort = (TextView) popCreationSet.getPopupView().findViewById(R.id.tvSort);
        TextView tvSetAudio = (TextView) popCreationSet.getPopupView().findViewById(R.id.tvSetAudio);

        TextUtility.setBold(
                tvSort,
                tvSetAudio,
                (TextView) popCreationSet.getPopupView().findViewById(R.id.tvTitle),
                (TextView) popCreationSet.getPopupView().findViewById(R.id.tvPreview),
                (TextView) popCreationSet.getPopupView().findViewById(R.id.tvPreviewAll),
                (TextView) popCreationSet.getPopupView().findViewById(R.id.tv1),
                (TextView) popCreationSet.getPopupView().findViewById(R.id.tv2)
        );


        selectPreviewPage = (ImageView) popCreationSet.getPopupView().findViewById(R.id.selectPreviewPage);
        selectPreviewAll = (ImageView) popCreationSet.getPopupView().findViewById(R.id.selectPreviewAll);
        linPreviewPage = (LinearLayout) popCreationSet.getPopupView().findViewById(R.id.linPreviewPage);
        linPreviewAll = (LinearLayout) popCreationSet.getPopupView().findViewById(R.id.linPreviewAll);


        View vContentSet = popCreationSet.getPopupView().findViewById(R.id.linBackground);
        tvSort.setOnTouchListener(new ClickDragDismissListener(vContentSet, this));
        tvSetAudio.setOnTouchListener(new ClickDragDismissListener(vContentSet, this));
        linPreviewPage.setOnTouchListener(new ClickDragDismissListener(vContentSet, this));
        linPreviewAll.setOnTouchListener(new ClickDragDismissListener(vContentSet, this));
        ((RelativeLayout) popCreationSet.getPopupView().findViewById(R.id.r1)).setOnTouchListener(new ClickDragDismissListener(vContentSet, this));


        tvSendPreview = (TextView) popCreationSet.getPopupView().findViewById(R.id.tvSendPreview);
        tvSendPreview.setOnTouchListener(new ClickDragDismissListener(vContentSet, this));

        edPreviewPageStart = (EditText) popCreationSet.getPopupView().findViewById(R.id.edPreviewPageStart);
        edPreviewPageStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvSendPreview.setVisibility(View.VISIBLE);

            }
        });



        /**/
        popCreateAdd = new PopupCustom(mActivity);
        popCreateAdd.setPopup(R.layout.pop_2_0_0_creation_add, R.style.pinpinbox_popupAnimation_bottom);
        LinearLayout linAddPhoto = (LinearLayout) popCreateAdd.getPopupView().findViewById(R.id.linAddPhoto);
        LinearLayout linAddVideo = (LinearLayout) popCreateAdd.getPopupView().findViewById(R.id.linAddVideo);
        tvSelect_Photo_or_Template = (TextView) popCreateAdd.getPopupView().findViewById(R.id.tvSelect_Photo_or_Template);
        photo_or_templateImg = (ImageView) popCreateAdd.getPopupView().findViewById(R.id.photo_or_templateImg);

        TextUtility.setBold(tvSelect_Photo_or_Template, true);
        TextUtility.setBold((TextView) popCreateAdd.getPopupView().findViewById(R.id.tvVideo), true);
        TextUtility.setBold((TextView) popCreateAdd.getPopupView().findViewById(R.id.tvTitle), true);

        View vContentAdd = popCreateAdd.getPopupView().findViewById(R.id.linBackground);

        linAddPhoto.setOnTouchListener(new ClickDragDismissListener(vContentAdd, this));
        linAddVideo.setOnTouchListener(new ClickDragDismissListener(vContentAdd, this));




        /**/
        popCreateAudio = new PopupCustom(mActivity);
        popCreateAudio.setPopup(R.layout.pop_2_0_0_creation_audio, R.style.pinpinbox_popupAnimation_bottom);
        (popCreateAudio.getPopupView().findViewById(R.id.tvSave)).setOnClickListener(this);


        selectAudioNoneImg = popCreateAudio.getPopupView().findViewById(R.id.selectAudioNoneImg);
        selectAudioPageImg = popCreateAudio.getPopupView().findViewById(R.id.selectAudioPageImg);
        selectAudioBackgroundImg = popCreateAudio.getPopupView().findViewById(R.id.selectAudioBackgroundImg);
        selectAudioCustomImg = popCreateAudio.getPopupView().findViewById(R.id.selectAudioCustomImg);
        controlImg = popCreateAudio.getPopupView().findViewById(R.id.controlImg);

        tvSelectAudioNone = popCreateAudio.getPopupView().findViewById(R.id.tvSelectAudioNone);
        tvSelectAudioPage = popCreateAudio.getPopupView().findViewById(R.id.tvSelectAudioPage);
        tvSelectAudioBackground = popCreateAudio.getPopupView().findViewById(R.id.tvSelectAudioBackground);
        tvSelectAudioCustom = popCreateAudio.getPopupView().findViewById(R.id.tvSelectAudioCustom);
        tvSelectFile = popCreateAudio.getPopupView().findViewById(R.id.tvSelectFile);
        tvAudioTarget = popCreateAudio.getPopupView().findViewById(R.id.tvAudioTarget);

        uploadSingularAudioLoading = popCreateAudio.getPopupView().findViewById(R.id.loadingView);


        TextUtility.setBold(tvSelectAudioNone, tvSelectAudioPage, tvSelectAudioBackground, tvSelectAudioCustom,
                (TextView) popCreateAudio.getPopupView().findViewById(R.id.tvTitle),
                (TextView) popCreateAudio.getPopupView().findViewById(R.id.tvSave));


        linearLayoutManager = new ScrollLinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvAudio = popCreateAudio.getPopupView().findViewById(R.id.rvAudio);
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
        selectAudioCustomImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAudioMode(CUSTOM);
            }
        });


        tvSelectFile.setOnClickListener(this);
        controlImg.setOnClickListener(this);


        audioAdapter = new RecyclerAlbumSettingsAdapter(mActivity, audioList, true);
        rvAudio.setAdapter(audioAdapter);

        audioAdapter.setOnRecyclerViewListener(new RecyclerAlbumSettingsAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(final int position, View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                if (!mySelectAudioMode.equals(SINGULAR) && rvAudio.getAlpha() == 0.2f) {
                    return;
                }

                if (rvAudio.getAlpha() != 1f) {
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
                selectAudioCustomImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

                tvSelectAudioNone.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                tvSelectAudioPage.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioBackground.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioCustom.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

                linearLayoutManager.setScrollEnabled(false);

                rvAudio.setAlpha(0.2f);
                tvSelectFile.setAlpha(0.2f);

                tvSelectFile.setClickable(false);
                controlImg.setClickable(false);

                mySelectAudioMode = NONE;

                if (mpBackground != null && mpBackground.isPlaying()) {
                    mpBackground.stop();
                }

                break;

            case PLURAL://多首
                selectAudioNoneImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioPageImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
                selectAudioBackgroundImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioCustomImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

                tvSelectAudioNone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioPage.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                tvSelectAudioBackground.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioCustom.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

                linearLayoutManager.setScrollEnabled(false);

                rvAudio.setAlpha(0.2f);
                tvSelectFile.setAlpha(0.2f);

                tvSelectFile.setClickable(false);
                controlImg.setClickable(false);

                mySelectAudioMode = PLURAL;

                if (mpBackground != null && mpBackground.isPlaying()) {
                    mpBackground.stop();
                }

                break;

            case SINGULAR://單首
                selectAudioNoneImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioPageImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioBackgroundImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
                selectAudioCustomImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);

                tvSelectAudioNone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioPage.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioBackground.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                tvSelectAudioCustom.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

                linearLayoutManager.setScrollEnabled(true);

                rvAudio.setAlpha(1f);
                tvSelectFile.setAlpha(0.2f);

                tvSelectFile.setClickable(false);
                controlImg.setClickable(false);

                mySelectAudioMode = SINGULAR;

                isCustomAudio = false;

                break;

            case CUSTOM://上傳

                selectAudioNoneImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioPageImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioBackgroundImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
                selectAudioCustomImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);

                tvSelectAudioNone.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioPage.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioBackground.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                tvSelectAudioCustom.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));


                linearLayoutManager.setScrollEnabled(false);

                rvAudio.setAlpha(0.2f);
                tvSelectFile.setAlpha(1f);

                tvSelectFile.setClickable(true);
                controlImg.setClickable(true);

                mySelectAudioMode = SINGULAR; //童單首

                isCustomAudio = true;

                if (mpBackground != null && mpBackground.isPlaying()) {
                    mpBackground.stop();
                }

                break;


        }

    }

    private void clickListener() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addPicImg.setOnClickListener(Creation2Activity.this);
            }
        }, 400);


        addUserImg.setOnClickListener(this);
        aviaryImg.setOnClickListener(this);
        deleteImg.setOnClickListener(this);
        videoPlayImg.setOnClickListener(this);
        tvAddDescription.setOnClickListener(this);

        audioRecordingImg.setOnClickListener(this);
        audioPlayImg.setOnClickListener(this);
        audioDeleteImg.setOnClickListener(this);

        locationImg.setOnClickListener(this);
        locationDeleteImg.setOnClickListener(this);

        linkImg.setOnClickListener(this);
        linkDeleteImg.setOnClickListener(this);


    }

    /*添加新一頁*/
    private void addPictureClick() {
        /*選取已達上限*/
        if (jsonArray.length() >= intUserGrade) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_count_max);
            return;
        }
        SDpermissionEnable();
    }

    /*新增用戶*/
    private void addUserClick() {
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

    /*濾鏡*/
    private void aviaryClick() {
        if (jsonArray.length() != 0) {

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

    /*刪除當前照片*/
    private void deleteClick() {
        if (jsonArray.length() != 0) {

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

    /*播放影片*/
    private void playVideoClick() {

        ActivityIntent.toVideoPlay(mActivity, videoUrl);
    }

    /*單頁說明*/
    private void addDescriptionClick() {

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

    /*錄音*/
    @SuppressLint("WrongConstant")
    private void recordingClick() {

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
                    doSendAudioMode();
                }
            });
            d.show();

            return;
        }


        if (mp3Recorder == null) {


            if (popSelectAudioFile == null) {


                popSelectAudioFile = new PopupCustom(mActivity);
                popSelectAudioFile.setPopup(R.layout.pop_2_0_0_upload_audio, R.style.pinpinbox_popupAnimation_bottom);


                TextView tvRecording = popSelectAudioFile.getPopupView().findViewById(R.id.tvRecording);
                TextView tvSelectAudioFile = popSelectAudioFile.getPopupView().findViewById(R.id.tvSelectAudioFile);


                TextUtility.setBold(tvRecording, tvSelectAudioFile, (TextView) popSelectAudioFile.getPopupView().findViewById(R.id.tvTitle));


                View vContents = popSelectAudioFile.getPopupView().findViewById(R.id.linBackground);

                tvRecording.setOnTouchListener(new ClickDragDismissListener(vContents, Creation2Activity.this));
                tvSelectAudioFile.setOnTouchListener(new ClickDragDismissListener(vContents, Creation2Activity.this));

            }

            popSelectAudioFile.show(rBackground);

        } else {

            if (mp3Recorder.isRecording()) {
                mp3Recorder.stop();
                mp3Recorder = null;
                rippleBackgroundRecording.stopRippleAnimation();
                rippleBackgroundRecording.setVisibility(View.INVISIBLE);

                setClickable(true);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doUploadPageAudio();

                    }
                }, 300);//500

            }

        }

    }

    private void checkRecorder() {

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

    }

    private void record() {

        /*畫面禁止點擊    開始錄音*/
        setClickable(false);


        mp3Path = DirClass.ExternalFileDir(mActivity) + PPBApplication.getInstance().getMyDir() + DirClass.pathName_RecordMp3;

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
            audioRecordingImg.setImageResource(R.drawable.ic200_recording_white);

            rippleBackgroundRecording.startRippleAnimation();
            rippleBackgroundRecording.setVisibility(View.VISIBLE);


            rPlay_Delete.setVisibility(View.GONE);

        } catch (IOException e) {
            if (mp3Recorder != null) {
                mp3Recorder.stop();
                mp3Recorder = null;
                rippleBackgroundRecording.stopRippleAnimation();
                rippleBackgroundRecording.setVisibility(View.INVISIBLE);
            }
            setClickable(true);
            e.printStackTrace();
        }


    }

    private void resetRecordingImg() {

        audioRecordingImg.setImageResource(R.drawable.ic200_micro_white);

    }


    /*播放錄音*/
    private void playAudioClick() {

        if (mp3Recorder != null && mp3Recorder.isRecording()) {
            PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_audio_recording);
            return;
        }

        /*單獨添加*/
        refreshImg.setClickable(false);

        /*畫面禁止點擊*/
        setClickable(false);

        if (mPlayer != null && mPlayer.isPlaying()) {
            cleanMp3();
            rippleBackgroundPlay.stopRippleAnimation();
            rippleBackgroundPlay.setVisibility(View.INVISIBLE);

            refreshImg.setClickable(true);
            setClickable(true);
            return;
        }

        if (mPlayer == null) {


            rippleBackgroundPlay.startRippleAnimation();
            rippleBackgroundPlay.setVisibility(View.VISIBLE);
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
                                rippleBackgroundPlay.setVisibility(View.INVISIBLE);
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
                                rippleBackgroundPlay.setVisibility(View.INVISIBLE);
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

    /*刪除錄音*/
    private void deleteAudioClick() {
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

    /*單頁地點*/
    private void addLocationClick() {
        if (!checkMyPhoto(thisPosition)) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
            return;
        }

        if (dlgCreationLocation == null) {

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

    /*刪除地點*/
    private void deleteLocationClick() {
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

    /*圖片連結*/
    private void addlinkClick() {

        if (!checkMyPhoto(thisPosition)) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
            return;
        }

        buildDialogHyperLink();


        String hyperlink = (String) photoList.get(thisPosition).get(Key.hyperlink);

        List<ItemHyperlink> itemHyperlinkList = new ArrayList<>();

        try {

            if (hyperlink != null && !hyperlink.equals("") && !hyperlink.equals("null")) {

                JSONArray jsonHyperlinkArray = new JSONArray(hyperlink);

                if (jsonHyperlinkArray.length() > 0) {

                    for (int i = 0; i < jsonHyperlinkArray.length(); i++) {

                        JSONObject obj = (JSONObject) jsonHyperlinkArray.get(i);

                        ItemHyperlink itemHyperlink = new ItemHyperlink();

                        itemHyperlink.setText(JsonUtility.GetString(obj, ProtocolKey.text));
                        itemHyperlink.setUrl(JsonUtility.GetString(obj, ProtocolKey.url));

                        itemHyperlinkList.add(itemHyperlink);

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (itemHyperlinkList.size() > 0) {

            for (int i = 0; i < itemHyperlinkList.size(); i++) {

                String name = itemHyperlinkList.get(i).getText();

                String url = itemHyperlinkList.get(i).getUrl();


                switch (i) {

                    case 0:

                        dlgCreationLink.getEdLinkName1().setText(name);
                        dlgCreationLink.getEdLinkUrl1().setText(url);


                        break;

                    case 1:

                        dlgCreationLink.getEdLinkName2().setText(name);
                        dlgCreationLink.getEdLinkUrl2().setText(url);

                        break;

                }

            }

        } else {

            dlgCreationLink.getEdLinkName1().setText("");
            dlgCreationLink.getEdLinkUrl1().setText("");
            dlgCreationLink.getEdLinkName2().setText("");
            dlgCreationLink.getEdLinkUrl2().setText("");

        }


        dlgCreationLink.getDialog().show();


    }

    /*刪除連結*/
    private void deleteLinkClick() {

        if (!checkMyPhoto(thisPosition)) {
            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deal_with_your_uploaded_items);
            return;
        }

        buildDialogHyperLink();


        DialogV2Custom d = new DialogV2Custom(mActivity);
        d.setStyle(DialogStyleClass.CHECK);
        d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_delete);
        d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_delete_hyperlink);
        d.setCheckExecute(new CheckExecute() {
            @Override
            public void DoCheck() {

                dlgCreationLink.getEdLinkName1().setText("");
                dlgCreationLink.getEdLinkName2().setText("");
                dlgCreationLink.getEdLinkUrl1().setText("");
                dlgCreationLink.getEdLinkUrl2().setText("");


                doSendHyperLink();
            }
        });
        d.show();


    }

    private void buildDialogHyperLink() {
        if (dlgCreationLink == null) {
            dlgCreationLink = new DialogCreationLink(mActivity);
            dlgCreationLink.getConfirmImg().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlgCreationLink.getDialog().dismiss();
                    doSendHyperLink();
                }
            });
        }
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


    private void confirmPreviewClick() {

        if (previewMode == -1) {

            PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_select_preview_way);

        } else if (previewMode == PREVIEW_ALL) {

            doSendPreview();

        } else {

            if (photoList != null && photoList.size() == 0) {
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_content_is_null);
                return;
            }

            if (edPreviewPageStart.getText().toString() == null || edPreviewPageStart.getText().toString().equals("")) {
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_type_the_number_you_want_to_be_visible);
                return;
            }

            sendPreviewCount = StringIntMethod.StringToInt(edPreviewPageStart.getText().toString());

            if (sendPreviewCount < 1) {

                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_at_least_1_page);

            } else if (sendPreviewCount > photoList.size()) {

                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_can_not_larger_than_the_total);

            } else {

                doSendPreview();

            }

        }

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

            SnackManager.showCollecttionSnack();

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

    private void checkHyperLink(int position) {

        String strHyperlink = (String) photoList.get(position).get(Key.hyperlink);

        if (strHyperlink == null || strHyperlink.equals("null") || strHyperlink.equals("")) {

            rLinkDelete.setVisibility(View.GONE);

        } else {

            boolean isVisible = false;

            try {

                JSONArray jsonHyperlinkArray = new JSONArray(strHyperlink);

                if (jsonHyperlinkArray.length() > 0) {

                    for (int i = 0; i < jsonHyperlinkArray.length(); i++) {

                        JSONObject obj = (JSONObject) jsonHyperlinkArray.get(i);

                        String text = JsonUtility.GetString(obj, ProtocolKey.text);
                        String url = JsonUtility.GetString(obj, ProtocolKey.url);

                        if (!text.equals("") || !url.equals("")) {
                            isVisible = true;
                            break;
                        } else {
                            isVisible = false;
                        }

                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            if (isVisible) {
                rLinkDelete.setVisibility(View.VISIBLE);
            } else {
                rLinkDelete.setVisibility(View.GONE);
            }


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

//            if (SystemUtility.getSystemVersion() < Build.VERSION_CODES.O) {
            aviaryImg.setVisibility(View.VISIBLE);
//            }

            videoPlayImg.setVisibility(View.GONE);

            audioUrl = (String) photoList.get(position).get("audio_url");
            if (audioUrl == null || audioUrl.equals("null") || audioUrl.equals("")) {
                resetRecordingImg();

                rAudioRecording.setVisibility(View.VISIBLE);


                rPlay_Delete.setVisibility(View.GONE);
            } else {
                //有錄音 可播放

                rAudioRecording.setVisibility(View.GONE);
                rPlay_Delete.setVisibility(View.VISIBLE);
            }

        } else {
            /*此頁為影片 隱藏錄音，特效選項*/

            rAudioRecording.setVisibility(View.GONE);
            rPlay_Delete.setVisibility(View.GONE);
//            if (SystemUtility.getSystemVersion() < Build.VERSION_CODES.O) {
            aviaryImg.setVisibility(View.GONE);
//            }

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

            HashMap<String, Object> mp = photoList.get(lastposition);

            if (mp.containsKey(Key.select)) {
                mp.remove(Key.select);
                mp.put(Key.select, false);
                photoList.remove(lastposition);
                photoList.add(lastposition, mp);
            }


        }

        HashMap<String, Object> mp = photoList.get(thisposition);

        if (mp.containsKey(Key.select)) {
            mp.remove(Key.select);
            mp.put(Key.select, true);
            photoList.remove(thisposition);
            photoList.add(thisposition, mp);
        }


    }


    private void deleteDownloadFile() {
        if (downloadFile != null && downloadFile.exists()) {
            downloadFile.delete();
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


        Uri fromUri = null;


        if (SystemUtility.getSystemVersion() >= Build.VERSION_CODES.N) {

            MyLog.Set("e", getClass(), "11111111111");

            fromUri = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", downloadFile);
        } else {
            MyLog.Set("e", getClass(), "22222222222");
            fromUri = Uri.fromFile(downloadFile);
        }


        File editDirFile = new File(DirClass.sdPath + "pinpinbox_edit/");
        if (!editDirFile.exists()) {
            editDirFile.mkdirs();
            MyLog.Set("e", getClass(), "3333333333333");
        }

        MyLog.Set("e", getClass(), "editDirFile => " + editDirFile.getAbsolutePath());


        Intent intent = new Intent(this, DsPhotoEditorActivity.class);
        intent.setData(fromUri);
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.setFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
//        intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);


        intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_API_KEY, KeysForSKD.DSPHOTOEDITOR());
        intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "pinpinbox_edit");


        if (fragmentSelectPhoto2 != null) {
            fragmentSelectPhoto2.setFromPPBCamera(true);
        }

        startActivityForResult(intent, ACTION_REQUEST_FEATHER);

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

                String hyperlink = JsonUtility.GetString(obj, ProtocolKey.hyperlink);


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
                                + "user_id => " + user_id + "\n"
                                + "hyperlink => " + hyperlink
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

                map.put(Key.hyperlink, hyperlink);

                photoList.add(map);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setChangeProject() {

        checkCount();
        checkLocation(thisPosition);
        checkHyperLink(thisPosition);
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

                        getdata.edit().putBoolean(TaskKeyClass.first_to_creation, true).commit();
                        MyLog.Set("d", mActivity.getClass(), "save first_to_creation");


                        SDpermissionEnable();


                        addPicImg.setClickable(true);
                        addUserImg.setClickable(true);
                        albumSetImg.setClickable(true);


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
            bundle.putString(Key.special, strSpecialUrl);
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
                        doUploadPageAudio();
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

                    case DoSendHyperLink:
                        doSendHyperLink();
                        break;

                    case DoingTypeClass.DoGetSettings:
                        doGetAlbumDataOptions();
                        break;

                    case DoingTypeClass.DoSetSettings:
                        doSetSettings();
                        break;

                    case DoingTypeClass.DoSendAudioMode:
                        doSendAudioMode();
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

    private void doUploadPageAudio() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        upLoadPageAudioTask = new UpLoadPageAudioTask();
        upLoadPageAudioTask.execute();
    }

    private void doUploadAlbumAudio(final String mp3Path) {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        this.mp3Path = mp3Path;

        JSONObject object = new JSONObject();

        try {

            object.put(ProtocolKey.album_audio_refer, "file");

            object.put(ProtocolKey.album_audio_mode, SINGULAR);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String settings = object.toString();

        File file = new File(mp3Path);

        protocol33 = new Protocol33_AlbumSettings(
                mActivity,
                id,
                token,
                album_id,
                settings,
                file,
                new Protocol33_AlbumSettings.TaskCallBack() {

                    @Override
                    public void Prepare() {

                        uploadSingularAudioLoading.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void Post() {

                        uploadSingularAudioLoading.setVisibility(View.GONE);

                    }

                    @Override
                    public void Success() {

                        PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_upload_success);

                        doSetSettings();


                    }

                    @Override
                    public void TimeOut() {
                        doUploadAlbumAudio(mp3Path);
                    }
                }

        );

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

        String idlist = "";
        if (previewMode == PREVIEW_ALL) {
            for (int i = 0; i < photoList.size(); i++) {
                if (i == 0) {
                    idlist = photoList.get(i).get(Key.photo_id) + "";
                } else {
                    idlist = idlist + "," + photoList.get(i).get(Key.photo_id);
                }
            }
        } else {
            for (int i = 0; i < sendPreviewCount; i++) {
                if (i == 0) {
                    idlist = photoList.get(i).get(Key.photo_id) + "";
                } else {
                    idlist = idlist + "," + photoList.get(i).get(Key.photo_id);
                }
            }
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put(ProtocolKey.album_preview, idlist);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyLog.Set("e", mActivity.getClass(), "idlist => " + idlist);

        protocol33 = new Protocol33_AlbumSettings(
                mActivity,
                id,
                token,
                album_id,
                obj.toString(),
                new Protocol33_AlbumSettings.TaskCallBack() {

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
                        isModify = true;
                        photoPreviewType = false;
                        PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_edit_finish);
                        tvSendPreview.setVisibility(View.GONE);
                    }

                    @Override
                    public void TimeOut() {
                        doSendPreview();
                    }


                }
        );


    }

    private void doGetAlbumDataOptions() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        getAlbumDataOptionsTask = new GetAlbumDataOptionsTask();
        getAlbumDataOptionsTask.execute();

    }

    private void doSetSettings() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }


        protocol34 = new Protocol34_GetAlbumSettings(
                mActivity,
                id,
                token,
                album_id,
                new Protocol34_GetAlbumSettings.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }


                    @Override
                    public void Success(ItemAlbum itemAlbum) {
                        setAudio(itemAlbum);
                    }

                    @Override
                    public void TimeOut() {
                        doSetSettings();
                    }

                    private void setAudio(ItemAlbum itemAlbum) {

//                        audio_mode (enum, 相本音訊模式, none: 無 / singular: 單首 / plural: 多首)
//                        audio_refer (enum, 相本音訊來源, embed: 嵌入 / file: 檔案 / system: 系統)
//                        audio_target (int / string, 相本音訊位置,
//                                     audio_refer 為 embed 時為嵌入代碼;
//                                     audio_refer 為 file 時為接收檔案;
//                                     audio_refer 為 system 時為 int id)

                        MyLog.Set("e", Creation2Activity.this.getClass(), "--------------------" + itemAlbum.getAudio_mode());
                        MyLog.Set("e", Creation2Activity.this.getClass(), "--------------------" + itemAlbum.getAudio_refer());
                        MyLog.Set("e", Creation2Activity.this.getClass(), "--------------------" + itemAlbum.getAudio_target());

                        if (itemAlbum.getAudio_target() != null && !itemAlbum.getAudio_target().equals("")) {

                            for (int i = 0; i < audioList.size(); i++) {
                                if (itemAlbum.getAudio_target().equals(StringIntMethod.IntToString(audioList.get(i).getId()))) {
                                    audioList.get(i).setSelect(true);
                                    audioAdapter.notifyItemChanged(i);
                                    rvAudio.scrollToPosition(i);
                                    break;
                                }
                            }

                            //currentAudioMode從57接口或取 這裡不需套用

                            if (itemAlbum.getAudio_refer().equals("file")) {

                                tvAudioTarget.setText(R.string.pinpinbox_2_0_0_other_text_upload_already);

                                setAudioMode(CUSTOM);
                            } else {

                                setAudioMode(currentAudioMode);

                            }

                        }

                    }

                }
        );

    }

    private void doSendAudioMode() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        JSONObject object = new JSONObject();

        try {
            object.put(ProtocolKey.album_audio_mode, mySelectAudioMode);

            if (mySelectAudioMode.equals(SINGULAR) && !isCustomAudio)

                for (int i = 0; i < audioList.size(); i++) {
                    if (audioList.get(i).isSelect()) {
                        object.put(ProtocolKey.audio, audioList.get(i).getId());
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

        protocol33 = new Protocol33_AlbumSettings(
                mActivity,
                id,
                token,
                album_id,
                object.toString(),
                new Protocol33_AlbumSettings.TaskCallBack() {
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

                        isModify = true;

                        if (!mySelectAudioMode.equals(currentAudioMode)) {
                            PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_mode_is_change);
                        }

                        currentAudioMode = mySelectAudioMode;

                        popCreateAudio.dismiss();

                        doDeleteBitmapToRefresh();

                    }

                    @Override
                    public void TimeOut() {
                        doSendAudioMode();
                    }
                }
        );


    }

    private void doSendHyperLink() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        JSONArray array = null;

        try {

            JSONObject obj1 = new JSONObject();
            obj1.put("text", dlgCreationLink.getEdLinkName1().getText().toString());
            obj1.put("url", dlgCreationLink.getEdLinkUrl1().getText().toString());

            JSONObject obj2 = new JSONObject();
            obj2.put("text", dlgCreationLink.getEdLinkName2().getText().toString());
            obj2.put("url", dlgCreationLink.getEdLinkUrl2().getText().toString());

            array = new JSONArray();
            array.put(obj1);
            array.put(obj2);


        } catch (Exception e) {
            e.printStackTrace();
        }


        sendHyperlinkTask = new SendHyperlinkTask(array.toString());
        sendHyperlinkTask.execute();

    }


    /*protocol32*/
    @SuppressLint("StaticFieldLeak")
    private class GetAlbumDataOptionsTask extends AsyncTask<Void, Void, Object> {

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

    /*protocol55*/
    @SuppressLint("StaticFieldLeak")
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

    /*protocol55*/
    @SuppressLint("StaticFieldLeak")
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

            protocol33 = new Protocol33_AlbumSettings(
                    mActivity,
                    id,
                    token,
                    album_id,
                    settings,
                    new Protocol33_AlbumSettings.TaskCallBack() {

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
    @SuppressLint("StaticFieldLeak")
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
                        strPreviewPageNum = JsonUtility.GetString(jsonAlbum, ProtocolKey.preview_page_num);

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


                firstInto();


                setPreviewCout();


                int array = jsonArray.length();

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

        private void firstInto() {

            if (bFirstInto) {

                MyLog.Set("d", mActivity.getClass(), "第一次進入");

                initView();
                setCount();
                clickListener();

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

        }

        private void setPreviewCout() {

            if (isNewCreate || strPreviewPageNum.equals("0") || strPreviewPageNum.equals("")) {
                selectPreviewAll();
            } else {

                if((StringIntMethod.StringToInt(strPreviewPageNum)==photoList.size())){
                    selectPreviewAll();
                }else {
                    selectPreviewPage();

                }


            }
        }


    }

    @SuppressLint("StaticFieldLeak")
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
                deleteDownloadFile();
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
    @SuppressLint("StaticFieldLeak")
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

                    p59Result = jsonObject.getString(ProtocolKey.result);
                    if (p59Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                        if (bmp != null && !bmp.isRecycled()) {
                            bmp.recycle();
                            bmp = null;
                            System.gc();
                        }

                    } else if (p59Result.equals("0")) {
                        p59Message = jsonObject.getString(ProtocolKey.message);
                    } else {
                        p59Result = "";
                    }


                } catch (Exception e) {
                    p59Result = "";
                    deleteDownloadFile();
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            deleteDownloadFile();

            if (p59Result != null) {
                if (p59Result.equals("1")) {

                    isModify = true; //已修改內容
                    setChangeProject();
                    lastPosition = thisPosition;
                    FlurryUtil.onEvent(FlurryKey.creation_use_adobe);

                    if (uploadFile != null && uploadFile.exists()) {
                        uploadFile.delete();
                    }

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
    @SuppressLint("StaticFieldLeak")
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
    @SuppressLint("StaticFieldLeak")
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
    @SuppressLint("StaticFieldLeak")
    private class UpLoadPageAudioTask extends AsyncTask<Void, Void, Object> {

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


                rAudioRecording.setVisibility(View.GONE);
                rPlay_Delete.setVisibility(View.VISIBLE);

                if (!currentAudioMode.equals(PLURAL)) {
                    setAudioMode(PLURAL);
                    doSendAudioMode();
                }


            } else if (p78Result.equals("0")) {
                resetRecordingImg();

                DialogV2Custom.BuildError(mActivity, p78Message);

            } else if (p78Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                resetRecordingImg();

                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }
        }
    }

    /*protocol79*/
    @SuppressLint("StaticFieldLeak")
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

                resetRecordingImg();

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
    @SuppressLint("StaticFieldLeak")
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
    @SuppressLint("StaticFieldLeak")
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
                    deleteDownloadFile();
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
    @SuppressLint("StaticFieldLeak")
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
                    deleteDownloadFile();
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

    /*新增刪除連結都使用protocol59*/
    @SuppressLint("StaticFieldLeak")
    private class SendHyperlinkTask extends AsyncTask<Void, Void, Object> {

        private String hyperlink;

        public SendHyperlinkTask(String hyperlink) {
            this.hyperlink = hyperlink;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoSendHyperLink;
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String photo_id = (String) photoList.get(thisPosition).get("photo_id");

            MyLog.Set("d", getClass(), "allready hyperlink => " + hyperlink);

            Map<String, String> data = new HashMap<>();
            data.put("id", id);
            data.put("token", token);
            data.put("album_id", album_id);
            data.put("photo_id", photo_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("album_id", album_id);
            sendData.put("photo_id", photo_id);
            sendData.put("settings[hyperlink]", hyperlink);
            sendData.put("sign", sign);

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P59_UpDatePhotoOfDiy, sendData, null);
                MyLog.Set("d", getClass(), "p59strJson by settings hyperlink => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p59Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {

                    JSONObject jsonObject = new JSONObject(strJson);

                    p59Result = jsonObject.getString(ProtocolKey.result);
                    if (p59Result.equals("1")) {

                        getJsonArray_setBottomList(jsonObject);

                    } else if (p59Result.equals("0")) {
                        p59Message = jsonObject.getString(ProtocolKey.message);
                    } else {
                        p59Result = "";
                    }

                } catch (Exception e) {
                    p59Result = "";
                    deleteDownloadFile();
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

    @SuppressLint("StaticFieldLeak")
    private class BeforeAviaryTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                downloadFile = FileUtility.createAviaryFile(mActivity, id);
            } catch (Exception e) {
                MyLog.Set("e", getClass(), "建立文件失敗");
                e.printStackTrace();
            }

            HttpUtility hu = new HttpUtility();
            hu.downPic((String) photoList.get(lastPosition).get("image_url"),
                    DirClass.pathName_FromAviary,
                    DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id),
                    getApplication().getApplicationContext());

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

        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        cleanPicasso();

        switch (view.getId()) {


            case R.id.addUserImg:
                addUserClick();
                break;

            case R.id.addPicImg:
                addPictureClick();
                break;

            case R.id.aviaryImg:
                aviaryClick();
                break;

            case R.id.deleteImg:
                deleteClick();
                break;

            case R.id.videoPlayImg:
                playVideoClick();
                break;

            case R.id.tvAddDescription:
                addDescriptionClick();
                break;

            case R.id.audioRecordingImg:
                recordingClick();
                break;

            case R.id.audioPlayImg:
                playAudioClick();
                break;

            case R.id.audioDeleteImg:
                deleteAudioClick();
                break;

            case R.id.locationImg:
                addLocationClick();
                break;

            case R.id.locationDeleteImg:
                deleteLocationClick();
                break;

            case R.id.linkImg:
                addlinkClick();
                break;

            case R.id.linkDeleteImg:
                deleteLinkClick();
                break;


            case R.id.tvCheck:
                sendAlbumCheck();
                break;

            case R.id.refreshImg:
                reFreshClick();
                break;

            case R.id.albumSetImg:

                if (!identity.equals("admin")) {
                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_deficiency_identity);
                    return;
                }

                if (photoList.size() == 0) {

                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_album_content_is_null);

                    return;

                }

                popCreationSet.show(rBackground);

                doGetAlbumDataOptions();


                break;


            /*pop creation audio*/
            case R.id.tvSave:

                if (mySelectAudioMode.equals(currentAudioMode)) {
                    popCreateAudio.dismiss();
                    doSendAudioMode();
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
                        doSendAudioMode();
                    }
                });
                d.show();

                break;

            case R.id.tvSelectFile:

                popCreateAudio.dismiss();

                openFragmentSelectAudio(SINGULAR);


                break;

            case R.id.controlImg:


                break;


            /*pop creation sort*/
            case R.id.tvSortConfirm:
                popCreateSort.dismiss();
                break;

        }

    }

    private void selectPreviewPage() {
        previewMode = PREVIEW_PAGE;
        selectPreviewPage.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        selectPreviewAll.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        linPreviewAll.setAlpha(0.2f);
        linPreviewPage.setAlpha(1f);
        edPreviewPageStart.setEnabled(true);

        if (edPreviewPageStart.getText().toString().equals("0")) {
            edPreviewPageStart.setText("1");
        }else {
            edPreviewPageStart.setText(strPreviewPageNum);
        }



    }

    private void selectPreviewAll() {
        previewMode = PREVIEW_ALL;
        selectPreviewPage.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        selectPreviewAll.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        linPreviewAll.setAlpha(1f);
        linPreviewPage.setAlpha(0.2f);
        edPreviewPageStart.setEnabled(false);

    }

    private void openFragmentSelectAudio(String audio_mode) {

        Bundle bundle = new Bundle();


        bundle.putString(Key.audio_mode, audio_mode);


        getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (fragmentSelectAudio2 == null) {
            fragmentSelectAudio2 = new FragmentSelectAudio2();
            fragmentSelectAudio2.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, fragmentSelectAudio2, fragmentSelectAudio2.getClass().getSimpleName()).commit();
        } else {
            fragmentSelectAudio2.setAudio_mode(audio_mode);
            getSupportFragmentManager().beginTransaction().show(fragmentSelectAudio2).commit();
        }

    }

    @Override
    public void OnClick(View v) {

        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }

        switch (v.getId()) {

            /*pop  add*/
            case R.id.linAddPhoto:
                popCreateAdd.dismiss();
                addPicByFast();
                break;

            case R.id.linAddVideo:
                popCreateAdd.dismiss();
                addVideo();
                break;

            /*pop  set*/
            case R.id.tvSort:
                popCreationSet.dismiss();
                changeItemClick();
                break;


            case R.id.tvSetAudio:
                popCreationSet.dismiss();
                setAudioMode(currentAudioMode);
                popCreateAudio.show(rBackground);
                break;

            case R.id.linPreviewPage:

                selectPreviewPage();

                tvSendPreview.setVisibility(View.VISIBLE);

                break;

            case R.id.linPreviewAll:

                selectPreviewAll();

                tvSendPreview.setVisibility(View.VISIBLE);

                break;


            case R.id.tvSendPreview:

                confirmPreviewClick();

                break;

            case R.id.tvRecording:

                popSelectAudioFile.dismiss();

                checkRecorder();


                break;

            case R.id.tvSelectAudioFile:

                popSelectAudioFile.dismiss();


                openFragmentSelectAudio(PLURAL);


                break;


        }

    }

    @Override
    public void OnDismiss() {

        if (popCreateAdd != null && popCreateAdd.getPopupWindow().isShowing()) {
            popCreateAdd.dismiss();
        }

        if (popCreationSet != null && popCreationSet.getPopupWindow().isShowing()) {
            popCreationSet.dismiss();
        }


        if (popCreateSort != null && popCreateSort.getPopupWindow().isShowing()) {
            popCreateSort.dismiss();
        }

        if (popCreateAudio != null && popCreateAudio.getPopupWindow().isShowing()) {
            popCreateAudio.dismiss();
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

    public void uploadMyAudioFile(String mp3Path) {

        this.mp3Path = mp3Path;

        doUploadPageAudio();

    }

    public void upLoadMyAudioFileForSingular(String mp3Path) {

        this.mp3Path = mp3Path;

        doUploadAlbumAudio(mp3Path);

    }

    public void showPopCreateAudio() {

        popCreateAudio.show(rBackground);
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

        } else if (fragmentSelectAudio2 != null && fragmentSelectAudio2.isAdded() && fragmentSelectAudio2.isVisible()) {
            fragmentSelectAudio2.cleanMedia();
            getSupportFragmentManager().beginTransaction().hide(fragmentSelectAudio2).commit();

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
        } else if (resultCode == RESULT_OK || resultCode == 0) {
            if (requestCode == ACTION_REQUEST_FEATHER) {

                if (null != data) {

                    try {

                        Uri uuu = null;

//                        if (SystemUtility.getSystemVersion() >= Build.VERSION_CODES.N) {
//
//                            MyLog.Set("e", getClass(), "44444444");
//                            MyLog.Set("e", getClass(), "data.getData().toString() => " + data.getData().toString());
//
////                            /file:/storage/emulated/0/pinpinbox_edit/pinpinbox_edit_1537326192524.png
//
//                            uuu = FileProvider.getUriForFile(mActivity, BuildConfig.APPLICATION_ID + ".provider", new File(data.getData().toString()));
//                        } else {
                        MyLog.Set("e", getClass(), "55555555");
                        uuu = data.getData();
//                        }

                        uploadFile = new File(new URI(uuu.toString()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    try {
//                        uploadFile = new File(new URI(outputUri.toString()));
//
//                        MyLog.Set("e", getClass(), "outputUri.getPath() => " + outputUri.getPath());
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    doAfterAviary();

                } else {

                    MyLog.Set("e", getClass(), "data => null");

                }

            }
        } else {
            deleteDownloadFile();
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


        GAControl.sendViewName("編輯器");


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

                MyLog.Set("e", getClass(), "-------------");
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
        cancelTask(upLoadPageAudioTask);
        cancelTask(deleteAudioTask);
        cancelTask(firstCreateFreeAlbumTask);
        cancelTask(descriptionTask);
        cancelTask(getAlbumDataOptionsTask);
        cancelTask(locationTask);
        cancelTask(protocol33);

        Recycle.IMG(videoPlayImg);
        Recycle.IMG(photo_or_templateImg);
        Recycle.IMG(addUserImg);
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

        deleteDownloadFile();

        File f = new File(mp3Path);
        if (f.exists()) {
            f.delete();
        }

        System.gc();

        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }

}


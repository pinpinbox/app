package com.pinpinbox.android.pinpinbox2_0_0.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.BitmapUtility;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.OkHttpClientManager;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.StickyGridViewHeader.StickyGridHeadersGridView;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CreationActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CreationTemplate2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.LocalPhotoAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.GridItem;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.CreateDir;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.KeysForSKD;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.YMDComparator;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DismissExcute;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.OnSpotlightEndedListener;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.OnSpotlightStartedListener;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.SimpleTarget;
import com.pinpinbox.android.pinpinbox2_0_0.libs.spotlight.Spotlight;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;
import com.pinpinbox.android.util.CheckExternalStorage;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TimeZone;


/**
 * Created by kevin9594 on 2017/2/19.
 */
public class FragmentSelectPhoto extends Fragment implements View.OnTouchListener, View.OnClickListener {

    private Fragment fragment;
    private NoConnect noConnect;
    private LoadingAnimation loading;
    private DialogV2Custom dlgUploading;
    private LoaderManager loaderManager;

    private CursorLoader cursorLoader;

    private Handler handlerText;
    private Handler upLoadHandler;

    private LocalPhotoAdapter adapter;

    private GetAlbumContentTask getAlbumContentTask;
    private UpLoadTask loadTask;
    private SendPhotoTask sendPhotoTask;

    private List<GridItem> nonHeaderIdList;
    private List<GridItem> sendList;
    private List<Boolean> booleanList;
    private List<GridItem> hasHeaderIdList;

    private StickyGridHeadersGridView gvPhoto;
    //    private LinearLayout linBottom;
    private RelativeLayout rBackground, rBottom;
    private ImageView backImg, cameraImg, sizeImg;
    private TextView tvAlbumCount, tvSelectCount, tvStartUpLoad;

    private String id, token;
    private String myDir;
    private String album_id;
    private String p58Message = "";
    private String strSinglePhotoPath = "";

    private static final int REQUEST_CODE_CAMERA = 104;
    private int p58Result = -1;
    private int intSelectCount;
    private int intMaxCount;
    private int intAlbumCount;
    private int doingType;
    private int total;
    private int upCount;
    private int round;
    private int totalRound;
    private int lastRound;
    private int createMode;
    private int onceCount;

    private int onceLoadPhotoCount = 0;
    private int totalPhotoCount = 0;
    private int stopPosition = 0;


    private boolean isGetPosition = false;
    private boolean isLoadMore = false;
    private boolean isPhotoSizeMax = false;
    private boolean isMax = false;
    private boolean isFromPPBCamera = false;

    private boolean isUploading = false;

    private boolean isOriginal = false;


    /*
     * onCreate
     * onCreateView
     * onActivityCreated
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2_0_0_select_photo, container, false);
        v.setOnTouchListener(this);

        rBackground = v.findViewById(R.id.rBackground);
        rBottom = v.findViewById(R.id.rBottom);
        gvPhoto = v.findViewById(R.id.gvPhoto);
        tvAlbumCount = v.findViewById(R.id.tvAlbumCount);
        tvSelectCount = v.findViewById(R.id.tvSelectCount);
        tvStartUpLoad = v.findViewById(R.id.tvStartUpLoad);
        backImg = v.findViewById(R.id.backImg);
        cameraImg = v.findViewById(R.id.cameraImg);
        sizeImg = v.findViewById(R.id.sizeImg);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBundle();

        init();

        cleanFile();

        getAllPictureToSetAdapter();

        selectControl();

        if (createMode == 0) {
            rBottom.setVisibility(View.VISIBLE);
            setHandler();
            doGetAlbumContent();
        } else {
            rBottom.setVisibility(View.GONE);
        }


        if (!PPBApplication.getInstance().getData().getBoolean(TaskKeyClass.first_to_select_photo, false)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    guideSize();
                }
            }, 300);

        }

    }

    private void getBundle() {

        album_id = getArguments().getString(Key.album_id, "");
        createMode = getArguments().getInt(Key.create_mode, 0);

    }


    private void init() {

        fragment = this;

        loading = new LoadingAnimation(getActivity());


        loading.dialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {


                checkStopUpload();


                return true;
            }
        });


        loaderManager = getLoaderManager();

        nonHeaderIdList = new ArrayList<>();
        booleanList = new ArrayList<>();
        hasHeaderIdList = nonHeaderIdList;

        SharedPreferences getdata = getActivity().getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);

        PPBApplication.getInstance().setSharedPreferences(getdata);

        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");
        myDir = "PinPinBox" + id + "/";

        strSinglePhotoPath = DirClass.ExternalFileDir(getActivity()) + DirClass.getMyDir(id) + DirClass.pathName_singleUploadPhoto;

        intSelectCount = 0;

        onceCount = 10;
        onceLoadPhotoCount = 500;
        totalPhotoCount = 0;
        stopPosition = 0;


        backImg.setOnClickListener(this);
        cameraImg.setOnClickListener(this);
        sizeImg.setOnClickListener(this);
        tvStartUpLoad.setOnClickListener(this);

        adapter = new LocalPhotoAdapter(getActivity(), hasHeaderIdList);
        gvPhoto.setAdapter(adapter);

        gvPhoto.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

//                * SCROLL_STATE_TOUCH_SCROLL：手指正拖着ListView滑动
//                * SCROLL_STATE_FLING：ListView正自由滑动
//                * SCROLL_STATE_IDLE：ListView滑动后静止

                switch (i) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                        MyLog.Set("d", FragmentSelectPhoto.class, "SCROLL_STATE_TOUCH_SCROLL");
                        new AsyncTask<Void, Void, Object>() {

                            @Override
                            protected Object doInBackground(Void... voids) {
                                clearCache();
                                return null;
                            }
                        };

                        break;

                    case SCROLL_STATE_FLING:
                        MyLog.Set("d", FragmentSelectPhoto.class, "SCROLL_STATE_FLING");
                        break;

                    case SCROLL_STATE_IDLE:
                        MyLog.Set("d", FragmentSelectPhoto.class, "SCROLL_STATE_IDLE");

                        if (absListView.getLastVisiblePosition() == (absListView.getCount() - 1)) {
                            MyLog.Set("d", FragmentSelectPhoto.class, "滑動到底部了");
                            isLoadMore = true;

                            if (!isPhotoSizeMax) {
                                loaderManager.restartLoader(0, null, loaderCallbacks);
                            }


                        }


                        break;

                }


            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });


    }


    public void checkStopUpload() {

        if (isUploading()) {

            if (dlgUploading == null) {
                dlgUploading = new DialogV2Custom(getActivity());
                dlgUploading.setStyle(DialogStyleClass.CHECK);
                dlgUploading.setMessage(R.string.pinpinbox_2_0_0_dialog_message_stop_to_upload);
                dlgUploading.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_stop);
                dlgUploading.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {

                        //20171016 需要優化

                        /*重置數據*/
                        total = 0;
                        round = 0;//
                        totalRound = 0;//批次總次數
                        lastRound = 0;

                        /*重置上傳列表*/
                        if (sendList != null) {
                            sendList.clear();
                        }

                        /*已上傳項目*/
                        if (booleanList != null) {
                            booleanList.clear();
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                OkHttpClientManager.getInstance().getOKHttp().cancel(ProtocolsClass.P58_InsertPhotoOfDiy);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        upLoadHandler.removeCallbacksAndMessages(null);
                                        upLoadStop();
                                        upLoadStop();
                                        resetButtonUpLoad();

                                    }
                                });

                            }
                        }).start();


                    }
                });
            }
            dlgUploading.show();

        }

    }

    private void cleanFile() {

        try {
            FileUtility.delAllFile(DirClass.ExternalFileDir(getActivity()) + DirClass.getMyDir(id));
            MyLog.Set("d", getClass(), "移除 => copy/ 底下檔案");
        } catch (Exception e) {
            e.printStackTrace();
        }


        CreateDir.create(getActivity(), id);

    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        private int currentCount = 0;
        private boolean isFirstLoad = true;

        private String[] projection = {MediaStore.Images.Media._ID,//0
                MediaStore.Images.Media.DATA,//1
                MediaStore.Images.Media.ORIENTATION,//2
                MediaStore.Images.Media.DATE_ADDED//3
        };


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            MyLog.Set("d", FragmentSelectPhoto.class, "loader.getId => " + id);


            cursorLoader = new CursorLoader(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    MediaStore.Images.Media.DATE_ADDED);


            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
            MyLog.Set("d", FragmentSelectPhoto.class, "onLoadFinished");

            if (isLoadMore) {

                currentCount = cursor.getCount();
                getPhotoContent(cursor);

                isLoadMore = false;
            }


            if (isFirstLoad) {
                currentCount = cursor.getCount();
                getPhotoContent(cursor);
                isFirstLoad = false;//相片獲取完畢再轉值，否則影響排序

            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            MyLog.Set("d", FragmentSelectPhoto.class, "onLoaderReset");
        }


        private void getPhotoContent(Cursor cursor) {

            if (!isGetPosition) {
                totalPhotoCount = cursor.getCount();
                isGetPosition = true;
            }

            if (totalPhotoCount == cursor.getCount()) {
                cursor.moveToLast();
            } else {
                cursor.moveToPosition(totalPhotoCount);
            }

            stopPosition = totalPhotoCount - onceLoadPhotoCount;


            if (totalPhotoCount > 0) {

                do {

                    String path = cursor.getString(cursor.getColumnIndex(projection[1]));
                    File file = new File(path);
                    if (file.exists()) {

                        GridItem item = new GridItem();

                        /*原路徑*/
                        item.setPath(path);

                        /*選取狀態*/
                        item.setSelect(false);


                        /*日期*/
                        long times = cursor.getLong(cursor.getColumnIndex(projection[3]));
                        String mTime = paserTimeToYMD(times, "yyyy / MM");
                        item.setTime(mTime);

                        int _id = cursor.getInt(cursor.getColumnIndex(projection[0]));
                        item.setMedia_id(_id);

                        /*旋轉角度*/
                        int orientation = cursor.getInt(cursor.getColumnIndex(projection[2]));
                        item.setDegree(orientation);

                        MyLog.Set("e", FragmentSelectPhoto.class, "path => " + path);
                        MyLog.Set("e", FragmentSelectPhoto.class, "_id => " + _id);
                        MyLog.Set("e", FragmentSelectPhoto.class, "times => " + times);
                        MyLog.Set("e", FragmentSelectPhoto.class, "mTime => " + mTime);
                        MyLog.Set("e", FragmentSelectPhoto.class, "orientation => " + orientation);
                        MyLog.Set("d", FragmentSelectPhoto.class, "---------------------------------------------------------------------");
                        nonHeaderIdList.add(item);

                    }

                    totalPhotoCount--;

                    if (totalPhotoCount == 0) {
                        isPhotoSizeMax = true;
                        break;
                    }

                    if (totalPhotoCount == stopPosition) {
                        /*讀取位置到達停止位置*/
                        break;
                    }


                } while (cursor.moveToPrevious());

                //反排
//                    Collections.reverse(nonHeaderIdList);
                hasHeaderIdList = generateHeaderId(nonHeaderIdList);

                //排序
                Collections.sort(hasHeaderIdList, new YMDComparator());
                adapter.notifyDataSetChanged();

            }

        }

    };

    private void getAllPictureToSetAdapter() {

        loaderManager.initLoader(0, null, loaderCallbacks);
    }

    private void selectControl() {

        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {


                if (createMode == 1) {

                    if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                        return;
                    }


                    DialogV2Custom d = new DialogV2Custom(getActivity());
                    d.setStyle(DialogStyleClass.CHECK);
                    d.setSmallImgByFile(nonHeaderIdList.get(i).getPath());
                    d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_use_aviary);
                    d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_filters);
                    d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_no_need);
                    d.setDismissExcute(new DismissExcute() {
                        @Override
                        public void AfterDismissDo() {
                            sendPhotoTask = new SendPhotoTask(i);
                            sendPhotoTask.execute();
                        }
                    });

                    d.setCheckExecute(new CheckExecute() {
                        @Override
                        public void DoCheck() {
                            if (!HttpUtility.isConnect(getActivity())) {
                                ((CreationTemplate2Activity) getActivity()).setNoConnect();
                                return;
                            }
                            String s = nonHeaderIdList.get(i).getPath();
                            copy(s);
                            startFeather(strSinglePhotoPath);
                        }
                    });
                    d.show();


                    return;
                }


                if (isMax) {
                    PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_count_max);
                    return;
                }

                MyLog.Set("d", fragment.getClass(), nonHeaderIdList.get(i).getPath());

                boolean isSelect = nonHeaderIdList.get(i).isSelect();

                if (!isSelect) {

                    if (intSelectCount + intAlbumCount >= intMaxCount) {
                        PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_count_max);
                        return;
                    } else {
                        intSelectCount++;
                        nonHeaderIdList.get(i).setSelect(true);
                    }

                } else {
                    intSelectCount--;
                    nonHeaderIdList.get(i).setSelect(false);
                }

                setItemCount(intSelectCount);
                adapter.notifyDataSetChanged();

            }
        });

    }

    private void guideSize() {


        SimpleTarget firstTarget = new SimpleTarget.Builder(getActivity())
                .setPoint(sizeImg)
                .setDescription(getResources().getString(R.string.pinpinbox_2_0_0_other_text_teach_select_original_size_photo))
                .build();

        Spotlight.with(getActivity())
                .setDuration(400) // duration of Spotlight emerging and disappearing in ms
                .setAnimation(new DecelerateInterpolator(2f)) // animation of Spotlight
                .setTargets(firstTarget) // set targets. see below for more info
                .setOnSpotlightStartedListener(new OnSpotlightStartedListener() { // callback when Spotlight starts
                    @Override
                    public void onStarted() {

                    }
                })
                .setOnSpotlightEndedListener(new OnSpotlightEndedListener() { // callback when Spotlight ends
                    @Override
                    public void onEnded() {

                        PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.first_to_select_photo, true).commit();

                    }
                })
                .start(); // start Spotlight


    }

    private void setHandler() {

        if (handlerText == null) {
            handlerText = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    switch (msg.what) {


                        case 1:

                            tvStartUpLoad.setText(getResources().getString(R.string.pinpinbox_2_0_0_button_uploaded) + upCount);

                            Bundle bundle = msg.getData();

                            if (bundle != null) {

                                String defaultPath = bundle.getString(Key.photo_default_path);

                                if (defaultPath != null) {


                                    for (int i = 0; i < nonHeaderIdList.size(); i++) {


                                        if ((nonHeaderIdList.get(i).getPath()).equals(defaultPath)) {

                                            nonHeaderIdList.get(i).setSelect(false);
                                            adapter.notifyDataSetChanged();

                                            intSelectCount--;
                                            setItemCount(intSelectCount);

                                            break;
                                        }

                                    }

                                }

                            }


                            break;
                    }

                }
            };
        }


        final DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);

        upLoadHandler = new Handler() {

            private int screenWidth = outMetrics.widthPixels;
            private int screenHeight = outMetrics.heightPixels;

            private void upLoadFinish() {
                ((CreationActivity) getActivity()).reFreshBottomPhoto();
                FileUtility.delAllFile(DirClass.ExternalFileDir(getActivity()) + DirClass.getMyDir(id) + DirClass.dirCopy);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(fragment);
                transaction.commit();
                resetAll();
                clearCache();
            }

            @Override
            public void handleMessage(final Message msg) {

                final int generalW = screenWidth / 2;
                final int generalH = screenHeight / 2;

                switch (msg.what) {
                    case 0:

                        //第幾輪
                        round++;
                        MyLog.Set("e", FragmentSelectPhoto.class, "第 " + round + " 輪上傳");

                        //每輪的最大上限值
                        totalRound = round * onceCount;

                        //lastRound 為上一輪最大值
                        for (int i = lastRound; i < totalRound; i++) {
                            final String copyPath = DirClass.ExternalFileDir(getActivity()) + DirClass.getMyDir(id) + DirClass.dirCopy + i + "copy.jpg";
                            final String path = sendList.get(i).getPath();
                            final int degree = sendList.get(i).getDegree();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap smallBitmap = null;
                                    MyLog.Set("d", FragmentSelectPhoto.class, "上傳一般尺寸");
                                    try {

                                        smallBitmap = BitmapUtility.getSmallBitmap(path, generalW, generalH, degree);

                                        BitmapUtility.saveToLocal(smallBitmap, copyPath, 100);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (smallBitmap != null) {
                                        smallBitmap.recycle();
                                    }

                                    final File Fto = new File(copyPath);

                                    protocol58(Fto, path);
                                    if (getActivity() == null) {
                                        return;
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                            if (p58Result == 1) {
                                                if (booleanList.size() == sendList.size()) {

                                                    upLoadStop();

                                                    upLoadFinish();

                                                    return;
                                                }
                                                if (booleanList.size() == totalRound) {
                                                    MyLog.Set("d", FragmentSelectPhoto.class, "傳送5張了 準備執行下一輪");

                                                    lastRound = totalRound;

                                                    total = total - onceCount;

                                                    if (total <= onceCount) {
                                                        Message message = new Message();
                                                        message.what = 1;
                                                        upLoadHandler.sendMessage(message);
                                                    } else if (total > onceCount) {
                                                        /*再執行1輪*/
                                                        Message message = new Message();
                                                        message.what = 0;
                                                        upLoadHandler.sendMessage(message);
                                                    }

                                                }

                                            } else if (p58Result == 0) {

                                                upLoadStop();

                                                DialogV2Custom.BuildError(getActivity(), p58Message);
                                            } else {

                                                upLoadStop();
                                                DialogV2Custom.BuildUnKnow(getActivity(), FragmentSelectPhoto.class.getSimpleName());
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }


                        break;

                    case 1:


                        for (int i = totalRound; i < sendList.size(); i++) {
                            final String copyPath = DirClass.ExternalFileDir(getActivity()) + DirClass.getMyDir(id) + DirClass.dirCopy + i + "copy.jpg";
                            final String path = sendList.get(i).getPath();
                            final int degree = sendList.get(i).getDegree();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap smallBitmap = null;
                                    MyLog.Set("d", getClass(), "上傳一般尺寸");
                                    try {

                                        smallBitmap = BitmapUtility.getSmallBitmap(path, generalW, generalH, degree);

                                        BitmapUtility.saveToLocal(smallBitmap, copyPath, 100);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    if (smallBitmap != null) {
                                        smallBitmap.recycle();
                                    }

                                    final File Fto = new File(copyPath);

                                    if (Fto.exists()) {
                                        protocol58(Fto, path);
                                    } else {
                                        MyLog.Set("d", FragmentSelectPhoto.class, "copyPath => 不存在");
                                    }

                                    if (getActivity() == null) {
                                        return;
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                            if (p58Result == 1) {
                                                if (booleanList.size() == sendList.size()) {
                                                    upLoadStop();

                                                    upLoadFinish();
                                                }
                                            } else if (p58Result == 0) {
                                                upLoadStop();
                                                DialogV2Custom.BuildError(getActivity(), p58Message);
                                            } else {
                                                upLoadStop();
                                                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
                                            }
                                        }
                                    });

                                }
                            }).start();
                        }


                        break;


                }

            }
        };


    }

    private void setSendPathList() {

        if (sendList == null) {
            sendList = new ArrayList<>();
        } else {
            sendList.clear();
        }

        int count = nonHeaderIdList.size();

        for (int i = 0; i < count; i++) {

            boolean b = nonHeaderIdList.get(i).isSelect();

            if (b) {
                GridItem item = nonHeaderIdList.get(i);
                sendList.add(item);
            }
        }

    }

    private void startUploadCheck() {


        if (nonHeaderIdList != null && nonHeaderIdList.size() > 0) {


            clearCache();

            System.gc();
        }


        if (intSelectCount == 0) {
            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_no_select_photo);
            return;
        }

//        setSendPathList();

//        checkSize();

        /*20171106*/
        if (isOriginal) {

            UploadTypeOriginal();

        } else {

            UploadTypeGeneral();

        }


    }

    private void checkSize() {
        final PopupCustom p = new PopupCustom(getActivity());
        p.setPopup(R.layout.pop_2_0_0_select_photo_size, R.style.pinpinbox_popupAnimation_bottom);

        TextView tvGeneral = p.getPopupView().findViewById(R.id.tvGeneral);
        TextView tvOriginal = p.getPopupView().findViewById(R.id.tvOriginal);


        /*一般上傳*/
        tvGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                p.getPopupWindow().dismiss();


                upLoadStart();

                tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_uploading);
                tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                tvStartUpLoad.setBackgroundResource(0);

                //20171016 reset sendList
                setSendPathList();

                total = sendList.size();

                upCount = 0;

                if (sendList.size() < 11) {

                    MyLog.Set("e", FragmentSelectPhoto.class, "一次上傳");

                    Message msg = new Message();
                    msg.what = 1;
                    upLoadHandler.sendMessage(msg);
                } else {

                    MyLog.Set("e", FragmentSelectPhoto.class, "分批上傳");

                    Message msg = new Message();
                    msg.what = 0;
                    upLoadHandler.sendMessage(msg);
                }


            }
        });

        tvOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                p.getPopupWindow().dismiss();

                tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_uploading);
                tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                tvStartUpLoad.setBackgroundResource(0);

                //20171016 reset sendList
                setSendPathList();

                loadTask = new UpLoadTask(sendList);
                loadTask.execute();


            }
        });


        p.show(rBackground);
    }


    /*一般上傳*/
    private void UploadTypeGeneral() {


        upLoadStart();

        tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_uploading);
        tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvStartUpLoad.setBackgroundResource(0);

        //20171016 reset sendList
        setSendPathList();

        total = sendList.size();

        upCount = 0;

        if (sendList.size() < 11) {

            MyLog.Set("e", FragmentSelectPhoto.class, "一次上傳");

            Message msg = new Message();
            msg.what = 1;
            upLoadHandler.sendMessage(msg);
        } else {

            MyLog.Set("e", FragmentSelectPhoto.class, "分批上傳");

            Message msg = new Message();
            msg.what = 0;
            upLoadHandler.sendMessage(msg);
        }
    }

    /*原始尺寸上傳*/
    private void UploadTypeOriginal() {

        tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_uploading);
        tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvStartUpLoad.setBackgroundResource(0);

        //20171016 reset sendList
        setSendPathList();

        loadTask = new UpLoadTask(sendList);
        loadTask.execute();

    }

    private void protocol58(File Fto, String defaultPath) {

        Map<String, String> data = new HashMap<>();
        data.put(Key.id, id);
        data.put(Key.token, token);
        data.put(Key.album_id, album_id);
        String sign = IndexSheet.encodePPB(data);
        Map<String, String> sendData = new HashMap<>();
        sendData.put(Key.id, id);
        sendData.put(Key.token, token);
        sendData.put(Key.album_id, album_id);
        sendData.put(Key.sign, sign);

        try {
            String strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P58_InsertPhotoOfDiy, sendData, Fto);
            MyLog.Set("d", getClass(), "p58strJson => " + strJson);
            JSONObject jsonObject = new JSONObject(strJson);
            p58Result = JsonUtility.GetInt(jsonObject, Key.result);
            if (p58Result == 1) {

                MyLog.Set("d", getClass(), "上傳成功");

                String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                JSONObject j = new JSONObject(jdata);

                String usergrade = JsonUtility.GetString(j, "usergrade");
                JSONObject uj = new JSONObject(usergrade);
                intMaxCount = JsonUtility.GetInt(uj, "photo_limit_of_album"); //2016.05.04

                String photo = JsonUtility.GetString(j, ProtocolKey.photo);
                JSONArray jsonArray = new JSONArray(photo);

                intAlbumCount = jsonArray.length();


                booleanList.add(true);

                upCount++;

                Bundle bundle = new Bundle();
                bundle.putString(Key.photo_default_path, defaultPath);
                Message msg = new Message();
                msg.what = 1;
                msg.setData(bundle);
                handlerText.sendMessage(msg);


            } else if (p58Result == 0) {
                if (Fto.exists()) {
                    Fto.delete();
                }
                p58Message = JsonUtility.GetString(jsonObject, Key.message);
            }
        } catch (Exception e) {
            if (Fto.exists()) {
                Fto.delete();
            }
            e.printStackTrace();
        }


    }


    private Uri cameraUri;

    private File camFile;


    private void dispatchTakePictureIntent() {


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (createMode == 0) {

            try {

                FileUtility.createCamDir(getActivity(), id);

                camFile = FileUtility.createCamFile(getActivity(), id);

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (SystemUtility.getSystemVersion() >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", camFile);
            } else {
                cameraUri = Uri.fromFile(camFile);
            }


            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);

            fragment.startActivityForResult(intent, REQUEST_CODE_CAMERA);
        } else {
            fragment.startActivityForResult(intent, 1);
        }

    }

    private void back() {

        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        clearCache();
    }

    private void upLoadStart() {

        loading.show();
        isUploading = true;

    }

    private void upLoadStop() {
        loading.dismiss();
        isUploading = false;
    }

    public void copy(String path) {
        File Ffrom = new File(path);
        String s = Ffrom.getName();
        File Fto = new File(DirClass.sdPath + myDir + "aviary_edit/" + s);
        if (Fto.exists()) {
            Fto.delete();
        } else {
            try {
                FileUtility.createSDFile(DirClass.sdPath + myDir + "aviary_edit/" + s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        FileUtility.copyfile(Ffrom, Fto, false);
        strSinglePhotoPath = Fto.getPath();
    }

    private void startFeather(String picPath) {
        if (!CheckExternalStorage.isExternalStorageAvailable()) {
            getActivity().showDialog(CheckExternalStorage.EXTERNAL_STORAGE_UNAVAILABLE);
            return;
        }

        Uri fromUri = null;

        if (SystemUtility.getSystemVersion() >= Build.VERSION_CODES.N) {

            MyLog.Set("e", getClass(), "11111111111");

            fromUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", new File(picPath));
        } else {
            MyLog.Set("e", getClass(), "22222222222");
            fromUri = Uri.fromFile(new File(picPath));
        }


        File editDirFile = new File(DirClass.sdPath + "pinpinbox_edit/");
        if (!editDirFile.exists()) {
            editDirFile.mkdirs();
        }

        Intent intent = new Intent(getActivity(), DsPhotoEditorActivity.class);
        intent.setData(fromUri);
        intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_API_KEY, KeysForSKD.DSPHOTOEDITOR());
        intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "pinpinbox_edit");

        startActivityForResult(intent, 100);
    }

    private void resetButtonUpLoad() {
        tvStartUpLoad.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);
        tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.WHITE));
        tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_start_upload);
        tvStartUpLoad.setClickable(true);
    }

    private void resetAll() {

        /*重置選擇狀態*/
        int size = nonHeaderIdList.size();
        for (int i = 0; i < size; i++) {
            nonHeaderIdList.get(i).setSelect(false);
        }
        adapter.notifyDataSetChanged();

        isMax = false;
        isFromPPBCamera = false;

        /*重置數據*/
        total = 0;
        round = 0;//
        totalRound = 0;//批次總次數
        lastRound = 0;
        intSelectCount = 0;

        setItemCount(intSelectCount);

        /*重置上傳列表*/
        if (sendList != null) {
            sendList.clear();
        }

        /*已上傳項目*/
        if (booleanList != null) {
            booleanList.clear();
        }


    }

    private void setButtonUpLoadByPhotoCountIsMax() {

        tvStartUpLoad.setBackgroundResource(0);
        tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_start_upload);
        tvStartUpLoad.setClickable(false);

    }

    private void setItemCount(int count) {
        tvSelectCount.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_current_select_item) + count + getResources().getString(R.string.pinpinbox_2_0_0_other_text_item));

    }


    /**
     * 对GridView的Item生成HeaderId, 根据图片的添加时间的年、月、日来生成HeaderId
     * 年、月、日相等HeaderId就相同
     *
     * @param nonHeaderIdList
     * @return
     */
    private List<GridItem> generateHeaderId(List<GridItem> nonHeaderIdList) {
        Map<String, Integer> mHeaderIdMap = new HashMap<String, Integer>();
        int mHeaderId = 1;
        List<GridItem> hasHeaderIdList;

        for (ListIterator<GridItem> it = nonHeaderIdList.listIterator(); it.hasNext(); ) {
            GridItem mGridItem = it.next();
            String ymd = mGridItem.getTime();
            if (!mHeaderIdMap.containsKey(ymd)) {
                mGridItem.setHeaderId(mHeaderId);
                mHeaderIdMap.put(ymd, mHeaderId);
                mHeaderId++;
            } else {
                mGridItem.setHeaderId(mHeaderIdMap.get(ymd));
            }
        }
        hasHeaderIdList = nonHeaderIdList;

        return hasHeaderIdList;
    }


    /**
     * 将毫秒数装换成pattern这个格式，我这里是转换成年月日
     *
     * @param time
     * @param pattern
     * @return
     */
    public String paserTimeToYMD(long time, String pattern) {
//        System.setProperty("user.timezone", "Asia/Shanghai");
//        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone tz = TimeZone.getDefault();
        TimeZone.setDefault(tz);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = new Date(time * 1000L);
        return sdf.format(date);
//        return sdf.format(new Date(time * 1000L));
    }


    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {
                    case DoingTypeClass.DoGetAlbumPhotoContent:
                        doGetAlbumContent();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);

    }

    public void doGetAlbumContent() {

        if (!HttpUtility.isConnect(getActivity())) {
            noConnect = new NoConnect(getActivity());
            ((CreationActivity) getActivity()).setNoConnect();
            return;
        }

        getAlbumContentTask = new GetAlbumContentTask();
        getAlbumContentTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void clearCache() {

        if (nonHeaderIdList != null && nonHeaderIdList.size() > 0) {

            new AsyncTask<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... objects) {

                    Glide.get(getActivity()).clearDiskCache();


                    return null;
                }
            }.execute();


            Glide.get(getActivity()).clearMemory();

            System.gc();
        }


    }


    /*protocol57*/
    public class GetAlbumContentTask extends AsyncTask<Void, Void, Object> {

        private int p57Result = -1;
        private String p57Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetAlbumPhotoContent;
            loading.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P57_GetAlbumOfDiy
                        , SetMapByProtocol.setParam57_getalbumofdiy(id, token, album_id)
                        , null);
                MyLog.Set("d", getClass(), "p57strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p57Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p57Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p57Result == 1) {
                        String jdata = JsonUtility.GetString(jsonObject, Key.data);
                        JSONObject j = new JSONObject(jdata);

                        String usergrade = JsonUtility.GetString(j, "usergrade");
                        JSONObject uj = new JSONObject(usergrade);
                        intMaxCount = JsonUtility.GetInt(uj, "photo_limit_of_album"); //2016.05.04

                        MyLog.Set("d", getClass(), "intMaxCount(by usergrade) => " + intMaxCount);

                        String photo = JsonUtility.GetString(j, "photo");
                        JSONArray jsonArray = new JSONArray(photo);

                        intAlbumCount = jsonArray.length();

                    } else if (p57Result == 0) {//if(p57Result.equals("1"))
                        p57Message = JsonUtility.GetString(jsonObject, Key.message);
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

            loading.dismiss();

            if (p57Result == 1) {

                tvAlbumCount.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_current_item_count) + intAlbumCount + "/" + intMaxCount);
                resetButtonUpLoad();

                if (intAlbumCount >= intMaxCount) {

                    DialogV2Custom.BuildError(getActivity(), getResources().getString(R.string.pinpinbox_2_0_0_toast_message_count_max));
                    setButtonUpLoadByPhotoCountIsMax();

                    isMax = true;

                }

            } else if (p57Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p57Message);
            } else if (p57Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }
        }

    }

    /*protocol58*/
    private class UpLoadTask extends AsyncTask<Void, Void, Object> {

        private List<GridItem> itemList;

        private int upCount;

        private UpLoadTask(List<GridItem> itemList) {

            this.itemList = itemList;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            upLoadStart();

        }

        @Override
        protected Void doInBackground(Void... params) {

            int count = itemList.size();

            for (int i = 0; i < count; i++) {//2016.02.18修改成sendList

//                final String copyPath = sdPath + myDir + "copy/"+ i + "copy.jpg";

                MyLog.Set("d", getClass(), "上傳原始尺寸");

                final String path = itemList.get(i).getPath();

                /*------------------------------------------------------------------------------------------------------------------------------------------------------*/
                File Fto;

                Fto = new File(path);

                /*------------------------------------------------------------------------------------------------------------------------------------------------------*/

                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put("token", token);
                data.put("album_id", album_id);
                String sign = IndexSheet.encodePPB(data);
                Map<String, String> sendData = new HashMap<>();
                sendData.put("id", id);
                sendData.put("token", token);
                sendData.put("album_id", album_id);
                sendData.put("sign", sign);

                try {
                    String strJson = HttpUtility.uploadSubmit(false, ProtocolsClass.P58_InsertPhotoOfDiy, sendData, Fto);

                    MyLog.Set("d", getClass(), "p58strJson => " + strJson);

                    JSONObject jsonObject = new JSONObject(strJson);
                    p58Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p58Result == 1) {

                        MyLog.Set("d", getClass(), "上傳成功");


                        String jdata = jsonObject.getString(Key.data);

                        JSONObject j = new JSONObject(jdata);

                        String usergrade = j.getString("usergrade");
                        JSONObject uj = new JSONObject(usergrade);
                        intMaxCount = uj.getInt("photo_limit_of_album");

                        String photo = JsonUtility.GetString(j, "photo");
                        JSONArray jsonArray = new JSONArray(photo);

                        intAlbumCount = jsonArray.length();

                        MyLog.Set("d", getClass(), "目前張數 => " + jsonArray.length());

                        upCount = i + 1;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvStartUpLoad.setText(getResources().getString(R.string.pinpinbox_2_0_0_button_uploaded) + upCount);


                                if (path != null) {


                                    for (int i = 0; i < nonHeaderIdList.size(); i++) {


                                        if ((nonHeaderIdList.get(i).getPath()).equals(path)) {

                                            nonHeaderIdList.get(i).setSelect(false);
                                            adapter.notifyDataSetChanged();
                                            intSelectCount--;
                                            setItemCount(intSelectCount);

                                            break;
                                        }


                                    }

                                }


                            }
                        });

                        if (intAlbumCount >= intMaxCount) {
                            break;
                        }

                    } else if (p58Result == 0) {

                        p58Message = JsonUtility.GetString(jsonObject, Key.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            upLoadStop();

            if (p58Result == 1) {

                FileUtility.delAllFile(DirClass.sdPath + myDir + "copy/");
                MyLog.Set("d", getClass(), "FileUtility.deleteFileFolder(new File(sdPath + myDir + copy))");


                ((CreationActivity) getActivity()).reFreshBottomPhoto();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(fragment);
                transaction.commit();

                resetAll();
                clearCache();


            } else if (p58Result == 0) {
                DialogV2Custom.BuildError(getActivity(), p58Message);
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), getClass().getSimpleName());
            }


        }

    }

    /*CreateMode = 1 才執行*/
    private class SendPhotoTask extends AsyncTask<Void, Void, Object> {

        private int position;
        private Bitmap bmp, reBitmap;

        private SendPhotoTask(int position) {
            this.position = position;
            bmp = null;
            reBitmap = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //無網路線程 不用loadingStart
            loading.show();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String path = nonHeaderIdList.get(position).getPath();

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            opts.inJustDecodeBounds = true;

            Bitmap bitmap = BitmapFactory.decodeFile(path, opts);

            int w = opts.outWidth;
            int h = opts.outHeight;
            int wh = w * h;

            MyLog.Set("d", FragmentSelectPhoto.class, opts.outWidth + "");
            MyLog.Set("d", FragmentSelectPhoto.class, opts.outHeight + "");


            opts.inJustDecodeBounds = false;

            try {
                if (wh > 1600000) {
                    MyLog.Set("d", FragmentSelectPhoto.class, "resize =>3");


                    opts.inSampleSize = 3;
                    bmp = BitmapFactory.decodeFile(path, opts);

                } else if (wh < 1600000 && wh > 350000) {
                    MyLog.Set("d", FragmentSelectPhoto.class, "resize =>2");


                    opts.inSampleSize = 2;
                    bmp = BitmapFactory.decodeFile(path, opts);

                } else {
                    MyLog.Set("d", FragmentSelectPhoto.class, "resize =>1");

                    opts.inSampleSize = 1;
                    bmp = BitmapFactory.decodeFile(path, opts);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            int rotate = BitmapUtility.getExifOrientation(path);

            reBitmap = BitmapUtility.rotateBitmapByDegree(bmp, rotate);

            MyLog.Set("d", FragmentSelectPhoto.class, "rotate => " + rotate);

            FileUtility.saveBitmap(reBitmap, DirClass.sdPath + myDir, "pinpinbox_select.jpg");


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (bmp != null && !bmp.isRecycled()) {
                bmp.recycle();
                bmp = null;
            }

            if (reBitmap != null && !reBitmap.isRecycled()) {
                reBitmap.recycle();
                reBitmap = null;
            }

            System.gc();

            loading.dismiss();


            ImageView img = ((CreationTemplate2Activity) getActivity()).getToFragmentImg();
            RelativeLayout relativeLayout = ((CreationTemplate2Activity) getActivity()).getToFragmentRelativeLayout();
            int area = ((CreationTemplate2Activity) getActivity()).getToFragmentArea();
            int area_y = ((CreationTemplate2Activity) getActivity()).getToFragmentArea_Y();

            ((CreationTemplate2Activity) getActivity()).setPicture(img, strSinglePhotoPath, relativeLayout, area, area_y);

            back();
        }
    }

    public void setFromPPBCamera(boolean isFromPPBCamera) {
        this.isFromPPBCamera = isFromPPBCamera;
    }

    public boolean isUploading() {
        return isUploading;
    }

    public void setUploading(boolean uploading) {
        isUploading = uploading;
    }


    private void cameraPhotoToList(Uri uri) {


        String path = "";

        if (SystemUtility.getSystemVersion() >= Build.VERSION_CODES.N) {
            path = camFile.getAbsolutePath();
        } else {
            path = FileUtility.getImageAbsolutePath(getActivity(), uri);
        }

        MyLog.Set("d", getClass(), "onActivityResult => path => " + path);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy / MM");
        String time = sdf.format(new java.util.Date());

        int degree = BitmapUtility.getExifOrientation(path);

        GridItem gridItem = new GridItem();
        gridItem.setPath(path);
        gridItem.setTime(time);

        if (intAlbumCount + intSelectCount < intMaxCount) {
            intSelectCount++;
            gridItem.setSelect(true);
        } else {
            gridItem.setSelect(false);
            MyLog.Set("d", FragmentSelectPhoto.class, "張數已滿");
        }


        gridItem.setMedia_id(0);
        gridItem.setDegree(degree);
        nonHeaderIdList.add(0, gridItem);


        hasHeaderIdList = generateHeaderId(nonHeaderIdList);

        Collections.sort(hasHeaderIdList, new YMDComparator());

        adapter.notifyDataSetChanged();

        setItemCount(intSelectCount);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //RESULT_OK = -1

        MyLog.Set("d", getClass(), "resultCode => " + resultCode);
        MyLog.Set("d", getClass(), "requestCode => " + requestCode);

        if (resultCode == -1 || resultCode == 0) {


            switch (requestCode) {

                case REQUEST_CODE_CAMERA://快速建立照相返回

                    isFromPPBCamera = true;

                    Uri mImageCaptureUri = null;


                    if (data != null && data.getData() != null) {

                        MyLog.Set("d", getClass(), "data 存在");

                        mImageCaptureUri = data.getData();
                    } else {

                        MyLog.Set("d", getClass(), "data == null");

                    }


                    if (mImageCaptureUri == null) {
                        if (cameraUri != null) {
                            mImageCaptureUri = cameraUri;
                        }
                    }


                    if (mImageCaptureUri == null) {
                        return;

                    }

                    cameraPhotoToList(mImageCaptureUri);


                    break;

                case 1: //套用版型照相返回

                    isFromPPBCamera = true;

                    if (data != null) {
                        /*取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意*/
//                        Uri nImageCaptureUri = data.getData();
                        Uri nImageCaptureUri = null;

                        try {

                            if (SystemUtility.getSystemVersion() >= Build.VERSION_CODES.N) {
                                nImageCaptureUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", new File(data.getDataString()));
                            } else {
                                nImageCaptureUri = data.getData();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                        /* 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取*/
                        if (nImageCaptureUri != null) {

                            String path = FileUtility.getImageAbsolutePath(getActivity(), nImageCaptureUri);
                            MyLog.Set("d", getClass(), "onActivityResult => path => " + path);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy / MM");
                            String time = sdf.format(new java.util.Date());

                            int degree = BitmapUtility.getExifOrientation(path);

                            GridItem gridItem = new GridItem();
                            gridItem.setPath(path);
                            gridItem.setTime(time);
                            gridItem.setSelect(false);
                            gridItem.setMedia_id(0);
                            gridItem.setDegree(degree);
                            gridItem.setThumbnailsPath("");
                            nonHeaderIdList.add(0, gridItem);


                            hasHeaderIdList = generateHeaderId(nonHeaderIdList);

                            Collections.sort(hasHeaderIdList, new YMDComparator());

                            adapter.notifyDataSetChanged();

                        }
                    }


                    break;


                case 100:

                    if (null != data) {
                        Bundle extra = data.getExtras();
                        if (null != extra) {
                            ImageView img = ((CreationTemplate2Activity) getActivity()).getToFragmentImg();
                            RelativeLayout relativeLayout = ((CreationTemplate2Activity) getActivity()).getToFragmentRelativeLayout();
                            int area = ((CreationTemplate2Activity) getActivity()).getToFragmentArea();
                            int area_y = ((CreationTemplate2Activity) getActivity()).getToFragmentArea_Y();
                            ((CreationTemplate2Activity) getActivity()).setPicture(img, strSinglePhotoPath, relativeLayout, area, area_y);
                            back();
                        }
                    }

                    break;


            }

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

    @PermissionGrant(REQUEST_CODE_CAMERA)
    public void requestCameraSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                dispatchTakePictureIntent();
            }
        }, 500);


    }

    @PermissionDenied(REQUEST_CODE_CAMERA)
    public void requestCameraFailed() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> false");

            DialogV2Custom d = new DialogV2Custom(getActivity());
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_setting);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_open_permission_camera);
            d.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    SystemUtility.getAppDetailSettingIntent(getActivity());
                }
            });
            d.show();

        } else {

            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (view.getId()) {

            case R.id.cameraImg:

                switch (checkPermission(getActivity(), Manifest.permission.CAMERA)) {
                    case SUCCESS:
                        dispatchTakePictureIntent();
                        break;
                    case REFUSE:
                        MPermissions.requestPermissions(FragmentSelectPhoto.this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
                        break;
                }


                break;

            case R.id.tvStartUpLoad:

                startUploadCheck();

                break;
            case R.id.backImg:

                back();

                break;

            case R.id.sizeImg:

                if (isOriginal) {

                    //一般
                    isOriginal = false;
                    sizeImg.setImageResource(R.drawable.ic200_photosize_light);

                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_original_size_has_been_canceled);

                } else {

                    //原始
                    isOriginal = true;
                    sizeImg.setImageResource(R.drawable.ic200_photosize_dark);

                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_original_size_has_been_selected);

                }

                break;

        }
    }

    @Override
    public void onPause() {
        clearCache();
        super.onPause();
    }

    @Override
    public void onDestroy() {

        if (getAlbumContentTask != null && !getAlbumContentTask.isCancelled()) {
            getAlbumContentTask.cancel(true);
        }
        getAlbumContentTask = null;

        if (loadTask != null && !loadTask.isCancelled()) {
            loadTask.cancel(true);
        }
        loadTask = null;

        if (sendPhotoTask != null && !sendPhotoTask.isCancelled()) {
            sendPhotoTask.cancel(true);
        }
        sendPhotoTask = null;

        if (nonHeaderIdList != null && nonHeaderIdList.size() > 0) {

            Glide.get(getActivity()).clearMemory();

            nonHeaderIdList.clear();
            nonHeaderIdList = null;
        }


        if (sendList != null) {
            sendList.clear();
            sendList = null;
        }

        if (booleanList != null) {
            booleanList.clear();
            booleanList = null;
        }


        Recycle.IMG(backImg);
        Recycle.IMG(cameraImg);

        if (upLoadHandler != null) {
            upLoadHandler.removeCallbacksAndMessages(null);
        }
        if (handlerText != null) {
            handlerText.removeCallbacksAndMessages(null);
        }
        upLoadHandler = null;
        handlerText = null;

        System.gc();

        MyLog.Set("d", FragmentSelectPhoto.class, "onDestroy");
        super.onDestroy();
    }


}

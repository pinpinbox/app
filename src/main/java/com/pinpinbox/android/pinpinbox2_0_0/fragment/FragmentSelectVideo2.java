package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.libs.coreprogress.helper.ProgressHelper;
import com.pinpinbox.android.pinpinbox2_0_0.libs.coreprogress.listener.impl.UIProgressListener;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.StickyGridViewHeader.StickyGridHeadersGridView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.GridItem;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.YMDComparator;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Creation2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.VideoPlayActivity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.LocalVideoAdapter;
import com.squareup.picasso.Picasso;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vmage on 2017/3/17.
 */
public class FragmentSelectVideo2 extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private Fragment fragment;
    private LoadingAnimation loading;
    private SimpleDateFormat formatter;
    private NoConnect noConnect;
    private LruCache<String, Bitmap> lruCache;
    private DialogV2Custom dialogRefreshPhoto;

    private GetAlbumContentTask getAlbumContentTask;

    private LocalVideoAdapter adapter;


    private List<GridItem> nonHeaderIdList;
    private List<GridItem> hasHeaderIdList;
    private List<HashMap<String, Object>> thumList;

    private StickyGridHeadersGridView gvVideo;
    private TextView tvStartUpLoad, tvProgressrate, tvAlbumCount;
    private ImageView backImg, cameraImg;
    private ProgressBar progress;

    private String id, token;
    private String myDir;
    private String album_id;
    private String strSendPath;
    private String p80Result, p80Message;

    private static final int REQUEST_CODE_CAMERA = 105;
    private int doingType;
    private int intMaxCount;
    private int intAlbumCount;
    private int intSelectCount;

    private boolean isMax = false;
    private boolean isFromPPBCamera = false;
    private boolean isFromPPBRefresh = false;

    private OkHttpClient client = new OkHttpClient.Builder()
            //设置超时，不设置可能会报异常
            .connectTimeout(20, TimeUnit.MINUTES)
            .readTimeout(20, TimeUnit.MINUTES)
            .writeTimeout(20, TimeUnit.MINUTES)
            .build();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2_0_0_select_video, container, false);
        v.setOnTouchListener(this);


        gvVideo = (StickyGridHeadersGridView) v.findViewById(R.id.gvVideo);
        tvStartUpLoad = (TextView) v.findViewById(R.id.tvStartUpLoad);
        tvProgressrate = (TextView) v.findViewById(R.id.tvProgressrate);
        tvAlbumCount = (TextView) v.findViewById(R.id.tvAlbumCount);
        backImg = (ImageView) v.findViewById(R.id.backImg);
        cameraImg = (ImageView) v.findViewById(R.id.cameraImg);
        progress = (ProgressBar) v.findViewById(R.id.progress);

        TextUtility.setBold(tvStartUpLoad, true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvTitle), true);


        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBundle();

        init();

//        getVideoThum();


        getLoaderManager().initLoader(1, null, videoLoader);


        selectControl();

        doGetAlbumContent();


    }

    private void getBundle() {

        album_id = getArguments().getString(Key.album_id, "");
    }


    private void init() {

        fragment = this;

        formatter = new SimpleDateFormat("mm:ss");

        loading = new LoadingAnimation(getActivity());

        int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
        int maxSize = maxMemory / 4;//拿到缓存的内存大小 35         lruCache = new LruCache<String, Bitmap>(maxSize){

        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {

                if (value != null) {
                    return value.getByteCount();
                    //return value.getRowBytes() * value.getHeight();	//旧版本的方法
                } else {
                    return 0;
                }
            }

        };

        thumList = new ArrayList<>();
        nonHeaderIdList = new ArrayList<>();
        hasHeaderIdList = nonHeaderIdList;

        SharedPreferences getdata = getActivity().getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");
        myDir = "PinPinBox" + id + "/";

        intSelectCount = 0;


        backImg.setOnClickListener(this);
        cameraImg.setOnClickListener(this);
        tvStartUpLoad.setOnClickListener(this);

        adapter = new LocalVideoAdapter(getActivity(), hasHeaderIdList, gvVideo, lruCache);
        gvVideo.setAdapter(adapter);


    }

    private CursorLoader cursorLoader;
    private LoaderManager.LoaderCallbacks<Cursor> videoLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

//        private String[] thumProjection = {MediaStore.Video.Thumbnails.DATA,
//                MediaStore.Video.Thumbnails.VIDEO_ID};

        private String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE
        };

        private int currentCount = 0;

        private boolean isFirstLoad = true;


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

            cursorLoader = new CursorLoader(getActivity(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    projection[2]);

            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {

            MyLog.Set("d", fragment.getClass(), "loader.getId() => " + loader.getId());

//            getPhotoContent(cursor);


            if (isFirstLoad) {
                currentCount = cursor.getCount();
                getVideoContent(cursor);
                isFirstLoad = false;//相片獲取完畢再轉值，否則影響排序

            } else {

//                if (!isFromPPBCamera) {
//                    MyLog.Set("d", fragment.getClass(), "isFromPPBCamera => false");
//
//
//                    if(isFromPPBRefresh){
//                        isFromPPBRefresh = false;
//                    }else {
//
//                        if (currentCount != cursor.getCount()) {
//                            if (dialogRefreshPhoto == null) {
//                                dialogRefreshPhoto = new DialogV2Custom(getActivity());
//                                dialogRefreshPhoto.setStyle(DialogStyleClass.CHECK);
//                                dialogRefreshPhoto.setMessage(R.string.pinpinbox_2_0_0_dialog_message_data_photo_change_to_refrest);
//                                dialogRefreshPhoto.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_ok);
//                                dialogRefreshPhoto.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_be_later);
//                                dialogRefreshPhoto.setCheckExecute(new CheckExecute() {
//                                    @Override
//                                    public void DoCheck() {
//                                        resetPhoto();
//                                        getVideoContent(cursor);
//                                        currentCount = cursor.getCount();
//
//                                    }
//                                });
//                                dialogRefreshPhoto.show();
//                            } else if (dialogRefreshPhoto != null && !dialogRefreshPhoto.getmDialog().isShowing()) {
//                                dialogRefreshPhoto.show();
//                            }
//                        }
//
//                    }
//
//                } else {
//
//                    isFromPPBRefresh = true;
//
//                    MyLog.Set("d", fragment.getClass(), "isFromPPBCamera => true, 將轉為false");
//                    isFromPPBCamera = false;
//                }

            }


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

            MyLog.Set("d", fragment.getClass(), "onLoaderReset");


        }

        private void resetPhoto() {

            lruCache.evictAll();

            nonHeaderIdList.clear();
            hasHeaderIdList.clear();
            strSendPath = "";

            intSelectCount = 0;

        }


        private void getVideoContent(Cursor cursor) {

            int count = cursor.getCount();


            if (count > 0) {

//                cursor.moveToFirst();
                cursor.moveToLast();

                do {

                    long duration = cursor.getLong(cursor.getColumnIndexOrThrow(projection[4]));// 时长

                    if (duration < 31000) {

                        GridItem item = new GridItem();

                        /*時間*/
                        String strDuration = formatter.format(duration);
                        item.setDuration(strDuration);

                       /*id*/
                        long id = cursor.getLong(cursor.getColumnIndexOrThrow(projection[0]));
                        item.setMedia_id((int) id);

                          /*縮圖*/
//                        Cursor thumbCursor = getActivity().getContentResolver().query(
//                                MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
//                                thumProjection,
//                                MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id,
//                                null,
//                                null
//                        );
//                        if (thumbCursor.moveToFirst()) {
//                            String thumbPath = thumbCursor.getString(thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA));
//                            item.setThumbnailsPath(thumbPath);
//                        }


                        /*日期*/
                        long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(projection[2]));
                        String mTime = paserTimeToYMD(dateTime, "yyyy / MM");
                        item.setTime(mTime);

                        /*原路徑*/
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(projection[3]));
                        item.setPath(path);

                        /*選取狀態*/
                        item.setSelect(false);

//                        /*id*/
//                        item.setMedia_id(id);

                        nonHeaderIdList.add(item);

//                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(projection[5]));

                    }

//                } while (cursor.moveToNext());
                } while (cursor.moveToPrevious());


                //反排
//                Collections.reverse(nonHeaderIdList);
                hasHeaderIdList = generateHeaderId(nonHeaderIdList);

                //排序
                Collections.sort(hasHeaderIdList, new YMDComparator());
                adapter.notifyDataSetChanged();

            }

        }

    };

    private void selectControl() {

        gvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if (isMax) {
                    PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_count_max);
                    return;
                }

                MyLog.Set("d", fragment.getClass(), nonHeaderIdList.get(i).getPath());

                boolean isSelect = nonHeaderIdList.get(i).isSelect();

                if (!isSelect) {

                    if (intSelectCount > 0) {
                        PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_once_upload_count);
                        return;
                    } else {
                        intSelectCount++;
                        nonHeaderIdList.get(i).setSelect(true);
                        strSendPath = nonHeaderIdList.get(i).getPath();
                    }

                } else {
                    intSelectCount--;
                    nonHeaderIdList.get(i).setSelect(false);
                }


                adapter.notifyDataSetChanged();

            }
        });


        gvVideo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final String path = nonHeaderIdList.get(i).getPath();


                if (new File(path).exists()) {

                    PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_opening_video);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            bundle.putString("videopath", path);
                            Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            MyLog.Set("d", getClass(), "open vedioActivity");
                        }
                    }, 300);


                } else {
                    DialogV2Custom.BuildError(getActivity(), getResources().getString(R.string.pinpinbox_2_0_0_dialog_message_video_play_error_check_file));
                }


                return true;
            }
        });


    }

    private void startUploadCheck() {



        if (nonHeaderIdList != null && nonHeaderIdList.size() > 0) {
            int count = nonHeaderIdList.size();
            for (int i = 0; i < count; i++) {
                Picasso.with(getActivity().getApplicationContext()).invalidate(nonHeaderIdList.get(i).getPath());
            }
            System.gc();
        }

        if (intSelectCount == 0) {
            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_no_select_video);
            return;
        }


        tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_uploading);
        tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
        tvStartUpLoad.setBackgroundResource(0);
        tvStartUpLoad.setClickable(false);

        if(loading!=null){
            loading.dismiss();
        }
        loading.show();

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
        try {
            RequestBody requestBody = HttpUtility.uploadSubmitWithFileLengthByOkHttp3(sendData, new File(strSendPath));

            //從application 獲取語系

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(ProtocolsClass.P80_InsertVideoOfDiy)
                    .post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressResponseListener))
                    .build();
            client.newCall(request).enqueue(callback);
        } catch (Exception e) {

            if(loading!=null){
                loading.dismiss();
            }

            DialogV2Custom d = new DialogV2Custom(getActivity());
            d.setStyle(DialogStyleClass.ERROR);
            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_update_error_restart_app);
            d.show();


            resetAll();

            //try again
            e.printStackTrace();
        }
    }

    private void setButtonUpLoadByPhotoCountIsMax() {

        tvStartUpLoad.setBackgroundResource(0);
        tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_start_upload);
        tvStartUpLoad.setClickable(false);

    }

    private void resetButtonUpLoad() {
        tvStartUpLoad.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);
        tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.WHITE));
        tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_start_upload);
        tvStartUpLoad.setClickable(true);
    }

    private void resetProgress() {
        tvProgressrate.setText(R.string.pinpinbox_2_0_0_other_text_current_select_schedule_0);
        progress.setProgress(0);
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
        intSelectCount = 0;

        /*重置上傳路徑*/
        strSendPath = "";


    }

    /**
     * 0(低质量，176 x 144分辨率)
     * 1(高质量，如1080p (1920 x 1080)分辨率)
     */
    private void dispatchTakeVideoIntent() { //32 20
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 1);
        isFromPPBCamera = true;
    }

    private void back() {
        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        cleanPicasso();
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

    private Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            MyLog.Set("d", fragment.getClass(), e.toString());

            resetButtonUpLoad();
            resetProgress();

            if (loading.dialog().isShowing()) {
                loading.dismiss();
            }

            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_upload_fail);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
//            Log.e("TAG", response.body().string());

            if (response.isSuccessful()) {

                String result = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    p80Result = jsonObject.getString(Key.result);
                    if (p80Result.equals("1")) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (loading.dialog().isShowing()) {
                                    loading.dismiss();
                                }

                                PinPinToast.showSuccessToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_upload_success);

                                resetButtonUpLoad();
                                resetProgress();
                                resetAll();
                                ((Creation2Activity) getActivity()).reFreshBottomPhoto();

                                back();

                            }
                        });


                    } else if (p80Result.equals("0")) {
                        p80Message = jsonObject.getString(Key.message);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (loading.dialog().isShowing()) {
                                    loading.dismiss();
                                }

                                PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_upload_fail);

                            }
                        });


                    } else {
                        p80Result = "";

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (loading.dialog().isShowing()) {
                                    loading.dismiss();
                                }

                                PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_upload_fail);

                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            tvStartUpLoad.setClickable(true);


        }
    };

    private UIProgressListener uiProgressResponseListener = new UIProgressListener() {

        @Override
        public void onUIProgress(long bytesRead, long contentLength, boolean done) {
            Log.e("TAG", "bytesRead:" + bytesRead);
            Log.e("TAG", "contentLength:" + contentLength);
            Log.e("TAG", "done:" + done);
            if (contentLength != -1) {
                //长度未知的情况下回返回-1
                Log.e("TAG", (100 * bytesRead) / contentLength + "% done");
            }
            Log.e("TAG", "================================");
            //ui层回调

            int length = (int) ((100 * bytesRead) / contentLength);

            tvProgressrate.setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_current_select_schedule) + length + "%");

            progress.setProgress(length);

        }

        @Override
        public void onUIStart(long bytesRead, long contentLength, boolean done) {
            super.onUIStart(bytesRead, contentLength, done);
            tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_uploading);
            tvStartUpLoad.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
            tvStartUpLoad.setBackgroundResource(0);
            tvStartUpLoad.setClickable(false);
            loading.show();

        }

        @Override
        public void onUIFinish(long bytesRead, long contentLength, boolean done) {
            super.onUIFinish(bytesRead, contentLength, done);


            PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_server_refreshing);


        }
    };

    private void cleanPicasso() {

        if (nonHeaderIdList != null && nonHeaderIdList.size() > 0) {
            int count = nonHeaderIdList.size();
            for (int i = 0; i < count; i++) {
                Picasso.with(getActivity().getApplicationContext()).invalidate(nonHeaderIdList.get(i).getPath());
            }
            System.gc();
        }


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
            ((Creation2Activity) getActivity()).setNoConnect();
            return;
        }

        getAlbumContentTask = new GetAlbumContentTask();
        getAlbumContentTask.execute();
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
                resetProgress();
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

    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        switch (view.getId()) {

            case R.id.cameraImg:


                switch (checkPermission(getActivity(), Manifest.permission.CAMERA)){

                    case SUCCESS:
                        dispatchTakeVideoIntent();
                        break;
                    case REFUSE:

                        MPermissions.requestPermissions(FragmentSelectVideo2.this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA);
                        break;

//                    case REFUSE_NO_ASK:
//
//                        PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_open_permission_camera);
//                        SystemUtility.getAppDetailSettingIntent(getActivity());
//                        break;
                }

                break;

            case R.id.tvStartUpLoad:
                startUploadCheck();
                break;
            case R.id.backImg:
                back();
                break;

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == -1) {


            switch (requestCode) {

                case 1:

//                      移至點擊開啟錄影
                    isFromPPBCamera = true;

                    if (data != null) {

                        Uri mImageCaptureUri = data.getData();
                        if (mImageCaptureUri != null) {

                            String resultPath = FileUtility.getImageAbsolutePath(getActivity(), mImageCaptureUri);

                            final String[] columns = {
                                    MediaStore.Video.Media._ID, //0
                                    MediaStore.Video.Media.DISPLAY_NAME, //1
                                    MediaStore.Video.Media.DATE_ADDED, //2
                                    MediaStore.Video.Media.DATA, //3
                                    MediaStore.Video.Media.DURATION, //4
                                    MediaStore.Video.Media.SIZE  //5
                            };
                            final String orderBy = MediaStore.Video.Media.DATE_ADDED;
                            Cursor cursor = getActivity().getContentResolver().query(
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns,
                                    null, null, orderBy);

                            if (cursor != null) {

                                while (cursor.moveToNext()) {
                                    int dataColumnIndex = cursor.getColumnIndex(columns[3]);
                                    String picPath = cursor.getString(dataColumnIndex);
                                    if (picPath.equals(resultPath)) {

                                        GridItem item = new GridItem();

                                        item.setThumbnailsPath(null);

                                        /*id*/
                                        item.setMedia_id((int)cursor.getLong(cursor.getColumnIndexOrThrow(columns[0])));
                                        
                                        /*原路徑*/
                                        item.setPath(picPath);

                                        /*選取狀態*/

                                        if (intAlbumCount + intSelectCount < intMaxCount) {
                                            intSelectCount++;
                                            item.setSelect(true);
                                            strSendPath = picPath;
                                        }else {
                                            item.setSelect(false);
                                            MyLog.Set("d", FragmentSelectPhoto2.class, "項目已滿");
                                        }

//                                        item.setSelect(false);
                                        
                                         /*日期*/
                                        long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(columns[2]));
                                        String mTime = paserTimeToYMD(dateTime, "yyyy / MM");
                                        item.setTime(mTime);
                                        
                                        /*時間*/
                                        Long dataDuration = cursor.getLong(cursor.getColumnIndex(columns[4]));
                                        String strDuration = formatter.format(dataDuration);
                                        item.setDuration(strDuration);

                                        nonHeaderIdList.add(0, item);

                                        break;
                                    }
                                }

                                cursor.close();

                                isFromPPBCamera = true;

                                hasHeaderIdList = generateHeaderId(nonHeaderIdList);

                                Collections.sort(hasHeaderIdList, new YMDComparator());

                                adapter.notifyDataSetChanged();

                            }
                        }
                    }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {

        } else {

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private final int SUCCESS = 0;
    private final int REFUSE = -1;

    private int checkPermission(Activity ac, String permission){

        int doingType = 0;

        if (ActivityCompat.checkSelfPermission(ac, permission) == PackageManager.PERMISSION_GRANTED) {
            //已授權
            doingType = SUCCESS;
        }else {
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

                dispatchTakeVideoIntent();
            }
        },500);


    }

    @PermissionDenied(REQUEST_CODE_CAMERA)
    public void requestCameraFailed() {

        if(!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),  Manifest.permission.CAMERA)){

            MyLog.Set("d" , getClass(), "shouldShowRequestPermissionRationale =======> false");

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

        }else {

            MyLog.Set("d" , getClass(), "shouldShowRequestPermissionRationale =======> true");

        }

    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }


    @Override
    public void onPause() {
        cleanPicasso();
        super.onPause();
    }


    @Override
    public void onDestroy() {


        if (getAlbumContentTask != null && !getAlbumContentTask.isCancelled()) {
            getAlbumContentTask.cancel(true);
        }
        getAlbumContentTask = null;


        if (nonHeaderIdList != null && nonHeaderIdList.size() > 0) {
            int count = nonHeaderIdList.size();
            for (int i = 0; i < count; i++) {
                Picasso.with(getActivity().getApplicationContext()).invalidate(nonHeaderIdList.get(i).getPath());
            }
            nonHeaderIdList.clear();
            nonHeaderIdList = null;
        }

        lruCache.evictAll();


        Recycle.IMG(backImg);
        Recycle.IMG(cameraImg);


        System.gc();

        MyLog.Set("d", FragmentSelectVideo2.class, "onDestroy");
        super.onDestroy();
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.BitmapUtility;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.bean.GridItem;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetVideoImage;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.UserGradeChange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewVisibility;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentFromShareText;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin9594 on 2017/5/13.
 */
public class FromSharePhoto2Activity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity = this;

    private FragmentFromShareText fragmentFromShareText;

    private SharedPreferences getdata;
    private DialogV2Custom dlgLogin, dlgCheckEdit;


    private FastCreateTask fastCreateTask;
    private UpLoadTask upLoadTask;

    private Handler mHandler;
    private Handler upLoadHandler;

    private List<GridItem> photoList;
    private List<GridItem> sendList;
    private List<Boolean> booleanList;

    private String id, token;
    private String newAlbum_id;
    private String p58Message = "";

    private static final int REQUEST_CODE_SDCARD = 105;
    private static final int COMMON_SIZE_UPLOAD = 1;
    private static final int ORIGINAL_SIZE_UPLOAD = 2;
    private int p58Result = -1;
    private int doingType;
    private int uploadType;
    private int intMaxCount;
    private int selectCount = 0;
    private int total;
    private int upCount;
    private int round;
    private int totalRound;
    private int lastRound;
    private int onceCount;
    private int intAlbumCount;
    private int screenWidth;
    private int screenHeight;

    private LinearLayout linLoad;
    private RelativeLayout rSelect, rBottom;
    private ImageView backImg;
    private TextView tvCount, tvStartUpLoad, tvOriginalSize, tvUpLoadCount, tvFileName;
    private GridView gvPhoto;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSwipeBackEnable(false);

        checkID();

    }


    private void checkID() {

        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString(Key.id, "");
        if (id.equals("")) {

            dlgLogin = new DialogV2Custom(mActivity);
            dlgLogin.setStyle(DialogStyleClass.CHECK);
            dlgLogin.setMessage(R.string.pinpinbox_2_0_0_dialog_message_please_login);
            dlgLogin.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_to_login);
            dlgLogin.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {

                    List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
                    int count = activityList.size();
                    if (count > 0) {
                        for (int i = 0; i < count; i++) {
                            activityList.get(i).finish();
                        }
                    }

                    Intent intent = new Intent(mActivity, FirstInstallActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);
                }
            });
            dlgLogin.getTvLeftOrTop().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlgLogin.dismiss();
                    finish();
                }
            });

            dlgLogin.getDarkBg().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlgLogin.dismiss();
                    finish();
                }
            });
            dlgLogin.show();
            return;
        } else {
            token = getdata.getString(Key.token, "");
        }

        boolean cancelActivity = false;

        final List<Activity> activityList = SystemUtility.SysApplication.getInstance().getmList();
        final int count = activityList.size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String strAcName = activityList.get(i).getClass().getSimpleName();
                if (strAcName.equals(CreationActivity.class.getSimpleName()) || strAcName.equals(AlbumSettingsActivity.class.getSimpleName())) {
                    cancelActivity = true;
                }
            }
        }
        if (cancelActivity) {
            dlgCheckEdit = new DialogV2Custom(mActivity);
            dlgCheckEdit.setStyle(DialogStyleClass.CHECK);
            dlgCheckEdit.setMessage(R.string.pinpinbox_2_0_0_dialog_message_check_album_edit);
            dlgCheckEdit.setOrientation(LinearLayout.VERTICAL);
            dlgCheckEdit.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_exit_last_schedule);
            dlgCheckEdit.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_close_view);

            dlgCheckEdit.setCheckExecute(new CheckExecute() {
                @Override
                public void DoCheck() {
                    for (int i = 0; i < count; i++) {
                        String strAcName = activityList.get(i).getClass().getSimpleName();
                        if (strAcName.equals(CreationActivity.class.getSimpleName())
                                || strAcName.equals(AlbumSettingsActivity.class.getSimpleName())
                                || strAcName.equals(MyCollectActivity.class.getSimpleName())
                                || strAcName.equals(AlbumGroupActivity.class.getSimpleName())) {
                            activityList.get(i).finish();
                        }
                    }

                    checkTextOrImage();
                }
            });

            dlgCheckEdit.getTvLeftOrTop().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlgCheckEdit.dismiss();
                    finish();
                }
            });

            dlgCheckEdit.getDarkBg().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dlgCheckEdit.dismiss();
                    finish();
                }
            });

            dlgCheckEdit.show();

        } else {
            checkTextOrImage();
        }


    }

    private void checkTextOrImage() {


        Intent intent = getIntent();

        MyLog.Set("e", this.getClass(), "intent.toString() => " + intent.toString());

        if (intent != null && intent.getType().equals("text/plain")) {

            String url = intent.getStringExtra(Intent.EXTRA_TEXT);

            MyLog.Set("e", this.getClass(), "urlurlurl => " + url);

            /*開啟我的作品 群組作品*/

            showFromShareText(url);


        } else {


            switch (checkPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                case SUCCESS:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fromSelectPhoto();
                        }
                    }, 200);
                    break;
                case REFUSE:
                    MPermissions.requestPermissions(mActivity, REQUEST_CODE_SDCARD, Manifest.permission.READ_EXTERNAL_STORAGE);
                    break;

            }

        }


    }


    private void showFromShareText(String text){

        setContentView(R.layout.activity_2_0_0_from_share_photo);

        Bundle bundle = new Bundle();
        bundle.putString(Key.text, text);

        fragmentFromShareText = new FragmentFromShareText();
        fragmentFromShareText.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.frame, fragmentFromShareText).commit();

    }



    private void fromSelectPhoto() {

        setContentView(R.layout.activity_2_0_0_from_share_photo);

        init();

        getPhoto();

        setPhoto();

        setHandler();

    }

    private void getPhoto() {


        try {

            photoList = SystemUtility.getFromShareListPicPath(this);

            for (int i = 0; i < photoList.size(); i++) {

                if (i + 1 <= intMaxCount) {
                    photoList.get(i).setSelect(true);
                    selectCount++;
                } else {
                    photoList.get(i).setSelect(false);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (photoList != null && photoList.size() > 0) {
            rBottom.setVisibility(View.VISIBLE);
        } else {
            rBottom.setVisibility(View.GONE);
        }

    }

    private void init() {

        booleanList = new ArrayList<>();

        onceCount = 10;
        total = 0;
        round = 0;
        totalRound = 0;
        lastRound = 0;

        uploadType = COMMON_SIZE_UPLOAD;

        screenWidth = ScreenUtils.getScreenWidth();
        screenHeight = ScreenUtils.getScreenHeight();

        linLoad = (LinearLayout) findViewById(R.id.linLoad);
        rSelect = (RelativeLayout) findViewById(R.id.rSelect);
        rBottom = (RelativeLayout) findViewById(R.id.rBottom);

        backImg = (ImageView) findViewById(R.id.backImg);

        tvCount = (TextView) findViewById(R.id.tvCount);
        tvStartUpLoad = (TextView) findViewById(R.id.tvStartUpLoad);
        tvOriginalSize = (TextView) findViewById(R.id.tvOriginalSize);
        tvUpLoadCount = (TextView) findViewById(R.id.tvUpLoadCount);
        tvFileName = (TextView) findViewById(R.id.tvFileName);

        gvPhoto = (GridView) findViewById(R.id.gvPhoto);

        backImg.setOnClickListener(this);
        tvStartUpLoad.setOnClickListener(this);
        tvOriginalSize.setOnClickListener(this);


        intMaxCount = StringIntMethod.StringToInt(
                UserGradeChange.PicCountByUserGrade(
                        (getdata.getString(Key.usergrade, "22"))
                )
        );

        rSelect.setVisibility(View.VISIBLE);

    }

    private void setPhoto() {

        setCount();

        final PhotoAdapter adapter = new PhotoAdapter();
        gvPhoto.setAdapter(adapter);

        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long l) {

                boolean isSelect = photoList.get(position).isSelect();

                if (isSelect) {

                    photoList.get(position).setSelect(false);
                    selectCount--;

                } else {

                    if (selectCount >= intMaxCount) {
                        PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_count_max);
                        return;
                    }

                    photoList.get(position).setSelect(true);
                    selectCount++;
                }

                adapter.notifyDataSetChanged();

                if (selectCount > 0) {
                    rBottom.setVisibility(View.VISIBLE);
                } else {
                    rBottom.setVisibility(View.GONE);
                }

                setCount();

            }
        });

    }

    private void setCount() {
        tvCount.setText(selectCount + "/" + intMaxCount);
    }

    private void setSendPathList() {

        if (sendList == null) {
            sendList = new ArrayList<>();
        } else {
            sendList.clear();
        }

        int count = photoList.size();

        for (int i = 0; i < count; i++) {

            boolean b = photoList.get(i).isSelect();

            if (b) {
                GridItem item = photoList.get(i);
                sendList.add(item);
            }
        }

    }

    private void setHandler() {
        /*防止crash 遺失銀幕長寬*/
        DensityUtility.setScreen(this);

        PPBApplication.getInstance().setSharedPreferences(getdata);


        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            setUploadCount();
                            break;
                    }

                }
            };
        }
        upLoadHandler = new Handler() {

            private void upLoadFinish() {

                FileUtility.delAllFile(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCopy);
                MyLog.Set("d", getClass(), "FileUtility.deleteFileFolder(new File(sdPath + myDir + copy))");


                Bundle bundle = new Bundle();
                bundle.putString("album_id", newAlbum_id);
                bundle.putString("identity", "admin");
                bundle.putInt("create_mode", 0);
                bundle.putBoolean(Key.isNewCreate, true);

                bundle.putBoolean(Key.fromlocal, true);

                Intent intent = new Intent(mActivity, CreationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }

            @Override
            public void handleMessage(final Message msg) {

                final int generalW = screenWidth / 2;
                final int generalH = screenHeight / 2;

                switch (msg.what) {
                    case 0:

                        //第幾輪
                        round++;
                        MyLog.Set("e", mActivity.getClass(), "第 " + round + " 輪上傳");

                        //每輪的最大上限值
                        totalRound = round * onceCount;

                        //lastRound 為上一輪最大值
                        for (int i = lastRound; i < totalRound; i++) {
                            final String copyPath = DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCopy + i + "copy.jpg";
                            final String path = sendList.get(i).getPath();
                            final int degree = sendList.get(i).getDegree();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Bitmap smallBitmap = null;
                                    MyLog.Set("d", mActivity.getClass(), "上傳一般尺寸");
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
                                    protocol58(Fto);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (p58Result == 1) {
                                                if (booleanList.size() == sendList.size()) {

                                                    upLoadFinish();

                                                    return;
                                                }
                                                if (booleanList.size() == totalRound) {
                                                    MyLog.Set("d", mActivity.getClass(), "傳送5張了 準備執行下一輪");

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
                                                DialogV2Custom.BuildError(mActivity, p58Message);
                                            } else {
                                                DialogV2Custom.BuildUnKnow(mActivity, mActivity.getClass().getSimpleName());
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }


                        break;

                    case 1:


                        for (int i = totalRound; i < sendList.size(); i++) {
                            final String copyPath = DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCopy + i + "copy.jpg";
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
                                    protocol58(Fto);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (p58Result == 1) {
                                                if (booleanList.size() == sendList.size()) {
                                                    upLoadFinish();
                                                }
                                            } else if (p58Result == 0) {
                                                DialogV2Custom.BuildError(mActivity, p58Message);
                                            } else {
                                                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
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

    private void setUploadCount() {

        tvUpLoadCount.setText(upCount + "/" + sendList.size());

    }

    private void setFileName(String name) {

        tvFileName.setText(name);

    }

    private void protocol58(File Fto) {

        Map<String, String> data = new HashMap<>();
        data.put("id", id);
        data.put("token", token);
        data.put("album_id", newAlbum_id);
        String sign = IndexSheet.encodePPB(data);
        Map<String, String> sendData = new HashMap<>();
        sendData.put("id", id);
        sendData.put("token", token);
        sendData.put("album_id", newAlbum_id);
        sendData.put("sign", sign);

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

                String photo = JsonUtility.GetString(j, "photo");
                JSONArray jsonArray = new JSONArray(photo);

                intAlbumCount = jsonArray.length();

                booleanList.add(true);

                upCount++;

                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);


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

    private void cleanPicasso() {

        if (photoList != null && photoList.size() > 0) {
            for (int i = 0; i < photoList.size(); i++) {
                Picasso.get().invalidate(photoList.get(i).getPath());
            }

        }
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoFastCreate:
                        doFastCreate();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);
    }

    private void doFastCreate() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        fastCreateTask = new FastCreateTask();
        fastCreateTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class FastCreateTask extends AsyncTask<Void, Void, Object> {

        private int p54Result = -1;
        private String p54Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoFastCreate;
        }

        @Override
        protected Object doInBackground(Void... params) {

            protocol54();

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (p54Result == 1) {

                if (newAlbum_id != null) {

                    upLoadTask = new UpLoadTask(sendList);
                    upLoadTask.execute();

                }

            } else if (p54Result == 0) {

                DialogV2Custom.BuildError(mActivity, p54Message);


            } else if (p54Result == Key.TIMEOUT) {
                connectInstability();
            } else {

                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());

            }

        }


        private void protocol54() {

            String strJson = "";
            try {

                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P54_InsertAlbumOfDiy,
                        SetMapByProtocol.setParam54_insertalbumofdiy(id, token, "0"), null);
                MyLog.Set("d", getClass(), "p54strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p54Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p54Result = JsonUtility.GetInt(jsonObject, Key.result);
                    if (p54Result == 1) {
                        newAlbum_id = JsonUtility.GetString(jsonObject, Key.data);
                    } else if (p54Result == 0) {
                        p54Message = JsonUtility.GetString(jsonObject, Key.message);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }


    }

    /*protocol58*/
    @SuppressLint("StaticFieldLeak")
    private class UpLoadTask extends AsyncTask<Void, Void, Object> {

        private List<GridItem> itemList;

        private File fileMedia;

        private String strUploadMessage = "";

        private int upLoadResult = -1;
        private int generalW = screenWidth / 2;
        private int generalH = screenHeight / 2;


        private UpLoadTask(List<GridItem> itemList) {

            this.itemList = itemList;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            int count = itemList.size();

            for (int i = 0; i < count; i++) {

                String path = itemList.get(i).getPath();

                /*------------------------------------------------------------------------------------------------------------------------------------------------------*/

                fileMedia = new File(path);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFileName(fileMedia.getName());
                    }
                });

                /*------------------------------------------------------------------------------------------------------------------------------------------------------*/

                Map<String, String> data = new HashMap<>();
                data.put("id", id);
                data.put("token", token);
                data.put("album_id", newAlbum_id);
                String sign = IndexSheet.encodePPB(data);
                Map<String, String> sendData = new HashMap<>();
                sendData.put("id", id);
                sendData.put("token", token);
                sendData.put("album_id", newAlbum_id);
                sendData.put("sign", sign);

                String strJson = "";

                try {
                    if (FileUtility.isVideo_mp4(fileMedia.getName())) {

                        strJson = HttpUtility.uploadSubmit(false, ProtocolsClass.P80_InsertVideoOfDiy, sendData, fileMedia);

                    } else if (FileUtility.isImage(fileMedia.getName())) {
                        if (uploadType == COMMON_SIZE_UPLOAD) {
                            final String copyPath = DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCopy + i + "copy.jpg";
                            final int degree = sendList.get(i).getDegree();
                            Bitmap smallBitmap = null;
                            MyLog.Set("d", mActivity.getClass(), "執行 => 上傳一般尺寸");
                            try {
                                smallBitmap = BitmapUtility.getSmallBitmap(path, generalW, generalH, degree);
                                BitmapUtility.saveToLocal(smallBitmap, copyPath, 100);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (smallBitmap != null) {
                                smallBitmap.recycle();
                            }
                            fileMedia = new File(copyPath);
                        }
                        strJson = HttpUtility.uploadSubmit(false, ProtocolsClass.P58_InsertPhotoOfDiy, sendData, fileMedia);
                    }


                    MyLog.Set("d", getClass(), "upload strJson => " + strJson);

                    JSONObject jsonObject = new JSONObject(strJson);
                    upLoadResult = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (upLoadResult == 1) {

                        MyLog.Set("d", getClass(), "上傳成功");

                        String jdata = jsonObject.getString(ProtocolKey.data);

                        JSONObject j = new JSONObject(jdata);

                        String usergrade = j.getString(ProtocolKey.usergrade);
                        JSONObject uj = new JSONObject(usergrade);
                        intMaxCount = uj.getInt("photo_limit_of_album");

                        String photo = JsonUtility.GetString(j, "photo");
                        JSONArray jsonArray = new JSONArray(photo);

                        intAlbumCount = jsonArray.length();

                        MyLog.Set("d", getClass(), "目前張數 => " + jsonArray.length());

                        upCount = i + 1;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setUploadCount();
                            }
                        });

                        if (intAlbumCount >= intMaxCount) {
                            break;
                        }

                    } else if (upLoadResult == 0) {

                        strUploadMessage = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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


            if (upLoadResult == 1) {

                FileUtility.delAllFile(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCopy);
                MyLog.Set("d", getClass(), "FileUtility.deleteFileFolder(new File(sdPath + myDir + copy))");

                Bundle bundle = new Bundle();
                bundle.putString("album_id", newAlbum_id);
                bundle.putString("identity", "admin");
                bundle.putInt("create_mode", 0);
                bundle.putBoolean(Key.isNewCreate, true);

                bundle.putBoolean(Key.fromlocal, true);

                Intent intent = new Intent(mActivity, CreationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();


            } else if (upLoadResult == 0) {
                DialogV2Custom.BuildError(mActivity, strUploadMessage);
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }

    }

    private class PhotoAdapter extends BaseAdapter {

        private SetVideoImage setVideoImage;

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public Object getItem(int i) {
            return photoList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder mViewHolder;
            if (convertView == null) {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_2_0_0_photo, parent, false);
                mViewHolder.photoImg = convertView.findViewById(R.id.image);
                mViewHolder.videoImg = convertView.findViewById(R.id.videoImg);
                mViewHolder.vSelect = convertView.findViewById(R.id.vSelect);
                convertView.setTag(mViewHolder);

            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
                mViewHolder.photoImg.setImageBitmap(null);
                mViewHolder.videoImg.setImageResource(0);
            }


            boolean isSelect = photoList.get(position).isSelect();
            if (isSelect) {
                mViewHolder.vSelect.setBackgroundResource(R.drawable.icon_select_pink500_120x120);
                mViewHolder.photoImg.setAlpha(0.6f);
            } else {
                mViewHolder.vSelect.setBackgroundResource(R.drawable.icon_unselect_teal500_120x120);
                mViewHolder.photoImg.setAlpha(1.0f);
            }


            String path = photoList.get(position).getPath();

            File file = new File(path);

            if (FileUtility.isImage(file.getName())) {

                mViewHolder.videoImg.setVisibility(View.GONE);

                if (path != null && !path.equals("")) {
                    ImageUtility.setFileImageToGrid(mActivity, mViewHolder.photoImg, file);
                } else {
                    mViewHolder.photoImg.setImageResource(R.drawable.bg_2_0_0_no_image);
                }

            }


            if (FileUtility.isVideo_mp4(file.getName())) {

                mViewHolder.videoImg.setVisibility(View.VISIBLE);

                if (SetVideoImage.getVideoThumb(path) == null) {

                    if (setVideoImage == null) {
                        setVideoImage = new SetVideoImage(mActivity);
                    }

                    setVideoImage.start(photoList.get(position).getMedia_id(), path, mViewHolder.photoImg);

                } else {

                    mViewHolder.photoImg.setImageBitmap(SetVideoImage.getVideoThumb(path));

                }

            }


            return convertView;
        }

        public class ViewHolder {
            public ImageView photoImg, videoImg;
            public View vSelect;

        }

    }

    public Fragment getFragment(String fragmentName) {

        @SuppressLint("RestrictedApi")
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        Fragment getFragment = null;

        for (int i = 0; i < fragmentList.size(); i++) {

            Fragment fragment = fragmentList.get(i);

            if (fragment.getClass().getSimpleName().equals(fragmentName)) {
                getFragment = fragment;
                MyLog.Set("d", getClass(), "fragmentName => " + fragmentName);
                break;
            }
        }

        return getFragment;
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.backImg:
                finish();
                break;

            case R.id.tvStartUpLoad:

                setSendPathList();

                ViewVisibility.setGone(rSelect);
                ViewVisibility.setVisible(linLoad);

                uploadType = COMMON_SIZE_UPLOAD;

                doFastCreate();

                break;

            case R.id.tvOriginalSize:

                if (uploadType == COMMON_SIZE_UPLOAD) {
                    uploadType = ORIGINAL_SIZE_UPLOAD;
                    tvOriginalSize.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                } else {
                    uploadType = COMMON_SIZE_UPLOAD;
                    tvOriginalSize.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                }

                break;
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
            doingType = REFUSE;

        }
        return doingType;

    }


    @PermissionGrant(REQUEST_CODE_SDCARD)
    public void requestSdcardSuccess() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fromSelectPhoto();
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
            d.getmDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    finish();
                }
            });

        } else {
            MyLog.Set("d", getClass(), "shouldShowRequestPermissionRationale =======> true");
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (dlgLogin != null && dlgLogin.getmDialog().isShowing()) {
            dlgLogin.dismiss();
        }

        if (dlgCheckEdit != null && dlgCheckEdit.getmDialog().isShowing()) {
            dlgCheckEdit.dismiss();
        }

        finish();
    }


    @Override
    public void onDestroy() {

        FileUtility.delAllFile(DirClass.ExternalFileDir(mActivity) + DirClass.getMyDir(id) + DirClass.dirCopy);

        if (fastCreateTask != null && !fastCreateTask.isCancelled()) {
            fastCreateTask.cancel(true);
        }
        fastCreateTask = null;

        if (upLoadTask != null && !upLoadTask.isCancelled()) {
            upLoadTask.cancel(true);
        }
        upLoadTask = null;

        if (SetVideoImage.lruCache != null) {
            SetVideoImage.lruCache.evictAll();
            SetVideoImage.lruCache = null;
        }


        cleanPicasso();

        Recycle.IMG(backImg);

        System.gc();
        super.onDestroy();
    }
}

package com.pinpinbox.android.SelfMadeClass;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pinpinbox.android.Activity.ReadAlbum.ReadAlbumActivity;
import com.pinpinbox.android.DialogTool.DialogSet;
import com.pinpinbox.android.R;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.OkHttpClientManager;
import com.pinpinbox.android.Utility.ZipUtility;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.NoConnect;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ch.halcyon.squareprogressbar.SquareProgressBar;
import ch.halcyon.squareprogressbar.utils.PercentStyle;


/**
 * Created by vmage on 2015/12/9.
 */
public class AlbumDownLoad {

    private SharedPreferences getdata;
    private Activity mActivity;
    private Dialog crdlg;
    private DialogSet dlgCancelDownload;
    private NoConnect noConnect;
    private LoadingAnimation loading;

    private FinishGetTask finishGetTask;

    private DownloadTask downloadTask;

    private DownloadBean bean;
    private BtnListener btnListener;
    private Bitmap downBackGroundBmp;

    public static final int REFRESH = 1;
    private int pbResult;
    private int doingType;
    private static final int DoFinishGetTask = 0;


    private boolean downloadFinish = false;
    private boolean buy;//判斷是否第一次購買

    //    private ImageView coverImg;
//    private ImageView downloadActionImg;
//    private ProgressBar downloadProgressBar;
    private SquareProgressBar squareProgressBar;

    private String TAG = AlbumDownLoad.class.getSimpleName();
    private String album_id;
    private String download_id;
    private String id, token;
    private String sdPath = Environment.getExternalStorageDirectory() + "/";
    private String dirAlbumList = "albumlist/";
    private String dirZip = "zip/";
    private String myDir;
    private String strCover;
    private String p15Result, p15Message;

    private Handler mHandler;

    private Handler progressbarkHandler = new Handler() {
        public void handleMessage(Message msg) {

            squareProgressBar.setProgress(getProgressInt(bean, 100));

//            squareProgressBar.setProgress(getProgressInt(bean, downloadProgressBar.getMax()));

//            pbResult = getProgressInt(bean, downloadProgressBar.getMax());
//            downloadProgressBar.setProgress(pbResult);
//            downloadActionImg.setEnabled(isEnable(bean));
        }
    };

    public DownloadTask getDownloadTask() {
        return this.downloadTask;
    }


    public Bitmap getDownBackGroundBmp() {
        return this.downBackGroundBmp;
    }

    private Handler fileHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {//Handler 處理下載線程和UI間的通訊
            if (!Thread.currentThread().isInterrupted()) {
                switch (message.what) {
                    case 0:
                        downloadFinish = false;

                        break;
                    case 1://解壓縮

                        MyLog.Set("d", getClass(), "in unzip");
                        ZipUtility.Unzip(sdPath + myDir + dirZip + album_id, sdPath + myDir + dirAlbumList + album_id + "/");

                        if (buy) {
                            protocol15();//通知server下載完成
                        } else {
                            Message msg = new Message();
                            msg.what = 0;
                            mHandler.sendMessage(msg);
                        }

                        break;

                    case 2:


                        MyLog.Set("d", getClass(), "delete zip");

                        deleteZipFile();

                        //2016.06.09 clean cover

                        if (strCover != null && !strCover.equals("")) {
                            Picasso.with(mActivity.getApplicationContext()).invalidate(strCover);
                            System.gc();
                        }


                        break;
                    case -1:
                        String error = message.getData().getString("error");
                        Toast.makeText(mActivity, error, Toast.LENGTH_SHORT).show();
                        break;


                    case 99:
                        if (bean.size > 0 && bean.loadedSize == bean.size) {
                            String localPath = sdPath + myDir + dirZip + bean.name;
                            File tmpFile = new File(localPath + ".tmp");
                            tmpFile.renameTo(new File(localPath));
                            bean.enable = false;
                            bean.state = DownloadBean.STATE_COMPLETED;

                            Message msg = new Message();
                            msg.what = 1;
                            fileHandler.sendMessage(msg);


                        } else {
                            if (!bean.enable) {
                                bean.state = DownloadBean.STATE_INTERRUPTED;
                            } else {
                                bean.state = DownloadBean.STATE_DOWNLOAD_FAIL;
                            }
                        }

                        progressbarkHandler.sendEmptyMessage(REFRESH);

                        break;


                }
            }
            super.handleMessage(message);
        }
    };


    public void getAlbumDownLoad(Activity activity, String album_id, String download_id
            , Handler finishHandler, boolean buy) {
        this.mActivity = activity;
        this.album_id = album_id;
        this.download_id = download_id;
        this.mHandler = finishHandler;
        this.buy = buy;


        setView();
        initBeanDownload();
        init();
        doDownload();


    }


    private void setView() {
        crdlg = new Dialog(mActivity, R.style.myDialog);
        Window crosswin = crdlg.getWindow();
        crosswin.setWindowAnimations(R.style.dialog_enter_exit);
        crosswin.setContentView(R.layout.lay_download_album);
        crdlg.show();


        crdlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void initBeanDownload() {
        bean = new DownloadBean();
        bean.url = ProtocolsClass.P14_DownloadAlbumZipFile;
        bean.state = DownloadBean.STATE_INTERRUPTED;
        bean.size = bean.loadedSize = 0l;
        bean.enable = true;
    }

    private void init() {

        getdata = mActivity.getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString("id", "");
        token = getdata.getString("token", "");
        myDir = "/PinPinBox" + id + "/";
        strCover = getdata.getString("album_" + album_id + "_" + "cover", "");

        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        loading = new LoadingAnimation(mActivity);

//        coverImg = (ImageView) crdlg.findViewById(R.id.coverImg);
//        downloadActionImg = (ImageView) crdlg.findViewById(R.id.downloadBtnAction);

        squareProgressBar = (SquareProgressBar) crdlg.findViewById(R.id.squareProgressBar);
        squareProgressBar.setProgress(100);
        squareProgressBar.setColor("#009688");
        squareProgressBar.setWidth(12);
        squareProgressBar.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
        squareProgressBar.getImageView().setLayoutParams(new RelativeLayout.LayoutParams(dm.widthPixels, dm.heightPixels));
        squareProgressBar.setImageGrayscale(true);//畫面恢暗
        squareProgressBar.showProgress(true);//顯示數字進度
        PercentStyle percentStyle = new PercentStyle(Paint.Align.CENTER, 150, true);
        percentStyle.setTextColor(Color.parseColor("#009688"));
        squareProgressBar.setPercentStyle(percentStyle);
        squareProgressBar.drawOutline(true);


//        downloadProgressBar = (ProgressBar) crdlg.findViewById(R.id.downloadProgressBar);
//        downloadProgressBar.setProgress(getProgressInt(bean, downloadProgressBar.getMax()));

        btnListener = new BtnListener();


        if (strCover != null && !strCover.equals("")) {

//            Picasso.with(mActivity.getApplicationContext())
//                    .load(strCover)
//                    .config(Bitmap.Config.RGB_565)
//                    .error(R.drawable.no_image)
//                    .tag(mActivity.getApplicationContext())
//                    .into(coverImg);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        downBackGroundBmp = Picasso.with(mActivity.getApplicationContext())
                                .load(strCover)
                                .config(Bitmap.Config.RGB_565)
                                .error(R.drawable.no_image)
                                .tag(mActivity.getApplicationContext())
                                .get();


                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                squareProgressBar.setImageBitmap(downBackGroundBmp);

                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }


    }

    class BtnListener implements View.OnClickListener {
        public void onClick(View v) {

            switch (bean.state) {
                case DownloadBean.STATE_LOADING:
                    System.out.println("點擊了暫停");
                    pauseDownload();
                    break;
                case DownloadBean.STATE_INTERRUPTED:
                    System.out.println("點擊了繼續");
                    doDownload();
                    break;
                case DownloadBean.STATE_DOWNLOAD_FAIL:
                    System.out.println("點擊了重載");
                    reloadDownload();
                    break;
                case DownloadBean.STATE_COMPLETED:
                    System.out.println("點擊了安裝");
                    break;
            }

        }
    }


    private void pauseDownload() {
        bean.enable = false;
        progressbarkHandler.sendEmptyMessage(REFRESH);
//        downloadActionImg.setImageResource(R.drawable.button_download_play);
//        tvDownload.setVisibility(View.VISIBLE);
//        tvDownload.setText(getProgressTxt(bean));

    }

    private void doDownload() {
        progressbarkHandler.sendEmptyMessage(REFRESH);

        downloadTask = new DownloadTask();
        downloadTask.execute();

    }

    public void deleteZipFile() {

        File deleteZip = new File(sdPath + myDir + dirZip + album_id);

        if (deleteZip.exists()) {

            deleteZip.delete();

        }

    }

    private void reloadDownload() {
        bean.size = bean.loadedSize = 0;
        bean.enable = true;
        File deleteZip = new File(sdPath + myDir + dirZip + album_id);
        deleteZip.delete();
        doDownload();
    }

    public static int getProgressInt(DownloadBean bean, int max) {
        return (bean.size > 0) ? (int) (bean.loadedSize * max * 1.0 / bean.size) : 0;
    }

    private boolean isEnable(DownloadBean bean) {
        boolean enable = true;
        if (!bean.enable && bean.state == DownloadBean.STATE_LOADING) {
            enable = false;
        }
        return enable;
    }

    public static String getProgressTxt(DownloadBean bean) {
        String resStr = "0%";
        if (bean.size != 0) {
            double result = bean.loadedSize * 1.0 / bean.size;
            DecimalFormat decFormat = new DecimalFormat("#%");
            resStr = decFormat.format(result);
        }
        return resStr;
    }

    public Dialog getCrdlg() {
        return this.crdlg;
    }


    private void protocol15() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Map<String, String> data = new HashMap<String, String>();
                    data.put("id", id);
                    data.put("token", token);
                    data.put("download_id", download_id);
                    String sign = IndexSheet.encodePPB(data);
                    Map<String, String> sendData = new HashMap<String, String>();
                    sendData.put("id", id);
                    sendData.put("token", token);
                    sendData.put("download_id", download_id);
                    sendData.put("sign", sign);
                    String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P15_FinishGetAlbum, sendData, null);//15
                    JSONObject object = new JSONObject(strJson);
                    p15Result = object.getString("result");


                } catch (Exception e) {
                    e.printStackTrace();
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (p15Result.equals("1")) {

                            downloadFinish = true;

                            Message msg = new Message();
                            msg.what = 0;
                            mHandler.sendMessage(msg);
//                            crdlg.dismiss();
                        }
                    }
                });
            }
        }).start();

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {
                    case DoFinishGetTask:
                        doFinishGet();
                        break;
                }
            }
        };

        DialogSet d = new DialogSet(mActivity);
        d.ConnectInstability();
        d.setConnectInstability(connectInstability);

    }

    private void doFinishGet() {

        if (!HttpUtility.isConnect(mActivity)) {
            noConnect = new NoConnect(mActivity);
            ((ReadAlbumActivity) mActivity).setNoConnect(noConnect);
            return;
        }

        finishGetTask = new FinishGetTask();
        finishGetTask.execute();
    }

    private class DownloadTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected Object doInBackground(Void... params) {
            bean.name = album_id;
            bean.state = DownloadBean.STATE_LOADING;
            bean.enable = true;
            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("albumid", album_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("albumid", album_id);
            sendData.put("sign", sign);


            final String dir = sdPath + myDir + dirZip;
            final String name = bean.name + ".tmp";

            OkHttpClientManager.Param[] param = OkHttpClientManager.map2Params(sendData);
            try {
                Response response = OkHttpClientManager.post(bean.url, param);


                OkHttpClient okHttpClient = OkHttpClientManager.getInstance().getOKHttp();
                Call call = okHttpClient.newCall(response.request());
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        InputStream is = null;
                        byte[] buf = new byte[4096];
                        int len = 0;
                        FileOutputStream fos = null;
                        try {
                            is = response.body().byteStream();
                            File file = new File(dir, name);
                            fos = new FileOutputStream(file);

                            bean.size = response.body().contentLength();
                            Message msg = new Message();
                            msg.what = 0;
                            fileHandler.sendMessage(msg);

                            try {
                                String time = response.header("Last-Modified");

                                SimpleDateFormat Gmt = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

                                Date d1 = new Date(Gmt.parse(time).getTime());

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                                String strModifiedTime = sdf.format(d1);

                                Log.e("strModifiedTime", strModifiedTime);
                                long epoch = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(strModifiedTime).getTime() / 1000;

                                Log.e("epoch", epoch + "");

                                getdata.edit().putLong(album_id + "_modified_time", epoch).commit();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                                bean.loadedSize += len;
                                progressbarkHandler.sendEmptyMessage(REFRESH);

                            }
                            fos.flush();

                            Message msg99 = new Message();
                            msg99.what = 99;
                            fileHandler.sendMessage(msg99);

                        } catch (IOException e) {

                            e.printStackTrace();

                            Log.e("AlbumDownLoad", "IOException");

                            Message msg = new Message();
                            msg.what = 2;
                            fileHandler.sendMessage(msg);

                        } finally {
                            try {
                                if (is != null) is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                if (fos != null) fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });


            } catch (Exception e) {

                Message msg = new Message();
                msg.what = 2;
                fileHandler.sendMessage(msg);
                e.printStackTrace();
            }


            return null;
        }


    }

    private class FinishGetTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoFinishGetTask;
            loading.show();
        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("download_id", download_id);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("download_id", download_id);
            sendData.put("sign", sign);

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P15_FinishGetAlbum, sendData, null);//15
                MyLog.Set("d", getClass(), "p15strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p15Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p15Result = jsonObject.getString(Key.message);
                    if (p15Result.equals("1")) {

                    } else if (p15Result.equals("0")) {
                        p15Message = jsonObject.getString(Key.message);
                    } else {
                        p15Result = "";
                    }

                } catch (Exception e) {
                    p15Result = "";
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();

            if (p15Result.equals("1")) {

                downloadFinish = true;
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);

            } else if (p15Result.equals("0")) {

            } else if (p15Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogSet d = new DialogSet(mActivity);
                d.DialogUnKnow();
            }


        }
    }


}

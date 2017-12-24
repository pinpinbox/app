package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.pinpinbox.android.DialogTool.DialogV2Custom;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Scan.camera.CameraManager;
import com.pinpinbox.android.Scan.decoding.CaptureActivityHandler;
import com.pinpinbox.android.Scan.decoding.DecodeHandlerInterface;
import com.pinpinbox.android.Scan.decoding.InactivityTimer;
import com.pinpinbox.android.Scan.view.ViewfinderView;
import com.pinpinbox.android.SelfMadeClass.ConnectInstability;
import com.pinpinbox.android.SelfMadeClass.IndexSheet;
import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.SelfMadeClass.PPBApplication;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Widget.ActivityAnim;
import com.pinpinbox.android.Widget.Key;
import com.pinpinbox.android.Widget.MapKey;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.Widget.NoConnect;
import com.pinpinbox.android.Widget.PPBWidget;
import com.pinpinbox.android.Widget.PinPinToast;
import com.pinpinbox.android.Widget.SetMapByProtocol;
import com.pinpinbox.android.Widget.Value;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumInfo2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumSettings2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Login2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Main2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Reader2Activity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by vmage on 2015/9/16.
 */
public class FragmentScanSearch2 extends Fragment implements SurfaceHolder.Callback, DecodeHandlerInterface, View.OnTouchListener {

    private NoConnect noConnect;
    private FragmentScanSearch2 fragmentScanSearch2;

    private JoinCooperationTask joinCooperationTask;

    private String characterSet;
    private String strCooperationAlbumId = "";

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;

    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
//    private boolean vibrate;
    private SurfaceView surfaceView;


    private View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.lay_search_fragment_scan_search, container, false);
        CameraManager.init(getActivity());
        viewfinderView = (ViewfinderView) v
                .findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(getActivity());

        return v;
    }

    private void setSurfaceView() {
        surfaceView = (SurfaceView) v
                .findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getActivity()
                .getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
//        vibrate = true;
    }

    private String album_id;
    private String p31Result, p31data, p31Message;

    private void getActivityToSetNoConnect() {


        String strActivityName = getActivity().getClass().getSimpleName();

        if (strActivityName.equals(Main2Activity.class.getSimpleName())) {
            ((Main2Activity) getActivity()).setNoConnect();
        } else if (strActivityName.equals(AlbumSettings2Activity.class.getSimpleName())) {
            ((AlbumSettings2Activity) getActivity()).setNoConnect();
        }


    }

    public void handleDecode(final Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        // FIXME

        if (!HttpUtility.isConnect(getActivity())) {
            noConnect = new NoConnect(getActivity());
            getActivityToSetNoConnect();
            return;
        }


        if (resultString.equals("")) {
            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_dialog_message_scan_error);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (handler != null) {   //实现连续扫描
                        handler.restartPreviewAndDecode();
                    }
                }
            }, 2000);


           return;
        } else {

            MyLog.Set("d", getClass(), "resultString => " + resultString);

            BarcodeFormat s = result.getBarcodeFormat();


            /*2016.10.17新增*/
            if (getArguments() != null) {

                boolean isReference = getArguments().getBoolean("reference", false);
                if (isReference) {
                    MyLog.Set("d", getClass(), "isReference => true");

                    if( !s.toString().equals("QR_CODE")){
                        ((AlbumSettings2Activity) getActivity()).getEdScan().setText(resultString);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        transaction.remove(fragmentScanSearch2);
                        transaction.commit();

                    }else {

                        PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_only_barcode);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (handler != null) {   //实现连续扫描
                                    handler.restartPreviewAndDecode();
                                }
                            }
                        }, 2000);

                    }

                    return;
                }


                /*20170922 */
                boolean scanLogin = getArguments().getBoolean(Key.scanLogin,false);

                if(scanLogin){
                    MyLog.Set("d", getClass(), "scanLogin => true");


                    if(s.toString().equals("QR_CODE")){

                        Uri uri = Uri.parse(resultString);

                        String businessuser_id = uri.getQueryParameter(Key.businessuser_id);

                        if(businessuser_id==null || businessuser_id.equals("") || businessuser_id.equals("null")){
                            PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_dialog_message_scan_invalid);
                        }else {
                            ((Login2Activity)getActivity()).doBusiness(businessuser_id);
                        }



                    }else {

                        PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_dialog_message_scan_invalid);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (handler != null) {   //实现连续扫描
                                    handler.restartPreviewAndDecode();
                                }
                            }
                        }, 2000);

                    }



                    return;
                }




            }


            if (s.toString().equals("QR_CODE")) {

                String a = result.getText();

                String arg = a.substring(a.indexOf("?") + 1, a.length());
                String[] strs = arg.split("&");
                HashMap<String, String> map = new HashMap<String, String>();
                for (int x = 0; x < strs.length; x++) {
                    String[] strs2 = strs[x].split("=");
                    if (strs2.length == 2) {

                        MyLog.Set("d", getClass(), strs2[0] + "  " + strs2[1]);

                        map.put(strs2[0], strs2[1]);
                    }
                }


                try {
                    String type = map.get(MapKey.type);

                    String is_cooperation = "";
//                                String type_id = "";
//                                String is_follow = "";

                    if (type != null && !type.equals("") && type.equals(Value.album)) {
                        strCooperationAlbumId = map.get(MapKey.type_id);
                        is_cooperation = map.get(MapKey.is_cooperation);
//                                    is_follow = map.get(MapKey.is_follow);

                        if (is_cooperation.equals("1")) {
                            doJoinCooperation();
                            return;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                MyLog.Set("d", getClass(), "scan album_id => " + album_id);
                album_id = map.get("album_id");
                toAlbumInfo(album_id);//  takePhoto = true;

            } else {
                //ONE-CODE

                strOneCodeResult = result.getText();

                doRetrievealbumpbypn();

            }

        }
    }

    private String strOneCodeResult = "";

    private int doingType;

    private static final int DoRetrievealbumpbypn = 0;
    private static final int DoJoinCooperation = 1;

    private RetrievealbumpbypnTask retrievealbumpbypnTask;

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoRetrievealbumpbypn:
                        doRetrievealbumpbypn();
                        break;
                    case DoJoinCooperation:
                        doJoinCooperation();
                        break;
                }
            }
        };

        DialogV2Custom.BuildTimeOut(getActivity(), connectInstability);

    }

    public void doRetrievealbumpbypn() {

        if (!HttpUtility.isConnect(getActivity())) {
            noConnect = new NoConnect(getActivity());
            getActivityToSetNoConnect();
            return;
        }
        retrievealbumpbypnTask = new RetrievealbumpbypnTask();
        retrievealbumpbypnTask.execute();

    }

    public void doJoinCooperation() {
        if (!HttpUtility.isConnect(getActivity())) {
            noConnect = new NoConnect(getActivity());
            getActivityToSetNoConnect();
            return;
        }
        joinCooperationTask = new JoinCooperationTask();
        joinCooperationTask.execute();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }

    private class RetrievealbumpbypnTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoRetrievealbumpbypn;

        }

        @Override
        protected Object doInBackground(Void... params) {


            String id = PPBApplication.getInstance().getId();
            String token = PPBApplication.getInstance().getToken();
            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("productn", strOneCodeResult);
            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("productn", strOneCodeResult);
            sendData.put("sign", sign);

            try {
                String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P31_Retrievealbumpbypn, sendData, null);
                MyLog.Set("d", getClass(), "p31strJson => " + strJson);

                JSONObject jsonObject = new JSONObject(strJson);
                p31Result = jsonObject.getString(Key.result);
                if (p31Result.equals("1")) {
                    p31data = jsonObject.getString(Key.data);
                } else if (p31Result.equals("0")) {
                    p31Message = jsonObject.getString(Key.message);
                } else {
                    p31Result = "";
                }

            } catch (Exception e) {
                p31Result = "";
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (p31Result.equals("1")) {

                try {
                    JSONObject jsonObject = new JSONObject(p31data);
                    album_id = jsonObject.getString("albumid");
                    toAlbumInfo(album_id);//  takePhoto = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (p31Result.equals("0")) {

                PinPinToast.showErrorToast(getActivity(), p31Message);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (handler != null) {   //实现连续扫描
                            handler.restartPreviewAndDecode();
                        }
                    }
                }, 2000);

//                surfaceView.setVisibility(View.INVISIBLE);
//                viewfinderView.setVisibility(View.INVISIBLE);
//
//                DialogV2Custom d = new DialogV2Custom(getActivity());
//                d.setStyle(DialogStyleClass.ERROR);
//                d.setMessage(p31Message);
//                d.show();
//
//                if (d.getmDialog().isShowing()) {
//                    d.getmDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialogInterface) {
//                            surfaceView.setVisibility(View.VISIBLE);
//                            viewfinderView.setVisibility(View.VISIBLE);
//                        }
//                    });
//
//                }

            } else if (p31Result.equals(Key.timeout)) {
                connectInstability();
            } else {

                PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_dialog_message_scan_invalid);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (handler != null) {   //实现连续扫描
                            handler.restartPreviewAndDecode();
                        }
                    }
                }, 2000);

//                DialogV2Custom.BuildUnKnow(getActivity(), this.getClass().getSimpleName());
            }

        }

    }


    private class JoinCooperationTask extends AsyncTask<Void, Void, Object> {

        private String id = ((PPBApplication) getActivity().getApplicationContext()).getId();
        private String token = ((PPBApplication) getActivity().getApplicationContext()).getToken();

        private String p46Result, p46Message;
        //        private String p63Result;
        private String p17Result, p17Message;

        /**
         * protocol17 params
         */
        private String strZipped = "";


        private LoadingAnimation loading = new LoadingAnimation(getActivity());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.show();
            doingType = DoJoinCooperation;

        }

        @Override
        protected Object doInBackground(Void... params) {

            protocol46();

            /**獲取zipped*/
            if (p46Result.equals("1")) {
                protocol17();
            }

//            /**此作品可下載並判斷作品更新時間*/
//            if (p46Result.equals("1") && p17Result.equals("1") && strZipped.equals("1")) {
//                protocol68();
//            }


            /**權限改成共用者並進入編輯器*/
//            if (p46Result.equals("1")) {
//                protocol63();
//            }
//            if (p63Result.equals("1")) {
//                protocol17();
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            loading.dismiss();
//            && p63Result.equals("1") && p17Result.equals("1")
            if (p46Result.equals("1") && p17Result.equals("1")) {

                surfaceView.setVisibility(View.INVISIBLE);
                viewfinderView.setVisibility(View.INVISIBLE);

                if (!strZipped.equals("1")) {

                    DialogV2Custom d = new DialogV2Custom(getActivity());
                    d.setStyle(DialogStyleClass.ERROR);
                    d.setMessage(R.string.pinpinbox_2_0_0_toast_message_album_save_not_yet);
                    d.show();

                    if (d.getmDialog().isShowing()) {
                        d.getmDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                surfaceView.setVisibility(View.VISIBLE);
                                viewfinderView.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                    return;
                }

                Intent intent = new Intent(getActivity(), Reader2Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, strCooperationAlbumId);
                intent.putExtras(bundle);
                startActivity(intent);
                ActivityAnim.StartAnim(getActivity());

            } else if (p46Result.equals("0")) {
                surfaceView.setVisibility(View.INVISIBLE);
                viewfinderView.setVisibility(View.INVISIBLE);


                DialogV2Custom d = new DialogV2Custom(getActivity());
                d.setStyle(DialogStyleClass.ERROR);
                d.setMessage(p46Message);
                d.show();

                if (d.getmDialog().isShowing()) {
                    d.getmDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            surfaceView.setVisibility(View.VISIBLE);
                            viewfinderView.setVisibility(View.VISIBLE);
                        }
                    });

                }
            } else if (p46Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(getActivity(), this.getClass().getSimpleName());
            }

        }

        /**
         * 加入共用清單
         */
        private void protocol46() {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P46_InsertCooperation, SetMapByProtocol.setParam46_insertcooperation(id, token, Value.album, strCooperationAlbumId, id), null);
                MyLog.Set("d", getActivity().getClass(), "p46strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p46Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p46Result = PPBWidget.GetStringByJsonObject(jsonObject, Key.result);

                if (p46Result.equals("1")) {

                } else if (p46Result.equals("0")) {
                    p46Message = PPBWidget.GetStringByJsonObject(jsonObject, Key.message);
                } else {
                    p46Result = "";
                }

            } catch (Exception e) {
                p46Result = "";
                e.printStackTrace();
            }
        }

        /**
         * 修改權限為共用者
         */
//        private void protocol63() {
//
//            //修改權限  //admin<管理者> / approver<副管理者> / editor<共用者> / viewer<瀏覽者>
//            String strJson = "";
//            try {
//                strJson = HttpUtility.uploadSubmit(true,
//                        ProtocolsClass.P63_UpDateCooperation,
//                        SetMapByProtocol.setParam63_updatecooperation(id, token, Value.album, strCooperationAlbumId, id, "editor"), null);
//
//                MyLog.Set("d", getActivity().getClass(), "p63strJson => " + strJson);
//            } catch (SocketTimeoutException timeout) {
//                p63Result = Key.timeout;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                JSONObject jsonObject = new JSONObject(strJson);
//                p63Result = PPBWidget.GetStringByJsonObject(jsonObject, Key.result);
//
//                if (p63Result.equals("1")) {
//
//                } else if (p63Result.equals("0")) {
//                    p63Message = PPBWidget.GetStringByJsonObject(jsonObject, Key.message);
//                } else {
//                    p63Result = "";
//                }
//
//            } catch (Exception e) {
//                p63Result = "";
//                e.printStackTrace();
//            }
//
//
//        }

        /**
         * 獲取template
         */
        private void protocol17() {
            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P17_GetCloudAlbumList, SetMapByProtocol.setParam17_getcloudalbumlist(id, token, "cooperation", "0,200"), null);
                MyLog.Set("d", getClass(), "p17strJson => cooperation => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p17Result = Key.timeout;
            } catch (Exception e) {
                p17Result = "";
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p17Result = jsonObject.getString(Key.result);
                    if (p17Result.equals("1")) {
                        String jdata = jsonObject.getString(Key.data);
                        JSONArray p17JsonArray = new JSONArray(jdata);
                        int arrayCount = p17JsonArray.length();
                        for (int i = 0; i < arrayCount; i++) {
                            try {
                                JSONObject obj = (JSONObject) p17JsonArray.get(i);

                                String album = PPBWidget.GetStringByJsonObject(obj, Key.album);
//                                String template = PPBWidget.GetStringByJsonObject(obj, Key.template);
//                                String cooperation = PPBWidget.GetStringByJsonObject(obj, Key.cooperation);

                                JSONObject aj = new JSONObject(album);

                                String p17_json_album_id = PPBWidget.GetStringByJsonObject(aj, Key.album_id);

                                if (p17_json_album_id.equals(strCooperationAlbumId)) {

                                    strZipped = PPBWidget.GetStringByJsonObject(aj, Key.zipped);
//                                    strAlbumName = PPBWidget.GetStringByJsonObject(aj, Key.name);
//                                    strCover = PPBWidget.GetStringByJsonObject(aj, Key.cover);


//                                    JSONObject temj = new JSONObject(template);
//                                    strTemplate_id = PPBWidget.GetStringByJsonObject(temj, Key.template_id);

//                                    JSONObject cj = new JSONObject(cooperation);
//                                    strIdentity = PPBWidget.GetStringByJsonObject(cj, Key.identity);

                                    break;
                                }


                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }//for


                    } else if (p17Result.equals("0")) {
                        p17Message = jsonObject.getString(Key.message);
                    } else {
                        p17Result = "";
                    }
                } catch (Exception e) {
                    p17Result = "";
                    e.printStackTrace();
                }
            }
        }

    }


    private void toAlbumInfo(String album_id) {

        surfaceView.setVisibility(View.INVISIBLE);
        viewfinderView.setVisibility(View.INVISIBLE);

        Bundle bundle = new Bundle();
        bundle.putString("album_id", album_id);

        Intent intent = new Intent(getActivity(), AlbumInfo2Activity.class);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
        ActivityAnim.StartAnim(getActivity());

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
//        if (vibrate) {
//            Vibrator vibrator = (Vibrator) getActivity().getSystemService(
//                    Context.VIBRATOR_SERVICE);
//            vibrator.vibrate(VIBRATE_DURATION);
//        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void resturnScanResult(int resultCode, Intent data) {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void launchProductQuary(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(intent);
    }

    @Override
    public void onResume() {

        MyLog.Set("d", getClass(), "onResume");

        super.onResume();

        setSurfaceView();
        surfaceView.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onPause() {

        MyLog.Set("d", getClass(), "onPause");

        super.onPause();
        //2016.04.17添加
        surfaceView.setVisibility(View.INVISIBLE);
        viewfinderView.setVisibility(View.INVISIBLE);
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {

        if (retrievealbumpbypnTask != null && !retrievealbumpbypnTask.isCancelled()) {
            retrievealbumpbypnTask.cancel(true);
            retrievealbumpbypnTask = null;
        }

        if (joinCooperationTask != null && !joinCooperationTask.isCancelled()) {
            joinCooperationTask.cancel(true);
            joinCooperationTask = null;
        }

        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentScanSearch2 = this;

    }


}

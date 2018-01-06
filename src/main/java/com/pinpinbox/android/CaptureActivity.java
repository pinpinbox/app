package com.pinpinbox.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import com.pinpinbox.android.pinpinbox2_0_0.libs.scan.camera.CameraManager;
import com.pinpinbox.android.pinpinbox2_0_0.libs.scan.decoding.CaptureActivityHandler;
import com.pinpinbox.android.pinpinbox2_0_0.libs.scan.decoding.InactivityTimer;
import com.pinpinbox.android.pinpinbox2_0_0.libs.scan.view.ViewfinderView;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.StringClass.ProtocolsClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CaptureActivity extends Activity implements Callback {

    private static final String TAG = "CaptureActivity";
    private NoConnect noConnect;
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;

    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ImageView cancel;
    private String jsonResult, jsonData;

    private String reference_index;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去標題欄
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去信息欄
        setContentView(R.layout.test_activity_capture);
        CameraManager.init(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        cancel = (ImageView) findViewById(R.id.canceltext);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backLoadActivity = new Intent();

                startActivity(backLoadActivity);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            reference_index = bundle.getString("reference_index");
        }else {
            reference_index = "";
        }

    }


    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume~~~");
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);

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
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause~~~");
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }


    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
//        if (handler == null) {
//            handler = new CaptureActivityHandler(this, decodeFormats,
//                    characterSet);
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        if (!hasSurface) {
//            hasSurface = true;
//            initCamera(holder);
//        }

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

    public void handleDecode(final Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        viewfinderView.drawResultBitmap(barcode);
        playBeepSoundAndVibrate();

        String codeType = result.getBarcodeFormat().toString();
//                + ":"+result.getText();
        System.out.println(codeType);

        BarcodeFormat s = result.getBarcodeFormat();
        System.out.println(s);
        if(!reference_index.equals("")){

        } else {


            if (s.toString().equals("QR_CODE")) {
                System.out.println("掃描的是QR-CODE");

            } else {
                System.out.println("掃描的是ONE-CODE");
                final String productn = result.getText();
                System.out.println(productn);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferences getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
                            String id = getdata.getString("id", "");
                            String token = getdata.getString("token", "");
                            String productn = result.getText();
                            Map<String, String> data = new HashMap<String, String>();
                            data.put("id", id);
                            data.put("token", token);
                            data.put("productn", productn);
                            String sign = IndexSheet.encodePPB(data);
                            Map<String, String> sendData = new HashMap<String, String>();
                            sendData.put("id", id);
                            sendData.put("token", token);
                            sendData.put("productn", productn);
                            sendData.put("sign", sign);

                            String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P31_Retrievealbumpbypn, sendData, null);
                            System.out.println("---CaptureActivity---strJson => " + strJson);
                            JSONObject jsonObject = new JSONObject(strJson);
                            jsonResult = jsonObject.getString("result");
                            jsonData = jsonObject.getString("data");
                        } catch (Exception e) {

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (jsonResult.equals("1")) {

                                } else {
                                    Toast.makeText(CaptureActivity.this, "掃描失敗", Toast.LENGTH_SHORT).show();
                                    recreate();
                                }
                            }
                        });

                    }
                }).start();


            }
        }


    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.in_from_left_30,R.anim.out_to_right);

    }



        @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart~~~");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {

            if (noConnect == null) {
                noConnect = new NoConnect(this);
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop~~~");
    }

}
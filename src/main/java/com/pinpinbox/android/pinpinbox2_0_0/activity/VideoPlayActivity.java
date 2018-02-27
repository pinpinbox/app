package com.pinpinbox.android.pinpinbox2_0_0.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.dailymotion.android.player.sdk.PlayerWebView;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.StringUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VideoPlayActivity extends DraggerActivity {

    private Activity mActivity;
    private LoadingAnimation loading;
    private CallbackManager callbackManager;

    private EMVideoView videoView;
    private WebView webView;

    private String copyPath = Environment.getExternalStorageDirectory() + "/" + "pinpinbox_Instant_Play.mp4";

    private String videoPath;
    private String facebook_video_id;

    private int Mode;
    private final static int FileMode = 0;
    private final static int UrlMode = 1;
    private final static int VimeoMode = 2;

    AccessTokenTracker tokenTracker;
    AccessToken accessToken;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_2_0_0_video_play);
        SystemUtility.SysApplication.getInstance().addActivity(this);

        FacebookSdk.sdkInitialize(getApplicationContext());

        getBundleVideoPath();

        init();


        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };


    }

    private void getBundleVideoPath() {

        Bundle bundle = getIntent().getExtras();
        videoPath = bundle.getString("videopath", "");


    }


    private void init() {

        mActivity = this;

        webView = (WebView) findViewById(R.id.webView);
        videoView = (EMVideoView) findViewById(R.id.videoview);
        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        dmWebView = (PlayerWebView) findViewById(R.id.dmWebView);

        String str = "";
        try {
            str = videoPath.substring(0, 4);
        } catch (Exception e) {

            DialogV2Custom.BuildUnKnow(mActivity, mActivity.getClass().getSimpleName());

            e.printStackTrace();
            return;
        }


        if (str.equals("http")) {
            Mode = UrlMode;

            if (StringUtil.containsString(videoPath, "facebook")) {

                callbackManager = CallbackManager.Factory.create();

                setFacebookContents();

                facebook_video_id = getFacebookVideoId(videoPath);

                getVideoSourceToSetVideoView();


            } else if (StringUtil.containsString(videoPath, "dailymotion")) {

                setDailymotionView(videoPath);

            } else {

//                setVideoView(copyPath);

                setVideoView(videoPath);

            }


        } else {

            Mode = FileMode;

            File file = new File(copyPath);

            if (file.exists()) {
                file.delete();
            }

            try {
                FileUtility.createSDFile(copyPath);
                FileUtility.copyfile(new File(videoPath), new File(copyPath), true);

            } catch (Exception e) {
                e.printStackTrace();
            }

            setVideoView(copyPath);

        }


    }

    private PlayerWebView dmWebView;
    private FrameLayout frameLayout = null;
    private WebChromeClient chromeClient = null;
    private View myView = null;
    private WebChromeClient.CustomViewCallback myCallBack = null;


    private void facebookCompleted(GraphResponse response) {

        MyLog.Set("d", VideoPlayActivity.class, "response.toString() => " + response.toString());
        MyLog.Set("d", VideoPlayActivity.class, "response.getRawResponse() => " + response.getRawResponse());

        MyLog.Set("e", VideoPlayActivity.class, "------------------2");

        if (response.getRawResponse() == null) {

            FacebookRequestError error = response.getError();
            int errorCode = error.getErrorCode();

            if (errorCode == 104) {
                MyLog.Set("d", VideoPlayActivity.class, "errorCode => " + errorCode);
                MyLog.Set("d", VideoPlayActivity.class, "重新獲取token");
                LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList(""));
            } else if (errorCode == 190) {

                //20171204
                setLiveVideo();

            } else {

                MyLog.Set("e", VideoPlayActivity.class, "------------------3");

                setWebView(videoPath);

            }


        } else {

            //responseCode: 200

            try {


                int responseCode = 0 ;
                responseCode = response.getConnection().getResponseCode();

                if(responseCode == 200){

                    MyLog.Set("e", VideoPlayActivity.class, "responseCode == 200");

                    setLiveVideo();

                    return;
                }


                MyLog.Set("e", VideoPlayActivity.class, "------------------4");

                JSONObject jsonObject = new JSONObject(response.getRawResponse());

                String url = JsonUtility.GetString(jsonObject, "source");

                if (url != null && !url.equals("")) {
                    setVideoView(videoPath);
                } else {
                    setWebView(videoPath);
                }


            } catch (Exception e) {

                e.printStackTrace();
            }

        }


    }

    private void setFacebookContents() {

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        MyLog.Set("d", mActivity.getClass(), "fblogin => onSuccess");
                        MyLog.Set("d", mActivity.getClass(), "loginResult.getAccessToken().toString() => " + loginResult.getAccessToken().toString());

                        AccessToken.setCurrentAccessToken(loginResult.getAccessToken());

                        Bundle bundle = new Bundle();
                        bundle.putString("fields", "id,source");

                        new GraphRequest(
                                AccessToken.getCurrentAccessToken(),
                                facebook_video_id,
                                bundle,
                                HttpMethod.GET,
                                new GraphRequest.Callback() {
                                    public void onCompleted(GraphResponse response) {
                                         /* handle the result */
//                                        MyLog.Set("d", VideoPlayActivity.class, "response.toString() => " + response.toString());
//                                        MyLog.Set("d", VideoPlayActivity.class, "response.getRawResponse() => " + response.getRawResponse());
//
//                                        MyLog.Set("e", VideoPlayActivity.class, "------------------1");
//
//
//                                        try {
//                                            JSONObject jsonObject = new JSONObject(response.getRawResponse());//直播異常 要使用 String.format("/%d/live_videos", facebook_video_id),
//
//                                            String url = JsonUtility.GetString(jsonObject, "source");
//
//                                            if(url!=null && !url.equals("")){
//                                                setVideoView(videoPath);
//                                            }else {
//                                                setWebView(videoPath);
//                                            }
//
//
//                                        } catch (Exception e) {
//
//                                            e.printStackTrace();
//                                        }


                                        facebookCompleted(response);

                                    }
                                }
                        ).executeAsync();


                    }

                    @Override
                    public void onCancel() {
                        MyLog.Set("d", mActivity.getClass(), "fblogin => onCancel");
                        setWebView(videoPath);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        MyLog.Set("e", mActivity.getClass(), "fblogin => onError");
                        exception.printStackTrace();
                        setWebView(videoPath);
                    }

                });

            }

    private String getFacebookVideoId(String path) {

        String fvid = "";

        Uri uri = Uri.parse(path);
        List<String> pathList = uri.getPathSegments();

        for (int i = 0; i < pathList.size(); i++) {
            if (isNumeric(pathList.get(i))) {
                fvid = pathList.get(i);
                MyLog.Set("e", getClass(), "pathList.get(i) => " + pathList.get(i));
                break;
            }
        }

        return fvid;
    }


    @SuppressLint("DefaultLocale")
    private void setLiveVideo() {

        MyLog.Set("e", VideoPlayActivity.class, "setLiveVideo");

        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,source");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                facebook_video_id,
                bundle,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                                         /* handle the result */
                        MyLog.Set("d", VideoPlayActivity.class, "response.toString() => " + response.toString());
                        MyLog.Set("d", VideoPlayActivity.class, "response.getRawResponse() => " + response.getRawResponse());

                        MyLog.Set("e", VideoPlayActivity.class, "------------------5");

                        if (response.getRawResponse() == null) {

                            FacebookRequestError error = response.getError();
                            int errorCode = error.getErrorCode();

                            if (errorCode == 104) {
                                MyLog.Set("d", VideoPlayActivity.class, "errorCode => " + errorCode);
                                MyLog.Set("d", VideoPlayActivity.class, "重新獲取token");
                                LoginManager.getInstance().logInWithReadPermissions(mActivity, Arrays.asList(""));
                            } else {

                                MyLog.Set("e", VideoPlayActivity.class, "------------------6");

                                setWebView(videoPath);
                            }

                        } else {

                            try {
                                MyLog.Set("e", VideoPlayActivity.class, "------------------7");

                                JSONObject jsonObject = new JSONObject(response.getRawResponse());

                                String url = JsonUtility.GetString(jsonObject, "source");

                                if (url != null && !url.equals("")) {
                                    setVideoView(videoPath);
                                } else {
                                    setWebView(videoPath);
                                }


                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                        }


                    }
                }
        ).executeAsync();


    }


    private void getVideoSourceToSetVideoView() {


        Bundle bundle = new Bundle();
        bundle.putString("fields", "id,source");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                facebook_video_id,
                bundle,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                                         /* handle the result */

                        facebookCompleted(response);


                    }
                }
        ).executeAsync();


    }


    private void setVideoView(String path) {

        MyLog.Set("e", getClass(), "setVideoView");

        Uri uri = null;

        switch (Mode) {

            case FileMode:
                uri = Uri.parse(path);
                break;

            case UrlMode:
                uri = Uri.parse(path);
                break;
        }


        frameLayout.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);


        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared() {
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {

                mActivity.finish();

            }
        });


                        /* 设置模式-播放进度条 */
//        videoView.setMediaController(new MediaController(VideoPlayActivity.this));
//        videoView.setVideoURI(uri);
//
////        videoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);
//
//        videoView.requestFocus();
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                videoView.start();
//            }
//        });
//
//
//        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//
//                MyLog.Set("d", getClass(), "video error");
//
//                finish();
//                ActivityAnim.FinishAnim(mActivity);
//
//                return false;
//            }
//        });

    }

    private void setWebView(String url) {

        MyLog.Set("e", getClass(), "setWebView");

        frameLayout.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        webView.setWebViewClient(new MyWebviewCient());

        chromeClient = new MyChromeClient();

        webView.setWebChromeClient(chromeClient);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        final String USER_AGENT_STRING = webView.getSettings().getUserAgentString() + " Rong/2.0";
        webView.getSettings().setUserAgentString(USER_AGENT_STRING);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.loadUrl(url);

    }

    private void setDailymotionView(String url) {


        videoView.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
        dmWebView.setVisibility(View.VISIBLE);

//        Pattern pattern = Pattern.compile("http://www.dailymotion.com/video/([A-Za-z0-9])");


        String dailyMotionVideo_id = url.substring(url.lastIndexOf("/") + 1);

        MyLog.Set("e", getClass(), "dailyMotionVideo_id => " + dailyMotionVideo_id);


        dmWebView.load(dailyMotionVideo_id);

        final LoadingAnimation loading = new LoadingAnimation(mActivity);
        loading.show();

        dmWebView.setEventListener(new PlayerWebView.EventListener() {
            @Override
            public void onEvent(String event, HashMap<String, String> map) {

                MyLog.Set("e", getClass(), "event => " + event);

                if (event.equals(PlayerWebView.EVENT_VIDEO_START)) {
                    loading.dismiss();
                }


            }
        });


//http://www.dailymotion.com/video/x624cwi


//https://api.dailymotion.com/video/x624cwi


    }


    /*判斷是否為整數 (正數)  如果要判断全部数字，正则表达式需要修改为 -?[0-9]+.?[0-9]+*/
    public boolean isNumeric(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");

        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public class MyWebviewCient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          String url) {
            WebResourceResponse response = null;
            response = super.shouldInterceptRequest(view, url);
            return response;
        }
    }


    public class MyChromeClient extends WebChromeClient {

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (myView != null) {
                callback.onCustomViewHidden();
                return;
            }
            frameLayout.removeView(webView);
            frameLayout.addView(view);
            myView = view;
            myCallBack = callback;
        }

        @Override
        public void onHideCustomView() {
            if (myView == null) {
                return;
            }
            frameLayout.removeView(myView);
            myView = null;
            frameLayout.addView(webView);
            myCallBack.onCustomViewHidden();
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            // TODO Auto-generated method stub
            Log.d("ZR", consoleMessage.message() + " at " + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber());
            return super.onConsoleMessage(consoleMessage);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        if (myView == null) {
            finish();
            ActivityAnim.FinishAnim(this);
        } else {
            chromeClient.onHideCustomView();
        }
    }

    @Override
    public void onResume() {

        if (!HttpUtility.isConnect(this)) {
            DialogV2Custom.BuildNoConnect(mActivity);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (dmWebView.getVideoId() != null && !dmWebView.getVideoId().equals("null"))
                dmWebView.onResume();
        }


        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        videoView.pause();

        if (webView != null) {
            webView.onPause();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (dmWebView.getVideoId() != null && !dmWebView.getVideoId().equals("null"))
                dmWebView.onPause();
        }

    }

    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        tokenTracker.stopTracking();

        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        videoView = null;

        if (webView != null) {
            webView.reload();
        }
        webView = null;

        if (dmWebView != null) {
            dmWebView.reload();
            dmWebView.release();
        }
        dmWebView = null;

        File file = new File(copyPath);

        if (file.exists()) {
            file.delete();
        }

        super.onDestroy();
    }


}

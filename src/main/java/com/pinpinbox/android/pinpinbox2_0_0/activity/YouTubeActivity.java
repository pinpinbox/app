package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.Toast;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogSet;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vmage on 2015/12/28.
 */
public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    static private final String DEVELOPER_KEY = "AIzaSyATCeohA43aiTn-DkMI0ATpLJMiMWMDhdU";

    private YouTubePlayer mYouTubePlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_2_0_0_youtube);
        SystemUtility.SysApplication.getInstance().addActivity(this);
        YouTubePlayerView youTubeView = (YouTubePlayerView)
                findViewById(R.id.youtube_view);
        youTubeView.initialize(DEVELOPER_KEY, this);


    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mYouTubePlayer = youTubePlayer;

        mYouTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        mYouTubePlayer.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!b) {
            Bundle bundle = getIntent().getExtras();
            String path = bundle.getString("path");

            MyLog.Set("d", getClass(), path);

            String idPath = getVideoId(path);
//            String idPath = path.substring(path.lastIndexOf("/")+1);

            mYouTubePlayer.loadVideo(idPath);

//            youTubePlayer.cueVideo(VIDEO);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Oh no! ", Toast.LENGTH_LONG).show();
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
            MyLog.Set("d", getClass(), "onAdStarted");
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
            MyLog.Set("d", getClass(), "onError");
        }

        @Override
        public void onLoaded(String arg0) {
            MyLog.Set("d", getClass(), "onLoaded");
        }

        @Override
        public void onLoading() {
            MyLog.Set("d", getClass(), "onLoading");
        }

        @Override
        public void onVideoEnded() {
            MyLog.Set("d", getClass(), "onVideoEnded");
        }

        @Override
        public void onVideoStarted() {
            MyLog.Set("d", getClass(), "onVideoStarted");
        }
    };

    final static String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";

    public static String getVideoId(@NonNull String videoUrl) {
        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);

        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    public static String getVideoUrl(@NonNull String videoId) {
        return "http://youtu.be/" + videoId;
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onResume() {

        if (!HttpUtility.isConnect(this)) {
            DialogSet d = new DialogSet(this);
            d.setNoConnect();
        }

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
            mYouTubePlayer = null;
        }
        super.onDestroy();
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Feature2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ApiKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;

import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT;


/**
 * Created by vmage on 2018/4/18.
 */

public class FragmentCGAbannerVideo extends YouTubePlayerSupportFragment implements YouTubePlayer.OnInitializedListener {


    private YouTubePlayer mYouTubePlayer;

    private String youtubeVideoId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {

            youtubeVideoId = bundle.getString(Key.youbuteVideoId, "");

            MyLog.Set("e", getClass(), "youtubeVideoId => " + youtubeVideoId);

        }


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize(ApiKey.YOUTUBE, this);


    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        mYouTubePlayer = youTubePlayer;

        mYouTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        mYouTubePlayer.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!b) {

            if (!youtubeVideoId.equals("") && !youtubeVideoId.equals("null")) {
                mYouTubePlayer.loadVideo(youtubeVideoId);
            } else {
                PinPinToast.showErrorToast(getActivity(), "影片來源異常");
            }


        }


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        PinPinToast.showErrorToast(getActivity(), "影片來源異常");
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

            MyLog.Set("e", FragmentCGAbannerVideo.class, "onPlaying");

            ((Feature2Activity) getActivity()).hideLargeUserList();

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


    @Override
    public void onDestroy() {
        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
            mYouTubePlayer = null;
        }
        super.onDestroy();
    }


}

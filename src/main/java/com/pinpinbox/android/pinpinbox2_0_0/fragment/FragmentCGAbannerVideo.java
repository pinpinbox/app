package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Feature2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.KeysForSKD;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.SnackManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;


/**
 * Created by vmage on 2018/4/18.
 */

public class FragmentCGAbannerVideo extends YouTubePlayerSupportFragment{


    private YouTubePlayer mYouTubePlayer;

    private String youtubeVideoId;

    private boolean isVisibleToUser = false;

    public static FragmentCGAbannerVideo newInstance(String youtubeVideoId) {

        FragmentCGAbannerVideo fragmentCGAbannerVideo = new FragmentCGAbannerVideo();

        Bundle bundle = new Bundle();
        bundle.putString(Key.youtubeVideoId, youtubeVideoId);

        fragmentCGAbannerVideo.setArguments(bundle);

        return fragmentCGAbannerVideo;
    }


    /*onCreate前執行*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        this.isVisibleToUser = isVisibleToUser;

        MyLog.Set("e", getClass(), "userVisible => " + isVisibleToUser);

        if(isVisibleToUser){

            if(mYouTubePlayer==null){
                setYouTube();
            }

        }else {
            if(mYouTubePlayer!=null) {
                mYouTubePlayer.release();
                mYouTubePlayer = null;
            }
        }


    }

    private void setYouTube(){

        initialize(KeysForSKD.YouTube, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                MyLog.Set("e", getClass(), "onInitializationSuccess");

                mYouTubePlayer = youTubePlayer;

                mYouTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
                mYouTubePlayer.setPlaybackEventListener(playbackEventListener);

                /** Start buffering **/
                if (!b) {

                    if (!youtubeVideoId.equals("") && !youtubeVideoId.equals("null")) {
                        mYouTubePlayer.loadVideo(youtubeVideoId);
                    } else {
                        PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_video_error);
                    }


                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                MyLog.Set("e", getClass(), "onInitializationFailure");

                PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_video_error);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {

            youtubeVideoId = bundle.getString(Key.youtubeVideoId, "");

            MyLog.Set("e", getClass(), "youtubeVideoId => " + youtubeVideoId);

        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (isVisibleToUser) {
            setYouTube();
        }

    }


//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//
//        MyLog.Set("e", getClass(), "onInitializationSuccess");
//
//
//        mYouTubePlayer = youTubePlayer;
//
//        mYouTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
//        mYouTubePlayer.setPlaybackEventListener(playbackEventListener);
//
//
//        /** Start buffering **/
//        if (!b) {
//
//            if (!youtubeVideoId.equals("") && !youtubeVideoId.equals("null")) {
//                mYouTubePlayer.loadVideo(youtubeVideoId);
//            } else {
//                PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_video_error);
//            }
//
//
//        }
//
//    }

//    @Override
//    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//
//        MyLog.Set("e", getClass(), "onInitializationFailure");
//
//
//        PinPinToast.showErrorToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_video_error);
//    }


    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {


        private boolean isFirstPlay = true;


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

            if (!PPBApplication.getInstance().getData().getBoolean(TaskKeyClass.first_to_set_video_autoplay, false)) {

                SnackManager.showSettingsSnack(getActivity(), false);
                PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.first_to_set_video_autoplay, true).commit();

            }


            if (isFirstPlay) {

                isFirstPlay = false;

                if (!PPBApplication.getInstance().getData().getBoolean(Key.videoAutoplayEnable, true)) {
                    mYouTubePlayer.pause();
                    MyLog.Set("e", FragmentCGAbannerVideo.class, "不自動播放");
                }

            }


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

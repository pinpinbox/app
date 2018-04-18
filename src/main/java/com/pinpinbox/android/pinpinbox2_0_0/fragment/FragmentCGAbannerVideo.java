package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ApiKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

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

        if(bundle!=null){

            youtubeVideoId = bundle.getString(Key.youbuteVideoId, "");

            MyLog.Set("e", getClass(), "youtubeVideoId => " + youtubeVideoId);

        }


    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        View v = inflater.inflate(R.layout.fragment_2_0_0_cga_banner_video, container, false);
//
//
//
//         videoView = (YouTubePlayerFragment) getActivity().getFragmentManager().findFragmentById(R.id.youtubeplayerfragment);
//
//
//        return v;
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initialize(ApiKey.YOUTUBE, this);


    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        mYouTubePlayer = youTubePlayer;

        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        /** Start buffering **/
        if (!b) {

            mYouTubePlayer.loadVideo(youtubeVideoId);

        }


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

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



    @Override
    public void onDestroy() {
        if (mYouTubePlayer != null) {
            mYouTubePlayer.release();
            mYouTubePlayer = null;
        }
        super.onDestroy();
    }




}

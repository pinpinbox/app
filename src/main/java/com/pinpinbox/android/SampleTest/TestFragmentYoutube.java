package com.pinpinbox.android.SampleTest;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Created by kevin9594 on 2018/4/18.
 */

public class TestFragmentYoutube extends YouTubePlayerSupportFragment {

    public static final String DEVELOPER_KEY = "AIzaSyATCeohA43aiTn-DkMI0ATpLJMiMWMDhdU";

    public TestFragmentYoutube() { }

    public static TestFragmentYoutube newInstance(String url) {

        TestFragmentYoutube f = new TestFragmentYoutube();

        Bundle b = new Bundle();
        b.putString("url", url);

        f.setArguments(b);
        f.init();

        return f;
    }

    private void init() {

        initialize(DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) { }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
//                    player.cueVideo(getArguments().getString("url"));
                    player.loadVideo(getArguments().getString("url"));
                }
            }
        });
    }
}



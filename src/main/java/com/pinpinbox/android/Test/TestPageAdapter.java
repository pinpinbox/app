package com.pinpinbox.android.Test;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pinpinbox.android.R;

/**
 * Created by vmage on 2018/4/17.
 */

public class TestPageAdapter extends PagerAdapter implements YouTubePlayer.OnInitializedListener{

    private Activity mActivity;

    static private final String DEVELOPER_KEY = "AIzaSyATCeohA43aiTn-DkMI0ATpLJMiMWMDhdU";


    public TestPageAdapter(Activity mActivity) {

        this.mActivity = mActivity;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View vPage = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.test_page_feture_banner, null);


        ImageView pageImg = (ImageView)vPage.findViewById(R.id.pageImg);


        YouTubePlayerView youTubeView = (YouTubePlayerView) mActivity.findViewById(R.id.youtube_view);
        youTubeView.initialize(DEVELOPER_KEY, this);



        return vPage;

    }


    @Override
    public int getCount() {
        return 0;
    }

    //判斷是否由對象生成介面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;

    }





    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}

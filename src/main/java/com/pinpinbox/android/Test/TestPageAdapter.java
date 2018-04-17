package com.pinpinbox.android.Test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemCategoryBanner;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2018/4/17.
 */

public class TestPageAdapter extends PagerAdapter implements YouTubePlayer.OnInitializedListener{

    private Activity mActivity;

    static private final String DEVELOPER_KEY = "AIzaSyATCeohA43aiTn-DkMI0ATpLJMiMWMDhdU";

    private List<ItemCategoryBanner> itemCategoryBannerList;


    public TestPageAdapter(Activity mActivity, List<ItemCategoryBanner> itemCategoryBannerList) {

        this.mActivity = mActivity;
        this.itemCategoryBannerList = itemCategoryBannerList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View vPage = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.test_page_feture_banner, null);


        ImageView pageImg = (ImageView)vPage.findViewById(R.id.pageImg);


//        YouTubePlayerView youTubeView = (YouTubePlayerView) vPage.findViewById(R.id.youtube_view);
//        youTubeView.initialize(DEVELOPER_KEY, this);

        FrameLayout frameLayout = (FrameLayout)vPage.findViewById(R.id.frameYoutube);


        String type = itemCategoryBannerList.get(position).getBannerType();

        if(type.equals(ItemCategoryBanner.TYPE_IMAGE)){
//            youTubeView.setVisibility(View.INVISIBLE);
            frameLayout.setVisibility(View.GONE);
            pageImg.setVisibility(View.VISIBLE);
            Picasso.with(mActivity.getApplicationContext())
                    .load(itemCategoryBannerList.get(position).getImageUrl())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(pageImg);
        }

        if(type.equals(ItemCategoryBanner.TYPE_VIDEO)){
//            youTubeView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            pageImg.setVisibility(View.GONE);


            TestFragmentYoutube testFragmentYoutube =  TestFragmentYoutube.newInstance("KdSjiAjeC0U");
            ( (TestPageForYoutubeActivity)mActivity).getSupportFragmentManager().beginTransaction().replace(R.id.frameYoutube, testFragmentYoutube).commit();

        }




        container.addView(vPage, 0);

        return vPage;

    }


    @Override
    public int getCount() {
        return itemCategoryBannerList.size();
    }

    //判斷是否由對象生成介面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;

    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }





    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}

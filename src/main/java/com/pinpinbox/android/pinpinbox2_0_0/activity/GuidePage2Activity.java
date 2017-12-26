package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2017/4/26.
 */
public class GuidePage2Activity extends DraggerActivity {


    private Activity mActivity;

    private List<View> viewList;
    private List<ImageView> imageViewList;

    private ViewPager vpGuide;
    private LinearLayout linGuideBack;
    private CircleIndicator indicator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_guidepage);

        init();

        setPage();

    }

    private void init() {


        mActivity = this;

        viewList = new ArrayList<>();
        imageViewList = new ArrayList<>();


        vpGuide = (ViewPager) findViewById(R.id.vpGudie);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        linGuideBack = (LinearLayout) findViewById(R.id.linGuideBack);

        vpGuide.setOffscreenPageLimit(3);

        linGuideBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                back();

            }
        });

        TextView tvBack = (TextView)findViewById(R.id.tvBack);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){

            String className = bundle.getString(Key.className, "");

            if(className.equals(AppSettings2Activity.class.getSimpleName())){
                tvBack.setText(R.string.pinpinbox_2_0_0_other_text_back_to_settings);
            }
        }



    }

    private void setPage() {


        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.page_2_0_0_image, null);

            ImageView guideImg = (ImageView)view.findViewById(R.id.guideImg);

            TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);

            TextView tvDescription = (TextView)view.findViewById(R.id.tvDescription);

            TextUtility.setBold(tvTitle, true);


            switch (i){

                case 0:

                    guideImg.setImageResource(R.drawable.bg200_guide01);

//                    Picasso.with(getApplicationContext()).load(R.drawable.bg200_guide01)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                    .into(guideImg);


                    tvTitle.setText(R.string.pinpinbox_2_0_0_guide_page_0);
                    tvDescription.setText(R.string.pinpinbox_2_0_0_guide_page_1);

                    break;

                case 1:
//                    Picasso.with(getApplicationContext()).load(R.drawable.bg200_guide02)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                            .into(guideImg);

                    guideImg.setImageResource(R.drawable.bg200_guide02);
                    tvTitle.setText(R.string.pinpinbox_2_0_0_guide_page_2);
                    tvDescription.setText(R.string.pinpinbox_2_0_0_guide_page_3);
                    break;

                case 2:
//                    Picasso.with(getApplicationContext()).load(R.drawable.bg200_guide03)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
//                            .into(guideImg);

                    guideImg.setImageResource(R.drawable.bg200_guide03);
                    tvTitle.setText(R.string.pinpinbox_2_0_0_guide_page_4);
                    tvDescription.setText(R.string.pinpinbox_2_0_0_guide_page_5);
                    break;

            }


            imageViewList.add(guideImg);
            viewList.add(view);
        }


        GuidePageAdapter adapter = new GuidePageAdapter();
        vpGuide.setAdapter(adapter);
        indicator.setViewPager(vpGuide);


    }

    private void back(){

        finish();
        overridePendingTransition(R.anim.begin_alpha_enter, R.anim.begin_alpha_exit);

    }

    private class GuidePageAdapter extends PagerAdapter {


        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));

        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            container.addView(viewList.get(position));
            return viewList.get(position);

        }


        @Override
        public int getCount() {
            return viewList.size();
        }

        //判斷是否由對象生成介面
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;

        }


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onDestroy() {

        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setImageResource(0);
        }

        Recycle.IMG((ImageView) findViewById(R.id.backImg));

        System.gc();
        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }





}

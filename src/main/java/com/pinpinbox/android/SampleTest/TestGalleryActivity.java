package com.pinpinbox.android.SampleTest;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.libs.GalleryRecyclerView.CardScaleHelper;
import com.pinpinbox.android.pinpinbox2_0_0.libs.GalleryRecyclerView.SpeedRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2018/3/8.
 */

public class TestGalleryActivity extends DraggerActivity {

    private SpeedRecyclerView mRecyclerView;
    private CardScaleHelper mCardScaleHelper = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_gallery);


        init();


    }

    private void init(){

        List<Integer> imgList = new ArrayList<>();


        for (int i = 0; i < 3; i++) {

            imgList.add(R.drawable.test_album);
            imgList.add(R.drawable.test_banner);
            imgList.add(R.drawable.test_get_color);


        }

        SampleGalleryAdapter adapter = new SampleGalleryAdapter(imgList);

        mRecyclerView = (SpeedRecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);

        adapter.setOnRecyclerViewListener(new SampleGalleryAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

            }

        });


    }

}

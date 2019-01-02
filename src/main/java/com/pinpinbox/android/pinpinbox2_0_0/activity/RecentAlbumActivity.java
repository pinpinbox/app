package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerRecentAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2017/4/15.
 */
public class RecentAlbumActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

    private RecyclerRecentAdapter adapter;

    private List<ItemAlbum>itemAlbumList;

    private View vHeader;
    private RecyclerView rvRecent;
    private ImageView backImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_recent);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        init();

        setRecent();

        setRecycler();


    }


    private void init() {

        mActivity = this;

        itemAlbumList = new ArrayList<>();

        rvRecent = (RecyclerView)findViewById(R.id.rvRecent);
        backImg = (ImageView)findViewById(R.id.backImg);

        vHeader = LayoutInflater.from(getApplicationContext()).inflate(R.layout.header_2_0_0_title, null);

        TextView tvTitle = vHeader.findViewById(R.id.tvTitle);
        ViewControl.setMargins(tvTitle,
                DensityUtility.dip2px(getApplicationContext(), 12),
                DensityUtility.dip2px(getApplicationContext(), 92),
                0,
                DensityUtility.dip2px(getApplicationContext(), 32));

        tvTitle.setText(R.string.pinpinbox_2_0_0_title_recent);

        backImg.setOnClickListener(this);

    }

    public void setRecent(){

        itemAlbumList.clear();

        String recent = PPBApplication.getInstance().getData().getString(Key.recentAlbumdata,"[]");

        try {
            JSONArray jsonArrayRecent = new JSONArray(recent);

            for (int i = 0; i < jsonArrayRecent.length(); i++) {

                JSONObject jsonAlbum = (JSONObject)jsonArrayRecent.get(i);

                ItemAlbum itemAlbum = new ItemAlbum();

                itemAlbum.setCover(JsonUtility.GetString(jsonAlbum, Key.cover));
                itemAlbum.setName(JsonUtility.GetString(jsonAlbum, Key.name));
                itemAlbum.setUser_name(JsonUtility.GetString(jsonAlbum, Key.user));
                itemAlbum.setAlbum_id(JsonUtility.GetString(jsonAlbum, Key.album_id));

                itemAlbumList.add(itemAlbum);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        if(adapter!=null){

            adapter.notifyDataSetChanged();
        }

    }

    private void setRecycler() {

        adapter = new RecyclerRecentAdapter(mActivity, itemAlbumList);
        rvRecent.setAdapter(adapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvRecent.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvRecent.setLayoutManager(manager);


        RecyclerViewUtils.setHeaderView(rvRecent, vHeader);


        adapter.setOnRecyclerViewListener(new RecyclerRecentAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                Bundle bundle = new Bundle();
                bundle.putString(Key.album_id, itemAlbumList.get(position).getAlbum_id());
                startActivity(new Intent(mActivity, ReaderActivity.class).putExtras(bundle));
                ActivityAnim.StartAnim(mActivity);

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });

    }



    private void back() {

        finish();
        ActivityAnim.FinishAnim(mActivity);

    }

    private void cleanPicsso(){

        int count = itemAlbumList.size();

        for (int i = 0; i < count; i++) {

            String cover = itemAlbumList.get(i).getCover();

            Picasso.with(mActivity.getApplicationContext()).invalidate(cover);

        }


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.backImg:
                back();
                break;

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

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        cleanPicsso();

        Recycle.IMG(backImg);

        System.gc();
        super.onDestroy();
    }


}

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

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.pinpinbox.android.Views.recyclerview.RecyclerViewUtils;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerOffLineAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


/**
 * Created by kevin9594 on 2016/5/22.
 */
public class OffLineActivity extends DraggerActivity implements View.OnClickListener {

    private Activity mActivity;

//    private List<File> fileList;
//    private List<String> titleList;
//    private List<String> descriptionList;
    private RecyclerOffLineAdapter adapter;

    private List<ItemAlbum>itemAlbumList;


    private RecyclerView rvOffLine;
    private ImageView backImg;
    private TextView tvBack;
    private View vHeader;



    private String albumids[];

    private String myDir;
    private String id;

    private boolean exitAPP = false;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_offline);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
            exitAPP = bundle.getBoolean(Key.exitAPP, false);
        }


        init();

        setRecycler();



        try {

            if(id.equals("")){
                PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_dialog_message_please_login);
                return;
            }

            setItem();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void init() {

        mActivity = this;

        id = PPBApplication.getInstance().getId();
        myDir = PPBApplication.getInstance().getMyDir();

        itemAlbumList = new ArrayList<>();

        rvOffLine = (RecyclerView)findViewById(R.id.rvOffLine);
        backImg = (ImageView)findViewById(R.id.backImg);
        tvBack = (TextView)findViewById(R.id.tvBack);


        vHeader = LayoutInflater.from(getApplicationContext()).inflate(R.layout.header_2_0_0_title, null);

        TextView tvTitle = vHeader.findViewById(R.id.tvTitle);
        tvTitle.setText(R.string.pinpinbox_2_0_0_title_offline_review);

        if(SystemUtility.Above_Equal_V5()){
            tvTitle.setLetterSpacing(0.1f);
        }


        ViewControl.setMargins(tvTitle,
                DensityUtility.dip2px(getApplicationContext(), 12),
                DensityUtility.dip2px(getApplicationContext(), 92),
                0,
                0);


        TextView tvBelowTitleText = (TextView)vHeader.findViewById(R.id.tvBelowTitleText);
        tvBelowTitleText.setVisibility(View.VISIBLE);
        tvBelowTitleText.setText(R.string.pinpinbox_2_0_0_other_text_only_read_photo_description);

        backImg.setOnClickListener(this);
        tvBack.setOnClickListener(this);


        if(exitAPP){
            backImg.setVisibility(View.GONE);
            tvBack.setVisibility(View.VISIBLE);
        }else {
            backImg.setVisibility(View.VISIBLE);
            tvBack.setVisibility(View.GONE);
        }


    }

    private void setRecycler(){


        adapter = new RecyclerOffLineAdapter(mActivity, itemAlbumList);
        rvOffLine.setAdapter(adapter);

        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(adapter);
        rvOffLine.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rvOffLine.setLayoutManager(manager);


        RecyclerViewUtils.setHeaderView(rvOffLine, vHeader);


        adapter.setOnRecyclerViewListener(new RecyclerOffLineAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                Bundle bundle = new Bundle();
                bundle.putString("album_id",itemAlbumList.get(position).getAlbum_id());
                bundle.putString("album_title", itemAlbumList.get(position).getName());

                Intent intent = new Intent(mActivity, OfflineReadActivity.class);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
                ActivityAnim.StartAnim(mActivity);



            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });



    }



    private void setItem() {
        final File file = new File(DirClass.sdPath + myDir + DirClass.dirAlbumList);
        albumids = file.list();

        for (int i = 0; i < albumids.length; i++) {


            ItemAlbum itemAlbum = new ItemAlbum();


            String albumDir = albumids[i];//相本資料夾(album_id)

            itemAlbum.setAlbum_id(albumDir);

            File f = new File(DirClass.sdPath + myDir + DirClass.dirAlbumList + albumDir);

            String cover = "";

            int count = f.list().length;

            for (int k = 0; k < count; k++) {

                String strJPG = f.list()[k];

                if(strJPG.equals("0.jpg#asdf")){
                    cover = strJPG;
                    break;
                }

            }

            if(cover.equals("0.jpg#asdf")) {
                itemAlbum.setCover(DirClass.sdPath + myDir + DirClass.dirAlbumList + albumDir + "/" + cover);
            }

            /**********************************************************************************************/

            try {
                JSONObject jsonObject = new JSONObject(FileUtility.getTXTdata(DirClass.sdPath + myDir + DirClass.dirAlbumList + albumDir + "/" + "info.txt#asdf"));

                itemAlbum.setName(JsonUtility.GetString(jsonObject, Key.title));

                itemAlbum.setUser_name(JsonUtility.GetString(jsonObject, Key.author));

            } catch (Exception e) {
                e.printStackTrace();
            }

            itemAlbumList.add(itemAlbum);
        }


        adapter.notifyDataSetChanged();


    }

    private void back(){


        if(exitAPP){
            finish();
            SystemUtility.SysApplication.getInstance().exit();

        }else {
            finish();
            ActivityAnim.FinishAnim(mActivity);
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.backImg:

                back();

                break;

            case R.id.tvBack:

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
        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }

        back();

    }

    @Override
    public void onDestroy() {


        System.gc();

        super.onDestroy();
    }



}

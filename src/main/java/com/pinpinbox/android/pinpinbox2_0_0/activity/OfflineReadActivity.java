package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.SampleTest.ReadAlbum.PageAdapter;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Views.ControllableViewPager;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.MyGallery;
import com.pinpinbox.android.Views.PinchImageView;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.GalleryAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemPhoto;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2016/4/11.
 */
public class OfflineReadActivity extends DraggerActivity {

    private Activity mActivity;

    private SharedPreferences getdata;

    private List<String> picList;
    private List<File> fileList;
    private List<View> viewList;//獲取PinchImageViewm
    private List<ItemPhoto>itemPhotoList;

    private LinearLayout linDescription;
    private RelativeLayout rActionBar, rBottom;
    private TextView tvTitle, tvPageDescription;
    private ControllableViewPager viewPager;
    private ImageView backImg;
    private MyGallery gallery;

    private String sdPath = Environment.getExternalStorageDirectory() + "/";
    private String dirAlbumList = "albumlist/";
    private String myDir;
    private String id;

    private String album_id;

    private boolean isItemClick = false;

    private boolean isPhotoMode = false;

    private Handler mHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_offline_read);

        getAlbum_id();

        init();

        getText();

        setPicture();

        setGallery();

        setPageListener();

        back();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setMode(0);
                setPageDescription(0);
            }
        },400);

    }

    private void getAlbum_id() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            album_id = bundle.getString("album_id");
        }
    }

    private void init() {

        mActivity = this;

        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);

        id = getdata.getString("id", "");

        myDir = "PinPinBox" + id + "/";

        picList = new ArrayList<>();
        fileList = new ArrayList<>();
        viewList = new ArrayList<>();
        itemPhotoList = new ArrayList<>();

        linDescription = (LinearLayout) findViewById(R.id.linDescription);
        rActionBar = (RelativeLayout)findViewById(R.id.rActionBar);
        rBottom = (RelativeLayout)findViewById(R.id.rBottom);
        gallery = (MyGallery) findViewById(R.id.gallery);
        tvTitle = (TextView) findViewById(R.id.readTitle);
        tvPageDescription = (TextView) findViewById(R.id.tvPageDescription);
        viewPager = (ControllableViewPager) findViewById(R.id.readViewPager);
        backImg = (ImageView) findViewById(R.id.backImg);

    }

    private void getText(){

        ItemAlbum itemAlbum = new ItemAlbum();

        try {
            JSONObject jsonObject = new JSONObject(FileUtility.getTXTdata(DirClass.sdPath + myDir + DirClass.dirAlbumList + album_id + "/" + "info.txt#asdf"));

            itemAlbum.setName(JsonUtility.GetString(jsonObject, Key.title));

            itemAlbum.setUser_name(JsonUtility.GetString(jsonObject, Key.author));

            itemAlbum.setDescription(JsonUtility.GetString(jsonObject, Key.description));

            String photo = JsonUtility.GetString(jsonObject, Key.photo);

            JSONArray jsonArray = new JSONArray(photo);

            for (int i = 0; i < jsonArray.length(); i++) {

                ItemPhoto itemPhoto = new ItemPhoto();

                JSONObject object = (JSONObject)jsonArray.get(i);

                itemPhoto.setDescription(JsonUtility.GetString(object, Key.description));
                itemPhoto.setName(JsonUtility.GetString(object, Key.name));
                itemPhoto.setLocation(JsonUtility.GetString(object, Key.location));

                itemPhotoList.add(itemPhoto);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        tvTitle.setText(itemAlbum.getName());

    }


    private void setPicture() {

        picList = FileUtility.getPictureListByAlbumSuffix_asdf(sdPath + myDir + dirAlbumList + "/" + album_id + "/");

        for (int i = 0; i < picList.size(); i++) {

            File file = new File(picList.get(i));
            fileList.add(file);

        }

        PageAdapter pageAdapter = new PageAdapter(mActivity, fileList);
        viewPager.setAdapter(pageAdapter);
        viewPager.setOffscreenPageLimit(5);


    }

    private void setGallery() {

        GalleryAdapter adapter = new GalleryAdapter(mActivity, fileList, null, null);
        gallery.setAdapter(adapter);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(position, true);
                        isItemClick = false;
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                isItemClick = true;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(position, true);
                    }
                });


            }
        });

    }

    private void setPageListener() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {

                setMode(position);

                //不是經由點擊觸發
                //滑動觸發
                if(!isItemClick){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            gallery.setSelection(position, true);
                        }
                    });
                }

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if ((position + 1) < fileList.size()) {

                                if (viewPager.findViewById(position + 1) != null) {

                                    PinchImageView lastPicImg = (PinchImageView) (viewPager.findViewById(position + 1)).findViewById(R.id.pic);
                                    lastPicImg.reset();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {

                            if (viewPager.findViewById(position - 1) != null) {
                                if (position - 1 >= 0) {

                                    PinchImageView nextPicImg = (PinchImageView) viewPager.findViewById(position - 1).findViewById(R.id.pic);
                                    nextPicImg.reset();

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, 400);


                setPageDescription(position);

//                //2016.06.10
//                //1 , 2 => 2,3
//                if (position > 2 && position < fileList.size() - 3) {
//                    Picasso.with(mActivity).invalidate(fileList.get(position - 3));
//                    Picasso.with(mActivity).invalidate(fileList.get(position + 3));
//                    System.gc();
//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0://沒動作

                        break;


                    case 1://正在滑動


                        break;


                    case 2://滑動完畢

                        break;
                }

            }
        });

    }

    private void setPageDescription(int position) {

        String strDescription = itemPhotoList.get(position).getDescription();
        tvPageDescription.setText(strDescription);
        if (strDescription.equals("")) {
            linDescription.setVisibility(View.GONE);
        } else {
            linDescription.setVisibility(View.VISIBLE);
        }

    }

    private void setMode(int position){

        if(viewPager!=null) {

            if(viewPager.findViewById(position)!=null) {

                View v = viewPager.findViewById(position);

                PinchImageView photoImg = (PinchImageView) v.findViewById(R.id.pic);

                photoImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!isPhotoMode) {
                            //一般模式 => 相片模式

                            rActionBar.setVisibility(View.GONE);
                            rBottom.setVisibility(View.GONE);
                            isPhotoMode = true;

                        } else {
                            //相片模式 => 一般模式
                            rActionBar.setVisibility(View.VISIBLE);
                            rBottom.setVisibility(View.VISIBLE);
                            isPhotoMode = false;


                        }


                    }
                });
            }

        }

    }

    private void back() {
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseReadTask closeReadTask = new CloseReadTask();
                closeReadTask.execute();
            }
        });
    }

    private class CloseReadTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            for (int i = 0; i < picList.size(); i++) {
                viewList.clear();
                Picasso.get().invalidate(fileList.get(i));
            }
            System.gc();


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            finish();
            ActivityAnim.FinishAnim(mActivity);

        }
    }

    @Override
    public void onBackPressed() {
        CloseReadTask closeReadTask = new CloseReadTask();
        closeReadTask.execute();

    }

}

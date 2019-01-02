package com.pinpinbox.android.pinpinbox2_0_0.activity;

/**
 * Created by vmage on 2016/5/12.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.BitmapUtility;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.NoDraggerActivity;
import com.pinpinbox.android.Views.collageviews.MultiTouchListener;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerCreationTemplateAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentSelectPhoto;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.util.LoadType;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2015/11/4.
 */
public class CreationTemplate2Activity extends NoDraggerActivity {
    private static String TAG = CreationTemplate2Activity.class.getSimpleName();
    private Activity mActivity;
    private SharedPreferences getdata;
    private LoadingAnimation loading;
    private MultiTouchListener multiTouchListener;
    private UpLoadTask upLoadTask;

    private FragmentSelectPhoto fragmentSelectPhoto;


    private Bitmap upBmp;
    private Bitmap bigTemplateBmp;
    private File uploadFile;

    private String id, token;
    private String identity;
    private String album_id;
    private String p58Result, p58Message;
    private String myDir;
    private String sdpath = Environment.getExternalStorageDirectory() + "/";

    private String picUrl;//大張template的url
    private String frame;

    private static final int UpLoad_OK = 111;
    private int intControlAreaWidth, intControlAreaHeight;//操作範圍寬高
    private int lastPosition = 0;
    private int thisPosition = 0;
    private int areaCount = 0;

    private int doingType;
    private static final int DoUpLoad = 0;

    private ArrayList<RelativeLayout> rChangeList;
    private ArrayList<HashMap<String, Object>> photoImgList;
    private ArrayList<HashMap<String, Object>> bottomList;
    private List<ImageView> list_deleteImg;//確認前要先將刪除按鈕移除
    private List<ImageView> list_addImg;
    private List<Bitmap> pictureBmps;

    private TextView tvDone;
    private ImageView back;
    private RelativeLayout rControl_area;
    private RecyclerView rvTemplate;

//    private HorizontalListView horizontalListView;
    private RecyclerCreationTemplateAdapter adapter;
    private View rControl_view;
    private ImageView temImg, changeImg;


    private ImageView firstImg;
    private ImageView readyChangeImg;


    //給ActivityResult使用
    private ImageView touchImageView;
    private RelativeLayout rrrr;

    private int wArea;
    private int changeCount = 0;
    private int intoCount = 0;

    private boolean isEdit;
    private boolean changeMode = false;


    private Bitmap firstBmp, readyChageBmp;

//    public LruCache<String, Bitmap> mMemoryCache;

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_creation_template);

        SystemUtility.SysApplication.getInstance().addActivity(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        getFrame();
        init();

        setRecycler();



        rSecondBar = findViewById(R.id.rSecondBar);
        vto = rSecondBar.getViewTreeObserver();

        myViewTreeObserver = new MyViewTreeObserver();
        vto.addOnGlobalLayoutListener(myViewTreeObserver);


//        selectTemplate();
        confirm();
        back();
    }

    private RelativeLayout rSecondBar;
    private ViewTreeObserver vto;
    private MyViewTreeObserver myViewTreeObserver;
    private class MyViewTreeObserver implements ViewTreeObserver.OnGlobalLayoutListener{
        @Override
        public void onGlobalLayout() {
            if(intoCount == 0) {
                MyLog.Set("d", mActivity.getClass(), "rSecondBar.getWidth()" + rSecondBar.getWidth());
                MyLog.Set("d", mActivity.getClass(), "rSecondBar.getHeight()" + rSecondBar.getHeight());
                intControlAreaHeight = rSecondBar.getHeight() - 16;
                intControlAreaWidth = (intControlAreaHeight / 3) * 2;

                rControl_area.setLayoutParams(new RelativeLayout.LayoutParams(intControlAreaWidth, intControlAreaHeight));
                setFrame();
            }
            intoCount++;
        }
    }


    private void getFrame() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            identity = bundle.getString(Key.identity);
            album_id = bundle.getString(Key.album_id);
            frame = bundle.getString(Key.frame);

            MyLog.Set("d", getClass(), "bundle => album_id => " + album_id);
            MyLog.Set("d", getClass(), "bundle => identity => " + identity);
            MyLog.Set("d", getClass(), "bundle => frame => " + frame);
        }

    }

    private void init() {
        mActivity = this;
        getdata = PPBApplication.getInstance().getData();
        id = getdata.getString(Key.id, "");
        token = getdata.getString(Key.token, "");

        /***********************************************************************************************************/

        myDir = "PinPinBox" + id + "/";

        /***********************************************************************************************************/

        loading = new LoadingAnimation(mActivity);

        multiTouchListener = new MultiTouchListener();


        /***********************************************************************************************************/

        isEdit = false;
        LoadType.TemplateAddPicture_canSelect = true;

        /***********************************************************************************************************/

        bottomList = new ArrayList<>();
        list_deleteImg = new ArrayList<>();
        list_addImg = new ArrayList<>();
        pictureBmps = new ArrayList<>();

        rChangeList = new ArrayList<>();
        photoImgList = new ArrayList<>();

        /***********************************************************************************************************/

        /**2016.08.27 內存溢出 暫時取消*/
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
//        final int cacheSize = maxMemory / 4;
//        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap bitmap) {
//                return bitmap.getRowBytes() * bitmap.getHeight();
//            }
//        };

        /***********************************************************************************************************/

        changeImg = findViewById(R.id.changeImg);
        tvDone = findViewById(R.id.tvDone);
        rvTemplate = findViewById(R.id.rvTemplate);
        rControl_area = findViewById(R.id.rControl_area);



        /**2016.11.23 new adde*/
        final TextView tvGuide = findViewById(R.id.tvGuide);

        changeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (list_deleteImg == null || list_deleteImg.size() < 2) {

                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_add_at_least_two_photos);

                    return;
                }


                if (changeMode) {
                    changeMode = false;
                    changeImg.setImageResource(R.drawable.ic200_template_photo_change_light);
                    rvTemplate.setVisibility(View.VISIBLE);
                    tvDone.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);

                    tvGuide.setVisibility(View.INVISIBLE);

                } else {
                    changeMode = true;
                    changeImg.setImageResource(R.drawable.ic200_template_photo_change_dark);
                    rvTemplate.setVisibility(View.INVISIBLE);
                    tvDone.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.INVISIBLE);


                    tvGuide.setVisibility(View.VISIBLE);

                }
                for (int i = 0; i < photoImgList.size(); i++) {

                    final int position = i;

                    final ImageView img = (ImageView) photoImgList.get(position).get("photoImg");

                    if (changeMode) {

                        img.setOnTouchListener(null);
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyLog.Set("d", mActivity.getClass(), "click photo");

                                changeCount++;

                                if (changeCount == 1) {
                                    firstImg = img;
                                } else if (changeCount == 2) {
                                    readyChangeImg = img;
                                }

                                /*設定第一個點擊的item*/
                                RelativeLayout rControl_view = (RelativeLayout) photoImgList.get(position).get("rControl_view");
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(rControl_view.getWidth(), rControl_view.getHeight());
                                params.setMargins((int) rControl_view.getX(), (int) rControl_view.getY(), 0, 0);
                                RelativeLayout rChange = new RelativeLayout(mActivity);
                                rChange.setLayoutParams(params);
                                rChange.setBackgroundResource(R.drawable.border_select_image_to_change);
                                rControl_area.addView(rChange);

                                /*加入交換位置的list*/
                                rChangeList.add(rChange);

                                /*如list數量為兩張 執行兩張交換*/
                                if (rChangeList.size() == 2) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            /*獲取第二張點擊的bitmap*/

                                            readyChangeImg.setDrawingCacheEnabled(true); //必須設置
                                            readyChageBmp = Bitmap.createBitmap(readyChangeImg.getDrawingCache());
                                            readyChangeImg.setDrawingCacheEnabled(false); //必須設置


                                            /*獲取第一張bitmap*/
                                            firstImg.setDrawingCacheEnabled(true); //必須設置
                                            firstBmp = Bitmap.createBitmap(firstImg.getDrawingCache());
                                            firstImg.setDrawingCacheEnabled(false); //必須設置


                                            /*清空第一張和第二張的圖*/
                                            firstImg.setImageBitmap(null);
                                            readyChangeImg.setImageBitmap(null);


                                            /*交換圖*/
                                            firstImg.setImageBitmap(readyChageBmp);
                                            readyChangeImg.setImageBitmap(firstBmp);

                                            firstImg.invalidate();
                                            readyChangeImg.invalidate();

//                                            readyChangeImg.setDrawingCacheEnabled(false); //必須設置
//                                            firstImg.setDrawingCacheEnabled(false); //必須設置

                                            for (int m = 0; m < rChangeList.size(); m++) {
                                                rControl_area.removeView(rChangeList.get(m));
                                            }
                                            rChangeList.clear();
                                            changeCount = 0;
                                            firstImg = null;
                                            readyChangeImg = null;

                                        }
                                    }, 200);
                                }
                            }
                        });
                    } else {
                        img.setOnTouchListener(multiTouchListener);
                        for (int m = 0; m < rChangeList.size(); m++) {
                            rControl_area.removeView(rChangeList.get(i));
                        }
                        rChangeList.clear();
                        changeCount = 0;
                        firstImg = null;
                        readyChangeImg = null;
                    }
                }
            }
        });
    }

    private void setRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTemplate.setLayoutManager(layoutManager);
        adapter = new RecyclerCreationTemplateAdapter(this, bottomList);
        rvTemplate.setAdapter(adapter);

        adapter.setOnRecyclerViewListener(new RecyclerCreationTemplateAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(final int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                    setNoConnect();
                    return;
                }


                MyLog.Set("d", mActivity.getClass(), "點擊第 " + position + " 張");


                if (!isEdit) {

                    areaCount = 0;

                    list_deleteImg.clear();

                    thisPosition = position;

                    setTemplateUrl(thisPosition);

                    selectBackground(thisPosition, lastPosition);

                    lastPosition = thisPosition;

                    adapter.notifyDataSetChanged();

                    clearChangeBitmap();


                } else {


                    DialogV2Custom d = new DialogV2Custom(mActivity);
                    d.setStyle(DialogStyleClass.CHECK);
                    d.setMessage(R.string.change_template);
                    d.setCheckExecute(new CheckExecute() {
                        @Override
                        public void DoCheck() {

                            areaCount = 0;

                            list_deleteImg.clear();

                            thisPosition = position;

                            setTemplateUrl(thisPosition);
                            selectBackground(thisPosition, lastPosition);

                            lastPosition = thisPosition;
                            adapter.notifyDataSetChanged();

                            clearChangeBitmap();

                            isEdit = false;

                        }
                    });
                    d.show();

                }

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });
    }


    /*設置下方版型*/
    private void setFrame() {

        try {
            JSONArray jsonArray = new JSONArray(frame);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String blank = obj.getString("blank");
                String image_url = obj.getString("image_url");
                String image_url_thumbnail = obj.getString("image_url_thumbnail");

                MyLog.Set("d", getClass(), "blank => " + blank);
                MyLog.Set("d", getClass(), "image_url => " + image_url);
                MyLog.Set("d", getClass(), "image_url_thumbnail => " + image_url_thumbnail);
                MyLog.Set("d", getClass(), "--------------------------------------------------");

                HashMap<String, Object> map = new HashMap<>();
                map.put("blank", blank);
                map.put("image_url", image_url);
                map.put("image_url_thumbnail", image_url_thumbnail);

                map.put("select", false);

                bottomList.add(map);
            }

            adapter.notifyDataSetChanged();

            setTemplateUrl(thisPosition);
            selectBackground(thisPosition, lastPosition);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*選擇下方版型時 背景變化*/
    private void selectBackground(int thisposition, int lastPposition) {

        if (lastPposition != bottomList.size()) {
            //原本點擊的恢復
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("blank", (String) bottomList.get(lastPposition).get("blank"));
            hashMap.put("image_url", (String) bottomList.get(lastPposition).get("image_url"));
            hashMap.put("image_url_thumbnail", (String) bottomList.get(lastPposition).get("image_url_thumbnail"));
            hashMap.put("select", false);
            bottomList.set(lastPposition, hashMap);
        }

        //點擊的改背景
        HashMap<String, Object> map = new HashMap<>();
        map.put("blank", (String) bottomList.get(thisposition).get("blank"));
        map.put("image_url", (String) bottomList.get(thisposition).get("image_url"));
        map.put("image_url_thumbnail", (String) bottomList.get(thisposition).get("image_url_thumbnail"));
        map.put("select", true);
        bottomList.set(thisposition, map);

    }

    private void clearChangeBitmap() {

        if (firstBmp != null && !firstBmp.isRecycled()) {
            firstBmp.recycle();
            firstBmp = null;
        }

        if (readyChageBmp != null && !readyChageBmp.isRecycled()) {
            readyChageBmp.recycle();
            readyChageBmp = null;
        }

    }

    /*設置瀏覽畫面*/
    private void setTemplateUrl(int position) {
        picUrl = (String) bottomList.get(position).get("image_url");

        recycleBitmaps();

        if (HttpUtility.isConnect(mActivity)) {
            LoadPictureTask loadPictureTask = new LoadPictureTask();
            loadPictureTask.execute();
        } else {
          setNoConnect();
            return;
        }

        /**2016.08.27 內存溢出 暫時取消*/
//        bigTemplateBmp = mMemoryCache.get(picUrl);
//
//        if (bigTemplateBmp != null && !bigTemplateBmp.isRecycled()) {
//
//            MyLog.Set("d", getClass(), "直接顯示圖片");
//
//            try {
//                rControl_area.removeAllViews();
//                String blank = (String) bottomList.get(thisPosition).get("blank");
//                try {
//                    JSONArray array = new JSONArray(blank);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject object = (JSONObject) array.get(i);
//                        int w = object.getInt("W");
//                        int h = object.getInt("H");
//                        int t = object.getInt("T");
//                        int l = object.getInt("L");
//                        addArea(w, h, t, l);//position => tag
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                temImg = new ImageView(mActivity);
//                temImg.setScaleType(ImageView.ScaleType.FIT_XY);
//                rControl_area.addView(temImg);
//                temImg.setImageBitmap(bigTemplateBmp);
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//        } else {
//
//            if (HttpUtility.isConnect(mActivity)) {
//                LoadPictureTask loadPictureTask = new LoadPictureTask();
//                loadPictureTask.execute();
//            } else {
//                noConnect = new NoConnect(mActivity);
//                return;
//            }
//
//        }

    }

    private ImageView toFragmentImg;
    private RelativeLayout toFragmentRelativeLayout;
    private int toFragmentArea, toFragmentArea_Y;

    public ImageView getToFragmentImg() {
        return toFragmentImg;
    }

    public RelativeLayout getToFragmentRelativeLayout() {
        return toFragmentRelativeLayout;
    }

    public int getToFragmentArea() {
        return toFragmentArea;
    }

    public int getToFragmentArea_Y() {
        return toFragmentArea_Y;
    }

    /*設置各版型內容(格子)*/
    private void addArea(final int w, final int h, int t, int l) {

        /**2016.08.27新增*/
        list_addImg.clear();

        rControl_view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.test_controlpic, null);//單一格
        final RelativeLayout controlR = rControl_view.findViewById(R.id.relativeLayout_control);

        ImageView addImg = controlR.findViewById(R.id.plus);
        list_addImg.add(addImg);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DoubleToInt(w), DoubleToInt(h));
        //left, top, right, bottom
        params.setMargins(DoubleToInt(l), DoubleToInt(t), 0, 0);
        controlR.setLayoutParams(params);
        controlR.setGravity(RelativeLayout.CENTER_IN_PARENT);
        rControl_area.addView(rControl_view);


        final ImageView photoImg = rControl_view.findViewById(R.id.single_control);
        photoImg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        photoImg.setOnTouchListener(multiTouchListener);
        photoImg.setVisibility(View.INVISIBLE);

        /**2016.11.23 new add*/
        HashMap<String, Object> map = new HashMap<>();
        map.put("photoImg", photoImg);
        map.put("rControl_view", rControl_view);
        photoImgList.add(map);


//        int a1[] = {DoubleToInt(l)                 , DoubleToInt(t)};//左上
//        int a2[] = {DoubleToInt(l) + DoubleToInt(w), DoubleToInt(t)};//右上
//        int b1[] = {DoubleToInt(l)                 , DoubleToInt(t) + DoubleToInt(h)}; //左下
//        int b2[] = {DoubleToInt(l) + DoubleToInt(w), DoubleToInt(t) + DoubleToInt(h)}; //右下


        controlR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                 setNoConnect();
                    return;
                }

                toFragmentImg = photoImg;
                toFragmentRelativeLayout = controlR;
                toFragmentArea = DoubleToInt(w);
                toFragmentArea_Y = DoubleToInt(h);


                if(fragmentSelectPhoto == null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(Key.create_mode, 1);
                    fragmentSelectPhoto = new FragmentSelectPhoto();

                    fragmentSelectPhoto.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.frame, fragmentSelectPhoto, fragmentSelectPhoto.getClass().getSimpleName())
                            .commit();
                }else {
                    getSupportFragmentManager().beginTransaction().show(fragmentSelectPhoto).commit();
                }



            }
        });
    }

    /*計算格子大小*/
    private int DoubleToInt(int i) {

        double a = (1336 * 100) / intControlAreaWidth;
        double b = a / 100;  //得到縮小的倍率
        double c = i / b;
        String d = Double.toString(c);
        BigDecimal mData = new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "mData=" + mData);
        }

        return mData.intValue();
    }

    /*設置圖片(格子)*/
    public void setPicture(final ImageView img, final String path, final RelativeLayout controlR, int wArea, int yArea) {

        int w = controlR.getWidth();//use to scale image

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "controlR.getWidth => " + w);
        }


        final Bitmap bitmap = getBitmap(path, w);

        if (bitmap != null && !bitmap.isRecycled()) {
            pictureBmps.add(bitmap);
            img.setImageBitmap(bitmap);
            img.setVisibility(View.VISIBLE);
            img.invalidate();
        }
        int x = (int) controlR.getX();
        int y = (int) controlR.getY();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "x =>" + x);
            Log.d(TAG, "y =>" + y);

        }


        final ImageView deleteImg = new ImageView(mActivity);
        int wDelete = intControlAreaWidth / 8; //delete長寬

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(wDelete, wDelete);
        params.setMargins(x + wArea - wDelete, y + yArea - wDelete, 0, 0);
        deleteImg.setLayoutParams(params);
        deleteImg.setPadding(2,2,2,2);
        deleteImg.setBackgroundResource(R.drawable.click_2_0_0_circle_pink_second);
        deleteImg.setImageResource(R.drawable.ic200_delete_white);
        rControl_area.addView(deleteImg);
//        button_template_add_picture_changphoto

        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setStyle(DialogStyleClass.CHECK);
                d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_delete_photo);
                d.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                            System.gc();
                        }
                        img.setImageBitmap(null);
                        img.setVisibility(View.INVISIBLE);
                        img.invalidate();

                        rControl_area.removeView(deleteImg);

                        /**2016.11.14 getTag to check*/
                        if (list_deleteImg != null && list_deleteImg.size() > 0) {
                            for (int i = 0; i < list_deleteImg.size(); i++) {
                                String strTag = (String) list_deleteImg.get(i).getTag();
                                if (strTag.equals(path)) {
                                    list_deleteImg.remove(i);
                                    break;
                                }
                            }
                        }

                        MyLog.Set("d", getClass(), "當前有照片的格數 => " + list_deleteImg.size());
                    }
                });

                d.show();

            }
        });

        /**2016.11.14 new add setTag by path*/
        deleteImg.setTag(path);

        list_deleteImg.add(deleteImg);//將delete放進list裡 確認前要移掉

        MyLog.Set("d", getClass(), "當前有照片的格數 => " + list_deleteImg.size());

        isEdit = true;//編輯狀態

    }

    /*獲取圖片*/
    private Bitmap getBitmap(String picPath, int controlW) {

        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(picPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "o.outWidth => " + w);
            Log.d(TAG, "o.outHeight => " + h);
        }

        int wh = w * h;

        if (w < controlW) {
            opts.inSampleSize = 1;
        } else {


            int d = w / controlW; //將圖片除框架得倍率

            MyLog.Set("d", getClass(), "w/controlW => " + d);

            opts.inSampleSize = d;
        }
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeFile(picPath, opts);


//        if (w * 3 == h * 2) {
//            bitmap = FileUtility.getImageThumbnail(picPath, ww, hh);
//        } else {
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            if (wh < 2000000) {
//
//                if(wh<100000){
//                    opts.inSampleSize = 1;
//                }else {
//                    opts.inSampleSize = 2;
//                }
//
//            } else {
//                opts.inSampleSize = 3; //大於2000000
//            }
//            opts.inPreferredConfig = Bitmap.Config.RGB_565;
//            bitmap = BitmapFactory.decodeFile(picPath, opts);
//        }
        return bitmap;

    }

    /*確認上傳*/
    private void confirm() {

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }

                if (!HttpUtility.isConnect(mActivity)) {
                 setNoConnect();
                    return;
                }


//                /**2016.10.18修改為有相片才能確認*/list_deleteImg.size() > 0
                if (list_deleteImg != null && list_deleteImg.size() == areaCount) {

                    for (int i = 0; i < list_deleteImg.size(); i++) {
                        list_deleteImg.get(i).setVisibility(View.GONE);
                    }

                    for (int i = 0; i < list_addImg.size(); i++) {
                        list_addImg.get(i).setVisibility(View.GONE);
                    }

                    doUpLoad();

                } else {

                    PinPinToast.showErrorToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_grid_empty);

                }


            }
        });


    }

    private void getUploadFile() {
        String createPath = sdpath + myDir + "elect.jpg";
        File f = new File(createPath);
        upBmp = BitmapUtility.createViewBitmap(rControl_area);//view轉成bitmap
        upBmp = ThumbnailUtils.extractThumbnail(upBmp, 668, 1002); //轉成要上傳的大小
        if (f.exists()) {
            f.delete();
        }
        try {
            FileUtility.createSDFile(createPath);//建立新檔案

            MyLog.Set("d", getClass(), "建立空白文件成功");

            uploadFile = new File(createPath);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(uploadFile);
            } catch (FileNotFoundException e) {
                MyLog.Set("e", getClass(), "建立上傳用圖片失敗");
                e.printStackTrace();
            }
            upBmp.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (Exception e) {
            MyLog.Set("e", getClass(), "建立文件失敗");
            if (f.exists()) {
                f.delete();
            }
            e.printStackTrace();
        }

    }

    public void recycleBitmaps() {
        if (bigTemplateBmp != null && !bigTemplateBmp.isRecycled()) {
            bigTemplateBmp.recycle();
            bigTemplateBmp = null;
        }
        if (upBmp != null && !upBmp.isRecycled()) {
            upBmp.recycle();
            upBmp = null;
        }

        if (pictureBmps != null && pictureBmps.size() > 0) {

            for (int i = 0; i < pictureBmps.size(); i++) {
                if (pictureBmps.get(i) != null && !pictureBmps.get(i).isRecycled()) {
                    pictureBmps.get(i).recycle();
                }
            }
        }
        System.gc();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case 100:
//                    boolean changed = true;
//                    if (null != data) {
//                        Bundle extra = data.getExtras();
//                        if (null != extra) {
//                            // image was changed by the user?
//                            changed = extra.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
////                            String path = selectSinglePicture.getPicPath();
//                            setPicture(touchImageView, SelectPicturePath.path, rrrr, wArea);
//
//
//                        }
//                    }
//                    if (!changed) {
//                        Log.w("TAG", "User did not modify the image, but just clicked on 'Done' button");
//                    }
//                    break;
//                case 1:
//                    List<String> listdata = selectSinglePicture.getNewPathList();
//
//
//                    if (data != null) {
//                    /*取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意*/
//                        Uri mImageCaptureUri = data.getData();
//
//                   /* 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取*/
//                        if (mImageCaptureUri != null) {
//
//                            String path = FileUtility.getImageAbsolutePath(mActivity, mImageCaptureUri);
//
//                            listdata.add(0, path);
//                            selectSinglePicture.setNewPathList(listdata);
//                            selectSinglePicture.getSinglePictureAdapter().notifyDataSetChanged();
//                        }
//
////                         else {
////                        Bundle extras = data.getExtras();
////                        if (extras != null) {
////
////                            Log.d("CreateAlbumActivity", "onActivityResult => if (extras != null)");
////
////                            //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
//////                            Bitmap image = extras.getParcelable("data");
//////                            if (image != null) {
//////                                photo.setImageBitmap(image);
//////                            }
////
////
////                        }
////                    }
//
//                    }
//                    break;
//
//
//            }
//        }
//
//    }

    private void back() {
        back = findViewById(R.id.backImg);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                ActivityAnim.FinishAnim(mActivity);

            }
        });
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getIdentity() {
        return identity;
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {
                switch (doingType) {
                    case DoUpLoad:
                        doUpLoad();
                        break;
                }
            }
        };
        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    private void doUpLoad() {

        if (!HttpUtility.isConnect(this)) {
        setNoConnect();
            return;
        }
        upLoadTask = new UpLoadTask();
        upLoadTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class UpLoadTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoUpLoad;
            loading.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            getUploadFile();

            Map<String, String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("token", token);
            data.put("album_id", album_id);

            String sign = IndexSheet.encodePPB(data);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put("id", id);
            sendData.put("token", token);
            sendData.put("album_id", album_id);
            sendData.put("sign", sign);

            String strJson = "";
            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P58_InsertPhotoOfDiy, sendData, uploadFile);
                MyLog.Set("d", getClass(), "p58strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p58Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p58Result = jsonObject.getString(Key.result);
                    if (p58Result.equals("1")) {
                        recycleBitmaps();

                    } else if (p58Result.equals("0")) {
                        p58Message = jsonObject.getString(Key.message);
                    }

                } catch (Exception e) {
                    p58Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            loading.dismiss();
            if (p58Result.equals("1")) {

                CreationTemplate2Activity.this.setResult(UpLoad_OK);
                finish();
                ActivityAnim.FinishAnim(mActivity);

            } else if (p58Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p58Message);


            } else if (p58Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());

            }
        }
    }

    private class LoadPictureTask extends AsyncTask<Void, Void, Object> {

        private RelativeLayout r;
        private ImageView loadImg;

        public LoadPictureTask() {

            r = new RelativeLayout(mActivity);
            r.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            loadImg = new ImageView(mActivity);
            loadImg.setLayoutParams(new RelativeLayout.LayoutParams(DensityUtility.dip2px(mActivity, 30), DensityUtility.dip2px(mActivity, 30)));
            loadImg.setImageResource(R.drawable.loading);
            r.addView(loadImg);

            rControl_area.addView(r);


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setDuration(1500);
            rotateAnimation.setRepeatCount(-1);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            loadImg.setVisibility(View.VISIBLE);
            loadImg.setAnimation(rotateAnimation);


        }

        @Override
        protected Object doInBackground(Void... params) {


            try {
                bigTemplateBmp = Picasso.with(mActivity)
                        .load(picUrl)
                        .config(Bitmap.Config.RGB_565)
                        .resize(intControlAreaWidth, intControlAreaHeight)
                        .get();
            } catch (Exception e) {


                e.printStackTrace();
            }


            /**2016.08.27 內存溢出 暫時取消*/
//            if (bigTemplateBmp != null) {
//                mMemoryCache.put(picUrl, bigTemplateBmp);
//                Log.e(TAG, "保存圖片");
//            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            rControl_area.removeView(r);

            rControl_area.removeAllViews();

            String blank = (String) bottomList.get(thisPosition).get("blank");
            try {
                JSONArray array = new JSONArray(blank);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    int w = object.getInt("W");
                    int h = object.getInt("H");
                    int t = object.getInt("T");
                    int l = object.getInt("L");
                    addArea(w, h, t, l);

                }

                /**2016.10.18新增判斷格子數量*/
                areaCount = array.length();


            } catch (Exception e) {
                e.printStackTrace();
            }

            temImg = new ImageView(mActivity);
            temImg.setScaleType(ImageView.ScaleType.FIT_XY);
            rControl_area.addView(temImg);

            temImg.setImageBitmap(bigTemplateBmp);


        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        MyLog.Set("d", getClass(), "resultCode => " + resultCode );
        MyLog.Set("d", getClass(), "requestCode => " + requestCode );

    }

    @Override
    public void onBackPressed() {


        finish();
        ActivityAnim.FinishAnim(mActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {
           setNoConnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vto.isAlive() && myViewTreeObserver!=null) {
            vto.removeOnGlobalLayoutListener(myViewTreeObserver);
            vto = null;
        }
    }


    @Override
    public void onDestroy() {

        SystemUtility.SysApplication.getInstance().removeActivity(mActivity);

        if (upLoadTask != null && !upLoadTask.isCancelled()) {
            upLoadTask.cancel(true);
            upLoadTask = null;
        }

        for (int i = 0; i < bottomList.size(); i++) {
            Picasso.with(mActivity.getApplicationContext()).invalidate((String) bottomList.get(i).get("image_url_thumbnail"));
        }

        recycleBitmaps();

        clearChangeBitmap();

        try {
            Picasso.with(mActivity).invalidate(picUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File f = new File(sdpath + myDir + "elect.jpg");
        if (f.exists()) {
            f.delete();
        }

        System.gc();


        super.onDestroy();
    }

}


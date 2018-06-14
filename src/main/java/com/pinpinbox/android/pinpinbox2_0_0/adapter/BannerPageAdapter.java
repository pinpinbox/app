package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pinpinbox.android.R;
import com.pinpinbox.android.SampleTest.Templateinfo.TemplateInfoActivity;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AdHighLight2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.RequestCodeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;

import org.json.JSONArray;

import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;


/**
 * Created by kevin9594 on 2016/12/22.
 */
public class BannerPageAdapter extends PagerAdapter {

    private Activity mActivity;
    private List<View> viewList;
    private ArrayList<HashMap<String, Object>> arrayList;
    private RequestOptions opts;


//    private List<GifDrawable> gifResource;
//
//    public List<GifDrawable> getGifResource(){
//        return this.gifResource;
//    }

//    private List<HashMap<String, Object>> gifResource;
//
//    public List<HashMap<String, Object>> getGifResource(){
//        return this.gifResource;
//    }

    private Fragment fragment;

    private List<HashMap<String, Object>> gifList;

    public BannerPageAdapter(Activity mActivity, List<View> viewList, ArrayList<HashMap<String, Object>> arrayList, Fragment fragment) {

        this.mActivity = mActivity;
        this.viewList = viewList;
        this.arrayList = arrayList;
        this.fragment = fragment;


        int radius = 0;
        if (SystemUtility.isTablet(mActivity.getApplicationContext())) {

            //平版
            radius = 4;

        } else {

            //手機
            radius = 6;

        }


        opts = new RequestOptions()
//                .transform(new RoundTransform(mActivity, radius))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.bg_2_0_0_no_image);

//        gifResource = new ArrayList<>();

        gifList = new ArrayList<>();


    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {


        container.removeView(viewList.get(position));

    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View v = viewList.get(position);

        final ImageView img = (ImageView) v.findViewById(R.id.bannerImg);

//        final RoundCornerImageView gifImg = (RoundCornerImageView)v.findViewById(R.id.gifImg);

        /*20171102*/
        if (arrayList == null || arrayList.size() < 1) {

            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageResource(R.drawable.bg_2_0_0_no_image);

            container.addView(viewList.get(position));
            return viewList.get(position);
        }


        final String imageUrl = (String) arrayList.get(position).get(Key.image);

        if (SystemUtility.Above_Equal_V5()) {

            img.setTransitionName(imageUrl);

        }

        if (imageUrl != null && !imageUrl.equals("") && !imageUrl.equals("null")) {

            String end = imageUrl.substring(imageUrl.lastIndexOf(".") + 1, imageUrl.length()).toLowerCase();

            if (end.equals("gif")) {


                Glide.with(mActivity.getApplicationContext())
                        .asGif()
                        .load(imageUrl)
                        .apply(opts)
                        .into(img);


                boolean urlIsExist = false;
                if (gifList != null && gifList.size() > 0) {
                    for (int i = 0; i < gifList.size(); i++) {
                        String url = (String) gifList.get(i).get(Key.url);
                        if (url.equals(imageUrl)) {
                            urlIsExist = true;
                            break;
                        }
                    }
                }

                if (!urlIsExist) {

                    MyLog.Set("e", getClass(), "gif url 不存在並添加到gifList");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(Key.url, imageUrl);
                    map.put(Key.imageView, img);
                    gifList.add(map);

                } else {

                    MyLog.Set("e", getClass(), "gif url 已存在");

                }


//                img.setVisibility(View.GONE);
//                gifImg.setVisibility(View.VISIBLE);

//                LoadGifTask task = new LoadGifTask(gifImg, position);
//                task.execute();


            } else {

//                img.setVisibility(View.VISIBLE);
//                gifImg.setVisibility(View.GONE);

//                Glide.with(mActivity.getApplicationContext())
//                        .load(url)
//                        .apply(opts)
//                        .into(img);


                Glide.with(mActivity.getApplicationContext())
                        .asBitmap()
                        .load(imageUrl)
                        .apply(opts)
                        .into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                                img.setImageBitmap(resource);


                                //判斷第一頁
                                if (position == 0) {


                                    //判斷是否是活動
                                    String event_id = (String) arrayList.get(position).get(Key.event_id);
                                    if (!event_id.equals("")) {

                                        //判斷image是否曾經顯示過
                                        boolean isUrlExist = false;
                                        String bannerList = PPBApplication.getInstance().getData().getString(Key.oldbannerUrlList, "[]");
                                        try {
                                            JSONArray bannerArray = new JSONArray(bannerList);
                                            for (int i = 0; i < bannerArray.length(); i++) {
                                                String strOldImageUrl = (String) bannerArray.get(i);
                                                if (strOldImageUrl.equals(imageUrl)) {
                                                    isUrlExist = true;
                                                    break;
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

//                                        List<Object> bannerImageUrlList = null;
//
//                                        if (!bannerList.equals("")) {
//
//                                            bannerImageUrlList = StringUtil.StringToList(bannerList);
//                                            for (int i = 0; i < bannerImageUrlList.size(); i++) {
//                                                String strOldImageUrl = (String) bannerImageUrlList.get(i);
//                                                if (strOldImageUrl.equals(imageUrl)) {
//                                                    isUrlExist = true;
//                                                    break;
//                                                }
//                                            }
//
//                                        }

                                        if (!isUrlExist) {

                                            MyLog.Set("e", BannerPageAdapter.class, "活動還沒顯示過");

                                            Bundle bundle = new Bundle();
                                            bundle.putString(Key.image, (String) arrayList.get(position).get(Key.image));
                                            bundle.putString(Key.event_id, (String) arrayList.get(position).get(Key.event_id));
                                            Intent intent = new Intent(mActivity, AdHighLight2Activity.class).putExtras(bundle);

                                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                                    makeSceneTransitionAnimation(mActivity,
                                                            img,
                                                            ViewCompat.getTransitionName(img));
//                                            mActivity.startActivity(intent, options.toBundle());

                                            fragment.startActivityForResult(intent, RequestCodeClass.CloseHighLight, options.toBundle());


                                        } else {


                                            MyLog.Set("e", BannerPageAdapter.class, "活動已顯示過");

//                                            ((FragmentHome2)fragment).setAutoScroll();


                                        }


                                    }


                                }

                            }
                        });


            }


        } else {
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageResource(R.drawable.bg_2_0_0_no_image);
        }


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
                    return;
                }


                if (arrayList != null && arrayList.size() > 0) {

                    FlurryUtil.onEvent(FlurryKey.home_click_event);

                    Bundle bundle = new Bundle();

                    Intent intent = new Intent();

                    String url = (String) arrayList.get(position).get(Key.url);

                    String event_id = (String) arrayList.get(position).get(Key.event_id);
                    String album_id = (String) arrayList.get(position).get(Key.album_id);
                    String template_id = (String) arrayList.get(position).get(Key.template_id);
                    String user_id = (String) arrayList.get(position).get(Key.user_id);


                    if (album_id != null && !album_id.equals("")) {


                        ActivityIntent.toAlbumInfo(mActivity, false, album_id, null, 0, null);

                        return;
                    }

                    if (template_id != null && !template_id.equals("")) {
                        bundle.putString(Key.template_id, template_id);
                        intent.putExtras(bundle);
                        intent.setClass(mActivity, TemplateInfoActivity.class);
                        mActivity.startActivity(intent);
                        ActivityAnim.StartAnim(mActivity);
                        return;
                    }

                    if (user_id != null && !user_id.equals("")) {

                        ActivityIntent.toUser(mActivity, false, false, user_id, null, null ,null);

                        return;
                    }

                    if (event_id != null && !event_id.equals("")) {

                        ActivityIntent.toEvent(mActivity, event_id);

                    } else {

                        if (url == null || url.equals("")) {

                            PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_null_intent);

                        } else {

                            Uri uri = Uri.parse(url);

                            String lastPath = uri.getLastPathSegment();

                            if(lastPath.equals("point")){
                                ActivityIntent.toBuyPoint(mActivity);
                            }else {


                                String categoryareaId = uri.getQueryParameter(Key.categoryarea_id);
                                if (categoryareaId != null && !categoryareaId.equals("")) {
                                    ActivityIntent.toFeature(mActivity, StringIntMethod.StringToInt(categoryareaId));
                                } else {
                                    bundle.putString("url", url);
                                    intent.putExtras(bundle);
                                    intent.setClass(mActivity, WebView2Activity.class);
                                    mActivity.startActivity(intent);
                                    ActivityAnim.StartAnim(mActivity);
                                }


                            }




                        }


                    }

                }

            }
        });

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

    private class LoadGifTask extends AsyncTask<Void, Void, Object> {

        private GifImageView gifImg;
        private int position;

        private ByteBuffer buffer;
//        private GifDrawable gifFromBytes;


        public LoadGifTask(GifImageView gifImg, int position) {

            this.gifImg = gifImg;
            this.position = position;

        }

        public void startGif() {
//            if (gifFromBytes != null) {
//                gifFromBytes.start();
//            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                String url = (String) arrayList.get(position).get(Key.image);

                URLConnection urlConnection = new URL(url).openConnection();
                urlConnection.connect();

                int contentLength = urlConnection.getContentLength();
                buffer = ByteBuffer.allocateDirect(contentLength);
                ReadableByteChannel channel = Channels.newChannel(urlConnection.getInputStream());

                while (buffer.remaining() > 0)
                    channel.read(buffer);
                channel.close();


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

//            try {
//                gifFromBytes = new GifDrawable(buffer);
//
//                gifImg.setImageDrawable(gifFromBytes);
//
////                gifFromBytes.stop();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }

    }


    public List<HashMap<String, Object>> getGifList() {

        return this.gifList;
    }

    public RequestOptions getOpts() {
        return this.opts;
    }


}

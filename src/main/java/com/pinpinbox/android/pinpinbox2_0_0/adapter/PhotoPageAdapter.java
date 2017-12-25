package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.PinchImageView;
import com.pinpinbox.android.Widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemPhoto;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin9594 on 2017/2/26.
 */

public class PhotoPageAdapter extends PagerAdapter {

    private Activity mActivity;

    private List<ItemPhoto> itemPhotoList;

    private ItemAlbum itemAlbum;

//    private Picasso.Builder picasso;

//    public Picasso.Builder getPicasso(){
//        return this.picasso;
//    }


    public PhotoPageAdapter(Activity mActivity, List<ItemPhoto> itemPhotoList, ItemAlbum itemAlbum) {

        this.mActivity = mActivity;
        this.itemPhotoList = itemPhotoList;
        this.itemAlbum = itemAlbum;


//        OkHttpClientManager.getInstance().getOKHttp().setConnectTimeout(8, TimeUnit.SECONDS);
//        OkHttpClientManager.getInstance().getOKHttp().setReadTimeout(8, TimeUnit.SECONDS);
//        OkHttpClientManager.getInstance().getOKHttp().setWriteTimeout(8, TimeUnit.SECONDS);


//        picasso = new Picasso.Builder(mActivity.getApplicationContext()).downloader(
//                new OkHttpDownloader(OkHttpClientManager.getInstance().getOKHttp()));

    }


    public void destroyItem(final ViewGroup view, final int position, final Object object) {


        MyLog.Set("d", PhotoPageAdapter.class, "destroyItem position => " + position);


        PinchImageView picImg = (PinchImageView) ((View) object).findViewById(R.id.photoImg);
        picImg.reset();
        picImg.setImageBitmap(null);
        view.removeView((View) object);

        if (position != itemPhotoList.size()) {

            String url = itemPhotoList.get(position).getImage_url();

            if (url != null && !url.equals("") && !url.equals("null")) {
                Picasso.with(mActivity).invalidate(url);
//                picasso.build().invalidate(url);


//                try{
//                    picasso.build().invalidate(url);
//                }catch (OutOfMemoryError outOfMemoryError){
//                    outOfMemoryError.printStackTrace();
//                }


            }
        }

    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        View v = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.page_2_0_0_photo, null);

        v.setId(position);

        final PinchImageView picImg = (PinchImageView) v.findViewById(R.id.photoImg);

        final ImageView refreshImg = (ImageView) v.findViewById(R.id.refreshImg);


        if (picImg != null) {

            String url = itemPhotoList.get(position).getImage_url();

            if (itemAlbum.isOwn()) {
                //已收藏或贊助
                if (url == null || url.equals("") || url.equals("null")) {
                    picImg.setImageResource(R.drawable.bg_2_0_0_no_image);

                    MyLog.Set("d", PhotoPageAdapter.class, "url => null or empty");

                    refreshImg.setVisibility(View.VISIBLE);

                } else {

//                    /*20171023*/
//                    picasso.build()
//                            .load(url)
//                            .config(Bitmap.Config.RGB_565)
//                            .error(R.drawable.no_image)
//                            .tag(mActivity.getApplicationContext())
//                            .into(picImg, new Callback() {
//                                @Override
//                                public void onSuccess() {
//                                    refreshImg.setVisibility(View.GONE);
//                                }
//
//                                @Override
//                                public void onError() {
//                                    MyLog.Set("d", PhotoPageAdapter.class, "new Callback => onError");
//                                    refreshImg.setVisibility(View.VISIBLE);
//                                }
//                            });


                    Picasso.with(mActivity.getApplicationContext())
                            .load(url)
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.bg_2_0_0_no_image)
                            .tag(mActivity.getApplicationContext())
                            .into(picImg, new Callback() {
                                @Override
                                public void onSuccess() {
                                    refreshImg.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    MyLog.Set("d", PhotoPageAdapter.class, "new Callback => onError");
                                    refreshImg.setVisibility(View.VISIBLE);
                                }
                            });
                }

            } else {
                //尚未收藏贊助
                if (position == itemPhotoList.size() - 1) {

                    picImg.setBackgroundResource(R.drawable.bg200_preview_normal);

                } else {

                    if (url == null || url.equals("") || url.equals("null")) {
                        picImg.setImageResource(R.drawable.bg_2_0_0_no_image);
                        MyLog.Set("d", PhotoPageAdapter.class, "url => null or empty");
                        refreshImg.setVisibility(View.VISIBLE);
                    } else {


//                        /*20171023*/
//                        picasso.build()
//                                .load(url)
//                                .config(Bitmap.Config.RGB_565)
//                                .error(R.drawable.no_image)
//                                .tag(mActivity.getApplicationContext())
//                                .into(picImg, new Callback() {
//                                    @Override
//                                    public void onSuccess() {
//                                        refreshImg.setVisibility(View.GONE);
//                                    }
//
//                                    @Override
//                                    public void onError() {
//                                        MyLog.Set("d", PhotoPageAdapter.class, "new Callback => onError");
//                                        refreshImg.setVisibility(View.VISIBLE);
//                                    }
//                                });


                        Picasso.with(mActivity.getApplicationContext())
                                .load(url)
                                .config(Bitmap.Config.RGB_565)
                                .error(R.drawable.bg_2_0_0_no_image)
                                .tag(mActivity.getApplicationContext())
                                .into(picImg, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        refreshImg.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError() {
                                        MyLog.Set("d", PhotoPageAdapter.class, "new Callback => onError");
                                        refreshImg.setVisibility(View.VISIBLE);
                                    }
                                });


                    }
                }
            }
        }


        container.addView(v, 0);
        return v;

    }


    @Override
    public int getCount() {
        return itemPhotoList.size();
    }


    //判斷是否由對象生成介面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}


//public class PhotoPageAdapter extends PagerAdapter {
//
//    private Activity mActivity;
//
//    private List<ItemPhoto> itemPhotoList;
//
//    private ItemAlbum itemAlbum;
//
//
//    public PhotoPageAdapter(Activity mActivity, List<ItemPhoto> itemPhotoList, ItemAlbum itemAlbum) {
//
//        this.mActivity = mActivity;
//        this.itemPhotoList = itemPhotoList;
//        this.itemAlbum = itemAlbum;
//
//
//    }
//
//
//    public void destroyItem(final ViewGroup view, final int position, final Object object) {
//
//        PinchImageView picImg = (PinchImageView) ((View) object).findViewById(R.id.photoImg);
//        picImg.reset();
//        picImg.setImageBitmap(null);
//        view.removeView((View) object);
//
//        if (position != itemPhotoList.size()) {
//
//            String url = itemPhotoList.get(position).getImage_url();
//
//            String end = url.substring(url.lastIndexOf(".") + 1, url.length()).toLowerCase();
//
//
//            if (url != null && !url.equals("") && !url.equals("null")) {
//
//
//                if (end.equals("gif")) {
//
////                    if (!((GifDrawable)picImg.getDrawable()).isRecycled()) {
////                        ((GifDrawable)picImg.getDrawable()).recycle();
////                    }
//
//                } else {
//
//                    Glide.get(mActivity.getApplicationContext()).clearMemory();
//
//                }
//
//            }
//        }
//
//    }
//
//
//    @Override
//    public Object instantiateItem(final ViewGroup container, final int position) {
//
//        View v = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.page_2_0_0_photo, null);
//
//        v.setId(position);
//
//        final PinchImageView picImg = (PinchImageView) v.findViewById(R.id.photoImg);
//
//        String url = itemPhotoList.get(position).getImage_url();
//
//
//        if (picImg != null) {
//
//
//            if (itemAlbum.isOwn()) {
//                //已收藏或贊助
//                setImg(url, position, picImg);
//
//            } else {
//                //尚未收藏贊助
//                if (position == itemPhotoList.size() - 1) {
//
//                    picImg.setBackgroundResource(R.drawable.bg200_preview_normal);
//
//                } else {
//                    setImg(url, position, picImg);
//                }
//            }
//        }
//
//
//        container.addView(v, 0);
//        return v;
//
//    }
//
//
//    private void setImg(String url, int position, PinchImageView picImg) {
//        if (url == null || url.equals("") || url.equals("null")) {
//            picImg.setImageResource(R.drawable.bg_2_0_0_no_image);
//        } else {
//
//            RequestOptions opts = new RequestOptions()
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .error(R.drawable.bg_2_0_0_no_image);
//
//            String end = url.substring(url.lastIndexOf(".") + 1, url.length()).toLowerCase();
//
//            if (end.equals("gif")) {
//
//                final LoadGifTask task = new LoadGifTask(picImg, position);
//                task.execute();
//
////                picImg.setOnLongClickListener(new View.OnLongClickListener() {
////                    @Override
////                    public boolean onLongClick(View view) {
////
////                        if(task!=null){
////                            task.startGif();
////                        }
////
////                        return false;
////                    }
////                });
//
//            } else {
//
//                Glide.with(mActivity.getApplicationContext())
//                        .load(url)
//                        .apply(opts)
//                        .into(picImg);
//
//            }
//
//        }
//    }
//
//
//    @Override
//    public int getCount() {
//        return itemPhotoList.size();
//    }
//
//
//    //判斷是否由對象生成介面
//    @Override
//    public boolean isViewFromObject(View arg0, Object arg1) {
//        return (arg0 == arg1);
//    }
//
//    private class LoadGifTask extends AsyncTask<Void, Void, Object> {
//
//        private GifImageView gifImg;
//        private int position;
//
//        private ByteBuffer buffer;
//        private GifDrawable gifFromBytes;
//
//
//        public LoadGifTask(GifImageView gifImg, int position) {
//
//            this.gifImg = gifImg;
//            this.position = position;
//
//        }
//
//        public void startGif() {
//            if(gifFromBytes!=null) {
//                gifFromBytes.start();
//            }
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            try {
//
//                String url = itemPhotoList.get(position).getImage_url();
//
//                URLConnection urlConnection = new URL(url).openConnection();
//                urlConnection.connect();
//
//                int contentLength = urlConnection.getContentLength();
//                buffer = ByteBuffer.allocateDirect(contentLength);
//                ReadableByteChannel channel = Channels.newChannel(urlConnection.getInputStream());
//
//                while (buffer.remaining() > 0)
//                    channel.read(buffer);
//                channel.close();
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//
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
//
//        }
//
//    }
//
//}

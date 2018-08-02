package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class SetVideoImage {

    private Activity mActivity;

    public SetVideoImage(Activity mActivity) {

        this.mActivity = mActivity;

        if (opts == null) {
            opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
        }


        if (lruCache == null) {
            int maxMemory = (int) Runtime.getRuntime().maxMemory();//获取最大的运行内存
            int maxSize = maxMemory / 4;//拿到缓存的内存大小 35         lruCache = new LruCache<String, Bitmap>(maxSize){
            lruCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    if (value != null) {
                        return value.getByteCount();
                    } else {
                        return 0;
                    }
                }
            };
        }
    }


    public void start(int videoId, String videoCoverPath, ImageView intoImg) {

        GetVideoTask getVideoTask = new GetVideoTask(videoId, videoCoverPath, intoImg);
        getVideoTask.execute();

    }


    @SuppressLint("StaticFieldLeak")
    private class GetVideoTask extends AsyncTask<Void, Void, Object> {

        private int videoId;
        private String videoCoverPath;
        private ImageView intoImg;
        private Bitmap bitmap;

        private GetVideoTask(int videoId, String videoCoverPath, ImageView intoImg) {
            this.videoId = videoId;
            this.videoCoverPath = videoCoverPath;
            this.intoImg = intoImg;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            bitmap = MediaStore.Video.Thumbnails.getThumbnail(mActivity.getContentResolver(), videoId, MediaStore.Video.Thumbnails.MICRO_KIND, opts);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (videoCoverPath != null && bitmap != null && lruCache != null) {
                if (getVideoThumb(videoCoverPath) == null) {
                    lruCache.put(videoCoverPath, bitmap);
                }
                intoImg.setImageBitmap(bitmap);
            }

        }

    }

    private static BitmapFactory.Options opts;

    public static LruCache<String, Bitmap> lruCache;

    public static Bitmap getVideoThumb(String videoCoverPath) {
        if (lruCache != null) {
            return lruCache.get(videoCoverPath);
        } else {
            return null;
        }
    }


}

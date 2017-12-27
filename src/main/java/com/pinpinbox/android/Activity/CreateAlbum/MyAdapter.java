package com.pinpinbox.android.Activity.CreateAlbum;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.Utility.HttpUtility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kevin9594 on 2016/7/8.
 */
public class MyAdapter extends RecyclerView.Adapter {

    public Activity mActivity;
    public ArrayList<HashMap<String, Object>> listData;
    public LruCache<String, Bitmap> mMemoryCache;

    public MyAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData) {
        super();
        this.mActivity = activity;
        this.listData = listData;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.list_item_2_0_0_creation, null);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder)holder;

        LoadTask loadTask = new LoadTask(viewHolder.picImg,position);
        loadTask.execute();


        if(position ==0){
            viewHolder.name.setText("封面");
        }else
        {
            viewHolder.name.setText(StringIntMethod.IntToString(position));
        }

        if((Object)listData.get(position).get("select")!=null) {
            viewHolder.select = (boolean) listData.get(position).get("select");
            if (viewHolder.select) {
                viewHolder.r.setVisibility(View.VISIBLE);
            } else {
                viewHolder.r.setVisibility(View.INVISIBLE);
            }
        }

        String audioUrl = (String) listData.get(position).get("audio_url");

        if (audioUrl == null || audioUrl.equals("null") || audioUrl.equals("")) {
            viewHolder.rAudio.setVisibility(View.GONE);
        } else {
            //有錄音 可播放
            viewHolder.rAudio.setVisibility(View.VISIBLE);
        }

        String videoUrl = (String) listData.get(position).get("video_url");

        if (videoUrl == null || videoUrl.equals("null") || videoUrl.equals("")) {
            viewHolder.rVideo.setVisibility(View.GONE);
        } else {
            //有錄像 可播放
            viewHolder.rVideo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picImg;

        public TextView name;

        public RelativeLayout r, rAudio, rVideo;

        public boolean select;

        public ViewHolder(View itemView) {
            super(itemView);

            picImg = (ImageView)itemView.findViewById(R.id.item_pic);
            name = (TextView)itemView.findViewById(R.id.name);
            r = (RelativeLayout)itemView.findViewById(R.id.rBorder);
//            rAudio = (RelativeLayout)itemView.findViewById(R.id.rAudio);
//            rVideo = (RelativeLayout)itemView.findViewById(R.id.rVideo);
            select = false;
        }
    }

    class LoadTask extends AsyncTask<Void, Void, Object> {

        private ImageView imageView;
        private int position;
        private Bitmap bitmap;
        private int w, h;



        public LoadTask( ImageView imageView , int position){
            this.position = position;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            //第一个执行方法
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            bitmap = HttpUtility.getNetBitmap((String) listData.get(position).get("image_url_thumbnail"));
            w = bitmap.getWidth();
            h = bitmap.getHeight();
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            //doInBackground返回时触发，换句话说，就是doInBackground执行完后触发
            //这里的result就是上面doInBackground执行后的返回值，所以这里是"执行完毕"
            super.onPostExecute(result);

            try {

                if(w*3 == h*2){
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }

                imageView.setImageBitmap(bitmap);
                if(bitmap!=null){
                    mMemoryCache.put((String) listData.get(position).get("image_url_thumbnail"),bitmap);
                }
            }catch (Exception e){
                System.out.println("--------------------------------------------- 3 --------------------------------------------------------");
                e.printStackTrace();
            }
        }
    }

    public LruCache<String,Bitmap> getAdapterMemoryCache(){
        return this.mMemoryCache;
    }


}

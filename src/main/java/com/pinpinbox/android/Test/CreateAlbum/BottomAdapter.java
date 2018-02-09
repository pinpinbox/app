package com.pinpinbox.android.Test.CreateAlbum;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.Utility.HttpUtility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kevin9594 on 2015/11/28.
 */
public class BottomAdapter extends BaseAdapter {


    private LayoutInflater mInflater;
    private Activity mActivity;
    private ArrayList<HashMap<String, Object>> listData;
    public LruCache<String,Bitmap> mMemoryCache ;

    public BottomAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData) {
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
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(final int position) {

        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            mInflater = LayoutInflater.from(mActivity);
            convertView = mInflater.inflate(R.layout.list_item_2_0_0_creation, null);
            holder.picImg = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.typeImg = (ImageView) convertView.findViewById(R.id.typeImg);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.r = (RelativeLayout) convertView.findViewById(R.id.rBorder);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        LoadTask loadTask = new LoadTask(holder.picImg,position);
        loadTask.execute();


        if(position ==0){
            holder.name.setText("封面");
        }else {
            holder.name.setText(StringIntMethod.IntToString(position));
        }

        if((Object)listData.get(position).get("select")!=null) {
            holder.select = (boolean) listData.get(position).get("select");
            if (holder.select) {
                holder.r.setVisibility(View.VISIBLE);
            } else {
                holder.r.setVisibility(View.INVISIBLE);
            }
        }

        String audioUrl = (String) listData.get(position).get("audio_url");
        String videoUrl = (String) listData.get(position).get("video_url");

        if(audioUrl==null || audioUrl.equals("") || audioUrl.equals("null")){
            holder.typeImg.setVisibility(View.GONE);


            if (videoUrl == null || videoUrl.equals("null") || videoUrl.equals("")) {
                holder.typeImg.setVisibility(View.GONE);
            } else {
                holder.typeImg.setVisibility(View.VISIBLE);
                holder.typeImg.setImageResource(R.drawable.ic200_video_border_white);
            }


        }else {
            holder.typeImg.setVisibility(View.VISIBLE);
            holder.typeImg.setImageResource(R.drawable.ic200_audio_play_white);
        }





        return convertView;
    }

    class LoadTask extends AsyncTask<Void, Void, Object> {


        private ImageView imageView;
        private int position;
        private Bitmap bitmap;
        private int w, h;

        private Bitmap big(Bitmap bitmap) {
            Matrix matrix = new Matrix();
            matrix.postScale(1.5f,1.5f); //长和宽放大缩小的比例
            return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        }



        public LoadTask( ImageView imageView , int position){
            this.position = position;
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                bitmap = HttpUtility.getNetBitmap((String) listData.get(position).get("image_url_thumbnail"));
                w = bitmap.getWidth();
                h = bitmap.getHeight();
            }catch (Exception e){
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
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

    public class ViewHolder{
        ImageView picImg;
        TextView name;
        RelativeLayout r;
        ImageView typeImg;
        boolean select;


    }

}



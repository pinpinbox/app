package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pinpinbox.android.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by kevin9594 on 2016/6/9.
 */
public class GalleryAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<File> fileList;
    private List<String> photoUseForList;
    private List<String> photoAudioReferList;

    private BitmapFactory.Options opts;

    public GalleryAdapter(Activity mActivity, List<File> fileList, List<String> photoUseForList, List<String> photoAudioReferList) {
        this.mActivity = mActivity;
        this.fileList = fileList;
        this.photoUseForList = photoUseForList;
        this.photoAudioReferList = photoAudioReferList;


        opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_gallery, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
//            holder.audioImg = (ImageView) convertView.findViewById(R.id.audioImg);
//            holder.videoImg = (ImageView) convertView.findViewById(R.id.videoImg);
//            holder.exchangeImg = (ImageView) convertView.findViewById(R.id.exchangeImg);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
//            holder.videoImg.setVisibility(View.GONE);
//            holder.audioImg.setVisibility(View.GONE);
//            holder.exchangeImg.setVisibility(View.GONE);
        }


//        if (photoUseForList != null) {
//
//            if(position < photoUseForList.size()) {
//
//                String usefor = photoUseForList.get(position);
//
//                switch (usefor) {
//                    case "image":
//                        holder.audioImg.setVisibility(View.GONE);
//                        if (photoAudioReferList != null) {
//                            String strTarget = photoAudioReferList.get(position);
//
//                            if (strTarget.equals("none") || strTarget.equals("")) {
//
//                                Log.e("--------------", "strTarget => " + strTarget);
//
//                                holder.audioImg.setVisibility(View.GONE);
//                            } else {
//                                holder.audioImg.setVisibility(View.VISIBLE);
//                            }
//                        }
//                        break;
//                    case "video":
//                        holder.videoImg.setImageResource(R.drawable.button_read_album_videoplay);
//                        holder.videoImg.setVisibility(View.VISIBLE);
//                        break;
//                    case "exchange":
//                        holder.exchangeImg.setVisibility(View.VISIBLE);
//                        break;
//                    case "slot":
//                        holder.videoImg.setImageResource(R.drawable.gift_00000);
//                        holder.videoImg.setVisibility(View.VISIBLE);
//                        break;
//
//                }
//
//            }
//
//
//        }


        File file = fileList.get(position);


        Picasso.with(mActivity.getApplicationContext())
                .load(file)
                .config(Bitmap.Config.RGB_565)
                .fit()
                .centerCrop()
                .tag(mActivity.getApplicationContext())
                .into(holder.img);





//        if (file != null && file.exists()) {
//            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
//        }
//        int w = opts.outWidth;
//        int h = opts.outHeight;
//
//        int wh = w * h;
//
//        setPicture(holder.img, file, w, h, wh);

        return convertView;
    }

    class ViewHolder {

        ImageView img;
        ImageView audioImg;
        ImageView videoImg;
        ImageView exchangeImg;

    }

    private void setPicture(ImageView img, File file, int w, int h, int wh) {

        if (wh > 4000000) {


            Picasso.with(mActivity.getApplicationContext())
                    .load(file)
                    .config(Bitmap.Config.RGB_565)
                    .resize(w / 15, h / 15)
                    .centerCrop()
                    .tag(mActivity.getApplicationContext())
                    .into(img);



        } else if (wh < 4000000 && wh > 500000) {


            Picasso.with(mActivity.getApplicationContext())
                    .load(file)
                    .config(Bitmap.Config.RGB_565)
                    .resize(w / 12, h / 12)
                    .centerCrop()
                    .tag(mActivity.getApplicationContext())
                    .into(img);

        } else if (wh < 500000 && wh > 250000) {


            Picasso.with(mActivity.getApplicationContext())
                    .load(file)
                    .config(Bitmap.Config.RGB_565)
                    .resize(w / 9, h / 9)
                    .centerCrop()
                    .tag(mActivity.getApplicationContext())
                    .into(img);

        } else if (wh < 250000 && wh > 0) {

            try {
                Picasso.with(mActivity.getApplicationContext())
                        .load(file)
                        .config(Bitmap.Config.RGB_565)
                        .resize(w / 6, h / 6)
                        .centerCrop()
                        .tag(mActivity.getApplicationContext())
                        .into(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}

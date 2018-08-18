package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by kevin9594 on 2017/4/9.
 */
public class RecyclerCreationAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Activity mActivity;

    private ArrayList<HashMap<String, Object>> listData;

    private String identity;


    public RecyclerCreationAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData, String identity) {
        this.mActivity = activity;
        this.listData = listData;
        this.identity = identity;
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
//        final int cacheSize = maxMemory / 4;

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_creation, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;


        String url = (String) listData.get(position).get("image_url_thumbnail");

        if(ImageUtility.isImageExist(url)){

            Picasso.with(mActivity.getApplicationContext())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.picImg);

        }else {

            holder.picImg.setImageResource(R.drawable.bg_2_0_0_no_image);

        }


        if(position ==0){
            holder.name.setText(R.string.pinpinbox_2_0_0_other_text_creation_cover);
        }else {
            holder.name.setText(StringIntMethod.IntToString(position));
        }

        boolean isSelect = (boolean) listData.get(position).get("select");
        if (isSelect) {
            holder.rBorder.setVisibility(View.VISIBLE);
        } else {
            holder.rBorder.setVisibility(View.INVISIBLE);
        }

        String audioUrl = (String) listData.get(position).get("audio_url");
        String videoUrl = (String) listData.get(position).get("video_url");

        if(audioUrl==null || audioUrl.equals("") || audioUrl.equals("null")){
            holder.typeImg.setVisibility(View.GONE);


            if (videoUrl == null || videoUrl.equals("null") || videoUrl.equals("")) {
                holder.typeImg.setVisibility(View.GONE);
            } else {
                holder.typeImg.setVisibility(View.VISIBLE);
                holder.typeImg.setImageResource(R.drawable.ic200_video_white);
            }


        }else {
            holder.typeImg.setVisibility(View.VISIBLE);
            holder.typeImg.setImageResource(R.drawable.ic200_audio_play_white);
        }


        //20171018
        if(identity.equals("admin")){
            holder.rLockOverlay.setVisibility(View.GONE);
        }else {

            String user_id = (String)listData.get(position).get(Key.user_id);
            String myId = PPBApplication.getInstance().getId();
            if(!user_id.equals(myId)){
                holder.rLockOverlay.setVisibility(View.VISIBLE);
            }else {
                holder.rLockOverlay.setVisibility(View.GONE);
            }

        }





    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RelativeLayout rBorder, rLockOverlay;
        private ImageView picImg, typeImg;
        private TextView name;

        public ViewHolder(View itemView) {
            super(itemView);


            typeImg = (ImageView)itemView.findViewById(R.id.typeImg);
            picImg = (ImageView)itemView.findViewById(R.id.picImg);
            name = (TextView)itemView.findViewById(R.id.name);
            rBorder = (RelativeLayout)itemView.findViewById(R.id.rBorder);
            rLockOverlay = (RelativeLayout)itemView.findViewById(R.id.rLockOverlay);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position, v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != onRecyclerViewListener) {
                return onRecyclerViewListener.onItemLongClick(position, v);
            }
            return false;
        }
    }




}

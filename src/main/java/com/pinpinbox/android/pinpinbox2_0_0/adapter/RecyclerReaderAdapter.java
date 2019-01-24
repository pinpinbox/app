package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemPhoto;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin9594 on 2017/2/26.
 */
public class RecyclerReaderAdapter extends RecyclerView.Adapter {


    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    private Activity mActivity;

    private List<ItemPhoto> itemPhotoList;


    public RecyclerReaderAdapter(Activity activity, List<ItemPhoto> itemPhotoList) {
        this.mActivity = activity;
        this.itemPhotoList = itemPhotoList;
    }


    @Override
    public int getItemCount() {
        return itemPhotoList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_reader, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        String url = itemPhotoList.get(position).getImage_url_thumbnail();

        if (url == null || url.equals("") || url.equals("null")) {
            holder.photoImg.setImageResource(R.drawable.bg_2_0_0_no_image);
        } else {

            if(url.equals("lastPage")){
//                holder.photoImg.setBackgroundColor(Color.parseColor(ColorClass.BLACK));


                Picasso.with(mActivity.getApplicationContext())
                        .load(R.drawable.bg200_preview_small)
                        .config(Bitmap.Config.RGB_565)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.photoImg);


            }else {
                Picasso.with(mActivity.getApplicationContext())
                        .load(url)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.bg_2_0_0_no_image)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.photoImg);
            }


        }

        String usrfor = itemPhotoList.get(position).getUsefor();

        switch (usrfor) {
            case "image":
                holder.useforImg.setVisibility(View.GONE);
                break;

            case "video":
                holder.useforImg.setVisibility(View.VISIBLE);
                holder.useforImg.setImageResource(R.drawable.ic200_video_white);
                break;

            case "audio":
                holder.useforImg.setVisibility(View.VISIBLE);
                holder.useforImg.setImageResource(R.drawable.ic200_audio_play_white);
                break;

            case "exchange":
                holder.useforImg.setVisibility(View.VISIBLE);
                holder.useforImg.setImageResource(R.drawable.ic200_gift_white);
                break;
            case "slot":
                holder.useforImg.setVisibility(View.VISIBLE);
                holder.useforImg.setImageResource(R.drawable.ic200_gift_white);
                break;

            case "lastPage":
                holder.useforImg.setVisibility(View.GONE);

                break;

        }


        boolean isSelect = itemPhotoList.get(position).isSelect();

        if (isSelect) {
            holder.rBackground.setBackgroundColor(Color.parseColor(ColorClass.MAIN_FIRST));
            holder.vDark.setVisibility(View.GONE);

        } else {
            holder.rBackground.setBackgroundColor(Color.parseColor(ColorClass.TRANSPARENT));
            holder.vDark.setVisibility(View.VISIBLE);

        }

    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RelativeLayout rBackground;
        private ImageView photoImg;
        private ImageView useforImg;
        private View vDark;



        public ViewHolder(View itemView) {
            super(itemView);

            rBackground = itemView.findViewById(R.id.rBackground);
            photoImg = itemView.findViewById(R.id.photoImg);
            useforImg = itemView.findViewById(R.id.useforImg);
            vDark = itemView.findViewById(R.id.vDark);


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


    public void addData(int position, ItemPhoto itemPhoto) {
        itemPhotoList.add(position, itemPhoto);
        notifyItemInserted(position);
    }

    public void setData(int position, ItemPhoto itemPhoto) {
        itemPhotoList.set(position, itemPhoto);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        itemPhotoList.remove(position);
        notifyItemRemoved(position);
    }

}

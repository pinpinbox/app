package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2017/1/18.
 */
public class RecyclerTemListAdapter extends RecyclerView.Adapter {

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

    private boolean isOwn = false;


    public RecyclerTemListAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData, boolean isOwn) {
        this.mActivity = activity;
        this.listData = listData;
        this.isOwn = isOwn;


    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         View view = null;

        if(isOwn){
             view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_temlistown, null);
            return new ViewHolderOwn(view);
        }else {
             view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_temlist, null);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        if(isOwn){

            ViewHolderOwn holder = (ViewHolderOwn) vHolder;
            holder.position = position;

            String strCover = (String)listData.get(position).get(Key.image);
            String strTemName = (String)listData.get(position).get(Key.name);

            Picasso.with(mActivity.getApplicationContext())
                    .load(strCover)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.coverImg);

            holder.tvTemName.setText(strTemName);



        }else {

            ViewHolder holder = (ViewHolder) vHolder;
            holder.position = position;

            String strCover = (String)listData.get(position).get(Key.image);
            String strTemName = (String)listData.get(position).get(Key.name);
            String strDownloadCount = (String)listData.get(position).get(Key.count);
            boolean isOwn = (boolean)listData.get(position).get(Key.own);


            Picasso.with(mActivity.getApplicationContext())
                    .load(strCover)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.coverImg);

            TextPaint tp = holder.tvTemName.getPaint();
            tp.setFakeBoldText(true);
            holder.tvTemName.setText(strTemName);


            TextPaint tp2 = holder.tvOwn.getPaint();
            tp2.setFakeBoldText(true);

            holder.tvDownloadCount.setText(strDownloadCount + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_used_count_by_template));

            if(isOwn){
                holder.rOwn.setVisibility(View.VISIBLE);
            }else {
                holder.rOwn.setVisibility(View.GONE);
            }



        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private TextView tvTemName, tvDownloadCount, tvOwn;
        private RoundedImageView coverImg;
        private RelativeLayout rOwn;


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            tvTemName = (TextView)itemView.findViewById(R.id.tvTemName);
            tvDownloadCount = (TextView)itemView.findViewById(R.id.tvDownloadCount);
            tvOwn = (TextView)itemView.findViewById(R.id.tvOwn);
            coverImg = (RoundedImageView)itemView.findViewById(R.id.coverImg);
            rOwn = (RelativeLayout)itemView.findViewById(R.id.rOwn);

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



    private class ViewHolderOwn extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private TextView tvTemName;
        private RoundedImageView coverImg;


        public ViewHolderOwn(View itemView) {
            super(itemView);

            itemView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);
            tvTemName = (TextView)itemView.findViewById(R.id.tvTemName);
            coverImg = (RoundedImageView)itemView.findViewById(R.id.coverImg);
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

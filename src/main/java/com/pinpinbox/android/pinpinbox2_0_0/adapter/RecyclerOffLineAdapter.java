package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by vmage on 2017/5/23.
 */
public class RecyclerOffLineAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Activity mActivity;

    private List<ItemAlbum> itemAlbumList;


    public RecyclerOffLineAdapter(Activity activity, List<ItemAlbum> itemAlbumList) {
        this.mActivity = activity;
        this.itemAlbumList = itemAlbumList;


    }


    @Override
    public int getItemCount() {
        return itemAlbumList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_recent, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        holder.tvAlbumName.setText(itemAlbumList.get(position).getName());
        holder.tvUserName.setText(itemAlbumList.get(position).getUser_name());


        String cover  = itemAlbumList.get(position).getCover();
        if(cover!=null&& !cover.equals("") && !cover.equals("null")) {
            Picasso.with(mActivity.getApplicationContext())
                    .load(new File(cover))
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.coverImg);
        }else {
            holder.coverImg.setImageResource(R.drawable.bg_2_0_0_no_image);
        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RelativeLayout rBackground;
        private RoundedImageView coverImg;
        private TextView tvAlbumName, tvUserName;


        public ViewHolder(View itemView) {
            super(itemView);

            rBackground = (RelativeLayout)itemView.findViewById(R.id.rBackground);
            coverImg = (RoundedImageView)itemView.findViewById(R.id.coverImg);
            tvAlbumName = (TextView)itemView.findViewById(R.id.tvAlbumName);
            tvUserName = (TextView)itemView.findViewById(R.id.tvUserName);

            rBackground.setOnClickListener(this);
            rBackground.setOnLongClickListener(this);

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

package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.TransformationControl;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin9594 on 2017/4/15.
 */
public class RecyclerRecentAdapter extends RecyclerView.Adapter {

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


    public RecyclerRecentAdapter(Activity activity, List<ItemAlbum> itemAlbumList) {
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


        String strCover = itemAlbumList.get(position).getCover();
        ImageUtility.setImage(mActivity, holder.detaillistImg, strCover);


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RelativeLayout rBackground;
        private RoundCornerImageView detaillistImg;
        private TextView tvAlbumName, tvUserName;


        public ViewHolder(View itemView) {
            super(itemView);

            rBackground = (RelativeLayout) itemView.findViewById(R.id.rBackground);
            detaillistImg = (RoundCornerImageView) itemView.findViewById(R.id.detaillistImg);
            tvAlbumName = (TextView) itemView.findViewById(R.id.tvAlbumName);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);

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

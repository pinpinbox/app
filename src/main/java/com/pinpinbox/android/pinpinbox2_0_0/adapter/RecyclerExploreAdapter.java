package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

import java.util.List;

/**
 * Created by vmage on 2017/11/21.
 */

public class RecyclerExploreAdapter extends RecyclerView.Adapter {


    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private RecyclerExploreAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(RecyclerExploreAdapter.OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Activity mActivity;

    private List<ItemAlbum> albumList;

    private int fixHeight;


    public RecyclerExploreAdapter(Activity activity, List<ItemAlbum> itemAlbumList) {
        this.mActivity = activity;
        this.albumList = itemAlbumList;

        fixHeight = SizeUtils.dp2px(160);

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_explore, null);
        return new RecyclerExploreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {


        final RecyclerExploreAdapter.ViewHolder holder = (RecyclerExploreAdapter.ViewHolder) vHolder;
        holder.position = position;


//        int h = albumList.get(position).getCover_height();
//        holder.coverImg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));


        int w = albumList.get(position).getCover_width();
        int h = albumList.get(position).getCover_height();


        if (albumList.get(position).getImage_orientation() == ItemAlbum.LANDSCAPE){

            holder.coverImg.setLayoutParams(new RelativeLayout.LayoutParams((fixHeight*w)/h, ViewGroup.LayoutParams.MATCH_PARENT));

        }else {

            holder.coverImg.setLayoutParams(new RelativeLayout.LayoutParams(SizeUtils.dp2px(128), ViewGroup.LayoutParams.MATCH_PARENT));

        }


            holder.coverImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String color = albumList.get(position).getCover_hex();
        final GradientDrawable drawable = (GradientDrawable) holder.rItemBg.getBackground();
        if (color != null && !color.equals("")) {
            drawable.setColor(Color.parseColor(color));
        } else {
            drawable.setColor(Color.parseColor(ColorClass.GREY_SECOND));
        }


        /*set album name*/
        try {

            TextUtility.setBold(holder.tvAlbumName, true);

            String strAlbumName = albumList.get(position).getName();

            holder.tvAlbumName.setText(strAlbumName);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*set cover*/
        try {
            final String strCover = albumList.get(position).getCover();

            if (SystemUtility.Above_Equal_V5()) {
                holder.coverImg.setTransitionName(strCover);
            }

            ImageUtility.setImage(mActivity, holder.coverImg, strCover);

        } catch (Exception e) {
            e.printStackTrace();
        }


        /*set usefor*/
        if (albumList.get(position).isVideo() || albumList.get(position).isSlot() || albumList.get(position).isExchange() || albumList.get(position).isAudio()) {
            holder.linType.setVisibility(View.VISIBLE);

            if (albumList.get(position).isAudio()) {
                holder.audioImg.setVisibility(View.VISIBLE);
            } else {
                holder.audioImg.setVisibility(View.GONE);
            }
            if (albumList.get(position).isVideo()) {
                holder.videoImg.setVisibility(View.VISIBLE);
            } else {
                holder.videoImg.setVisibility(View.GONE);
            }
            if (albumList.get(position).isSlot()) {
                holder.slotImg.setVisibility(View.VISIBLE);
            } else {

                if (albumList.get(position).isExchange()) {
                    holder.slotImg.setVisibility(View.VISIBLE);
                } else {
                    holder.slotImg.setVisibility(View.GONE);
                }

            }

        } else {
            holder.linType.setVisibility(View.GONE);
        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;


        private ImageView audioImg, videoImg, slotImg;
        private TextView tvAlbumName;
        private LinearLayout linType;
        private RoundCornerImageView coverImg;
        private RelativeLayout rItemBg, rClickArea;


        public ViewHolder(final View itemView) {
            super(itemView);

            coverImg = itemView.findViewById(R.id.coverImg);

            audioImg = itemView.findViewById(R.id.audioImg);
            videoImg = itemView.findViewById(R.id.videoImg);
            slotImg = itemView.findViewById(R.id.slotImg);


            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);


            linType = itemView.findViewById(R.id.linType);
            rItemBg = itemView.findViewById(R.id.rItemBg);
            rClickArea = itemView.findViewById(R.id.rClickArea);

            rClickArea.setOnClickListener(this);
            rClickArea.setOnLongClickListener(this);

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


    public void addData(int position, ItemAlbum itemAlbum) {
        albumList.add(position, itemAlbum);
        notifyItemInserted(position);
    }

    public void setData(int position, ItemAlbum itemAlbum) {
        albumList.set(position, itemAlbum);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        albumList.remove(position);
        notifyItemRemoved(position);
    }

}

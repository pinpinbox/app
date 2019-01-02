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

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

import java.util.ArrayList;

/**
 * Created by vmage on 2017/2/2.
 */
public class RecyclerSearchAlbumAdapter extends RecyclerView.Adapter {


    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    private Activity mActivity;

//    private ArrayList<HashMap<String, Object>> listData;
private ArrayList<ItemAlbum> albumList;

    public RecyclerSearchAlbumAdapter(Activity activity,  ArrayList<ItemAlbum> albumList) {
        this.mActivity = activity;
        this.albumList = albumList;


    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_search_album, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        /*set cover*/
        int h = albumList.get(position).getCover_height();
        holder.coverImg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
        holder.coverImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String color = albumList.get(position).getCover_hex();
        final GradientDrawable drawable = (GradientDrawable)holder.rItemBg.getBackground();
        if(color!=null && !color.equals("")){
            drawable.setColor(Color.parseColor(color));
        }else {
            drawable.setColor(Color.parseColor(ColorClass.GREY_SECOND));
        }


        if(SystemUtility.Above_Equal_V5()) {
            holder.coverImg.setTransitionName(albumList.get(position).getCover());
        }

        ImageUtility.setImage(mActivity, holder.coverImg, albumList.get(position).getCover());

        /*set album name*/
        TextUtility.setBold(holder.tvAlbumName);
        holder.tvAlbumName.setText(albumList.get(position).getName());

        /*set user name*/
//        holder.tvUserName.setText(albumList.get(position).getUser_name());


        if(albumList.get(position).isVideo() || albumList.get(position).isSlot() || albumList.get(position).isExchange() ||  albumList.get(position).isAudio()){
            holder.linType.setVisibility(View.VISIBLE);

            if(albumList.get(position).isAudio()) {holder.audioImg.setVisibility(View.VISIBLE);}else {holder.audioImg.setVisibility(View.GONE);}
            if(albumList.get(position).isVideo()){holder.videoImg.setVisibility(View.VISIBLE);}else {holder.videoImg.setVisibility(View.GONE);}
            if(albumList.get(position).isSlot()){holder.slotImg.setVisibility(View.VISIBLE);}else {holder.slotImg.setVisibility(View.GONE);}
            if(albumList.get(position).isExchange()){holder.slotImg.setVisibility(View.VISIBLE);}else{holder.slotImg.setVisibility(View.GONE);}//歸類於slot
        }else {
            holder.linType.setVisibility(View.GONE);
        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;


        private TextView tvAlbumName, tvUserName;
        private LinearLayout linType;
        private ImageView audioImg, videoImg, slotImg;
        private RoundCornerImageView coverImg;
        private RelativeLayout rItemBg;

        public ViewHolder(View itemView) {
            super(itemView);


            itemView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            rItemBg = (RelativeLayout) itemView.findViewById(R.id.rItemBg);
            coverImg = (RoundCornerImageView) itemView.findViewById(R.id.coverImg);
            tvAlbumName = (TextView) itemView.findViewById(R.id.tvAlbumName);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);

            linType = (LinearLayout)itemView.findViewById(R.id.linType);
            audioImg = (ImageView)itemView.findViewById(R.id.audioImg);
            videoImg = (ImageView)itemView.findViewById(R.id.videoImg);
            slotImg = (ImageView)itemView.findViewById(R.id.slotImg);

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


    public void addData(int position, ItemAlbum itemAlbum) {
        albumList.add(position, itemAlbum);
        notifyItemInserted(position);
    }

    public void setData(int position,  ItemAlbum itemAlbum) {
        albumList.set(position, itemAlbum);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        albumList.remove(position);
        notifyItemRemoved(position);
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
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

import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin9594 on 2016/12/18.
 */
public class RecyclerAuthorAdapter extends RecyclerView.Adapter {


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

    private List<ItemAlbum> albumList;

    public RecyclerAuthorAdapter(Activity activity, List<ItemAlbum> albumList) {
        this.mActivity = activity;
        this.albumList = albumList;

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_author, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {


        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        /*set album name*/
        try {
            holder.tvAlbumName.setText(albumList.get(position).getName());
            TextUtility.setBold(holder.tvAlbumName, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int h = albumList.get(position).getCover_height();
        holder.coverImg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
        holder.coverImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String color = albumList.get(position).getCover_hex();
        final GradientDrawable drawable = (GradientDrawable) holder.rItemBg.getBackground();
        if (color != null && !color.equals("")) {
            drawable.setColor(Color.parseColor(color));
        } else {
            drawable.setColor(Color.parseColor(ColorClass.GREY_SECOND));
        }


        if (SystemUtility.Above_Equal_V5()) {
            holder.coverImg.setTransitionName(albumList.get(position).getCover());
        }

        if (!albumList.get(position).getCover().equals("")) {
            Picasso.with(mActivity.getApplicationContext())
                    .load(albumList.get(position).getCover())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.coverImg, new Callback() {
                        @Override
                        public void onSuccess() {

                            drawable.setColor(Color.parseColor(ColorClass.GREY_SECOND));

                            holder.coverImg.setAlpha(0.8f);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            holder.coverImg.setImageResource(R.drawable.bg_2_0_0_no_image);
        }

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

                if(albumList.get(position).isExchange()){
                    holder.slotImg.setVisibility(View.VISIBLE);
                }else {
                    holder.slotImg.setVisibility(View.GONE);
                }

            }
//            if (albumList.get(position).isExchange()) {
//                holder.slotImg.setVisibility(View.VISIBLE);
//            } else {
//                holder.slotImg.setVisibility(View.GONE);
//            }//歸類於slot
        } else {
            holder.linType.setVisibility(View.GONE);
        }

    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;


        private TextView tvAlbumName;
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
            linType = (LinearLayout) itemView.findViewById(R.id.linType);
            audioImg = (ImageView) itemView.findViewById(R.id.audioImg);
            videoImg = (ImageView) itemView.findViewById(R.id.videoImg);
            slotImg = (ImageView) itemView.findViewById(R.id.slotImg);

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

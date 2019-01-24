package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;

import java.util.List;

public class RecyclerSelectAlbumAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    private OnRecyclerViewListener onRecyclerViewListener;

    private Activity mActivity;

    private List<ItemAlbum> itemAlbumList;

    public RecyclerSelectAlbumAdapter(Activity mActivity, List<ItemAlbum> itemAlbumList) {
        this.mActivity = mActivity;
        this.itemAlbumList = itemAlbumList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_select_album, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.position = position;

        mHolder.tvAlbumName.setText(itemAlbumList.get(position).getName());


        if (itemAlbumList.get(position).getIdentity().equals(Key.viewer)) {

            mHolder.tvCount.setTextColor(Color.parseColor(ColorClass.PINK_FRIST));
            mHolder.tvCount.setText(R.string.pinpinbox_2_0_0_toast_message_deficiency_identity);

        } else {

            mHolder.tvCount.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
            mHolder.tvCount.setText( itemAlbumList.get(position).getCount_photo() + "/" + itemAlbumList.get(position).getPhoto_limit_of_album());

        }





        ImageUtility.setImage(mActivity, mHolder.detaillistImg, itemAlbumList.get(position).getCover());

        if (itemAlbumList.get(position).isSelect()) {
            mHolder.selectAlbumImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
        } else {
            mHolder.selectAlbumImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position, List payloads) {


        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {

            ViewHolder mHolder = (ViewHolder) holder;
            mHolder.position = position;

            if (itemAlbumList.get(position).isSelect()) {
                mHolder.selectAlbumImg.setImageResource(R.drawable.border_2_0_0_click_default_radius);
            } else {
                mHolder.selectAlbumImg.setImageResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
            }
        }


    }

    @Override
    public int getItemCount() {
        return itemAlbumList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private int position;
        private LinearLayout linBackground;
        private ImageView selectAlbumImg;
        private RoundCornerImageView detaillistImg;
        private TextView tvAlbumName, tvCount;


        public ViewHolder(View itemView) {
            super(itemView);

            linBackground = itemView.findViewById(R.id.linBackground);
            selectAlbumImg = itemView.findViewById(R.id.selectAlbumImg);
            detaillistImg = itemView.findViewById(R.id.detaillistImg);
            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvCount = itemView.findViewById(R.id.tvCount);

            linBackground.setOnClickListener(this);

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

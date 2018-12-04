package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;

import java.util.List;

public class RecyclerFeatureAdapter extends RecyclerView.Adapter {


    private OnRecyclerViewListener onRecyclerViewListener;

    private Activity mActivity;

    private List<ItemUser> itemUserList;

    public RecyclerFeatureAdapter(Activity activity, List<ItemUser> itemUserList) {
        this.mActivity = activity;
        this.itemUserList = itemUserList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_home_feature, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, int position) {

        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;


        holder.coverImg.setImageResource(R.drawable.test_banner_960x450_1);

        holder.userImg.setImageResource(R.drawable.test_dog);

        holder.tvFeatureUserDescription.setText("我喜歡吃吃喝喝，旅遊踏青，喜歡文藝創作、插畫、兒童繪本。作品是用photoshop畫的呦!");


    }

    @Override
    public int getItemCount() {
        return itemUserList.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private LinearLayout linClickArea;
        private TextView tvFeatureUserDescription;
        private RoundCornerImageView coverImg, userImg;


        public ViewHolder(final View itemView) {
            super(itemView);

            linClickArea = itemView.findViewById(R.id.linClickArea);
            tvFeatureUserDescription = itemView.findViewById(R.id.tvFeatureUserDescription);
            coverImg = itemView.findViewById(R.id.coverImg);
            userImg = itemView.findViewById(R.id.userImg);


            linClickArea.setOnClickListener(this);
            linClickArea.setOnLongClickListener(this);

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


    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


}

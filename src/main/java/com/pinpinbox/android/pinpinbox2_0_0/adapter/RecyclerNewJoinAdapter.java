package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2018/6/21.
 */

public class RecyclerNewJoinAdapter extends RecyclerView.Adapter {


    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    private Activity mActivity;

    private List<ItemUser> itemUserList;


    public RecyclerNewJoinAdapter(Activity activity, List<ItemUser> itemUserList) {
        this.mActivity = activity;
        this.itemUserList = itemUserList;
    }


    @Override
    public int getItemCount() {
        return itemUserList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_home_newjoin, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        String strPicture = itemUserList.get(position).getPicture();
        String strName = itemUserList.get(position).getName();

        holder.tvName.setText(strName);

        if (SystemUtility.Above_Equal_V5()) {
            holder.userImg.setTransitionName(strPicture);
        }

        if (strPicture == null || strPicture.equals("")) {
            holder.userImg.setImageResource(R.drawable.member_back_head);
        } else {
            Picasso.get()
                    .load(strPicture)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.userImg);
        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private LinearLayout linBackground;
        private RoundCornerImageView userImg;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);


            linBackground = itemView.findViewById(R.id.linBackground);

            linBackground.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            userImg = itemView.findViewById(R.id.userImg);
            tvName = itemView.findViewById(R.id.tvName);

            linBackground.setOnClickListener(this);
            linBackground.setOnLongClickListener(this);

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


    public void addData(int position, ItemUser itemUser) {
        itemUserList.add(position, itemUser);
        notifyItemInserted(position);
    }

    public void setData(int position, ItemUser itemUser) {
        itemUserList.set(position, itemUser);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        itemUserList.remove(position);
        notifyItemRemoved(position);
    }

}

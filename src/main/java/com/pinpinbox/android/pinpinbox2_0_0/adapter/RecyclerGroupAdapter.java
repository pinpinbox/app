package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.pinpinbox2_0_0.listener.GroupSetListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2017/3/31.
 */
public class RecyclerGroupAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private GroupSetListener groupSetListener;

    public void setGroupSetListener(GroupSetListener groupSetListener) {
        this.groupSetListener = groupSetListener;
    }


    private Activity mActivity;

    private List<ItemUser> itemUserList;


    public RecyclerGroupAdapter(Activity activity, List<ItemUser> itemUserList) {
        this.mActivity = activity;
        this.itemUserList = itemUserList;
    }

    public List<ItemUser> getItemUserList() {
        return itemUserList;
    }

    @Override
    public int getItemCount() {
        return itemUserList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_cooperation_group, null);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        holder.tvName.setText(itemUserList.get(position).getName());

        String url = itemUserList.get(position).getPicture();

        if (url != null && !url.equals("")) {
            Picasso.with(mActivity.getApplicationContext())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.userImg);
        } else {
            holder.userImg.setImageResource(R.drawable.member_back_head);
        }


        if (position != 0) {

            String identity = itemUserList.get(position).getIdentity();
            switch (identity) {
                case "approver":
                    holder.tvIdentity.setText(R.string.pinpinbox_2_0_0_button_approver);
                    break;

                case "editor":
                    holder.tvIdentity.setText(R.string.pinpinbox_2_0_0_button_cooperation);
                    break;

                case "viewer":
                    holder.tvIdentity.setText(R.string.pinpinbox_2_0_0_button_browser);
                    break;
            }

            holder.deleteImg.setVisibility(View.VISIBLE);
            holder.tvIdentity.setBackgroundResource(R.drawable.click_2_0_0_grey_third_radius);
            holder.tvIdentity.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
            holder.tvIdentity.setClickable(true);
            holder.tvIdentity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ClickUtils.ButtonContinuousClick()) {
                        return;
                    }
                    groupSetListener.changeType(holder.position);
                }
            });

            holder.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ClickUtils.ButtonContinuousClick()) {
                        return;
                    }
                    groupSetListener.delete(holder.position);
                }
            });

        } else {
            holder.deleteImg.setVisibility(View.GONE);
            holder.tvIdentity.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_second_radius);
            holder.tvIdentity.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
            holder.tvIdentity.setText(R.string.pinpinbox_2_0_0_button_admin);
            holder.tvIdentity.setClickable(false);

        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RelativeLayout rBackground;
        private RoundedImageView userImg;
        private TextView tvName, tvIdentity;
        private ImageView deleteImg;

        public ViewHolder(View itemView) {
            super(itemView);


            rBackground = (RelativeLayout) itemView.findViewById(R.id.rBackground);

            rBackground.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            userImg = (RoundedImageView) itemView.findViewById(R.id.userImg);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvIdentity = (TextView) itemView.findViewById(R.id.tvIdentity);
            deleteImg = (ImageView) itemView.findViewById(R.id.deleteImg);

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               notifyDataSetChanged();
            }
        },400);

    }

    public void setData(int position, ItemUser itemUser) {
        itemUserList.set(position, itemUser);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        itemUserList.remove(position);
        notifyItemRemoved(position);
        if (position != itemUserList.size()) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, itemUserList.size() - position);
        }

    }


}

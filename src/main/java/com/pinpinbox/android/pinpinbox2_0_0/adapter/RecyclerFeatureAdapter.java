package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.squareup.picasso.Picasso;

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

        /*set cover*/
        try {
            final String strCover = itemUserList.get(position).getCover();
            if (SystemUtility.Above_Equal_V5()) {
                holder.coverImg.setTransitionName(strCover);
            }
            ImageUtility.setImage(mActivity, holder.coverImg, strCover);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*set user picture*/
        final String strPicture = itemUserList.get(position).getPicture();
        try {
            if (SystemUtility.Above_Equal_V5()) {
                holder.userImg.setTransitionName(strPicture);
            }
            if (strPicture != null && !strPicture.equals("") && !strPicture.equals("null")) {
                Picasso.with(mActivity.getApplicationContext())
                        .load(strPicture)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.member_back_head)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.userImg);
            } else {
                holder.userImg.setImageResource(R.drawable.member_back_head);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*set user description*/
        holder.tvFeatureUserDescription.setText(itemUserList.get(position).getDescription());


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

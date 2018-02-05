package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ColorClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.libs.TouchRange.TouchRange;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2018/1/2.
 */

public class RecyclerSponsorAdapter extends RecyclerView.Adapter {

    private Activity mActivity;

    private List<ItemUser> itemUserList;

    private RecyclerSponsorAdapter.OnRecyclerViewListener onRecyclerViewListener;
    private RecyclerSponsorAdapter.OnUserInterativeListener onUserInterativeListener;

    public RecyclerSponsorAdapter(Activity activity, List<ItemUser> itemUserList) {
        this.mActivity = activity;
        this.itemUserList = itemUserList;
    }


    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    public void setOnRecyclerViewListener(RecyclerSponsorAdapter.OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    public interface OnUserInterativeListener {

        void doFollow(int position);

        void doPostMessage(int position);
    }

    public void setOnUserInterativeListener(RecyclerSponsorAdapter.OnUserInterativeListener onUserInterativeListener) {
        this.onUserInterativeListener = onUserInterativeListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_sponsor, null);
        return new RecyclerSponsorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder mHolder = (ViewHolder) holder;
        mHolder.position = position;

        TouchRange.let( mHolder.tvFollow,  mHolder.tvMessage)
                // easy to use, like css padding
                .bounds()//default value is 8dp
//                .bounds(24.0f) //left,top,right,bottom=24dp
//                .bounds(24.0f,30.0f) //left,right=24dp top,bottom=30dp
//                .bounds(24.0f,30.0f,24.0f) //left=24dp,top=30dp,right=24dp,bottom=0dp
//                .bounds(24.0f, 30.0f, 24.0f, 30.0f) //left=24dp,top=30dp,right=24dp,bottom=30dp
                .change();



        String strPicture = itemUserList.get(position).getPicture();
        String strName = itemUserList.get(position).getName();
        int point = itemUserList.get(position).getPoint();
        boolean is_follow = itemUserList.get(position).isFollow();

        mHolder.tvName.setText(strName);

        if(point>0){
            mHolder.tvPoint.setVisibility(View.VISIBLE);
            mHolder.tvPtext.setVisibility(View.VISIBLE);
            mHolder.tvPoint.setText(point + "");
        }else {
            mHolder.tvPoint.setVisibility(View.GONE);
            mHolder.tvPtext.setVisibility(View.GONE);
        }



        if (SystemUtility.Above_Equal_V5()) {
            mHolder.userImg.setTransitionName(strPicture);
        }

        if (strPicture == null || strPicture.equals("")) {
            mHolder.userImg.setImageResource(R.drawable.member_back_head);
        } else {
            Picasso.with(mActivity.getApplicationContext())
                    .load(strPicture)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(mActivity.getApplicationContext())
                    .into(mHolder.userImg);
        }


        mHolder.tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (onUserInterativeListener != null) {
                    onUserInterativeListener.doPostMessage(position);
                }
            }
        });

        mHolder.tvFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }

                if (onUserInterativeListener != null) {
                    onUserInterativeListener.doFollow(position);
                }

            }
        });

        if(is_follow){

            mHolder.tvFollow.setBackgroundResource(R.drawable.click_2_0_0_second_grey_button_frame_white_radius);
            mHolder.tvFollow.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
            mHolder.tvFollow.setText(R.string.pinpinbox_2_0_0_button_follow_cancel);

        }else {

            mHolder.tvFollow.setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);
            mHolder.tvFollow.setTextColor(Color.parseColor(ColorClass.WHITE)); //pinpinbox_2_0_0_first_pink
            mHolder.tvFollow.setText(R.string.pinpinbox_2_0_0_button_follow);

        }


    }

    @Override
    public int getItemCount() {
        return itemUserList.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;
        RelativeLayout rBackground;
        private RoundCornerImageView userImg;
        private TextView tvName, tvPoint;
        private TextView tvFollow, tvMessage, tvPtext;

        public ViewHolder(View itemView) {
            super(itemView);

            rBackground = (RelativeLayout) itemView.findViewById(R.id.rBackground);
            userImg = (RoundCornerImageView) itemView.findViewById(R.id.userImg);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPoint = (TextView) itemView.findViewById(R.id.tvPoint);
            tvPtext = (TextView)itemView.findViewById(R.id.tvPtext);


            tvMessage = (TextView)itemView.findViewById(R.id.tvMessage);
            tvFollow = (TextView)itemView.findViewById(R.id.tvFollow);

            rBackground.setOnClickListener(this);

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

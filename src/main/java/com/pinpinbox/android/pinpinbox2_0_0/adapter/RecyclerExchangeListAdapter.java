package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2018/2/1.
 */

public class RecyclerExchangeListAdapter extends RecyclerView.Adapter {


    private Activity mActivity;
    private List<ItemExchange> exchangeList;
    private boolean isShowTime = false;

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public RecyclerExchangeListAdapter(Activity activity, List<ItemExchange> exchangeList, boolean isShowTime) {
        this.mActivity = activity;
        this.exchangeList = exchangeList;
        this.isShowTime = isShowTime;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_exchange, null);
        return new RecyclerExchangeListAdapter.ViewHolder(view);



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder mHolder = (ViewHolder) holder;
        mHolder.position = position;

        int h = exchangeList.get(position).getImageHeight();

        mHolder.exchangeImg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
        mHolder.exchangeImg.setScaleType(ImageView.ScaleType.CENTER_CROP);



        mHolder.tvExchangeName.setText(exchangeList.get(position).getName());
        TextUtility.setBold(mHolder.tvExchangeName,true);


        if(isShowTime){
            mHolder.tvExchangeTime.setVisibility(View.VISIBLE);
            mHolder.tvExchangeTime.setText(exchangeList.get(position).getTime());
            TextUtility.setBold(mHolder.tvExchangeTime,true);
        }else {
            mHolder.tvExchangeTime.setVisibility(View.GONE);
        }


        try{
            final String image = exchangeList.get(position).getImage();

            if (SystemUtility.Above_Equal_V5()) {
                mHolder.exchangeImg.setTransitionName(image);
            }

            if (image != null && !image.equals("") && !image.equals("null")) {

                Picasso.with(mActivity.getApplicationContext())
                        .load(image)
                        .config(Bitmap.Config.RGB_565)
                        .priority(Picasso.Priority.HIGH)
                        .error(R.drawable.bg_2_0_0_no_image)
                        .tag(mActivity.getApplicationContext())
                        .into(mHolder.exchangeImg);

            }else {
                mHolder.exchangeImg.setImageResource(R.drawable.bg_2_0_0_no_image);
            }

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return exchangeList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        public int position;


        private RoundCornerImageView exchangeImg;
        private TextView tvExchangeName, tvExchangeTime;




        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            exchangeImg = (RoundCornerImageView)itemView.findViewById(R.id.exchangeImg);
            tvExchangeName = (TextView)itemView.findViewById(R.id.tvExchangeName);
            tvExchangeTime = (TextView)itemView.findViewById(R.id.tvExchangeTime);


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

package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;

import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemExchange;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;


/**
 * Created by vmage on 2018/2/1.
 */

public class RecyclerExchangeListAdapter extends RecyclerView.Adapter {


    private Activity mActivity;

    private Date curDate;
    private SimpleDateFormat df;

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

        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        curDate = new Date(System.currentTimeMillis());//获取当前时间

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

        mHolder.tvExchangeName.setText(exchangeList.get(position).getName());
        TextUtility.setBold(mHolder.tvExchangeName, true);


        if (isShowTime) {

            if (exchangeList.get(position).getEndtime() == null || exchangeList.get(position).getEndtime().equals("") || exchangeList.get(position).getEndtime().equals("null")) {
                //∞
                mHolder.tvExchangeTime.setText(R.string.pinpinbox_2_0_0_other_text_no_time_limit);
                mHolder.tvExchangeTime.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));


            } else {

                try {
                    Date endDate = df.parse(exchangeList.get(position).getEndtime());

                    long l = endDate.getTime() - curDate.getTime();
                    long day = l / (24 * 60 * 60 * 1000);
                    long hour = (l / (60 * 60 * 1000) - day * 24);
                    long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);

                    //改為直向列表後 再優化英文版
//                    mHolder.tvExchangeTime.setText("剩餘時間:" + day + "天" + hour  + "小時" + min + "分");

                    mHolder.tvExchangeTime.setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_time_limit) + ":"
                            + day + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_day)
                            + hour + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_hour)
                            + min + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_min));


                    mHolder.tvExchangeTime.setTextColor(Color.parseColor(ColorClass.PINK_FRIST));

                    TextUtility.setBold(mHolder.tvExchangeTime, true);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            mHolder.tvExchangeTime.setVisibility(View.VISIBLE);

        } else {
            mHolder.tvExchangeTime.setVisibility(View.GONE);
        }


        try {
            final String image = exchangeList.get(position).getImage();

            if (SystemUtility.Above_Equal_V5()) {
                mHolder.detaillistImg.setTransitionName(image);
            }

            ImageUtility.setImage(mActivity,  mHolder.detaillistImg, image);

        } catch (Exception e) {
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


        private RoundCornerImageView detaillistImg;
        private TextView tvExchangeName, tvExchangeTime;


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setBackgroundResource(R.drawable.click_2_0_0_list_item);

            detaillistImg = (RoundCornerImageView) itemView.findViewById(R.id.detaillistImg);
            tvExchangeName = (TextView) itemView.findViewById(R.id.tvExchangeName);
            tvExchangeTime = (TextView) itemView.findViewById(R.id.tvExchangeTime);


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

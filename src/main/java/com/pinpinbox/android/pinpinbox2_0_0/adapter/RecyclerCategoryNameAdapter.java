package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.Gradient.ScrimUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumCategory;

import java.util.List;

/**
 * Created by vmage on 2017/11/22.
 */

public class RecyclerCategoryNameAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private RecyclerCategoryNameAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(RecyclerCategoryNameAdapter.OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Activity mActivity;

    private List<ItemAlbumCategory> itemAlbumCategoryList;


    public RecyclerCategoryNameAdapter(Activity activity, List<ItemAlbumCategory> itemAlbumCategoryList) {
        this.mActivity = activity;
        this.itemAlbumCategoryList = itemAlbumCategoryList;


    }


    @Override
    public int getItemCount() {
        return itemAlbumCategoryList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_category_name, null);
        return new RecyclerCategoryNameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        final RecyclerCategoryNameAdapter.ViewHolder holder = (RecyclerCategoryNameAdapter.ViewHolder) vHolder;
        holder.position = position;


        /*set name*/
        TextUtility.setBold(holder.tvName, true);
        holder.tvName.setText(itemAlbumCategoryList.get(position).getName());




        /*set gradient*/
        String color = itemAlbumCategoryList.get(position).getColorhex();

        try {
            if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {
                holder.tvName.setBackground(
                        ScrimUtil.setButtonGradient(
                                Color.parseColor(color), //颜色
                                2, //渐变层数 (偶數)
                                Gravity.BOTTOM,//起始方向
                                12)); //圓角
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;


        private TextView tvName;
        private View vGradient;
        private RelativeLayout rBackground;



        public ViewHolder(View itemView) {
            super(itemView);


            tvName = (TextView)itemView.findViewById(R.id.tvName);
//            vGradient = itemView.findViewById(R.id.vGradient);
//            rBackground = (RelativeLayout)itemView.findViewById(R.id.rBackground);

            tvName.setOnClickListener(this);
            tvName.setOnLongClickListener(this);

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

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
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
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

//        String name = itemAlbumCategoryList.get(position).getName();
//        switch (name){
//            case "拚星聞":
//
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_new)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼旅遊":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_travel)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼開運":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_luck)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼美食":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_food)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼藝文":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_art)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼知識":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_knowledge)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼寵物":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_dog)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼時尚":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_fashion)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼寶貝":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_baby)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//
//            case "拼好康":
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(R.drawable.test_gift)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.bg_2_0_0_no_image)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.img);
//                break;
//        }



        /*set gradient*/
        String color = itemAlbumCategoryList.get(position).getColorhex();

        try {
            if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {
                holder.tvName.setBackground(
                        ScrimUtil.setButtonGradient(
                                Color.parseColor(color), //颜色
                                2, //渐变层数 (偶數)
                                Gravity.BOTTOM,//起始方向
                                8)); //圓角
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;


        private TextView tvName;

        private RelativeLayout rBackground;
        private RoundCornerImageView img;



        public ViewHolder(View itemView) {
            super(itemView);


            tvName = (TextView)itemView.findViewById(R.id.tvName);

//            rBackground = (RelativeLayout)itemView.findViewById(R.id.rBackground);
//
//            img = (RoundCornerImageView)itemView.findViewById(R.id.img);

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

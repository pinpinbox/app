package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumCategory;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;

import java.util.List;

public class RecyclerCategoryAdapter extends RecyclerView.Adapter {


    private Activity mActivity;

    private List<ItemAlbumCategory> itemAlbumCategoryList;

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    public RecyclerCategoryAdapter(Activity activity, List<ItemAlbumCategory> albumCategoryList) {

        this.mActivity = activity;

        this.itemAlbumCategoryList = albumCategoryList;

    }

    @Override
    public int getItemCount() {
        return itemAlbumCategoryList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_category, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        holder.linItemBg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PPBApplication.getInstance().getStaggeredWidth()));

        ImageUtility.setCommonImage(mActivity, holder.categoryImg, itemAlbumCategoryList.get(position).getImage_360x360());

        holder.tvCategoryName.setText(itemAlbumCategoryList.get(position).getName());

    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private LinearLayout linItemBg;

        private ImageView categoryImg;

        private TextView tvCategoryName;

        public ViewHolder(View itemView) {
            super(itemView);

            linItemBg = itemView.findViewById(R.id.linItemBg);

            categoryImg = itemView.findViewById(R.id.categoryImg);

            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);

            linItemBg.setOnClickListener(this);
            linItemBg.setOnLongClickListener(this);

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

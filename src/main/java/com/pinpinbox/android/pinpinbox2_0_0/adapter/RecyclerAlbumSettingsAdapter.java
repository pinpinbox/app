package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumSettings;

import java.util.List;

/**
 * Created by kevin9594 on 2017/4/2.
 */
public class RecyclerAlbumSettingsAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    private Activity mActivity;

    private List<ItemAlbumSettings> settingsList;

    private boolean smallText = true;


    public RecyclerAlbumSettingsAdapter(Activity activity,  List<ItemAlbumSettings> settingsList) {
        this.mActivity = activity;
        this.settingsList = settingsList;
        smallText = false;
    }

    public RecyclerAlbumSettingsAdapter(Activity activity,  List<ItemAlbumSettings> settingsList, boolean smallText) {
        this.mActivity = activity;
        this.settingsList = settingsList;
        this.smallText = true;//強制true
    }


    @Override
    public int getItemCount() {
        return settingsList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_albumsettings, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        holder.tvName.setText(settingsList.get(position).getName());

        if(smallText){
            holder.tvName.setTextSize(12f);
        }

        if(settingsList.get(position).isSelect()){

            holder.tvName.setBackgroundResource(R.drawable.border_2_0_0_click_default_radius);

        }else {

            holder.tvName.setBackgroundResource(R.drawable.click_2_0_0_grey_third_radius);

        }

    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private TextView tvName;


        public ViewHolder(View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);

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

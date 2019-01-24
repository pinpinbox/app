package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;

import java.util.List;

/**
 * Created by vmage on 2017/7/21.
 */
public class RecyclerBarCodeAdapter extends RecyclerView.Adapter {

    /*item click*/
    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    /*delete click*/
    public interface OnDeleteListener{
        void delete(int position);
    }


    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListenter(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;

    }




    private Activity mActivity;

    private List<String> albumindexList;

    public RecyclerBarCodeAdapter(Activity activity, List<String> albumindexList) {
        this.mActivity = activity;
        this.albumindexList = albumindexList;
    }


    @Override
    public int getItemCount() {
        return albumindexList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_barcode, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        holder.tvBarCode.setText(albumindexList.get(position) + "");

        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onDeleteListener!=null){
                    onDeleteListener.delete(position);
                }
            }
        });


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private ImageView deleteImg;
        private TextView tvBarCode;


        public ViewHolder(View itemView) {
            super(itemView);
            tvBarCode = itemView.findViewById(R.id.tvBarCode);
            deleteImg = itemView.findViewById(R.id.deleteImg);


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


    public void addData(int position, String index) {
        albumindexList.add(position, index);
        notifyItemInserted(position);
    }

    public void setData(int position,  String index) {
        albumindexList.set(position, index);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        albumindexList.remove(position);
        notifyItemRemoved(position);
        if (position != albumindexList.size()) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, albumindexList.size() - position);
        }

    }


}

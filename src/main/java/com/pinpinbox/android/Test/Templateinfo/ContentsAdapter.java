package com.pinpinbox.android.Test.Templateinfo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinpinbox.android.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin9594 on 2016/9/28.
 */
public class ContentsAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private List<String> listData;
    private LayoutInflater mInflater;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    public ContentsAdapter(Activity mActivity, List<String> listData){

        this.mActivity = mActivity;
        this.listData = listData;
        mInflater = LayoutInflater.from(mActivity);

    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_tem_contents, parent, false);

        return new ContentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        ContentsViewHolder viewHolder = (ContentsViewHolder)holder;
        viewHolder.position = position;

        String url = listData.get(position);

        Picasso.with(mActivity.getApplicationContext())
                .load(url)
                .config(Bitmap.Config.RGB_565)
                .error(R.drawable.bg_2_0_0_no_image)
                .tag(mActivity.getApplicationContext())
                .into(viewHolder.contentImg);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ContentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private ImageView contentImg;
        public int position;

        public ContentsViewHolder(View view) {
            super(view);



            contentImg = (ImageView)view.findViewById(R.id.contentImg);


            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (null != mOnItemClickLitener) {
                mOnItemClickLitener.onItemClick(v, position);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (null != mOnItemClickLitener) {
                mOnItemClickLitener.onItemLongClick(v, position);
            }

            return true;
        }

    }


}

package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemBoard;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2017/4/7.
 */
public class RecyclerBoardAdapter  extends RecyclerView.Adapter  {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Activity mActivity;

    private List<ItemBoard> itemBoardList;


    public RecyclerBoardAdapter(Activity activity, List<ItemBoard> itemBoardList) {
        this.mActivity = activity;
        this.itemBoardList = itemBoardList;


    }


    @Override
    public int getItemCount() {
        return itemBoardList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_board, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        holder.tvName.setText(itemBoardList.get(position).getName());
        holder.tvText.setText(itemBoardList.get(position).getText());
        holder.tvInsertTime.setText(itemBoardList.get(position).getInserttime());

        String url  = itemBoardList.get(position).getPicture();
        if(url!=null&& !url.equals("")) {
            Picasso.with(mActivity.getApplicationContext())
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.userImg);
        }else {
            holder.userImg.setImageResource(R.drawable.member_back_head);
        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RoundedImageView userImg;
        private TextView tvName, tvText, tvInsertTime;


        public ViewHolder(View itemView) {
            super(itemView);


            userImg = (RoundedImageView)itemView.findViewById(R.id.userImg);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            tvText = (TextView)itemView.findViewById(R.id.tvText);
            tvInsertTime = (TextView)itemView.findViewById(R.id.tvInsertTime);


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

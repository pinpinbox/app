package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2017/2/2.
 */
public class RecyclerSearchUserAdapter  extends RecyclerView.Adapter {


    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    private Activity mActivity;

    private ArrayList<HashMap<String, Object>> listData;


    public RecyclerSearchUserAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData) {
        this.mActivity = activity;
        this.listData = listData;


    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_search_user, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        String strPicture = (String)listData.get(position).get(Key.picture);
        String strName = (String)listData.get(position).get(Key.name);

        holder.tvName.setText(strName);

        if (SystemUtility.Above_Equal_V5()) {
            holder.userImg.setTransitionName(strPicture);
        }

        if (strPicture == null || strPicture.equals("")) {
            holder.userImg.setImageResource(R.drawable.member_back_head);
        } else {
                Picasso.with(mActivity.getApplicationContext())
                        .load(strPicture)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.member_back_head)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.userImg);
        }



    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private LinearLayout linBackground;
        private RoundCornerImageView userImg;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);


            linBackground = (LinearLayout)itemView.findViewById(R.id.linBackground);

            linBackground.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            userImg = (RoundCornerImageView)itemView.findViewById(R.id.userImg);
            tvName = (TextView)itemView.findViewById(R.id.tvName);

            linBackground.setOnClickListener(this);
            linBackground.setOnLongClickListener(this);

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


    public void addData(int position, HashMap<String, Object> map) {
        listData.add(position, map);
        notifyItemInserted(position);
    }

    public void setData(int position, HashMap<String, Object> map) {
        listData.set(position, map);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }


}


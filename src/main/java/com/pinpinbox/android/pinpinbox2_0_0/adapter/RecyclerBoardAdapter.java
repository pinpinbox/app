package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemBoard;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.RadiusBackgroundSpan;
import com.pinpinbox.android.pinpinbox2_0_0.custom.manager.TagManager;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2017/4/7.
 */
public class RecyclerBoardAdapter extends RecyclerView.Adapter {


    public interface OnUserNameClickListener {
        void onNameClick(int position);
    }

    private OnUserNameClickListener onUserNameClickListener;

    public void setOnUserNameClickListener(OnUserNameClickListener onUserNameClickListener) {
        this.onUserNameClickListener = onUserNameClickListener;
    }


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

    private RadiusBackgroundSpan spanBg;


    public RecyclerBoardAdapter(Activity activity, List<ItemBoard> itemBoardList) {
        this.mActivity = activity;
        this.itemBoardList = itemBoardList;

        spanBg = new RadiusBackgroundSpan(Color.parseColor(ColorClass.GREY_SECOND), 8, Color.parseColor(ColorClass.PINK_FRIST));

    }


    @Override
    public int getItemCount() {
        return itemBoardList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_board, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        holder.tvName.setText(itemBoardList.get(position).getName());
        holder.tvInsertTime.setText(itemBoardList.get(position).getInserttime());

        String url = itemBoardList.get(position).getPicture();
        if (url != null && !url.equals("")) {
            Picasso.get()
                    .load(url)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.userImg);
        } else {
            holder.userImg.setImageResource(R.drawable.member_back_head);
        }


        try {
            TagManager manager = new TagManager(itemBoardList.get(position).getText());


            if (manager.getItemTagUserList().size() > 0) {

                MyLog.Set("d", RecyclerBoardAdapter.class, "有tag");
                MyLog.Set("d", RecyclerBoardAdapter.class, "message => " + manager.getMessage());

                for (int i = 0; i < manager.getItemTagUserList().size(); i++) {
                    MyLog.Set("e", RecyclerBoardAdapter.class, "name => " + manager.getItemTagUserList().get(i).getName());
                    MyLog.Set("e", RecyclerBoardAdapter.class, "user_id => " + manager.getItemTagUserList().get(i).getUser_id());
                    MyLog.Set("e", RecyclerBoardAdapter.class, "sendType => " + manager.getItemTagUserList().get(i).getSendType());
                    MyLog.Set("e", RecyclerBoardAdapter.class, "startIndex => " + manager.getItemTagUserList().get(i).getStartIndex());
                    MyLog.Set("e", RecyclerBoardAdapter.class, "endIndex => " + manager.getItemTagUserList().get(i).getEndIndex());
                    MyLog.Set("e", RecyclerBoardAdapter.class, "************************************************************************************************");
                }

                SpannableString spanString = new SpannableString(manager.getMessage());

                for (int i = 0; i < manager.getItemTagUserList().size(); i++) {

                    ForegroundColorSpan spanBg = new ForegroundColorSpan(Color.parseColor(ColorClass.MAIN_FIRST));
                    spanString.setSpan(spanBg, manager.getItemTagUserList().get(i).getStartIndex(), manager.getItemTagUserList().get(i).getEndIndex(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                holder.tvText.setText(spanString);

            } else {
                MyLog.Set("e", RecyclerBoardAdapter.class, "無tag");
                holder.tvText.setText(manager.getMessage());

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                if (onUserNameClickListener != null) {
                    onUserNameClickListener.onNameClick(position);
                }

            }
        });


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RoundedImageView userImg;
        private TextView tvName, tvText, tvInsertTime;


        public ViewHolder(View itemView) {
            super(itemView);


            userImg = itemView.findViewById(R.id.userImg);
            tvName = itemView.findViewById(R.id.tvName);
            tvText = itemView.findViewById(R.id.tvText);
            tvInsertTime = itemView.findViewById(R.id.tvInsertTime);


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

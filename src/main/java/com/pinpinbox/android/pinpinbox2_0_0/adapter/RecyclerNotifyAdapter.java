package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2016/12/20.
 */
public class RecyclerNotifyAdapter extends RecyclerView.Adapter {


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

    public RecyclerNotifyAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData) {
        this.mActivity = activity;
        this.listData = listData;
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static final int TYPE_USER = 0;
    public static final int TYPE_ALBUMQUEUE = 1;
    public static final int TYPE_ALBUMCOOPERATION = 2;
    public static final int TYPE_DATE = 3;
    public static final int TYPE_SYSTEM = 4;
    public static final int TYPE_ALBUMMESSAGEBOARD = 5;

    @Override
    public int getItemViewType(int position) {
        String viewType = (String) listData.get(position).get(MapKey.type);
        int type = -1;
        switch (viewType) {
            case "date":
                type = TYPE_DATE;
                break;
            case "user":
                type = TYPE_USER;
                break;
            case "albumqueue":
                type = TYPE_ALBUMQUEUE;
                break;
            case "albumcooperation":
                type = TYPE_ALBUMCOOPERATION;
                break;
            case "albumqueue@messageboard":
                type = TYPE_ALBUMMESSAGEBOARD;
                break;

            case "user@messageboard":
                type = TYPE_USER;
                break;

            case "event":
                type = TYPE_SYSTEM;
                break;

            case "categoryarea":
                type = TYPE_SYSTEM;
                break;

            case "":
                type = TYPE_SYSTEM;
                break;
        }

        return type;
    }

    private View getViewByViewType(int viewType) {

        View view = null;

        switch (viewType) {
            case TYPE_DATE:
                view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_notify_date, null);
                break;
            case TYPE_USER:
                view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_notify_user, null);
                break;

            case TYPE_ALBUMQUEUE:
                view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_notify_album, null);
                break;

            case TYPE_ALBUMCOOPERATION:
                view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_notify_user, null);
                break;

            case TYPE_ALBUMMESSAGEBOARD:
                view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_notify_album, null);
                break;

            case TYPE_SYSTEM:
                view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_notify_system, null);
                break;
            default:
                view = null;

        }
        return view;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = getViewByViewType(viewType);

        if (view != null) {

            if (viewType == TYPE_ALBUMQUEUE || viewType == TYPE_ALBUMMESSAGEBOARD) {
                return new ViewAlbumHolder(view);
            } else if (viewType == TYPE_DATE) {
                return new ViewDateHolder(view);
            } else if (viewType == TYPE_SYSTEM) {
                return new ViewSystemHolder(view);
            } else {
                return new ViewUserHolder(view);
            }
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        String type = (String) listData.get(position).get(MapKey.type);
        String strImage = (String) listData.get(position).get(Key.image);
        String strMessage = (String) listData.get(position).get(Key.message);
        String strTime = (String) listData.get(position).get(Key.messageTime);

        if (type.equals("albumqueue") || type.equals("albumqueue@messageboard")) {

            ViewAlbumHolder holder = (ViewAlbumHolder) vHolder;
            holder.position = position;
            holder.tvMessage.setText(strMessage);
            holder.tvTime.setText(strTime);

            if (strImage == null || strImage.equals("")) {
                holder.coverImg.setImageResource(R.drawable.bg_2_0_0_no_image);
            } else {
                Picasso.with(mActivity.getApplicationContext())
                        .load(strImage)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.bg_2_0_0_no_image)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.coverImg);
            }

            if(type.equals("albumqueue")) {

                holder.tvMessageTypeText.setText(R.string.pinpinbox_2_0_0_notify_type_album_notice);
                holder.messageTypeImg.setImageResource(R.drawable.ic200_create_album_white);
                holder.messageTypeImg.setBackgroundResource(R.drawable.border_2_0_0_notify_type_albumnotify);

            }else if(type.equals("albumqueue@messageboard")){

                holder.tvMessageTypeText.setText(R.string.pinpinbox_2_0_0_notify_type_user_interactive_notice);
                holder.messageTypeImg.setImageResource(R.drawable.ic200_userinteractive_white);
                holder.messageTypeImg.setBackgroundResource(R.drawable.border_2_0_0_notify_type_userinteractive);

            }


        } else if (type.equals("user") || type.equals("albumcooperation") || type.equals("user@messageboard")) {
            ViewUserHolder holder = (ViewUserHolder) vHolder;
            holder.position = position;
            holder.tvMessage.setText(strMessage);
            holder.tvTime.setText(strTime);



            if (strImage == null || strImage.equals("")) {
                holder.userImg.setImageResource(R.drawable.member_back_head);
            } else {
                Picasso.with(mActivity.getApplicationContext())
                        .load(strImage)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.member_back_head)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.userImg);
            }

            if (type.equals("user")|| type.equals("user@messageboard")) {

                if (SystemUtility.Above_Equal_V5()) {
                    holder.userImg.setTransitionName(strImage);
                }

                holder.tvMessageTypeText.setText(R.string.pinpinbox_2_0_0_notify_type_user_interactive_notice);
                holder.messageTypeImg.setImageResource(R.drawable.ic200_userinteractive_white);
                holder.messageTypeImg.setBackgroundResource(R.drawable.border_2_0_0_notify_type_userinteractive);


            } else if (type.equals("albumcooperation")) {
                holder.tvMessageTypeText.setText(R.string.pinpinbox_2_0_0_notify_type_cooperation_notice);
                holder.messageTypeImg.setImageResource(R.drawable.ic200_cooperation_white);
                holder.messageTypeImg.setBackgroundResource(R.drawable.border_2_0_0_notify_type_cooperation);

            }

        } else if (type.equals("date")) {

            ViewDateHolder holder = (ViewDateHolder) vHolder;
            holder.position = position;

            String strDate = (String) listData.get(holder.position).get("date");

            TextPaint tp = holder.tvDate.getPaint();

            if (strDate.equals(Value.noMessage)) {

                tp.setFakeBoldText(false);

                holder.tvDate.setGravity(Gravity.CENTER_VERTICAL);
                holder.tvDate.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                holder.tvDate.setText(R.string.pinpinbox_2_0_0_guide_no_notify);

                holder.pointImg.setVisibility(View.INVISIBLE);

            } else {

                tp.setFakeBoldText(true);

                holder.tvDate.setGravity(Gravity.BOTTOM);
                holder.tvDate.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                holder.tvDate.setText(strDate);

                holder.pointImg.setVisibility(View.VISIBLE);

            }


        } else if (type.equals("") || type.equals("event") || type.equals("categoryarea")) {

            ViewSystemHolder holder = (ViewSystemHolder) vHolder;
            holder.position = position;
            holder.tvMessage.setText(strMessage);
            holder.tvTime.setText(strTime);

            holder.tvMessageTypeText.setText(R.string.pinpinbox_2_0_0_notify_type_system_notice);
            holder.messageTypeImg.setImageResource(R.drawable.pinpin_192);


        }


    }

    private class ViewSystemHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private RelativeLayout rBackground;
        private ImageView messageTypeImg;
        private TextView tvMessage, tvTime, tvMessageTypeText;

        int position;

        public ViewSystemHolder(View itemView) {
            super(itemView);


            rBackground = (RelativeLayout)itemView.findViewById(R.id.rBackground);
            messageTypeImg = (ImageView) itemView.findViewById(R.id.messageTypeImg);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvMessageTypeText = (TextView) itemView.findViewById(R.id.tvMessageTypeText);


            rBackground.setOnClickListener(this);
            rBackground.setOnLongClickListener(this);

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


    private class ViewUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {


        private RelativeLayout rBackground;
        private RoundCornerImageView userImg;
        private ImageView messageTypeImg;

        private TextView tvMessage, tvTime, tvMessageTypeText;

        int position;

        public ViewUserHolder(View itemView) {
            super(itemView);

            rBackground = (RelativeLayout) itemView.findViewById(R.id.rBackground);
            userImg = (RoundCornerImageView) itemView.findViewById(R.id.userImg);
            messageTypeImg = (ImageView) itemView.findViewById(R.id.messageTypeImg);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvMessageTypeText = (TextView) itemView.findViewById(R.id.tvMessageTypeText);


            rBackground.setOnClickListener(this);
            rBackground.setOnLongClickListener(this);

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


    private class ViewAlbumHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;


        private RelativeLayout rBackground;
        private ImageView messageTypeImg;
        private RoundedImageView coverImg;
        private TextView tvMessage, tvTime, tvMessageTypeText;

        public ViewAlbumHolder(View itemView) {
            super(itemView);

            rBackground = (RelativeLayout) itemView.findViewById(R.id.rBackground);
            coverImg = (RoundedImageView) itemView.findViewById(R.id.coverImg);
            messageTypeImg = (ImageView) itemView.findViewById(R.id.messageTypeImg);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvMessageTypeText = (TextView) itemView.findViewById(R.id.tvMessageTypeText);


            rBackground.setOnClickListener(this);
            rBackground.setOnLongClickListener(this);

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

    private class ViewDateHolder extends RecyclerView.ViewHolder {

        int position;

        private TextView tvDate;
        private RoundedImageView pointImg;

        public ViewDateHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            pointImg = (RoundedImageView) itemView.findViewById(R.id.pointImg);
        }
    }

}

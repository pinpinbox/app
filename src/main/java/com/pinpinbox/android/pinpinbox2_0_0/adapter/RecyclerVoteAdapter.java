package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.labelview.LabelView;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Test.ScaleTouhListener;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2017/10/11.
 */

public class RecyclerVoteAdapter extends RecyclerView.Adapter {


    /*item click*/
    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    /*vote click*/
    public interface onVoteListener {
        void onVoteClick(int position);
    }

    private onVoteListener onVoteListener;

    public void setOnVoteListener(onVoteListener onVoteListener){
        this.onVoteListener = onVoteListener;
    }

    private Activity mActivity;

    private List<ItemAlbum> itemAlbumList;

    private EndlessRecyclerOnScrollListener mOnScrollListener;

    public RecyclerVoteAdapter(Activity activity, List<ItemAlbum> itemAlbumList, EndlessRecyclerOnScrollListener mOnScrollListener) {
        this.mActivity = activity;
        this.itemAlbumList = itemAlbumList;
        this.mOnScrollListener = mOnScrollListener;
    }


    @Override
    public int getItemCount() {
        return itemAlbumList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_vote2, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        setAlbumId(holder, position);

        setEventJoinCount(holder, position);

        setAlbumCover(holder, position);

        setAlbumName(holder, position);

        setUserPicture(holder, position);

        setUserName(holder, position);

        setClickUser(holder, position);

        setVoteClick(holder, position);

        checkVoteStatus(holder, position);


        if(searchKey.equals("")) {

            if (position + 1 < 61) {
                holder.lbRanking.setText(position + 1 + "");
                holder.lbRanking.setVisibility(View.VISIBLE);
            } else {
                holder.lbRanking.setVisibility(View.GONE);
            }

        }else {
            holder.lbRanking.setVisibility(View.GONE);
        }



    }


    private void setAlbumId(ViewHolder holder, int position) {
        holder.tvAlbumId.setText(itemAlbumList.get(position).getAlbum_id());
    }

    private void setEventJoinCount(ViewHolder holder, int position) {
        TextUtility.setBold(holder.tvVoteCount, true);
        holder.tvVoteCount.setText(itemAlbumList.get(position).getEvent_join() + "");
    }

    private void setAlbumCover(final ViewHolder holder, int position) {

//        int h = itemAlbumList.get(position).getCover_height();
//        holder.coverImg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
//        holder.coverImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String color = itemAlbumList.get(position).getCover_hex();
        final GradientDrawable drawable = (GradientDrawable) holder.rItemBg.getBackground();
        if (color != null && !color.equals("")) {
            drawable.setColor(Color.parseColor(color));
        } else {
            drawable.setColor(Color.parseColor(ColorClass.GREY_SECOND));
        }

        try {
            final String strCover = itemAlbumList.get(position).getCover();

            if (SystemUtility.Above_Equal_V5()) {
                holder.coverImg.setTransitionName(strCover);
            }

            if (strCover != null && !strCover.equals("") && !strCover.equals("null")) {

                Picasso.with(mActivity.getApplicationContext())
                        .load(strCover)
                        .config(Bitmap.Config.RGB_565)
                        .priority(Picasso.Priority.HIGH)
                        .error(R.drawable.bg_2_0_0_no_image)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.coverImg, new Callback() {
                            @Override
                            public void onSuccess() {

                                drawable.setColor(Color.parseColor(ColorClass.GREY_SECOND));

                                holder.coverImg.setAlpha(0.8f);
                            }

                            @Override
                            public void onError() {

                            }
                        });


            } else {
                holder.coverImg.setImageResource(R.drawable.bg_2_0_0_no_image);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAlbumName(ViewHolder holder, int position) {
        try {
            TextUtility.setBold(holder.tvAlbumName, true);

            String strAlbumName = itemAlbumList.get(position).getName();
            holder.tvAlbumName.setText(strAlbumName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUserPicture(ViewHolder holder, int position) {
        final String strPicture = itemAlbumList.get(position).getUser_picture();

        if (SystemUtility.Above_Equal_V5()) {
            ViewCompat.setTransitionName(holder.userImg,
                    strPicture);
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



    private void setUserName(ViewHolder holder, int position) {
        try {
            String strUsername = itemAlbumList.get(position).getUser_name();
            holder.tvUserName.setText(strUsername);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setClickUser(final ViewHolder holder, final int position) {
        holder.linUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                String author_id = itemAlbumList.get(position).getUser_id() + "";

                if (author_id.equals(PPBApplication.getInstance().getId())) {

//                    ((Main2Activity) mActivity).toMePage();

                } else {

                    ActivityIntent.toUser(
                            mActivity,
                            true,
                            false,
                            author_id,
                            itemAlbumList.get(position).getUser_picture(),
                            itemAlbumList.get(position).getUser_name(),
                            holder.userImg
                    );

                }

            }
        });
    }


    private void setVoteClick(final ViewHolder holder, final int position) {



        holder.voteImg.setOnTouchListener(new ScaleTouhListener(new ScaleTouhListener.TouchCallBack() {
            @Override
            public void Touch() {
                mOnScrollListener.setvScaleTouchView(holder.voteImg);
            }

            @Override
            public void Up() {

                if(ClickUtils.ButtonContinuousClick()){
                    return;
                }

                if(onVoteListener!=null){
                    onVoteListener.onVoteClick(position);
                }

            }
        }));



    }

    private void checkVoteStatus(ViewHolder holder, int position) {

        boolean isHas_Voted = itemAlbumList.get(position).isHas_voted();


        if (isHas_Voted) {


            holder.voteImg.setVisibility(View.INVISIBLE);


        } else {

            holder.voteImg.setVisibility(View.VISIBLE);

        }


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        private int position;
        private RoundCornerImageView userImg, coverImg;
        private ImageView voteImg;
        private TextView tvUserName, tvAlbumName, tvVoteCount, tvAlbumId;
        private LinearLayout linUser;
        private RelativeLayout rItemBg;
        private LabelView lbRanking;

        public ViewHolder(View itemView) {
            super(itemView);


            itemView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            coverImg = (RoundCornerImageView) itemView.findViewById(R.id.coverImg);
            userImg = (RoundCornerImageView) itemView.findViewById(R.id.userImg);

            voteImg = (ImageView)itemView.findViewById(R.id.voteImg);

            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvAlbumName = (TextView) itemView.findViewById(R.id.tvAlbumName);
            tvVoteCount = (TextView) itemView.findViewById(R.id.tvVoteCount);
            tvAlbumId = (TextView) itemView.findViewById(R.id.tvAlbumId);


            linUser = (LinearLayout) itemView.findViewById(R.id.linUser);

            rItemBg = (RelativeLayout) itemView.findViewById(R.id.rItemBg);

            lbRanking = (LabelView)itemView.findViewById(R.id.lbRanking);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


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


    public void addData(int position, ItemAlbum itemAlbum) {
        itemAlbumList.add(position, itemAlbum);
        notifyItemInserted(position);
    }

    public void setData(int position, ItemAlbum itemAlbum) {
        itemAlbumList.set(position, itemAlbum);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        itemAlbumList.remove(position);
        notifyItemRemoved(position);
    }

    private String searchKey = "";
    public void setSearchKeyCheck(String searchKey){
        this.searchKey = searchKey;
    }




}

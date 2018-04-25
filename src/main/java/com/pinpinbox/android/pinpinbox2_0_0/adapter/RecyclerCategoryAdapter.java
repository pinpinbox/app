package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kevin9594 on 2017/4/8.
 */
public class RecyclerCategoryAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Activity mActivity;

    private List<ItemAlbum> albumList;


    public RecyclerCategoryAdapter(Activity activity, List<ItemAlbum> albumList) {
        this.mActivity = activity;
        this.albumList = albumList;


    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_category_contents, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {

        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;


        /*set album name*/
        try {
            holder.tvAlbumName.setText(albumList.get(position).getName());
            TextUtility.setBold(holder.tvAlbumName, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*set album cover*/
        int h = albumList.get(position).getCover_height();
        holder.coverImg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));
        holder.coverImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        String color = albumList.get(position).getCover_hex();
        final GradientDrawable drawable = (GradientDrawable) holder.rItemBg.getBackground();
        if (color != null && !color.equals("")) {
            drawable.setColor(Color.parseColor(color));
        } else {
            drawable.setColor(Color.parseColor(ColorClass.GREY_SECOND));
        }

        if (SystemUtility.Above_Equal_V5()) {
            holder.coverImg.setTransitionName(albumList.get(position).getCover());
        }

        if (!albumList.get(position).getCover().equals("")) {
            Picasso.with(mActivity.getApplicationContext())
                    .load(albumList.get(position).getCover())
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.coverImg, new Callback() {
                        @Override
                        public void onSuccess() {

                            drawable.setColor(Color.parseColor(ColorClass.GREY_SECOND));

                            holder.coverImg.setAlpha(0.85f);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            holder.coverImg.setImageResource(R.drawable.bg_2_0_0_no_image);
        }


        /*set album type*/
        if (albumList.get(position).isVideo() || albumList.get(position).isSlot() || albumList.get(position).isExchange() || albumList.get(position).isAudio()) {
            holder.linType.setVisibility(View.VISIBLE);

            if (albumList.get(position).isAudio()) {
                holder.audioImg.setVisibility(View.VISIBLE);
            } else {
                holder.audioImg.setVisibility(View.GONE);
            }
            if (albumList.get(position).isVideo()) {
                holder.videoImg.setVisibility(View.VISIBLE);
            } else {
                holder.videoImg.setVisibility(View.GONE);
            }
            if (albumList.get(position).isSlot()) {
                holder.slotImg.setVisibility(View.VISIBLE);
            } else {

                if(albumList.get(position).isExchange()){
                    holder.slotImg.setVisibility(View.VISIBLE);
                }else {
                    holder.slotImg.setVisibility(View.GONE);
                }

            }
//            if (albumList.get(position).isExchange()) {
//                holder.slotImg.setVisibility(View.VISIBLE);
//            } else {
//                holder.slotImg.setVisibility(View.GONE);
//            }//歸類於slot
        } else {
            holder.linType.setVisibility(View.GONE);
        }

//           /*set viewed cound*/
//        try {
//
//            int intViewed = albumList.get(position).getViewed();
//
//            if (intViewed > 9999) {
//                String strViewed = StringUtil.ThousandToK(intViewed);
//                holder.tvViewed.setText(strViewed + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_k_times_views));
//            } else {
//
//                if (intViewed < 2) {
//                    holder.tvViewed.setText(intViewed + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_views_single));
//                } else {
//                    holder.tvViewed.setText(intViewed + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_views));
//
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        /*set likes count*/
//        int like = albumList.get(position).getLikes();
//
//        if (like > 9999) {
//            String strLike = StringUtil.ThousandToK(like);
//            holder.tvLike.setText(" ▪ " + strLike + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_k_times_likes));
//        } else {
//
//            if (like < 2) {
//                holder.tvLike.setText(" ▪ " + like + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_likes_single));
//            } else {
//                holder.tvLike.setText(" ▪ " + like + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_likes));
//            }
//
//        }
//
//
//
//
//        /*set user picture*/
//        if (!albumList.get(position).getUser_picture().equals("")) {
//            Picasso.with(mActivity.getApplicationContext())
//                    .load(albumList.get(position).getUser_picture())
//                    .config(Bitmap.Config.RGB_565)
//                    .error(R.drawable.member_back_head)
//                    .tag(mActivity.getApplicationContext())
//                    .into(holder.userImg);
//        } else {
//            holder.userImg.setImageResource(R.drawable.member_back_head);
//        }
//
//        /*set user name*/
//        holder.tvUserName.setText(albumList.get(position).getUser_name());
//
//
//        holder.linUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String author_id = StringIntMethod.IntToString(albumList.get(position).getUser_id());
//
//                if (author_id.equals(PPBApplication.getInstance().getId())) {
//
////                    ((Main2Activity)mActivity).toMePage();
//
//
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Key.author_id, author_id);
//                    Intent intent = new Intent(mActivity, Author2Activity.class);
//                    intent.putExtras(bundle);
//                    mActivity.startActivity(intent);
//                    ActivityAnim.StartAnim(mActivity);
//                }
//            }
//        });


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RoundedImageView userImg;
        private TextView tvUserName, tvAlbumName, tvViewed, tvLike;
        private LinearLayout linUser, linType;
        private ImageView audioImg, videoImg, slotImg;
        private RoundCornerImageView coverImg;
        private RelativeLayout rItemBg;


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            rItemBg = (RelativeLayout) itemView.findViewById(R.id.rItemBg);

            coverImg = (RoundCornerImageView) itemView.findViewById(R.id.coverImg);
            userImg = (RoundedImageView) itemView.findViewById(R.id.userImg);

            tvAlbumName = (TextView) itemView.findViewById(R.id.tvAlbumName);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvViewed = (TextView) itemView.findViewById(R.id.tvViewed);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);

            linType = (LinearLayout) itemView.findViewById(R.id.linType);
            linUser = (LinearLayout) itemView.findViewById(R.id.linUser);

            audioImg = (ImageView) itemView.findViewById(R.id.audioImg);
            videoImg = (ImageView) itemView.findViewById(R.id.videoImg);
            slotImg = (ImageView) itemView.findViewById(R.id.slotImg);

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

}

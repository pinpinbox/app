package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
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
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Utility.StringUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;

import java.util.List;

/**
 * Created by kevin9594 on 2016/12/18.
 */
public class RecyclerHomeAdapter extends RecyclerView.Adapter {


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


    private boolean isShowTime = false;

    public void setShowTime(boolean isShowTime) {
        this.isShowTime = isShowTime;
    }

    public boolean isShowTime() {
        return this.isShowTime;
    }


    public RecyclerHomeAdapter(Activity activity, List<ItemAlbum> itemAlbumList) {
        this.mActivity = activity;
        this.albumList = itemAlbumList;

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_home, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {


        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;


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


        /*set album name*/
        try {
            holder.tvAlbumName.setText(albumList.get(position).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*set cover*/
        try {
            final String strCover = albumList.get(position).getCover();

            if (SystemUtility.Above_Equal_V5()) {
                holder.coverImg.setTransitionName(strCover);
            }

            ImageUtility.setImage(mActivity, holder.coverImg, strCover);

        } catch (Exception e) {
            e.printStackTrace();
        }


        /*set usefor*/
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

        } else {
            holder.linType.setVisibility(View.GONE);
        }


        /*set time*/
        try {
            holder.tvTime.setText(albumList.get(position).getDifftime());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (isShowTime) {

            holder.linShowOrHide.setVisibility(View.VISIBLE);


            /*set location*/
            try {
                String strLocation = albumList.get(position).getLocation();
                if (strLocation.equals("")) {
                    holder.tvLocation.setText("");
                    holder.linLocation.setVisibility(View.GONE);
                } else {
                    holder.tvLocation.setText(strLocation);
                    holder.linLocation.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



         /*set viewed cound*/
            try {

                int intViewed = albumList.get(position).getViewed();

                if (intViewed > 9999) {
                    String strViewed = StringUtil.ThousandToK(intViewed);
                    holder.tvViewed.setText(strViewed + "K" + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_views));
                } else {

                    holder.tvViewed.setText(intViewed + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_views));


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        /*set likes count*/
            int like = albumList.get(position).getLikes();

            if (like > 9999) {
                String strLike = StringUtil.ThousandToK(like);
                holder.tvLike.setText(" ▪ " + strLike + "K" + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_likes));
            } else {


                holder.tvLike.setText(" ▪ " + like + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_likes));


            }


        } else {


            holder.linShowOrHide.setVisibility(View.GONE);

        }





        /*click user*/
//        holder.linUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (ClickUtils.ButtonContinuousClick()) {
//                    return;
//                }
//
//                FlurryUtil.onEvent(FlurryKey.home_click_item_user);
//
//
//                String author_id = albumList.get(position).getUser_id() + "";
//
//                if (author_id.equals(PPBApplication.getInstance().getId())) {
//
//                    ((MainActivity) mActivity).toMePage(false);
//
//                } else {
//
//                    ActivityIntent.toUser(
//                            mActivity,
//                            true,
//                            false,
//                            author_id,
//                            albumList.get(position).getUser_picture(),
//                            null,
//                            holder.userImg
//                    );
//
//                }
//
//
//            }
//        });


    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;


        private ImageView audioImg, videoImg, slotImg;
        private TextView tvAlbumName, tvTime, tvViewed, tvLike, tvLocation;
        private LinearLayout linLocation, linType, linShowOrHide;
        private RoundCornerImageView coverImg;
        private RelativeLayout rItemBg;



        public ViewHolder(final View itemView) {
            super(itemView);

            itemView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            coverImg = itemView.findViewById(R.id.coverImg);
            audioImg = itemView.findViewById(R.id.audioImg);
            videoImg = itemView.findViewById(R.id.videoImg);
            slotImg = itemView.findViewById(R.id.slotImg);

            tvAlbumName = itemView.findViewById(R.id.tvAlbumName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvViewed = itemView.findViewById(R.id.tvViewed);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvLocation = itemView.findViewById(R.id.tvLocation);

            linType = itemView.findViewById(R.id.linType);
            linLocation = itemView.findViewById(R.id.linLocation);
            linShowOrHide = itemView.findViewById(R.id.linShowOrHide);

            rItemBg = itemView.findViewById(R.id.rItemBg);

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
        albumList.add(position, itemAlbum);
        notifyItemInserted(position);
    }

    public void setData(int position, ItemAlbum itemAlbum) {
        albumList.set(position, itemAlbum);
        notifyItemChanged(position);
    }

    public void removeData(int position) {
        albumList.remove(position);
        notifyItemRemoved(position);
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }


}

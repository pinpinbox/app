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
import com.pinpinbox.android.pinpinbox2_0_0.custom.StringClass.ColorClass;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2017/11/21.
 */

public class RecyclerExploreAdapter extends RecyclerView.Adapter {


    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private RecyclerExploreAdapter.OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(RecyclerExploreAdapter.OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Activity mActivity;
    
    private List<ItemAlbum> albumList;


    public RecyclerExploreAdapter(Activity activity, List<ItemAlbum> itemAlbumList) {
        this.mActivity = activity;
        this.albumList = itemAlbumList;

    }


    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_explore, null);
        return new RecyclerExploreAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {


        final RecyclerExploreAdapter.ViewHolder holder = (RecyclerExploreAdapter.ViewHolder) vHolder;
        holder.position = position;


//        int h = albumList.get(position).getCover_height();
//        holder.coverImg.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h));

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

            TextUtility.setBold(holder.tvAlbumName, true);

            String strAlbumName = albumList.get(position).getName();

            holder.tvAlbumName.setText(strAlbumName);
        } catch (Exception e) {
            e.printStackTrace();
        }





//        /*set username*/
//        try {
//            String strUsername = albumList.get(position).getUser_name();
//            holder.tvUserName.setText(strUsername);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//
//        /*set user picture*/
//        final String strPicture = albumList.get(position).getUser_picture();
//        try {
//            if (SystemUtility.Above_Equal_V5()) {
//                holder.userImg.setTransitionName(strPicture);
//            }
//
//            if (strPicture != null && !strPicture.equals("") && !strPicture.equals("null")) {
//                Picasso.with(mActivity.getApplicationContext())
//                        .load(strPicture)
//                        .config(Bitmap.Config.RGB_565)
//                        .error(R.drawable.member_back_head)
//                        .tag(mActivity.getApplicationContext())
//                        .into(holder.userImg);
//
//
//            } else {
//                holder.userImg.setImageResource(R.drawable.member_back_head);
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



        /*set cover*/
        try {
            final String strCover = albumList.get(position).getCover();

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
                holder.slotImg.setVisibility(View.GONE);
            }
            if (albumList.get(position).isExchange()) {
                holder.slotImg.setVisibility(View.VISIBLE);
            } else {
                holder.slotImg.setVisibility(View.GONE);
            }//歸類於slot
        } else {
            holder.linType.setVisibility(View.GONE);
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
//                    ((Main2Activity) mActivity).toMePage();
//
//
//                } else {
//
//                    Bundle bundle = new Bundle();
//                    bundle.putString(Key.author_id, author_id);
//                    bundle.putString(Key.picture, albumList.get(position).getUser_picture());
//
//
//                    if (SystemUtility.Above_Equal_V5()) {
//
//                        Intent intent = new Intent(mActivity, Author2Activity.class).putExtras(bundle);
//                        ActivityOptionsCompat options = ActivityOptionsCompat.
//                                makeSceneTransitionAnimation(mActivity,
//                                        holder.userImg,
//                                        ViewCompat.getTransitionName(holder.userImg));
//                        mActivity.startActivity(intent, options.toBundle());
//
//
//                    } else {
//
//
//                        Intent intent = new Intent(mActivity, Author2Activity.class);
//                        intent.putExtras(bundle);
//                        mActivity.startActivity(intent);
//                        ActivityAnim.StartAnim(mActivity);
//
//
//                    }
//
//
//                }
//
//
//            }
//        });

    }





    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener{

        int position;

        private ImageView userImg;
        private ImageView audioImg, videoImg, slotImg;
        private TextView tvUserName, tvAlbumName;
        private LinearLayout linUser, linType;
        private RoundCornerImageView coverImg;
        private RelativeLayout rItemBg, rClickArea;



        public ViewHolder(final View itemView) {
            super(itemView);



//            itemView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

            coverImg = (RoundCornerImageView) itemView.findViewById(R.id.coverImg);
            userImg = (ImageView) itemView.findViewById(R.id.userImg);
            audioImg = (ImageView) itemView.findViewById(R.id.audioImg);
            videoImg = (ImageView) itemView.findViewById(R.id.videoImg);
            slotImg = (ImageView) itemView.findViewById(R.id.slotImg);

            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvAlbumName = (TextView) itemView.findViewById(R.id.tvAlbumName);


            linType = (LinearLayout) itemView.findViewById(R.id.linType);
            linUser = (LinearLayout) itemView.findViewById(R.id.linUser);
            rItemBg = (RelativeLayout) itemView.findViewById(R.id.rItemBg);
            rClickArea = (RelativeLayout) itemView.findViewById(R.id.rClickArea);

            rClickArea.setOnClickListener(this);
            rClickArea.setOnLongClickListener(this);

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
    
}

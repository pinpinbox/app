package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.listener.OnDetailClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2017/1/6.
 */
public class RecyclerCollectAdapter extends RecyclerView.Adapter {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private OnDetailClickListener onDetailClickListener;

    public void setOnDetailClickListener(OnDetailClickListener onDetailClickListener) {
        this.onDetailClickListener = onDetailClickListener;

    }


    public RecyclerCollectAdapter recyclerCollectAdapter = this;

    private Activity mActivity;

    private NoConnect noConnect;

    private ArrayList<HashMap<String, Object>> listData;

    private int intCollectType;
    private static final int intCollectTypeMy = 100;
    private static final int intCollectTypeOther = 101;
    private static final int intCollectTypeCooperation = 102;


    public RecyclerCollectAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData, int intCollectType) {
        this.mActivity = activity;
        this.listData = listData;
        this.intCollectType = intCollectType;

        recyclerCollectAdapter = this;


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_collect, null);
        return new ViewHolder(view);
    }

    private void itemOpenOrCloseDetail(int position, boolean detail_is_open) {


        HashMap<String, Object> mp = listData.get(position);

        if (mp.containsKey("detail_is_open")) {
            mp.remove("detail_is_open");
            if (detail_is_open) {
                mp.put("detail_is_open", true);
            } else {
                mp.put("detail_is_open", false);
            }


            listData.remove(position);

            listData.add(position, mp);

        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {


        final ViewHolder holder = (ViewHolder) vHolder;
        holder.position = position;

        /*判斷作品zip檔是否壓製  1:是 / 0:否*/
        String strZipped = (String) listData.get(position).get("zipped");
        if (strZipped.equals("1")) {
            holder.rWarn.setVisibility(View.GONE);

        } else {
            holder.rWarn.setVisibility(View.VISIBLE);
        }


        /*判斷detail視窗是否開啟*/
        boolean isOpen = (boolean) listData.get(position).get("detail_is_open");
        if (isOpen) {
            holder.rDetail.setVisibility(View.VISIBLE);

        } else {
            holder.rDetail.setVisibility(View.INVISIBLE);
        }

        /*set album name*/
        try {
            String strAlbumName = (String) listData.get(position).get("albumname");
            TextUtility.setBold(holder.tvAlbumName);
            holder.tvAlbumName.setText(strAlbumName);
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*set cover*/
        try {
            String strCover = (String) listData.get(position).get("albumcover");
            if (strCover != null && !strCover.equals("") && !strCover.isEmpty()) {
                Picasso.with(mActivity.getApplicationContext())
                        .load(strCover)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.bg_2_0_0_no_image)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.coverImg);
                holder.coverImg.setTag(strCover);
            }
        } catch (Exception e) {
            holder.coverImg.setImageResource(R.drawable.bg_2_0_0_no_image);
            e.printStackTrace();
        }

        /*set time*/
        try {
            String strTime = (String) listData.get(position).get("albuminsertdate"); //建立時間
            holder.tvTime.setText(strTime);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /*set count*/
        try {
            String strCooperationCount = (String) listData.get(position).get("count");
            if (strCooperationCount.equals("1")) {
                holder.linCooperationCount.setVisibility(View.GONE);
            } else {
                holder.linCooperationCount.setVisibility(View.VISIBLE);
                holder.tvCooperationCount.setText(strCooperationCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*set user image*/
        if (intCollectType == intCollectTypeOther || intCollectType == intCollectTypeCooperation) {
            holder.userImg.setVisibility(View.VISIBLE);
            final String strPicture = (String) listData.get(position).get(Key.picture);

            if (SystemUtility.Above_Equal_V5()) {
                holder.userImg.setTransitionName(strPicture);
            }

            if (strPicture == null || strPicture.equals("")) {
                holder.userImg.setImageResource(R.drawable.member_back_head);
            } else {
                try {
                    Picasso.with(mActivity.getApplicationContext())
                            .load(strPicture)
                            .config(Bitmap.Config.RGB_565)
                            .error(R.drawable.member_back_head)
                            .tag(mActivity.getApplicationContext())
                            .into(holder.userImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            holder.userImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ClickUtils.ButtonContinuousClick()) {
                        return;
                    }

                    ActivityIntent.toUser(
                            mActivity,
                            true,
                            false,
                            (String) listData.get(position).get(Key.user_id),
                            strPicture,
                            (String) listData.get(position).get("username"),
                            holder.userImg
                    );

                }
            });


        } else {
            holder.userImg.setVisibility(View.GONE);
        }

        holder.detailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rDetail.getVisibility() == View.VISIBLE) {
                    holder.rDetail.setVisibility(View.INVISIBLE);
                    itemOpenOrCloseDetail(position, false);
                } else {
                    holder.rDetail.setVisibility(View.VISIBLE);
                    itemOpenOrCloseDetail(position, true);
                }
                recyclerCollectAdapter.notifyItemChanged(position);
            }
        });

        /*privacy*/
        if (intCollectType == intCollectTypeMy) {

            String strAct = (String) listData.get(position).get(Key.act);

            if (strAct.equals("open")) {
                holder.smallprivacyImg.setImageResource(R.drawable.ic200_act_open_light);
            } else {
                holder.smallprivacyImg.setImageResource(R.drawable.ic200_act_close_pink);
            }

            holder.smallprivacyImg.setVisibility(View.VISIBLE);
            holder.smallprivacyImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtils.ButtonContinuousClick()) {
                        return;
                    }
                    onDetailClickListener.onPrivacyClick(position);
                }
            });


//            holder.linPrivacy.setVisibility(View.VISIBLE);
//            holder.linPrivacy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (ClickUtils.ButtonContinuousClick()) {
//                        return;
//                    }
//                    onDetailClickListener.onPrivacyClick(position);
//                }
//            });
        } else {
//            holder.linPrivacy.setVisibility(View.GONE);
        }

        /*edit*/
        if (intCollectType == intCollectTypeMy || intCollectType == intCollectTypeCooperation) {
            holder.linEdit.setVisibility(View.VISIBLE);
            holder.linEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtils.ButtonContinuousClick()) {
                        return;
                    }
                    onDetailClickListener.onEditClick(position);
                }
            });
        } else {
            holder.linEdit.setVisibility(View.GONE);
        }

        /*cooperation*/
        if (intCollectType == intCollectTypeMy || intCollectType == intCollectTypeCooperation) {
            holder.linCooperation.setVisibility(View.VISIBLE);
            holder.linCooperation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtils.ButtonContinuousClick()) {
                        return;
                    }
                    onDetailClickListener.onCooperationClick(position);
                }
            });
        } else {
            holder.linCooperation.setVisibility(View.GONE);
        }


        holder.linShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                onDetailClickListener.onShareClick(position);
            }
        });

        holder.linDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }
                onDetailClickListener.onDeleteClick(position);
            }
        });

//        holder.linDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onDetailClickListener.onDownloadClick(position);
//            }
//        });


        try {

            int downloadType = (int) listData.get(position).get(Key.downloadType);

            switch (downloadType) {

                case 0: /* 0 => 一般狀態*/

                    holder.downloadImg.setImageResource(R.drawable.ic200_download_white);
                    holder.downloadImg.setVisibility(View.VISIBLE);
//                    holder.avLoading.setVisibility(View.GONE);
                    holder.tvLoadType.setText(R.string.pinpinbox_2_0_0_other_text_download);

                    break;


                case 1: /* 1 => 下載中*/

                    holder.downloadImg.setVisibility(View.GONE);
//                    holder.avLoading.setVisibility(View.VISIBLE);
                    holder.tvLoadType.setText(R.string.pinpinbox_2_0_0_other_text_downloading);

                    break;


                case 2:  /* 2 =>  完成*/

                    holder.downloadImg.setImageResource(R.drawable.icon_2_0_0_dialog_check);
                    holder.downloadImg.setVisibility(View.VISIBLE);
//                    holder.avLoading.setVisibility(View.GONE);
                    holder.tvLoadType.setText(R.string.pinpinbox_2_0_0_other_text_can_redownload);

                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        int position;

        private RelativeLayout rBackground, rDetail, rWarn;
        private RoundedImageView coverImg;
        private RoundCornerImageView userImg;
        private ImageView detailImg, downloadImg, smallprivacyImg;
        private TextView tvAlbumName, tvTime, tvCooperationCount, tvLoadType;
        private LinearLayout  linEdit, linCooperation, linShare, linDelete, linCooperationCount, linDownload;


//        private ImageView downloadImg;


        public ViewHolder(View itemView) {
            super(itemView);

            rBackground = (RelativeLayout) itemView.findViewById(R.id.rBackground);
            rDetail = (RelativeLayout) itemView.findViewById(R.id.rDetail);
            rWarn = (RelativeLayout) itemView.findViewById(R.id.rWarn);

            coverImg = (RoundedImageView) itemView.findViewById(R.id.coverImg);
            userImg = (RoundCornerImageView) itemView.findViewById(R.id.userImg);
            detailImg = (ImageView) itemView.findViewById(R.id.detailImg);
            downloadImg = (ImageView) itemView.findViewById(R.id.downloadImg);
            smallprivacyImg = (ImageView)itemView.findViewById(R.id.smallprivacyImg);

            tvAlbumName = (TextView) itemView.findViewById(R.id.tvAlbumName);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvCooperationCount = (TextView) itemView.findViewById(R.id.tvCooperationCount);
            tvLoadType = (TextView) itemView.findViewById(R.id.tvLoadType);


            linEdit = (LinearLayout) itemView.findViewById(R.id.linEdit);
            linCooperation = (LinearLayout) itemView.findViewById(R.id.linCooperation);
            linShare = (LinearLayout) itemView.findViewById(R.id.linShare);
            linDelete = (LinearLayout) itemView.findViewById(R.id.linDelete);
            linCooperationCount = (LinearLayout) itemView.findViewById(R.id.linCooperationCount);






            rBackground.setOnClickListener(this);
            rBackground.setOnLongClickListener(this);

            rDetail.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });


        }


        @Override
        public void onClick(View v) {

            if (ClickUtils.ButtonContinuousClick()) {
                return;
            }

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


    public void remove(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        if (position != listData.size()) { // 如果移除的是最后一个，忽略
            notifyItemRangeChanged(position, listData.size() - position);
        }

    }

}

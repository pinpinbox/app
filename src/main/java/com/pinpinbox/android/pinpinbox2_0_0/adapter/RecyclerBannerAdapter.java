package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AdHighLightActivity;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemHomeBanner;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.RequestCodeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.RoundTransform;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.libs.GalleryRecyclerView.CardAdapterHelper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerBannerAdapter extends RecyclerView.Adapter<RecyclerBannerAdapter.ViewHolder> {


    public interface OnRecyclerViewListener {

        void onItemClick(int position, View v);

        boolean onItemLongClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private Activity mActivity;

    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    private List<ItemHomeBanner> itemHomeBannerList;

    private RequestOptions opts;

    private List<HashMap<String, Object>> gifList;

    private Fragment fragment;

    private int w, h;

    private LinearLayout.LayoutParams imgLayoutParams, textlayoutParams;

    public RecyclerBannerAdapter(Activity mActivity, List<ItemHomeBanner> itemHomeBannerList, Fragment fragment) {

        this.mActivity = mActivity;
        this.itemHomeBannerList = itemHomeBannerList;
        this.fragment = fragment;

        mCardAdapterHelper.setShowLeftCardWidth(8);
        mCardAdapterHelper.setPagePadding(4);//兩邊


        opts = new RequestOptions()
                .transform(new RoundTransform(mActivity, 6))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.bg_2_0_0_no_image);


        gifList = new ArrayList<>();

        w = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(32);

        h = (w*540)/960;

        imgLayoutParams = new LinearLayout.LayoutParams(w,h);

        textlayoutParams = new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_home_banner, parent, false);
        if(PPBApplication.getInstance().isPhone()) {
            mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if(PPBApplication.getInstance().isPhone()) {
            mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());

            holder.bannerImg.setLayoutParams(imgLayoutParams);

            holder.tvBannerName.setLayoutParams(textlayoutParams);

            ViewControl.setMargins(holder.tvBannerName, 0,0,0,SizeUtils.dp2px(16));

            holder.bannerImg.setBackgroundResource(R.drawable.image_background_2_0_0_banner);

        }

        holder.position = position;

        holder.tvBannerName.setText(itemHomeBannerList.get(position).getName());


        final String imageUrl = itemHomeBannerList.get(position).getImage();

        if (SystemUtility.Above_Equal_V5()) {

            holder.bannerImg.setTransitionName(imageUrl);

        }


        if (imageUrl != null && !imageUrl.equals("") && !imageUrl.equals("null")) {

            String end = imageUrl.substring(imageUrl.lastIndexOf(".") + 1, imageUrl.length()).toLowerCase();

            if (end.equals("gif")) {

                Glide.with(mActivity.getApplicationContext())
                        .asGif()
                        .load(imageUrl)
                        .apply(opts)
                        .into(holder.bannerImg);

                boolean urlIsExist = false;
                if (gifList != null && gifList.size() > 0) {
                    for (int i = 0; i < gifList.size(); i++) {
                        String url = (String) gifList.get(i).get(Key.url);
                        if (url.equals(imageUrl)) {
                            urlIsExist = true;
                            break;
                        }
                    }
                }

                if (!urlIsExist) {

                    MyLog.Set("e", getClass(), "gif url 不存在並添加到gifList");
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(Key.url, imageUrl);
                    map.put(Key.imageView, holder.bannerImg);
                    gifList.add(map);

                } else {

                    MyLog.Set("e", getClass(), "gif url 已存在");

                }


            } else {


                Glide.with(mActivity.getApplicationContext())
                        .asBitmap()
                        .load(imageUrl)
                        .apply(opts)
                        .into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                                holder.bannerImg.setImageBitmap(resource);

                                //判斷第一頁
                                if (position == 0) {

                                    //判斷是否是活動
                                    String event_id = itemHomeBannerList.get(position).getEvent_id();

                                    if (!event_id.equals("")) {

                                        //判斷image是否曾經顯示過
                                        boolean isUrlExist = false;
                                        String bannerList = PPBApplication.getInstance().getData().getString(Key.oldbannerUrlList, "[]");
                                        try {
                                            JSONArray bannerArray = new JSONArray(bannerList);
                                            for (int i = 0; i < bannerArray.length(); i++) {
                                                String strOldImageUrl = (String) bannerArray.get(i);
                                                if (strOldImageUrl.equals(imageUrl)) {
                                                    isUrlExist = true;
                                                    break;
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if (!isUrlExist) {

                                            MyLog.Set("e", BannerPageAdapter.class, "活動還沒顯示過");

                                            Bundle bundle = new Bundle();
                                            bundle.putString(Key.image, itemHomeBannerList.get(position).getImage());
                                            bundle.putString(Key.event_id, itemHomeBannerList.get(position).getEvent_id());
                                            Intent intent = new Intent(mActivity, AdHighLightActivity.class).putExtras(bundle);

                                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                                    makeSceneTransitionAnimation(mActivity,
                                                            holder.bannerImg,
                                                            ViewCompat.getTransitionName(holder.bannerImg));

                                            fragment.startActivityForResult(intent, RequestCodeClass.CloseHighLight, options.toBundle());

                                        } else {
                                            MyLog.Set("e", BannerPageAdapter.class, "活動已顯示過");
                                        }


                                    }


                                }

                            }
                        });


            }


        } else {

            Glide.with(mActivity.getApplicationContext())
                    .load(R.drawable.bg_2_0_0_no_image)
                    .apply(opts)
                    .into(holder.bannerImg);

        }



    }


    @Override
    public int getItemCount() {
        return itemHomeBannerList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView bannerImg;

        private TextView tvBannerName;

        int position;

        public ViewHolder(final View itemView) {
            super(itemView);

            bannerImg = itemView.findViewById(R.id.bannerImg);
            tvBannerName = itemView.findViewById(R.id.tvBannerName);

            bannerImg.setOnClickListener(this);
            bannerImg.setOnLongClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (null != onRecyclerViewListener) {
                onRecyclerViewListener.onItemClick(position, v);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return null != onRecyclerViewListener && onRecyclerViewListener.onItemLongClick(position, v);
        }
    }


    public List<HashMap<String, Object>> getGifList() {
        return this.gifList;
    }

    public RequestOptions getOpts() {
        return this.opts;
    }


}

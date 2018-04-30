package com.pinpinbox.android.SampleTest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.SizeUtils;
import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.libs.GalleryRecyclerView.CardAdapterHelper;

import java.util.List;

/**
 * Created by vmage on 2018/3/8.
 */

public class SampleGalleryAdapter extends RecyclerView.Adapter<SampleGalleryAdapter.ViewHolder> {

    public interface OnRecyclerViewListener {
        void onItemClick(int position, View v);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }


    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    private List<Integer> imgList;

    public SampleGalleryAdapter(List<Integer> imgList) {
        this.imgList = imgList;
        mCardAdapterHelper.setShowLeftCardWidth(SizeUtils.dp2px(16));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_gallery_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        holder.mImageView.setImageResource(imgList.get(position));
    }


    @Override
    public int getItemCount() {
        return imgList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private View rItemBg;
        public int position;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img);
            rItemBg = itemView.findViewById(R.id.rItemBg);

//            rItemBg.setOnTouchListener(this);


        }

//        @SuppressLint("ClickableViewAccessibility")
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//
//
//             float downX = 0, downY = 0;
//             float moveX = 0, moveY = 0;
//             int safeClickArea = 24;
//
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//
//                    downX = event.getRawX();
//                    downY = event.getRawY();
//
//
//
//                    MyLog.Set("e", this.getClass(), "downX => " + downX);
//                    MyLog.Set("e", this.getClass(), "downY => " + downY);
//
//                    v.animate().translationZ(2f)
//                            .scaleX(0.8f)
//                            .scaleY(0.8f)
//                            .start();
//
//                    break;
//
//                case MotionEvent.ACTION_MOVE:
//
//                    moveX = event.getRawX();//手指所在位置
//                    moveY = event.getRawY();//手指所在位置
//
//
//                    MyLog.Set("e", this.getClass(), "moveX => " + moveX);
//                    MyLog.Set("e", this.getClass(), "moveY => " + moveY);
//
//
////                    if (moveY - downY >= safeClickArea || moveY - downY <= -safeClickArea || moveX - downX >= safeClickArea || moveX - downX <= -safeClickArea) {
////                        if(v.getScaleX()<1f){
////                            v.animate().translationZ(16f)
////                                    .scaleX(1f)
////                                    .scaleY(1f)
////                                    .start();
////                        }
////                    }
//
//
//
//
//
//
//                    break;
//
//                case MotionEvent.ACTION_UP:
//
//                    MyLog.Set("e", this.getClass(), "ACTION_UP");
//
//                    if(v.getScaleX()<1f){
//                        v.animate().translationZ(16f)
//                                .scaleX(1f)
//                                .scaleY(1f)
//                                .start();
//                    }
//
//                    if (null != onRecyclerViewListener) {
//                        onRecyclerViewListener.onItemClick(position, v);
//                    }
//
//
//                    break;
//            }
//
//            return true;
//        }



    }


}

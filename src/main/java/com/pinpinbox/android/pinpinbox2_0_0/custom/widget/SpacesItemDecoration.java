package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;

/**
 * Created by vmage on 2017/10/11.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    private boolean hasHeader = false;

    private boolean isSquare = false;


    public SpacesItemDecoration(int space, boolean hasHeader) {
        this.mSpace = space;
        this.hasHeader = hasHeader;


    }

    public SpacesItemDecoration(int space, boolean hasHeader, boolean isSquare) {
        this.mSpace = space;
        this.hasHeader = hasHeader;
        this.isSquare = isSquare;


    }

    @Override
    public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();

        if (hasHeader) {

            if (position != 0) { //0
                setSpace(spanIndex, outRect);
            }

        } else {

            setSpace(spanIndex, outRect);

        }


    }


    private void setSpace(int spanIndex, Rect outRect) {
        if (!PPBApplication.getInstance().isPhone()) {

            if (spanIndex == 0) {
                outRect.left = mSpace;
                outRect.right = 0;
            } else if (spanIndex == 1) {
                outRect.left = mSpace/2;
                outRect.right = mSpace/2;
            } else {
                outRect.left = 0;
                outRect.right = mSpace;
            }

        } else if (PPBApplication.getInstance().isPhone()) {

            if (spanIndex == 0) {
                outRect.left = mSpace;
                outRect.right = 0;
            } else {
                outRect.left = 0;
                outRect.right = mSpace;
            }

        }

        if(isSquare){
            outRect.bottom = 0;
        }else {
            outRect.bottom = 32;
        }



    }


}

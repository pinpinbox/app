package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.SystemType;

/**
 * Created by vmage on 2017/10/11.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    private int deviceType = -1;

    private boolean hasHeader = false;

    private boolean isSquare = false;


    public SpacesItemDecoration(int space, int deviceType, boolean hasHeader) {
        this.mSpace = space;
        this.deviceType = deviceType;
        this.hasHeader = hasHeader;


    }

    public SpacesItemDecoration(int space, int deviceType, boolean hasHeader, boolean isSquare) {
        this.mSpace = space;
        this.deviceType = deviceType;
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
        if (deviceType == SystemType.TABLE) {

            if (spanIndex == 0) {
                outRect.left = mSpace;
                outRect.right = 0;
            } else if (spanIndex == 1) {
                outRect.left = mSpace;
                outRect.right = mSpace;
            } else {
                outRect.left = 0;
                outRect.right = mSpace;
            }

        } else if (deviceType == SystemType.PHONE) {

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

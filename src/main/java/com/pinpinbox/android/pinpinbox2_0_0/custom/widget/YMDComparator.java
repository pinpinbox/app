package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import com.pinpinbox.android.pinpinbox2_0_0.bean.GridItem;

import java.util.Comparator;

/**
 * Created by kevin9594 on 2017/2/18.
 */
public class YMDComparator implements Comparator<GridItem> {

    @Override
    public int compare(GridItem o1, GridItem o2) {
        return o2.getTime().compareTo(o1.getTime());
    }

}

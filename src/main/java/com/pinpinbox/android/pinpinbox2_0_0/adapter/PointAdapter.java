package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemPoint;

import java.util.List;

/**
 * Created by vmage on 2017/3/7.
 */
public class PointAdapter extends BaseAdapter {

    private Activity mActivity;
    private List<ItemPoint> itemPointList;

    public PointAdapter(Activity mActivity, List<ItemPoint> itemPointList){
        this.mActivity = mActivity;
        this.itemPointList = itemPointList;
    }


    @Override
    public int getCount() {
        return itemPointList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemPointList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

         convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_point, null);

        TextView tvPoint = convertView.findViewById(R.id.tvPoint);

        tvPoint.setText(itemPointList.get(position).getObtain() + " P");


        boolean isSelect = itemPointList.get(position).isSelect();

        if(isSelect){
            tvPoint.setBackgroundResource(R.drawable.border_2_0_0_click_default_radius);
        }else {
            tvPoint.setBackgroundResource(R.drawable.border_2_0_0_white_frame_grey_first_radius);
        }


        return convertView;
    }



}

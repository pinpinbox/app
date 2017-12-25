package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.Gradient.ScrimUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbumCategory;

import java.util.List;

/**
 * Created by kevin9594 on 2017/3/19.
 */
public class CategoryAdapter extends BaseAdapter {

    private Activity mActivity;

    private List<ItemAlbumCategory> itemAlbumCategoryList;

    public CategoryAdapter(Activity mActivity, List<ItemAlbumCategory> itemAlbumCategoryList) {
        this.mActivity = mActivity;
        this.itemAlbumCategoryList = itemAlbumCategoryList;
    }


    @Override
    public int getCount() {
        return itemAlbumCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemAlbumCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity.getApplication()).inflate(R.layout.list_item_2_0_0_category, viewGroup, false);

            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.vItemBackground = convertView.findViewById(R.id.vItemBackground);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        viewHolder.tvName.setText(itemAlbumCategoryList.get(position).getName());



        String color = itemAlbumCategoryList.get(position).getColorhex();



        try {
            if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {
                viewHolder.vItemBackground.setBackground(
                        ScrimUtil.makeCubicGradientScrimDrawable(
                                Color.parseColor(color), //颜色
                                8, //渐变层数
                                Gravity.LEFT)); //起始方向
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        TextUtility.setBold(viewHolder.tvName, true);

        return convertView;
    }

    public class ViewHolder {

        TextView tvName;
        RelativeLayout rColor;
        ImageView image;
        View vItemBackground;


    }


}

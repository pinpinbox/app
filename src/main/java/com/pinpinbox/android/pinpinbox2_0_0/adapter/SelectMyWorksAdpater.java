package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.ImageUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;

import java.util.ArrayList;

/**
 * Created by vmage on 2016/5/26.
 */
public class SelectMyWorksAdpater extends BaseAdapter {

    private Activity mActivity;

    private ArrayList<ItemAlbum> canContributeAlbumList;

    public SelectMyWorksAdpater(Activity mActivity, ArrayList<ItemAlbum> canContributeAlbumList) {
        this.mActivity = mActivity;
        this.canContributeAlbumList = canContributeAlbumList;
    }


    @Override
    public int getCount() {
        return canContributeAlbumList.size();
    }

    @Override
    public Object getItem(int position) {
        return canContributeAlbumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_contribute, null);
            holder.rContributionstatus = convertView.findViewById(R.id.rContributionstatus);
            holder.tvName = convertView.findViewById(R.id.tvName);
            holder.coverImg = convertView.findViewById(R.id.coverImg);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

        String strCover = canContributeAlbumList.get(position).getCover();
        String name = canContributeAlbumList.get(position).getName();


        if (canContributeAlbumList.get(position).isContributionstatus()) {
            holder.rContributionstatus.setVisibility(View.VISIBLE);
        } else {
            holder.rContributionstatus.setVisibility(View.INVISIBLE);
        }

        holder.tvName.setText(name);

        ImageUtility.setImage(mActivity, holder.coverImg, strCover);

        return convertView;
    }

    class ViewHolder {

        RelativeLayout rContributionstatus;

        TextView tvName;

        RoundCornerImageView coverImg;

    }

}

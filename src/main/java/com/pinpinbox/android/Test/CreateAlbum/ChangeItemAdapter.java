package com.pinpinbox.android.Test.CreateAlbum;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2015/7/15
 */
public class ChangeItemAdapter extends BaseAdapter {
   private LayoutInflater mInflater;

   private ArrayList<HashMap<String, Object>> listData;
   private Activity mActivity;


    public ChangeItemAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData) {
        this.mActivity = activity;
        this.listData = listData;
        mInflater = LayoutInflater.from(mActivity);

    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(final int position) {

        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_2_0_0_change_item, null);
            holder.picImg = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Picasso.with(mActivity)
                .load((String) listData.get(position).get("image_url_thumbnail"))
                .config(Bitmap.Config.RGB_565)
                .resize(120,180)
                .centerInside()
                .into(holder.picImg);



        if(position ==0){
            holder.name.setText(R.string.pinpinbox_2_0_0_other_text_creation_cover);
        }else
        {
            holder.name.setText(StringIntMethod.IntToString(position));
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView picImg;
        TextView name;
    }


}

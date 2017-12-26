package com.pinpinbox.android.Activity.CreateAlbum;

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
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2016/11/29.
 */
public class SelectPreviewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<HashMap<String, Object>> listData;
    private Activity mActivity;


    public SelectPreviewAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData) {
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
            convertView = mInflater.inflate(R.layout.list_item_2_0_0_select_preview, null);
            holder.picImg = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.selectImg = (ImageView)convertView.findViewById(R.id.selectImg);
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
            holder.name.setText("封面");
        }else {
            holder.name.setText(StringIntMethod.IntToString(position));
        }

        /**2016.11.29 new add*/
        boolean b = (boolean)listData.get(position).get(Key.is_preview);
        if(b){
            holder.selectImg.setImageResource(R.drawable.icon_select_pink500_120x120);
        }else {
            holder.selectImg.setImageResource(R.drawable.icon_unselect_teal500_120x120);
        }



        return convertView;
    }

    public class ViewHolder {
        ImageView picImg, selectImg;
        TextView name;
    }


}

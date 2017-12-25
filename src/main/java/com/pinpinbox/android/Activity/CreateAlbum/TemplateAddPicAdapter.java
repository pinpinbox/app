package com.pinpinbox.android.Activity.CreateAlbum;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pinpinbox.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2015/11/4.
 */
public class TemplateAddPicAdapter extends BaseAdapter {
    private Activity mActivity;

    private ArrayList<HashMap<String, Object>> listdata;


    public TemplateAddPicAdapter(Activity activity, ArrayList<HashMap<String, Object>> listdata) {
        this.mActivity = activity;
        this.listdata = listdata;



    }


    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_2_0_0_creation_template, null);
            holder.picImg = (ImageView) convertView.findViewById(R.id.temImg);
            holder.picImg.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.r = (RelativeLayout) convertView.findViewById(R.id.rBorder);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.r.setVisibility(View.INVISIBLE);



        Picasso.with(mActivity.getApplicationContext())
                .load((String) listdata.get(position).get("image_url_thumbnail"))
                .config(Bitmap.Config.RGB_565)
                .error(R.drawable.no_image)
                .tag(mActivity.getApplicationContext())
                .into(holder.picImg);

        holder.select = (boolean) listdata.get(position).get("select");

        if (!holder.select) {

            holder.r.setVisibility(View.INVISIBLE);

        } else {
            holder.r.setVisibility(View.VISIBLE);

        }


        return convertView;
    }


    public class ViewHolder {
        ImageView picImg;
        RelativeLayout r;
        boolean select;
    }



}

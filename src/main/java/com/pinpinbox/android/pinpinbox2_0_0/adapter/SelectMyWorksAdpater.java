package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2016/5/26.
 */
public class SelectMyWorksAdpater extends BaseAdapter {

    private Activity mActivity;

    private ArrayList<HashMap<String, Object>> listData;

    public SelectMyWorksAdpater(Activity mActivity, ArrayList<HashMap<String, Object>> listData){
        this.mActivity = mActivity;
        this.listData = listData;
    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
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
            convertView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item_2_0_0_contribute, null);
            holder.rContributionstatus = (RelativeLayout)convertView.findViewById(R.id.rContributionstatus);
            holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            holder.coverImg = (ImageView)convertView.findViewById(R.id.coverImg);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

        String cover = (String)listData.get(position).get("cover");
        String name = (String)listData.get(position).get("name");

        boolean contributionstatus = (boolean)listData.get(position).get("contributionstatus");

        if(contributionstatus){
            holder.rContributionstatus.setVisibility(View.VISIBLE);
        }else {
            holder.rContributionstatus.setVisibility(View.INVISIBLE);
        }

        holder.tvName.setText(name);


        if(cover!=null && !cover.equals("") && !cover.equals("null")) {
            Picasso.with(mActivity.getApplicationContext())
                    .load(cover)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.coverImg);
        }else {
            holder.coverImg.setImageResource(R.drawable.bg_2_0_0_no_image);
        }


        return convertView;
    }

    class ViewHolder{

        RelativeLayout rContributionstatus;

        TextView tvName;

        ImageView coverImg;

    }

}

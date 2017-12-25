package com.pinpinbox.android.Activity.RecommendAuthor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.SelfMadeClass.LoadingAnimation;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2015/9/24.
 */
public class RecommendAdapter extends BaseAdapter {

    private Activity mActivity;
    private LoadingAnimation loading;
    private ArrayList<HashMap<String, Object>> listdata;

    private SharedPreferences getdata;
    private String id, token;

    private RecommendAdapter recommendAdapter = this;


    public RecommendAdapter(Activity activity, ArrayList<HashMap<String, Object>> listData) {
        this.mActivity = activity;
        this.listdata = listData;
        getdata = mActivity.getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString("id", "");
        token = getdata.getString("token", "");


        loading = new LoadingAnimation(mActivity);

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_recommend_author, null);
            holder.picfileImg = (RoundedImageView) convertView.findViewById(R.id.item_picfileImg);
            holder.parameter_authorname = (TextView) convertView.findViewById(R.id.item_authorname);
            holder.parameter_count_from = (TextView) convertView.findViewById(R.id.item_count_form);
            holder.parameter_time = (TextView) convertView.findViewById(R.id.item_time);
            holder.tvAttention = (TextView)convertView.findViewById(R.id.tvAttention);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        String url = (String) listdata.get(position).get("picfileurl");

        if (url != null) {
            if (url.equals("null") | url.equals("")) {
                holder.picfileImg.setImageResource(R.drawable.member_back_head);
            } else {

                Picasso.with(mActivity)
                        .load(url)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.member_back_head)
                        .tag(mActivity)
                        .into(holder.picfileImg);

            }
        }

        final String authorid = (String) listdata.get(position).get("authorid");
        String name = (String) listdata.get(position).get("authorname");
        String count_from = (String) listdata.get(position).get("count_from");
        String inserttime = (String) listdata.get(position).get("inserttime");
        String attention = (String) listdata.get(position).get("attention");


        holder.parameter_authorname.setText(name);
        holder.parameter_count_from.setText(count_from);
        holder.parameter_time.setText("加入時間 : " + inserttime);


        final ViewHolder finalHolder = holder;




        return convertView;
    }

    public class ViewHolder {
        RoundedImageView picfileImg;
        TextView parameter_authorname;
        TextView parameter_count_from;
        TextView parameter_time;
        TextView tvAttention;


    }



}

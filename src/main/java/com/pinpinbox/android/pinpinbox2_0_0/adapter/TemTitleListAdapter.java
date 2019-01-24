package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinpinbox.android.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2015/11/24.
 */
public class TemTitleListAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<HashMap<String, Object>> listdata;

    public TemTitleListAdapter(Activity activity, ArrayList<HashMap<String, Object>> listdata) {
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
            convertView = mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_template_title_list, null);
            holder.tvSub = convertView.findViewById(R.id.item_subselect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvSub.setText((String) listdata.get(position).get("name"));
        holder.style_id = (String) listdata.get(position).get("style_id");

        return convertView;
    }

    public class ViewHolder {
        TextView tvSub;
        String style_id;
    }
}

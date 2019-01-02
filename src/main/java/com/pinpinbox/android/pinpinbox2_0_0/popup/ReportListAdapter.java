package com.pinpinbox.android.pinpinbox2_0_0.popup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemReport;

import java.util.List;

/**
 * Created by vmage on 2017/1/18.
 */
public class ReportListAdapter extends BaseAdapter {

    private Activity mActivity;

    private List<ItemReport> itemReportList;

    public ReportListAdapter(Activity mActivity, List<ItemReport> itemReportList){
        this.mActivity = mActivity;
        this.itemReportList = itemReportList;
    }

    @Override
    public int getCount() {
        return itemReportList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemReportList.get(position);
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_2_0_0_poplist, null);
            holder.textView = convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(R.drawable.click_2_0_0_list_item);

        holder.textView.setText(itemReportList.get(position).getName());

        return convertView;
    }


    public class ViewHolder {

        TextView textView;


    }



}

package com.pinpinbox.android.pinpinbox2_0_0.popup;

import android.app.Activity;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2017/1/18.
 */
public class PopListAdapter extends BaseAdapter {

    private Activity mActivity;

    private ArrayList<HashMap<String,Object>> listData;

    public PopListAdapter(Activity mActivity, ArrayList<HashMap<String,Object>>listData){
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

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_2_0_0_poplist, null);
            holder.textView = (TextView)convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(R.drawable.click_2_0_0_list_item);

        TextPaint tp = holder.textView.getPaint();
        tp.setFakeBoldText(true);

        String strName = (String)listData.get(position).get(MapKey.name);

        holder.textView.setText(strName);



        return convertView;
    }


    public class ViewHolder {

        TextView textView;


    }



}

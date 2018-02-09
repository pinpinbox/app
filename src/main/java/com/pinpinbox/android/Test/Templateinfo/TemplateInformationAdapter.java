package com.pinpinbox.android.Test.Templateinfo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vmage on 2015/11/3.
 */
public class TemplateInformationAdapter extends BaseAdapter {
    private ArrayList<HashMap<String, Object>> listdata;
    private Activity mActivity;
    private int mType;
    private DisplayMetrics dm;
    private int w, h;

    public TemplateInformationAdapter(Activity activity, ArrayList<HashMap<String, Object>> listdata, int type) {
        this.mActivity = activity;
        this.listdata = listdata;
        this.mType = type;

        dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        w = dm.widthPixels / 4;
        h = (w / 2) * 3;


    }

    @Override
    public int getCount() {

        if (mType == 0) {
            return listdata.size();
        } else {
            return 5;
        }

//        return listdata.size();
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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_template_pictures, null);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.linear);
//            holder.img_background = (LinearLayout) convertView.findViewById(R.id.item_img_background);
            holder.pictureImg = (ImageView) convertView.findViewById(R.id.item_pic);
            holder.name_author = (TextView) convertView.findViewById(R.id.item_name_author);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pictureImg.setScaleType(ImageView.ScaleType.FIT_XY);


        switch (mType) {
            case 0://typePIC
                holder.layout.removeView(holder.name_author);

                String url = (String) listdata.get(position).get("url");

                Picasso.with(mActivity.getApplicationContext())
                        .load(url)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.bg_2_0_0_no_image)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.pictureImg);



                break;
        }


        return convertView;
    }

    public class ViewHolder {
        LinearLayout layout;
        LinearLayout img_background;
        ImageView pictureImg;
        TextView name_author;

    }


}

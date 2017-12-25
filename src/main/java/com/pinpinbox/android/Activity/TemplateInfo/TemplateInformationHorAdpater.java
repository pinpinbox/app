package com.pinpinbox.android.Activity.TemplateInfo;

import android.app.Activity;
import android.graphics.Bitmap;
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
 * Created by vmage on 2016/2/5.
 */
public class TemplateInformationHorAdpater extends BaseAdapter {

    private ArrayList<HashMap<String, Object>> listdata;
    private Activity mActivity;
    private int mType;

    public TemplateInformationHorAdpater(Activity activity, ArrayList<HashMap<String, Object>> listdata, int mType) {
        this.mActivity = activity;
        this.listdata = listdata;
        this.mType = mType;

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
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_template_pictures_by_other_sample, null);
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
            case 1://typeOther

                String url = (String) listdata.get(position).get("image");

                Picasso.with(mActivity.getApplicationContext())
                        .load(url)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.no_image)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.pictureImg);

                holder.name_author.setText((String) listdata.get(position).get("name"));
                holder.template_id = (String) listdata.get(position).get("template_id");

                break;
            case 2://typeSample

                String cover = (String) listdata.get(position).get("cover");

                Picasso.with(mActivity.getApplicationContext())
                        .load(cover)
                        .config(Bitmap.Config.RGB_565)
                        .error(R.drawable.no_image)
                        .tag(mActivity.getApplicationContext())
                        .into(holder.pictureImg);

                holder.name_author.setText((String) listdata.get(position).get("name"));
                holder.album_id = (String) listdata.get(position).get("album_id");
                break;
        }


        return convertView;
    }

    public class ViewHolder {
        LinearLayout layout;
        LinearLayout img_background;
        ImageView pictureImg;
        TextView name_author;
        String album_id;
        String template_id;
    }


}

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
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemTemplate;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2015/11/2.
 */
public class TemplateListAdapter extends BaseAdapter {
    private Activity mActivity;

    private List<ItemTemplate> templateList;


    public TemplateListAdapter(Activity activity, List<ItemTemplate> templateList) {
        this.mActivity = activity;
        this.templateList = templateList;

    }


    @Override
    public int getCount() {
        return templateList.size();
    }

    @Override
    public Object getItem(int position) {
        return templateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.list_item_2_0_0_template_list, null);
            holder = new ViewHolder();
            holder.imageImg = (ImageView) convertView.findViewById(R.id.imageImg);
            holder.tvTemName = (TextView) convertView.findViewById(R.id.tvTemName);//OK
            holder.tvIsOwn = (TextView) convertView.findViewById(R.id.tvIsOwn);
//            holder.user = (TextView)convertView.findViewById(R.id.template_author);
//            holder.tvTemPoint = (TextView)convertView.findViewById(R.id.tvTemPoint);//OK
//            holder.count = (TextView)convertView.findViewById(R.id.template_count);//OK
//            holder.tvTemDescription = (TextView)convertView.findViewById(R.id.tvTemDescription);//OK

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        convertView.setBackgroundResource(R.drawable.click_2_0_0_staggeredgrid_item);

        TextUtility.setBold(holder.tvTemName, true);

        String image = templateList.get(position).getImage();

        String name = templateList.get(position).getName();


        if (image == null || image.equals("")) {
            holder.imageImg.setImageResource(R.drawable.bg_2_0_0_no_image);
        } else {


            final ViewHolder finalHolder = holder;
            Picasso.with(mActivity.getApplicationContext())
                    .load(image)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(holder.imageImg, new Callback() {
                        @Override
                        public void onSuccess() {
                            finalHolder.imageImg.setAlpha(0.8f);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }


        if (name != null) {
            holder.tvTemName.setText(name);
        }


//        if(description!=null){
//            holder.tvTemDescription.setText(description);
//        }
//        if(user!=null){
//            holder.user.setText(user);
//        }
//
//        if(count!=null){
//            holder.count.setText(count);
//        }


        return convertView;
    }

    public class ViewHolder {
        ImageView imageImg;
        TextView tvTemName;
        TextView tvIsOwn;
//        TextView tvTemDescription;
//        TextView tvTemPoint;
//        TextView count;
//        TextView user;
    }
}

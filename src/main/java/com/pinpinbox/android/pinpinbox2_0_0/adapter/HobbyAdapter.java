package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemHobby;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vmage on 2017/3/8.
 */
public class HobbyAdapter extends BaseAdapter {

    private Activity mActivity;

    private List<ItemHobby> itemHobbyList;

    public HobbyAdapter(Activity mActivity, List<ItemHobby> itemHobbyList) {
        this.mActivity = mActivity;
        this.itemHobbyList = itemHobbyList;

    }


    @Override
    public int getCount() {
        return itemHobbyList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemHobbyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        convertView =mActivity.getLayoutInflater().inflate(R.layout.list_item_2_0_0_hobby, null);

        RoundCornerImageView hobbyImg = convertView.findViewById(R.id.hobbyImg);
        TextView tvName = convertView.findViewById(R.id.tvName);
        LinearLayout linSelect = convertView.findViewById(R.id.linSelect);


        tvName.setText(itemHobbyList.get(position).getName());

        String image_url = itemHobbyList.get(position).getImage_url();

        if (image_url != null && !image_url.equals("") && !image_url.equals("null")) {

            Picasso.get()
                    .load(image_url)
                    .config(Bitmap.Config.RGB_565)
                    .fit()
                    .error(R.drawable.bg_2_0_0_no_image)
                    .tag(mActivity.getApplicationContext())
                    .into(hobbyImg);

        }else {

            hobbyImg.setImageResource(R.drawable.bg_2_0_0_no_image);

        }


        boolean isSelect = itemHobbyList.get(position).isSelect();

        if (isSelect) {

            linSelect.setBackgroundResource(R.drawable.border_2_0_0_click_default_radius);

        } else {

            linSelect.setBackgroundResource(0);

        }


        return convertView;
    }


}

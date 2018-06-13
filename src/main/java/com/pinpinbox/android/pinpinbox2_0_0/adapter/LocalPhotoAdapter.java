package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.StickyGridViewHeader.StickyGridHeadersSimpleAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.GridItem;

import java.io.File;
import java.util.List;

/**
 * Created by kevin9594 on 2017/2/18.
 */
public class LocalPhotoAdapter extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter {

    private Activity mActivity;
    private List<GridItem> hasHeaderIdList;
    private LayoutInflater mInflater;

    private RequestOptions opts;

    public LocalPhotoAdapter(Activity mActivity, List<GridItem> hasHeaderIdList) {
        mInflater = LayoutInflater.from(mActivity);
        this.mActivity = mActivity;
        this.hasHeaderIdList = hasHeaderIdList;

        opts = new RequestOptions()
                .centerCrop()
                .error(R.drawable.bg_2_0_0_no_image);


    }


    @Override
    public int getCount() {
        return hasHeaderIdList.size();
    }

    @Override
    public Object getItem(int position) {
        return hasHeaderIdList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_2_0_0_select_photo, parent, false);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            mViewHolder.vSelect = convertView.findViewById(R.id.vSelect);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            mViewHolder.mImageView.setImageBitmap(null);
        }


        String path = hasHeaderIdList.get(position).getPath();


        if (path != null && !path.equals("")) {
            final File file = new File(path);
            Glide.with(mActivity.getApplicationContext())
                    .load(file)
                    .apply(opts)
                    .into(mViewHolder.mImageView);














        } else {
            mViewHolder.mImageView.setImageResource(R.drawable.bg_2_0_0_no_image);
        }



        boolean b = hasHeaderIdList.get(position).isSelect();
        if (b) {
            mViewHolder.vSelect.setBackgroundResource(R.drawable.ic200_circle_select_light);
            mViewHolder.mImageView.setAlpha(0.4f);
        } else {
            mViewHolder.vSelect.setBackgroundResource(R.drawable.ic200_circle_select_alpha);
            mViewHolder.mImageView.setAlpha(1.0f);
        }

        return convertView;
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderHolder;

        if (convertView == null) {
            mHeaderHolder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_2_0_0_header_date, parent, false);
            mHeaderHolder.mTextView = (TextView) convertView
                    .findViewById(R.id.tvDate);
            convertView.setTag(mHeaderHolder);
        } else {
            mHeaderHolder = (HeaderViewHolder) convertView.getTag();
        }

        TextUtility.setBold(mHeaderHolder.mTextView, true);

        mHeaderHolder.mTextView.setText(hasHeaderIdList.get(position).getTime());

        return convertView;
    }

    /**
     * 获取HeaderId, 只要HeaderId不相等就添加一个Header
     */
    @Override
    public long getHeaderId(int position) {
        return hasHeaderIdList.get(position).getHeaderId();
    }


    public class ViewHolder {
        public ImageView mImageView;
        public View vSelect;
    }

    public class HeaderViewHolder {
        public TextView mTextView;
    }


}

package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.pinpinbox2_0_0.bean.GridItem;
import com.pinpinbox.android.Views.StickyGridViewHeader.StickyGridHeadersSimpleAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.List;

/**
 * Created by vmage on 2017/3/17.
 */
public class LocalVideoAdapter extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter {

    private Activity mActivity;
    private List<GridItem> hasHeaderIdList;
    private LayoutInflater mInflater;
    private GridView mGridView;

    private LruCache<String, Bitmap> lruCache;
    private ContentResolver cr;
    private BitmapFactory.Options opts;

    public LocalVideoAdapter(Activity mActivity, List<GridItem> hasHeaderIdList,
                             GridView mGridView, LruCache<String, Bitmap> lruCache) {
        mInflater = LayoutInflater.from(mActivity);
        this.mActivity = mActivity;
        this.mGridView = mGridView;
        this.hasHeaderIdList = hasHeaderIdList;
        this.lruCache = lruCache;
        cr = mActivity.getContentResolver();
        opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

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
        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_2_0_0_video, parent, false);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            mViewHolder.tvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
            mViewHolder.vSelect = convertView.findViewById(R.id.vSelect);
            convertView.setTag(mViewHolder);


        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            mViewHolder.mImageView.setImageDrawable(null);
        }


        String path = hasHeaderIdList.get(position).getPath();
//        mViewHolder.mImageView.setTag(path);

        if (getVideoThumbToCache(path) == null) {

            MyLog.Set("d", getClass(), "getVideoThumbToCache(path)");

            int id = hasHeaderIdList.get(position).getMedia_id();

            LoadTask task = new LoadTask(id, path, mViewHolder.mImageView);
            task.execute();


        } else {

            MyLog.Set("d", getClass(), "setImageBitmap");

            mViewHolder.mImageView.setImageBitmap(getVideoThumbToCache(path));
        }

        String time = hasHeaderIdList.get(position).getDuration();
        mViewHolder.tvDuration.setText(time);


        boolean b = hasHeaderIdList.get(position).isSelect();
        if (b) {
            mViewHolder.vSelect.setBackgroundResource(R.drawable.icon_select_pink500_120x120);
            mViewHolder.mImageView.setAlpha(0.4f);
        } else {
            mViewHolder.vSelect.setBackgroundResource(R.drawable.icon_unselect_teal500_120x120);
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
        TextPaint tp = mHeaderHolder.mTextView.getPaint();
        tp.setFakeBoldText(true);
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
        public TextView tvDuration;
        public View vSelect;
    }

    public class HeaderViewHolder {
        public TextView mTextView;
    }


    private class LoadTask extends AsyncTask<Void, Void, Object> {

        private String path;
        private ImageView img;
        private Bitmap bitmap;
        private int id;

        public LoadTask(int id, String path, ImageView img) {
            this.img = img;
            this.path = path;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

//            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
            bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr,
                    id, MediaStore.Video.Thumbnails.MICRO_KIND, opts);


            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);


            if (path == null || bitmap == null) {

            } else {

                addVideoThumbToCache(path, bitmap);

                    img.setImageBitmap(bitmap);

            }


//            if (path == null || bitmap == null) {
//
//            } else {
//
//                addVideoThumbToCache(path, bitmap);
//
////                if (img.getTag().equals(path)) {
//                    img.setImageBitmap(bitmap);
////                }
//            }

        }

    }

    public void addVideoThumbToCache(String path, Bitmap bitmap) {
        if (getVideoThumbToCache(path) == null) {
            //当前地址没有缓存时，就添加
            lruCache.put(path, bitmap);
        }
    }

    public Bitmap getVideoThumbToCache(String path) {

        return lruCache.get(path);

    }


}


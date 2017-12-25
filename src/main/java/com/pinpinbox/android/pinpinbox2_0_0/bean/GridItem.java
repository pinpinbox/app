package com.pinpinbox.android.pinpinbox2_0_0.bean;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * Created by kevin9594 on 2017/2/18.
 */
public class GridItem {

    public GridItem() {
        super();
    }


    /**
     * 图片的路径
     */
    private String path;
    /**
     * 图片加入手机中的时间，只取了年月日
     */
    private String time;
    /**
     * 每个Item对应的HeaderId
     */
    private int headerId;

    private boolean isSelect;

    private int media_id;

    private int degree;

    private String duration;

    private String thumbnailsPath;

    public String getThumbnailsPath() {
        return thumbnailsPath;
    }

    public void setThumbnailsPath(String thumbnailsPath) {
        this.thumbnailsPath = thumbnailsPath;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMedia_id() {
        return this.media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }


    private String getOriginalImagePath(Activity mActivity) {
        String path = null;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        Cursor cursor = mActivity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                MediaStore.Images.Media._ID + "=" + getMedia_id(), null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        return path;
    }


}

package com.pinpinbox.android.Widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

/**
 * Created by kevin9594 on 2017/2/18.
 */
public class ImageScanner {
    private Context mContext;

    public ImageScanner(Context context){
        this.mContext = context;
    }

    /**
     * 利用ContentProvider扫描手机中的图片，将扫描的Cursor回调到ScanCompleteCallBack
     * 接口的scanComplete方法中，此方法在运行在子线程中
     */
    public void scanImages(final ScanCompleteCallBack callback) {
        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                callback.scanComplete((Cursor[])msg.obj);
            }
        };

        new Thread(new Runnable() {

            @Override
            public void run() {
                //先发送广播扫描下整个sd卡
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));


//                MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
                ContentResolver mContentResolver = mContext.getContentResolver();

                String[] projection = { MediaStore.Images.Thumbnails.IMAGE_ID,
                        MediaStore.Images.Thumbnails.DATA,};

                Cursor mCursor = mContentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                );


                String[] projection2 ={MediaStore.Images.Media._ID,MediaStore.Images.Media.DATA,MediaStore.Images.ImageColumns.ORIENTATION};
                Cursor dateCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection2,
                        null,
                        null,
                        MediaStore.Images.Media.DATE_ADDED);

                Cursor[] cs = {mCursor, dateCursor};



                //利用Handler通知调用线程
                Message msg = mHandler.obtainMessage();
                msg.obj = cs;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    /**
     * 扫描完成之后的回调接口
     *
     */
    public static interface ScanCompleteCallBack{
        public void scanComplete(Cursor cursor[]);
    }


}
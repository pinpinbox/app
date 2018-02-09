package com.pinpinbox.android.Test.ReadAlbum;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Views.PinchImageView;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2014/12/8
 */
public class PageAdapter extends PagerAdapter {

    private Activity mActivity;

    private BitmapFactory.Options opts;

    private String TAG = this.getClass().getSimpleName();

    private List<File> pathlist;

    private List<AsyncTask> tasks;

    public PageAdapter(Activity activity, List<File> pathlist) {
        this.mActivity = activity;
        this.pathlist = pathlist;

        opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

        tasks = new ArrayList<>();

    }

    public void destroyItem(final ViewGroup view, final int position, final Object object) {

        PinchImageView piv = (PinchImageView) ((View) object).findViewById(R.id.pic);
        piv.reset();
        piv.setImageBitmap(null);

        view.removeView((View) object);
        Picasso.with(mActivity.getApplicationContext()).invalidate(pathlist.get(position));


    }


    //獲得當前介面數量
    @Override
    public int getCount() {
        return pathlist.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        final View v = LayoutInflater.from(mActivity).inflate(R.layout.page_pic, null);

        v.setId(position);

        PinchImageView picImg = (PinchImageView) v.findViewById(R.id.pic);

        if (pathlist.get(position) != null && pathlist.get(position).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(pathlist.get(position).getPath(), opts);
        }

        int bmpw = opts.outWidth;
        int bmph = opts.outHeight;
        int wh = bmpw * bmph;

        MyLog.Set("d", getClass(), bmpw + "x" + bmph + "=" + wh);


        if (picImg != null) {
            if (pathlist != null) {

                if(wh>2400000){
                    MyLog.Set("d", PagerAdapter.class,"resize =>2");
                    Picasso.with(mActivity.getApplicationContext())
                            .load(pathlist.get(position))
                            .config(Bitmap.Config.RGB_565)
                            .resize(bmpw / 2, bmph / 2)
                            .centerInside()
                            .error(R.drawable.bg_2_0_0_no_image)
                            .tag(mActivity.getApplicationContext())
                            .into(picImg);
                }else {
                    MyLog.Set("d", PagerAdapter.class,"resize =>1");
                    Picasso.with(mActivity.getApplicationContext())
                            .load(pathlist.get(position))
                            .config(Bitmap.Config.RGB_565)
                            .resize(bmpw, bmph)
                            .centerInside()
                            .error(R.drawable.bg_2_0_0_no_image)
                            .tag(mActivity.getApplicationContext())
                            .into(picImg);
                }

            }//if(pathlist!=null)
        }



        container.addView(v, 0);


        return v;
    }

    //判斷是否由對象生成介面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);
    }

}

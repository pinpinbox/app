package com.pinpinbox.android.pinpinbox2_0_0.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vmage on 2016/5/24.
 */
public class AdPageAdapter extends PagerAdapter {

    private Activity mActivity;
    private List<View> viewList;
    private ArrayList<HashMap<String, Object>> arrayList;


    public AdPageAdapter(Activity mActivity, List<View> viewList, ArrayList<HashMap<String, Object>> arrayList) {

        this.mActivity = mActivity;
        this.viewList = viewList;
        this.arrayList = arrayList;

    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        container.removeView(viewList.get(position));

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View v = viewList.get(position);

//        ImageView img = (ImageView) v.findViewById(R.id.adImg);

//        Picasso.with(mActivity.getApplicationContext()).
//                load((String) arrayList.get(position).get("image"))
//                .config(Bitmap.Config.RGB_565)
//                .error(R.drawable.bg_2_0_0_no_image)
//                .tag(mActivity.getApplicationContext())
//                .into(img);

//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Bundle bundle = new Bundle();
//
//                Intent intent = new Intent();
//
//                String url = (String) arrayList.get(position).get(Key.url);
//
//                String event_id = (String) arrayList.get(position).get(Key.event_id);
//                String album_id = (String) arrayList.get(position).get(Key.album_id);
//                String template_id = (String) arrayList.get(position).get(Key.template_id);
//                String user_id = (String) arrayList.get(position).get(Key.user_id);
//
//
//                if (album_id != null && !album_id.equals("")) {
////                    bundle.putString(Key.album_id, album_id);
////                    intent.putExtras(bundle);
////                    intent.setClass(mActivity, .class);
////                    mActivity.startActivity(intent);
////                    ActivityAnim.StartAnim(mActivity);
//                    return;
//                }
//
//                if (template_id != null && !template_id.equals("") && !template_id.equals("0")) {
//                    bundle.putString(Key.template_id, template_id);
//                    intent.putExtras(bundle);
//                    intent.setClass(mActivity, TemplateInfoActivity.class);
//                    mActivity.startActivity(intent);
//                    ActivityAnim.StartAnim(mActivity);
//                    return;
//                }
//
//                if (user_id != null && !user_id.equals("")) {
//
////                    bundle.putString(Key.author_id, user_id);
////                    intent.putExtras(bundle);
////                    intent.setClass(mActivity, .class);
////                    mActivity.startActivity(intent);
////                    ActivityAnim.StartAnim(mActivity);
//                    return;
//                }
//
//
//
//                if (event_id !=null && !event_id.equals("")) {
//
//                    bundle.putString(Key.event_id, event_id);
//                    intent.putExtras(bundle);
//                    intent.setClass(mActivity, Event2Activity.class);
//                    mActivity.startActivity(intent);
//                    ActivityAnim.StartAnim(mActivity);
//
//                } else {
//
//                    bundle.putString("url", url);
//                    intent.putExtras(bundle);
//                    intent.setClass(mActivity, WebView2Activity.class);
//                    mActivity.startActivity(intent);
//                    ActivityAnim.StartAnim(mActivity);
//
//                }
//
//
////                Log.d(TAG, (String)arrayList.get(position).get("image"));
////                Log.d(TAG, (String)arrayList.get(position).get("url"));
////                Log.d(TAG, (String)arrayList.get(position).get("event_id"));
////
////
////                String url =(String)arrayList.get(position).get("url");
////                String event_id = (String)arrayList.get(position).get("event_id");
////
////                Bundle bundle = new Bundle();
////                bundle.putString("event_id", event_id);
////
////                Intent intent = new Intent();
////                if(!event_id.equals("")){
////
////                    Log.d(TAG, "!event_id.equals()");
////                    intent.setClass(mActivity, Event2Activity.class);
////
////                }else {
////                    Log.d(TAG, "event_id.equals()");
////                    bundle.putString("url", url);
////                    intent.setClass(mActivity, WebView2Activity.class);
////
////                }
//
//
//
//
//            }
//        });
//
//        container.addView(viewList.get(position));
        return viewList.get(position);

    }


    @Override
    public int getCount() {
        return viewList.size();
    }

    //判斷是否由對象生成介面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;

    }
}

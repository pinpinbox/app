package com.pinpinbox.android.Test.CreateAlbum;


import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by vmage on 2015/10/30.
 */
public class TemplatePageAdapter extends PagerAdapter {

    private Activity mActivity;
    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;
    private TemplateHot templateHot;
    private TemplateFree templateFree;
    private TemplateSponsored templateSponsored;
    private TemplateOwn templateOwn;

    private View v0, v1, v2, v3;
    private List<View> mViewList;

    public TemplatePageAdapter(List<View> viewList, Activity activity, FragmentManager fragmentManager, List<Fragment> fragmentList, TemplateHot templateHot, TemplateFree templateFree, TemplateSponsored templateSponsored, TemplateOwn templateOwn) {
        this.mActivity = activity;
        this.fragmentManager = fragmentManager;
        this.fragmentList = fragmentList;
        this.templateHot = templateHot;
        this.templateFree = templateFree;
        this.templateSponsored = templateSponsored;
        this.templateOwn = templateOwn;
        this.mViewList = viewList;

        v0 = mViewList.get(0);
        v1 = mViewList.get(1);
        v2 = mViewList.get(2);
        v3 = mViewList.get(3);



    }

    public void destroyItem(ViewGroup view, int position, Object object) {

        view.removeView((View)object);



    }


    //獲得當前介面數量
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    // 初始化arg0
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        int a = position % fragmentList.size();

        switch (a) {
            case 0:
                if(!templateHot.isAdded()){

//                    FrameLayout frameLayout = (FrameLayout)v0.findViewById(R.id.content);
//                    fragmentManager.beginTransaction()
//                            .add(frameLayout.getId() , templateHot , TagClass.TagTemplateHot).commit();
                }



//                fragmentManager.beginTransaction().show(fragmentList.get(a)).commit();

            case 1:
                if(!templateFree.isAdded()){

//                    FrameLayout frameLayout = (FrameLayout)v1.findViewById(R.id.content);
//                    fragmentManager.beginTransaction()
//                            .add(frameLayout.getId() ,templateFree , TagClass.TagTemplateFree).commit();
                }


            case 2:
                if(!templateSponsored.isAdded()){

//                    FrameLayout frameLayout = (FrameLayout)v2.findViewById(R.id.content);
//                    fragmentManager.beginTransaction()
//                            .add(frameLayout.getId() , templateSponsored, TagClass.TagTemplateSponsored).commit();
                }


            case 3:

                if(!templateOwn.isAdded()){
//
//                    FrameLayout frameLayout = (FrameLayout)v3.findViewById(R.id.content);
//                    fragmentManager.beginTransaction()
//                            .add(frameLayout.getId() ,templateOwn , TagClass.TagTemplateOwn).commit();
                }
        }

        if(a==0){
            return v0;
        }else if(a==1){
            return v1;
        }else if (a==2){
            return v2;
        }else if (a==3){
            return v3;
        }else {
            return null;
        }
//        return v;
    }

    //判斷是否由對象生成介面
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return (arg0 == arg1);

    }

}

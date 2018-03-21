package com.pinpinbox.android.pinpinbox2_0_0.custom.manager;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Main2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.fragment.FragmentMe2;

/**
 * Created by kevin9594 on 2018/3/17.
 */

public class RedPointManager {

    public static void showOrHideOnMenu(boolean showOrHide){
        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkRP_menu, showOrHide).commit();

        if(showOrHide){
            Activity mActivity = SystemUtility.getActivity(Main2Activity.class.getSimpleName());
            if(mActivity!=null){
                Fragment fragment = ((Main2Activity)mActivity).getFragment(FragmentMe2.class.getSimpleName());
                if(fragment!=null){
                    ((FragmentMe2)fragment).showRP_menu();
                }
            }
        }
    }


    public static void showOrHideOnEditProfile(boolean showOrHide){
        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkRP_editProfile, showOrHide).commit();
        if(showOrHide){
            showOrHideOnMenu(true);
        }
    }

    public static void showOrHideOnWorkManage(boolean showOrHide){
        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkRP_workManage, showOrHide).commit();
        if(showOrHide){
            showOrHideOnMenu(true);
        }
    }

    public static void showOrHideOnMyFollow(boolean showOrHide){
        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkRP_myFollow, showOrHide).commit();
        if(showOrHide){
            showOrHideOnMenu(true);
        }

    }

    public static void showOrHideOnRecent(boolean showOrHide){
        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkRP_recent, showOrHide).commit();
        if(showOrHide){
            showOrHideOnMenu(true);
        }

    }

    public static void showOrHideOnBuyPoint(boolean showOrHide){
        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkRP_butPoint, showOrHide).commit();
        if(showOrHide){
            showOrHideOnMenu(true);
        }

    }

    public static void showOrHideOnExchangeList(boolean showOrHide){
        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkRP_exchangeList, showOrHide).commit();
        if(showOrHide){
            showOrHideOnMenu(true);
        }

    }

    public static void showOrHideOnSettings(boolean showOrHide){
        PPBApplication.getInstance().getData().edit().putBoolean(Key.checkRP_settings, showOrHide).commit();
        if(showOrHide){
            showOrHideOnMenu(true);
        }

    }


}

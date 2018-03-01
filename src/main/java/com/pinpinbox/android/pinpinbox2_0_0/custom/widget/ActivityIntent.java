package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumInfo2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Author2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CategoryContents2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Event2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeList2Activity;

/**
 * Created by vmage on 2018/2/21.
 */

public class ActivityIntent {


    public static void toAlbumInfo(Activity currentActivity, boolean sharedElement, String album_id, String coverUrl, int imageOrientation, View coverImg){


        Bundle bundle = new Bundle();

        bundle.putString(Key.album_id, album_id);
        bundle.putBoolean(Key.shareElement, sharedElement);

        if (SystemUtility.Above_Equal_V5() && sharedElement) {

            bundle.putString(Key.cover, coverUrl);
            bundle.putInt(Key.image_orientation, imageOrientation);

            coverImg.setTransitionName(coverUrl);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(
                            currentActivity,
                            coverImg,
                            ViewCompat.getTransitionName(coverImg)
                    );

            currentActivity.startActivity(
                    new Intent(currentActivity, AlbumInfo2Activity.class).putExtras(bundle), options.toBundle()
            );


        }else {

            currentActivity.startActivity(
                    new Intent(currentActivity, AlbumInfo2Activity.class).putExtras(bundle)
            );
            ActivityAnim.StartAnimFromBottom(currentActivity);


        }


    }

    public static void toUser(Activity currentActivity, boolean sharedElement, String user_id, String picture, String name, View userImg) {

        Bundle bundle = new Bundle();

        bundle.putString(Key.author_id, user_id);
        bundle.putBoolean(Key.shareElement, sharedElement);

        if (SystemUtility.Above_Equal_V5() && sharedElement) {

            bundle.putString(Key.picture, picture);
            bundle.putString(Key.name, name);

            userImg.setTransitionName(picture);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(
                            currentActivity,
                            userImg,
                            ViewCompat.getTransitionName(userImg));
            currentActivity.startActivity(
                    new Intent(currentActivity, Author2Activity.class).putExtras(bundle), options.toBundle()
            );


        } else {

            currentActivity.startActivity(
                    new Intent(currentActivity, Author2Activity.class).putExtras(bundle)
            );
            ActivityAnim.StartAnim(currentActivity);


        }


    }

    public static void toEvent(Activity currentActivity, String event_id){

        Bundle bundle = new Bundle();
        bundle.putString(Key.event_id, event_id);

        currentActivity.startActivity(
                new Intent(currentActivity, Event2Activity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toExchangeList(Activity currentActivity){


        currentActivity.startActivity(
                new Intent(currentActivity, ExchangeList2Activity.class)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toCategoryArea(Activity currentActivity, int categoryarea_id) {

        Bundle bundle = new Bundle();
        bundle.putInt(Key.categoryarea_id, categoryarea_id);

        currentActivity.startActivity(
                new Intent(currentActivity, CategoryContents2Activity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }


    public static void toFeature(Activity currentActivity, int categoryarea_id) {


    }


}

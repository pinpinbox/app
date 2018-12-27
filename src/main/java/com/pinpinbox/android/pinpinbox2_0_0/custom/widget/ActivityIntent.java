package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumInfoActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumSponsorListActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AppSettingsActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AuthorActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.BuyPointActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CategoryAllActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CategoryBookCaseActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.CreationActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.EditProfileActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.EventActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeListActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.FollowMeActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.LikeListActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyCollectActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.MyFollowActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.RecentAlbumActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.SponsorListActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.VideoPlayActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebViewActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.YouTubeActivity;

/**
 * Created by vmage on 2018/2/21.
 */

public class ActivityIntent {


    public static void toAlbumInfo(Activity currentActivity, boolean sharedElement, String album_id, String coverUrl, int imageOrientation, int width, int height, View coverImg) {


        Bundle bundle = new Bundle();

        bundle.putString(Key.album_id, album_id);
        bundle.putBoolean(Key.shareElement, sharedElement);

        if (SystemUtility.Above_Equal_V5() && sharedElement) {

            bundle.putString(Key.cover, coverUrl);
            bundle.putInt(Key.image_orientation, imageOrientation);
            bundle.putInt(Key.image_width, width);
            bundle.putInt(Key.image_height, height);

            coverImg.setTransitionName(coverUrl);

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(
                            currentActivity,
                            coverImg,
                            ViewCompat.getTransitionName(coverImg)
                    );

            currentActivity.startActivity(
                    new Intent(currentActivity, AlbumInfoActivity.class).putExtras(bundle), options.toBundle()
            );


        } else {

            currentActivity.startActivity(
                    new Intent(currentActivity, AlbumInfoActivity.class).putExtras(bundle)
            );
            ActivityAnim.StartAnimFromBottom(currentActivity);


        }


    }

    public static void toUser(Activity currentActivity, boolean sharedElement, boolean openBoard, String user_id, String picture, String name, View userImg) {

        Bundle bundle = new Bundle();

        bundle.putString(Key.author_id, user_id);
        bundle.putBoolean(Key.shareElement, sharedElement);
        bundle.putBoolean(Key.pinpinboard, openBoard);

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
                    new Intent(currentActivity, AuthorActivity.class).putExtras(bundle), options.toBundle()
            );

        } else {
            currentActivity.startActivity(
                    new Intent(currentActivity, AuthorActivity.class).putExtras(bundle)
            );
            ActivityAnim.StartAnim(currentActivity);
        }

    }

    public static void toEvent(Activity currentActivity, String event_id) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.event_id, event_id);

        currentActivity.startActivity(
                new Intent(currentActivity, EventActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toExchangeList(Activity currentActivity) {


        currentActivity.startActivity(
                new Intent(currentActivity, ExchangeListActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toCategoryAll(Activity currentActivity, int categoryarea_id, String categoryarea_name) {

        Bundle bundle = new Bundle();
        bundle.putInt(Key.categoryarea_id, categoryarea_id);
        bundle.putString(Key.categoryarea_name, categoryarea_name);

        currentActivity.startActivity(
                new Intent(currentActivity, CategoryAllActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toFeature(Activity currentActivity, int categoryarea_id) {

        Bundle bundle = new Bundle();
        bundle.putInt(Key.categoryarea_id, categoryarea_id);

        currentActivity.startActivity(
                new Intent(currentActivity, CategoryBookCaseActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toVideoPlay(Activity currentActivity, String path_or_url) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.path_or_url, path_or_url);

        currentActivity.startActivity(
                new Intent(currentActivity, VideoPlayActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toSettings(Activity currentActivity) {

        currentActivity.startActivity(
                new Intent(currentActivity, AppSettingsActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);


    }

    public static void toWeb(Activity currentActivity, String url, String title) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.url, url);
        bundle.putString(Key.title, title);

        currentActivity.startActivity(
                new Intent(currentActivity, WebViewActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toFollowMe(Activity currentActivity) {

        currentActivity.startActivity(
                new Intent(currentActivity, FollowMeActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toSponsorList(Activity currentActivity) {

        currentActivity.startActivity(
                new Intent(currentActivity, SponsorListActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toAlbumSponsorList(Activity currentActivity, String album_id) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);

        currentActivity.startActivity(
                new Intent(currentActivity, AlbumSponsorListActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toLikesList(Activity currentActivity, String album_id) {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);

        currentActivity.startActivity(
                new Intent(currentActivity, LikeListActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }

    public static void toEditProfile(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, EditProfileActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toWorkManage(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, MyCollectActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toMyFollow(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, MyFollowActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toRecent(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, RecentAlbumActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toBuyPoint(Activity currentActivity) {
        currentActivity.startActivity(
                new Intent(currentActivity, BuyPointActivity.class)
        );
        ActivityAnim.StartAnim(currentActivity);
    }

    public static void toCreation(Activity currentActivity, String album_id, String identity, int template_id, boolean newCreate, boolean fromLocal){

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);
        bundle.putString(Key.identity, identity);

        if (template_id == 0) {
            bundle.putInt(Key.create_mode, 0);
        } else {
            bundle.putInt(Key.create_mode, 1);
        }

        bundle.putBoolean(Key.isNewCreate, newCreate);
        bundle.putBoolean(Key.fromlocal, fromLocal);

        currentActivity.startActivity(
                new Intent(currentActivity, CreationActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);


    }


    public static void toYouTube(Activity currentActivity, String url){

        Bundle bundle = new Bundle();
        bundle.putString("path", url);
        currentActivity.startActivity(
                new Intent(currentActivity, YouTubeActivity.class).putExtras(bundle)
        );
        ActivityAnim.StartAnim(currentActivity);

    }


    public static void toAppSetting(Activity currentActivity) {
        currentActivity.startActivity(new Intent(currentActivity, AppSettingsActivity.class));
        ActivityAnim.StartAnim(currentActivity);
    }


}

package com.pinpinbox.android.StringClass;

import android.os.Environment;

/**
 * Created by vmage on 2015/4/20
 */
public class DirClass {

    public static final String sdPath = Environment.getExternalStorageDirectory() + "/";
    public static final String dirAlbumList = "albumlist/";
    public static final String dirZip = "zip/";
    public static final String dirCopy = "copy/";
    public static final String dirVideo = "video/";
    public static final String dirMP4 = "my_video/";
    public static final String dirJPG = "my_video_screenshot/";
    public static final String dirHead = "HeadPic/";
    public static final String dirCamera = "ppb_camera/";

    public static final String pathName_headpicture = "pinpinbox_memberheadpic.jpg";
    public static final String pathName_FromAviary = "elect.jpg";
    public static final String pathName_RecordMp3 = "recording.mp3";
    public static final String pathName_UserBanner = "userbanner.jpg";

    public static final String pathHeaderPicture = sdPath + dirHead + pathName_headpicture;

    public static final String pathUserBanner = sdPath + pathName_UserBanner;

    public static final String test = sdPath + "aaaaaaaaaaaaaaaaaaaaaaaaaaa" +pathName_UserBanner;

}

package com.pinpinbox.android.pinpinbox2_0_0.protocol;

import com.pinpinbox.android.pinpinbox2_0_0.mode.TestMode;

/**
 * Created by vmage on 2017/7/7.
 */
public class Url {

    public static String domain = TestMode.domain();

    public static String P13_BuyAlbum = domain + "/index/api/buyalbum" + "/2.0";

    public static String P21_UpdateUser = domain + "/index/api/updateuser" + "/2.0";

    public static String P33_AlbumSettings = domain + "/index/api/albumsettings" + "/2.0";


    public static String P42_GetPhotoUseFor_User = domain + "/index/api/getphotousefor_user" + "/1.3";

    public static String P43_UpdataPhotoUserFor_User = domain + "/index/api/updatephotousefor_user" + "/1.1";


    public static String P95_RefreshToken = domain + "/index/api/refreshtoken" + "/2.0";

    public static String P96_InsertAlbumIndex = domain + "/index/api/insertalbumindex" + "/2.0";

    public static String P97_DeleteAlbumIndex = domain + "/index/api/deletealbumindex" + "/2.0";

    public static String P98_BusinessRegister = domain + "/index/api/businesssubuserfastregister" + "/2.0";

    public static String P99_GetEventVoteList = domain + "/index/api/geteventvotelist" + "/2.0";

    public static String P100_Vote = domain + "/index/api/vote" + "/2.0";

    public static String P101_SetUserCover = domain + "/index/api/setusercover" + "/2.0";

    public static String P102_GetCategoryArea = domain + "/index/api/getcategoryarea" + "/2.0";

    public static String P103_GetThemeArea = domain + "/index/api/getthemearea" + "/2.0";

    public static String P104_GetSponsorList = domain + "/index/api/getsponsorlist" + "/2.0";

    public static String P105_GetLikesList = domain + "/index/api/getalbum2likeslist" + "/2.0";



}

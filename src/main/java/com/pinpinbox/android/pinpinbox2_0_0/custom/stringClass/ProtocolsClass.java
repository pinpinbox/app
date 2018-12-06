package com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass;

import com.pinpinbox.android.BuildConfig;

/**
 * Created by vmage on 2015/3/13
 */
public class ProtocolsClass {
//    http://platformvmage5.cloudapp.net/pinpinbox      /index/api/login
//    http://w3.pinpinbox.com                           /index/api/login


    public static String version = "/1.1";

    public static String P01_Login = BuildConfig.initAPI + "/index/api/login" + "/1.2";

    public static String P02_CheckToken = BuildConfig.initAPI + "/index/api/checktoken" + version;

    public static String P03_RequestSmsPassword = BuildConfig.initAPI + "/index/api/requestsmspwd" + version;

    public static String P04_Registration = BuildConfig.initAPI + "/index/api/registration" + "/1.2";

    public static String P05_UpdateProfilePic = BuildConfig.initAPI + "/index/api/updateprofilepic" + version;

    public static String P06_UpdateProfileHobby = BuildConfig.initAPI + "/index/api/updateprofilehobby" + version;

    public static String P07_RetrievePassword = BuildConfig.initAPI + "/index/api/retrievepassword" + "/1.2";

    public static String P08_RetrieveAlbumProfile = BuildConfig.initAPI + "/index/api/retrievealbump" + "/1.2"; //2016.04.21

    public static String P09_RetrieveCategoryList = BuildConfig.initAPI + "/index/api/retrievecatgeorylist" + "/1.2";

    public static String P10_RetrieveRankList = BuildConfig.initAPI + "/index/api/retrievehotrank" + "/1.2";

    public static String P11_RetrieveAuthor = BuildConfig.initAPI + "/index/api/retrieveauthor" + version;

    public static String P12_ChangeFollowStatus = BuildConfig.initAPI + "/index/api/changefollowstatus" + version;

    public static String P13_BuyTheAlbum = BuildConfig.initAPI + "/index/api/buyalbum" + version;

    public static String P14_DownloadAlbumZipFile = BuildConfig.initAPI + "/index/api/downloadalbumzip" + version;

    public static String P15_FinishGetAlbum = BuildConfig.initAPI + "/index/api/finishalbum" + version;

    public static String P16_DelAlbum = BuildConfig.initAPI + "/index/api/delalbum" + version;

    public static String P17_GetCloudAlbumList = BuildConfig.initAPI + "/index/api/getcalbumlist" + "/1.3"; //2016.03.17

    public static String P18_GetTheDownloadList = BuildConfig.initAPI + "/index/api/getdownloadlist" + version;

    public static String P19_DelDownloadList = BuildConfig.initAPI + "/index/api/deldownloadlist" + version;

    public static String P20_GetUpdateList = BuildConfig.initAPI + "/index/api/getupdatelist" + "/1.2";

    public static String P21_UpdateProfile = BuildConfig.initAPI + "/index/api/updateprofile" + version;

    public static String P22_UpdatePassword = BuildConfig.initAPI + "/index/api/updatepwd" + version;

    public static String P23_GetUserPoints = BuildConfig.initAPI + "/index/api/geturpoints" + version;

    public static String P24_GetPointStore = BuildConfig.initAPI + "/index/api/getpointstore" + version;

    public static String P28_Getprofile = BuildConfig.initAPI + "/index/api/getprofile" + version;

    public static String P29_GetPayload = BuildConfig.initAPI + "/index/api/getpayload" + version;

    public static String P30_Finishpurchased = BuildConfig.initAPI + "/index/api/finishpurchased" + "/1.2";

    public static String P31_Retrievealbumpbypn = BuildConfig.initAPI + "/index/api/retrievealbumpbypn" + version;

    public static String P32_GetAlbumdataOptions = BuildConfig.initAPI + "/index/api/getalbumdataoptions" + version;

    public static String P33_AlbumSettings = BuildConfig.initAPI + "/index/api/albumsettings" + "/1.2"; //2016.02.25 修改為1.2

    public static String P34_GetAlbumSettings = BuildConfig.initAPI + "/index/api/getalbumsettings" + "/2.0";

    public static String P35_FacebookLogin = BuildConfig.initAPI + "/index/api/facebooklogin" + version;

    public static String P36_GetTemplateList = BuildConfig.initAPI + "/index/api/gettemplatelist" + version;

    public static String P37GetTemplate = BuildConfig.initAPI + "/index/api/gettemplate" + version;

    public static String P38BuyTemplate = BuildConfig.initAPI + "/index/api/buytemplate" + version;

    public static String P40_GetCreative = BuildConfig.initAPI + "/index/api/getcreative" + version;

    public static String P41_Search = BuildConfig.initAPI + "/index/api/search" + version;

    public static String P42_GetPhotoUseFor_User = BuildConfig.initAPI + "/index/api/getphotousefor_user" + "/1.2";

    public static String P43_UpDatePhotoUseFor_User = BuildConfig.initAPI + "/index/api/updatephotousefor_user" + version;

    public static String P44_GetCooperationList = BuildConfig.initAPI + "/index/api/getcooperationlist" + version;

    public static String P45_DeleteCooperation = BuildConfig.initAPI + "/index/api/deletecooperation" + version;

    public static String P46_InsertCooperation = BuildConfig.initAPI + "/index/api/insertcooperation" + version;

    public static String P47_GetRecommendedAuthor = BuildConfig.initAPI + "/index/api/getrecommendedauthor" + version;

    public static String P48_GetTemplateStyleList = BuildConfig.initAPI + "/index/api/gettemplatestylelist" + version;

    public static String P50_SetAwsSns = BuildConfig.initAPI + "/index/api/setawssns" + version;

    public static String P51_GetSettings = BuildConfig.initAPI + "/index/api/getsettings" + "/1.2";

    public static String P52_Check = BuildConfig.initAPI + "/index/api/check" + version;

    public static String P53_UpdateCellphone = BuildConfig.initAPI + "/index/api/updatecellphone" + version;

    public static String P54_InsertAlbumOfDiy = BuildConfig.initAPI + "/index/api/insertalbumofdiy" + version;

    public static String P55_UpDateAlbumOfDiy = BuildConfig.initAPI + "/index/api/updatealbumofdiy" + version;

    //    public static String P57_GetAlbumOfDiy = domain + "/index/api/getalbumofdiy" + "/1.2";//2016.04.07
    public static String P57_GetAlbumOfDiy = BuildConfig.initAPI + "/index/api/getalbumofdiy" + version;//2016.04.07

    public static String P58_InsertPhotoOfDiy = BuildConfig.initAPI + "/index/api/insertphotoofdiy" + "/1.2";

    public static String P59_UpDatePhotoOfDiy = BuildConfig.initAPI + "/index/api/updatephotoofdiy" + "/1.2";

    public static String P60_DeletePhotoOfDiy = BuildConfig.initAPI + "/index/api/deletephotoofdiy" + "/1.2";

    public static String P62_SortPhotoOfDiy = BuildConfig.initAPI + "/index/api/sortphotoofdiy" + "/1.3"; //2016.06.23 升版

    public static String P63_UpDateCooperation = BuildConfig.initAPI + "/index/api/updatecooperation" + version;

    public static String P64_CheckNoticeQueue = BuildConfig.initAPI + "/index/api/checknoticequeue" + version;

    public static String P65_HideAlbumQueue = BuildConfig.initAPI + "/index/api/hidealbumqueue" + version;

    public static String P67_CheckAlbumOfDiy = BuildConfig.initAPI + "/index/api/checkalbumofdiy" + "/1.2"; //2016.04.14

    public static String P68_CheckAlbumZip = BuildConfig.initAPI + "/index/api/checkalbumzip" + version;

    public static String P69_GetReportintentlist = BuildConfig.initAPI + "/index/api/getreportintentlist" + "/1.2";

    public static String P70_InsertReport = BuildConfig.initAPI + "/index/api/insertreport" + "/1.2";

    public static String P71_GetInFoofdiy = BuildConfig.initAPI + "/index/api/getinfoofdiy" + "/1.2";

    public static String P73_SwitchStatusOfContribution = BuildConfig.initAPI + "/index/api/switchstatusofcontribution" + "/1.2";

    public static String P74_SwitchStatusOfVote = BuildConfig.initAPI + "/index/api/switchstatusofvote" + "/1.2";

    public static String P75_GetAdList = BuildConfig.initAPI + "/index/api/getadlist" + "/1.3";

    public static String P76_GetEvent = BuildConfig.initAPI + "/index/api/getevent" + "/1.2";

    public static String P77_InsertAudioOfDiy = BuildConfig.initAPI + "/index/api/insertaudioofdiy" + "/1.2";

    public static String P78_UpdateAudioOfDiy = BuildConfig.initAPI + "/index/api/updateaudioofdiy" + "/1.2";

    public static String P79_DeleteAudioOfDiy = BuildConfig.initAPI + "/index/api/deleteaudioofdiy" + "/1.2";

    public static String P80_InsertVideoOfDiy = BuildConfig.initAPI + "/index/api/insertvideoofdiy" + "/1.2";

    public static String P81_UpdateVideoOfDiy = BuildConfig.initAPI + "/index/api/updatevideoofdiy" + "/1.2";

    public static String P82_DeleteVideoOfDiy = BuildConfig.initAPI + "/index/api/deletevideoofdiy" + "/1.2";

    public static String P83_DoTask = BuildConfig.initAPI + "/index/api/dotask" + "/1.3";

    public static String P84_CheckTaskCompleted = BuildConfig.initAPI + "/index/api/checktaskcompleted" + "/1.3";

    public static String p85_GetFollowToList = BuildConfig.initAPI + "/index/api/getfollowtolist" + "/1.3";

    public static String P86_GetRecommendedList = BuildConfig.initAPI + "/index/api/getrecommendedlist" + "/1.3";

    public static String p87_GetPushQueue = BuildConfig.initAPI + "/index/api/getpushqueue" + "/1.0";

    public static String p88_CheckUpDateVersion = BuildConfig.initAPI + "/index/api/checkupdateversion" + "/1.0";

    public static String P89_GetQRCode = BuildConfig.initAPI + "/index/api/getqrcode" + "/1.0";

    public static String P90_GetMessageBoardList = BuildConfig.initAPI + "/index/api/getmessageboardlist" + "/1.3";

    public static String P91_InsertMessageBoard = BuildConfig.initAPI + "/index/api/insertmessageboard" + "/1.3";

    public static String P92_InsertAlbum2Likes = BuildConfig.initAPI + "/index/api/insertalbum2likes" + "/1.3";

    public static String P93_DeleteAlbum2Likes = BuildConfig.initAPI + "/index/api/deletealbum2likes" + "/1.3";

    public static String P94_GetHobbyList = BuildConfig.initAPI + "/index/api/gethobbylist" + "/1.3";


}

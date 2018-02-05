package com.pinpinbox.android.StringClass;

import com.pinpinbox.android.BuildConfig;
import com.pinpinbox.android.pinpinbox2_0_0.mode.TestMode;

/**
 * Created by vmage on 2015/3/13
 */
public class ProtocolsClass {
//    http://platformvmage3.cloudapp.net/pinpinbox      /index/api/login
//    http://w3.pinpinbox.com                           /index/api/login


//    public static String domain = UrlClass.platformvmage5;
//    public static String domain = UrlClass.testW3domain;
//    public static String domain = UrlClass.domain;


    public static String https_domain = "https://www.pinpinbox.com";
    public static String w3https_domain = "https://w3.pinpinbox.com";

    public static String domain = TestMode.domain();

    public static String version = "/1.1";

    //    public static String P01_Login = domain + "/index/api/login" + "/1.2";
    public static String P01_Login() {
        String strDomain = "";


        if (BuildConfig.FLAVOR.equals("w3_private")) {
            strDomain = domain + "/index/api/login" + "/1.2";
        } else if (BuildConfig.FLAVOR.equals("www_private")) {
            strDomain = https_domain + "/index/api/login" + "/1.2";
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            strDomain = https_domain + "/index/api/login" + "/1.2";
        }


        return strDomain;
    }

    public static String P02_CheckToken = domain + "/index/api/checktoken" + version;

    public static String P03_RequestSmsPassword = domain + "/index/api/requestsmspwd" + version;

    //    public static String P04_Registration = domain +"/index/api/registration" + "/1.2"; //2016.04.29
    public static String P04_Registration() {
        String strDomain = "";

//        if (TestMode.TESTMODE) {
//            //test
//            strDomain = domain + "/index/api/registration" + "/1.2";
//        } else {
//            strDomain = https_domain + "/index/api/registration" + "/1.2";
//        }



        if (BuildConfig.FLAVOR.equals("w3_private")) {
            strDomain = domain + "/index/api/registration" + "/1.2";
        } else if (BuildConfig.FLAVOR.equals("www_private")) {
            strDomain = https_domain + "/index/api/registration" + "/1.2";
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            strDomain = https_domain + "/index/api/registration" + "/1.2";
        }

        return strDomain;

    }

    public static String P05_UpdateProfilePic = domain + "/index/api/updateprofilepic" + version;

    public static String P06_UpdateProfileHobby = domain + "/index/api/updateprofilehobby" + version;

    //    public static String P07_RetrievePassword = domain + "/index/api/retrievepassword" + version;
    public static String P07_RetrievePassword() {

        String strDomain = "";


        if (BuildConfig.FLAVOR.equals("w3_private")) {
            strDomain = domain + "/index/api/retrievepassword" + "/1.2";
        } else if (BuildConfig.FLAVOR.equals("www_private")) {
            strDomain = https_domain + "/index/api/retrievepassword" + "/1.2";
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            strDomain = https_domain + "/index/api/retrievepassword" + "/1.2";
        }

        return strDomain;


    }

    public static String P08_RetrieveAlbumProfile = domain + "/index/api/retrievealbump" + "/1.2"; //2016.04.21

    public static String P09_RetrieveCategoryList = domain + "/index/api/retrievecatgeorylist" + "/1.2";

    public static String P10_RetrieveRankList = domain + "/index/api/retrievehotrank" + "/1.2";

    public static String P11_RetrieveAuthor = domain + "/index/api/retrieveauthor" + version;

    public static String P12_ChangeFollowStatus = domain + "/index/api/changefollowstatus" + version;

    public static String P13_BuyTheAlbum = domain + "/index/api/buyalbum" + version;

    public static String P14_DownloadAlbumZipFile = domain + "/index/api/downloadalbumzip" + version;

    public static String P15_FinishGetAlbum = domain + "/index/api/finishalbum" + version;

    public static String P16_DelAlbum = domain + "/index/api/delalbum" + version;

    public static String P17_GetCloudAlbumList = domain + "/index/api/getcalbumlist" + "/1.3"; //2016.03.17

    public static String P18_GetTheDownloadList = domain + "/index/api/getdownloadlist" + version;

    public static String P19_DelDownloadList = domain + "/index/api/deldownloadlist" + version;

    public static String P20_GetUpdateList = domain + "/index/api/getupdatelist" + "/1.2";

    public static String P21_UpdateProfile = domain + "/index/api/updateprofile" + version;

    //    public static String P22_UpdatePassword = domain + "/index/api/updatepwd" + version;
    public static String P22_UpdatePassword() {

        String strDomain = "";


        if (BuildConfig.FLAVOR.equals("w3_private")) {
            strDomain = domain + "/index/api/updatepwd" + "/1.2";
        } else if (BuildConfig.FLAVOR.equals("www_private")) {
            strDomain = https_domain + "/index/api/updatepwd" + "/1.2";
        } else if (BuildConfig.FLAVOR.equals("www_public")) {
            strDomain = https_domain + "/index/api/updatepwd" + "/1.2";
        }


        return strDomain;


    }

    public static String P23_GetUserPoints = domain + "/index/api/geturpoints" + version;

    public static String P24_GetPointStore = domain + "/index/api/getpointstore" + version;

    public static String P28_Getprofile = domain + "/index/api/getprofile" + version;

    public static String P29_GetPayload = domain + "/index/api/getpayload" + version;

    public static String P30_Finishpurchased = domain + "/index/api/finishpurchased" + "/1.2";

    public static String P31_Retrievealbumpbypn = domain + "/index/api/retrievealbumpbypn" + version;

    public static String P32_GetAlbumdataOptions = domain + "/index/api/getalbumdataoptions" + version;

    public static String P33_AlbumSettings = domain + "/index/api/albumsettings" + "/1.2"; //2016.02.25 修改為1.2

    public static String P34_GetAlbumSettings = domain + "/index/api/getalbumsettings" + version;

    public static String P35_FacebookLogin = domain + "/index/api/facebooklogin" + version;

    public static String P36_GetTemplateList = domain + "/index/api/gettemplatelist" + version;

    public static String P37GetTemplate = domain + "/index/api/gettemplate" + version;

    public static String P38BuyTemplate = domain + "/index/api/buytemplate" + version;

    public static String P40_GetCreative = domain + "/index/api/getcreative" + version;

    public static String P41_Search = domain + "/index/api/search" + version;

    public static String P42_GetPhotoUseFor_User = domain + "/index/api/getphotousefor_user" + "/1.2";

    public static String P43_UpDatePhotoUseFor_User = domain + "/index/api/updatephotousefor_user" + version;

    public static String P44_GetCooperationList = domain + "/index/api/getcooperationlist" + version;

    public static String P45_DeleteCooperation = domain + "/index/api/deletecooperation" + version;

    public static String P46_InsertCooperation = domain + "/index/api/insertcooperation" + version;

    public static String P47_GetRecommendedAuthor = domain + "/index/api/getrecommendedauthor" + version;

    public static String P48_GetTemplateStyleList = domain + "/index/api/gettemplatestylelist" + version;

    public static String P50_SetAwsSns = domain + "/index/api/setawssns" + version;

    public static String P51_GetSettings = domain + "/index/api/getsettings" + "/1.2";

    public static String P52_Check = domain + "/index/api/check" + version;

    public static String P53_UpdateCellphone = domain + "/index/api/updatecellphone" + version;

    public static String P54_InsertAlbumOfDiy = domain + "/index/api/insertalbumofdiy" + version;

    public static String P55_UpDateAlbumOfDiy = domain + "/index/api/updatealbumofdiy" + version;

    //    public static String P57_GetAlbumOfDiy = domain + "/index/api/getalbumofdiy" + "/1.2";//2016.04.07
    public static String P57_GetAlbumOfDiy = domain + "/index/api/getalbumofdiy" + version;//2016.04.07

    public static String P58_InsertPhotoOfDiy = domain + "/index/api/insertphotoofdiy" + "/1.2";

    public static String P59_UpDatePhotoOfDiy = domain + "/index/api/updatephotoofdiy" + "/1.2";

    public static String P60_DeletePhotoOfDiy = domain + "/index/api/deletephotoofdiy" + "/1.2";

    public static String P62_SortPhotoOfDiy = domain + "/index/api/sortphotoofdiy" + "/1.3"; //2016.06.23 升版

    public static String P63_UpDateCooperation = domain + "/index/api/updatecooperation" + version;

    public static String P64_CheckNoticeQueue = domain + "/index/api/checknoticequeue" + version;

    public static String P65_HideAlbumQueue = domain + "/index/api/hidealbumqueue" + version;

    public static String P67_CheckAlbumOfDiy = domain + "/index/api/checkalbumofdiy" + "/1.2"; //2016.04.14

    public static String P68_CheckAlbumZip = domain + "/index/api/checkalbumzip" + version;

    public static String P69_GetReportintentlist = domain + "/index/api/getreportintentlist" + "/1.2";

    public static String P70_InsertReport = domain + "/index/api/insertreport" + "/1.2";

    public static String P71_GetInFoofdiy = domain + "/index/api/getinfoofdiy" + "/1.2";

    public static String P73_SwitchStatusOfContribution = domain + "/index/api/switchstatusofcontribution" + "/1.2";

    public static String P74_SwitchStatusOfVote = domain + "/index/api/switchstatusofvote" + "/1.2";

    public static String P75_GetAdList = domain + "/index/api/getadlist" + "/1.3";

    public static String P76_GetEvent = domain + "/index/api/getevent" + "/1.2";

    public static String P77_InsertAudioOfDiy = domain + "/index/api/insertaudioofdiy" + "/1.2";

    public static String P78_UpdateAudioOfDiy = domain + "/index/api/updateaudioofdiy" + "/1.2";

    public static String P79_DeleteAudioOfDiy = domain + "/index/api/deleteaudioofdiy" + "/1.2";

    public static String P80_InsertVideoOfDiy = domain + "/index/api/insertvideoofdiy" + "/1.2";

    public static String P81_UpdateVideoOfDiy = domain + "/index/api/updatevideoofdiy" + "/1.2";

    public static String P82_DeleteVideoOfDiy = domain + "/index/api/deletevideoofdiy" + "/1.2";

    public static String P83_DoTask = domain + "/index/api/dotask" + "/1.3";

    public static String P84_CheckTaskCompleted = domain + "/index/api/checktaskcompleted" + "/1.3";

    public static String p85_GetFollowToList = domain + "/index/api/getfollowtolist" + "/1.3";

    public static String p86_GetRecommendedList = domain + "/index/api/getrecommendedlist" + "/1.3";

    public static String p87_GetPushQueue = domain + "/index/api/getpushqueue" + "/1.0";

    public static String p88_CheckUpDateVersion = domain + "/index/api/checkupdateversion" + "/1.0";

    public static String P89_GetQRCode = domain + "/index/api/getqrcode" + "/1.0";

    public static String P90_GetMessageBoardList = domain + "/index/api/getmessageboardlist" + "/1.3";

    public static String P91_InsertMessageBoard = domain + "/index/api/insertmessageboard" + "/1.3";

    public static String P92_InsertAlbum2Likes = domain + "/index/api/insertalbum2likes" + "/1.3";

    public static String P93_DeleteAlbum2Likes = domain + "/index/api/deletealbum2likes" + "/1.3";

    public static String P94_GetHobbyList = domain + "/index/api/gethobbylist" + "/1.3";




}

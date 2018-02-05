package com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass;

import com.pinpinbox.android.pinpinbox2_0_0.mode.TestMode;

/**
 * Created by vmage on 2015/5/12
 */
public class UrlClass {

//    public static String domain = "http://www.pinpinbox.com";//正式機domain
//
//    public static String testW3domain = "http://w3.pinpinbox.com";//測試機domain
//
//    public static String platformvmage5 = "http://platformvmage5.cloudapp.net/pinpinbox";

    public static final String shareUserUrl = TestMode.domain() + "/index/creative/content/?user_id=";//分享創作人

    public static final String shareAlbumUrl = TestMode.domain() + "/index/album/content/?album_id=";//分享相本

    public static final String shareTemplateUrl =  TestMode.domain()  + "/index/template/content/?template_id=";//分享套版

    public static final String message = TestMode.domain()  + "/index/discuss/?albumid=";//留言板

    public static final String privacy = TestMode.domain()  + "/index/index/privacy/";//隱私權

    public static final String platform = TestMode.domain()  + "/index/index/terms/";//平台規範



}

package com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass;

import com.pinpinbox.android.BuildConfig;

/**
 * Created by vmage on 2015/5/12
 */
public class UrlClass {

//    public static String domain = "http://www.pinpinbox.com";//正式機domain
//
//    public static String testW3domain = "http://w3.pinpinbox.com";//測試機domain
//
//    public static String platformvmage5 = "http://platformvmage5.cloudapp.net/pinpinbox";

    public static final String shareUserUrl = BuildConfig.initAPI + "/index/creative/content/?user_id=";//分享創作人

    public static final String shareAlbumUrl = BuildConfig.initAPI + "/index/album/content/?album_id=";//分享相本

    public static final String shareTemplateUrl =  BuildConfig.initAPI  + "/index/template/content/?template_id=";//分享套版

    public static final String message = BuildConfig.initAPI  + "/index/discuss/?albumid=";//留言板

    public static final String privacy = BuildConfig.initAPI  + "/index/index/privacy/";//隱私權

    public static final String platform = BuildConfig.initAPI  + "/index/index/terms/";//平台規範

    public static final String about = BuildConfig.initAPI  + "/index/about/";//了解






}

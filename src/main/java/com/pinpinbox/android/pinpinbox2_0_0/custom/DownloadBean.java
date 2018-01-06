package com.pinpinbox.android.pinpinbox2_0_0.custom;

/**
 * Created by vmage on 2015/5/25
 */
public class DownloadBean {
    /** 正在下载数据。Button应显示“暂停”。 */
    public static final int STATE_LOADING = 0;

    /** 数据全部下载完成。Button应显示“安装”。 */
    public static final int STATE_COMPLETED = 1;

    /** 数据下载过程中被暂停。Button应显示“继续”。 */
    public static final int STATE_INTERRUPTED = 2;

    /** 下载安装包失败。Button应显示“失败”。 */
    public static final int STATE_DOWNLOAD_FAIL = 3;


    /** 下载资源名称 */
    public String name;
    /** 下载链接 */
    public String url;
    /** 资源字节数 */
    public long size;
    /** 已经下载的字节数 */
    public long loadedSize;
    /** 下载状态 */
    public int state;
    /** 标记是否允许下载的boolean变量 */
    public boolean enable;


    public static long loadsize = 0;


}

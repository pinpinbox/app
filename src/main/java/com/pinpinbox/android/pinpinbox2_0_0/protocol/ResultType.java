package com.pinpinbox.android.pinpinbox2_0_0.protocol;

/**
 * Created by vmage on 2017/7/7.
 */
public class ResultType {

    /**
     * SYSTEM_ERROR : 系統級別錯誤
     * SYSTEM_OK : 呼叫成功
     * USER_ERROR : 用戶級別錯誤
     * USER_REQUEST_USERLOGIN : 要求用戶登入
     * TIMEOUT : 連線逾時 (client端自定)
     * TOKEN_ERROR : Token 錯誤
     * PHOTOUSEFOR_USER_HAS_EXCHANGED : 已歸屬兌換的用戶
     * PHOTOUSEFOR_USER_HAS_GAINED : 用戶已獲得
     */

    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    public static final String SYSTEM_OK = "SYSTEM_OK";
    public static final String USER_ERROR = "USER_ERROR";
    public static final String USER_EXISTS = "USER_EXISTS";
    public static final String USER_REQUEST_USERLOGIN = "USER_REQUEST_USERLOGIN";
    public static final String TIMEOUT = "TIMEOUT";
    public static final String TOKEN_ERROR = "TOKEN_ERROR";

    public static final String PHOTOUSEFOR_USER_HAS_EXCHANGED = "PHOTOUSEFOR_USER_HAS_EXCHANGED";
    public static final String PHOTOUSEFOR_USER_HAS_GAINED = "PHOTOUSEFOR_USER_HAS_GAINED";
    public static final String PHOTOUSEFOR_USER_HAS_SLOTTED = "PHOTOUSEFOR_USER_HAS_SLOTTED";
    public static final String PHOTOUSEFOR_HAS_EXPIRED = "PHOTOUSEFOR_HAS_EXPIRED";
    public static final String PHOTOUSEFOR_HAS_SENT_FINISHED ="PHOTOUSEFOR_HAS_SENT_FINISHED";



}

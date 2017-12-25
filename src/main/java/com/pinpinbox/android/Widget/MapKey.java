package com.pinpinbox.android.Widget;

/**
 * Created by kevin9594 on 2016/1/23.
 */
public class MapKey {

    //a
    public static String account = "account"; /*使用者帳號(為 E-mail)，如果此接口參數 way 不為 none，則請以 E-mail 補上*/
    public static String albumid = "albumid"; /*相本 id*/  /*08 , 11 , 13 , 14, 16, 33, 34 , 49*/
    public static String album_id = "album_id"; /*相本 id*/  /*55  , 57 , 58 , 59 , 60 , 61 , 62 , 65 , 68*/
    public static String authorid = "authorid"; /*作者的 id*/  /*12 , 40*/
    public static String adarea_id = "adarea_id";
    public static String albumstatistics = "albumstatistics";
    public static String albumname = "albumname";
    public static String albumdescription = "albumdescription";
    public static String albumcover = "albumcover";
    public static String albumact = "albumact";
    public static String albuminsertdate = "albuminsertdate";


    //b
    public static String birthday = "birthday"; /*生日(預設值 1900-01-01) YYYY-mm-dd*/

    //c
    public static String coordinate = "coordinate"; /*座標 (緯度),(經度)*/
    public static String cellphone = "cellphone"; /*傳送的內容為國碼接上電話號碼,中間用逗號隔開,  + 號不用傳送, Server 再傳送時自動加上*/
    public static String currency = "currency"; /*貨幣TWD / USD*/
    public static String checkvalue = "checkvalue"; /*校驗值*/
    public static String checkcolumn = "checkcolumn";
    public static String count = "count";
    public static String categoryarea_id = "categoryarea_id";

     /*校驗欄位
           account => 帳號
           cellphone => 手機號碼(886,917123456)
           creative_code => 創作者代號
           */

    //d
    public static String download_id = "download_id"; /*下載 id*/
    public static String dataSignature = "dataSignature"; /*內購驗證參數將平台回傳給 app 的內購資訊，藉由此參數傳給 server 來驗證內購*/
    public static String devicetoken = "devicetoken"; /*裝置 token*/
    public static String description = "description";

    //e
    public static String event_id = "event_id"; /*活動id*/
    public static String effect = "effect"; /*作用guide (導向) / execute (執行)*/

    //f
    public static String file = "file"; /*大頭貼的圖檔*/
    public static String facebookid = "facebookid"; /*使用者 facebookid(代號)*/

    //g
    public static String gender = "gender"; /*性別 none(無) / male(男) / female(女)*/

    //h
    public static String hobby = "hobby"; /*興趣編號, 每一個編號以","隔開, 最多3個, 最少1個*/

    //i
    public static String id = "id"; /*使用者 unique id*/
    public static String identifier = "identifier"; /*裝置唯一識別碼與接口 50 setawssns 傳遞參數 identifier 意義相同，此值來源要一致*/
    public static String identity = "identity"; /*身分admin<管理者> / approver<副管理者> / editor<共用者> / viewer<瀏覽者>*/
    public static String image_url = "image_url";
    public static String image = "image";
    public static String inserttime = "inserttime";
    public static String is = "is";
    public static String is_cooperation = "is_cooperation";
    public static String is_follow = "is_follow";

    //j

    //k
    public static String keyword = "keyword";
     /* 關鍵字
          COPYRIGHT => (text) 著作權聲明
          PAYMENT_TERMS => (text) 支付條款
          PHOTO_GRADE_LIMIT => (json) 用戶級別 photo 製作數量限制
          PRIVACY => (text) 隱私權聲明
          TERMS => (text) 平台規範
          */

    //l
    public static String limit = "limit"; /*列表每次添加的數量*/
    public static String lang_id = "lang_id";


    //m
    public static String message = "message";

    //n
    public static String nickname = "nickname"; /*使用者暱稱*/
    public static String name = "name"; /*使用者暱稱*/
    public static String newpwd = "newpwd"; /*新的使用者密碼*/
    public static String newcellphone = "newcellphone"; /*新的手機號碼*/

    //o
    public static String oldpwd = "oldpwd"; /*舊的使用者密碼*/
    public static String os = "os"; /*android / ios*/
    public static String oldcellphone = "oldcellphone"; /*舊的手機號碼*/
    public static String order_id = "order_id"; /*訂單 id*/
    public static String own = "own";

    //p
    public static String pwd = "pwd"; /*使用者的密碼，如果此接口參數 way 不為 none，則請給予 null*/
    public static String password = "password"; /*使用者的密碼，如果此接口參數 way 不為 none，則請給予 null*/
    public static String platform = "platform"; /*平台apple / google*/
    public static String platform_flag = "platform_flag"; /*平台標示*/
    public static String productn = "productn"; /*產品序號(一維碼)*/
    public static String photo_id = "photo_id"; /*相片 id*/
    public static String photousefor_user_id = "photousefor_user_id"; /*相片用途x用戶 id*/
    public static String pushqueue = "pushqueue";
    public static String picfile = "picfile";
    public static String point = "point";
    public static String picture = "picture";

    //q

    //r
    public static String rank = "rank"; /*我上傳的相本 mine / 其他收藏 other / 共用收藏 cooperation*/
    public static String reportintent_id = "reportintent_id"; /*檢舉意向id*/

    //s
    public static String sign = "sign";
    public static String sort = "sort";/* 依順序排列的 photo_id array*/
    public static String smspassword = "smspassword"; /*手機簡訊驗證碼(目前四碼)*/
    public static String surl = "surl"; /*個人短網址規格:17字元，僅可輸入英數字元，且不能為 index*/
    public static String searchtype = "searchtype"; /*搜尋類型album (相本) / albumindex (相本索引) / user (用戶)*/
    public static String searchkey = "searchkey"; /*搜尋鍵*/
    public static String settings = "settings";
    public static String style_id = "style_id";
    /*obj(
        act (string, 動作, close: 關閉 / open: 開啟 / delete: 刪除)
        audio (int, 音頻 id)
        description (string, 描述)
        location (string, 地點)
        mood (string, 心情)
        secondpaging (int, 次類別)
        title (string, 名稱)
        weather (string, 天氣)
        }*/

    //t
    public static String token = "token"; /*所持有的 token*/
    public static String template_id = "template_id"; /*版型 id*/
    public static String type = "type"; /*類型album / template*/
    public static String type_id = "type_id"; /*類型的 id*/
    public static String task_for = "task_for"; /*任務關鍵字*/
    public static String target2type = "target2type";
    public static String target2type_id = "target2type_id";
    public static String text = "text";


    //u
    public static String usefor = "usefor"; /*用於register => 註冊 , editcellphone => 修改手機號碼*/
    public static String user_id = "user_id"; /*用戶 id*/
    public static String user = "user";
    public static String username = "username";

    //v
    public static String version = "version";

    //w
    public static String way = "way"; /*註冊方式none(一般註冊) / facebook(臉書註冊)*/
    public static String way_id = "way_id"; /*第三方註冊方式取得的 id，如果此接口參數 way 為 none，則請給予 null*/

    //x

    //y

    //z
    public static String zipped = "zipped";


}

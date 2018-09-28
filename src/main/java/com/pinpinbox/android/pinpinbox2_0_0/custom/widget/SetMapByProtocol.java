package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin9594 on 2016/1/23.
 */
public class SetMapByProtocol {


    public static Map<String, String> map ;

    public static Map<String, String> setParam01_login(String account, String pwd){
        map = new HashMap<>();
        map.put(MapKey.account, account);
        map.put(MapKey.pwd, pwd);
        return sendData(map);
    }

    public static Map<String, String> setParam02_checktoken(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }

    public static Map<String, String> setParam03_requestsmspwd(String account, String cellphone, String usefor){
        map = new HashMap<>();
        map.put(MapKey.account, account);
        map.put(MapKey.cellphone, cellphone);
        map.put(MapKey.usefor, usefor);
        return sendData(map);
    }

    public static Map<String, String> setParam04_registration(String account, String password, String name, String cellphone, String smspassword, String way, String way_id,
                                                              String gender, String birthday, String coordinate){
        map = new HashMap<>();
        map.put(MapKey.account, account);
        map.put(MapKey.password, password);
        map.put(MapKey.name ,name);
        map.put(MapKey.cellphone, cellphone);
        map.put(MapKey.smspassword, smspassword);
        map.put(MapKey.way, way);
        map.put(MapKey.way_id, way_id);
        map.put(MapKey.gender, gender);
        map.put(MapKey.birthday, birthday);
        map.put(MapKey.coordinate, coordinate);
        return sendData(map);
    }

    public static Map<String, String> setParam08_retrievealbumprofile(String id, String token, String album_id){ //2016.04.21 albumid => album_id
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }

    public static Map<String, String> setParam09_retrievecatgeorylist(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }

    public static Map<String, String> setParam10_retrievehotrank(String id, String token, String categoryarea_id, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.categoryarea_id, categoryarea_id);
        map.put(MapKey.limit, limit);
        return sendData(map);
    }

    public static Map<String, String> setParam13_buyalbum(String id, String token, String platform, String albumid){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.platform, platform);
        map.put(MapKey.albumid, albumid);
        return sendData(map);
    }


    public static Map<String, String> setParam17_getcloudalbumlist(String id, String token, String rank, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.rank, rank);
        map.put(MapKey.limit, limit);
        return sendData(map);
    }

    public static Map<String, String> setParam20_getupdatelist(String id, String token, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.limit, limit);
        return sendData(map);
    }

    public static Map<String, String> setParam23_getuserpoints(String id, String token, String platform){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.platform, platform);
        return sendData(map);
    }

    public static Map<String, String> setParam24_getpointstore(String id, String token, String platform, String currency){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.platform, platform);
        map.put(MapKey.currency, currency);
        return sendData(map);
    }

    public static Map<String, String> setParam28_getprofile(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }

    public static Map<String, String> setParam29_getpayload(String id, String token, String platform, String platform_flag){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.platform, platform);
        map.put(MapKey.platform_flag, platform_flag);
        return sendData(map);
    }

    public static Map<String, String> setParam30_Finishpurchased(String id, String token, String platform, String order_id, String dataSignature){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.platform, platform);
        map.put(MapKey.order_id, order_id);
        map.put(MapKey.dataSignature, dataSignature);
        return sendData(map);
    }

    public static Map<String, String> setParam32_getalbumdataoptions(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }

    public static Map<String, String> setParam33_albumsettings(String id, String token, String albumid, String settings){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.albumid, albumid);
        map.put(MapKey.settings, settings);

        return sendData(map);
    }


    public static Map<String, String> setParam34_getalbumsettings(String id, String token, String album_id){
        map = new HashMap<>();
        map.put(MapKey.user_id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }

    public static Map<String, String> setParam35_facebooklogin(String facebookid){
        map = new HashMap<>();
        map.put(MapKey.facebookid, facebookid);
        return sendData(map);
    }

    public static Map<String, String> setParam37_gettemplate(String id, String token, String template_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.template_id, template_id);
        return sendData(map);
    }

    public static Map<String, String> setParam38_buytemplate(String id, String token, String template_id, String platform){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.template_id, template_id);
        map.put(MapKey.platform, platform);
        return sendData(map);
    }

    public static Map<String, String> setParam40_getcreative(String id, String token, String authorid, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.authorid,authorid);
        map.put(MapKey.limit, limit);
        return sendData(map);
    }

    public static Map<String, String> setParam41_search(String id, String token, String searchtype, String searchkey, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.searchtype,searchtype);
        map.put(MapKey.searchkey,searchkey);
        map.put(MapKey.limit,limit);

        return sendData(map);
    }


    public static Map<String, String> setParam44_getcooperationlist(String id, String token, String type, String type_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        return sendData(map);
    }



    public static Map<String, String> setParam45_deletecooperation(String id, String token, String type, String type_id, String user_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        map.put(MapKey.user_id, user_id);
        return sendData(map);
    }


    public static Map<String, String> setParam46_insertcooperation(String id, String token, String type, String type_id, String user_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        map.put(MapKey.user_id, user_id);
        return sendData(map);
    }

    public static Map<String, String> setParam48_gettemplatestylelist(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }

    public static Map<String, String> setParam51_getsettings(String keyword){
        map = new HashMap<>();
        map.put(MapKey.keyword, keyword);
        return sendData(map);
    }

    public static Map<String, String> setParam52_check(String checkcolumn, String checkvalue){
        map = new HashMap<>();
        map.put(MapKey.checkcolumn, checkcolumn);
        map.put(MapKey.checkvalue, checkvalue);
        return sendData(map);
    }

    public static Map<String, String> setParam54_insertalbumofdiy(String id, String token, String template_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.template_id, template_id);
        return sendData(map);
    }


    public static Map<String, String> setParam55_updatealbumofdiy(String id, String token, String album_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }

    public static Map<String, String> setParam57_getalbumofdiy(String id, String token, String album_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }

    public static Map<String, String> setParam59_updatephotoofdiy(String id, String token, String album_id, String photo_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        map.put(MapKey.photo_id, photo_id);
        return sendData(map);
    }




    public static Map<String, String> setParam62_sortphotoofdiy(String id, String token, String album_id, String sort){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        map.put(MapKey.sort, sort);
        return sendData(map);
    }


    public static Map<String, String> setParam63_updatecooperation(String id, String token, String type, String type_id, String user_id, String identity){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        map.put(MapKey.user_id, user_id);
        map.put(MapKey.identity, identity);
        return sendData(map);
    }



    public static Map<String, String> setParam64_checknoticequeue(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }

    public static Map<String, String> setParam67_checkalbumofdiy(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }

    public static Map<String, String> setParam68_checkalbumofdiy(String id, String token, String album_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }

    public static Map<String, String> setParam69_getreportintentlist(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }

    public static Map<String, String> setParam70_insertreport(String id, String token, String reportintent_id, String type, String type_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.reportintent_id, reportintent_id);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        return sendData(map);
    }

    public static Map<String, String> setParam71_getinfoofdiy(String id, String token, String album_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }

    public static Map<String, String> setParam73_switchstatusofcontribution(String id, String token, String event_id, String album_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.event_id, event_id);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }

    public static Map<String, String> setParam74_switchstatusofvote(String id, String token, String event_id, String album_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.event_id, event_id);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }

    public static Map<String, String> setParam75_getadlist(String id, String token, String adarea_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.adarea_id, adarea_id);

        return sendData(map);
    }



    public static Map<String, String> setParam76_getevent(String id, String token, String event_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.event_id, event_id);
        return sendData(map);
    }


    public static Map<String, String> setParam78_UpdateAudioOfDiy(String id, String token, String album_id, String photo_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        map.put(MapKey.photo_id, photo_id);
        return sendData(map);
    }

    public static Map<String, String> setParam79_deleteaudioofdiy(String id, String token, String album_id, String photo_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        map.put(MapKey.photo_id, photo_id);
        return sendData(map);
    }

    public static Map<String, String> setParam82_deletevideoofdiy(String id, String token, String album_id, String photo_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        map.put(MapKey.photo_id, photo_id);
        return sendData(map);
    }

    public static Map<String, String> setParam83_dotask(String id, String token, String task_for, String platform){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.task_for, task_for);
        map.put(MapKey.platform, platform);
        return sendData(map);
    }

    public static Map<String, String> setParam83_dotask_for_share_to_fb(String id, String token, String task_for, String platform, String type, String type_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.task_for, task_for);
        map.put(MapKey.platform, platform);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        return sendData(map);
    }

    public static Map<String, String> setParam84_checktaskcompleted(String id, String token, String task_for, String platform, String type, String type_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.task_for, task_for);
        map.put(MapKey.platform, platform);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        return sendData(map);
    }


    public static Map<String, String> setParam84_checktaskcompleted(String id, String token, String task_for, String platform, Map<String, String> exceptData){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.task_for, task_for);
        map.put(MapKey.platform, platform);

        return sendDataExcept(map, exceptData);
    }



    public static Map<String, String> setParam86_getrecommendedlist(String id, String token, String type, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.type, type);
        map.put(MapKey.limit, limit);
        return sendData(map);
    }

    public static Map<String, String> setParam87_getpushqueue(String id, String token, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.limit, limit);
        return sendData(map);
    }

    public static Map<String, String> setParam88_checkupdateversion(String platform, String version){
        map = new HashMap<>();
        map.put(MapKey.platform, platform);
        map.put(MapKey.version, version);
        return sendData(map);
    }


    public static Map<String, String> setParam90_getmessageboardlist(String id, String token, String type, String type_id, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        map.put(MapKey.limit, limit);
        return sendData(map);
    }

    public static Map<String, String> setParam91_insertmessageboard(String id, String token, String type, String type_id, String text, String limit){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.type, type);
        map.put(MapKey.type_id, type_id);
        map.put(MapKey.text, text);
        map.put(MapKey.limit, limit);
        return sendData(map);
    }


    public static Map<String, String> setParam92_insertalbum2likes(String id, String token, String album_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);

        return sendData(map);
    }


    public static Map<String, String> setParam93_deletealbum2likes(String id, String token, String album_id){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        map.put(MapKey.album_id, album_id);
        return sendData(map);
    }


    public static Map<String, String> setParam94_gethobbylist(String id, String token){
        map = new HashMap<>();
        map.put(MapKey.id, id);
        map.put(MapKey.token, token);
        return sendData(map);
    }


    public static Map<String, String> setParam103_getthemearea(String user_id, String token){
        map = new HashMap<>();
        map.put(Key.user_id, user_id);
        map.put(Key.token, token);
        return sendData(map);
    }





    public static Map<String, String> sendData(Map<String, String> data){
        String sign = IndexSheet.encodePPB(data);
        Map<String, String> map = new HashMap<String, String>();

        for (Map.Entry entry : data.entrySet()) {
            String key = (String)entry.getKey( );
            String value = (String)entry.getValue();
            map.put(key, value);
        }
        map.put("sign",sign);
        return map;
    }


    public static Map<String, String> sendDataExcept(Map<String, String> data, Map<String, String> exceptData){
        String sign = IndexSheet.encodePPB(data);
        Map<String, String> map = new HashMap<String, String>();

        for (Map.Entry entry : data.entrySet()) {
            String key = (String)entry.getKey( );
            String value = (String)entry.getValue();
            map.put(key, value);
        }
        map.put("sign",sign);

        for (Map.Entry entry : exceptData.entrySet()) {
            String key = (String)entry.getKey( );
            String value = (String)entry.getValue();
            map.put(key, value);
        }

        return map;
    }











}

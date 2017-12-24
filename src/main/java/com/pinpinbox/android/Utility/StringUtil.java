package com.pinpinbox.android.Utility;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.pinpinbox.android.Widget.StringIntMethod;
import com.pinpinbox.android.Widget.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by kevin9594 on 2016/9/24.
 */
public class StringUtil {

    private static final String SEP1 = "#";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";

    /**
     * List转换String
     *
     * @param list :需要转换的List
     * @return String转换后的字符串
     */
    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    sb.append(SEP1);
                } else if (list.get(i) instanceof Map) {
                    sb.append(MapToString((Map<?, ?>) list.get(i)));
                    sb.append(SEP1);
                } else {
                    sb.append(list.get(i));
                    sb.append(SEP1);
                }
            }
        }
        return "L" + sb.toString();
    }

    /**
     * Map转换String
     *
     * @param map :需要转换的Map
     * @return String转换后的字符串
     */
    public static String MapToString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        // 遍历map
        for (Object obj : map.keySet()) {
            if (obj == null) {
                continue;
            }
            Object key = obj;
            Object value = map.get(key);
            if (value instanceof List<?>) {
                sb.append(key.toString() + SEP1 + ListToString((List<?>) value));
                sb.append(SEP2);
            } else if (value instanceof Map<?, ?>) {
                sb.append(key.toString() + SEP1
                        + MapToString((Map<?, ?>) value));
                sb.append(SEP2);
            } else {
                sb.append(key.toString() + SEP3 + value.toString());
                sb.append(SEP2);
            }
        }
        return "M" + sb.toString();
    }

    /**
     * String转换Map
     *
     * @param mapText          :需要转换的字符串
     * @param KeySeparator     :字符串中的分隔符每一个key与value中的分割
     * @param ElementSeparator :字符串中每个元素的分割
     * @return Map<?,?>
     */
    public static Map<String, Object> StringToMap(String mapText) {

        if (mapText == null || mapText.equals("")) {
            return null;
        }
        mapText = mapText.substring(1);

        mapText = mapText;

        Map<String, Object> map = new HashMap<String, Object>();
        String[] text = mapText.split("\\" + SEP2); // 转换为数组
        for (String str : text) {
            String[] keyText = str.split(SEP3); // 转换key与value的数组
            if (keyText.length < 1) {
                continue;
            }
            String key = keyText[0]; // key
            String value = keyText[1]; // value
            if (value.charAt(0) == 'M') {
                Map<?, ?> map1 = StringToMap(value);
                map.put(key, map1);
            } else if (value.charAt(0) == 'L') {
                List<?> list = StringToList(value);
                map.put(key, list);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * String转换List
     *
     * @param listText :需要转换的文本
     * @return List<?>
     */
    public static List<Object> StringToList(String listText) {
        if (listText == null || listText.equals("")) {
            return null;
        }
        listText = listText.substring(1);

        listText = listText;

        List<Object> list = new ArrayList<Object>();
        String[] text = listText.split(SEP1);
        for (String str : text) {
            if (str.charAt(0) == 'M') {
                Map<?, ?> map = StringToMap(str);
                list.add(map);
            } else if (str.charAt(0) == 'L') {
                List<?> lists = StringToList(str);
                list.add(lists);
            } else {
                list.add(str);
            }
        }
        return list;
    }


    public static String ThousandToK(int count) {

        String strCount = "";

        /**破萬時轉為K顯示*/
//        if(count>9999){

        strCount = StringIntMethod.IntToString(count);
        strCount = strCount.substring(0, strCount.length() - 3);
        MyLog.Set("d", StringUtil.class, "strViewCount => " + strCount);


        return strCount;

    }

    public static void ThousandToK(TextView tv, int count){

        if(count>9999) {
            String strFollow = StringUtil.ThousandToK(count);
            tv.setText(strFollow + "K");
        }else {
            tv.setText(count + "");
        }

    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!");//替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }



    /**判斷是否為youtube url*/
    public static String checkYoutubeId(@NonNull String videoUrl) {
        String reg = "(?:youtube(?:-nocookie)?\\.com\\/(?:[^\\/\\n\\s]+\\/\\S+\\/|(?:v|e(?:mbed)?)\\/|\\S*?[?&]v=)|youtu\\.be\\/)([a-zA-Z0-9_-]{11})";
        Pattern pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(videoUrl);

        if (matcher.find())
            return matcher.group(1);
        return null;
    }

    /**判斷是否包含字串*/
    public static boolean containsString(String src, String dest) {
        boolean flag = false;
        if (src.contains(dest)) {
            flag = true;
        }
        return flag;
    }


}

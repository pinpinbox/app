package com.pinpinbox.android.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vmage on 2015/12/2.
 */
public class MapQuery {

   public static String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }
   public static String urlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("p1", 12);
        map.put("p2", "cat");
        map.put("p3", "a & b");
        System.out.println(urlEncodeUTF8(map));
        // prints "p3=a+%26+b&p2=cat&p1=12"
    }

    public static String toQueryString(Map<?, ?> data) throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();

        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(URLEncoder.encode((String) pair.getKey(), "UTF-8")).append("=");
            queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8")).append("&");
        }

        if (queryString.length () > 0) {
            queryString.deleteCharAt ( queryString.length () - 1 );
        }

        return queryString.toString ();
    }

}

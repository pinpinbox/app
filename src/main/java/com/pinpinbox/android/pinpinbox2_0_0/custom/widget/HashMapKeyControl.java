package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

import java.util.HashMap;

/**
 * Created by kevin9594 on 2017/7/8.
 */
public class HashMapKeyControl {


    public static void changeMapKey(HashMap<String, Object> map, String key, String value) {

        if (map.containsKey(key)) {

            map.remove(key);
            map.put(key, value);

        }
    }

    public static void changeMapKey(HashMap<String, Object> map, String key, boolean value) {

        if (map.containsKey(key)) {

            map.remove(key);
            map.put(key, value);

        }
    }

}

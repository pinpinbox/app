package com.pinpinbox.android.Utility;

//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;

import com.pinpinbox.android.pinpinbox2_0_0.custom.KeysForSKD;

import org.json.JSONObject;

/**
 * Created by vmage on 2015/3/20.
 */
public class MapUtility {
    public static JSONObject getLocationInfo(String address) {

        JSONObject jsonObject = null;

        /*example*/
//        https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=台灣&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=

        try {

//            String str = OkHttpClientManager.getAsString("https://maps.googleapis.com/maps/api/geocode/json?address=" + address
//
//                    + "&key=" + KeysForSKD.GOOGLE_MAP + "&sensor=false");

//            String str = OkHttpClientManager.getAsString("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + address
//
//                    + "&inputtype=textquery"
//
//                    + "&fields=formatted_address,name,geometry"
//
//                    + "&key=" + KeysForSKD.GOOGLE_MAP);

            String str = OkHttpClientManager.getAsString("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + address

                    + "&key=" + KeysForSKD.GOOGLE_MAP);


            jsonObject = new JSONObject(str);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;

    }


//    public static GeoPoint getGeoPoint(JSONObject jsonObject) {
//
//        Double lon = new Double(0);
//
//        Double lat = new Double(0);
//
//        try {
//
//            lon = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//
//                    .getJSONObject("geometry").getJSONObject("location")
//
//                    .getDouble("lng");
//
//            lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
//
//                    .getJSONObject("geometry").getJSONObject("location")
//
//                    .getDouble("lat");
//
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//
//        }
//
//        return new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
//
//    }


}

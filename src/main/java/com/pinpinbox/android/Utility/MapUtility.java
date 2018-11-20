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

        try {
//           String str = OkHttpClientManager.getAsString("http://maps.google."
//
//                    + "com/maps/api/geocode/json?address=" + address
//
//                    + "&sensor=false");

            String str = OkHttpClientManager.getAsString("https://maps.googleapis.com/maps/api/geocode/json?address=" + address

                    + "&key=" + KeysForSKD.GOOGLE_MAP);

            jsonObject = new JSONObject(str);

        }catch (Exception e){
            e.printStackTrace();
        }



//        HttpGet httpGet = new HttpGet("http://maps.google."
//
//                + "com/maps/api/geocode/json?address=" + address
//
//                + "&sensor=false");
//
//        HttpClient client = new DefaultHttpClient();
//
//        HttpResponse response;
//
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try {
//
//            response = client.execute(httpGet);
//
//            HttpEntity entity = response.getEntity();
//
//            InputStream stream = entity.getContent();
//
//            int b;
//
//            while ((b = stream.read()) != -1) {
//
//                stringBuilder.append((char) b);
//
//            }
//
//        } catch (IOException e) {
//
//            e.printStackTrace();
//
//        }
//
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//
//            jsonObject = new JSONObject(stringBuilder.toString());
//
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//
//        }

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

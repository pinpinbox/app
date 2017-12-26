package com.pinpinbox.android.pinpinbox2_0_0.custom.widget;

/**
 * Created by vmage on 2015/10/26.
 */
public class UserGradeChange {

    private static String p51Result, p51Message;

    public static String PicCountByUserGrade(String strGrade){
       String count = "";


        /**2016.11.03new add*/
        if(strGrade.equals("")){
            count = "22";
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try{
//                    String strJson = HttpUtility.uploadSubmit(ProtocolsClass.P51_GetSettings, SetMapByProtocol.setParam51_getsettings("PHOTO_GRADE_LIMIT"), null);
//                    Log.d("PicCountByUserGrade", "p51strJson => " + strJson);
//                    JSONObject jsonObject = new JSONObject(strJson);
//                    p51Result = jsonObject.getString("result");
//                    if(p51Result.equals("1")){
//                        Log.d("PicCountByUserGrade ", "p51Result => " + p51Result);
//                    }else if(p51Result.equals("0")) {
//                         p51Message = jsonObject.getString("message");
//                    }else {
//                        p51Result = "";
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//        }).start();

        switch (strGrade){
            case "free":
                count = "22";
                break;
            case "plus":
                count = "200";
                break;

            case "profession":
                count = "200";
                break;
        }
        return count;
    }


}

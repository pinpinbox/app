package com.pinpinbox.android.SelfMadeClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by vmage on 2015/6/22
 */
public class MyInstalledReceiver extends BroadcastReceiver {

    private Context mContext ;


    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Log.i("homer", "收到一封簡訊");
            getSms(mContext);
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                for (Object pdu : pdus) {
                    // 构建短信的对象
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                    // 取出号码和内容
                    String address = smsMessage.getOriginatingAddress();
                    String body = smsMessage.getMessageBody();

                    System.out.println("号码: " + address + ", 内容: " + body);
                    abortBroadcast(); // 把短信给拦截了. 终止广播
//                    if ("110".equals(address)) {
//                        // 当前需要拦截此号码
//
//                        // 把数据和内容提交到服务器中.
//                        abortBroadcast(); // 把短信给拦截了. 终止广播
//                    }
                }
            }
        }//if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))



    }

    public void getSms(Context context){
//      final Dialog sendOKdlg = new Dialog(context, R.style.Dialog_Fullscreen);
//       Window crosswin = sendOKdlg.getWindow();
//       crosswin.setContentView(R.layout.dialog_sms_check);
//       sendOKdlg.show();
//        ImageView backImg = (ImageView) sendOKdlg.findViewById(R.id.dialog_back);
//        backImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendOKdlg.dismiss();
//            }
//        });


    }



}

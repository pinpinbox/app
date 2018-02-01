package com.pinpinbox.android;


import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.StringClass.DialogStyleClass;
import com.pinpinbox.android.StringClass.SharedPreferencesDataClass;
import com.pinpinbox.android.StringClass.TagClass;
import com.pinpinbox.android.Test.Category2Activity;
import com.pinpinbox.android.Test.FragmentPhotoCrop;
import com.pinpinbox.android.Test.TestStatusActivity;
import com.pinpinbox.android.Test.TextTestActivity;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.ExchangeList2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Login2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.OffLine2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.TypeFacebookFriend2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.VideoPlayActivity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.NoConnect;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Rotate3d;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.service.DownLoadService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;

//import com.pinpinbox.android.Test.PieChartActivity;


public class OldMainActivity extends FragmentActivity {

    private static final String TAG = "OldMainActivity";
    private SharedPreferences getdata;
    private CountDownTimer countDownTimer;

    private String id, token;
    private JSONArray jsonArray;
    private int getj;

    private String sdpath = Environment.getExternalStorageDirectory() + "/";
    private String myDir;
    private String albumListDir = "albumlist";
    private String zipDir = "/zip/";

    private Dialog loaddlg;

    Activity mActivity;

    private NoConnect noConnect;

    private int position = 0;


    public void shareLine() {
        ComponentName cn = new ComponentName("jp.naver.line.android"
                , "jp.naver.line.android.activity.selectchat.SelectChatActivity");

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
//        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap, null,null));
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg"); //图片分享
        shareIntent.setType("text/plain"); // 纯文本
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享的标题");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "分享的内容");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setComponent(cn);//跳到指定APP的Activity
        mActivity.startActivity(Intent.createChooser(shareIntent, ""));


//        if (coverUrl != null && !coverUrl.equals("")) {
//            Uri u = Uri.parse(coverUrl);
//            intent.putExtra(Intent.EXTRA_STREAM, u);
//        }
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//        intent.putExtra(Intent.EXTRA_TEXT, itemAlbum.getName() + " , " + UrlClass.shareAlbumUrl + album_id + "&autoplay=1");//分享內容
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mActivity.startActivity(Intent.createChooser(intent, mActivity.getTitle()));


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_activity_main);
        init();


        final Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                FileUtility.delAllFile(Environment.getExternalStorageDirectory() + "/" + myDir);

                File file = new File(sdpath);

                String allfilespath[] = file.list();

                if (allfilespath != null && allfilespath.length > 0) {
                    for (int i = 0; i < allfilespath.length; i++) {

                        String dirName = allfilespath[i];

                        if (dirName.length() > 8) {

                            String strFirst9 = dirName.substring(0, 9);

                            if (strFirst9.equals("PinPinBox")) {

                                FileUtility.deleteFileFolder(new File(sdpath + allfilespath[i]));

                            }

                        }

                    }
                }


                SharedPreferences getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
                getData.edit().clear().commit();

                SharedPreferences albummusic = getSharedPreferences(SharedPreferencesDataClass.albummusic, Activity.MODE_PRIVATE);
                albummusic.edit().clear().commit();

                SharedPreferences album110 = getSharedPreferences("pinpinbox_110", Activity.MODE_PRIVATE);
                album110.edit().clear().commit();


//
//                FileUtility.delAllFile(sdpath + myDir + "template/tem_copy");
//                FileUtility.delAllFile(sdpath + myDir + "copy/");

                FileUtility.delAllFile(Environment.getExternalStorageDirectory() + "/" + "HeadPic/");

                Toast.makeText(OldMainActivity.this, "已刪除所有相本和暫存檔,請重新啟動pinpinbox", Toast.LENGTH_LONG).show();
                Intent i = new Intent(OldMainActivity.this, Login2Activity.class);
                startActivity(i);
                finish();
            }
        });

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences getData = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
                getData.edit().clear().commit();


                FileUtility.delAllFile(Environment.getExternalStorageDirectory() + "/" + "pinpinbox_small/");
                FileUtility.delAllFile(Environment.getExternalStorageDirectory() + "/" + "pinpinbox_camera/");


                Toast.makeText(OldMainActivity.this, "已刪除資訊和用pinpinbox照相的照片", Toast.LENGTH_LONG).show();
                Intent i = new Intent(OldMainActivity.this, Login2Activity.class);
                startActivity(i);
                finish();

            }
        });

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareLine();


//                album_id = bundle.getString("album_id");
//                template_id = bundle.getString("template_id");
//                identity = bundle.getString("identity");
//                CreationMode = bundle.getInt("create_mode"); 0-3

            /*    454套版
                              475快速*/

         /* private static final int CreationFast = 0;//快速
                     private static final int CreationTemplate = 1;//套版

                     private static final int CreationEdit = 3;//編輯*/


            }
        });

        Button btn4 = (Button) findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mActivity, TestStatusActivity.class);
                startActivity(intent);

            }
        });


        Button btn5 = (Button) findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PackageManager pm = getPackageManager();

                List activities = pm.queryIntentActivities(new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

                if (activities.size() != 0) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    // 语言模式和自由模式的语音识别
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    // 提示语音开始
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
                    // 开始语音识别
                    startActivityForResult(intent, 1234);
                } else {
                    // 若检测不到语音识别程序在本机安装，测将扭铵置灰
                    Toast.makeText(mActivity, "沒有語音裝置", Toast.LENGTH_SHORT).show();
                }


            }
        });

        final Button btn6 = (Button) findViewById(R.id.button6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(OldMainActivity.this, Hobby2Activity.class);
//                startActivity(intent);

                PinPinToast.showSponsorToast(
                        mActivity.getApplicationContext(),
                        "KevinLin" + getResources().getString(R.string.pinpinbox_2_0_0_toast_message_thank_by_sponsor),
                        "https://cdn.pinpinbox.com/storage/zh_TW/user/4056/picture$50fb.jpg"
                );


            }
        });

        Button btn7 = (Button) findViewById(R.id.button7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(OldMainActivity.this, CreateAlbum2Activity.class);
//                startActivity(intent);


                Bundle bundle = new Bundle();
                bundle.putString(Key.url, "https://w3.pinpinbox.com/test.php");


                Intent intent = new Intent(OldMainActivity.this, WebView2Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        Button btn8 = (Button) findViewById(R.id.button8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = new Bundle();
                bundle.putString("videopath", "https://vimeo.com/180857301");
                Intent intent = new Intent(OldMainActivity.this, VideoPlayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });

        Button btn9 = (Button) findViewById(R.id.button9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                findViewById(R.id.linRichEdit).setVisibility(View.VISIBLE);
                showEditText();


            }
        });

        Button btn10 = (Button) findViewById(R.id.button10);
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putBoolean(Key.exitAPP, true);

                Intent intent = new Intent(OldMainActivity.this, OffLine2Activity.class);
                intent.putExtras(b);
                startActivity(intent);

            }
        });


        Button btn11 = (Button) findViewById(R.id.button11);
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences logData = getSharedPreferences("TestLog", Activity.MODE_PRIVATE);

                TextView tvLog = (TextView) findViewById(R.id.tvLog);
                tvLog.setVisibility(View.VISIBLE);
                tvLog.setText(logData.getString("TestLog", "沒有log"));


            }
        });


        Button btn12 = (Button) findViewById(R.id.button12);
        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

                String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
                if (ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置


                Log.d(TAG, "緯度：" + location.getLatitude());
                Log.d(TAG, "經度：" + location.getLongitude());

            }
        });

        Button btn13 = (Button) findViewById(R.id.button13);
        btn13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OldMainActivity.this, TextTestActivity.class));

            }
        });


        Button btn14 = (Button) findViewById(R.id.button14);
        btn14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {


                        int bgColor = Color.parseColor(ColorClass.MAIN_THIRD);

                        try {
                            Bitmap bitmap = Picasso.with(getApplicationContext())
                                    .load(R.drawable.test_banner)
                                    .config(Bitmap.Config.RGB_565)
                                    .resize(50, 50)
                                    .get();

                            if (bitmap != null) {

                                Palette.Builder palette = Palette.from(bitmap);

                                bgColor = searchColor(palette.generate());

                                int red = (bgColor & 0xff0000) >> 16;
                                int green = (bgColor & 0x00ff00) >> 8;
                                int blue = (bgColor & 0x0000ff);


                                RGB(bgColor);

                                getAllColor(palette.generate());

                                bgColor = Color.argb(120, red, green, blue);

                                bitmap.recycle();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }).start();


            }
        });

        Button btn15 = (Button) findViewById(R.id.button15);
        btn15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OldMainActivity.this, Category2Activity.class);
                startActivity(intent);

            }
        });


        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "timer =>" + (millisUntilFinished / 1000) + "");
            }

            @Override
            public void onFinish() {

                Log.e(TAG, "timer => finish()");

                countDownTimer.cancel();
            }
        };

        Button btn16 = (Button) findViewById(R.id.button16);
        btn16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer.start();

                } else {

                    countDownTimer.start();
                }

            }
        });

        Button button17 = (Button) findViewById(R.id.button17);
        button17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView = (ImageView) findViewById(R.id.img);
//                imageView.post(new SwapStartViews());


                startActivity(new Intent(OldMainActivity.this, TypeFacebookFriend2Activity.class));


            }
        });

        Button button18 = (Button) findViewById(R.id.button18);
        button18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lin);
                linearLayout.setVisibility(View.VISIBLE);

                Button btError = (Button) findViewById(R.id.btError);
                final Button btCheck = (Button) findViewById(R.id.btCheck);
                Button btTimeout = (Button) findViewById(R.id.btTimeout);
                Button btNoconnect = (Button) findViewById(R.id.btNoconnect);
                Button btUnknow = (Button) findViewById(R.id.btUnknow);
                ImageView closeImg = (ImageView) findViewById(R.id.closeImg);

                closeImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        linearLayout.setVisibility(View.GONE);
                    }
                });


                btError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogV2Custom.BuildError(mActivity, "錯誤訊息");

                    }
                });

                btCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogV2Custom d = new DialogV2Custom(mActivity);
                        d.setStyle(DialogStyleClass.CHECK);
                        d.setMessage("確認訊息");
                        d.show();

                    }
                });

                btTimeout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DialogV2Custom.BuildTimeOut(mActivity, null);

                    }
                });

                btNoconnect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogV2Custom.BuildNoConnect(mActivity);
                    }
                });

                btUnknow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogV2Custom.BuildUnKnow(mActivity, "123456");
                    }
                });


//                DialogV2Custom d = new DialogV2Custom(mActivity);
//                d.setStyle(DialogStyleClass.ERROR);
////                d.setConnectInstability();
//                d.show();


            }
        });

        Button button19 = (Button) findViewById(R.id.button19);
        button19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OldMainActivity.this, ExchangeList2Activity.class));


            }
        });


        final FragmentPhotoCrop fragmentPhotoCrop = new FragmentPhotoCrop();
        Button button20 = (Button) findViewById(R.id.button20);
        button20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!fragmentPhotoCrop.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.frame, fragmentPhotoCrop, TagClass.TagFragmentPhotoCrop).commit();

                    getSupportFragmentManager().beginTransaction().show(fragmentPhotoCrop);

                } else {
                    getSupportFragmentManager().beginTransaction().show(fragmentPhotoCrop);
                }

            }
        });

        final Button button21 = (Button) findViewById(R.id.button21);
        button21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ViewPropertyAnimator aaa = button21.animate();
                aaa.setDuration(1000)
                        .translationZ(32f)

                        .start();


            }
        });


        final String aa[] = {"3348", "3349", "3351", "3364"};

        Button button22 = (Button) findViewById(R.id.button22);
        button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position > 3) {
                    return;
                }

                List<Service> serviceList = SystemUtility.SysApplication.getInstance().getsList();

                DownLoadService downLoadService = null;
                for (int i = 0; i < serviceList.size(); i++) {
                    if (serviceList.get(i).getClass().getSimpleName().equals(DownLoadService.class.getSimpleName())) {
                        downLoadService = (DownLoadService) serviceList.get(i);

                        break;
                    }
                }


                if (downLoadService == null) {
                    downLoadService = new DownLoadService();
                    Bundle bundle = new Bundle();
                    bundle.putString(Key.album_id, aa[position]);
                    Intent intentService = new Intent(OldMainActivity.this, downLoadService.getClass());
                    intentService.putExtras(bundle);
                    startService(intentService);
                    MyLog.Set("d", mActivity.getClass(), "startService");
                } else {
                    MyLog.Set("d", mActivity.getClass(), "DownLoadService 已存在");
//                    downLoadService.addAlbum(aa[position] + "");
                }

                position++;

            }
        });

        Button button23 = (Button) findViewById(R.id.button23);
        button23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                aaaa++;


            }
        });

    }

    private void showEditText() {

        final RichEditor mEditor = (RichEditor) findViewById(R.id.editor);
        final TextView tvPreview = (TextView) findViewById(R.id.tvPreview);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                tvPreview.setText(text);
            }
        });

        String text = "";
        Resources resources = this.getResources();
        InputStream is = null;
        try {
            is = resources.openRawResource(R.raw.htmltest);
            byte buffer[] = new byte[is.available()];
            is.read(buffer);
            text = new String(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mEditor.setHtml(text);

        mEditor.setEditorFontSize(18);
        mEditor.setEditorFontColor(getResources().getColor(R.color.pinpinbox_2_0_0_first_grey, null));
        mEditor.setPadding(16, 16, 16, 16);
        mEditor.setPlaceholder("Insert text here...");


        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
                        "dachshund");
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });


    }


    //    private ImageView imageView;
    private final class SwapStartViews implements Runnable {

        private int mDuration;
        private ImageView mImageView;

        public SwapStartViews(int duration, ImageView imageView) {
            this.mDuration = duration;
            this.mImageView = imageView;
        }

        @Override
        public void run() {

            final float centerX = mImageView.getWidth() / 2.0f;
            final float centerY = mImageView.getHeight() / 2.0f;
            final Rotate3d rotation = new Rotate3d(0, 360,
                    centerX, centerY, 360.0f, true);
            rotation.setDuration(mDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new AccelerateInterpolator());
            rotation.setAnimationListener(new DisplayNextView(mDuration));
            mImageView.startAnimation(rotation);

        }
    }

    private final class SwapViews implements Runnable {

        private int mDuration;
        private ImageView mImageView;

        public SwapViews(int duration, ImageView imageView) {
            this.mDuration = duration;
            this.mImageView = imageView;
        }

        public void run() {
            final float centerX = mImageView.getWidth() / 2.0f;
            final float centerY = mImageView.getHeight() / 2.0f;
            Rotate3d rotation = null;
            mImageView.requestFocus();
            rotation = new Rotate3d(360, 0, centerX, centerY, 360.0f,
                    false);
            rotation.setDuration(mDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            rotation.setAnimationListener(new DisplayBackView(mDuration));
            mImageView.startAnimation(rotation);

        }
    }

    private final class DisplayNextView implements Animation.AnimationListener {

        private int mDuration;

        public DisplayNextView(int duration) {
            this.mDuration = duration;
        }

        public void onAnimationStart(Animation animation) {
        }

        // 动画结束
        public void onAnimationEnd(Animation animation) {
//            imageView.post(new SwapViews());


        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private final class DisplayBackView implements Animation.AnimationListener {

        private int mDuration;

        public DisplayBackView(int duration) {
            this.mDuration = duration;
        }

        public void onAnimationStart(Animation animation) {
        }

        // 动画结束
        public void onAnimationEnd(Animation animation) {
//            imageView.post(new SwapStartViews());

        }

        public void onAnimationRepeat(Animation animation) {
        }
    }


    private void init() {
        mActivity = this;
        getdata = getSharedPreferences(SharedPreferencesDataClass.memberdata, Activity.MODE_PRIVATE);
        id = getdata.getString("id", "");
        token = getdata.getString("token", "");
        myDir = "/PinPinBox" + id + "";

    }

    private int searchColor(Palette palette) {

        int color = Color.parseColor(ColorClass.MAIN_THIRD);

        if (palette.getVibrantSwatch() != null) {

            color = palette.getVibrantSwatch().getRgb();

        } else if (palette.getLightVibrantSwatch() != null) {

            color = palette.getLightVibrantSwatch().getRgb();

        } else if (palette.getDarkVibrantSwatch() != null) {

            color = palette.getDarkVibrantSwatch().getRgb();

        } else if (palette.getMutedSwatch() != null) {

            color = palette.getMutedSwatch().getRgb();

        } else if (palette.getLightMutedSwatch() != null) {

            color = palette.getLightMutedSwatch().getRgb();

        }


        return color;

    }

    private void getAllColor(Palette palette) {


        try {
            MyLog.Set("d", getClass(), "Vibrant => " + RGB(palette.getVibrantSwatch().getRgb()));
        } catch (Exception e) {
            MyLog.Set("d", getClass(), "Vibrant => ");
        }

        try {
            MyLog.Set("d", getClass(), "LightVibrant => " + RGB(palette.getLightVibrantSwatch().getRgb()));
        } catch (Exception e) {
            MyLog.Set("d", getClass(), "LightVibrant => ");
        }

        try {
            MyLog.Set("d", getClass(), "DarkVibrant => " + RGB(palette.getDarkVibrantSwatch().getRgb()));
        } catch (Exception e) {
            MyLog.Set("d", getClass(), "DarkVibrant => ");
        }

        try {
            MyLog.Set("d", getClass(), "Muted => " + RGB(palette.getMutedSwatch().getRgb()));
        } catch (Exception e) {
            MyLog.Set("d", getClass(), "Muted => ");
        }

        try {
            MyLog.Set("d", getClass(), "LightMuted => " + RGB(palette.getLightMutedSwatch().getRgb()));
        } catch (Exception e) {
            MyLog.Set("d", getClass(), "LightMuted => ");
        }

        try {
            MyLog.Set("d", getClass(), "DarkMuted => " + RGB(palette.getDarkMutedSwatch().getRgb()));
        } catch (Exception e) {
            MyLog.Set("d", getClass(), "DarkMuted => ");
        }

    }


    private String RGB(int color) {

        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);

        MyLog.Set("d", getClass(), "red =>" + red);
        MyLog.Set("d", getClass(), "green =>" + green);
        MyLog.Set("d", getClass(), "blue =>" + blue);


        String r, g, b;
        StringBuilder su = new StringBuilder();
        r = Integer.toHexString(red);
        g = Integer.toHexString(green);
        b = Integer.toHexString(blue);
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        su.append("0xFF");
        su.append(r);
        su.append(g);
        su.append(b);
        //0xFF0000FF
        MyLog.Set("d", mActivity.getClass(), "hex => " + su.toString());

        return su.toString();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (!HttpUtility.isConnect(this)) {

            if (noConnect == null) {
                noConnect = new NoConnect(this);
            }
        }
    }


    @Override
    protected void onDestroy() {

        SharedPreferences setdata = getSharedPreferences("PinPinbox_membrdata", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = setdata.edit();
        editor.putInt("downlist", 0).commit();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        System.out.println("按下了back键 onBackPressed()");
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 回调获取从谷歌得到的数据
        if (requestCode == 1234
                && resultCode == RESULT_OK) {
            // 取得语音的字符
            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String resultString = "";
            for (int i = 0; i < results.size(); i++) {
                resultString += results.get(i);
            }
            Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show();
        }
        // 语音识别后的回调，将识别的字串以Toast显示
        super.onActivityResult(requestCode, resultCode, data);
    }


}

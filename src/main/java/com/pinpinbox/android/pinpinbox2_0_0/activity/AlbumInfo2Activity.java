package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.orhanobut.logger.Logger;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Test.ScaleTouhListener;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.FlurryUtil;
import com.pinpinbox.android.Utility.Gradient.ScrimUtil;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.StringUtil;
import com.pinpinbox.android.Utility.SystemUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundCornerImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.parallaxscroll.views.ParallaxScrollView;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickDragDismissListener;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.IndexSheet;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.TaskKeyClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.UrlClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.FlurryKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.LinkText;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MapKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Recycle;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Value;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogHandselPoint;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.libs.TouchRange.TouchRange;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol100_Vote;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol13_BuyAlbum;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopBoard;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopPicker;
import com.pinpinbox.android.pinpinbox2_0_0.popup.PopupCustom;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by kevin9594 on 2016/12/26.
 */
public class AlbumInfo2Activity extends DraggerActivity implements View.OnClickListener, ClickDragDismissListener.ActionUpListener {

    private Activity mActivity;
    //
    private PopupCustom popSelectShare, popMore;
    private PopPicker popPicker;
    private PopBoard board;
    private ItemAlbum itemAlbum;

    private ShareTask shareTask;
    private CheckShareTask checkShareTask;
    private GetAlbumInfoTask getAlbumInfoTask;

    private FirstCollectAlbumTask firstCollectAlbumTask;
    private GetReportTask getReportTask;
    private SendReportTask sendReportTask;
    private GetPointTask getPointTask;
    private SendLikeTask sendLikeTask;
    private DeleteLikeTask deleteLikeTask;
    private Protocol13_BuyAlbum protocol13;
    private Protocol100_Vote protocol100;

//    private GyroscopeObserver gyroscopeObserver;

    private RelativeLayout rLocation, rDetail;
    private LinearLayout linEvent, linType, linAuthor;

    private TextView tvAlbumName, tvAlbumAuthor, tvViewed, tvLocation, tvEvent, tvAlbumDescription, tvMessageCount, tvLikeCount, tvVote;
    private TextView tvRead;
    private RoundCornerImageView coverImg;
    private ImageView backImg, messageImg, likeImg, moreImg;
    private ImageView signalVideoImg, signalSlotImg, signalAudioImg;
    private RoundCornerImageView userImg;
//    private View vGradient;

    private ArrayList<HashMap<String, Object>> reportList;
    private List<String> strReportList;


    private String id, token, album_id;
    private String myDir;
    private String p23Result, p23Message;
    private String p68Result, p68Message;
    private String p69Result, p69Message;
    private String p70Result, p70Message;
    private String p83Result, p83Message;
    private String p90Message = "";
    private String coverUrl;
    private String strReportSelect;
    private String report_id;
    private String event, event_id, strEventName, strEventUrl;
    private String strJsonPhoto;
    private String strPicture;

    private int p90Result = -1;
    private int round = 0, count = 0;
    private int loadCount = 0;
    private int intVoteCount;
    private int userP;

    private int doingType;
    private static final int DoShareTask = 10000;
    private static final int DoCheckShare = 10002;
    private static final int DoCollectTask = 10004;
    private static final int DoGetReport = 10006;
    private static final int DoSendReport = 10007;
    private static final int DoGetPoint = 10008;

    private boolean bVotestatus = false;//投票狀態 => false: 未投票, true: 已投票
    //    private boolean isVideo = false, isExchange = false, isSlot = false, isAudio = false;
    private boolean isReturn = false;
    private boolean isFBShareComplate = false;
    private boolean shareElement = true;

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_info);

        getStatusControl().setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));

        supportPostponeEnterTransition();

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        setScrollControl();

        setShareElementAnim();

    }

    private void doing() {
        //2017.08.19 用戶專區返回可能無目的地
        SystemUtility.SysApplication.getInstance().addActivity(this);
        getBundleAlbum_id();
        init();
        doGetAlbumInfo();
    }

    private void setShareElementAnim() {
        coverUrl = getIntent().getExtras().getString(Key.cover, "");
        coverImg = (RoundCornerImageView) findViewById(R.id.coverImg);


        int orientation = getIntent().getExtras().getInt(Key.image_orientation, 0);


        if (orientation == ItemAlbum.LANDSCAPE) {

            MyLog.Set("e", getClass(), "ItemAlbum.LANDSCAPE");

            RelativeLayout rImageArea = (RelativeLayout) findViewById(R.id.rImageArea);

            LinearLayout.LayoutParams params = null;
            if (SystemUtility.isTablet(getApplicationContext())) {
                params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), SizeUtils.dp2px(440));
            } else {
                params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), SizeUtils.dp2px(240));
            }


            rImageArea.setLayoutParams(params);

        } else if (orientation == ItemAlbum.PORTRAIT) {

            MyLog.Set("e", getClass(), "ItemAlbum.PORTRAIT");
            RelativeLayout rImageArea = (RelativeLayout) findViewById(R.id.rImageArea);
            LinearLayout.LayoutParams params = null;
            if (SystemUtility.isTablet(getApplicationContext())) {
                params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), SizeUtils.dp2px(800));
            } else {
                params = new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(), SizeUtils.dp2px(360));
            }


            rImageArea.setLayoutParams(params);

        } else {

            if (SystemUtility.isTablet(getApplicationContext())) {
                MyLog.Set("e", getClass(), "560");
            } else {
                MyLog.Set("e", getClass(), "300");
            }

        }


        if (SystemUtility.Above_Equal_V5()) {
            coverImg.setTransitionName(coverUrl);
        }


        if (!coverUrl.equals("")) {
            Picasso.with(getApplicationContext())
                    .load(coverUrl)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.bg_2_0_0_no_image)
                    .noFade()
                    .tag(getApplicationContext())
                    .into(coverImg, new Callback() {
                        @Override
                        public void onSuccess() {

                            MyLog.Set("d", AlbumInfo2Activity.class, "onSuccess !coverurl => " + coverUrl);

                            supportStartPostponedEnterTransition();


//                            Picasso
//                                    .with(getApplicationContext())
//                                    .load(R.drawable.ic200_act_close_dark)
//                                    .noFade()
//                                    .noPlaceholder()
//                                    .into(coverImg);


                            doing();

                        }

                        @Override
                        public void onError() {

                            MyLog.Set("d", AlbumInfo2Activity.class, "onError coverurl => " + coverUrl);

                            supportStartPostponedEnterTransition();
                            doing();
                        }
                    });
        } else {

            supportStartPostponedEnterTransition();
            doing();

        }

    }

    private void setScrollControl() {

        rDetail = (RelativeLayout) findViewById(R.id.rDetail);

        ParallaxScrollView parallaxScrollView = (ParallaxScrollView) findViewById(R.id.parallaxScrollView);

        parallaxScrollView.setScrollDismissView(findViewById(R.id.linContents));

        parallaxScrollView.setAlphaView(findViewById(R.id.linAlpha), rDetail);

        parallaxScrollView.setActivity(this);

        parallaxScrollView.setCloseActivityListener(new ParallaxScrollView.CloseActivityListener() {
            @Override
            public void close() {

                back();

            }
        });

    }

    private void getBundleAlbum_id() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            album_id = bundle.getString(Key.album_id, "");
            isReturn = bundle.getBoolean("return", false);
            shareElement = bundle.getBoolean(Key.shareElement, true);
        }

    }

    private void init() {

        mActivity = this;

        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();
        myDir = PPBApplication.getInstance().getMyDir();

        reportList = new ArrayList<>();
        strReportList = new ArrayList<>();

        rLocation = (RelativeLayout) findViewById(R.id.rLocation);
//        rAuthor = (RelativeLayout) findViewById(R.id.rAuthor);
        linAuthor = (LinearLayout) findViewById(R.id.linAuthor);

        linEvent = (LinearLayout) findViewById(R.id.linEvent);
        linType = (LinearLayout) findViewById(R.id.linType);


        tvAlbumName = (TextView) findViewById(R.id.tvName);
        tvAlbumAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvAlbumDescription = (TextView) findViewById(R.id.tvDescription);
        tvViewed = (TextView) findViewById(R.id.tvViewed);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvRead = (TextView) findViewById(R.id.tvRead);
        tvEvent = (TextView) findViewById(R.id.tvEvent);
        tvMessageCount = (TextView) findViewById(R.id.tvMessageCount);
        tvLikeCount = (TextView) findViewById(R.id.tvLikeCount);
        tvVote = (TextView) findViewById(R.id.tvVote);

//        coverImg = (ImageView) findViewById(R.id.coverImg);
        userImg = (RoundCornerImageView) findViewById(R.id.userImg);
        signalVideoImg = (ImageView) findViewById(R.id.signalVideoImg);
        signalSlotImg = (ImageView) findViewById(R.id.signalSlotImg);
        signalAudioImg = (ImageView) findViewById(R.id.signalAudioImg);
        backImg = (ImageView) findViewById(R.id.backImg);
        messageImg = (ImageView) findViewById(R.id.messageImg);
        likeImg = (ImageView) findViewById(R.id.likeImg);
        moreImg = (ImageView) findViewById(R.id.moreImg);


        TextUtility.setBold(tvAlbumName, true);
        TextUtility.setBold(tvAlbumAuthor, true);
        TextUtility.setBold(tvRead, true);
        TextUtility.setBold(tvEvent, true);
        TextUtility.setBold(tvVote, true);


//        try {
//            if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {
//                vGradient.setBackground(
//                        ScrimUtil.makeCubicGradientScrimDrawable(
//                                Color.parseColor(ColorClass.WHITE), //颜色
//                                2, //渐变层数
//                                Gravity.BOTTOM)); //起始方向
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            if (SystemUtility.getSystemVersion() >= SystemUtility.V4_4) {

                if (rDetail != null) {
                    rDetail.setBackground(
                            ScrimUtil.makeCubicGradientScrimDrawable(
                                    Color.parseColor(ColorClass.BLACK_ALPHA),
                                    8, //渐变层数
                                    Gravity.BOTTOM)); //起始方向

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        (findViewById(R.id.linLikeCount)).setOnClickListener(this);
        tvMessageCount.setOnClickListener(this);
        backImg.setOnClickListener(this);

    }

    private void selectShareMode() {

        popSelectShare = new PopupCustom(mActivity);
        popSelectShare.setPopup(R.layout.pop_2_0_0_select_share, R.style.pinpinbox_popupAnimation_bottom);

        TextView tvShareFB = (TextView) popSelectShare.getPopupView().findViewById(R.id.tvShareFB);
        TextView tvShare = (TextView) popSelectShare.getPopupView().findViewById(R.id.tvShare);

        TextUtility.setBold((TextView) popSelectShare.getPopupView().findViewById(R.id.tvTitle), true);
        TextUtility.setBold(tvShareFB, true);
        TextUtility.setBold(tvShare, true);

        View vContents = popSelectShare.getPopupView().findViewById(R.id.linBackground);

        tvShareFB.setOnTouchListener(new ClickDragDismissListener(vContents, this));
        tvShare.setOnTouchListener(new ClickDragDismissListener(vContents, this));


        popSelectShare.show((RelativeLayout) findViewById(R.id.rBackground));


    }

    private void taskShare() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {


            String shareUrl = "";

            if (event.equals("") || event == null || event.equals("null")) {
                shareUrl = UrlClass.shareAlbumUrl + album_id + "&autoplay=1";
            } else {
                shareUrl = UrlClass.shareAlbumUrl + album_id;
            }


            if (coverUrl != null && !coverUrl.equals("") && !coverUrl.equals("null")) {

                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(shareUrl))
                        .setImageUrl(Uri.parse(coverUrl))
                        .build();
                shareDialog.show(content);

            } else {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(shareUrl))
                        .build();
                shareDialog.show(content);
            }


        } else {

            MyLog.Set("e", getClass(), "-----------------***********************-------------------");
        }

    }

    private void checkBuyPoint() {

        final DialogV2Custom d = new DialogV2Custom(mActivity);
        d.getrTopBackground().setBackgroundResource(R.drawable.border_2_0_0_dialog_check);
        d.getBgImg().setImageResource(R.drawable.icon_2_0_0_dialog_pinpin);
        d.getLinBottom().setOrientation(LinearLayout.HORIZONTAL);
        d.getTvCenter().setVisibility(View.GONE);

        d.getTvMessage().setText(R.string.pinpinbox_2_0_0_dialog_message_point_insufficient);


        ViewControl.setMargins(d.getTvLeftOrTop(), 0, 0, DensityUtility.dip2px(mActivity.getApplicationContext(), 8), 0);
        ViewControl.setMargins(d.getTvRightOrBottom(), DensityUtility.dip2px(mActivity.getApplicationContext(), 8), 0, 0, 0);

        d.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_dialog_go_buy_point);
        d.getTvRightOrBottom().setTextColor(Color.parseColor(ColorClass.WHITE));
        d.getTvRightOrBottom().setBackgroundResource(R.drawable.click_2_0_0_main_button_radius);

        d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_be_later);
        d.getTvLeftOrTop().setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
        d.getTvLeftOrTop().setBackgroundResource(R.drawable.click_2_0_0_default);

        d.getBlurView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        d.getTvRightOrBottom().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mActivity, BuyPoint2Activity.class);
                mActivity.startActivity(intent);
                ActivityAnim.StartAnim(mActivity);


            }
        });

        d.getTvLeftOrTop().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        d.show();

    }

    private void showPop() {

        /*default select*/

        MyLog.Set("d", mActivity.getClass(), "strReportSelect => " + strReportSelect);
        popPicker = new PopPicker(mActivity);
        popPicker.setPopup();
        popPicker.setTitle(R.string.pinpinbox_2_0_0_pop_title_what_to_report);
        popPicker.setPickerData(strReportList);

        popPicker.getTvConfirm().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strReportSelect = popPicker.getStrSelect();

                if (strReportSelect == null || strReportSelect.equals("null") || strReportSelect.equals("")) {
                    strReportSelect = (String) reportList.get(reportList.size() / 2).get("name");
                }

                MyLog.Set("d", mActivity.getClass(), "strReportSelect => " + strReportSelect);

                int count = reportList.size();

                for (int i = 0; i < count; i++) {
                    String name = (String) reportList.get(i).get("name");
                    if (strReportSelect.equals(name)) {
                        report_id = (String) reportList.get(i).get("reportintent_id");
                        break;
                    }
                }

                MyLog.Set("d", mActivity.getClass(), "reportintent_id => " + report_id);

                doSendReport();

            }
        });

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                popPicker.getPickerView().setSelected(reportList.size()/2);
//                strReportSelect = (String) reportList.get(reportList.size() / 2).get("name");
//                MyLog.Set("d", mActivity.getClass(), "strReportSelect =========> " + strReportSelect);
//            }
//        },500);


        popPicker.show((RelativeLayout) findViewById(R.id.rBackground));

    }


    private void systemShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        if (coverUrl != null && !coverUrl.equals("")) {
//            Uri u = Uri.parse(coverUrl);
//            intent.putExtra(Intent.EXTRA_STREAM, u);
//        }
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");


        if (event.equals("") || event == null || event.equals("null")) {
            intent.putExtra(Intent.EXTRA_TEXT, itemAlbum.getName() + " , " + UrlClass.shareAlbumUrl + album_id + "&autoplay=1");
        } else {
            intent.putExtra(Intent.EXTRA_TEXT, itemAlbum.getName() + " , " + UrlClass.shareAlbumUrl + album_id);
        }


        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(Intent.createChooser(intent, mActivity.getTitle()));

    }

    private void backCheck() {
        if (SystemUtility.getSystemVersion() >= SystemUtility.V5 && shareElement) {
            coverImg.setRadius(16);//(px)
            supportFinishAfterTransition();
        } else {
            finish();
            overridePendingTransition(0, R.anim.bottom_exit);
        }
    }

    private void back() {

        boolean isTeach = PPBApplication.getInstance().getData().getBoolean(TaskKeyClass.first_to_scroll_side, false);

        if (!isTeach) {


            final Dialog mDialog = new Dialog(mActivity, R.style.myDialog);
            mDialog.getWindow().setWindowAnimations(R.style.dialog_enter_exit);
            mDialog.getWindow().setContentView(R.layout.dialog_2_0_0_teach_scroll_side);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                mDialog.getWindow().setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
            }


            TextView tv1 = (TextView) mDialog.findViewById(R.id.tv1);
            TextView tv2 = (TextView) mDialog.findViewById(R.id.tv2);
            TextView tv3 = (TextView) mDialog.findViewById(R.id.tv3);
            TextView tv4 = (TextView) mDialog.findViewById(R.id.tv4);
            TextView tv5 = (TextView) mDialog.findViewById(R.id.tv5);


            TextUtility.setBold(tv1, true);
            TextUtility.setBold(tv2, true);
            TextUtility.setBold(tv3, true);
            TextUtility.setBold(tv4, true);
            TextUtility.setBold(tv5, true);

            CloseClick closeClick = new CloseClick(mDialog);

            mDialog.findViewById(R.id.rBackground).setOnClickListener(closeClick);

            tv1.setOnClickListener(closeClick);
            tv2.setOnClickListener(closeClick);
            tv3.setOnClickListener(closeClick);
            tv4.setOnClickListener(closeClick);
            tv5.setOnClickListener(closeClick);

            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                    PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.first_to_scroll_side, true).commit();
                    backCheck();


                }
            });
            mDialog.show();

        } else {

            backCheck();

        }
    }


    private class CloseClick implements View.OnClickListener {

        private Dialog dialog;

        private CloseClick(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View view) {
            dialog.dismiss();
        }
    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoShareTask:
                        doShareTask();
                        break;

                    case DoingTypeClass.DoGetAlbumInfo:
                        doGetAlbumInfo();
                        break;

                    case DoCheckShare:
                        doCheckShare();
                        break;

                    case DoingTypeClass.DoCollectAlbum:
                        doCollectAlbum();
                        break;

                    case DoCollectTask:
                        doCollectTask();
                        break;

                    case DoGetReport:
                        doGetReport();
                        break;

                    case DoSendReport:
                        doSendReport();
                        break;

                    case DoGetPoint:
                        doGetPoint();
                        break;


                    case DoingTypeClass.DoSendLike:
                        doSendLike();
                        break;

                    case DoingTypeClass.DoDeleteLike:
                        doDeleteLike();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    private void doShareTask() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        shareTask = new ShareTask();
        shareTask.execute();

    }

    public void doGetAlbumInfo() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getAlbumInfoTask = new GetAlbumInfoTask();
        getAlbumInfoTask.execute();

    }

    private void doCheckShare() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        checkShareTask = new CheckShareTask();
        checkShareTask.execute();

    }

    private void doCollectAlbum() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        protocol13 = new Protocol13_BuyAlbum(
                mActivity,
                id,
                token,
                album_id,
                "google",
                itemAlbum.getPoint() + "",
                new Protocol13_BuyAlbum.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        doingType = DoingTypeClass.DoCollectAlbum;
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success() {
                        itemAlbum.setOwn(true);


                        if (itemAlbum.getPoint() == 0) {

                            PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_added_to_collect);

                            doCollectTask();

                        } else {

                            PinPinToast.showSponsorToast(
                                    mActivity.getApplicationContext(),
                                    itemAlbum.getUser_name() + getResources().getString(R.string.pinpinbox_2_0_0_toast_message_thank_by_sponsor),
                                    itemAlbum.getUser_picture()
                            );

                            doCollectTask();

                        }
                    }

                    @Override
                    public void IsOwnAlbum() {

                    }

                    @Override
                    public void TimeOut() {
                        doCollectAlbum();
                    }
                });

    }

    private void doCollectTask() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        firstCollectAlbumTask = new FirstCollectAlbumTask();
        firstCollectAlbumTask.execute();

    }

    private void doGetReport() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getReportTask = new GetReportTask();
        getReportTask.execute();

    }

    private void doSendReport() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        sendReportTask = new SendReportTask();
        sendReportTask.execute();


    }

    private void doGetPoint() {

        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }

        getPointTask = new GetPointTask();
        getPointTask.execute();

    }

    private void doSendLike() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        sendLikeTask = new SendLikeTask();
        sendLikeTask.execute();
    }

    private void doDeleteLike() {
        if (!HttpUtility.isConnect(this)) {
            setNoConnect();
            return;
        }
        deleteLikeTask = new DeleteLikeTask();
        deleteLikeTask.execute();
    }

    private class GetAlbumInfoTask extends AsyncTask<Void, Void, Object> {

        private int p08Result = -1;
        private String p08Message = "";
        private String getCover = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoGetAlbumInfo;
            startLoading();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String strJson = "";

            try {

                Map<String, String> data = new HashMap<String, String>();
                data.put(Key.id, id);
                data.put(Key.token, token);
                data.put(Key.album_id, album_id);
                String sign = IndexSheet.encodePPB(data);
                Map<String, String> sendData = new HashMap<String, String>();
                sendData.put(Key.id, id);
                sendData.put(Key.token, token);
                sendData.put(Key.album_id, album_id);

                   /*判斷是否增加過瀏覽次數*/
                String addviewedList = PPBApplication.getInstance().getData().getString("addviewed", "");

                try {

                    if (!addviewedList.equals("")) {
                        List<Object> albumIdList = StringUtil.StringToList(addviewedList);
                        boolean isAdd = false;
                        for (int i = 0; i < albumIdList.size(); i++) {
                            String aid = (String) albumIdList.get(i);
                            if (aid.equals(album_id)) {
                                isAdd = true;
                                break;
                            }
                        }

                        if (!isAdd) {

                            MyLog.Set("d", this.getClass(), "瀏覽次數 + 1");

                            sendData.put("viewed", "1");
                            albumIdList.add(album_id);
                            PPBApplication.getInstance().getData().edit().putString("addviewed", StringUtil.ListToString(albumIdList)).commit();

                        } else {
                            MyLog.Set("d", this.getClass(), "今天已增加過瀏覽次數");
                        }

                    } else {

                        MyLog.Set("d", this.getClass(), "addviewed為空的");

                        List<String> list = new ArrayList<>();
                        list.add(album_id);
                        PPBApplication.getInstance().getData().edit().putString("addviewed", StringUtil.ListToString(list)).commit();
                        sendData.put("viewed", "1");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                sendData.put(Key.sign, sign);

                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P08_RetrieveAlbumProfile, sendData, null);//08


                MyLog.Set("d", mActivity.getClass(), "p08strJson =>" + strJson);

                Logger.json(strJson);


            } catch (UnknownHostException uhe) {

                //重新啟動APP

            } catch (SocketTimeoutException timeout) {
                p08Result = Key.TIMEOUT;
            } catch (Exception e) {

                MyLog.Set("e", mActivity.getClass(), "getMessage = > " + e.getMessage());

                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p08Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p08Result == 1) {

                        itemAlbum = new ItemAlbum();

                        String data = jsonObject.getString(ProtocolKey.data);

                        JSONObject object = new JSONObject(data);

                        String album = JsonUtility.GetString(object, ProtocolKey.album);
                        String user = JsonUtility.GetString(object, ProtocolKey.user);
                        String albumstatistics = JsonUtility.GetString(object, ProtocolKey.albumstatistics);
                        String photo = JsonUtility.GetString(object, ProtocolKey.photo);

                        JSONObject jsonAlbum = new JSONObject(album);
                        JSONObject jsonUser = new JSONObject(user);
                        JSONObject jsonAlbumstatistics = new JSONObject(albumstatistics);

                    /*album detail*/
                        itemAlbum.setName(JsonUtility.GetString(jsonAlbum, ProtocolKey.name));

//                        itemAlbum.setDescription(JsonUtility.GetString(jsonAlbum, ProtocolKey.description));

                        /*原始字串*/
                        String strDescription = JsonUtility.GetString(jsonAlbum, ProtocolKey.description);

                        /*加入已去除字串前後方空格的字串*/
                        itemAlbum.setDescription(strDescription.trim());


                        itemAlbum.setLocation(JsonUtility.GetString(jsonAlbum, ProtocolKey.location));
                        itemAlbum.setOwn(JsonUtility.GetBoolean(jsonAlbum, ProtocolKey.own));
                        itemAlbum.setPoint(JsonUtility.GetInt(jsonAlbum, ProtocolKey.point));
                        itemAlbum.setCount_photo(JsonUtility.GetInt(jsonAlbum, ProtocolKey.count_photo));
                        itemAlbum.setIs_likes(JsonUtility.GetBoolean(jsonAlbum, ProtocolKey.is_likes));

                        String usefor = JsonUtility.GetString(jsonAlbum, ProtocolKey.usefor);
                        JSONObject jsonUsefor = new JSONObject(usefor);
                        itemAlbum.setExchange(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.exchange));
                        itemAlbum.setSlot(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.slot));
                        itemAlbum.setVideo(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.video));
                        itemAlbum.setAudio(JsonUtility.GetBoolean(jsonUsefor, ProtocolKey.audio));

                    /*user detail*/
                        itemAlbum.setUser_name(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                        itemAlbum.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));
                        itemAlbum.setUser_picture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));

                    /*albumstatistics*/
                        itemAlbum.setViewed(JsonUtility.GetInt(jsonAlbumstatistics, ProtocolKey.viewed));
                        itemAlbum.setLikes(JsonUtility.GetInt(jsonAlbumstatistics, ProtocolKey.likes));
                        itemAlbum.setMessageboard(JsonUtility.GetInt(jsonAlbumstatistics, ProtocolKey.messageboard));

                    /*photo*/
                        try {
                            JSONArray jsonArray = new JSONArray(photo);
                            int array = jsonArray.length();
                            for (int i = 0; i < array; i++) {
                                JSONObject obj = (JSONObject) jsonArray.get(i);
                                String image_url = obj.getString("image_url");

                                if (image_url != null && !image_url.equals("") && !image_url.equals("null")) {
                                    getCover = image_url;
                                    break;
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        /**2016.09.14新增*/
                        strJsonPhoto = object.getString("photo");

                    /*event*/
                        event = object.getString("event");

                        if (event.equals("") || event == null || event.equals("null")) {

                        } else {

                            JSONObject ej = new JSONObject(event);
                            event_id = ej.getString("event_id");
                            strEventName = ej.getString("name");
                            strEventUrl = ej.getString("url");
                            bVotestatus = ej.getBoolean("votestatus");


                         /*eventjoin  票數 有活動再接即可*/
                            String eventjoin = object.getString("eventjoin");
                            JSONObject ejj = new JSONObject(eventjoin);
                            intVoteCount = ejj.getInt("count");
                        }

                    } else if (p08Result == 0) {
                        p08Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return null;
        }



        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p08Result == 1) {


                /*作品名稱*/
                tvAlbumName.setText(itemAlbum.getName());

                /*作者*/
                if (itemAlbum.getUser_id() == StringIntMethod.StringToInt(id)) {
                    linAuthor.setVisibility(View.GONE);
                } else {
                    linAuthor.setVisibility(View.VISIBLE);
                    tvAlbumAuthor.setText(itemAlbum.getUser_name());

                    strPicture = itemAlbum.getUser_picture();

                    try {
                        if (SystemUtility.Above_Equal_V5()) {
                            userImg.setTransitionName(strPicture);
                        }
                        if (strPicture != null && !strPicture.equals("") && !strPicture.equals("null")) {
                            Picasso.with(mActivity.getApplicationContext())
                                    .load(strPicture)
                                    .config(Bitmap.Config.RGB_565)
                                    .error(R.drawable.member_back_head)
                                    .tag(mActivity.getApplicationContext())
                                    .into(userImg);
                        } else {
                            userImg.setImageResource(R.drawable.member_back_head);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }




                /*作品介紹*/
                if (itemAlbum.getDescription().equals("")) {
                    tvAlbumDescription.setVisibility(View.GONE);
                } else {
                    LinkText.set(mActivity, tvAlbumDescription, LinkText.defaultColor, LinkText.defaultColorOfHighlightedLink, itemAlbum.getDescription());
                }


                try {

                     /*瀏覽數*/
                    if (itemAlbum.getViewed() > 9999) {
                        String strViewed = StringUtil.ThousandToK(itemAlbum.getViewed());
                        tvViewed.setText(strViewed + "K" + getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_views));
                    } else {

                        tvViewed.setText(itemAlbum.getViewed() + getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_views));

                    }

                        /*讚數*/
                    if (itemAlbum.getLikes() > 9999) {
                        String strLikes = StringUtil.ThousandToK(itemAlbum.getLikes());
                        tvLikeCount.setText(strLikes + "K");
                    } else {
                        tvLikeCount.setText(itemAlbum.getLikes() + "");
                    }

                     /*留言數*/
                    if (itemAlbum.getMessageboard() > 9999) {
                        String strMessageboard = StringUtil.ThousandToK(itemAlbum.getMessageboard());
                        tvMessageCount.setText(strMessageboard + "K" + getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_message));
                    } else {
                        tvMessageCount.setText(itemAlbum.getMessageboard() + getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_message));
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                /*是否讚過*/
                if (itemAlbum.is_likes()) {

                    likeImg.setImageResource(R.drawable.ic200_like_main);

                } else {
                    likeImg.setImageResource(R.drawable.ic200_like_dark);
                }


                /*地址*/
                if (itemAlbum.getLocation() == null || itemAlbum.getLocation().equals("")) {
                    rLocation.setVisibility(View.GONE);
                    itemAlbum.setLocation("");
                } else {
                    rLocation.setVisibility(View.VISIBLE);
                    tvLocation.setText(itemAlbum.getLocation());
                }


                /*封面*/
                if (coverUrl.equals("")) {
                    coverUrl = getCover;
                    if (coverUrl != null && !coverUrl.equals("") && !coverUrl.equals("null")) {
                        Picasso.with(mActivity.getApplicationContext())
                                .load(coverUrl)
                                .config(Bitmap.Config.RGB_565)
                                .error(R.drawable.bg_2_0_0_no_image)
                                .tag(mActivity.getApplicationContext())
                                .into(coverImg);

                    } else {
                        coverImg.setImageResource(R.drawable.bg_2_0_0_no_image);
                    }

                }


                /*是否是自己的*/
                if (id.equals(itemAlbum.getUser_id() + "")) {
                    tvAlbumAuthor.setClickable(false);
                }

                /*活動相關*/
                if (event.equals("") || event == null || event.equals("null")) {
                    linEvent.setVisibility(View.GONE);

                } else {


                    linEvent.setVisibility(View.VISIBLE);
                      /*活動名稱*/
                    tvEvent.setText(strEventName);

                    if (bVotestatus) {
                        tvVote.setText(R.string.pinpinbox_2_0_0_other_text_voted);
                        tvVote.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                    } else {
                        tvVote.setText(R.string.pinpinbox_2_0_0_button_voting_for_this_work);
                        tvVote.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                    }


                    if (SystemUtility.checkActivityExist(Event2Activity.class.getSimpleName())) {

                        linEvent.setVisibility(View.GONE);

                    }


                    TouchRange.let(tvVote)
                            // easy to use, like css padding
                            .bounds()//default value is 14dp
//                            .bounds(24.0f) //left,top,right,bottom=24dp
//                .bounds(24.0f,30.0f) //left,right=24dp top,bottom=30dp
//                .bounds(24.0f,30.0f,24.0f) //left=24dp,top=30dp,right=24dp,bottom=0dp
//                .bounds(24.0f, 30.0f, 24.0f, 30.0f) //left=24dp,top=30dp,right=24dp,bottom=30dp
                            .change();

                }


                try {
                    /*2016.09.14新增*/
                    if (itemAlbum.isVideo()) {
                        signalVideoImg.setVisibility(View.VISIBLE);
                    } else {
                        signalVideoImg.setVisibility(View.GONE);
                    }

                    if (itemAlbum.isExchange()) {
                        signalSlotImg.setVisibility(View.VISIBLE);
                    } else {
                        signalSlotImg.setVisibility(View.GONE);
                    }

                    if (itemAlbum.isSlot()) {
                        signalSlotImg.setVisibility(View.VISIBLE);
                    } else {
                        signalSlotImg.setVisibility(View.GONE);
                    }

                    if (itemAlbum.isAudio()) {
                        signalAudioImg.setVisibility(View.VISIBLE);
                    } else {
                        signalAudioImg.setVisibility(View.GONE);
                    }


                    if (!itemAlbum.isVideo() && !itemAlbum.isExchange() && !itemAlbum.isSlot() && !itemAlbum.isAudio()) {
                        linType.setVisibility(View.INVISIBLE);//要佔據畫面 不可用gone
                    } else {
                        linType.setVisibility(View.VISIBLE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                ViewControl.AlphaTo1(findViewById(R.id.linAlpha), 200);

                if (rDetail != null) {
                    ViewControl.AlphaTo1(rDetail, 200);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Bundle bundle = getIntent().getExtras();

                        boolean pinpinboard = bundle.getBoolean(Key.pinpinboard, false);

                        if (pinpinboard) {
                            if (board == null) {

                                board = new PopBoard(mActivity, PopBoard.TypeAlbum, album_id, (RelativeLayout) findViewById(R.id.rBackground), false);
                                board.setTvCount(tvMessageCount);

                            } else {

                                board.clearList();
                                board.doGetBoard();

                            }
                        }

                    }
                }, 200);


                linAuthor.setOnTouchListener(new ScaleTouhListener(new ScaleTouhListener.TouchCallBack() {
                    @Override
                    public void Touch() {

                    }

                    @Override
                    public void Up() {

                        if(ClickUtils.ButtonContinuousClick()){
                            return;
                        }

                        FlurryUtil.onEvent(FlurryKey.albuminfo_click_user);

                        if (isReturn) {
                            back();
                        } else {

                            toAuthor();

                        }
                    }
                }));


                tvRead.setOnClickListener(AlbumInfo2Activity.this);
//                linAuthor.setOnClickListener(AlbumInfo2Activity.this);
                linEvent.setOnClickListener(AlbumInfo2Activity.this);
                coverImg.setOnClickListener(AlbumInfo2Activity.this);
                messageImg.setOnClickListener(AlbumInfo2Activity.this);
                likeImg.setOnClickListener(AlbumInfo2Activity.this);
                moreImg.setOnClickListener(AlbumInfo2Activity.this);
                tvVote.setOnClickListener(AlbumInfo2Activity.this);


            } else if (p08Result == 0) {

                DialogV2Custom d = new DialogV2Custom(mActivity);
                d.setStyle(DialogStyleClass.ERROR);
                d.setMessage(p08Message);
                d.show();
                d.getmDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {


                        if (SystemUtility.getSystemVersion() >= SystemUtility.V5) {
                            supportFinishAfterTransition();
                        } else {

                            MyLog.Set("d", getClass(), "手機版本小於5.0");

                            finish();
                            overridePendingTransition(0, R.anim.bottom_exit);

                        }
                    }
                });


            } else if (p08Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }

    }

    private class CheckShareTask extends AsyncTask<Void, Void, Object> {

        private int p84Result = -1;
        private String p84Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoCheckShare;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {
            String strJson = "";

            try {


                HashMap<String, String> map = new HashMap<>();
                map.put(Key.type, Value.album);
                map.put(Key.type_id, album_id);

                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P84_CheckTaskCompleted, SetMapByProtocol.setParam84_checktaskcompleted(id, token, TaskKeyClass.share_to_fb, "google", map), null);
                MyLog.Set("d", getClass(), "p84strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p84Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p84Result = JsonUtility.GetInt(jsonObject, Key.result);

                    if (p84Result == 1) {
                    } else if (p84Result == 2) {
                        p84Message = jsonObject.getString(Key.message);
                    } else if (p84Result == 0) {
                        p84Message = jsonObject.getString(Key.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p84Result == 1) {
                /*任務已完成*/
                systemShare();


            } else if (p84Result == 2) {

                /*尚有次數未完成*/
                selectShareMode();

            } else if (p84Result == 0) {

                systemShare();


            } else if (p84Result == Key.TIMEOUT) {

                connectInstability();

            } else {

                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());

            }


        }

    }

    private class ShareTask extends AsyncTask<Void, Void, Object> {

        private String restriction;
        private String restriction_value;
        private String name;
        private String reward;
        private String reward_value;
        private String url;

        private int numberofcompleted;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoShareTask;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            Map<String, String> map = new HashMap<>();
            map.put(MapKey.id, id);
            map.put(MapKey.token, token);
            map.put(MapKey.task_for, TaskKeyClass.share_to_fb);
            map.put(MapKey.platform, "google");
            String sign = IndexSheet.encodePPB(map);
            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(MapKey.id, id);
            sendData.put(MapKey.token, token);
            sendData.put(MapKey.task_for, TaskKeyClass.share_to_fb);
            sendData.put(MapKey.platform, "google");
            sendData.put(MapKey.type, "album");
            sendData.put(MapKey.type_id, album_id);
            sendData.put("sign", sign);

            String strJson = "";


            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                MyLog.Set("d", mActivity.getClass(), "p83strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p83Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = jsonObject.getString(Key.result);

                    if (p83Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);

                        JSONObject object = new JSONObject(jdata);

                        String task = object.getString(Key.task);
                        String event = object.getString(Key.event);

                        JSONObject taskObj = new JSONObject(task);
                        name = taskObj.getString(Key.name);
                        reward = taskObj.getString(Key.reward);
                        reward_value = taskObj.getString(Key.reward_value);
                        restriction = taskObj.getString(Key.restriction);
                        restriction_value = taskObj.getString(Key.restriction_value);

                        numberofcompleted = taskObj.getInt(Key.numberofcompleted);

                        JSONObject eventObj = new JSONObject(event);
                        url = eventObj.getString(Key.url);

                    } else if (p83Result.equals("2")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("3")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("0")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else {
                        p83Result = "";
                    }
                } catch (Exception e) {
                    p83Result = "";
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p83Result.equals("1")) {

                final DialogHandselPoint d = new DialogHandselPoint(mActivity);

                if (restriction.equals("personal")) {
                    d.getTvTitle().setText(name);
                    d.getTvRestriction().setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_count) + numberofcompleted + "/" + restriction_value);
                    d.getTvRestriction().setVisibility(View.VISIBLE);
                } else {
                    d.getTvTitle().setText(name);
                }

                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");
                    /*獲取當前使用者P點*/
                    String point = PPBApplication.getInstance().getData().getString(Key.point, "");
                    int p = StringIntMethod.StringToInt(point);

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    PPBApplication.getInstance().getData().edit().putString(Key.point, newP).commit();


                } else {
                    d.getTvDescription().setText(reward_value);
                }


                if (url == null || url.equals("") || url.equals("null")) {
                    d.getTvLink().setVisibility(View.GONE);
                } else {
                    d.getTvLink().setVisibility(View.VISIBLE);
                    d.getTvLink().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putString("url", url);
                            Intent intent = new Intent(mActivity, WebView2Activity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            ActivityAnim.StartAnim(mActivity);
                        }
                    });
                }


            } else if (p83Result.equals("2")) {


            } else if (p83Result.equals("3")) {


            } else if (p83Result.equals("0")) {


            } else if (p83Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    private class GetPointTask extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetPoint;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(id, token, "google"), null);

                MyLog.Set("d", getClass(), "p23strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p23Result = Key.timeout;
            } catch (Exception e) {

                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p23Result = jsonObject.getString("result");
                    if (p23Result.equals("1")) {

                        String userPoint = jsonObject.getString("data");

                        userP = StringIntMethod.StringToInt(userPoint);

                    } else if (p23Result.equals("0")) {
                        p23Message = jsonObject.getString(Key.message);
                    } else {
                        p23Result = "";
                    }
                } catch (Exception e) {
                    p23Result = "";
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p23Result.equals("1")) {

                if (userP < itemAlbum.getPoint()) {
                    MyLog.Set("d", getClass(), "P點不足 前去買點");
                    checkBuyPoint();
                } else {
                    MyLog.Set("d", getClass(), "P點足夠 進行購買");

                    doCollectAlbum();

                }


            } else if (p23Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p23Message);

            } else if (p23Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    private class FirstCollectAlbumTask extends AsyncTask<Void, Void, Object> {

        private String restriction;
        private String restriction_value;
        private String name;
        private String reward;
        private String reward_value;
        private String url;

        private int numberofcompleted;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoCollectTask;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {


            Map<String, String> map = new HashMap<>();
            map.put(Key.id, id);
            map.put(Key.token, token);

            if (itemAlbum.getPoint() == 0) {
                map.put(Key.task_for, TaskKeyClass.collect_free_album);
            } else {
                map.put(Key.task_for, TaskKeyClass.collect_pay_album);
            }

            map.put(Key.platform, "google");
            String sign = IndexSheet.encodePPB(map);

            Map<String, String> sendData = new HashMap<String, String>();
            sendData.put(Key.id, id);
            sendData.put(Key.token, token);


            if (itemAlbum.getPoint() == 0) {
                sendData.put(Key.task_for, TaskKeyClass.collect_free_album);
            } else {
                sendData.put(Key.task_for, TaskKeyClass.collect_pay_album);
            }

            sendData.put(Key.platform, "google");
            sendData.put(Key.type, "album");
            sendData.put(Key.type_id, album_id);
            sendData.put(Key.sign, sign);


            String strJson = "";
            try {
                if (itemAlbum.getPoint() == 0) {
                    strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                } else {
                    strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P83_DoTask, sendData, null);
                }
                MyLog.Set("d", getClass(), "p83strJson => " + strJson);

            } catch (SocketTimeoutException timeout) {
                p83Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p83Result = jsonObject.getString(Key.result);

                    if (p83Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);

                        JSONObject object = new JSONObject(jdata);

                        String task = object.getString(Key.task);
                        String event = object.getString(Key.event);

                        JSONObject taskObj = new JSONObject(task);
                        name = taskObj.getString(Key.name);
                        reward = taskObj.getString(Key.reward);
                        reward_value = taskObj.getString(Key.reward_value);
                        restriction = taskObj.getString(Key.restriction);
                        restriction_value = taskObj.getString(Key.restriction_value);
                        numberofcompleted = taskObj.getInt(Key.numberofcompleted);

                        JSONObject eventObj = new JSONObject(event);
                        url = eventObj.getString(Key.url);

                    } else if (p83Result.equals("2")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else if (p83Result.equals("0")) {
                        p83Message = jsonObject.getString(Key.message);
                    } else {
                        p83Result = "";
                    }
                } catch (Exception e) {
                    p83Result = "";
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();

            if (p83Result.equals("1")) {

                final DialogHandselPoint d = new DialogHandselPoint(mActivity);

                if (restriction.equals("personal")) {
                    d.getTvTitle().setText(name);
                    d.getTvRestriction().setText(getResources().getString(R.string.pinpinbox_2_0_0_other_text_count) + numberofcompleted + "/" + restriction_value);
                    d.getTvRestriction().setVisibility(View.VISIBLE);
                } else {
                    d.getTvTitle().setText(name);
                }


                if (reward.equals("point")) {
                    d.getTvDescription().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_task_get_point) + reward_value + "P!");
                    /*獲取當前使用者P點*/
                    String point = PPBApplication.getInstance().getData().getString(Key.point, "");
                    int p = StringIntMethod.StringToInt(point);

                    /*任務獲得P點轉換型態*/
                    int rp = StringIntMethod.StringToInt(reward_value);

                    /*加總*/
                    String newP = StringIntMethod.IntToString(rp + p);

                    /*儲存data*/
                    PPBApplication.getInstance().getData().edit().putString(Key.point, newP).commit();

//                    if (itemAlbum.getPoint() == 0) {
//                        PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.collect_free_album, false).commit();
//                    } else {
//                        PPBApplication.getInstance().getData().edit().putBoolean(TaskKeyClass.collect_pay_album, false).commit();
//                    }

                } else {
                    d.getTvDescription().setText(reward_value);
                }


                d.getTvLink().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        Intent intent = new Intent(mActivity, WebView2Activity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        ActivityAnim.StartAnim(mActivity);
                    }
                });


            } else if (p83Result.equals("2")) {

            } else if (p83Result.equals("0")) {

            } else if (p83Result.equals("3")) {

            } else if (p83Result.equals(Key.timeout)) {

                connectInstability();

            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    private class GetReportTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoGetReport;
            startLoading();
        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P69_GetReportintentlist,
                        SetMapByProtocol.setParam69_getreportintentlist(id, token), null);
                MyLog.Set("d", getClass(), "p69strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p69Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p69Result = jsonObject.getString(Key.result);
                    if (p69Result.equals("1")) {

                        String jdata = jsonObject.getString(Key.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        int array = jsonArray.length();
                        for (int i = 0; i < array; i++) {
                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            String reportintent_id = obj.getString("reportintent_id");
                            String name = obj.getString("name");
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("reportintent_id", reportintent_id);
                            map.put("name", name);
                            reportList.add(map);
                            strReportList.add(name);
                        }

                    } else if (p69Result.equals("0")) {
                        p69Message = jsonObject.getString(Key.message);
                    } else {
                        p69Result = "";
                    }

                } catch (Exception e) {
                    p69Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            dissmissLoading();


            if (p69Result.equals("1")) {
                showPop();
            } else if (p69Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p69Message);
            } else if (p69Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }

    private class SendReportTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoSendReport;
            startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P70_InsertReport,
                        SetMapByProtocol.setParam70_insertreport(id, token, report_id, "album", album_id), null);

                MyLog.Set("d", getClass(), "p70strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p70Result = Key.timeout;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p70Result = jsonObject.getString("result");
                    if (p70Result.equals("1")) {

                    } else if (p70Result.equals("0")) {
                        p70Message = jsonObject.getString("message");
                    } else {
                        p70Result = "";
                    }

                } catch (Exception e) {
                    p70Result = "";
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p70Result.equals("1")) {

                popPicker.dismiss();

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_send_report_success);

            } else if (p70Result.equals("0")) {
                DialogV2Custom.BuildError(mActivity, p70Message);
            } else if (p70Result.equals(Key.timeout)) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }


        }
    }

    private class SendLikeTask extends AsyncTask<String, Integer, Integer> {

        private String p92Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSendLike;
            startLoading();

        }


        @Override
        protected Integer doInBackground(String... strings) {

            int result = -1;

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P92_InsertAlbum2Likes,
                        SetMapByProtocol.setParam92_insertalbum2likes(
                                id, token, album_id), null);

                MyLog.Set("d", getClass(), "p92strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (result == 0) {
                        p92Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (result == 1) {

                setIsLike();

            } else if (result == 0) {
                DialogV2Custom.BuildError(mActivity, p92Message);
            } else if (result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }
    }

    private class DeleteLikeTask extends AsyncTask<String, Integer, Integer> {

        private String p93Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDeleteLike;
            startLoading();

        }


        @Override
        protected Integer doInBackground(String... strings) {

            int result = -1;

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P93_DeleteAlbum2Likes,
                        SetMapByProtocol.setParam93_deletealbum2likes(
                                id, token, album_id), null);

                MyLog.Set("d", getClass(), "p93strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {

                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (result == 0) {
                        p93Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            dissmissLoading();

            if (result == 1) {

                setDeleteLike();

            } else if (result == 0) {
                DialogV2Custom.BuildError(mActivity, p93Message);
            } else if (result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }
        }

    }


    @Override
    public void onClick(View view) {

        if (ClickUtils.ButtonContinuousClick()) {//1秒內防止連續點擊
            return;
        }
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        switch (view.getId()) {

            case R.id.tvRead:
                FlurryUtil.onEvent(FlurryKey.albuminfo_click_read);
                readAlbum();
                break;

            case R.id.coverImg:
                FlurryUtil.onEvent(FlurryKey.albuminfo_click_coverImg);
                readAlbum();
                break;

            case R.id.linAuthor:

                FlurryUtil.onEvent(FlurryKey.albuminfo_click_user);

                if (isReturn) {
                    back();
                } else {

                    toAuthor();

                }

                break;

            case R.id.linEvent:
                toEvent();
                break;

            case R.id.backImg:
                back();
                break;

            case R.id.messageImg:


                FlurryUtil.onEvent(FlurryKey.albuminfo_click_board);

                if (board == null) {

                    board = new PopBoard(mActivity, PopBoard.TypeAlbum, album_id, (RelativeLayout) findViewById(R.id.rBackground), false);
                    board.setTvCount(tvMessageCount);

                } else {

                    board.clearList();
                    board.doGetBoard();

                }


                break;

            case R.id.likeImg:


                FlurryUtil.onEvent(FlurryKey.albuminfo_click_like);

                if (itemAlbum.is_likes()) {

                    doDeleteLike();

                } else {

                    doSendLike();

                }


                break;

            case R.id.moreImg:

                showMore();

                break;

            case R.id.linLikeCount:

                toLikesList();

                break;

            case R.id.tvMessageCount:

                if (board == null) {

                    board = new PopBoard(mActivity, PopBoard.TypeAlbum, album_id, (RelativeLayout) findViewById(R.id.rBackground), false);
                    board.setTvCount(tvMessageCount);

                } else {

                    board.clearList();
                    board.doGetBoard();

                }


                break;

            case R.id.tvVote:


                if (bVotestatus) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_other_text_voted);
                } else {


                    DialogV2Custom d = new DialogV2Custom(mActivity);
                    d.setStyle(DialogStyleClass.CHECK);
                    d.setMessage(getResources().getString(R.string.pinpinbox_2_0_0_other_text_vote_for) + itemAlbum.getName() + "]?");
                    d.setCheckExecute(new CheckExecute() {
                        @Override
                        public void DoCheck() {
                            doVote(0);
                        }
                    });
                    d.show();

                }

                break;


        }

    }


    @Override
    public void OnClick(View v) {
        if (ClickUtils.ButtonContinuousClick()) {
            return;
        }

        switch (v.getId()) {

            case R.id.tvShareFB:

                popSelectShare.dismiss();
                if (!isFBShareComplate) {

                          /*設置facebook api*/
                    FacebookSdk.sdkInitialize(getApplicationContext());
                    callbackManager = CallbackManager.Factory.create();

                       /*設置facebook share api*/
                    shareDialog = new ShareDialog(mActivity);
                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

                        @Override
                        public void onSuccess(Sharer.Result result) {
                            doShareTask();
                            MyLog.Set("d", mActivity.getClass(), "shareDialog => onSuccess");
                        }

                        @Override
                        public void onCancel() {
                            MyLog.Set("d", mActivity.getClass(), "shareDialog => onCancel");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            MyLog.Set("d", mActivity.getClass(), "shareDialog => onError");
                        }
                    });

                    isFBShareComplate = true;
                }

                taskShare();


                break;

            case R.id.tvShare:

                popSelectShare.dismiss();
                systemShare();

                break;


            case R.id.linEditWork:
                popMore.dismiss();
                toCreation();
                break;

            case R.id.linEditInfo:

                popMore.dismiss();
                toAlbumInfo();

                break;

            case R.id.linCollect:

                if (!itemAlbum.isOwn()) {
                    popMore.dismiss();
                    FlurryUtil.onEvent(FlurryKey.albuminfo_click_collect);
                    collectAlbum();
                }

                break;

            case R.id.linShare:
                popMore.dismiss();
                FlurryUtil.onEvent(FlurryKey.albuminfo_click_share);
                share();
                break;

            case R.id.linReport:
                popMore.dismiss();
                report();
                break;


        }

    }

    @Override
    public void OnDismiss() {

        if (popSelectShare != null && popSelectShare.getPopupWindow().isShowing()) {
            popSelectShare.dismiss();
        }

        if (popMore != null && popMore.getPopupWindow().isShowing()) {
            popMore.dismiss();
        }


    }


    /*click*/
    private void collectAlbum() {

        if (itemAlbum.getPoint() == 0) {
            doCollectAlbum();
        } else {

            final DialogV2Custom d = new DialogV2Custom(mActivity);
            d.setStyle(DialogStyleClass.CHECK);
            d.getTvMessage().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_message_confirm_sponsor) + itemAlbum.getPoint() + "P?");
            CheckExecute checkExecute = new CheckExecute() {
                @Override
                public void DoCheck() {
                    d.dismiss();
                    doGetPoint();
                }
            };
            d.setCheckExecute(checkExecute);
            d.show();

        }
    }

    /*click*/
    private void readAlbum() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);
        Intent intent = new Intent(mActivity, Reader2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);


    }

    /*click*/
    private void report() {
        doGetReport();
    }

    /*click*/
    private void share() {

        doCheckShare();

    }

    /*click*/
    private void toAuthor() {


//        ActivityIntent.toUser(
//                mActivity,
//                true,
//                StringIntMethod.IntToString(itemAlbum.getUser_id()),
//                itemAlbum.getUser_picture(),
//                itemAlbum.getUser_name(),
//                userImg
//        );

        Bundle bundle = new Bundle();
        bundle.putString(Key.author_id, itemAlbum.getUser_id() + "");
        bundle.putString(Key.picture, itemAlbum.getUser_picture());
        bundle.putString(Key.name, itemAlbum.getUser_name());

        if (SystemUtility.Above_Equal_V5()) {


            bundle.putBoolean(Key.shareElement, true);
            bundle.putBoolean(Key.from_album_info, true);

            userImg.setTransitionName(itemAlbum.getUser_picture());



            Pair<View, String> pImg = Pair.create(findViewById(R.id.userImg), itemAlbum.getUser_picture());
            Pair<View, String> pBg = Pair.create(findViewById(R.id.linAuthor), linAuthor.getTransitionName());
            Pair<View, String> pName = Pair.create(findViewById(R.id.tvAuthor), tvAlbumAuthor.getTransitionName());


            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, pImg, pBg, pName);

//            ActivityOptionsCompat ccc = ActivityOptionsCompat
//                    .makeScaleUpAnimation(
//                            linAuthor,
//                            (int)linAuthor.getX(),
//                            (int)linAuthor.getY(),
//                            0,
//                            0);
//
//            ccc.update(compat);


            Intent intent = new Intent(mActivity, Author2Activity.class).putExtras(bundle);
            intent.putExtras(bundle);


            ActivityCompat.startActivity(mActivity, intent, compat.toBundle());

            /*無效果*/
//            ActivityOptionsCompat option1 = ActivityOptionsCompat.
//                    makeSceneTransitionAnimation(mActivity,
//                            userImg,
//                            ViewCompat.getTransitionName(userImg));
//
//
//            ActivityOptionsCompat option2 = ActivityOptionsCompat.
//                    makeSceneTransitionAnimation(mActivity,
//                            linAuthor,
//                            ViewCompat.getTransitionName(linAuthor));
//
//            ActivityOptionsCompat option3 = ActivityOptionsCompat.
//                    makeSceneTransitionAnimation(mActivity,
//                            tvAlbumAuthor,
//                            ViewCompat.getTransitionName(tvAlbumAuthor));
//
//            option2.update(option1);
//            option3.update(option2);
//
//            startActivity(intent, option3.toBundle());

        } else {

            bundle.putBoolean(Key.shareElement, false);

            Intent intent = new Intent(mActivity, Author2Activity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            ActivityAnim.StartAnim(mActivity);

        }

    }

    /*click*/
    private void showMore() {

        popMore = new PopupCustom(mActivity);
        popMore.setPopup(R.layout.pop_2_0_0_albuminfo_more, R.style.pinpinbox_popupAnimation_bottom);
        View v = popMore.getPopupView();


        LinearLayout linEditContent = (LinearLayout) v.findViewById(R.id.linEditContent);

        LinearLayout linEditWork = (LinearLayout) v.findViewById(R.id.linEditWork);
        LinearLayout linEditInfo = (LinearLayout) v.findViewById(R.id.linEditInfo);

        LinearLayout linCollect = (LinearLayout) v.findViewById(R.id.linCollect);
        LinearLayout linShare = (LinearLayout) v.findViewById(R.id.linShare);
        LinearLayout linReport = (LinearLayout) v.findViewById(R.id.linReport);

        TextUtility.setBold((TextView) v.findViewById(R.id.tvEditWork), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvEditInfo), true);

        TextUtility.setBold((TextView) v.findViewById(R.id.tvTitle), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvShare), true);
        TextUtility.setBold((TextView) v.findViewById(R.id.tvReport), true);

        TextView tvCollect = (TextView) v.findViewById(R.id.tvCollect);
        TextUtility.setBold(tvCollect, true);

        View vContent = v.findViewById(R.id.linBackground);

        linEditWork.setOnTouchListener(new ClickDragDismissListener(vContent, this));
        linEditInfo.setOnTouchListener(new ClickDragDismissListener(vContent, this));
        linCollect.setOnTouchListener(new ClickDragDismissListener(vContent, this));
        linShare.setOnTouchListener(new ClickDragDismissListener(vContent, this));
        linReport.setOnTouchListener(new ClickDragDismissListener(vContent, this));


        if (id.equals(itemAlbum.getUser_id() + "")) {

            linReport.setVisibility(View.GONE);
            linCollect.setVisibility(View.GONE);

            //20171114
            linEditContent.setVisibility(View.VISIBLE);

        } else {

            //20171114
            linEditContent.setVisibility(View.GONE);

            linReport.setVisibility(View.VISIBLE);
            linCollect.setVisibility(View.VISIBLE);
            if (itemAlbum.isOwn()) {
                tvCollect.setText(R.string.pinpinbox_2_0_0_itemtype_collected);
                tvCollect.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));
                linCollect.setClickable(false);
            } else {
                tvCollect.setTextColor(Color.parseColor(ColorClass.GREY_FIRST));
                if (itemAlbum.getPoint() > 0) {

                    tvCollect.setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_itemtype_collect_need_sponsor) + itemAlbum.getPoint() + "P)");

                    TextView tvSponsorMore = (TextView) v.findViewById(R.id.tvSponsorMore);
                    tvSponsorMore.setVisibility(View.VISIBLE);
                    tvSponsorMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popMore.dismiss();

                            DialogV2Custom d = new DialogV2Custom(mActivity);
                            d.setMessage(R.string.pinpinbox_2_0_0_dialog_message_click_read_and_go_to_last_page_can_set_point);
                            d.setStyle(DialogStyleClass.CHECK);
                            d.getTvRightOrBottom().setVisibility(View.GONE);
                            d.getTvLeftOrTop().setText(R.string.pinpinbox_2_0_0_dialog_i_know);
                            d.show();

                        }
                    });

                } else {
                    tvCollect.setText(R.string.pinpinbox_2_0_0_itemtype_collect);
                }


            }
        }


        popMore.show((RelativeLayout) findViewById(R.id.rBackground));

    }

    /*click*/
    private void toEvent() {
        ActivityIntent.toEvent(mActivity, event_id);
    }

    /*click*/
    private void toCreation() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);
        bundle.putString(Key.identity, "admin");
//        String strTemplate_id = (String) p17arraylist.get(position).get(Key.template_id);
//        if (strTemplate_id.equals("0")) {
//            bundle.putInt(Key.create_mode, 0);
//        } else {
//            bundle.putInt(Key.create_mode, 1);
//        }

        bundle.putInt(Key.create_mode, 0);
        Intent intent = new Intent(mActivity, Creation2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);

    }

    /*click*/
    private void toAlbumInfo() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);
        bundle.putString(Key.identity, "admin");
        bundle.putBoolean(Key.audio, itemAlbum.isAudio());

        Intent intent = new Intent(mActivity, AlbumSettings2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);

    }

    /*click*/
    private void toLikesList() {

        Bundle bundle = new Bundle();
        bundle.putString(Key.album_id, album_id);

        Intent intent = new Intent(mActivity, LikeList2Activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        ActivityAnim.StartAnim(mActivity);

    }

    /*click*/
    private void doVote(final int position) {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        protocol100 = new Protocol100_Vote(
                mActivity,
                id,
                token,
                event_id,
                album_id,
                new Protocol100_Vote.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        startLoading();
                    }

                    @Override
                    public void Post() {
                        dissmissLoading();
                    }

                    @Override
                    public void Success(String vote_left) {

                        bVotestatus = true;

                        tvVote.setText(R.string.pinpinbox_2_0_0_other_text_voted);
                        tvVote.setTextColor(Color.parseColor(ColorClass.GREY_SECOND));

                    }

                    @Override
                    public void TimeOut() {
                        doVote(position);
                    }
                }
        );

    }


    public String getAlbum_id() {
        return this.album_id;
    }

    public void setOwn(boolean own) {
        this.itemAlbum.setOwn(own);
    }

    public void setIsLike() {
        int count = StringIntMethod.StringToInt(tvLikeCount.getText().toString());
        tvLikeCount.setText((count + 1) + "");
        likeImg.setImageResource(R.drawable.ic200_like_main);
        itemAlbum.setIs_likes(true);
    }

    public void setDeleteLike() {
        int count = StringIntMethod.StringToInt(tvLikeCount.getText().toString());
        tvLikeCount.setText((count - 1) + "");
        likeImg.setImageResource(R.drawable.ic200_like_dark);
        itemAlbum.setIs_likes(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        MyLog.Set("e", getClass(), "requestCode => " + requestCode);
        MyLog.Set("e", getClass(), "resultCode => " + resultCode);
        MyLog.Set("e", getClass(), "data => " + data.toString());

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {


        try {
            SystemUtility.SysApplication.getInstance().removeActivity(mActivity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (board != null && board.getPopupWindow().isShowing()) {
            board.dismiss();
        }

        Recycle.IMG(backImg);
        Recycle.IMG(signalVideoImg);
        Recycle.IMG(signalAudioImg);
        Recycle.IMG(signalSlotImg);
        Recycle.IMG(messageImg);
        Recycle.IMG(likeImg);
        Recycle.IMG(moreImg);
        Recycle.IMG((ImageView) findViewById(R.id.imageView13));


        cancelTask(shareTask);
        cancelTask(checkShareTask);

        cancelTask(getAlbumInfoTask);
        cancelTask(firstCollectAlbumTask);
        cancelTask(getReportTask);
        cancelTask(sendReportTask);
        cancelTask(getPointTask);

        cancelTask(sendLikeTask);
        cancelTask(deleteLikeTask);
        cancelTask(protocol13);


        if (coverUrl != null && !coverUrl.equals("") && !coverUrl.equals("null")) {

            Picasso.with(getApplicationContext()).invalidate(coverUrl);
            coverImg.setImageBitmap(null);
            coverImg.setImageDrawable(null);
        } else {
            Recycle.IMG(coverImg);
        }

        if (strPicture != null && !strPicture.equals("") && !strPicture.equals("null")) {

            Picasso.with(getApplicationContext()).invalidate(strPicture);
            userImg.setImageBitmap(null);
            userImg.setImageDrawable(null);
        } else {
            Recycle.IMG(userImg);
        }


        System.gc();
        MyLog.Set("d", this.getClass(), "onDestroy");
        super.onDestroy();
    }

}

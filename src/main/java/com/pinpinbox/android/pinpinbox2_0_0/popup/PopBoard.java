package com.pinpinbox.android.pinpinbox2_0_0.popup;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Utility.StringUtil;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.CircleView.RoundedImageView;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.Views.recyclerview.EndlessRecyclerOnScrollListener;
import com.pinpinbox.android.pinpinbox2_0_0.activity.AlbumInfo2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Author2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Reader2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerBoardAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerTagUserAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemBoard;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemTagUser;
import com.pinpinbox.android.pinpinbox2_0_0.bean.ItemUser;
import com.pinpinbox.android.pinpinbox2_0_0.custom.ClickUtils;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.RadiusBackgroundSpan;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DoingTypeClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.listener.ConnectInstability;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


public class PopBoard {


    private Activity mActivity;
    private CountDownTimer countDownTimer;


    private GetBoardTask getBoardTask;
    private SendBoardTask sendBoardTask;
    private MoreDataTask moreDataTask;
    private UserListTask userListTask;

    private RecyclerBoardAdapter boardAdapter;
    private RecyclerTagUserAdapter tagUserAdapter;

    private List<ItemBoard> itemBoardList;
    private List<ItemUser> itemUserList;
    private List<ItemTagUser> tagsList;


    /***********************************************/
    private PopupWindow popupWindow;
    private RelativeLayout rBackground, rShow;
    private BlurView blurView;
    private View v;
    /***********************************************/
    private RecyclerView rvBoard, rvTag;
    private EditText edText;
    //    private AutoCompleteEditText edText;
    private SmoothProgressBar pbLoadMore;
    private RoundedImageView userImg;
    private TextView tvTitle, tvSecondTitle;

    private TextView tvCount;

    /***********************************************/

    private String type;
    private String type_id;
    private String id, token;
    private String p90Message = "";
    private String strSendText = "";


    private final static int intAnimDuration = 300;
    private int p90Result = -1;
    private int round, count;
    private int loadCount = 0;
    private int doingType;
    private int boardType;
    public final static int TypeAlbum = 1;
    public final static int TypeUser = 2;

    private boolean showToastBySendTextContinuous = true;
    private boolean sizeMax = false;
    private boolean isNoDataToastAppeared = false; //判斷無資料訊息是否出現過
    private boolean isShowRecyclerPadding = false;
    private boolean changeToDarkStatus = true;

    public PopBoard(Activity mActivity, int type, String type_id, RelativeLayout rShow, boolean changeToDarkStatus) {
        this.mActivity = mActivity;
        this.type_id = type_id;
        this.boardType = type;
        this.rShow = rShow;
        this.changeToDarkStatus = changeToDarkStatus;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            mActivity.getWindow().setStatusBarColor(Color.parseColor(ColorClass.TRANSPARENT));
//        }


        id = PPBApplication.getInstance().getId();
        token = PPBApplication.getInstance().getToken();


        initView();

        setBoard();

        setTag();

        setTagListener();

        checkType();

        doGetBoard();


    }

    private void initView() {

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.pop_2_0_0_board_album, null);

        pbLoadMore = (SmoothProgressBar) v.findViewById(R.id.pbLoadMore);
        rvBoard = (RecyclerView) v.findViewById(R.id.rvBoard);
        rvTag = (RecyclerView) v.findViewById(R.id.rvTag);
        edText = (EditText) v.findViewById(R.id.edText);
        userImg = (RoundedImageView) v.findViewById(R.id.userImg);
        tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        tvSecondTitle = (TextView) v.findViewById(R.id.tvSecondTitle);


        popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.pinpinbox_popupAnimation_bottom);

        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        /*設置關閉時執行*/
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {


                if (changeToDarkStatus) {
                    ((DraggerActivity) mActivity).setCurrentActivityStatusMode();
                }


                if (getBoardTask != null && !getBoardTask.isCancelled()) {
                    getBoardTask.cancel(true);
                }
                getBoardTask = null;

                if (moreDataTask != null && !moreDataTask.isCancelled()) {
                    moreDataTask.cancel(true);
                }
                moreDataTask = null;

                if (sendBoardTask != null && !sendBoardTask.isCancelled()) {
                    sendBoardTask.cancel(true);
                }
                sendBoardTask = null;

                if (userListTask != null && !userListTask.isCancelled()) {
                    userListTask.cancel(true);
                }
                userListTask = null;


                int count = itemBoardList.size();
                if (count > 0) {
                    for (int i = 0; i < count; i++) {
                        Picasso.with(mActivity.getApplicationContext()).invalidate(itemBoardList.get(i).getPicture());
                    }
                }
                /*恢復背景*/
                resetBackground();

                if (tvCount != null) {

                  /*留言數*/
                    if (count > 9999) {
                        String strMessageboard = StringUtil.ThousandToK(count);
                        tvCount.setText(strMessageboard + "K" + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_message));
                    } else {
                        tvCount.setText(count + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_text_times_message));
                    }
                }
            }
        });

    }

    private void setTag() {

        itemUserList = new ArrayList<>();

        rvTag.setHorizontalFadingEdgeEnabled(true);
        rvTag.setFadingEdgeLength(32);

        final LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        rvTag.setLayoutManager(manager);
        tagUserAdapter = new RecyclerTagUserAdapter(mActivity, itemUserList);
        rvTag.setAdapter(tagUserAdapter);


        tagsList = new ArrayList<>();


        tagUserAdapter.setOnRecyclerViewListener(new RecyclerTagUserAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (ClickUtils.ButtonContinuousClick()) {
                    return;
                }


                /*判斷是否是自己*/
                if (tagsList != null && tagsList.size() > 0) {
                    for (int i = 0; i < tagsList.size(); i++) {
                        if ((tagsList.get(i).getUser_id()).equals(id)) {
                            PinPinToast.showErrorToast(mActivity, "不可標記自己");
                            return;
                        }
                    }
                }


                /*判斷是否已經tag*/
                if (tagsList != null && tagsList.size() > 0) {
                    for (int i = 0; i < tagsList.size(); i++) {
                        if ((tagsList.get(i).getUser_id()).equals(itemUserList.get(position).getUser_id())) {
                            PinPinToast.showErrorToast(mActivity, "不可重複標記");
                            return;
                        }
                    }
                }

                /*判斷前字符是否為tag*/
                boolean isTag = false;
                if (tagsList != null && tagsList.size() > 0) {
                    int cursorIndex = edText.getSelectionStart();
                    MyLog.Set("e", PopBoard.class, "cursorIndex => " + cursorIndex);
                    for (int i = 0; i < tagsList.size(); i++) {
                        if (cursorIndex == tagsList.get(i).getEndIndex() + 2) {
                            isTag = true;
                            break;
                        }
                    }
                }

                if (isTag) {
                    //評估是否添加一空格
                    MyLog.Set("e", PopBoard.class, "isTag");
                    edText.setText(edText.getText().toString().replaceFirst("@" + strSendText, itemUserList.get(position).getName()));
                } else {
                    MyLog.Set("e", PopBoard.class, "!isTag");
                    edText.setText(edText.getText().toString().replaceFirst(" @" + strSendText, itemUserList.get(position).getName()));
                }

                /*建立tag*/
                ItemTagUser tags = new ItemTagUser();
                tags.setName(itemUserList.get(position).getName());
                tags.setUser_id(itemUserList.get(position).getUser_id());

                /*移除name後面空格*/
                String name = itemUserList.get(position).getName().substring(1, itemUserList.get(position).getName().length() - 1);
                tags.setSendType("[" + itemUserList.get(position).getUser_id() + ":" + name + "]");

                /*該次tag位置*/
                Pattern pattern = Pattern.compile(itemUserList.get(position).getName());
                Matcher matcher = pattern.matcher(edText.getText().toString());

                if (matcher.find()) {
                    MyLog.Set("e", PopBoard.class, "matcher.start() => " + matcher.start());
                    MyLog.Set("e", PopBoard.class, "matcher.end() => " + matcher.end());
                    tags.setStartIndex(matcher.start());
                    tags.setEndIndex(matcher.end());
                }

                tagsList.add(tags);


                /*設定字串樣式*/
                SpannableString spanString = new SpannableString(edText.getText());
                for (int i = 0; i < tagsList.size(); i++) {

                    RadiusBackgroundSpan spanBg = new RadiusBackgroundSpan(Color.parseColor(ColorClass.GREY_SECOND), 8, Color.parseColor(ColorClass.PINK_FRIST));
                    spanString.setSpan(spanBg, tagsList.get(i).getStartIndex(), tagsList.get(i).getEndIndex(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                }

                edText.setText(spanString);
                setSelection();

            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }

        });


    }

    private void setTagListener() {

        edText.addTextChangedListener(new TextWatcher() {

//            private boolean isDeleteIng = false;

            private void searchUser() {

                String reg = " @" + "\\S*\\z";

                String text = "";

                String beforeCursor = edText.getText().toString().substring(0, edText.getSelectionStart());

                Pattern pattern = Pattern.compile(reg);
                Matcher matcher = pattern.matcher(beforeCursor);
                if (matcher.find()) {

                    text = matcher.group(0);
                    MyLog.Set("e", PopBoard.class, "text => " + text);

                    strSendText = text.substring(2);

                    MyLog.Set("e", PopBoard.class, "strSendText => " + strSendText);


                    if (strSendText.equals("")) {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            MyLog.Set("d", PopBoard.class, "取消倒數");
                        }
                    } else {

                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer.start();
                            MyLog.Set("d", PopBoard.class, "重新倒數");
                        }
                    }


                } else {
                    MyLog.Set("e", PopBoard.class, "text => " + text);
                    rvTag.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                if (isDeleteIng) {
//                    isDeleteIng = false;
//                    return;
//                }

                String strInput = edText.getText().toString();

                if (tagsList != null && tagsList.size() > 0) {

                    for (int i = 0; i < tagsList.size(); i++) {

                        /*檢查名稱是否還在字串內*/
                        Pattern pattern = Pattern.compile(tagsList.get(i).getName());
                        Matcher matcher = pattern.matcher(strInput);

                        if (!matcher.find()) {

                            MyLog.Set("e", PopBoard.class, "名稱損毀");
//
//                            /*或取損毀後的文字*/
//                            String strDamage= edText.getText().toString().substring(tagsList.get(i).getStartIndex(), tagsList.get(i).getEndIndex()-1);
//                            MyLog.Set("e", PopBoard.class, "strDamage => " + strDamage);
//
//                            /*確認損毀文字位置*/
//                            Pattern dPattern = Pattern.compile(strDamage);
//                            Matcher m = dPattern.matcher(edText.getText().toString());//重新獲取字串
//                            if(m.find()){
//                                int dStartIndex = m.start();
//                                int dEndInedx = m.end();
//
//                                isDeleteIng = true;//連續刪除字串會重複執行afterTextChanged 在此設定斷點
//
//                                edText.getText().delete(dStartIndex, dEndInedx);
//
//                            }
//
                            tagsList.remove(i);

                        } else {

                            MyLog.Set("e", PopBoard.class, "名稱還在");

                            /*重新設置位置*/

                            MyLog.Set("e", PopBoard.class, "reset matcher.start() => " + matcher.start());
                            MyLog.Set("e", PopBoard.class, "reset matcher.end() => " + matcher.end());

                            tagsList.get(i).setStartIndex(matcher.start());
                            tagsList.get(i).setEndIndex(matcher.end());
                        }

                    }

                }


                /*判斷開頭tag*/
                if (strInput.length() == 1 && strInput.substring(0, 1).equals("@")) {
                    edText.setText(" @");
                    setSelection();
                }


                /*搜尋用戶*/
                searchUser();


            }



        });


        countDownTimer = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                MyLog.Set("d", PopBoard.class, "timer =>" + (millisUntilFinished / 1000) + "");
            }

            @Override
            public void onFinish() {
                MyLog.Set("d", PopBoard.class, "timer => finish()");
                countDownTimer.cancel();
                doSearchUser();
            }
        };


    }

    private void setSelection() {
        edText.setSelection(edText.getText().toString().length());
    }

    private void setBoard() {

        /*創建array*/
        itemBoardList = new ArrayList<>();

        /*設定頭像*/
        String profilepic = PPBApplication.getInstance().getData().getString(Key.profilepic, "");
        if (!profilepic.equals("")) {
            Picasso.with(mActivity.getApplicationContext())
                    .load(profilepic)
                    .config(Bitmap.Config.RGB_565)
                    .error(R.drawable.member_back_head)
                    .tag(mActivity.getApplicationContext())
                    .into(userImg);
        } else {
            userImg.setImageResource(R.drawable.member_back_head);
        }

        /*粗體標題*/
        TextUtility.setBold(tvTitle, true);

        /*隱藏讀取更多進度條*/
        pbLoadMore.progressiveStop();

        /*設定留言列表*/
        final LinearLayoutManager manager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        boardAdapter = new RecyclerBoardAdapter(mActivity, itemBoardList);
        rvBoard.setAdapter(boardAdapter);
        rvBoard.setLayoutManager(manager);
        rvBoard.addOnScrollListener(mOnScrollListener);


        /*設定click*/
        v.findViewById(R.id.linBackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        v.findViewById(R.id.tvClear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*模擬送出字串*/
//                String readySendText = edText.getText().toString();
//
//                for (int i = 0; i < tagsList.size(); i++) {
//
//                    Pattern pattern = Pattern.compile(tagsList.get(i).getName());
//                    Matcher matcher = pattern.matcher(readySendText);
//
//                    if (matcher.find()) {
//                        readySendText = readySendText.replaceFirst(tagsList.get(i).getName(), tagsList.get(i).getSendType());
//                    }
//                }
//
//
//                MyLog.Set("e", PopBoard.class, "readySendText => " + readySendText);


                edText.setText("");
            }
        });

        v.findViewById(R.id.tvSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ClickUtils.ButtonContinuousClick_4s()) {//1秒內防止連續點擊
                    if (showToastBySendTextContinuous) {
                        PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_can_not_send_continuous);
                        showToastBySendTextContinuous = false;
                    }
                    return;
                }
                showToastBySendTextContinuous = true;

                if (edText.getText().toString().equals("")) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_text_is_not_fill_in);
                    return;
                }

                doSendText();
            }
        });

    }

    private void checkType() {

        switch (boardType) {
            case TypeAlbum:
                type = Key.album;
                break;

            case TypeUser:
                type = Key.user;
                break;
        }

    }

    private void setNoConnect() {

        String actName = mActivity.getClass().getSimpleName();

        if (actName.equals(AlbumInfo2Activity.class.getSimpleName())) {

            ((AlbumInfo2Activity) mActivity).setNoConnect();

        } else if (actName.equals(Reader2Activity.class.getSimpleName())) {

            ((Reader2Activity) mActivity).setNoConnect();

        } else if (actName.equals(Author2Activity.class.getSimpleName())) {

            ((Author2Activity) mActivity).setNoConnect();

        }

    }

    private void startLoading() {

        String actName = mActivity.getClass().getSimpleName();

        if (actName.equals(AlbumInfo2Activity.class.getSimpleName())) {

            ((AlbumInfo2Activity) mActivity).startLoading();

        } else if (actName.equals(Reader2Activity.class.getSimpleName())) {

            ((Reader2Activity) mActivity).startLoading();

        } else if (actName.equals(Author2Activity.class.getSimpleName())) {

            ((Author2Activity) mActivity).startLoading();

        }
    }

    private void dissmissLoading() {

        String actName = mActivity.getClass().getSimpleName();

        if (actName.equals(AlbumInfo2Activity.class.getSimpleName())) {

            ((AlbumInfo2Activity) mActivity).dissmissLoading();

        } else if (actName.equals(Reader2Activity.class.getSimpleName())) {

            ((Reader2Activity) mActivity).dissmissLoading();

        } else if (actName.equals(Author2Activity.class.getSimpleName())) {

            ((Author2Activity) mActivity).dissmissLoading();

        }


    }

    public void scrollToTop() {

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rvBoard.getLayoutManager();

        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (firstVisibleItemPosition > 10) {
            rvBoard.scrollToPosition(10);
            MyLog.Set("d", getClass(), "先移動至第10項目");
        }

        rvBoard.smoothScrollToPosition(0);
    }

    private void protocol90(String limit) {

        String strJson = "";

        try {
            strJson = HttpUtility.uploadSubmit(ProtocolsClass.P90_GetMessageBoardList,
                    SetMapByProtocol.setParam90_getmessageboardlist(
                            id,
                            token,
                            type,
                            type_id,
                            limit)
                    , null);

            MyLog.Set("d", this.getClass(), "p90strJson => " + strJson);
        } catch (SocketTimeoutException timeout) {
            p90Result = Key.TIMEOUT;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (strJson != null && !strJson.equals("")) {
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                p90Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                if (p90Result == 1) {

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String strCurrentTime = df.format(curDate);

                    String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                    JSONArray jsonArray = new JSONArray(data);

                    loadCount = jsonArray.length();
                    for (int i = 0; i < loadCount; i++) {

                        JSONObject object = (JSONObject) jsonArray.get(i);

                        String user = JsonUtility.GetString(object, ProtocolKey.user);
                        String pinpinboard = JsonUtility.GetString(object, ProtocolKey.pinpinboard);

                        JSONObject jsonUser = new JSONObject(user);
                        JSONObject jsonPinpinboard = new JSONObject(pinpinboard);

                        ItemBoard itemBoard = new ItemBoard();
                        itemBoard.setName(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                        itemBoard.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                        itemBoard.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));

                        itemBoard.setText(JsonUtility.GetString(jsonPinpinboard, ProtocolKey.text));
//                        itemBoard.setText("[284:Drummers David]物語，科科不我是台灣人不是人[165:Kawa Lup][256:author003]");


//                            itemBoard.setInserttime(JsonUtility.GetString(jsonPinpinboard, ProtocolKey.inserttime));

                        Date currentTime = df.parse(strCurrentTime);
                        Date messageTime = df.parse(JsonUtility.GetString(jsonPinpinboard, ProtocolKey.inserttime));

                        long l = currentTime.getTime() - messageTime.getTime();
                        long day = l / (24 * 60 * 60 * 1000);
                        long hour = (l / (60 * 60 * 1000) - day * 24);
                        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
//                            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                        String strMessageTime;
                        if (day != 0) {
//                            strMessageTime = day + "天前";

                            if (day < 2) {
                                strMessageTime = day + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_day_single);
                            } else {
                                strMessageTime = day + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_day);
                            }

                        } else if (hour != 0) {
//                            strMessageTime = hour + "小時前";

                            if (hour < 2) {
                                strMessageTime = hour + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_hour_single);
                            } else {
                                strMessageTime = hour + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_hour);
                            }

                        } else if (min != 0) {
//                            strMessageTime = min + "分鐘前";

                            if (min < 2) {
                                strMessageTime = min + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_minute_single);
                            } else {
                                strMessageTime = min + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_minute);
                            }

                        } else {
                            strMessageTime = mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_just_now);

                        }

                        itemBoard.setInserttime(strMessageTime);


                        itemBoardList.add(itemBoard);
                    }


                } else if (p90Result == 0) {
                    p90Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
                }

            } catch (Exception e) {
                loadCount = 0;
                e.printStackTrace();
            }
        }

    }

    private void connectInstability() {

        ConnectInstability connectInstability = new ConnectInstability() {
            @Override
            public void DoingAgain() {

                switch (doingType) {

                    case DoingTypeClass.DoDefault:
                        doGetBoard();
                        break;

                    case DoingTypeClass.DoMoreData:
                        doMoreData();
                        break;

                    case DoingTypeClass.DoSendBorad:
                        doSendText();
                        break;

                    case DoingTypeClass.DoSearchUserList:
                        doSearchUser();
                        break;

                }
            }
        };

        DialogV2Custom.BuildTimeOut(mActivity, connectInstability);

    }

    public void doGetBoard() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        getBoardTask = new GetBoardTask();
        getBoardTask.execute();

    }

    private void doSendText() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        sendBoardTask = new SendBoardTask();
        sendBoardTask.execute();

    }

    private void doMoreData() {
        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }
        moreDataTask = new MoreDataTask();
        moreDataTask.execute();
    }

    private void doSearchUser() {

        if (!HttpUtility.isConnect(mActivity)) {
            setNoConnect();
            return;
        }

        userListTask = new UserListTask();
        userListTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class GetBoardTask extends AsyncTask<Void, Void, Object> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoDefault;
            startLoading();
            sizeMax = false;
            isNoDataToastAppeared = false;
            round = 0;
            count = 16;

        }

        @Override
        protected Object doInBackground(Void... params) {

            protocol90(round + "," + count);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            dissmissLoading();
            if (p90Result == 1) {

                if (itemBoardList.size() > 0) {

                    if (!isShowRecyclerPadding) {
                        rvBoard.setClipToPadding(false);
                        rvBoard.setPadding(0, 0, 0, DensityUtility.dip2px(mActivity.getApplicationContext(), 16));
                        isShowRecyclerPadding = true;
                    }


                    round = round + count;

                }


                boardAdapter.notifyDataSetChanged();

                show(rShow);


            } else if (p90Result == 0) {
                DialogV2Custom.BuildError(mActivity, p90Message);
            } else if (p90Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MoreDataTask extends AsyncTask<Void, Void, Object> {

        private int moreFirstPosition;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoMoreData;
            pbLoadMore.setVisibility(View.VISIBLE);
            pbLoadMore.progressiveStart();
            moreFirstPosition = itemBoardList.size();
        }

        @Override
        protected Void doInBackground(Void... params) {

            protocol90(round + "," + count);

            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            pbLoadMore.setVisibility(View.GONE);
            pbLoadMore.progressiveStop();
            if (p90Result == 1) {
                if (loadCount == 0) {
                    sizeMax = true; // 已達最大值
                    if (!isNoDataToastAppeared) {
                        PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                        isNoDataToastAppeared = true;
                    }
                    return;
                }

                boardAdapter.notifyItemRangeInserted(itemBoardList.size(), count);

                round = round + count;

                rvBoard.scrollToPosition(moreFirstPosition);


            } else if (p90Result == 0) {
                DialogV2Custom.BuildError(mActivity, p90Message);
            } else if (p90Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SendBoardTask extends AsyncTask<Void, Void, Object> {

        private String p91Message = "";
        private int p91Result = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSendBorad;
            startLoading();

            round = 0;
            count = 16;

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";


            String readySendText = edText.getText().toString();

            for (int i = 0; i < tagsList.size(); i++) {

                Pattern pattern = Pattern.compile(tagsList.get(i).getName());
                Matcher matcher = pattern.matcher(readySendText);

                if (matcher.find()) {
                    readySendText = readySendText.replaceFirst(tagsList.get(i).getName(), tagsList.get(i).getSendType());
                }
            }


            try {
                strJson = HttpUtility.uploadSubmit(ProtocolsClass.P91_InsertMessageBoard,
                        SetMapByProtocol.setParam91_insertmessageboard(
                                id, token, type, type_id, readySendText, round + "," + count)
                        , null);

                MyLog.Set("d", getClass(), "p90strJson => " + strJson);
            } catch (SocketTimeoutException timeout) {
                p91Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p91Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p91Result == 1) {


                        String data = JsonUtility.GetString(jsonObject, ProtocolKey.data);

                        JSONArray jsonArray = new JSONArray(data);

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                        String strCurrentTime = df.format(curDate);

                        itemBoardList.clear();

                        int count = jsonArray.length();
                        for (int i = 0; i < count; i++) {

                            JSONObject object = (JSONObject) jsonArray.get(i);

                            String user = JsonUtility.GetString(object, ProtocolKey.user);
                            String pinpinboard = JsonUtility.GetString(object, ProtocolKey.pinpinboard);

                            JSONObject jsonUser = new JSONObject(user);
                            JSONObject jsonPinpinboard = new JSONObject(pinpinboard);

                            ItemBoard itemBoard = new ItemBoard();
                            itemBoard.setName(JsonUtility.GetString(jsonUser, ProtocolKey.name));
                            itemBoard.setPicture(JsonUtility.GetString(jsonUser, ProtocolKey.picture));
                            itemBoard.setUser_id(JsonUtility.GetInt(jsonUser, ProtocolKey.user_id));

                            itemBoard.setText(JsonUtility.GetString(jsonPinpinboard, ProtocolKey.text));

                            Date currentTime = df.parse(strCurrentTime);
                            Date messageTime = df.parse(JsonUtility.GetString(jsonPinpinboard, ProtocolKey.inserttime));

                            long l = currentTime.getTime() - messageTime.getTime();
                            long day = l / (24 * 60 * 60 * 1000);
                            long hour = (l / (60 * 60 * 1000) - day * 24);
                            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
//                            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
                            String strMessageTime;
                            if (day != 0) {

                                if (day < 2) {
                                    strMessageTime = day + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_day_single);
                                } else {
                                    strMessageTime = day + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_day);
                                }


                            } else if (hour != 0) {

                                if (hour < 2) {
                                    strMessageTime = hour + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_hour_single);
                                } else {
                                    strMessageTime = hour + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_hour);
                                }


                            } else if (min != 0) {


                                if (min < 2) {
                                    strMessageTime = min + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_minute_single);
                                } else {
                                    strMessageTime = min + mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_minute);
                                }

                            } else {
                                strMessageTime = mActivity.getResources().getString(R.string.pinpinbox_2_0_0_time_before_just_now);
                            }

                            itemBoard.setInserttime(strMessageTime);


                            itemBoardList.add(itemBoard);
                        }


                    } else if (p91Result == 0) {
                        p91Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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
            if (p91Result == 1) {

                PinPinToast.showSuccessToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_send_text_success);

                edText.setText("");

                round = round + count;

                if (!isShowRecyclerPadding) {
                    rvBoard.setClipToPadding(false);
                    rvBoard.setPadding(0, 0, 0, DensityUtility.dip2px(mActivity.getApplicationContext(), 16));
                    isShowRecyclerPadding = true;
                }

                boardAdapter.notifyDataSetChanged();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollToTop();
                    }
                }, 200);


                sizeMax = false;
                isNoDataToastAppeared = false;

            } else if (p91Result == 0) {
                DialogV2Custom.BuildError(mActivity, p91Message);
            } else if (p91Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, this.getClass().getSimpleName());
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    private class UserListTask extends AsyncTask<Void, Void, Object> {

        private int p41Result = -1;
        private String p41Message = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            doingType = DoingTypeClass.DoSearchUserList;

            /*執行搜尋前全部移除*/
            tagUserAdapter.notifyItemRangeRemoved(0, itemUserList.size());

            if (itemUserList.size() > 0) {
                itemUserList.clear();
            }

            if (itemUserList.size() > 0) {
                for (int i = 0; i < itemUserList.size(); i++) {
                    String strPicture = itemUserList.get(i).getPicture();
                    Picasso.with(mActivity.getApplicationContext()).invalidate(strPicture);
                }
                itemUserList.clear();
            }

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P41_Search,
                        SetMapByProtocol.setParam41_search(id, token, "user", strSendText, "0,8"), null);
                MyLog.Set("d", PopBoard.class, "p41strJson(user) =>" + strJson);
            } catch (SocketTimeoutException timeout) {
                p41Result = Key.TIMEOUT;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);
                    p41Result = JsonUtility.GetInt(jsonObject, ProtocolKey.result);
                    if (p41Result == 1) {

                        String jdata = JsonUtility.GetString(jsonObject, ProtocolKey.data);
                        JSONArray jsonArray = new JSONArray(jdata);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject j = (JSONObject) jsonArray.get(i);

                            String user = JsonUtility.GetString(j, ProtocolKey.user);
                            JSONObject object = new JSONObject(user);

                            ItemUser itemUser = new ItemUser();
                            itemUser.setName(" " + JsonUtility.GetString(object, ProtocolKey.name) + " ");
                            itemUser.setUser_id(JsonUtility.GetString(object, ProtocolKey.user_id));
                            itemUser.setPicture(JsonUtility.GetString(object, ProtocolKey.picture));


                            if (!itemUser.getUser_id().equals(id)) {
                                itemUserList.add(itemUser);
                            }
                        }


                    } else if (p41Result == 0) {
                        p41Message = jsonObject.getString(Key.message);
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

            if (p41Result == 1) {

                tagUserAdapter.notifyItemRangeInserted(0, itemUserList.size());

                if (itemUserList.size() > 0) {
                    rvTag.setVisibility(View.VISIBLE);
                }


            } else if (p41Result == 0) {
                DialogV2Custom.BuildError(mActivity, p41Message);
            } else if (p41Result == Key.TIMEOUT) {
                connectInstability();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

        }


    }


    private void resetBackground() {
        ViewPropertyAnimator alphaTo0 = blurView.animate();
        alphaTo0.setDuration(intAnimDuration)
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        MyLog.Set("d", PopupCustom.class, "onAnimationEnd");

                                 /*移除模糊背景*/
                        rBackground.removeView(blurView);
                        blurView = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
    }

    public PopupWindow getPopupWindow() {
        return this.popupWindow;
    }

    public void show(RelativeLayout rBackground) {

        if (changeToDarkStatus) {
            ((DraggerActivity) mActivity).setStatusColor(Color.parseColor(ColorClass.TRANSPARENT));
        }


        /*彈出時 設定background*/
        this.rBackground = rBackground;

        /*設定背景模糊*/
        setBlur(rBackground);

        /*彈出並顯示並模糊背景*/
        ViewPropertyAnimator alphaTo1 = blurView.animate();
        alphaTo1.setDuration(intAnimDuration)
                .alpha(1)
                .start();
        popupWindow.showAtLocation(rBackground, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    public void dismiss() {

        popupWindow.dismiss();

    }

    private void setBlur(RelativeLayout rBackground) {

        /*建立模糊視窗*/
        blurView = new BlurView(mActivity);
        blurView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        blurView.setOverlayColor(Color.parseColor("#82000000"));

        final float radius = 4f;
        final View decorView = mActivity.getWindow().getDecorView();
        final View rootView = decorView.findViewById(android.R.id.content);
        final Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView)
                .windowBackground(windowBackground)
                .blurAlgorithm(new RenderScriptBlur(mActivity, true)) //Preferable algorithm, needs RenderScript support mode enabled
                .blurRadius(radius);

        /*先設置為透明*/
        blurView.setAlpha(0);

        /*添加置background*/
        rBackground.addView(blurView);


    }

    public void setSecondTitle(String secTitle) {
        tvSecondTitle.setVisibility(View.VISIBLE);
        tvSecondTitle.setText(secTitle);
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            if (!sizeMax) {
                MyLog.Set("e", PopBoard.class, "onLoad");
                doMoreData();
            } else {

                if (!isNoDataToastAppeared) {
                    PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_scroll_max);
                    isNoDataToastAppeared = true;
                }

                MyLog.Set("e", PopBoard.class, "sizeMax");
            }
        }
    };

    public void clearList() {
        itemBoardList.clear();
    }

    public void setTvCount(TextView tvCount) {
        this.tvCount = tvCount;
    }


}

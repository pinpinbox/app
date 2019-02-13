package com.pinpinbox.android.pinpinbox2_0_0.custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.DensityUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.JsonUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.ProtocolsClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ProtocolKey;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.SetMapByProtocol;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.StringIntMethod;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ViewControl;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol13_BuyAlbum;

import org.json.JSONObject;

import java.net.SocketTimeoutException;


public class CollectionProcess {

    public interface RespondListener {
        void onSuccess();
    }

    public static class Builder {

        private CollectionProcess process;

        private Activity mActivity;
        private String userId;
        private String token;
        private String album_id;
        private String platform;
        private int albumPoint;
        private String param;
        private RespondListener respondListener;

        public Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setAlbumId(String album_id) {
            this.album_id = album_id;
            return this;
        }

        public Builder setPlatform(String platform) {
            this.platform = platform;
            return this;
        }

        public Builder setAlbumPoint(int albumPoint) {
            this.albumPoint = albumPoint;
            return this;
        }

        public Builder setParam(String param) {
            this.param = param;
            return this;
        }


        public Builder setRespondListener(RespondListener listener) {
            this.respondListener = listener;
            return this;
        }


        public Builder run() {

            process = new CollectionProcess(this);
            process.start();

            return this;
        }

        public void clear() {

            process.destroy();
            process = null;

            mActivity = null;
            userId = null;
            token = null;
            album_id  = null;
            platform  = null;
            albumPoint  = 0;
            param  = null;
            respondListener  = null;


        }

    }

    public Builder builder;

    private Activity mActivity;
    private String userId;
    private String token;
    private String album_id;
    private String platform;
    private int albumPoint;
    private String param;
    private RespondListener respondListener;


    private Protocol13_BuyAlbum protocol13;
    private GetPointTask getPointTask;


    private CollectionProcess(Builder builder) {

        this.mActivity = builder.mActivity;
        this.userId = builder.userId;
        this.token = builder.token;
        this.album_id = builder.album_id;
        this.platform = builder.platform;
        this.albumPoint = builder.albumPoint;
        this.param = builder.param;
        this.respondListener = builder.respondListener;
        this.builder = builder;

    }

    private void start() {

        if (albumPoint == 0) {

            doCollectAlbum();

        } else {

            final DialogV2Custom d = new DialogV2Custom(mActivity);

            d.setStyle(DialogStyleClass.CHECK);

            d.getTvMessage().setText(mActivity.getResources().getString(R.string.pinpinbox_2_0_0_other_message_confirm_sponsor) + albumPoint + "P(NTD" + albumPoint/2 + ")?" + "\n" +
            mActivity.getResources().getString(R.string.pinpinbox_2_0_0_dialog_message_click_read_and_go_to_last_page_can_set_point));

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

    private void doCollectAlbum() {

        if (!HttpUtility.isConnect(mActivity)) {
            ((DraggerActivity) mActivity).setNoConnect();
            return;
        }

        protocol13 = new Protocol13_BuyAlbum(
                mActivity,
                userId,
                token,
                album_id,
                platform,
                albumPoint + "",
                param,
                new Protocol13_BuyAlbum.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        ((DraggerActivity) mActivity).startLoading();
                    }

                    @Override
                    public void Post() {
                        ((DraggerActivity) mActivity).dissmissLoading();
                    }

                    @Override
                    public void Success() {

                        respondListener.onSuccess();

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

    private void doGetPoint() {

        if (!HttpUtility.isConnect(mActivity)) {
            ((DraggerActivity) mActivity).setNoConnect();
            return;
        }

        getPointTask = new GetPointTask();
        getPointTask.execute();

    }

    @SuppressLint("StaticFieldLeak")
    private class GetPointTask extends AsyncTask<Void, Void, Object> {

        private String p23Result, p23Message;

        private int userPoint;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ((DraggerActivity) mActivity).startLoading();

        }

        @Override
        protected Object doInBackground(Void... params) {

            String strJson = "";

            try {
                strJson = HttpUtility.uploadSubmit(true, ProtocolsClass.P23_GetUserPoints, SetMapByProtocol.setParam23_getuserpoints(userId, token, platform), null);

                Logger.json(strJson);

            } catch (SocketTimeoutException timeout) {
                p23Result = Key.timeout;
            } catch (Exception e) {

                e.printStackTrace();
            }

            if (strJson != null && !strJson.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(strJson);

                    p23Result = JsonUtility.GetString(jsonObject, ProtocolKey.result);

                    if (p23Result.equals("1")) {

                        userPoint = JsonUtility.GetInt(jsonObject, ProtocolKey.data);

                    } else if (p23Result.equals("0")) {
                        p23Message = JsonUtility.GetString(jsonObject, ProtocolKey.message);
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

            ((DraggerActivity) mActivity).dissmissLoading();

            if (p23Result.equals("1")) {

                //需要在其他地方立即顯示
                PPBApplication.getInstance().getData().edit().putString(Key.point, StringIntMethod.IntToString(userPoint)).commit();

                if (userPoint < albumPoint) {
                    MyLog.Set("d", getClass(), "P點不足 前去買點");
                    checkBuyPoint();
                } else {
                    MyLog.Set("d", getClass(), "P點足夠 進行購買");
                    doCollectAlbum();
                }


            } else if (p23Result.equals("0")) {

                DialogV2Custom.BuildError(mActivity, p23Message);

            } else if (p23Result.equals(Key.timeout)) {
                doGetPoint();
            } else {
                DialogV2Custom.BuildUnKnow(mActivity, getClass().getSimpleName());
            }

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

        d.getDarkBg().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        d.getTvRightOrBottom().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityIntent.toBuyPoint(mActivity);

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

    public void destroy() {

        ((DraggerActivity) mActivity).cancelTask(protocol13);
        ((DraggerActivity) mActivity).cancelTask(getPointTask);

        mActivity = null;

        MyLog.Set("e", getClass(), "all clean");

    }


}

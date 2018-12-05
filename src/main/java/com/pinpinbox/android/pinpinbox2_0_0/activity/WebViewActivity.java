package com.pinpinbox.android.pinpinbox2_0_0.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.Views.DraggerActivity.DraggerScreen.DraggerActivity;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityAnim;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.ActivityIntent;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by vmage on 2015/12/21.
 */
public class WebViewActivity extends DraggerActivity {

    private Activity mActivity;

    private WebView w;
    private TextView tvTitle, tvBack;


    private String url, title;

    private int scrollPosition = -1;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_0_0_webview);


        getBundle();

        init();

        setTitleName();

        setWeb();

        back();


    }

    private void getBundle() {
        Bundle b = getIntent().getExtras();

        if (b != null) {

            url = b.getString(Key.url, "");
            title = b.getString(Key.title, "");

            MyLog.Set("d", this.getClass(), "webview url => " + url);

        }
    }

    private void init() {

        mActivity = this;

        w = (WebView) findViewById(R.id.messageWebView);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvBack = (TextView) findViewById(R.id.tvBack);
//        backImg = (ImageView) findViewById(R.id.web_back);

        TextUtility.setBold(tvTitle, true);

    }

    private void setTitleName() {

        if (title == null || title.equals("")) {
            title = "pinpinbox";
        }

        tvTitle.setText(title);

    }

    private void setWeb() {


        //20171215
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            w.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    MyLog.Set("d", this.getClass(), "scrollX => " + scrollX);
                    MyLog.Set("d", this.getClass(), "scrollY => " + scrollY);
                    MyLog.Set("d", this.getClass(), "oldScrollX => " + oldScrollX);
                    MyLog.Set("d", this.getClass(), "oldScrollY => " + oldScrollY);


                }
            });

        }


        w.loadUrl(url);

        w.canGoBack();

        WebSettings settings = w.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);//必須添加 不然縮放不起作用
        settings.setDisplayZoomControls(false);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);


        w.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器

                MyLog.Set("e", WebViewActivity.class, "urlurl => " + url);


                Uri uri = Uri.parse(url);

                if (url.startsWith("intent")) {

                    if (uri != null) {

                        List<String> strPathPrefix1 = uri.getPathSegments();
                        String param0 = strPathPrefix1.get(0);
                        if (param0.equals("creative")) {

                            String userId = uri.getQueryParameter(Key.user_id);

                            ActivityIntent.toUser(mActivity, false, false, userId, null, null, null);

                            scrollPosition = w.getScrollY();


                        }

                    }

                    return true;

                } else {

                    if (url.startsWith("http") || url.startsWith("https")) {
                        view.loadUrl(url);
                    }


                }


                if (scrollPosition != -1) {
                    w.setScrollY(scrollPosition);
                }


//                String scheme = uri.getScheme();
//
//                String host = uri.getHost();
//
//                String userId = uri.getQueryParameter(Key.user_id);
//
//                MyLog.Set("e", WebViewActivity.class, "scheme => " + scheme);
//                MyLog.Set("e", WebViewActivity.class, "host => " + host);
//                MyLog.Set("e", WebViewActivity.class, "userId => " + userId);


//                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                MyLog.Set("d", WebViewActivity.class, "onPageFinished");

            }


        });

        //优先使用缓存
//        w.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

    }


    private void back() {

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                ActivityAnim.FinishAnim(mActivity);
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else {

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//        ActivityAnim.FinishAnim(mActivity);
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (w.canGoBack()) {
                w.goBack();//返回上一页面
                return true;
            } else {
                finish();
                ActivityAnim.FinishAnim(mActivity);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

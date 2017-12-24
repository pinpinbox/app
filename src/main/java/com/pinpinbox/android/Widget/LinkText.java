package com.pinpinbox.android.Widget;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.URLSpan;
import android.widget.TextView;

import com.pinpinbox.android.pinpinbox2_0_0.activity.WebView2Activity;
import com.pinpinbox.android.R;
import com.pinpinbox.android.StringClass.ColorClass;
import com.pinpinbox.android.pinpinbox2_0_0.libs.link_builder.Link;
import com.pinpinbox.android.pinpinbox2_0_0.libs.link_builder.LinkBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmage on 2016/11/9.
 */
public class LinkText {

    public static final String defaultColorWhite = "#ffffff";
    public static final String defaultColor = ColorClass.GREY_FIRST; //black
    public static final String defaultColorOfHighlightedLink = "#ffffff";

    public static final String inReaderDefaultColor = ColorClass.MAIN_SECOND;
    public static final String inReaderHighLight = ColorClass.MAIN_FIRST;


    public static void set(final Activity mActivity, TextView tv, String textColor, String hightlightColorLink, String des) {

        List<Link> linkList = new ArrayList<>();

        tv.setAutoLinkMask(1);//default web

        tv.setText(des);

        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) tv.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);

            tv.setAutoLinkMask(0);


            for (final URLSpan url : urls) {



                Link link = new Link(url.getURL())
                        .setTextColor(Color.parseColor(textColor))                  // optional, defaults to holo blue
                        .setTextColorOfHighlightedLink(Color.parseColor(hightlightColorLink)) // optional, defaults to holo blue
                        .setHighlightAlpha(.25f)                                     // optional, defaults to .15f
                        .setUnderlined(false)                                       // optional, defaults to true
                        .setBold(true)                                              // optional, defaults to false
                        .setOnLongClickListener(new Link.OnLongClickListener() {
                            @Override
                            public void onLongClick(String clickedText) {
                                ClipboardManager mClipboardManager = (ClipboardManager) mActivity.getSystemService("clipboard");
                                ClipData mClipData = ClipData.newPlainText("Label", url.getURL());
                                mClipboardManager.setPrimaryClip(mClipData);
                                PinPinToast.ShowToast(mActivity, R.string.pinpinbox_2_0_0_toast_message_copied_this_link);
                            }
                        })
                        .setOnClickListener(new Link.OnClickListener() {
                            @Override
                            public void onClick(String clickedText) {
                                // single clicked
                                Intent intent = new Intent(mActivity, WebView2Activity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(Key.url, url.getURL());
                                intent.putExtras(bundle);
                                mActivity.startActivity(intent);
                                ActivityAnim.StartAnim(mActivity);
                            }
                        });

                linkList.add(link);

            }

            if (tv.getText().toString().length() > 0 && linkList.size() > 0) {
                LinkBuilder.on(tv)
                        .addLinks(linkList)
                        .build(); // create the clickable links
            }
        }


    }


}

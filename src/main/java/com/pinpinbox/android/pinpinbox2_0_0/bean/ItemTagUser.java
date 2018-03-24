package com.pinpinbox.android.pinpinbox2_0_0.bean;

/**
 * Created by kevin9594 on 2018/3/24.
 */

public class ItemTagUser {

    private int startIndex = -1;
    private int endIndex = -1;
    private String user_id = "";
    private String name = "";
    private String sendType = "";

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
}

package com.pinpinbox.android.pinpinbox2_0_0.bean;

/**
 * Created by vmage on 2018/4/26.
 */

public class ItemNotify {

    private String identity = "";
    private String image_url = "";
    private String message = "";
    private String target2type = "";
    private String inserttime = "";
    private String url = "";
    private String date = "";
    private int target2type_id = -1;
    private int template_id = -1;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTarget2type() {
        return target2type;
    }

    public void setTarget2type(String target2type) {
        this.target2type = target2type;
    }

    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTarget2type_id() {
        return target2type_id;
    }

    public void setTarget2type_id(int target2type_id) {
        this.target2type_id = target2type_id;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }
}

package com.pinpinbox.android.pinpinbox2_0_0.bean;

import java.io.Serializable;

/**
 * Created by vmage on 2018/1/26.
 */

public class ItemExchange implements Serializable {

    private String description;
    private String image;
    private String name;
    private String time;
//    private String endtime, starttime;

    private int photo_id;
    private int photousefor_id;
    private int photousefor_user_id;


    /*custom*/
    private int imageWidth;
    private int imageHeight;

    private boolean is_existing;
    private boolean has_gained;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getEndtime() {
//        return endtime;
//    }
//
//    public void setEndtime(String endtime) {
//        this.endtime = endtime;
//    }
//
//    public String getStarttime() {
//        return starttime;
//    }
//
//    public void setStarttime(String starttime) {
//        this.starttime = starttime;
//    }

    public int getPhotousefor_id() {
        return photousefor_id;
    }

    public void setPhotousefor_id(int photousefor_id) {
        this.photousefor_id = photousefor_id;
    }

    public int getPhotousefor_user_id() {
        return photousefor_user_id;
    }

    public void setPhotousefor_user_id(int photousefor_user_id) {
        this.photousefor_user_id = photousefor_user_id;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public boolean isHas_gained() {
        return has_gained;
    }

    public void setHas_gained(boolean has_gained) {
        this.has_gained = has_gained;
    }

    public boolean isIs_existing() {
        return is_existing;
    }

    public void setIs_existing(boolean is_existing) {
        this.is_existing = is_existing;
    }


}



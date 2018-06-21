package com.pinpinbox.android.pinpinbox2_0_0.bean;

/**
 * Created by kevin9594 on 2017/4/8.
 */
public class ItemAlbumCategory {


    private String name = "";
    private String colorhex = "";
    private String image_360x360 = "";

    private int categoryarea_id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorhex() {
        return colorhex;
    }

    public void setColorhex(String colorhex) {
        this.colorhex = colorhex;
    }

    public String getImage_360x360() {
        return image_360x360;
    }

    public void setImage_360x360(String image_360x360) {
        this.image_360x360 = image_360x360;
    }

    public int getCategoryarea_id() {
        return categoryarea_id;
    }

    public void setCategoryarea_id(int categoryarea_id) {
        this.categoryarea_id = categoryarea_id;
    }
}

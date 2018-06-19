package com.pinpinbox.android.pinpinbox2_0_0.bean;

/**
 * Created by kevin9594 on 2018/4/17.
 */

public class ItemCategoryBanner {

    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPE_CREATIVE = "creative";

    private String bannerType = "";

    private String imageUrl = "";
    private String imageLink = "";

    private String videoIdByUrl = "";
    private String videoLink = "";
    private String videoText = "";
    private String btnText = "";
    private boolean videoAuto;
    private boolean videoMute;
    private boolean videoRepeat;


    public String getBannerType() {
        return bannerType;
    }

    public void setBannerType(String bannerType) {
        this.bannerType = bannerType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getVideoIdByUrl() {
        return videoIdByUrl;
    }

    public void setVideoIdByUrl(String videoIdByUrl) {
        this.videoIdByUrl = videoIdByUrl;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getVideoText() {
        return videoText;
    }

    public void setVideoText(String videoText) {
        this.videoText = videoText;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public boolean isVideoAuto() {
        return videoAuto;
    }

    public void setVideoAuto(boolean videoAuto) {
        this.videoAuto = videoAuto;
    }

    public boolean isVideoMute() {
        return videoMute;
    }

    public void setVideoMute(boolean videoMute) {
        this.videoMute = videoMute;
    }

    public boolean isVideoRepeat() {
        return videoRepeat;
    }

    public void setVideoRepeat(boolean videoRepeat) {
        this.videoRepeat = videoRepeat;
    }
}

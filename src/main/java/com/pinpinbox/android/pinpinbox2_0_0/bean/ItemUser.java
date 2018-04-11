package com.pinpinbox.android.pinpinbox2_0_0.bean;

import java.io.Serializable;

/**
 * Created by kevin9594 on 2017/3/5.
 */
public class ItemUser implements Serializable {

    private String user_id = "";
    private String name = "";
    private String description = "";
    private String picture = "";
    private String creative_name = "";
    private String identity = "";
    private String cover = "";
    private String ratio = "";
    private String company_identity = "identity";
    private String info_url = "";


    private String facebook = "";
    private String google = "";
    private String instagram = "";
    private String linkedin = "";
    private String pinterest = "";
    private String twitter = "";
    private String web = "";
    private String youtube = "";

    private int viewed = 0; //瀏覽數
    private int count_from = 0; //關注數
    private int besponsored = 0;
    private int sum = 0; //總收益
    private int sumofsettlement = 0; //可領取
    private int sumofunsettlement = 0; //未結算
    private int point = 0;

    private boolean follow = false;
    private boolean invite = false;
    private boolean disscuss = false;


    public String getInfo_url() {
        return info_url;
    }

    public void setInfo_url(String info_url) {
        this.info_url = info_url;
    }

    public boolean isDisscuss() {
        return disscuss;
    }

    public void setDisscuss(boolean disscuss) {
        this.disscuss = disscuss;
    }


    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


    public String getCompany_identity() {
        return company_identity;
    }

    public void setCompany_identity(String company_identity) {
        this.company_identity = company_identity;
    }


    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getSumofsettlement() {
        return sumofsettlement;
    }

    public void setSumofsettlement(int sumofsettlement) {
        this.sumofsettlement = sumofsettlement;
    }

    public int getSumofunsettlement() {
        return sumofunsettlement;
    }

    public void setSumofunsettlement(int sumofunsettlement) {
        this.sumofunsettlement = sumofunsettlement;
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getBesponsored() {
        return besponsored;
    }

    public void setBesponsored(int besponsored) {
        this.besponsored = besponsored;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public boolean isInvite() {
        return invite;
    }

    public void setInvite(boolean invite) {
        this.invite = invite;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public int getCount_from() {
        return count_from;
    }

    public void setCount_from(int count_from) {
        this.count_from = count_from;
    }

    public String getCreative_name() {
        return creative_name;
    }

    public void setCreative_name(String creative_name) {
        this.creative_name = creative_name;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getPinterest() {
        return pinterest;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

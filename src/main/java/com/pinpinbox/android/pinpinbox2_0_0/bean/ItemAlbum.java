package com.pinpinbox.android.pinpinbox2_0_0.bean;

import java.util.List;

/**
 * Created by kevin9594 on 2017/2/25.
 */
public class ItemAlbum {

    private List<String> albumindexList;

    private String album_id; //作品ID
    private String description; //描述
    private String location; //地點
    private String name; //名稱
    private String audio_mode; //相本音訊模式
    private String audio_refer; //相本音訊來源
    private String audio_target; //相本音訊位置
    private String cover; //作品封面
    private String inserttime; //建立時間
    private String difftime;
    private String cover_hex;
    private String weather;
    private String mood;
    private String act;


    private int messageboard;
    private int likes;
    private int count_photo; //相片數量
    private int point; //價格
    private int count; //下載數
    private int viewed; //閱覽數
    private int cover_width; //封面寬
    private int cover_height; //封面高
    private int color;
    private int sponsorCount = 0;
    private int categoryarea_id;
    private int category_id;

    private int image_orientation = 0;
    public static final int PORTRAIT = 1; //直
    public static final int LANDSCAPE = 2; //橫
    public static final int SQUARE = 0;//方形


    private boolean is_likes;
    private boolean own; //是否擁有
    private boolean exchange; //是否有兌換券類相片
    private boolean image; //是否有相片類相片
    private boolean slot; //是否有拉霸類相片
    private boolean video; //是否有影片類相片
    private boolean audio; //是否有影音類相片
    private boolean audio_loop; //相本音訊循環

    private boolean contributionstatus;
    private boolean isFocuse = false;
//    private boolean detail_is_open; //作品設定選項是否開啟


    /*活動類*/
    private int event_id; //活動ID
    private int event_count; //票數
    private String event_name; //名稱
    private String event_url; //網址

    private boolean has_voted; //投票狀態 => false: 未投票, true: 已投票
    private int event_join;

    /*作者類*/
    private int user_id; //作者ID
    private String user_name; //作者名稱
    private String user_picture;

    public List<String> getAlbumindexList() {
        return albumindexList;
    }

    public void setAlbumindexList(List<String> albumindexList) {
        this.albumindexList = albumindexList;
    }

    public int getCategoryarea_id() {
        return categoryarea_id;
    }

    public void setCategoryarea_id(int categoryarea_id) {
        this.categoryarea_id = categoryarea_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public int getSponsorCount() {
        return sponsorCount;
    }

    public void setSponsorCount(int sponsorCount) {
        this.sponsorCount = sponsorCount;
    }

    public boolean isContributionstatus() {
        return contributionstatus;
    }

    public void setContributionstatus(boolean contributionstatus) {
        this.contributionstatus = contributionstatus;
    }

    public boolean isFocuse() {
        return isFocuse;
    }

    public void setFocuse(boolean focuse) {
        isFocuse = focuse;
    }


    public int getImage_orientation() {
        return image_orientation;
    }

    public void setImage_orientation(int image_orientation) {
        this.image_orientation = image_orientation;
    }

    public boolean isHas_voted() {
        return has_voted;
    }

    public void setHas_voted(boolean has_voted) {
        this.has_voted = has_voted;
    }

    public int getEvent_join() {
        return event_join;
    }

    public void setEvent_join(int eventjoin) {
        this.event_join = eventjoin;
    }


    public String getCover_hex() {
        return cover_hex;
    }

    public void setCover_hex(String cover_hex) {
        this.cover_hex = cover_hex;
    }

    public int getColor() {return color;}
    public void setColor(int color) {this.color = color;}

    public boolean is_likes() {
        return is_likes;
    }

    public void setIs_likes(boolean is_likes) {
        this.is_likes = is_likes;
    }

    public int getMessageboard() {
        return messageboard;
    }

    public void setMessageboard(int messageboard) {
        this.messageboard = messageboard;
    }

    public String getDifftime() {
        return difftime;
    }

    public void setDifftime(String difftime) {
        this.difftime = difftime;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUser_picture() {
        return user_picture;
    }

    public void setUser_picture(String user_picture) {
        this.user_picture = user_picture;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAudio_mode() {
        return audio_mode;
    }

    public void setAudio_mode(String audio_mode) {
        this.audio_mode = audio_mode;
    }

    public boolean isAudio_loop() {
        return audio_loop;
    }

    public void setAudio_loop(boolean audio_loop) {
        this.audio_loop = audio_loop;
    }

    public String getAudio_refer() {
        return audio_refer;
    }

    public void setAudio_refer(String audio_refer) {
        this.audio_refer = audio_refer;
    }

    public String getAudio_target() {
        return audio_target;
    }

    public void setAudio_target(String audio_target) {
        this.audio_target = audio_target;
    }

    public int getCount_photo() {
        return count_photo;
    }

    public void setCount_photo(int count_photo) {
        this.count_photo = count_photo;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    public boolean isExchange() {
        return exchange;
    }

    public void setExchange(boolean exchange) {
        this.exchange = exchange;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public boolean isSlot() {
        return slot;
    }

    public void setSlot(boolean slot) {
        this.slot = slot;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isAudio() {
        return audio;
    }

    public void setAudio(boolean audio) {
        this.audio = audio;
    }

    public int getCover_width() {
        return cover_width;
    }

    public void setCover_width(int cover_width) {
        this.cover_width = cover_width;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getCover_height() {
        return cover_height;
    }

    public void setCover_height(int cover_height) {
        this.cover_height = cover_height;
    }


    public String getInserttime() {
        return inserttime;
    }

    public void setInserttime(String inserttime) {
        this.inserttime = inserttime;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getEvent_count() {
        return event_count;
    }

    public void setEvent_count(int event_count) {
        this.event_count = event_count;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }



    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}

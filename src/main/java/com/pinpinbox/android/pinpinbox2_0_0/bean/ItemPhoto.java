package com.pinpinbox.android.pinpinbox2_0_0.bean;

/**
 * Created by kevin9594 on 2017/2/25.
 */
public class ItemPhoto {

//    private int audio_loop; // 重播 => 1, 不重播 => 0
    private int duration; //翻頁間隔時間
    private int photo_id; //相片ID

    private String audio_refer; //音訊來源方式
    private String audio_target; //音訊位置
    private String description; //描述
    private String location; //地點
    private String name; //名稱
    private String usefor; //相片類型=> image<一般相片> / video<視訊相片> / exchange<兌換券> / slot<拉霸抽獎>
    private String video_refer; //視訊來源方式
    private String video_target; //視訊位置
    private String image_url; //圖像 url 原始尺寸, 1002x1002
    private String image_url_thumbnail; //圖像 url 縮圖尺寸, 501x501

    private boolean isSelect;

    public boolean isAudio_loop() {
        return audio_loop;
    }

    public void setAudio_loop(boolean audio_loop) {
        this.audio_loop = audio_loop;
    }

    private boolean audio_loop;

    /*連結類*/
//    private String hyperlink_icon; //圖示
//    private String hyperlink_text; //說明
//    private String hyperlink_url; //連結




    public String getAudio_refer() {
        return audio_refer;
    }

    public void setAudio_refer(String audio_refer) {
        this.audio_refer = audio_refer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(int photo_id) {
        this.photo_id = photo_id;
    }

    public String getAudio_target() {
        return audio_target;
    }

    public void setAudio_target(String audio_target) {
        this.audio_target = audio_target;
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

    public String getUsefor() {
        return usefor;
    }

    public void setUsefor(String usefor) {
        this.usefor = usefor;
    }

    public String getVideo_refer() {
        return video_refer;
    }

    public void setVideo_refer(String video_refer) {
        this.video_refer = video_refer;
    }

    public String getVideo_target() {
        return video_target;
    }

    public void setVideo_target(String video_target) {
        this.video_target = video_target;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url_thumbnail() {
        return image_url_thumbnail;
    }

    public void setImage_url_thumbnail(String image_url_thumbnail) {
        this.image_url_thumbnail = image_url_thumbnail;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}

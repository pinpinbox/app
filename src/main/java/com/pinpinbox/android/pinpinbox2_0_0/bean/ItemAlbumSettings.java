package com.pinpinbox.android.pinpinbox2_0_0.bean;

/**
 * Created by kevin9594 on 2017/4/2.
 */
public class ItemAlbumSettings {

    private String name;
    private String url;
    private String secondpaging;
    private String strId;

    private int id;

    private boolean select;

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getSecondpaging() {
        return secondpaging;
    }

    public void setSecondpaging(String secondpaging) {
        this.secondpaging = secondpaging;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public ItemAlbumSettings(Builder builder){
        this.name = builder.name;
        this.url = builder.url;
        this.id = builder.id;
        this.secondpaging = builder.secondpaging;
        this.select = builder.select;
        this.strId = builder.strId;

    }

    public static class Builder{
        private String name;
        private String url;
        private String secondpaging;
        private String strId;
        private int id;
        private boolean select;

        public Builder name(String name){
            this.name=name;
            return this;
        }
        public Builder url(String url){
            this.url = url;
            return this;
        }
        public Builder id(int id){
            this.id = id;
            return this;
        }

        public Builder secondpaging(String secondpaging){
            this.secondpaging = secondpaging;
            return this;
        }

        public Builder select(boolean select){
            this.select = select;
            return this;
        }

        public Builder strId(String strId){
            this.strId = strId;
            return this;
        }


        public ItemAlbumSettings build(){
            return new ItemAlbumSettings(this);
        }
    }


}

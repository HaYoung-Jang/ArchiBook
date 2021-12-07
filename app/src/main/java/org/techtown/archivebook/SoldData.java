package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

public class SoldData {

    @SerializedName("soldId")
    private int sold_id;

    @SerializedName("registerdId")
    private int registerd_id;

    @SerializedName("buyerId")
    private String buyer_id;

    @SerializedName("nickName")
    private String nick_name;

    @SerializedName("profileColor")
    private String profile_color;

    @SerializedName("isbn")
    private String isbn;

    @SerializedName("title")
    private String title;

    private String userNow;

    public int getSold_id() {
        return sold_id;
    }

    public void setSold_id(int sold_id) {
        this.sold_id = sold_id;
    }

    public int getRegisterd_id() {
        return registerd_id;
    }

    public void setRegisterd_id(int registerd_id) {
        this.registerd_id = registerd_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProfile_color() {
        return profile_color;
    }

    public void setProfile_color(String profile_color) {
        this.profile_color = profile_color;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUserNow() {
        return userNow;
    }

    public void setUserNow(String userNow) {
        this.userNow = userNow;
    }
}

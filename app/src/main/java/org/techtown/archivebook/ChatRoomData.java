package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

public class ChatRoomData {

    @SerializedName("chatId")
    private String chat_id;

    @SerializedName("sellerId")
    private String seller_id;

    @SerializedName("buyerId")
    private String buyer_id;

    @SerializedName("registerdId")
    private int registerd_id;

    @SerializedName("roomName")
    private String room_name;

    @SerializedName("dateLast")
    private String date_last;

    @SerializedName("timeLast")
    private String time_last;

    @SerializedName("sellerNick")
    private String seller_nick;

    @SerializedName("buyerNick")
    private String buyer_nick;

    @SerializedName("sellerProfile")
    private String seller_profile;

    @SerializedName("buyerProfile")
    private String buyer_profile;

    @SerializedName("isbn")
    private String isbn;

    private String userNow;

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getRegisterd_id() {
        return registerd_id;
    }

    public void setRegisterd_id(int registerd_id) {
        this.registerd_id = registerd_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getDate_last() {
        return date_last;
    }

    public void setDate_last(String date_last) {
        this.date_last = date_last;
    }

    public String getTime_last() {
        return time_last;
    }

    public void setTime_last(String time_last) {
        this.time_last = time_last;
    }

    public String getSeller_nick() {
        return seller_nick;
    }

    public void setSeller_nick(String seller_nick) {
        this.seller_nick = seller_nick;
    }

    public String getBuyer_nick() {
        return buyer_nick;
    }

    public void setBuyer_nick(String buyer_nick) {
        this.buyer_nick = buyer_nick;
    }

    public String getSeller_profile() {
        return seller_profile;
    }

    public void setSeller_profile(String seller_profile) {
        this.seller_profile = seller_profile;
    }

    public String getBuyer_profile() {
        return buyer_profile;
    }

    public void setBuyer_profile(String buyer_profile) {
        this.buyer_profile = buyer_profile;
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

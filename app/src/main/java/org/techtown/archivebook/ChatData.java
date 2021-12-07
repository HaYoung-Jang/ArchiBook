package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

public class ChatData {

    @SerializedName("message")
    private String message;

    @SerializedName("nickName")
    private String nickName;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("profileColor")
    private String profileColor;

    @SerializedName("chatRoomId")
    private String chatRoomId;

    public ChatData() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfileColor() {
        return profileColor;
    }

    public void setProfileColor(String profileColor) {
        this.profileColor = profileColor;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}

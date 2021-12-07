package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("memberId")
    private int member_id;

    @SerializedName("accountId")
    private String account_id;

    @SerializedName("name")
    private String name;

    @SerializedName("nickName")
    private String nick_name;

    @SerializedName("email")
    private String email;

    @SerializedName("bookNum")
    private int book_num;

    @SerializedName("profileColor")
    private String profile_color;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBook_num() {
        return book_num;
    }

    public void setBook_num(int book_num) {
        this.book_num = book_num;
    }

    public String getProfile_color() {
        return profile_color;
    }

    public void setProfile_color(String profile_color) {
        this.profile_color = profile_color;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "member_id=" + member_id +
                ", account_id='" + account_id + '\'' +
                ", name='" + name + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", email='" + email + '\'' +
                ", book_num=" + book_num +
                ", profile_color='" + profile_color + '\'' +
                '}';
    }
}

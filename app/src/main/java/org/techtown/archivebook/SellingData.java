package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

// 서버로부터 받아올 판매 도서 DTO
public class SellingData {

    @SerializedName("registerdId")
    private int registerd_id;

    @SerializedName("isbn")
    private String isbn;

    @SerializedName("accountId")
    private String account_id;

    @SerializedName("priceRegisterd")
    private int price_registerd;

    @SerializedName("isSold")
    private boolean is_sold;

    @SerializedName("imagePath")
    private String image_path;

    @SerializedName("describeDetail")
    private String describe_detail;

    @SerializedName("dateRegisterd")
    private String date_registerd;

    @SerializedName("nickName")
    private String nick_name;

    @SerializedName("profileColor")
    private String profile_color;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("datePublished")
    private String date_published;

    @SerializedName("priceOrigin")
    private String price_origin;

    private String userNow;

    public int getRegisterd_id() {
        return registerd_id;
    }

    public void setRegisterd_id(int registerd_id) {
        this.registerd_id = registerd_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public int getPrice_registerd() {
        return price_registerd;
    }

    public void setPrice_registerd(int price_registerd) {
        this.price_registerd = price_registerd;
    }

    public boolean getIs_sold() {
        return is_sold;
    }

    public void setIs_sold(boolean is_sold) {
        this.is_sold = is_sold;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getDescribe_detail() {
        return describe_detail;
    }

    public void setDescribe_detail(String describe_detail) {
        this.describe_detail = describe_detail;
    }

    public String getDate_registerd() {
        return date_registerd;
    }

    public void setDate_registerd(String date_registerd) {
        this.date_registerd = date_registerd;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getProfile_color() {
        return profile_color;
    }

    public void setProfile_color(String profile_color) {
        this.profile_color = profile_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate_published() {
        return date_published;
    }

    public void setDate_published(String date_published) {
        this.date_published = date_published;
    }

    public String getPrice_origin() {
        return price_origin;
    }

    public void setPrice_origin(String price_origin) {
        this.price_origin = price_origin;
    }

    public String getUserNow() {
        return userNow;
    }

    public void setUserNow(String userNow) {
        this.userNow = userNow;
    }

    @Override
    public String toString() {
        return "SellingData{" +
                "registerd_id=" + registerd_id +
                ", isbn='" + isbn + '\'' +
                ", account_id='" + account_id + '\'' +
                ", price_registerd=" + price_registerd +
                ", is_sold=" + is_sold +
                ", image_path='" + image_path + '\'' +
                ", describe_detail='" + describe_detail + '\'' +
                ", date_registerd='" + date_registerd + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", profile_color='" + profile_color + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", date_published='" + date_published + '\'' +
                ", price_origin='" + price_origin + '\'' +
                '}';
    }
}

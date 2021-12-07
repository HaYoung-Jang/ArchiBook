package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

// 서버로부터 받아올 독서 기록 DTO
public class RecordData {

    @SerializedName("recordId")
    private int record_id;

    @SerializedName("accountId")
    private String account_id;

    @SerializedName("isbn")
    private String isbn;

    @SerializedName("dateStarted")
    private String date_started;

    @SerializedName("dateFinished")
    private String date_finished;

    @SerializedName("dateWriting")
    private String date_writing;

    @SerializedName("bookScore")
    private int book_score;

    @SerializedName("content")
    private String content;

    @SerializedName("isPublic")
    private boolean is_public;

    @SerializedName("title")
    private String title;

    @SerializedName("author")
    private String author;

    @SerializedName("publisher")
    private String publisher;

    @SerializedName("imageUrl")
    private String image_url;

    @SerializedName("nickName")
    private String nick_name;

    @SerializedName("profileColor")
    private String profile_color;

    private String userNow;

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDate_started() {
        return date_started;
    }

    public void setDate_started(String date_started) {
        this.date_started = date_started;
    }

    public String getDate_finished() {
        return date_finished;
    }

    public void setDate_finished(String date_finished) {
        this.date_finished = date_finished;
    }

    public String getDate_writing() {
        return date_writing;
    }

    public void setDate_writing(String date_writing) {
        this.date_writing = date_writing;
    }

    public int getBook_score() {
        return book_score;
    }

    public void setBook_score(int book_score) {
        this.book_score = book_score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public void setProfile_color(String prifile_color) {
        this.profile_color = prifile_color;
    }

    public String getUserNow() {
        return userNow;
    }

    public void setUserNow(String userNow) {
        this.userNow = userNow;
    }

    @Override
    public String toString() {
        return "RecordData{" +
                "record_id=" + record_id +
                ", account_id='" + account_id + '\'' +
                ", isbn='" + isbn + '\'' +
                ", date_started='" + date_started + '\'' +
                ", date_finished='" + date_finished + '\'' +
                ", date_writing='" + date_writing + '\'' +
                ", book_score=" + book_score +
                ", content='" + content + '\'' +
                ", is_public=" + is_public +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", image_url='" + image_url + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", profile_color='" + profile_color + '\'' +
                '}';
    }
}

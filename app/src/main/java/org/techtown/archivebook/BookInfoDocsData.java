package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

// ISBN open API로부터 받아올 도서 정보 데이터 docs 클래스
public class BookInfoDocsData {
    @SerializedName("title")
    String bookTitle;

    @SerializedName("author")
    String booKAuthor;

    @SerializedName("publisher")
    String bookPublisher;

    @SerializedName("pubdate")
    String bookDate;

    @SerializedName("price")
    int bookPrice;

    @SerializedName("image")
    String bookImage;

    @SerializedName("description")
    String bookDescription;

    @SerializedName("link")
    String bookLink;

    @SerializedName("isbn")
    String isbn;

    String userNow;

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBooKAuthor() {
        return booKAuthor;
    }

    public void setBooKAuthor(String booKAuthor) {
        this.booKAuthor = booKAuthor;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public int getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(int bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookLink() {
        return bookLink;
    }

    public void setBookLink(String bookLink) {
        this.bookLink = bookLink;
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

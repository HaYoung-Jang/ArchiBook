package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// ISBN open API로부터 받아올 도서 정보 데이터 클래스
public class BookInfoData {

    @SerializedName("total")
    int resultCount;

    @SerializedName("items")
    List<BookInfoDocsData> bookInfoDocsData;

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<BookInfoDocsData> getBookInfoDocsData() {
        return bookInfoDocsData;
    }

    public void setBookInfoDocsData(List<BookInfoDocsData> bookInfoDocsData) {
        this.bookInfoDocsData = bookInfoDocsData;
    }
}

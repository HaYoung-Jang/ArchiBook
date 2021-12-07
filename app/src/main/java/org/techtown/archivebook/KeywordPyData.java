package org.techtown.archivebook;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class KeywordPyData {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("data")
    private ArrayList<String> keyword;

    public ArrayList<String> getKeyword() {
        return keyword;
    }
}

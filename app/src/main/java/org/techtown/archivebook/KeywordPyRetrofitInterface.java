package org.techtown.archivebook;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KeywordPyRetrofitInterface {

    @GET("/getkeyword")
    Call<KeywordPyData> getList(@Query("link") String link);
}

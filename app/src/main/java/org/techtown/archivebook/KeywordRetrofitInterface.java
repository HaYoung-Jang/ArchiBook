package org.techtown.archivebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KeywordRetrofitInterface {

    @GET("keyword/list")
    Call <List<KeywordData>> getList(@Query("isbn") String isbn);

    @GET("keyword/same")
    Call <List<KeywordData>> getSame(@Query("keyword") String keyword, @Query("isbn") String isbn);

    @GET("keyword/search")
    Call <List<KeywordData>> getSearch(@Query("keyword") String keyword);

    @GET("keyword/count")
    Call <Integer> getCount(@Query("isbn") String isbn);

    @POST("keyword/register")
    Call <Integer> postRegister(@Body KeywordData keywordData);
}

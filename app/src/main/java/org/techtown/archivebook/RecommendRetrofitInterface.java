package org.techtown.archivebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RecommendRetrofitInterface {

    @GET("/recommend/list")
    Call <List<RecommendData>> getList(@Query("account_id") String account_id);

    @POST("/recommend/register")
    Call <Integer> postRegister(@Body RecommendData recommendData);
}

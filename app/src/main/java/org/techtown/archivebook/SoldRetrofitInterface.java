package org.techtown.archivebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SoldRetrofitInterface {

    @GET("sold/list")
    Call<List<SoldData>> getList(@Query("account_id") String account_id);

    @POST("sold/register")
    Call<Integer> postRegister(@Body SoldData soldData);
}

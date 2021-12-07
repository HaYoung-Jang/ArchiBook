package org.techtown.archivebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface SellingRetrofitInterface {

    @GET("selling/list")
    Call<List<SellingData>> getList();

    @GET("selling/all")
    Call<List<SellingData>> getAll();

    @GET("selling/personal")
    Call<List<SellingData>> getPersonal(@Query("account_id") String accountId);

    @GET("selling/detail")
    Call<SellingData> getDetail(@Query("registerd_id") int registerdId);

    @POST("selling/register")
    Call<Integer> postRegister(@Body SellingData sellingData);

    @PUT("selling/sold")
    Call<Integer> putSold(@Body SellingData sellingData);

    @DELETE("selling/delete")
    Call<Integer> deleteDelete(@Query("registerd_id") int registerdId, @Query("account_id") String accountId);
}

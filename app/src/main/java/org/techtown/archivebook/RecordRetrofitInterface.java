package org.techtown.archivebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RecordRetrofitInterface {

    @GET("record/list")
    Call<List<RecordData>> getList();

    @GET("record/personal")
    Call<List<RecordData>> getPersonal(@Query("account_id") String accountId);

    @GET("record/detail")
    Call<RecordData> getDetail(@Query("record_id") int recordId);

    @POST("record/register")
    Call<Integer> postRegister(@Body RecordData recordData);

    @PUT("record/public")
    Call<Integer> putPublic(@Body RecordData recordData);

    @PUT("record/private")
    Call<Integer> putPrivate(@Body RecordData recordData);

    @DELETE("record/delete")
    Call<Integer> deleteDelete(@Query("record_id") int recordId, @Query("account_id") String accountId);
}

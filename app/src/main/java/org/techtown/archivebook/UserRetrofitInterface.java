package org.techtown.archivebook;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserRetrofitInterface {

    @GET("user/detail")
    Call<UserData> getDetail(@Query("account_id") String accountId);

    @GET("user/nickname")
    Call<Integer> getNickname(@Query("nick_name") String nickName);

    @POST("user/register")
    Call<Integer> postRegister(@Body UserData userData);
}

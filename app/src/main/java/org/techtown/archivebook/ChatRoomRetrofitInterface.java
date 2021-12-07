package org.techtown.archivebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChatRoomRetrofitInterface {

    @GET("chat/list")
    Call <List<ChatRoomData>> getList(@Query("account_id") String account_id);

    @GET("chat/detail")
    Call <ChatRoomData> getDetail(@Query("chat_id") String chat_id);

    @GET("chat/sold")
    Call <List<ChatRoomData>> getSold(@Query("registerd_id") int registerd_id, @Query("account_id") String account_id);

    @POST("chat/register")
    Call <Integer> postRegister(@Body ChatRoomData chatRoomData);
}

package org.techtown.archivebook;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookOurRetrofitInterface {

    @GET("book/detail")
    Call<BookData> getDetail(@Query("isbn") String isbn);

    @POST("/book/register")
    Call<Integer> postRegister(@Body BookData bookData);
}

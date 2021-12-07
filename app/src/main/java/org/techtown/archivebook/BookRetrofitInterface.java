package org.techtown.archivebook;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookRetrofitInterface {

    @Headers({
            "X-Naver-Client-Id: GFsVlEbWa3C4tTTOEcBy",
            "X-Naver-Client-Secret: uapUz_iN7c"
    })
    @GET("v1/search/book.json")
    Call<BookInfoData> getPosts(@Query("query") String book_isbn, @Query("display") String page_size);

    @Headers({
            "X-Naver-Client-Id: GFsVlEbWa3C4tTTOEcBy",
            "X-Naver-Client-Secret: uapUz_iN7c"
    })
    @GET("v1/search/book_adv.json")
    Call<BookInfoData> getTitle(@Query("d_titl") String title, @Query("display") int page_size);

    @Headers({
            "X-Naver-Client-Id: GFsVlEbWa3C4tTTOEcBy",
            "X-Naver-Client-Secret: uapUz_iN7c"
    })
    @GET("v1/search/book_adv.json")
    Call<BookInfoData> getAuthor(@Query("d_auth") String author, @Query("display") int page_size);

    @Headers({
            "X-Naver-Client-Id: GFsVlEbWa3C4tTTOEcBy",
            "X-Naver-Client-Secret: uapUz_iN7c"
    })
    @GET("v1/search/book_adv.json")
    Call<BookInfoData> getIsbn(@Query("d_isbn") String isbn, @Query("display") int page_size);
}

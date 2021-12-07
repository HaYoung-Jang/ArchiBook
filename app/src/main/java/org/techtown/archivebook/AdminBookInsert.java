package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminBookInsert extends AppCompatActivity {

    EditText et_admin_book_search;
    ImageButton ibtn_search;
    TextView tv_admin_book_search_no_result;
    TextView tv_admin_book_search_no_input;
    RelativeLayout rl_admin_book_search_result_info;
    TextView tv_admin_book_search_result_title;
    TextView tv_admin_book_search_result_isbn;
    TextView tv_admin_book_search_result_author;
    TextView tv_admin_book_search_result_publisher;
    TextView tv_admin_book_search_result_date_published;
    TextView tv_admin_book_search_result_price_orgin;
    ImageView iv_admin_book_search_result_image;


    Retrofit retrofit;
    BookOurRetrofitInterface bookOurRetrofitInterface;
    KeywordRetrofitInterface keywordRetrofitInterface;
    KeywordPyRetrofitInterface keywordPyRetrofitInterface;

    private final String BOOK_BASE_URL = "https://openapi.naver.com/";

    String page_size;
    String book_isbn;

    String accountId;

    String link;
    String image_url;
    RequestOptions options;

    int book_total;

    KeywordData keywordData = new KeywordData();
    BookInfoDocsData bookInfoDocsData = new BookInfoDocsData();

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    // Keyword Server
    private final String PY_BASE_URL = "http://52.79.183.67:5000/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_book_insert);

        rl_admin_book_search_result_info = (RelativeLayout) findViewById(R.id.rl_admin_book_search_result_info);
        et_admin_book_search = (EditText) findViewById(R.id.et_admin_book_search);
        ibtn_search = (ImageButton) findViewById(R.id.ibtn_search);
        tv_admin_book_search_no_result = (TextView) findViewById(R.id.tv_admin_book_search_no_result);
        tv_admin_book_search_no_input = (TextView) findViewById(R.id.tv_admin_book_search_no_input);
        tv_admin_book_search_result_title = (TextView) findViewById(R.id.tv_admin_book_search_result_title);
        tv_admin_book_search_result_isbn = (TextView) findViewById(R.id.tv_admin_book_search_result_isbn);
        tv_admin_book_search_result_author = (TextView) findViewById(R.id.tv_admin_book_search_result_author);
        tv_admin_book_search_result_publisher = (TextView) findViewById(R.id.tv_admin_book_search_result_publisher);
        tv_admin_book_search_result_date_published = (TextView) findViewById(R.id.tv_admin_book_search_result_date_published);
        tv_admin_book_search_result_price_orgin = (TextView) findViewById(R.id.tv_admin_book_search_result_price_orgin);
        iv_admin_book_search_result_image = (ImageView) findViewById(R.id.iv_admin_book_search_result_image);

        Intent intentGet = getIntent();
        accountId = intentGet.getStringExtra("account_id");

        Log.d("accountId-sell-search", accountId);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        retrofit2.Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(PY_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit2.Retrofit retrofit3 = new Retrofit.Builder()
                .baseUrl(BOOK_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bookOurRetrofitInterface = retrofit.create(BookOurRetrofitInterface.class);
        keywordRetrofitInterface = retrofit.create(KeywordRetrofitInterface.class);
        keywordPyRetrofitInterface = retrofit2.create(KeywordPyRetrofitInterface.class);

        BookRetrofitInterface bookRetrofitInterface = retrofit3.create(BookRetrofitInterface.class);

        // 검색 버큰 클릭시
        ibtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색어 입력하지 않으면 검색어 입력해주세요 텍스트 보이기
                if (et_admin_book_search.getText().toString().equals("")) {
                    tv_admin_book_search_no_input.setVisibility(View.VISIBLE);
                    rl_admin_book_search_result_info.setVisibility(View.GONE);
                    tv_admin_book_search_no_result.setVisibility(View.GONE);
                }
                else {
                    // 입력한 ISBN 받아오기
                    book_isbn = et_admin_book_search.getText().toString();

                    // open API 승인키, 입력된 ISBN 통해 도서 정보 요청
                    bookRetrofitInterface.getPosts(book_isbn, page_size).enqueue(new Callback<BookInfoData>() {
                        @Override
                        public void onResponse(Call<BookInfoData> call, Response<BookInfoData> response) {

                            // 통신이 원활하게 이루어진 경우
                            if (response.isSuccessful()) {
                                Log.d("ISBN open API", "success");
                                BookInfoData bookResult = response.body();
                                List<BookInfoDocsData> bookInfoDocsResult = bookResult.getBookInfoDocsData();

                                book_total = bookResult.getResultCount();

                                // 도서 검색된 결과 개수가 0일 경우
                                if (bookResult.getResultCount() == 0) {
                                    rl_admin_book_search_result_info.setVisibility(View.GONE);
                                    tv_admin_book_search_no_input.setVisibility(View.GONE);
                                    tv_admin_book_search_no_result.setVisibility(View.VISIBLE);
                                }
                                else {
                                    // 검색된 도서 정보 레이아웃 보이기
                                    rl_admin_book_search_result_info.setVisibility(View.VISIBLE);
                                    tv_admin_book_search_no_input.setVisibility(View.GONE);
                                    tv_admin_book_search_no_result.setVisibility(View.GONE);

                                    // 각 도서 정보 띄우기
                                    tv_admin_book_search_result_title.setText(bookInfoDocsResult.get(0).getBookTitle());
                                    tv_admin_book_search_result_isbn.setText(book_isbn);
                                    tv_admin_book_search_result_author.setText(bookInfoDocsResult.get(0).getBooKAuthor());
                                    tv_admin_book_search_result_publisher.setText(bookInfoDocsResult.get(0).getBookPublisher());
                                    tv_admin_book_search_result_date_published.setText(bookInfoDocsResult.get(0).getBookDate());
                                    tv_admin_book_search_result_price_orgin.setText(Integer.toString(bookInfoDocsResult.get(0).getBookPrice()));
                                    link = bookInfoDocsResult.get(0).getBookLink();

                                    // 도서 정보 저장
                                    bookInfoDocsData.setBookTitle(bookInfoDocsResult.get(0).getBookTitle());
                                    bookInfoDocsData.setBooKAuthor(bookInfoDocsResult.get(0).getBooKAuthor());
                                    bookInfoDocsData.setBookPublisher(bookInfoDocsResult.get(0).getBookPublisher());
                                    bookInfoDocsData.setBookDate(bookInfoDocsResult.get(0).getBookDate());
                                    bookInfoDocsData.setBookPrice(bookInfoDocsResult.get(0).getBookPrice());
                                    bookInfoDocsData.setBookImage(bookInfoDocsResult.get(0).getBookImage());
                                    bookInfoDocsData.setBookLink(link);

                                    // 도서 표지 이미지 주소 받아오고 띄우기
                                    image_url = bookInfoDocsResult.get(0).getBookImage();
                                    options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);

                                    if (!image_url.equals("")) {
                                        Glide.with(getApplicationContext()).load(image_url).apply(options).into(iv_admin_book_search_result_image);
                                    }
                                    else {
                                        iv_admin_book_search_result_image.setImageResource(R.drawable.img_no_book_image);
                                    }
                                }

                                Log.d("total_count", Integer.toString(bookResult.getResultCount()));
                            }
                            else {
                                Log.d("ISBN open API", "fail" + response);
                                rl_admin_book_search_result_info.setVisibility(View.GONE);
                                tv_admin_book_search_no_input.setVisibility(View.GONE);
                                tv_admin_book_search_no_result.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<BookInfoData> call, Throwable t) {
                            Log.d("ISBN open API", t.getMessage());
                        }

                    });
                }
            }
        });

        // 도서 검색 결과 클릭시 DB에 도서 정보 저장하고 키워드 조회 및 저장
        rl_admin_book_search_result_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 해당 도서 DB 저장 여부 파악하고 저장이 안된 경우 저장하기
                bookOurRetrofitInterface.getDetail(book_isbn).enqueue(new Callback<BookData>() {
                    @Override
                    public void onResponse(Call<BookData> call, Response<BookData> response) {
                        Log.d("server book detail", "success");
                        BookData bookResult = response.body();
                        if (bookResult == null) {

                            // DB에 저장할 도서 데이터 생성
                            BookData bookData = new BookData();
                            bookData.setIsbn(book_isbn);
                            bookData.setTitle(bookInfoDocsData.getBookTitle());
                            bookData.setAuthor(bookInfoDocsData.getBooKAuthor());
                            bookData.setPublisher(bookInfoDocsData.getBookPublisher());
                            bookData.setDate_published(bookInfoDocsData.getBookDate());
                            bookData.setPrice_origin(bookInfoDocsData.getBookPrice());
                            bookData.setImage_url(bookInfoDocsData.getBookImage());
                            bookData.setLink(link);

                            // Book 데이터 DB에 저장
                            bookOurRetrofitInterface.postRegister(bookData).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {

                                    if (response.code() == 200) {
                                        Log.d("server book register", "success" + " " + response);
                                        Toast.makeText(AdminBookInsert.this, "도서 등록 성공", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d("book datas", bookData.getIsbn() + " " + bookData.getTitle() + " " + bookData.getAuthor() + " " + bookData.getPublisher() + " " + bookData.getDate_published() + " " + bookData.getPrice_origin() + " " + bookData.getImage_url() + " " + bookData.getLink());
                                        Toast.makeText(AdminBookInsert.this, "도서 등록 실패", Toast.LENGTH_SHORT).show();
                                        Log.d("server book register", "fail" + " " + response);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Log.d("server book register", "fail" + t);
                                }
                            });
                        }
                        else {
                            Toast.makeText(AdminBookInsert.this, "도서 정보 존재", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BookData> call, Throwable t) {
                        Log.d("server book detail", "fail" + " " + t);
                    }
                });

                // 도서 등록 성공하면 해당 도서의 키워드가 DB에 저장되어있는지 확인
                keywordRetrofitInterface.getCount(book_isbn).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.d("server keyword count", "success" + " " + response);
                        Log.d("keyword count : ", Integer.toString(response.body()));

                        // DB에 저장된 키워드가 없을 경우 파이썬 서버로부터 키워드 받아오기
                        if (response.body() == 0) {
                            keywordPyRetrofitInterface.getList(link).enqueue(new Callback<KeywordPyData>() {
                                @Override
                                public void onResponse(Call<KeywordPyData> call, Response<KeywordPyData> response) {
                                    Log.d("python keyword list", "success" + " " + response);

                                    if (response.code() == 200) {
                                        List<String> keywordPyResult = response.body().getKeyword();
                                        Log.d("python keyword result", "keyword : " + keywordPyResult);

                                        // 받아온 키워드 DB에 저장하기
                                        for (int i = 0; i < keywordPyResult.size(); i++) {
                                            keywordData.setKeyword(keywordPyResult.get(i));
                                            keywordData.setIsbn(book_isbn);

                                            keywordRetrofitInterface.postRegister(keywordData).enqueue(new Callback<Integer>() {
                                                @Override
                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                    if (response.code() == 200) {
                                                        Log.d("server keyword register", "success" + " " + response);
                                                        Toast.makeText(AdminBookInsert.this, "키워드 등록 성공", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Log.d("server keyword register", "fail" + " " + response);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Integer> call, Throwable t) {
                                                    Log.d("server keyword register", "fail" + " " + t);
                                                }
                                            });
                                        }
                                    }
                                    else {
                                        Log.d("python keyword list", "fail" + " " + response);
                                        Toast.makeText(AdminBookInsert.this, "키워드 등록 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<KeywordPyData> call, Throwable t) {
                                    Log.d("python keyword list", "fail" + " " + t);
                                }
                            });
                        }
                        else {
                            Log.d("keyword data", "exist");
                            Toast.makeText(AdminBookInsert.this, "키워드 존재", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Log.d("server keyword count", "fail" + " " + t);
                    }
                });
            }
        });
    }

    // 뒤로가기 이동시 accountId 값 전달
    @Override
    public void onBackPressed() {
//        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
//        intentBack.putExtra("account_id", accountId);
//        Log.d("intentBack - accountId", accountId);
//        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intentBack);
//        finish();

        finish();
    }
}
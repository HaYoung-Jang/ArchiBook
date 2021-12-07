package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordInputSearch extends AppCompatActivity {

    EditText et_record_input_search;
    ImageButton ibtn_search;
    TextView tv_record_input_search_no_result;
    TextView tv_record_input_search_no_input;
    RelativeLayout rl_record_input_search_result_info;
    TextView tv_record_input_search_result_title;
    TextView tv_record_input_search_result_isbn;
    TextView tv_record_input_search_result_author;
    TextView tv_record_input_search_result_publisher;
    TextView tv_record_input_search_result_date_published;
    TextView tv_record_input_search_result_price_orgin;
    ImageView iv_record_input_search_result_image;
    Spinner sp_record_input_search_select;
    ListView lv_record_input_result;

    Retrofit retrofit;
    BookRetrofitInterface bookRetrofitInterface;

    RecordInputSearchListAdapter recordInputSearchListAdapter;

    private final String BOOK_BASE_URL = "https://openapi.naver.com/";

    int page_size = 9;
    String book_isbn;
    ArrayList<String> isbnList = new ArrayList<>();
    ArrayList<BookInfoDocsData> bookList = new ArrayList<>();

    String link;
    String image_url;
    RequestOptions options;

    String searchSelect;
    String searchWord;

    ArrayList<String> spArr = new ArrayList<>();

    String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_input_search);

        et_record_input_search = (EditText) findViewById(R.id.et_record_input_search);
        ibtn_search = (ImageButton) findViewById(R.id.ibtn_search);
        tv_record_input_search_no_result = (TextView) findViewById(R.id.tv_record_input_search_no_result);
        tv_record_input_search_no_input = (TextView) findViewById(R.id.tv_record_input_search_no_input);
//        rl_record_input_search_result_info = (RelativeLayout) findViewById(R.id.rl_record_input_search_result_info);
//        tv_record_input_search_result_title = (TextView) findViewById(R.id.tv_record_input_search_result_title);
//        tv_record_input_search_result_isbn = (TextView) findViewById(R.id.tv_record_input_search_result_isbn);
//        tv_record_input_search_result_author = (TextView) findViewById(R.id.tv_record_input_search_result_author);
//        tv_record_input_search_result_publisher = (TextView) findViewById(R.id.tv_record_input_search_result_publisher);
//        tv_record_input_search_result_date_published = (TextView) findViewById(R.id.tv_record_input_search_result_date_published);
//        tv_record_input_search_result_price_orgin = (TextView) findViewById(R.id.tv_record_input_search_result_price_orgin);
//        iv_record_input_search_result_image = (ImageView) findViewById(R.id.iv_record_input_search_result_image);
        sp_record_input_search_select = (Spinner) findViewById(R.id.sp_record_input_search_select);
        lv_record_input_result = (ListView) findViewById(R.id.lv_record_input_result);

        Intent intentGet = getIntent();
        accountId = intentGet.getStringExtra("account_id");

        Log.d("accountId-record_search", accountId);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BOOK_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BookRetrofitInterface bookRetrofitInterface = retrofit.create(BookRetrofitInterface.class);

        // spinner item set
        spArr.add("제목");
        spArr.add("저자");
        spArr.add("isbn");

        // spinner arraylist adapter 연결
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_record_input_search_select.setAdapter(adapter);

        // spinner item 선택
        sp_record_input_search_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchSelect = spArr.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 리스트뷰 어댑터 연결
        recordInputSearchListAdapter = new RecordInputSearchListAdapter(bookList);
        lv_record_input_result.setAdapter(recordInputSearchListAdapter);

        // 검색 버큰 클릭시
        ibtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookList.clear();
                isbnList.clear();

                // 검색어 입력하지 않으면 검색어 입력해주세요 텍스트 보이기
                if (et_record_input_search.getText().toString().equals("")) {
                    tv_record_input_search_no_input.setVisibility(View.VISIBLE);
                    rl_record_input_search_result_info.setVisibility(View.GONE);
                    tv_record_input_search_no_result.setVisibility(View.GONE);
                }
                else {
//                    // 입력한 ISBN 받아오기
//                    book_isbn = et_record_input_search.getText().toString();
//
//                    // open API 승인키, 입력된 ISBN 통해 도서 정보 요청
//                    bookRetrofitInterface.getPosts(book_isbn, page_size).enqueue(new Callback<BookInfoData>() {
//                        @Override
//                        public void onResponse(Call<BookInfoData> call, Response<BookInfoData> response) {
//
//                            // 통신이 원활하게 이루어진 경우
//                            if (response.isSuccessful()) {
//                                Log.d("ISBN open API", "success");
//                                BookInfoData bookResult = response.body();
//                                List<BookInfoDocsData> bookInfoDocsResult = bookResult.getBookInfoDocsData();
//
//                                // 도서 검색된 결과 개수가 0일 경우
//                                if (bookResult.getResultCount() == 0) {
//                                    rl_record_input_search_result_info.setVisibility(View.GONE);
//                                    tv_record_input_search_no_input.setVisibility(View.GONE);
//                                    tv_record_input_search_no_result.setVisibility(View.VISIBLE);
//                                }
//                                else {
//                                    // 검색된 도서 정보 레이아웃 보이기
//                                    rl_record_input_search_result_info.setVisibility(View.VISIBLE);
//                                    tv_record_input_search_no_input.setVisibility(View.GONE);
//                                    tv_record_input_search_no_result.setVisibility(View.GONE);
//
//                                    // 각 도서 정보 띄우기
//                                    tv_record_input_search_result_title.setText(bookInfoDocsResult.get(0).getBookTitle());
//                                    tv_record_input_search_result_isbn.setText(book_isbn);
//                                    tv_record_input_search_result_author.setText(bookInfoDocsResult.get(0).getBooKAuthor());
//                                    tv_record_input_search_result_publisher.setText(bookInfoDocsResult.get(0).getBookPublisher());
//                                    tv_record_input_search_result_date_published.setText(bookInfoDocsResult.get(0).getBookDate());
//                                    tv_record_input_search_result_price_orgin.setText(Integer.toString(bookInfoDocsResult.get(0).getBookPrice()));
//                                    link = bookInfoDocsResult.get(0).getBookLink();
//
//                                    // 도서 표지 이미지 주소 받아오고 띄우기
//                                    image_url = bookInfoDocsResult.get(0).getBookImage();
//                                    options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
//
//                                    if (!image_url.equals("")) {
//                                        Glide.with(getApplicationContext()).load(image_url).apply(options).into(iv_record_input_search_result_image);
//                                    }
//                                    else {
//                                        iv_record_input_search_result_image.setImageResource(R.drawable.img_no_book_image);
//                                    }
//                                }
//
//                                Log.d("total_count", Integer.toString(bookResult.getResultCount()));
//                            }
//                            else {
//                                Log.d("ISBN open API", "fail");
//                                rl_record_input_search_result_info.setVisibility(View.GONE);
//                                tv_record_input_search_no_input.setVisibility(View.GONE);
//                                tv_record_input_search_no_result.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<BookInfoData> call, Throwable t) {
//                            Log.d("ISBN open API", t.getMessage());
//                        }
//                    });

                    // 입력한 단어 가져오기
                    searchWord = et_record_input_search.getText().toString();

                    // spinner 선택이 제목인 경우 제목 검색 결과의 isbn 가져오기
                    if (searchSelect.equals("제목")) {
                        bookRetrofitInterface.getTitle(searchWord, page_size).enqueue(new Callback<BookInfoData>() {
                            @Override
                            public void onResponse(Call<BookInfoData> call, Response<BookInfoData> response) {
                                Log.d("Title open API", "success");
                                BookInfoData bookResult = response.body();
                                List<BookInfoDocsData> bookInfoDocsResult = bookResult.getBookInfoDocsData();

                                for (int i = 0; i < bookInfoDocsResult.size(); i++) {
                                    book_isbn = bookInfoDocsResult.get(i).getIsbn().substring(11);
                                    isbnList.add(book_isbn);
                                    Log.d("isbn", book_isbn);
                                }

                                //isStartSearch = true;
                                searchBookIsbn(bookRetrofitInterface);
                            }

                            @Override
                            public void onFailure(Call<BookInfoData> call, Throwable t) {
                                Log.d("Title open API", t.getMessage());
                            }
                        });
                    }
                    // spinner 선택이 저자인 경우 저자 검색 결과의 isbn 가져오기
                    else if (searchSelect.equals("저자")) {
                        bookRetrofitInterface.getAuthor(searchWord, page_size).enqueue(new Callback<BookInfoData>() {
                            @Override
                            public void onResponse(Call<BookInfoData> call, Response<BookInfoData> response) {
                                Log.d("Author open API", "success");
                                BookInfoData bookResult = response.body();
                                List<BookInfoDocsData> bookInfoDocsResult = bookResult.getBookInfoDocsData();

                                for (int i = 0; i < bookInfoDocsResult.size(); i++) {
                                    book_isbn = bookInfoDocsResult.get(i).getIsbn().substring(11);
                                    isbnList.add(book_isbn);
                                    Log.d("isbn", book_isbn);
                                }

                                //isStartSearch = true;
                                searchBookIsbn(bookRetrofitInterface);
                            }

                            @Override
                            public void onFailure(Call<BookInfoData> call, Throwable t) {
                                Log.d("Author open API", t.getMessage());
                            }
                        });
                    }

                    // spinner 선택이 isbn인 경우 입력된 isbn 가져오기
                    else if (searchSelect.equals("isbn")) {
                        isbnList.add(searchWord);
                        //isStartSearch = true;
                        searchBookIsbn(bookRetrofitInterface);
                    }
                }
            }
        });

        // 도서 검색 결과 클릭시 기록 정보 입력 화면으로 전환
//        rl_record_input_search_result_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), RecordInfo.class);
//                intent.putExtra("account_id", accountId);
//                intent.putExtra("title", tv_record_input_search_result_title.getText());
//                intent.putExtra("isbn", tv_record_input_search_result_isbn.getText());
//                intent.putExtra("author", tv_record_input_search_result_author.getText());
//                intent.putExtra("publisher", tv_record_input_search_result_publisher.getText());
//                intent.putExtra("date_published", tv_record_input_search_result_date_published.getText());
//                intent.putExtra("price_origin", Integer.parseInt(tv_record_input_search_result_price_orgin.getText().toString()));
//                intent.putExtra("image_url", image_url);
//                intent.putExtra("link", link);
//                startActivity(intent);
//            }
//        });

    }

    // isbn 도서 데이터 받아오는 메서드
    public void searchBookIsbn(BookRetrofitInterface bookRetrofitInterface) {
        Log.d("start search isbn", searchSelect);
        for (int i = 0; i < isbnList.size(); i++) {
            String isbn = isbnList.get(i);
            bookRetrofitInterface.getIsbn(isbn, page_size).enqueue(new Callback<BookInfoData>() {
                @Override
                public void onResponse(Call<BookInfoData> call, Response<BookInfoData> response) {
                    Log.d("ISBN open API", "success");
                    BookInfoData bookResult = response.body();

                    // 도서 검색된 결과 개수가 0일 경우
                    if (bookResult.getResultCount() == 0) {
                        lv_record_input_result.setVisibility(View.GONE);
                        tv_record_input_search_no_input.setVisibility(View.GONE);
                        tv_record_input_search_no_result.setVisibility(View.VISIBLE);
                    }
                    else {
                        // 검색된 도서 정보 레이아웃 보이기
                        lv_record_input_result.setVisibility(View.VISIBLE);
                        tv_record_input_search_no_input.setVisibility(View.GONE);
                        tv_record_input_search_no_result.setVisibility(View.GONE);

                        List<BookInfoDocsData> bookInfoDocsResult = bookResult.getBookInfoDocsData();

                        BookInfoDocsData bookItem = new BookInfoDocsData();
                        bookItem.setBookTitle(bookInfoDocsResult.get(0).getBookTitle());
                        bookItem.setBooKAuthor(bookInfoDocsResult.get(0).getBooKAuthor());
                        bookItem.setBookPublisher(bookInfoDocsResult.get(0).getBookPublisher());
                        bookItem.setBookDate(bookInfoDocsResult.get(0).getBookDate());
                        bookItem.setBookPrice(bookInfoDocsResult.get(0).getBookPrice());
                        bookItem.setBookImage(bookInfoDocsResult.get(0).getBookImage());
                        bookItem.setBookLink(bookInfoDocsResult.get(0).getBookLink());
                        bookItem.setIsbn(isbn);
                        bookItem.setUserNow(accountId);

                        bookList.add(bookItem);
                        recordInputSearchListAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<BookInfoData> call, Throwable t) {
                    Log.d("Author open API", t.getMessage());
                }
            });
        }
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
package org.techtown.archivebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordDescribe extends AppCompatActivity {

    RatingBar rb_record_input_book_score;
    EditText et_record_input_content;
    RadioGroup rg_record_is_public;
    RadioButton rb_record_public;
    RadioButton rb_record_private;
    Button btn_record_register;

    boolean is_record_public;

    String account_id;

    Calendar calToday = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    Retrofit retrofit;
    BookOurRetrofitInterface bookOurRetrofitInterface;
    RecordRetrofitInterface recordRetrofitInterface;
    KeywordRetrofitInterface keywordRetrofitInterface;
    KeywordPyRetrofitInterface keywordPyRetrofitInterface;
    RecommendRetrofitInterface recommendRetrofitInterface;

    KeywordData keywordData = new KeywordData();
    RecommendData recommendData = new RecommendData();

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    //keyword Server
    private final String PY_BASE_URL = "http://52.79.183.67:5000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_describe);

        rb_record_input_book_score = (RatingBar) findViewById(R.id.rb_record_input_book_score);
        et_record_input_content = (EditText) findViewById(R.id.et_record_input_content);
        rg_record_is_public = (RadioGroup) findViewById(R.id.rg_record_is_public);
        rb_record_public = (RadioButton) findViewById(R.id.rb_record_public);
        rb_record_private = (RadioButton) findViewById(R.id.rb_record_private);
        btn_record_register = (Button) findViewById(R.id.btn_record_register);

        Intent intentGet = getIntent();
        account_id = intentGet.getStringExtra("account_id");

        Log.d("accountId-record-descri", account_id);

        // ??????/????????? ???????????????
        rg_record_is_public.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_record_public:
                        is_record_public = true;
                        break;
                    case R.id.rb_record_private:
                        is_record_public = false;
                        break;
                }
            }
        });

        // Retrofit ?????? ??????
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

        bookOurRetrofitInterface = retrofit.create(BookOurRetrofitInterface.class);
        recordRetrofitInterface = retrofit.create(RecordRetrofitInterface.class);
        keywordRetrofitInterface = retrofit.create(KeywordRetrofitInterface.class);
        keywordPyRetrofitInterface = retrofit2.create(KeywordPyRetrofitInterface.class);
        recommendRetrofitInterface = retrofit.create(RecommendRetrofitInterface.class);

        // ??? ???????????? ?????? ????????? ?????? ?????? ?????? ??????
        btn_record_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????? ?????? DB ?????? ?????? ???????????? ????????? ?????? ?????? ????????????
                bookOurRetrofitInterface.getDetail(intentGet.getStringExtra("isbn")).enqueue(new Callback<BookData>() {
                    @Override
                    public void onResponse(Call<BookData> call, Response<BookData> response) {
                        Log.d("server book detail", "success");
                        BookData bookResult = response.body();
                        if (bookResult == null) {

                            // DB??? ????????? ?????? ????????? ??????
                            BookData bookData = new BookData();
                            bookData.setIsbn(intentGet.getStringExtra("isbn"));
                            bookData.setTitle(intentGet.getStringExtra("title"));
                            bookData.setAuthor(intentGet.getStringExtra("author"));
                            bookData.setPublisher(intentGet.getStringExtra("publisher"));
                            bookData.setDate_published(intentGet.getStringExtra("date_published"));
                            bookData.setPrice_origin(intentGet.getIntExtra("price_origin", 0));
                            bookData.setImage_url(intentGet.getStringExtra("image_url"));
                            bookData.setLink(intentGet.getStringExtra("link"));

                            // Book ????????? DB??? ??????
                            bookOurRetrofitInterface.postRegister(bookData).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {

                                    if (response.code() == 200) {
                                        Log.d("server book register", "success" + " " + response);
                                    }
                                    else {
                                        Log.d("book datas", bookData.getIsbn() + " " + bookData.getTitle() + " " + bookData.getAuthor() + " " + bookData.getPublisher() + " " + bookData.getDate_published() + " " + bookData.getPrice_origin() + " " + bookData.getImage_url() + " " + bookData.getLink());
                                        Toast.makeText(RecordDescribe.this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                        Log.d("server book register", "fail" + " " + response);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Log.d("server book register", "fail" + t);
                                }
                            });
                        }

                        // DB??? ????????? ?????? ?????? ????????? ??????
                        RecordData recordData = new RecordData();
                        recordData.setAccount_id(account_id);
                        recordData.setIsbn(intentGet.getStringExtra("isbn"));
                        recordData.setDate_started(intentGet.getStringExtra("date_started"));
                        recordData.setDate_finished(intentGet.getStringExtra("date_finished"));
                        recordData.setDate_writing(dateFormat.format(calToday.getTime()).toString());
                        recordData.setBook_score((int)rb_record_input_book_score.getRating());
                        recordData.setContent(et_record_input_content.getText().toString());
                        recordData.setIs_public(is_record_public);

                        // ?????? ?????? DB??? ????????????
                        recordRetrofitInterface.postRegister(recordData).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.code() == 200) {
                                    Log.d("server record register", "success" + " " + response + " " + intentGet.getStringExtra("isbn"));

                                    // ?????? ?????? ?????? ???????????? ?????? ????????? ???????????? DB??? ????????????????????? ??????
                                    keywordRetrofitInterface.getCount(intentGet.getStringExtra("isbn")).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            Log.d("server keyword count", "success" + " " + response);
                                            Log.d("keyword count : ", Integer.toString(response.body()));

                                            // DB??? ????????? ???????????? ?????? ?????? ????????? ??????????????? ????????? ????????????
                                            if (response.body() == 0) {
                                                keywordPyRetrofitInterface.getList(intentGet.getStringExtra("link")).enqueue(new Callback<KeywordPyData>() {
                                                    @Override
                                                    public void onResponse(Call<KeywordPyData> call, Response<KeywordPyData> response) {
                                                        Log.d("python keyword list", "success" + " " + response);

                                                        List<String> keywordPyResult = response.body().getKeyword();
                                                        Log.d("python keyword result", "keyword : " + keywordPyResult);

                                                        // ????????? ????????? DB??? ????????????
                                                        for (int i = 0; i < keywordPyResult.size(); i++) {
                                                            keywordData.setKeyword(keywordPyResult.get(i));
                                                            keywordData.setIsbn(intentGet.getStringExtra("isbn"));

                                                            keywordRetrofitInterface.postRegister(keywordData).enqueue(new Callback<Integer>() {
                                                                @Override
                                                                public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                                    if (response.code() == 200) {
                                                                        Log.d("server keyword register", "success" + " " + response);
                                                                    }
                                                                    else {
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

                                                    @Override
                                                    public void onFailure(Call<KeywordPyData> call, Throwable t) {
                                                        Log.d("python keyword list", "fail" + " " + t);
                                                    }
                                                });
                                            }
                                            else {
                                                Log.d("keyword data", "exist");
                                            }

                                            // ?????? ????????? 4 ????????? ?????? ?????? ????????? ????????? ??????
                                            if (recordData.getBook_score() >= 4) {
                                                keywordRetrofitInterface.getList(recordData.getIsbn()).enqueue(new Callback<List<KeywordData>>() {
                                                    @Override
                                                    public void onResponse(Call<List<KeywordData>> call, Response<List<KeywordData>> response) {
                                                        Log.d("server keyword list", "success" + " " + response);

                                                        List<KeywordData> keywordResultList = response.body();

                                                        // ?????? ????????? ???????????? ????????? ????????? ?????? ????????? isbn ??????
                                                        for (int i = 0; i < keywordResultList.size(); i++) {
                                                            keywordRetrofitInterface.getSame(keywordResultList.get(i).getKeyword(), recordData.getIsbn()).enqueue(new Callback<List<KeywordData>>() {
                                                                @Override
                                                                public void onResponse(Call<List<KeywordData>> call, Response<List<KeywordData>> response) {
                                                                    Log.d("server keyword same", "success" + " " + response);

                                                                    for (int j = 0; j < response.body().size(); j++) {
                                                                        recommendData.setIsbn(response.body().get(j).getIsbn());
                                                                        recommendData.setAccount_id(account_id);

                                                                        // ????????? ????????? isbn ?????? DB??? ??????
                                                                        recommendRetrofitInterface.postRegister(recommendData).enqueue(new Callback<Integer>() {
                                                                            @Override
                                                                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                                                                Log.d("server recomm register", "success" + " " + response);
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<Integer> call, Throwable t) {
                                                                                Log.d("server recomm register", "fail" + " " + t);
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<List<KeywordData>> call, Throwable t) {
                                                                    Log.d("server keyword same", "fail" + " " + t);
                                                                }
                                                            });
                                                        }

                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<KeywordData>> call, Throwable t) {
                                                        Log.d("server keyword list", "fail" + " " + t);
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            Log.d("server keyword count", "fail" + " " + t);
                                        }
                                    });

                                    // ??? ?????? ?????? ?????? ??????
                                    Intent intentPut = new Intent(getApplicationContext(), RecordComplete.class);
                                    intentPut.putExtra("account_id", account_id);
                                    startActivity(intentPut);
                                    finish();
                                }
                                else {
                                    Log.d("record datas", recordData.getIsbn() + " " + recordData.getAccount_id() + " " + recordData.getContent() + " " + recordData.getDate_started() + " " + recordData.getDate_finished() + " " + recordData.getDate_writing() + " " + recordData.getBook_score() + " " + recordData.getIs_public());
                                    Toast.makeText(RecordDescribe.this, "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                    Log.d("server record register", "fail" + " " + response);
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Toast.makeText(RecordDescribe.this, "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                Log.d("server record register", "fail" + t);
                            }
                        });

            }

                    @Override
                    public void onFailure(Call<BookData> call, Throwable t) {
                        Log.d("server book register", "fail" + t);
                    }
                });
            }
        });

    }

    // ???????????? ?????????
    @Override
    public void onBackPressed() {
        // ?????? ?????? ?????? ?????? dialog ?????????
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordDescribe.this)
                .setMessage("?????? ????????? ?????????????????????????")
                .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ????????? ????????? ?????? ?????? ??????
                    }
                })
                .setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ??? ????????? accountId ??? ??????????????? ????????????
                        Intent intentBack = new Intent(RecordDescribe.this, MainActivity.class);
                        intentBack.putExtra("account_id", account_id);
                        Log.d("intentBack - accountId", account_id);
                        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentBack);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

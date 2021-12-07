package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class SellRegisConfirm extends AppCompatActivity {

    EditText et_sell_regis_input_price_registerd;
    Button btn_sell_register;

    Calendar calToday = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    Retrofit retrofit;
    BookOurRetrofitInterface bookOurRetrofitInterface;
    SellingRetrofitInterface sellingRetrofitInterface;
    KeywordRetrofitInterface keywordRetrofitInterface;
    KeywordPyRetrofitInterface keywordPyRetrofitInterface;

    String account_id;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference imgRef;

    String imgStr;

    KeywordData keywordData = new KeywordData();

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
        setContentView(R.layout.activity_sell_regis_confirm);

        et_sell_regis_input_price_registerd = (EditText) findViewById(R.id.et_sell_regis_input_price_registerd);
        btn_sell_register = (Button) findViewById(R.id.btn_sell_register);

        Intent intentGet = getIntent();
        account_id = intentGet.getStringExtra("account_id");

        Log.d("accountId-sell-comfirm", account_id);

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

        sellingRetrofitInterface = retrofit.create(SellingRetrofitInterface.class);
        bookOurRetrofitInterface = retrofit.create(BookOurRetrofitInterface.class);
        keywordRetrofitInterface = retrofit.create(KeywordRetrofitInterface.class);
        keywordPyRetrofitInterface = retrofit2.create(KeywordPyRetrofitInterface.class);

        //Firebase DB 관리 객체와 chat노드 참조객체 얻어오기
        firebaseDatabase = FirebaseDatabase.getInstance("https://archibook-8eca7-default-rtdb.asia-southeast1.firebasedatabase.app/");
        imgRef = firebaseDatabase.getReference();

        btn_sell_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 해당 도서 DB 저장 여부 파악하고 저장이 안된 경우 저장하기
                bookOurRetrofitInterface.getDetail(intentGet.getStringExtra("isbn")).enqueue(new Callback<BookData>() {
                    @Override
                    public void onResponse(Call<BookData> call, Response<BookData> response) {
                        Log.d("server book detail", "success");
                        BookData bookResult = response.body();
                        if (bookResult == null) {

                            // DB에 저장할 도서 데이터 생성
                            BookData bookData = new BookData();
                            bookData.setIsbn(intentGet.getStringExtra("isbn"));
                            bookData.setTitle(intentGet.getStringExtra("title"));
                            bookData.setAuthor(intentGet.getStringExtra("author"));
                            bookData.setPublisher(intentGet.getStringExtra("publisher"));
                            bookData.setDate_published(intentGet.getStringExtra("date_published"));
                            bookData.setPrice_origin(intentGet.getIntExtra("price_origin", 0));
                            bookData.setImage_url(intentGet.getStringExtra("image_url"));
                            bookData.setLink(intentGet.getStringExtra("link"));

                            // Book 데이터 DB에 저장
                            bookOurRetrofitInterface.postRegister(bookData).enqueue(new Callback<Integer>() {
                                @Override
                                public void onResponse(Call<Integer> call, Response<Integer> response) {

                                    if (response.code() == 200) {
                                        Log.d("server book register", "success" + " " + response);
                                    }
                                    else {
                                        Log.d("book datas", bookData.getIsbn() + " " + bookData.getTitle() + " " + bookData.getAuthor() + " " + bookData.getPublisher() + " " + bookData.getDate_published() + " " + bookData.getPrice_origin() + " " + bookData.getImage_url() + " " + bookData.getLink());
                                        Toast.makeText(SellRegisConfirm.this, "도서 등록 실패", Toast.LENGTH_SHORT).show();
                                        Log.d("server book register", "fail" + " " + response);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Integer> call, Throwable t) {
                                    Log.d("server book register", "fail" + t);
                                }
                            });
                        }

                        // DB에 저장할 도서 판매 데이터 생성
                        SellingData sellingData = new SellingData();
                        long timeNow = System.currentTimeMillis();

                        sellingData.setIsbn(intentGet.getStringExtra("isbn"));
                        sellingData.setAccount_id(account_id);
                        if(!et_sell_regis_input_price_registerd.getText().toString().equals("")) {
                            sellingData.setPrice_registerd(Integer.parseInt(et_sell_regis_input_price_registerd.getText().toString()));
                        }
                        // image_path 받아오기
                        sellingData.setImage_path(intentGet.getStringExtra("image_path"));
                        //sellingData.setImage_path("-");
                        sellingData.setDescribe_detail(intentGet.getStringExtra("describe_detail"));
                        sellingData.setDate_registerd(dateFormat.format(calToday.getTime()).toString());

                        // 이미지 문자열 파이어베이스에 저장하기
//                        imgStr = intentGet.getStringExtra("image_path");
//                        imgRef.child("image").child(account_id + timeNow).push().setValue(imgStr);

                        // 판매 도서 DB에 저장하기
                        sellingRetrofitInterface.postRegister(sellingData).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {

                                if (response.code() == 200) {
                                    Log.d("server selling register", "success" + " " + response);

                                    // 판매 도서 등록 성공하면 해당 도서의 키워드가 DB에 저장되어있는지 확인
                                    keywordRetrofitInterface.getCount(intentGet.getStringExtra("isbn")).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            Log.d("server keyword count", "success" + " " + response);
                                            Log.d("keyword count : ", Integer.toString(response.body()));

                                            // DB에 저장된 키워드가 없을 경우 파이썬 서버로부터 키워드 받아오기
                                            if (response.body() == 0) {
                                                keywordPyRetrofitInterface.getList(intentGet.getStringExtra("link")).enqueue(new Callback<KeywordPyData>() {
                                                    @Override
                                                    public void onResponse(Call<KeywordPyData> call, Response<KeywordPyData> response) {
                                                        Log.d("python keyword list", "success" + " " + response);

                                                        List<String> keywordPyResult = response.body().getKeyword();
                                                        Log.d("python keyword result", "keyword : " + keywordPyResult);

                                                        // 받아온 키워드 DB에 저장하기
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
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            Log.d("server keyword count", "fail" + " " + t);
                                        }
                                    });

                                    // 글 등록 완료 화면 전환
                                    Intent intentPut = new Intent(getApplicationContext(), SellRegisComplete.class);
                                    intentPut.putExtra("account_id", account_id);
                                    startActivity(intentPut);
                                    finish();
                                }
                                else {
                                    Log.d("selling datas", sellingData.getIsbn() + " " + sellingData.getAccount_id() + " " + sellingData.getDescribe_detail() + " " + sellingData.getPrice_registerd() + " " + sellingData.getDate_registerd());
                                    Toast.makeText(SellRegisConfirm.this, "판매 도서 등록 실패", Toast.LENGTH_SHORT).show();
                                    Log.d("server selling register", "fail" + " " + response);
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Toast.makeText(SellRegisConfirm.this, "판매 도서 등록 실패", Toast.LENGTH_SHORT).show();
                                Log.d("server selling register", "fail" + t);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<BookData> call, Throwable t) {
                        Log.d("server book detail", "fail" + t);
                    }
                });
            }
        });
    }

    // 뒤로가기 버튼 클릭시
    @Override
    public void onBackPressed() {
        // 판매 취소 여부 묻는 dialog 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(SellRegisConfirm.this)
                .setMessage("판매 등록을 취소하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 아니오 선택시 화면 전환 없음
                    }
                })
                .setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 예 선택시 accountId 값 전달하면서 뒤로가기
                        Intent intentBack = new Intent(SellRegisConfirm.this, MainActivity.class);
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
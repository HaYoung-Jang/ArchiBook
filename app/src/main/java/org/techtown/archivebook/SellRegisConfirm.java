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

        sellingRetrofitInterface = retrofit.create(SellingRetrofitInterface.class);
        bookOurRetrofitInterface = retrofit.create(BookOurRetrofitInterface.class);
        keywordRetrofitInterface = retrofit.create(KeywordRetrofitInterface.class);
        keywordPyRetrofitInterface = retrofit2.create(KeywordPyRetrofitInterface.class);

        //Firebase DB ?????? ????????? chat?????? ???????????? ????????????
        firebaseDatabase = FirebaseDatabase.getInstance("https://archibook-8eca7-default-rtdb.asia-southeast1.firebasedatabase.app/");
        imgRef = firebaseDatabase.getReference();

        btn_sell_register.setOnClickListener(new View.OnClickListener() {
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
                                        Toast.makeText(SellRegisConfirm.this, "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
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
                        SellingData sellingData = new SellingData();
                        long timeNow = System.currentTimeMillis();

                        sellingData.setIsbn(intentGet.getStringExtra("isbn"));
                        sellingData.setAccount_id(account_id);
                        if(!et_sell_regis_input_price_registerd.getText().toString().equals("")) {
                            sellingData.setPrice_registerd(Integer.parseInt(et_sell_regis_input_price_registerd.getText().toString()));
                        }
                        // image_path ????????????
                        sellingData.setImage_path(intentGet.getStringExtra("image_path"));
                        //sellingData.setImage_path("-");
                        sellingData.setDescribe_detail(intentGet.getStringExtra("describe_detail"));
                        sellingData.setDate_registerd(dateFormat.format(calToday.getTime()).toString());

                        // ????????? ????????? ????????????????????? ????????????
//                        imgStr = intentGet.getStringExtra("image_path");
//                        imgRef.child("image").child(account_id + timeNow).push().setValue(imgStr);

                        // ?????? ?????? DB??? ????????????
                        sellingRetrofitInterface.postRegister(sellingData).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {

                                if (response.code() == 200) {
                                    Log.d("server selling register", "success" + " " + response);

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
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            Log.d("server keyword count", "fail" + " " + t);
                                        }
                                    });

                                    // ??? ?????? ?????? ?????? ??????
                                    Intent intentPut = new Intent(getApplicationContext(), SellRegisComplete.class);
                                    intentPut.putExtra("account_id", account_id);
                                    startActivity(intentPut);
                                    finish();
                                }
                                else {
                                    Log.d("selling datas", sellingData.getIsbn() + " " + sellingData.getAccount_id() + " " + sellingData.getDescribe_detail() + " " + sellingData.getPrice_registerd() + " " + sellingData.getDate_registerd());
                                    Toast.makeText(SellRegisConfirm.this, "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                    Log.d("server selling register", "fail" + " " + response);
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Toast.makeText(SellRegisConfirm.this, "?????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
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

    // ???????????? ?????? ?????????
    @Override
    public void onBackPressed() {
        // ?????? ?????? ?????? ?????? dialog ?????????
        AlertDialog.Builder builder = new AlertDialog.Builder(SellRegisConfirm.this)
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
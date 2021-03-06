package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordDetail extends AppCompatActivity {

    ImageView iv_book_record_image;
    ImageView iv_book_record_profile;
    TextView tv_book_record_nick_name;
    ImageButton ibtn_record_locked;
    TextView tv_book_record_title;
    TextView tv_book_record_date_registerd;
    TextView tv_book_record_author;
    TextView tv_book_record_date_reading;
    RatingBar rb_book_score;
    TextView tv_book_record_content;
    Button btn_change_public;
    ImageButton ibtn_delete;

    Retrofit retrofit;
    RecordRetrofitInterface recordRetrofitInterface;

    int recordId;
    String imageUrl;
    RequestOptions options;

    String accountId;

    ProfileColor profile_color = new ProfileColor();

    String backPage = "";
    String nickNameBack = "";

    boolean isPublic;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);

        iv_book_record_image = (ImageView) findViewById(R.id.iv_book_record_image);
        iv_book_record_profile = (ImageView) findViewById(R.id.iv_book_record_profile);
        tv_book_record_nick_name = (TextView) findViewById(R.id.tv_book_record_nick_name);
        ibtn_record_locked = (ImageButton) findViewById(R.id.ibtn_record_locked);
        tv_book_record_title = (TextView) findViewById(R.id.tv_book_record_title);
        tv_book_record_date_registerd = (TextView) findViewById(R.id.tv_book_record_date_registerd);
        tv_book_record_author = (TextView) findViewById(R.id.tv_book_record_author);
        tv_book_record_date_reading = (TextView) findViewById(R.id.tv_book_record_date_reading);
        rb_book_score = (RatingBar) findViewById(R.id.rb_book_score);
        tv_book_record_content = (TextView) findViewById(R.id.tv_book_record_content);
        btn_change_public = (Button) findViewById(R.id.btn_change_public);
        ibtn_delete = (ImageButton) findViewById(R.id.ibtn_delete);

        // Retrofit ?????? ??????
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recordRetrofitInterface = retrofit.create(RecordRetrofitInterface.class);

        // ?????? ?????? ???????????? ?????? ????????? record_id ????????????
        Intent intent = getIntent();
        recordId = intent.getIntExtra("record_id", 1);
        accountId = intent.getStringExtra("account_id");
        backPage = intent.getStringExtra("back_page");
        nickNameBack = intent.getStringExtra("nick_name");

        Log.d("accountId-record-detail", accountId);
        Log.d("recordId", Integer.toString(recordId));

        // ??????????????? ?????? record_id??? ????????? ????????????
        recordRetrofitInterface.getDetail(recordId).enqueue(new Callback<RecordData>() {
            @Override
            public void onResponse(Call<RecordData> call, Response<RecordData> response) {
                Log.d("server record detail", "success" + response);

                RecordData recordResult = response.body();

                // ????????? ?????????
                imageUrl = recordResult.getImage_url();
                options = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);

                if (imageUrl != null) {
                    Glide.with(getApplicationContext()).load(imageUrl).apply(options).into(iv_book_record_image);
                }
                else {
                    iv_book_record_image.setImageResource(R.drawable.img_no_book_image);
                }
                // ??????/????????? ????????? ??????
                if (recordResult.getIs_public()) {
                    ibtn_record_locked.setImageResource(R.drawable.ibtn_unlocked);
                }
                else {
                    ibtn_record_locked.setImageResource(R.drawable.ibtn_locked);
                }
                iv_book_record_profile.setBackground(profile_color.colorDrawable(getApplicationContext(), recordResult.getProfile_color()));
                tv_book_record_nick_name.setText(recordResult.getNick_name());
                tv_book_record_title.setText(recordResult.getTitle());
                tv_book_record_date_registerd.setText(recordResult.getDate_writing());
                tv_book_record_author.setText(recordResult.getAuthor());
                if (recordResult.getDate_started() == null && recordResult.getDate_finished() == null) {
                    tv_book_record_date_reading.setText("");
                }
                else if (recordResult.getDate_started() != null && recordResult.getDate_finished() == null) {
                    tv_book_record_date_reading.setText(recordResult.getDate_started() + " ~ ");
                }
                else if (recordResult.getDate_started() == null && recordResult.getDate_finished() != null) {
                    tv_book_record_date_reading.setText(" ~ " + recordResult.getDate_finished());
                }
                else {
                    tv_book_record_date_reading.setText(recordResult.getDate_started() + " ~ " + recordResult.getDate_finished());
                }
                rb_book_score.setRating(recordResult.getBook_score());
                tv_book_record_content.setText(recordResult.getContent());

                isPublic = recordResult.getIs_public();

                // ?????? ????????? ?????? ?????? ?????? ?????? ???????????? ?????? ??????
                if (isPublic) {
                    btn_change_public.setText("???????????? ??????");
                }
                // ?????? ????????? ?????? ?????? ????????? ?????? ????????? ?????? ??????
                else {
                    btn_change_public.setText("????????? ??????");
                }

                // ?????? ?????? ???????????? id??? ????????? ??? id ????????? ?????? ??????/????????? ?????? ??????, ?????? ?????? ?????????
                if (recordResult.getAccount_id().equals(accountId)) {
                    btn_change_public.setVisibility(View.VISIBLE);
                    ibtn_delete.setVisibility(View.VISIBLE);
                }
                else {
                    btn_change_public.setVisibility(View.INVISIBLE);
                    ibtn_delete.setVisibility(View.INVISIBLE);
                }

                Log.d("book score", Integer.toString(recordResult.getBook_score()));
            }

            @Override
            public void onFailure(Call<RecordData> call, Throwable t) {
                Log.d("server record detail", "fail" + " " + t + " " + recordId);
            }
        });

        btn_change_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPublic) {
                    // ????????? ?????? ?????? dialog ?????????
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecordDetail.this)
                            .setMessage("???????????? ?????????????????????????")
                            .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // ????????? ????????? ?????? ?????? ??????
                                }
                            })
                            .setNegativeButton("???", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // ??? ????????? ?????? DB is_public false??? ??????
                                    RecordData recordData = new RecordData();
                                    recordData.setRecord_id(recordId);
                                    recordData.setAccount_id(accountId);

                                    isPublic = !isPublic;

                                    recordRetrofitInterface.putPrivate(recordData).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            if (response.code() == 200) {
                                                Log.d("server record private", "success" + " " + response);

                                                btn_change_public.setText("????????? ??????");
                                                ibtn_record_locked.setImageResource(R.drawable.ibtn_locked);
                                            }
                                            else {
                                                Log.d("server record private", "fail" + " " + response);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            Log.d("server record private", "fail" + " " + t);
                                        }
                                    });
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                // ????????? ?????? ?????? dialog ?????????
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecordDetail.this)
                            .setMessage("????????? ?????????????????????????")
                            .setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // ????????? ????????? ?????? ?????? ??????
                                }
                            })
                            .setNegativeButton("???", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // ??? ????????? ?????? DB is_public true??? ??????
                                    RecordData recordData = new RecordData();
                                    recordData.setRecord_id(recordId);
                                    recordData.setAccount_id(accountId);

                                    isPublic = !isPublic;

                                    recordRetrofitInterface.putPublic(recordData).enqueue(new Callback<Integer>() {
                                        @Override
                                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                                            if (response.code() == 200) {
                                                Log.d("server record public", "success" + " " + response);

                                                btn_change_public.setText("???????????? ??????");
                                                ibtn_record_locked.setImageResource(R.drawable.ibtn_unlocked);
                                            }
                                            else {
                                                Log.d("server record public", "fail" + " " + response);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Integer> call, Throwable t) {
                                            Log.d("server record public", "fail" + " " + t);
                                        }
                                    });
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        ibtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????? ?????? ?????? ?????? dialog ?????????
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordDetail.this)
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
                                // ??? ????????? ?????? DB?????? ?????? ????????? ?????? ??? ???????????? ?????? ??????
                                recordRetrofitInterface.deleteDelete(recordId, accountId).enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        if (response.code() == 200) {
                                            Log.d("server record delete", "success" + " " + response);

                                            Intent intentList = new Intent(getApplicationContext(), MainActivity.class);
                                            intentList.putExtra("account_id", accountId);
                                            startActivity(intentList);
                                            finish();
                                        }
                                        else {
                                            Log.d("server record delete", "fail" + " " + response);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        Log.d("server record delete", "fail" + " " + t);
                                    }
                                });
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    // ???????????? ????????? accountId ??? ??????
    @Override
    public void onBackPressed() {
        Log.d("back_page", backPage);
        // ????????? ?????? ??????
//        if (backPage.equals("record")) {
//            Intent intentBackRecord = new Intent(getApplicationContext(), MyRecordList.class);
//            intentBackRecord.putExtra("account_id", accountId);
//            intentBackRecord.putExtra("nick_name", nickNameBack);
//            Log.d("intentBack - accountId", accountId);
//            startActivity(intentBackRecord);
//            finish();
//        }
//        else {
//            Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
//            intentBack.putExtra("account_id", accountId);
//            Log.d("intentBack - accountId", accountId);
//            intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intentBack);
//            finish();
//        }

        finish();
    }
}
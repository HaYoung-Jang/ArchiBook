package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendDetailList extends AppCompatActivity {

    ListView lv_recommend_list;

    RecommendDetailListAdapter recommendDetailListAdapter;

    Retrofit retrofit;
    RecommendRetrofitInterface recommendRetrofitInterface;

    String accountId;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_detail_list);

        lv_recommend_list = (ListView) findViewById(R.id.lv_recommend_list);

        Intent intentGet = getIntent();
        accountId = intentGet.getStringExtra("account_id");

        Log.d("accountId-sell-complete", accountId);

        //리스트뷰에 어댑터 연결
        recommendDetailListAdapter = new RecommendDetailListAdapter();
        lv_recommend_list.setAdapter(recommendDetailListAdapter);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recommendRetrofitInterface = retrofit.create(RecommendRetrofitInterface.class);

        // 서버로부터 추천 도서 데이터 가져오기
        recommendRetrofitInterface.getList(accountId).enqueue(new Callback<List<RecommendData>>() {
            @Override
            public void onResponse(Call<List<RecommendData>> call, Response<List<RecommendData>> response) {
                Log.d("server recomm list", "success" + " " + response);

                List<RecommendData> recommendResultList = response.body();

                for (int i = 0; i < recommendResultList.size(); i++) {
                    recommendDetailListAdapter.addItem(recommendResultList.get(i).getIsbn(), accountId, recommendResultList.get(i).getImage_url(), recommendResultList.get(i).getTitle(), recommendResultList.get(i).getAuthor(), recommendResultList.get(i).getPublisher(), recommendResultList.get(i).getDate_published());
                }
            }

            @Override
            public void onFailure(Call<List<RecommendData>> call, Throwable t) {
                Log.d("server recomm list", "fail" + " " + t);
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
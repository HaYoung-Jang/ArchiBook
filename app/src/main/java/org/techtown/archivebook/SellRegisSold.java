package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SellRegisSold extends AppCompatActivity {

    TextView tv_no_select;
    ListView lv_regis_sold;

    Retrofit retrofit;
    ChatRoomRetrofitInterface chatRoomRetrofitInterface;

    int registerdId;
    String accountId;

    SellRegisSoldAdapter sellRegisSoldAdapter;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_regis_sold);

        tv_no_select = (TextView) findViewById(R.id.tv_no_select);
        lv_regis_sold = (ListView) findViewById(R.id.lv_regis_sold);

        Intent intent = getIntent();
        registerdId = intent.getIntExtra("registerd_id", 1);
        accountId = intent.getStringExtra("account_id");

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        chatRoomRetrofitInterface = retrofit.create(ChatRoomRetrofitInterface.class);

        // 리스트뷰와 어댑터 연결
        sellRegisSoldAdapter = new SellRegisSoldAdapter();
        lv_regis_sold.setAdapter(sellRegisSoldAdapter);

        // 서버로부터 채팅 상대 데이터 받아오기
        chatRoomRetrofitInterface.getSold(registerdId, accountId).enqueue(new Callback<List<ChatRoomData>>() {
            @Override
            public void onResponse(Call<List<ChatRoomData>> call, Response<List<ChatRoomData>> response) {
                Log.d("server chat sold", "success" + " " + response);
                Log.d("chat result", response.body().get(0).getBuyer_nick());

                List<ChatRoomData> chatRoomResult = response.body();

                for (int i = 0; i < chatRoomResult.size(); i++) {
                    sellRegisSoldAdapter.addItem(chatRoomResult.get(i).getIsbn(), chatRoomResult.get(i).getBuyer_id(), chatRoomResult.get(i).getBuyer_nick(), registerdId, accountId);
                }
            }

            @Override
            public void onFailure(Call<List<ChatRoomData>> call, Throwable t) {
                Log.d("server chat sold", "fail" + " " + t);
            }
        });

        tv_no_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
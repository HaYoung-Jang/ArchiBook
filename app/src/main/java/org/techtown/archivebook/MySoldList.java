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

public class MySoldList extends AppCompatActivity {

    View v_my_page_profile_color;
    TextView tv_my_page_user_name;
    ListView lv_my_sold_list;

    String accountId;
    String name;
    String profile_color;
    String nickName;

    ArrayList<String> titleList;

    ProfileColor profileColor = new ProfileColor();

    MySoldListAdapter mySoldListAdapter;

    Retrofit retrofit;
    SoldRetrofitInterface soldRetrofitInterface;
    SellingRetrofitInterface sellingRetrofitInterface;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sold_list);

        v_my_page_profile_color = (View) findViewById(R.id.v_my_page_profile_color);
        tv_my_page_user_name = (TextView) findViewById(R.id.tv_my_page_user_name);
        lv_my_sold_list = (ListView) findViewById(R.id.lv_my_sold_list);

        Intent intent = getIntent();
        accountId = intent.getStringExtra("account_id");
        name = intent.getStringExtra("user_name");
        profile_color = intent.getStringExtra("profile_color");
        nickName = intent.getStringExtra("nick_name");

        Log.d("accountId-my-selling", accountId);

        // 사용자 이름, 프로필 색상 띄우기
        tv_my_page_user_name.setText(nickName);
        //v_my_page_profile_color.setBackground(profileColor.colorDrawable(getApplicationContext(), profile_color));
        v_my_page_profile_color.setVisibility(View.GONE);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sellingRetrofitInterface = retrofit.create(SellingRetrofitInterface.class);
        soldRetrofitInterface = retrofit.create(SoldRetrofitInterface.class);

        // 리스트뷰에 어댑터 연결
        mySoldListAdapter = new MySoldListAdapter();
        lv_my_sold_list.setAdapter(mySoldListAdapter);

        soldRetrofitInterface.getList(accountId).enqueue(new Callback<List<SoldData>>() {
            @Override
            public void onResponse(Call<List<SoldData>> call, Response<List<SoldData>> response) {
                Log.d("server sold list", "success" + " " + response);

                List<SoldData> soldResult = response.body();
                for (int i = 0; i < soldResult.size(); i++) {
//                    titleList = new ArrayList<>();
//                    sellingRetrofitInterface.getDetail(soldResult.get(i).getRegisterd_id()).enqueue(new Callback<SellingData>() {
//                        @Override
//                        public void onResponse(Call<SellingData> call, Response<SellingData> response) {
//                            Log.d("server selling detail", "success" + " " + response);
//                            titleList.add(response.body().getTitle());
//                            Log.d("title list", titleList.get(0));
//                        }
//
//                        @Override
//                        public void onFailure(Call<SellingData> call, Throwable t) {
//                            Log.d("server selling detail", "fail" + " " + t);
//                        }
//                    });
//                    // 리스트뷰에 어댑터 연결
//                    mySoldListAdapter = new MySoldListAdapter();
//                    lv_my_sold_list.setAdapter(mySoldListAdapter);
                    //Log.d("title list", titleList.get(0));

                    mySoldListAdapter.addItem(soldResult.get(i).getIsbn(), soldResult.get(i).getProfile_color(), soldResult.get(i).getNick_name(), soldResult.get(i).getTitle(), soldResult.get(i).getRegisterd_id(), accountId);
                }
            }

            @Override
            public void onFailure(Call<List<SoldData>> call, Throwable t) {
                Log.d("server sold list", "fail" + " " + t);
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
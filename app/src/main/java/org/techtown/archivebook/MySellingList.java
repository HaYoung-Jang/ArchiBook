package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MySellingList extends AppCompatActivity {

    View v_my_page_profile_color;
    TextView tv_my_page_user_name;
    ListView lv_my_selling_list;

    MySellingListAdapter mySellingListAdapter;

    Retrofit retrofit;
    SellingRetrofitInterface sellingRetrofitInterface;

    String accountId;
    String name;
    String profile_color;
    String nickName;

    ProfileColor profileColor = new ProfileColor();

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_selling_list);

        v_my_page_profile_color = (View) findViewById(R.id.v_my_page_profile_color);
        tv_my_page_user_name = (TextView) findViewById(R.id.tv_my_page_user_name);
        lv_my_selling_list = (ListView) findViewById(R.id.lv_my_selling_list);

        Intent intent = getIntent();
        accountId = intent.getStringExtra("account_id");
        name = intent.getStringExtra("user_name");
        profile_color = intent.getStringExtra("profile_color");
        nickName = intent.getStringExtra("nick_name");

        Log.d("accountId-my-selling", accountId);
        Log.d("my-selling-name,color", name + " " + profile_color);

        // 사용자 이름, 프로필 색상 띄우기
        tv_my_page_user_name.setText(nickName);
        //v_my_page_profile_color.setBackground(profileColor.colorDrawable(getApplicationContext(), "green"));
        //v_my_page_profile_color.setBackground(android.graphics.drawable.GradientDrawable@375abd0);
        //Log.d("drawable", profileColor.colorDrawable(getApplicationContext(), "green").toString());
        //v_my_page_profile_color.setBackground(profileColor.colorDrawable(getApplicationContext(), profile_color));
        v_my_page_profile_color.setVisibility(View.GONE);

        // 리스트뷰에 어댑터 연결
        mySellingListAdapter = new MySellingListAdapter();
        lv_my_selling_list.setAdapter(mySellingListAdapter);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sellingRetrofitInterface = retrofit.create(SellingRetrofitInterface.class);

        // 서버로부터 해당 사용자의 판매 내역 데이터 받아오기
        sellingRetrofitInterface.getPersonal(accountId).enqueue(new Callback<List<SellingData>>() {
            @Override
            public void onResponse(Call<List<SellingData>> call, Response<List<SellingData>> response) {
                Log.d("server selling personal", "success" + " " + response);

                List<SellingData> sellingResultList = response.body();

                if (sellingResultList.size() != 0) {
                    for (int i = 0; i < sellingResultList.size(); i++){
                        mySellingListAdapter.addItem(sellingResultList.get(i).getIsbn(), sellingResultList.get(i).getAccount_id(), sellingResultList.get(i).getProfile_color(), sellingResultList.get(i).getTitle(), sellingResultList.get(i).getDate_registerd(), sellingResultList.get(i).getNick_name(), sellingResultList.get(i).getAuthor(), sellingResultList.get(i).getPublisher(), sellingResultList.get(i).getDate_published(), sellingResultList.get(i).getRegisterd_id(), sellingResultList.get(i).getPrice_registerd(), accountId);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SellingData>> call, Throwable t) {
                Log.d("server selling personal", "fail" + " " + t);
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
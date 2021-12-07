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

public class MyRecordList extends AppCompatActivity {

    View v_my_page_profile_color;
    TextView tv_my_page_user_name;
    ListView lv_my_record_list;

    MyRecordListAdapter myRecordListAdapter;

    Retrofit retrofit;
    RecordRetrofitInterface recordRetrofitInterface;

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
        setContentView(R.layout.activity_my_record_list);

        v_my_page_profile_color = (View) findViewById(R.id.v_my_page_profile_color);
        tv_my_page_user_name = (TextView) findViewById(R.id.tv_my_page_user_name);
        lv_my_record_list = (ListView) findViewById(R.id.lv_my_record_list);

        Intent intent = getIntent();
        accountId = intent.getStringExtra("account_id");
        name = intent.getStringExtra("user_name");
        nickName = intent.getStringExtra("nick_name");

        Log.d("accountId-my-selling", accountId);

        // 사용자 이름, 프로필 색상 띄우기
        tv_my_page_user_name.setText(nickName);
        //v_my_page_profile_color.setBackground(profileColor.colorDrawable(getApplicationContext(), profile_color));
        v_my_page_profile_color.setVisibility(View.GONE);

        //리스트뷰에 어댑터 연결
        myRecordListAdapter = new MyRecordListAdapter();
        lv_my_record_list.setAdapter(myRecordListAdapter);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recordRetrofitInterface = retrofit.create(RecordRetrofitInterface.class);

        // 서버로부터 해당 사용자의 판매 내역 데이터 받아오기
        recordRetrofitInterface.getPersonal(accountId).enqueue(new Callback<List<RecordData>>() {
            @Override
            public void onResponse(Call<List<RecordData>> call, Response<List<RecordData>> response) {
                Log.d("server record personal", "success" + " " + response);

                List<RecordData> recordResultList = response.body();

                if (recordResultList.size() != 0) {
                    for (int i = 0; i < recordResultList.size(); i++) {
                        myRecordListAdapter.addItem(recordResultList.get(i).getRecord_id(), recordResultList.get(i).getImage_url(), recordResultList.get(i).getProfile_color(), recordResultList.get(i).getAccount_id(), recordResultList.get(i).getNick_name(), recordResultList.get(i).getTitle(), recordResultList.get(i).getIs_public(), recordResultList.get(i).getDate_writing(), recordResultList.get(i).getAuthor(), recordResultList.get(i).getDate_started(), recordResultList.get(i).getDate_finished(), recordResultList.get(i).getBook_score(), recordResultList.get(i).getContent(), accountId);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RecordData>> call, Throwable t) {
                Log.d("server record personal", "fail" + " " + t);
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
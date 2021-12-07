package org.techtown.archivebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecordFragment extends Fragment {

    Button btn_is_record_all;
    ListView lv_record_list;
    Button btn_record_write;

    RecordListAdapter recordAdapter;

    Calendar calToday = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    Retrofit retrofit;
    RecordRetrofitInterface recordRetrofitInterface;

    boolean is_record_all = true;
    String accountId;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_record, container, false);

        btn_is_record_all = (Button) rootView.findViewById(R.id.btn_is_record_all);
        lv_record_list = (ListView) rootView.findViewById(R.id.lv_record_list);
        btn_record_write = (Button) rootView.findViewById(R.id.btn_record_write);

        // MainActivity로부터 사용자 id 값 받기
        if(getArguments() != null) {
            accountId = getArguments().getString("accountId");
        }
        // id값이 null인 경우 로그인 화면으로 전환
        else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        Log.d("accountId-frag-record", accountId);

        // 리스뷰에 어댑터 연결
        recordAdapter = new RecordListAdapter();
        lv_record_list.setAdapter(recordAdapter);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recordRetrofitInterface = retrofit.create(RecordRetrofitInterface.class);

        if (is_record_all) {
            // 모두의 기록장 표시
            btn_is_record_all.setText("모두의 기록장");

            // 전체 기록 목록 데이터 가져오기
            recordRetrofitInterface.getList().enqueue(new Callback<List<RecordData>>() {
                @Override
                public void onResponse(Call<List<RecordData>> call, Response<List<RecordData>> response) {
                    Log.d("server record list", "success");
                    List<RecordData> recordListResult = response.body();

                    // 받아온 데이터 리스트뷰에 띄우기
                    for (int i = 0; i < recordListResult.size(); i++) {
                        lv_record_list.setVisibility(View.VISIBLE);
                        recordAdapter.addItem(recordListResult.get(i).getRecord_id(), recordListResult.get(i).getImage_url(), recordListResult.get(i).getProfile_color(), recordListResult.get(i).getAccount_id(), recordListResult.get(i).getNick_name(), recordListResult.get(i).getTitle(), recordListResult.get(i).getIs_public(), recordListResult.get(i).getDate_writing(), recordListResult.get(i).getAuthor(), recordListResult.get(i).getDate_started(), recordListResult.get(i).getDate_finished(), recordListResult.get(i).getBook_score(), recordListResult.get(i).getContent(), accountId);
                    }
                }

                @Override
                public void onFailure(Call<List<RecordData>> call, Throwable t) {
                    Log.d("server record list", "fail");
                }
            });
        }

        // 기록 공개/비공개 목록 전환 버튼 클릭시
        btn_is_record_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_record_all = !is_record_all;

                // 리스트뷰 기존 아이템 리셋
                recordAdapter.resetItem();

                // 모두의 기록장 버튼 클릭 시
                if (is_record_all) {
                    // 모두의 기록장 표시
                    btn_is_record_all.setText("모두의 기록장");

                    // 전체 기록 목록 데이터 가져오기
                    recordRetrofitInterface.getList().enqueue(new Callback<List<RecordData>>() {
                        @Override
                        public void onResponse(Call<List<RecordData>> call, Response<List<RecordData>> response) {
                            Log.d("server record list", "success");
                            List<RecordData> recordListResult = response.body();

                            // 받아온 전체 기록 데이터 리스트뷰에 띄우기
                            lv_record_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < recordListResult.size(); i++) {
                                recordAdapter.addItem(recordListResult.get(i).getRecord_id(), recordListResult.get(i).getImage_url(), recordListResult.get(i).getProfile_color(), recordListResult.get(i).getAccount_id(), recordListResult.get(i).getNick_name(), recordListResult.get(i).getTitle(), recordListResult.get(i).getIs_public(), recordListResult.get(i).getDate_writing(), recordListResult.get(i).getAuthor(), recordListResult.get(i).getDate_started(), recordListResult.get(i).getDate_finished(), recordListResult.get(i).getBook_score(), recordListResult.get(i).getContent(), accountId);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RecordData>> call, Throwable t) {
                            Log.d("server record list", "fail");
                        }
                    });
                }
                // 나의 기록장 버튼 클릭 시
                else {
                    btn_is_record_all.setText("나의 기록장");

                    recordRetrofitInterface.getPersonal(accountId).enqueue(new Callback<List<RecordData>>() {
                        @Override
                        public void onResponse(Call<List<RecordData>> call, Response<List<RecordData>> response) {
                            Log.d("server record personal", "success");
                            List<RecordData> recordPersonalList = response.body();

                            // 기록의 개수가 존재할 경우에
                            if (recordPersonalList.size() != 0) {
                                lv_record_list.setVisibility(View.VISIBLE);
                                // 받아온 개인 기록 데이터 리스트뷰에 띄우기
                                for (int i = 0; i < recordPersonalList.size(); i++) {
                                    recordAdapter.addItem(recordPersonalList.get(i).getRecord_id(), recordPersonalList.get(i).getImage_url(), recordPersonalList.get(i).getProfile_color(), recordPersonalList.get(i).getAccount_id(), recordPersonalList.get(i).getNick_name(), recordPersonalList.get(i).getTitle(), recordPersonalList.get(i).getIs_public(), recordPersonalList.get(i).getDate_writing(), recordPersonalList.get(i).getAuthor(), recordPersonalList.get(i).getDate_started(), recordPersonalList.get(i).getDate_finished(), recordPersonalList.get(i).getBook_score(), recordPersonalList.get(i).getContent(), accountId);
                                }
                            }
                            else {
                                lv_record_list.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RecordData>> call, Throwable t) {
                            Log.d("server record personal", "fail");
                        }
                    });
                }
            }
        });

        // 글쓰기 버튼 클릭시 글 작성 화면으로 전환
        btn_record_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordInputSearch.class);
                intent.putExtra("account_id", accountId);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
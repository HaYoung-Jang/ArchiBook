package org.techtown.archivebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class KeywordFragment extends Fragment {

    EditText et_keyword_search;
    ImageButton ibtn_search;
    ListView lv_book_list_keyword_search;
    TextView tv_keyword_no_search_result;

    String accountId;

    String keyword;

    KeywordSearchListAdapter keywordSearchListAdapter;

    Retrofit retrofit;
    KeywordRetrofitInterface keywordRetrofitInterface;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_keyword, container, false);

        et_keyword_search = (EditText) rootView.findViewById(R.id.et_keyword_search);
        ibtn_search = (ImageButton) rootView.findViewById(R.id.ibtn_search);
        lv_book_list_keyword_search = (ListView) rootView.findViewById(R.id.lv_book_list_keyword_search);
        tv_keyword_no_search_result = (TextView) rootView.findViewById(R.id.tv_keyword_no_search_result);

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
        Log.d("accountId-frag-keyword", accountId);

        // 리스트뷰에 어댑터 연결
        keywordSearchListAdapter = new KeywordSearchListAdapter();
        lv_book_list_keyword_search.setAdapter(keywordSearchListAdapter);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        keywordRetrofitInterface = retrofit.create(KeywordRetrofitInterface.class);

        // 검색 버튼 클릭시 키워드 검색
        ibtn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_keyword_no_search_result.setVisibility(View.GONE);
                lv_book_list_keyword_search.setVisibility(View.VISIBLE);

                // 리스트뷰 기존 아이템 리셋
                keywordSearchListAdapter.resetItem();

                keyword = et_keyword_search.getText().toString();

                // 서버로부터 검색 결과 가져오기
                keywordRetrofitInterface.getSearch(keyword).enqueue(new Callback<List<KeywordData>>() {
                    @Override
                    public void onResponse(Call<List<KeywordData>> call, Response<List<KeywordData>> response) {
                        Log.d("server keyword search", "success" + " " + response);

                        List<KeywordData> keywordResult = response.body();

                        for (int i = 0; i < keywordResult.size(); i++) {
                            keywordSearchListAdapter.addItem(keywordResult.get(i).getKeyword(), keywordResult.get(i).getIsbn(), keywordResult.get(i).getImage_url(), keywordResult.get(i).getTitle(), keywordResult.get(i).getAuthor(), keywordResult.get(i).getPublisher(), keywordResult.get(i).getDate_published());
                        }

                        // 검색 결과가 없으면 검색 결과 없음 텍스트 띄우기
                        if (keywordResult.size() == 0) {
                            tv_keyword_no_search_result.setVisibility(View.VISIBLE);
                            lv_book_list_keyword_search.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<KeywordData>> call, Throwable t) {
                        Log.d("server keyword search", "fail" + " " + t);
                    }
                });
            }
        });

        return rootView;
    }
}
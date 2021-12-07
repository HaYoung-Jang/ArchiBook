package org.techtown.archivebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {

    EditText et_sell_regis_search;
    ImageButton ibtn_search;
    TextView tv_selling_list_is_all;
    ImageButton ibtn_change_list;
    Button btn_selling_register;

    ListView lv_book_list_selling;
    SellingListAdapter sellingAdapter;

    Calendar calToday = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    Retrofit retrofit;
    SellingRetrofitInterface sellingRetrofitInterface;

    boolean is_list_all = true;

    String accountId;

    ArrayList<SellingData> sellingResult;

    ArrayList<SellingData> sellingAllList = new ArrayList<>();
    ArrayList<SellingData> sellingList = new ArrayList<>();
    ArrayList<SellingData> sellingAdapterList = new ArrayList<>();

    //private ActivityResultLauncher<Intent> resultLauncher;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        et_sell_regis_search = (EditText) rootView.findViewById(R.id.et_sell_regis_search);
        ibtn_search = (ImageButton) rootView.findViewById(R.id.ibtn_search);
        tv_selling_list_is_all = (TextView) rootView.findViewById(R.id.tv_selling_list_is_all);
        ibtn_change_list = (ImageButton) rootView.findViewById(R.id.ibtn_change_list);
        lv_book_list_selling = (ListView) rootView.findViewById(R.id.lv_book_list_selling);
        btn_selling_register = (Button) rootView.findViewById(R.id.btn_selling_register);

        // activity 콜백 함수 - 뒤로 가기로 화면 이동시 accountId 값 받기
//        resultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if(result.getResultCode() == RESULT_OK) {
//                            Intent intentToBack = result.getData();
//                            String CallType = intentToBack.getStringExtra("accountId");
//                            accountId = CallType;
//                        }
//                    }
//                }
//        );

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
        Log.d("accountId-frag", accountId);

        // test
        //Toast.makeText(getActivity(), "Today's date : " + calToday.get(Calendar.YEAR) + "." + (calToday.get(Calendar.MONTH)+1) + "." + calToday.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();

        //test
//        tv_account_id = (TextView) rootView.findViewById(R.id.tv_account_id);
//        tv_account_id.setText(accountId);

        // 리스트뷰에 어댑터 연결
//        sellingAdapter = new SellingListAdapter();
//        lv_book_list_selling.setAdapter(sellingAdapter);

        sellingAdapter = new SellingListAdapter(sellingAdapterList, accountId);
        lv_book_list_selling.setAdapter(sellingAdapter);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sellingRetrofitInterface = retrofit.create(SellingRetrofitInterface.class);

        //테스트용
        //sellingAdapter.addItem("9791160506822", "wkd", "자바 웹을 다루는 기술", dateFormat.format(calToday.getTime()), "hhyy", "이병승", "길벗", "2019년 01월 07일");;
        //sellingAdapter.addItem("9791189571214", "tt123", "언제까지나 쇼팽", "2021년 10월 5일", "우주인", "나카야마 시치리", "블루홀식스", "2020년 04월 29일");


        if (is_list_all) {
            // 판매 완료 포함 표시
            tv_selling_list_is_all.setText("판매 완료 포함");
            sellingAdapterList.clear();

            // 리스트뷰 기존 아이템 리셋
            //sellingAdapter.resetItem();

            // 판매 완료 포함으로 설정된 경우 전체 판매 도서 목록 데이터 가져오기
            sellingRetrofitInterface.getAll().enqueue(new Callback<List<SellingData>>() {
                @Override
                public void onResponse(Call<List<SellingData>> call, Response<List<SellingData>> response) {
                    Log.d("server selling all", "success");
                    List<SellingData> sellingResultList = response.body();

                    Log.d("selling data count", sellingResultList.size() + " ");
                    // 받아온 데이터 리스트뷰에 띄우기
                    for (int i = 0; i < sellingResultList.size(); i++) {
                        Log.d("selling data", sellingResultList.get(i).getRegisterd_id() + " ");
                        //sellingAdapter.addItem(sellingResultList.get(i).getIsbn(), sellingResultList.get(i).getAccount_id(), sellingResultList.get(i).getProfile_color(), sellingResultList.get(i).getTitle(), sellingResultList.get(i).getDate_registerd(), sellingResultList.get(i).getNick_name(), sellingResultList.get(i).getAuthor(), sellingResultList.get(i).getPublisher(), sellingResultList.get(i).getDate_published(), sellingResultList.get(i).getRegisterd_id(), sellingResultList.get(i).getPrice_registerd(), sellingResultList.get(i).getIs_sold(), accountId);
                        sellingAllList.add(sellingResultList.get(i));
                    }
                    sellingAdapterList.addAll(sellingAllList);
                    //sellingAdapter = new SellingListAdapter(sellingAllList);
                    sellingAdapter.notifyDataSetChanged();
                    //sellingResult = new ArrayList<>();
//
//                    for (int i = 0; i < sellingResultList.size(); i++) {
//                        SellingData sellingResultItem = new SellingData();
//                        sellingResultItem.setIsbn(sellingResultList.get(i).getIsbn());
//                        sellingResultItem.setAccount_id(sellingResultList.get(i).getAccount_id());
//                        sellingResultItem.setProfile_color(sellingResultList.get(i).getProfile_color());
//                        sellingResultItem.setTitle(sellingResultList.get(i).getTitle());
//                        sellingResultItem.setDate_registerd(sellingResultList.get(i).getDate_registerd());
//                        sellingResultItem.setNick_name(sellingResultList.get(i).getNick_name());
//                        sellingResultItem.setAuthor(sellingResultList.get(i).getAuthor());
//                        sellingResultItem.setPublisher(sellingResultList.get(i).getPublisher());
//                        sellingResultItem.setDate_published(sellingResultList.get(i).getDate_published());
//                        sellingResultItem.setRegisterd_id(sellingResultList.get(i).getRegisterd_id());
//                        sellingResultItem.setPrice_registerd(sellingResultList.get(i).getPrice_registerd());
//                        sellingResultItem.setIs_sold(sellingResultList.get(i).getIs_sold());
//                        sellingResultItem.setUserNow(accountId);
//
//                        sellingResult.add(sellingResultItem);
//                    }
//
//                    // 리스트뷰에 어댑터 연결
//                    sellingAdapter = new SellingListAdapter(sellingResult);
//                    lv_book_list_selling.setAdapter(sellingAdapter);
                }

                @Override
                public void onFailure(Call<List<SellingData>> call, Throwable t) {
                    Log.d("server selling all", "fail");
                }
            });
        }

        // 판매 도서 포함 여부 변경 버튼 클릭시
        ibtn_change_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_list_all = !is_list_all;
                sellingAdapterList.clear();

                // 리스트뷰 기존 아이템 리셋
                //sellingAdapter.resetItem();

                if (is_list_all) {
                    // 판매 완료 포함 표시
                    tv_selling_list_is_all.setText("판매 완료 포함");
                    sellingAllList.clear();
                    sellingAdapterList.clear();

                    // 판매 완료 포함으로 설정된 경우 전체 판매 도서 목록 데이터 가져오기
                    sellingRetrofitInterface.getAll().enqueue(new Callback<List<SellingData>>() {
                        @Override
                        public void onResponse(Call<List<SellingData>> call, Response<List<SellingData>> response) {
                            Log.d("server selling all", "success");
                            List<SellingData> sellingResultList = response.body();

                            Log.d("selling data count", sellingResultList.size() + " ");
                            // 받아온 데이터 리스트뷰에 띄우기
                            for (int i = 0; i < sellingResultList.size(); i++) {
                                Log.d("selling data", sellingResultList.get(i).getRegisterd_id() + " ");
                                //sellingAdapter.addItem(sellingResultList.get(i).getIsbn(), sellingResultList.get(i).getAccount_id(), sellingResultList.get(i).getProfile_color(), sellingResultList.get(i).getTitle(), sellingResultList.get(i).getDate_registerd(), sellingResultList.get(i).getNick_name(), sellingResultList.get(i).getAuthor(), sellingResultList.get(i).getPublisher(), sellingResultList.get(i).getDate_published(), sellingResultList.get(i).getRegisterd_id(), sellingResultList.get(i).getPrice_registerd(), sellingResultList.get(i).getIs_sold(), accountId);
                                sellingAllList.add(sellingResultList.get(i));
                            }
                            sellingAdapterList.addAll(sellingAllList);
                            //sellingAdapter = new SellingListAdapter(sellingAllList);
                            sellingAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<List<SellingData>> call, Throwable t) {
                            Log.d("server selling all", "fail");
                        }
                    });
                }
                else {
                    // 판매 완료 미포함으로 설정된 경우 판매 완료 도서 제외한 목록 데이터 가져오기
                    tv_selling_list_is_all.setText("판매 완료 미포함");
                    sellingList.clear();
                    sellingAdapterList.clear();

                    sellingRetrofitInterface.getList().enqueue(new Callback<List<SellingData>>() {
                        @Override
                        public void onResponse(Call<List<SellingData>> call, Response<List<SellingData>> response) {
                            Log.d("server selling list", "success");
                            List<SellingData> sellingResultList = response.body();

                            Log.d("selling data count", sellingResultList.size() + " ");
                            for (int i = 0; i < sellingResultList.size(); i++) {
                                Log.d("selling data", sellingResultList.get(i).getRegisterd_id() + " ");
                                //sellingAdapter.addItem(sellingResultList.get(i).getIsbn(), sellingResultList.get(i).getAccount_id(), sellingResultList.get(i).getProfile_color(), sellingResultList.get(i).getTitle(), sellingResultList.get(i).getDate_registerd(), sellingResultList.get(i).getNick_name(), sellingResultList.get(i).getAuthor(), sellingResultList.get(i).getPublisher(), sellingResultList.get(i).getDate_published(), sellingResultList.get(i).getRegisterd_id(), sellingResultList.get(i).getPrice_registerd(), sellingResultList.get(i).getIs_sold(), accountId);
                                sellingList.add(sellingResultList.get(i));
                            }
                            sellingAdapterList.addAll(sellingList);
                            //sellingAdapter = new SellingListAdapter(sellingList);
                            sellingAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<List<SellingData>> call, Throwable t) {
                            Log.d("server selling list", "fail");
                        }
                    });
                }
            }
        });

//        sellingAdapter = new SellingListAdapter(sellingAdapterList);
//        lv_book_list_selling.setAdapter(sellingAdapter);

        // 제목 검색 edit text 변화 감지
        et_sell_regis_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = et_sell_regis_search.getText().toString();
                search(text);
            }
        });

        // 판매 등록 버튼 클릭시 판매 등록 화면으로 전환
        btn_selling_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellRegisSearch.class);
                intent.putExtra("account_id", accountId);
                startActivity(intent);
            }
        });

        return rootView;
    }

    // 검색 수행 메서드
    public void search(String text) {
        sellingAdapterList.clear();

        if (text.length() == 0) {
            if (is_list_all) {
                sellingAdapterList.addAll(sellingAllList);
            }
            else {
                sellingAdapterList.addAll(sellingList);
            }
        }
        else {
            if (is_list_all) {
                for (int i = 0; i < sellingAllList.size(); i++) {
                    if (sellingAllList.get(i).getTitle().toLowerCase().contains(text)) {
                        sellingAdapterList.add(sellingAllList.get(i));
                    }
                }
            }
            else {
                for (int i = 0; i < sellingList.size(); i++) {
                    if (sellingList.get(i).getTitle().toLowerCase().contains(text)) {
                        sellingAdapterList.add(sellingList.get(i));
                    }
                }
            }
        }

        sellingAdapter.notifyDataSetChanged();
    }
}
package org.techtown.archivebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPageFragment extends Fragment {

    View v_my_page_profile_color;
    TextView tv_my_page_user_name;
    TextView tv_my_page_nick_name;
    ImageButton ibtn_my_selling_list;
    ImageButton ibtn_my_sold_list;
    ImageButton ibtn_my_record_list;
    RecyclerView rv_book_recommend_list;
    Button btn_recommend_detail;
    Button btn_admin_book_insert;
    TextView tv_my_page_email;

    String accountId;

    String name;
    String profile_color;
    String nickName;

    RecommendListAdapter recommendListAdapter;

    Retrofit retrofit;
    UserRetrofitInterface userRetrofitInterface;
    RecommendRetrofitInterface recommendRetrofitInterface;

    ProfileColor profileColor = new ProfileColor();

    Context context;
    MainActivity activity;

    ArrayList<RecommendData> recommendList;

    String AdminId1 = "gUWD71n3AZUbprr9FpWiN3sMIKPSO4JvoTconsfOAkg";
    String AdminId2 = "k4hvpJbVfQ0qfiEeFfv-Xsym2_KMoKi8JnBcaEYtoUg";

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my_page, container, false);

        v_my_page_profile_color = (View) rootView.findViewById(R.id.v_my_page_profile_color);
        tv_my_page_user_name = (TextView) rootView.findViewById(R.id.tv_my_page_user_name);
        tv_my_page_nick_name = (TextView) rootView.findViewById(R.id.tv_my_page_nick_name);
        ibtn_my_selling_list = (ImageButton) rootView.findViewById(R.id.ibtn_my_selling_list);
        ibtn_my_sold_list = (ImageButton) rootView.findViewById(R.id.ibtn_my_sold_list);
        ibtn_my_record_list = (ImageButton) rootView.findViewById(R.id.ibtn_my_record_list);
        rv_book_recommend_list = (RecyclerView) rootView.findViewById(R.id.rv_book_recommend_list);
        btn_recommend_detail = (Button) rootView.findViewById(R.id.btn_recommend_detail);
        btn_admin_book_insert = (Button) rootView.findViewById(R.id.btn_admin_book_insert);
        tv_my_page_email = (TextView) rootView.findViewById(R.id.tv_my_page_email);

        context = getActivity();

        // Retrofit ?????? ??????
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userRetrofitInterface = retrofit.create(UserRetrofitInterface.class);
        recommendRetrofitInterface = retrofit.create(RecommendRetrofitInterface.class);

        // MainActivity????????? ????????? id ??? ??????
        if(getArguments() != null) {
            accountId = getArguments().getString("accountId");
        }
        // id?????? null??? ?????? ????????? ???????????? ??????
        else {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        Log.d("accountId-frag-mypage", accountId);

        // ?????? ????????? ???????????? admin ?????????
        if (accountId.equals(AdminId1) || accountId.equals(AdminId2)) {
            btn_admin_book_insert.setVisibility(View.VISIBLE);
        }

        // ??????????????? ????????? ????????? ????????????
        userRetrofitInterface.getDetail(accountId).enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                Log.d("server user detail", "success" + " " + response);

                // ????????? ????????? ????????? ?????????
                UserData userResult = response.body();
                tv_my_page_user_name.setText("(" + userResult.getName() + ")");
                tv_my_page_nick_name.setText(userResult.getNick_name());
                tv_my_page_email.setText(userResult.getEmail());
                name = userResult.getName();
                profile_color = userResult.getProfile_color();
                nickName = userResult.getNick_name();
                Log.d("user datas", userResult.getName() + " " + userResult.getNick_name() + " " + userResult.getProfile_color());
                v_my_page_profile_color.setBackground(profileColor.colorDrawable(activity, userResult.getProfile_color()));
                //v_my_page_profile_color.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.d("server user detail", "fail" + " " + t);
            }
        });

        // ???????????? ?????? ????????? ?????? ?????? ????????? ???????????? ??????
        ibtn_my_selling_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPut = new Intent(getActivity(), MySellingList.class);
                intentPut.putExtra("account_id", accountId);
                intentPut.putExtra("user_name", name);
                intentPut.putExtra("profile_color", profile_color);
                intentPut.putExtra("nick_name", nickName);
                Log.d("putName", name);
                Log.d("putColor", profile_color);
                startActivity(intentPut);
            }
        });

        // ???????????? ?????? ????????? ?????? ?????? ????????? ???????????? ??????
        ibtn_my_sold_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPut = new Intent(getActivity(), MySoldList.class);
                intentPut.putExtra("account_id", accountId);
                intentPut.putExtra("user_name", name);
                intentPut.putExtra("profile_color", profile_color);
                intentPut.putExtra("nick_name", nickName);
                Log.d("putName", name);
                Log.d("putColor", profile_color);
                startActivity(intentPut);
            }
        });

        // ???????????? ?????? ????????? ?????? ?????? ????????? ???????????? ??????
        ibtn_my_record_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPut = new Intent(getActivity(), MyRecordList.class);
                intentPut.putExtra("account_id", accountId);
                intentPut.putExtra("user_name", name);
                intentPut.putExtra("profile_color", profile_color);
                intentPut.putExtra("nick_name", nickName);
                Log.d("putName", name);
                Log.d("putColor", profile_color);
                startActivity(intentPut);
            }
        });

        // recyclerview??? ?????? ?????? ?????????
        recommendRetrofitInterface.getList(accountId).enqueue(new Callback<List<RecommendData>>() {
            @Override
            public void onResponse(Call<List<RecommendData>> call, Response<List<RecommendData>> response) {
                Log.d("server recomm list", "success" + " " + response);

                List<RecommendData> recommendResultList = response.body();
                recommendList = new ArrayList<>();

                // ?????? ?????? ?????? ????????? 10??? ????????? ?????? ?????? 10?????? recyclerview??? ?????????
                if (recommendResultList.size() > 10) {
                    for (int i = 0; i < 10; i++) {
                        recommendList.add(recommendResultList.get(i));
                    }
                }
                else {
                    for (int i = 0; i < recommendResultList.size(); i++) {
                        recommendList.add(recommendResultList.get(i));
                    }
                }

                // recyclerview??? adapter ??????
                recommendListAdapter = new RecommendListAdapter(recommendList);
                rv_book_recommend_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));
                rv_book_recommend_list.setAdapter(recommendListAdapter);
                rv_book_recommend_list.scrollToPosition(recommendList.size() - 1);
            }

            @Override
            public void onFailure(Call<List<RecommendData>> call, Throwable t) {
                Log.d("server recomm list", "fail" + " " + t);
            }
        });

        // ?????? ?????? ???????????? ????????? ?????? ?????? ?????? ?????? ???????????? ??????
        btn_recommend_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecommendDetailList.class);
                intent.putExtra("account_id", accountId);
                startActivity(intent);
            }
        });

        // admin page??? ?????? ??????
        btn_admin_book_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdmin = new Intent(getActivity(), AdminBookInsert.class);
                intentAdmin.putExtra("account_id", accountId);
                startActivity(intentAdmin);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
    }
}
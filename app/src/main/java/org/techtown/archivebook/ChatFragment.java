package org.techtown.archivebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChatFragment extends Fragment {

    ListView lv_chat_room_list;

    String accountId;

    boolean isChatSet = false;

    Retrofit retrofit;
    UserRetrofitInterface userRetrofitInterface;
    ChatRoomRetrofitInterface chatRoomRetrofitInterface;

    ChatRoomAdapter chatRoomAdapter;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);

        lv_chat_room_list = (ListView) rootView.findViewById(R.id.lv_chat_room_list);

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
        Log.d("accountId-frag-chat", accountId);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userRetrofitInterface = retrofit.create(UserRetrofitInterface.class);
        chatRoomRetrofitInterface = retrofit.create(ChatRoomRetrofitInterface.class);

//        // 채팅에 사용할 정보 저장 - 내 닉네임, 프로필 컬러
//        if (!isChatSet) {
//            userRetrofitInterface.getDetail(accountId).enqueue(new Callback<UserData>() {
//                @Override
//                public void onResponse(Call<UserData> call, Response<UserData> response) {
//                    Log.d("server user detail", "success" + " " + response);
//
//                    UserData userResult = response.body();
//
//                    ChatSet.nickName = userResult.getNick_name();
//                    ChatSet.profileColor = userResult.getProfile_color();
//                }
//
//                @Override
//                public void onFailure(Call<UserData> call, Throwable t) {
//                    Log.d("server user detail", "fail" + " " + t);
//                }
//            });
//        }

        // 리스트뷰에 어댑터 연결
        chatRoomAdapter = new ChatRoomAdapter();
        lv_chat_room_list.setAdapter(chatRoomAdapter);

        // 서버로부터 채팅방 데이터 가져오기
        chatRoomRetrofitInterface.getList(accountId).enqueue(new Callback<List<ChatRoomData>>() {
            @Override
            public void onResponse(Call<List<ChatRoomData>> call, Response<List<ChatRoomData>> response) {
                Log.d("server chat list", "success" + " " + response);

                List<ChatRoomData> chatRoomResult = response.body();

                for (int i = 0; i < chatRoomResult.size(); i++) {
                    // 사용자가 구매자인자 판매자인지 구분
                    if (chatRoomResult.get(i).getBuyer_id().equals(accountId)) {
                        chatRoomAdapter.addSetOther("buyer");
                    }
                    else if (chatRoomResult.get(i).getSeller_id().equals(accountId)) {
                        chatRoomAdapter.addSetOther("seller");
                    }

                    chatRoomAdapter.addChatItem(chatRoomResult.get(i).getChat_id(), chatRoomResult.get(i).getSeller_id(), chatRoomResult.get(i).getSeller_nick(), chatRoomResult.get(i).getSeller_profile(), chatRoomResult.get(i).getBuyer_id(), chatRoomResult.get(i).getBuyer_nick(), chatRoomResult.get(i).getBuyer_profile(), chatRoomResult.get(i).getRoom_name());
                }

            }

            @Override
            public void onFailure(Call<List<ChatRoomData>> call, Throwable t) {
                Log.d("server chat list", "fail" + " " + t);
            }
        });

        return rootView;
    }
}
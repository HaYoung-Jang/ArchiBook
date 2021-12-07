package org.techtown.archivebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatMessageList extends AppCompatActivity {

    TextView tv_other_nick_name;
    TextView tv_room_name_title;
    ListView lv_chat_message_list;
    EditText et_input_message;
    ImageButton ibtn_send_message;

    ArrayList<ChatData> chatList;
    ChatAdapter chatAdapter;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatRef;

    String accountId;
    String room_name;
    String other_nick;
    String my_nick;
    String my_profile;
    String chat_id;
    String other_profile;

    ChatRoomData chatRoomData;

    Retrofit retrofit;
    ChatRoomRetrofitInterface chatRoomRetrofitInterface;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message_list);

        tv_other_nick_name = (TextView) findViewById(R.id.tv_other_nick_name);
        tv_room_name_title = (TextView) findViewById(R.id.tv_room_name_title);
        lv_chat_message_list = (ListView) findViewById(R.id.lv_chat_message_list);
        et_input_message = (EditText) findViewById(R.id.et_input_message);
        ibtn_send_message = (ImageButton) findViewById(R.id.ibtn_send_message);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        chatRoomRetrofitInterface = retrofit.create(ChatRoomRetrofitInterface.class);

        // 사용자 id와 채팅방 이름 , 상대 닉네임 가져오기
        Intent intent = getIntent();
        accountId = intent.getStringExtra("account_id");
        other_nick = intent.getStringExtra("other_nick");
        my_nick = intent.getStringExtra("my_nick");
        my_profile = intent.getStringExtra("my_profile");
        room_name = intent.getStringExtra("room_name");
        chat_id = intent.getStringExtra("chat_id");
        other_profile = intent.getStringExtra("other_profile");

        Log.d("chat_id", chat_id + "");
        Log.d("my_nick", my_nick);

        tv_room_name_title.setText(room_name);
        tv_other_nick_name.setText(other_nick);

        //Firebase DB 관리 객체와 chat노드 참조객체 얻어오기
        firebaseDatabase = FirebaseDatabase.getInstance("https://archibook-8eca7-default-rtdb.asia-southeast1.firebasedatabase.app/");
        chatRef = firebaseDatabase.getReference();

        // Firebase DB 채팅 실시간 읽어오기
        chatRef.child("chat").child(chat_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                // 새로 추가된 데이터 가져오기
                ChatData chatData = snapshot.getValue(ChatData.class);

                // 새로운 데이터 리스트뷰에 추가
                chatList.add(chatData);

                // 리스트뷰 갱신
                chatAdapter.notifyDataSetChanged();
                lv_chat_message_list.setSelection(chatList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // 리스트뷰와 어댑터 연결
        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatList, my_nick, my_profile, other_profile);
        lv_chat_message_list.setAdapter(chatAdapter);

        // 채팅 보내기 버튼 클릭
        ibtn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // firebase DB에 저장할 값들
                String nickName = my_nick;
                String message = et_input_message.getText().toString();
                String profileColor = my_profile;

                // 현재 날짜, 시간
                Calendar calendar = Calendar.getInstance();
                String date = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.DAY_OF_MONTH);
                Log.d("Today's date : ", date);
                String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

                ChatData chatData = new ChatData();
                chatData.setNickName(nickName);
                chatData.setMessage(message);
                chatData.setProfileColor(profileColor);
                chatData.setDate(date);
                chatData.setTime(time);
                chatData.setChatRoomId(chat_id);

                chatRef.child("chat").child(chat_id).push().setValue(chatData);

                et_input_message.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
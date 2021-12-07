package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRegister extends AppCompatActivity {

    TextView tv_user_name;
    TextView tv_user_email;
    EditText et_user_input_nick_name;
    Button btn_check_available;
    TextView tv_is_nick_name_pass;
    RadioGroup rg_select_profile_color;
    RadioButton rb_blue;
    RadioButton rb_yellow;
    RadioButton rb_pink;
    RadioButton rb_green;
    RadioButton rb_orange;
    RadioButton rb_purple;
    Button btn_user_register;

    String accountId;
    String name;
    String email;

    String nickName;
    boolean isNickNamePass = false;

    String profileColor = "blue";

    Retrofit retrofit;
    UserRetrofitInterface userRetrofitInterface;

    //AVD
    //private final String SERVER_BASE_URL = "http://10.0.2.2:8080/";

    //Device
    //private final String SERVER_BASE_URL = "http://172.30.1.34:8080/";

    //Server
    private final String SERVER_BASE_URL = "http://13.125.236.123:8080/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_email = (TextView) findViewById(R.id.tv_user_email);
        et_user_input_nick_name = (EditText) findViewById(R.id.et_user_input_nick_name);
        btn_check_available = (Button) findViewById(R.id.btn_check_available);
        tv_is_nick_name_pass = (TextView) findViewById(R.id.tv_is_nick_name_pass);
        rg_select_profile_color = (RadioGroup) findViewById(R.id.rg_select_profile_color);
        rb_blue = (RadioButton) findViewById(R.id.rb_blue);
        rb_yellow = (RadioButton) findViewById(R.id.rb_yellow);
        rb_pink = (RadioButton) findViewById(R.id.rb_pink);
        rb_green = (RadioButton) findViewById(R.id.rb_green);
        rb_orange = (RadioButton) findViewById(R.id.rb_orange);
        rb_purple = (RadioButton) findViewById(R.id.rb_purple);
        btn_user_register = (Button) findViewById(R.id.btn_user_register);

        // 네이버 아이디 로그인을 통해 받아온 사용자 데이터 넘겨받기
        Intent intentGet = getIntent();
        accountId = intentGet.getStringExtra("account_id");
        name = intentGet.getStringExtra("name");
        email = intentGet.getStringExtra("email");

        // 받아온 사용자 이름, 이메일 띄우기
        tv_user_name.setText(name);
        tv_user_email.setText(email);

        // data intent 안됨 (null)
        Log.d("intent datas get", "accountId : " + " " + intentGet.getStringExtra("account_id") + " " + "name : " + name + " " + "email : " + email);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userRetrofitInterface = retrofit.create(UserRetrofitInterface.class);

        btn_check_available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 닉네임 입력 안했을 경우 닉네임 입력 알리기
                if (et_user_input_nick_name.getText().toString().equals("")) {
                    tv_is_nick_name_pass.setText("사용할 닉네임을 입력해주세요.");
                    tv_is_nick_name_pass.setTextColor(Color.parseColor("#000000"));
                    tv_is_nick_name_pass.setVisibility(View.VISIBLE);
                }
                else {
                    // 서버로부터 닉네임 중복 여부 조회
                    userRetrofitInterface.getNickname(et_user_input_nick_name.getText().toString()).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.d("server user nickname", "success");

                            // 입력 받은 닉네임 DB에 존재할 경우 중복 닉네임 알리기
                            if (response.body() == 1) {
                                tv_is_nick_name_pass.setText("중복 닉네임입니다.");
                                tv_is_nick_name_pass.setTextColor(Color.parseColor("#FF0000"));
                                tv_is_nick_name_pass.setVisibility(View.VISIBLE);
                            }
                            // 입력 받은 닉네임 DB에 존재하지 않을 경우 사용 가능 알리고 닉네임 저장, 닉네임 통과 여부 true
                            else if (response.body() == 0) {
                                tv_is_nick_name_pass.setText("사용 가능한 닉네임입니다.");
                                tv_is_nick_name_pass.setTextColor(Color.parseColor("#0000FF"));
                                tv_is_nick_name_pass.setVisibility(View.VISIBLE);

                                nickName = et_user_input_nick_name.getText().toString();
                                isNickNamePass = true;
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d("server user nickname", "fail" + " " + t);
                        }
                    });
                }
            }
        });

        // 프로필 색상 선택 라디오 버튼
        rg_select_profile_color.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_blue:
                        profileColor = "blue";
                        break;
                    case R.id.rb_yellow:
                        profileColor = "yellow";
                        break;
                    case R.id.rb_pink:
                        profileColor = "pink";
                        break;
                    case R.id.rb_green:
                        profileColor = "green";
                        break;
                    case R.id.rb_orange:
                        profileColor = "orange";
                        break;
                    case R.id.rb_purple:
                        profileColor = "purple";
                        break;
                }
            }
        });

        btn_user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //닉네임 통과 여부가 참인 경우
                if (isNickNamePass) {
                    // 서버에 사용자 등록
                    UserData userData = new UserData();
                    userData.setAccount_id(accountId);
                    userData.setName(name);
                    userData.setEmail(email);
                    userData.setNick_name(nickName);
                    userData.setProfile_color(profileColor);

                    userRetrofitInterface.postRegister(userData).enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {

                            if (response.code() == 200) {
                                Log.d("server user register", "success" + " " + response);

                                // 메인화면으로 전환
                                Intent intentPut = new Intent(getApplicationContext(), MainActivity.class);
                                intentPut.putExtra("account_id", accountId);
                                startActivity(intentPut);
                                finish();
                            }
                            else {
                                Log.d("user datas", userData.getAccount_id() + " " + userData.getEmail() + " " + userData.getName() + " " + userData.getNick_name() + " " + userData.getProfile_color());
                                Toast.makeText(UserRegister.this, "회원 등록 실패", Toast.LENGTH_SHORT).show();
                                Log.d("server user register", "fail" + " " + response);
                            }

                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d("server user register", "fail" + " " + t);
                        }
                    });
                }
                // 닉네임 통과 여부가 거짓인 경우
                else {
                    Toast.makeText(getApplicationContext(), "닉네임 입력 후 중복 확인이 필요합니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
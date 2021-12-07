package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private static OAuthLogin oAuthLoginModule;
    private static Context context;

    private static OAuthLoginButton btn_login_naver;
    private static TextView tv_logout_naver;

    static String naverUserId;
    static String naverUserName;
    static String naverUserEmail;

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
        setContentView(R.layout.activity_login);

        context = this;

        // 로그인 인스턴스 초기화
        oAuthLoginModule = OAuthLogin.getInstance();
        oAuthLoginModule.init(LoginActivity.this, getResources().getString(R.string.naver_client_id), getResources().getString(R.string.naver_client_secret), getResources().getString(R.string.naver_client_name));

        tv_logout_naver = (TextView) findViewById(R.id.tv_logout_naver);
        btn_login_naver = (OAuthLoginButton) findViewById(R.id.btn_login_naver);
        btn_login_naver.setOAuthLoginHandler(oAuthLoginHandler);

        // Retrofit 객체 생성
        retrofit2.Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userRetrofitInterface = retrofit.create(UserRetrofitInterface.class);

        btn_login_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naverLogin();

//                userRetrofitInterface.getDetail(naverUserId).enqueue(new Callback<UserData>() {
//                    @Override
//                    public void onResponse(Call<UserData> call, Response<UserData> response) {
//                        Log.d("server user detail", "success");
//
//                        UserData userResult = response.body();
//
//                        // 회원 조회 결과가 없으면 회원 등록 화면으로 전환
//                        if(userResult == null) {
//                            Intent intent = new Intent(getApplicationContext(), UserRegister.class);
//                            intent.putExtra("account_id", naverUserId);
//                            intent.putExtra("name", naverUserName);
//                            intent.putExtra("email", naverUserEmail);
//                            startActivity(intent);
//                            finish();
//                        }
//                        else {
//                            // 메인 화면으로 전환 (로그인)
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.putExtra("account_id", naverUserId);
//                            intent.putExtra("name", naverUserName);
//                            intent.putExtra("email", naverUserEmail);
//                            startActivity(intent);
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserData> call, Throwable t) {
//                        Log.d("server user detail", "fail");
//                    }
//                });
            }
        });

        // 로그아웃 텍스트 클릭시 네이버 로그아웃
        tv_logout_naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oAuthLoginModule.logout(context);
                Toast.makeText(LoginActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private OAuthLoginHandler oAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = oAuthLoginModule.getAccessToken(context);
                String refreshToken = oAuthLoginModule.getRefreshToken(context);
                long expiredAt = oAuthLoginModule.getExpiresAt(context);
                String tokenType = oAuthLoginModule.getTokenType(context);
                Toast.makeText(context, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                new RequestApiTask(context, oAuthLoginModule).execute();
            } else {
                String errorCode = oAuthLoginModule.getLastErrorCode(context).getCode();
                String errorDesc = oAuthLoginModule.getLastErrorDesc(context);
                Toast.makeText(context, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        };
    };

    private void naverLogin() {
        oAuthLoginModule.startOauthLoginActivity(LoginActivity.this, oAuthLoginHandler);
    }

    public static class RequestApiTask extends AsyncTask<Void, Void, String> {
        private final Context context;
        private final OAuthLogin oAuthLoginModule;
        public RequestApiTask(Context context, OAuthLogin oAuthLoginModule) {
            this.context = context;
            this.oAuthLoginModule = oAuthLoginModule;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... voids) {
            String url = "https://openapi.naver.com/v1/nid/me";
            String at = oAuthLoginModule.getAccessToken(context);

            return oAuthLoginModule.requestApi(context, at, url);
        }

        @Override
        protected void onPostExecute(String content) {
            try {
                JSONObject loginResult = new JSONObject(content);
                if (loginResult.getString("resultcode").equals("00")) {
                    JSONObject response = loginResult.getJSONObject("response");
                    naverUserId = response.getString("id");
                    naverUserName = response.getString("name");
                    naverUserEmail = response.getString("email");
                    //Toast.makeText(context, "id : " + naverUserId + "name : " + naverUserName + "email : " + naverUserEmail, Toast.LENGTH_SHORT).show();

                    UserRetrofitInterface userRetrofitInterface;
                    // Retrofit 객체 생성
                    retrofit2.Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://13.125.236.123:8080/")
                            .addConverterFactory(new NullOnEmptyConverterFactory())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    userRetrofitInterface = retrofit.create(UserRetrofitInterface.class);
                    userRetrofitInterface.getDetail(naverUserId).enqueue(new Callback<UserData>() {
                        @Override
                        public void onResponse(Call<UserData> call, Response<UserData> response) {
                            Log.d("server user detail", "success");

                            UserData userResult = response.body();

                            // 회원 조회 결과가 없으면 회원 등록 화면으로 전환
                            if(userResult == null) {
                                Intent intent = new Intent(context, UserRegister.class);
                                intent.putExtra("account_id", naverUserId);
                                intent.putExtra("name", naverUserName);
                                intent.putExtra("email", naverUserEmail);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                                //finish();
                            }
                            else {
                                // 메인 화면으로 전환 (로그인)
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.putExtra("account_id", naverUserId);
                                intent.putExtra("name", naverUserName);
                                intent.putExtra("email", naverUserEmail);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                context.startActivity(intent);
                                //finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserData> call, Throwable t) {
                            Log.d("server user detail", "fail" + " " + t);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
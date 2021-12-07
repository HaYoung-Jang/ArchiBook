package org.techtown.archivebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation_view;

    String accountId;

    long backBtnTime = 0;
    long currTime, gapTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_navigation_view = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);

        // 사용자 id 받아오기
        Intent intent = getIntent();
        accountId = intent.getStringExtra("account_id");

        Log.d("accountId - Main", accountId);
        
        // 데이터 전달할 fragment 객체 생성
        MainFragment mainFragment = new MainFragment();
        ChatFragment chatFragment = new ChatFragment();
        KeywordFragment keywordFragment = new KeywordFragment();
        RecordFragment recordFragment = new RecordFragment();
        MyPageFragment myPageFragment = new MyPageFragment();

        Bundle bundle = new Bundle();
        bundle.putString("accountId", accountId);

        mainFragment.setArguments(bundle);
        chatFragment.setArguments(bundle);
        keywordFragment.setArguments(bundle);
        recordFragment.setArguments(bundle);
        myPageFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.frame_main, mainFragment).commit();

        // 판매 상세의 채팅으로 거래하기 버튼 클릭시
        if (intent.getStringExtra("frag") != null) {
            if (intent.getStringExtra("frag").equals("chat")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, chatFragment).commit();
            }
        }

        // bottom navigation bar와 각 fragment 연결
        bottom_navigation_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_main:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, mainFragment).commit();
                        break;
                    case R.id.item_keyword:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, keywordFragment).commit();
                        break;
                    case R.id.item_chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, chatFragment).commit();
                        break;
                    case R.id.item_record:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, recordFragment).commit();
                        break;
                    case R.id.item_my_page:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_main, myPageFragment).commit();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 두번 눌러 종료
        currTime = System.currentTimeMillis();
        gapTime = currTime - backBtnTime;

        if ((gapTime >= 0) && (gapTime <= 2000)){
            super.onBackPressed();
        }
        else{
            backBtnTime = currTime;
            Toast.makeText(MainActivity.this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
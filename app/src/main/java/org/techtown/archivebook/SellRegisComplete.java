package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SellRegisComplete extends AppCompatActivity {

    Button btn_sell_regis_main;

    MainFragment fragment_main;

    String accountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_regis_complete);

        btn_sell_regis_main = (Button) findViewById(R.id.btn_sell_regis_main);

        Intent intentGet = getIntent();
        accountId = intentGet.getStringExtra("account_id");

        Log.d("accountId-sell-complete", accountId);

        fragment_main = new MainFragment();

        // 메인페이지 이동 버튼 클릭시 메인 화면으로 전환
        btn_sell_regis_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("account_id", accountId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }

    // 뒤로가기 방지
    @Override
    public void onBackPressed() {

    }
}
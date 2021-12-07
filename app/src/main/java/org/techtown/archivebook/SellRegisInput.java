package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SellRegisInput extends AppCompatActivity {

    Button btn_sell_regis_search;
    Button btn_sell_regis_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_regis_input);

        btn_sell_regis_search = (Button) findViewById(R.id.btn_sell_regis_search);
        btn_sell_regis_next = (Button) findViewById(R.id.btn_sell_regis_next);

        // 검색 버튼 클릭시 검색 화면으로 전환
        btn_sell_regis_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellRegisSearch.class);
                startActivity(intent);
                finish();
            }
        });

        // 다음 버튼 클릭시 도서 상태 정보 입력 화면으로 전환
        btn_sell_regis_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellRegisDescribe.class);
                startActivity(intent);
            }
        });
    }
}
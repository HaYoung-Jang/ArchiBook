package org.techtown.archivebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RecordComplete extends AppCompatActivity {

    Button btn_record_complete_to_record_main;

    String account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_complete);

        btn_record_complete_to_record_main = (Button) findViewById(R.id.btn_record_complete_to_record_main);

        Intent intentGet = getIntent();
        account_id = intentGet.getStringExtra("account_id");

        Log.d("accountId-record-compl", account_id);

        btn_record_complete_to_record_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("account_id", account_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    // 뒤로가기 방지
    @Override
    public void onBackPressed() {

    }
}

package org.techtown.archivebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SellingDescribeDialog extends AppCompatActivity {

    TextView tv_book_selling_describe;
    Button btn_book_describe_cancel;

    String bookDescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_describe_dialog);

        tv_book_selling_describe = (TextView) findViewById(R.id.tv_book_selling_describe);
        btn_book_describe_cancel = (Button) findViewById(R.id.btn_book_describe_cancel);

        // 판매 등록된 도서 상태 설명 받아오기
        Intent intent = getIntent();
        bookDescribe = intent.getStringExtra("bookDescribe");

        tv_book_selling_describe.setText(bookDescribe);

        // 닫기 버튼 클릭시 상태 설명 화면 닫기
        btn_book_describe_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
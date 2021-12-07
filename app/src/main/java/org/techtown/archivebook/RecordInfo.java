package org.techtown.archivebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RecordInfo extends AppCompatActivity {

    TextView tv_record_input_date_started;
    TextView tv_record_input_date_finished;
    ImageButton ibtn_date_started;
    ImageButton ibtn_date_finished;
    Button btn_record_info_to_describe;

    Spinner sps_year, sps_month, sps_day, spf_year, spf_month, spf_day;

    String account_id;

    ArrayList<Integer> listYear = new ArrayList<>();
    ArrayList <Integer> listMonth = new ArrayList<>();
    ArrayList <Integer> listDay = new ArrayList<>();

    int year_s, month_s, day_s;
    int year_f, month_f, day_f;

    String date_started;
    String date_finished;

    Calendar calToday = Calendar.getInstance();
    // 날짜 형식 지정
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_info);

        btn_record_info_to_describe = (Button) findViewById(R.id.btn_record_info_to_describe);
        sps_year = (Spinner) findViewById(R.id.sps_year);
        sps_month = (Spinner) findViewById(R.id.sps_month);
        sps_day = (Spinner) findViewById(R.id.sps_day);
        spf_year = (Spinner) findViewById(R.id.spf_year);
        spf_month = (Spinner) findViewById(R.id.spf_month);
        spf_day = (Spinner) findViewById(R.id.spf_day);


        Intent intentGet = getIntent();
        account_id = intentGet.getStringExtra("account_id");

        Log.d("accountId-record_info", account_id);

        // datepicker 통해 날짜 입력받기
//        ibtn_date_started.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dateSetListener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        tv_record_input_date_started.setText(year + "" + month + "" + dayOfMonth);
//                    }
//                };
//            }
//        });
//
//        ibtn_date_finished.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dateSetListener = new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        tv_record_input_date_finished.setText(year + "" + month + "" + dayOfMonth);
//                    }
//                };
//            }
//        });

        // 각 arrayList에 년도, 월, 일 set
        listYear = SetDate.setSpinnerItemYear(listYear, calToday.get(Calendar.YEAR));
        listMonth = SetDate.setSpinnerItemMonth(listMonth);
        listDay = SetDate.setSpinnerItemDay(listDay);

        // spinner, arrayList adapter 연결
        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, listYear);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sps_year.setAdapter(adapterYear);
        spf_year.setAdapter(adapterYear);

        ArrayAdapter<Integer> adapterMonth = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, listMonth);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sps_month.setAdapter(adapterMonth);
        spf_month.setAdapter(adapterMonth);

        ArrayAdapter<Integer> adapterDay = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, listDay);
        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sps_day.setAdapter(adapterDay);
        spf_day.setAdapter(adapterDay);

        // 각 스피너 선택 값 set
        sps_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year_s = listYear.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year_s = listYear.get(calToday.get(Calendar.YEAR));
            }
        });

        sps_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month_s = listMonth.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                month_s = listMonth.get(calToday.get(Calendar.MONTH)+1);
            }
        });

        sps_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day_s = listDay.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                day_s = listDay.get(calToday.get(Calendar.DAY_OF_MONTH));
            }
        });

        spf_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year_f = listYear.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                year_f = listYear.get(calToday.get(Calendar.YEAR));
            }
        });

        spf_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                month_f = listMonth.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                month_f = listMonth.get(calToday.get(Calendar.MONTH));
            }
        });

        spf_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                day_f = listDay.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                day_f = listDay.get(calToday.get(Calendar.DAY_OF_MONTH));
            }
        });

        // 다음 버튼 클릭시 독서기록 상세 화면으로 전환
        btn_record_info_to_describe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 날짜 유효성 체크
                Log.d("date validation s, f", CheckDate.checkDateVali(year_s, month_s, day_s) + " " + CheckDate.checkDateVali(year_f, month_f, day_f));
                if (!CheckDate.checkDateVali(year_s, month_s, day_s) || !CheckDate.checkDateVali(year_f, month_f, day_f)) {
                    Toast.makeText(RecordInfo.this, "날짜를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                // 시작일 > 종료일 체크
                else if (!CheckDate.checkDateOrder(year_s, month_s, day_s, year_f, month_f, day_f)){
                    Toast.makeText(RecordInfo.this, "시작일과 종료일 날짜를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                // 시작일, 종료일 > 오늘 날짜 체크
                else if (!CheckDate.checkDateOrder(year_s, month_s, day_s, calToday.get(Calendar.YEAR), calToday.get(Calendar.MONTH)+1, calToday.get(Calendar.DAY_OF_MONTH)) || !CheckDate.checkDateOrder(year_f, month_f, day_f, calToday.get(Calendar.YEAR), calToday.get(Calendar.MONTH)+1, calToday.get(Calendar.DAY_OF_MONTH))) {
                    Log.d("checkDateOrder", CheckDate.checkDateOrder(year_s, month_s, day_s, calToday.get(Calendar.YEAR), calToday.get(Calendar.MONTH), calToday.get(Calendar.DAY_OF_MONTH)) + " " + CheckDate.checkDateOrder(year_f, month_f, day_f, calToday.get(Calendar.YEAR), calToday.get(Calendar.MONTH), calToday.get(Calendar.DAY_OF_MONTH)));
                    Log.d("today date", calToday.get(Calendar.YEAR) + " " + calToday.get(Calendar.MONTH) + " " + calToday.get(Calendar.DAY_OF_MONTH));
                    Toast.makeText(RecordInfo.this, "날짜를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // 날짜 형식 yyMMdd
                    date_started = String.format("%04d%02d%02d", year_s, month_s, day_s);
                    date_finished = String.format("%04d%02d%02d", year_f, month_f, day_f);

                    Intent intentPut = new Intent(getApplicationContext(), RecordDescribe.class);
                    intentPut.putExtra("account_id", account_id);
                    intentPut.putExtra("title", intentGet.getStringExtra("title"));
                    intentPut.putExtra("isbn", intentGet.getStringExtra("isbn"));
                    intentPut.putExtra("author", intentGet.getStringExtra("author"));
                    intentPut.putExtra("publisher", intentGet.getStringExtra("publisher"));
                    intentPut.putExtra("date_published", intentGet.getStringExtra("date_published"));
                    intentPut.putExtra("price_origin", intentGet.getIntExtra("price_origin", 0));
                    intentPut.putExtra("image_url", intentGet.getStringExtra("image_url"));
                    intentPut.putExtra("link", intentGet.getStringExtra("link"));
                    intentPut.putExtra("date_started", date_started);
                    intentPut.putExtra("date_finished", date_finished);

                    startActivity(intentPut);
                }
            }
        });

    }

    // 뒤로가기 클릭시
    @Override
    public void onBackPressed() {
        // 판매 취소 여부 묻는 dialog 띄우기
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordInfo.this)
                .setMessage("판매 등록을 취소하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 아니오 선택시 화면 전환 없음
                    }
                })
                .setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 예 선택시 accountId 값 전달하면서 뒤로가기
                        Intent intentBack = new Intent(RecordInfo.this, MainActivity.class);
                        intentBack.putExtra("account_id", account_id);
                        Log.d("intentBack - accountId", account_id);
                        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentBack);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

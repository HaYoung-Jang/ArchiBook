package org.techtown.archivebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SellFragment extends Fragment {

    Button btn_sell_regis_barcode;
    Button btn_sell_regis_search;
    Button btn_sell_regis_input;

    String accountId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sell, container, false);

        btn_sell_regis_barcode = (Button) rootView.findViewById(R.id.btn_sell_regis_barcode);
        btn_sell_regis_search = (Button) rootView.findViewById(R.id.btn_sell_regis_search);
        btn_sell_regis_input = (Button) rootView.findViewById(R.id.btn_sell_regis_input);

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
        Log.d("accountId-frag-sell", accountId);

        btn_sell_regis_barcode.setClickable(false);

        // 바코드 스캔 버튼 클릭시 바코드 스캔 화면으로 전환
        btn_sell_regis_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellRegisBarcode.class);
                startActivity(intent);
            }
        });

        // ISBN 또는 책 제목으로 검색 버튼 클릭시 검색 화면으로 전환
        btn_sell_regis_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellRegisSearch.class);
                intent.putExtra("account_id", accountId);
                startActivity(intent);
            }
        });

        btn_sell_regis_input.setVisibility(View.GONE);

        // 수동으로 정보 입력 버튼 클릭시 입력 화면으로 전환
        btn_sell_regis_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SellRegisInput.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
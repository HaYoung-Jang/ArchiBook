package org.techtown.archivebook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MySoldListAdapter extends BaseAdapter {

    private ArrayList<SoldData> soldList = new ArrayList<>();
    //private ArrayList<String> titleList = new ArrayList<>();

    MySoldListAdapter() {

    }


    @Override
    public int getCount() {
        return soldList.size();
    }

    @Override
    public Object getItem(int position) {
        return soldList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        // profile_color uri
        ProfileColor profile_color = new ProfileColor();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_my_sold, parent, false);
        }

        TextView tv_my_sold_nick_name = (TextView) convertView.findViewById(R.id.tv_my_sold_nick_name);
        TextView tv_my_sold_book_title = (TextView) convertView.findViewById(R.id.tv_my_sold_book_title);
        LinearLayout ll_my_sold_list = (LinearLayout) convertView.findViewById(R.id.ll_my_sold_list);
        View v_profile_color = (View) convertView.findViewById(R.id.v_profile_color);

        // 리스트뷰에 각 데이터 띄우기
        SoldData soldItem = soldList.get(position);
        tv_my_sold_nick_name.setText(soldItem.getNick_name());
        tv_my_sold_book_title.setText(soldItem.getTitle() );
        v_profile_color.setBackground(profile_color.colorDrawable(context, soldItem.getProfile_color()));

        // 리스트뷰 아이템 클릭시 판매 상세 화면으로 전환
        ll_my_sold_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SellingDetail.class);
                intent.putExtra("registerdId", soldList.get(position).getRegisterd_id());
                intent.putExtra("account_id", soldList.get(position).getUserNow());
                intent.putExtra("nick_name", soldItem.getNick_name());
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    // 리스트뷰 아이템 생성 메소드
    public void addItem(String isbn, String profile_color, String nickName, String title, int registerdId, String userNow) {
        SoldData soldItem = new SoldData();

        soldItem.setIsbn(isbn);
        soldItem.setProfile_color(profile_color);
        soldItem.setNick_name(nickName);
        soldItem.setTitle(title);
        soldItem.setRegisterd_id(registerdId);
        soldItem.setUserNow(userNow);

        soldList.add(soldItem);

        notifyDataSetChanged();
    }

//    public void addTitle(String title) {
//        titleList.add(title);
//        Log.d("title added", title);
//    }
    public void resetItem() {
        soldList.clear();
    }
}

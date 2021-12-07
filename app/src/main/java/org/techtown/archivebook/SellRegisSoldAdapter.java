package org.techtown.archivebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SellRegisSoldAdapter extends BaseAdapter {

    ArrayList<ChatRoomData> chatRoomList = new ArrayList<>();

    SellRegisSoldAdapter() {

    }


    @Override
    public int getCount() {
        return chatRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_regis_sold_list, parent, false);
        }

        TextView tv_regis_sold_nick_name = (TextView) convertView.findViewById(R.id.tv_regis_sold_nick_name);
        LinearLayout ll_regis_sold_list = (LinearLayout) convertView.findViewById(R.id.ll_regis_sold_list);

        // 채팅 상대 닉네임 띄우기
        tv_regis_sold_nick_name.setText(chatRoomList.get(position).getBuyer_nick());

        // 구매자 선택하면 구매자 데이터 가지고 판매 상세 화면으로 전환
        ll_regis_sold_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SellingDetail.class);
                intent.putExtra("isbn", chatRoomList.get(position).getIsbn());
                intent.putExtra("buyer_id", chatRoomList.get(position).getBuyer_id());
                intent.putExtra("registerdId", chatRoomList.get(position).getRegisterd_id());
                intent.putExtra("account_id", chatRoomList.get(position).getUserNow());
                intent.putExtra("select", "yes");
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });

        return convertView;
    }

    public void addItem(String isbn, String buyerId, String buyerNickName, int registerdId, String userNow) {
        ChatRoomData chatRoomItem = new ChatRoomData();

        chatRoomItem.setIsbn(isbn);
        chatRoomItem.setBuyer_id(buyerId);
        chatRoomItem.setBuyer_nick(buyerNickName);
        chatRoomItem.setRegisterd_id(registerdId);
        chatRoomItem.setUserNow(userNow);

        chatRoomList.add(chatRoomItem);

        notifyDataSetChanged();
    }
}

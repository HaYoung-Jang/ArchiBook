package org.techtown.archivebook;

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

public class MySellingListAdapter extends BaseAdapter {

    private ArrayList<SellingData> sellingList = new ArrayList<>();

    MySellingListAdapter() {

    }


    @Override
    public int getCount() {
        return sellingList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellingList.get(position);
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
            convertView = inflater.inflate(R.layout.list_item_my_selling, parent, false);
        }

        TextView tv_my_selling_nick_name = (TextView) convertView.findViewById(R.id.tv_my_selling_nick_name);
        TextView tv_my_selling_date = (TextView) convertView.findViewById(R.id.tv_my_selling_date);
        TextView tv_my_selling_book_title = (TextView) convertView.findViewById(R.id.tv_my_selling_book_title);
        LinearLayout ll_my_selling_list = (LinearLayout) convertView.findViewById(R.id.ll_my_selling_list);
        View v_profile_color = (View) convertView.findViewById(R.id.v_profile_color);

        // 리스트뷰에 각 데이터 띄우기
        SellingData sellingItem = sellingList.get(position);
        tv_my_selling_nick_name.setText(sellingItem.getNick_name());
        tv_my_selling_date.setText(sellingItem.getDate_registerd());
        tv_my_selling_book_title.setText(sellingItem.getTitle());
        v_profile_color.setBackground(profile_color.colorDrawable(context, sellingItem.getProfile_color()));

        // 리스트뷰 아이템 클릭시 기록 상세 화면으로 전환
        ll_my_selling_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SellingDetail.class);
                intent.putExtra("registerdId", sellingList.get(position).getRegisterd_id());
                intent.putExtra("account_id", sellingList.get(position).getUserNow());
                intent.putExtra("nick_name", sellingItem.getNick_name());
                intent.putExtra("back_page", "selling");
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    // 리스트뷰 아이템 생성 메소드
    public void addItem(String isbn, String account_id, String profile_color, String title, String dateRegisterd, String nickName, String author, String publisher, String datePublished, int registerdId, int priceRegisterd, String userNow) {
        SellingData sellingItem = new SellingData();

        sellingItem.setIsbn(isbn);
        sellingItem.setAccount_id(account_id);
        sellingItem.setProfile_color(profile_color);
        sellingItem.setTitle(title);
        sellingItem.setDate_registerd(dateRegisterd);
        sellingItem.setNick_name(nickName);
        sellingItem.setAuthor(author);
        sellingItem.setPublisher(publisher);
        sellingItem.setDate_published(datePublished);
        sellingItem.setRegisterd_id(registerdId);
        sellingItem.setPrice_registerd(priceRegisterd);
        sellingItem.setUserNow(userNow);

        sellingList.add(sellingItem);

        notifyDataSetChanged();
    }

    public void resetItem() {
        sellingList.clear();
    }
}

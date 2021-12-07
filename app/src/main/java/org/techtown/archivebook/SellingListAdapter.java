package org.techtown.archivebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SellingListAdapter extends BaseAdapter {

    private ArrayList<SellingData> sellingList = new ArrayList<>();
    String account_id;

    SellingListAdapter() {
    }

    SellingListAdapter(ArrayList<SellingData> sellingList, String account_id) {
        this.sellingList = sellingList;
        this.account_id = account_id;
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
            convertView = inflater.inflate(R.layout.list_item_book_selling, parent, false);
        }

        TextView tv_book_selling_nick_name = (TextView) convertView.findViewById(R.id.tv_book_selling_nick_name);
        TextView tv_book_registerd_date = (TextView) convertView.findViewById(R.id.tv_book_registerd_date);
        TextView tv_book_is_sold = (TextView) convertView.findViewById(R.id.tv_book_is_sold);
        TextView tv_book_selling_title = (TextView) convertView.findViewById(R.id.tv_book_selling_title);
        LinearLayout ll_book_selling_list = (LinearLayout) convertView.findViewById(R.id.ll_book_selling_list);
        View v_profile_color = (View) convertView.findViewById(R.id.v_profile_color);

        // 리스트뷰에 각 데이터 띄우기
        SellingData bookItem = sellingList.get(position);
        tv_book_selling_nick_name.setText(bookItem.getNick_name());
        tv_book_registerd_date.setText(bookItem.getDate_registerd());
        Log.d("bookItem", bookItem.getRegisterd_id() + " 판매여부 : " + bookItem.getIs_sold());
        if (bookItem.getIs_sold()) {
            tv_book_is_sold.setText("판매완료");
            tv_book_is_sold.setTextColor(Color.parseColor("#FA457E"));
            tv_book_is_sold.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_box_red));
        }
        else {
            tv_book_is_sold.setText("판매중");
            tv_book_is_sold.setTextColor(Color.parseColor("#1482F8"));
            tv_book_is_sold.setBackground(ContextCompat.getDrawable(context, R.drawable.tv_box_blue));
        }
        tv_book_selling_title.setText(bookItem.getTitle());
        v_profile_color.setBackground(profile_color.colorDrawable(context, bookItem.getProfile_color()));

        // 리스트뷰 아이템 클릭시 판매 도서 상세 화면으로 전환
        ll_book_selling_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SellingDetail.class);
                intent.putExtra("registerdId", sellingList.get(position).getRegisterd_id());
                intent.putExtra("account_id", account_id);
                intent.putExtra("back_page", "main");
                v.getContext().startActivity(intent);
            }
        });


        return convertView;
    }

    // 리스트뷰 아이템 생성 메소드
    public void addItem(String isbn, String account_id, String profile_color, String title, String dateRegisterd, String nickName, String author, String publisher, String datePublished, int registerdId, int priceRegisterd, boolean isSold, String userNow) {
        SellingData bookItem = new SellingData();

        bookItem.setIsbn(isbn);
        bookItem.setAccount_id(account_id);
        bookItem.setProfile_color(profile_color);
        bookItem.setTitle(title);
        bookItem.setDate_registerd(dateRegisterd);
        bookItem.setNick_name(nickName);
        bookItem.setAuthor(author);
        bookItem.setPublisher(publisher);
        bookItem.setDate_published(datePublished);
        bookItem.setRegisterd_id(registerdId);
        bookItem.setPrice_registerd(priceRegisterd);
        bookItem.setIs_sold(isSold);
        bookItem.setUserNow(userNow);

        sellingList.add(bookItem);

        Log.d("selling add item", bookItem.getRegisterd_id() + " ");

        notifyDataSetChanged();
    }

    public void deleteItem(int pos) {
        sellingList.remove(pos);

        notifyDataSetChanged();
    }

    public void resetItem() {
        sellingList.clear();
    }
}

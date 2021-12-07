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

public class RecordListAdapter extends BaseAdapter {

    private ArrayList<RecordData> recordList = new ArrayList<>();

    RecordListAdapter() {

    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
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
            convertView = inflater.inflate(R.layout.list_item_book_record, parent, false);
        }

        TextView tv_record_nick_name = (TextView) convertView.findViewById(R.id.tv_record_nick_name);
        TextView tv_record_date = (TextView) convertView.findViewById(R.id.tv_record_date);
        TextView tv_record_book_title = (TextView) convertView.findViewById(R.id.tv_record_book_title);
        LinearLayout ll_record_list = (LinearLayout) convertView.findViewById(R.id.ll_record_list);
        View v_profile_color = (View) convertView.findViewById(R.id.v_profile_color);

        // 리스트뷰에 각 데이터 띄우기
        RecordData recordItem = recordList.get(position);
        tv_record_nick_name.setText(recordItem.getNick_name());
        tv_record_date.setText(recordItem.getDate_writing());
        tv_record_book_title.setText(recordItem.getTitle());
        v_profile_color.setBackground(profile_color.colorDrawable(context, recordItem.getProfile_color()));

        // 리스트뷰 아이템 클릭시 기록 상세 화면으로 전환
        ll_record_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RecordDetail.class);
                intent.putExtra("account_id", recordItem.getUserNow());
                intent.putExtra("record_id", recordItem.getRecord_id());
                intent.putExtra("back_page", "main");
                Log.d("record-list-usernow", recordItem.getUserNow());
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    // 리스트뷰 아이템 생성 메소드
    public void addItem(int recordId, String imageUrl, String profileColor, String accountId, String nickName, String title, boolean isPublic, String dateWriting, String author, String dateStarted, String dateFinished, int bookScore, String content, String userNow) {
        RecordData recordItem = new RecordData();

        recordItem.setRecord_id(recordId);
        recordItem.setImage_url(imageUrl);
        recordItem.setProfile_color(profileColor);
        recordItem.setAccount_id(accountId);
        recordItem.setNick_name(nickName);
        recordItem.setTitle(title);
        recordItem.setIs_public(isPublic);
        recordItem.setDate_writing(dateWriting);
        recordItem.setAuthor(author);
        recordItem.setDate_started(dateStarted);
        recordItem.setDate_finished(dateFinished);
        recordItem.setBook_score(bookScore);
        recordItem.setContent(content);
        recordItem.setUserNow(userNow);

        recordList.add(recordItem);

        notifyDataSetChanged();
    }

    public void resetItem() {
        recordList.clear();
    }
}

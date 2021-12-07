package org.techtown.archivebook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    private ArrayList<ChatData> chatList = new ArrayList<>();
    private String myNick;
    private String myProfile;
    private String otherProfile;

    ChatAdapter(ArrayList<ChatData> chatList, String myNick, String myProfile, String otherProfile) {
        this.chatList = chatList;
        this.myNick = myNick;
        this.myProfile = myProfile;
        this.otherProfile = otherProfile;
        Log.d("adpater set my nick", myNick);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
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

        ChatData chatItem = chatList.get(position);

        View itemView = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 메세지 보낸 사람 구분
        if (chatItem.getNickName().equals(myNick)) {
            itemView = inflater.inflate(R.layout.list_item_chat_message_my, parent, false);
            View v_profile_color = itemView.findViewById(R.id.v_profile_color);
            v_profile_color.setBackground(profile_color.colorDrawable(context, myProfile));
        }
        else {
            itemView = inflater.inflate(R.layout.list_item_chat_message_other, parent, false);
            View v_profile_color = itemView.findViewById(R.id.v_profile_color);
            v_profile_color.setBackground(profile_color.colorDrawable(context, otherProfile));
        }

        //View v_profile_color = itemView.findViewById(R.id.v_profile_color);
        TextView tv_nick_name = itemView.findViewById(R.id.tv_nick_name);
        TextView tv_message = itemView.findViewById(R.id.tv_message);
        TextView tv_date = itemView.findViewById(R.id.tv_date);
        TextView tv_time = itemView.findViewById(R.id.tv_time);

        tv_nick_name.setText(chatItem.getNickName());
        tv_message.setText(chatItem.getMessage());
        tv_date.setText(chatItem.getDate());
        tv_time.setText(chatItem.getTime());
        //v_profile_color.setBackground(profile_color.colorDrawable(context, chatItem.getProfileColor()));

        return itemView;
    }
}

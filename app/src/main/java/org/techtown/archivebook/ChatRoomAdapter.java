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

public class ChatRoomAdapter extends BaseAdapter {

    private ArrayList<ChatRoomData> chatRoomList = new ArrayList<>();
    private ArrayList<String> setOtherList = new ArrayList<>();

    String otherNickName;
    String myNickName;
    String myProfile;
    String otherProfile;

    ChatRoomAdapter() {

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

        // profile_color uri
        ProfileColor profile_color = new ProfileColor();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_chat_room, parent, false);
        }

        View v_profile_color = (View) convertView.findViewById(R.id.v_profile_color);
        TextView tv_other_nick_name = (TextView) convertView.findViewById(R.id.tv_other_nick_name);
        TextView tv_room_name_title = (TextView) convertView.findViewById(R.id.tv_room_name_title);
        LinearLayout ll_chat_room_list = (LinearLayout) convertView.findViewById(R.id.ll_chat_room_list);

        ChatRoomData chatRoomItem = chatRoomList.get(position);

        // 사용자가 구매자일 경우 판매자 닉네임, 프로필 상대 위치에 표시
        if (setOtherList.get(position).equals("buyer")) {
            v_profile_color.setBackground(profile_color.colorDrawable(context, chatRoomItem.getSeller_profile()));
            tv_other_nick_name.setText(chatRoomItem.getSeller_nick());
            Log.d("set", setOtherList.get(position));
            otherNickName = chatRoomItem.getSeller_nick();
            Log.d("other_nick", chatRoomItem.getSeller_nick());
            Log.d("chat_id", chatRoomItem.getChat_id() + "");
        }
        // 사용자가 판매자일 경우 구매자 닉네임, 프로필 상대 위치에 표시
        else if (setOtherList.get(position).equals("seller")){
            v_profile_color.setBackground(profile_color.colorDrawable(context, chatRoomItem.getBuyer_profile()));
            tv_other_nick_name.setText(chatRoomItem.getBuyer_nick());
            Log.d("set", setOtherList.get(position));
            otherNickName = chatRoomItem.getBuyer_nick();
            Log.d("other_nick", chatRoomItem.getBuyer_nick());
            Log.d("chat_id", chatRoomItem.getChat_id() + "");
        }

        tv_room_name_title.setText(chatRoomItem.getRoom_name());

        // 채팅방 리스트 클릭하면 채팅 화면으로 전환
        ll_chat_room_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setOtherList.get(position).equals("buyer")) {
                    otherNickName = chatRoomItem.getSeller_nick();
                    myNickName = chatRoomItem.getBuyer_nick();
                    myProfile = chatRoomItem.getBuyer_profile();
                    otherProfile = chatRoomItem.getSeller_profile();
                }
                else if (setOtherList.get(position).equals("seller")){
                    otherNickName = chatRoomItem.getBuyer_nick();
                    myNickName = chatRoomItem.getSeller_nick();
                    myProfile = chatRoomItem.getSeller_profile();
                    otherProfile = chatRoomItem.getBuyer_profile();
                }
                Intent intent = new Intent(context, ChatMessageList.class);
                intent.putExtra("chat_id", chatRoomItem.getChat_id());
                intent.putExtra("other_nick", otherNickName);
                intent.putExtra("my_nick", myNickName);
                intent.putExtra("room_name", chatRoomItem.getRoom_name());
                intent.putExtra("my_profile", myProfile);
                intent.putExtra("other_profile", otherProfile);

                Log.d("other nick", otherNickName);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void addChatItem(String chatId, String sellerId, String sellerNickName, String sellerProfileColor, String buyerId, String buyerNickName, String buyerProfileColor, String roomName) {
        ChatRoomData chatRoomItem = new ChatRoomData();

        chatRoomItem.setChat_id(chatId);
        chatRoomItem.setSeller_id(sellerId);
        chatRoomItem.setSeller_nick(sellerNickName);
        chatRoomItem.setSeller_profile(sellerProfileColor);
        chatRoomItem.setBuyer_id(buyerId);
        chatRoomItem.setBuyer_nick(buyerNickName);
        chatRoomItem.setBuyer_profile(buyerProfileColor);
        chatRoomItem.setRoom_name(roomName);

        chatRoomList.add(chatRoomItem);

        notifyDataSetChanged();
    }

    public void addSetOther(String set) {
        setOtherList.add(set);

        notifyDataSetChanged();
    }
}

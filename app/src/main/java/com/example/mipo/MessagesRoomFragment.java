package com.example.mipo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessagesRoomFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView list_view;
    static ArrayList<MessageRoomBean> messageRoomBeanArrayList;
    static MessagesRoomAdapter messagesRoomAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        View rootView = inflater.inflate (R.layout.activity_messages_room, container, false);
        list_view = (ListView) rootView.findViewById (R.id.listView);
        list_view.setTranscriptMode (1);
        messageRoomBeanArrayList = new ArrayList<MessageRoomBean> ();
        messagesRoomAdapter = new MessagesRoomAdapter (getActivity ().getApplicationContext (), messageRoomBeanArrayList);
        list_view.setAdapter(messagesRoomAdapter);
        list_view.setOnItemClickListener(this);
        loadMessages();

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent (getActivity ().getApplicationContext (), ChatActivity.class);
        Bundle b = new Bundle ();
        MessageItemHolder holder = (MessageItemHolder) view.getTag ();
        MessageRoomBean mrb = (MessageRoomBean) holder.name.getTag ();
        intent.putExtra ("userId", mrb.id);
        intent.putExtra("userName", mrb.name);
        b.putInt("index", mrb.getIndexInList());
        intent.putExtras (b);
        startActivity (intent);
    }

    public void loadMessages() {

        final ParseQuery<Room> query = ParseQuery.getQuery (Room.class);
        query.whereEqualTo("userNum1", GlobalVariables.CUSTOMER_PHONE_NUM);
        query.orderByDescending("updatedAt");
        List<Room> rooms = null;
        ArrayList<MessageRoomBean> mrbListNew = new ArrayList<MessageRoomBean> ();
        try {
            rooms = query.find ();
            final ParseQuery<Room> query2 = ParseQuery.getQuery (Room.class);
            query2.whereEqualTo ("userNum2", GlobalVariables.CUSTOMER_PHONE_NUM);
            query2.orderByDescending ("updatedAt");
            List<Room> rooms2 = query2.find ();
            rooms.addAll (rooms2);
            Collections.sort (rooms, new Comparator<Room> () {
                public int compare(Room o1, Room o2) {
                    return o2.getUpdatedAt ().compareTo (o1.getUpdatedAt ());
                }
            });
            for (int i = 0; i < rooms.size (); i++) {
                Room room = rooms.get(i);
                String otherPhoneNum = "";
                if (room.getUserNum1 ().equals (GlobalVariables.CUSTOMER_PHONE_NUM)) {
                    otherPhoneNum = room.getUserNum2 ();
                } else {
                    otherPhoneNum = room.getUserNum1 ();
                }
                UserDetails otherUser = StaticMethods.getUserFromPhoneNum(otherPhoneNum);
                if(GlobalVariables.userDataList.contains(otherUser))
                mrbListNew.add (new MessageRoomBean (otherUser.getName (),
                                                            room.getLastMessage (),
                                                            otherUser.getUserPhoneNum (),
                                                            otherUser.getPicUrl (),
                                                            otherUser.indexInAllDataList,true

                ));

            }
            messageRoomBeanArrayList.clear ();
            messageRoomBeanArrayList.addAll (mrbListNew);
            messagesRoomAdapter.notifyDataSetChanged ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalVariables.stopLoadingMessages = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        GlobalVariables.stopLoadingMessages = false;

    }
}
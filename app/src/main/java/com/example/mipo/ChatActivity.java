package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {

    private EditText etMessage;
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private boolean mFirstLoad;
    private Handler handler = new Handler ();
    static String otherUserId;
    String des;
    String des2;
    static String other_user_name;
    int combinedConversationId;
    int conversationId;
    int otherConversationId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.activity_chat);
        Intent intent = getIntent ();

        otherUserId = intent.getStringExtra ("userId");
        other_user_name = intent.getStringExtra ("userName");

        for (int i = 0; i < MainPageActivity.userDataList.size (); i++) {
            if (other_user_name.equals (MainPageActivity.userDataList.get (i).getName ())) {
                otherConversationId = MainPageActivity.userDataList.get (i).getMessage_roomId ();
            }
        }
        conversationId = MainPageActivity.currentUser.getMessage_roomId ();
        combinedConversationId = (otherConversationId * conversationId);

        etMessage = (EditText) findViewById (R.id.etMessage);
        lvChat = (ListView) findViewById (R.id.lvChat);

        mMessages = new ArrayList<Message> ();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode (1);
        des = MainPageActivity.currentUser.getId () + " - " + otherUserId;
        des2 = otherUserId + " - " + MainPageActivity.currentUser.getId ();

        mFirstLoad = true;
        mAdapter = new ChatListAdapter (ChatActivity.this, MainPageActivity.currentUser.getId (), mMessages);
        lvChat.setAdapter (mAdapter);
        receiveNoBackGround ();
        handler.postDelayed (runnable, 500);
    }

    public void sendMessage(View view) {
        String body = etMessage.getText ().toString ();
        Message message = new Message ();
        message.setUserId (MainPageActivity.currentUser.getId ());
        message.setBody (body);
        message.setCombinedID (Integer.toString (combinedConversationId));
        try {
            message.save ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        etMessage.setText ("");
        receiveNoBackGround ();
        deleteMessageRoomItem (body);
    }


    private void receiveMessage() {
        ParseQuery<Message> query = ParseQuery.getQuery (Message.class);
        query.whereEqualTo ("combinedID", Integer.toString (combinedConversationId));
        query.orderByAscending ("createdAt");
        query.findInBackground (new FindCallback<Message> () {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    if (messages.size () > mMessages.size ()) {
                        mMessages.clear ();
                        mMessages.addAll (messages);
                        mAdapter.notifyDataSetChanged (); // update adapter
                        // Scroll to the bottom of the list on initial load
                        if (mFirstLoad) {
                            lvChat.setSelection (mAdapter.getCount () - 1);
                            mFirstLoad = false;
                        }
                    }
                } else {
                    e.printStackTrace ();
                }
            }
        });
    }

    private void receiveNoBackGround() {
        ParseQuery<Message> query = ParseQuery.getQuery (Message.class);
        query.whereEqualTo ("combinedID", Integer.toString (combinedConversationId));
        query.orderByAscending ("createdAt");
        List<Message> messages = null;
        try {
            messages = query.find ();
            if (messages.size () > mMessages.size ()) {
                mMessages.clear ();
                mMessages.addAll (messages);
                mAdapter.notifyDataSetChanged (); // update adapter
                // Scroll to the bottom of the list on initial load
                if (mFirstLoad) {
                    lvChat.setSelection (mAdapter.getCount () - 1);
                    mFirstLoad = false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace ();
        }
    }

    private Runnable runnable = new Runnable () {
        @Override
        public void run() {
            receiveMessage ();
            handler.postDelayed (this, 500);
        }
    };

    public void deleteMessageRoomItem(final String body) {
        ParseQuery<Room> query = ParseQuery.getQuery (Room.class);
        query.whereEqualTo ("ConversationId", combinedConversationId);
        query.findInBackground (new FindCallback<Room> () {
            public void done(List<Room> list, ParseException e) {
                if (e == null) {
                    if (list.size () == 0) {
                        Room room = new Room ();
                        ParseACL parseACL = new ParseACL ();
                        parseACL.setPublicWriteAccess (true);
                        parseACL.setPublicReadAccess (true);
                        room.setACL (parseACL);
                        room.setUserId (MainPageActivity.currentUser.getId ());
                        room.setConversationId (combinedConversationId);
                        room.setDes (MainPageActivity.currentUser.getName () + ": " + body);
                        room.saveInBackground ();
                    } else {
                        if (list.size () > 0) {
                            for (int i = 1; i < list.size (); i++) {
                                list.get (i).deleteInBackground ();
                            }
                        }
                        Room obj = list.get (0);
                        obj.setUserId (MainPageActivity.currentUser.getId ());
                        obj.setConversationId (combinedConversationId);
                        obj.setDes (MainPageActivity.currentUser.getName () + ": " + body);
                        obj.saveInBackground ();
                    }
                } else {
                    e.printStackTrace ();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause ();
        handler.removeCallbacks (runnable);
    }

    @Override
    public void onRestart() {
        super.onRestart ();
        handler.postDelayed (runnable, 500);
    }
}
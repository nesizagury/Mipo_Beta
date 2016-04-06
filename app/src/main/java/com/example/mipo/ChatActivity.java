package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ChatActivity extends Activity {

    private EditText etMessage;
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private boolean mFirstLoad;
    private Handler handler = new Handler ();
    static String otherUserId;
    static UserDetails otherUser;
    static String other_user_name;
    String otherUser_index;
    String lastMessage;
    static ImageLoader imageLoader;
    DisplayImageOptions options;
    int pixels;
    ImageView image;
    TextView nameTV;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Map<String,?> keys;
    int firstUnread = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        image = (ImageView) findViewById(R.id.imageV);
        nameTV = (TextView) findViewById(R.id.nameTV);
        setImageLoader();
        GlobalVariables.pushAction = "";
        GlobalVariables.pushUserName = "";
        GlobalVariables.pushIndex = "";
        GlobalVariables.ChatWith = otherUserId = intent.getStringExtra("userId");
        other_user_name = intent.getStringExtra("userName");
        nameTV.setText(other_user_name);
        int otherUserIndex = intent.getIntExtra("index", 0);
        otherUser = GlobalVariables.userDataList.get(otherUserIndex);
        imageLoader.displayImage(otherUser.getPicUrl(),image);
        otherUser_index = String.valueOf(otherUserIndex);
        etMessage = (EditText) findViewById (R.id.etMessage);
        lvChat = (ListView) findViewById (R.id.lvChat);
        mMessages = new ArrayList <Message> ();
        lvChat.setTranscriptMode(1);
        prefs = this.getSharedPreferences(otherUserId, this.MODE_PRIVATE);
        ParsePush.subscribeInBackground("a" + otherUser.getUserPhoneNum() + GlobalVariables.CUSTOMER_PHONE_NUM);
        mFirstLoad = true;
        mAdapter = new ChatListAdapter (ChatActivity.this, otherUserIndex, mMessages);
        lvChat.setAdapter(mAdapter);

        keys = prefs.getAll();
        int i = 0;
        ArrayList <Message> list = new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            Gson gson1 = new Gson();
            String json1 = entry.getValue().toString();
            Message msg = gson1.fromJson(json1, Message.class);
            list.add(msg);
            if(!msg.getSeen() && firstUnread == -1)
            {
                firstUnread = i;
            }
            i++;
        }
        Collections.sort(list, new Comparator<Message>() {
            @Override
            public int compare(Message a, Message b) {
                if (a.getIndex() < b.getIndex()) return -1;
                if (a.getIndex() >= b.getIndex()) return 1;
                return 0;
            }
        });

        mMessages.addAll(list);


        if(firstUnread == -1)
            firstUnread = i - 1;



        handler.postDelayed(runnable, 500);
    }

    public void sendMessage(View view) {
        String body = etMessage.getText ().toString();
        Message message = new Message ();

        String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
        String otherPhone = otherUser.getUserPhoneNum ();
        if (currentPhone.compareTo (otherPhone) > 0) {
            message.setUserNum1 (currentPhone);
            message.setUserNum2 (otherPhone);
        } else {
            message.setUserNum1 (otherPhone);
            message.setUserNum2 (currentPhone);
        }
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
        String DateToStr = format.format(curDate);
        int messageKey = Integer.parseInt(DateToStr.replaceAll("\\D", ""));
        message.setIndex(messageKey);
        message.setSenderId(currentPhone);
        message.setMessageBody(body);
        message.setReceiverId(otherPhone);
        message.setSeen(false);
        message.setKey(DateFormat.getDateTimeInstance().format(new Date()));
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();

        try {
                message.save();
                ParsePush push = new ParsePush();
                push.setChannel("a" + GlobalVariables.CUSTOMER_PHONE_NUM + otherUser.getUserPhoneNum());
                JSONObject data = new JSONObject();
                try {
                    data.put("action",GlobalVariables.CUSTOMER_PHONE_NUM);
                    data.put("_alert",etMessage.getText ().toString());
                    data.put("messageObjectId",message.getObjectId());
                    data.put("userName",GlobalVariables.currentUser.getName());
                    data.put("index",GlobalVariables.currentUser.getIndexInAllDataList());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                push.setData(data);
                push.sendInBackground();
            } catch (ParseException e) {
                e.printStackTrace();
            }


       lastMessage = body;
        etMessage.setText ("");
        deleteMessageRoomItem(body,message);
    }


    private void receiveMessage() {

        ParseQuery<Message> query = ParseQuery.getQuery (Message.class);
        final String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
        final String otherPhone = otherUser.getUserPhoneNum ();
        if (currentPhone.compareTo (otherPhone) > 0) {
            query.whereEqualTo ("userNum1", currentPhone);
            query.whereEqualTo ("userNum2", otherPhone);
        } else {
            query.whereEqualTo ("userNum1", otherPhone);
            query.whereEqualTo ("userNum2", currentPhone);
        }

        if(mMessages.size() > 0)
        query.whereGreaterThanOrEqualTo("index",mMessages.get(firstUnread).getIndex());
        query.orderByAscending("createdAt");
        query.findInBackground (new FindCallback<Message> () {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                            for (int i = 0; i < messages.size(); i++) {
                                if(messages.get(i).getReceiverId().equals(currentPhone)
                                        &&  !messages.get(i).getSeen()){
                                    mMessages.add(messages.get(i));
                                    Gson gson = new Gson();
                                    String json = gson.toJson(messages.get(i));
                                    prefs.edit().putString(DateFormat.getDateTimeInstance()
                                            .format(new Date()), json).apply();
                                    messages.get(i).setSeen(true);
                                    messages.get(i).saveInBackground();
                                }
                                if(messages.get(i).getSenderId().equals(currentPhone) && messages.get(i).getSeen()){
                                    Gson gson = new Gson();
                                    String json = gson.toJson(messages.get(i));
                                    prefs.edit().putString(messages.get(i).getKey(), json).commit();
                                }
                            }

                        for (int i = 0; i < messages.size(); i++) {

                            mMessages.set((mMessages.size()-1) - i,messages.get((messages.size() -1) - i));
                          }

                    mAdapter.notifyDataSetChanged();
                        if (mFirstLoad) {
                            lvChat.setSelection (mAdapter.getCount () - 1);
                            mFirstLoad = false;
                        }

                } else {
                    e.printStackTrace ();
                }
            }
        });
    }

    private Runnable runnable = new Runnable () {
        @Override
        public void run() {
            receiveMessage ();
            handler.postDelayed (this, 500);
        }
    };

    public void deleteMessageRoomItem(final String body , final Message message) {
        ParseQuery<Room> query = ParseQuery.getQuery (Room.class);
        String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
        String otherPhone = otherUser.getUserPhoneNum ();
        if (currentPhone.compareTo (otherPhone) > 0) {
            query.whereEqualTo ("userNum1", currentPhone);
            query.whereEqualTo ("userNum2", otherPhone);
        } else {
            query.whereEqualTo ("userNum1", otherPhone);
            query.whereEqualTo ("userNum2", currentPhone);
        }
        query.findInBackground(new FindCallback<Room>() {
            public void done(List<Room> list, ParseException e) {
                if (e == null) {
                    String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
                    String otherPhone = otherUser.getUserPhoneNum();
                    if (list.size() == 0) {
                        Room room = new Room();
                        ParseACL parseACL = new ParseACL();
                        parseACL.setPublicWriteAccess(true);
                        parseACL.setPublicReadAccess(true);
                        room.setACL(parseACL);
                        if (currentPhone.compareTo(otherPhone) > 0) {
                            room.setUserNum1(currentPhone);
                            room.setUserNum2(otherPhone);
                        } else {
                            room.setUserNum1(otherPhone);
                            room.setUserNum2(currentPhone);
                        }
                        room.put("messagePointer",message);
                        room.setLastMessage(currentPhone + ": " + body);
                        room.saveInBackground();
                    } else {
                        if (list.size() > 0) {
                            for (int i = 1; i < list.size(); i++) {
                                list.get(i).deleteInBackground();
                            }
                        }
                        Room room = list.get(0);
                        if (currentPhone.compareTo(otherPhone) > 0) {
                            room.setUserNum1(currentPhone);
                            room.setUserNum2(otherPhone);
                        } else {
                            room.setUserNum1(otherPhone);
                            room.setUserNum2(currentPhone);
                        }
                        room.put("messagePointer",message);
                        room.setLastMessage(currentPhone + ": " + body);
                        room.saveInBackground();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        GlobalVariables.ChatWith = "";
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onRestart() {
        super.onRestart ();
        handler.postDelayed(runnable, 1500);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalVariables.ChatWith = otherUserId;
    }

    public void StopNotification(View view){
        ParsePush.unsubscribeInBackground("a" + otherUser.getUserPhoneNum() + GlobalVariables.CUSTOMER_PHONE_NUM);
    }

    public void setImageLoader() {

        float density = getResources ().getDisplayMetrics ().density;
        pixels = (int) (64 * density + 0.5f);

        options = new DisplayImageOptions.Builder ()
                .cacheOnDisk (true)
                .cacheInMemory (true)
                .bitmapConfig (Bitmap.Config.RGB_565)
                .imageScaleType (ImageScaleType.EXACTLY)
                .resetViewBeforeLoading (true)
                .build ();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder (this)
                .defaultDisplayImageOptions (options)
                .threadPriority (Thread.MAX_PRIORITY)
                .threadPoolSize (2)
                .memoryCache (new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory ()
                .build ();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }



}
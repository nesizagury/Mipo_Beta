package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {

    private int SELECT_PICTURE=1;
    private boolean image_selected=false;
    private Bitmap bmp;
    private ImageButton uploadPic;
    private ImageButton delete_image;
    private ImageView message_pic;
    private EditText etMessage;
    private ListView lvChat;
    private ArrayList<Message> mMessages;
    private ChatListAdapter mAdapter;
    private boolean mFirstLoad;
    private Handler handler = new Handler ();
    static String otherUserId;
    static UserDetails otherUser;
    static String other_user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.activity_chat);
        Intent intent = getIntent();

        otherUserId = intent.getStringExtra ("userId");
        other_user_name = intent.getStringExtra("userName");
        int otherUserIndex = intent.getIntExtra ("index", 0);
        otherUser = GlobalVariables.userDataList.get (otherUserIndex);

        etMessage = (EditText) findViewById (R.id.etMessage);
        lvChat = (ListView) findViewById (R.id.lvChat);
        message_pic=(ImageView)findViewById(R.id.message_pic);
        uploadPic=(ImageButton)findViewById(R.id.upload_pic);
        delete_image=(ImageButton)findViewById(R.id.delete_image);
        if(!image_selected){
            message_pic.setVisibility(View.GONE);
            delete_image.setVisibility(View.GONE);

        }

        mMessages = new ArrayList<Message> ();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        lvChat.setTranscriptMode (1);

        mFirstLoad = true;
        mAdapter = new ChatListAdapter (ChatActivity.this, otherUserIndex, mMessages);
        lvChat.setAdapter (mAdapter);
        receiveNoBackGround ();
        handler.postDelayed (runnable, 500);
    }

    public void sendMessage(View view) {
        if(etMessage.getText ().toString ().isEmpty() && image_selected==false){
            Toast.makeText(getApplicationContext(),R.string.type_or_upload,Toast.LENGTH_LONG).show();
            return;
        }


        String body = etMessage.getText ().toString ();
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
        message.setSenderId(currentPhone);
        message.setMessageBody(body);
        try {
            if (image_selected) {
                message_pic.buildDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream ();
                if(bmp.getByteCount () > 500000) {
                    bmp.compress (Bitmap.CompressFormat.JPEG, 100, stream);
                } else{
                    bmp.compress (Bitmap.CompressFormat.PNG, 100, stream);
                }
                byte[] image = stream.toByteArray ();
                ParseFile file = new ParseFile ("picturePath", image);
                try {
                    file.save ();
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                ParseACL parseAcl = new ParseACL ();
                parseAcl.setPublicReadAccess (true);
                parseAcl.setPublicWriteAccess (true);
                message.setACL (parseAcl);
                message.put ("pic", file);
            }else {
                message.put ("pic", null);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        mMessages.add(message);
        lvChat.invalidateViews();


        try {
            message.save ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }


        message_pic.setVisibility(View.GONE);
        delete_image.setVisibility(View.GONE);
        image_selected=false;
        message_pic.setImageDrawable(null);
        bmp=null;
        etMessage.setText ("");
        receiveMessage ();
        deleteMessageRoomItem (body);
    }


    private void receiveMessage() {
        ParseQuery<Message> query = ParseQuery.getQuery (Message.class);
        String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
        String otherPhone = otherUser.getUserPhoneNum ();
        if (currentPhone.compareTo (otherPhone) > 0) {
            query.whereEqualTo ("userNum1", currentPhone);
            query.whereEqualTo ("userNum2", otherPhone);
        } else {
            query.whereEqualTo ("userNum1", otherPhone);
            query.whereEqualTo ("userNum2", currentPhone);
        }
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
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
        String otherPhone = otherUser.getUserPhoneNum ();
        if (currentPhone.compareTo (otherPhone) > 0) {
            query.whereEqualTo ("userNum1", currentPhone);
            query.whereEqualTo ("userNum2", otherPhone);
        } else {
            query.whereEqualTo ("userNum1", otherPhone);
            query.whereEqualTo ("userNum2", currentPhone);
        }
        query.orderByAscending("createdAt");
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
        String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
        String otherPhone = otherUser.getUserPhoneNum ();
        if (currentPhone.compareTo (otherPhone) > 0) {
            query.whereEqualTo ("userNum1", currentPhone);
            query.whereEqualTo ("userNum2", otherPhone);
        } else {
            query.whereEqualTo ("userNum1", otherPhone);
            query.whereEqualTo ("userNum2", currentPhone);
        }
        query.findInBackground (new FindCallback<Room> () {
            public void done(List<Room> list, ParseException e) {
                if (e == null) {
                    String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
                    String otherPhone = otherUser.getUserPhoneNum ();
                    if (list.size () == 0) {
                        Room room = new Room ();
                        ParseACL parseACL = new ParseACL ();
                        parseACL.setPublicWriteAccess (true);
                        parseACL.setPublicReadAccess (true);
                        room.setACL (parseACL);
                        if (currentPhone.compareTo (otherPhone) > 0) {
                            room.setUserNum1 (currentPhone);
                            room.setUserNum2 (otherPhone);
                        } else {
                            room.setUserNum1 (otherPhone);
                            room.setUserNum2 (currentPhone);
                        }
                        room.setLastMessage (currentPhone + ": " + body);
                        room.saveInBackground ();
                    } else {
                        if (list.size () > 0) {
                            for (int i = 1; i < list.size (); i++) {
                                list.get (i).deleteInBackground ();
                            }
                        }
                        Room room = list.get (0);
                        if (currentPhone.compareTo (otherPhone) > 0) {
                            room.setUserNum1 (currentPhone);
                            room.setUserNum2 (otherPhone);
                        } else {
                            room.setUserNum1 (otherPhone);
                            room.setUserNum2 (currentPhone);
                        }
                        room.setLastMessage(currentPhone + ": " + body);
                        room.saveInBackground ();
                    }
                } else {
                    e.printStackTrace ();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        handler.postDelayed(runnable, 500);
    }

    public void imageUpload(View view) {
        Intent i = new Intent (
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            ParcelFileDescriptor parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = this.getContentResolver ().openFileDescriptor (selectedImage, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace ();
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor ();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            try {
                parcelFileDescriptor.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            Matrix matrix = new Matrix ();
            int angleToRotate = StaticMethods.getOrientation(selectedImage, this);
            matrix.postRotate (angleToRotate);
            Bitmap rotatedBitmap = Bitmap.createBitmap (image,0, 0, image.getWidth (), image.getHeight (), matrix, true);
            bmp = rotatedBitmap;
            message_pic.setImageBitmap(rotatedBitmap);
            image_selected = true;
            delete_image.setVisibility(View.VISIBLE);
            message_pic.setVisibility(View.VISIBLE);
        }
    }
    public void removePic(){
        message_pic.setImageDrawable(null);
        delete_image.setVisibility(View.INVISIBLE);
        message_pic.setVisibility(View.INVISIBLE);

    }
}
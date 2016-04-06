package com.example.mipo.chat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;

import com.example.mipo.GlobalVariables;
import com.example.mipo.R;
import com.example.mipo.Room;
import com.example.mipo.StaticMethods;
import com.example.mipo.UserDetails;
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

public class ChatActivity extends ActionBarActivity {

    private int SELECT_PICTURE = 1;
    private Bitmap bmp;
    private ListView listViewOfMessages;
    private ArrayList<Message> mMessages;
    private ArrayList<MessageWrapper> messageWrapperList;
    private MessageAdapter mAdapter;
    private Handler handler = new Handler ();
    static String otherUserId;
    static UserDetails otherUser;
    static String other_user_name;
    private MessageInputToolBox box;
    boolean didTouched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_chat_new);
        initMessageInputToolBox ();
        Intent intent = getIntent ();

        otherUserId = intent.getStringExtra ("userId");
        other_user_name = intent.getStringExtra ("userName");
        int otherUserIndex = intent.getIntExtra ("index", 0);
        otherUser = GlobalVariables.userDataList.get (otherUserIndex);

        listViewOfMessages = (ListView) findViewById (R.id.messageListview);

        mMessages = new ArrayList<Message> ();
        messageWrapperList = new ArrayList<MessageWrapper> ();
        // Automatically scroll to the bottom when a data set change
        // notification is received and only if the last item is already visible on screen.
        // Don't scroll to the bottom otherwise.
        listViewOfMessages.setOnTouchListener (new OnTouchListener () {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                box.hide ();
                didTouched = true;
                return false;
            }
        });

        mAdapter = new MessageAdapter (ChatActivity.this, messageWrapperList);
        listViewOfMessages.setAdapter (mAdapter);
        receiveNoBackGround ();
        handler.postDelayed (runnable, 500);
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
                        fromParseMessageToWrapperList (messages, messageWrapperList);
                        mAdapter.notifyDataSetChanged ();
                        mMessages.clear ();
                        mMessages.addAll (messages);
                        // Scroll to the bottom of the list on initial load
                        if(!didTouched) {
                            listViewOfMessages.setSelection (listViewOfMessages.getCount ());
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
        List<Message> messages = null;
        try {
            messages = query.find ();
            if (messages.size () > mMessages.size ()) {
                fromParseMessageToWrapperList (messages, messageWrapperList);
                mAdapter.notifyDataSetChanged ();
                mMessages.clear ();
                mMessages.addAll (messages);
                // Scroll to the bottom of the list on initial load
                listViewOfMessages.setSelection(listViewOfMessages.getCount());
            }
        } catch (ParseException e) {
            e.printStackTrace ();
        }
    }

    private Runnable runnable = new Runnable () {
        @Override
        public void run() {
            receiveMessage ();
            if(!didTouched) {
                listViewOfMessages.setSelection (listViewOfMessages.getCount ());
            }
            handler.postDelayed (this, 500);
        }
    };

    public void updateMessageRoomItem(final String body) {
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
                        room.setLastMessage (currentPhone + ": " + body);
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
        super.onPause ();
        handler.removeCallbacks (runnable);
    }

    @Override
    public void onRestart() {
        super.onRestart ();
        handler.postDelayed (runnable, 500);
    }

    public void imageUpload(View view) {
        Intent i = new Intent (
                                      Intent.ACTION_PICK,
                                      MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult (i, SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData ();
            ParcelFileDescriptor parcelFileDescriptor = null;
            try {
                parcelFileDescriptor = this.getContentResolver ().openFileDescriptor (selectedImage, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace ();
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor ();
            Bitmap image = BitmapFactory.decodeFileDescriptor (fileDescriptor);
            try {
                parcelFileDescriptor.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            Matrix matrix = new Matrix ();
            int angleToRotate = StaticMethods.getOrientation (selectedImage, this);
            matrix.postRotate (angleToRotate);
            Bitmap rotatedBitmap = Bitmap.createBitmap (image, 0, 0, image.getWidth (), image.getHeight (), matrix, true);
            bmp = rotatedBitmap;
            String body = "";
            final Message message = new Message ();
            String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
            String otherPhone = otherUser.getUserPhoneNum ();
            if (currentPhone.compareTo (otherPhone) > 0) {
                message.setUserNum1 (currentPhone);
                message.setUserNum2 (otherPhone);
            } else {
                message.setUserNum1 (otherPhone);
                message.setUserNum2 (currentPhone);
            }
            message.setSenderId (currentPhone);
            message.setMessageBody (body);
            (new TestAsync ()).execute (message, null, null);
        }
    }

    class TestAsync extends AsyncTask<Message, Integer, String> {
        protected void onPreExecute() {
        }

        protected String doInBackground(Message... arg0) {
            Message message = arg0[0];
            ByteArrayOutputStream stream = new ByteArrayOutputStream ();
            if (bmp.getByteCount () > 500000) {
                bmp.compress (Bitmap.CompressFormat.JPEG, 100, stream);
            } else {
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
            message.setMessageType (MessageWrapper.MSG_TYPE_PHOTO);
            message.saveInBackground ();
            bmp = null;
            return "You are at PostExecute";
        }

        protected void onProgressUpdate(Integer... a) {
        }

        protected void onPostExecute(String result) {
        }
    }

    void fromParseMessageToWrapperList(List<Message> mMessages, ArrayList<MessageWrapper> messageWrapperList) {
        ArrayList<MessageWrapper> messageWrapperListTemp = new ArrayList<MessageWrapper> ();
        for (Message parseMessage : mMessages) {
            String toUser = null;
            if (parseMessage.getSenderId ().equals (parseMessage.getUserNum1 ())) {
                toUser = parseMessage.getUserNum2 ();
            } else {
                toUser = parseMessage.getUserNum1 ();
            }
            String picUrl = null;
            if (parseMessage.getMessageType () == MessageWrapper.MSG_TYPE_PHOTO) {
                picUrl = parseMessage.getPic ().getUrl ();
            }
            MessageWrapper messageWrapper = new MessageWrapper (parseMessage.getMessageType (),
                                                                       MessageWrapper.MSG_STATE_SUCCESS,
                                                                       parseMessage.getSenderId (),
                                                                       toUser,
                                                                       parseMessage.getMessageBody (),
                                                                       GlobalVariables.CUSTOMER_PHONE_NUM.equals (parseMessage.getSenderId ()),
                                                                       true,
                                                                       parseMessage.getCreatedAt (),
                                                                       picUrl);
            messageWrapperListTemp.add (messageWrapper);
        }
        messageWrapperList.clear ();
        messageWrapperList.addAll (messageWrapperListTemp);
    }

    /**
     * init MessageInputToolBox
     */
    @SuppressLint("ShowToast")
    private void initMessageInputToolBox() {
        box = (MessageInputToolBox) findViewById (R.id.messageInputToolBox);
        box.setOnOperationListener (new OnOperationListener () {

            @Override
            public void send(String content) {
                String body = content;
                final Message message = new Message ();
                String currentPhone = GlobalVariables.CUSTOMER_PHONE_NUM;
                String otherPhone = otherUser.getUserPhoneNum ();
                if (currentPhone.compareTo (otherPhone) > 0) {
                    message.setUserNum1 (currentPhone);
                    message.setUserNum2 (otherPhone);
                } else {
                    message.setUserNum1 (otherPhone);
                    message.setUserNum2 (currentPhone);
                }
                message.setSenderId (currentPhone);
                message.setMessageBody (body);
                message.setMessageType (MessageWrapper.MSG_TYPE_TEXT);
                try {
                    message.save ();
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                receiveNoBackGround ();
                updateMessageRoomItem (body);
            }

            @Override
            public void selectedFuncation() {
                Intent i = new Intent (
                                              Intent.ACTION_PICK,
                                              MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult (i, SELECT_PICTURE);
            }

        });

    }
}
package com.example.mipo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JSONCustomReceiver extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushReceive(Context mContext, Intent i) {

        JSONObject json = null;

        try {
            json = new JSONObject(i.getExtras().getString("com.parse.Data"));
            if(!json.getString("action").equals(GlobalVariables.ChatWith)) {
                getUserPhoto(mContext, json, json.getString("action"));
                updateMessage(json.getString("messageObjectId"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {

        }

    public  void generateNotification(Context context, Bitmap bmp ,JSONObject object) {

        Intent intent = null;
       if(!MainPageActivity.done)
       {
           intent = new Intent(context, MainPageActivity.class);
       }
        else
       {
           int index = 0;
           intent = new Intent(context, ChatActivity.class);
           Bundle b = new Bundle();
           try {
               intent.putExtra("userId", object.getString("action"));
               intent.putExtra("userName", object.getString("userName"));
               index = Integer.parseInt(object.getString("index"));
           } catch (JSONException e) {
               e.printStackTrace();
           }
           b.putInt("index", index);
           intent.putExtras(b);
       }


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        Notification noti = null;
        try {
            GlobalVariables.pushAction = object.getString("action");
            GlobalVariables.pushUserName = object.getString("userName");
            GlobalVariables.pushIndex = object.getString("index");
            noti = new Notification.Builder(context)
                    .setContentTitle(object.getString("userName"))
                    .setContentText(object.getString("_alert"))
                    .setSmallIcon(R.drawable.fire)
                    .setLargeIcon(bmp)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setGroup(object.getString("userName"))
                    .setGroupSummary(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .build();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        noti.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager mgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.notify(generateRandom(), noti);


    }

    private void getUserPhoto(final Context context,  final JSONObject object,String id) {
        ParseQuery<Profile> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo ("number", id);
        query.findInBackground(new FindCallback<Profile>() {
            public void done(List<Profile> profiles, ParseException e) {
                if (e == null) {
                    if (profiles.size() > 0) {

                        ParseFile imageFile = (ParseFile) profiles.get(0).get("pic");
                        if (imageFile != null) {
                            imageFile.getDataInBackground(new GetDataCallback() {
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {

                                        Bitmap bmp = BitmapFactory
                                                .decodeByteArray(
                                                        data, 0,
                                                        data.length);
                                        generateNotification(context, bmp, object);


                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }


    public void updateMessage(String objectId){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("MipoMessage");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject message, ParseException arg1) {
                if (arg1 == null) {

                    message.put("pending", true);
                    message.saveInBackground();
                }
            }
        });


    }

}

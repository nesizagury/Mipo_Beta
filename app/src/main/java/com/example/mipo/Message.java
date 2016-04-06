package com.example.mipo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("MipoMessage")
public class Message extends ParseObject {

    public String getMessageBody() {
        return getString ("messageBody");
    }

    public void setMessageBody(String messageBody) {
        put ("messageBody", messageBody);
    }

    public void setUserNum1(String userNum1) {
        put ("userNum1", userNum1);
    }

    public String getUserNum1() {
        return getString ("userNum1");
    }

    public void setUserNum2(String userNum2) {
        put ("userNum2", userNum2);
    }

    public String getUserNum2() {
        return getString ("userNum2");
    }

    public void setSenderId(String senderId) {
        put ("senderId", senderId);
    }

    public String getSenderId() {
        return getString ("senderId");
    }

    public void setSeen(boolean seen) {
        put ("seen", seen);
    }

    public boolean getSeen() {
        return getBoolean("seen");
    }

    public boolean getPending() {
        return getBoolean("pending");
    }

    public void setReceiverId(String receiverId) {
        put ("receiverId", receiverId);
    }

    public String getReceiverId() {
        return getString("receiverId");
    }


    public void setIndex(int index) {
        put ("index", index);
    }

    public int getIndex() {
        return getInt("index");
    }

    public void setKey(String key) {
        put ("key", key);
    }

    public String getKey() {
        return getString("key");
    }


}
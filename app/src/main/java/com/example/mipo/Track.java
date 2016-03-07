package com.example.mipo;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("Track")
public class Track extends ParseObject{

    public String getName() {
        return getString ("name");
    }

    public void setName(String name) {
        put ("name", name);
    }

    public String getReciverId() {
        return getString ("reciverId");
    }

    public void setReciverId(String reciverId) {
        put ("reciverId", reciverId);
    }


    public String getSenderId() {
        return getString ("senderId");
    }

    public void setSenderId(String senderId) {
        put ("senderId", senderId);
    }

    public String getSenderName() {
        return getString ("senderName");
    }

    public void setSenderName(String senderName) {
        put ("senderName", senderName);
    }

    public String getSenderPicUrl() {
        return getString ("senderPicUrl");
    }

    public void setSenderPicUrl(String senderPicUrl) {
        put ("senderPicUrl", senderPicUrl);
    }
}

package com.example.mipo;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("Track")
public class Track extends ParseObject {

    public String getTrackName() {
        return getString ("name");
    }

    public void setTrackName(String name) {
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
}

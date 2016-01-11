package com.example.mipo;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

    public String getUserId() {
        return getString("userId");
    }

    public String getBody() {
        return getString("body");
    }

    public void setUserId(String userId) {
        put("userId", userId);
    }

    public void setBody(String body) {
        put("body", body);
    }

    public void setDes(String des) {
        put("des", des);
    }

    public String getDes(){
        return getString("des");
    }

    public void setCombinedID(String combinedID) {
        put("combinedID", combinedID);
    }

    public String getCombinedID(){
        return getString("combinedID");
    }
}
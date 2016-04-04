package com.example.mipo;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("MipoMessage")
public class Message extends ParseObject {

    public ParseFile getPic() {

        return getParseFile ("pic");
    }

    public void setPic(ParseFile file) {
        put ("pic", file);
    }

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
}
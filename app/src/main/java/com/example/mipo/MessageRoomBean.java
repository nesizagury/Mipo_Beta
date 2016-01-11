package com.example.mipo;

import java.io.Serializable;

public class MessageRoomBean implements Serializable {

    int imageId;
    String name;
    String body;
    int conversationId;
    String id;


    public MessageRoomBean(){

    }

    public MessageRoomBean(int imageId, String name, String body,int conversationId) {

        this.imageId = imageId;
        this.name = name;
        this.conversationId = conversationId;
        this.body = body;


    }
    public int getImageId(){
        return imageId;
    }

    public void setImageId(int imageId){

        this.imageId = imageId;
        this.conversationId = conversationId;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getBody()
    {
        return  body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;



    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int id) {
        this.conversationId = conversationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

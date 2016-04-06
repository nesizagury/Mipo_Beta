package com.example.mipo.chat;

import java.util.Date;

public class MessageWrapper {
    public final static int MSG_TYPE_TEXT = 0;
    public final static int MSG_TYPE_PHOTO = 1;
    public final static int MSG_TYPE_FACE = 2;

    public final static int MSG_STATE_SENDING = 0;
    public final static int MSG_STATE_SUCCESS = 1;
    public final static int MSG_STATE_FAIL = 2;

    private Long id;
    private Integer type;        // 0-text | 1-photo | 2-face | more type ... TODO://
    private Integer state;        // 0-sending | 1-success | 2-fail
    private String fromUser;
    private String toUser;
    private String content;
    private String picUrl;

    private Boolean isMeSending;
    private Boolean sendSucces;
    private Date time;

    public MessageWrapper(Integer type, Integer state, String fromUser, String toUser,
                          String content, Boolean isMeSending, Boolean sendSucces, Date time, String picUrl) {
        super ();
        this.type = type;
        this.state = state;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.content = content;
        this.isMeSending = isMeSending;
        this.sendSucces = sendSucces;
        this.time = time;
        this.picUrl = picUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsMeSending() {
        return isMeSending;
    }

    public void setIsMeSending(Boolean isMeSending) {
        this.isMeSending = isMeSending;
    }

    public Boolean getSendSucces() {
        return sendSucces;
    }

    public void setSendSucces(Boolean sendSucces) {
        this.sendSucces = sendSucces;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

}

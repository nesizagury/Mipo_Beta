package com.example.mipo;

public class TrackItem {

    String name;
    String trackName;
    String picUrl;
    String userPhone;

    public TrackItem(String name, String trackName, String picUrl, String userPhone) {
        this.name = name;
        this.trackName = trackName;
        this.picUrl = picUrl;
        this.userPhone = userPhone;
    }

    public String getSenderUserPhone() {
        return userPhone;
    }

    public void setSenderUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
}

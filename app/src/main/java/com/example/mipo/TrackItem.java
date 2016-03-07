package com.example.mipo;

/**
 * Created by מנהל on 28/02/2016.
 */
public class TrackItem {

    String name;
    String trackName;
    String picUrl;

    public TrackItem(String name, String trackName, String picUrl) {
        this.name = name;
        this.trackName = trackName;
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}

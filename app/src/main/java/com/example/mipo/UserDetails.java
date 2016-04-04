package com.example.mipo;

import com.parse.ParseGeoPoint;

import java.util.Date;
import java.util.List;

public class UserDetails {

    String userPhoneNum;
    String name;
    String age;
    Date lastSeen;
    String status;
    String height;
    String Weight;
    String nation;
    String body_type;
    String relationship_status;
    String looking_for;
    String about;
    String picUrl;
    String gender;
    String preferred;
    ParseGeoPoint userLocation;
    int indexInAllDataList;

    private boolean favorite = false;
    boolean isFilteredOK = true;
    boolean isOnline = false;
    double dist;
    List<String> blocked;
    List<String> blockedBy;

    public UserDetails(String userPhoneNum,String gender,String preferred,
                       String name,
                       String age,
                       Date lastSeen,
                       String status,
                       String height,
                       String weight,
                       String nation,
                       String body_type,
                       String relationship_status,
                       String looking_for,
                       String about,
                       String picUrl,
                       ParseGeoPoint userLocation,
                       int indexInAllDataList,
                       boolean isOnline,
                       List blocked,
                       List blockedBy) {
        this.userPhoneNum = userPhoneNum;
        this.gender=gender;
        this.preferred=preferred;
        this.name = name;
        this.age = age;
        this.lastSeen = lastSeen;
        this.status = status;
        this.height = height;
        this.Weight = weight;
        this.nation = nation;
        this.body_type = body_type;
        this.relationship_status = relationship_status;
        this.looking_for = looking_for;
        this.about = about;
        this.picUrl = picUrl;
        this.userLocation = userLocation;
        this.indexInAllDataList = indexInAllDataList;
        this.isOnline = isOnline;
        this.blocked = blocked;
        this.blockedBy=blockedBy;

    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPreferred() {
        return preferred;
    }

    public void setPreferred(String preferred) {
        this.preferred = preferred;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getEthnicity() {
        return nation;
    }

    public void setEtnicity(String nation) {
        this.nation = nation;
    }

    public String getBody_type() {
        return body_type;
    }

    public void setBody_type(String body_type) {
        this.body_type = body_type;
    }

    public String getRelationship_status() {
        return relationship_status;
    }

    public void setRelationship_status(String relationship_status) {
        this.relationship_status = relationship_status;
    }

    public String getLooking_for() {
        return looking_for;
    }

    public void setLooking_for(String looking_for) {
        this.looking_for = looking_for;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public ParseGeoPoint getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(ParseGeoPoint userLocation) {
        this.userLocation = userLocation;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isFilteredOK() {
        return isFilteredOK;
    }

    public void setIsFilteredOK(boolean isFilteredOK) {
        this.isFilteredOK = isFilteredOK;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public int getIndexInAllDataList() {
        return indexInAllDataList;
    }

    public void setIndexInAllDataList(int indexInAllDataList) {
        this.indexInAllDataList = indexInAllDataList;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public List getBlockedBy() {return blockedBy;}

    public void setBlockedBy(List blockedBy) {
        this.blockedBy = blockedBy;
    }

    public List getBlocked() {return blocked;}

    public void setBlocked(List blocked) {
        this.blocked = blocked;
    }
}

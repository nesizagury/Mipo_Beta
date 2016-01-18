package com.example.mipo;

public class User {

    int imageId;
    String name;
    int on_off;
    boolean currentUser;
    private boolean favorite = false;
    String id;
    int indexInUD;

    public User(int imageId, String name, int on_off, boolean currentUser, String id, int indexInUD) {
        this.imageId = imageId;
        this.name = name;
        this.on_off = on_off;
        this.currentUser = currentUser;
        this.id = id;
        this.indexInUD = indexInUD;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public int getIndexInUD() {
        return indexInUD;
    }

    public void setIndexInUD(int indexInUD) {
        this.indexInUD = indexInUD;
    }

}
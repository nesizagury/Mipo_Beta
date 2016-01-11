package com.example.mipo;

public class User {
	
	int imageId;
	String name;
	int on_off;
	boolean curentUser;
	String id;

	public User(int imageId, String name, int on_off, boolean curentUser, String id) {
		this.imageId = imageId;
		this.name = name;
		this.on_off = on_off;
		this.curentUser = curentUser;
		this.id = id;
	}

	public int getImageId(){
		return imageId;
	}

	public String getId() {
		return id;
	}
}

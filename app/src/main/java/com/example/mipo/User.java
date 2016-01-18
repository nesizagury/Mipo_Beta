package com.example.mipo;

public class User {
	
	int imageId;
	String name;
	int on_off;
	boolean currentUser;
	String id;

	public User(int imageId, String name, int on_off, boolean currentUser, String id) {
		this.imageId = imageId;
		this.name = name;
		this.on_off = on_off;
		this.currentUser = currentUser;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public boolean isCurrentUser() {
		return currentUser;
	}
}

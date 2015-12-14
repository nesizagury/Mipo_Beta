package com.example.aaa;

public class User {
	
	int imageId;
	String name;
	int on_off;
	
	public User(int imageId, String name, int on_off) {
		
		this.imageId = imageId;
		this.name = name;
		this.on_off = on_off;
	}
	public int getImageId(){
		return imageId;
	}

}

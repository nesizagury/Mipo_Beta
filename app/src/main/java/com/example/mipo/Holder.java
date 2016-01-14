package com.example.mipo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Holder {

	ImageView image;
	ImageView image2;
	TextView name;
	
	public Holder (View v) {
		
		
		image = (ImageView) v.findViewById(R.id.imageView1);
		image2 = (ImageView) v.findViewById(R.id.imageView2);
		name = (TextView) v.findViewById(R.id.textView1);
	}

}

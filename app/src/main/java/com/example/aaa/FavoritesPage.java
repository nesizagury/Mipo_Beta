package com.example.aaa;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class FavoritesPage extends Activity implements AdapterView.OnItemClickListener{


    GridView grid;
    ImageView iv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        grid = (GridView) findViewById(R.id.gridView1);
        iv = (ImageView) findViewById(R.id.imageView2);

        Adapts adapts = new Adapts(this,iv, MainPageActivity.lov.getFavorites_list());
        grid.setAdapter(adapts);
        grid.setOnItemClickListener(this);


    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        Intent intent = new Intent(this,UserPage.class);
        Holder holder = (Holder) view.getTag();
        User user = (User)holder.image.getTag();
        intent.putExtra("userImage",user.imageId);
        intent.putExtra("userName", user.name);
        startActivity(intent);

    }








}
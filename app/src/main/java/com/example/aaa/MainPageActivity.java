package com.example.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainPageActivity extends Activity implements AdapterView.OnItemClickListener{

    GridView grid;
    ImageView iv;
    public static List list;
    int favorites_barButton;
    public static  ListOfFavorites lov = new ListOfFavorites();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        grid = (GridView) findViewById(R.id.gridView1);
        iv = (ImageView) findViewById(R.id.imageView2);
        Adapts adapts = new Adapts(this,iv,addToList());
        grid.setAdapter(adapts);
        grid.setOnItemClickListener(this);


    }

    public List addToList(){

        list = new ArrayList();
        String[] userName_list ;
        userName_list = getResources().getStringArray(R.array.userNames);

        Random rand = new Random();
        int randSign = 0;
        int sign = 0;

        for (int j = 0; j < 20; j++) {

            for (int i = 0; i < 3; i++) {

                if (i == 1)
                    list.add(new User(R.drawable.pic0 + i, userName_list[i], 0));
                else
                    list.add(new User(R.drawable.pic0 + i, userName_list[i], R.drawable.online));

            }

        }
        return list;

    }

    public static User getUser(int i){

        return (User) list.get(i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Bundle b = new Bundle();
        Intent intent = new Intent(this,UserPage.class);
        Holder holder = (Holder) view.getTag();
        User user = (User)holder.image.getTag();
        intent.putExtra("userImage",user.imageId);
        intent.putExtra("userName", user.name);
        b.putInt("userIndex", i);
        b.putInt("online",user.on_off);
        intent.putExtras(b);
        startActivity(intent);

    }


    public void goToMessages(View view){

        Intent intent = new Intent(this,ChatActivity.class);
        startActivity(intent);

    }

    public void goToFavorites(View view){

        Intent i = new Intent(this,FavoritesPage.class);
        startActivity(i);

    }

}

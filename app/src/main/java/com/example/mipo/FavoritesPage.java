package com.example.mipo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

public class FavoritesPage extends Activity implements AdapterView.OnItemClickListener {


    GridView grid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.activity_favorities_page);
        grid = (GridView) findViewById (R.id.gridView11);

        for (int i = 7; i < 12; i++) {
            MainPageActivity.lov.addToFavorites_list (MainPageActivity.getUser (i));
        }

        grid.setAdapter (new GridAdaptor (this, MainPageActivity.lov.getFavorites_list ()));
        grid.setOnItemClickListener (this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent (this, UserPage.class);
        Bundle b = new Bundle ();
        User user = MainPageActivity.lov.getFavorites_list ().get (position);
        intent.putExtra ("userImage", user.imageId);
        intent.putExtra ("userName", user.name);
        b.putInt ("userIndex", position);
        intent.putExtras (b);
        startActivity (intent);

    }

}
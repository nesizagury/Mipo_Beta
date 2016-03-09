package com.example.mipo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FavoritesPage extends Activity implements AdapterView.OnItemClickListener {


    GridView grid;
    public final static List<UserDetails> ListOfFavorites = new ArrayList<UserDetails> ();
    TextView gpsAccuracy;
    ImageView gpsAccuracyIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.activity_favorities_page);
        grid = (GridView) findViewById (R.id.gridView11);
        gpsAccuracy = (TextView) findViewById (R.id.distanceAcuuraty);
        gpsAccuracyIcon = (ImageView) findViewById (R.id.imageView3);
        gpsAccuracy.setVisibility (View.INVISIBLE);
        gpsAccuracyIcon.setVisibility (View.INVISIBLE);
        if(GlobalVariables.MY_LOCATION != null){
            gpsAccuracy.setText (GlobalVariables.Last_SHOWED_LOCATION_ACCURACY + "m");
            gpsAccuracy.setVisibility (View.VISIBLE);
            gpsAccuracyIcon.setVisibility (View.VISIBLE);
        }
        ListOfFavorites.clear ();
        for (int i = 0; i < GlobalVariables.userDataList.size (); i++) {
            UserDetails user = GlobalVariables.userDataList.get (i);
            if (!user.equals (GlobalVariables.currentUser)) {
                if (user.isFavorite ()) {
                    ListOfFavorites.add (user);
                }
            }
        }

        grid.setAdapter (new GridAdaptor (this, ListOfFavorites, true));
        grid.setOnItemClickListener (this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent (this, UserPage.class);
        Bundle b = new Bundle ();
        UserDetails user = ListOfFavorites.get (position);
        intent.putExtra ("userName", user.name);
        intent.putExtra ("userCurrent", StaticMethods.isCurrentUser (user));
        b.putString ("userID", user.getUserPhoneNum ());
        b.putInt ("index", user.getIndexInAllDataList ());
        Date currentDate = new Date();
        long diff = currentDate.getTime() - user.getLastSeen().getTime ();
        long diffMinutes = diff / (60 * 1000);
        b.putInt ("online", (int)diffMinutes);
        intent.putExtras (b);
        startActivity (intent);
    }

    @Override
    public void onRestart() {
        super.onRestart ();

        grid = (GridView) findViewById (R.id.gridView11);
        ListOfFavorites.clear ();
        List<UserDetails> listAll = GlobalVariables.userDataList;
        for (int i = 0; i < listAll.size (); i++) {
            UserDetails user = listAll.get (i);
            if (user.isFavorite ()) {
                ListOfFavorites.add (listAll.get (i));
            }
        }

        grid.setAdapter (new GridAdaptor (this, ListOfFavorites, true));
        grid.setOnItemClickListener (this);
    }

}
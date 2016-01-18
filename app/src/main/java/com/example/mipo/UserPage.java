package com.example.mipo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserPage extends Activity implements ImageButton.OnClickListener {


    ImageButton detailed_button;
    ImageButton favorite_button;
    ImageButton report_button;
    ImageButton message_button;
    String user_name;
    private String userID;
    boolean user_current;
    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.activity_user_page);
        detailed_button = (ImageButton) findViewById (R.id.detailed_profile_button);
        favorite_button = (ImageButton) findViewById (R.id.favorite_main_button);
        message_button = (ImageButton) findViewById (R.id.message_button);
        report_button = (ImageButton) findViewById (R.id.imageButton);
        detailed_button.setOnClickListener (this);
        favorite_button.setOnClickListener (this);
        report_button.setOnClickListener (this);

        Intent intent = getIntent ();
        if (intent != null) {
            int image_id = intent.getIntExtra ("userImage", R.drawable.pic0);
            ImageView user_image = (ImageView) findViewById (R.id.usrPage_image);
            user_image.setImageResource (image_id);
            Bundle b = getIntent ().getExtras ();
            userID = b.getString ("userID");
            user_current = intent.getBooleanExtra ("userCurrent", false);
            user_name = intent.getStringExtra ("userName");
            index = b.getInt ("index");

            TextView userNameTF = (TextView) findViewById (R.id.name_profile);
            TextView seenTF = (TextView) findViewById (R.id.seen_profile);

            for (int i = 0; i < MainPageActivity.ud.size (); i++) {
                if (MainPageActivity.ud.get (i).getName ().equals (user_name)) {
                    userNameTF.setText (user_name + " , " + MainPageActivity.ud.get (i).getAge ());
                    if (user_current) {
                        seenTF.setText ("Online" + " | " + "0 meters away");
                    } else {
                        seenTF.setText (MainPageActivity.ud.get (i).getSeen () + " | " + MainPageActivity.ud.get (i).getDistance ());
                    }
                }
            }
            if (user_current) {
                favorite_button.setVisibility (View.INVISIBLE);
                report_button.setVisibility (View.INVISIBLE);
                message_button.setVisibility (View.INVISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v == detailed_button) {
            user_current = getIntent ().getBooleanExtra ("userCurrent", false);
            Intent intent = new Intent (this, DetailedProfileActivity.class);
            intent.putExtra ("userName", user_name);
            intent.putExtra ("currentUser", user_current);
            startActivity (intent);
        }

        if (v == favorite_button) {
            MainPageActivity.lov.addToFavorites_list (MainPageActivity.getUser (index));
            Toast.makeText (getApplicationContext (), "Added To Favorites", Toast.LENGTH_SHORT).show ();
        }

        if (v == report_button) {
            Toast.makeText (getApplicationContext (), "You banned this profile!", Toast.LENGTH_SHORT).show ();
        }

    }

    public void messaging(View view) {
        Intent intent = new Intent (this, ChatActivity.class);
        intent.putExtra ("userId", userID);
        intent.putExtra ("userName", user_name);
        startActivity (intent);
    }

}

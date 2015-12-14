package com.example.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class UserPage extends Activity implements ImageButton.OnClickListener{

    ImageButton detailed_button;
    ImageButton favorite_button;
    int index;
    String user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        detailed_button = (ImageButton) findViewById(R.id.detailed_profile_button);
        favorite_button = (ImageButton) findViewById(R.id.favorite_main_button);
        detailed_button.setOnClickListener(this);
        favorite_button.setOnClickListener(this);

        Intent intent = getIntent();

        if (intent != null) {

            int image_id = intent.getIntExtra("userImage", R.drawable.pic0);
            ImageView user_image = (ImageView) findViewById(R.id.usrPage_image);
            user_image.setImageResource(image_id);
            Bundle b = getIntent().getExtras();
            index = b.getInt("userIndex");

            user_name = intent.getStringExtra("userName");
            TextView userNameTF = (TextView) findViewById(R.id.name_profile);
            TextView seenTF = (TextView) findViewById(R.id.seen_profile);


            if (user_name.equals("Moran Atias"))
            {
                seenTF.setText("0 meter | online");
                userNameTF.setText(user_name + " | " + 22);

            }
            if (user_name.equals("Rotem Sela"))
            {
                seenTF.setText("28 meter | Seen 12 min ago");
                userNameTF.setText(user_name + " | " + 27);

            }
            if (user_name.equals("Mila Kunis"))
            {
                seenTF.setText("159 meter | online");
                userNameTF.setText(user_name + " | " + 23);

            }

        }
    }

    @Override
    public void onClick(View v) {

        if(v == detailed_button) {
            Intent intent = new Intent(this, DetailedProfileActivity.class);
            startActivity(intent);

        }

        if(v == favorite_button) {

         MainPageActivity.lov.addToFavorites_list(MainPageActivity.getUser(index));

        }


    }

    public void messaging(View view){

        Intent intent = new Intent(this,ChatActivity.class);
        startActivity(intent);

    }
}

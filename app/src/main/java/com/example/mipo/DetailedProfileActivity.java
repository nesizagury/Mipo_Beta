package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedProfileActivity extends Activity {

    TableRow akevot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.detailed_profile);

        Intent intent = getIntent ();
        ImageView user_image = (ImageView) findViewById (R.id.detailed_image);
        String user_name = intent.getStringExtra ("userName");
        Boolean user_current = intent.getBooleanExtra ("currentUser", false);
        akevot = (TableRow) findViewById (R.id.tableRow3);
        if (user_current) {
            akevot.setVisibility (View.INVISIBLE);
        }
        TextView userNameAgeTF = (TextView) findViewById (R.id.detailed_name_age);
        TextView currentStatusTF = (TextView) findViewById (R.id.detailed_current_status);
        TextView heightWeightTF = (TextView) findViewById (R.id.detailed_height_wheight);
        TextView bodyNationTF = (TextView) findViewById (R.id.detailed_body_nation);
        TextView relationSeekingTF = (TextView) findViewById (R.id.detailed_relatioStatus_seeking);
        TextView aboutTF = (TextView) findViewById (R.id.detailed_about);

        for (int i = 0; i < MainPageActivity.ud.size (); i++) {
            if (MainPageActivity.ud.get (i).getName ().equals (user_name)) {
                user_image.setImageResource (R.drawable.pic0 + MainPageActivity.ud.get (i).image_source);
                userNameAgeTF.setText (MainPageActivity.ud.get (i).getName () + " , " + MainPageActivity.ud.get (i).getAge ());
                currentStatusTF.setText (MainPageActivity.ud.get (i).getStatus ());
                heightWeightTF.setText (MainPageActivity.ud.get (i).getHeight () + " | " + MainPageActivity.ud.get (i).getWeight ());
                bodyNationTF.setText (MainPageActivity.ud.get (i).getBody_type () + " | " + MainPageActivity.ud.get (i).getNation ());
                relationSeekingTF.setText (MainPageActivity.ud.get (i).getRelationship_status () + " | " + MainPageActivity.ud.get (i).getLooking_for ());
                aboutTF.setText (MainPageActivity.ud.get (i).getAbout ());
            }
        }
    }

    public void like(View view) {
        Toast.makeText (getApplicationContext (), "You sent Like to her.", Toast.LENGTH_SHORT).show ();
    }

    public void hot(View view) {
        Toast.makeText (getApplicationContext (), "You sent \"You'r Hot!\" to her.", Toast.LENGTH_SHORT).show ();
    }

    public void love(View view) {
        Toast.makeText (getApplicationContext (), "You sent \"I Love your profile\" to her.", Toast.LENGTH_SHORT).show ();
    }

    public void hi(View view) {
        Toast.makeText (getApplicationContext (), "You sent \"Hi\" to her.", Toast.LENGTH_SHORT).show ();
    }
}

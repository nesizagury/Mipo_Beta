package com.example.parser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "VxNKZoBXZHTyFmx3cRhLFpGOQNMl9NMySrE6PiLP", "lZaGELhSL2Fon7Kd7TyouMBaA4zrBPg1Hm5GQYu2");
        ParseUser.enableAutomaticUser();
        ParseACL defaultAcl = new ParseACL();
        defaultAcl.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultAcl, true);

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser().getCurrentUser())){
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
        }

        else {

            ParseUser currentUser = ParseUser.getCurrentUser();

            if(currentUser != null) {

                Toast.makeText(getApplicationContext(), "Successfully Signed in", Toast.LENGTH_SHORT).show();

            }

            else {

                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }

        }


    }



}

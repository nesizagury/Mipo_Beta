package com.example.mipo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class MainActivity extends Activity {
    static boolean didLogin = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        connectToParse ();
    }

    public void connectToParse() {
        ParseUser.enableAutomaticUser ();
        ParseObject.registerSubclass (com.example.mipo.Message.class);
        ParseObject.registerSubclass (com.example.mipo.Room.class);
        ParseObject.registerSubclass (Profile.class);
        ParseObject.registerSubclass (Track.class);
        Parse.initialize (this,
                                 "WFBr5H7TSDr7hmteiK55gohpjSSqVMuDWLodr0z5",
                                 "nFKJYJXqTcc7E5QV3fsF4uZNzaeVKQm5iR5vndY7");
        ParseACL defaultAcl = new ParseACL ();
        defaultAcl.setPublicReadAccess (true);
        defaultAcl.setPublicWriteAccess (true);
        ParseACL.setDefaultACL (defaultAcl, true);


        StaticMethods.initFilterValues ();
        StaticMethods.initProfileValues ();
        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
        startActivity (intent);
        finish ();
        didLogin = true;
    }
}
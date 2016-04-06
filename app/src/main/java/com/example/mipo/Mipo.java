package com.example.mipo;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;


@ReportsCrashes(formUri = "https://collector.tracepot.com/b30094f1")
public class Mipo extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseUser.enableAutomaticUser();
        ParseObject.registerSubclass(com.example.mipo.Message.class);
        ParseObject.registerSubclass(com.example.mipo.Room.class);
        ParseObject.registerSubclass(Profile.class);
        ParseObject.registerSubclass(Track.class);
        Parse.initialize(this,
                "RJvGyrtJDjDVi8pll79cOl7mg6HGrNIXnx4IhR4L",
                "hGCEGvd43cFw0ixPXEXBggebX9Bl1dRfHGpPsDlM");
        try {
            ParseInstallation.getCurrentInstallation().save ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        Log.e("installation id ",ParseInstallation.getCurrentInstallation().getInstallationId() );
        ParseACL defaultAcl = new ParseACL ();
        defaultAcl.setPublicReadAccess(true);
        defaultAcl.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultAcl, true);
        StaticMethods.initFilterValues();
        StaticMethods.initProfileValues();
        ACRA.init (this);

    }
}
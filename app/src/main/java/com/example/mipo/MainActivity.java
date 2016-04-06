package com.example.mipo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.mipo.StaticMethods.GpsICallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class MainActivity extends Activity  implements GpsICallback {
    static boolean didLogin = false;
    String number = "";
    boolean isFundigo = false;
    String fundigoNumber = "";
    static boolean LoogedIn = false;
    private Handler handler = new Handler ();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        StaticMethods.updateDeviceLocationGPS (this, this);
        setLogin();
    }

    public void setLogin() {

        if (Locale.getDefault().getDisplayLanguage ().equals ("עברית"))
            GlobalVariables.isHeb = true;

        number = readFromFile ();
        if (number != null && !number.equals ("")) {
            GlobalVariables.CUSTOMER_PHONE_NUM = number;
            userLogin();
        }

        if(number == null || number == "")
            guestLogin();

        didLogin = true;
    }


    private String readFromFile() {
        String ret = "";

        try {
            InputStream inputStream = openFileInput("Mipo");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("a", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("a", "Can not read file: " + e.toString());
        }
        return ret;
    }

    @Override
    public void gpsCallback() {
        finish();
    }


    public void userLogin() {
        try {
            ParseUser.logIn (GlobalVariables.CUSTOMER_PHONE_NUM,
                    GlobalVariables.CUSTOMER_PHONE_NUM);
            Toast.makeText(getApplicationContext(),
                    R.string.successLoggedIn,
                    Toast.LENGTH_SHORT).show ();
            Intent intent = new Intent (MainActivity.this, MainPageActivity.class);
            startActivity (intent);
            finish ();
            LoogedIn = true;
        } catch (ParseException e) {
            e.printStackTrace ();
        }

    }

    public void guestLogin() {
        try {
            ParseUser.logIn ("m", "d"); // m,d = bar refaeli (default user).
        } catch (ParseException e1) {
            e1.printStackTrace ();
        }
        Toast.makeText (getApplicationContext (), R.string.successLoggedIn, Toast.LENGTH_SHORT).show ();
        Intent intent = new Intent (this, MainPageActivity.class);
        startActivity (intent);
        GlobalVariables.CUSTOMER_PHONE_NUM = null;
        finish ();
        LoogedIn = true;
    }


}

package com.example.mipo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

public class LoginFromFundigoActivity extends Activity {

    EditText ageET;
    EditText heightET;
    Button fundigoLogin;
    Button guestLogin;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login_from_fundigo);

        ageET = (EditText) findViewById (R.id.ageET);
        heightET = (EditText) findViewById (R.id.heightET);
        fundigoLogin = (Button) findViewById (R.id.fundigoLogin);
        guestLogin = (Button) findViewById (R.id.guestLogin);
        number = getIntent ().getStringExtra ("number");

        ParseQuery query = new ParseQuery ("Profile");
        query.whereEqualTo ("number", number);
        Profile object = null;
        try {
            object = (Profile) query.getFirst ();
            if (object.getAge () != null && object.getAge().toString().length() > 0
                    ) {

                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("Mipo", Context.MODE_PRIVATE));
                    outputStreamWriter.write(number);
                    outputStreamWriter.close();

                GlobalVariables.CUSTOMER_PHONE_NUM = number;
                login ();
            }
        } catch (IOException e1) {
            e1.printStackTrace ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }
    }

    public void guestLogin(View view) {
        try {
            ParseUser.logIn ("m", "d"); // m,d = bar refaeli (default user).
        } catch (ParseException e1) {
            e1.printStackTrace ();
        }
        Intent intent = new Intent (this, MainPageActivity.class);
        startActivity (intent);
        finish ();
        LoginActivity.LoogedIn = true;
    }

    public void fundigoLogin(View view) {
        if (ageET.getText ().toString ().length () > 0 && heightET.getText ().toString ().length () > 0) {
            saveToFileAndServer (number);
            GlobalVariables.CUSTOMER_PHONE_NUM = number;
            login ();
        }
    }

    void saveToFileAndServer(String phone_number) { // this function turns the fundigo user into a mipo user
        ParseQuery<Profile> query = new ParseQuery ("Profile");
        query.whereEqualTo ("number", phone_number);
        Profile object = null;
        try {
            object = query.getFirst ();
            object.put ("age", ageET.getText ().toString ());
            object.put ("height", heightET.getText ().toString ());
            object.put("lastSeen", new Date());
            object.save();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("Mipo", Context.MODE_PRIVATE));
            outputStreamWriter.write(phone_number);
            outputStreamWriter.close();
            Log.e(phone_number + "", "created");
        } catch (ParseException e) {
            e.printStackTrace ();
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public void login() {
        try {
            ParseUser.logIn (GlobalVariables.CUSTOMER_PHONE_NUM,
                                    GlobalVariables.CUSTOMER_PHONE_NUM);
            Intent intent = new Intent (LoginFromFundigoActivity.this, MainPageActivity.class);
            startActivity (intent);
            finish ();
            LoginActivity.LoogedIn = true;
        } catch (ParseException e) {
            e.printStackTrace ();
        }
    }
}



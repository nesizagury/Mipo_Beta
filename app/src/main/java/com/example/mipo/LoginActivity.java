package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button logginButton;
    String number = "";
    boolean isFundigo = false;
    String fundigoNumber = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Locale.getDefault().getDisplayLanguage().equals("עברית"))
            GlobalVariables.isHeb = true;

            number = readFromFile ();

        if(isFundigo)
        {
            Intent intent = new Intent(LoginActivity.this,LoginFromFundigoActivity.class);
            intent.putExtra("number", fundigoNumber);
            startActivity(intent);
            finish();
        }
        else {
            if (number != null && !number.equals("")) {
                GlobalVariables.CUSTOMER_PHONE_NUM = number;
            }

            logginButton = (Button) findViewById(R.id.button_loggin);
            if (!GlobalVariables.isHeb) {
                if (number != null && !number.equals("")) {
                    logginButton.setText("Log In as " + number);
                } else {

                    logginButton.setText("Log In as guest");
                }
            } else if (number != null && !number.equals("")) {
                logginButton.setText(getResources().getString(R.string.LoginAs) + number);
            } else {
                logginButton.setText(R.string.LoginAsGuest);
            }
            logginButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()) {
            case R.id.button_loggin:
                if (number != null && !number.equals ("")) {
                    login ();
                }
                else {
                    guestLogin ();
                }
                break;
        }
    }

    public void guestLogin() {
        try {
            ParseUser.logIn ("m", "d"); // m,d = bar refaeli (default user).
        } catch (ParseException e1) {
            e1.printStackTrace ();
        }
      //  Toast.makeText (getApplicationContext (), "Successfully Logged in", Toast.LENGTH_SHORT).show ();
        Intent intent = new Intent (this, MainPageActivity.class);
        startActivity (intent);
        finish ();

    }

    public void login() {
        try {
            ParseUser.logIn (GlobalVariables.CUSTOMER_PHONE_NUM,
                                    GlobalVariables.CUSTOMER_PHONE_NUM);
           // Toast.makeText (getApplicationContext (),"Successfully Logged in", Toast.LENGTH_SHORT).show ();
            Intent intent = new Intent (LoginActivity.this, MainPageActivity.class);
            intent.putExtra("index","ewe");
            intent.putExtra("fundigo","ewe");
            startActivity (intent);
            finish ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }

    }

    private String readFromFile() {
        String number = "";
        String myData = "";
        try {
            File myExternalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"verify.txt");
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                myData = myData + strLine;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(myData != null) {
            if(myData.contains("isFundigo")) {
                String[] parts = myData.split(" ");
                fundigoNumber = parts[0];
                isFundigo = true;
            }

        }

        return myData;
    }
}

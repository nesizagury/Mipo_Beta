package com.example.parser;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {


    Button loginButton;
    Button signupButton;
    String username;
    String password;
    EditText usernameET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameET = (EditText) findViewById(R.id.password_et);
        passwordET = (EditText) findViewById(R.id.password_et);

        loginButton = (Button) findViewById(R.id.button_login);
        signupButton = (Button) findViewById(R.id.button_password);


    }


    public void Login(View v)
    {

        username = usernameET.getText().toString();
        password = passwordET.getText().toString();


        ParseUser.logInInBackground(username, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {

                if (user != null)
                    Toast.makeText(getApplicationContext(), "Successfully Loged in", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "this user does not exist", Toast.LENGTH_SHORT).show();


            }
        });

    }
    public void Signup(View view) {

        username = usernameET.getText().toString();
        password = passwordET.getText().toString();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null)
                    Toast.makeText(getApplicationContext(), "Successfully Signed up", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), " Error ): ", Toast.LENGTH_SHORT).show();


            }
        });

    }


}

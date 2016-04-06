package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by מנהל on 01/04/2016.
 */
public class FromFundigo extends Activity {

    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVariables.userPhoneNumFromFundigo = getIntent().getStringExtra("index");
        number = readFromFile ();
        if (number != null && !number.equals ("")){
            if(GlobalVariables.userDataList.size() > 0) {
                StaticMethods.openUserPage(this, getIntent().getStringExtra("index"));
            }
            else{
                Intent intent = new Intent(FromFundigo.this,MainPageActivity.class);
                startActivity(intent);
            }

        }
        else{

            Intent intent = new Intent(FromFundigo.this,LoginFromFundigoActivity.class);
            intent.putExtra("number",getIntent().getStringExtra("userNumber"));
            startActivity(intent);

        }

        finish();

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
}

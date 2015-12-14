package com.example.aaa;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

public class DetailedProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_profile);
        Intent intent = getIntent();
    }

}

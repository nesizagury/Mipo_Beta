package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends Activity {

    Button smsVerify;
    Button updateProfile;
    Button block_user_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_menu);
        smsVerify = (Button) findViewById (R.id.smsVerification);
        updateProfile = (Button) findViewById (R.id.editProfile);
        block_user_button = (Button) findViewById (R.id.block_user_button);
        if (GlobalVariables.CUSTOMER_PHONE_NUM != null && !GlobalVariables.CUSTOMER_PHONE_NUM.isEmpty ()) {
            smsVerify.setVisibility (View.GONE);
        } else {
            updateProfile.setVisibility (View.GONE);
            block_user_button.setVisibility (View.GONE);
        }
    }

    public void editProfile(View v) {
        Intent intent = new Intent (MenuActivity.this, EditProfileActivity.class);
        startActivity (intent);
        finish ();
    }

    public void smsVerification(View v) {
        Intent intent = new Intent (MenuActivity.this, SmsSignUpActivityPart1.class);
        startActivity (intent);
        finish ();
    }

    public void refreshData(View v) {
        MainPageActivity.downloadProfilesData ();
    }

    public void AboutApp(View v) {
        Intent aboutIntent = new Intent (MenuActivity.this, AboutActivity.class);
        startActivity (aboutIntent);
    }

    public void BlockProfiles(View v) {
        Intent aboutIntent = new Intent (MenuActivity.this, BlockProfilesActivity.class);
        startActivity (aboutIntent);
    }
}

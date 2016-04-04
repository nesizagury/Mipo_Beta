package com.example.mipo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sinch.verification.CodeInterceptionException;
import com.sinch.verification.Config;
import com.sinch.verification.IncorrectCodeException;
import com.sinch.verification.InvalidInputException;
import com.sinch.verification.ServiceErrorException;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;


public class SmsSignUpActivityPart1 extends Activity  {

    private String[] array_spinner;
    private Spinner s;
    private EditText sms_phoneET;
    private Button sms_next_bt1;

    private String area;
    private String username;
    private String phone_number_to_verify;
    private String phone_number_no_country_prefix;



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        forceRTLIfSupported();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sign_up_activity_part1);
        s = (Spinner) findViewById (R.id.sms_spinner);
        sms_phoneET=(EditText)findViewById(R.id.sms_phoneET);
        sms_next_bt1=(Button)findViewById(R.id.sms_next_bt1);


        array_spinner = new String[6];
        array_spinner[0] = "050";
        array_spinner[1] = "052";
        array_spinner[2] = "053";
        array_spinner[3] = "054";
        array_spinner[4] = "055";
        array_spinner[5] = "058";


        ArrayAdapter adapter = new ArrayAdapter (this, android.R.layout.simple_spinner_item, array_spinner);
        s.setAdapter (adapter);

        sms_phoneET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null &&
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    area = s.getSelectedItem().toString();
                    username = sms_phoneET.getText().toString();
                    phone_number_to_verify = getNumber(sms_phoneET.getText().toString(), area);
                    Log.d("m123",phone_number_to_verify);
                    phone_number_no_country_prefix = area + sms_phoneET.getText().toString();


                }
                return false;
            }
        });

        sms_next_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sms_phoneET.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),R.string.enter_phone_number,Toast.LENGTH_LONG).show();
                    return;
                }
                if(sms_phoneET.getText().toString().length()!=7){
                    Toast.makeText(getApplicationContext(),R.string.phone_number_must_be_7,Toast.LENGTH_LONG).show();
                    return;
                }
                area = s.getSelectedItem().toString();
                phone_number_to_verify = getNumber(sms_phoneET.getText().toString(), area);
                phone_number_no_country_prefix = area + sms_phoneET.getText().toString();
                username = sms_phoneET.getText().toString();
                phone_number_to_verify = getNumber(sms_phoneET.getText().toString(), area);

                Intent intent =new Intent(SmsSignUpActivityPart1.this,SmsSignUpActivityPart2.class);
                intent.putExtra("username",username);
                intent.putExtra("phone_number_no_country_prefix",phone_number_no_country_prefix);
                intent.putExtra("phone_number_to_verify",phone_number_to_verify);

                startActivityForResult(intent, 1);

                smsVerify(phone_number_to_verify);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1 && resultCode==RESULT_OK){
            finish();
        }
    }
    public String getNumber(String number, String area) {
        switch (area) {
            case "050":
                number = "97250" + number;
                break;
            case "052":
                number = "97252" + number;
                break;
            case "053":
                number = "97253" + number;
                break;
            case "054":
                number = "97254" + number;
                break;
            case "055":
                number = "97255" + number;
                break;
            case "058":
                number = "97258" + number;
                break;
        }
        return number;
    }


    public void smsVerify(String phone_number) {
        Config config = SinchVerification.config().applicationKey ("b9ee3da5-0dc9-40aa-90aa-3d30320746f3").context (getApplicationContext ()).build ();
        VerificationListener listener = new MyVerificationListener ();
        Verification verification = SinchVerification.createSmsVerification(config, phone_number, listener);
        verification.initiate();
    }

    class MyVerificationListener implements VerificationListener {
        @Override
        public void onInitiated() {

        }

        @Override
        public void onInitiationFailed(Exception e) {
            if (e instanceof InvalidInputException) {
                // Incorrect number provided
                e.printStackTrace ();
            } else if (e instanceof ServiceErrorException) {
                // Sinch service error
                e.printStackTrace ();
            } else {
                // Other system error, such as UnknownHostException in case of network error
                e.printStackTrace ();
            }
        }

        @Override
        public void onVerified() {
            Toast.makeText(getApplicationContext(), phone_number_no_country_prefix + " Verified!",
                    Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(SmsSignUpActivityPart1.this,SmsSignUpActivityPart2.class);
            intent.putExtra("username",username);
            intent.putExtra("phone_number_no_country_prefix",phone_number_no_country_prefix);
            intent.putExtra("phone_number_to_verify",phone_number_to_verify);

            startActivityForResult(intent, 1);


        }

        @Override
        public void onVerificationFailed(Exception e) {
            if (e instanceof InvalidInputException) {
                e.printStackTrace ();
                // Incorrect number or code provided
                Toast.makeText (getApplicationContext (), "invalid phone number try again.", Toast.LENGTH_SHORT).show ();
            } else if (e instanceof CodeInterceptionException) {
                // Intercepting the verification code automatically failed, input the code manually with verify()
                e.printStackTrace ();
            } else if (e instanceof IncorrectCodeException) {
                e.printStackTrace ();
            } else if (e instanceof ServiceErrorException) {
                e.printStackTrace ();
            } else {
                e.printStackTrace ();
            }
        }
    }
}

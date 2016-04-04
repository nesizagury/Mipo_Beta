package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SmsSignUpActivityPart2 extends Activity {

    private static final int SELECT_PICTURE = 1;
    private ImageView sms_p2_imageV;
    private Button sms_p2_upload_button;
    private EditText sms_p2_usernameTE;
    private Spinner sms_p2_age_s;
    private Spinner sms_p2_gender_s;
    private Spinner sms_p2_lokingfor_s;
    private Spinner sms_p2_height_s;
    private Button sms_p2_button;
    private String phone_number_no_country_prefix;
    private String phone_number_to_verify;
    private Profile previousDataFound;
    private String username;
    private String[] arrHeight=new String[120];
    private String[] arrAge=new String[50];
    private String[] arrGender=new String[2];
    private boolean image_selected = false;
    private boolean image_was_before;
    private Bitmap bmp;
    private TextView legalStatement;
    private CheckBox legalStaCheckBox;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sign_up_activity_part2);

        sms_p2_imageV=(ImageView)findViewById(R.id.sms_p2_imageV);
        sms_p2_upload_button=(Button)findViewById(R.id.sms_p2_upload_button);
        sms_p2_usernameTE=(EditText)findViewById(R.id.sms_p2_usernameTE);
        sms_p2_age_s=(Spinner)findViewById(R.id.sms_p2_age_s);
        sms_p2_gender_s=(Spinner)findViewById(R.id.sms_p2_gender_s);
        sms_p2_lokingfor_s=(Spinner)findViewById(R.id.sms_p2_lokingfor_s);
        sms_p2_height_s=(Spinner)findViewById(R.id.sms_p2_height_s);
        sms_p2_button=(Button)findViewById(R.id.sms_p2_button);
        legalStatement=(TextView)findViewById(R.id.sms_p2_legal_statement_tv);
        legalStaCheckBox=(CheckBox)findViewById(R.id.sms_p2_legal_check_box);
        String udata=getResources().getString(R.string.legal_statement);
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        legalStatement.setText(content);

        final Intent intentLegal=new Intent(SmsSignUpActivityPart2.this,LegalStatement.class);
        legalStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentLegal);
            }
        });


        Intent intent=getIntent();
        phone_number_no_country_prefix=intent.getStringExtra("phone_number_no_country_prefix");
        phone_number_to_verify=intent.getStringExtra("phone_number_to_verify");
        username=intent.getStringExtra("username");
        Log.d("m123",phone_number_no_country_prefix);
        getUserPreviousDetails();



        for(int i=0;i<120;i++){
            arrHeight[i]=""+(i+100);
        }
        for(int i=0;i<50;i++){
            arrAge[i]=""+(i+18);
        }
        arrGender[0]=this.getString(R.string.man);
        arrGender[1]=this.getString(R.string.woman);

        ArrayAdapter adapterAge = new ArrayAdapter (this, android.R.layout.simple_spinner_item, arrAge);
        sms_p2_age_s.setAdapter (adapterAge);

        ArrayAdapter adapterGender = new ArrayAdapter (this, android.R.layout.simple_spinner_item, arrGender);
        sms_p2_gender_s.setAdapter (adapterGender);

        ArrayAdapter adapterHeight = new ArrayAdapter (this, android.R.layout.simple_spinner_item, arrHeight);
        sms_p2_height_s.setAdapter (adapterHeight);

        ArrayAdapter adapterLookingFor = new ArrayAdapter (this, android.R.layout.simple_spinner_item, arrGender);
        sms_p2_lokingfor_s.setAdapter (adapterLookingFor);


    }

    public void Signup(View view) {

        username = sms_p2_usernameTE.getText ().toString ();
        if(username.isEmpty() || username.equals(" ")){
            Toast.makeText(this,getResources().getString(R.string.enter_name),Toast.LENGTH_LONG).show();
            return;
        }
        if(!legalStaCheckBox.isChecked()){
            Toast.makeText(this,getResources().getString(R.string.read_legal_statement),Toast.LENGTH_LONG).show();
            return;
        }

        Profile profile;
        if (previousDataFound != null) {
            profile = previousDataFound;
            ParseUser.logOut();
            try {
                ParseUser.logIn (phone_number_no_country_prefix, phone_number_no_country_prefix);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
        } else {

            profile = new Profile ();
            ParseUser user = new ParseUser ();
            user.setUsername (phone_number_no_country_prefix);
            user.setPassword(phone_number_no_country_prefix);
            try {
                user.signUp ();
            } catch (ParseException e) {
                e.printStackTrace ();
            }
            profile.setNumber (phone_number_no_country_prefix);
            profile.setUser (user);
        }
        profile.setName(username);
        profile.setAge(sms_p2_age_s.getSelectedItem().toString());
        profile.setHeight(String.valueOf(sms_p2_height_s.getSelectedItem().toString()));
        profile.setLastSeen(new Date());
        if(sms_p2_gender_s.getSelectedItemPosition()==0) {
            profile.setGender("man");
        }else{
            profile.setGender("woman");
        }

        if(sms_p2_lokingfor_s.getSelectedItemPosition()==0) {
            profile.setPreferred("man");
            Log.d("m123","if(sms_p2_lokingfor: "+sms_p2_lokingfor_s.getSelectedItem());
        }else{
            profile.setPreferred("woman");
            Log.d("m123", "else                " + profile.getPreferred());
        }



        ParseGeoPoint parseGeoPoint = new ParseGeoPoint (32.78486991,
                35.52234626);
        profile.setLocation (parseGeoPoint);
        try {
            if (image_selected) {
                sms_p2_imageV.buildDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream ();
                if(bmp.getByteCount () > 500000) {
                    bmp.compress (Bitmap.CompressFormat.JPEG, 100, stream);
                } else{
                    bmp.compress (Bitmap.CompressFormat.PNG, 100, stream);
                }
                byte[] image = stream.toByteArray ();
                ParseFile file = new ParseFile ("picturePath", image);
                try {
                    file.save ();
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                ParseACL parseAcl = new ParseACL ();
                parseAcl.setPublicReadAccess (true);
                parseAcl.setPublicWriteAccess (true);
                profile.setACL (parseAcl);
                profile.put ("pic", file);
            } else if (!image_was_before) {
                bmp = BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.no_image_icon_md);
                sms_p2_imageV.setImageBitmap (bmp);
                sms_p2_imageV.buildDrawingCache ();
                ByteArrayOutputStream stream = new ByteArrayOutputStream ();
                bmp.compress (Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image = stream.toByteArray ();
                ParseFile file = new ParseFile ("picturePath", image);
                try {
                    file.save ();
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                ParseACL parseAcl = new ParseACL ();
                parseAcl.setPublicReadAccess (true);
                parseAcl.setPublicWriteAccess (true);
                profile.setACL (parseAcl);
                profile.put ("pic", file);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
        try {
            profile.save ();
            GlobalVariables.CUSTOMER_PHONE_NUM = phone_number_no_country_prefix;
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.successProfileCreated), Toast.LENGTH_SHORT).show();
            saveToFile(phone_number_no_country_prefix);
            MainPageActivity.downloadProfilesData ();

            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            finish();

        } catch (ParseException e) {
            Toast.makeText (getApplicationContext (), " Error ): ", Toast.LENGTH_SHORT).show ();
            e.printStackTrace ();
        }
    }
    void saveToFile(String phone_number) {
        File myExternalFile = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "verify.txt");
        try {
            FileOutputStream fos = new FileOutputStream (myExternalFile);
            fos.write(phone_number.getBytes());
            fos.close();
            Log.e("number", phone_number);
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public void imageUpload(View view) {
        Intent i = new Intent (
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult (i, SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData ();
            ParcelFileDescriptor parcelFileDescriptor =
                    null;
            try {
                parcelFileDescriptor = this.getContentResolver ().openFileDescriptor (selectedImage, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace ();
            }
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor ();
            Bitmap image = BitmapFactory.decodeFileDescriptor (fileDescriptor);
            try {
                parcelFileDescriptor.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
            Matrix matrix = new Matrix ();
            int angleToRotate = StaticMethods.getOrientation (selectedImage, this);
            matrix.postRotate (angleToRotate);
            Bitmap rotatedBitmap = Bitmap.createBitmap (image,
                    0,
                    0,
                    image.getWidth (),
                    image.getHeight (),
                    matrix,
                    true);
            bmp = rotatedBitmap;
            sms_p2_imageV.setImageBitmap (rotatedBitmap);
            image_selected = true;
        }
    }
    private void getUserPreviousDetails() {
        ParseQuery<Profile> query = ParseQuery.getQuery ("Profile");
        query.whereEqualTo ("number", phone_number_no_country_prefix);
        query.findInBackground (new FindCallback<Profile>() {
            public void done(List<Profile> profiles, ParseException e) {
                if (e == null) {
                    if (profiles.size () > 0) {
                        previousDataFound = (Profile) profiles.get(0);
                        if (sms_p2_usernameTE.getText ().toString ().isEmpty ()) {
                            sms_p2_usernameTE.setText(previousDataFound.get("name").toString());
                            sms_p2_usernameTE.setSelection (sms_p2_usernameTE.getText ().length ());

                        }


                        String age = previousDataFound.getAge();
                        String height=previousDataFound.getHeight();
                        String gender=previousDataFound.getGender();
                        String preferred=previousDataFound.getPreferred();
                        Log.d("m1234","previousDataFound: "+preferred);

                        for (int i = 0; i < arrHeight.length; i++) {
                            String spinnerHeight = arrHeight[i];
                            if (height.equals (spinnerHeight)) {
                                sms_p2_height_s.setSelection (i);
                                break;
                            }
                        }

                        if (gender.equals ("man")) {
                            sms_p2_gender_s.setSelection (0);
                        }else{
                            sms_p2_gender_s.setSelection (1);
                        }

                        if (preferred.equals ("man")) {
                            sms_p2_lokingfor_s.setSelection (0);
                        }else{
                            sms_p2_lokingfor_s.setSelection (1);
                        }


                        for (int i = 0; i < arrAge.length; i++) {
                            String spinnerAge = arrAge[i];
                            if (age.equals (spinnerAge)) {
                                sms_p2_age_s.setSelection(i);
                                break;
                            }
                        }
                        if (!image_selected) {
                            ParseFile imageFile = (ParseFile) previousDataFound.get("pic");
                            if (imageFile != null) {
                                imageFile.getDataInBackground (new GetDataCallback() {
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null) {
                                            Bitmap bmp = BitmapFactory
                                                    .decodeByteArray(
                                                            data, 0,
                                                            data.length);
                                            sms_p2_imageV.setImageBitmap(bmp);
                                            image_was_before = true;
                                        } else {
                                            e.printStackTrace ();
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else {
                    e.printStackTrace ();
                }
            }
        });
    }
}

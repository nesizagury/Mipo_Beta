package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class DetailedProfileActivity extends Activity {

    TableRow akevot;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.detailed_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow ().getDecorView ().setLayoutDirection (View.LAYOUT_DIRECTION_LOCALE);
        }

        Intent intent = getIntent ();
        ImageView user_image = (ImageView) findViewById (R.id.detailed_image);
        Boolean user_current = intent.getBooleanExtra ("currentUser", false);
        int userIndex = intent.getIntExtra ("index", 0);
        userDetails = GlobalVariables.userDataList.get (userIndex);
        akevot = (TableRow) findViewById (R.id.tableRow3);
        if (user_current) {
            akevot.setVisibility (View.INVISIBLE);
        }
        TextView userNameAgeTF = (TextView) findViewById (R.id.detailed_name_age);
        TextView currentStatusTF = (TextView) findViewById (R.id.detailed_current_status);
        TextView heightWeightTF = (TextView) findViewById (R.id.detailed_height_wheight);
        TextView bodyNationTF = (TextView) findViewById (R.id.detailed_body_nation);
        TextView relationSeekingTF = (TextView) findViewById (R.id.detailed_relatioStatus_seeking);
        TextView aboutTF = (TextView) findViewById (R.id.detailed_about);
        if (imageLoader == null || (imageLoader != null && !imageLoader.isInited ())) {
            options = new DisplayImageOptions.Builder ()
                              .cacheOnDisk (true)
                              .cacheInMemory (true)
                              .bitmapConfig (Bitmap.Config.RGB_565)
                              .imageScaleType (ImageScaleType.EXACTLY)
                              .resetViewBeforeLoading (true)
                              .build ();
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder (this)
                                                      .defaultDisplayImageOptions (options)
                                                      .threadPriority (Thread.MAX_PRIORITY)
                                                      .threadPoolSize (1)
                                                      .memoryCache (new WeakMemoryCache ())
                                                      .denyCacheImageMultipleSizesInMemory ()
                                                      .build ();
            imageLoader = ImageLoader.getInstance ();
            imageLoader.init (config);
        }
        imageLoader.displayImage (userDetails.getPicUrl (), user_image);

        userNameAgeTF.setText (userDetails.getName () + " , " + userDetails.getAge ());
        String status;
        if (userDetails.getStatus () == null || userDetails.getStatus ().isEmpty ()) {
            status = "n/a";
        } else {
            status = userDetails.getStatus ();
        }
        currentStatusTF.setText (status);
        String height;
        if (userDetails.getHeight () == null || userDetails.getHeight ().isEmpty ()) {
            height = "n/a";
        } else {
            height = userDetails.getHeight ();
        }
        String weight;
        if (userDetails.getWeight () == null || userDetails.getWeight ().isEmpty ()) {
            weight = "n/a";
        } else {
            weight = userDetails.getWeight ();
        }
        heightWeightTF.setText (height + " | " + weight);
        String body_type;
        if (userDetails.getBody_type () == null || userDetails.getBody_type ().isEmpty ()) {
            body_type = "n/a";
        } else {
            body_type = StaticMethods.getProfileDetailsAsString (userDetails.getBody_type (),
                                                                        GlobalVariables.array_spinner_profile_Body_type,
                                                                        getResources ().getStringArray (R.array.bodyTypeSpinner));
        }
        String ethnicity;
        if (userDetails.getEthnicity () == null || userDetails.getEthnicity ().isEmpty ()) {
            ethnicity = "n/a";
        } else {
            ethnicity = StaticMethods.getProfileDetailsAsString (userDetails.getEthnicity (),
                                                                        GlobalVariables.array_spinner_filter_Ethnicity,
                                                                        getResources ().getStringArray (R.array.ethnicitySpinner));
        }
        bodyNationTF.setText (body_type + " | " + ethnicity);
        String relationship_status;
        if (userDetails.getRelationship_status () == null || userDetails.getRelationship_status ().isEmpty ()) {
            relationship_status = "n/a";
        } else {
            relationship_status = StaticMethods.getProfileDetailsAsString (userDetails.getRelationship_status (),
                                                                                  GlobalVariables.array_spinner_profile_Relationship_Status,
                                                                                  getResources ().getStringArray (R.array.relationshipSpinner));
        }
        String looking_for;
        if (userDetails.getLooking_for () == null || userDetails.getLooking_for ().isEmpty ()) {
            looking_for = "n/a";
        } else {
            looking_for = StaticMethods.getProfileDetailsAsString (userDetails.getLooking_for (),
                                                                          GlobalVariables.array_spinner_profile_Looking_for,
                                                                          getResources ().getStringArray (R.array.lookingForSpinner1));
        }
        relationSeekingTF.setText (relationship_status + " | " + looking_for);
        String about;
        if (userDetails.getAbout () == null || userDetails.getAbout ().isEmpty ()) {
            about = "n/a";
        } else {
            about = userDetails.getAbout ();
        }
        aboutTF.setText (about);
        if (GlobalVariables.isHeb) {
            currentStatusTF.setGravity (Gravity.RIGHT);
            userNameAgeTF.setGravity (Gravity.RIGHT);
            relationSeekingTF.setGravity (Gravity.RIGHT);
            bodyNationTF.setGravity (Gravity.RIGHT);
            heightWeightTF.setGravity (Gravity.RIGHT);
        } else {
            currentStatusTF.setGravity (Gravity.LEFT);
            userNameAgeTF.setGravity (Gravity.LEFT);
            relationSeekingTF.setGravity (Gravity.LEFT);
            bodyNationTF.setGravity (Gravity.LEFT);
            heightWeightTF.setGravity (Gravity.LEFT);
        }
    }

    public void hi(View view) {
        if (!StaticMethods.isGuestUser ()) {
            sendTrack (GlobalVariables.CUSTOMER_PHONE_NUM, userDetails.getUserPhoneNum (), "like");
            Toast.makeText (getApplicationContext (), getResources ().getString (R.string.likeTrack), Toast.LENGTH_SHORT).show ();
        } else {
            Toast.makeText (getApplicationContext (), getResources ().getString (R.string.pleaseProfile), Toast.LENGTH_SHORT).show ();
        }

    }

    public void like(View view) {
        if (!StaticMethods.isGuestUser ()) {
            sendTrack (GlobalVariables.CUSTOMER_PHONE_NUM, userDetails.getUserPhoneNum (), "love");
            Toast.makeText (getApplicationContext (), getResources ().getString (R.string.loveTrack), Toast.LENGTH_SHORT).show ();
        } else
            Toast.makeText (getApplicationContext (), getResources ().getString (R.string.pleaseProfile), Toast.LENGTH_SHORT).show ();

    }

    public void love(View view) {
        if (!StaticMethods.isGuestUser ()) {
            sendTrack (GlobalVariables.CUSTOMER_PHONE_NUM, userDetails.getUserPhoneNum (), "letsHang");
            Toast.makeText (getApplicationContext (), getResources ().getString (R.string.hangTrack), Toast.LENGTH_SHORT).show ();
        } else
            Toast.makeText (getApplicationContext (), getResources ().getString (R.string.pleaseProfile), Toast.LENGTH_SHORT).show ();

    }

    public void hot(View view) {
        if (!StaticMethods.isGuestUser ()) {
            sendTrack (GlobalVariables.CUSTOMER_PHONE_NUM, userDetails.getUserPhoneNum (), "sexy");
            Toast.makeText (getApplicationContext (), getResources ().getString (R.string.sexyTrack), Toast.LENGTH_SHORT).show ();
        } else
            Toast.makeText (getApplicationContext (), getResources ().getString (R.string.pleaseProfile), Toast.LENGTH_SHORT).show ();

    }

    public void sendTrack(final String sender, final String reciver, final String trackName) {

        ParseQuery query = new ParseQuery ("Track");
        query.whereEqualTo ("senderId", sender);
        query.whereEqualTo ("reciverId", reciver);
        query.getFirstInBackground (new GetCallback<ParseObject> () {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    try {
                        object.delete ();
                    } catch (ParseException e1) {
                        e1.printStackTrace ();
                    }
                    object.saveInBackground ();
                }
                saveTrack (sender, reciver, trackName);
            }
        });
    }


    public void saveTrack(String sender, final String reciver, String trackName) {
        Track track = new Track ();
        ParseACL parseAcl = new ParseACL ();
        parseAcl.setPublicReadAccess (true);
        parseAcl.setPublicWriteAccess (true);
        track.setACL (parseAcl);
        track.setTrackName (trackName);
        track.setReciverId (reciver);
        track.setSenderId (sender);
        track.saveInBackground ();
    }


}

package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
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
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DetailedProfileActivity extends Activity {

    TableRow akevot;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    List <Track> tracks;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.detailed_profile);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
        }

        Intent intent = getIntent ();
        ImageView user_image = (ImageView) findViewById (R.id.detailed_image);
        Boolean user_current = intent.getBooleanExtra("currentUser", false);
        int userIndex = intent.getIntExtra("index", 0);
        userDetails = GlobalVariables.userDataList.get(userIndex);
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
        options = new DisplayImageOptions.Builder ()
                          .cacheOnDisk (true)
                          .cacheInMemory (true)
                          .bitmapConfig (Bitmap.Config.RGB_565)
                          .imageScaleType (ImageScaleType.EXACTLY)
                          .resetViewBeforeLoading (true)
                          .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder (this)
                                                  .defaultDisplayImageOptions (options)
                                                  .threadPriority (Thread.MAX_PRIORITY)
                                                  .threadPoolSize (1)
                                                  .memoryCache (new WeakMemoryCache ())
                                                  .denyCacheImageMultipleSizesInMemory ()
                                                  .build ();
        imageLoader = ImageLoader.getInstance ();
        imageLoader.init(config);
        imageLoader.displayImage(userDetails.getPicUrl(), user_image);

        if(GlobalVariables.isHeb) {

            userNameAgeTF.setText( getResources().getStringArray(R.array.userNames1)[userIndex] + " , " + userDetails.getAge());
            currentStatusTF.setText (getResources().getStringArray(R.array.userStatus)[userIndex]);
            Random r = new Random();
            int i1 = (r.nextInt(getResources().getStringArray(R.array.bodyTypeSpinner).length - 1) + 0);
            int i2 = (r.nextInt(getResources().getStringArray(R.array.originSpinner).length - 1) + 0);
            int i3 = (r.nextInt(getResources().getStringArray(R.array.lookingForSpinner1).length - 1) + 0);
            int i4 = (r.nextInt(getResources().getStringArray(R.array.relationshipSpinner).length - 1) + 0);
            bodyNationTF.setText ((getResources().getStringArray(R.array.bodyTypeSpinner)[i1] + " | " + (getResources().getStringArray(R.array.originSpinner)[i2])));
            relationSeekingTF.setText (getResources().getStringArray(R.array.lookingForSpinner1)[i3] + " | " + getResources().getStringArray(R.array.relationshipSpinner)[i4]);

        }
        else
        {
            userNameAgeTF.setText (userDetails.getName () + " , " + userDetails.getAge ());
            currentStatusTF.setText (userDetails.getStatus ());
            bodyNationTF.setText (userDetails.getBody_type() + " | " + userDetails.getNation ());
            relationSeekingTF.setText (userDetails.getRelationship_status () + " | " + userDetails.getLooking_for ());
        }
        heightWeightTF.setText (userDetails.getHeight () + " | " + userDetails.getWeight ());
        aboutTF.setText (userDetails.getAbout ());

    }

    public void like(View view) {
        if (!StaticMethods.isGuestUser ()) {
            deleteTrack(ParseUser.getCurrentUser().getObjectId(),userDetails.getUserId(),getResources().getString(R.string.like));
            Toast.makeText (getApplicationContext (), getResources().getString(R.string.likeTrack), Toast.LENGTH_SHORT).show ();
        } else {
            Toast.makeText(getApplicationContext(),  getResources().getString(R.string.createProfile), Toast.LENGTH_SHORT).show();
        }

    }

    public void love(View view) {
        if (!StaticMethods.isGuestUser ()) {
            deleteTrack(ParseUser.getCurrentUser().getObjectId(),userDetails.getUserId(),getResources().getString(R.string.love));
            Toast.makeText(getApplicationContext(),  getResources().getString(R.string.loveTrack), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText (getApplicationContext (), getResources().getString(R.string.createProfile), Toast.LENGTH_SHORT).show ();

    }

    public void letsHang(View view) {
        if (!StaticMethods.isGuestUser ()) {
            deleteTrack(ParseUser.getCurrentUser().getObjectId(),userDetails.getUserId(),getResources().getString(R.string.letsHang));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.hangTrack), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText (getApplicationContext (), getResources().getString(R.string.createProfile), Toast.LENGTH_SHORT).show ();

    }

    public void sexy(View view) {
        if (!StaticMethods.isGuestUser ()) {
            deleteTrack(ParseUser.getCurrentUser().getObjectId(),userDetails.getUserId(),getResources().getString(R.string.sexy));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sexyTrack), Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText (getApplicationContext (), getResources().getString(R.string.createProfile), Toast.LENGTH_SHORT).show ();

    }

    public void deleteTrack(final String sender, final String reciver, final String trackName){

        ParseQuery query = new ParseQuery("Track");
        query.whereEqualTo("senderId", sender);
        query.whereEqualTo("reciverId", reciver);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    try {
                        object.delete();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                    object.saveInBackground();
                }

                saveTrack(sender, reciver, trackName);
            }
        });


    }


    public void saveTrack(String sender, final String reciver,String trackName){


        Track track = new Track();
        ParseACL parseAcl = new ParseACL();
        parseAcl.setPublicReadAccess(true);
        parseAcl.setPublicWriteAccess(true);
        track.setACL(parseAcl);
        track.setName(trackName);
        track.setReciverId(reciver);
        track.setSenderId(sender);
        track.setSenderName(GlobalVariables.currentUser.getName());
        track.setSenderPicUrl(GlobalVariables.currentUser.getPicUrl());
        track.saveInBackground();


    }



}

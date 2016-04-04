package com.example.mipo;

import android.Manifest.permission;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class StaticMethods {
    //in millis
    private static final int GPS_UPDATE_TIME_INTERVAL = 10000;

    public static LocationManager locationManager;
    public static LocationListener locationListener;

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt (context.getContentResolver (), Settings.Secure.LOCATION_MODE);
            } catch (SettingNotFoundException e) {
                e.printStackTrace ();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString (context.getContentResolver (), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty (locationProviders);
        }
    }

    public static int getOrientation(Uri selectedImage, Context context) {
        int orientation = 0;
        final String[] projection = new String[]{MediaStore.Images.Media.ORIENTATION};
        final Cursor cursor = context.getContentResolver ().query (selectedImage, projection, null, null, null);
        if (cursor != null) {
            final int orientationColumnIndex = cursor.getColumnIndex (MediaStore.Images.Media.ORIENTATION);
            if (cursor.moveToFirst ()) {
                orientation = cursor.isNull (orientationColumnIndex) ? 0 : cursor.getInt (orientationColumnIndex);
            }
            cursor.close ();
        }
        return orientation;
    }

    public interface GpsICallback {
        void gpsCallback();
    }

    public static void updateDeviceLocationGPS(Context context, GpsICallback iCallback) {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        boolean passive_enabled = false;

        locationManager = (LocationManager) context.getSystemService (Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener (iCallback, context);
        try {
            gps_enabled = locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
        try {
            network_enabled = locationManager.isProviderEnabled (LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
        try {
            passive_enabled = locationManager.isProviderEnabled (LocationManager.PASSIVE_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace ();
        }
        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission (context, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (context, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //do none
            } else {
                locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, GPS_UPDATE_TIME_INTERVAL, 0, locationListener);
            }
        }
        if (network_enabled) {
            locationManager.requestLocationUpdates (LocationManager.NETWORK_PROVIDER, GPS_UPDATE_TIME_INTERVAL, 0, locationListener);
        }
        if (passive_enabled) {
            locationManager.requestLocationUpdates (LocationManager.PASSIVE_PROVIDER, GPS_UPDATE_TIME_INTERVAL, 0, locationListener);
        }
    }

    private static class MyLocationListener implements LocationListener {
        GpsICallback ic;
        Context context;

        MyLocationListener(GpsICallback iCallback, Context context) {
            ic = iCallback;
            this.context = context;
        }

        @Override
        public void onLocationChanged(final Location location) {
            if (location != null) {
                GlobalVariables.MY_LOCATION = location;
                GlobalVariables.LOCATION_ACCURACY = location.getAccuracy ();
                if (!isGuestUser ()) {
                    ParseQuery<Profile> query = ParseQuery.getQuery ("Profile");
                    query.whereEqualTo ("number", GlobalVariables.CUSTOMER_PHONE_NUM);
                    query.findInBackground (new FindCallback<Profile> () {
                        public void done(List<Profile> list, ParseException e) {
                            if (e == null) {
                                Profile profile = list.get (0);
                                ParseGeoPoint parseGeoPoint = new ParseGeoPoint (location.getLatitude (),
                                                                                        location.getLongitude ());
                                profile.setLocation (parseGeoPoint);
                                profile.saveInBackground ();
                            } else {
                                e.printStackTrace ();
                            }
                        }
                    });
                }
                ic.gpsCallback ();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    public static boolean isCurrentUser(UserDetails userDetails) {
        if (GlobalVariables.CUSTOMER_PHONE_NUM != null &&
                    !GlobalVariables.CUSTOMER_PHONE_NUM.isEmpty ()) {
            if (userDetails.getUserPhoneNum ().equals (GlobalVariables.CUSTOMER_PHONE_NUM)) {
                return true;
            }
        } else {
            if (userDetails.getUserPhoneNum ().equals ("GUEST")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isGuestUser() {
        return GlobalVariables.CUSTOMER_PHONE_NUM == null || GlobalVariables.CUSTOMER_PHONE_NUM.isEmpty ();
    }

    public static UserDetails getUserFromPhoneNum(String phone) {
        for (UserDetails userDetails : GlobalVariables.userDataList) {
            if (userDetails.getUserPhoneNum ().equals (phone)) {
                return userDetails;
            }
        }
        return null;
    }

    public static String getTimeLastSeenToShow(Date date) {
        Date currentDate = new Date ();
        long diff = currentDate.getTime () - date.getTime ();
        long diffMinutes = diff / (60 * 1000);
        if (diffMinutes < 60) {
            return "Seen " + diffMinutes + " min ago";
        } else {
            long diffHours = diff / (60 * 60 * 1000);
            if (diffHours < 24) {
                return "Seen " + diffHours + " hour ago";
            } else {
                long diffDays = diff / (24 * 60 * 60 * 1000);
                return "Seen " + diffDays + " days ago";
            }
        }
    }

    public static String getDistanceToShow(double distance) {
        if (distance < 1000) {
            int distanceInt = (int) distance;
            return distanceInt + " meters away";
        } else {
            DecimalFormat df = new DecimalFormat ("#.##");
            String dx = df.format (distance / 1000.0);
            double formatedDistance = Double.valueOf (dx);
            return formatedDistance + " km away";
        }
    }

    public static String getTimeLastSeenToShowHeb(Date date) {
        Date currentDate = new Date ();
        long diff = currentDate.getTime () - date.getTime ();
        long diffMinutes = diff / (60 * 1000);
        if (diffMinutes < 60) {
            return "נראה לפני " + diffMinutes + " דקות";
        } else {
            long diffHours = diff / (60 * 60 * 1000);
            if (diffHours < 24) {
                return "נראה לפני " + diffHours + " שעות";
            } else {
                long diffDays = diff / (24 * 60 * 60 * 1000);
                return "נראה לפני " + diffHours + " ימים";
            }
        }
    }

    public static String getDistanceToShowHeb(double distance) {
        if (distance < 1000) {
            int distanceInt = (int) distance;
            return distanceInt + " מטרים ממך";
        } else {
            DecimalFormat df = new DecimalFormat ("#.##");
            String dx = df.format (distance / 1000.0);
            double formatedDistance = Double.valueOf (dx);
            return formatedDistance + " קמ ממך";
        }
    }

    public static void initFilterValues() {
        GlobalVariables.array_spinner_filter_Looking_for = new String[7];
        GlobalVariables.array_spinner_filter_Looking_for[0] = "All";
        GlobalVariables.array_spinner_filter_Looking_for[1] = "Chat";
        GlobalVariables.array_spinner_filter_Looking_for[2] = "Dates";
        GlobalVariables.array_spinner_filter_Looking_for[3] = "Friends";
        GlobalVariables.array_spinner_filter_Looking_for[4] = "Networking";
        GlobalVariables.array_spinner_filter_Looking_for[5] = "Relationship";
        GlobalVariables.array_spinner_filter_Looking_for[6] = "Right Now";

        GlobalVariables.array_spinner_filter_Body_type = new String[6];
        GlobalVariables.array_spinner_filter_Body_type[0] = "All";
        GlobalVariables.array_spinner_filter_Body_type[1] = "Slim";
        GlobalVariables.array_spinner_filter_Body_type[2] = "Toned";
        GlobalVariables.array_spinner_filter_Body_type[3] = "Average";
        GlobalVariables.array_spinner_filter_Body_type[4] = "Large";
        GlobalVariables.array_spinner_filter_Body_type[5] = "Stocky";

        GlobalVariables.array_spinner_filter_Ethnicity = new String[9];
        GlobalVariables.array_spinner_filter_Ethnicity[0] = "All";
        GlobalVariables.array_spinner_filter_Ethnicity[1] = "Middle Eastern";
        GlobalVariables.array_spinner_filter_Ethnicity[2] = "Native American";
        GlobalVariables.array_spinner_filter_Ethnicity[3] = "Black";
        GlobalVariables.array_spinner_filter_Ethnicity[4] = "Latino";
        GlobalVariables.array_spinner_filter_Ethnicity[5] = "Mixed";
        GlobalVariables.array_spinner_filter_Ethnicity[6] = "Other";
        GlobalVariables.array_spinner_filter_Ethnicity[7] = "South Asian";
        GlobalVariables.array_spinner_filter_Ethnicity[8] = "White";

        GlobalVariables.array_spinner_filter_Relationship_Status = new String[9];
        GlobalVariables.array_spinner_filter_Relationship_Status[0] = "All";
        GlobalVariables.array_spinner_filter_Relationship_Status[1] = "Committed";
        GlobalVariables.array_spinner_filter_Relationship_Status[2] = "Dating";
        GlobalVariables.array_spinner_filter_Relationship_Status[3] = "Engaged";
        GlobalVariables.array_spinner_filter_Relationship_Status[4] = "Exclusive";
        GlobalVariables.array_spinner_filter_Relationship_Status[5] = "Married";
        GlobalVariables.array_spinner_filter_Relationship_Status[6] = "Open Relationship";
        GlobalVariables.array_spinner_filter_Relationship_Status[7] = "Partnered";
        GlobalVariables.array_spinner_filter_Relationship_Status[8] = "Single";
    }

    public static void initProfileValues() {
        GlobalVariables.array_spinner_profile_Looking_for = new String[7];
        GlobalVariables.array_spinner_profile_Looking_for[0] = "Do Not Show";
        GlobalVariables.array_spinner_profile_Looking_for[1] = "Chat";
        GlobalVariables.array_spinner_profile_Looking_for[2] = "Dates";
        GlobalVariables.array_spinner_profile_Looking_for[3] = "Friends";
        GlobalVariables.array_spinner_profile_Looking_for[4] = "Networking";
        GlobalVariables.array_spinner_profile_Looking_for[5] = "Relationship";
        GlobalVariables.array_spinner_profile_Looking_for[6] = "Right Now";

        GlobalVariables.array_spinner_profile_Body_type = new String[6];
        GlobalVariables.array_spinner_profile_Body_type[0] = "Do Not Show";
        GlobalVariables.array_spinner_profile_Body_type[1] = "Slim";
        GlobalVariables.array_spinner_profile_Body_type[2] = "Toned";
        GlobalVariables.array_spinner_profile_Body_type[3] = "Average";
        GlobalVariables.array_spinner_profile_Body_type[4] = "Large";
        GlobalVariables.array_spinner_profile_Body_type[5] = "Stocky";

        GlobalVariables.array_spinner_profile_Ethnicity = new String[9];
        GlobalVariables.array_spinner_profile_Ethnicity[0] = "Do Not Show";
        GlobalVariables.array_spinner_profile_Ethnicity[1] = "Middle Eastern";
        GlobalVariables.array_spinner_profile_Ethnicity[2] = "Native American";
        GlobalVariables.array_spinner_profile_Ethnicity[3] = "Black";
        GlobalVariables.array_spinner_profile_Ethnicity[4] = "Latino";
        GlobalVariables.array_spinner_profile_Ethnicity[5] = "Mixed";
        GlobalVariables.array_spinner_profile_Ethnicity[6] = "Other";
        GlobalVariables.array_spinner_profile_Ethnicity[7] = "South Asian";
        GlobalVariables.array_spinner_profile_Ethnicity[8] = "White";

        GlobalVariables.array_spinner_profile_Relationship_Status = new String[9];
        GlobalVariables.array_spinner_profile_Relationship_Status[0] = "Do Not Show";
        GlobalVariables.array_spinner_profile_Relationship_Status[1] = "Committed";
        GlobalVariables.array_spinner_profile_Relationship_Status[2] = "Dating";
        GlobalVariables.array_spinner_profile_Relationship_Status[3] = "Engaged";
        GlobalVariables.array_spinner_profile_Relationship_Status[4] = "Exclusive";
        GlobalVariables.array_spinner_profile_Relationship_Status[5] = "Married";
        GlobalVariables.array_spinner_profile_Relationship_Status[6] = "Open Relationship";
        GlobalVariables.array_spinner_profile_Relationship_Status[7] = "Partnered";
        GlobalVariables.array_spinner_profile_Relationship_Status[8] = "Single";
    }

    public static String getProfileDetailsAsString(String detail,
                                                   String[] source,
                                                   String[] displayStringArray) {
        for (int i = 0; i < displayStringArray.length; i++) {
            if (source[i].equals (detail)) {
                return displayStringArray[i];
            }
        }
        return null;
    }
}

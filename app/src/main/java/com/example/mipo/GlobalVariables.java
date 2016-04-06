package com.example.mipo;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariables {
    public static String CUSTOMER_PHONE_NUM = null;
    public static Location MY_LOCATION = null;
    public static float LOCATION_ACCURACY;
    public static int Last_SHOWED_LOCATION_ACCURACY;
    public static List<UserDetails> userDataList = new ArrayList<UserDetails> ();
    public static UserDetails currentUser;

    public static String array_spinner_profile_Looking_for[];
    public static String array_spinner_profile_Body_type[];
    public static String array_spinner_profile_Ethnicity[];
    public static String array_spinner_profile_Relationship_Status[];

    public static String array_spinner_filter_Looking_for[];
    public static String array_spinner_filter_Body_type[];
    public static String array_spinner_filter_Ethnicity[];
    public static String array_spinner_filter_Relationship_Status[];

    public static boolean isHeb = false;
    public static String userPhoneNumFromFundigo = null;

    public static String pushAction = "";
    public static String pushUserName = "";
    public static String pushIndex = "";
    public static String ChatWith = "";
    public static int messagesCounter = 0;
    public static boolean stopLoadingMessages = false;




}

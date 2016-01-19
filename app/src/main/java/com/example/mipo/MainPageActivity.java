package com.example.mipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.parse.ParseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainPageActivity extends Activity implements AdapterView.OnItemClickListener {

    GridView grid;
    public final static List<User> firstUsersList = new ArrayList<User> ();
    public final static List<User> filteredUsersList = new ArrayList<User> ();
    public static List<UserDetails> userDataList = new ArrayList<UserDetails> ();
    static int conversationId;
    static int otherConversationId;
    static UserDetails currentUser;
    static int currUserIndex;
    static boolean didInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.activity_main_page);
        grid = (GridView) findViewById (R.id.gridView1);
        if (!didInit) {
            uploadUserData ();
            addToList ();
            didInit = true;
            firstUsersList.addAll (filteredUsersList);
        }
        grid.setAdapter (new GridAdaptor (this, filteredUsersList));
        grid.setOnItemClickListener (this);
    }

    private void uploadUserData() {
        InputStream is;
        BufferedReader input;
        List<String> list;
        boolean newUser = true;
        for (int i = 0; i <= 26; i++) {
            is = this.getResources ().openRawResource (R.raw.user0 + i);
            input = new BufferedReader (new InputStreamReader (is), 1024 * 8);
            String line;
            list = new ArrayList<String> ();
            try {
                int j = 0;
                while ((line = input.readLine ()) != null) {
                    if (j % 2 == 0)
                        list.add (line);
                    j++;
                }
            } catch (IOException e) {
                e.printStackTrace ();
            }
            if (ParseUser.getCurrentUser ().getObjectId ().equals (list.get (1))) {
                userDataList.add (new UserDetails (list.get (0), list.get (1), list.get (2), "0", list.get (4), list.get (5), list.get (6),
                                                          list.get (7), list.get (8), list.get (9), list.get (10), list.get (11),
                                                          list.get (12), list.get (13), Integer.parseInt (list.get (14)),
                                                          Integer.parseInt (list.get (15)), Integer.parseInt (list.get (16))));
            } else {
                userDataList.add (new UserDetails (list.get (0), list.get (1), list.get (2), list.get (3), list.get (4), list.get (5), list.get (6),
                                                          list.get (7), list.get (8), list.get (9), list.get (10), list.get (11),
                                                          list.get (12), list.get (13), Integer.parseInt (list.get (14)),
                                                          Integer.parseInt (list.get (15)), Integer.parseInt (list.get (16))));
            }

        }

        //Sorting
        Collections.sort (userDataList, new Comparator<UserDetails> () {
            @Override
            public int compare(UserDetails user1, UserDetails user2) {
                Double dist1, dist2;
                if (user1.getDistanceType () == 0) {
                    dist1 = Double.parseDouble (user1.getDistance ());
                } else {
                    dist1 = Double.parseDouble (user1.getDistance ()) * 1000.0;
                }
                if (user2.getDistanceType () == 0) {
                    dist2 = Double.parseDouble (user2.getDistance ());
                } else {
                    dist2 = Double.parseDouble (user2.getDistance ()) * 1000.0;
                }
                if (dist1 > dist2) {
                    return 1;
                } else if (dist1 < dist2) {
                    return -1;
                }
                return 0;
            }
        });

        for (int i = 0; i <= 26; i++) {
            if (ParseUser.getCurrentUser ().getObjectId ().equals (userDataList.get (i).getId ())) {
                currUserIndex = i;
                conversationId = userDataList.get (i).getMessage_roomId ();
                newUser = false;
                currentUser = userDataList.get (i);
                break;
            }
        }

        if (newUser) {
            Random r = new Random ();
            int Low = 0;
            int High = 27;
            currUserIndex = r.nextInt (High - Low) + Low;
            UserDetails userDetails = userDataList.get (currUserIndex);
            userDataList.remove (currUserIndex);
            userDataList.add (0, userDetails);
            currentUser = userDataList.get (0);
            conversationId = userDataList.get (0).getMessage_roomId ();
            currentUser.setDistance ("0");
            currentUser.setDistanceType (0);
        }

        for (int i = 7; i < 12; i++) {
            if (i != currUserIndex) {
                userDataList.get (i).setFavorite (true);
            }
        }
    }

    public static void addToList() {
        filteredUsersList.add (new User (R.drawable.me0 + currentUser.getImage_source (),
                                                userDataList.get (currUserIndex).getName (), 0,
                                                true, userDataList.get (currUserIndex).id, currUserIndex
                                                , userDataList.get (0)));
        filteredUsersList.get (0).getUserDetails ().setIsFilteredOK (true);
        for (int i = 1; i <= 26; i++) {
            if (userDataList.get (i).getOn_off ().equals ("Online"))
                filteredUsersList.add (new User (R.drawable.pic0 + userDataList.get (i).getImage_source (),
                                                        userDataList.get (i).getName (),
                                                        R.drawable.online, false, userDataList.get (i).id, i,
                                                        userDataList.get (i)));
            else {
                filteredUsersList.add (new User (R.drawable.pic0 + userDataList.get (i).getImage_source (),
                                                        userDataList.get (i).getName (),
                                                        0, false, userDataList.get (i).id, i
                                                        , userDataList.get (i)));
            }
            filteredUsersList.get (i).getUserDetails ().setIsFilteredOK (true);
        }
    }

    public static User getUser(int i) {
        return filteredUsersList.get (i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Bundle b = new Bundle ();
        Intent intent = new Intent (this, UserPage.class);
        User user = filteredUsersList.get (position);
        intent.putExtra ("userImage", user.imageId);
        intent.putExtra ("userName", user.name);
        intent.putExtra ("userCurrent", user.currentUser);
        b.putString ("userID", filteredUsersList.get (position).id);
        b.putInt ("index", user.getIndexInUD ());
        b.putInt ("online", user.on_off);
        intent.putExtras (b);
        startActivity (intent);
    }

    public void goToMessages(View view) {
        Intent intent = new Intent (this, MessagesRoom.class);
        Bundle b = new Bundle ();
        b.putInt ("otherConvId", otherConversationId);
        startActivity (intent);
    }

    public void goToFavorites(View view) {
        Intent i = new Intent (this, FavoritesPage.class);
        startActivity (i);
    }

    public void goToFilter(View v) {
        Intent intent = new Intent (this, FilterActivity.class);
        startActivity (intent);
    }


//    public static void ref()
//    {
//        GridAdaptor gridAdaptor = (GridAdaptor)grid.getAdapter ();
//        gridAdaptor.notifyDataSetChanged ();
//    }
}

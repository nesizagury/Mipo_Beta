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
import java.util.List;
import java.util.Random;

public class MainPageActivity extends Activity implements AdapterView.OnItemClickListener {

    GridView grid;
    public final static List<User> list = new ArrayList<User> ();
    public final static ListOfFavorites lov = new ListOfFavorites ();
    public final static List<UserDetails> ud = new ArrayList<UserDetails> ();
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
        }
        grid.setAdapter (new GridAdaptor (this, list));
        grid.setOnItemClickListener (this);
    }

    private void uploadUserData() {
        InputStream is;
        BufferedReader input;
        List<String> list;
        boolean newUser = true;
        for (int i = 0; i < 15; i++) {
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
            ud.add (new UserDetails (list.get (0), list.get (1), list.get (2), list.get (3), list.get (4), list.get (5), list.get (6),
                                            list.get (7), list.get (8), list.get (9), list.get (10), list.get (11), list.get (12), list.get (13), Integer.parseInt (list.get (14)), Integer.parseInt (list.get (15))));
            if (ParseUser.getCurrentUser ().getObjectId ().equals (ud.get (i).getId ())) {
                currUserIndex = i;
                conversationId = ud.get (i).getMessage_roomId ();
                newUser = false;
                currentUser = new UserDetails (list.get (0), list.get (1), list.get (2), list.get (3), list.get (4), list.get (5), list.get (6),
                                                      list.get (7), list.get (8), list.get (9), list.get (10), list.get (11), list.get (12), list.get (13), Integer.parseInt (list.get (14)), Integer.parseInt (list.get (15)));
            }
        }
        if (newUser) {
            Random r = new Random ();
            int Low = 0;
            int High = 15;
            int Result = r.nextInt (High - Low) + Low;
            conversationId = ud.get (Result).getMessage_roomId ();
            is = this.getResources ().openRawResource (R.raw.user0 + Result);
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
            currentUser = new UserDetails (list.get (0), list.get (1), list.get (2), list.get (3), list.get (4), list.get (5), list.get (6),
                                                  list.get (7), list.get (8), list.get (9), list.get (10), list.get (11), list.get (12), list.get (13), Integer.parseInt (list.get (14)), Integer.parseInt (list.get (15)));
            currUserIndex = Result;
        }
    }

    public List addToList() {
        list.add (new User (R.drawable.me0 + currUserIndex, ud.get (currUserIndex).getName (), 0, true, ud.get (currUserIndex).id));

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 15; i++) {
                if (ud.get (i).getMessage_roomId () != conversationId) {
                    if (ud.get (i).getOn_off ().equals ("Online"))
                        list.add (new User (R.drawable.pic0 + i, ud.get (i).getName (), R.drawable.online, false, ud.get (i).id));
                    else
                        list.add (new User (R.drawable.pic0 + i, ud.get (i).getName (), 0, false, ud.get (i).id));
                }
            }
            for (int i = 0; i < 13; i++) {
                if (ud.get (i).getMessage_roomId () != conversationId) {
                    if (ud.get (i).getOn_off ().equals ("Online"))
                        list.add (new User (R.drawable.i0 + i, ud.get (i).getName (), R.drawable.online, false, ud.get (i).id));
                    else
                        list.add (new User (R.drawable.i0 + i, ud.get (i).getName (), 0, false, ud.get (i).id));
                }
            }
        }
        return list;
    }

    public static User getUser(int i) {
        return list.get (i);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Bundle b = new Bundle ();
        Intent intent = new Intent (this, UserPage.class);
        User user = list.get (position);
        intent.putExtra ("userImage", user.imageId);
        intent.putExtra ("userName", user.name);
        intent.putExtra ("userCurrent", user.currentUser);
        b.putString ("userID", list.get (position).id);
        b.putInt ("index", position);
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

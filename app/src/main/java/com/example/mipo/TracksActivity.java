package com.example.mipo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class TracksActivity extends Fragment {

    private ListView list_view;
    private ArrayList<TrackItem> tracksItemArrayList;
    private TracksAdapter tracksAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.activity_tracks, container, false);
        list_view = (ListView) rootView.findViewById (R.id.tracksListView);
        tracksItemArrayList = new ArrayList<TrackItem> ();
        tracksAdapter = new TracksAdapter (getActivity ().getApplicationContext (), tracksItemArrayList);
        list_view.setAdapter (tracksAdapter);
        loadTracks ();
        return rootView;
    }

    void loadTracks() {
        ParseQuery query = new ParseQuery ("Track");
        query.whereEqualTo ("reciverId", GlobalVariables.CUSTOMER_PHONE_NUM);
        query.orderByDescending ("updatedAt");
        List<Track> tracks = null;
        List<TrackItem> items = new ArrayList<TrackItem> ();

        try {
            tracks = query.find ();
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        for (int i = 0; i < tracks.size (); i++) {
            for (int j = 0; j < GlobalVariables.userDataList.size (); j++) {
                if (GlobalVariables.userDataList.get (j).getUserPhoneNum ().equals (tracks.get (i).getSenderId ())) {
                    UserDetails userDetails = GlobalVariables.userDataList.get (j);
                    items.add (new TrackItem (userDetails.getName (),
                                                     tracks.get (i).getTrackName (),
                                                     userDetails.getPicUrl (),
                                                     tracks.get (i).getSenderId ()));
                }
            }
        }
        tracksItemArrayList.clear ();
        tracksItemArrayList.addAll (items);
        tracksAdapter.notifyDataSetChanged ();
    }

    void loadTracksInBackGround() {
        ParseQuery query = new ParseQuery ("Track");
        query.whereEqualTo ("reciverId", GlobalVariables.CUSTOMER_PHONE_NUM);
        query.orderByDescending ("updatedAt");
        query.findInBackground (new FindCallback<Track> () {
            public void done(List<Track> tracks, ParseException e) {
                if (e == null) {
                    List<TrackItem> items = new ArrayList<TrackItem> ();
                    for (int i = 0; i < tracks.size (); i++) {
                        for (int j = 0; j < GlobalVariables.userDataList.size (); j++) {
                            if (GlobalVariables.userDataList.get (j).getUserPhoneNum ().equals (tracks.get (i).getSenderId ())) {
                                UserDetails userDetails = GlobalVariables.userDataList.get (j);
                                items.add (new TrackItem (userDetails.getName (),
                                                                 tracks.get (i).getTrackName (), userDetails.getPicUrl (), tracks.get (i).getSenderId ()));
                            }
                        }
                    }
                    tracksItemArrayList.clear ();
                    tracksItemArrayList.addAll (items);
                    tracksAdapter.notifyDataSetChanged ();
                } else {
                    e.printStackTrace ();
                }
            }
        });
    }
}

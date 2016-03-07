package com.example.mipo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by מנהל on 28/02/2016.
 */
public class TracksActivity extends Fragment {

    private ListView list_view;
    private ArrayList<TrackItem> tracksItemArrayList;
    private TracksAdapter tracksAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.activity_tracks, container, false);
        list_view = (ListView) rootView.findViewById(R.id.tracksListView);
        tracksItemArrayList = new ArrayList<TrackItem> ();
        tracksAdapter = new TracksAdapter (getActivity ().getApplicationContext (), tracksItemArrayList);
        list_view.setAdapter(tracksAdapter);
        loadTracks();
        return rootView;
    }

    private void loadTracks() {

        ParseQuery query = new ParseQuery("Track");
        query.whereEqualTo("reciverId", ParseUser.getCurrentUser().getObjectId());
        query.orderByDescending("updatedAt");
        List <Track> tracks = null;
        List<TrackItem> items = new ArrayList<TrackItem>();

        try {
            tracks = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tracks.size(); i++) {
            for (int j = 0; j < GlobalVariables.userDataList.size(); j++) {
                if(GlobalVariables.userDataList.get(j).getUserId().equals(tracks.get(i).getSenderId())){
                    items.add(new TrackItem(tracks.get(i).getSenderName(),
                            tracks.get(i).getName(),tracks.get(i).getSenderPicUrl()));
                }
            }


        }

        tracksItemArrayList.addAll(items);
        tracksAdapter.notifyDataSetChanged();

    }

}

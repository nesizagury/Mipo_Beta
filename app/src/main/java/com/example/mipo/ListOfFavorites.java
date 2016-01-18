package com.example.mipo;


import java.util.ArrayList;
import java.util.List;

public class ListOfFavorites {

    static final List<User> favorites_list = new ArrayList<User> ();

    public void addToFavorites_list(User user) {
        for (int i = 0; i < favorites_list.size (); i++) {
            if (favorites_list.get (i) == user) {
                return;
            }
        }
        favorites_list.add (user);
    }


    public List<User> getFavorites_list() {
        return favorites_list;
    }

}

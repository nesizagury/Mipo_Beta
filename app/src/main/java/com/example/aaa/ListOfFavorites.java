package com.example.aaa;


import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListOfFavorites {

    static List <User> favorites_list = new ArrayList <User> ();

    public int addToFavorites_list(User user){

        for (int i = 0; i < favorites_list.size(); i++)
        {
            if (favorites_list.get(i) == user) {
                return 0;
            }
        }
        favorites_list.add(user);

        return 0;
    }


    public List getFavorites_list(){

        return favorites_list;

    }


}

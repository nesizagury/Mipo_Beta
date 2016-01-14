package com.example.mipo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class Adapts extends BaseAdapter {

    private final List<User> list;
    private Context context;

    public Adapts(Context c, List<User> list) {
        this.context = c;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size ();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        View row = inflator.inflate (R.layout.grid_item, viewGroup, false);
        Holder vholder = new Holder (row);
        row.setTag (vholder);
        User user = list.get (i);
        if (!user.isCurentUser()) {
            vholder.image.setBackgroundResource (0);
            vholder.image.setPadding (0,0,0,0);
        }
        vholder.image.setImageResource (user.imageId);
        vholder.image2.setImageResource (user.on_off);
        vholder.name.setText (user.name);
        vholder.image.setTag (user);
        vholder.name.setTag (user);
        return row;
    }
}

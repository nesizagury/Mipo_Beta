package com.example.mipo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class MessagesRoomAdapter extends ArrayAdapter<MessageRoomBean> {


    public MessagesRoomAdapter (Context c, List <MessageRoomBean> list) {
        super(c, 0, list);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            view = LayoutInflater.from(getContext()).
                                                                   inflate (R.layout.messages_room_item, viewGroup, false);
            final MessageItemHolder m_holder = new MessageItemHolder(view);
            view.setTag(m_holder);

        }
        final MessageRoomBean message_bean = (MessageRoomBean)getItem (i);
        final MessageItemHolder holder = (MessageItemHolder)view.getTag();

        holder.image.setImageResource(message_bean.getImageId ());
        holder.body.setText (message_bean.getBody ());
        holder.name.setText(message_bean.getName ());

        holder.image.setTag(message_bean);
        holder.name.setTag(message_bean);
        holder.body.setTag(message_bean);

        return view;

    }






}

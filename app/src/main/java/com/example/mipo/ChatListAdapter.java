package com.example.mipo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class ChatListAdapter extends ArrayAdapter<Message> {


    Context context;
    int otherUserIndex;

    public ChatListAdapter(Context context, int otherUserIndex, List<Message> messages) {
        super (context, 0, messages);
        this.otherUserIndex = otherUserIndex;
        this.context = context;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from (getContext ()).inflate (R.layout.chat_item, parent, false);
            final ViewHolder holder = new ViewHolder ();
            holder.imageV = (ImageView) convertView.findViewById (R.id.vIV);
            holder.body = (TextView) convertView.findViewById (R.id.tvBody);
            convertView.setTag (holder);
        }
        final Message message = (Message) getItem (position);
        final ViewHolder holder = (ViewHolder) convertView.getTag ();
        final boolean isMe = message.getSenderId ().equals (GlobalVariables.CUSTOMER_PHONE_NUM);
        if (isMe) {
            if(message.getSeen())
                holder.imageV.setImageResource(R.drawable.green_v);
            else
            if(message.getPending())
                holder.imageV.setImageResource(R.drawable.two_v);
            else
                holder.imageV.setImageResource(R.drawable.v);

            holder.imageV.setVisibility(View.VISIBLE);
            holder.body.setGravity (Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            UserDetails userDetails = GlobalVariables.userDataList.get (otherUserIndex);
            holder.imageV.setVisibility (View.GONE);
            holder.body.setGravity (Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        holder.body.setText (message.getMessageBody ());
        return convertView;
    }

    class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public ImageView imageV;
        public TextView body;
    }




}
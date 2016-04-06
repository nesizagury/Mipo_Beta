package com.example.mipo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mipo.chat.ChatActivity;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Date;
import java.util.List;

public class TracksAdapter extends ArrayAdapter<TrackItem> {

    List<TrackItem> list;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    int pixels;
    Context c;
    Button profileButton;
    Button messageButton;
    String id;

    public TracksAdapter(Context c, List<TrackItem> list) {
        super (c, 0, list);
        this.list = list;
        this.c = c;
        if (imageLoader == null || (imageLoader != null && !imageLoader.isInited ())) {
            setImageLoader ();
        }
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from (getContext ()).inflate (R.layout.track_item, viewGroup, false);
            final TrackItemHolder m_holder = new TrackItemHolder (view);
            view.setTag (m_holder);
        }
        final TrackItem trackItem = (TrackItem) getItem (pos);
        final TrackItemHolder holder = (TrackItemHolder) view.getTag ();
        final UserDetails userDetails = StaticMethods.getUserFromPhoneNum (trackItem.getSenderUserPhone ());

        if (list.get (pos).picUrl != null) {
            imageLoader.displayImage (list.get (pos).picUrl, holder.image);
        } else {
            holder.image.setImageResource (R.drawable.favorite);
        }
        String trackLine = getTrackLine (trackItem.getTrackName ());

        holder.name.setText (userDetails.getName () + " " + trackLine);
        holder.image.setTag (trackItem);
        holder.name.setTag (trackItem);

        profileButton = (Button) view.findViewById (R.id.profileButton);
        profileButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                switch (v.getId ()) {
                    case R.id.profileButton:
                        Bundle b = new Bundle ();
                        Intent intent = new Intent (c, UserPage.class);
                        UserDetails user = userDetails;
                        intent.putExtra ("userName", user.name);
                        intent.putExtra ("userCurrent", StaticMethods.isCurrentUser (user));
                        b.putString ("userID", user.getUserPhoneNum ());
                        b.putInt ("index", user.getIndexInAllDataList ());
                        Date currentDate = new Date ();
                        long diff = currentDate.getTime () - user.getLastSeen ().getTime ();
                        long diffMinutes = diff / (60 * 1000);
                        b.putInt ("online", (int) diffMinutes);
                        intent.putExtras (b);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity (intent);
                        break;
                }
            }
        });

        messageButton = (Button) view.findViewById (R.id.messageButton);
        messageButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                switch (v.getId ()) {
                    case R.id.messageButton:
                        Intent intent = new Intent (c, ChatActivity.class);
                        Bundle b = new Bundle ();
                        b.putInt ("index", userDetails.getIndexInAllDataList ());
                        intent.putExtra ("userId", id);
                        intent.putExtra ("userName", userDetails.getName ());
                        intent.putExtras (b);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity (intent);
                        break;

                }
            }
        });

        return view;
    }

    public void setImageLoader() {
        float density = c.getResources ().getDisplayMetrics ().density;
        pixels = (int) (80 * density + 0.5f);

        options = new DisplayImageOptions.Builder ()
                          .cacheOnDisk (true)
                          .cacheInMemory (true)
                          .bitmapConfig (Bitmap.Config.RGB_565)
                          .imageScaleType (ImageScaleType.EXACTLY)
                          .resetViewBeforeLoading (true)
                          .build ();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder (c)
                                                  .defaultDisplayImageOptions (options)
                                                  .threadPriority (Thread.MAX_PRIORITY)
                                                  .threadPoolSize (4)
                                                  .memoryCache (new WeakMemoryCache ())
                                                  .denyCacheImageMultipleSizesInMemory ()
                                                  .build ();
        imageLoader = ImageLoader.getInstance ();
        imageLoader.init (config);
    }

    class TrackItemHolder {
        ImageView image;
        TextView name;

        public TrackItemHolder(View v) {
            image = (ImageView) v.findViewById (R.id.trackImage);
            name = (TextView) v.findViewById (R.id.trackNameTV);
        }
    }

    String getTrackLine(String nameFromParse) {
        if (nameFromParse.equals ("like")) {
            return c.getResources ().getString (R.string.like);
        } else if (nameFromParse.equals ("love")) {
            return c.getResources ().getString (R.string.love);
        } else if (nameFromParse.equals ("letsHang")) {
            return c.getResources ().getString (R.string.letsHang);
        } else {
            return c.getResources ().getString (R.string.sexy);
        }
    }

}

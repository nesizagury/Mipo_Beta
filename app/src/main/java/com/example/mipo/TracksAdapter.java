package com.example.mipo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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
        super(c, 0, list);
        this.list = list;
        this.c = c;
        setImageLoader();
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

        if (list.get (pos).picUrl != null) {
            imageLoader.displayImage (list.get (pos).picUrl, holder.image);
        } else {
            holder.image.setImageResource (R.drawable.favorite);
        }

        holder.name.setText (trackItem.getName() + " " + list.get(pos).getTrackName());
        holder.image.setTag(trackItem);
        holder.name.setTag(trackItem);


        profileButton = (Button) view.findViewById (R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.profileButton:
                        Intent intent = new Intent(c, UserPage.class);
                        Bundle b = new Bundle();
                        for (int i = 0; i < GlobalVariables.userDataList.size(); i++) {
                            if (list.get(pos).getPicUrl().equals(GlobalVariables.userDataList.get(i).getPicUrl())) {
                                b.putInt("index", i);
                                i = GlobalVariables.userDataList.size();
                            }
                        }
                        intent.putExtras(b);
                        intent.putExtra("userName", trackItem.getName());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(intent);
                        break;

                }
            }
        });


        messageButton = (Button) view.findViewById (R.id.messageButton);
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.messageButton:
                        Intent intent = new Intent(c, ChatActivity.class);
                        Bundle b = new Bundle();
                        for (int i = 0; i < GlobalVariables.userDataList.size(); i++) {
                            if (list.get(pos).getPicUrl().equals(GlobalVariables.userDataList.get(i).getPicUrl())) {
                                b.putInt("index", i);
                                id = GlobalVariables.userDataList.get(i).getUserPhoneNum();
                                i = GlobalVariables.userDataList.size();
                            }
                        }
                        intent.putExtra ("userId", id);
                        intent.putExtra("userName", trackItem.getName());
                        intent.putExtras(b);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.startActivity(intent);
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

}

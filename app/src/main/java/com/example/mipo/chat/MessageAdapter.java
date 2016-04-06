package com.example.mipo.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.mipo.R;
import com.example.mipo.ViewUserPic;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<MessageWrapper> data = null;
    static ImageLoader imageLoader;
    DisplayImageOptions options;
    int pixels;

    public MessageAdapter(Context context, List<MessageWrapper> list) {
        super ();
        this.context = context;
        this.data = list;
        if (imageLoader == null || !imageLoader.isInited ()) {
            setImageLoader ();
        }
    }

    @Override
    public int getCount() {
        return data != null ? data.size () : 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get (position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return this.data.get (position).getIsMeSending () ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {

        final MessageWrapper messageWrapper = data.get (position);
        boolean isSend = messageWrapper.getIsMeSending ();

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder ();
            if (isSend) {
                convertView = LayoutInflater.from (context).inflate (R.layout.msg_item_right, null);
            } else {
                convertView = LayoutInflater.from (context).inflate (R.layout.msg_item_left, null);
            }
            viewHolder.sendDateTextView = (TextView) convertView.findViewById (R.id.sendDateTextView);
            viewHolder.sendTimeTextView = (TextView) convertView.findViewById (R.id.sendTimeTextView);
            viewHolder.textTextView = (TextView) convertView.findViewById (R.id.textTextView);
            viewHolder.photoImageView = (ImageView) convertView.findViewById (R.id.photoImageView);
            viewHolder.failImageView = (ImageView) convertView.findViewById (R.id.failImageView);
            viewHolder.sendingProgressBar = (ProgressBar) convertView.findViewById (R.id.sendingProgressBar);


            viewHolder.isMeSending = isSend;
            convertView.setTag (viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag ();
        }

        try {
            String dateString = DateFormat.format ("dd-MM-yyyy h:mmaa", messageWrapper.getTime ()).toString ();
            String[] t = dateString.split (" ");
            viewHolder.sendDateTextView.setText (t[0]);
            viewHolder.sendTimeTextView.setText (t[1]);

            if (position == 0) {
                viewHolder.sendDateTextView.setVisibility (View.VISIBLE);
            } else {
                MessageWrapper pmsg = data.get (position - 1);
                if (inSameDay (pmsg.getTime (), messageWrapper.getTime ())) {
                    viewHolder.sendDateTextView.setVisibility (View.GONE);
                } else {
                    viewHolder.sendDateTextView.setVisibility (View.VISIBLE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace ();
        }

        switch (messageWrapper.getType ()) {
            case 0://text
                viewHolder.textTextView.setText (messageWrapper.getContent ());
                viewHolder.textTextView.setVisibility (View.VISIBLE);
                viewHolder.photoImageView.setVisibility (View.GONE);
                if (messageWrapper.getIsMeSending ()) {
                    LayoutParams sendTimeTextViewLayoutParams = (LayoutParams) viewHolder.sendTimeTextView.getLayoutParams ();
                    sendTimeTextViewLayoutParams.addRule (RelativeLayout.ALIGN_LEFT, R.id.textTextView);
                    sendTimeTextViewLayoutParams.addRule (RelativeLayout.BELOW, R.id.textTextView);
                    viewHolder.sendTimeTextView.setLayoutParams (sendTimeTextViewLayoutParams);

                    LayoutParams layoutParams = (LayoutParams) viewHolder.failImageView.getLayoutParams ();
                    layoutParams.addRule (RelativeLayout.LEFT_OF, R.id.textTextView);
                    if (messageWrapper.getSendSucces () != null && messageWrapper.getSendSucces () == false) {
                        viewHolder.failImageView.setVisibility (View.VISIBLE);
                        viewHolder.failImageView.setLayoutParams (layoutParams);
                    } else {
                        viewHolder.failImageView.setVisibility (View.GONE);
                    }

                    if (messageWrapper.getState () != null && messageWrapper.getState () == 0) {
                        viewHolder.sendingProgressBar.setVisibility (View.VISIBLE);
                        viewHolder.sendingProgressBar.setLayoutParams (layoutParams);
                    } else {
                        viewHolder.sendingProgressBar.setVisibility (View.GONE);
                    }
                } else {
                    viewHolder.failImageView.setVisibility (View.GONE);
                    viewHolder.sendingProgressBar.setVisibility (View.GONE);

                    LayoutParams sendTimeTextViewLayoutParams = (LayoutParams) viewHolder.sendTimeTextView.getLayoutParams ();
                    sendTimeTextViewLayoutParams.addRule (RelativeLayout.ALIGN_RIGHT, R.id.textTextView);
                    sendTimeTextViewLayoutParams.addRule (RelativeLayout.BELOW, R.id.textTextView);
                    viewHolder.sendTimeTextView.setLayoutParams (sendTimeTextViewLayoutParams);
                }
                break;

            case 1://photo
                viewHolder.textTextView.setVisibility (View.GONE);
                viewHolder.photoImageView.setVisibility (View.VISIBLE);

                imageLoader.displayImage (messageWrapper.getPicUrl (), viewHolder.photoImageView);
                viewHolder.photoImageView.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent (context, ViewUserPic.class);
                        intent.putExtra ("picUrl", messageWrapper.getPicUrl ());
                        context.startActivity (intent);
                    }
                });

                if (messageWrapper.getIsMeSending ()) {
                    LayoutParams sendTimeTextViewLayoutParams = (LayoutParams) viewHolder.sendTimeTextView.getLayoutParams ();
                    sendTimeTextViewLayoutParams.addRule (RelativeLayout.ALIGN_LEFT, R.id.photoImageView);
                    sendTimeTextViewLayoutParams.addRule (RelativeLayout.BELOW, R.id.photoImageView);
                    viewHolder.sendTimeTextView.setLayoutParams (sendTimeTextViewLayoutParams);

                    LayoutParams layoutParams = (LayoutParams) viewHolder.failImageView.getLayoutParams ();
                    layoutParams.addRule (RelativeLayout.LEFT_OF, R.id.photoImageView);
                    if (messageWrapper.getSendSucces () != null && messageWrapper.getSendSucces () == false) {
                        viewHolder.failImageView.setVisibility (View.VISIBLE);
                        viewHolder.failImageView.setLayoutParams (layoutParams);
                    } else {
                        viewHolder.failImageView.setVisibility (View.GONE);
                    }

                    if (messageWrapper.getState () != null && messageWrapper.getState () == 0) {
                        viewHolder.sendingProgressBar.setVisibility (View.VISIBLE);
                        viewHolder.sendingProgressBar.setLayoutParams (layoutParams);
                    } else {
                        viewHolder.sendingProgressBar.setVisibility (View.GONE);
                    }

                } else {
                    viewHolder.failImageView.setVisibility (View.GONE);
                    LayoutParams sendTimeTextViewLayoutParams = (LayoutParams) viewHolder.sendTimeTextView.getLayoutParams ();
                    sendTimeTextViewLayoutParams.addRule (RelativeLayout.ALIGN_RIGHT, R.id.photoImageView);
                    sendTimeTextViewLayoutParams.addRule (RelativeLayout.BELOW, R.id.photoImageView);
                    viewHolder.sendTimeTextView.setLayoutParams (sendTimeTextViewLayoutParams);
                }
                break;

            default:
                break;
        }
        return convertView;
    }

    public static boolean inSameDay(Date date1, Date Date2) {
        Calendar calendar = Calendar.getInstance ();
        calendar.setTime (date1);
        int year1 = calendar.get (Calendar.YEAR);
        int day1 = calendar.get (Calendar.DAY_OF_YEAR);

        calendar.setTime (Date2);
        int year2 = calendar.get (Calendar.YEAR);
        int day2 = calendar.get (Calendar.DAY_OF_YEAR);

        if ((year1 == year2) && (day1 == day2)) {
            return true;
        }
        return false;
    }

    static class ViewHolder {
        public TextView sendDateTextView;

        public TextView textTextView;
        public ImageView photoImageView;

        public ImageView failImageView;
        public TextView sendTimeTextView;
        public ProgressBar sendingProgressBar;

        public boolean isMeSending = true;
    }

    public void setImageLoader() {
        float density = context.getResources ().getDisplayMetrics ().density;
        pixels = (int) (64 * density + 0.5f);

        options = new DisplayImageOptions.Builder ()
                          .cacheOnDisk (true)
                          .cacheInMemory (true)
                          .bitmapConfig (Bitmap.Config.RGB_565)
                          .imageScaleType (ImageScaleType.EXACTLY)
                          .resetViewBeforeLoading (true)
                          .build ();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder (context)
                                                  .defaultDisplayImageOptions (options)
                                                  .threadPriority (Thread.MAX_PRIORITY)
                                                  .threadPoolSize (2)
                                                  .memoryCache (new WeakMemoryCache ())
                                                  .denyCacheImageMultipleSizesInMemory ()
                                                  .build ();
        imageLoader = ImageLoader.getInstance ();
        imageLoader.init (config);
    }
}

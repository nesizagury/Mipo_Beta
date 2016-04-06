package com.example.mipo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class BlockProfilesActivity extends AppCompatActivity {

    private ListView listView;
    private Context context;
    ArrayList<UserDetails> usersArray = new ArrayList<> ();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_profiles);
        listView = (ListView) findViewById(R.id.block_listView);
        context = this;
        if(GlobalVariables.currentUser.getBlocked()!=null) {
            if(!GlobalVariables.currentUser.getBlocked().isEmpty()) {
                for (int i = 0; i < GlobalVariables.userDataList.size(); i++) {
                    if (GlobalVariables.currentUser.getBlocked().indexOf(GlobalVariables.userDataList.get(i).getUserPhoneNum()) != -1) {
                        usersArray.add(GlobalVariables.userDataList.get(i));
                    }
                }
                listView.setAdapter(new BlockListAdapter(this, usersArray));
            }else{
                Toast.makeText(this,this.getResources().getString(R.string.have_no_block),Toast.LENGTH_LONG).show();
            }
        }

    }

    class BlockListAdapter extends BaseAdapter{


        Context context;
        ArrayList<UserDetails> users;
        ArrayList<BlockRow> blockList;
        ImageLoader imageLoader;
        DisplayImageOptions options;
        int pixels;



        public BlockListAdapter(Context context, ArrayList<UserDetails> blockUsers){

            this.blockList = new ArrayList<BlockRow> ();
            this.users = blockUsers;
            this.context = context;
            for (int i = 0; i < users.size (); i++) {
                this.blockList.add (new BlockRow (users.get (i).getName(), users.get(i).getPicUrl(),users.get(i).getUserPhoneNum()));
            }
            if (imageLoader == null || (imageLoader != null && !imageLoader.isInited ())) {
                setImageLoader ();
            }

        }

        @Override
        public int getCount() {
            return blockList.size();
        }

        @Override
        public Object getItem(int position) {
            return blockList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            LayoutInflater layoutInfla = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            final View row = layoutInfla.inflate (R.layout.block_user_row, parent, false);
            final TextView nameTV = (TextView) row.findViewById (R.id.block_username);
            Button cancelBlockBu = (Button) row.findViewById (R.id.cancel_block_button);
            ImageView image = (ImageView) row.findViewById (R.id.block_user_image);

            cancelBlockBu.setVisibility(View.GONE);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (row.findViewById(R.id.cancel_block_button).getVisibility() == View.GONE) {
                        row.findViewById(R.id.cancel_block_button).setVisibility(View.VISIBLE);
                    } else {
                        row.findViewById(R.id.cancel_block_button).setVisibility(View.GONE);
                    }
                }
            });

            final BlockRow temp = blockList.get (position);
            nameTV.setText(temp.name);
            imageLoader.displayImage(temp.picUrl, image);

            cancelBlockBu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String phoneNum =temp.phoneNum;
                    GlobalVariables.currentUser.getBlocked().remove(phoneNum);

                    ParseQuery<Profile> query = new ParseQuery ("Profile");
                    query.whereEqualTo("number", GlobalVariables.currentUser.getUserPhoneNum());
                    query.getFirstInBackground(new GetCallback<Profile> () {
                        public void done(Profile profilesList, ParseException e) {
                            if (e == null) {
                                Profile profile = profilesList;
                                profile.getBlocked().clear();
                                profile.setBlocked(GlobalVariables.currentUser.getBlocked());
                                profile.saveInBackground();
                                blockList.remove(temp);
                                listView.invalidateViews();

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

                    ParseQuery query2 = new ParseQuery ("Profile");
                    query2.whereEqualTo ("number", phoneNum);
                    query2.getFirstInBackground(new GetCallback<Profile> () {
                        public void done(Profile profilesList, ParseException e) {
                            if (e == null) {
                                if (profilesList.getBlockedBy() != null) {
                                    Profile profile = profilesList;
                                    List by = profile.getBlockedBy();
                                    by.remove(GlobalVariables.currentUser.getUserPhoneNum());
                                    profile.getBlockedBy().clear();
                                    profile.setBlockedBy(by);
                                    profile.saveInBackground();
                                }

                            } else {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

            return row;
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
                    .memoryCache (new WeakMemoryCache())
                    .denyCacheImageMultipleSizesInMemory ()
                    .build();
            imageLoader = ImageLoader.getInstance ();
            imageLoader.init(config);
        }


        class BlockRow{
            String name;
            String picUrl;
            String phoneNum;

            public BlockRow(String name, String picUrl,String phoneNum){
                this.name=name;
                this.picUrl=picUrl;
                this.phoneNum=phoneNum;
            }
        }
    }

}

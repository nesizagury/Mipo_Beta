package com.example.mipo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class CommunicationsActivity extends AppCompatActivity {
    TabPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_communication);

        TabLayout tabLayout = (TabLayout) findViewById (R.id.tab_layout);
        tabLayout.addTab (tabLayout.newTab ().setText (getResources ().getString (R.string.messages)));
        tabLayout.addTab (tabLayout.newTab ().setText (getResources ().getString (R.string.tracks)));
        tabLayout.setTabGravity (TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById (R.id.pager);
        adapter = new TabPagerAdapter
                          (getSupportFragmentManager (), tabLayout.getTabCount ());
        viewPager.setAdapter (adapter);
        viewPager.addOnPageChangeListener (new TabLayout.TabLayoutOnPageChangeListener (tabLayout));
        tabLayout.setOnTabSelectedListener (new TabLayout.OnTabSelectedListener () {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem (tab.getPosition ());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart ();
        if (adapter != null && adapter.tab1 != null && adapter.tab1.handler != null) {
            adapter.tab1.handler.postDelayed (adapter.tab1.runnable, 500);
        }
        if (adapter != null && adapter.tab2 != null) {
            adapter.tab2.loadTracksInBackGround ();
        }
    }

    @Override
    public void onPause() {
        super.onPause ();
        if (adapter != null && adapter.tab1 != null && adapter.tab1.handler != null) {
            adapter.tab1.handler.removeCallbacks (adapter.tab1.runnable);
        }
    }
}

package com.example.wladek.pocketcard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.example.wladek.pocketcard.pagerAdapters.BuyViewPagerAdapter;

/**
 * Created by wladek on 7/7/16.
 */
public class BuyScreenActivity extends ActionBarActivity implements ActionBar.TabListener {

    ActionBar actionBar;
    ViewPager viewPager;
    FragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_activity_layout);
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.buy_pager);
        setContentView(viewPager);
//        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public int getCount() {
//                return 0;
//            }
//
//            @Override
//            public Fragment getItem(int position) {
//                return null;
//            }
//        };

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayOptions(0 , ActionBar.DISPLAY_SHOW_TITLE);

        //set tabs
        actionBar.addTab(actionBar.newTab().setText("Search").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Cart").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Quick order").setTabListener(this));

        final BuyViewPagerAdapter adapter = new BuyViewPagerAdapter
                (getSupportFragmentManager(), actionBar.getTabCount());

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}

package com.example.wladek.pocketcard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.example.wladek.pocketcard.helper.DatabaseHelper;
import com.example.wladek.pocketcard.pagerAdapters.BuyViewPagerAdapter;
import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;

/**
 * Created by wladek on 7/7/16.
 */
public class BuyScreenActivity extends ActionBarActivity implements ActionBar.TabListener {
    DatabaseHelper myDb;

    ActionBar actionBar;
    ViewPager viewPager;
    BuyViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDb  = new DatabaseHelper(BuyScreenActivity.this);
        super.onCreate(savedInstanceState);
        viewPager = new ViewPager(this);
        viewPager.setId(R.id.buy_pager);
        setContentView(viewPager);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        //set tabs
        actionBar.addTab(actionBar.newTab().setText("Search").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Cart").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Quick order").setTabListener(this));

        adapter = new BuyViewPagerAdapter
                (getSupportFragmentManager(), actionBar.getTabCount());

        //Pass this list to Buy page adapter
        adapter.setShopItems((ArrayList<ShopItem>) getIntent().getSerializableExtra("item_list"));

        viewPager.setAdapter(adapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                actionBar.setSelectedNavigationItem(position);

                Fragment fragment = ((FragmentStatePagerAdapter)viewPager.getAdapter()).getItem(position);

                if (position == 2 && fragment != null){
                    fragment.onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

    }
}

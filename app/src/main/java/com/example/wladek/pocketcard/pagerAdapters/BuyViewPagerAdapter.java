package com.example.wladek.pocketcard.pagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.wladek.pocketcard.fragment.CartFragment;
import com.example.wladek.pocketcard.fragment.QuickOrderFragment;
import com.example.wladek.pocketcard.fragment.SearchFragment;

/**
 * Created by wladek on 7/13/16.
 */
public class BuyViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public BuyViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                SearchFragment sf = new SearchFragment();
                return sf;
            case 1:
                CartFragment cf = new CartFragment();
                return cf;
            case 2:
                QuickOrderFragment qf = new QuickOrderFragment();
                return qf;

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

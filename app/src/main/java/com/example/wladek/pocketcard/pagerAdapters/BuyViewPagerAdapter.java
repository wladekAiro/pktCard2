package com.example.wladek.pocketcard.pagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.wladek.pocketcard.fragment.CartFragment;
import com.example.wladek.pocketcard.fragment.QuickOrderFragment;
import com.example.wladek.pocketcard.fragment.SearchFragment;
import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;

/**
 * Created by wladek on 7/13/16.
 */
public class BuyViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    ArrayList<ShopItem> shopItems;

    public BuyViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                SearchFragment sf = new SearchFragment();
                sf.setItemResults(getShopItems());
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

    public ArrayList<ShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(ArrayList<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }
}

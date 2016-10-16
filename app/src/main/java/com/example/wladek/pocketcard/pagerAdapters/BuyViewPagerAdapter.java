package com.example.wladek.pocketcard.pagerAdapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.example.wladek.pocketcard.fragment.CartFragment;
import com.example.wladek.pocketcard.fragment.QuickOrderFragment;
import com.example.wladek.pocketcard.fragment.SearchFragment;
import com.example.wladek.pocketcard.pojo.ShopItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wladek on 7/13/16.
 */
public class BuyViewPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    ArrayList<ShopItem> shopItems;
    private Map<Integer , String> mFragmentTags;
    private FragmentManager fm;

    public BuyViewPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.fm = fm;
        mFragmentTags = new HashMap<Integer , String>();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 2:
                SearchFragment sf = new SearchFragment();
                sf.setItemResults(getShopItems());
                return sf;
            case 1:
                CartFragment cf = new CartFragment();
                return cf;
            case 0:
                QuickOrderFragment qf = new QuickOrderFragment();
                return qf;

            default:
                return null;

        }
    }

    @Override
    public Object instantiateItem(View container, int position) {
        Object obj =  super.instantiateItem(container, position);

        if (obj instanceof Fragment){
            //Record fragment tag here
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position , tag);
        }

        return obj;
    }

    public Fragment getFragment(int position){
        String tag = mFragmentTags.get(position);

        if (tag == null){
            return null;
        }

        return fm.findFragmentByTag(tag);
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

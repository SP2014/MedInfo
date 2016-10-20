package com.starsoft.medinfo.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.starsoft.medinfo.asyncrssclient.RssItem;
import com.starsoft.medinfo.fragments.TipFragment;

import java.util.List;

/**
 * Created by Aashish on 9/7/2016.
 */
public class TipPagerAdapter extends FragmentStatePagerAdapter {

    private List<RssItem> itemList = null;

    public TipPagerAdapter(FragmentManager fm, List<RssItem> items) {
        super(fm);
        this.itemList = items;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return TipFragment.newInstance(itemList.get(0));
            case 1: return TipFragment.newInstance(itemList.get(1));
            case 2: return TipFragment.newInstance(itemList.get(2));
            case 3: return TipFragment.newInstance(itemList.get(3));
            case 4: return TipFragment.newInstance(itemList.get(4));

            default:return TipFragment.newInstance(itemList.get(0));
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}

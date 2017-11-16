package com.dartmouth.cs.takenote;

/**
 * Created by acaciah on 11/16/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class TabViewPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    public TabViewPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "palette";
            case 1:
                return "note";
            case 2:
                return "notelist";

            case 3:
                return "settings";
            default:
                break;
        }
        return null;

    }
}

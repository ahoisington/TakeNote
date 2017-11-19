package com.dartmouth.cs.takenote.tab;

/**
 * Created by acaciah on 11/16/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.dartmouth.cs.takenote.NoteListFragment;

import java.util.ArrayList;


public class TabViewPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    public TabViewPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;



//        setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                Toast.makeText(getApplicationContext(),"tells us anything? "+mViewPageAdapter.getItem(position).getId() ,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),"tells us anything? "+mViewPageAdapter.getItem(position).getClass().getName() ,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(),"tells us anything? "+mViewPageAdapter.getItem(position).getClass().isInstance(new NoteListFragment()) ,Toast.LENGTH_SHORT).show();
//
//
//                if (mViewPageAdapter.getItem(position).getId() == 2){
//                    mViewPageAdapter.getItem(1).onResume();
//                }
//            }
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {}
//            @Override
//            public void onPageScrollStateChanged(int arg0) {}
//        });
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

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }
}

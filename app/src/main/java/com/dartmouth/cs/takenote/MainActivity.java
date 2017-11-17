package com.dartmouth.cs.takenote;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.support.v4.view.ViewPager;
import com.dartmouth.cs.takenote.SlidingTabStrip;
import com.dartmouth.cs.takenote.NoteFragment;
import com.dartmouth.cs.takenote.NoteListFragment;
import com.dartmouth.cs.takenote.PaletteFragment;
import com.dartmouth.cs.takenote.TabViewPageAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private TabViewPageAdapter mViewPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new PaletteFragment());
        fragments.add(new NoteFragment());
        fragments.add(new NoteListFragment());
        fragments.add(new SettingsFragment());

        mViewPageAdapter = new TabViewPageAdapter(getSupportFragmentManager(),fragments);

        mViewPager.setAdapter(mViewPageAdapter);
        mViewPager.setCurrentItem(1); // sets note fragment to default

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mViewPager);
    }
}

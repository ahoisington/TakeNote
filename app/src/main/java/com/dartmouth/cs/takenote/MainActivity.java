package com.dartmouth.cs.takenote;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import 	android.support.v4.app.FragmentTransaction;

import com.dartmouth.cs.takenote.tab.SlidingTabLayout;
import com.dartmouth.cs.takenote.tab.TabViewPageAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.dartmouth.cs.takenote.NoteFragment.SHARED_PREF;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    public ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    private TabViewPageAdapter mViewPageAdapter;
    public boolean toasted = false;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private long lastShake=0;
    private long thisShake;

    public void changeTab(int page) {
        mViewPager.setCurrentItem(page);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        sp.edit().clear().commit();

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        NoteListFragment noteListFragment = new NoteListFragment();

        fragments = new ArrayList<Fragment>();
        fragments.add(new PaletteFragment());
        fragments.add(new NoteFragment());
        fragments.add(noteListFragment);
        fragments.add(new SettingsFragment());

        mViewPageAdapter = new myFragmentPagerAdaptor(getSupportFragmentManager(), fragments);

        //Add sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        mViewPager.setAdapter(mViewPageAdapter);
        mViewPager.setCurrentItem(1); // sets note fragment to default

//        FragmentTransaction ftr = this.getSupportFragmentManager().beginTransaction();
//        ftr.detach(NoteListFragment).attach(NoteList.this).commit();

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mViewPager);

        if (toasted == false) {
            Toast.makeText(this.getApplicationContext(), "Hi! Make sure to save your note before exiting!", Toast.LENGTH_SHORT).show();
            toasted = true;
        }


    }


    public static class myFragmentPagerAdaptor extends TabViewPageAdapter {

        public myFragmentPagerAdaptor(FragmentManager fm, ArrayList al) {
            super(fm, al);
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            Log.d("TEST", "RETURNED NONE");
            return POSITION_NONE;
        }
    }

    public void dataChanged() {
        mViewPageAdapter.notifyDataSetChanged();
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter


            if (mAccel > 13) {
                thisShake = System.currentTimeMillis();
                Log.d("DEBUG1",String.valueOf(thisShake-lastShake));

                if(thisShake-lastShake>1000) {
                    if (x > 11) {
                        mViewPager.arrowScroll(View.FOCUS_RIGHT);
                        Toast.makeText(getApplicationContext(), "Hard SHAKE", Toast.LENGTH_SHORT).show();
                    } else if (x < -11) {
                        mViewPager.arrowScroll(View.FOCUS_LEFT);
                    }
                }
                lastShake = System.currentTimeMillis();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy){}
    };
}

package com.dartmouth.cs.takenote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import java.io.File;

/**
 * Created by acaciah on 11/16/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor ;
    public static String SHARED_PREF = "my_sharedpref"; // where we are storing profile text


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        sp = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sp.edit();

        //display user's email after input
        EditTextPreference email = (EditTextPreference) findPreference("email");
        email.setSummary("Please insert your email.");
        email.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
                return true;
            }
        });

        Preference delete = findPreference("delete");
        delete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference p){


//                File file = new File(ih.getImgPath(id));
//                boolean deleted = file.delete();
                Integer endId = Integer.valueOf(sp.getString("currId", ""));
                Log.d("DEBUG", "readNotes: delete. endId is" + endId);

                String filename;
                for (int id = 1; id < endId; id=id+1) {
                    String filepath = getContext().getFilesDir() + "/" + "note"+id+".txt";
                    File f = new File(filepath);
                    f.delete();
                    Log.d("DEBUG", "readNotes: does file exist? " + id + " does it? "+ f.exists());

                }
                //deletes local data from shared prefs
                editor.putString("currId", "0");
                editor.putString("count", "0");
                editor.apply(); // commit the changes to shared prefs

                Toast.makeText(getContext(), "Your notes have been deleted.",Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }
}






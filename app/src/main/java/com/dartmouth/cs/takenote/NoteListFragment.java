package com.dartmouth.cs.takenote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dartmouth.cs.takenote.tab.NoteListAdapter;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by acaciah on 11/16/17.
 */

public class NoteListFragment extends Fragment {

    public static String SHARED_PREF = "my_sharedpref"; // where we are storing profile text
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor ;

    private BufferedReader reader = null;

    private ArrayList<String> noteTitleList;
    private ArrayList<String> noteDateList;
    private ArrayList<Integer> noteIdList;
    private Integer count;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notelist, container, false);
        sp = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sp.edit();
        count = Integer.valueOf(sp.getString("count", ""));

        noteTitleList = new ArrayList<String>();
        noteDateList = new ArrayList<String>();
        noteIdList = new ArrayList<Integer>();
        readNotes();

        NoteListAdapter adapter = new NoteListAdapter(getActivity(), noteTitleList, noteDateList, noteIdList); //ORIG
        ListView listView= (ListView) rootView.findViewById(R.id.notelist);
        listView.setAdapter(adapter);

        return rootView;
    }

    private void readNotes (){
        Integer endId = Integer.valueOf(sp.getString("currId", ""));
        String filename;
        Log.d("DEBUG", "readNotes: endId is "+endId);

        try{
            for(int currId=1;currId<endId;currId++){
                filename = "note"+currId;
                FileInputStream file = getContext().openFileInput(filename);
                reader = new BufferedReader(new InputStreamReader(file));
                String eol = System.getProperty("line.seperator");
                String title = reader.readLine();
                String date = reader.readLine();

                noteTitleList.add(title);
                noteDateList.add(date);
                noteIdList.add(currId);

                currId++;
                reader.close();
            }
        } catch(FileNotFoundException e){
            Log.d("ERROR", "onCreateView: "+e);
            e.printStackTrace();
        }catch (IOException e){
            Log.d("ERROR", "onCreateView: "+e);
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Toast.makeText(getContext().getApplicationContext(), "resumed!",Toast.LENGTH_SHORT).show();
        super.onResume();
    }
}
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
import java.io.File;
import android.widget.Toast;

import com.dartmouth.cs.takenote.tab.NoteListAdapter;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notelist, container, false);
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

        try {
            for (int id = 1; id < endId; id=id+1) {
                Log.d("DEBUG", "readNotes: currId is " + id + " endId is "+endId);

                String filepath = getContext().getFilesDir() + "/" + "note"+id+".txt";
                Log.d("DEBUG", "readNotes: filePath is " + filepath);

                File f = new File(filepath);

                if(f.exists()){
                    StringBuilder text = new StringBuilder();
                    FileInputStream fis = new FileInputStream (f);

                    reader = new BufferedReader(new InputStreamReader(fis));
                    String line = "";
                    String title="";
                    String date="";
                    String lines[];
                    if ((line = reader.readLine()) != null){
                        text.append(line.replaceAll("null", "\n"));
                        lines =text.toString().split("\\r?\\n");
                        title=lines[0];
                        if (lines.length>1){
                            date=lines[1];
                        }
                    }

                    Log.d("DEBUG", "readNotes: id "+id+" title " + title+ " date "+date );

                    noteTitleList.add(title);
                    noteDateList.add(date);
                    noteIdList.add(id);

                    id++;
                    reader.close();
                }
            }


        } catch(FileNotFoundException e){
            Log.d("ERROR", "onCreateView: "+e);
            e.printStackTrace();
        }catch (IOException e){
            Log.d("ERROR", "onCreateView: "+e);
            e.printStackTrace();
        }catch (NullPointerException e) {
            Log.d("ERROR", "onCreateView: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
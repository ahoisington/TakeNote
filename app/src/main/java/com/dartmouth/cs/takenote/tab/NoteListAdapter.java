package com.dartmouth.cs.takenote.tab;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dartmouth.cs.takenote.R;

import java.util.ArrayList;

/**
 * Created by acaciah on 11/19/17.
 */

public class NoteListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> noteTitleList;
    private ArrayList<String> noteDateList;
    private ArrayList<Integer> noteIdList; //gets passed in if we wanted to show some of the note's content on the notelist panels

    public NoteListAdapter (Activity context, ArrayList<String> noteTitleList, ArrayList<String> noteDateList, ArrayList<Integer> noteIdList){
        super(context, R.layout.notelist);
        this.context = context;
        this.noteTitleList = noteTitleList;
        this.noteDateList = noteDateList;
        this.noteIdList = noteIdList;

        Toast.makeText(getContext().getApplicationContext(), "adapter called woah!",Toast.LENGTH_SHORT).show();

        Log.d("DEBUG", "NoteListAdapter: this is titlenotelist "+noteTitleList);

        if (noteTitleList.isEmpty()){
            Log.d("DEBUG", "NoteListAdapter: no note list!");
        }
        notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.notelist, null,true);

        TextView titleView = (TextView) rowView.findViewById(R.id.title);
        TextView dateView = (TextView) rowView.findViewById(R.id.date);

        titleView.setText(noteTitleList.get(position));
        dateView.setText(noteDateList.get(position));
//        infoView.setText(noteInfoList.get(position)); //populate note info with top two lines of the note

        return rowView;

    }


}
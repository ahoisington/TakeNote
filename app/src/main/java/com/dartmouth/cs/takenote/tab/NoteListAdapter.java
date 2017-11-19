package com.dartmouth.cs.takenote.tab;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dartmouth.cs.takenote.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by acaciah on 11/19/17.
 */

public class NoteListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> noteTitleList;
    private ArrayList<String> noteDateList;
    private ArrayList<Integer> noteIdList; //gets passed in if we wanted to show some of the note's content on the notelist panels
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor ;
    public static String SHARED_PREF = "my_sharedpref"; // where we are storing profile text



    public NoteListAdapter (Activity context, ArrayList<String> noteTitleList, ArrayList<String> noteDateList, ArrayList<Integer> noteIdList){
        super(context, R.layout.notelist);
        this.context = context;
        this.noteTitleList = noteTitleList;
        this.noteDateList = noteDateList;
        this.noteIdList = noteIdList;

        Log.d("DEBUG", "NoteListAdapter: this is titlelist "+noteTitleList);
        Log.d("DEBUG", "NoteListAdapter: this is datelist "+noteDateList);
        Log.d("DEBUG", "NoteListAdapter: this is idlist "+noteIdList);

        if (noteTitleList.isEmpty()){
            Log.d("DEBUG", "NoteListAdapter: no note list!");
        }
//        notifyDataSetChanged();
    }


    public View getView(int position, View view, ViewGroup parent) {


        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.notelist, null,true);

        TextView titleView = (TextView) rowView.findViewById(R.id.title);
        TextView dateView = (TextView) rowView.findViewById(R.id.date);

        Log.d("DEBUG", "NoteListAdapter: current title "+noteTitleList.get(position)+" date is "+noteDateList.get(position)+ " id is "+ noteIdList.get(position));

        titleView.setText(noteTitleList.get(position));
        dateView.setText(noteDateList.get(position));


//        infoView.setText(noteInfoList.get(position)); //populate note info with top two lines of the note

        return rowView;

    }

    @Override
    public int getCount() {
//        sp= getContext().getApplicationContext().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
//        editor = sp.edit();
//        Integer currId = Integer.valueOf(sp.getString("currId",""));
//        return currId;
        return noteTitleList.size();
    }


}
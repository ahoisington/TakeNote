package com.dartmouth.cs.takenote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.*;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Toast;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by acaciah on 11/16/17.
 */

public class NoteFragment extends Fragment {


    public static String SHARED_PREF = "my_sharedpref"; // where we are storing profile text
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor ;
    BufferedWriter writer = null;

    private EditText titleView;
    private TextView dateView;
    private EditText contentView;
    private String title;
    private String date;
    private String content;


    private Integer count = 0;
    private Integer currId = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        Button saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
        sp = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sp.edit();
        count = Integer.valueOf(sp.getString("count",""));
        currId = Integer.valueOf(sp.getString("currId",""));



        titleView = (EditText) rootView.findViewById(R.id.title);
        dateView = (TextView) rootView.findViewById(R.id.date);
        contentView = (EditText) rootView.findViewById(R.id.content);

        //updates date view
        titleView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                Calendar cal = Calendar.getInstance();
                dateView.setText(dateFormat.format(cal.getTime()));           }
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = contentView.getText().toString();
                title = titleView.getText().toString();
                date = dateView.getText().toString();
                saveNote(title, date, content);
            }
        });

        return rootView;
    }

    private void saveNote(String title, String date, String content){

        //save currId number and save count
        count = count + 1;
        currId = currId + 1;
        editor.putString("count", String.valueOf(count));
        editor.putString("currId", String.valueOf(currId));
        editor.apply();

        //save note to internal storage file
        try{
            String filepath = getContext().getFilesDir() + "/" + "note"+currId+".txt";
            StringBuilder text = new StringBuilder();
            FileOutputStream fos = new FileOutputStream (new File(filepath));
//            FileOutputStream fos = getContext().getApplicationContext().openFileOutput(filepath, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            String eol = System.getProperty("line.separator");
            writer.write(title);
            writer.write("\n\r");
            writer.write(date);
            writer.newLine();
            writer.write(content);
            writer.newLine();
            writer.close();

            File file = new File(filepath);
            if(file.exists()){
                Toast.makeText(getContext().getApplicationContext(), "Your note has been saved :) note id is "+currId,Toast.LENGTH_SHORT).show();
//                Log.d("DEBUG", "saveNote: file exists "+filepath);
            } else{
                Log.d("DEBUG", "saveNote: doesnt exist sad "+filepath);
            }

        }catch(FileNotFoundException e){
            Log.d("ERROR", "onCreateView: "+e);
            e.printStackTrace();
        }catch (IOException e){
            Log.d("ERROR", "onCreateView: "+e);
            e.printStackTrace();
        }
    }
}
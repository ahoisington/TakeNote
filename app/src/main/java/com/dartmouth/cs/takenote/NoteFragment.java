package com.dartmouth.cs.takenote;

import android.app.TabActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
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

import java.lang.reflect.Array;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;



/**
 * Created by acaciah on 11/16/17.
 */

public class NoteFragment extends Fragment{


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

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private int shakeCount=0;
    private long firstTime;

    private ViewPager pager;


    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter


            if (mAccel>5){
                if (shakeCount == 0) {
                    firstTime = System.currentTimeMillis();
                }
                if(shakeCount>9){
                    long lastTime = System.currentTimeMillis();
                    if(lastTime-firstTime < 4000) {
                        int start = contentView.getText().toString().lastIndexOf(" ");
                        if (start == -1){start = 0;}
                        int end = contentView.getText().toString().length();
                        Editable newText = contentView.getEditableText().delete(start,end);
                        contentView.setText(newText);
                    }
                    shakeCount = 0;

                }
                else if(shakeCount>5 ){
                    long lastTime = System.currentTimeMillis();
                    if(lastTime-firstTime < 2000) {
                        int start = contentView.getText().toString().lastIndexOf(" ");
                        if (start == -1){start = 0;}
                        int end = contentView.getText().toString().length();
                        Editable newText = contentView.getEditableText().delete(start,end);
                        contentView.setText(newText);
                    }
                    else{
                        shakeCount = 0;
                    }
                }
                else{
                    shakeCount += 1;
                }
            }
    }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };


        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        Button saveBtn = (Button) rootView.findViewById(R.id.saveBtn);
        sp = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = sp.edit();
        count = Integer.valueOf(sp.getString("count","0"));
        currId = Integer.valueOf(sp.getString("currId","0"));

        titleView = (EditText) rootView.findViewById(R.id.title);
        dateView = (TextView) rootView.findViewById(R.id.date);
        contentView = (EditText) rootView.findViewById(R.id.content);


        //Add sensors
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;



            //updates date view
        titleView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                Calendar cal = Calendar.getInstance();
                dateView.setText(dateFormat.format(cal.getTime()));           }
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        contentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                editor.putInt("Content Length",s.length()-1).commit();
                if(sp.getInt("editableFlag",0)==0){
                    contentView.setText(formatText(s));
                }
                editor.putInt("editableFlag",0);
                contentView.setSelection(contentView.length(),contentView.length());
            }
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
                ((MainActivity) getActivity()).dataChanged();
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

    public Spannable formatText(Editable s){
        Spannable altText = new SpannableString(s);
        int endIndex = contentView.getText().length();
        Log.d("LENGTH",String.valueOf(endIndex));


        String boldIndexesString = sp.getString("bold","0");
        String italicIndexesString = sp.getString("italic","0");
        String underlineIndexesString = sp.getString("underline","0");

        String largeIndexesString = sp.getString("l","0");
        String mediumIndexesString = sp.getString("m","0");
        String smallIndexesString = sp.getString("s","0");


        if(boldIndexesString != "0"){
            String[] boldIndexes = boldIndexesString.split(",");
            if(boldIndexes.length%2!=0){
                altText.setSpan(new StyleSpan(Typeface.BOLD), Integer.valueOf(boldIndexes[boldIndexes.length-1]), endIndex-1, 0);
            }
            for(int i=0; i < boldIndexes.length - 1; i+=2) {
                altText.setSpan(new StyleSpan(Typeface.BOLD), Integer.valueOf(boldIndexes[i]), Integer.valueOf(boldIndexes[i + 1]), 0);
            }

        }

        if(italicIndexesString != "0"){
            String[] italicIndexes = italicIndexesString.split(",");
            for(int i=0; i < italicIndexes.length - 1; i+=2) {
                altText.setSpan(new StyleSpan(Typeface.ITALIC), Integer.valueOf(italicIndexes[i]), Integer.valueOf(italicIndexes[i + 1]), 0);
            }
            if(italicIndexes.length%2!=0){
                altText.setSpan(new StyleSpan(Typeface.ITALIC), Integer.valueOf(italicIndexes[italicIndexes.length-1]), endIndex-1, 0);
            }
        }

        if(underlineIndexesString != "0"){
            String[] underlineIndexes = underlineIndexesString.split(",");
            for(int i=0; i < underlineIndexes.length - 1; i+=2) {
                altText.setSpan(new StyleSpan(Typeface.ITALIC), Integer.valueOf(underlineIndexes[i]), Integer.valueOf(underlineIndexes[i + 1]), 0);
            }
            if(underlineIndexes.length%2!=0){
                altText.setSpan(new UnderlineSpan(), Integer.valueOf(underlineIndexes[underlineIndexes.length-1]), endIndex-1, 0);
            }
        }

        ///////////////////////////////////////////////////////////////////////

        if(largeIndexesString != "0"){
            String[] largeIndexes = largeIndexesString.split(",");
            for(int i=0; i < largeIndexes.length - 1; i+=2) {
                altText.setSpan(new AbsoluteSizeSpan(200), Integer.valueOf(largeIndexes[i]), Integer.valueOf(largeIndexes[i + 1]), 0);
            }
            if(largeIndexes.length%2!=0){
                altText.setSpan(new AbsoluteSizeSpan(200), Integer.valueOf(largeIndexes[largeIndexes.length-1]), endIndex-1, 0);
            }
        }

        if(mediumIndexesString != "0"){
            String[] mediumIndexes = mediumIndexesString.split(",");
            for(int i=0; i < mediumIndexes.length - 1; i+=2) {
               altText.setSpan(new AbsoluteSizeSpan(80), Integer.valueOf(mediumIndexes[i]), Integer.valueOf(mediumIndexes[i + 1]), 0);
            }
            if(mediumIndexes.length%2!=0){
               altText.setSpan(new AbsoluteSizeSpan(80), Integer.valueOf(mediumIndexes[mediumIndexes.length-1]), endIndex-1, 0);
            }
        }


        if(smallIndexesString != "0"){
            String[] smallIndexes = smallIndexesString.split(",");
            for(int i=0; i < smallIndexes.length - 1; i+=2) {
                altText.setSpan(new AbsoluteSizeSpan(40), Integer.valueOf(smallIndexes[i]), Integer.valueOf(smallIndexes[i + 1]), 0);
            }
            if(smallIndexes.length%2!=0){
                altText.setSpan(new AbsoluteSizeSpan(40), Integer.valueOf(smallIndexes[smallIndexes.length-1]), endIndex-1, 0);
            }
        }

        String[] colorList = {"black","red","orange","yellow","green","blue","purple","crimson","aqua","pink"};
        for(String color:colorList){
            int resId = getResources().getColor(getResources().getIdentifier(color, "color", "com.dartmouth.cs.takenote"));
            String temp = sp.getString(color,"0");

            if(temp!="0") {
                String[] tempArray = temp.split(",");
                for (int i = 0; i < tempArray.length - 1; i += 2) {
                    altText.setSpan(new ForegroundColorSpan(resId), Integer.valueOf(tempArray[i]), Integer.valueOf(tempArray[i + 1]), 0);
                }
                if (tempArray.length % 2 != 0) {
                    altText.setSpan(new ForegroundColorSpan(resId), Integer.valueOf(tempArray[tempArray.length - 1]), endIndex - 1, 0);
                }
            }
        }

        editor.putInt("editableFlag",1);
        return altText;

    }
}
package com.dartmouth.cs.takenote;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * Created by acaciah on 11/16/17.
 */

public class PaletteFragment extends Fragment {
    public static String SHARED_PREF = "my_sharedpref"; // where we are storing profile text
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_palette, container, false);
        sp = getActivity().getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);

        Button t1 = rootView.findViewById(R.id.tBtn1);
        Button t2 = rootView.findViewById(R.id.tBtn2);
        Button t3 = rootView.findViewById(R.id.tBtn3);

        Button tv1 = rootView.findViewById(R.id.tv1);
        Button tv2 = rootView.findViewById(R.id.tv2);
        Button tv3 = rootView.findViewById(R.id.tv3);

        Button c1 = rootView.findViewById(R.id.black);
        Button c2 = rootView.findViewById(R.id.red);
        Button c3 = rootView.findViewById(R.id.orange);
        Button c4 = rootView.findViewById(R.id.yellow);
        Button c5 = rootView.findViewById(R.id.green);
        Button c6 = rootView.findViewById(R.id.blue);

        Button c7 = rootView.findViewById(R.id.purple);
        Button c8 = rootView.findViewById(R.id.crimson);
        Button c9 = rootView.findViewById(R.id.aqua);
        Button c10 =rootView.findViewById(R.id.pink);
        Button c11 = rootView.findViewById(R.id.brown);
        Button c12 = rootView.findViewById(R.id.navy);

        View.OnClickListener selectFormat = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sp.edit();

                Button currBtn = (Button) v;
                String btnTitle = currBtn.getText().toString().toLowerCase();

                Integer contentLength = sp.getInt("Content Length",-1);

                String oldString = sp.getString(btnTitle,"-1");
                String newString = "";
                if(oldString.equals("-1")){
                    newString = String.valueOf(contentLength);
                }
                else{
                    newString = oldString + "," + String.valueOf(contentLength);
                }

                editor.putString(btnTitle,newString).commit();
                editor.putString("Last Color",btnTitle).commit();

                Toast.makeText(getActivity(),"Format selected!\n" + newString,Toast.LENGTH_SHORT).show();
            }
        };

        t1.setOnClickListener(selectFormat);
        t2.setOnClickListener(selectFormat);
        t3.setOnClickListener(selectFormat);

        tv1.setOnClickListener(selectFormat);
        tv2.setOnClickListener(selectFormat);
        tv3.setOnClickListener(selectFormat);

        c1.setOnClickListener(selectFormat);
        c2.setOnClickListener(selectFormat);
        c3.setOnClickListener(selectFormat);
        c4.setOnClickListener(selectFormat);
        c5.setOnClickListener(selectFormat);
        c6.setOnClickListener(selectFormat);

        c7.setOnClickListener(selectFormat);
        c8.setOnClickListener(selectFormat);
        c9.setOnClickListener(selectFormat);
        c10.setOnClickListener(selectFormat);
        c11.setOnClickListener(selectFormat);
        c12.setOnClickListener(selectFormat);

        return rootView;
    }

}

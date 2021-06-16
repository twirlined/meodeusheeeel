package com.example.myapplication;

import com.opencsv.CSVReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class readCSV extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        read();
    }

    public Health_data read() {
        String filename = "test.csv";
        ArrayList<String[]> data = new ArrayList<String[]>();
        Health_data d;
        d = new Health_data(0,"0",0);
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.test)));
            String [] nextLine;
            int line = 0;
            while((nextLine = reader.readNext()) != null) {
                data.add(nextLine);
                line++;
            }
            d = health(data, line);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }
        return d;
    }

    public Health_data health(ArrayList<String[]> data, int line) {
        // [0] step_count
        // [5] time (yyyy-mm-dd)
        // [10] calorie
        // get the newest data from the csv file
        String s = data.get(line)[0];
        int step_count = Integer.parseInt(s);
        String time = data.get(line)[5].substring(0, 9);
        String c = data.get(line)[10];
        float calorie = Float.parseFloat(c);
        Health_data d;
        d = new Health_data(step_count, time, calorie);
        return d;
    }
}

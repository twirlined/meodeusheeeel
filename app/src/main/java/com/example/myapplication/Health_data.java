package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class Health_data extends Activity {
    int step_count;
    String time;
    float calorie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Health_data(int step_count, String time, float calorie) {
        this.step_count = step_count;
        this.time = time;
        this.calorie = calorie;
    }
}
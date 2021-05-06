package com.example.meodeusheeeel;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import java.io.IOException;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepArray();
    }

    private void prepArray() {
        try {
            List <samsung_health> beans = new CsvToBeanBuilder(new FileReader(new InputStreamReader(getResources().openRawResource(R.raw.test.activity.day.summary)))
                    .withType(samsung_health.class).build().parse();
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.test.activity.day.summary)));//Specify asset file name
            String[] nextLine;
            // 첫줄 무시, 두번째줄 header, 3번째줄 step_count, active_time, distance, calorie 등 필요한 정보만 저장해야함.
            while ((nextLine = reader.readNext()) != null) {
                for (int i = 0; i < nextLine.length; i++) {
                    System.out.println(i + " " + nextLine[i]);
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// bean: 데이터를 저장하기 위한 클래스. 확인필요
public class samsung_health{
    @CsvBindByName
    private String step_count;

    @CsvBindByName
    private String active_time;

    @CsvBindByName
    private String distance;

    @CsvBindByName
    private String calorie;
}
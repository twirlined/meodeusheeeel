package com.example.read_csv;

import android.os.Bundle;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvToBean;
import com.opencsv.exceptions.CsvValidationException;

import java.util.ArrayList;
import java.util.Date;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVread {
    private String filename = "test"; //경로 설정 필요
    public CSVread() {}

    protected void onCreate(Bundle savedInstanceState) {
        
    }

    public List<CSVBean> readCsv() {
        List<CSVBean> data = null;
        try {
            CSVReader reader1 = new CSVReader(new FileReader(filename));
            ColumnPositionMappingStrategy<CSVBean> start = new ColumnPositionMappingStrategy<CSVBean>();
            String[] columns = new String[]{"update_time", "step_count", "calorie"};
            start.setColumnMapping(columns);
            CsvToBean<CSVBean> csv = new CsvToBean<CSVBean>();
            data = csv.parse(); // parameter?
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        }
        return data;
    }

    /*private void read() {
        try {
            CSVReader reader = new CSVReader(new FileReader(filename));
            ArrayList<Integer> step_count = new ArrayList<Integer>();
            // UTF-8
            // CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"), ",", '"', 1);
            // 첫줄 skip하고 다음줄 header, 그 다음줄
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //step_count.add(nextLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }*/


    public class CSVBean {
        @CsvBindByName(column = "update_time")
        @CsvDate("yyyy-MM-dd HH:mm:ss.sss")
        private Date updateTime;
        //2018-01-15 14:29:33.663

        @CsvBindByName(column = "step_count", required = true)
        private int stepCount;

        @CsvBindByName (column = "calorie", required = true)
        private float calorie;

    }

}

package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.opencsv.CSVReader;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    String[] tempt = new String[21];
    String[] PREV_SELECTION_DATE = new String[3];
    String ID;
    String DIFF_USER;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    double latitude;
    double longitude;

    int PICK[] = new int[3];

    List<Double> LATITUDE_ARRAY = new ArrayList<>();
    List<Double> LONGITUDE_ARRAY = new ArrayList<>();
    List<String> ALL_ID = new ArrayList<>();

    boolean STATE[] = new boolean[7];
    boolean user_alergy[] = new boolean[21];
    boolean TOGETHER_EAT[] = new boolean[41];
    boolean can_eat[] = new boolean[41];
    boolean USER_VEGAN;
    boolean DIFF_VEGAN;

    boolean DIFF_user_alergy[] = new boolean[21];
    boolean DIFF_can_eat[] = new boolean[41];

    Vector<Integer> FLUSH_NUM = new Vector<Integer>();

    float weight[] = new float[41];
    float diff_weight[] = new float[41];
    int BUDGET;
    int HEIGHT;
    int USER_WEIGHT;
    int PREV_SELECTION[] = new int[3];
    int DIFF_ACTIVATE;

    int DIFF_SPICY_LEVEL;
    int PAY_DAY;
    int SPICY_LEVEL;
    int step_count;
    String time;
    String c;
    float calorie;

    public static readCSV CSV_reader;

    private FirebaseFirestore mdb = FirebaseFirestore.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation();

        ImageButton First = (ImageButton) findViewById(R.id.First_image);
        ImageButton Second = (ImageButton) findViewById((R.id.Second_image));
        ImageButton Third = (ImageButton) findViewById(R.id.Third_image);

        TextView First_text = (TextView) findViewById(R.id.First_menu);
        TextView Second_text = (TextView) findViewById(R.id.Second_menu);
        TextView Third_text = (TextView) findViewById(R.id.Third_menu);
        TextView SELECT_ID = (TextView) findViewById(R.id.SELECT_ID);

        EditText EDIT_ID = (EditText) findViewById(R.id.USER_ID);

        Button INSERT = (Button) findViewById(R.id.INSERT);
        Button FLUSH = (Button) findViewById(R.id.FLUSH);

        Intent prev_intent = getIntent();
        ID = prev_intent.getStringExtra("ID");
        DocumentReference CLOUD_weight_DATA = mdb.collection("users").document(ID).collection("WEIGHT").document("weight");
        DocumentReference user_alergy_doc = mdb.collection("users").document(ID).collection("ALERGY").document("alergy");
        DocumentReference user_prev_selection_doc = mdb.collection("users").document(ID).collection("PREV_SELECTION")
                .document("PREV_SELECTION");
        DocumentReference CLOUD_user_info = mdb.collection("users").document(ID);

        Map<String, Object> user = new HashMap<>();
        user.put("LATITUDE", latitude);
        user.put("LONGITUDE", longitude);
        CLOUD_user_info.update(user);

        Arrays.fill(user_alergy, false);
        Arrays.fill(STATE, false);
        Arrays.fill(can_eat, true);

        ArrayList<String[]> data = new ArrayList<String[]>();
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.test)));
            String[] nextLine;
            int line = 0;
            while ((nextLine = reader.readNext()) != null) {
                data.add(nextLine);
                line++;
            }

            line = line - 1;
            String s = data.get(line)[0];
            step_count = Integer.parseInt(s);
            time = data.get(line)[5].substring(0, 10);
            c = data.get(line)[10];
            calorie = Float.parseFloat(c);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }

        int STEP = step_count;
        float CALORIE = calorie;
        String TIME_EXERCISE = time;

        String[] alergy_name = getResources().getStringArray(R.array.alergy_name);
        String[] menu_name = getResources().getStringArray(R.array.menu_name);
        String[] alergy_each_menu = getResources().getStringArray(R.array.ALERGY_EACH_MENU);
        String[] VEGAN = getResources().getStringArray(R.array.vegan_array);
        String[] PREV_SELECTION_STRING = getResources().getStringArray(R.array.PREV_SELECTION_STRING);
        String[] CATEGORY = getResources().getStringArray(R.array.분류);
        int[] spicy_level_menu = getResources().getIntArray(R.array.menu_spicy_level);

        user_alergy_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> Data = document.getData();
                        for (int i = 0; i < 21; i++) {
                            tempt[i] = Data.get(alergy_name[i]).toString();
                            if (tempt[i].equals("true")) {
                                user_alergy[i] = true;
                            } else {
                                user_alergy[i] = false;
                            }
                        }
                    }
                }
            }
        });

        CLOUD_weight_DATA.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> Data = document.getData();
                        for (int i = 0; i < 41; i++) {
                            String tempt = Data.get(menu_name[i]).toString();
                            weight[i] = Float.parseFloat(tempt);
                        }
                    }
                }
            }
        });

        user_prev_selection_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> Data = document.getData();
                        for (int i = 0; i < 3; i++) {
                            PREV_SELECTION[i] = Integer.parseInt(Data.get(PREV_SELECTION_STRING[i]).toString());
                        }
                        for (int i = 3; i < 6; i++) {
                            PREV_SELECTION_DATE[i - 3] = Data.get(PREV_SELECTION_STRING[i]).toString();
                        }
                    }
                }
            }
        });

        mdb.collection("users")
                .whereEqualTo("ACTIVATION", 1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> Data = document.getData();
                                double tm = Double.parseDouble(Data.get("LATITUDE").toString());
                                LATITUDE_ARRAY.add(Double.parseDouble(Data.get("LATITUDE").toString()));
                                LONGITUDE_ARRAY.add(Double.parseDouble(Data.get("LONGITUDE").toString()));
                                ALL_ID.add(Data.get("ID").toString());
                            }
                        }
                    }
                });

        CLOUD_user_info.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> Data = document.getData();
                        BUDGET = Integer.parseInt(Data.get("BUDGET").toString());
                        HEIGHT = Integer.parseInt(Data.get("HEIGHT").toString());
                        USER_WEIGHT = Integer.parseInt(Data.get("WEIGHT").toString());
                        PAY_DAY = Integer.parseInt(Data.get("PAY_DAY").toString());
                        SPICY_LEVEL = Integer.parseInt(Data.get("SPICY_LEVEL").toString());
                        String tempt = Data.get("VEGAN").toString();
                        if (tempt == "true") {
                            USER_VEGAN = true;
                        } else {
                            USER_VEGAN = false;
                        }
                    }
                }
            }
        });

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("YYYY-MM-dd");
        String getTime = simpleDate.format(mDate);

        Switch Activation = (Switch) findViewById(R.id.activation);
        Switch recent = (Switch) findViewById(R.id.recent_switch);
        Switch alcohol = (Switch) findViewById(R.id.alcohol_switch);
        Switch together = (Switch) findViewById(R.id.Together_switch);
        Switch random_switch = (Switch) findViewById(R.id.Random_switch);
        Switch GPS_search = (Switch) findViewById(R.id.GPS_search);

        Activation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    STATE[0] = true;
                    if (!STATE[4]) {
                        Arrays.fill(can_eat, true);

                        Map<String, Object> user = new HashMap<>();
                        user.put("ACTIVATION", 1);
                        DocumentReference tempt = mdb.collection("users").document(ID);
                        tempt.update(user);
                        if (USER_VEGAN) {
                            for (int i = 0; i < 41; i++) {
                                if (VEGAN[i].equals("false")) {
                                    can_eat[i] = false;
                                }
                            }
                        }

                        for (int i = 0; i < 21; i++) {
                            for (int j = 0; j < 41; j++) {
                                if (user_alergy[i]) {
                                    char A = alergy_each_menu[i].charAt(j);
                                    if (A == '1') {
                                        can_eat[j] = false;
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < 41; i++) {
                            if (SPICY_LEVEL < spicy_level_menu[i]) {
                                can_eat[i] = false;
                            }
                        }

                        Vector<Member> weight_array = new Vector<Member>();

                        if(STATE[1] && STATE[2]){
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    boolean check_bool = true;
                                    for (int j = 0; j < 3; j++) {
                                        if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                            String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                            String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                            String today_month = getTime.substring(5, 7);
                                            String today_day = getTime.substring(8, 10);
                                            int tempt_month_int = Integer.parseInt(tempt_month);
                                            int tempt_day_int = Integer.parseInt(tempt_day);
                                            int today_month_int = Integer.parseInt(today_month);
                                            int today_day_int = Integer.parseInt(today_day);

                                            if (today_month_int - tempt_month_int == 0) {
                                                if (today_day_int - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            } else {
                                                if (today_day_int + 30 - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (check_bool) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean second_check = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                second_check = true;
                                            }
                                        }
                                        if (second_check) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                        }
                        else if(!(STATE[1]) && STATE[2]){
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                    boolean check_bool = false;
                                    for (int j = 0; j < SPLIT_STRING.length; j++) {
                                        if (SPLIT_STRING[j].equals("안주")) {
                                            check_bool = true;
                                        }
                                    }
                                    if (check_bool) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                        }
                        else if(STATE[1] && !(STATE[2])){
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    boolean check_bool = true;
                                    for (int j = 0; j < 3; j++) {
                                        if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                            String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                            String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                            String today_month = getTime.substring(5, 7);
                                            String today_day = getTime.substring(8, 10);
                                            int tempt_month_int = Integer.parseInt(tempt_month);
                                            int tempt_day_int = Integer.parseInt(tempt_day);
                                            int today_month_int = Integer.parseInt(today_month);
                                            int today_day_int = Integer.parseInt(today_day);

                                            if (today_month_int - tempt_month_int == 0) {
                                                if (today_day_int - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            } else {
                                                if (today_day_int + 30 - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (check_bool) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                        }
                        else{
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    Member tempt_member = new Member();
                                    tempt_member.weight = weight[i];
                                    tempt_member.index = i;
                                    weight_array.add(tempt_member);
                                }
                            }
                        }

                        Collections.sort(weight_array, new MemberComparator());
                        PICK[0] = weight_array.get(0).index;
                        PICK[1] = weight_array.get(1).index;
                        PICK[2] = weight_array.get(2).index;
                        First_text.setText(menu_name[weight_array.get(0).index]);
                        Second_text.setText(menu_name[weight_array.get(1).index]);
                        Third_text.setText(menu_name[weight_array.get(2).index]);
                    }
                    else {
                        Arrays.fill(TOGETHER_EAT, true);

                        if (DIFF_ACTIVATE == 1) {
                            Arrays.fill(TOGETHER_EAT, true);
                            if (USER_VEGAN || DIFF_VEGAN) {
                                for (int i = 0; i < 41; i++) {
                                    if (VEGAN[i].equals("false")) {
                                        TOGETHER_EAT[i] = false;
                                    }
                                }
                            }

                            for (int i = 0; i < 21; i++) {
                                for (int j = 0; j < 41; j++) {
                                    if (user_alergy[i] || DIFF_user_alergy[i]) {
                                        char A = alergy_each_menu[i].charAt(j);
                                        if (A == '1') {
                                            TOGETHER_EAT[j] = false;
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < 41; i++) {
                                if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                    TOGETHER_EAT[i] = false;
                                }
                            }

                            Vector<Member> weight_array = new Vector<Member>();
                            if(STATE[2]){
                                for (int i = 0; i < 41; i++) {
                                    if (TOGETHER_EAT[i]) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean check_bool = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                check_bool = true;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                            else {
                                for (int i = 0; i < 41; i++) {
                                    if (TOGETHER_EAT[i]) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = (6 * weight[i] + 5 * diff_weight[i]) / 11;
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                            Collections.sort(weight_array, new MemberComparator());
                            PICK[0] = weight_array.get(0).index;
                            PICK[1] = weight_array.get(1).index;
                            PICK[2] = weight_array.get(2).index;
                            First_text.setText(menu_name[weight_array.get(0).index]);
                            Second_text.setText(menu_name[weight_array.get(1).index]);
                            Third_text.setText(menu_name[weight_array.get(2).index]);
                            SELECT_ID.setText(DIFF_USER);
                        }
                        else{
                            First_text.setText("CHECK FRIEND's ACTIVATION");
                            Second_text.setText("");
                            Third_text.setText("");
                        }
                    }
                } else {
                    STATE[0] = false;
                    Map<String, Object> user = new HashMap<>();
                    user.put("ACTIVATION", 0);
                    DocumentReference tempt = mdb.collection("users").document(ID);
                    tempt.update(user);
                    First_text.setText("CHECK ACTIVA");
                    Second_text.setText("CHECK ACTIVA");
                    Third_text.setText("CHECK ACTIVA");
                }
            }});

        recent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    STATE[1] = true;
                    if (STATE[0]) {
                        if(!STATE[4]) {

                            Arrays.fill(can_eat, true);

                            if (USER_VEGAN) {
                                for (int i = 0; i < 41; i++) {
                                    if (VEGAN[i].equals("false")) {
                                        can_eat[i] = false;
                                    }
                                }
                            }

                            for (int i = 0; i < 21; i++) {
                                for (int j = 0; j < 41; j++) {
                                    if (user_alergy[i]) {
                                        char A = alergy_each_menu[i].charAt(j);
                                        if (A == '1') {
                                            can_eat[j] = false;
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < 41; i++) {
                                if (SPICY_LEVEL < spicy_level_menu[i]) {
                                    can_eat[i] = false;
                                }
                            }

                            Vector<Member> weight_array = new Vector<Member>();
                            if(!STATE[2]) {
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        boolean check_bool = true;
                                        for (int j = 0; j < 3; j++) {
                                            if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                                String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                                String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                                String today_month = getTime.substring(5, 7);
                                                String today_day = getTime.substring(8, 10);
                                                int tempt_month_int = Integer.parseInt(tempt_month);
                                                int tempt_day_int = Integer.parseInt(tempt_day);
                                                int today_month_int = Integer.parseInt(today_month);
                                                int today_day_int = Integer.parseInt(today_day);

                                                if (today_month_int - tempt_month_int == 0) {
                                                    if (today_day_int - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                } else {
                                                    if (today_day_int + 30 - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                            else{
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        boolean check_bool = true;
                                        for (int j = 0; j < 3; j++) {
                                            if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                                String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                                String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                                String today_month = getTime.substring(5, 7);
                                                String today_day = getTime.substring(8, 10);
                                                int tempt_month_int = Integer.parseInt(tempt_month);
                                                int tempt_day_int = Integer.parseInt(tempt_day);
                                                int today_month_int = Integer.parseInt(today_month);
                                                int today_day_int = Integer.parseInt(today_day);

                                                if (today_month_int - tempt_month_int == 0) {
                                                    if (today_day_int - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                } else {
                                                    if (today_day_int + 30 - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        if (check_bool) {
                                            String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                            boolean second_check = false;
                                            for (int j = 0; j < SPLIT_STRING.length; j++) {
                                                if (SPLIT_STRING[j].equals("안주")) {
                                                    second_check = true;
                                                }
                                            }
                                            if (second_check) {
                                                Member tempt_member = new Member();
                                                tempt_member.weight = weight[i];
                                                tempt_member.index = i;
                                                weight_array.add(tempt_member);
                                            }
                                        }
                                    }
                                }
                            }
                            Collections.sort(weight_array, new MemberComparator());
                            PICK[0] = weight_array.get(0).index;
                            PICK[1] = weight_array.get(1).index;
                            PICK[2] = weight_array.get(2).index;
                            First_text.setText(menu_name[weight_array.get(0).index]);
                            Second_text.setText(menu_name[weight_array.get(1).index]);
                            Third_text.setText(menu_name[weight_array.get(2).index]);
                        }
                        else{
                            Arrays.fill(TOGETHER_EAT, true);

                            if (DIFF_ACTIVATE == 1) {
                                Arrays.fill(TOGETHER_EAT, true);
                                if (USER_VEGAN || DIFF_VEGAN) {
                                    for (int i = 0; i < 41; i++) {
                                        if (VEGAN[i].equals("false")) {
                                            TOGETHER_EAT[i] = false;
                                        }
                                    }
                                }

                                for (int i = 0; i < 21; i++) {
                                    for (int j = 0; j < 41; j++) {
                                        if (user_alergy[i] || DIFF_user_alergy[i]) {
                                            char A = alergy_each_menu[i].charAt(j);
                                            if (A == '1') {
                                                TOGETHER_EAT[j] = false;
                                            }
                                        }
                                    }
                                }

                                for (int i = 0; i < 41; i++) {
                                    if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                        TOGETHER_EAT[i] = false;
                                    }
                                }

                                Vector<Member> weight_array = new Vector<Member>();
                                if(STATE[2]){
                                    for (int i = 0; i < 41; i++) {
                                        if (TOGETHER_EAT[i]) {
                                            String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                            boolean check_bool = false;
                                            for (int j = 0; j < SPLIT_STRING.length; j++) {
                                                if (SPLIT_STRING[j].equals("안주")) {
                                                    check_bool = true;
                                                }
                                            }
                                            if (check_bool) {
                                                Member tempt_member = new Member();
                                                tempt_member.weight = weight[i];
                                                tempt_member.index = i;
                                                weight_array.add(tempt_member);
                                            }
                                        }
                                    }
                                }
                                else {
                                    for (int i = 0; i < 41; i++) {
                                        if (TOGETHER_EAT[i]) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = (6 * weight[i] + 5 * diff_weight[i]) / 11;
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                                Collections.sort(weight_array, new MemberComparator());
                                PICK[0] = weight_array.get(0).index;
                                PICK[1] = weight_array.get(1).index;
                                PICK[2] = weight_array.get(2).index;
                                First_text.setText(menu_name[weight_array.get(0).index]);
                                Second_text.setText(menu_name[weight_array.get(1).index]);
                                Third_text.setText(menu_name[weight_array.get(2).index]);
                                SELECT_ID.setText(DIFF_USER);
                            }
                            else{
                                First_text.setText("CHECK FRIEND's ACTIVATION");
                                Second_text.setText("");
                                Third_text.setText("");
                            }
                        }
                    }
                    else{
                        First_text.setText("CHECK ACTIVA");
                        Second_text.setText("CHECK ACTIVA");
                        Third_text.setText("CHECK ACTIVA");
                    }
                } else {
                    STATE[1] = false;
                    if(STATE[0]) {
                        if (!STATE[4]) {
                            Arrays.fill(can_eat, true);

                            Vector<Member> weight_array = new Vector<Member>();
                            if(!STATE[2]) {
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                            else{
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean check_bool = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                check_bool = true;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                            Collections.sort(weight_array, new MemberComparator());
                            PICK[0] = weight_array.get(0).index;
                            PICK[1] = weight_array.get(1).index;
                            PICK[2] = weight_array.get(2).index;
                            First_text.setText(menu_name[weight_array.get(0).index]);
                            Second_text.setText(menu_name[weight_array.get(1).index]);
                            Third_text.setText(menu_name[weight_array.get(2).index]);
                        }
                        else{
                            Arrays.fill(TOGETHER_EAT, true);

                            if (DIFF_ACTIVATE == 1) {
                                Arrays.fill(TOGETHER_EAT, true);
                                if (USER_VEGAN || DIFF_VEGAN) {
                                    for (int i = 0; i < 41; i++) {
                                        if (VEGAN[i].equals("false")) {
                                            TOGETHER_EAT[i] = false;
                                        }
                                    }
                                }

                                for (int i = 0; i < 21; i++) {
                                    for (int j = 0; j < 41; j++) {
                                        if (user_alergy[i] || DIFF_user_alergy[i]) {
                                            char A = alergy_each_menu[i].charAt(j);
                                            if (A == '1') {
                                                TOGETHER_EAT[j] = false;
                                            }
                                        }
                                    }
                                }

                                for (int i = 0; i < 41; i++) {
                                    if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                        TOGETHER_EAT[i] = false;
                                    }
                                }

                                Vector<Member> weight_array = new Vector<Member>();
                                if(STATE[2]){
                                    for (int i = 0; i < 41; i++) {
                                        if (TOGETHER_EAT[i]) {
                                            String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                            boolean check_bool = false;
                                            for (int j = 0; j < SPLIT_STRING.length; j++) {
                                                if (SPLIT_STRING[j].equals("안주")) {
                                                    check_bool = true;
                                                }
                                            }
                                            if (check_bool) {
                                                Member tempt_member = new Member();
                                                tempt_member.weight = weight[i];
                                                tempt_member.index = i;
                                                weight_array.add(tempt_member);
                                            }
                                        }
                                    }
                                }
                                else {
                                    for (int i = 0; i < 41; i++) {
                                        if (TOGETHER_EAT[i]) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = (6 * weight[i] + 5 * diff_weight[i]) / 11;
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                                Collections.sort(weight_array, new MemberComparator());
                                PICK[0] = weight_array.get(0).index;
                                PICK[1] = weight_array.get(1).index;
                                PICK[2] = weight_array.get(2).index;
                                First_text.setText(menu_name[weight_array.get(0).index]);
                                Second_text.setText(menu_name[weight_array.get(1).index]);
                                Third_text.setText(menu_name[weight_array.get(2).index]);
                                SELECT_ID.setText(DIFF_USER);
                            }
                            else{
                                First_text.setText("CHECK FRIEND's ACTIVATION");
                                Second_text.setText("");
                                Third_text.setText("");
                            }
                        }
                    }
                    else{
                        First_text.setText("CHECK ACTIVA");
                        Second_text.setText("CHECK ACTIVA");
                        Third_text.setText("CHECK ACTIVA");
                    }
                }
            }
        });

        alcohol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    STATE[2] = true;
                    if (STATE[0]) {
                        if(!STATE[4]) {

                            Arrays.fill(can_eat, true);

                            if (USER_VEGAN) {
                                for (int i = 0; i < 41; i++) {
                                    if (VEGAN[i].equals("false")) {
                                        can_eat[i] = false;
                                    }
                                }
                            }

                            for (int i = 0; i < 21; i++) {
                                for (int j = 0; j < 41; j++) {
                                    if (user_alergy[i]) {
                                        char A = alergy_each_menu[i].charAt(j);
                                        if (A == '1') {
                                            can_eat[j] = false;
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < 41; i++) {
                                if (SPICY_LEVEL < spicy_level_menu[i]) {
                                    can_eat[i] = false;
                                }
                            }

                            Vector<Member> weight_array = new Vector<Member>();
                            if(!STATE[1]) {
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean check_bool = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                check_bool = true;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                            else{
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        boolean check_bool = true;
                                        for (int j = 0; j < 3; j++) {
                                            if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                                String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                                String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                                String today_month = getTime.substring(5, 7);
                                                String today_day = getTime.substring(8, 10);
                                                int tempt_month_int = Integer.parseInt(tempt_month);
                                                int tempt_day_int = Integer.parseInt(tempt_day);
                                                int today_month_int = Integer.parseInt(today_month);
                                                int today_day_int = Integer.parseInt(today_day);

                                                if (today_month_int - tempt_month_int == 0) {
                                                    if (today_day_int - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                } else {
                                                    if (today_day_int + 30 - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        if (check_bool) {
                                            String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                            boolean second_check = false;
                                            for (int j = 0; j < SPLIT_STRING.length; j++) {
                                                if (SPLIT_STRING[j].equals("안주")) {
                                                    second_check = true;
                                                }
                                            }
                                            if (second_check) {
                                                Member tempt_member = new Member();
                                                tempt_member.weight = weight[i];
                                                tempt_member.index = i;
                                                weight_array.add(tempt_member);
                                            }
                                        }
                                    }
                                }
                            }
                            Collections.sort(weight_array, new MemberComparator());
                            PICK[0] = weight_array.get(0).index;
                            PICK[1] = weight_array.get(1).index;
                            PICK[2] = weight_array.get(2).index;
                            First_text.setText(menu_name[weight_array.get(0).index]);
                            Second_text.setText(menu_name[weight_array.get(1).index]);
                            Third_text.setText(menu_name[weight_array.get(2).index]);
                        }
                        else{
                            Arrays.fill(TOGETHER_EAT, true);

                            if (DIFF_ACTIVATE == 1) {
                                Arrays.fill(TOGETHER_EAT, true);
                                if (USER_VEGAN || DIFF_VEGAN) {
                                    for (int i = 0; i < 41; i++) {
                                        if (VEGAN[i].equals("false")) {
                                            TOGETHER_EAT[i] = false;
                                        }
                                    }
                                }

                                for (int i = 0; i < 21; i++) {
                                    for (int j = 0; j < 41; j++) {
                                        if (user_alergy[i] || DIFF_user_alergy[i]) {
                                            char A = alergy_each_menu[i].charAt(j);
                                            if (A == '1') {
                                                TOGETHER_EAT[j] = false;
                                            }
                                        }
                                    }
                                }

                                for (int i = 0; i < 41; i++) {
                                    if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                        TOGETHER_EAT[i] = false;
                                    }
                                }

                                Vector<Member> weight_array = new Vector<Member>();
                                for (int i = 0; i < 41; i++) {
                                    if (TOGETHER_EAT[i]) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean check_bool = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                check_bool = true;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                                Collections.sort(weight_array, new MemberComparator());
                                PICK[0] = weight_array.get(0).index;
                                PICK[1] = weight_array.get(1).index;
                                PICK[2] = weight_array.get(2).index;
                                First_text.setText(menu_name[weight_array.get(0).index]);
                                Second_text.setText(menu_name[weight_array.get(1).index]);
                                Third_text.setText(menu_name[weight_array.get(2).index]);
                                SELECT_ID.setText(DIFF_USER);
                            }
                            else{
                                First_text.setText("CHECK FRIEND's ACTIVATION");
                                Second_text.setText("");
                                Third_text.setText("");
                            }
                        }
                    }
                    else{
                        First_text.setText("CHECK ACTIVA");
                        Second_text.setText("CHECK ACTIVA");
                        Third_text.setText("CHECK ACTIVA");
                    }
                }
                else {
                    if (STATE[0]) {
                        STATE[2] = false;
                        if(!STATE[4]) {

                            Arrays.fill(can_eat, true);

                            if (USER_VEGAN) {
                                for (int i = 0; i < 41; i++) {
                                    if (VEGAN[i].equals("false")) {
                                        can_eat[i] = false;
                                    }
                                }
                            }

                            for (int i = 0; i < 21; i++) {
                                for (int j = 0; j < 41; j++) {
                                    if (user_alergy[i]) {
                                        char A = alergy_each_menu[i].charAt(j);
                                        if (A == '1') {
                                            can_eat[j] = false;
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < 41; i++) {
                                if (SPICY_LEVEL < spicy_level_menu[i]) {
                                    can_eat[i] = false;
                                }
                            }

                            Vector<Member> weight_array = new Vector<Member>();
                            if(STATE[1]) {
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        boolean check_bool = true;
                                        for (int j = 0; j < 3; j++) {
                                            if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                                String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                                String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                                String today_month = getTime.substring(5, 7);
                                                String today_day = getTime.substring(8, 10);
                                                int tempt_month_int = Integer.parseInt(tempt_month);
                                                int tempt_day_int = Integer.parseInt(tempt_day);
                                                int today_month_int = Integer.parseInt(today_month);
                                                int today_day_int = Integer.parseInt(today_day);

                                                if (today_month_int - tempt_month_int == 0) {
                                                    if (today_day_int - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                } else {
                                                    if (today_day_int + 30 - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                            else{
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                            Collections.sort(weight_array, new MemberComparator());
                            PICK[0] = weight_array.get(0).index;
                            PICK[1] = weight_array.get(1).index;
                            PICK[2] = weight_array.get(2).index;
                            First_text.setText(menu_name[weight_array.get(0).index]);
                            Second_text.setText(menu_name[weight_array.get(1).index]);
                            Third_text.setText(menu_name[weight_array.get(2).index]);
                        }
                        else{
                            Arrays.fill(TOGETHER_EAT, true);

                            if (DIFF_ACTIVATE == 1) {
                                Arrays.fill(TOGETHER_EAT, true);
                                if (USER_VEGAN || DIFF_VEGAN) {
                                    for (int i = 0; i < 41; i++) {
                                        if (VEGAN[i].equals("false")) {
                                            TOGETHER_EAT[i] = false;
                                        }
                                    }
                                }

                                for (int i = 0; i < 21; i++) {
                                    for (int j = 0; j < 41; j++) {
                                        if (user_alergy[i] || DIFF_user_alergy[i]) {
                                            char A = alergy_each_menu[i].charAt(j);
                                            if (A == '1') {
                                                TOGETHER_EAT[j] = false;
                                            }
                                        }
                                    }
                                }

                                for (int i = 0; i < 41; i++) {
                                    if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                        TOGETHER_EAT[i] = false;
                                    }
                                }

                                Vector<Member> weight_array = new Vector<Member>();
                                for (int i = 0; i < 41; i++) {
                                    if (TOGETHER_EAT[i]) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                                Collections.sort(weight_array, new MemberComparator());
                                PICK[0] = weight_array.get(0).index;
                                PICK[1] = weight_array.get(1).index;
                                PICK[2] = weight_array.get(2).index;
                                First_text.setText(menu_name[weight_array.get(0).index]);
                                Second_text.setText(menu_name[weight_array.get(1).index]);
                                Third_text.setText(menu_name[weight_array.get(2).index]);
                                SELECT_ID.setText(DIFF_USER);
                            }
                            else{
                                First_text.setText("CHECK FRIEND's ACTIVATION");
                                Second_text.setText("");
                                Third_text.setText("");
                            }
                        }
                    }
                    else{
                        First_text.setText("CHECK ACTIVA");
                        Second_text.setText("CHECK ACTIVA");
                        Third_text.setText("CHECK ACTIVA");
                    }
                }
            }
        });

        together.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (STATE[0]) {
                        STATE[4] = true;

                        Arrays.fill(TOGETHER_EAT, true);

                        if(DIFF_ACTIVATE == 1){
                            Arrays.fill(TOGETHER_EAT, true);
                            if (USER_VEGAN || DIFF_VEGAN ) {
                                for (int i = 0; i < 41; i++) {
                                    if (VEGAN[i].equals("false")) {
                                        TOGETHER_EAT[i] = false;
                                    }
                                }
                            }

                            for (int i = 0; i < 21; i++) {
                                for (int j = 0; j < 41; j++) {
                                    if (user_alergy[i] || DIFF_user_alergy[i]) {
                                        char A = alergy_each_menu[i].charAt(j);
                                        if (A == '1') {
                                            TOGETHER_EAT[j] = false;
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < 41; i++) {
                                if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                    TOGETHER_EAT[i] = false;
                                }
                            }

                            Vector<Member> weight_array = new Vector<Member>();
                            if(STATE[2]){
                                for (int i = 0; i < 41; i++) {
                                    if (TOGETHER_EAT[i]) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean check_bool = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                check_bool = true;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                            else {
                                for (int i = 0; i < 41; i++) {
                                    if (TOGETHER_EAT[i]) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = (6 * weight[i] + 5 * diff_weight[i]) / 11;
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                            Collections.sort(weight_array, new MemberComparator());
                            PICK[0] = weight_array.get(0).index;
                            PICK[1] = weight_array.get(1).index;
                            PICK[2] = weight_array.get(2).index;
                            First_text.setText(menu_name[weight_array.get(0).index]);
                            Second_text.setText(menu_name[weight_array.get(1).index]);
                            Third_text.setText(menu_name[weight_array.get(2).index]);
                            SELECT_ID.setText(DIFF_USER);
                        }
                        else{
                            First_text.setText("CHECK FRIEND's ACTIVATION");
                            Second_text.setText("");
                            Third_text.setText("");
                        }
                    }
                    else{
                        First_text.setText("CHECK ACTIVA");
                        Second_text.setText("CHECK ACTIVA");
                        Third_text.setText("CHECK ACTIVA");
                    }
                }
                else {
                    STATE[4] = false;
                    if(STATE[0]) {
                        Arrays.fill(can_eat, true);

                        Map<String, Object> user = new HashMap<>();
                        user.put("ACTIVATION", 1);
                        DocumentReference tempt = mdb.collection("users").document(ID);
                        tempt.update(user);
                        if (USER_VEGAN) {
                            for (int i = 0; i < 41; i++) {
                                if (VEGAN[i].equals("false")) {
                                    can_eat[i] = false;
                                }
                            }
                        }

                        for (int i = 0; i < 21; i++) {
                            for (int j = 0; j < 41; j++) {
                                if (user_alergy[i]) {
                                    char A = alergy_each_menu[i].charAt(j);
                                    if (A == '1') {
                                        can_eat[j] = false;
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < 41; i++) {
                            if (SPICY_LEVEL < spicy_level_menu[i]) {
                                can_eat[i] = false;
                            }
                        }

                        Vector<Member> weight_array = new Vector<Member>();

                        if (STATE[1] && STATE[2]) {
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    boolean check_bool = true;
                                    for (int j = 0; j < 3; j++) {
                                        if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                            String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                            String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                            String today_month = getTime.substring(5, 7);
                                            String today_day = getTime.substring(8, 10);
                                            int tempt_month_int = Integer.parseInt(tempt_month);
                                            int tempt_day_int = Integer.parseInt(tempt_day);
                                            int today_month_int = Integer.parseInt(today_month);
                                            int today_day_int = Integer.parseInt(today_day);

                                            if (today_month_int - tempt_month_int == 0) {
                                                if (today_day_int - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            } else {
                                                if (today_day_int + 30 - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (check_bool) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean second_check = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                second_check = true;
                                            }
                                        }
                                        if (second_check) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                        } else if (!(STATE[1]) && STATE[2]) {
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                    boolean check_bool = false;
                                    for (int j = 0; j < SPLIT_STRING.length; j++) {
                                        if (SPLIT_STRING[j].equals("안주")) {
                                            check_bool = true;
                                        }
                                    }
                                    if (check_bool) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                        } else if (STATE[1] && !(STATE[2])) {
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    boolean check_bool = true;
                                    for (int j = 0; j < 3; j++) {
                                        if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                            String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                            String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                            String today_month = getTime.substring(5, 7);
                                            String today_day = getTime.substring(8, 10);
                                            int tempt_month_int = Integer.parseInt(tempt_month);
                                            int tempt_day_int = Integer.parseInt(tempt_day);
                                            int today_month_int = Integer.parseInt(today_month);
                                            int today_day_int = Integer.parseInt(today_day);

                                            if (today_month_int - tempt_month_int == 0) {
                                                if (today_day_int - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            } else {
                                                if (today_day_int + 30 - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (check_bool) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                        } else {
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    Member tempt_member = new Member();
                                    tempt_member.weight = weight[i];
                                    tempt_member.index = i;
                                    weight_array.add(tempt_member);
                                }
                            }
                        }

                        Collections.sort(weight_array, new MemberComparator());
                        PICK[0] = weight_array.get(0).index;
                        PICK[1] = weight_array.get(1).index;
                        PICK[2] = weight_array.get(2).index;
                        First_text.setText(menu_name[weight_array.get(0).index]);
                        Second_text.setText(menu_name[weight_array.get(1).index]);
                        Third_text.setText(menu_name[weight_array.get(2).index]);
                    }
                    else{
                        First_text.setText("CHECK ACTIVA");
                        Second_text.setText("CHECK ACTIVA");
                        Third_text.setText("CHECK ACTIVA");
                    }
                }
            }
        });

        GPS_search.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    STATE[5] = true;
                    for (int i = 0; i < LATITUDE_ARRAY.size(); i++) {
                        if (ALL_ID.get(i).toString().equals(ID)) {
                            continue;
                        } else {
                            if (getDistance(latitude, longitude, Double.parseDouble(LATITUDE_ARRAY.get(i).toString()),
                                    Double.parseDouble(LONGITUDE_ARRAY.get(i).toString()))) {
                                DIFF_USER = ALL_ID.get(i);

                                DocumentReference CLOUD_weight_DATA = mdb.collection("users").document(DIFF_USER).collection("WEIGHT").document("weight");
                                DocumentReference user_alergy_doc = mdb.collection("users").document(DIFF_USER).collection("ALERGY").document("alergy");
                                DocumentReference CLOUD_user_info = mdb.collection("users").document(DIFF_USER);

                                user_alergy_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Map<String, Object> Data = document.getData();
                                                for (int i = 0; i < 21; i++) {
                                                    tempt[i] = Data.get(alergy_name[i]).toString();
                                                    if (tempt[i].equals("true")) {
                                                        DIFF_user_alergy[i] = true;
                                                    } else {
                                                        DIFF_user_alergy[i] = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });

                                CLOUD_weight_DATA.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Map<String, Object> Data = document.getData();
                                                for (int i = 0; i < 41; i++) {
                                                    String tempt = Data.get(menu_name[i]).toString();
                                                    diff_weight[i] = Float.parseFloat(tempt);
                                                }
                                            }
                                        }
                                    }
                                });

                                CLOUD_user_info.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Map<String, Object> Data = document.getData();
                                                DIFF_SPICY_LEVEL = Integer.parseInt(Data.get("SPICY_LEVEL").toString());
                                                DIFF_ACTIVATE = Integer.parseInt(Data.get("ACTIVATION").toString());
                                                String tempt = Data.get("VEGAN").toString();
                                                if (tempt == "true") {
                                                    DIFF_VEGAN = true;
                                                } else {
                                                    DIFF_VEGAN = false;
                                                }
                                            }
                                        }
                                    }
                                });
                                break;
                            }
                        }
                    }
                } else {
                    STATE[5] = false;
                }
            }
        });

        random_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    STATE[6] = true;
                    if (STATE[0]) {
                        if(!STATE[4]) {
                            Arrays.fill(can_eat, true);

                            if (USER_VEGAN) {
                                for (int i = 0; i < 41; i++) {
                                    if (VEGAN[i].equals("false")) {
                                        can_eat[i] = false;
                                    }
                                }
                            }

                            for (int i = 0; i < 21; i++) {
                                for (int j = 0; j < 41; j++) {
                                    if (user_alergy[i]) {
                                        char A = alergy_each_menu[i].charAt(j);
                                        if (A == '1') {
                                            can_eat[j] = false;
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < 41; i++) {
                                if (SPICY_LEVEL < spicy_level_menu[i]) {
                                    can_eat[i] = false;
                                }
                            }

                            Random rnd = new Random();
                            rnd.setSeed(System.currentTimeMillis());

                            Vector<Member> weight_array = new Vector<Member>();
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    Member tempt_member = new Member();
                                    tempt_member.weight = weight[i];
                                    tempt_member.index = i;
                                    weight_array.add(tempt_member);
                                }
                            }
                            int random_array[] = new int[3];
                            for (int i = 0; i < 3; i++) {
                                random_array[i] = rnd.nextInt(weight_array.size());
                                for (int j = 0; j < i; j++) {
                                    if (random_array[j] == random_array[i]) {
                                        i--;
                                        break;
                                    }
                                }
                            }
                            PICK[0] = random_array[0];
                            PICK[1] = random_array[1];
                            PICK[2] = random_array[2];
                            First_text.setText(menu_name[random_array[0]]);
                            Second_text.setText(menu_name[random_array[1]]);
                            Third_text.setText(menu_name[random_array[2]]);
                        }
                        else{

                            if(DIFF_ACTIVATE == 1){
                                Arrays.fill(TOGETHER_EAT, true);
                                if (USER_VEGAN || DIFF_VEGAN ) {
                                    for (int i = 0; i < 41; i++) {
                                        if (VEGAN[i].equals("false")) {
                                            TOGETHER_EAT[i] = false;
                                        }
                                    }
                                }

                                for (int i = 0; i < 21; i++) {
                                    for (int j = 0; j < 41; j++) {
                                        if (user_alergy[i] || DIFF_user_alergy[i]) {
                                            char A = alergy_each_menu[i].charAt(j);
                                            if (A == '1') {
                                                TOGETHER_EAT[j] = false;
                                            }
                                        }
                                    }
                                }

                                for (int i = 0; i < 41; i++) {
                                    if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                        TOGETHER_EAT[i] = false;
                                    }
                                }

                                Random rnd = new Random();
                                rnd.setSeed(System.currentTimeMillis());
                                Vector<Member> weight_array = new Vector<Member>();

                                for (int i = 0; i < 41; i++) {
                                    if (TOGETHER_EAT[i]) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                                int random_array[] = new int[3];
                                for (int i = 0; i < 3; i++) {
                                    random_array[i] = rnd.nextInt(weight_array.size());
                                    for (int j = 0; j < i; j++) {
                                        if (random_array[j] == random_array[i]) {
                                            i--;
                                            break;
                                        }
                                    }
                                }
                                PICK[0] = random_array[0];
                                PICK[1] = random_array[1];
                                PICK[2] = random_array[2];
                                First_text.setText(menu_name[random_array[0]]);
                                Second_text.setText(menu_name[random_array[1]]);
                                Third_text.setText(menu_name[random_array[2]]);
                                SELECT_ID.setText(DIFF_USER);
                            }
                            else{
                                First_text.setText("CHECK FRIEND's ACTIVATION");
                                Second_text.setText("");
                                Third_text.setText("");
                            }
                        }
                    }
                    else{
                        First_text.setText("CHECK ACTIVA");
                        Second_text.setText("CHECK ACTIVA");
                        Third_text.setText("CHECK ACTIVA");
                    }
                }
                else {
                    STATE[6] = false;
                    if (STATE[0]) {
                        if(!STATE[4]) {
                            Arrays.fill(can_eat, true);

                            if (USER_VEGAN) {
                                for (int i = 0; i < 41; i++) {
                                    if (VEGAN[i].equals("false")) {
                                        can_eat[i] = false;
                                    }
                                }
                            }

                            for (int i = 0; i < 21; i++) {
                                for (int j = 0; j < 41; j++) {
                                    if (user_alergy[i]) {
                                        char A = alergy_each_menu[i].charAt(j);
                                        if (A == '1') {
                                            can_eat[j] = false;
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < 41; i++) {
                                if (SPICY_LEVEL < spicy_level_menu[i]) {
                                    can_eat[i] = false;
                                }
                            }

                            Vector<Member> weight_array = new Vector<Member>();

                            if (STATE[1] && STATE[2]) {
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        boolean check_bool = true;
                                        for (int j = 0; j < 3; j++) {
                                            if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                                String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                                String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                                String today_month = getTime.substring(5, 7);
                                                String today_day = getTime.substring(8, 10);
                                                int tempt_month_int = Integer.parseInt(tempt_month);
                                                int tempt_day_int = Integer.parseInt(tempt_day);
                                                int today_month_int = Integer.parseInt(today_month);
                                                int today_day_int = Integer.parseInt(today_day);

                                                if (today_month_int - tempt_month_int == 0) {
                                                    if (today_day_int - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                } else {
                                                    if (today_day_int + 30 - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        if (check_bool) {
                                            String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                            boolean second_check = false;
                                            for (int j = 0; j < SPLIT_STRING.length; j++) {
                                                if (SPLIT_STRING[j].equals("안주")) {
                                                    second_check = true;
                                                }
                                            }
                                            if (second_check) {
                                                Member tempt_member = new Member();
                                                tempt_member.weight = weight[i];
                                                tempt_member.index = i;
                                                weight_array.add(tempt_member);
                                            }
                                        }
                                    }
                                }
                            } else if (!(STATE[1]) && STATE[2]) {
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean check_bool = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                check_bool = true;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            } else if (STATE[1] && !(STATE[2])) {
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        boolean check_bool = true;
                                        for (int j = 0; j < 3; j++) {
                                            if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                                String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                                String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                                String today_month = getTime.substring(5, 7);
                                                String today_day = getTime.substring(8, 10);
                                                int tempt_month_int = Integer.parseInt(tempt_month);
                                                int tempt_day_int = Integer.parseInt(tempt_day);
                                                int today_month_int = Integer.parseInt(today_month);
                                                int today_day_int = Integer.parseInt(today_day);

                                                if (today_month_int - tempt_month_int == 0) {
                                                    if (today_day_int - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                } else {
                                                    if (today_day_int + 30 - tempt_day_int <= 4) {
                                                        check_bool = false;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                        if (check_bool) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < 41; i++) {
                                    if (can_eat[i]) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }

                            Collections.sort(weight_array, new MemberComparator());
                            PICK[0] = weight_array.get(0).index;
                            PICK[1] = weight_array.get(1).index;
                            PICK[2] = weight_array.get(2).index;
                            First_text.setText(menu_name[weight_array.get(0).index]);
                            Second_text.setText(menu_name[weight_array.get(1).index]);
                            Third_text.setText(menu_name[weight_array.get(2).index]);
                        }
                        else{
                            Arrays.fill(TOGETHER_EAT, true);

                            if (DIFF_ACTIVATE == 1) {
                                Arrays.fill(TOGETHER_EAT, true);
                                if (USER_VEGAN || DIFF_VEGAN) {
                                    for (int i = 0; i < 41; i++) {
                                        if (VEGAN[i].equals("false")) {
                                            TOGETHER_EAT[i] = false;
                                        }
                                    }
                                }

                                for (int i = 0; i < 21; i++) {
                                    for (int j = 0; j < 41; j++) {
                                        if (user_alergy[i] || DIFF_user_alergy[i]) {
                                            char A = alergy_each_menu[i].charAt(j);
                                            if (A == '1') {
                                                TOGETHER_EAT[j] = false;
                                            }
                                        }
                                    }
                                }

                                for (int i = 0; i < 41; i++) {
                                    if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                        TOGETHER_EAT[i] = false;
                                    }
                                }

                                Vector<Member> weight_array = new Vector<Member>();
                                if(STATE[2]){
                                    for (int i = 0; i < 41; i++) {
                                        if (TOGETHER_EAT[i]) {
                                            String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                            boolean check_bool = false;
                                            for (int j = 0; j < SPLIT_STRING.length; j++) {
                                                if (SPLIT_STRING[j].equals("안주")) {
                                                    check_bool = true;
                                                }
                                            }
                                            if (check_bool) {
                                                Member tempt_member = new Member();
                                                tempt_member.weight = weight[i];
                                                tempt_member.index = i;
                                                weight_array.add(tempt_member);
                                            }
                                        }
                                    }
                                }
                                else {
                                    for (int i = 0; i < 41; i++) {
                                        if (TOGETHER_EAT[i]) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = (6 * weight[i] + 5 * diff_weight[i]) / 11;
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                                Collections.sort(weight_array, new MemberComparator());
                                PICK[0] = weight_array.get(0).index;
                                PICK[1] = weight_array.get(1).index;
                                PICK[2] = weight_array.get(2).index;
                                First_text.setText(menu_name[weight_array.get(0).index]);
                                Second_text.setText(menu_name[weight_array.get(1).index]);
                                Third_text.setText(menu_name[weight_array.get(2).index]);
                                SELECT_ID.setText(DIFF_USER);
                            }
                            else{
                                First_text.setText("CHECK FRIEND's ACTIVATION");
                                Second_text.setText("");
                                Third_text.setText("");
                            }
                        }
                    }
                    else{
                        First_text.setText("CHECK ACTIVA");
                        Second_text.setText("CHECK ACTIVA");
                        Third_text.setText("CHECK ACTIVA");
                    }
                }
            }
        });

        First.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(!STATE[4]){
                    weight[PICK[0]] = weight[PICK[0]] + 0.7f;
                    weight[PICK[1]] = weight[PICK[1]] - 0.3f;
                    weight[PICK[2]] = weight[PICK[2]] - 0.2f;

                    PREV_SELECTION[2] = PREV_SELECTION[1];
                    PREV_SELECTION[1] = PREV_SELECTION[0];
                    PREV_SELECTION[0] = PICK[0];
                    PREV_SELECTION_DATE[2] = PREV_SELECTION_DATE[1];
                    PREV_SELECTION_DATE[1] = PREV_SELECTION_DATE[0];
                    PREV_SELECTION_DATE[0] = getTime;

                    Map<String, Object> user_selection = new HashMap<>();

                    user_selection.put("FIRST_SELECTION", PREV_SELECTION[0]);
                    user_selection.put("SECOND_SELECTION", PREV_SELECTION[1]);
                    user_selection.put("THIRD_SELECTION", PREV_SELECTION[2]);
                    user_selection.put("FIRST_SELECTION_DATE", PREV_SELECTION_DATE[0]);
                    user_selection.put("SECOND_SELECTION_DATE", PREV_SELECTION_DATE[1]);
                    user_selection.put("THIRD_SELECTION_DATE", PREV_SELECTION_DATE[2]);

                    DocumentReference new_PREV = mdb.collection("users").document(ID).collection("PREV_SELECTION").document("PREV_SELECTION");
                    new_PREV.update(user_selection);

                    Map<String, Object> user = new HashMap<>();

                    user.put(menu_name[PICK[0]], weight[PICK[0]]);
                    user.put(menu_name[PICK[1]], weight[PICK[1]]);
                    user.put(menu_name[PICK[2]], weight[PICK[2]]);
                    DocumentReference tempt = mdb.collection("users").document(ID).collection("WEIGHT").document("weight");
                    tempt.update(user);

                    Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                    intent.putExtra("data", menu_name[PICK[0]]);
                    startActivityForResult(intent, 1);



                }
            }
        });

        Second.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(!STATE[4]){
                    weight[PICK[0]] = weight[PICK[0]] - 0.5f;
                    weight[PICK[1]] = weight[PICK[1]] + 0.9f;
                    weight[PICK[2]] = weight[PICK[2]] - 0.2f;

                    PREV_SELECTION[2] = PREV_SELECTION[1];
                    PREV_SELECTION[1] = PREV_SELECTION[0];
                    PREV_SELECTION[0] = PICK[1];
                    PREV_SELECTION_DATE[2] = PREV_SELECTION_DATE[1];
                    PREV_SELECTION_DATE[1] = PREV_SELECTION_DATE[0];
                    PREV_SELECTION_DATE[0] = getTime;

                    Map<String, Object> user_selection = new HashMap<>();

                    user_selection.put("FIRST_SELECTION", PREV_SELECTION[0]);
                    user_selection.put("SECOND_SELECTION", PREV_SELECTION[1]);
                    user_selection.put("THIRD_SELECTION", PREV_SELECTION[2]);
                    user_selection.put("FIRST_SELECTION_DATE", PREV_SELECTION_DATE[0]);
                    user_selection.put("SECOND_SELECTION_DATE", PREV_SELECTION_DATE[1]);
                    user_selection.put("THIRD_SELECTION_DATE", PREV_SELECTION_DATE[2]);

                    DocumentReference new_PREV = mdb.collection("users").document(ID).collection("PREV_SELECTION").document("PREV_SELECTION");
                    new_PREV.update(user_selection);

                    Map<String, Object> user = new HashMap<>();
                    user.put(menu_name[PICK[0]], weight[PICK[0]]);
                    user.put(menu_name[PICK[1]], weight[PICK[1]]);
                    user.put(menu_name[PICK[2]], weight[PICK[2]]);
                    DocumentReference tempt = mdb.collection("users").document(ID).collection("WEIGHT").document("weight");
                    tempt.update(user);

                    Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                    intent.putExtra("data", menu_name[PICK[1]]);
                    startActivityForResult(intent, 1);

                }
            }
        });

        Third.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(!STATE[4]){
                    weight[PICK[0]] = weight[PICK[0]] - 0.5f;
                    weight[PICK[1]] = weight[PICK[1]] - 0.4f;
                    weight[PICK[2]] = weight[PICK[2]] + 1.2f;

                    PREV_SELECTION[2] = PREV_SELECTION[1];
                    PREV_SELECTION[1] = PREV_SELECTION[0];
                    PREV_SELECTION[0] = PICK[2];
                    PREV_SELECTION_DATE[2] = PREV_SELECTION_DATE[1];
                    PREV_SELECTION_DATE[1] = PREV_SELECTION_DATE[0];
                    PREV_SELECTION_DATE[0] = getTime;

                    Map<String, Object> user_selection = new HashMap<>();

                    user_selection.put("FIRST_SELECTION", PREV_SELECTION[0]);
                    user_selection.put("SECOND_SELECTION", PREV_SELECTION[1]);
                    user_selection.put("THIRD_SELECTION", PREV_SELECTION[2]);
                    user_selection.put("FIRST_SELECTION_DATE", PREV_SELECTION_DATE[0]);
                    user_selection.put("SECOND_SELECTION_DATE", PREV_SELECTION_DATE[1]);
                    user_selection.put("THIRD_SELECTION_DATE", PREV_SELECTION_DATE[2]);

                    DocumentReference new_PREV = mdb.collection("users").document(ID).collection("PREV_SELECTION").document("PREV_SELECTION");
                    new_PREV.update(user_selection);

                    Map<String, Object> user = new HashMap<>();
                    user.put(menu_name[PICK[0]], weight[PICK[0]]);
                    user.put(menu_name[PICK[1]], weight[PICK[1]]);
                    user.put(menu_name[PICK[2]], weight[PICK[2]]);
                    DocumentReference tempt = mdb.collection("users").document(ID).collection("WEIGHT").document("weight");
                    tempt.update(user);

                    Intent intent = new Intent(MainActivity.this, PopupActivity.class);
                    intent.putExtra("data", menu_name[PICK[2]]);
                    startActivityForResult(intent, 1);
                }
            }
        });

        FLUSH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(STATE[0]) {
                    if (!(STATE[4])) {
                        FLUSH_NUM.add(PICK[0]);
                        FLUSH_NUM.add(PICK[1]);
                        FLUSH_NUM.add(PICK[2]);

                        Arrays.fill(can_eat, true);

                        if (USER_VEGAN) {
                            for (int i = 0; i < 41; i++) {
                                if (VEGAN[i].equals("false")) {
                                    can_eat[i] = false;
                                }
                            }
                        }

                        for (int i = 0; i < 21; i++) {
                            for (int j = 0; j < 41; j++) {
                                if (user_alergy[i]) {
                                    char A = alergy_each_menu[i].charAt(j);
                                    if (A == '1') {
                                        can_eat[j] = false;
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < 41; i++) {
                            if (SPICY_LEVEL < spicy_level_menu[i]) {
                                can_eat[i] = false;
                            }
                        }

                        for(Integer i : FLUSH_NUM){
                            can_eat[i] = false;
                        }

                        weight[PICK[0]] = weight[PICK[0]] - 0.2f;
                        weight[PICK[1]] = weight[PICK[1]] - 0.2f;
                        weight[PICK[2]] = weight[PICK[2]] - 0.2f;
                        Map<String, Object> user = new HashMap<>();
                        user.put(menu_name[PICK[0]], weight[PICK[0]]);
                        user.put(menu_name[PICK[1]], weight[PICK[1]]);
                        user.put(menu_name[PICK[2]], weight[PICK[2]]);
                        DocumentReference tempt = mdb.collection("users").document(ID).collection("WEIGHT").document("weight");
                        tempt.update(user);

                        Vector<Member> weight_array = new Vector<Member>();
                        if(STATE[1] && STATE[2]){
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    boolean check_bool = true;
                                    for (int j = 0; j < 3; j++) {
                                        if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                            String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                            String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                            String today_month = getTime.substring(5, 7);
                                            String today_day = getTime.substring(8, 10);
                                            int tempt_month_int = Integer.parseInt(tempt_month);
                                            int tempt_day_int = Integer.parseInt(tempt_day);
                                            int today_month_int = Integer.parseInt(today_month);
                                            int today_day_int = Integer.parseInt(today_day);

                                            if (today_month_int - tempt_month_int == 0) {
                                                if (today_day_int - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            } else {
                                                if (today_day_int + 30 - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (check_bool) {
                                        String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                        boolean second_check = false;
                                        for (int j = 0; j < SPLIT_STRING.length; j++) {
                                            if (SPLIT_STRING[j].equals("안주")) {
                                                second_check = true;
                                            }
                                        }
                                        if (second_check) {
                                            Member tempt_member = new Member();
                                            tempt_member.weight = weight[i];
                                            tempt_member.index = i;
                                            weight_array.add(tempt_member);
                                        }
                                    }
                                }
                            }
                        }
                        else if(!(STATE[1]) && STATE[2]){
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                    boolean check_bool = false;
                                    for (int j = 0; j < SPLIT_STRING.length; j++) {
                                        if (SPLIT_STRING[j].equals("안주")) {
                                            check_bool = true;
                                        }
                                    }
                                    if (check_bool) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                        }
                        else if(STATE[1] && !(STATE[2])){
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    boolean check_bool = true;
                                    for (int j = 0; j < 3; j++) {
                                        if (PREV_SELECTION[j] == i && !(PREV_SELECTION_DATE[j].equals("-1"))) {
                                            String tempt_month = PREV_SELECTION_DATE[j].substring(5, 7);
                                            String tempt_day = PREV_SELECTION_DATE[j].substring(8, 10);
                                            String today_month = getTime.substring(5, 7);
                                            String today_day = getTime.substring(8, 10);
                                            int tempt_month_int = Integer.parseInt(tempt_month);
                                            int tempt_day_int = Integer.parseInt(tempt_day);
                                            int today_month_int = Integer.parseInt(today_month);
                                            int today_day_int = Integer.parseInt(today_day);

                                            if (today_month_int - tempt_month_int == 0) {
                                                if (today_day_int - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            } else {
                                                if (today_day_int + 30 - tempt_day_int <= 4) {
                                                    check_bool = false;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                    if (check_bool) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                        }
                        else{
                            for (int i = 0; i < 41; i++) {
                                if (can_eat[i]) {
                                    Member tempt_member = new Member();
                                    tempt_member.weight = weight[i];
                                    tempt_member.index = i;
                                    weight_array.add(tempt_member);
                                }
                            }
                        }

                        Collections.sort(weight_array, new MemberComparator());
                        PICK[0] = weight_array.get(0).index;
                        PICK[1] = weight_array.get(1).index;
                        PICK[2] = weight_array.get(2).index;
                        First_text.setText(menu_name[weight_array.get(0).index]);
                        Second_text.setText(menu_name[weight_array.get(1).index]);
                        Third_text.setText(menu_name[weight_array.get(2).index]);
                    }
                    else {

                        FLUSH_NUM.add(PICK[0]);
                        FLUSH_NUM.add(PICK[1]);
                        FLUSH_NUM.add(PICK[2]);

                        Arrays.fill(TOGETHER_EAT, true);

                        for(Integer i : FLUSH_NUM){
                            TOGETHER_EAT[i] = false;
                        }

                        if (USER_VEGAN || DIFF_VEGAN ) {
                            for (int i = 0; i < 41; i++) {
                                if (VEGAN[i].equals("false")) {
                                    TOGETHER_EAT[i] = false;
                                }
                            }
                        }

                        for (int i = 0; i < 21; i++) {
                            for (int j = 0; j < 41; j++) {
                                if (user_alergy[i] || DIFF_user_alergy[i]) {
                                    char A = alergy_each_menu[i].charAt(j);
                                    if (A == '1') {
                                        TOGETHER_EAT[j] = false;
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < 41; i++) {
                            if (SPICY_LEVEL < spicy_level_menu[i] || DIFF_SPICY_LEVEL < spicy_level_menu[i]) {
                                TOGETHER_EAT[i] = false;
                            }
                        }

                        Vector<Member> weight_array = new Vector<Member>();
                        if(STATE[2]){
                            for (int i = 0; i < 41; i++) {
                                if (TOGETHER_EAT[i]) {
                                    String[] SPLIT_STRING = CATEGORY[i].split(" ");
                                    boolean check_bool = false;
                                    for (int j = 0; j < SPLIT_STRING.length; j++) {
                                        if (SPLIT_STRING[j].equals("안주")) {
                                            check_bool = true;
                                        }
                                    }
                                    if (check_bool) {
                                        Member tempt_member = new Member();
                                        tempt_member.weight = weight[i];
                                        tempt_member.index = i;
                                        weight_array.add(tempt_member);
                                    }
                                }
                            }
                        }
                        else {
                            for (int i = 0; i < 41; i++) {
                                if (TOGETHER_EAT[i]) {
                                    Member tempt_member = new Member();
                                    tempt_member.weight = (6 * weight[i] + 5 * diff_weight[i]) / 11;
                                    tempt_member.index = i;
                                    weight_array.add(tempt_member);
                                }
                            }
                        }
                        Collections.sort(weight_array, new MemberComparator());
                        PICK[0] = weight_array.get(0).index;
                        PICK[1] = weight_array.get(1).index;
                        PICK[2] = weight_array.get(2).index;
                        First_text.setText(menu_name[weight_array.get(0).index]);
                        Second_text.setText(menu_name[weight_array.get(1).index]);
                        Third_text.setText(menu_name[weight_array.get(2).index]);
                        SELECT_ID.setText(DIFF_USER);
                    }
                }
                else{
                    First_text.setText("CHECK ACTIVA");
                    Second_text.setText("CHECK ACTIVA");
                    Third_text.setText("CHECK ACTIVA");
                }
            }
        });

        INSERT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(STATE[0]) {

                    DIFF_USER = EDIT_ID.getText().toString();

                    DocumentReference CLOUD_weight_DATA = mdb.collection("users").document(DIFF_USER).collection("WEIGHT").document("weight");
                    DocumentReference user_alergy_doc = mdb.collection("users").document(DIFF_USER).collection("ALERGY").document("alergy");
                    DocumentReference CLOUD_user_info = mdb.collection("users").document(DIFF_USER);

                    user_alergy_doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> Data = document.getData();
                                    for (int i = 0; i < 21; i++) {
                                        tempt[i] = Data.get(alergy_name[i]).toString();
                                        if (tempt[i].equals("true")) {
                                            DIFF_user_alergy[i] = true;
                                        } else {
                                            DIFF_user_alergy[i] = false;
                                        }
                                    }
                                }
                            }
                        }
                    });

                    CLOUD_weight_DATA.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> Data = document.getData();
                                    for (int i = 0; i < 41; i++) {
                                        String tempt = Data.get(menu_name[i]).toString();
                                        diff_weight[i] = Float.parseFloat(tempt);
                                    }
                                }
                            }
                        }
                    });

                    CLOUD_user_info.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> Data = document.getData();
                                    DIFF_SPICY_LEVEL = Integer.parseInt(Data.get("SPICY_LEVEL").toString());
                                    DIFF_ACTIVATE = Integer.parseInt(Data.get("ACTIVATION").toString());
                                    String tempt = Data.get("VEGAN").toString();
                                    if (tempt == "true") {
                                        DIFF_VEGAN = true;
                                    } else {
                                        DIFF_VEGAN = false;
                                    }
                                }
                            }
                        }
                    });
                }
                else{
                    First_text.setText("CHECK ACTIVA");
                    Second_text.setText("CHECK ACTIVA");
                    Third_text.setText("CHECK ACTIVA");
                }
            }
        });
    }


    class Member {
        public float weight;
        public int index;
    }

    class MemberComparator implements Comparator<Member> {
        @Override
        public int compare(Member arg0, Member arg1) {
            return arg0.weight < arg1.weight ? 1 : arg0.weight > arg1.weight ? -1 : 0;
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {
                                mLastLocation = location;
                                latitude = mLastLocation.getLatitude();
                                longitude = mLastLocation.getLongitude();
                                // TODO: 사용자 두 명의 location을 집어넣으면 둘 사이 거리를 m단위로 알려줌
                                // getDistance(Location A, Location B);
                            }
                        }
                    });
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public boolean getDistance(double first_la, double first_lo, double second_la, double second_lo) {
        Location A = new Location("point A"); // 고예송집
        A.setLatitude(first_la);
        A.setLongitude(first_lo);
        Location B = new Location("point B"); // 학교
        B.setLatitude(second_la);
        B.setLongitude(second_lo);

        // 실제 거리 계산 코드
        float distance = A.distanceTo(B);
        if (distance <= 10) // 근처
            return true;
        else
            return false;
    }

    public boolean isTrip(Location original, Location now) {
        float distance = original.distanceTo(now);
        if (distance > 30000) // 초기 입력 위치에서 30km 이상 떨어짐: 여행
            return true;
        else
            return false;
    }

}

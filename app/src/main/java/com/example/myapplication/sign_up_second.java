package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;

public class sign_up_second extends AppCompatActivity {

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");
    private FirebaseFirestore mdb = FirebaseFirestore.getInstance();

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    double latitude;
    double longitude;

    int height_value = -1;
    int weight_value = -1;
    int age_value = -1;
    int budget_value = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_second);
        Intent prev_intent = getIntent();

        Button REGISTER = (Button)findViewById(R.id.register);

        EditText height = (EditText)findViewById(R.id.edit_height);
        EditText weight = (EditText)findViewById(R.id.edit_weight);
        EditText age = (EditText)findViewById(R.id.edit_age);
        EditText Budget = (EditText)findViewById(R.id.edit_Budget);

        Spinner spinner = findViewById(R.id.spicy_spinner);
        String[] items = {"1단계 매운거 아예 못 먹음", "2단계 신라면정도", "3단계 신라면 2배정도", "4단계 불닭정도", "5단계 핵불닭정도"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        CheckBox male = (CheckBox)findViewById(R.id.Male);
        CheckBox female = (CheckBox)findViewById(R.id.Female);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation();

        int saved_position = spinner.getSelectedItemPosition();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }

        });

        REGISTER.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(height.getText().toString().isEmpty()))
                {
                    String tempt = height.getText().toString();
                    Boolean parsing_okay = true;
                    for(int i = 0; i < tempt.length(); i++)
                    {
                        char tmp = tempt.charAt(i);
                        if(Character.isDigit(tmp) == false)
                        {
                            parsing_okay = false;
                            break;
                        }
                    }
                    if(parsing_okay == true)
                    {
                        height_value = Integer.parseInt(tempt);
                    }
                    else
                    {
                        height_value = -1;
                    }
                }
                else
                {
                    height_value = -1;
                }

                if(!(weight.getText().toString().isEmpty()))
                {
                    String tempt = weight.getText().toString();
                    Boolean parsing_okay = true;
                    for(int i = 0; i < tempt.length(); i++)
                    {
                        char tmp = tempt.charAt(i);
                        if(Character.isDigit(tmp) == false)
                        {
                            parsing_okay = false;
                            break;
                        }
                    }
                    if(parsing_okay == true)
                    {
                        weight_value = Integer.parseInt(tempt);
                    }
                    else
                    {
                        weight_value = -1;
                    }
                }
                else
                {
                    weight_value = -1;
                }

                if(!(age.getText().toString().isEmpty()))
                {
                    String tempt = age.getText().toString();
                    Boolean parsing_okay = true;
                    for(int i = 0; i < tempt.length(); i++)
                    {
                        char tmp = tempt.charAt(i);
                        if(Character.isDigit(tmp) == false)
                        {
                            parsing_okay = false;
                            break;
                        }
                    }
                    if(parsing_okay == true)
                    {
                        age_value = Integer.parseInt(tempt);
                    }
                    else
                    {
                        age_value = -1;
                    }
                }
                else
                {
                    age_value = -1;
                }

                if(!(Budget.getText().toString().isEmpty()))
                {
                    String tempt = Budget.getText().toString();
                    Boolean parsing_okay = true;
                    for(int i = 0; i < tempt.length(); i++)
                    {
                        char tmp = tempt.charAt(i);
                        if(Character.isDigit(tmp) == false)
                        {
                            parsing_okay = false;
                            break;
                        }
                    }
                    if(parsing_okay == true)
                    {
                        budget_value = Integer.parseInt(tempt);
                    }
                    else
                    {
                        budget_value = -1;
                    }
                }
                else
                {
                    budget_value = -1;
                }

                int gender = 0;

                if(male.isChecked())
                {
                    gender = gender + 1;
                }

                if(female.isChecked())
                {
                    gender = gender + 2;
                }

                Intent intent = new Intent(sign_up_second.this, StartActivity.class);

                if(height_value != -1 && weight_value != -1 && age_value != -1 && budget_value != -1 && (gender == 1 || gender == 2))
                {
                    String ID = prev_intent.getStringExtra("ID");
                    String PASSWORD = prev_intent.getStringExtra("PASSWORD");
                    String NAME = prev_intent.getStringExtra("NAME");
                    int PAY_DAY = prev_intent.getIntExtra("PAY", -1);

                    boolean[] alergy_bool = prev_intent.getBooleanArrayExtra("ALERGY");
                    boolean vegan_bool = prev_intent.getBooleanExtra("VEGAN", false);

                    Map<String, Object> user = new HashMap<>();
                    user.put("ID", ID);
                    user.put("PASSWORD", PASSWORD);
                    user.put("NAME", NAME);
                    user.put("PAY_DAY", PAY_DAY);
                    user.put("VEGAN", vegan_bool);
                    user.put("LATITUDE", latitude);
                    user.put("LONGITUDE",longitude);

                    user.put("HEIGHT", height_value);
                    user.put("WEIGHT", weight_value);
                    user.put("AGE", age_value);
                    user.put("BUDGET", budget_value);
                    user.put("SPICY_LEVEL", spinner.getSelectedItemPosition() + 1);
                    user.put("ACTIVATION", 0);

                    //user.put("ALERGY",alergy_bool);

                    mdb.collection("users").document(ID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

                    Map<String, Object> user_alergy = new HashMap<>();

                    String[] ALERGY_STRING = getResources().getStringArray(R.array.alergy_name);
                    for(int i = 0; i < ALERGY_STRING.length; i++) {
                        user_alergy.put(ALERGY_STRING[i], alergy_bool[i]);
                    }
                    mdb.collection("users").document(ID).collection("ALERGY").document("alergy").set(user_alergy);

                    String[] menu_name = getResources().getStringArray(R.array.menu_name);
                    Map<String, Object> user_weight = new HashMap<>();
                    for(int i = 0; i < menu_name.length; i++){
                        float tempt = 0;
                        user_weight.put(menu_name[i], tempt);
                    }

                    mdb.collection("users").document(ID).collection("WEIGHT").document("weight").set(user_weight);

                    Map<String, Object> prev_selection = new HashMap<>();
                    prev_selection.put("FIRST_SELECTION", "-1");
                    prev_selection.put("SECOND_SELECTION", "-1");
                    prev_selection.put("THIRD_SELECTION", "-1");
                    prev_selection.put("FIRST_SELECTION_DATE", "-1");
                    prev_selection.put("SECOND_SELECTION_DATE", "-1");
                    prev_selection.put("THIRD_SELECTION_DATE", "-1");

                    mdb.collection("users").document(ID).collection("PREV_SELECTION").document("PREV_SELECTION").set(prev_selection);

                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                else
                {
                    Toast toast = Toast.makeText(sign_up_second.this, "키, 몸무게, 나이, 성별 정보를 채워주세요", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    public void sign_up(View view){

        EditText IDView = (EditText) findViewById(R.id.ID_TEXT);
        EditText PasswordView = (EditText) findViewById(R.id.password);
        EditText NameView = (EditText) findViewById(R.id.Name);
        String IDText = IDView.getText().toString();
        String PasswordText = PasswordView.getText().toString();
        String NameText = NameView.getText().toString();

        if(IDText.isEmpty() || PasswordText.isEmpty() || NameText.isEmpty())
        {
            return;
        }
        Map<String, Object> user = new HashMap<>();
        user.put("ID", IDText);
        user.put("PASSWORD", PasswordText);
        user.put("NAME", NameText);

        mdb.collection("users").document(IDText).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
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

    public boolean getDistance(Location A, Location B) {
        // 임의 위치 설정 후 테스트
        // Location A = new Location("point A"); // 고예송집
        // A.setLatitude(36.0087154);
        // A.setLongitude(129.3312471);
        // Location B = new Location("point B"); // 학교
        // B.setLatitude(36.01264);
        // B.setLongitude(129.32126);

        // 실제 거리 계산 코드
        float distance = A.distanceTo(B);
        if (distance <= 50) // 근처
            return true;
        else
            return false;
    }

    public boolean isTrip(Location original, Location now) {
        float distance = original.distanceTo(now);
        if (distance > 300000) // 초기 입력 위치에서 30km 이상 떨어짐: 여행
            return true;
        else
            return false;
    }

}

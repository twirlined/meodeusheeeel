package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");
    private FirebaseFirestore mdb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button button_sign = (Button) findViewById(R.id.sign_in_button);
        Button button_login = (Button) findViewById(R.id.login_button);
        Button button_initialize = (Button) findViewById(R.id.INITIALIZE);

        button_sign.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, sign_up_first.class);
                startActivity(intent);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(StartActivity.this, login_Activity.class);
                startActivity(intent);
            }
        });

        button_initialize.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String[] menu_string = getResources().getStringArray(R.array.menu_name);
                String[] alergy_name = getResources().getStringArray(R.array.alergy_name);
                String[] alergy_each_menu = getResources().getStringArray(R.array.ALERGY_EACH_MENU);
                String[] category_menu = getResources().getStringArray(R.array.분류);
                String[] weather_array = getResources().getStringArray(R.array.날씨);
                String[] main_ingredient = getResources().getStringArray(R.array.주재료);
                String[] vegan_possiblility = getResources().getStringArray(R.array.vegan_array);
                int[] BUDGET = getResources().getIntArray(R.array.budget);
                int[] spicy_level = getResources().getIntArray(R.array.menu_spicy_level);

                for(int i = 0; i < 41; i++){
                    Map<String, Object> user = new HashMap<>();
                    user.put("날씨", weather_array[i]);
                    user.put("주재료", main_ingredient[i]);
                    user.put("분류", category_menu[i]);
                    if(vegan_possiblility[i].equals("1")){
                        user.put("비건", true);
                    }
                    else {
                        user.put("비건", false);
                    }
                    user.put("비용", BUDGET[i]);
                    user.put("맵기", spicy_level[i]);
                    mdb.collection("database").document(menu_string[i]).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    Map<String, Object> alergy = new HashMap<>();
                    for(int j = 0; j < 21; j++) {
                        char temp = alergy_each_menu[j].charAt(i);
                        if(temp == '1'){
                            alergy.put(alergy_name[j], true);
                        }
                        else{
                            alergy.put(alergy_name[j], false);
                        }
                    }
                    mdb.collection("database").document(menu_string[i]).collection("ALERGY").document("alergy").set(alergy);
                    Map<String, Object> weight = new HashMap<>();

                }

            }
        });
    }

}

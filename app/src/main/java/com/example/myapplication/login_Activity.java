package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class login_Activity extends AppCompatActivity {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    String ID;
    String PASSWORD;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText id_text = (EditText) findViewById(R.id.login_edit_ID);
        EditText password_text = (EditText) findViewById(R.id.login_edit_PASSWORD);

        Button login_button = (Button) findViewById(R.id.login_activity_button);

        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(id_text.getText().toString().isEmpty() || password_text.getText().toString().isEmpty()){

                    Toast toast = Toast.makeText(login_Activity.this, "ID, PASSWORD 정보를 둘 다 입력해주세요", Toast.LENGTH_SHORT);
                    toast.show();

                }
                else {

                    ID = id_text.getText().toString();
                    PASSWORD = password_text.getText().toString();

                    DocumentReference docRef = db.collection("users").document(ID);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    Map<String, Object> Data = document.getData();
                                    String cloud_password_data = Data.get("PASSWORD").toString();

                                    if(cloud_password_data.equals(PASSWORD)) {
                                        Intent intent = new Intent(login_Activity.this, MainActivity.class);
                                        intent.putExtra("ID", ID);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
                    /*
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    Map<String, Object> Data = document.getData();
                                    String cloud_password_data = Data.get("PASSWORD").toString();

                                    if(cloud_password_data.equals(PASSWORD)) {
                                        Intent intent = new Intent(login_Activity.this, MainActivity.class);
                                        intent.putExtra("ID", ID);
                                        startActivity(intent);
                                    }
                                    else {
                                        Toast toast = Toast.makeText(login_Activity.this, "ID, PASSWORD 정보를 제대로 입력해주세요", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }

                                }
                                else {
                                    Toast toast = Toast.makeText(login_Activity.this, "ID, PASSWORD 정보를 제대로 입력해주세요", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                            else{
                                Toast toast = Toast.makeText(login_Activity.this, "ID, PASSWORD 정보를 제대로 입력해주세요", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });
*/


                }

            }

        });


    }

}

package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");

    TextView mQuoteTextView;
    private FirebaseFirestore mdb = FirebaseFirestore.getInstance();

    String id_string ="";
    String pass_string = "";
    String name_string ="";
    String pay_string ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuoteTextView = (TextView) findViewById(R.id.ID);

        Button button_sign = (Button) findViewById(R.id.next_page);
        EditText id_text = (EditText) findViewById(R.id.ID_TEXT);
        EditText pass_text = (EditText) findViewById(R.id.password);
        EditText name_text = (EditText) findViewById(R.id.Name);
        EditText pay_text = (EditText) findViewById(R.id.ABCD);

        boolean next_ok = true;

        if(id_text.getText().toString().length() == 0){
            next_ok = false;
        }
        else
        {
            id_string = id_text.getText().toString();
        }

        if(pass_text.getText().toString().length() == 0){
            next_ok = false;
        }
        else
        {
            pass_string = pass_text.getText().toString();
        }

        if(name_text.getText().toString().length() == 0){
            next_ok = false;
        }
        else
        {
            name_string = name_text.getText().toString();
        }

        if(pay_text.getText().toString().length() == 0){
            next_ok = false;
        }
        else
        {
            pay_string = pay_text.getText().toString();
        }

        boolean Alergy_bool[] = new boolean[21];

        CheckBox maemil_checkbox = (CheckBox) findViewById(R.id.MAEMIL);
        CheckBox wheat_checkbox = (CheckBox) findViewById(R.id.밀);
        CheckBox soybean_checkbox = (CheckBox) findViewById(R.id.대두);
        CheckBox walnut_checkbox = (CheckBox) findViewById(R.id.호두);

        CheckBox peach_checkbox = (CheckBox) findViewById(R.id.복숭아);
        CheckBox tomato_checkbox = (CheckBox) findViewById(R.id.토마토);
        CheckBox pork_checkbox = (CheckBox) findViewById(R.id.돼지고기);
        CheckBox egg_checkbox = (CheckBox) findViewById(R.id.난류);

        CheckBox milk_checkbox = (CheckBox) findViewById(R.id.우유);
        CheckBox chicken_checkbox = (CheckBox) findViewById(R.id.닭고기);
        CheckBox beef_checkbox = (CheckBox) findViewById(R.id.소고기);
        CheckBox shrimp_checkbox = (CheckBox) findViewById(R.id.새우);

        CheckBox mussel_checkbox = (CheckBox) findViewById(R.id.홍합);
        CheckBox jeonbok_checkbox = (CheckBox) findViewById(R.id.전복);
        CheckBox oyster_checkbox = (CheckBox) findViewById(R.id.굴);
        CheckBox clam_checkbox = (CheckBox) findViewById(R.id.조개류);

        CheckBox peanut_checkbox = (CheckBox) findViewById(R.id.땅콩);
        CheckBox mackerel_checkbox = (CheckBox) findViewById(R.id.고등어);
        CheckBox squid_checkbox = (CheckBox) findViewById(R.id.오징어);
        CheckBox crab_checkbox = (CheckBox) findViewById(R.id.게);
        CheckBox H2SO3_checkbox = (CheckBox) findViewById(R.id.아황산포함식품);

        CheckBox vegan_checkbox = (CheckBox) findViewById(R.id.비건);

        if(maemil_checkbox.isChecked())
        {
            Alergy_bool[0] = true;
        }
        else
        {
            Alergy_bool[0] = false;
        }

        if(wheat_checkbox.isChecked())
        {
            Alergy_bool[1] = true;
        }
        else
        {
            Alergy_bool[1] = false;
        }

        if(soybean_checkbox.isChecked())
        {
            Alergy_bool[2] = true;
        }
        else
        {
            Alergy_bool[2] = false;
        }

        if(walnut_checkbox.isChecked())
        {
            Alergy_bool[3] = true;
        }
        else
        {
            Alergy_bool[3] = false;
        }



        if(peach_checkbox.isChecked())
        {
            Alergy_bool[4] = true;
        }
        else
        {
            Alergy_bool[4] = false;
        }

        if(tomato_checkbox.isChecked())
        {
            Alergy_bool[5] = true;
        }
        else
        {
            Alergy_bool[5] = false;
        }

        if(pork_checkbox.isChecked())
        {
            Alergy_bool[6] = true;
        }
        else
        {
            Alergy_bool[6] = false;
        }

        if(egg_checkbox.isChecked())
        {
            Alergy_bool[7] = true;
        }
        else
        {
            Alergy_bool[7] = false;
        }


        if(milk_checkbox.isChecked())
        {
            Alergy_bool[8] = true;
        }
        else
        {
            Alergy_bool[8] = false;
        }

        if(chicken_checkbox.isChecked())
        {
            Alergy_bool[9] = true;
        }
        else
        {
            Alergy_bool[9] = false;
        }

        if(beef_checkbox.isChecked())
        {
            Alergy_bool[10] = true;
        }
        else
        {
            Alergy_bool[10] = false;
        }

        if(shrimp_checkbox.isChecked())
        {
            Alergy_bool[11] = true;
        }
        else
        {
            Alergy_bool[11] = false;
        }



        if(mussel_checkbox.isChecked())
        {
            Alergy_bool[12] = true;
        }
        else
        {
            Alergy_bool[12] = false;
        }

        if(jeonbok_checkbox.isChecked())
        {
            Alergy_bool[13] = true;
        }
        else
        {
            Alergy_bool[13] = false;
        }

        if(oyster_checkbox.isChecked())
        {
            Alergy_bool[14] = true;
        }
        else
        {
            Alergy_bool[14] = false;
        }

        if(clam_checkbox.isChecked())
        {
            Alergy_bool[15] = true;
        }
        else
        {
            Alergy_bool[15] = false;
        }


        if(peanut_checkbox.isChecked())
        {
            Alergy_bool[16] = true;
        }
        else
        {
            Alergy_bool[16] = false;
        }

        if(mackerel_checkbox.isChecked())
        {
            Alergy_bool[17] = true;
        }
        else
        {
            Alergy_bool[17] = false;
        }

        if(squid_checkbox.isChecked())
        {
            Alergy_bool[18] = true;
        }
        else
        {
            Alergy_bool[18] = false;
        }

        if(crab_checkbox.isChecked())
        {
            Alergy_bool[19] = true;
        }
        else
        {
            Alergy_bool[19] = false;
        }

        if(H2SO3_checkbox.isChecked())
        {
            Alergy_bool[20] = true;
        }
        else
        {
            Alergy_bool[20] = false;
        }

        boolean vegan_boolean;

        if(vegan_checkbox.isChecked())
        {
            vegan_boolean = true;
        }
        else
        {
            vegan_boolean = false;
        }

        button_sign.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, sign_up.class);
                intent.putExtra("ID", id_string);
                intent.putExtra("PASSWORD", pass_string);
                intent.putExtra("NAME", name_string);
                intent.putExtra("PAY", pay_string);

                startActivity(intent);
            }
        });

    }
/*
    public void fetchQuote(View view) {
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Map<String, Object> myData = documentSnapshot.getData();
                    mQuoteTextView.setText(myData.toString());
                }
            }
        });
    }

    public void saveQuote(View view){
        EditText quoteView = (EditText) findViewById(R.id.ID_TEXT);
        EditText authorView = (EditText) findViewById(R.id.password);
        String quoteText = quoteView.getText().toString();
        String authorText = authorView.getText().toString();

        if(quoteText.isEmpty() || authorText.isEmpty())
        {
            return;
        }

        Map<String, String> dataToSave = new HashMap<String, String>();
        dataToSave.put(authorText, quoteText);

        mDocRef.set(dataToSave, SetOptions.merge());

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
    }*/
}
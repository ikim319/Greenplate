package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Person;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.greenplate.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class PersonalInformation extends AppCompatActivity {

    EditText editTextHeight, editTextWeight;
    Spinner spinnerGender;
    private DatabaseReference rootDatabref;
    FirebaseManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        spinnerGender = findViewById(R.id.spinnerGender);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
        spinnerGender.setSelection(adapter.getPosition("Male"));


        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonIngredients = findViewById(R.id.Ingredient);
        Button buttonInputMeal = findViewById(R.id.btn_inputmeal);
        Button buttonRecipe = findViewById(R.id.Recipe);
        Button buttonShoppingList = findViewById(R.id.shoppinglist);
        Button buttonBackWelcome = findViewById(R.id.Logout);
        Button buttonLog = findViewById(R.id.btn_Log);

        rootDatabref = FirebaseManager.getInstance().getRef().child("Users");
        manager = FirebaseManager.getInstance();

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePersonalInfo();
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformation.this, Home.class));
            }
        });

        buttonIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformation.this, Ingredients.class));
            }
        });

        buttonInputMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformation.this, InputMeal.class));
            }
        });

        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(PersonalInformation.this, Recipe.class));
            }
        });

        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(PersonalInformation.this, ShoppingList.class));
            }
        });

        buttonBackWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonalInformation.this, Login.class));
            }
        });
    }


    private void savePersonalInfo() {
        String Height = editTextHeight.getText().toString();
        String Weight = editTextWeight.getText().toString();
        String Gender;
        if (spinnerGender != null) {
            Gender = spinnerGender.getSelectedItem().toString();
        } else {
            Gender = "Male";
        }


        Users users = new Users(Height, Weight, Gender);
        String userId = manager.getAuth().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(userId)
                .child("Personal_Info");

        userRef.setValue(users);
        Toast.makeText(PersonalInformation.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
    }



}
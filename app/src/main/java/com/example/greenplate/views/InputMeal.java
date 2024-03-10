package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenplate.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class InputMeal extends AppCompatActivity {


    EditText editTextMeal, editTextCalories;
    private DatabaseReference rootDatabref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal);
        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonIngredients = findViewById(R.id.Ingredient);
        Button buttonRecipe = findViewById(R.id.Recipe);
        Button buttonShoppingList = findViewById(R.id.shoppinglist);
        Button buttonBackWelcome = findViewById(R.id.Logout);
        Button buttonPersonalInfo = findViewById(R.id.PInformation);
        Button buttonLog = findViewById(R.id.btn_Log);

        editTextMeal = findViewById(R.id.editTextMeal);
        editTextCalories = findViewById(R.id.editTextCalories);

        rootDatabref = FirebaseDatabase.getInstance().getReference().child("Meal");

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeal();
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, Home.class));
            }
        });
        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(InputMeal.this, Recipe.class));
            }
        });
        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(InputMeal.this, ShoppingList.class));
            }
        });

        buttonIngredients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(InputMeal.this, Ingredients.class));
            }
        });

        buttonBackWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, Login.class));
            }
        });

        buttonPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, PersonalInformation.class));
            }
        });

    private void saveMeal() {
        String Meal = editTextMeal.getText().toString();
        String Calories = editTextCalories.getText().toString();

        Meal meal = new Meal(Meal, Calories);
        rootDatabref.push().setValue(meal);
        Toast.makeText(InputMeal.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
    }


    /*
    method for updating the height, weight, and gender displayed on InputMeal
    waiting for database integration to finish
     */
        private void updateUserInfoTextViews (String height, String weight, String gender){
            TextView textViewHeightValue = findViewById(R.id.textViewHeightValue);
            TextView textViewWeightValue = findViewById(R.id.textViewWeightValue);
            TextView textViewGenderValue = findViewById(R.id.textViewGenderValue);

            textViewHeightValue.setText(height);
            textViewWeightValue.setText(weight);
            textViewGenderValue.setText(gender);

        }
    }

}
package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.os.Bundle;
import android.widget.Button;
import com.example.greenplate.R;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;


public class PersonalInformation extends AppCompatActivity {

    EditText editTextHeight;
    EditText editTextWeight;
    EditText editTextGender;

    Button buttonHome;
    Button buttonIngredients;
    Button buttonInputMeal;
    Button buttonRecipe;
    Button buttonShoppingList;
    Button buttonBackWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextGender = findViewById(R.id.editTextGender);

        buttonHome = findViewById(R.id.btn_Home);
        buttonIngredients = findViewById(R.id.Ingredient);
        buttonInputMeal = findViewById(R.id.btn_inputmeal);
        buttonRecipe = findViewById(R.id.Recipe);
        buttonShoppingList = findViewById(R.id.shoppinglist);


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
    }

}
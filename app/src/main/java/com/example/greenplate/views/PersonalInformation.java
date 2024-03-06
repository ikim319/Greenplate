package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Person;
import android.os.Bundle;
import android.widget.Button;
import com.example.greenplate.R;
import android.view.View;
import android.content.Intent;


public class PersonalInformation extends AppCompatActivity {


    Button buttonHome = findViewById(R.id.btn_Home);
    Button buttonIngredients = findViewById(R.id.Ingredient);
    Button buttonInputMeal = findViewById(R.id.btn_inputmeal);
    Button buttonRecipe = findViewById(R.id.Recipe);
    Button buttonShoppingList = findViewById(R.id.shoppinglist);
    Button buttonBackWelcome = findViewById(R.id.Logout);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

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
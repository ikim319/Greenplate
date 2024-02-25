package com.example.greenplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InputMeal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal);
        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonIngredients = findViewById(R.id.Ingredient);
        Button buttonRecipe = findViewById(R.id.Recipe);
        Button buttonShoppingList = findViewById(R.id.shoppinglist);
        Button buttonBackWelcome = findViewById(R.id.Logout);

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
    }
}
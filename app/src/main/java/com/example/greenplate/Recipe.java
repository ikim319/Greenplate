package com.example.greenplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Recipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonIngredients = findViewById(R.id.Ingredient);
        Button buttonInputMeal = findViewById(R.id.btn_inputmeal);
        Button buttonRecipe = findViewById(R.id.Recipe);
        Button buttonShoppingList = findViewById(R.id.shoppinglist);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Recipe.this, Home.class));
            }
        });
        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(Recipe.this, Recipe.class));
            }
        });
        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(Recipe.this, ShoppingList.class));
            }
        });

        buttonIngredients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(Recipe.this, Ingredients.class));
            }
        });

        buttonInputMeal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(Recipe.this, InputMeal.class));
            }
        });
    }
}
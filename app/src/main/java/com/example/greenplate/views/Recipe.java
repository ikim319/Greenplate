package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.greenplate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Recipe extends AppCompatActivity {


    EditText editTextRecipeName, editTextIngredReq, editTextQuantRecipe;
    private DatabaseReference rootDatabref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonIngredients = findViewById(R.id.Ingredient);
        Button buttonInputMeal = findViewById(R.id.btn_inputmeal);
        Button buttonShoppingList = findViewById(R.id.shoppinglist);
        Button buttonBackWelcome = findViewById(R.id.Logout);
        Button buttonPersonalInfo = findViewById(R.id.PInformation);
        Button buttonLog = findViewById(R.id.btn_Log);


        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextIngredReq = findViewById(R.id.editTextIngredReq);
        editTextQuantRecipe = findViewById(R.id.editTextQuantRecipe);

        rootDatabref = FirebaseDatabase.getInstance().getReference().child("Cookbook");

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCookBook();
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Recipe.this, Home.class));
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

        buttonBackWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Recipe.this, Login.class));
            }
        });

        buttonPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Recipe.this, PersonalInformation.class));
            }
        });
    }

    private void saveCookBook() {
        String RecipeName = editTextRecipeName.getText().toString();
        String IngredReq = editTextIngredReq.getText().toString();
        String QuantRecipe = editTextQuantRecipe.getText().toString();

        Cookbook cookbook = new Cookbook(RecipeName, IngredReq, QuantRecipe);
        rootDatabref.push().setValue(cookbook);
        Toast.makeText(Recipe.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
    }

}
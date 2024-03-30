package com.example.greenplate.views;
import java.util.List;
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

import java.util.ArrayList;

public class Recipe extends AppCompatActivity {

    EditText editTextRecipeName, editTextIngredReq;
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
        Button buttonLog = findViewById(R.id.buttonSave);

        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextIngredReq = findViewById(R.id.editTextIngredients);

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
        String recipeName = editTextRecipeName.getText().toString().trim();
        String ingredientsText = editTextIngredReq.getText().toString().trim();

        // Check if any field is empty
        if (recipeName.isEmpty() || ingredientsText.isEmpty()) {
            Toast.makeText(Recipe.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Split the ingredients by newline
        String[] ingredientsArray = ingredientsText.split("\n");

        // Create a StringBuilder to construct the ingredients list
        List<IngredientRequirement> ingredientsBuilder = new ArrayList<>();
        for (String ingredient : ingredientsArray) {
            String[] ingredPair;
            ingredPair = ingredient.split(",");
            IngredientRequirement ingred = new IngredientRequirement(ingredPair[0], ingredPair[1]);
            ingredientsBuilder.add(ingred);
        }

        // Save the recipe with its ingredients
        Cookbook cookbook = new Cookbook(recipeName, ingredientsBuilder);
        rootDatabref.push().setValue(cookbook);

        Toast.makeText(Recipe.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
    }

}

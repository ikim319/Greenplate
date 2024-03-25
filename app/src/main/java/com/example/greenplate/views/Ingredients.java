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

public class Ingredients extends AppCompatActivity {

    EditText editTextIngredientName, editTextQuantities, editTextPoExpire;
    private DatabaseReference rootDatabref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonInputMeal = findViewById(R.id.btn_inputmeal);
        Button buttonRecipe = findViewById(R.id.Recipe);
        Button buttonShoppingList = findViewById(R.id.shoppinglist);
        Button buttonBackWelcome = findViewById(R.id.Logout);
        Button buttonPersonalInfo = findViewById(R.id.PInformation);
        Button buttonLog = findViewById(R.id.btn_Log);

        editTextIngredientName = findViewById(R.id.editTextIngredientName);
        editTextQuantities = findViewById(R.id.editTextQuantities);
        editTextPoExpire = findViewById(R.id.editTextPoExpire);

        rootDatabref = FirebaseDatabase.getInstance().getReference().child("Pantry");

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePantry();
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Ingredients.this, Home.class));
            }
        });
        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(Ingredients.this, Recipe.class));
            }
        });
        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(Ingredients.this, ShoppingList.class));
            }
        });

        buttonInputMeal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(Ingredients.this, InputMeal.class));
            }
        });

        buttonBackWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Ingredients.this, Login.class));
            }
        });

        buttonPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Ingredients.this, PersonalInformation.class));
            }
        });

    }

    private void savePantry() {
        String IngredientName = editTextIngredientName.getText().toString();
        String Quantity = editTextQuantities.getText().toString();
        String PoExpire = editTextPoExpire.getText().toString();

        Pantry pantry = new Pantry(IngredientName, Quantity, PoExpire);
        rootDatabref.push().setValue(pantry);
        Toast.makeText(Ingredients.this, "Saved successfully!", Toast.LENGTH_SHORT).show();
    }
}
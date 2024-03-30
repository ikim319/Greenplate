package com.example.greenplate.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenplate.viewModels.IngredientViewModel;
import com.example.greenplate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class Ingredients extends AppCompatActivity {

    EditText editTextIngredientName, editTextQuantities, editTextPoExpire, editTextIngredientCalories;
    private DatabaseReference rootDatabref;
    private FirebaseManager manager;

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
        Button addIngredientButton = findViewById(R.id.addIngredient);
        RelativeLayout addIngredientForm = findViewById(R.id.ingredientForm);
        manager = FirebaseManager.getInstance();

        editTextIngredientName = findViewById(R.id.editTextIngredientName);
        editTextQuantities = findViewById(R.id.editTextQuantities);
        editTextPoExpire = findViewById(R.id.editTextPoExpire);
        editTextIngredientCalories = findViewById(R.id.editTextIngredientCalories);
        LinearLayout ingredientListLayout = findViewById(R.id.linearIngredients);

        rootDatabref = FirebaseDatabase.getInstance().getReference().child("Pantry");


        final ScrollView scrollView = findViewById(R.id.scrollIngredients);
        DatabaseReference userPantryRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(manager.getAuth().getUid())
                .child("Pantry");

        userPantryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ingredientListLayout.removeAllViews();
                for (DataSnapshot ingredientSnapshot : dataSnapshot.getChildren()) {
                    String ingredientName = ingredientSnapshot.child("ingredientName").getValue(String.class);
                    String ingredientQuantity = ingredientSnapshot.child("quantity").getValue(String.class);

                    LinearLayout ingredientLayout = new LinearLayout(Ingredients.this);
                    ingredientLayout.setOrientation(LinearLayout.HORIZONTAL);
                    ingredientLayout.setGravity(Gravity.CENTER_HORIZONTAL); // Center elements horizontally
                    ingredientLayout.setBackgroundResource(R.drawable.border_background);

                    TextView textView = new TextView(Ingredients.this);
                    String nameAndQuantity = ingredientName + ":    " + ingredientQuantity;
                    textView.setText(nameAndQuantity);

                    Button addButton = new Button(Ingredients.this);
                    addButton.setText("+");
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int newQuantity = Integer.parseInt(ingredientQuantity) + 1;
                            ingredientSnapshot.getRef().child("quantity").setValue(String.valueOf(newQuantity));
                        }
                    });

                    Button subtractButton = new Button(Ingredients.this);
                    subtractButton.setText("-");
                    subtractButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int newQuantity = Integer.parseInt(ingredientQuantity) - 1;
                            if (newQuantity > 0) {
                                ingredientSnapshot.getRef().child("quantity").setValue(String.valueOf(newQuantity));
                            } else {
                                ingredientSnapshot.getRef().removeValue();
                            }
                        }
                    });

                    ingredientLayout.addView(textView);
                    ingredientLayout.addView(addButton);
                    ingredientLayout.addView(subtractButton);
                    ingredientListLayout.addView(ingredientLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientForm.setVisibility(View.VISIBLE);
            }
        });

    }

    private void savePantry() {
        final String ingredientName = editTextIngredientName.getText().toString();
        final String quantity = editTextQuantities.getText().toString();
        final String poExpire = editTextPoExpire.getText().toString();
        final String ingredientCalories = editTextIngredientCalories.getText().toString();

        if (TextUtils.isEmpty(ingredientName) || TextUtils.isEmpty(quantity) || TextUtils.isEmpty(ingredientCalories)) {
            Toast.makeText(Ingredients.this, "Error: No empty inputs or negative quantity/calories", Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference userPantryRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(manager.getAuth().getUid())
                .child("Pantry");

        userPantryRef.orderByChild("ingredientName").equalTo(ingredientName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(Ingredients.this, "Error: Ingredient already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            Pantry pantry = new Pantry(ingredientName, quantity, ingredientCalories, poExpire);
                            String pantryId = userPantryRef.push().getKey();
                            userPantryRef.child(pantryId).setValue(pantry);
                            Toast.makeText(Ingredients.this, "Saved successfully!", Toast.LENGTH_SHORT).show();

                            editTextIngredientName.setText("");
                            editTextQuantities.setText("");
                            editTextPoExpire.setText("");
                            editTextIngredientCalories.setText("");
                            RelativeLayout addIngredientForm = findViewById(R.id.ingredientForm);
                            addIngredientForm.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Ingredients.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
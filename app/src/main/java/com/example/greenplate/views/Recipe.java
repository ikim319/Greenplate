package com.example.greenplate.views;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenplate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Recipe extends AppCompatActivity {

    EditText editTextRecipeName, editTextIngredReq;
    private DatabaseReference rootDatabref;
    private FirebaseManager manager;
    EditText editTextSearch;
    Button buttonSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Button buttonHome = findViewById(R.id.btn_Home);
//        Button buttonIngredients = findViewById(R.id.Ingredient);
//        Button buttonInputMeal = findViewById(R.id.btn_inputmeal);
//        Button buttonShoppingList = findViewById(R.id.shoppinglist);
//        Button buttonBackWelcome = findViewById(R.id.Logout);
//        Button buttonPersonalInfo = findViewById(R.id.PInformation);
        Button buttonLog = findViewById(R.id.buttonSave);

        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextIngredReq = findViewById(R.id.editTextIngredients);
        manager = FirebaseManager.getInstance();
        rootDatabref = FirebaseDatabase.getInstance().getReference().child("Cookbook");
        Button buttonSortAlphabetical = findViewById(R.id.buttonSortAlphabetical);
        // Initialize search bar and button
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);

        // Set click listener for search button
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchRecipe();
            }
        });
        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCookBook();
            }
        });
        buttonSortAlphabetical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortRecipesAlphabetically();
            }
        });
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Recipe.this, Home.class));
            }
        });
//        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // navigate to our login
//                startActivity(new Intent(Recipe.this, ShoppingList.class));
//            }
//        });
//
//        buttonIngredients.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // navigate to our login
//                startActivity(new Intent(Recipe.this, Ingredients.class));
//            }
//        });
//
//        buttonInputMeal.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // navigate to our login
//                startActivity(new Intent(Recipe.this, InputMeal.class));
//            }
//        });
//
//        buttonBackWelcome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Recipe.this, Login.class));
//            }
//        });
//
//        buttonPersonalInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Recipe.this, PersonalInformation.class));
//            }
//        });
        displayRecipes();
    }
    private void searchRecipe() {
        String searchQuery = editTextSearch.getText().toString().trim();

        if (searchQuery.isEmpty()) {
            Toast.makeText(this, "Please enter a recipe name to search.", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference recipesRef = manager.getRef().child("Cookbook");

        recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean recipeFound = false;
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Cookbook cookbook = recipeSnapshot.getValue(Cookbook.class);
                    if (cookbook != null && cookbook.getRecipeName().equalsIgnoreCase(searchQuery)) {
                        recipeFound = true;
                        displayRecipeDetails(cookbook);
                        break;
                    }
                }
                if (!recipeFound) {
                    Toast.makeText(Recipe.this, "Recipe not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Recipe.this, "Failed to search for recipe.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sortRecipesAlphabetically() {
        DatabaseReference recipesRef = manager.getRef().child("Cookbook");

        recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Cookbook> recipes = new ArrayList<>();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Cookbook cookbook = recipeSnapshot.getValue(Cookbook.class);
                    if (cookbook != null) {
                        recipes.add(cookbook);
                    }
                }

                // Sort recipes alphabetically by recipe name
                Collections.sort(recipes, new Comparator<Cookbook>() {
                    @Override
                    public int compare(Cookbook r1, Cookbook r2) {
                        return r1.getRecipeName().compareToIgnoreCase(r2.getRecipeName());
                    }
                });

                // Update the recipe list layout with sorted recipes
                updateRecipeListLayout(recipes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Recipe.this, "Failed to load recipes.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to update the recipe list layout with sorted recipes
    private void updateRecipeListLayout(List<Cookbook> recipes) {
        LinearLayout recipeListLayout = findViewById(R.id.linearRecipes);
        recipeListLayout.removeAllViews();

        for (Cookbook recipe : recipes) {
            TextView textView = new TextView(Recipe.this);
            textView.setText(recipe.getRecipeName());
            textView.setTextSize(18);
            textView.setPadding(0, 8, 0, 8);
            // Set background color or icon based on ingredient availability
            // Note: You may need to update this part if ingredient availability is not present in Cookbook object
//            setIngredientAvailabilityVisualIndicator(textView, true); // Assume all recipes have ingredients available
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayRecipeDetails(recipe); // Pass the clicked recipe to display details
                }
            });
            recipeListLayout.addView(textView);
        }
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

    private void displayRecipes() {
        LinearLayout recipeListLayout = findViewById(R.id.linearRecipes);
        rootDatabref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                recipeListLayout.removeAllViews(); // Clear existing views
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Cookbook cookbook = recipeSnapshot.getValue(Cookbook.class);
                    if (cookbook != null) {
                        List<IngredientRequirement> ingredReqs = cookbook.getIngredReqs();
                        if (ingredReqs != null) {
                            // Process the recipe if ingredReqs is not null
                            // Create a TextView for each recipe and add it to the layout
                            TextView textView = new TextView(Recipe.this);
                            textView.setText(cookbook.getRecipeName());
                            textView.setTextSize(18);
                            textView.setPadding(0, 8, 0, 8);
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    displayRecipeDetails(cookbook); // Pass the clicked recipe to display details
                                }
                            });
                            // Set background color or icon based on ingredient availability
                            checkIngredientAvailability(cookbook, new OnIngredientAvailabilityCheckedListener() {
                                @Override
                                public void onAllIngredientsChecked(boolean allAvailable) {
                                    if (allAvailable) {
                                        textView.setBackgroundColor(getResources().getColor(R.color.white)); // Change to the appropriate color
                                    } else {
                                        textView.setBackgroundColor(getResources().getColor(R.color.black)); // Change to the appropriate color
                                    }
                                    recipeListLayout.addView(textView);
                                }
                            });
                        } else {
                            // Handle case where ingredReqs is null
                            // This could be due to missing or improperly formatted data in the database
                            Toast.makeText(Recipe.this, "Error: Ingredients not found for recipe " + cookbook.getRecipeName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Recipe.this, "Failed to load recipes.", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void checkIngredientAvailability(Cookbook recipe, OnIngredientAvailabilityCheckedListener listener) {
        DatabaseReference userPantryRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(manager.getAuth().getUid())
                .child("Pantry");

        int totalIngredients = recipe.getIngredReqs().size();
        AtomicInteger ingredientsChecked = new AtomicInteger(0);

        for (IngredientRequirement ingredient : recipe.getIngredReqs()) {
            String ingredientName = ingredient.getIngredientName();
            // Inside checkIngredientAvailability method
            int requiredQuantity = Integer.parseInt(ingredient.getQuantity().trim());

            userPantryRef.orderByChild("ingredientName").equalTo(ingredientName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Pantry pantryItem = snapshot.getValue(Pantry.class);
                                    if (pantryItem != null) {
                                        int availableQuantity = Integer.parseInt(pantryItem.getQuantity());
                                        if (availableQuantity >= requiredQuantity) {
                                            // Ingredient available in sufficient quantity
                                            ingredientsChecked.incrementAndGet();
                                            if (ingredientsChecked.get() == totalIngredients) {
                                                listener.onAllIngredientsChecked(true);
                                            }
                                            return;
                                        }
                                    }
                                }
                            }
                            // Ingredient not found or not available in sufficient quantity
                            ingredientsChecked.incrementAndGet();
                            if (ingredientsChecked.get() == totalIngredients) {
                                listener.onAllIngredientsChecked(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled event
                            ingredientsChecked.incrementAndGet();
                            if (ingredientsChecked.get() == totalIngredients) {
                                listener.onAllIngredientsChecked(false);
                            }
                        }
                    });
        }
    }

    // Interface for callback
    interface OnIngredientAvailabilityCheckedListener {
        void onAllIngredientsChecked(boolean allAvailable);
    }
    // Method to show recipe details in a new activity or fragment
    private void displayRecipeDetails(Cookbook recipe) {
        // Populate UI elements with recipe details
        TextView recipeNameTextView = findViewById(R.id.recipeNameTextView);
        TextView ingredientsTextView = findViewById(R.id.ingredientsTextView);
        TextView instructionsTextView = findViewById(R.id.instructionsTextView);

        // Set the visibility of the recipe details layout to visible
        findViewById(R.id.recipeDetailsLayout).setVisibility(View.VISIBLE);

        // Set the text of recipe name TextView
        recipeNameTextView.setText(recipe.getRecipeName());

        // Initialize a StringBuilder to construct the ingredients list
        StringBuilder ingredientsBuilder = new StringBuilder();

        // Iterate through each ingredient requirement and append it to the StringBuilder
        for (IngredientRequirement ingredient : recipe.getIngredReqs()) {
            ingredientsBuilder.append(ingredient.getIngredientName())
                    .append(": ")
                    .append(ingredient.getQuantity())
                    .append("\n");
        }

        // Set the text of ingredients TextView with the constructed string
        ingredientsTextView.setText(ingredientsBuilder.toString());

        // Set the text of instructions TextView (you need to replace this with actual instructions)
        instructionsTextView.setText("Instructions: Add instructions here");
    }



}

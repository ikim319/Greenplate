package com.example.greenplate.views;
import java.util.Calendar;
import java.util.Date;
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
import com.example.greenplate.model.Meal;
import com.example.greenplate.model.ShoppingListModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Recipe extends AppCompatActivity {

    public EditText editTextRecipeName;
    public EditText editTextIngredReq;
    private DatabaseReference rootDatabref;
    private FirebaseManager manager;
    private EditText editTextSearch;
    private Button buttonSearch;
    private Button buttonAddToShoppingList;
    private SortingStrategy sortingStrategy;
    public Toast lastToast; // Field to hold the last displayed Toast
    public String lastToastMessage;
    private Cookbook recipe_one;


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
        Button buttonSortAlphabetical = findViewById(R.id.buttonSortAlphabetical);
        Button buttonAddToShoppingList = findViewById(R.id.buttonAddToShoppingList);
        Button buttonCook = findViewById(R.id.buttonCook);
        editTextRecipeName = findViewById(R.id.editTextRecipeName);
        editTextIngredReq = findViewById(R.id.editTextIngredients);
        manager = FirebaseManager.getInstance();
        rootDatabref = FirebaseDatabase.getInstance().getReference().child("Cookbook");

        // Initialize search bar and button
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);

        buttonAddToShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndAddToShoppingList(recipe_one);
            }
        });
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
                SortingStrategy alphabeticalSortingStrategy = new AlphabeticalSortingStrategy();
                setSortingStrategy(alphabeticalSortingStrategy);
                sortRecipes();
            }
        });
        buttonCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cookRecipe(recipe_one); // Pass the clicked recipe to display details
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
        displayRecipes();

    }
    public void setSortingStrategy(SortingStrategy sortingStrategy) {
        this.sortingStrategy = sortingStrategy; //sets sorting strategy
    }
    public void searchRecipe() {
        String searchQuery = editTextSearch.getText().toString().trim();

        if (searchQuery.isEmpty()) {
            lastToast = Toast.makeText(this, "Please enter a recipe name to search.", Toast.LENGTH_SHORT);
            lastToastMessage = "Please enter a recipe name to search.";
            lastToast.show();
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
                    lastToast = Toast.makeText(Recipe.this, "Recipe not found.", Toast.LENGTH_SHORT);
                    lastToastMessage = "Recipe not found.";
                    lastToast.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                lastToast = Toast.makeText(Recipe.this, "Failed to search for recipe.", Toast.LENGTH_SHORT);
                lastToastMessage = "Failed to search for recipe.";
                lastToast.show();
            }
        });
    }

    public void sortRecipes() {
        if (sortingStrategy != null) {
            DatabaseReference recipesRef = FirebaseManager.getInstance().getRef().child("Cookbook");
            recipesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Cookbook> recipes = new ArrayList<>();
                    for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                        // Parse recipe data
                        String recipeName = recipeSnapshot.child("recipeName").getValue(String.class);
                        List<IngredientRequirement> ingredReqs = new ArrayList<>();
                        for (DataSnapshot ingredSnapshot : recipeSnapshot.child("ingredReqs").getChildren()) {
                            String ingredientName = ingredSnapshot.child("ingredientName").getValue(String.class);
                            String quantity = ingredSnapshot.child("quantity").getValue(String.class);
                            // Create IngredientRequirement object and add to list
                            IngredientRequirement ingredientRequirement = new IngredientRequirement(ingredientName, quantity);
                            ingredReqs.add(ingredientRequirement);
                        }
                        // Create Cookbook object and add to list
                        Cookbook cookbook = new Cookbook(recipeName, ingredReqs);
                        recipes.add(cookbook);
                    }
                    // Sort recipes using the specified sorting strategy
                    sortingStrategy.sort(recipes);
                    // Update the recipe list layout with sorted recipes
                    updateRecipeListLayout(recipes);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle database error
                    lastToast = Toast.makeText(Recipe.this, "Failed to load recipes.", Toast.LENGTH_SHORT);
                    lastToastMessage = "Failed to load recipes.";
                    lastToast.show();
                }
            });
        } else {
            // Sorting strategy not set
            lastToast = Toast.makeText(Recipe.this, "Sorting strategy not set.", Toast.LENGTH_SHORT);
            lastToastMessage = "Sorting strategy not set.";
            lastToast.show();
        }
    }

    /* Example usage:
    Recipe recipeActivity = new Recipe();
    SortingStrategy alphabeticalSortingStrategy = new AlphabeticalSortingStrategy();
    recipeActivity.setSortingStrategy(alphabeticalSortingStrategy);
    recipeActivity.sortRecipes(); */


    // Helper method to update the recipe list layout with sorted recipes
    private void updateRecipeListLayout(List<Cookbook> recipes) {
        LinearLayout recipeListLayout = findViewById(R.id.linearRecipes);
        recipeListLayout.removeAllViews();
        for (Cookbook recipe : recipes) {
            TextView textView = new TextView(Recipe.this);
            textView.setText(recipe.getRecipeName());
            textView.setTextSize(18);
            textView.setPadding(0, 8, 0, 8);

            // Check ingredient availability for the recipe
            checkIngredientAvailability(recipe, new OnIngredientAvailabilityCheckedListener() {
                @Override
                public void onAllIngredientsChecked(boolean allAvailable) {
                    // Set background color or icon based on ingredient availability
                    if (allAvailable) {
                        textView.setBackgroundColor(getResources().getColor(R.color.green)); // Change to the appropriate color for available ingredients
                    } else {
                        textView.setBackgroundColor(getResources().getColor(R.color.black)); // Change to the appropriate color for unavailable ingredients
                    }
                    recipeListLayout.addView(textView);
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayRecipeDetails(recipe); // Pass the clicked recipe to display details
                }
            });


        }
    }
    private void cookRecipe(Cookbook recipe) {
        TextView mealText = findViewById(R.id.recipeNameTextView);
        String mealName = mealText.getText().toString();
        TextView caloriesText = findViewById(R.id.caloriesTextView);
        String calories = caloriesText.getText().toString();;
        String numericPart = calories.replaceAll("[^\\d.]", ""); // Remove non-numeric characters
        Date currentDate = new Date();

        String userId = manager.getAuth().getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatabaseReference userMealRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(userId)
                .child("Meal_Log");

        String mealId = userMealRef.push().getKey();
        String formattedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month, day);

        Meal meal = new Meal(mealName, numericPart, formattedDate);
        userMealRef.child(mealId).setValue(meal);

        DatabaseReference userPantryRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(userId)
                .child("Pantry");

        for (IngredientRequirement ingredient : recipe.getIngredReqs()) {
            String ingredientName = ingredient.getIngredientName();
            int requiredQuantity = Integer.parseInt(ingredient.getQuantity().trim());
            System.out.println(ingredientName);
            // Update the pantry quantity for each ingredient used in the recipe
            userPantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot pantrySnapshot : dataSnapshot.getChildren()) {
                            String pantryIngredientName = pantrySnapshot.child("ingredientName").getValue(String.class);
                            System.out.print("pantryIngredientName");
                            if (pantryIngredientName.equals(ingredientName)) {
                                int currentQuantity = Integer.parseInt(pantrySnapshot.child("quantity").getValue(String.class));
                                int updatedQuantity = currentQuantity - requiredQuantity;
                                if (updatedQuantity > 0) {
                                    pantrySnapshot.getRef().child("quantity").setValue(String.valueOf(updatedQuantity));
                                } else {
                                    pantrySnapshot.getRef().removeValue();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event
                    Toast.makeText(Recipe.this, "Error updating pantry", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void saveCookBook() {
        String recipeName = editTextRecipeName.getText().toString().trim();
        String ingredientsText = editTextIngredReq.getText().toString().trim();

        // Check if any field is empty
        if (recipeName.isEmpty() || ingredientsText.isEmpty()) {
            lastToast = Toast.makeText(Recipe.this, "Please fill in all fields", Toast.LENGTH_SHORT);
            lastToastMessage = "Please fill in all fields.";
            lastToast.show();
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

        lastToast = Toast.makeText(Recipe.this, "Saved successfully!", Toast.LENGTH_SHORT);
        lastToastMessage = "Saved successfully!";
        lastToast.show();
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
                                    recipe_one = cookbook;
                                }
                            });
                            // Set background color or icon based on ingredient availability
                            checkIngredientAvailability(cookbook, new OnIngredientAvailabilityCheckedListener() {
                                @Override
                                public void onAllIngredientsChecked(boolean allAvailable) {
                                    if (allAvailable) {
                                        textView.setBackgroundColor(getResources().getColor(R.color.green)); // Change to the appropriate color
                                    } else {
                                        textView.setBackgroundColor(getResources().getColor(R.color.black)); // Change to the appropriate color
                                    }
                                    recipeListLayout.addView(textView);
                                }
                            });
                        } else {
                            // Handle case where ingredReqs is null
                            // This could be due to missing or improperly formatted data in the database
                            lastToast = Toast.makeText(Recipe.this, "Error: Ingredients not found for recipe " + cookbook.getRecipeName(), Toast.LENGTH_SHORT);
                            lastToastMessage = "Error: Ingredients not found for recipe " + cookbook.getRecipeName();
                            lastToast.show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                lastToast = Toast.makeText(Recipe.this, "Failed to load recipes.", Toast.LENGTH_SHORT);
                lastToastMessage = "Failed to load recipes.";
                lastToast.show();
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
            // Extract the numeric part of the quantity string and parse it as an integer
            int requiredQuantity;
            try {
                // Remove non-numeric characters from the quantity string
                String numericPart = ingredient.getQuantity().replaceAll("[^\\d.]", "");
                // Parse the numeric part as an integer
                requiredQuantity = Integer.parseInt(numericPart);
            } catch (NumberFormatException e) {
                // Handle the case where the quantity cannot be parsed as an integer
                // You may want to log an error message or take appropriate action
                e.printStackTrace();
                // Set required quantity to 0 or handle the error as needed
                requiredQuantity = 0;
            }
            int finalRequiredQuantity = requiredQuantity;
            userPantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot pantrySnapshot : dataSnapshot.getChildren()) {
                            String pantryIngredientName = pantrySnapshot.child("ingredientName").getValue(String.class);
                            if (pantryIngredientName.equals(ingredientName)) {
                                int availableQuantity = Integer.parseInt(pantrySnapshot.child("quantity").getValue(String.class));
                                if (availableQuantity >= finalRequiredQuantity) {
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
        System.out.println("Reached Deatils");
        // Populate UI elements with recipe details
        TextView recipeNameTextView = findViewById(R.id.recipeNameTextView);
        TextView ingredientsTextView = findViewById(R.id.ingredientsTextView);
        TextView caloriesTextView = findViewById(R.id.caloriesTextView);

        // Set the visibility of the recipe details layout to visible
        findViewById(R.id.recipeDetailsLayout).setVisibility(View.VISIBLE);

        // Set the text of recipe name TextView
        recipeNameTextView.setText(recipe.getRecipeName());

        // Initialize a StringBuilder to construct the ingredients list and calories
        StringBuilder ingredientsBuilder = new StringBuilder();
        final int[] calories = {0};

        DatabaseReference userPantryRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(manager.getAuth().getUid())
                .child("Pantry");
        // Iterate through each ingredient requirement and append it to the StringBuilder
        String calorieText = null;
        for (IngredientRequirement ingredient : recipe.getIngredReqs()) {
            ingredientsBuilder.append(ingredient.getIngredientName())
                    .append(": ")
                    .append(ingredient.getQuantity())
                    .append("\n");
            final int[] ingredCal = new int[1];
            userPantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    AtomicInteger ingredientsProcessed = new AtomicInteger(0);
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot pantrySnapshot : dataSnapshot.getChildren()) {
                            String pantryIngredientName = pantrySnapshot.child("ingredientName").getValue(String.class);
                            if (pantryIngredientName.equals(ingredient.getIngredientName())) {
                                System.out.println(pantrySnapshot.child("ingredientCalories").getValue());
                                ingredCal[0] = (Integer.parseInt((String) pantrySnapshot.child("ingredientCalories").getValue()));
                                String quantityString = ingredient.getQuantity().trim();
                                String numericPart = quantityString.replaceAll("[^\\d.]", ""); // Remove non-numeric characters
                                int requiredQuantity = Integer.parseInt(numericPart);
                                calories[0] += (requiredQuantity * ingredCal[0]);
                                String calorieText = "Calories: " + calories[0];
                                System.out.println(calorieText);
                                caloriesTextView.setText(calorieText);
                            }
                        }
                    }
                    int count = ingredientsProcessed.incrementAndGet();
                    if (count == recipe.getIngredReqs().size()) {
                        runOnUiThread(() -> {
                            // Set the text of ingredients TextView with the constructed string
                            ingredientsTextView.setText(ingredientsBuilder.toString());

                            // Set the text of calories TextView
                            String calorieText = "Calories: " + calories[0];
                            System.out.println(calorieText);
                            caloriesTextView.setText(calorieText);
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled event
                    return;
                }
            });
        }


        // Set the text of ingredients TextView with the constructed string
        ingredientsTextView.setText(ingredientsBuilder.toString());
        // Set the text of instructions TextView (you need to replace this with actual instructions
    }

    public void checkAndAddToShoppingList(Cookbook recipe) {
        fetchPantryItems(new OnPantryItemsFetchedListener() {
            @Override
            public void onPantryItemsFetched(List<Pantry> pantryItems) {
                if (pantryItems != null) {
                    List<shoppingIngredient> neededIngredients = new ArrayList<>();
                    for (IngredientRequirement required : recipe.getIngredReqs()) {
                        int found = 0;
                        String requiredName = required.getIngredientName();
                        int requiredQuantity = Integer.parseInt(required.getQuantity().trim());
                        for (Pantry item : pantryItems) {
                            if (item.getIngredientName().equals(requiredName)) {
                                found = 1;
                                int currQuantity = Integer.parseInt(item.getQuantity());
                                int remainingQuantity = requiredQuantity - currQuantity;
                                if (remainingQuantity > 0) {
                                    neededIngredients.add(new shoppingIngredient(requiredName, Integer.toString(remainingQuantity)));
                                }
                                break;
                            }
                        }
                        if (found == 0) {
                            neededIngredients.add(new shoppingIngredient(requiredName, required.getQuantity()));
                        }
                    }
                addMissingToShoppingList(neededIngredients);

                } else {
                   return;
                }
            }
        });
    }

    private void addMissingToShoppingList(List<shoppingIngredient> missingList) {
        DatabaseReference userShoppingListRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(manager.getAuth().getUid())
                .child("ShoppingList");

        for (shoppingIngredient item : missingList) {
            final String ingredientName = item.getIngredientName();
            final String quantity = item.getQuantity();

            userShoppingListRef.orderByChild("ingredientName").equalTo(ingredientName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Ingredient already exists in the shopping list, update quantity if needed
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    snapshot.getRef().child("quantity").setValue(String.valueOf(quantity));
                                }
                            } else {
                                // Ingredient does not exist in the shopping list, add new item
                                ShoppingListModel listItem = new ShoppingListModel(ingredientName, quantity);
                                String itemId = userShoppingListRef.push().getKey();
                                userShoppingListRef.child(itemId).setValue(listItem);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle onCancelled event
                            Toast.makeText(Recipe.this, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void fetchPantryItems(OnPantryItemsFetchedListener listener) {
        DatabaseReference userPantryRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(manager.getAuth().getUid())
                .child("Pantry");

        List<Pantry> pantryItems = new ArrayList<>();

        userPantryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot pantrySnapshot : dataSnapshot.getChildren()) {
                    String ingredientName = pantrySnapshot.child("ingredientName").getValue(String.class);
                    String quantity = pantrySnapshot.child("quantity").getValue(String.class);
                    String calories = pantrySnapshot.child("ingredientCalories").getValue(String.class);
                    String expireDate = pantrySnapshot.child("poExpire").getValue(String.class);

                    Pantry pantryItem = new Pantry(ingredientName, quantity, calories, expireDate);
                    pantryItems.add(pantryItem);
                }
                listener.onPantryItemsFetched(pantryItems);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled event
                listener.onPantryItemsFetched(null);
            }
        });
    }
    interface OnPantryItemsFetchedListener {
        void onPantryItemsFetched(List<Pantry> pantryItems);
    }
}
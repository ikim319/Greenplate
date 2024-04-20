package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.greenplate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class Cart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Nav Bar Button instantiation
        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonIngredients = findViewById(R.id.Ingredient);
        Button buttonInputMeal = findViewById(R.id.btn_inputmeal);
        Button buttonRecipe = findViewById(R.id.Recipe);
        Button buttonBackWelcome = findViewById(R.id.Logout);
        Button buttonPersonalInfo = findViewById(R.id.PInformation);
        Button buttonShoppingList = findViewById(R.id.shoppinglist);
        Button buttonBuyItems = findViewById(R.id.btn_buy_items);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, Home.class));
            }
        });

        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, Recipe.class));
            }
        });

        buttonIngredients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, Ingredients.class));
            }
        });

        buttonInputMeal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, InputMeal.class));
            }
        });

        buttonBackWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, Login.class));
            }
        });

        buttonPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, PersonalInformation.class));
            }
        });

        // Fetch shopping list items from Firebase and display them in the cart
        displayShoppingListItems();

        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Cart.this, ShoppingList.class));
            }
        });

        buttonBuyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyItems();
            }
        });
    }

    private void displayShoppingListItems() {
        DatabaseReference shoppingListRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("ShoppingList");

        shoppingListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> shoppingItems = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemName = snapshot.child("ingredientName").getValue(String.class);
                    if (itemName != null) { // Check if itemName is not null
                        shoppingItems.add(itemName);
                        Log.d("ShoppingItemsNew", "Item Name: " + itemName);
                    } else {
                        Log.d("ShoppingItemsNew", "Item Name is null");
                    }
                }
                displayShoppingItems(shoppingItems);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Toast.makeText(Cart.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void displayShoppingItems(ArrayList<String> shoppingItems) {
        LinearLayout cartLayout = findViewById(R.id.cartLayout);
        cartLayout.removeAllViews(); // Clear existing views to prevent duplication

        for (String item : shoppingItems) {
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);

                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(item);

                itemLayout.addView(checkBox);
                cartLayout.addView(itemLayout);
        }
    }


    private void buyItems() {
        LinearLayout cartLayout = findViewById(R.id.cartLayout);
        ArrayList<String> itemsToRemove = new ArrayList<>();
        ArrayList<View> viewsToRemove = new ArrayList<>();

        for (int i = 0; i < cartLayout.getChildCount(); i++) {
            View itemView = cartLayout.getChildAt(i);
            if (itemView instanceof LinearLayout) {
                LinearLayout itemLayout = (LinearLayout) itemView;
                CheckBox checkBox = (CheckBox) itemLayout.getChildAt(0);
                if (checkBox.isChecked()) {
                    // Get the text (item name) of the checked checkbox
                    String itemName = checkBox.getText().toString();
                    // Add the item to the list of items to be removed from the shopping list database
                    itemsToRemove.add(itemName);
                    // Add the view to be removed to the list
                    viewsToRemove.add(itemView);
                }
            }
        }

        // Remove checked items from the shopping list database
        removeItemsFromShoppingList(itemsToRemove);

        // Remove the views associated with checked items from the cart layout
        for (View view : viewsToRemove) {
            cartLayout.removeView(view);
        }

        // Add checked items to the user's pantry database
        addToPantry(itemsToRemove);

        // Refresh the cart view
        displayShoppingListItems();
    }




    private void removeItemsFromShoppingList(ArrayList<String> itemsToRemove) {
        DatabaseReference shoppingListRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("ShoppingList");

        // Iterate over the list of items to be removed
        Iterator<String> iterator = itemsToRemove.iterator();
        while (iterator.hasNext()) {
            String itemName = iterator.next();

            // Query the database to find the corresponding item
            shoppingListRef.orderByChild("ingredientName").equalTo(itemName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Remove the item from the database
                                snapshot.getRef().removeValue();
                                    iterator.remove(); // Remove the item from the local ArrayList
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(Cart.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    private void addToPantry(ArrayList<String> itemsToAdd) {
        DatabaseReference shoppingListRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("ShoppingList");

        DatabaseReference pantryRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Pantry");

        // Add each item to the pantry
        for (String itemName : itemsToAdd) {
            // Check if the item already exists in the pantry
            pantryRef.orderByChild("ingredientName").equalTo(itemName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Item already exists in the pantry, update its quantity if needed
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    // Update the quantity based on the shopping list
                                    String quantity = snapshot.child("quantity").getValue(String.class);
                                    if (quantity != null) {
                                        snapshot.getRef().child("quantity").setValue(quantity);
                                    }
                                }
                            } else {
                                    // Item doesn't exist in the pantry, add it with default values
                                    // Get default expiration date (2 weeks ahead of current date)
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.add(Calendar.WEEK_OF_YEAR, 2);
                                    Date expirationDate = calendar.getTime();

                                    // Set default calorie count (if applicable)
                                    int defaultCalories = getDefaultCaloriesForItem(itemName);

                                    // Create a new entry in the pantry for the item
                                    String itemId = pantryRef.push().getKey();
                                    Pantry pantryItem = new Pantry(itemName, "1", String.valueOf(defaultCalories), expirationDate.toString());
                                    pantryRef.child(itemId).setValue(pantryItem);
                                }
                            }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(Cart.this, "Database Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private int getDefaultCaloriesForItem(String itemName) {
        // Default calorie count for all items (can be adjusted as needed)
        return 100;
    }


}

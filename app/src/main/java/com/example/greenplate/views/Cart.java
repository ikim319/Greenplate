package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
                    String itemName = snapshot.child("itemName").getValue(String.class);
                    shoppingItems.add(itemName);
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
                }
            }
        }

        // Remove checked items from the shopping list database
        removeItemsFromShoppingList(itemsToRemove);

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

        // Remove each item from the shopping list
        for (String itemName : itemsToRemove) {
            shoppingListRef.orderByChild("ingredientName").equalTo(itemName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue();
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
            // Retrieve additional information about the item from the shopping list
            shoppingListRef.orderByChild("ingredientName").equalTo(itemName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Get quantity, expiration date, and other necessary information from the shopping list entry
                                String quantity = snapshot.child("quantity").getValue(String.class);
                                String expirationDate = snapshot.child("expirationDate").getValue(String.class);
                                String poExpire = snapshot.child("poExpire").getValue(String.class);
                                // You can retrieve other necessary information similarly

                                // Create a new entry in the pantry for the item
                                String itemId = pantryRef.push().getKey();
                                Pantry pantryItem = new Pantry(itemName, quantity, expirationDate, poExpire);
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
}

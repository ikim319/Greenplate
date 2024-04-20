package com.example.greenplate.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenplate.R;
import com.example.greenplate.model.ShoppingListModel;
import com.example.greenplate.viewModels.ShoppingListViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ShoppingList extends AppCompatActivity {
    EditText editTextIngredientName, editTextQuantities;
    private DatabaseReference rootDatabref;
    private FirebaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_shopping_list);

        //Nav Bar Button instantiation
        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonIngredients = findViewById(R.id.Ingredient);
        Button buttonInputMeal = findViewById(R.id.btn_inputmeal);
        Button buttonRecipe = findViewById(R.id.Recipe);
        Button buttonBackWelcome = findViewById(R.id.Logout);
        Button buttonPersonalInfo = findViewById(R.id.PInformation);
        Button buttonCart = findViewById(R.id.btn_Cart);


        //Form button instantiation
        Button buttonAdd = findViewById(R.id.btn_add);
        Button addIngredientButton = findViewById(R.id.addShoppingItem);


        RelativeLayout addShoppingForm = findViewById(R.id.shoppingForm);
        LinearLayout shoppingListLayout = findViewById(R.id.linearShopping);


        editTextIngredientName = findViewById(R.id.editTextIngredientName);
        editTextQuantities = findViewById(R.id.editTextQuantities);

        manager = FirebaseManager.getInstance();
        rootDatabref = FirebaseDatabase.getInstance().getReference().child("ShoppingList");


        final ScrollView scrollView = findViewById(R.id.scrollShopping);
        DatabaseReference userShoppingRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(manager.getAuth().getUid())
                .child("ShoppingList");
        userShoppingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shoppingListLayout.removeAllViews();
                for (DataSnapshot ingredientSnapshot : dataSnapshot.getChildren()) {
                    String ingredientName = ingredientSnapshot.child("ingredientName").getValue(String.class);
                    String ingredientQuantity = ingredientSnapshot.child("quantity").getValue(String.class);

                    LinearLayout shoppingLayout = new LinearLayout(ShoppingList.this);
                    shoppingLayout.setOrientation(LinearLayout.HORIZONTAL);
                    shoppingLayout.setGravity(Gravity.CENTER_HORIZONTAL); // Center elements horizontally
                    shoppingLayout.setBackgroundResource(R.drawable.border_background);

                    TextView textView = new TextView(ShoppingList.this);
                    String nameAndQuantity = ingredientName + ":    " + ingredientQuantity;
                    textView.setText(nameAndQuantity);

                    Button addButton = new Button(ShoppingList.this);
                    addButton.setText("+");
                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int newQuantity = Integer.parseInt(ingredientQuantity) + 1;
                            ingredientSnapshot.getRef().child("quantity").setValue(String.valueOf(newQuantity));
                        }
                    });

                    Button subtractButton = new Button(ShoppingList.this);
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

                    shoppingLayout.addView(textView);
                    shoppingLayout.addView(addButton);
                    shoppingLayout.addView(subtractButton);
                    shoppingListLayout.addView(shoppingLayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ShoppingList.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingList.this, Cart.class));
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(ShoppingList.this, Home.class));
            }
        });
        
        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(ShoppingList.this, Recipe.class));
            }
        });

        buttonIngredients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(ShoppingList.this, Ingredients.class));
            }
        });

        buttonInputMeal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(ShoppingList.this, InputMeal.class));
            }
        });

        buttonBackWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingList.this, Login.class));
            }
        });

        buttonPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShoppingList.this, PersonalInformation.class));
            }
        });

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShoppingForm.setVisibility(View.VISIBLE);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveList();
            }
        });
    }
    private void saveList() {
        final String ingredientName = editTextIngredientName.getText().toString();
        final String quantity = editTextQuantities.getText().toString();
        String valid = checkValidity(ingredientName, quantity);
        if (valid.equals("Failed: Negative Value or 0.")
                || valid.equals("Failed: Calories and Quantity must be numbers.") ||
                valid.equals("Failed: All fields must be nonempty.")) {
            Toast.makeText(ShoppingList.this, valid, Toast.LENGTH_SHORT).show();
            return;
        }

        final DatabaseReference userShoppingRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(manager.getAuth().getUid())
                .child("ShoppingList");

        userShoppingRef.orderByChild("ingredientName").equalTo(ingredientName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Toast.makeText(ShoppingList.this, "Error: Ingredient already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            ShoppingListModel listItem = new ShoppingListModel(ingredientName, quantity);
                            String itemId = userShoppingRef.push().getKey();
                            userShoppingRef.child(itemId).setValue(listItem);
                            Toast.makeText(ShoppingList.this, "Saved successfully!", Toast.LENGTH_SHORT).show();

                            editTextIngredientName.setText("");
                            editTextQuantities.setText("");
                            RelativeLayout shoppingForm = findViewById(R.id.shoppingForm);
                            shoppingForm.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ShoppingList.this, "Database Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String checkValidity(String ingredientName, String quantity) {
        ShoppingListViewModel inputView = new ShoppingListViewModel();
        return inputView.checkValidIngredientEntry(ingredientName, quantity);
    }
}


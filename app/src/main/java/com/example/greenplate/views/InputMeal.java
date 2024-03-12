package com.example.greenplate.views;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenplate.R;
import com.example.greenplate.model.Meal;
import com.example.greenplate.viewModels.InputMealViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class InputMeal extends AppCompatActivity {


    EditText editTextMeal, editTextCalories;
    FirebaseManager manager;
    private DatabaseReference rootDatabref;
    private DatabaseReference userDatabref;

    public static int todaysCalories;
    public static int caloricGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meal);
        Button buttonHome = findViewById(R.id.btn_Home);
        Button buttonIngredients = findViewById(R.id.Ingredient);
        Button buttonRecipe = findViewById(R.id.Recipe);
        Button buttonShoppingList = findViewById(R.id.shoppinglist);
        Button buttonBackWelcome = findViewById(R.id.Logout);
        Button buttonPersonalInfo = findViewById(R.id.PInformation);
        Button buttonLog = findViewById(R.id.btn_Log);
        Button buttonCaloriesVsTime = findViewById(R.id.calories_vs_time);
        Button buttonTodayIntake = findViewById(R.id.today_intake);

        TextView textViewHeightValue = findViewById(R.id.textViewHeightValue);
        TextView textViewHeight = findViewById(R.id.textViewHeight);
        TextView textViewWeight = findViewById(R.id.textViewWeight);
        TextView textViewGender = findViewById(R.id.textViewGender);

        TextView textViewWeightValue = findViewById(R.id.textViewWeightValue);
        TextView textViewGenderValue = findViewById(R.id.textViewGenderValue);
        TextView textViewCalorieGoalValue = findViewById(R.id.textViewCalorieGoalValue);
        TextView textViewCalorieGoal = findViewById(R.id.textViewCalorieGoal);

        TextView textViewTodayCaloriesValue = findViewById(R.id.textViewTodayCaloriesValue);
        TextView textViewTodayCalories = findViewById(R.id.textViewTodayCalories);

        TextView inputInfoTextView = findViewById(R.id.inputInfoTextView);
        TextView textViewAccountInfo = findViewById(R.id.textViewAccountInfo);
        inputInfoTextView.setVisibility(View.GONE);




        manager = FirebaseManager.getInstance();
        editTextMeal = findViewById(R.id.editTextMeal);
        editTextCalories = findViewById(R.id.editTextCalories);

        rootDatabref = manager.getRef().child("Meal");


        String userId = manager.getAuth().getCurrentUser().getUid();

        userDatabref = manager.getRef().child("Users").child(userId).child("Personal_Info");

        userDatabref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String height = dataSnapshot.child("height").getValue(String.class);
                String weight = dataSnapshot.child("weight").getValue(String.class);
                String gender = dataSnapshot.child("gender").getValue(String.class);
                String calorieGoal = calorieCounter(height, weight, gender);

                if (height == null || weight == null || gender == null) {

                    textViewHeightValue.setVisibility(View.GONE);
                    textViewWeightValue.setVisibility(View.GONE);
                    textViewGenderValue.setVisibility(View.GONE);
                    textViewHeight.setVisibility(View.GONE);
                    textViewWeight.setVisibility(View.GONE);
                    textViewGender.setVisibility(View.GONE);
                    textViewCalorieGoal.setVisibility(View.GONE);
                    textViewAccountInfo.setVisibility(View.GONE);
                    textViewTodayCalories.setVisibility(View.GONE);
                    inputInfoTextView.setVisibility(View.VISIBLE);
                } else {
                    textViewCalorieGoal.setVisibility(View.VISIBLE);
                    textViewAccountInfo.setVisibility(View.VISIBLE);
                    textViewTodayCalories.setVisibility(View.VISIBLE);
                    textViewHeightValue.setVisibility(View.VISIBLE);
                    textViewWeightValue.setVisibility(View.VISIBLE);
                    textViewGenderValue.setVisibility(View.VISIBLE);
                    textViewHeight.setVisibility(View.VISIBLE);
                    textViewWeight.setVisibility(View.VISIBLE);
                    textViewGender.setVisibility(View.VISIBLE);
                    textViewHeightValue.setText(height);
                    textViewWeightValue.setText(weight);
                    textViewGenderValue.setText(gender);
                    textViewCalorieGoalValue.setText(calorieGoal);
                    fetchMealsForToday();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeal();
                fetchMealsForToday();
            }

        });

        buttonCaloriesVsTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, CaloriesVsTime.class));
            }

        });

        buttonTodayIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, TodayIntake.class));
            }

        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, Home.class));
            }
        });
        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, Recipe.class));
            }
        });
        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, ShoppingList.class));
            }
        });

        buttonIngredients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, Ingredients.class));
            }
        });

        buttonBackWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, Login.class));
            }
        });

        buttonPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, PersonalInformation.class));
            }
        });
    }

    private void saveMeal() {
        String mealName = editTextMeal.getText().toString();
        String calories = editTextCalories.getText().toString();
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

        Meal meal = new Meal(mealName, calories, formattedDate);
        userMealRef.child(mealId).setValue(meal);

        Toast.makeText(InputMeal.this, "Meal saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void fetchMealsForToday() {
        TextView textViewTodayCaloriesValue = findViewById(R.id.textViewTodayCaloriesValue);
        String userId = manager.getAuth().getCurrentUser().getUid();

        DatabaseReference userMealRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(userId)
                .child("Meal_Log");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are zero-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String formattedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month, day);

        userMealRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int todayCalories = 0;
                // Iterate through all meals
                for (DataSnapshot mealSnapshot : dataSnapshot.getChildren()) {
                    // Get the meal data
                    String mealDate = mealSnapshot.child("date").getValue(String.class);
                    String calories = mealSnapshot.child("calories").getValue(String.class);
                    // Check if the meal date matches today's date
                    if (mealDate != null && mealDate.equals(formattedDate)) {
                        // Add the calories to the total for today
                        if (calories != null && !calories.isEmpty()) {
                            todayCalories += Integer.parseInt(calories);
                        }
                    }
                }
                // After calculating total calories, you can use it as needed
                String todayCaloriesString = String.valueOf(todayCalories);
                todaysCalories = todayCalories;
                textViewTodayCaloriesValue.setText(todayCaloriesString);
                String totalCaloriesString = String.valueOf(todayCalories);
                textViewTodayCaloriesValue.setText(totalCaloriesString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                textViewTodayCaloriesValue.setText("N/A");
            }
        });
    }

    private String calorieCounter(String height, String weight, String gender) {
        int heightInt = 0;
        if (height != null && !height.isEmpty()) {
            heightInt = Integer.parseInt(height);
        }
        int weightInt = 0;
        if (weight != null && !weight.isEmpty()) {
            weightInt = (int) Integer.parseInt(weight);
        }
        int calorieGoal = 0;

        if (gender != null && gender.equals("Male")) {
            calorieGoal = (int) (((6.23 * weightInt) + (12.7 * heightInt) + 66) * 1.55);
        } else {
            calorieGoal = (int) (((4.35 * weightInt) + (4.7 * heightInt) + 65) * 1.55);
        }
        caloricGoal = calorieGoal;
        return Integer.toString(calorieGoal);
    }
    public String calorieCounter2(String height, String weight, String gender) {
        InputMealViewModel inputView = new InputMealViewModel();
        return inputView.calorieCounter(height, weight, gender);
    }
}

package com.example.greenplate.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenplate.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import com.anychart.AnyChart;
//import com.anychart.AnyChartView;
//import com.anychart.chart.common.dataentry.DataEntry;
//import com.anychart.chart.common.dataentry.ValueDataEntry;
//import java.util.ArrayList;
public class InputMeal extends AppCompatActivity {


    EditText editTextMeal, editTextCalories;
    FirebaseManager manager;
    private DatabaseReference rootDatabref;
    private DatabaseReference userDatabref;

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

        // Initialize Firebase Database reference for the user's data
        userDatabref = manager.getRef().child("Users").child(userId).child("Personal_Info");

        // Attach a ValueEventListener to read data from Firebase
        userDatabref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated

                // Retrieve the data from the dataSnapshot
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
                    // Add the TextView in place of TextViews
                    inputInfoTextView.setVisibility(View.VISIBLE);
                } else {
                    // Update the TextViews with the retrieved data
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

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(InputMeal.this, Home.class));
            }
        });
        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(InputMeal.this, Recipe.class));
            }
        });
        buttonShoppingList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
                startActivity(new Intent(InputMeal.this, ShoppingList.class));
            }
        });

        buttonIngredients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // navigate to our login
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
        // Get current date
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
                        todayCalories += Integer.parseInt(calories);
                    }
                }
                // After calculating total calories, you can use it as needed
                String totalCaloriesString = String.valueOf(todayCalories);
                textViewTodayCaloriesValue.setText(totalCaloriesString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private String calorieCounter(String height, String weight, String gender) {
        // Check if any of the parameters are null
        if (height == null || weight == null || gender == null) {
            return "N/A"; // or any other default value or error message
        }

        int heightInt;
        int weightInt;
        try {
            heightInt = Integer.parseInt(height);
            weightInt = Integer.parseInt(weight);
        } catch (NumberFormatException e) {
            // Handle the case where height or weight cannot be parsed to integers
            return "N/A"; // or any other default value or error message
        }

        int calorieGoal = 0;

        if (gender.equals("Male")) {
            calorieGoal = (int) (((6.23 * weightInt) + (12.7 * heightInt) + 66) * 1.55);
        } else {
            calorieGoal = (int) (((4.35 * weightInt) + (4.7 * heightInt) + 65) * 1.55);
        }

        return Integer.toString(calorieGoal);
    }


/*    
=======



//        private AnyChartView chartView;
//        private Button userVisualizationButton;
//        private Button mealVisualizationButton;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_input_meal);
//
//            chartView = findViewById(R.id.chart_view);
//            userVisualizationButton = findViewById(R.id.user_visualization_button);
//            mealVisualizationButton = findViewById(R.id.meal_visualization_button);
//
//            userVisualizationButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Generate and display user visualization
//                    displayUserVisualization();
//                }
//            });
//            mealVisualizationButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Generate and display meal database visualization
//                    displayMealVisualization();
//                }
//            });
//        }
//
//        private void displayUserVisualization() {
//            // Fetch user data and create visualization using AnyChart
//            // For example, create a bar chart showing daily caloric intake over the past month
//            AnyChart.columnChart()
//                    .data(generateUserData())
//                    .title("Daily Caloric Intake")
//                    .xAxisTitle("Date")
//                    .yAxisTitle("Calories")
//                    .render(chartView);
//
//            private void displayMealVisualization() {
//                // Fetch meal database data and create visualization using AnyChart
//            }
//            // For example, create a pie chart showing distribution of meal types
//            AnyChart.pieChart()
//                    .data(generateMealData())
//                    .title("Meal Distribution")
//                    .render(chartView);
//        }
//
//        private List<DataEntry> generateUserData() {
//            // Generate dummy user data for demonstration
//            List<DataEntry> data = new ArrayList<>();
//            // Add data entries for each day's caloric intake
//            // You would fetch this data from the user database
//            data.add(new ValueDataEntry("Day 1", 2000));
//            data.add(new ValueDataEntry("Day 2", 1800));
//            // Add more data entries...
//            return data;
//        }
//
//        private List<DataEntry> generateMealData() {
//            // Generate dummy meal data for demonstration
//            List<DataEntry> data = new ArrayList<>();
//            // Add data entries for meal types and their frequency
//            // You would fetch this data from the meal database
//            data.add(new ValueDataEntry("Breakfast", 3));
//            data.add(new ValueDataEntry("Lunch", 5));
//            // Add more data entries...
//            return data;
//        }
 */

}

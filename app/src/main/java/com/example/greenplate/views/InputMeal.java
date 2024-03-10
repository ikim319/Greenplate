package com.example.greenplate.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greenplate.R;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import com.anychart.AnyChart;
//import com.anychart.AnyChartView;
//import com.anychart.chart.common.dataentry.DataEntry;
//import com.anychart.chart.common.dataentry.ValueDataEntry;
//import java.util.ArrayList;
public class InputMeal extends AppCompatActivity {


    EditText editTextMeal, editTextCalories;
    FirebaseManager manager;
    private DatabaseReference rootDatabref;
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
        manager = FirebaseManager.getInstance();
        editTextMeal = findViewById(R.id.editTextMeal);
        editTextCalories = findViewById(R.id.editTextCalories);

        rootDatabref = manager.getRef().child("Meal");

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMeal();
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

    private void saveMeal() {
        String mealName = editTextMeal.getText().toString();
        String calories = editTextCalories.getText().toString();

        String userId = manager.getAuth().getCurrentUser().getUid();

        DatabaseReference userMealRef = FirebaseManager.getInstance().getRef()
                .child("Users")
                .child(userId)
                .child("Meal_Log");

        String mealId = userMealRef.push().getKey();

        Meal meal = new Meal(mealName, calories);
        userMealRef.child(mealId).setValue(meal);

        Toast.makeText(InputMeal.this, "Meal saved successfully!", Toast.LENGTH_SHORT).show();
    }

    
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
//    }



    /*
    method for updating the height, weight, and gender displayed on InputMeal
    waiting for database integration to finish
     */
        private void updateUserInfoTextViews (String height, String weight, String gender){
            TextView textViewHeightValue = findViewById(R.id.textViewHeightValue);
            TextView textViewWeightValue = findViewById(R.id.textViewWeightValue);
            TextView textViewGenderValue = findViewById(R.id.textViewGenderValue);

            textViewHeightValue.setText(height);
            textViewWeightValue.setText(weight);
            textViewGenderValue.setText(gender);

        }
    }

}
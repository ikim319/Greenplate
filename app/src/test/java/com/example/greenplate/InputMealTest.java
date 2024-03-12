package com.example.greenplate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.example.greenplate.viewModels.InputMealViewModel;
import com.example.greenplate.views.InputMeal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InputMealTest {

    @Test
    public void testCalorieCounter_Male() {
        InputMealViewModel viewModel = new InputMealViewModel();
        String height = "68";
        String weight = "135";
        String gender = "Male";
        String result = viewModel.calorieCounter(height, weight, gender);
        assertEquals("Calorie calculation for Male is correct", "2744", result);
    }

    @Test
    public void testCalorieCounter_Female() {
        InputMealViewModel viewModel = new InputMealViewModel();
        String height = "68";
        String weight = "135";
        String gender = "Female";

        String result = viewModel.calorieCounter(height, weight, gender);

        assertEquals("Calorie calculation for Female is incorrect", "1506", result);
    }

    @Test
    public void testCalorieCounter_NullInput() {
        InputMealViewModel viewModel = new InputMealViewModel();
        String height = null;
        String weight = "60";
        String gender = "Female";

        String result = viewModel.calorieCounter(height, weight, gender);

        assertEquals("Calorie calculation for Null input should return N/A", "N/A", result);
    }

    @Test
    public void testCalorieCounter_NonNumericInput() {
        InputMealViewModel viewModel = new InputMealViewModel();
        String height = "abc";
        String weight = "60";
        String gender = "Female";

        String result = viewModel.calorieCounter(height, weight, gender);

        assertEquals("Calorie calculation for Non-numeric input should return N/A", "N/A", result);
    }

    // Test case for verifying calorie calculation when all inputs are empty
    @Test
    public void testCalorieCounter_AllInputsEmpty() {
        InputMealViewModel viewModel = new InputMealViewModel();
        String height = "";
        String weight = "";
        String gender = "";
        String result = viewModel.calorieCounter(height, weight, gender);
        assertEquals("Calorie calculation for all empty inputs should return N/A", "N/A", "N/A");
    }

    // Test case for verifying calorie calculation when all inputs are strings with spaces
    @Test
    public void testCalorieCounter_AllInputsEmptySpaces() {
        InputMealViewModel viewModel = new InputMealViewModel();
        String height = "      ";
        String weight = "      ";
        String gender = "      ";
        String result = viewModel.calorieCounter(height, weight, gender);
        assertEquals("Calorie calculation for all empty inputs should return N/A", "N/A", "N/A");
    }

    // Test case for verifying calorie calculation when weight and height are negative
    @Test
    public void testCalorieCounter_NegativeWeight() {
        InputMealViewModel viewModel = new InputMealViewModel();
        String height = "-68";
        String weight = "-135";
        String gender = "Male";
        String result = viewModel.calorieCounter(height, weight, gender);
        assertEquals("Calorie calculation for negative weight should return N/A", "N/A", result);
    }
}

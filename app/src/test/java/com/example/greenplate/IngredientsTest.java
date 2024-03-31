package com.example.greenplate;

import static junit.framework.TestCase.assertEquals;

import com.example.greenplate.viewModels.PantryViewModel;
import com.example.greenplate.views.Ingredients;
import com.example.greenplate.views.Pantry;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IngredientsTest {
    // Test for negative or 0 quantities
    @Test
    public void TestIngredientsValidity1() {
        PantryViewModel test1 = new PantryViewModel();
        String quantity = "-1";
        String resultVal = test1.negativeQuantity(quantity);
        assertEquals("Quantity value for ingredient is invalid.", "Failed: Negative Value or 0.", resultVal);
    }

    // Test for properly asserted quantities (positive)
    @Test
    public void TestIngredientsValidity2() {
        PantryViewModel test2 = new PantryViewModel();
        String quantity2 = "2";
        String resultVal2 = test2.negativeQuantity(quantity2);
        assertEquals("Quantity value for ingredient is valid.", "2", resultVal2);

    }


    // Test for duplicate ingredient entry
    @Test
    public void testDuplicateIngredient() {
        PantryViewModel pantryViewModel = new PantryViewModel();
        List<Pantry> existingIngredients = new ArrayList<>();
        existingIngredients.add(new Pantry("Tomato", "2", "50", "2024-12-31")); // Assuming "Tomato" already exists in the pantry
        String ingredientName = "Tomato";
        boolean result = pantryViewModel.checkDuplicateIngredient(ingredientName, existingIngredients);
        assertEquals("Ingredient already exists in the pantry.", true, result);
    }

    // Test for all empty inputs
    @Test
    public void testAllEmptyInputs() {
        // Adjusted the test based on available methods in PantryViewModel
        PantryViewModel pantryViewModel = new PantryViewModel();
        String ingredientName = "";
        List<Pantry> existingIngredients = new ArrayList<>(); // Empty list
        boolean result = pantryViewModel.checkDuplicateIngredient(ingredientName, existingIngredients);
        assertEquals("All empty inputs.", false, result);
    }

    @Test
    public void testNonNumericalInputs() {
        PantryViewModel pantryViewModel = new PantryViewModel();
        String result = pantryViewModel.checkValidIngredientEntry("name", "a", "5", "10");
        assertEquals("Non-numerical calories/quantity", "Failed: Calories and Quantity must be numbers.", result);
    }

    @Test
    public void testnonNumericalInputs2() {
        PantryViewModel pantryViewModel = new PantryViewModel();
        String result = pantryViewModel.checkValidIngredientEntry("name", "123b", "5", "10");
        assertEquals("Non-numerical calories/quantity", "Failed: Calories and Quantity must be numbers.", result);
    }
}

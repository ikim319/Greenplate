package com.example.greenplate;
import static junit.framework.TestCase.assertEquals;

import com.example.greenplate.viewModels.ShoppingListViewModel;
import com.example.greenplate.views.Ingredients;
import com.example.greenplate.model.ShoppingListModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListTest {
    // Test for negative or 0 quantities
    @Test
    public void TestIngredientsValidity1() {
        ShoppingListViewModel test1 = new ShoppingListViewModel();
        String quantity = "-1";
        String resultVal = test1.negativeQuantity(quantity);
        assertEquals("Quantity value for ingredient is invalid.", "Failed: Negative Value or 0.", resultVal);
    }

    // Test for properly asserted quantities (positive)
    @Test
    public void TestIngredientsValidity2() {
        ShoppingListViewModel test2 = new ShoppingListViewModel();
        String quantity2 = "2";
        String resultVal2 = test2.negativeQuantity(quantity2);
        assertEquals("Quantity value for ingredient is valid.", "2", resultVal2);

    }


    // Test for duplicate ingredient entry
    @Test
    public void testDuplicateIngredient() {
        ShoppingListViewModel ShoppingListViewModel1 = new ShoppingListViewModel();
        List<ShoppingListModel> existingIngredients = new ArrayList<>();
        existingIngredients.add(new ShoppingListModel("Tomato", "2")); // Assuming "Tomato" already exists in the pantry
        String ingredientName = "Tomato";
        boolean result = ShoppingListViewModel1.checkDuplicateIngredient(ingredientName, existingIngredients);
        assertEquals("Ingredient already exists in the pantry.", true, result);
    }

    // Test for all empty arguments
    @Test
    public void testAllEmptyInputs() {
        // Adjusted the test based on available methods in ShoppingListViewModel
        ShoppingListViewModel ShoppingListViewModel2 = new ShoppingListViewModel();
        String ingredientName = "";
        List<ShoppingListModel> existingIngredients = new ArrayList<>(); // Empty list
        boolean result = ShoppingListViewModel2.checkDuplicateIngredient(ingredientName, existingIngredients);
        assertEquals("All empty inputs values for new ingredient.", false, result);
    }

    @Test
    public void testNonNumericalInputs() {
        ShoppingListViewModel ShoppingListViewModel = new ShoppingListViewModel();
        String result = com.example.greenplate.viewModels.ShoppingListViewModel.checkValidIngredientEntry("name", "a");
        assertEquals("Non-numerical calories/quantity", "Failed: Calories and Quantity must be numbers.", result);
    }

    @Test
    public void testnonNumericalInputs2() {
        ShoppingListViewModel ShoppingListViewModel = new ShoppingListViewModel();
        String result = com.example.greenplate.viewModels.ShoppingListViewModel.checkValidIngredientEntry("name", "123b");
        assertEquals("Non-numerical calories/quantity", "Failed: Calories and Quantity must be numbers.", result);
    }
}

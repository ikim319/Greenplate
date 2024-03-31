package com.example.greenplate;

import static junit.framework.TestCase.assertEquals;

import com.example.greenplate.viewModels.PantryViewModel;

import org.junit.Test;

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
}

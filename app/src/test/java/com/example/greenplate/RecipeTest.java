package com.example.greenplate;

import com.example.greenplate.views.AlphabeticalSortingStrategy;
import com.example.greenplate.views.Recipe;
import com.example.greenplate.views.Cookbook;
import com.example.greenplate.views.Pantry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecipeTest {

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
    }

    @Test
    void testSearchRecipe_RecipeNotFound() {
        // Set the search query to a non-existent recipe name
        recipe.editTextSearch.setText("Non-existent Recipe");

        // Call the searchRecipe method
        recipe.searchRecipe();

        // Verify that a toast message is shown indicating the recipe is not found
        // (You should add assertions here based on the expected behavior)
    }

    @Test
    void testSearchRecipe_RecipeFound() {
        // Set the search query to match an existing recipe name
        recipe.editTextSearch.setText("Test Recipe");

        // Call the searchRecipe method
        recipe.searchRecipe();

        // Assert that the recipe is found and its details are displayed
        // (Add appropriate assertions based on expected behavior)
    }

    @Test
    void testSortRecipes_Alphabetical() {
        // Call the sortRecipes method with the alphabetical sorting strategy
        recipe.setSortingStrategy(new AlphabeticalSortingStrategy());
        recipe.sortRecipes();

        // Assert that recipes are sorted alphabetically
        // (Add appropriate assertions based on expected behavior)
    }

    @Test
    void testSaveCookBook_Successful() {
        // Set up input data for a recipe
        recipe.editTextRecipeName.setText("New Recipe");
        recipe.editTextIngredReq.setText("Ingredient1, Quantity1\nIngredient2, Quantity2");

        // Call the saveCookBook method
        recipe.saveCookBook();
    }

    @Test
    void testSaveCookBook_EmptyFields() {
        // Set up input data with empty fields
        recipe.editTextRecipeName.setText("");
        recipe.editTextIngredReq.setText("");

        // Call the saveCookBook method
        recipe.saveCookBook();
    }
}


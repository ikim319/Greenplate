package com.example.greenplate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

import com.example.greenplate.views.Cookbook;
import com.example.greenplate.views.RecipeSearcher;

public class RecipeSearcherTest {

    private List<Cookbook> recipes;

    @Before
    public void setUp() {
        // Initialize the list of recipes before each test
        recipes = new ArrayList<>();
        // Add sample recipes for testing
        recipes.add(new Cookbook("Pasta Carbonara", null));
        recipes.add(new Cookbook("Chicken Stir Fry", null));
        recipes.add(new Cookbook("Tomato Soup", null));
    }

    @After
    public void tearDown() {
        // Clean up resources after each test
        recipes = null;
    }

    @Test
    public void testSearchRecipe_Found() {
        // Test case where the recipe is found
        String searchQuery = "Chicken Stir Fry";
        assertTrue(RecipeSearcher.searchRecipe(recipes, searchQuery));
    }

    @Test
    public void testSearchRecipe_NotFound() {
        // Test case where the recipe is not found
        String searchQuery = "Spaghetti Bolognese";
        assertFalse(RecipeSearcher.searchRecipe(recipes, searchQuery));
    }

    @Test
    public void testSearchRecipe_EmptyQuery() {
        // Test case where the search query is empty
        String searchQuery = "";
        assertFalse(RecipeSearcher.searchRecipe(recipes, searchQuery));
    }

    // Add more test cases as needed
}

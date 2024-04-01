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
        recipes = new ArrayList<>();
        recipes.add(new Cookbook("Pasta Carbonara", null));
        recipes.add(new Cookbook("Chicken Stir Fry", null));
        recipes.add(new Cookbook("Tomato Soup", null));
    }

    @After
    public void tearDown() {
        recipes = null;
    }

    @Test
    public void testSearchRecipe_Found() {
        String searchQuery = "Chicken Stir Fry";
        assertTrue(RecipeSearcher.searchRecipe(recipes, searchQuery));
    }

    @Test
    public void testSearchRecipe_NotFound() {
        String searchQuery = "Spaghetti Bolognese";
        assertFalse(RecipeSearcher.searchRecipe(recipes, searchQuery));
    }

    @Test
    public void testSearchRecipe_EmptyQuery() {
        String searchQuery = "";
        assertFalse(RecipeSearcher.searchRecipe(recipes, searchQuery));
    }

}

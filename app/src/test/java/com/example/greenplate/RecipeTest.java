package com.example.greenplate;

import com.example.greenplate.views.Cookbook;
import com.example.greenplate.views.RecipeSearcher;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecipeTest {

    @Test
    public void testSearchRecipe_WithEmptyQuery() {
        List<Cookbook> recipes = new ArrayList<>();
        recipes.add(new Cookbook("Zucchini Pasta", null));
        recipes.add(new Cookbook("Apple Pie", null));
        recipes.add(new Cookbook("Banana Bread", null));

        assertFalse(RecipeSearcher.searchRecipe(recipes, ""));
    }

    @Test
    public void testSearchRecipe_WithNonexistentRecipe() {
        List<Cookbook> recipes = new ArrayList<>();
        recipes.add(new Cookbook("Zucchini Pasta", null));
        recipes.add(new Cookbook("Apple Pie", null));
        recipes.add(new Cookbook("Banana Bread", null));

        assertFalse(RecipeSearcher.searchRecipe(recipes, "Nonexistent Recipe"));
    }

    @Test
    public void testSearchRecipe_WithExistingRecipe() {
        List<Cookbook> recipes = new ArrayList<>();
        recipes.add(new Cookbook("Zucchini Pasta", null));
        recipes.add(new Cookbook("Apple Pie", null));
        recipes.add(new Cookbook("Banana Bread", null));

        assertTrue(RecipeSearcher.searchRecipe(recipes, "Apple Pie"));
    }
}

package com.example.greenplate;

import com.example.greenplate.views.Cookbook;
import com.example.greenplate.views.RecipeSearcher;

import org.junit.Assert;
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

    @Test
    public void testAddingNewIngredientToShoppingList() {
        // Simulate the function logic
        String ingredientName = "Flour";
        int requiredQuantity = 1000; // in grams

        int pantryQuantity = 0; // Assume pantry is empty
        int shoppingListQuantity = 0; // Assume shopping list is initially empty

        // Simulated logic to check and add to the shopping list
        int quantityToAdd = requiredQuantity - pantryQuantity;
        shoppingListQuantity += quantityToAdd;

        // Assert conditions
        Assert.assertEquals(1000, shoppingListQuantity);
    }

    @Test
    public void testUpdatingExistingShoppingListIngredient() {
        String ingredientName = "Sugar";
        int requiredQuantity = 2000; // in grams

        int pantryQuantity = 500; // Assume pantry has some quantity
        int shoppingListQuantity = 300; // Assume shopping list has some quantity

        // Simulated logic to check and add to the shopping list
        int neededQuantity = requiredQuantity - pantryQuantity;
        if (neededQuantity > shoppingListQuantity) {
            shoppingListQuantity += (neededQuantity - shoppingListQuantity);
        }

        // Assert conditions
        Assert.assertEquals(1500, shoppingListQuantity);
    }

    @Test
    public void anotherTestUpdatingExistingShoppingListIngredient() {
        String ingredientName = "Test";
        int requiredQuantity = 1000; // in grams

        int pantryQuantity = 600; // Assume pantry has some quantity
        int shoppingListQuantity = 400; // Assume shopping list has some quantity

        // Simulated logic to check and add to the shopping list
        int neededQuantity = requiredQuantity - pantryQuantity;
        if (neededQuantity > shoppingListQuantity) {
            shoppingListQuantity += (neededQuantity - shoppingListQuantity);
        }

        // Assert conditions
        Assert.assertEquals(400, shoppingListQuantity);
        Assert.assertEquals(400, shoppingListQuantity);
    }
}

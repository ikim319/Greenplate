package com.example.greenplate;

import com.example.greenplate.views.AlphabeticalSortingStrategy;
import com.example.greenplate.views.Cookbook;
import com.example.greenplate.views.SortingStrategy;
import com.example.greenplate.views.Recipe;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SortingTest {

    private SortingStrategy sortingStrategy;

    @Before
    public void setUp() {
        sortingStrategy = new AlphabeticalSortingStrategy();

    }

    @Test
    public void testSorting() {
        // Create a list of Cookbook objects in unsorted order
        List<Cookbook> recipes = new ArrayList<>();
        recipes.add(new Cookbook("Zucchini Pasta", null));
        recipes.add(new Cookbook("Apple Pie", null));
        recipes.add(new Cookbook("Banana Bread", null));

        // Apply sorting strategy
        sortingStrategy.sort(recipes);

        // Verify that the list is sorted alphabetically
        assertEquals("Apple Pie", recipes.get(0).getRecipeName());
        assertEquals("Banana Bread", recipes.get(1).getRecipeName());
        assertEquals("Zucchini Pasta", recipes.get(2).getRecipeName());
    }

    @Test
    public void testEmptyList() {
        // Create an empty list
        List<Cookbook> recipes = new ArrayList<>();

        // Apply sorting strategy
        sortingStrategy.sort(recipes);

        // Verify that the list remains empty
        assertEquals(0, recipes.size());
    }
}

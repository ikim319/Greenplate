package com.example.greenplate.views;

import java.util.List;

public class RecipeSearcher {

    public static boolean searchRecipe(List<Cookbook> recipes, String searchQuery) {
        if (searchQuery.isEmpty()) {
            return false;
        }

        for (Cookbook cookbook : recipes) {
            if (cookbook != null && cookbook.getRecipeName().equalsIgnoreCase(searchQuery)) {
                return true;
            }
        }

        return false;
    }
}


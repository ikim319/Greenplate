package com.example.greenplate.views;

import java.util.List;

public class Cookbook {
    String recipeName;
    List<IngredientRequirement> ingredReqs;

    public Cookbook(String recipeName, List<IngredientRequirement> ingredReqs) {
        this.recipeName = recipeName;
        this.ingredReqs = ingredReqs;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public List<IngredientRequirement> getIngredReqs() {
        return ingredReqs;
    }
}

class IngredientRequirement {
    String ingredientName;
    String quantity;

    public IngredientRequirement(String ingredientName, String quantity) {
        this.ingredientName = ingredientName;
        this.quantity = quantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getQuantity() {
        return quantity;
    }
}

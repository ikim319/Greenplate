package com.example.greenplate.views;

public class Cookbook {
    String RecipeName;
    String IngredReq;
    String QuantRecipe;

    public Cookbook(String recipeName, String ingredReq, String quantRecipe) {
        RecipeName = recipeName;
        IngredReq = ingredReq;
        QuantRecipe = quantRecipe;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public String getIngredReq() {
        return IngredReq;
    }

    public String getQuantRecipe() {
        return QuantRecipe;
    }
}

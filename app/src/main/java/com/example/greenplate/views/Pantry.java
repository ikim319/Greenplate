package com.example.greenplate.views;

public class Pantry {
    String IngredientName;
    String Quantity;
    String PoExpire;
    String IngredientCalories;

    public Pantry(String ingredientName, String quantity, String ingredientCalories, String poExpire) {
        IngredientName = ingredientName;
        Quantity = quantity;
        IngredientCalories = ingredientCalories;
        PoExpire = poExpire;
    }

    public String getIngredientName() {
        return IngredientName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getIngredientCalories() {
        return IngredientCalories;
    }

    public String getPoExpire() {
        return PoExpire;
    }
}

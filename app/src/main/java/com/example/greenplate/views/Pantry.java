package com.example.greenplate.views;

public class Pantry {
    String IngredientName;
    String Quantity;
    String PoExpire;

    public Pantry(String ingredientName, String quantity, String poExpire) {
        IngredientName = ingredientName;
        Quantity = quantity;
        PoExpire = poExpire;
    }

    public String getIngredientName() {
        return IngredientName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getPoExpire() {
        return PoExpire;
    }
}

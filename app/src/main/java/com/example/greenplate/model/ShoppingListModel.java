package com.example.greenplate.model;

public class  ShoppingListModel{
    String IngredientName;
    String Quantity;

    public ShoppingListModel(String ingredientName, String quantity) {
        IngredientName = ingredientName;
        Quantity = quantity;
    }

    public String getIngredientName() {
        return IngredientName;
    }

    public String getQuantity() {
        return Quantity;
    }
}

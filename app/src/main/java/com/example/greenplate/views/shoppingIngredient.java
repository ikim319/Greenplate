package com.example.greenplate.views;

class shoppingIngredient {
    String ingredientName;
    String quantity;
    public shoppingIngredient() {
        // Default constructor required for calls to DataSnapshot.getValue(IngredientRequirement.class)
    }
    public shoppingIngredient(String ingredientName, String quantity) {
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

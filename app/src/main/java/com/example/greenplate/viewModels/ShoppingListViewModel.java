package com.example.greenplate.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.greenplate.model.ShoppingListModel;

import java.util.List;

public class ShoppingListViewModel extends ViewModel {
    public ShoppingListViewModel() {
    }

    // Check for negative or 0 quantity by parsing the string
    // to a integer and comparing.
    public String negativeQuantity(String quantity) {
        int quanNeg = Integer.parseInt(quantity);
        if (quanNeg <= 0) {
            return "Failed: Negative Value or 0.";
        } else {
            return Integer.toString(quanNeg);
        }
    }

    // Check for duplicate ingredient entry
    public boolean checkDuplicateIngredient(String ingredientName, List<ShoppingListModel> existingIngredients) {
        // Check if the ingredient name already exists in the list of existing ingredients
        for (ShoppingListModel item : existingIngredients) {
            if (item.getIngredientName().equalsIgnoreCase(ingredientName)) {
                return true;
            }
        }
        return false;
    }

    // Validate ingredient entry
    public static String checkValidIngredientEntry(String ingredientName, String quantity) {
        // Implement logic to check if all fields are valid
        if (ingredientName.isEmpty() || quantity.isEmpty()) {
            return "Failed: All fields must be nonempty.";
        } else if (!isNumeric(quantity)){
            return "Failed: Calories and Quantity must be numbers.";
        }
        else if (Integer.parseInt(quantity) <= 0) {
            return "Failed: Negative Value or 0.";
        }
        else {
            return "Success: Ingredient entry is valid.";
        }
    }

    public static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}
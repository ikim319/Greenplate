package com.example.greenplate.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.greenplate.views.Pantry;

import java.util.List;

public class PantryViewModel extends ViewModel {
    public PantryViewModel() {
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
    public boolean checkDuplicateIngredient(String ingredientName, List<Pantry> existingIngredients) {
        // Check if the ingredient name already exists in the list of existing ingredients
        for (Pantry pantry : existingIngredients) {
            if (pantry.getIngredientName().equalsIgnoreCase(ingredientName)) {
                return true;
            }
        }
        return false;
    }

    // Validate ingredient entry
    public String checkValidIngredientEntry(String ingredientName, String quantity, String calories, String expiry) {
        // Implement logic to check if all fields are valid
        if (ingredientName.isEmpty() || quantity.isEmpty() || calories.isEmpty() || expiry.isEmpty()) {
            return "Failed: All fields cannot be empty.";
        } else {
            // Additional validation logic can be added here if needed
            return "Success: Ingredient entry is valid.";
        }
    }
}
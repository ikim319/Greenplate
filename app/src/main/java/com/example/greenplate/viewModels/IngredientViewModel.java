package com.example.greenplate.viewModels;

import androidx.lifecycle.ViewModel;
public class IngredientViewModel extends ViewModel {


    public IngredientViewModel() {
    }

    public Boolean validInput(String name, String quantity, String calories) {
        if (name.isEmpty() || quantity.isEmpty() || calories.isEmpty()) {
            return false;
        }
        int quantityValue;
        int calorieValue;

        try {
            quantityValue = Integer.parseInt(quantity);
            calorieValue = Integer.parseInt(calories);
        } catch (NumberFormatException e) {
            return false;
        }

        if (quantityValue <= 0 || calorieValue < 0) {
            return false;
        }
        return true;
    }


}
package com.example.greenplate.viewModels;

import androidx.lifecycle.ViewModel;
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
}
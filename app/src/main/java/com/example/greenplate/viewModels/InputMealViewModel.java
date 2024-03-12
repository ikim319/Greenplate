package com.example.greenplate.viewModels;

import androidx.lifecycle.ViewModel;
public class InputMealViewModel extends ViewModel {


    public InputMealViewModel() {
    }

    public String calorieCounter(String height, String weight, String gender) {
        if (height == null || weight == null || gender == null) {
            return "N/A";
        }

        int heightInt;
        int weightInt;
        try {
            heightInt = Integer.parseInt(height);
            weightInt = Integer.parseInt(weight);
        } catch (NumberFormatException e) {
            return "N/A";
        }

        int calorieGoal;

        if (gender.equals("Male")) {
            calorieGoal = (int) (((6.23 * weightInt) + (12.7 * heightInt) + 66) * 1.55);
        } else {
            calorieGoal = (int) (((4.35 * weightInt) + (4.7 * heightInt) + 65) * 1.55);
        }

        return Integer.toString(calorieGoal);
    }

}
package com.example.greenplate.views;

public class Meal {
    String Meal;

    String Calories;

    public Meal(String meal, String calories) {
        Meal = meal;
        Calories = calories;
    }

    public String getMeal() {
        return Meal;
    }

    public String getCalories() {
        return Calories;
    }
}

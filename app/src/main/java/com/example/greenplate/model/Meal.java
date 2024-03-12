package com.example.greenplate.model;
import android.os.Build;

public class Meal {
    String Meal;
    String Date;
    String Calories;
    public Meal() {
    }
    public Meal(String meal, String calories, String Date) {
        Meal = meal;
        Calories = calories;
        this.Date = Date;
    }

    public String getMeal() {
        return Meal;
    }

    public String getCalories() {
        return Calories;
    }

    public String getDate() {
        return Date;
    }
}

package com.example.greenplate.views;
public class Users {
    String Height;
    String Weight;
    String Gender;

    public Users(String height, String weight, String gender) {
        Height = height;
        Weight = weight;
        Gender = gender;
    }

    public String getHeight() {
        return Height;
    }

    public String getWeight() {
        return Weight;
    }

    public String getGender() {
        return Gender;
    }
}

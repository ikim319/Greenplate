package com.example.greenplate.views;
public class Users {
    private String height;
    private String weight;
    private String gender;

    public Users(String height, String weight, String gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getGender() {
        return gender;
    }
}

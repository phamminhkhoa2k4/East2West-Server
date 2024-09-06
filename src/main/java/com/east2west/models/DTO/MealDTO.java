package com.east2west.models.DTO;

public class MealDTO {
    private int mealid;
    private String mealname;
    private String mealthumbnail;
    private String mealduration;
    private String mealactivity;

    public int getMealid() {
        return this.mealid;
    }

    public void setMealid(int mealid) {
        this.mealid = mealid;
    }

    public String getMealname() {
        return this.mealname;
    }

    public void setMealname(String mealname) {
        this.mealname = mealname;
    }

    public String getMealthumbnail() {
        return this.mealthumbnail;
    }

    public void setMealthumbnail(String mealthumbnail) {
        this.mealthumbnail = mealthumbnail;
    }

    public String getMealduration() {
        return this.mealduration;
    }

    public void setMealduration(String mealduration) {
        this.mealduration = mealduration;
    }

    public String getMealactivity() {
        return this.mealactivity;
    }

    public void setMealactivity(String mealactivity) {
        this.mealactivity = mealactivity;
    }
    
}


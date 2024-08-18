package com.east2west.models.DTO;

public class MealDTO {
    private int mealId;
    private String mealName;
    private String mealThumbnail;
    private String mealDuration;
    private String mealActivity;

    // Getters and Setters
    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealThumbnail() {
        return mealThumbnail;
    }

    public void setMealThumbnail(String mealThumbnail) {
        this.mealThumbnail = mealThumbnail;
    }

    public String getMealDuration() {
        return mealDuration;
    }

    public void setMealDuration(String mealDuration) {
        this.mealDuration = mealDuration;
    }

    public String getMealActivity() {
        return mealActivity;
    }

    public void setMealActivity(String mealActivity) {
        this.mealActivity = mealActivity;
    }
}


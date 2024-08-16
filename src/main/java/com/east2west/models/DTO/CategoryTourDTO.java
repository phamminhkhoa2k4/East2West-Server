package com.east2west.models.DTO;

import com.east2west.models.Entity.CategoryTour;

public class CategoryTourDTO {
    private int categoryTourId;
    private String categoryTourName;

    // Getters and setters
    public int getCategoryTourId() {
        return categoryTourId;
    }

    public void setCategoryTourId(int categoryTourId) {
        this.categoryTourId = categoryTourId;
    }

    public String getCategoryTourName() {
        return categoryTourName;
    }

    public void setCategoryTourName(String categoryTourName) {
        this.categoryTourName = categoryTourName;
    }
}
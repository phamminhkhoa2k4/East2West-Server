package com.east2west.models.DTO;

import java.util.List;

public class TourPackageFilterDTO {
    private List<Long> categoryTourId;
    private List<Long> themeTourId;
    private List<Long> suitableTourId;
    private String budget;

    // Getters and Setters
    public List<Long> getCategoryTourId() {
        return categoryTourId;
    }

    public void setCategoryTourId(List<Long> categoryTourId) {
        this.categoryTourId = categoryTourId;
    }

    public List<Long> getThemeTourId() {
        return themeTourId;
    }

    public void setThemeTourId(List<Long> themeTourId) {
        this.themeTourId = themeTourId;
    }

    public List<Long> getSuitableTourId() {
        return suitableTourId;
    }

    public void setSuitableTourId(List<Long> suitableTourId) {
        this.suitableTourId = suitableTourId;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}

package com.east2west.models.DTO;


public class TourCategoryTourDTO {
    private int tourCategoryTourId;
    private int packageid;
    private CategoryTourDTO categoryTour;

    // Getters and setters
    public int getTourCategoryTourId() {
        return tourCategoryTourId;
    }

    public void setTourCategoryTourId(int tourCategoryTourId) {
        this.tourCategoryTourId = tourCategoryTourId;
    }

    public int getPackageid() {
        return packageid;
    }

    public void setPackageid(int packageid) {
        this.packageid = packageid;
    }

    public CategoryTourDTO getCategoryTour() {
        return categoryTour;
    }

    public void setCategoryTour(CategoryTourDTO categoryTour) {
        this.categoryTour = categoryTour;
    }
}